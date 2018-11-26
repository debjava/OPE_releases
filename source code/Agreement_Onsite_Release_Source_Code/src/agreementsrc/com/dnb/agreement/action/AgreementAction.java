/********************************************************************************************
 * Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                       *
 * IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.      *
 * The Program or any portion thereof may not be reproduced in any form whatsoever except   *
 * as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd.*
 *																							*
 * File Name                   : AgreementAction.java	                                    *
 * Author                      : Anantaraj S							                    *
 *  * Creation Date            : 18-July-08                                                 *
 * Description                 : Action Class for agreement New ,Edit ,Previous,Next		* 								 									                 
 * Modification History        :                                                            *																						    
 * Version No.               Date               Brief description of change                 *
 *----------------------------------------------------------------------------------------- *
 *                       |                  |											    *
 *                       |                  |											    *
 *  --------------------------------------------------------------------------------------- *
 ********************************************************************************************/
package com.dnb.agreement.action;

import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dnb.agreement.DAO.AgreementDAO;
import com.dnb.agreement.DAO.DAOOpe;
import com.dnb.agreement.bean.AgreementBean;
import com.dnb.agreement.bean.AgreementCommonBean;
import com.dnb.agreement.common.OPEConstants;
import com.dnb.agreement.exception.AgreementBusinessException;
import com.dnb.agreement.exception.AgreementSystemException;
import com.dnb.agreement.exception.IErrorCodes;
import com.dnb.common.exception.CommonSystemException;
import com.dnb.common.session.SessionDataHandler;


public class AgreementAction extends Action{
	private Logger logger=Logger.getLogger(AgreementAction.class);
	boolean returnValue;
	List lst,agrList,agrMapList,serviceMapList;
	
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
	
	public ActionForward execute(ActionMapping mapping,
			ActionForm form,HttpServletRequest request,
			HttpServletResponse response)throws AgreementSystemException,AgreementBusinessException
	{
		String message="failed";
		String userId=null;
		Date businessDate=null;

		List opeErrorList=new ArrayList();
		List entityList=new ArrayList();

		AgreementCommonBean acb=null;
		acb=(AgreementCommonBean) form;
		SessionDataHandler sdh = SessionDataHandler
		.getSessionDataHandler(request.getSession());
//        AgreementBean agmtBean[] = acb.getFormItems();
		DAOOpe daoOpe=DAOOpe.getDAOOpe(1);
		AgreementDAO agreementDAO=daoOpe.getAgreementDAO();
		
		try {
			if (sdh != null) {
				userId = sdh.getCurrentUser();
				businessDate = (java.sql.Date)sdh.getCurrentBusinessDate();
			} else {
				logger.debug("Could not retreive data from session");
				throw new CommonSystemException(IErrorCodes.SESSION_LOST_ERROR,
				"LOST USER SESSION");
			}  	
	
			if(acb.getEditorHold().equals("NEW"))
			{
				request.setAttribute("requestAgreementId", acb.getInternalAgreementId());
				request.setAttribute("requestServices", acb.getSelectedServices());
				// logger.debug("acb.getSelectedServices() ====>>>>>"+acb.getSelectedServices());
				// logger.debug("acb.getAgreementId() ---> "+acb.getInternalAgreementId());
				// logger.debug("acb.getAgreementId() ---> "+acb.);
				
				message = createAction(acb,agreementDAO,userId,businessDate,opeErrorList);

			}else if(acb.getEditorHold().equals("EDIT")){

				message =editAction(acb,agreementDAO,userId,businessDate,opeErrorList);

			}else if(acb.getEditorHold().equals("PREVIOUS")){

				message=previousAction(request,acb, agreementDAO, userId, businessDate, opeErrorList);

			}else if(acb.getEditorHold().equals("NEXT")){

				message=nextAction(request, acb, agreementDAO, userId, businessDate, opeErrorList);			

			}else if(acb.getEditorHold().equals("CLEAR")){

				message=clearAction(request, acb,agreementDAO);			

			}			
		}catch (AgreementSystemException se) {
			opeErrorList.add(se.getErrorCode());
			logger.error("AgreementSytemException : " + se.getErrorCode());
		} catch (AgreementBusinessException be) {
			opeErrorList.add(be.getErrorCode());
			logger.error("AgreementBusinessException : " + be.getErrorCode());
		}catch (CommonSystemException cs){
			opeErrorList.add(cs.getErrorCode());
			logger.error("CommonSystemException : " + cs.getErrorCode());
		}
			finally{
			if (message.equals("failed")) {
				
				if (opeErrorList == null){
					opeErrorList.add(IErrorCodes.DEFAULT_ERROR);
				}
				entityList.add(acb);
				request.setAttribute(OPEConstants.OPEERRORLIST, opeErrorList);
				request.setAttribute("agreementDetails", entityList);
			}	
			return mapping.findForward(message);
		}		
	}

