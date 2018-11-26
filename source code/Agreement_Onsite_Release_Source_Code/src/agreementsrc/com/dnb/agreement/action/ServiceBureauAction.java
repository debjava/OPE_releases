
/********************************************************************************************
 * Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                        *
 * IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.       *
 * The Program or any portion thereof may not be reproduced in any form whatsoever except    *
 * as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd. *
 *												                        					 *
 * File Name                   : ServiceBureauAction.java                                    *
 * Author                      : Manjunath N G                                               *
 * Creation Date               : 28-July-2008                                                *
 * Description                 : This file serves as the action file which calls the DAO     *
 *								for database connection and does the Service Bureau action.  *
 * Modification History        :                                                             *
 *																						     *
 *     Version No.               Date               Brief description of change              *
 *  ---------------------------------------------------------------------------------------  *
 *                       |                  |											     *
 *                       |                  |											     *
 *  ---------------------------------------------------------------------------------------  *
 *********************************************************************************************/

package com.dnb.agreement.action;

import java.lang.reflect.InvocationTargetException;
import java.sql.Date;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dnb.agreement.DTO.ServiceBureauDTO;
import com.dnb.agreement.bean.ServiceBureauBean;
import com.dnb.agreement.exception.IErrorCodes;
import com.dnb.agreement.exception.AgreementBusinessException;
import com.dnb.agreement.exception.AgreementSystemException;
import com.dnb.agreement.common.OPEConstants;
import com.dnb.common.exception.CommonSystemException;
import com.dnb.common.session.SessionDataHandler;
import com.dnb.agreement.DAO.DAOOpe;
import com.dnb.agreement.DAO.ServiceBureauDAO;

public class ServiceBureauAction extends Action {

	boolean result_ret;
	List lst;	

	static Logger logger = Logger.getLogger(ServiceBureauAction.class);

