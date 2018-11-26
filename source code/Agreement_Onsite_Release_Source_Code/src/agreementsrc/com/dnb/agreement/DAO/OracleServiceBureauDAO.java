
/********************************************************************************************
 * Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                        *
 * IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.       *
 * The Program or any portion thereof may not be reproduced in any form whatsoever except    *
 * as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd. *
 *																							*
 * File Name                   : OracleServiceBureauDAO.java                                 *
 * Author                      : Manjunath N G                                               *
 * Creation Date               : 28-July-2008                                                *
 * Description                 : This file serves as the interface, which contains all       *
 *                               the methods which are used for Unit.                        *                                             *
 * Modification History        :                                                             *
 *																						    *
 *     Version No.               Date               Brief description of change              *
 *  ---------------------------------------------------------------------------------------  *
 *                       |                  |											    *
 *                       |                  |											    *
 *  ---------------------------------------------------------------------------------------  *
 *********************************************************************************************/

/** 
 * Create or import Packages 
 */

package com.dnb.agreement.DAO;

import java.util.List;
import java.util.ArrayList;
import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleTypes;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Timestamp;
import oracle.sql.ARRAY;
import oracle.sql.STRUCT;
import oracle.sql.ArrayDescriptor;
import oracle.sql.StructDescriptor;
import org.apache.log4j.Logger;
import com.dnb.common.commons.CommonConstants;
import com.dnb.common.database.DescriptorUtility;
import com.dnb.agreement.DTO.ServiceBureauDTO;
import com.dnb.agreement.DTO.ServiceBureauSearchDTO;
import com.dnb.agreement.bean.KekAukServiceBureauBean;
import com.dnb.agreement.bean.ServiceBureauBean;
import com.dnb.agreement.exception.IErrorCodes;
import com.dnb.agreement.exception.AgreementSystemException;
import com.dnb.agreement.exception.AgreementBusinessException;
import com.dnb.agreement.utility.SQLUtils;	
import com.dnb.agreement.utility.StringUtils;

/**
 * Interface that all BureauDAO must support
 */

public class OracleServiceBureauDAO implements ServiceBureauDAO  {

	static Logger logger = Logger.getLogger(OracleServiceBureauDAO.class);

	/** 
	 * Method is used to insert a new Service Bureau record to OPE Database
	 * Get ServiceBureauDTO Object as parameter
	 */

	public boolean insert(ServiceBureauDTO e,String userId, Date businessDate,String action) throws AgreementSystemException,
	AgreementBusinessException{

		OracleConnection oraConRef = null;
		Connection con = null;
		OracleCallableStatement ocs=null;
		StructDescriptor bureauStrDesc;
		ArrayDescriptor bureauArrDesc,bureauEntityArrDesc;

		int res;
		boolean ret = false;				

		try {
			con = SQLUtils.getConnection(CommonConstants.DNBDatabase);
			oraConRef = DescriptorUtility.getOracleConnection(con);			
		} catch (SQLException sq) {
			logger.error("Could not obtain OracleConnection");
			throw new AgreementSystemException(IErrorCodes.CONNECTION_RETRIEVAL_ERROR,
					"Unable to obtain OracleConnection", sq);
		}

		try {
			// Create Descriptors for OracleTypes			
			bureauStrDesc = StructDescriptor.createDescriptor("BUREAU_TYPE",oraConRef);
			bureauArrDesc = ArrayDescriptor.createDescriptor("BUREAU_TABLE",oraConRef);						
			bureauEntityArrDesc = ArrayDescriptor.createDescriptor("ENTITY_ID_TYPE",oraConRef);

		}catch (SQLException t) {
			logger.error("Exception in insertBureau [Could not create Struct/Array] : "	+ t);
			throw new AgreementSystemException(IErrorCodes.DB_DESCRIPTOR_ERROR,
					"Could not create db Descriptors", t);
		}

		try {			

			// Make the object from the bean class
			Object[] deptRecobj = {new Integer(1),
					e.getInternalBureauId()
					,e.getBureauId()
					,e.getBureauName()
					,e.getBureauDescription()
					,e.getCompanyName()
					,e.getPatuId()
					,e.getContactPerson1()
					,e.getContactPerson2()
					,e.getAddressLine1()
					,e.getPinCode1()
					,e.getCity1()
					,e.getCountry1()
					,e.getTelephoneNo1()
					,e.getAddressLine2()
					,e.getPinCode2()
					,e.getCity2()
					,e.getCountry2()
					,e.getTelephoneNo2()	
					,"A"
					,userId
					,businessDate
					,userId
					,businessDate
					,userId
					,businessDate
			};

			// Set up the struct object						
			STRUCT p1struct = new STRUCT(bureauStrDesc, oraConRef, deptRecobj);
			Object[]deptArrobj = new Object[] { p1struct };

			Object[] deptDelListObj=new Object[1];
			deptDelListObj[0]="";

			// Set up the array object
			ARRAY deptArr = new ARRAY(bureauArrDesc, oraConRef, deptArrobj);
			ARRAY deptDelArr = new ARRAY(bureauEntityArrDesc,oraConRef,deptDelListObj);

			// Prepare the OracleCallableStatement
			ocs = (OracleCallableStatement) oraConRef
			.prepareCall("{? = call OPEPK_BUREAU_DEFN.FN_BUREAU(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");

			/** Bind inputs and register outputs*/

			ocs.registerOutParameter(1, OracleTypes.NUMERIC);

			ocs.setString(2, userId);
			ocs.setString(3, action);
			ocs.setString(4, "");

			ocs.setDate(5, businessDate);			

			ocs.setString(6,"");
			ocs.setString(7,"");
			ocs.setString(8,"");
			ocs.setString(9,"");

			ocs.setARRAY(10, deptDelArr);
			ocs.setARRAY(11,deptArr);

			ocs.setInt(12,1);
			ocs.registerOutParameter(13,OracleTypes.VARCHAR);				
			ocs.registerOutParameter(14, OracleTypes.ARRAY, "BUREAU_TABLE");

			ocs.registerOutParameter(15, OracleTypes.VARCHAR);
			ocs.registerOutParameter(16, OracleTypes.VARCHAR);


			/** Execute the procedure */
			ocs.execute();
			res = ocs.getInt(1);

			logger.debug("RETURN : " + res);
			logger.debug("ERROR CODE : " + ocs.getString(15));
			logger.debug("ERROR DESC : " + ocs.getString(16));

			if(res==0) {
				oraConRef.rollback();
				if(ocs.getString(15)!=null){
					throw new AgreementBusinessException(ocs.getString(15),"Procedure Failed to Insert");
				}else{
					throw new AgreementBusinessException(IErrorCodes.SERVICE_BUREAU_INSERT_ERROR,
					"Procedure Failed to Insert");
				}				
			}			
			// Return the result to the BureauAction
			if (res == 1) {
				oraConRef.commit();
				ret = true;
			} else {
				oraConRef.rollback();
				ret = false;
			}			
			return ret;
		}catch (SQLException t) {
			logger.error("Exception in insertBureau : " + t);			
			throw new AgreementSystemException(IErrorCodes.SERVICE_BUREAU_INSERT_ERROR,
					"Failed to Insert Record for New Bureau", t);
		}catch(RuntimeException re) {			
			throw new AgreementBusinessException(IErrorCodes.TYPECONVERSION_ERROR,
			"Exception while creating Object Array");
		}finally{
			SQLUtils.ensureClosedConnection(con,ocs,oraConRef);
		}
	}

