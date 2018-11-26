
/********************************************************************************************
 * Copyright 2008 IDEAL INVENT Technologies Pvt. Ltd.                                        *
 * IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.       *
 * The Program or any portion thereof may not be reproduced in any form whatsoever except    *
 * as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd. *
 *																							 *
 * File Name                   : PatuKeyAuditDetailsSearchAction.java                        *
 * Author                      : Manjunath N G                                               *
 * Creation Date               : 07-Nov-2008                                                 *
 * Description                 : This file serves as the action file which calls the DAO     *
 *								 for database connection 								     *
 *								 and does the PatuKeyAuditDetailsSearchAction				 *
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

package com.dnb.agreement.helpdesk.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dnb.agreement.common.OPEConstants;
import com.dnb.agreement.exception.AgreementBusinessException;
import com.dnb.agreement.exception.AgreementSystemException;
import com.dnb.agreement.exception.IErrorCodes;
import com.dnb.agreement.helpdesk.DAO.HelpDeskDAOOpe;
import com.dnb.agreement.helpdesk.DAO.HelpDeskReportsDAO;
import com.dnb.agreement.helpdesk.DTO.PatuKeyAuditSearchDTO;
import com.dnb.agreement.helpdesk.bean.PatuKeyAuditSearchBean;
import com.dnb.common.exception.CommonSystemException;
import com.dnb.common.session.SessionDataHandler;

public class PatuKeyAuditDetailsSearchAction extends Action 
{
	static Logger logger = Logger.getLogger(PatuKeyAuditDetailsSearchAction.class);
	
	public ActionForward execute(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
	throws AgreementSystemException,AgreementBusinessException,CommonSystemException {
		
		List opeErrorList = new ArrayList();
		String rep_ret = "";
		String userId = null;
				
		String msg="failed";
		String fetchStatus = "true";
		PatuKeyAuditSearchDTO edto = null;
		PatuKeyAuditSearchBean ea = null;
		String userSystemDate=null;
		String dateFormat="";

		try
		{
			//	Obtain the user id , business date from session
			
	        SessionDataHandler sdh = SessionDataHandler
					.getSessionDataHandler(request.getSession());
			if (sdh != null) {
				userId = sdh.getCurrentUser();	
				dateFormat=sdh.getDateFormat();
			} else {
				logger.debug("Could not retreive data from session");
				throw new CommonSystemException(IErrorCodes.SESSION_LOST_ERROR,
						"LOST USER SESSION");
			}

			/*Copy elements of Bean to DTO*/
			
			ea = (PatuKeyAuditSearchBean) form; 
			edto = new PatuKeyAuditSearchDTO();
			
			// Copy the properties from the Bean to the DTO
			try{
				PropertyUtils.copyProperties(edto, ea);
			} catch(NoSuchMethodException nsme){
				logger.error("No such method exception "	+ nsme);
				throw new AgreementSystemException(IErrorCodes.NO_SUCH_METHOD_ERROR,
				"Could not copy data from Bean to DTO", nsme);				
			} catch(InvocationTargetException ite){
				logger.error("Invocation Target Exception "	+ ite);
				throw new AgreementSystemException(IErrorCodes.INVOCATION_TARGET_ERROR,
				"Could not copy data from Bean to DTO", ite);				
			} catch(IllegalAccessException iae){
				logger.error("Illegal Access Exception "	+ iae);
				throw new AgreementSystemException(IErrorCodes.ILLEGAL_ACCESS_ERROR,
				"Could not copy data from Bean to DTO", iae);				
			}
								
			/*Obtain the DAO Factory corresponding to our database*/
			
			HelpDeskDAOOpe pmf = HelpDeskDAOOpe.getDAOOpe(1);
			HelpDeskReportsDAO entDAO = pmf.getHelpDeskReportsDAO();
			userSystemDate=edto.userSystemDate();
								
			/* Call Procedure to fetch data into temp table*/
			rep_ret= entDAO.showPatuKeyAuditDetails(edto,userId,dateFormat);
					        		        		        
			/*Delete the folder "Documents" every time when a user Query the report*/									
			//String reportPath=request.getParameter("reportPath");						
			
			try {
		    	Properties properties = new Properties();
		    		    	
		    	/*Get the Path of the temparary document*/
		    	String Location = request.getSession().getServletContext().getRealPath("/WEB-INF/classes/applicationconfig/installation_en.properties");
		        properties.load(new FileInputStream(Location));
		        		        	        
		        String str = properties.getProperty("rptRoot");       
		        
				/*Delete the folder "Documents" every time when a user Query the report*/		        
				File file = new File(str);
				boolean bo = edto.del(file);				
		    	}
			catch (IOException e) {				
				logger.debug("IOException: "+e);
				System.out.println("IOException:**** "+e);
		    }
			
			if(rep_ret ==null){
				rep_ret="";
			}
			
			/* Check whether records exists for the search criteria */
			if(rep_ret ==  "ERR-RPT-001" || rep_ret.equals("ERR-RPT-001"))	{
				msg="success";	
				fetchStatus = "false";
			} else {
				/*Setting the attributes for Report Design requirement*/
				request.setAttribute("patuUserId",edto.getPatuUserId());
				request.setAttribute("patuId",edto.getPatuId());
				request.setAttribute("serviceBureauId",edto.getServiceBureauId());
				request.setAttribute("serviceBureauName",edto.getServiceBureauName());
				request.setAttribute("dateFrom",edto.getDateFrom());
				request.setAttribute("dateTo",edto.getDateTo());				
				
				msg="success";
				fetchStatus = "true";
			}			
			
			/**
			 * Show Message in UI on failure 
			 */
		}		
		catch(AgreementSystemException se)
		{
			opeErrorList.add(se.getErrorCode());
			logger.debug("AgreementSystemException : "+se.getErrorCode());
		}
		catch(AgreementBusinessException be)
		{
			opeErrorList.add(be.getErrorCode());
			logger.debug("AgreementBusinessException : "+be.getErrorCode());
		}catch(CommonSystemException cse){		
			opeErrorList.add(cse.getErrorCode());
			logger.error("Could not get the data from session"	+ cse);									
		}
		finally
		{	
			request.setAttribute("userSystemDate",userSystemDate);
			request.setAttribute("result", msg);
			request.setAttribute("fetchStatus", fetchStatus);
			
			if(msg.equals("failed")) {
				if(opeErrorList==null)
					opeErrorList.add(IErrorCodes.DEFAULT_ERROR);
				request.setAttribute(OPEConstants.OPEERRORLIST,opeErrorList);
			}
			return mapping.findForward(msg);	
		}
	}
}