
/********************************************************************************************
* Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                        *
* IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.       *
* The Program or any portion thereof may not be reproduced in any form whatsoever except    *
* as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd. *
*																							*
* File Name                   : OracleLoginDAO.java                                         *
* Author                      : Chenchi Reddy                                                    *
* Creation Date               : 24-Jun-2008                                                 *
* Description                 : This file serves as the interface, which contains all       *
*                               the methods which are used for Login.                       *                                             *
* Modification History        :                                                             *
*																						    *
*  Version No.  Edited By         Date               Brief description of change            *
*  ---------------------------------------------------------------------------------------  *
*   V1.0      | Balu Narayanasamy  |  29-Aug-2006   |makeList(),getUserRightsAndAttributes()*
*             |                    |				|   methods modified.				    *
*  ---------------------------------------------------------------------------------------  *
*********************************************************************************************/


/** 
 * Create or import Packages 
 */
package com.dnb.common.DAO;

import java.sql.Array;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.List;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleTypes;

import org.apache.log4j.Logger;

import com.dnb.common.DTO.LoginDTO;
import com.dnb.common.bean.UserRightsBean;
import com.dnb.common.commons.CommonConstants;
import com.dnb.common.database.DescriptorUtility;
import com.dnb.common.exception.IErrorCodes;
import com.dnb.common.exception.CommonBusinessException;
import com.dnb.common.exception.CommonSystemException;
import com.dnb.common.utility.SQLUtils;
/** 
 * Interface that all LoginDAO must support
 */
public class OracleLoginDAO implements LoginDAO
{
	static Logger logger = Logger.getLogger(OracleLoginDAO.class);
	  boolean checkFlag = false;
	/**		
	 * Method is used to insert a new Login record to Statement Database
	 * Get LoginDTO Object as parameter
	 */
   
	 public boolean validateUser(LoginDTO ldto) throws CommonSystemException,
			CommonBusinessException {
		OracleConnection oraConRef = null;
		Connection con = null;
		OracleCallableStatement ocs = null;
		
		int res;
		try {
			//Getting database Connection
			con =  SQLUtils.getConnection(CommonConstants.DNBDatabase);
			oraConRef = DescriptorUtility.getOracleConnection(con);			
		} catch (SQLException sq) {
			sq.printStackTrace();
			logger.error("Could not obtain OracleConnection"+sq);
			throw new CommonSystemException(IErrorCodes.CONNECTION_RETRIEVAL_ERROR,
					"Unable to obtain OracleConnection", sq);
		}

		try {
			// Declare the OracleCallableStatement.
			ocs = (OracleCallableStatement)
				oraConRef.prepareCall("{? = call dmpk_user.Fn_Validate_User(?,?,?,?)}");
			
			// Bind inputs and register outputs			
			ocs.registerOutParameter(1, OracleTypes.NUMERIC);
			ocs.setString(2,ldto.getUserId());
			ocs.setString(3,ldto.getPassword());
			ocs.registerOutParameter(4, OracleTypes.VARCHAR);	
			ocs.registerOutParameter(5, OracleTypes.VARCHAR);
			
			// Execute the procedure
		    ocs.execute();
		   	// Log the Result
			res = ocs.getInt(1);
	
			logger.info("RETURN : " + res );				
			logger.debug("ERROR CODE : " + ocs.getString(4));
			logger.debug("ERROR DESC : " + ocs.getString(5));
			if (res == 0) {
				if(ocs.getString(4)!=null)
				{		
					throw new CommonBusinessException(ocs.getString(4),ocs.getString(5));
				}
			}
			// Return the result to the BatchAction
			if (res == 1) {
				oraConRef.commit();
				checkFlag = true;
			} else {
				oraConRef.rollback();
				checkFlag = false;
			}
			return checkFlag;
		}
		catch (SQLException t) {			
			throw new CommonBusinessException(t.getErrorCode()+":",t.getMessage());
		} catch(RuntimeException re) {			
			throw new CommonBusinessException(IErrorCodes.CONNECTION_RETRIEVAL_ERROR,re.getMessage());
		}finally{
			SQLUtils.ensureClosed(ocs, con, oraConRef);
		}

	}
	 

