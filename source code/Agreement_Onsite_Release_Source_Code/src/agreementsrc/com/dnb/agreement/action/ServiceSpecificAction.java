/********************************************************************************************
 * Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                       *
 * IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.      *
 * The Program or any portion thereof may not be reproduced in any form whatsoever except   *
 * as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd.*
 *																							*
 * File Name                   : ServiceSpecificAction.java	                                *
 * Author                      : Anantaraj S							                    *
 * Creation Date               : 04-Aug-08                                                  * 
 * Description                 : Action Class for Service specific New ,Edit,               *
 *                               Find Duplicate	service Id,GetBureau name, Delete Service   * 								 									                 
 * Modification History        :                                                            *																						    
 * Version No.               Date               Brief description of change                 *
 *----------------------------------------------------------------------------------------- *
 *                       |                  |											    *
 *                       |                  |											    *
 *  --------------------------------------------------------------------------------------- *
 ********************************************************************************************/
package com.dnb.agreement.action;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dnb.agreement.DAO.DAOOpe;
import com.dnb.agreement.DAO.ServiceSpecificDAO;
import com.dnb.agreement.DTO.ServiceSpecificDTO;

import com.dnb.agreement.bean.ServiceSpecificAccountBean;
import com.dnb.agreement.bean.ServiceSpecificCommonBean;
import com.dnb.agreement.bean.ServiceSpecificCustomerBean;
import com.dnb.agreement.common.OPEConstants;
import com.dnb.agreement.exception.AgreementBusinessException;
import com.dnb.agreement.exception.AgreementSystemException;
import com.dnb.agreement.exception.IErrorCodes;
import com.dnb.common.exception.CommonSystemException;
import com.dnb.common.session.SessionDataHandler;


public class ServiceSpecificAction extends Action{