	/** 
	 * Method is used to search records for Service Bureau from OPE Database
	 * It searches on Service Bureau id or Service Bureau name or PATU ID
	 * Get ServiceBureauSearchDTO Object as parameter
	 */

	public List search(ServiceBureauSearchDTO e,String action) throws AgreementSystemException,
	AgreementBusinessException {

		OracleConnection oraConRef = null;
		Connection con = null;
		OracleCallableStatement ocs=null;
		StructDescriptor deptStrDesc;
		ArrayDescriptor deptArrDesc,deptEntityArrDesc;
		List ret=null;
		int res;

		/*Get Connection from Database */

		try {
			con = SQLUtils.getConnection(CommonConstants.DNBDatabase);
			oraConRef = DescriptorUtility.getOracleConnection(con);			
		} catch (SQLException sq) {
			logger.error("Could not obtain OracleConnection");
			throw new AgreementSystemException(IErrorCodes.CONNECTION_RETRIEVAL_ERROR,
					"Unable to obtain OracleConnection", sq);
		}

		try {
			// Create Descriptors for OracleTypes			
			deptStrDesc = StructDescriptor.createDescriptor("BUREAU_TYPE",oraConRef);
			deptArrDesc = ArrayDescriptor.createDescriptor("BUREAU_TABLE",oraConRef);						
			deptEntityArrDesc = ArrayDescriptor.createDescriptor("ENTITY_ID_TYPE",oraConRef);

		}catch (SQLException t) {
			logger.error("Exception in insertBureau [Could not create Struct/Array] : "	+ t);
			throw new AgreementSystemException(IErrorCodes.DB_DESCRIPTOR_ERROR,
					"Could not create db Descriptors", t);
		}

		try {
			Object[] deptRecObj =   {null,null,null,null,null,null
					,null,null,null,null,null,null
					,null,null,null,null,null,null
					,null,null,null,null,null,null,null,null
			};

			STRUCT p1struct = new STRUCT(deptStrDesc, oraConRef, deptRecObj);

			Object[] deptArrobj = new Object[] { p1struct };
			Object[] deptDelListObj=new Object[1];

			deptDelListObj[0]="";			

			//	Set up the array object
			ARRAY deptArr = new ARRAY(deptArrDesc, oraConRef, deptArrobj);
			ARRAY deptDelArr = new ARRAY(deptEntityArrDesc,oraConRef,deptDelListObj);

			//	Prepare the OracleCallableStatement

			ocs = (OracleCallableStatement) oraConRef			
			.prepareCall("{? = call OPEPK_BUREAU_DEFN.FN_BUREAU(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");

			/** Bind inputs and register outputs */

			ocs.registerOutParameter(1, OracleTypes.NUMERIC);

			ocs.setString(2, "");
			ocs.setString(3, action);
			ocs.setString(4, "");
			ocs.setDate(5, null);

			ocs.setString(6,e.getBureauId());
			ocs.setString(7,e.getBureauName());
			ocs.setString(8,e.getCompanyName());
			ocs.setString(9,e.getPatuId());

			ocs.setARRAY(10, deptDelArr);
			ocs.setARRAY(11,deptArr);

			ocs.setInt(12,1);
			ocs.registerOutParameter(13,OracleTypes.VARCHAR);
			ocs.registerOutParameter(14, OracleTypes.ARRAY, "BUREAU_TABLE");

			ocs.registerOutParameter(15, OracleTypes.VARCHAR);
			ocs.registerOutParameter(16, OracleTypes.VARCHAR);			

			/** Execute the procedure */
			ocs.execute();

			//	Log the result
			res = ocs.getInt(1);

			logger.debug("RETURN : " + res );
			logger.debug("ERROR CODE : " + ocs.getString(15));
			logger.debug("ERROR DESC : " + ocs.getString(16));			

			if(res==0) {
				oraConRef.rollback();
				if(ocs.getString(15)!=null){
					throw new AgreementBusinessException(ocs.getString(15),"Procedure Failed to Fetch");
				}
				else{
					throw new AgreementSystemException(IErrorCodes.SERVICE_BUREAU_FETCH_ERROR,
					"Procedure Failed to Fetch");
				}
			}

			//	set up the array object to accept data
			ARRAY attributes = ocs.getARRAY(14);  
			Object[] object = (Object[])attributes.getArray();

			List lst = makeList(object,"");

			//	Return the result to the ServiceBureauAction
			if(res == 1) {
				oraConRef.commit();
				ret = lst;
			} else {
				oraConRef.rollback();
				ret = null;
			}						
			return ret;
		} catch (SQLException t) {
			logger.debug("Exception in searchBureau : " + t);
			throw new AgreementSystemException(IErrorCodes.SERVICE_BUREAU_FETCH_ERROR,
					"Failed to Fetch records for service bureau", t);
		} catch(RuntimeException re) {
			throw new AgreementBusinessException(
					IErrorCodes.TYPECONVERSION_ERROR,
			"Exception while creating Object Array");
		} finally{
			SQLUtils.ensureClosedConnection(con,ocs,oraConRef);
		}
	}

	/** 
	 * Function to delete service bureau
	 * Get ServiceBureauSearchDTO Object as parameter
	 */

