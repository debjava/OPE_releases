/********************************************************************************************
 * Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                       *
 * IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.      *
 * The Program or any portion thereof may not be reproduced in any form whatsoever except   *
 * as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd.*
 *																							*
 * File Name                   : AgreementPrintAction.java	                                *
 * Author                      : Anantaraj S							                    *
 * Creation Date               : 23-Sep-08                                                  *
 * Description                 : Action Class for Printing agreement details		        * 								 									                 
 * Modification History        :                                                            *																						    
 * Version No.               Date               Brief description of change                 *
 *----------------------------------------------------------------------------------------- *
 *                       |                  |											    *
 *                       |                  |											    *
 *  --------------------------------------------------------------------------------------- *
 ********************************************************************************************/

package com.dnb.agreement.action;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dnb.agreement.DAO.AgreementDAO;
import com.dnb.agreement.DAO.DAOOpe;
import com.dnb.agreement.DTO.ServiceAccountSpecificDTO;
import com.dnb.agreement.DTO.ServiceSpecificDTO;
import com.dnb.agreement.bean.AgreementCommonBean;
import com.dnb.agreement.common.OPEConstants;
import com.dnb.agreement.exception.AgreementBusinessException;
import com.dnb.agreement.exception.AgreementSystemException;
import com.dnb.agreement.exception.IErrorCodes;
import com.dnb.common.exception.CommonSystemException;
import com.dnb.common.session.SessionDataHandler;

public class AgreementPrintAction extends Action{

	Logger logger = Logger.getLogger(AgreementPrintAction.class); 
	List agreementList,serviceCustomerIdList,serviceList,serviceSpecificationList,serviceAccountList;
	List serviceBureauList,kekAukList,lst,accountsList;

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
		List opeErrorList = new ArrayList();
		String userId;
		Date businessDate;
		List list=null;
		
		String message="failed";
		logger.debug("Inside Agreement Print Action");
		AgreementCommonBean agreementBean=null;	

		agreementBean=(AgreementCommonBean) form;


		DAOOpe daoOpe=DAOOpe.getDAOOpe(1);
		AgreementDAO agreementDAO=daoOpe.getAgreementDAO();
		
		SessionDataHandler sdh = SessionDataHandler
		.getSessionDataHandler(request.getSession());
		
		try
		{
			if (sdh != null) {
				userId = sdh.getCurrentUser();
				businessDate = (java.sql.Date)sdh.getCurrentBusinessDate();
			} else {
				logger.debug("Could not retreive data from session");
				throw new CommonSystemException(IErrorCodes.SESSION_LOST_ERROR,
				"LOST USER SESSION");
			}  	
			
			lst=agreementDAO.print(agreementBean);

			agreementList=(List) lst.get(0);
			serviceList= (List) lst.get(1);
			serviceSpecificationList=(List)lst.get(2);
			serviceCustomerIdList=(List)lst.get(3);
			serviceAccountList=(List)lst.get(4);
			serviceBureauList=(List)lst.get(5);
			kekAukList=(List)lst.get(6);
			
			/**
			 * following conditions for displaying only unique account numbers under 
			 * File Transfer Service heading in agreement print
			 */
			
			if(serviceAccountList.size()>0)
			{
				Set accountSet = new HashSet();
				for(int i=0;i< serviceAccountList.size();i++)
				{
					accountSet.add(serviceAccountList.get(i));
				}
				list = new ArrayList(accountSet);
			}
	
			
			List accountsList= new ArrayList();
			List accountsList1= new ArrayList();
			
			/**
			 * Following condition for printing all the account numbers belong to specific services
			 */						
			if(serviceSpecificationList.size()>0 && serviceAccountList.size()>0)
			{
					HashMap serviceMap = null;
					for(int k=0;k<serviceSpecificationList.size();k++)
					{
						ServiceSpecificDTO specificDTO = (ServiceSpecificDTO) serviceSpecificationList.get(k);
						String serviceCode = specificDTO.getServiceCode();
						String internalRefId2 = specificDTO.getInternalRefId();
						HashMap accountMap = new HashMap();
						
						for(int i=0;i< serviceAccountList.size();i++)
						{
							
							ServiceAccountSpecificDTO accountDTO = (ServiceAccountSpecificDTO) serviceAccountList.get(i);
							String accountNum = accountDTO.getAccountNum();
							String internalRefId1 = accountDTO.getInternalRefId();
						
							if(internalRefId2.equals(internalRefId1))
							{
								
								accountMap.put(accountNum,serviceCode);
								if(serviceMap==null){
									serviceMap = new HashMap();
								}
					
								if(!serviceMap.containsValue(serviceCode)) {
								
									if(!serviceMap.isEmpty()) {								
										HashMap serviceMapNew = new HashMap();
										serviceMapNew.putAll(serviceMap);
										accountsList.add(serviceMapNew);
										serviceMap.clear();	
										
									}
								}	
								serviceMap.putAll(accountMap);
								
							}
							if(k==serviceSpecificationList.size()-1 && i==serviceAccountList.size()-1){
								logger.debug("Inside last if condition");
									HashMap serviceMapNew = new HashMap();
									serviceMapNew.putAll(serviceMap);																
									accountsList.add(serviceMapNew);
									serviceMap.clear();
									
								}
						}
							
					}
					
					Iterator it1 = accountsList.iterator();
					while(it1.hasNext())
					{
						HashMap accMap = (HashMap) it1.next();
						
						logger.info("accMap : "+accMap);
						
						Set s = accMap.entrySet();
						Iterator it = s.iterator();
						while (it.hasNext())
						{
							ServiceAccountSpecificDTO ssDTO = new ServiceAccountSpecificDTO();
							Map.Entry me =(Map.Entry) it.next();
							ssDTO.setAccountNum((String)me.getKey());
							ssDTO.setServiceCode((String)me.getValue());
							accountsList1.add(ssDTO);
						}
					}		
			}
			
			if(agreementList.size()>0)
			{
				request.setAttribute("agmtDetails",agreementList);
				request.setAttribute("services", serviceList);
				request.setAttribute("serviceSpecifications", serviceSpecificationList);
				request.setAttribute("agmtMapDetails", serviceCustomerIdList);
				request.setAttribute("serviceAccounts", list);
				request.setAttribute("serviceBureaus", serviceBureauList);
				request.setAttribute("kekAuk", kekAukList);
				request.setAttribute("accountsList", accountsList1);

				message="list";
			} else { 
				message = "failed";
				opeErrorList.add(IErrorCodes.AGREEMENT_FETCH_ERROR);
				logger.debug("Could not find List::" + IErrorCodes.AGREEMENT_FETCH_ERROR);
			}

		}catch(AgreementSystemException se) {
			opeErrorList.add(se.getErrorCode());
			logger.debug("AgreementSystemException : "+se.getErrorCode()+ " Failed to Fetch records for agreement ");
		} catch(AgreementBusinessException be)	{
			opeErrorList.add(be.getErrorCode());
			logger.debug("AgreementBusinessException : "+be.getErrorCode()+" Failed to Fetch records for agreement ");		
		} catch (CommonSystemException cs){
			opeErrorList.add(cs.getErrorCode());
			logger.error("CommonSystemException : " + cs.getErrorCode());
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
