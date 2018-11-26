/********************************************************************************************
 * Copyright 2008 IDEAL INVENT Technologies Pvt. Ltd.                                        *
 * IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.       *
 * The Program or any portion thereof may not be reproduced in any form whatsoever except    *
 * as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd. *
 *																							 *
 * File Name                   : OracleHelpDeskReportsDAO.java                               *
 * Author                      : Manjunath N G                                               *
 * Creation Date               : 10-Nov-2008                                                 *
 * Description                 : This file serves as the interface, which contains all       *
 *                               the methods which are used for All Help Desk Reports.       *
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
package com.dnb.agreement.helpdesk.DAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleTypes;
import org.apache.log4j.Logger;

import com.dnb.agreement.exception.AgreementBusinessException;
import com.dnb.agreement.exception.AgreementSystemException;
import com.dnb.common.commons.CommonConstants;

import com.dnb.common.database.DescriptorUtility;
import com.dnb.agreement.helpdesk.DTO.PatuKeyAuditSearchDTO;
import com.dnb.agreement.helpdesk.DTO.FileTransferAuditDTO;
import com.dnb.agreement.exception.IErrorCodes;
import com.dnb.agreement.utility.SQLUtils;	
import com.dnb.agreement.utility.DateUtils;

public class OracleHelpDeskReportsDAO implements HelpDeskReportsDAO {

	static Logger logger = Logger.getLogger(OracleHelpDeskReportsDAO.class);
	
	/** 
	 * Method is used to search records based on inputs given 
	 * for showPatuKeyAuditDetails Report
	 * Input Field(s) - Online Status
	 */
	
	public String showPatuKeyAuditDetails(PatuKeyAuditSearchDTO e, String userId,String dateFormat) throws AgreementSystemException,
	AgreementBusinessException {
		
		OracleConnection oraConRef = null;
		Connection con = null;
		OracleCallableStatement ocs = null;		
		
		int res;
		String ret = "";
		
		try {
			con = SQLUtils.getConnection(CommonConstants.DNBDatabase);
			oraConRef = DescriptorUtility.getOracleConnection(con);			
		} catch (SQLException sq) {
			logger.error("Could not obtain OracleConnection");
			throw new AgreementSystemException(IErrorCodes.CONNECTION_RETRIEVAL_ERROR,
					"Unable to obtain OracleConnection", sq);
		}
				
		try {
			
			java.sql.Date sqlDateFrom = null;
			java.sql.Date sqlDateTo = null;
						
			if(e.getDateFrom().length()!=0){
				java.util.Date utilDateFrom=DateUtils.parseDate(dateFormat,e.getDateFrom());
				sqlDateFrom = new java.sql.Date(utilDateFrom.getTime());
			}
			
			if(e.getDateTo().length()!=0){
				java.util.Date utilDateTo=DateUtils.parseDate(dateFormat,e.getDateTo());
				sqlDateTo = new java.sql.Date(utilDateTo.getTime());
			}			
								
			// Prepare the OracleCallableStatement
			ocs = (OracleCallableStatement) oraConRef
			.prepareCall("{? = call opepk_help_desk_report.fn_patu_key_audit_report(?,?,?,?,?,?,?,?,?)}");

			// Bind inputs and register outputs
			ocs.registerOutParameter(1, OracleTypes.NUMERIC);
			ocs.setString(2, userId);
			ocs.setString(3, e.getPatuUserId());
			ocs.setString(4, e.getPatuId());
			ocs.setString(5, e.getServiceBureauId());
			ocs.setString(6, e.getServiceBureauName());
			ocs.setDate(7, sqlDateFrom);
			ocs.setDate(8, sqlDateTo);
						
			ocs.registerOutParameter(9, OracleTypes.VARCHAR);
			ocs.registerOutParameter(10, OracleTypes.VARCHAR);
			ocs.execute();

			// Log the result
			res = ocs.getInt(1);
			logger.debug("RETURN : " + res);
			logger.debug("ERROR CODE : " + ocs.getString(9));
			logger.debug("ERROR DESC : " + ocs.getString(10));			
			
			if (res == 0) {
				oraConRef.rollback();
				if(ocs.getString(9)!=null)
					throw new AgreementBusinessException(
							ocs.getString(9),
					"Procedure Failed to Insert");		
				else									
					throw new AgreementSystemException(
							IErrorCodes.REPORT_SEARCH_ERROR,
					"Date From and Date To Fields are Mandatory");				
			}			
			ret = ocs.getString(9);
			
			if (res == 1) {
				oraConRef.commit();
			} else {
				oraConRef.rollback();
			}			
			return ret;
		} 
		catch (SQLException t) {
			logger.debug("Failed to Search Records : " + t);
			throw new AgreementSystemException(IErrorCodes.REPORT_SEARCH_ERROR,
					"Failed to Search Records", t);

		} catch(RuntimeException re) {
			throw new AgreementBusinessException(
					IErrorCodes.REPORT_SEARCH_ERROR,
			"Failed to Search Records");
		} finally{
			SQLUtils.ensureClosedConnection(con,ocs,oraConRef);
		}
	}	
	
	/** 
	 * Method is used to search records based on inputs given 
	 * for showFileTransferAuditDetails Report
	 * Input Field(s) - Patu Id, Agreement Id, Service Type, Date From, Date To, Group By
	 */
	
	public String showFileTransferAuditDetails(FileTransferAuditDTO e, String userId,String dateFormat) throws AgreementSystemException,
	AgreementBusinessException {
		
		OracleConnection oraConRef = null;
		Connection con = null;
		OracleCallableStatement ocs = null;		
		
		int res;
		String ret = "";
		
		try {
			con = SQLUtils.getConnection(CommonConstants.DNBDatabase);
			oraConRef = DescriptorUtility.getOracleConnection(con);			
		} catch (SQLException sq) {
			logger.error("Could not obtain OracleConnection");
			throw new AgreementSystemException(IErrorCodes.CONNECTION_RETRIEVAL_ERROR,
					"Unable to obtain OracleConnection", sq);
		}
				
		try {
			
			java.sql.Timestamp sqlDateFrom = null;
			java.sql.Timestamp sqlDateTo = null;
						
			if(e.getDateFrom().length()!=0){				
				sqlDateFrom=getTimeStamp(e.getDateFrom(), Integer.parseInt(e.getHoursFrom()) ,Integer.parseInt(e.getAmpmFrom()),dateFormat,"FROM");				
			}
			
			System.out.println("Date From " +sqlDateFrom);
			
			if(e.getDateTo().length()!=0){				
				sqlDateTo=getTimeStamp(e.getDateTo(), Integer.parseInt(e.getHoursTo()) ,Integer.parseInt(e.getAmpmTo()),dateFormat,"TO");
			}
			
			System.out.println("Date To " +sqlDateTo);
								
			// Prepare the OracleCallableStatement
			ocs = (OracleCallableStatement) oraConRef
			.prepareCall("{? = call opepk_help_desk_report.fn_file_audit_report(?,?,?,?,?,?,?,?,?)}");

			// Bind inputs and register outputs
			ocs.registerOutParameter(1, OracleTypes.NUMERIC);
			ocs.setString(2, userId);
			ocs.setString(3, e.getPatuId());
			ocs.setString(4, e.getAgreementId());
			ocs.setString(5, e.getServiceType());
			
			ocs.setTimestamp(6, sqlDateFrom);
			ocs.setTimestamp(7, sqlDateTo);
			
			ocs.setString(8, e.getGroupBy());						
			ocs.registerOutParameter(9, OracleTypes.VARCHAR);
			ocs.registerOutParameter(10, OracleTypes.VARCHAR);
			ocs.execute();

			// Log the result
			res = ocs.getInt(1);
			logger.debug("RETURN : " + res);
			logger.debug("ERROR CODE : " + ocs.getString(9));
			logger.debug("ERROR DESC : " + ocs.getString(10));		
			
			if (res == 0) {
				oraConRef.rollback();
				if(ocs.getString(9)!=null)
					throw new AgreementBusinessException(
							ocs.getString(9),
					"Procedure Failed to Insert");		
				else									
					throw new AgreementSystemException(
							IErrorCodes.REPORT_SEARCH_ERROR,
					"Date From and Date To Fields are Mandatory");				
			}			
			ret = ocs.getString(9);
			
			if (res == 1) {
				oraConRef.commit();
			} else {
				oraConRef.rollback();
			}			
			return ret;
		} 
		catch (SQLException t) {
			logger.debug("Failed to Search Records : " + t);
			throw new AgreementSystemException(IErrorCodes.REPORT_SEARCH_ERROR,
					"Failed to Search Records", t);

		} catch(RuntimeException re) {
			throw new AgreementBusinessException(
					IErrorCodes.REPORT_SEARCH_ERROR,
			"Failed to Search Records");
		} finally{
			SQLUtils.ensureClosedConnection(con,ocs,oraConRef);
		}
	}
	
	/** 
	 * Method is used to convert SQL date to SQL Timestamp for a given date format
	 * and to add hours and specify AM/PM
	 **/
	
		public Timestamp getTimeStamp(String date,int hoursToAdd, int amPm,String dateFormat,String type){
			java.sql.Timestamp timestamp=null;
		    
			try {
		    		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);    		
		    		java.sql.Date sqlDate= null;            
		            
		            Calendar cal = Calendar.getInstance();
		            cal.setTime(sdf.parse(date));
		            
		            if(type.equals("TO") && hoursToAdd==0){
		            	cal.add(Calendar.HOUR, 23);
		            	cal.add(Calendar.MINUTE, 59);
		            	cal.add(Calendar.SECOND, 59);
		            	
		            }else{
		            	cal.add(Calendar.HOUR, hoursToAdd);
			            cal.add(Calendar.AM_PM,amPm);			            	
		            }
		            
		            sqlDate = new java.sql.Date(cal.getTimeInMillis());		                                      
		            timestamp = new java.sql.Timestamp(sqlDate.getTime());		                      
		
		    } catch (ParseException pe) {		        	
		    	logger.debug("Could not convert date to timestamp. " +pe);
		    }
		    return timestamp;
		}
}