	public boolean delete(ServiceBureauSearchDTO e,String userId, Date businessDate,String action,String deleteMode) throws AgreementSystemException,
	AgreementBusinessException {

		OracleConnection oraConRef = null;
		Connection con = null;
		OracleCallableStatement ocs=null;
		StructDescriptor stdesc;
		ArrayDescriptor arrdesc,arrdescEnt;
		int res;
		boolean ret = false;

		try {
			con = SQLUtils.getConnection(CommonConstants.DNBDatabase);
			oraConRef = DescriptorUtility.getOracleConnection(con);			
		} catch (SQLException sq) {
			logger.error("Could not obtain OracleConnection");
			throw new AgreementSystemException(IErrorCodes.CONNECTION_RETRIEVAL_ERROR,
					"Unable to obtain OracleConnection", sq);
		}

		try	{
			// Create Descriptors for OracleTypes
			stdesc= StructDescriptor.createDescriptor("BUREAU_TYPE", oraConRef);	 
			arrdesc = ArrayDescriptor.createDescriptor("BUREAU_TABLE", oraConRef);
			arrdescEnt = ArrayDescriptor.createDescriptor("ENTITY_ID_TYPE", oraConRef);

		} catch (SQLException t) {
			logger.error("Exception in deleteBureau [Could not create Struct/Array] : "	+ t);
			throw new AgreementSystemException(IErrorCodes.DB_DESCRIPTOR_ERROR,
					"Could not create db Descriptors", t);
		}

		try {
			// Create and load the object with values from Bean Class
			Object[] deptRecObj =   {null,null,null,null,null,null
					,null,null,null,null,null,null
					,null,null,null,null,null,null
					,null,null,null,null,null,null,null,null
			};

			STRUCT p1struct = new STRUCT(stdesc, oraConRef, deptRecObj);

			Object[] deptArrobj = new Object[] { p1struct };
			Object[] deptDelListObj = StringUtils.createObject(e.getDeleteMe());

			// Set up the ARRAY object.	
			ARRAY deptArr = new ARRAY(arrdesc, oraConRef, deptArrobj);
			ARRAY  deptDelArr = new ARRAY(arrdescEnt,oraConRef,deptDelListObj);

			ocs = (OracleCallableStatement) oraConRef
			.prepareCall("{? = call OPEPK_BUREAU_DEFN.FN_BUREAU(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");

			/** Bind inputs and register outputs*/

			ocs.registerOutParameter(1, OracleTypes.NUMERIC);

			ocs.setString(2, userId);
			ocs.setString(3, action);
			ocs.setString(4, "");
			ocs.setDate(5, businessDate);			

			ocs.setString(6,"");
			ocs.setString(7,"");
			ocs.setString(8,"");
			ocs.setString(9,"");				

			ocs.setARRAY(10, deptDelArr);
			ocs.setARRAY(11,deptArr);

			ocs.setInt(12,1);
			ocs.registerOutParameter(13,OracleTypes.VARCHAR);
			ocs.registerOutParameter(14, OracleTypes.ARRAY, "BUREAU_TABLE");

			ocs.registerOutParameter(15, OracleTypes.VARCHAR);
			ocs.registerOutParameter(16, OracleTypes.VARCHAR);			 	

			/** Execute the procedure */
			ocs.execute();

			// Log the Result
			res = ocs.getInt(1);
			logger.debug("RETURN : " + res );
			logger.debug("ERROR CODE : " + ocs.getString(15));
			logger.debug("ERROR DESC : " + ocs.getString(16));			

			//Return the result to BureauAction
			if(res==0) {
				oraConRef.rollback();
				ret = false;
				if(ocs.getString(15)!=null){
					throw new AgreementBusinessException(ocs.getString(15),
					"Procedure Failed to delete bureau");
				}else{
					throw new AgreementSystemException(IErrorCodes.SERVICE_BUREAU_DELETE_ERROR,
					"Procedure Failed to delete bureau");
				}
			}

			// Return the result to ServiceBureauAction
			if(res == 1) {
				oraConRef.commit();
				ret = true;
			} else {
				oraConRef.rollback();	
				ret = false;
			}	
			return ret;
		}catch (SQLException t) {
			logger.error("Exception in deleteBureau : " + t);
			throw new AgreementSystemException(IErrorCodes.SERVICE_BUREAU_DELETE_ERROR,
					"Failed to delete bureau", t);
		}catch(RuntimeException re) {
			throw new AgreementBusinessException(
					IErrorCodes.TYPECONVERSION_ERROR,
			"Exception while creating Object Array");
		}finally{
			SQLUtils.ensureClosedConnection(con,ocs,oraConRef);
		}
	}

	/** 
	 * Method is used to edit a Service Bureau record to OPE Database
	 * Get ServiceBureauDTO Object as parameter
	 */

	public boolean edit(ServiceBureauDTO e,String userId, Date businessDate,String action) throws AgreementSystemException,
	AgreementBusinessException{

		OracleConnection oraConRef = null;
		Connection con = null;
		OracleCallableStatement ocs=null;
		StructDescriptor bureauStrDesc;
		ArrayDescriptor bureauArrDesc,bureauEntityArrDesc;

		int res;
		boolean ret = false;				

		try {
			con = SQLUtils.getConnection(CommonConstants.DNBDatabase);
			oraConRef = DescriptorUtility.getOracleConnection(con);			
		} catch (SQLException sq) {
			logger.error("Could not obtain OracleConnection");
			throw new AgreementSystemException(IErrorCodes.CONNECTION_RETRIEVAL_ERROR,
					"Unable to obtain OracleConnection", sq);
		}

		try {
			// Create Descriptors for OracleTypes			
			bureauStrDesc = StructDescriptor.createDescriptor("BUREAU_TYPE",oraConRef);
			bureauArrDesc = ArrayDescriptor.createDescriptor("BUREAU_TABLE",oraConRef);						
			bureauEntityArrDesc = ArrayDescriptor.createDescriptor("ENTITY_ID_TYPE",oraConRef);

		}catch (SQLException t) {
			logger.error("Exception in editBureau [Could not create Struct/Array] : "	+ t);
			throw new AgreementSystemException(IErrorCodes.DB_DESCRIPTOR_ERROR,
					"Could not create db Descriptors", t);
		}

		try {			
			// Make the object from the bean class
			Object[] deptRecobj = {e.getVersionNo(),
					e.getInternalBureauId()
					,e.getBureauId()
					,e.getBureauName()
					,e.getBureauDescription()
					,e.getCompanyName()       // Patu user id
					,e.getPatuId()
					,e.getContactPerson1()
					,e.getContactPerson2()
					,e.getAddressLine1()
					,e.getPinCode1()
					,e.getCity1()
					,e.getCountry1()
					,e.getTelephoneNo1()
					,e.getAddressLine2()
					,e.getPinCode2()
					,e.getCity2()
					,e.getCountry2()
					,e.getTelephoneNo2()			
					,"A"
					,e.getCreatedBy()
					,null
					,userId
					,businessDate
					,userId
					,businessDate
			};

			// Set up the struct object			
			STRUCT p1struct = new STRUCT(bureauStrDesc, oraConRef, deptRecobj);
			Object[] deptArrobj = new Object[] { p1struct };

			Object[] deptDelListObj=new Object[1];
			deptDelListObj[0]="";

			// Set up the array object
			ARRAY deptArr = new ARRAY(bureauArrDesc, oraConRef, deptArrobj);
			ARRAY deptDelArr = new ARRAY(bureauEntityArrDesc,oraConRef,deptDelListObj);

			// Prepare the OracleCallableStatement
			ocs = (OracleCallableStatement) oraConRef
			.prepareCall("{? = call OPEPK_BUREAU_DEFN.FN_BUREAU(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");

			/** Bind inputs and register outputs*/

			ocs.registerOutParameter(1, OracleTypes.NUMERIC);

			ocs.setString(2, userId);
			ocs.setString(3, action);
			ocs.setString(4, "");
			ocs.setDate(5, businessDate);			

			ocs.setString(6,"");
			ocs.setString(7,"");
			ocs.setString(8,"");
			ocs.setString(9,"");

			ocs.setARRAY(10, deptDelArr);
			ocs.setARRAY(11,deptArr);				

			ocs.setInt(12,1);
			ocs.registerOutParameter(13,OracleTypes.VARCHAR);
			ocs.registerOutParameter(14, OracleTypes.ARRAY, "BUREAU_TABLE");

			ocs.registerOutParameter(15, OracleTypes.VARCHAR);
			ocs.registerOutParameter(16, OracleTypes.VARCHAR);				

			/** Execute the procedure */
			ocs.execute();
			res = ocs.getInt(1);

			logger.debug("RETURN : " + res);
			logger.debug("ERROR CODE : " + ocs.getString(15));
			logger.debug("ERROR DESC : " + ocs.getString(16));

			if(res==0) {
				oraConRef.rollback();
				if(ocs.getString(15)!=null){
					throw new AgreementBusinessException(ocs.getString(15),"Procedure Failed to Edit");
				}else{
					throw new AgreementBusinessException(IErrorCodes.SERVICE_BUREAU_EDIT_ERROR,
					"Procedure Failed to Edit");
				}				
			}

			// Return the result to the ServiceBureauAction
			if (res == 1) {
				oraConRef.commit();
				ret = true;
			} else {
				oraConRef.rollback();
				ret = false;
			}			
			return ret;
		}catch (SQLException t) {
			logger.error("Exception in editBureau : " + t);			
			throw new AgreementSystemException(IErrorCodes.SERVICE_BUREAU_EDIT_ERROR,
					"Failed to Edit Record for Bureau", t);
		}catch(RuntimeException re) {			
			throw new AgreementBusinessException(IErrorCodes.TYPECONVERSION_ERROR,
			"Exception while creating Object Array");
		}finally{
			SQLUtils.ensureClosedConnection(con,ocs,oraConRef);
		}
	}

