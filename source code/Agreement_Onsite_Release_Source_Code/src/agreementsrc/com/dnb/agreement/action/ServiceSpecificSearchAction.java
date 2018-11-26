/********************************************************************************************
 * Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                       *
 * IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.      *
 * The Program or any portion thereof may not be reproduced in any form whatsoever except   *
 * as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd.*
 *																							*
 * File Name                   : ServiceSpecificSearchAction.java	                        *
 * Author                      : Anantaraj S							                    *
 * Creation Date               : 18-July-08                                                 *
 * Description                 : Action Class for Searching service specifications		    * 								 									                 
 * Modification History        :                                                            *																						    
 * Version No.               Date               Brief description of change                 *
 *----------------------------------------------------------------------------------------- *
 *                       |                  |											    *
 *                       |                  |											    *
 *  --------------------------------------------------------------------------------------- *
 ********************************************************************************************/

package com.dnb.agreement.action;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.ArrayList;
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
import com.dnb.agreement.bean.ServiceSpecificCommonBean;
import com.dnb.agreement.exception.AgreementBusinessException;
import com.dnb.agreement.exception.AgreementSystemException;
import com.dnb.agreement.exception.IErrorCodes;
import com.dnb.common.exception.CommonSystemException;
import com.dnb.common.session.SessionDataHandler;

public class ServiceSpecificSearchAction extends Action{

	Logger logger=Logger.getLogger(ServiceSpecificSearchAction.class);
	List serviceList,accountMapList,lst;

	
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
		logger.debug(" you are inside ServiceSpecificSearchAction ");
		List opeErrorList = new ArrayList();
		String message="failed";
		ServiceSpecificCommonBean sscb=null;
		ServiceSpecificDTO ssDTO=null;

		sscb=(ServiceSpecificCommonBean) form;
		ssDTO=new ServiceSpecificDTO();

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

		DAOOpe daoOPE=DAOOpe.getDAOOpe(1);
		ServiceSpecificDAO ssDAO=daoOPE.getServiceSpecificDAO();

		try {

			if(ssDTO.getVersionNo()!=null)
			{
				// Setting the attribute of maximum no. of version to display version status 
				// in audit trail of jsp page. for example 1 of 4,2 of 4,3 of 4,4 of 4 

				request.setAttribute("maxVersionNo", ssDTO.getVersionNo());		
				String action="F";

				lst = ssDAO.search(sscb,action);
				serviceList=(List) lst.get(0);

				if(serviceList.size()>0) {
					request.setAttribute("serviceDetails",serviceList);
					message="success";
				} else { 
					message = "failed";
					opeErrorList.add(IErrorCodes.SERVICE_SPECIFIC_FETCH_ERROR);
					logger.debug("Could not find List::" + IErrorCodes.SERVICE_SPECIFIC_FETCH_ERROR);
				}

			} 

		}catch(AgreementSystemException se) {
			opeErrorList.add(se.getErrorCode());
			logger.debug("AgreementSystemException : "+se.getErrorCode());
		} catch(AgreementBusinessException be)	{
			opeErrorList.add(be.getErrorCode());
			logger.debug("AgreementBusinessException : "+be.getErrorCode());		
		} catch(RuntimeException re) {
			opeErrorList.add(IErrorCodes.DEFAULT_ERROR);
			logger.debug("RuntimeException :: "+re);
		}
		finally{
			return mapping.findForward(message);
		}


	}
}


