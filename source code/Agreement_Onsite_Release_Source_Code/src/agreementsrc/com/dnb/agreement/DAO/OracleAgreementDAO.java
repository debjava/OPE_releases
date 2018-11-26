/********************************************************************************************
 * Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                        *
 * IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.       *
 * The Program or any portion thereof may not be reproduced in any form whatsoever except    *
 * as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd. *
 *																							 *
 * File Name                   : OracleAgreementDAO.java                                     *
 * Author                      : Anantaraj S                                                 *
 * Creation Date               : 21-July-2008                                                *
 * Description                 : This file serves as the DAO Class, which contains all       *
 *                               the methods which are used for Agreement.                   *                                             *
 * Modification History        :                                                             *
 *																						     *
 *     Version No.               Date               Brief description of change              *
 *  ---------------------------------------------------------------------------------------  *
 *                       |                  |											     *
 *                       |                  |											     *
 *  ---------------------------------------------------------------------------------------  *
 *********************************************************************************************/


/** 
 * Create or import Packages 
 */
package com.dnb.agreement.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleTypes;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;

import com.dnb.agreement.DTO.AgreementDTO;
import com.dnb.agreement.DTO.AgreementSearchDTO;
import com.dnb.agreement.DTO.ServiceAccountSpecificDTO;
import com.dnb.agreement.DTO.ServiceCustomerIdSpecificDTO;
import com.dnb.agreement.DTO.ServiceSpecificDTO;
import com.dnb.agreement.bean.AgreementCommonBean;
import com.dnb.agreement.bean.CustomerIdBean;
import com.dnb.agreement.bean.ServiceBureauBean;
import com.dnb.agreement.bean.ServiceBureauPatuIdSpecificBean;
import com.dnb.agreement.common.OPEConstants;
import com.dnb.common.database.DescriptorUtility;
import com.dnb.agreement.exception.AgreementBusinessException;
import com.dnb.agreement.exception.AgreementSystemException;
import com.dnb.agreement.exception.IErrorCodes;
import com.dnb.agreement.utility.SQLUtils;
import com.dnb.common.commons.CommonConstants;

public class OracleAgreementDAO implements AgreementDAO{

	private static Logger logger = Logger.getLogger(OracleAgreementDAO.class);

	/** 
	 * Method is used to insert new agreement 
	 * AgreementCommonBean,AgreementBean[],user id, date ,action, as parameter
	 */
	public boolean insert(AgreementCommonBean acb,String userId,
			Date businessDate,String action)throws AgreementSystemException,AgreementBusinessException

			{
		OracleConnection oraConRef=null;
		Connection con=null;
		OracleCallableStatement ocs=null;
		StructDescriptor agrStrDesc,serviceMapStrDesc;
		ArrayDescriptor agrArrDesc,agrEntityArrDesc,serviceMapArrDesc;
		int res;
		boolean ret=false;

		try {
			con = SQLUtils.getConnection(CommonConstants.DNBDatabase);
			oraConRef = DescriptorUtility.getOracleConnection(con);			
		} catch (SQLException sq) {
			logger.error("Could not obtain OracleConnection");
			throw new AgreementSystemException(IErrorCodes.CONNECTION_RETRIEVAL_ERROR,
					"Unable to obtain OracleConnection", sq);
		}

		try{
			agrStrDesc=StructDescriptor.createDescriptor("AGRE_DEFN_TYPE", oraConRef);
			agrArrDesc=ArrayDescriptor.createDescriptor("AGRE_DEFN_TABLE", oraConRef);

			serviceMapStrDesc=StructDescriptor.createDescriptor("SERVICE_DEFN_TYPE", oraConRef);
			serviceMapArrDesc=ArrayDescriptor.createDescriptor("SERVICE_DEFN_TABLE", oraConRef);

			agrEntityArrDesc=ArrayDescriptor.createDescriptor("ENTITY_ID_TYPE",oraConRef);

		}catch (SQLException t) {
			logger.error("Exception in insert agreement [Could not create Struct/Array] : "	+ t);
			throw new AgreementSystemException(IErrorCodes.DB_DESCRIPTOR_ERROR,
					"Could not create db Descriptors", t);
		}

		try {

			ARRAY serviceMapArr=null;
			ARRAY agrArr=null,agrDelArr=null;
			String [] serviceStr;
			String str1;

			// Get the selected services with comma seperated values
			String services=acb.getSelectedServices();	
			serviceStr=createStrings(services);

			int countStr=serviceStr.length;
			Object [] serviceObjTable=new Object[countStr];

			if(serviceStr!=null)
			{
				for (int k=0;k<countStr;k++)
				{
					str1=serviceStr[k];		   
					Object serviceObject []={1,acb.getInternalAgreementId(),str1};		 	   	 
					STRUCT serviceStruct=new STRUCT(serviceMapStrDesc, oraConRef, serviceObject);
					serviceObjTable[k] = serviceStruct; 	  
				}
				serviceMapArr=new ARRAY(serviceMapArrDesc,oraConRef,serviceObjTable);  
			}

			Object[] agrCommonObj = {acb.getInternalAgreementId()
					,new Integer(1)			          //version no 
			,acb.getAgreementTitle()           
			,acb.getPrimaryContact()
			,acb.getStreetAddress()
			,acb.getZipCode()
			,acb.getCity()
			,acb.getCountry()
			,acb.getEmail()
			,acb.getTelephone() 
			,null
			,parseDate(acb.getValidFrom())
			,parseDate(acb.getValidTo()) 
			,acb.getSecurityMethod()
			,"A"                                    //acb.getStatus()
			,userId                                 //acb.getCreatedBy()
			,businessDate                           //acb.getCreatedOn()
			,userId                                 //acb.getLastUpdatedBy()
			,businessDate                           //acb.getLastUpdatedOn()  
			,userId                                 //acb.getAuthorisedBy()             
			,businessDate                           //acb.getAuthorisedOn()
			};

			// Set up the struct object

			try{

				STRUCT agrStruct = new STRUCT(agrStrDesc, oraConRef, agrCommonObj);

				Object[] agrArrobj = new Object[1];
				agrArrobj[0]= agrStruct;

				Object[] agrDelListObj=new Object[1];
				agrDelListObj[0]="";

				agrArr = new ARRAY(agrArrDesc, oraConRef, agrArrobj);
				agrDelArr = new ARRAY(agrEntityArrDesc,oraConRef,agrDelListObj);

			}catch(RuntimeException r){
				logger.debug("Run time Exception Exception "+r);
			}

			// Prepare the OracleCallableStatement
			ocs = (OracleCallableStatement)
			oraConRef.prepareCall("{? = call opepk_agreement.fn_agreement(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");

			/** Bind inputs and register outputs*/
			ocs.registerOutParameter(1, OracleTypes.NUMERIC);
			ocs.setString(2, userId);
			ocs.setString(3, action);
			ocs.setDate(4, (java.sql.Date) businessDate);                  //business date
			ocs.setString(5, acb.getInternalAgreementId());                //agreement ID
			ocs.setString(6, null);                                        //customer name
			ocs.setInt(7,1);                                               //version number
			ocs.setARRAY(8,agrDelArr);                                     //entity_id_type
			ocs.setARRAY(9,agrArr);                                        //agre_defn_table
			ocs.setARRAY(10, serviceMapArr);                               //SERVICE_DEFN_TABLE

			ocs.registerOutParameter(11, OracleTypes.ARRAY, "AGRE_DEFN_TABLE");
			ocs.registerOutParameter(12, OracleTypes.ARRAY, "SERVICE_DEFN_TABLE");

			ocs.registerOutParameter(13, OracleTypes.VARCHAR);		
			ocs.registerOutParameter(14,OracleTypes.VARCHAR);
			ocs.registerOutParameter(15,OracleTypes.VARCHAR);

			/** Execute the procedure */
			ocs.execute();
			res = ocs.getInt(1);

			logger.debug("RETURN : " + res);
			logger.debug("ERROR CODE : " + ocs.getString(14));
			logger.debug("ERROR DESC : " + ocs.getString(15));

			if(res==0) {
				oraConRef.rollback();
				if(ocs.getString(14)!=null){
					throw new AgreementBusinessException(ocs.getString(14),"Procedure Failed to Insert");
				}else{
					throw new AgreementBusinessException(IErrorCodes.AGREEMENT_INSERT_ERROR,
					"Procedure Failed to Insert");
				}				
			}

			// Return the result to the Agreement Action
			if (res == 1) {
				oraConRef.commit();
				ret = true;
			} else {
				oraConRef.rollback();
				ret = false;
			}	
			return ret;

		}catch (SQLException t) {
			logger.error("Exception in insert agreement : " + t);			
			throw new AgreementSystemException(IErrorCodes.AGREEMENT_INSERT_ERROR,
					"Failed to Insert Record for New agreement", t);
		}catch(RuntimeException re) {
			re.printStackTrace();
			throw new AgreementBusinessException(IErrorCodes.TYPECONVERSION_ERROR,
			"Exception while creating Object Array");
		}
		finally
		{
			// connection is closing here
			SQLUtils.ensureClosedConnection(con,ocs,oraConRef);
		}

			}