	/** 
	 * Method is used to search previous versions of bureau 
	 * Get ServiceBureauDTO Object as parameter
	 */

	public List showVersion(ServiceBureauDTO e,String action) throws AgreementSystemException,
	AgreementBusinessException {

		OracleConnection oraConRef = null;
		Connection con = null;
		OracleCallableStatement ocs=null;
		StructDescriptor stdesc;
		ArrayDescriptor arrdesc,arrdescEnt;

		List ret=null;
		int res;

		/*Get Connection from Database */

		try {
			con = SQLUtils.getConnection(CommonConstants.DNBDatabase);
			oraConRef = DescriptorUtility.getOracleConnection(con);			
		} catch (SQLException sq) {
			logger.error("Could not obtain OracleConnection");
			throw new AgreementSystemException(IErrorCodes.CONNECTION_RETRIEVAL_ERROR,
					"Unable to obtain OracleConnection", sq);
		}		

		try	{
			// Create Descriptors for OracleTypes
			stdesc= StructDescriptor.createDescriptor("BUREAU_TYPE", oraConRef);	 
			arrdesc = ArrayDescriptor.createDescriptor("BUREAU_TABLE", oraConRef);
			arrdescEnt = ArrayDescriptor.createDescriptor("ENTITY_ID_TYPE",oraConRef);

		} catch (SQLException t) {
			logger.error("Exception in showDepartmentVersion [Could not create Struct/Array] : " + t);
			throw new AgreementSystemException(IErrorCodes.DB_DESCRIPTOR_ERROR,
					"Could not create db Descriptors", t);
		}

		try {
			Object[] deptRecObj = 	{null,null,null,null,null,null
					,null,null,null,null,null,null
					,null,null,null,null,null,null
					,null,null,null,null,null,null,null,null
			};

			STRUCT p1struct = new STRUCT(stdesc, oraConRef, deptRecObj);

			Object[] deptArrobj = new Object[] { p1struct };
			Object[] deptDelListObj=new Object[1];

			deptDelListObj[0]="";			

			// Set up the array object
			ARRAY deptarr = new ARRAY(arrdesc, oraConRef, deptArrobj);
			ARRAY deptDelListArr = new ARRAY(arrdescEnt,oraConRef,deptDelListObj);

			// Prepare the OracleCallableStatement.
			ocs = (OracleCallableStatement) oraConRef
			.prepareCall("{? = call OPEPK_BUREAU_DEFN.FN_BUREAU(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");

			/** Bind inputs and register outputs*/
			ocs.registerOutParameter(1, OracleTypes.NUMERIC);

			ocs.setString(2, "");
			ocs.setString(3, action);
			ocs.setString(4, "");
			ocs.setDate(5, null);

			ocs.setString(6,e.getInternalBureauId());
			ocs.setString(7,"");
			ocs.setString(8,"");
			ocs.setString(9,"");

			ocs.setARRAY(10, deptDelListArr);
			ocs.setARRAY(11,deptarr);

			ocs.setInt(12,Integer.parseInt(e.getVersionNo()));
			ocs.registerOutParameter(13,OracleTypes.VARCHAR);
			ocs.registerOutParameter(14, OracleTypes.ARRAY, "BUREAU_TABLE");

			ocs.registerOutParameter(15, OracleTypes.VARCHAR);
			ocs.registerOutParameter(16, OracleTypes.VARCHAR);

			/** Execute the procedure */
			ocs.execute();

			// Log the Result
			res = ocs.getInt(1);
			logger.debug("RETURN : " + res );
			logger.debug("ERROR CODE : " + ocs.getString(15));
			logger.debug("ERROR DESC : " + ocs.getString(16));

			// Set up the ARRAY object to hold records.
			ARRAY attributes = ocs.getARRAY(14);  
			Object[] obj = (Object[])attributes.getArray();
			List lst = makeList(obj,ocs.getString(13));

			if(res==0) {
				oraConRef.rollback();
				if(ocs.getString(15)!=null){
					throw new AgreementBusinessException(ocs.getString(15),
					"Procedure Failed to fetch different version of bureau");
				}else{
					throw new AgreementSystemException(IErrorCodes.SERVICE_BUREAU_VERSION_ERROR,
					"Procedure Failed to fetch different version of bureau");
				}
			}			
			// Return the result to BureauAction
			if(res == 1) {
				oraConRef.commit();
				ret = lst;
			} else {
				oraConRef.rollback();
				ret = null;
			}
			return ret;
		}catch (SQLException t) {
			logger.error("Exception in showBureauVersion : " + t);
			throw new AgreementSystemException(IErrorCodes.SERVICE_BUREAU_VERSION_ERROR,
					"Failed to show different bureau versions", t);
		}finally{
			SQLUtils.ensureClosedConnection(con,ocs,oraConRef);
		}
	}