	/**
	 * Returns the <code>ActionForward</code> named "success" if one is
	 * configured or <code>null</code>if it cannot be found.
	 * 
	 * Searches first for a local forward, then a global forward.
	 * 
	 * @param mapping
	 *            The ActionMapping used to select this instance
	 * @param form
	 *            The optional ActionForm bean for this request (if any)
	 * @param request
	 *            The HTTP request we are processing
	 * @param response
	 *            The HTTP response we are creating
	 * 
	 * @exception Exception
	 *                if mapping.findForward throws an Exception
	 * 
	 * @return the "success" ActionForward, or null if it cannot be found
	 */

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws AgreementSystemException, AgreementBusinessException {

		String msg = "failed";
		String userId = null;
		List entityList = new ArrayList();		
		List opeErrorList = new ArrayList();

		Date businessDate = null;
		ServiceBureauBean bureauBean = null;
		ServiceBureauDTO bureauDTO = null;

		bureauBean = (ServiceBureauBean) form;			
		bureauDTO = new ServiceBureauDTO();

		bureauBean.setCreatedOn(request.getParameter("createdOn"));
		bureauBean.setLastUpdatedOn(request.getParameter("lastUpdatedOn"));
		bureauBean.setAuthorizedOn(request.getParameter("authorizedOn"));
		
		// Copy the properties from the Bean to the DTO
		try{
			PropertyUtils.copyProperties(bureauDTO, bureauBean);
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

		try{		
//			Obtain the user id from session
			SessionDataHandler sdh = SessionDataHandler.getSessionDataHandler(request.getSession());

			if (sdh != null) {
				userId = sdh.getCurrentUser();
				businessDate = (java.sql.Date)sdh.getCurrentBusinessDate();
			} else {
				logger.debug("Could not retreive data from session");
				throw new CommonSystemException(IErrorCodes.SESSION_LOST_ERROR,
				"LOST USER SESSION");
			}

			if (bureauBean.getEditorHold().equals("NEW")) {				
				msg = createAction(request, bureauDTO, userId, businessDate, opeErrorList);
			}	else if(bureauBean.getEditorHold().equals("EDIT")) {				
				msg = editAction(request, bureauDTO, userId, businessDate, opeErrorList);
			}	else if (bureauBean.getEditorHold().equals("PREVIOUS")) {			
				msg = previousAction(request, bureauDTO, userId,businessDate,opeErrorList);							
			} 	else if (bureauBean.getEditorHold().equals("NEXT")) {
				msg = nextAction(request, bureauDTO,userId,businessDate,opeErrorList);
			}	else if (bureauBean.getEditorHold().equals("CREATE_PATUID")) {				
				msg = createPatuId(request, bureauDTO,opeErrorList);
			}

		} catch (AgreementSystemException se) {
			opeErrorList.add(se.getErrorCode());
			logger.error("AgreementSytemException : " + se.getErrorCode());
		} catch (AgreementBusinessException be) {
			opeErrorList.add(be.getErrorCode());
			logger.error("AgreementBusinessException : " + be.getErrorCode());
		} catch (CommonSystemException cs){
			opeErrorList.add(cs.getErrorCode());
			logger.error("CommonSystemException : " + cs.getErrorCode());
		}
		finally{
			if (msg.equals("failed")) {
				if (opeErrorList == null){
					opeErrorList.add(IErrorCodes.DEFAULT_ERROR);
				}
				entityList.add(bureauBean);
				request.setAttribute(OPEConstants.OPEERRORLIST, opeErrorList);
				request.setAttribute("bureauDetails", entityList);
			}		
			return mapping.findForward(msg);
		}
	}

	/**
	 * used to insert new service bureaus details
	 * @param request
	 * @param deptDTO
	 * @param userId
	 * @param businessDate
	 * @param opeErrorList
	 * @return string
	 * @throws AgreementSystemException
	 * @throws AgreementBusinessException
	 */
	public String createAction(HttpServletRequest request, ServiceBureauDTO deptDTO,			
			String userId, Date businessDate,List opeErrorList) throws AgreementSystemException,
			AgreementBusinessException {

		String action = "N";
		String message = null;

		logger.debug("You are in create action.");

		/** 
		 * Create the required DAO-Factory 
		 */			
		DAOOpe daoOpe = DAOOpe.getDAOOpe(1);

		/**  
		 * Create an ServiceBureauDAO 
		 */
		ServiceBureauDAO serviceBureauDAO = daoOpe.getServiceBureauDAO();

		result_ret = serviceBureauDAO.insert(deptDTO, userId, businessDate, action);

		if (result_ret) {
			logger.debug("Create service bureau was successful.");		
			message = "success";
		} else {
			logger.debug("Create service bureau was unsuccessful");
			opeErrorList.add(IErrorCodes.SERVICE_BUREAU_INSERT_ERROR);
			message = "failed";
		}
		return message;
	}

	/**
	 * used to edit service bureau details
	 * @param request
	 * @param deptDTO
	 * @param userId
	 * @param businessDate
	 * @param opeErrorList
	 * @return string
	 * @throws AgreementSystemException
	 * @throws AgreementBusinessException
	 */

	public String editAction(HttpServletRequest request, ServiceBureauDTO deptDTO,			
			String userId, Date businessDate,List opeErrorList) throws AgreementSystemException,
			AgreementBusinessException {

		String action = "E";
		String message = null;

		logger.debug("You are in edit action.");

		/** 
		 * Create the required DAO-Factory 
		 */			
		DAOOpe daoOpe = DAOOpe.getDAOOpe(1);

		/**  
		 * Create an ServiceBureauDAO
		 */			
		ServiceBureauDAO serviceBureauDAO = daoOpe.getServiceBureauDAO();						

		result_ret = serviceBureauDAO.edit(deptDTO, userId, businessDate, action);

		if (result_ret) {
			logger.debug("Edit service bureau was successful.");
			opeErrorList.add(IErrorCodes.SERVICE_BUREAU_EDIT_SUCCESSFUL);
			message = "success";
		} else {
			logger.debug("Edit service bureau was unsuccessful");
			opeErrorList.add(IErrorCodes.SERVICE_BUREAU_EDIT_ERROR);
			message = "failed";
		}
		return message;
	}		


	/**
	 * used get the previous version of the service bureau details
	 * @param request
	 * @param deptDTO
	 * @param userId
	 * @param businessDate
	 * @param opeErrorList
	 * @return string
	 * @throws AgreementSystemException
	 * @throws AgreementBusinessException
	 */

	public String previousAction(HttpServletRequest request, ServiceBureauDTO deptDTO,			
			String userId, Date businessDate,List opeErrorList) throws AgreementSystemException,
			AgreementBusinessException {

		int ver = Integer.parseInt(deptDTO.getVersionNo()) - 1;
		String ver_no = String.valueOf(ver);
		deptDTO.setVersionNo(ver_no);
		String action = "S";
		boolean flag = false;
		String message = null;		
		List lst = null;

		logger.debug("You are in previous action.");

		// Setting the attribute of maximum no. of version to display version status 
		// in audit trail of jsp page. for example 1 of 4,2 of 4,3 of 4,4 of 4 

		request.setAttribute("maxVersionNo", request.getParameter("lastVersionNo"));

		// Show previous bureau

		/** 
		 * Create the required DAO-Factory 
		 */			
		DAOOpe daoOpe = DAOOpe.getDAOOpe(1);

		/**  
		 * Create an ServiceBureauDAO 
		 */
		ServiceBureauDAO serviceBureauDAO = daoOpe.getServiceBureauDAO();

		lst = serviceBureauDAO.showVersion(deptDTO,action);

		Iterator it = lst.iterator();

		while (it.hasNext()) {
			ServiceBureauDTO e = (ServiceBureauDTO) it.next();
			flag = true;
		}

		if(flag && lst!=null){				
			request.setAttribute("bureauDetails", lst);
			message="list";
		}else {				
			opeErrorList.add(IErrorCodes.SERVICE_BUREAU_VERSION_ERROR);
			message="failed";
			logger.debug("Unable to get previous Records.");
		}	
		return message;
	}
	
	/**
	 * used to get next versions of service bureaus
	 * @param request
	 * @param deptDTO
	 * @param userId
	 * @param businessDate
	 * @param opeErrorList
	 * @return string
	 * @throws AgreementSystemException
	 * @throws AgreementBusinessException
	 */

	public String nextAction(HttpServletRequest request, ServiceBureauDTO deptDTO,			 
			String userId, Date businessDate,List opeErrorList) throws AgreementSystemException,
			AgreementBusinessException {

		boolean flag = false;
		int ver = Integer.parseInt(deptDTO.getVersionNo()) + 1;
		String ver_no = String.valueOf(ver);
		deptDTO.setVersionNo(ver_no);

		String action = "S";		
		List lst = null;
		String message = null;

		logger.debug("You are in next action.");

		// Setting the attribute of maximum no. of version to display version status 
		// in audit trail of jsp page. for example 1 of 4,2 of 4,3 of 4,4 of 4 

		request.setAttribute("maxVersionNo", request.getParameter("lastVersionNo"));

		/** 
		 * Create the required DAO-Factory 
		 */			
		DAOOpe daoOpe = DAOOpe.getDAOOpe(1);

		/**  
		 * Create an ServiceBureauDAO 
		 */
		ServiceBureauDAO serviceBureauDAO = daoOpe.getServiceBureauDAO();

		lst = serviceBureauDAO.showVersion(deptDTO,action);

		Iterator it = lst.iterator();

		while (it.hasNext()) {
			ServiceBureauDTO e = (ServiceBureauDTO) it.next();
			flag = true;
		}

		if(flag && lst!=null){				
			request.setAttribute("bureauDetails", lst);
			message="list";
		}else {				
			opeErrorList.add(IErrorCodes.SERVICE_BUREAU_VERSION_ERROR);
			message="failed";
			logger.debug("Unable to get Next Records.");
		}	

		return message;
	}

	/**
	 * used to create patu id 
	 * @param request
	 * @param deptDTO
	 * @param opeErrorList
	 * @return string
	 * @throws AgreementSystemException
	 * @throws AgreementBusinessException
	 */
	public String createPatuId(HttpServletRequest request, ServiceBureauDTO deptDTO,				
			List opeErrorList) throws AgreementSystemException,
			AgreementBusinessException {

		String patuId = null;
		String message = "";

		logger.debug("You are in create patu id action");			

		/** 
		 * Create the required DAO-Factory 
		 */			
		DAOOpe daoOpe = DAOOpe.getDAOOpe(1);

		/**  
		 * Create an ServiceBureauDAO 
		 */
		ServiceBureauDAO serviceBureauDAO = daoOpe.getServiceBureauDAO();

		patuId = serviceBureauDAO.createPatuId(deptDTO);

		request.setAttribute("patu_id",patuId);

		if(patuId!=null){
			message="list";				 
		}else{
			message="failed";	            
			logger.debug("Unable to generate patu id");
		}
		return message;
	}
}