	/** 
	 * Method is used to search the agreement 
	 * AgreementSearchDTO,AgreementBean[],user id, date,action, as parameter
	 */

	public List search(AgreementSearchDTO agrSearchDTO, String action)
	throws AgreementSystemException,AgreementBusinessException 
	{
		OracleConnection oraConRef=null;
		Connection con=null;
		OracleCallableStatement ocs=null;
		StructDescriptor agrStrDesc,serviceMapStrDesc;
		ArrayDescriptor agrArrDesc,agrEntityArrDesc,serviceMapArrDesc;
		int res;
		List ret=null;

		try {
			con = SQLUtils.getConnection(CommonConstants.DNBDatabase);
			oraConRef = DescriptorUtility.getOracleConnection(con);			
		} catch (SQLException sq) {
			logger.error("Could not obtain OracleConnection");
			throw new AgreementSystemException(IErrorCodes.CONNECTION_RETRIEVAL_ERROR,
					"Unable to obtain OracleConnection", sq);
		}

		try{

			agrStrDesc=StructDescriptor.createDescriptor("AGRE_DEFN_TYPE", oraConRef);
			agrArrDesc=ArrayDescriptor.createDescriptor("AGRE_DEFN_TABLE", oraConRef);

			serviceMapStrDesc=StructDescriptor.createDescriptor("SERVICE_DEFN_TYPE", oraConRef);
			serviceMapArrDesc=ArrayDescriptor.createDescriptor("SERVICE_DEFN_TABLE", oraConRef);

			agrEntityArrDesc=ArrayDescriptor.createDescriptor("ENTITY_ID_TYPE",oraConRef);

		}catch (SQLException t) {
			logger.error("Exception in search agreement [Could not create Struct/Array] : "	+ t);
			throw new AgreementSystemException(IErrorCodes.DB_DESCRIPTOR_ERROR,
					"Could not create db Descriptors", t);
		} 

		try {

			ARRAY serviceMapArr=null;
			ARRAY agrArr=null,agrDelArr=null;

			//  service object table array
			Object [] serviceObjTable=new Object[1];
			Object serviceObject []={null,null,null};

			STRUCT serviceStruct= new STRUCT(serviceMapStrDesc, oraConRef, serviceObject);
			serviceObjTable[0] = serviceStruct;    
			serviceMapArr=new ARRAY(serviceMapArrDesc,oraConRef,serviceObjTable);

			//  agreement common object for Agreement defnition table
			Object[] agrCommonObj = {null,null,null,null,null,null,
									null,null,null,null,null,null,
									null,null,null,null,null,null,
									null,null,null
			};                

			STRUCT agrStruct = new STRUCT(agrStrDesc, oraConRef, agrCommonObj);

			Object[] agrArrobj = new Object[1];
			agrArrobj[0]= agrStruct;

			Object[] agrDelListObj=new Object[1];
			agrDelListObj[0]="";

			agrArr = new ARRAY(agrArrDesc, oraConRef, agrArrobj);
			agrDelArr = new ARRAY(agrEntityArrDesc,oraConRef,agrDelListObj);


			//	Prepare the OracleCallableStatement
			ocs = (OracleCallableStatement)
			oraConRef.prepareCall("{? = call opepk_agreement.fn_agreement(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");

			/** Bind inputs and register outputs*/
			ocs.registerOutParameter(1, OracleTypes.NUMERIC);
			ocs.setString(2, "");
			ocs.setString(3, action);
			ocs.setDate(4, null);                                           //business date	
			ocs.setString(5, agrSearchDTO.getInternalAgreementId());        //internal agreement ID
			ocs.setString(6, agrSearchDTO.getAgreementTitle());             //AgreementTitle
			ocs.setInt(7,1);                                                //version number
			ocs.setARRAY(8,agrDelArr);                                      //entity_id_type
			ocs.setARRAY(9,agrArr);                                         //agre_defn_table
			ocs.setARRAY(10, serviceMapArr);                                //service_map_defn_table

			ocs.registerOutParameter(11, OracleTypes.ARRAY, "AGRE_DEFN_TABLE");
			ocs.registerOutParameter(12, OracleTypes.ARRAY, "SERVICE_DEFN_TABLE");

			ocs.registerOutParameter(13, OracleTypes.VARCHAR);	
			ocs.registerOutParameter(14,OracleTypes.VARCHAR);
			ocs.registerOutParameter(15,OracleTypes.VARCHAR);

			/** Execute the procedure */
			ocs.execute();
			res = ocs.getInt(1);

			logger.debug("RETURN : " + res);
			logger.debug("ERROR CODE : " + ocs.getString(14));
			logger.debug("ERROR DESC : " + ocs.getString(15));

			if(res==0) {
				oraConRef.rollback();
				if(ocs.getString(14)!=null){
					throw new AgreementBusinessException(ocs.getString(14),"Procedure Failed to search");
				}else{
					throw new AgreementBusinessException(IErrorCodes.AGREEMENT_FETCH_ERROR,
					"Procedure Failed to search");
				}				
			}

			//	set up the array object to accept data

			ARRAY attributes = ocs.getARRAY(11);  
			Object[] agrObj = (Object[])attributes.getArray();

			List agrList = makeListOfAgreement(agrObj,"");

			ARRAY serviceAttributes=ocs.getARRAY(12);
			Object[] serviceMapObj=(Object[]) serviceAttributes.getArray();

			List serviceMapList=makeListOfServiceMap(serviceMapObj, "");		
			List lst=new ArrayList();

			lst.add(0, agrList);
			lst.add(1, serviceMapList);

			//	Return the result to the Agreement Search Action		
			if(res == 1) {
				oraConRef.commit();
				ret = lst;
			} else {
				oraConRef.rollback();
				ret = null;
			}						
			return ret;

		} catch (SQLException t) {
			logger.debug("Exception in agreement search  : " + t);
			throw new AgreementSystemException(IErrorCodes.AGREEMENT_FETCH_ERROR,
					"Failed to Fetch records for agreement", t);
		} catch(RuntimeException re) {
			throw new AgreementBusinessException(
					IErrorCodes.TYPECONVERSION_ERROR,
			"Exception while creating Object Array");
		} finally{
			// connection is closing here
			SQLUtils.ensureClosedConnection(con,ocs,oraConRef);
		}
	}