	/** 
	 * Method is used to search records for Service Bureau from OPE Database
	 * It searches on Service Bureau id or Service Bureau name or PATU ID
	 * Get ServiceBureauSearchDTO Object as parameter
	 */

	public String createPatuId(ServiceBureauDTO e) throws AgreementSystemException,
	AgreementBusinessException {

		OracleConnection oraConRef = null;
		Connection con = null;
		OracleCallableStatement ocs=null;	
		String ret=null;
		int res;
		String patuId=null;

		/*Get Connection from Database */

		try {
			con = SQLUtils.getConnection(CommonConstants.DNBDatabase);
			oraConRef = DescriptorUtility.getOracleConnection(con);			
		} catch (SQLException sq) {
			logger.error("Could not obtain OracleConnection");
			throw new AgreementSystemException(IErrorCodes.CONNECTION_RETRIEVAL_ERROR,
					"Unable to obtain OracleConnection", sq);
		}			

		try {

			//	Prepare the OracleCallableStatement
			ocs = (OracleCallableStatement) oraConRef			
			.prepareCall("{? = call OPEPK_BUREAU_DEFN.FN_CREATE_PATU_ID(?,?,?,?)}");

			/** Bind inputs and register outputs */

			ocs.registerOutParameter(1, OracleTypes.NUMERIC);

			ocs.setString(2, e.getCompanyName());			
			ocs.registerOutParameter(3,OracleTypes.VARCHAR);							
			ocs.registerOutParameter(4, OracleTypes.VARCHAR);
			ocs.registerOutParameter(5, OracleTypes.VARCHAR);			

			/** Execute the procedure */
			ocs.execute();

			//	Log the result
			res = ocs.getInt(1);

			logger.debug("RETURN : " + res );
			logger.debug("ERROR CODE : " + ocs.getString(4));
			logger.debug("ERROR DESC : " + ocs.getString(5));			

			if(res==0) {
				oraConRef.rollback();
				if(ocs.getString(4)!=null){
					throw new AgreementBusinessException(ocs.getString(4),"Procedure Failed to Create Patu Id");
				}
				else{
					throw new AgreementSystemException(IErrorCodes.SERVICE_BUREAU_FETCH_ERROR,
					"Procedure Failed to Create Patu Id");
				}
			}

			patuId = ocs.getString(3);

			//	Return the result to the ServiceBureauAction
			if(res == 1) {
				oraConRef.commit();
				ret = patuId;
			} else {
				oraConRef.rollback();
				ret = null;
			}						
			return ret;
		} catch (SQLException t) {
			logger.debug("Exception in createPatuId : " + t);
			throw new AgreementSystemException(IErrorCodes.SERVICE_BUREAU_FETCH_ERROR,
					"Failed to create patu id", t);
		} catch(RuntimeException re) {
			throw new AgreementBusinessException(
					IErrorCodes.TYPECONVERSION_ERROR,
			"Exception while creating Object Array");
		} finally{
			SQLUtils.ensureClosedConnection(con,ocs,oraConRef);
		}
	}

	/** 
	 * Method is used to search previous versions of bureau 
	 * Get ServiceBureauDTO Object as parameter
	 */

	public List generateKeys(ServiceBureauBean e) throws AgreementSystemException,
	AgreementBusinessException {

		OracleConnection oraConRef = null;
		Connection con = null;
		OracleCallableStatement ocs=null;

		List ret=null;
		int res;
		/*Get Connection from Database */
		try {
			con = SQLUtils.getConnection(CommonConstants.DNBDatabase);
			oraConRef = DescriptorUtility.getOracleConnection(con);			
		} catch (SQLException sq) {
			logger.error("Could not obtain OracleConnection");
			throw new AgreementSystemException(IErrorCodes.CONNECTION_RETRIEVAL_ERROR,
					"Unable to obtain OracleConnection", sq);
		}	
		try {				
			// Prepare the OracleCallableStatement.
			ocs = (OracleCallableStatement) oraConRef
			.prepareCall("{? = call OPEPK_BUREAU_DEFN.FN_GET_OLD_KEK_AUK(?,?,?,?)}");

			/** Bind inputs and register outputs*/
			ocs.registerOutParameter(1, OracleTypes.NUMERIC);

			ocs.setString(2, e.getInternalBureauId());					    
			ocs.registerOutParameter(3, OracleTypes.ARRAY, "KEK_AUK_DEFN_TABLE");

			ocs.registerOutParameter(4, OracleTypes.VARCHAR);
			ocs.registerOutParameter(5, OracleTypes.VARCHAR);

			/** Execute the procedure */
			ocs.execute();

			// Log the Result
			res = ocs.getInt(1);
			logger.debug("RETURN : " + res );
			logger.debug("ERROR CODE : " + ocs.getString(4));
			logger.debug("ERROR DESC : " + ocs.getString(5));
			// Return the result to GenerateKeyAction
			if(res == 1) {
				//	Set up the ARRAY object to hold records.
				ARRAY attributes = ocs.getARRAY(3);  
				Object[] object = (Object[])attributes.getArray();

				// Create the List to hold records.
				List lst = makeListOfKeys(object);					
				oraConRef.commit();					
				ret = lst;
			} else {
				oraConRef.rollback();					
				ret = null;
			}
			return ret;
		}catch (SQLException t) {
			logger.error("Exception in generating keys: " + t);
			throw new AgreementSystemException(IErrorCodes.SERVICE_BUREAU_VERSION_ERROR,
					"Failed to generate the keys", t);
		}finally{
			SQLUtils.ensureClosedConnection(con,ocs,oraConRef);
		}
	}

	/** 
	 * Method is used to insert a new Service Bureau record to OPE Database
	 * Get ServiceBureauDTO Object as parameter
	 */