	private Logger logger=Logger.getLogger(ServiceSpecificAction.class);
	boolean returnValue;
	List lst;
	
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
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
	throws AgreementSystemException,AgreementBusinessException
	{

		String message="failed";
		String userId=null;

		List opeErrorList=new ArrayList();
		List entityList=new ArrayList();

		Date businessDate=null;

		ServiceSpecificCommonBean sscb=null;
		ServiceSpecificAccountBean []ssab=null;
		ServiceSpecificCustomerBean [] ssCustomerBean=null;
		ServiceSpecificDTO ssDTO=null;
		
		sscb=(ServiceSpecificCommonBean) form;
		ssDTO=new ServiceSpecificDTO();
		ssab=sscb.getFormItems1();	
		ssCustomerBean=sscb.getFormItems();

		//obtain the user id from session

		SessionDataHandler sdh = SessionDataHandler
		.getSessionDataHandler(request.getSession());

		DAOOpe daoOPE=DAOOpe.getDAOOpe(1);
		ServiceSpecificDAO ssDAO=daoOPE.getServiceSpecificDAO();
		sscb.setAgreementId(request.getParameter("agreementId"));
		
		try {
			if (sdh != null) {
				userId = sdh.getCurrentUser();
				businessDate = (java.sql.Date)sdh.getCurrentBusinessDate();
			} else {
				logger.debug("Could not retreive data from session");
				throw new CommonSystemException(IErrorCodes.SESSION_LOST_ERROR,
				"LOST USER SESSION");
			}
		
			try {
				PropertyUtils.copyProperties(ssDTO, sscb);
			} catch(NoSuchMethodException nsme){
				logger.error("No such method exception "+ nsme);
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
	
			if(sscb.getEditorHold().equals("NEW")){

				message = createAction(ssab,ssCustomerBean,sscb,ssDAO,userId,businessDate,opeErrorList);
				String versionNo = message.substring(message.lastIndexOf("~")+1,message.length());
				message = message.substring(0, message.lastIndexOf("~")); 
				
				request.setAttribute("versionNo", versionNo);

			}else if(sscb.getEditorHold().equals("EDIT")){

				message=editAction(ssab,ssCustomerBean,sscb,ssDAO,userId,businessDate, opeErrorList);
				String versionNo = message.substring(message.lastIndexOf("~")+1,message.length());
				message = message.substring(0, message.lastIndexOf("~")); 
				
				request.setAttribute("versionNo", versionNo);

			}else if(sscb.getEditorHold().equals("CHECK_SERVICE_ID")){

				message=checkDuplicateService(request,sscb,ssDAO,opeErrorList);

			}else if(sscb.getEditorHold().equals("GET_BUREAU_NAME")){

				message=getBureauNameAction(request,sscb,ssDAO,opeErrorList);

			}else if(sscb.getEditorHold().equals("NEW_SERVICE")){

				message=createServiceAction(request,sscb,ssDAO,opeErrorList);

			}else if(sscb.getEditorHold().equals("DELETE_SERVICE")){

				message=deleteServiceAction(request,sscb,ssDAO,opeErrorList);

			}else if(sscb.getEditorHold().equals("EDIT_SERVICE")){

				message=editServiceAction(request,sscb,ssDAO,opeErrorList);
			}

		}catch (AgreementSystemException se) {
			opeErrorList.add(se.getErrorCode());
			logger.error("AgreementSytemException : " + se.getErrorCode());
		} catch (AgreementBusinessException be) {
			opeErrorList.add(be.getErrorCode());
			logger.error("AgreementBusinessException : " + be.getErrorCode());
		}catch(CommonSystemException cs){
			opeErrorList.add(cs.getErrorCode());
			logger.error("CommonSystemException : " + cs.getErrorCode());
		}
		finally{
			if (message.equals("failed")) {
				if (opeErrorList == null){
					opeErrorList.add(IErrorCodes.DEFAULT_ERROR);
				}
				entityList.add(sscb);
				request.setAttribute(OPEConstants.OPEERRORLIST, opeErrorList);
				request.setAttribute("agreementServiceDetails", entityList);
			}	
			return mapping.findForward(message);
		}
	}

	/**
	 * This method is used to insert new service specifcations type
	 * @param ssab
	 * @param ssCustomerBean
	 * @param sscb
	 * @param ssDAO
	 * @param userId
	 * @param businessDate
	 * @param opeErrorList
	 * @return string
	 * @throws AgreementSystemException
	 * @throws AgreementBusinessException
	 */
	String createAction(ServiceSpecificAccountBean[]ssab,ServiceSpecificCustomerBean [] ssCustomerBean,ServiceSpecificCommonBean sscb,
			ServiceSpecificDAO ssDAO,String userId,Date businessDate,List opeErrorList)
	throws AgreementSystemException,AgreementBusinessException
	{

		String action="N";
		String message = null;

		logger.debug("You are in create action");

		int versionNo = ssDAO.insert(ssab,ssCustomerBean, sscb,userId, businessDate, action);

		if(versionNo>0){
			logger.debug("Create service specific is successfull");
			message="success"+"~"+versionNo;
		}else{
			logger.debug("Create service specific new was unsuccessfull");
			opeErrorList.add(IErrorCodes.SERVICE_SPECIFIC_INSERT_ERROR);
			message = "failed"+"~"+sscb.getVersionNo();
		}
		return message;
	}


	/**
	 * This method is used to edit the service specifications type
	 * @param ssab
	 * @param ssCustomerBean
	 * @param sscb
	 * @param ssDAO
	 * @param userId
	 * @param businessDate
	 * @param opeErrorList
	 * @return string
	 * @throws AgreementSystemException
	 * @throws AgreementBusinessException
	 */

	String editAction(ServiceSpecificAccountBean[]ssab,ServiceSpecificCustomerBean [] ssCustomerBean,
			ServiceSpecificCommonBean sscb,
			ServiceSpecificDAO ssDAO,String userId,Date businessDate,List opeErrorList)
	throws AgreementSystemException,AgreementBusinessException
	{
		String action="E";
		String message = null;

		logger.debug("You are in edit action");
		int versionNo=ssDAO.edit(ssab,ssCustomerBean,sscb,userId, businessDate, action);

		if(versionNo!=0){
			
			logger.debug(" Edit service specific is successfull ");
			message="success"+"~"+versionNo;
		}else{
			logger.debug("Edit service specific  was unsuccessful");
			opeErrorList.add(IErrorCodes.SERVICE_SPECIFIC_EDIT_ERROR);
			message = "failed";
			message="failed"+"~"+sscb.getVersionNo();
		}
		return message;
	}
	
	/**
	 * This method is used to get the bureau name based on bureau id,
	 * sent in ajax call in service specifications
	 * @param request
	 * @param sscb
	 * @param ssDAO
	 * @param opeErrorList
	 * @return string
	 * @throws AgreementSystemException
	 * @throws AgreementBusinessException
	 */

	String getBureauNameAction(HttpServletRequest request,ServiceSpecificCommonBean sscb,
			ServiceSpecificDAO ssDAO,List opeErrorList)
	throws AgreementSystemException,AgreementBusinessException
	{
		List lst=null;
		String message=null;

		logger.debug("you are in getBureauNameAction");

		lst=ssDAO.getBureauName(sscb); 

		if(lst.size()>0){
			message="list";
			request.setAttribute("bureauNameList", lst);
		}else{
			logger.debug("Unable to get bureau name");
			message="failed";
		}
		return message;
	}

	/**
	 * this method is used to delete service id's from temp table 
	 * @param request
	 * @param sscb
	 * @param ssDAO
	 * @param opeErrorList
	 * @return string
	 * @throws AgreementSystemException
	 * @throws AgreementBusinessException
	 */
	String deleteServiceAction(HttpServletRequest request,ServiceSpecificCommonBean sscb,
			ServiceSpecificDAO ssDAO,List opeErrorList)
	throws AgreementSystemException,AgreementBusinessException
	{
		int returnValue=0;
		String message="failed";

		logger.debug("you are in Delete Service Specification Action");

		returnValue=ssDAO.deleteService(sscb); 

		if(returnValue==1){
			message="list";
			request.setAttribute("result", returnValue);
		}else{
			logger.debug("Unable to Delete Service Specification");
			message="failed";
		}
		return message;
	}
	
	/**
	 * this method is used to insert only service id , 
	 * while creating new service specifications
	 * @param request
	 * @param sscb
	 * @param ssDAO
	 * @param opeErrorList
	 * @return string
	 * @throws AgreementSystemException
	 * @throws AgreementBusinessException
	 */

	String createServiceAction(HttpServletRequest request,ServiceSpecificCommonBean sscb,
			ServiceSpecificDAO ssDAO,List opeErrorList)
	throws AgreementSystemException,AgreementBusinessException
	{

		boolean ret;
		String message="failed";

		logger.debug("you are in Create Service Specification Action");

		ret=ssDAO.insertServiceId(sscb); 

		if(ret==true){
			message="success";
		}else{
			logger.debug("Unable to create Service Specification");
			message="failed";
		}
		return message;
	}

	/**
	 * this method is used to edit the only service id's 
	 * @param request
	 * @param sscb
	 * @param ssDAO
	 * @param opeErrorList
	 * @return string
	 * @throws AgreementSystemException
	 * @throws AgreementBusinessException
	 */
	String editServiceAction(HttpServletRequest request,ServiceSpecificCommonBean sscb,
			ServiceSpecificDAO ssDAO,List opeErrorList)
	throws AgreementSystemException,AgreementBusinessException
	{

		boolean ret;
		String message="failed";

		logger.debug("you are in edit Service Specification Action");

		ret=ssDAO.editServiceId(sscb); 

		if(ret==true){
			message="success";
		}else{
			logger.debug("Unable to edit Service Specification");
			message="failed";
		}
		return message;
	}

	/**
	 * this method is used to find the duplicate service id is existing or not
	 * @param request
	 * @param sscb
	 * @param ssDAO
	 * @param opeErrorList
	 * @return string
	 * @throws AgreementSystemException
	 * @throws AgreementBusinessException
	 */
	String checkDuplicateService(HttpServletRequest request,ServiceSpecificCommonBean sscb,
			ServiceSpecificDAO ssDAO,List opeErrorList)
	throws AgreementSystemException,AgreementBusinessException
	{

		int returnVal;
		String message="failed";
		logger.debug("Inside check duplicate service method ");

		returnVal=ssDAO.checkDuplicateServiceId(sscb);

		if(returnVal==0){
			message="success";
			request.setAttribute("result", returnVal);
		}else{
			logger.debug("No ServiceId and Bureau ID combination existing in database");
			message="failed";
			request.setAttribute("result", returnVal);
		}
		return message;
	}

}