	/** 
	 * Method is used to delete the agreement 
	 * AgreementSearchDTO, user id, date,action, as parameter
	 */

	public boolean delete(AgreementSearchDTO agrSearchDTO, String userId, Date businessDate, String action, String deleteMode)
	throws AgreementSystemException, AgreementBusinessException 
	{

		OracleConnection oraConRef=null;
		Connection con=null;
		OracleCallableStatement ocs=null;
		StructDescriptor agrStrDesc,serviceMapStrDesc;
		ArrayDescriptor agrArrDesc,agrEntityArrDesc,serviceMapArrDesc;
		int res;
		boolean ret;

		try {
			con = SQLUtils.getConnection(CommonConstants.DNBDatabase);
			oraConRef = DescriptorUtility.getOracleConnection(con);			
		} catch (SQLException sq) {
			logger.error("Could not obtain OracleConnection");
			throw new AgreementSystemException(IErrorCodes.CONNECTION_RETRIEVAL_ERROR,
					"Unable to obtain OracleConnection", sq);
		}
		try{			  	
			agrStrDesc=StructDescriptor.createDescriptor("AGRE_DEFN_TYPE", oraConRef);
			agrArrDesc=ArrayDescriptor.createDescriptor("AGRE_DEFN_TABLE", oraConRef);

			serviceMapStrDesc=StructDescriptor.createDescriptor("SERVICE_DEFN_TYPE", oraConRef);
			serviceMapArrDesc=ArrayDescriptor.createDescriptor("SERVICE_DEFN_TABLE", oraConRef);

			agrEntityArrDesc=ArrayDescriptor.createDescriptor("ENTITY_ID_TYPE",oraConRef);

		}catch (SQLException t) {
			logger.error("Exception in delete agreement [Could not create Struct/Array] : "	+ t);
			throw new AgreementSystemException(IErrorCodes.DB_DESCRIPTOR_ERROR,
					"Could not create db Descriptors", t);
		} 

		try {
			ARRAY serviceMapArr=null;
			ARRAY agrArr=null,agrDelArr=null;

			//  service object table array

			Object [] serviceObjTable=new Object[1];
			Object serviceObject []={null,null,null};

			STRUCT serviceStruct= new STRUCT(serviceMapStrDesc, oraConRef, serviceObject);
			serviceObjTable[0] = serviceStruct;    
			serviceMapArr=new ARRAY(serviceMapArrDesc,oraConRef,serviceObjTable);

			// agreementCommonObj for agreement defnition table

			Object[] agrmtCommonObj = {null,null,null,null,null,null,
					                   null,null,null,null,null,null,
					                   null,null,null,null,null,null,
					                   null,null,null
			};                

			STRUCT agrStruct = new STRUCT(agrStrDesc, oraConRef, agrmtCommonObj);
			Object[] agrArrobj = new Object[1];
			agrArrobj[0]= agrStruct;
			agrArr = new ARRAY(agrArrDesc, oraConRef, agrArrobj);

			Object[] agrDelListObj=createObject(agrSearchDTO.getDeleteMe());														
			agrDelArr = new ARRAY(agrEntityArrDesc,oraConRef,agrDelListObj);

			//	Prepare the OracleCallableStatement
			ocs = (OracleCallableStatement)
			oraConRef.prepareCall("{? = call opepk_agreement.fn_agreement(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");

			ocs.registerOutParameter(1, OracleTypes.NUMERIC);
			ocs.setString(2, userId);
			ocs.setString(3, action);
			ocs.setDate(4, (java.sql.Date) businessDate);                      //business date	
			ocs.setString(5,"" );                                              //Internal agreement ID
			ocs.setString(6, "");                                              //Agreement title
			ocs.setInt(7,1);                                                   //version number
			ocs.setARRAY(8,agrDelArr);                                         //entity_id_type
			ocs.setARRAY(9,agrArr);                                            //agre_defn_table
			ocs.setARRAY(10, serviceMapArr);                                   //service_defn_table

			ocs.registerOutParameter(11, OracleTypes.ARRAY, "AGRE_DEFN_TABLE");
			ocs.registerOutParameter(12, OracleTypes.ARRAY, "SERVICE_DEFN_TABLE");
			ocs.registerOutParameter(13, OracleTypes.VARCHAR);

			ocs.registerOutParameter(14,OracleTypes.VARCHAR);
			ocs.registerOutParameter(15,OracleTypes.VARCHAR);

			/** Execute the procedure */
			ocs.execute();

			res = ocs.getInt(1);			
			logger.debug("RETURN : " + res);
			logger.debug("ERROR CODE : " + ocs.getString(14));
			logger.debug("ERROR DESC : " + ocs.getString(15));

			if(res==0) {
				oraConRef.rollback();
				ret = false;
				if(ocs.getString(14)!=null){
					throw new AgreementBusinessException(ocs.getString(14),
					"Procedure Failed to delete agreement");
				}
				else{
					throw new AgreementSystemException(IErrorCodes.AGREEMENT_DELETE_ERROR,
					"Procedure Failed to delete agreement");
				}
			}

			if(res == 1) {
				oraConRef.commit();
				ret = true;
			} else {
				oraConRef.rollback();	
				ret = false;
			}	
			return ret;

		}catch (SQLException t) {
			logger.error("Exception in delete agreement : " + t);
			throw new AgreementSystemException(IErrorCodes.AGREEMENT_DELETE_ERROR,
					"Failed to delete agreement ", t);
		}catch(RuntimeException re) {
			throw new AgreementBusinessException(
					IErrorCodes.TYPECONVERSION_ERROR,
			"Exception while creating Object Array");
		}finally{
			// closing connection here
			SQLUtils.ensureClosedConnection(con,ocs,oraConRef);
		}    	
	}

	/** 
	 * Method is used to edit the agreement 
	 * agreementbean[],Agreement common bean, user id, date,action, as parameter
	 */