	public boolean insertKeys(ServiceBureauBean e,Timestamp businessDate,String versionNo,String action) throws AgreementSystemException,
	AgreementBusinessException{
		
		OracleConnection oraConRef = null;
		Connection con = null;
		OracleCallableStatement ocs=null;
		StructDescriptor keyStrDesc;
		ArrayDescriptor keyArrDesc;
		int generationNumber;

		int res;
		boolean ret = false;				

		try {
			con = SQLUtils.getConnection(CommonConstants.DNBDatabase);
			oraConRef = DescriptorUtility.getOracleConnection(con);			
		} catch (SQLException sq) {
			logger.error("Could not obtain OracleConnection");
			throw new AgreementSystemException(IErrorCodes.CONNECTION_RETRIEVAL_ERROR,
					"Unable to obtain OracleConnection", sq);
		}

		try {
			// Create Descriptors for OracleTypes										
			keyStrDesc = StructDescriptor.createDescriptor("KEK_AUK_DEFN_TYPE",oraConRef);
			keyArrDesc = ArrayDescriptor.createDescriptor("KEK_AUK_DEFN_TABLE",oraConRef);	

		}catch (SQLException t) {
			logger.error("Exception in insertBureau [Could not create Struct/Array] : "	+ t);
			throw new AgreementSystemException(IErrorCodes.DB_DESCRIPTOR_ERROR,
					"Could not create db Descriptors", t);
		}

		if(e.getGenerationNumber()== null){
			generationNumber=-1;
		}else{
			generationNumber = Integer.parseInt(e.getGenerationNumber());
		}
		try {					
			// Make the object of Key information from the bean class
			Object[] keyRecobj = {Integer.parseInt(versionNo),
					e.getInternalBureauId()
					,e.getKeyKEKPart1()
					,e.getKeyKEKPart2()
					,e.getKeyKEK()
					,generationNumber
					,""
					,e.getKeyKVV()											
					,businessDate
					,null
					,null

			};

			// Set up the struct object						
			STRUCT p1struct = new STRUCT(keyStrDesc, oraConRef, keyRecobj);
			Object[] keyArrobj = new Object[] { p1struct };

			// Set up the array object
			ARRAY keyArr = new ARRAY(keyArrDesc, oraConRef, keyArrobj);

			// Prepare the OracleCallableStatement
			ocs = (OracleCallableStatement) oraConRef
			.prepareCall("{? = call OPEPK_BUREAU_DEFN.FN_INSERT_KEK_AUK(?,?,?,?,?)}");

			/** Bind inputs and register outputs*/					 
			ocs.registerOutParameter(1, OracleTypes.NUMERIC);					
			ocs.setARRAY(2,keyArr);
			ocs.setString(3, action);
			ocs.setString(4, e.getKeyAUK());

			ocs.registerOutParameter(5, OracleTypes.VARCHAR);
			ocs.registerOutParameter(6, OracleTypes.VARCHAR);						

			/** Execute the procedure */
			ocs.execute();
			res = ocs.getInt(1);					
			logger.debug("RETURN : " + res);
			logger.debug("ERROR CODE : " + ocs.getString(5));
			logger.debug("ERROR DESC : " + ocs.getString(6));

			if(res==0) {
				oraConRef.rollback();
				if(ocs.getString(5)!=null){
					throw new AgreementBusinessException(ocs.getString(5),"Procedure Failed to Insert Keys");
				}else{
					throw new AgreementBusinessException(IErrorCodes.SERVICE_BUREAU_INSERT_ERROR,
					"Procedure Failed to Insert Keys");
				}				
			}			
			// Return the result to the BureauAction
			if (res == 1) {
				oraConRef.commit();
				ret = true;
			} else {
				oraConRef.rollback();
				ret = false;
			}			
			return ret;
		}catch (SQLException t) {
			logger.error("Exception in insertKeys : " + t);			
			throw new AgreementSystemException(IErrorCodes.SERVICE_BUREAU_INSERT_ERROR,
					"Failed to Insert Keys", t);
		}catch(RuntimeException re) {			
			throw new AgreementBusinessException(IErrorCodes.TYPECONVERSION_ERROR,
			"Exception while creating Object Array");
		}finally{
			SQLUtils.ensureClosedConnection(con,ocs,oraConRef);
		}
	}

	/** 
	 * Method is used to reset keys in OPE Database
	 * Get ServiceBureauBean Object as parameter
	 */

	public boolean resetKeys(ServiceBureauBean e) throws AgreementSystemException,
	AgreementBusinessException{
		OracleConnection oraConRef = null;
		Connection con = null;
		OracleCallableStatement ocs=null;
		int res;
		boolean ret = false;				

		try {
			con = SQLUtils.getConnection(CommonConstants.DNBDatabase);
			oraConRef = DescriptorUtility.getOracleConnection(con);			
		} catch (SQLException sq) {
			logger.error("Could not obtain OracleConnection");
			throw new AgreementSystemException(IErrorCodes.CONNECTION_RETRIEVAL_ERROR,
					"Unable to obtain OracleConnection", sq);
		}			
		try {					

			// Prepare the OracleCallableStatement
			ocs = (OracleCallableStatement) oraConRef
			.prepareCall("{? = call OPEPK_BUREAU_DEFN.FN_RESET_OLD_KEK_AUK(?,?,?)}");

			/** Bind inputs and register outputs*/					 
			ocs.registerOutParameter(1, OracleTypes.NUMERIC);					
			ocs.setString(2,e.getInternalBureauId());
			ocs.registerOutParameter(3, OracleTypes.VARCHAR);
			ocs.registerOutParameter(4, OracleTypes.VARCHAR);						

			/** Execute the procedure */
			ocs.execute();
			res = ocs.getInt(1);					
			logger.debug("RETURN : " + res);
			logger.debug("ERROR CODE : " + ocs.getString(3));
			logger.debug("ERROR DESC : " + ocs.getString(4));

			if(res==0) {
				oraConRef.rollback();
				if(ocs.getString(3)!=null){
					throw new AgreementBusinessException(ocs.getString(3),"Procedure Failed to Reset Keys");
				}else{
					throw new AgreementBusinessException(IErrorCodes.SERVICE_BUREAU_INSERT_ERROR,
					"Procedure Failed to Reset Keys");
				}				
			}			
			// Return the result to the BureauAction
			if (res == 1) {
				oraConRef.commit();
				ret = true;
			} else {
				oraConRef.rollback();
				ret = false;
			}			
			return ret;
		}catch (SQLException t) {
			logger.error("Exception in resetKeys : " + t);			
			throw new AgreementSystemException(IErrorCodes.SERVICE_BUREAU_INSERT_ERROR,
					"Failed to Reset Keys", t);
		}catch(RuntimeException re) {			
			throw new AgreementBusinessException(IErrorCodes.TYPECONVERSION_ERROR,
			"Exception while creating Object Array");
		}finally{
			SQLUtils.ensureClosedConnection(con,ocs,oraConRef);
		}
	}

