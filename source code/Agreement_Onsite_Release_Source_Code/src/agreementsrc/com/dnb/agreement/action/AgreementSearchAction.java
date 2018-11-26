/********************************************************************************************
 * Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                       *
 * IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.      *
 * The Program or any portion thereof may not be reproduced in any form whatsoever except   *
 * as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd.*
 *																							*
 * File Name                   : AgreementSearchAction.java	                                *
 * Author                      : Anantaraj S							                    *
 * Creation Date               : 18-July-08                                                 *
 * Description                 : This class serves Agreement search						    * 								 									                 
 * Modification History        :                                                            *																						    
 * Version No.               Date               Brief description of change                 *
 *----------------------------------------------------------------------------------------- *
 *                       |                  |											    *
 *                       |                  |											    *
 *  --------------------------------------------------------------------------------------- *
 ********************************************************************************************/

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

import com.dnb.agreement.DAO.AgreementDAO;
import com.dnb.agreement.DAO.DAOOpe;
import com.dnb.agreement.DTO.AgreementDTO;
import com.dnb.agreement.DTO.AgreementSearchDTO;
import com.dnb.agreement.DTO.ServiceSpecificDTO;
import com.dnb.agreement.bean.AgreementSearchBean;
import com.dnb.agreement.common.OPEConstants;
import com.dnb.agreement.exception.AgreementBusinessException;
import com.dnb.agreement.exception.AgreementSystemException;
import com.dnb.agreement.exception.IErrorCodes;

import com.dnb.common.exception.CommonSystemException;
import com.dnb.common.session.SessionDataHandler;

public class AgreementSearchAction extends Action{

	Logger logger=Logger.getLogger(AgreementSearchAction.class);
	List lst,agreementList,serviceMapList;

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
	 * @throws CommonSystemException 
	 */
	
	public ActionForward execute(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
	throws AgreementSystemException,AgreementBusinessException
	{
		List listAgreement=new ArrayList();
		List listService=new ArrayList();	
		List opeErrorList = new ArrayList();

		String message="failed";
		String userId=null;
		String deleteMode=null;

		Date businessDate = null;		
		boolean agrReturn;

		AgreementSearchBean agmtSearchBean = null;
		AgreementSearchDTO agmtSearchDTO = null;

		agmtSearchBean=(AgreementSearchBean)form;
		agmtSearchDTO=new AgreementSearchDTO();
		//obtain the user id from session
		SessionDataHandler sdh = SessionDataHandler
		.getSessionDataHandler(request.getSession());
        
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

		
			try{
				PropertyUtils.copyProperties(agmtSearchDTO, agmtSearchBean);
			} catch(NoSuchMethodException nsme){
				logger.error("No such method exception "	+ nsme);
				throw new AgreementSystemException(IErrorCodes.NO_SUCH_METHOD_ERROR,
						"Could not copy data from Bean to DTO", nsme);
			} catch(InvocationTargetException ite){
				logger.error("Invocation Target Exception "	+ ite);
				throw new AgreementSystemException(IErrorCodes.INVOCATION_TARGET_ERROR,
						"Could not copy data from Bean to DTO", ite);
			} catch(IllegalAccessException iae){
				logger.error("Illegal Access Exception "+ iae);
				throw new AgreementSystemException(IErrorCodes.ILLEGAL_ACCESS_ERROR,
						"Could not copy data from Bean to DTO", iae);
			} 	
							
			if(agmtSearchDTO.getDeleteMe()!=null ){	
				if(!(agmtSearchDTO.getDeleteMe().equals(""))){
				try{
					deleteMode = request.getParameter("deleteMode");
				}catch(NullPointerException np){
					logger.error("No value found for delete mode.");
				}

				String action="D";	
				agrReturn = agreementDAO.delete(agmtSearchDTO,userId,businessDate,action,deleteMode);

				if(deleteMode.equals("D")){
					if(agrReturn){
						logger.debug("Records Deleted Successfully");	
						opeErrorList.add(IErrorCodes.AGREEMENT_DELETE_SUCCESSFUL);
						message="deleted";				
					}else{
						logger.debug("Could not Delete Records: ");
						opeErrorList.add(IErrorCodes.AGREEMENT_DELETE_ERROR);
						message="failed";
					}	
				}else if(deleteMode.equals("R")){
					if(agrReturn){
						logger.debug("Records Reopened Successfully");						
						message="deleted";				
					}else{
						logger.debug("Could not reopen records: ");
						opeErrorList.add(IErrorCodes.RECORD_REOPEN_ERROR);
						message="failed";
					}	
				}
			}	
			}
		
			if(agmtSearchDTO.getVersionNo()!=null)
			{
				// Setting the attribute of maximum no. of version to display version status 
				// in audit trail of jsp page. for example 1 of 4,2 of 4,3 of 4,4 of 4 

				request.setAttribute("maxVersionNo", agmtSearchDTO.getVersionNo());

				Iterator it1 = agreementList.iterator();
				while (it1.hasNext()) 
				{
					AgreementDTO e = (AgreementDTO)it1.next();
					if(e.getAgreementId().equals(agmtSearchDTO.getAgreementId()))
					{					 
						listAgreement.add(0,e);
						request.setAttribute("agreementDetails",listAgreement);
					}
				}

				Iterator it3 = serviceMapList.iterator();		
				while (it3.hasNext()) 
				{
					ServiceSpecificDTO e = (ServiceSpecificDTO)it3.next();
					if(e.getAgreementId().equals(agmtSearchDTO.getAgreementId()))
					{					 
						listService.add(0,e);
						request.setAttribute("serviceMapDetails",listService);
					}
				}	
				message="success";
			} 
			else
			{
				/**  
				 * Search Agreement 
				 */			
				String action="F";

				lst = agreementDAO.search(agmtSearchDTO,action);
				agreementList=(List) lst.get(0);
				serviceMapList=(List) lst.get(1);

				if(agreementList.size()>0) {
					request.setAttribute("agreementList",agreementList);
					request.setAttribute("serviceMapList", serviceMapList);
					message="success";
				} else { 
					message = "failed";
					opeErrorList.add(IErrorCodes.AGREEMENT_FETCH_ERROR);
					logger.debug("Could not find List::" + IErrorCodes.AGREEMENT_FETCH_ERROR);
				}
			}

		}catch(AgreementSystemException se) {
			opeErrorList.add(se.getErrorCode());
			logger.debug("AgreementSystemException : "+se.getErrorCode());
		} catch(AgreementBusinessException be)	{
			opeErrorList.add(be.getErrorCode());
			logger.debug("AgreementBusinessException : "+be.getErrorCode());		
		}catch(CommonSystemException cs){
			opeErrorList.add(cs.getErrorCode());
			logger.debug("CommonSystemException : "+cs.getErrorCode());
		}catch(RuntimeException re) {
			opeErrorList.add(IErrorCodes.DEFAULT_ERROR);
			logger.debug("RuntimeException :: "+re);
		}finally{

			if(message.equals("failed")){
				if(opeErrorList==null)
					opeErrorList.add(IErrorCodes.DEFAULT_ERROR);
				request.setAttribute(OPEConstants.OPEERRORLIST,opeErrorList);
			}
			return mapping.findForward(message);
		}
	}		 
}