	public boolean edit(AgreementCommonBean acb, String userId,
			Date businessDate,String action)
	throws AgreementSystemException, AgreementBusinessException
	{

		OracleConnection oraConRef=null;
		Connection con=null;
		OracleCallableStatement ocs=null;
		StructDescriptor agrStrDesc,serviceMapStrDesc;
		ArrayDescriptor agrArrDesc,agrEntityArrDesc,serviceMapArrDesc;
		int res;
		boolean ret=false;

		try {
			con = SQLUtils.getConnection(CommonConstants.DNBDatabase);
			oraConRef = DescriptorUtility.getOracleConnection(con);			
		} catch (SQLException sq) {
			logger.error("Could not obtain OracleConnection");
			throw new AgreementSystemException(IErrorCodes.CONNECTION_RETRIEVAL_ERROR,
					"Unable to obtain OracleConnection", sq);
		}

		try{

			agrStrDesc=StructDescriptor.createDescriptor("AGRE_DEFN_TYPE", oraConRef);
			agrArrDesc=ArrayDescriptor.createDescriptor("AGRE_DEFN_TABLE", oraConRef);

			serviceMapStrDesc=StructDescriptor.createDescriptor("SERVICE_DEFN_TYPE", oraConRef);
			serviceMapArrDesc=ArrayDescriptor.createDescriptor("SERVICE_DEFN_TABLE", oraConRef);

			agrEntityArrDesc=ArrayDescriptor.createDescriptor("ENTITY_ID_TYPE",oraConRef);


		}catch (SQLException t) {
			logger.error("Exception in insert agreement [Could not create Struct/Array] : "	+ t);
			throw new AgreementSystemException(IErrorCodes.DB_DESCRIPTOR_ERROR,
					"Could not create db Descriptors", t);
		}

		try {

			ARRAY serviceMapArr=null;
			ARRAY agrArr=null,agrDelArr=null;
			String [] serviceStr;
			String str1;

			// make service object for service_map_defn_table 

			String services=acb.getSelectedServices();	
			serviceStr=createStrings(services);

			int countStr=serviceStr.length;
			Object [] serviceObjTable=new Object[countStr];

			if(serviceStr!=null)
			{
				for (int k=0;k<countStr;k++)
				{
					str1=serviceStr[k];		   
					Object serviceObject []={acb.getVersionNo(),acb.getInternalAgreementId(),str1};		 	   	 
					STRUCT serviceStruct=new STRUCT(serviceMapStrDesc, oraConRef, serviceObject);
					serviceObjTable[k] = serviceStruct; 	  
				}
				serviceMapArr=new ARRAY(serviceMapArrDesc,oraConRef,serviceObjTable);  
			}


			Object[] agrCommonObj = {acb.getInternalAgreementId()
					,acb.getVersionNo()			          //acb.getVersionNo() 
					,acb.getAgreementTitle()           
					,acb.getPrimaryContact()
					,acb.getStreetAddress()
					,acb.getZipCode()
					,acb.getCity()
					,acb.getCountry()
					,acb.getEmail()
					,acb.getTelephone() 
					,null
					,parseDate(acb.getValidFrom())
					,parseDate(acb.getValidTo())
					,acb.getSecurityMethod()
					,"A"                                    //acb.getStatus()
					,acb.getCreatedBy()                     //acb.getCreatedBy()
					,null                                   //acb.getCreatedOn()
					,userId                                 //acb.getLastUpdatedBy()
					,businessDate                           //acb.getLastUpdatedOn() 
					,userId                                 //acb.getAuthorisedBy()
					,businessDate                           //acb.getAuthorisedOn()
			};
			// Set up the struct object														
			try{

				STRUCT agrStruct = new STRUCT(agrStrDesc, oraConRef, agrCommonObj);

				Object[] agrArrobj = new Object[1];
				agrArrobj[0]= agrStruct;

				Object[] agrDelListObj=new Object[1];
				agrDelListObj[0]="";

				agrArr = new ARRAY(agrArrDesc, oraConRef, agrArrobj);
				agrDelArr = new ARRAY(agrEntityArrDesc,oraConRef,agrDelListObj);

			}catch(RuntimeException r){
				logger.debug("Run time Exception Exception "+r);
			}				
			//Prepare the OracleCallableStatement
			ocs = (OracleCallableStatement)
			oraConRef.prepareCall("{? = call opepk_agreement.fn_agreement(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");

			/** Bind inputs and register outputs*/
			ocs.registerOutParameter(1, OracleTypes.NUMERIC);
			ocs.setString(2, userId);
			ocs.setString(3, action);
			ocs.setDate(4, (java.sql.Date) businessDate);                  //business date
			ocs.setString(5, acb.getInternalAgreementId());                //agreement ID
			ocs.setString(6, null);                                        //customer name
			ocs.setInt(7,1);                                               //version number
			ocs.setARRAY(8,agrDelArr);                                     //entity_id_type
			ocs.setARRAY(9,agrArr);                                        //agre_defn_table
			ocs.setARRAY(10, serviceMapArr);                                //SERVICE_DEFN_TABLE

			ocs.registerOutParameter(11, OracleTypes.ARRAY, "AGRE_DEFN_TABLE");
			ocs.registerOutParameter(12, OracleTypes.ARRAY, "SERVICE_DEFN_TABLE");

			ocs.registerOutParameter(13, OracleTypes.VARCHAR);		
			ocs.registerOutParameter(14,OracleTypes.VARCHAR);
			ocs.registerOutParameter(15,OracleTypes.VARCHAR);

			/** Execute the procedure */
			ocs.execute();
			res = ocs.getInt(1);

			logger.debug("RETURN : " + res);
			logger.debug("ERROR CODE : " + ocs.getString(14));
			logger.debug("ERROR DESC : " + ocs.getString(15));

			if(res==0) {
				oraConRef.rollback();
				if(ocs.getString(14)!=null){
					throw new AgreementBusinessException(ocs.getString(14),"Procedure Failed to Edit");
				}else{
					throw new AgreementBusinessException(IErrorCodes.AGREEMENT_EDIT_ERROR,
					"Procedure Failed to Edit");
				}				
			}

			// Return the result to the Agreement Action
			if (res == 1) {
				oraConRef.commit();
				ret = true;
			} else {
				oraConRef.rollback();
				ret = false;
			}	
			return ret;

		}catch (SQLException t) {
			logger.error("Exception in edit agreement : " + t);			
			throw new AgreementSystemException(IErrorCodes.AGREEMENT_EDIT_ERROR,
					"Failed to edit Record for agreement", t);
		}catch(RuntimeException re) {
			re.printStackTrace();
			throw new AgreementBusinessException(IErrorCodes.TYPECONVERSION_ERROR,
			"Exception while creating Object Array");
		}
		finally
		{
			// connection is closing here
			SQLUtils.ensureClosedConnection(con,ocs,oraConRef);
		}			
	}


	/** 
	 * Method is used to search show versions of agreement 
	 * Agreement common bean, action, agreementbean[] as parameter
	 */

