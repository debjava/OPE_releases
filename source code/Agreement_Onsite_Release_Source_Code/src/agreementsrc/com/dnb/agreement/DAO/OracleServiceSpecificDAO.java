/********************************************************************************************
 * Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                        *
 * IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.       *
 * The Program or any portion thereof may not be reproduced in any form whatsoever except    *
 * as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd. *
 *																							 *
 * File Name                   : OracleServiceSpecificDAO.java                               *
 * Author                      : Anantaraj S                                                 *
 * Creation Date               : 06-Aug-2008                                                 * 
 * Description                 : This file serves as the DAO Class, which contains all       *
 *                               the methods which are used for Service specification.       *                                            
 * Modification History        :                                                             *
 *																						     *
 *     Version No.               Date               Brief description of change              *
 *  ---------------------------------------------------------------------------------------  *
 *                       |                  |											     *
 *                       |                  |											     *
 *  ---------------------------------------------------------------------------------------  *
 *********************************************************************************************/

package com.dnb.agreement.DAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleTypes;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;

import com.dnb.agreement.DTO.ServiceSpecificDTO;
import com.dnb.agreement.bean.ServiceSpecificAccountBean;
import com.dnb.agreement.bean.ServiceSpecificCommonBean;
import com.dnb.agreement.bean.ServiceSpecificCustomerBean;
import com.dnb.common.commons.CommonConstants;
import com.dnb.common.database.DescriptorUtility;
import com.dnb.agreement.exception.AgreementBusinessException;
import com.dnb.agreement.exception.AgreementSystemException;
import com.dnb.agreement.exception.IErrorCodes;
import com.dnb.agreement.utility.SQLUtils;


public class OracleServiceSpecificDAO implements ServiceSpecificDAO{

	Logger logger=Logger.getLogger(OracleServiceSpecificDAO.class);