	/**
	 * method is used to insert new agreement record
	 * @param agrb
	 * @param acb
	 * @param agreementDAO
	 * @param userId
	 * @param businessDate
	 * @param opeErrorList
	 * @return
	 * @throws AgreementSystemException
	 * @throws AgreementBusinessException
	 */
	String createAction(AgreementCommonBean acb,
			AgreementDAO agreementDAO,
			String userId,Date businessDate, List opeErrorList)
	throws AgreementSystemException,AgreementBusinessException {

		String action="N";
		String message = "null";

		logger.debug("You are in create action");	
		
		returnValue=agreementDAO.insert(acb,userId,businessDate,action);		

		if(returnValue){
			logger.debug("Create agreement is successfull");
			message="success";
		}else{
			logger.debug("Create agreement is unsuccessful ");
			opeErrorList.add(IErrorCodes.AGREEMENT_INSERT_ERROR);
			message="failed";
		}		
		return message;
	}

	/**
	 * This method is used to edit the existing agreement
	 * @param agrb
	 * @param acb
	 * @param agreementDAO
	 * @param userId
	 * @param businessDate
	 * @param opeErrorList
	 * @return
	 * @throws AgreementSystemException
	 * @throws AgreementBusinessException
	 */
	String editAction(AgreementCommonBean acb,
			AgreementDAO agreementDAO,
			String userId,Date businessDate, List opeErrorList)
	throws AgreementSystemException,AgreementBusinessException {

		String action="E";
		String message = null;		
		logger.debug("You are in edit action");		
		returnValue=agreementDAO.edit(acb, userId, businessDate, action);		

		if(returnValue){
			logger.debug("Edit agreement is successfull");
			message="success";
		}else{
			logger.debug("Edit agreement is unsuccessful");
			opeErrorList.add(IErrorCodes.AGREEMENT_EDIT_ERROR);
			message="failed";
		}		
		return message;
	}

	/**
	 * This method is used to get previous version of agreement
	 * @param request
	 * @param agrb
	 * @param acb
	 * @param agreementDAO
	 * @param userId
	 * @param businessDate
	 * @param opeErrorList
	 * @return
	 * @throws AgreementSystemException
	 * @throws AgreementBusinessException
	 */
	String previousAction(HttpServletRequest request,AgreementCommonBean acb,
			AgreementDAO agreementDAO,
			String userId,Date businessDate, List opeErrorList)
	throws AgreementSystemException,AgreementBusinessException {

		logger.debug("You are in previous action.");

		int ver=Integer.parseInt(acb.getVersionNo())-1;	
		String ver_no=String.valueOf(ver);
		String message = null;			
		String action="S";

		acb.setVersionNo(ver_no);
		request.setAttribute("maxVersionNo", request.getParameter("lastVersionNo"));

		lst=agreementDAO.showVersion(acb, action);

		agrList=(List) lst.get(0);
		serviceMapList=(List) lst.get(1);

		if(agrList.size()>0) {
			request.setAttribute("agreementDetails",agrList);
			request.setAttribute("serviceMapDetails", serviceMapList);
			message="list";
		} else { 
			message = "failed";
			opeErrorList.add(IErrorCodes.AGREEMENT_VERSION_ERROR);
			logger.debug("Could not find previous List::" + IErrorCodes.AGREEMENT_VERSION_ERROR);
		}	
		return message;
	}

	/**
	 * This method is used to get next version of agreement
	 * @param request
	 * @param agrb
	 * @param acb
	 * @param agreementDAO
	 * @param userId
	 * @param businessDate
	 * @param opeErrorList
	 * @return
	 * @throws AgreementSystemException
	 * @throws AgreementBusinessException
	 */

	String nextAction(HttpServletRequest request,AgreementCommonBean acb,
			AgreementDAO agreementDAO,
			String userId,Date businessDate, List opeErrorList)
	throws AgreementSystemException,AgreementBusinessException {

		logger.debug("You are in next action.");

		int ver=Integer.parseInt(acb.getVersionNo())+1;

		String ver_no=String.valueOf(ver);
		String message = null;		
		String action="S";
		acb.setVersionNo(ver_no);
		request.setAttribute("maxVersionNo", request.getParameter("lastVersionNo"));
		lst=agreementDAO.showVersion(acb, action);

		agrList=(List) lst.get(0);
		serviceMapList=(List) lst.get(1);

		if(agrList.size()>0) {
			request.setAttribute("agreementDetails",agrList);
			request.setAttribute("serviceMapDetails", serviceMapList);
			message="list";
		} else { 
			message = "failed";
			opeErrorList.add(IErrorCodes.AGREEMENT_VERSION_ERROR);
			logger.debug("Could not find next List::" + IErrorCodes.AGREEMENT_VERSION_ERROR);
		}	
		return message;
	}	
	
	String clearAction(HttpServletRequest request,AgreementCommonBean acb,AgreementDAO agreementDAO)
	throws AgreementSystemException,AgreementBusinessException {

		logger.debug("You are in clear action.");
		String message= "failed";
		int returnValue=0;
		returnValue=agreementDAO.clear(acb);
		
		if(returnValue==1){
			message="success";
			request.setAttribute("result", returnValue);
		}else{
			logger.debug("Unable to Clear Agreement records");
			message="failed";
		}
		return message;
	}	
}