	 public List getUserDetails(String userId) throws CommonBusinessException, CommonSystemException
		{
		 OracleConnection oraConRef = null;
			Connection con = null;
			OracleCallableStatement ocs = null;
			int res;
			List lst = new ArrayList();
			String time="";
			
			try {
				//Getting database Connection
				con =  SQLUtils.getConnection(CommonConstants.DNBDatabase);
				oraConRef = DescriptorUtility.getOracleConnection(con);		
			} catch (SQLException sq) {
				sq.printStackTrace();
				logger.error("Could not obtain OracleConnection"+sq);
				throw new CommonSystemException(IErrorCodes.CONNECTION_RETRIEVAL_ERROR,
						"Unable to obtain OracleConnection", sq);
			}
			try {	
				
				
				// Declare the CallableStatement
				ocs = (OracleCallableStatement)
				oraConRef.prepareCall("{? = call dmpk_user.fn_get_user_details_new(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");

				// Bind inputs and register outputs		
				ocs.registerOutParameter(1, OracleTypes.NUMERIC);
				ocs.setString(2,userId);
				ocs.registerOutParameter(3, OracleTypes.VARCHAR);  //Date Format
				ocs.registerOutParameter(4, OracleTypes.DATE);     //Last Login Date
				ocs.registerOutParameter(5, OracleTypes.DATE);     //Current Business Date
				ocs.registerOutParameter(6, OracleTypes.VARCHAR);  //Language Id
				ocs.registerOutParameter(7, OracleTypes.VARCHAR);  //country Id
				ocs.registerOutParameter(8, OracleTypes.VARCHAR);  //UserName(FirstName LastName)
				ocs.registerOutParameter(9, OracleTypes.VARCHAR);  //Division Name
				ocs.registerOutParameter(10, OracleTypes.VARCHAR);  //Division Code
				ocs.registerOutParameter(11, OracleTypes.VARCHAR);  //Ent id
				ocs.registerOutParameter(12, OracleTypes.VARCHAR);  //Ent Name
				ocs.registerOutParameter(13, OracleTypes.VARCHAR);  //Ent Status
				ocs.registerOutParameter(14, OracleTypes.VARCHAR);  //Ent Ver No
				ocs.registerOutParameter(15, OracleTypes.VARCHAR);  //Branch Local Currency
				ocs.registerOutParameter(16, OracleTypes.VARCHAR);  //Debug Mode
				ocs.registerOutParameter(17, OracleTypes.ARRAY, "USER_RIGHTS_TB"); //User Rights
				ocs.registerOutParameter(18, OracleTypes.VARCHAR);
				ocs.registerOutParameter(19, OracleTypes.VARCHAR);			
				// Execute the procedure
				ocs.execute();

				
				res = ocs.getInt(1);
				
				try
				{
					time = ocs.getString(4);
					
					//time = time.substring(10);
					
				}catch(NullPointerException ne)
				{
					time = "";
				}
				
				logger.info("RETURN : " + res);
				logger.info("ERROR CODE : " + ocs.getString(18));
				logger.info("ERROR DESC : " + ocs.getString(19));

				if(res==0) {
					
				
					oraConRef.rollback();
					if(ocs.getString(18)!=null)
						throw new CommonBusinessException(ocs.getString(18), ocs.getString(19));
					else
						throw new CommonBusinessException(
								IErrorCodes.LOGIN_RIGHTS_ERROR,
						"Procedure Failed to Fetch");
				}
				if(res == 1)
				{
					
					
					oraConRef.commit();
					Array attributes = (Array) ocs.getArray(17);  
					
					
					Object[] obj = (Object[])attributes.getArray();
					
					lst = makeList(obj,ocs.getString(3),ocs.getString(4),ocs.getDate(5),ocs.getString(6),ocs.getString(7),userId,ocs.getString(9),ocs.getString(10),ocs.getString(11),ocs.getString(12),ocs.getString(13),ocs.getString(14),ocs.getString(15));	
						}
				else  
						{
					
					oraConRef.rollback();
				        }
			
				return lst;
				
			
			} catch (SQLException t) {
				
				throw new CommonSystemException(IErrorCodes.LOGIN_RIGHTS_ERROR,
						"Failed to get rights", t);
			} finally{
//				Closing Oracle Connections
				SQLUtils.ensureClosed(ocs, con, oraConRef);
			}
		}//getUserDetails()
				
				
					
				
	
		
	/** 
	 * Method is change password
	 */
	  public boolean logoutUser(String userId) throws CommonSystemException,
			CommonBusinessException {
		OracleConnection oraConRef = null;
		Connection con = null;
		OracleCallableStatement ocs = null;
		boolean ret=false;
		int res;

		try {
//			Getting database Connection
			con =  SQLUtils.getConnection(CommonConstants.DNBDatabase);
			oraConRef = DescriptorUtility.getOracleConnection(con);
		} catch (SQLException sq) {
			logger.error("Could not obtain OracleConnection"+sq);
			throw new CommonSystemException(IErrorCodes.CONNECTION_RETRIEVAL_ERROR,
					"Unable to obtain OracleConnection", sq);
		}

		try {
			// Declare the OracleCallableStatement.
			ocs = (OracleCallableStatement)
				oraConRef.prepareCall("{? = call dmpk_user.fn_update_logout(?,?,?)}");
			
			// Bind inputs and register outputs		
			ocs.registerOutParameter(1, OracleTypes.NUMERIC);
			ocs.setString(2,userId);
		 	ocs.registerOutParameter(3, OracleTypes.VARCHAR);
		 	ocs.registerOutParameter(4, OracleTypes.VARCHAR);

			// Execute the procedure
		    ocs.execute();


			// Log the Result
			res = ocs.getInt(1);
			logger.info("RETURN : " + res );
			
			
			if(res==1) {
				oraConRef.commit();				
				ret=true;
			} else {
				oraConRef.rollback();				
				ret=false;
			}			
			return ret;
		} catch (SQLException t) {
			logger.error("Exception in logoutUser : " + t);
			throw new CommonSystemException(IErrorCodes.LOGIN_CHANGEPWD_ERROR,
					"Failed to change password", t);
		}finally{
			SQLUtils.ensureClosed(ocs, con, oraConRef);
			}
	  }


//	Method used to making list
		private List makeList(Object[] obj,String dateFormat, String lastLoginDate,Date currentBusinessDate,String languageId,String countryId,String userName,String divisionName,String divisionCode,String enterpriseId,String enterpriseName,String enterpriseStatus,String enterpriseversionNo,String branchlocalCurrency)	{
			List list = new ArrayList();
			try {
				for (int i = 0; i < obj.length; i++) {		  
					Struct attrObject = (Struct) obj[i];
					Object[] strt = attrObject.getAttributes();
		
				if(!(strt[1].toString().equals("0000000000000000"))){
				  UserRightsBean urb = new UserRightsBean();
				  urb.entityCode=(String)strt[0];
				  if(strt[1]!=null) {					  
					urb.rightNo=strt[1].toString();
				  } else urb.rightNo="";
			 	  
				   if(userName!=null&&userName!="")
				   urb.userName = userName ; 
				   else
				   urb.userName = "" ; 
				   if(dateFormat!=""&&dateFormat!=null)
				   urb.dateFormat = dateFormat;
				   else
				   urb.dateFormat="";
				
				   urb.lastLoginDate = lastLoginDate;
				   urb.currentBusinessDate = currentBusinessDate;
				   urb.languageId = languageId;
				   urb.divisionId=divisionCode;
				   urb.enterpriseId=enterpriseId;
				  list.add(urb);		
				  }//IF
				}
		   } catch (Exception e) {			   
					logger.error("Could not create User-Rights list" + e);
			 }			 
		   return list;
		}
		
		 
		 private String makeString(Object st) {
				String s = "";

				if (st != null) {
					s = (st.toString());
				}
				return s;
			}	
}