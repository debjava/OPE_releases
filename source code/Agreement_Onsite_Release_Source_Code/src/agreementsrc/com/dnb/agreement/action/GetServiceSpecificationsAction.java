/********************************************************************************************
 * Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                       *
 * IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.      *
 * The Program or any portion thereof may not be reproduced in any form whatsoever except   *
 * as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd.*
 *																							*
 * File Name                   : GetServiceSpecificationsAction.java	                        *
 * Author                      : Anantaraj S							                    *
 * Creation Date               : 12-Nov-08                                                  *
 * Description                 : Action Class for getting the service specification data	* 								 									                 
 * Modification History        :                                                            *																						    
 * Version No.               Date               Brief description of change                 *
 *----------------------------------------------------------------------------------------- *
 *                       |                  |											    *
 *                       |                  |											    *
 *  --------------------------------------------------------------------------------------- *
 ********************************************************************************************/
package com.dnb.agreement.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.log4j.Logger;

import com.dnb.agreement.DAO.DAOOpe;
import com.dnb.agreement.DAO.ServiceSpecificDAO;
import com.dnb.agreement.bean.ServiceSpecificCommonBean;
import com.dnb.agreement.common.OPEConstants;
import com.dnb.agreement.exception.AgreementBusinessException;
import com.dnb.agreement.exception.AgreementSystemException;
import com.dnb.agreement.exception.IErrorCodes;

public class GetServiceSpecificationsAction extends Action{

	Logger logger= Logger.getLogger(GetServiceSpecificationsAction.class);
	List lst,serviceList,accountMapList,customerMapList;

	public ActionForward execute(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
	{

		logger.debug("Inside GetServiceSpecificationsAction ");

		List opeErrorList = new ArrayList();
		String message="failed";
		ServiceSpecificCommonBean sscb=null;
		sscb=(ServiceSpecificCommonBean)form;

		DAOOpe daoOPE=DAOOpe.getDAOOpe(1);
		ServiceSpecificDAO ssDAO=daoOPE.getServiceSpecificDAO();

		try {

			lst = ssDAO.getService(sscb);

			serviceList=(List) lst.get(0);
			customerMapList=(List) lst.get(1);
			accountMapList=(List) lst.get(2);

			if(serviceList.size()>0) {
				request.setAttribute("serviceDetails",serviceList);
				request.setAttribute("customerDetails", customerMapList);
				request.setAttribute("accountDetails", accountMapList);
				message="success";
			} else { 
				message = "failed";
				logger.debug("Could not find List::" + IErrorCodes.SERVICE_SPECIFIC_FETCH_ERROR);
			}


		} catch(AgreementSystemException se) {
			opeErrorList.add(se.getErrorCode());
			logger.debug("AgreementSystemException : "+se.getErrorCode());
		} catch(AgreementBusinessException be)	{
			opeErrorList.add(be.getErrorCode());
			logger.debug("AgreementBusinessException : "+be.getErrorCode());		
		}catch(RuntimeException re) {
			opeErrorList.add(IErrorCodes.DEFAULT_ERROR);
			logger.debug("RuntimeException :: "+re);
		}

		finally{
			if(message.equals("failed")){
				if(opeErrorList==null)
					opeErrorList.add(IErrorCodes.DEFAULT_ERROR);		
				request.setAttribute(OPEConstants.OPEERRORLIST,opeErrorList);
			}
			return mapping.findForward(message);
		}

	}

}