	public List showVersion(AgreementCommonBean acb, String action)
	throws AgreementSystemException, AgreementBusinessException
	{

		OracleConnection oraConRef=null;
		Connection con=null;
		OracleCallableStatement ocs=null;
		StructDescriptor agrStrDesc,serviceMapStrDesc;
		ArrayDescriptor agrArrDesc,agrEntityArrDesc,serviceMapArrDesc;
		int res;
		List ret=null;

		try {
			con = SQLUtils.getConnection(CommonConstants.DNBDatabase);
			oraConRef = DescriptorUtility.getOracleConnection(con);			
		} catch (SQLException sq) {
			logger.error("Could not obtain OracleConnection");
			throw new AgreementSystemException(IErrorCodes.CONNECTION_RETRIEVAL_ERROR,
					"Unable to obtain OracleConnection", sq);
		}

		try{

			agrStrDesc=StructDescriptor.createDescriptor("AGRE_DEFN_TYPE", oraConRef);
			agrArrDesc=ArrayDescriptor.createDescriptor("AGRE_DEFN_TABLE", oraConRef);

			serviceMapStrDesc=StructDescriptor.createDescriptor("SERVICE_DEFN_TYPE", oraConRef);
			serviceMapArrDesc=ArrayDescriptor.createDescriptor("SERVICE_DEFN_TABLE", oraConRef);

			agrEntityArrDesc=ArrayDescriptor.createDescriptor("ENTITY_ID_TYPE",oraConRef);

		}catch (SQLException t) {
			logger.error("Exception in search agreement [Could not create Struct/Array] : "	+ t);
			throw new AgreementSystemException(IErrorCodes.DB_DESCRIPTOR_ERROR,
					"Could not create db Descriptors", t);
		} 

		try {
			ARRAY serviceMapArr=null;
			ARRAY agrArr=null,agrDelArr=null;

			//  service object table array
			Object [] serviceObjTable=new Object[1];
			Object serviceObject []={null,null,null};

			STRUCT serviceStruct= new STRUCT(serviceMapStrDesc, oraConRef, serviceObject);
			serviceObjTable[0] = serviceStruct;    
			serviceMapArr=new ARRAY(serviceMapArrDesc,oraConRef,serviceObjTable);

			//  agrCommonObj for agreement defnition table

			Object[] agrCommonObj = {null,null,null,null,null,null,
					                 null,null,null,null,null,null,
					                 null,null,null,null,null,null,
					                 null,null,null
			};                

			STRUCT agrStruct = new STRUCT(agrStrDesc, oraConRef, agrCommonObj);

			Object[] agrArrobj = new Object[1];
			agrArrobj[0]= agrStruct;

			Object[] agrDelListObj=new Object[1];
			agrDelListObj[0]="";

			agrArr = new ARRAY(agrArrDesc, oraConRef, agrArrobj);
			agrDelArr = new ARRAY(agrEntityArrDesc,oraConRef,agrDelListObj);


			//Prepare the OracleCallableStatement
			ocs = (OracleCallableStatement)
			oraConRef.prepareCall("{? = call opepk_agreement.fn_agreement(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");

			/** Bind inputs and register outputs*/
			ocs.registerOutParameter(1, OracleTypes.NUMERIC);
			ocs.setString(2, "");
			ocs.setString(3, action);
			ocs.setDate(4, null);                                           //business date	
			ocs.setString(5, acb.getInternalAgreementId());                 //internal agreement ID
			ocs.setString(6, acb.getAgreementTitle());                      //AgreementTitle
			ocs.setInt(7,Integer.parseInt(acb.getVersionNo()));             //version number
			ocs.setARRAY(8,agrDelArr);                                      //entity_id_type
			ocs.setARRAY(9,agrArr);                                         //agre_defn_table
			ocs.setARRAY(10, serviceMapArr);                                //service_map_defn_table

			ocs.registerOutParameter(11, OracleTypes.ARRAY, "AGRE_DEFN_TABLE");
			ocs.registerOutParameter(12, OracleTypes.ARRAY, "SERVICE_DEFN_TABLE");

			ocs.registerOutParameter(13, OracleTypes.VARCHAR);	
			ocs.registerOutParameter(14,OracleTypes.VARCHAR);
			ocs.registerOutParameter(15,OracleTypes.VARCHAR);

			/** Execute the procedure */
			ocs.execute();										 
			res = ocs.getInt(1);

			logger.debug("RETURN : " + res);
			logger.debug("ERROR CODE : " + ocs.getString(14));
			logger.debug("ERROR DESC : " + ocs.getString(15));

			if(res==0) {
				oraConRef.rollback();
				if(ocs.getString(14)!=null){
					throw new AgreementBusinessException(ocs.getString(14),"Procedure Failed to show version");
				}else{
					throw new AgreementBusinessException(IErrorCodes.AGREEMENT_VERSION_ERROR,
					"Procedure Failed to show version");
				}				
			}

			//set up the array object to accept data

			ARRAY attributes = ocs.getARRAY(11);  
			Object[] agrObj = (Object[])attributes.getArray();

			List agrList = makeListOfAgreement(agrObj,ocs.getString(13));


			ARRAY serviceAttributes=ocs.getARRAY(12);
			Object[] serviceMapObj=(Object[]) serviceAttributes.getArray();

			List serviceMapList=makeListOfServiceMap(serviceMapObj, ocs.getString(13));	

			List lst=new ArrayList();

			lst.add(0, agrList);
			lst.add(1, serviceMapList);

			//	Return the result to the Agreement Search Action

			if(res == 1) {
				oraConRef.commit();
				ret = lst;
			} else {
				oraConRef.rollback();
				ret = null;
			}						
			return ret;

		} catch (SQLException t) {
			logger.debug("Exception in agreement show version  : " + t);
			throw new AgreementSystemException(IErrorCodes.AGREEMENT_VERSION_ERROR,
					"Failed to Fetch records for agreement", t);
		} catch(RuntimeException re) {
			throw new AgreementBusinessException(
					IErrorCodes.TYPECONVERSION_ERROR,
			"Exception while creating Object Array");
		} finally{
			// connection is closing here
			SQLUtils.ensureClosedConnection(con,ocs,oraConRef);
		}
	}

	/**
	 * this method makes list of services, customer ids, account numbers
	 * for specific agreement, used while printing agreement
	 * @param object[]
	 * @param string
	 * @return list
	 * @throws AgreementSystemException
	 * @throws AgreementBusinessException
	 */

	public List print(AgreementCommonBean bean) throws AgreementSystemException, AgreementBusinessException 
	{
		OracleConnection oraConRef=null;
		Connection con=null;
		OracleCallableStatement ocs=null;
		int res;
		List ret=null;

		try {
			con = SQLUtils.getConnection(CommonConstants.DNBDatabase);
			oraConRef = DescriptorUtility.getOracleConnection(con);			
		} catch (SQLException sq) {
			logger.error("Could not obtain OracleConnection");
			throw new AgreementSystemException(IErrorCodes.CONNECTION_RETRIEVAL_ERROR,
					"Unable to obtain OracleConnection", sq);
		}

		// Prepare the OracleCallableStatement
		try {
			ocs = (OracleCallableStatement)
			oraConRef.prepareCall("{? = call opepk_agreement.fn_agreement_print(?,?,?,?,?,?,?,?,?,?,?)}");

			/** Bind inputs and register outputs*/
			ocs.registerOutParameter(1, OracleTypes.NUMERIC);
			ocs.setString(2,bean.getInternalAgreementId());  //Agreement Id
			ocs.setString(3,bean.getVersionNo());

			ocs.registerOutParameter(4, OracleTypes.ARRAY, "AGRE_DEFN_TABLE");
			ocs.registerOutParameter(5, OracleTypes.ARRAY, "SERVICE_DEFN_TABLE");
			ocs.registerOutParameter(6, OracleTypes.ARRAY, "BUREAU_MAP_TABLE");
			ocs.registerOutParameter(7, OracleTypes.ARRAY, "CUST_MAP_TABLE");
			ocs.registerOutParameter(8, OracleTypes.ARRAY, "ACCOUNT_MAP_TABLE");
			ocs.registerOutParameter(9, OracleTypes.ARRAY, "BUREAU_TABLE");
			ocs.registerOutParameter(10,OracleTypes.ARRAY, "KEK_AUK_DEFN_TABLE");

			ocs.registerOutParameter(11,OracleTypes.VARCHAR);
			ocs.registerOutParameter(12,OracleTypes.VARCHAR);

			/** Execute the procedure */
			ocs.execute();										 
			res = ocs.getInt(1);

			logger.debug("RETURN : " + res);
			logger.debug("ERROR CODE : " + ocs.getString(11));
			logger.debug("ERROR DESC : " + ocs.getString(12));

			if(res==0) {
				oraConRef.rollback();
				if(ocs.getString(11)!=null){
					throw new AgreementBusinessException(ocs.getString(11),"Procedure Failed to get Agreement Records");
				}else{
					throw new AgreementBusinessException(IErrorCodes.AGREEMENT_VERSION_ERROR,
					"Procedure Failed to get Agreement Records");
				}				
			}

			ARRAY agreementAttribute = ocs.getARRAY(4);  
			Object[] agrObj = (Object[])agreementAttribute.getArray();									
			List agreementList = makeListOfAgreement(agrObj,"");

			ARRAY serviceAttributes=ocs.getARRAY(5);
			Object[] serviceMapObj=(Object[]) serviceAttributes.getArray();				
			List serviceMapList=makeListOfServiceMap(serviceMapObj, "");

			ARRAY serviceSpecificAttributes=ocs.getARRAY(6);
			Object[] serviceSpecificObj=(Object[]) serviceSpecificAttributes.getArray();				
			List serviceSpecificList=makeListOfServiceSpecification(serviceSpecificObj, "");

			ARRAY customerAttributes=ocs.getARRAY(7);
			Object[] customerObject=(Object[]) customerAttributes.getArray();
			List customerList=makeListOfCustomerMap(customerObject, "");

			ARRAY accountAttributes=ocs.getARRAY(8);
			Object[] accountObject=(Object[]) accountAttributes.getArray();				
			List accountList=makeListOfAccounts(accountObject, "");

			ARRAY serviceBureauAttributes=ocs.getARRAY(9);
			Object[] serviceBureauObj=(Object[]) serviceBureauAttributes.getArray();				
			List serviceBureauList=makeListOfServiceBureaus(serviceBureauObj, "");

			ARRAY kekAukAttributes=ocs.getARRAY(10);
			Object[] kekAukObj=(Object[]) kekAukAttributes.getArray();				
			List kekAukList=makeListOfKekAuk(kekAukObj, "");


			List lst=new ArrayList();						

			lst.add(0, agreementList);
			lst.add(1, serviceMapList);
			lst.add(2, serviceSpecificList);
			lst.add(3, customerList);
			lst.add(4, accountList);
			lst.add(5, serviceBureauList);
			lst.add(6, kekAukList);

			if(res == 1) {
				oraConRef.commit();
				ret = lst;
			}else {
				oraConRef.rollback();
				ret = null;
			}						
			return ret;


		} catch (SQLException t) {
			logger.debug("Exception in agreement Print : " + t);
			throw new AgreementSystemException(IErrorCodes.AGREEMENT_VERSION_ERROR,
					"Failed to Fetch records for agreement", t);
		} catch(RuntimeException re) {
			throw new AgreementBusinessException(
					IErrorCodes.TYPECONVERSION_ERROR,"Exception while creating Object Array");
		} finally{
			SQLUtils.ensureClosedConnection(con,ocs,oraConRef);
		}

	}
	
