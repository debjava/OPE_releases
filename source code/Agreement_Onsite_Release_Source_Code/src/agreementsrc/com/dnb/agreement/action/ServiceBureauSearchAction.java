
/********************************************************************************************
* Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                        *
* IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.       *
* The Program or any portion thereof may not be reproduced in any form whatsoever except    *
* as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd. *
*																							*
* File Name                   : ServiceBureauSearchAction.java                              *
* Author                      : Manjunath N G                                               *
* Creation Date               : 04-August-2008                                              *
* Description                 : This file serves as the action file which calls the DAO     *
*								for database connection and does the Service Bureau action. *
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

package com.dnb.agreement.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.commons.beanutils.PropertyUtils;

import org.apache.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.sql.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import com.dnb.agreement.common.OPEConstants;
import com.dnb.agreement.bean.ServiceBureauSearchBean;

import com.dnb.agreement.DTO.ServiceBureauSearchDTO;
import com.dnb.agreement.DTO.ServiceBureauDTO;
import com.dnb.agreement.DAO.ServiceBureauDAO;
import com.dnb.agreement.DAO.DAOOpe;
import com.dnb.agreement.exception.IErrorCodes;
import com.dnb.agreement.exception.AgreementSystemException;
import com.dnb.agreement.exception.AgreementBusinessException;
import com.dnb.common.exception.CommonSystemException;
import com.dnb.common.session.SessionDataHandler;

public class ServiceBureauSearchAction extends Action 
{
	List lst;
	
	static Logger logger = Logger.getLogger(ServiceBureauSearchAction.class);
   
/**  
 * Action Methods 
 */