	/** 
	 * Method is used to insert a new Service Specification record 
	 * Get ServiceSpecificCommonBean ,ServiceSpecificAccountBean, ServiceSpecificCustomerBean as parameter
	 */
	public int insert(ServiceSpecificAccountBean[] ssab,
			ServiceSpecificCustomerBean [] ssCustomerBean,
			ServiceSpecificCommonBean sscb,
			String userId, Date businessDate, String action)
	throws AgreementSystemException,AgreementBusinessException
	{

		OracleConnection oraConRef=null;
		Connection con=null;
		OracleCallableStatement ocs=null;
		StructDescriptor serviceBureauStrDesc,serviceAccountMapStrDesc,serviceCustomerMapStrDesc;
		ArrayDescriptor serviceBureauArrDesc,serviceAccountMapArrDesc,serviceCustomerMapArrDesc;
		int countRow=0,count=0;
		int res;
		int versionNo = 0;
		boolean ret=false;

		try {
			con = SQLUtils.getConnection(CommonConstants.DNBDatabase);
			oraConRef = DescriptorUtility.getOracleConnection(con);			
		} catch (SQLException sq) {
			logger.error("Could not obtain OracleConnection");
			throw new AgreementSystemException(IErrorCodes.CONNECTION_RETRIEVAL_ERROR,
					"Unable to obtain OracleConnection", sq);
		}

		try {
			serviceAccountMapStrDesc= StructDescriptor.createDescriptor("ACCOUNT_MAP_TYPE", oraConRef);
			serviceAccountMapArrDesc= ArrayDescriptor.createDescriptor("ACCOUNT_MAP_TABLE", oraConRef);

			serviceCustomerMapStrDesc= StructDescriptor.createDescriptor("CUST_MAP_TYPE", oraConRef);
			serviceCustomerMapArrDesc= ArrayDescriptor.createDescriptor("CUST_MAP_TABLE", oraConRef);

			serviceBureauStrDesc= StructDescriptor.createDescriptor("BUREAU_MAP_TYPE", oraConRef);
			serviceBureauArrDesc= ArrayDescriptor.createDescriptor("BUREAU_MAP_TABLE", oraConRef);

		} catch (SQLException t) {
			logger.error("Exception in insert service specification [Could not create Struct/Array] : "	+ t);
			throw new AgreementSystemException(IErrorCodes.DB_DESCRIPTOR_ERROR,
					"Could not create db Descriptors", t);			
		}


		try {
			ServiceSpecificAccountBean ssab2=null;
			ARRAY serviceAccountMapArr=null;
			ARRAY serviceArr=null;

			if(ssab[0].getAccountNum()!=null){
				logger.debug("When account exists");
				for(int i = 0; i < ssab.length; i++){
					countRow++;
				}						
				Object[] serviceObjTable = new Object[countRow];		

				// Make the object from the bean class for account information		 			   
				for (int j = 0; j < ssab.length; j++){
					ssab2 = (ServiceSpecificAccountBean) ssab[j];
					Object[] serviceAccountObject = {new Integer(1),
							sscb.getInternalRefId(),
							ssab2.getAccountNum(),
							ssab2.getAccountName()

					};			

					STRUCT serviceAccountStruct = new STRUCT(serviceAccountMapStrDesc, oraConRef, serviceAccountObject);
					serviceObjTable[j] = serviceAccountStruct;
				}	   
				serviceAccountMapArr = new ARRAY(serviceAccountMapArrDesc, oraConRef, serviceObjTable);		   		 
			}
			else
			{
				logger.debug("When no accounts ");
				/**
				 * else part will execute when user select Currency Exchange rate service
				 * it doesnot have account fields
				 */
				Object[] serviceObjTable = new Object[1];
				Object[] serviceAccountObject ={null,null,null,null};
				STRUCT serviceAccountStruct = new STRUCT(serviceAccountMapStrDesc, oraConRef, serviceAccountObject);
				serviceObjTable[0]=serviceAccountStruct;
				serviceAccountMapArr = new ARRAY(serviceAccountMapArrDesc, oraConRef, serviceObjTable);
				
			}

			ServiceSpecificCustomerBean ssCustomer2;
			ARRAY serviceCustomerMapArr=null;

			if(ssCustomerBean!=null){
				for(int i = 0; i < ssCustomerBean.length; i++){
					count++;
				}						
				Object[] serviceObjTable = new Object[count];		

				// Make the object from the bean class for customer Id and customer name		 			   
				for (int j = 0; j < ssCustomerBean.length; j++){
					ssCustomer2 = (ServiceSpecificCustomerBean) ssCustomerBean[j];
					Object[] serviceCustomerObject = {new Integer(1),
							sscb.getInternalRefId(),
							ssCustomer2.getCustomerId(),
							ssCustomer2.getCustomerName()

					};		


					STRUCT serviceCustomerStruct = new STRUCT(serviceCustomerMapStrDesc, oraConRef, serviceCustomerObject);
					serviceObjTable[j] = serviceCustomerStruct;
				}	   
				serviceCustomerMapArr = new ARRAY(serviceCustomerMapArrDesc, oraConRef, serviceObjTable);		   		 
			} 


			Object[] serviceObject={new Integer(1),
					sscb.getInternalRefId(),
					sscb.getServiceId(),
					sscb.getServiceCode(),
					sscb.getBureauId(),
					sscb.getBureauName(),
					sscb.getInternalAgreementId(),
					sscb.getSuo(),
					sscb.getStatus()               			 
			};


			STRUCT serviceStruct=new STRUCT(serviceBureauStrDesc,oraConRef,serviceObject);
			Object[] serviceArrObject = new Object[1];
			serviceArrObject[0]= serviceStruct;

			serviceArr=new ARRAY(serviceBureauArrDesc,oraConRef,serviceArrObject);

			ocs=(OracleCallableStatement)
			oraConRef.prepareCall("{? = call opepk_service_specific.fn_service_specific(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");

			// bind the inputs and output to procedure

			ocs.registerOutParameter(1, OracleTypes.NUMERIC);
			ocs.setString(2, userId);
			ocs.setString(3, action);
			ocs.setString(4, sscb.getServiceCode());
			ocs.setString(5, sscb.getInternalAgreementId());
			ocs.setInt(6, 1);
			ocs.setARRAY(7, serviceArr);
			ocs.setARRAY(8, serviceCustomerMapArr);
			ocs.setARRAY(9, serviceAccountMapArr);

			ocs.registerOutParameter(10, OracleTypes.ARRAY, "BUREAU_MAP_TABLE");
			ocs.registerOutParameter(11, OracleTypes.ARRAY, "CUST_MAP_TABLE");
			ocs.registerOutParameter(12, OracleTypes.ARRAY, "ACCOUNT_MAP_TABLE");
			ocs.registerOutParameter(13, OracleTypes.NUMERIC);
			ocs.registerOutParameter(14, OracleTypes.VARCHAR);
			ocs.registerOutParameter(15, OracleTypes.VARCHAR);


			// executing procedure

			ocs.execute();
			res = ocs.getInt(1);
			logger.debug("RETURN : " + res);
			logger.debug("ERROR CODE : " + ocs.getString(14));
			logger.debug("ERROR DESC : " + ocs.getString(15));

			versionNo = ocs.getInt(13);
			logger.debug("Version No From DataBase : " + ocs.getInt(13));
			
			
			if(res==0){
				oraConRef.rollback();
				if(ocs.getString(14)!=null){
					throw new AgreementBusinessException(ocs.getString(14),"Procedure Failed to Insert");
				}else{
					throw new AgreementBusinessException(IErrorCodes.SERVICE_SPECIFIC_INSERT_ERROR,
					"Procedure Failed to Insert");
				}				
			}

			if(res==1){
				oraConRef.commit();

				ret=true;
			}else{
				oraConRef.rollback();
				ret=false;
			}
			return versionNo;

		}catch (SQLException t) {
			logger.error("Exception in insert service specification : " + t);			
			throw new AgreementSystemException(IErrorCodes.SERVICE_SPECIFIC_INSERT_ERROR,
					"Failed to Insert Record for New agreement", t);
		}catch (RuntimeException r){
			logger.error("run time exception "+r);

			throw new AgreementBusinessException(IErrorCodes.TYPECONVERSION_ERROR,
			"Exception while creating Object Array");
		}finally{
			// closing connections here
			SQLUtils.ensureClosedConnection(con,ocs,oraConRef);
		}		 

	}

	/** 
	 * Method is used to search records for Service specification from OPE Database
	 * It searches on Service id Agreement Id and Version No
	 * Get ServiceSpecificCommonBean Object as parameter
	 */


	public List search(ServiceSpecificCommonBean sscb,String action) 
	throws AgreementSystemException,AgreementBusinessException 

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