	/** 
	 * Method makes a list of records returned from database
	 */

	private List makeListOfKeys(Object[] obj)throws AgreementSystemException,
	AgreementBusinessException {

		List list = new ArrayList();

		for (int i = 0; i < obj.length; i++) 
		{
			try {
				STRUCT attrObject = (STRUCT)obj[i];
				Object[] strt = attrObject.getAttributes();
				ServiceBureauBean ed=new ServiceBureauBean();

				if(strt[0]!=null){
					ed.setVersionNo(strt[0].toString());
				}else
					ed.setVersionNo("");

				ed.setInternalBureauId((String)strt[1]);	

				ed.setKeyKEKPart1((String)strt[2]);
				ed.setKeyKEKPart2((String)strt[3]);
				ed.setKeyKEK((String)strt[4]);			  	  
				if(strt[5]!=null){
					ed.setGenerationNumber(strt[5].toString());
				}else
					ed.setGenerationNumber("");
				ed.setStatus((String)strt[6]);
				ed.setKeyKVV((String)strt[7]);
				if(strt[8]!=null){
					ed.setGenerationDate(strt[8].toString());
				}else
					ed.setGenerationDate("");
				if(strt[9]!=null){
					ed.setKeyExpireDate(strt[9].toString());
				}else
					ed.setKeyExpireDate("");
				ed.setControlCode((String)strt[10]);
				// Add Object to list
				list.add(i,ed);

			} catch (Exception e){
				logger.error("Exception in Create list" + e);
				throw new AgreementSystemException(IErrorCodes.SERVICE_BUREAU_MAKELIST_ERROR,
						"Failed to Fetch Records", e);
			}
		}
		return list;
	}	

	/** 
	 * Method makes a list of records returned from database
	 */

	private List makeList(Object[] obj,String version_pos)throws AgreementSystemException,
	AgreementBusinessException {

		List list = new ArrayList();

		for (int i = 0; i < obj.length; i++) 
		{
			try {
				STRUCT attrObject = (STRUCT)obj[i];
				Object[] strt = attrObject.getAttributes();
				ServiceBureauDTO ed=new ServiceBureauDTO();

				if(strt[0]!=null){
					ed.setVersionNo(strt[0].toString());
				}else
					ed.setVersionNo("");

				ed.setInternalBureauId((String)strt[1]);	
				ed.setBureauId((String)strt[2]);		  
				ed.setBureauName((String)strt[3]);
				ed.setBureauDescription((String)strt[4]);

				ed.setCompanyName((String)strt[5]);			  
				ed.setPatuId((String)strt[6]);
				ed.setContactPerson1((String)strt[7]);
				ed.setContactPerson2((String)strt[8]);

				ed.setAddressLine1((String)strt[9]);
				ed.setPinCode1((String)strt[10]);
				ed.setCity1((String)strt[11]);
				ed.setCountry1((String)strt[12]);
				ed.setTelephoneNo1((String)strt[13]);

				ed.setAddressLine2((String)strt[14]);
				ed.setPinCode2((String)strt[15]);
				ed.setCity2((String)strt[16]);
				ed.setCountry2((String)strt[17]);
				ed.setTelephoneNo2((String)strt[18]);

				ed.setStatus((String)strt[19]);			  
				ed.setCreatedBy((String)strt[20]);			  
				if(strt[21]!=null){
					ed.setCreatedOn(strt[21].toString());
				}else
					ed.setCreatedOn("");			  
				ed.setLastUpdatedBy((String)strt[22]);			  
				if(strt[23]!=null){
					ed.setLastUpdatedOn(strt[23].toString());
				}else
					ed.setLastUpdatedOn("");  
				
				ed.setAuthorizedBy((String)strt[24]);			  
				
				if(strt[25]!=null){
					ed.setAuthorizedOn(strt[25].toString());
				}else
					ed.setAuthorizedOn("");

				ed.setVersionPos(version_pos);

				// Add Object to list
				list.add(i,ed);

			} catch (Exception e){
				logger.error("Exception in Create list" + e);
				throw new AgreementSystemException(IErrorCodes.SERVICE_BUREAU_MAKELIST_ERROR,
						"Failed to Fetch Records", e);
			}
		}
		return list;
	}	

	/**
	 * Method used to display the active and inactive keys to the service bureau user interface
	 */
	public List getKekAukDisplay(String internalBureauId)throws AgreementSystemException,
	AgreementBusinessException {
		OracleConnection oraConRef = null;
		Connection con = null;		
		OracleCallableStatement ocs=null;
		KekAukServiceBureauBean kekAukBean = null;
		int res;
		//Object[] object=null;
		List kekaukList = new ArrayList();
		try {
			con = SQLUtils.getConnection(CommonConstants.DNBDatabase);
			oraConRef = DescriptorUtility.getOracleConnection(con);			
		} catch (SQLException sq) {
			logger.error("Could not obtain OracleConnection");
			throw new AgreementSystemException(IErrorCodes.CONNECTION_RETRIEVAL_ERROR,
					"Unable to obtain OracleConnection", sq);
		}	

		try {	
			
			if(internalBureauId != null)
			{
				ocs = (OracleCallableStatement) oraConRef
				.prepareCall("{? = call OPEPK_BUREAU_DEFN.FN_KEK_AUK_DISPLAY(?,?,?,?)}");       
				ocs.registerOutParameter(1,OracleTypes.NUMERIC);
				ocs.setString(2,internalBureauId);
				ocs.registerOutParameter(3, OracleTypes.ARRAY,"KEK_AUK_DISPLAY_TABLE");
				ocs.registerOutParameter(4, OracleTypes.VARCHAR);
				ocs.registerOutParameter(5, OracleTypes.VARCHAR);
				ocs.execute();	
			}
			res = ocs.getInt(1);
			logger.debug("RETURN     :" + res );			
			logger.debug("ERROR CODE :" + ocs.getString(4));
			logger.debug("ERROR DESC :" + ocs.getString(5));

			if(res==0) {
				oraConRef.rollback();				
			}
			if(res == 1) {			
				ARRAY attributes = ocs.getARRAY(3);  					
				Object[] object = (Object[])attributes.getArray();

				for(int j=0;j<object.length;j++) {
					try
					{
						STRUCT attrObject = (STRUCT)object[j];
						Object[] objStruct = attrObject.getAttributes();
						kekAukBean = new KekAukServiceBureauBean();
						kekAukBean.setKekGenerationNo(String.valueOf(objStruct[0]));
						kekAukBean.setGenerationDate(objStruct[1].toString());
						kekAukBean.setKvv(objStruct[2].toString());
						kekAukBean.setAukGenerationNo(objStruct[3].toString());
						kekAukBean.setKekGenerationNo1(String.valueOf(objStruct[4]));
						if(objStruct[5]!=null){
							kekAukBean.setGenerationDate1(objStruct[5].toString());
						}
						if(objStruct[6]!=null){
							kekAukBean.setKvv1(objStruct[6].toString());	
						}
					}
					catch(NullPointerException e)
					{
						logger.error("Null value :::"+e.getMessage());
					}
					kekaukList.add(kekAukBean);
				}
				oraConRef.commit();
			}
			
		} catch (SQLException sqlException) {
			logger.debug(sqlException);
		} 
		finally {			
				//con.close();
				SQLUtils.ensureClosedConnection(con,ocs,oraConRef);
			
		}
		return kekaukList;
	}