/**
 * Returns the <code>ActionForward</code> named "success" if one is
 * configured or <code>null</code>if it cannot be found.
 *
 * Searches first for a local forward, then a global forward.
 *
 * @param mapping The ActionMapping used to select this instance
 * @param form The optional ActionForm bean for this request (if any)
 * @param request The HTTP request we are processing
 * @param response The HTTP response we are creating
 *
 * @exception Exception if mapping.findForward throws an Exception
 *
 * @return the "success" ActionForward, or null if it cannot be found
 */
    
	public ActionForward execute(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response){
		 
		List lst_disp_for_edit=new ArrayList();
		List pmErrorList = new ArrayList();
		boolean dept_ret=false; 
		String msg="failed";
		String userId=null;
		ServiceBureauSearchBean deptSearchBean = null;
		ServiceBureauSearchDTO deptSearchDTO = null;
		Date businessDate = null;
		String deleteMode=null;
		String action="";
		
	try
	{	        
		// Obtain the user id from session
		SessionDataHandler sdh = SessionDataHandler.getSessionDataHandler(request.getSession());
		
		
			if (sdh != null) {
				userId = sdh.getCurrentUser();
				businessDate = (java.sql.Date)sdh.getCurrentBusinessDate();
			} else {
					logger.debug("Could not retreive data from session");
					throw new CommonSystemException(IErrorCodes.SESSION_LOST_ERROR,
							"LOST USER SESSION");
			}
		
		
		/** 
		 * Get the Service Bureau Form Bean 
		 */
		
		deptSearchBean = (ServiceBureauSearchBean) form; 
		deptSearchDTO = new ServiceBureauSearchDTO();
		
		/** 
		 * Create the required DAO-Factory 
		 */			
		 DAOOpe daoOpe = DAOOpe.getDAOOpe(1);
		 
		 /**  
		 * Create an ServiceBureauDAO 
		 */			
         ServiceBureauDAO serviceBureauDAO = daoOpe.getServiceBureauDAO();
		
		// Copy the properties from the Bean to the DTO
		try{
			PropertyUtils.copyProperties(deptSearchDTO, deptSearchBean);
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
		
	   
		/**
		 * Check condition and do delete bureau
		 */
		try{
						
			if(deptSearchDTO.getDeleteMe()!=null ){
				if(!(deptSearchDTO.getDeleteMe().equals(""))){			
				try{
					deleteMode = request.getParameter("deleteMode");
				}catch(NullPointerException np){
					logger.error("No value found for delete mode.");
				}	
				action="D";
				dept_ret = serviceBureauDAO.delete(deptSearchDTO,userId,businessDate,action,deleteMode);
				
				if(deleteMode.equals("D")){
					if(dept_ret){
						logger.debug("Records Deleted Successfully");	
						pmErrorList.add(IErrorCodes.SERVICE_BUREAU_DELETE_SUCCESSFUL);
						msg="deleted";				
					}else{
						logger.debug("Could not Delete Records: ");
						pmErrorList.add(IErrorCodes.SERVICE_BUREAU_DELETE_ERROR);
						msg="failed";
					}	
				}else if(deleteMode.equals("R")){
					if(dept_ret){
						logger.debug("Records Reopened Successfully");						
						msg="deleted";				
					}else{
						logger.debug("Could not reopen records: ");
						pmErrorList.add(IErrorCodes.RECORD_REOPEN_ERROR);
						msg="failed";
					}	
				}
			}
			}
		}catch(AgreementBusinessException be)	{
			pmErrorList.add(be.getErrorCode());
			logger.debug("AgreementBusinessException : "+be.getErrorCode());
			request.setAttribute(OPEConstants.OPEERRORLIST,pmErrorList);
		}

		/**
		 * Check condition and do search all or display for modification
		 */
		
		if(deptSearchDTO.getVersionNo()!=null)
		{
			// Setting the attribute of maximum no. of version to display version status 
			// in audit trail of jsp page. for example 1 of 4,2 of 4,3 of 4,4 of 4 

			request.setAttribute("maxVersionNo", deptSearchDTO.getVersionNo());
			
			Iterator it = lst.iterator();
			while (it.hasNext()) 
			{
				 ServiceBureauDTO e = (ServiceBureauDTO)it.next();
				 
				 if(e.getInternalBureauId().equals(deptSearchDTO.getInternalBureauId())){					 
					 lst_disp_for_edit.add(0,e);
					 request.setAttribute("bureauDetails",lst_disp_for_edit);
				 }
			} 
			msg="success";
		}else{
		    
			/**  
			* Search Bureau 
			*/
								
			action="F";
								
			lst = serviceBureauDAO.search(deptSearchDTO,action);
			
			Iterator it = lst.iterator();
			 while (it.hasNext()) 
			{
				 ServiceBureauDTO e = (ServiceBureauDTO)it.next();				 
			} 
					
			if(lst.size()>0) {
				request.setAttribute("bureauList",lst);
				msg="success";
			} else { 
				msg = "failed";
				pmErrorList.add(IErrorCodes.SERVICE_BUREAU_FETCH_ERROR);
				logger.debug("Could not find List::" + IErrorCodes.SERVICE_BUREAU_FETCH_ERROR);
			}
		}

	    /**
		 * Show Message in UI on failure 
		 */	 
	} catch(AgreementSystemException se) {
		pmErrorList.add(se.getErrorCode());
		logger.debug("AgreementSystemException : "+se.getErrorCode());
	} catch(AgreementBusinessException be)	{
		pmErrorList.add(be.getErrorCode());
		logger.debug("AgreementBusinessException : "+be.getErrorCode());		
	}catch(CommonSystemException cs){
		pmErrorList.add(cs.getErrorCode());
		logger.debug("CommonSystemException : "+cs.getErrorCode());
	}
	catch(RuntimeException re) {
		pmErrorList.add(IErrorCodes.DEFAULT_ERROR);
		logger.debug("RuntimeException :: "+re);
	}
	  finally	{
		
		// If its failed, mapping.findForward to its own action
		if(msg.equals("failed")) {			
			if(pmErrorList==null)
				pmErrorList.add(IErrorCodes.DEFAULT_ERROR);
			request.setAttribute(OPEConstants.OPEERRORLIST,pmErrorList);
		}
		return mapping.findForward(msg);
	}
  }
}