		try {

			ocs=(OracleCallableStatement)
			oraConRef.prepareCall("{? = call opepk_service_specific.fn_find_service_specific(?,?,?,?,?,?)}");
			ocs.registerOutParameter(1, OracleTypes.NUMERIC);
			ocs.setString(2, sscb.getServiceCode());
			ocs.setString(3, sscb.getInternalAgreementId());
			ocs.setInt(4, Integer.parseInt(sscb.getVersionNo()));

			ocs.registerOutParameter(5, OracleTypes.ARRAY, "BUREAU_MAP_TABLE");

			ocs.registerOutParameter(6, OracleTypes.VARCHAR);
			ocs.registerOutParameter(7, OracleTypes.VARCHAR);

			// executing procedure				  
			ocs.execute();			   
			res = ocs.getInt(1);

			logger.debug("RETURN : " + res);
			logger.debug("ERROR CODE : " + ocs.getString(6));
			logger.debug("ERROR DESC : " + ocs.getString(7));

			if(res==0){
				oraConRef.rollback();
				if(ocs.getString(6)!=null){
					throw new AgreementBusinessException(ocs.getString(6),"Procedure Failed to search");
				}else{
					throw new AgreementBusinessException(IErrorCodes.SERVICE_SPECIFIC_FETCH_ERROR,
					"Procedure Failed to search");
				}				
			}

			ARRAY serviceAttributes = ocs.getARRAY(5);  
			Object[] serviceObj = (Object[])serviceAttributes.getArray();
			List serviceSpecificList=makeListOfServiceSpecific(serviceObj,"");

			List lst=new ArrayList();				
			lst.add(0, serviceSpecificList);

			if(res==1)
			{
				oraConRef.commit();
				ret=lst;
			}
			else 
			{
				oraConRef.rollback();
				ret = null;
			}

			return ret;

		}catch (SQLException t) {
			logger.error("Exception in search service specification : " + t);			
			throw new AgreementSystemException(IErrorCodes.SERVICE_SPECIFIC_FETCH_ERROR,
					"Failed to search Record for service specific", t);
		}catch (RuntimeException r){
			logger.error("run time exception "+r);
			throw new AgreementBusinessException(IErrorCodes.TYPECONVERSION_ERROR,
			"Exception while creating Object Array");
		}finally{
			// closing connection here
			SQLUtils.ensureClosedConnection(con,ocs,oraConRef);
		}		 							

	}	

	/** 
	 * Method is used to edit a Service specification record to OPE Database
	 * Get ServiceSpecificCommonBean Object as parameter
	 */

	public int edit(ServiceSpecificAccountBean[] ssab, 
			ServiceSpecificCustomerBean [] ssCustomerBean,
			ServiceSpecificCommonBean sscb,
			String userId, Date businessDate, String action)
	throws AgreementSystemException, AgreementBusinessException {

		OracleConnection oraConRef=null;
		Connection con=null;
		OracleCallableStatement ocs=null;
		StructDescriptor serviceBureauStrDesc,serviceAccountMapStrDesc,serviceCustomerMapStrDesc;
		ArrayDescriptor serviceBureauArrDesc,serviceAccountMapArrDesc,serviceCustomerMapArrDesc;
		int countRow=0,count=0;
		int res;
		int ret=0;

		try {
			con = SQLUtils.getConnection(CommonConstants.DNBDatabase);
			oraConRef = DescriptorUtility.getOracleConnection(con);			
		} catch (SQLException sq) {
			logger.error("Could not obtain OracleConnection");
			throw new AgreementSystemException(IErrorCodes.CONNECTION_RETRIEVAL_ERROR,
					"Unable to obtain OracleConnection", sq);
		}


		try {
			serviceAccountMapStrDesc= StructDescriptor.createDescriptor("ACCOUNT_MAP_TYPE", oraConRef);
			serviceAccountMapArrDesc= ArrayDescriptor.createDescriptor("ACCOUNT_MAP_TABLE", oraConRef);

			serviceCustomerMapStrDesc= StructDescriptor.createDescriptor("CUST_MAP_TYPE", oraConRef);
			serviceCustomerMapArrDesc= ArrayDescriptor.createDescriptor("CUST_MAP_TABLE", oraConRef);

			serviceBureauStrDesc= StructDescriptor.createDescriptor("BUREAU_MAP_TYPE", oraConRef);
			serviceBureauArrDesc= ArrayDescriptor.createDescriptor("BUREAU_MAP_TABLE", oraConRef);

		} catch (SQLException t) {
			logger.error("Exception in edit service specification [Could not create Struct/Array] : "	+ t);
			throw new AgreementSystemException(IErrorCodes.DB_DESCRIPTOR_ERROR,
					"Could not create db Descriptors", t);			
		}

		try {
			ServiceSpecificAccountBean ssab2=null;
			ARRAY serviceAccountMapArr=null;
			ARRAY serviceArr=null;

			if(ssab[0].getAccountNum()!=null){
				logger.debug("When account exists");
				for(int i = 0; i < ssab.length; i++){
					countRow++;
				}						
				Object[] serviceObjTable = new Object[countRow];		

				// Make the object from the bean class for account information		 			   
				for (int j = 0; j < ssab.length; j++){
					ssab2 = (ServiceSpecificAccountBean) ssab[j];
					Object[] serviceAccountObject = {sscb.getVersionNo(),
							sscb.getInternalRefId(),
							ssab2.getAccountNum(),
							ssab2.getAccountName()

					};			

					STRUCT serviceAccountStruct = new STRUCT(serviceAccountMapStrDesc, oraConRef, serviceAccountObject);
					serviceObjTable[j] = serviceAccountStruct;


				}	   
				serviceAccountMapArr = new ARRAY(serviceAccountMapArrDesc, oraConRef, serviceObjTable);		   		 
			}
			else
			{
				logger.debug("When No Accounts");
					/**
					 * else part will execute when user select Currency Exchange rate service
					 * it doesnot have account fields
					 */
					Object[] serviceObjTable = new Object[1];
					Object[] serviceAccountObject ={null,null,null,null};
					STRUCT serviceAccountStruct = new STRUCT(serviceAccountMapStrDesc, oraConRef, serviceAccountObject);
					serviceObjTable[0]=serviceAccountStruct;
					serviceAccountMapArr = new ARRAY(serviceAccountMapArrDesc, oraConRef, serviceObjTable);
			}
			
			ServiceSpecificCustomerBean ssCustomer2;
			ARRAY serviceCustomerMapArr=null;


			if(ssCustomerBean!=null){
				for(int i = 0; i < ssCustomerBean.length; i++){
					count++;
				}						
				Object[] serviceObjTable = new Object[count];		

				// Make the object from the bean class for customer Id and customer name		 			   
				for (int j = 0; j < ssCustomerBean.length; j++){
					ssCustomer2 = (ServiceSpecificCustomerBean) ssCustomerBean[j];
					Object[] serviceCustomerObject = {sscb.getVersionNo(),
							sscb.getInternalRefId(),
							ssCustomer2.getCustomerId(),
							ssCustomer2.getCustomerName()

					};		
					STRUCT serviceCustomerStruct = new STRUCT(serviceCustomerMapStrDesc, oraConRef, serviceCustomerObject);
					serviceObjTable[j] = serviceCustomerStruct;
				}	   
				serviceCustomerMapArr = new ARRAY(serviceCustomerMapArrDesc, oraConRef, serviceObjTable);		   		 
			} 

			Object[] serviceObject={sscb.getVersionNo(),
					sscb.getInternalRefId(),
					sscb.getServiceId(),
					sscb.getServiceCode(),
					sscb.getBureauId(),
					sscb.getBureauName(),
					sscb.getInternalAgreementId(),
					sscb.getSuo(),
					sscb.getStatus()               			 
			};

			STRUCT serviceStruct=new STRUCT(serviceBureauStrDesc,oraConRef,serviceObject);
			Object[] serviceArrObject = new Object[1];
			serviceArrObject[0]= serviceStruct;

			serviceArr=new ARRAY(serviceBureauArrDesc,oraConRef,serviceArrObject);

			ocs=(OracleCallableStatement)
			oraConRef.prepareCall("{? = call opepk_service_specific.fn_service_specific(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");

			ocs.registerOutParameter(1, OracleTypes.NUMERIC);
			ocs.setString(2, userId);
			ocs.setString(3, action);
			ocs.setString(4, sscb.getServiceCode());
			ocs.setString(5, sscb.getInternalAgreementId());
			ocs.setInt(6, Integer.parseInt(sscb.getVersionNo()));
			ocs.setARRAY(7, serviceArr);
			ocs.setARRAY(8, serviceCustomerMapArr);
			ocs.setARRAY(9, serviceAccountMapArr);

			ocs.registerOutParameter(10, OracleTypes.ARRAY, "BUREAU_MAP_TABLE");
			ocs.registerOutParameter(11, OracleTypes.ARRAY, "CUST_MAP_TABLE");
			ocs.registerOutParameter(12, OracleTypes.ARRAY, "ACCOUNT_MAP_TABLE");
			ocs.registerOutParameter(13, OracleTypes.NUMERIC);
			
			
			ocs.registerOutParameter(14, OracleTypes.VARCHAR);
			ocs.registerOutParameter(15, OracleTypes.VARCHAR);

			// executing procedure

			ocs.execute();
			res = ocs.getInt(1);

			logger.debug("RETURN : " + res);
			logger.debug("Version No after Editing : " + ocs.getInt(13));
			logger.debug("ERROR CODE : " + ocs.getString(14));
			logger.debug("ERROR DESC : " + ocs.getString(15));

			int versionNo = ocs.getInt(13);
			
			
			if(res==0){
				oraConRef.rollback();
				if(ocs.getString(14)!=null){
					throw new AgreementBusinessException(ocs.getString(14),"Procedure Failed to Edit");
				}else{
					throw new AgreementBusinessException(IErrorCodes.SERVICE_SPECIFIC_EDIT_ERROR,
					"Procedure Failed to Edit");
				}				
			}

			if(res==1){
				oraConRef.commit();	 
				ret=versionNo;
			}else{
				oraConRef.rollback();
				ret=0;
			}

			return ret;

		}catch (SQLException t) {
			logger.error("Exception in edit service specification : " + t);			
			throw new AgreementSystemException(IErrorCodes.SERVICE_SPECIFIC_EDIT_ERROR,
					"Failed to edit Record for service specification", t);
		}catch (RuntimeException r){
			logger.error("run time exception "+r);			 
			throw new AgreementBusinessException(IErrorCodes.TYPECONVERSION_ERROR,
			"Exception while creating Object Array");
		}finally{
			// closing connection here
			SQLUtils.ensureClosedConnection(con,ocs,oraConRef);
		}		 
	}

	/** 
	 * Method is used to get the bureau name for selected bureau id
	 * Get ServiceSpecificCommonBean Object as parameter
	 */

	public List getBureauName(ServiceSpecificCommonBean sscb) 
	throws AgreementSystemException, AgreementBusinessException 
	{
		List bureaulist=new ArrayList();
		List ret=null;
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
			oraConRef.prepareCall("{? = call opepk_service_specific.fn_service_bureau_name(?,?,?,?)}");

			ocs.registerOutParameter(1, OracleTypes.NUMERIC);
			ocs.setString(2, sscb.getBureauId());
			ocs.registerOutParameter(3, OracleTypes.VARCHAR); // bureau name
			ocs.registerOutParameter(4, OracleTypes.VARCHAR); //error code 
			ocs.registerOutParameter(5, OracleTypes.VARCHAR); // error message

			// executing procedure

			ocs.execute();
			res = ocs.getInt(1);

			logger.debug("RETURN : " + res);
			logger.debug("ERROR CODE : " + ocs.getString(4));
			logger.debug("ERROR DESC : " + ocs.getString(5));

			if(res==0){
				oraConRef.rollback();
				if(ocs.getString(4)!=null){
					throw new AgreementBusinessException(ocs.getString(4),"Procedure Failed to get Bureau name");
				}else{
					throw new AgreementBusinessException(IErrorCodes.SERVICE_SPECIFIC_FETCH_ERROR,
					"Procedure Failed to get Bureau name");
				}				
			}

			ServiceSpecificCommonBean commonbean=new ServiceSpecificCommonBean();
			commonbean.setBureauName(ocs.getString(3));			
			bureaulist.add(commonbean);

			if(res==1)
			{
				oraConRef.commit();
				ret=bureaulist;
			}
			else 
			{
				oraConRef.rollback();
				ret = null;
			}

			return ret;

		}catch (SQLException t) {
			logger.error("Exception in getting service Bureau name : " + t);			
			throw new AgreementSystemException(IErrorCodes.SERVICE_SPECIFIC_FETCH_ERROR,
					"Failed to get service Bureau name", t);
		}catch (RuntimeException r){
			logger.error("run time exception "+r);

			throw new AgreementBusinessException(IErrorCodes.TYPECONVERSION_ERROR,
			"Exception while creating Object Array");
		}finally{
			// closing connection here
			SQLUtils.ensureClosedConnection(con,ocs,oraConRef);
		}		 	

	}

	/** 
	 * Method is used to get the service type
	 * Get ServiceSpecificCommonBean Object as parameter
	 */

	public List getService(ServiceSpecificCommonBean sscb) 
	throws AgreementSystemException, AgreementBusinessException {

		OracleConnection oraConRef=null;
		Connection con=null;
		OracleCallableStatement ocs=null;
		int res;
		List ret;

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
			oraConRef.prepareCall("{? = call opepk_service_specific.fn_service_specific_out(?,?,?,?,?,?,?)}");

						
			ocs.registerOutParameter(1, OracleTypes.NUMERIC);
			ocs.setString(2, sscb.getInternalRefId());
            ocs.setString(3, sscb.getVersionNo());
			ocs.registerOutParameter(4, OracleTypes.ARRAY, "BUREAU_MAP_TABLE");
			ocs.registerOutParameter(5, OracleTypes.ARRAY, "CUST_MAP_TABLE");
			ocs.registerOutParameter(6, OracleTypes.ARRAY, "ACCOUNT_MAP_TABLE");

			ocs.registerOutParameter(7, OracleTypes.VARCHAR);
			ocs.registerOutParameter(8, OracleTypes.VARCHAR);

			// executing procedure				  
			ocs.execute();			   
			res = ocs.getInt(1);

			logger.debug("RETURN : " + res);
			logger.debug("ERROR CODE : " + ocs.getString(7));
			logger.debug("ERROR DESC : " + ocs.getString(8));

			if(res==0){
				oraConRef.rollback();
				if(ocs.getString(7)!=null)
				{
					throw new AgreementBusinessException(ocs.getString(7),"Procedure Failed to Fetch");
				}else
				{
					throw new AgreementBusinessException(IErrorCodes.SERVICE_SPECIFIC_FETCH_ERROR,
					"Procedure Failed to search");
				}				
			}

			ARRAY serviceAttributes = ocs.getARRAY(4);  
			Object[] serviceObj = (Object[])serviceAttributes.getArray();
			List serviceSpecificList=makeListOfServiceSpecific(serviceObj,"");

			ARRAY customerAttributes = ocs.getARRAY(5);  
			Object[] customerObj = (Object[])customerAttributes.getArray();
			List serviceCustomerList=makeListOfServiceCustomer(customerObj,"");

			ARRAY accountAttributes = ocs.getARRAY(6);  
			List serviceAccountList;
			Object[] accountObj = (Object[])accountAttributes.getArray();
			// if user selects currency exchange rate service there is no account numbers	
			if(accountObj.length!=0)
			{
				serviceAccountList=makeListOfServiceAccount(accountObj,"");
			}
			else
			{
				serviceAccountList=null;
			}
			
			List lst=new ArrayList();	

			lst.add(0,serviceSpecificList);
			lst.add(1,serviceCustomerList);
			lst.add(2,serviceAccountList);

			if(res==1)
			{
				oraConRef.commit();
				ret=lst;
			}
			else 
			{
				oraConRef.rollback();
				ret = null;
			}
			return ret;

		} catch (SQLException t) {
			logger.error("Exception in search service specification : " + t);			
			throw new AgreementSystemException(IErrorCodes.SERVICE_SPECIFIC_FETCH_ERROR,
					"Failed to search Record for service specific", t);
		}catch (RuntimeException r){
			logger.error("run time exception "+r);

			throw new AgreementBusinessException(IErrorCodes.TYPECONVERSION_ERROR,
			"Exception while creating Object Array");
		}finally{
			// closing connection here
			SQLUtils.ensureClosedConnection(con,ocs,oraConRef);
		}		 								

	}

	/** 
	 * Method is used delete the services from temporary table 
	 * Get ServiceSpecificCommonBean Object as parameter
	 */

	public int deleteService(ServiceSpecificCommonBean sscb) 
	throws AgreementSystemException, AgreementBusinessException {

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
			oraConRef.prepareCall("{? = call opepk_service_specific.fn_service_specific_delete(?,?,?,?)}");

			// bid the input out put of the procedure

			logger.debug("  VersionNo ===> "+sscb.getVersionNo());
			logger.debug("  InternalRefId ===> "+sscb.getInternalRefId());
			
			
			ocs.registerOutParameter(1, OracleTypes.NUMERIC);
			ocs.setString(2, sscb.getInternalRefId());
			ocs.setString(3, sscb.getVersionNo());
			ocs.registerOutParameter(4, OracleTypes.VARCHAR); // error code 
			ocs.registerOutParameter(5, OracleTypes.VARCHAR); // error message

			// executing procedure

			ocs.execute();
			res = ocs.getInt(1);

			logger.debug("RETURN : " + res);
			logger.debug("ERROR CODE : " + ocs.getString(4));
			logger.debug("ERROR DESC : " + ocs.getString(5));

			if(res==0){
				oraConRef.rollback();
				if(ocs.getString(4)!=null){
					throw new AgreementBusinessException(ocs.getString(4),"Procedure Failed to Delete");
				}else{
					throw new AgreementBusinessException(IErrorCodes.SERVICE_SPECIFIC_DELETE_ERROR,
					"Procedure Failed to Delete");
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
			logger.error("Exception in Delete service  : " + t);			
			throw new AgreementSystemException(IErrorCodes.SERVICE_SPECIFIC_DELETE_ERROR,
					"Failed to Delete Service Specification", t);
		}finally{
			SQLUtils.ensureClosedConnection(con,ocs,oraConRef);
		}		 	
	}

	/** 
	 * Method is used to enter service id list to temporary table
	 * Get ServiceSpecificCommonBean Object as parameter
	 */

	public boolean insertServiceId(ServiceSpecificCommonBean sscb)
	throws AgreementSystemException, AgreementBusinessException 
	{

		OracleConnection oraConRef=null;
		Connection con=null;
		OracleCallableStatement ocs=null;
		int res;
		boolean ret;
		String[] serviceStr;
		String[] versionStr;
		StructDescriptor serviceStrDesc;
		ArrayDescriptor serviceArrDesc;

		try {
			con = SQLUtils.getConnection(CommonConstants.DNBDatabase);
			oraConRef = DescriptorUtility.getOracleConnection(con);			
		} catch (SQLException sq) {
			logger.error("Could not obtain OracleConnections...");
			throw new AgreementSystemException(IErrorCodes.CONNECTION_RETRIEVAL_ERROR,
					"Unable to obtain OracleConnection", sq);
		}

		try {
			serviceStrDesc=StructDescriptor.createDescriptor("INTERNAL_TEMP_TYPE",oraConRef);
			serviceArrDesc=ArrayDescriptor.createDescriptor("INTERNAL_TEMP_TABLE",oraConRef);

		} catch (SQLException e) {
			logger.error("Exception in insert Specification [Could not create Struct/Array] : "	+ e);
			throw new AgreementSystemException(IErrorCodes.DB_DESCRIPTOR_ERROR,
					"Could not create db Descriptors", e);
		}


		try{
			String services=sscb.getListOfServiceId();
			String version=sscb.getListOfServiceVersionNo();
			
			logger.info("Version String : "+version);
			
			ARRAY serviceArray=null;

			serviceStr=createStrings(services);
			versionStr=createStrings(version);
			String str;
			String strVersion;

			int countStr=serviceStr.length;
			Object [] serviceObjTable=new Object[countStr];

			if(serviceStr!=null)
			{
				for(int k=0;k<countStr;k++)
				{
					str=serviceStr[k];
					strVersion = versionStr[k];
					Object serviceObject[]={str,sscb.getInternalAgreementId(),sscb.getServiceCode(),strVersion,null};
					STRUCT serviceStruct = new STRUCT(serviceStrDesc,oraConRef,serviceObject);
					serviceObjTable[k]=serviceStruct;
				} 
				serviceArray =new ARRAY(serviceArrDesc,oraConRef,serviceObjTable);  	
			}
			//	Prepare the OracleCallableStatement

			ocs=(OracleCallableStatement)
			oraConRef.prepareCall("{? = call opepk_service_specific.fn_service_type_save(?,?,?)}");

			ocs.registerOutParameter(1, OracleTypes.NUMERIC);  
			ocs.setARRAY(2, serviceArray);
			ocs.registerOutParameter(3,OracleTypes.VARCHAR);
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
					throw new AgreementBusinessException(ocs.getString(3),"Procedure Failed to Insert");
				}else{
					throw new AgreementBusinessException(IErrorCodes.SERVICE_SPECIFIC_INSERT_ERROR,
					"Procedure Failed to Insert");
				}				
			}

			// Return the result to the Service Specific Action
			if (res == 1) {
				oraConRef.commit();
				ret = true;
			} else {
				oraConRef.rollback();
				ret = false;
			}	
			return ret;


		}catch (SQLException t) {
			logger.error("Exception in insert service Ids : " + t);			
			throw new AgreementSystemException(IErrorCodes.SERVICE_SPECIFIC_INSERT_ERROR,
					"Failed to Insert Record for Service ID", t);
		}catch (RuntimeException r){
			logger.error("run time exception "+r);

			throw new AgreementBusinessException(IErrorCodes.TYPECONVERSION_ERROR,
			"Exception while creating Object Array");
		}finally{
			SQLUtils.ensureClosedConnection(con,ocs,oraConRef);
		}		 
	}

	/** 
	 * Method is used to edit services
	 * Get ServiceSpecificCommonBean Object as parameter
	 */

	public boolean editServiceId(ServiceSpecificCommonBean sscb)
	throws AgreementSystemException, AgreementBusinessException 
	{

		OracleConnection oraConRef=null;
		Connection con=null;
		OracleCallableStatement ocs=null;
		int res;
		boolean ret;
		String[] serviceStr;
		String[] versionStr;
		StructDescriptor serviceStrDesc;
		ArrayDescriptor serviceArrDesc;

		try {
			con = SQLUtils.getConnection(CommonConstants.DNBDatabase);
			oraConRef = DescriptorUtility.getOracleConnection(con);			
		} catch (SQLException sq) {
			logger.error("Could not obtain OracleConnections...");
			throw new AgreementSystemException(IErrorCodes.CONNECTION_RETRIEVAL_ERROR,
					"Unable to obtain OracleConnection", sq);
		}

		try {
			serviceStrDesc=StructDescriptor.createDescriptor("INTERNAL_TEMP_TYPE",oraConRef);
			serviceArrDesc=ArrayDescriptor.createDescriptor("INTERNAL_TEMP_TABLE",oraConRef);

		} catch (SQLException e) {
			logger.error("Exception in insert Specification [Could not create Struct/Array] : "	+ e);
			throw new AgreementSystemException(IErrorCodes.DB_DESCRIPTOR_ERROR,
					"Could not create db Descriptors", e);
		}


		try{
			String services=sscb.getListOfServiceId();
			String version=sscb.getListOfServiceVersionNo();
			
			logger.info("Version String : "+version);
			
			ARRAY serviceArray=null;

			serviceStr=createStrings(services);
			versionStr=createStrings(version);
			String str;
			String strVersion;

			int countStr=serviceStr.length;
			Object [] serviceObjTable=new Object[countStr];

			if(serviceStr!=null)
			{
				for(int k=0;k<countStr;k++)
				{
					str=serviceStr[k];
					strVersion = versionStr[k];
					Object serviceObject[]={str,sscb.getInternalAgreementId(),sscb.getServiceCode(),strVersion,null};
					STRUCT serviceStruct = new STRUCT(serviceStrDesc,oraConRef,serviceObject);
					serviceObjTable[k]=serviceStruct;
				} 
				serviceArray =new ARRAY(serviceArrDesc,oraConRef,serviceObjTable);  	
			}
			//	Prepare the OracleCallableStatement

			ocs=(OracleCallableStatement)
			oraConRef.prepareCall("{? = call opepk_service_specific.fn_service_type_save(?,?,?)}");

			ocs.registerOutParameter(1, OracleTypes.NUMERIC);  
			ocs.setARRAY(2, serviceArray);
			ocs.registerOutParameter(3,OracleTypes.VARCHAR);
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
					throw new AgreementBusinessException(ocs.getString(3),"Procedure Failed to Edit");
				}else{
					throw new AgreementBusinessException(IErrorCodes.SERVICE_SPECIFIC_EDIT_ERROR,
					"Procedure Failed to Edit");
				}				
			}

			// Return the result to the Service Specific Action
			if (res == 1) {
				oraConRef.commit();
				ret = true;
			} else {
				oraConRef.rollback();
				ret = false;
			}	
			return ret;

		}catch (SQLException t) {
			logger.error("Exception in edit service Ids : " + t);			
			throw new AgreementSystemException(IErrorCodes.SERVICE_SPECIFIC_EDIT_ERROR,
					"Failed to Insert Record for Service ID", t);
		}catch (RuntimeException r){
			logger.error("run time exception "+r);

			throw new AgreementBusinessException(IErrorCodes.TYPECONVERSION_ERROR,
			"Exception while creating Object Array");
		}finally{
			// closing connection here
			SQLUtils.ensureClosedConnection(con,ocs,oraConRef);
		}		 
	}
	
	/**
	 * method checks the duplicate service id from database
	 * ServiceSpecificCommonBean as parameter
	 * returns 1 if service id existing
	 * 0 for no service id
	 */
	
	public int checkDuplicateServiceId(ServiceSpecificCommonBean sscb)
	throws AgreementSystemException, AgreementBusinessException {


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
			oraConRef.prepareCall("{? = call opepk_service_specific.fn_service_id_exist(?,?,?,?,?,?,?)}");


			ocs.registerOutParameter(1, OracleTypes.NUMERIC);
			ocs.setString(2, sscb.getServiceId());
			ocs.setString(3, sscb.getServiceCode());
			ocs.setString(4, sscb.getBureauId());
			ocs.setString(5, sscb.getInternalRefId());
			ocs.setString(6, sscb.getAgreementId());
			ocs.registerOutParameter(7, OracleTypes.VARCHAR); // error code 
			ocs.registerOutParameter(8, OracleTypes.VARCHAR); // error message

			ocs.execute();
			res = ocs.getInt(1);

			logger.debug("RETURN : " + res);
			logger.debug("ERROR CODE : " + ocs.getString(7));
			logger.debug("ERROR DESC : " + ocs.getString(8));

			return res;

		}catch (SQLException t) {
			logger.error("Exception in finding duplicate service Id, bureau id : " + t);			
			throw new AgreementSystemException(IErrorCodes.SERVICE_SPECIFIC_FETCH_ERROR,
					"Failed to find duplicate Service Id, bureau id", t);
		}finally{
			//closing connection here
			SQLUtils.ensureClosedConnection(con,ocs,oraConRef);
		}		 	

	}

	/** 
	 * Method is used to make the list of service specations for specific service Id
	 * Get object[] as parameter
	 */

	private List makeListOfServiceSpecific(Object[] obj,String version_pos)
	throws AgreementSystemException,
	AgreementBusinessException 

	{                                 
		List list = new ArrayList();			
		for (int i = 0; i < obj.length; i++) 
		{
			try {
				STRUCT attrObject = (STRUCT)obj[i];
				Object[] strt = attrObject.getAttributes();
				ServiceSpecificDTO ed=new ServiceSpecificDTO();

				if(strt[0]!=null){
					ed.setVersionNo(strt[0].toString());
				} else
					ed.setVersionNo("");

				ed.setInternalRefId((String)strt[1]);
				ed.setServiceId((String)strt[2]);
				ed.setServiceCode((String)strt[3]);
				ed.setBureauId((String)strt[4]);
				ed.setBureauName((String)strt[5]);
				ed.setInternalAgreementId((String)strt[6]);
				ed.setSuo((String)strt[7]);
				ed.setStatus((String)strt[8]);

				// Add Object to list
				list.add(i,ed);		
			} catch (Exception e){
				logger.error("Exception in Create list" + e);
				throw new AgreementSystemException(IErrorCodes.SERVICE_SPECIFIC_MAKELIST_ERROR,
						"Failed to Fetch Records", e);
			}
		}				
		return list;
	}

	/** 
	 * Method is used to make the list of customer id details for specific service Id
	 * Get object[] as parameter
	 */

	private List makeListOfServiceCustomer(Object[] obj,String version_pos)
	throws AgreementSystemException,
	AgreementBusinessException 

	{                                 

		List list = new ArrayList();			
		for (int i = 0; i < obj.length; i++) 
		{
			try {
				STRUCT attrObject = (STRUCT)obj[i];
				Object[] strt = attrObject.getAttributes();
				ServiceSpecificDTO ed=new ServiceSpecificDTO();

				if(strt[0]!=null){
					ed.setVersionNo(strt[0].toString());
				} else
					ed.setVersionNo("");

				ed.setInternalRefId((String)strt[1]);
				ed.setCustomerId((String)strt[2]);
				ed.setCustomerName((String)strt[3]);

				// Add Object to list
				list.add(i,ed);		
			} catch (Exception e){
				logger.error("Exception in Create list" + e);
				throw new AgreementSystemException(IErrorCodes.SERVICE_SPECIFIC_MAKELIST_ERROR,
						"Failed to Fetch Records", e);
			}
		}				
		return list;
	}

	/** 
	 * Method is used to make the list of account number details for specific service Id
	 * Get object[] as parameter
	 */

	private List makeListOfServiceAccount(Object[] obj,String version_pos)
	throws AgreementSystemException,
	AgreementBusinessException 

	{                                 

		List list = new ArrayList();			
		for (int i = 0; i < obj.length; i++) 
		{
			try {
				STRUCT attrObject = (STRUCT)obj[i];
				Object[] strt = attrObject.getAttributes();
				ServiceSpecificDTO ed=new ServiceSpecificDTO();

				if(strt[0]!=null){
					ed.setVersionNo(strt[0].toString());
				} else
					ed.setVersionNo("");

				ed.setInternalRefId((String)strt[1]);
				ed.setAccountNum((String)strt[2]);
				ed.setAccountName((String)strt[3]);

				// Add Object to list
				list.add(i,ed);		
			} catch (Exception e){
				logger.error("Exception in Create list" + e);
				throw new AgreementSystemException(IErrorCodes.SERVICE_SPECIFIC_MAKELIST_ERROR,
						"Failed to Fetch Records", e);
			}
		}				
		return list;
	}

	/**
	 * this method returns string[] from string tokenizer for selected services 
	 * @param string 
	 * @return string[]
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
	 * method returns number of tokens 
	 * @param ids
	 * @return int
	 */

	private int getLength(String ids) {
		return ids.split(",").length;
	}
	
	/**
	 * This method is added to clear Temporary table 
	 * @param ids
	 * @return int
	 */
	
		
	public int clearTemporaryTable(ServiceSpecificCommonBean sscb)
	throws AgreementSystemException  {

		OracleConnection oraConRef=null;
		Connection con=null;
		OracleCallableStatement ocs=null;
		boolean status = false;
		int res = 0;
		
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
			oraConRef.prepareCall("{? = call opepk_service_specific.fn_service_X_window(?,?,?,?)}");
					
			ocs.registerOutParameter(1, OracleTypes.NUMERIC);
			ocs.setString(2, sscb.getServiceCode());
			ocs.setString(3, sscb.getAgreementId());
			ocs.registerOutParameter(4, OracleTypes.VARCHAR); // error code 
			ocs.registerOutParameter(5, OracleTypes.VARCHAR); // error message

			ocs.execute();
			res = ocs.getInt(1);

			logger.debug("RETURN : " + res);
			logger.debug("ERROR CODE : " + ocs.getString(4));
			logger.debug("ERROR DESC : " + ocs.getString(5));
						
			// Return the result to the Service Specific Action
			if (res == 1) {
				logger.debug(" Content Deleted Successfully");
			} else {
				logger.debug(" Content Deleted Failed");
			}	
			
			return res;

		}catch (SQLException t) {
			logger.error("clearTemporaryTable : " + t);			
			throw new AgreementSystemException(" ","Exception on clear Temporary Table", t);
		}finally{
			//closing connection here
			SQLUtils.ensureClosedConnection(con,ocs,oraConRef);
		}		 	
	}

}