	/**
	 * Method used to insert the auk key into the data base  
	 */ 	
	public boolean insertAuk(String internalBureauId,String generatedAuk, java.sql.Timestamp businessDate)throws AgreementSystemException,
	AgreementBusinessException {
		OracleConnection oraConRef = null;
		Connection con = null;		
		OracleCallableStatement ocs=null;
		boolean flag = false;
		int res;

		try {
			con = SQLUtils.getConnection(CommonConstants.DNBDatabase);
			oraConRef = DescriptorUtility.getOracleConnection(con);			
		} catch (SQLException sq) {
			logger.error("Could not obtain OracleConnection");
			throw new AgreementSystemException(IErrorCodes.CONNECTION_RETRIEVAL_ERROR,
					"Unable to obtain OracleConnection", sq);
		}	

		try {
			if(internalBureauId != null)
			{
				ocs = (OracleCallableStatement) oraConRef
				.prepareCall("{? = call OPEPK_BUREAU_DEFN.FN_AUK_RESET(?,?,?,?,?)}");       
				ocs.registerOutParameter(1,OracleTypes.NUMERIC);
				ocs.setString(2,internalBureauId);
				ocs.setString(3,generatedAuk);
				ocs.setTimestamp(4,businessDate);
				ocs.registerOutParameter(5, OracleTypes.VARCHAR);
				ocs.registerOutParameter(6, OracleTypes.VARCHAR);
				ocs.execute();	
			}
			res = ocs.getInt(1);
			logger.debug("RETURN   :::" + res );			
			logger.debug("ERROR CODE :" + ocs.getString(5));
			logger.debug("ERROR DESC :" + ocs.getString(6));

			if(res==0) {
				oraConRef.rollback();
				flag = false;				
			}
			if(res == 1) {			
				oraConRef.commit();
				flag = true;
			}
			
		} catch (SQLException sqlException) {
			logger.error(sqlException);
		} 
		finally {
			SQLUtils.ensureClosedConnection(con,ocs,oraConRef);
		}
		return flag;
	}

	/**
	 * Method is used to print the keys when user clicks on print patu id button in service bureau edit 
	 */
	public List printKeys(String internalBureauId)throws AgreementSystemException,
	AgreementBusinessException {
		OracleConnection oraConRef = null;
		Connection con = null;		
		OracleCallableStatement ocs=null;
		ServiceBureauBean serviceBureauBean=null;
		int res = 0;
		List printList = new ArrayList();
		try {
			con = SQLUtils.getConnection(CommonConstants.DNBDatabase);
			oraConRef = DescriptorUtility.getOracleConnection(con);			
		} catch (SQLException sq) {
			logger.error("Could not obtain OracleConnection");
			throw new AgreementSystemException(IErrorCodes.CONNECTION_RETRIEVAL_ERROR,
					"Unable to obtain OracleConnection", sq);
		}	

		try {		
			if(internalBureauId != null)
			{
				ocs = (OracleCallableStatement) oraConRef.prepareCall("{? = call OPEPK_BUREAU_DEFN.FN_PRINT_KEK_AUK(?,?,?,?,?,?,?)}");       
				ocs.registerOutParameter(1,OracleTypes.NUMERIC);
				ocs.setString(2,internalBureauId);
				ocs.registerOutParameter(3, OracleTypes.ARRAY,"KEK_AUK_DEFN_TABLE");
				ocs.registerOutParameter(4, OracleTypes.VARCHAR);
				ocs.registerOutParameter(5, OracleTypes.VARCHAR);
				ocs.registerOutParameter(6, OracleTypes.VARCHAR);
				ocs.registerOutParameter(7, OracleTypes.VARCHAR);
				ocs.registerOutParameter(8, OracleTypes.VARCHAR);
				ocs.execute();	
			}
			res = ocs.getInt(1);
			logger.debug("RETURN     :" + res );			
			logger.debug("ERROR CODE :" + ocs.getString(6));
			logger.debug("ERROR DESC :" + ocs.getString(7));
		}catch(Exception ee){
			ee.printStackTrace();
		}

		try{
			if(res==0) {
				oraConRef.rollback();				
			}
			if(res == 1) {			
				ARRAY attributes = ocs.getARRAY(3);  				
				String customerName1 = ocs.getString(4);
				String CustomerName2 = ocs.getString(5);
				String controlCode = ocs.getString(6);

				Object[] object = (Object[])attributes.getArray();

				for(int j=0;j<object.length;j++) {
					try{
						STRUCT attrObject = (STRUCT)object[j];
						Object[] objStruct = attrObject.getAttributes();
						serviceBureauBean =new ServiceBureauBean();
						serviceBureauBean.setVersionNo(objStruct[0].toString());
						serviceBureauBean.setInternalBureauId(objStruct[1].toString());
						serviceBureauBean.setKeyKEKPart1(objStruct[2].toString());
						serviceBureauBean.setKeyKEKPart2(objStruct[3].toString());
						serviceBureauBean.setKeyKEK(objStruct[4].toString());
						serviceBureauBean.setGenerationNumber(objStruct[5].toString());
						serviceBureauBean.setStatus(objStruct[6].toString());
						serviceBureauBean.setKeyKVV(objStruct[7].toString());
						serviceBureauBean.setGenerationDate(objStruct[8].toString());
						serviceBureauBean.setKeyExpireDate(objStruct[9].toString());
						serviceBureauBean.setControlCode(objStruct[10].toString());
						serviceBureauBean.setContactPerson1(customerName1);
						serviceBureauBean.setContactPerson2(CustomerName2);
						serviceBureauBean.setControlCode(controlCode);
					}catch(NullPointerException npe){
						logger.error("Null value in print keys ::"+npe.getMessage());
					}
				}
				printList.add(serviceBureauBean);
				oraConRef.commit();
			}
		} catch (SQLException sqlException) {
			logger.error(sqlException);
		} 
		finally {
			SQLUtils.ensureClosedConnection(con,ocs,oraConRef);
		}
		return printList;
	}
}