	/**
	 * this method is used to clear all the agreements details
	 */
	public int clear(AgreementCommonBean acb) 
	throws AgreementSystemException, AgreementBusinessException 
	{
		OracleConnection oraConRef=null;
		Connection con=null;
		OracleCallableStatement ocs=null;
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
			ocs=(OracleCallableStatement)
			oraConRef.prepareCall("{? = call opepk_agreement.fn_agreement_clear(?,?,?)}");

			// bid the input out put of the procedure

			ocs.registerOutParameter(1, OracleTypes.NUMERIC);
			ocs.setString(2, acb.getInternalAgreementId());
			ocs.registerOutParameter(3, OracleTypes.VARCHAR); // error code 
			ocs.registerOutParameter(4, OracleTypes.VARCHAR); // error message

			// executing procedure

			ocs.execute();
			res = ocs.getInt(1);

			logger.debug("RETURN : " + res);
			logger.debug("ERROR CODE : " + ocs.getString(3));
			logger.debug("ERROR DESC : " + ocs.getString(4));

			if(res==0){
				oraConRef.rollback();
				if(ocs.getString(3)!=null){
					throw new AgreementBusinessException(ocs.getString(3),"Procedure Failed to clear Agreement");
				}else{
					throw new AgreementBusinessException(IErrorCodes.AGREEMENT_DELETE_ERROR,
					"Procedure Failed to clear Agreement");
				}				
			}

			if(res==1)
			{
				oraConRef.commit();
			}
			else 
			{
				oraConRef.rollback();
			}

			return res;

		}catch (SQLException t) {
			logger.error("Exception in clear Agreement : " + t);			
			throw new AgreementSystemException(IErrorCodes.AGREEMENT_DELETE_ERROR,
					"Failed to clear Agreements data", t);
		}finally{
			SQLUtils.ensureClosedConnection(con,ocs,oraConRef);
		}		 	
		
	}

	/**
	 * this method makes list of Agreement main details 
	 * @param object[]
	 * @param string
	 * @return list
	 * @throws AgreementSystemException
	 * @throws AgreementBusinessException
	 */

	private List makeListOfAgreement(Object[] obj,String version_pos)throws AgreementSystemException,
	AgreementBusinessException 

	{     

		List list = new ArrayList();			
		for (int i = 0; i < obj.length; i++) 
		{
			try {
				STRUCT attrObject = (STRUCT)obj[i];
				Object[] strt = attrObject.getAttributes();
				AgreementDTO ed=new AgreementDTO();

				ed.setInternalAgreementId((String)strt[0]); 

				if(strt[1]!=null){
					ed.setVersionNo(strt[1].toString());
				} else
					ed.setVersionNo("");

				ed.setAgreementTitle((String)strt[2]);
				ed.setPrimaryContact((String)strt[3]);
				ed.setStreetAddress((String)strt[4]);

				ed.setZipCode((String)strt[5]);
				ed.setCity((String)strt[6]);
				ed.setCountry((String)strt[7]);
				ed.setEmail((String)strt[8]);
				ed.setTelephone((String)strt[9]);

				if(strt[11]!=null){
					ed.setValidFrom(strt[11].toString());		    	
				}else
					ed.setValidFrom("");

				if(strt[12]!=null){
					ed.setValidTo(strt[12].toString());		    	
				}else
					ed.setValidTo("");

				ed.setSecurityMethod((String)strt[13]);
				ed.setStatus((String)strt[14]);
				ed.setCreatedBy((String)strt[15]);

				if(strt[16]!=null){
					ed.setCreatedOn(strt[16].toString());
				}else 
					ed.setCreatedOn("");

				ed.setLastUpdatedBy((String)strt[17]);

				if(strt[18]!=null){
					ed.setLastUpdatedOn(strt[18].toString());
				}else 
					ed.setLastUpdatedOn("");
				
				ed.setAuthorisedBy((String)strt[19]);

				if(strt[20]!=null){
					ed.setAuthorisedOn(strt[20].toString());
					
				}else 
					ed.setAuthorisedOn("");
				ed.setVersionPos(version_pos);
				
				// Add Object to list
				list.add(i,ed);		
			} catch (Exception e){
				logger.error("Exception in Create list" + e);
				throw new AgreementSystemException(IErrorCodes.AGREEMENT_MAKELIST_ERROR,
						"Failed to Fetch Records", e);
			}
		}	

		return list;
	}


	/**
	 * this method makes list of Agreements  customer id informations
	 * @param object[]
	 * @param string
	 * @return list
	 * @throws AgreementSystemException
	 * @throws AgreementBusinessException
	 */
	private List makeListOfCustomerMap(Object[] obj,String version_pos)throws AgreementSystemException,
	AgreementBusinessException 
	{	
		List list = new ArrayList();
		Set customerIdSet = new HashSet();

		for(int i=0;i< obj.length; i++)
		{
			try
			{
				STRUCT attrObject = (STRUCT)obj[i];
				Object[] strt = attrObject.getAttributes();
				//ServiceSpecificDTO ed=new ServiceSpecificDTO();
				//Added by Debadatta Mishra for the new change request
				
				ServiceCustomerIdSpecificDTO ed = new ServiceCustomerIdSpecificDTO();

				if(strt[0]!=null){
					ed.setVersionNo(strt[0].toString());
				} else{
					ed.setVersionNo("");
				}
				ed.setInternalRefId((String)strt[1]);
				ed.setCustomerId((String)strt[2]);
				ed.setCustomerName((String)strt[3]);

				ed.setVersionPos(version_pos);
				//customerIdSet.add(ed);
				list.add(i, ed);	

			}catch (Exception e){
				logger.error("Exception in Create list" + e);
				throw new AgreementSystemException(IErrorCodes.AGREEMENT_MAKELIST_ERROR,
						"Failed to Fetch Records", e);
			}
		}	
		//list = new ArrayList(customerIdSet);
		return list;
	}



	/**
	 * this makes list of Services for single agreement
	 * @param object[]
	 * @param string
	 * @return list
	 * @throws AgreementSystemException
	 * @throws AgreementBusinessException
	 */
	private List makeListOfServiceMap(Object[] obj,String version_pos)throws AgreementSystemException,
	AgreementBusinessException
	{		
		List list = new LinkedList();//ArrayList();
		Map tempMap = new HashMap();
		for(int i=0; i<obj.length; i++)
		{
			try
			{
				STRUCT attrObject = (STRUCT)obj[i];
				Object[] strt = attrObject.getAttributes();
				ServiceSpecificDTO ed = new ServiceSpecificDTO();

				if(strt[0]!=null){
					ed.setVersionNo(strt[0].toString());
				} else{
					ed.setVersionNo("");
				}
				ed.setInternalAgreementId((String)strt[1]);
				ed.setServiceCode((String)strt[2]);
				ed.setVersionPos(version_pos);
			
				tempMap.put((String)strt[2], ed);
				
//				list.add(i, ed);
			}catch (Exception e){
				logger.error("Exception in Create list" + e);
				throw new AgreementSystemException(IErrorCodes.SERVICE_SPECIFIC_MAKELIST_ERROR,
						"Failed to Fetch Records", e);
			}
		}
		addToList(tempMap, list);
		return list;
	}
	
	/**Method used to create a sequential list
	 * containing the service code in the
	 * sequential manner.
	 * @author Debadatta Mishra
	 * @param map of type {@link Map}
	 * @param list of type {@link List}
	 */
	private void addToList( Map map , List list )
	{
		
		if( map.get("S001") != null )
			list.add(map.get("S001"));
		if( map.get("S003") != null )
			list.add(map.get("S003"));
		if( map.get("S005") != null )
			list.add(map.get("S005"));
		if( map.get("S002") != null )
			list.add(map.get("S002"));
		if( map.get("S009") != null )
			list.add(map.get("S009"));
		if( map.get("S004") != null )
			list.add(map.get("S004"));
		if( map.get("S006") != null )
			list.add(map.get("S006"));
		if( map.get("S007") != null )
			list.add(map.get("S007"));
		if( map.get("S011") != null )
			list.add(map.get("S011"));
		if( map.get("S008") != null )
			list.add(map.get("S008"));
		if( map.get("S010") != null )
			list.add(map.get("S010"));
	}

	/** 
	 * Method returns List of Service specifcation Details
	 * @param object[]
	 * @param String 
	 * @return List
	 * @throws AgreementSystemException
	 * @throws AgreementBusinessException
	 */

	private List makeListOfServiceSpecification(Object[] obj,String version_pos)throws AgreementSystemException,
	AgreementBusinessException
	{		

		List list=new ArrayList();

		for(int i=0; i<obj.length; i++)
		{
			try
			{
				STRUCT attrObject = (STRUCT)obj[i];
				Object[] strt = attrObject.getAttributes();
				ServiceSpecificDTO ed = new ServiceSpecificDTO();

				if(strt[0]!=null){
					ed.setVersionNo(strt[0].toString());
				} else{
					ed.setVersionNo("");
				}
				ed.setInternalRefId((String)strt[1]);
				ed.setServiceId((String)strt[2]);
				ed.setServiceCode((String)strt[3]);
				ed.setBureauId((String)strt[4]);
				ed.setBureauName((String)strt[5]);
				ed.setInternalAgreementId((String)strt[6]);
				ed.setSuo((String)strt[7]);

				list.add(i, ed);

			}catch (Exception e){
				logger.error("Exception in Create list" + e);
				throw new AgreementSystemException(IErrorCodes.SERVICE_SPECIFIC_MAKELIST_ERROR,
						"Failed to Fetch Records", e);
			}

		}	

		return list;
	}

	/** 
	 * Method returns List of Service specifcation account list
	 * @param object[]
	 * @param String 
	 * @return List
	 * @throws AgreementSystemException
	 * @throws AgreementBusinessException
	 */

	private List makeListOfAccounts(Object[] obj,String version_pos)throws AgreementSystemException,
	AgreementBusinessException
	{		
//		Set accountSet = new HashSet();
    	List list=new ArrayList();
		
//		List list = null;
		

		for(int i=0; i<obj.length; i++)
		{
			try
			{
				STRUCT attrObject = (STRUCT)obj[i];
				Object[] strt = attrObject.getAttributes();
//				ServiceSpecificDTO ed = new ServiceSpecificDTO();
				//Added by Debadatta Mishra for the new change request.
				ServiceAccountSpecificDTO ed = new ServiceAccountSpecificDTO();

				if(strt[0]!=null){
					ed.setVersionNo(strt[0].toString());
				} else{
					ed.setVersionNo("");
				}

				ed.setInternalRefId((String)strt[1]);
				ed.setAccountNum((String)strt[2]);
				ed.setAccountName((String)strt[3]);
				
//				accountSet.add(ed);
				list.add(i, ed);

			}catch (Exception e){
				logger.error("Exception in Create list" + e);
				throw new AgreementSystemException(IErrorCodes.SERVICE_SPECIFIC_MAKELIST_ERROR,
						"Failed to Fetch Records", e);
			}

		}
//		list = new ArrayList( accountSet );
		return list;
	}

	/** 
	 * Method returns List of Service Bureaus details
	 * @param object[]
	 * @param String 
	 * @return List
	 * @throws AgreementSystemException
	 * @throws AgreementBusinessException 
	 */	

	private List makeListOfServiceBureaus(Object[] obj,String version_pos)throws AgreementSystemException,
	AgreementBusinessException
	{	
		List list = null;//new ArrayList();
		Set customerIdSet = new HashSet();
		for(int i=0; i<obj.length; i++)
		{
			try
			{
				STRUCT attrObject = (STRUCT)obj[i];
				Object[] strt = attrObject.getAttributes();
//				ServiceBureauBean ed = new ServiceBureauBean();
				
				//Added by Debadatta Mishra for the new change request.
				ServiceBureauPatuIdSpecificBean ed = new ServiceBureauPatuIdSpecificBean();

				if(strt[0]!=null){
					ed.setVersionNo(strt[0].toString());
				} else{
					ed.setVersionNo("");
				}

				ed.setInternalBureauId((String)strt[1]);
				ed.setBureauId((String)strt[2]);
				ed.setBureauName((String)strt[3]);
				ed.setBureauDescription((String)strt[4]);
				ed.setCompanyName((String)strt[5]);       // PATU user id
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
				
				customerIdSet.add(ed);
//				list.add(i, ed);

			}catch (Exception e){
				logger.error("Exception in Create list" + e);
				throw new AgreementSystemException(IErrorCodes.SERVICE_BUREAU_MAKELIST_ERROR,
						"Failed to Fetch Records", e);
			}

		}	
		list = new ArrayList(customerIdSet);
		return list;
	}

	/** 
	 * Method returns List of Kek and Auk keys details
	 * @param object[]
	 * @param String 
	 * @return List
	 * @throws AgreementSystemException
	 * @throws AgreementBusinessException
	 */	

	private List makeListOfKekAuk(Object[] obj,String version_pos)throws AgreementSystemException,
	AgreementBusinessException
	{		

		List list = new ArrayList();

		for(int i=0; i<obj.length; i++)
		{
			try
			{
				STRUCT attrObject = (STRUCT)obj[i];
				Object[] strt = attrObject.getAttributes();
				ServiceBureauBean ed = new ServiceBureauBean();

				ed.setInternalBureauId((String)strt[1]);

				if(strt[2]!=null){
					ed.setKeyKEKPart1((String)strt[2]);
				}else
					ed.setKeyKEKPart1("");

				if(strt[3]!=null){
					ed.setKeyKEKPart2((String)strt[3]);
				}else
					ed.setKeyKEKPart2("");

				if(strt[4]!=null){
					ed.setKeyKEK((String)strt[4]);	
				}else
					ed.setKeyKEK("");

				if(strt[5]!=null){
					ed.setGenerationNumber(String.valueOf(strt[5]));
				}else
					ed.setGenerationNumber("0");


				ed.setStatus((String)strt[6]);

				if(strt[7]!=null){
					ed.setKeyKVV((String)strt[7]);
				}else
					ed.setKeyKVV("");

				if(strt[8]!=null){
					ed.setGenerationDate(strt[8].toString());
				}else 
					ed.setGenerationDate("");

				if(strt[9]!=null){
					ed.setKeyExpireDate(strt[9].toString());
				}else 
					ed.setKeyExpireDate("");

				list.add(i, ed);

			}catch (Exception e){
				logger.error("Exception in Create list..." + e);
				throw new AgreementSystemException(IErrorCodes.SERVICE_BUREAU_MAKELIST_ERROR,
						"Failed to Fetch Records", e);
			}

		}		

		return list;
	}


	/** 
	 * Method returns object[] of record from StringTokenizer
	 * @param string 
	 * @return object[]
	 * @throws AgreementSystemException
	 * @throws AgreementBusinessException
	 */	
	private Object[] createObject(String ids)throws AgreementSystemException,
	AgreementBusinessException {

		int i=0;
		Object[] loadAll=null;

		try	{
			StringTokenizer token = new StringTokenizer(ids,",");
			loadAll=new Object[getLength(ids)];

			while (token.hasMoreTokens()) 
			{
				loadAll[i]=token.nextToken();
				i++;	
			}
			return loadAll;
		}
		catch(ArrayIndexOutOfBoundsException e) {
			logger.error("Exception in createObject : "+e);
			throw new AgreementSystemException(IErrorCodes.RECORD_CREATEOBJECT_ERROR,
					"Failed to Delete Records", e);
		}
	}


	/**
	 * this method returns string[] from string tokenizer for selected services 
	 * @param string 
	 * @return string[]
	 * @throws AgreementSystemException
	 * @throws AgreementBusinessException
	 */
	private String[] createStrings(String ids)throws AgreementSystemException,
	AgreementBusinessException{				
		int i=0;
		String[] allStr=null;
		try	{
			StringTokenizer token = new StringTokenizer(ids,",");
			allStr=new String[getLength(ids)];					   
			while (token.hasMoreTokens()) 
			{
				allStr[i]=token.nextToken();
				i++;	

			}
			return allStr;
		}
		catch(ArrayIndexOutOfBoundsException e) {
			logger.error("Exception in create String : "+e);
			throw new AgreementSystemException(IErrorCodes.RECORD_CREATEOBJECT_ERROR,
					"Failed to Delete Records", e);
		}		
	}
	/**
	 * this method returns number of string tokens  from string  
	 * @param string 
	 * @return int
	 */

	private int getLength(String ids) {
		return ids.split(",").length;
	}

	/**
	 * this method returns SQL Date from string parameter 
	 * @param string 
	 * @return SQL Date
	 */

	private java.sql.Date parseDate(String dt)
	{
		Date parsedDate=null;
		if(dt!=null )
		{
			if(dt.length()!=0){
				try{
					SimpleDateFormat sdf=new SimpleDateFormat(OPEConstants.DATE_FORMAT); //Date format for parsing the date
					parsedDate= sdf.parse(dt);
				} 
				catch(java.text.ParseException pe){
					logger.debug("Unable to parse the date");
				}

				return new java.sql.Date(parsedDate.getTime());
			}else
			{
				return null;
			}
		}
		else{
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see com.dnb.agreement.DAO.AgreementDAO#getAccountDetails(java.lang.String, java.lang.String)
	 */
	
	public String getAccountDetails(String accountNo, String queryString) throws SQLException {
		String acctDetails = null;
		  Connection con = null;
		  PreparedStatement prestmt = null;
		  ResultSet rset = null;
		  try
		  {
			  con = SQLUtils.getConnection(CommonConstants.DNBDatabase);
			 
			  prestmt = con.prepareStatement(queryString);
			  prestmt.setString(1, accountNo);
			  rset = prestmt.executeQuery();
			  System.out.println("ResultSet--------->>>"+rset);
			  if( rset != null )
			  {
				  while( rset.next() )
				  {
					  acctDetails = rset.getString(1);
					  System.out.println("OracleAgreementDAO : getAccountDetails - accountDetails------>>>"+acctDetails);
				  }
			  }
			  
		  }
		  catch( SQLException se )
		  {
			  se.printStackTrace();
			  throw se;
		  }
		  catch( Exception e )
		  {
			  e.printStackTrace();
		  }
		  finally
		  {
			 try
			 {
				if( rset != null )
					rset.close();
				if( prestmt != null )
					prestmt.close();
			 }
			 catch(Exception e )
			 {
				 e.printStackTrace();
			 }
		  }
		  return acctDetails;
	}

	/* (non-Javadoc)
	 * @see com.dnb.agreement.DAO.AgreementDAO#getCustomerIdDetails(java.lang.String, java.lang.String)
	 */
	
	public CustomerIdBean getCustomerIdDetails(String custId, String queryString) throws SQLException
	{
		 CustomerIdBean custIdBean = null;//new CustomerIdBean();
		  Connection con = null;
		  PreparedStatement prestmt = null;
		  ResultSet rset = null;
		  try
		  {
			  con = SQLUtils.getConnection(CommonConstants.DNBDatabase);
			  prestmt = con.prepareStatement(queryString);
			  prestmt.setString(1, custId);
			  rset = prestmt.executeQuery();

			  if( rset != null )
			  {
				  while( rset.next() )
				  {
					  String fullName = rset.getString(1);
					  String address = rset.getString(2);
					  System.out.println("OracleAgreementDAO : getCustomerIdDetails - fullName------>>>"+fullName);
					  System.out.println("OracleAgreementDAO : getCustomerIdDetails - address------>>>"+address);
					  custIdBean = new CustomerIdBean();
					  custIdBean.setCustomerName(fullName);
					  custIdBean.setAddress(address);
				  }
			  }
		  }
		  catch( SQLException se )
		  {
			  se.printStackTrace();
		  }
		  catch( Exception e )
		  {
			  e.printStackTrace();
		  }
		  finally
		  {
			  try
			  {
				  if( rset != null )
						rset.close();
					if( prestmt != null )
						prestmt.close();
			  }
			  catch( Exception e )
			  {
				  e.printStackTrace();
			  }
		  }
		  return custIdBean;
	}

	
}
