/********************************************************************************************
 * Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                        *
 * IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.       *
 * The Program or any portion thereof may not be reproduced in any form whatsoever except    *
 * as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd. *
 *												                        					 *
 * File Name                   : GenerateKeyAction.java                                      *
 * Author                      : Manjunath N G                                               *
 * Creation Date               : 04-September-2008                                           *
 * Description                 : This file serves as the action file which calls the DAO     *
 *								for database connection and does the GenerateKeyAction action*
 * Modification History        :                                                             *
 *																						     *
 *     Version No.               Date               Brief description of change              *
 *  ---------------------------------------------------------------------------------------  *
 *                       |                  |											     *
 *                       |                  |											     *
 *  ---------------------------------------------------------------------------------------  *
 *********************************************************************************************/

package com.dnb.agreement.action;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.dnb.agreement.DAO.DAOOpe;
import com.dnb.agreement.DAO.ServiceBureauDAO;
import com.dnb.agreement.bean.KekAukServiceBureauBean;
import com.dnb.agreement.bean.ServiceBureauBean;
import com.dnb.agreement.exception.IErrorCodes;
import com.dnb.agreement.exception.AgreementBusinessException;
import com.dnb.agreement.exception.AgreementSystemException;
import com.dnb.agreement.key.KeyGenerator;
import com.dnb.agreement.common.OPEConstants;
import com.dnb.common.exception.CommonSystemException;
import com.dnb.common.session.SessionDataHandler;

public class GenerateKeyAction extends Action {

	boolean result_ret;
	List lst;	

	static Logger logger = Logger.getLogger(GenerateKeyAction.class);

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
		List entityList = new ArrayList();		
		List opeErrorList = new ArrayList();		
		ServiceBureauBean bureauBean = null;
		bureauBean= (ServiceBureauBean) form;		

		try{			
			if(bureauBean.getEditorHold().equals("GENERATE_KEYS")) {
				String status = request.getParameter("action");
				msg = generateKeys(request, bureauBean,opeErrorList,status);
				logger.debug("Calling generate key method");
			}else if (bureauBean.getEditorHold().equals("RESET_KEYS")) {	
				logger.debug("Calling reset key method");
				String status = request.getParameter("action");
				logger.debug("Calling reset key method");
				msg = resetKeyAction(request, bureauBean,opeErrorList,status);
			}else if (bureauBean.getEditorHold().equals("DELETE_KEYS")){
				String status = request.getParameter("action");
				logger.debug("Calling delete key method");				
				msg = deleteKeys(request, bureauBean,opeErrorList,status);
			}else if(bureauBean.getEditorHold().equals("KEY_DISPLAY")){
				String status = request.getParameter("action");
				logger.debug("Calling key display method");
				DAOOpe daoOpe = DAOOpe.getDAOOpe(1);
				ServiceBureauDAO serviceBureauDAO = daoOpe.getServiceBureauDAO();
				String internalBureauId = request.getParameter("internalBureauId");
				msg = getAukKekDisplay(request, serviceBureauDAO,internalBureauId,status);
				request.setAttribute("result", "success");
			}else if(bureauBean.getEditorHold().equals("KEY_PRINT")){
				DAOOpe daoOpe = DAOOpe.getDAOOpe(1);
				ServiceBureauDAO serviceBureauDAO = daoOpe.getServiceBureauDAO();
				String internalBureauId = request.getParameter("internalBureauId");
				logger.debug("internalBureauId  -->"+internalBureauId);
				List printKeyList = serviceBureauDAO.printKeys(internalBureauId);
				String message = printKeys(request,printKeyList);
				request.setAttribute("result", message);
			}
		}catch (AgreementSystemException se) {
			opeErrorList.add(se.getErrorCode());
			logger.error("AgreementSytemException : " + se.getErrorCode());
		} catch (AgreementBusinessException be) {
			opeErrorList.add(be.getErrorCode());
			logger.error("AgreementBusinessException : " + be.getErrorCode());
		} finally{
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
	 * 	Action is used to generate keys
	 */
	public String generateKeys(HttpServletRequest request, ServiceBureauBean bureauBean,				
			List opeErrorList,String action) throws AgreementSystemException,
			AgreementBusinessException {

		String message = "";
		boolean ret=false;
		logger.debug("You are in generating AEK and KEK keys action");
		//String generation_number=request.getParameter("generationNumber");
		List keyList = new ArrayList();
		List KEKandAUKList = new ArrayList();
		String oldKEK=null;
		String generationNumber=null;
		Date businessDate = null;
		String versionNo=null;
		boolean flag=false;
		action="G";
		// Obtain the user id from session
		SessionDataHandler sdh = SessionDataHandler.getSessionDataHandler(request.getSession());
		try{
			if (sdh != null) {						
				businessDate = (java.sql.Date)sdh.getCurrentBusinessDate();
			} else {
				logger.debug("Could not retreive data from session");
				throw new CommonSystemException(IErrorCodes.SESSION_LOST_ERROR,
				"LOST USER SESSION");
			}
		}catch(Exception e){
			logger.debug("Could not retreive data from session");
		}

		/** 
		 * Create the required DAO-Factory 
		 */			
		DAOOpe daoOpe = DAOOpe.getDAOOpe(1);
		/**  
		 * Create an ServiceBureauDAO
		 */
		ServiceBureauDAO serviceBureauDAO = daoOpe.getServiceBureauDAO();
		ServiceBureauBean e=new ServiceBureauBean();
		action=request.getParameter("action");
		if(bureauBean.getEditorHold().equals("GENERATE_KEYS")){				 	
			keyList = serviceBureauDAO.generateKeys(bureauBean);
			if(keyList!=null){
				Iterator iter = keyList.iterator();
				while (iter.hasNext()) {
					ServiceBureauBean serviceBureauBean = (ServiceBureauBean) iter.next();
					oldKEK=serviceBureauBean.getKeyKEK();
					generationNumber = serviceBureauBean.getGenerationNumber();
					versionNo = serviceBureauBean.getVersionNo();
					flag = true;
				}						
			}
			if(flag==false){
				oldKEK=null;
				versionNo="1";						 
			}else if(action.equals("R")){
				generationNumber=Integer.toString(0);
				oldKEK=null;						 
			}
			KEKandAUKList = generateKEKandAUK(oldKEK,generationNumber);
			Iterator iter = KEKandAUKList.iterator();

			while (iter.hasNext()){		
				e=(ServiceBureauBean)iter.next();
				request.setAttribute("keyKEK",e.getKeyKEK());
				request.setAttribute("keyAUK",e.getKeyAUK());
				request.setAttribute("keyKEKPart1",e.getKeyKEKPart1());
				request.setAttribute("keyKEKPart2",e.getKeyKEKPart2());
				request.setAttribute("keyKVV",e.getKeyKVV());						 
				request.setAttribute("generationNumber", generationNumber);
				e.setInternalBureauId(bureauBean.getInternalBureauId());
				e.setGenerationNumber(generationNumber);
			}			
		}
		
		java.sql.Timestamp sqlTimeStamp = getSqlDateTime(businessDate.toString());
		
		ret = serviceBureauDAO.insertKeys(e,sqlTimeStamp,versionNo,action);
		getAukKekDisplay(request, serviceBureauDAO, bureauBean.getInternalBureauId(),action);
		if(keyList.size() > 0){				 
			message="success";				 
		}else{				 
			message="failed";	            
			logger.debug("Unable to generate KEK and AUK keys");
		}

		return message;
	}

	/**
	 * Method used to reset the the key 
	 * @param request
	 * @param bureauBean
	 * @param opeErrorList
	 * @param action
	 * @return
	 * @throws AgreementSystemException
	 * @throws AgreementBusinessException
	 */

	public String resetKeyAction(HttpServletRequest request, ServiceBureauBean bureauBean,
			List opeErrorList,String action) throws AgreementSystemException,
			AgreementBusinessException {
		String message = null;
		String kekKey = "";
		String currentBusinessdate = "";
		String generatedAuk = "";
		String internalBureauId = "";
		String generationNumber = "";
		boolean flag = false;
		logger.debug("You are in resetKeys action.");
		/** 
		 * Create the required DAO-Factory 
		 */			
		DAOOpe daoOpe = DAOOpe.getDAOOpe(1);
		/**  
		 * Create an ServiceBureauDAO 
		 */
		ServiceBureauDAO serviceBureauDAO = daoOpe.getServiceBureauDAO();
		List reSetKeyList = serviceBureauDAO.generateKeys(bureauBean);
		if(reSetKeyList!=null){
			Iterator it = reSetKeyList.iterator();
			while (it.hasNext()) {
				ServiceBureauBean serviceBureauBean = (ServiceBureauBean) it.next();
				internalBureauId = serviceBureauBean.getInternalBureauId();
				kekKey = serviceBureauBean.getKeyKEK();
				currentBusinessdate = serviceBureauBean.getGenerationDate();
				generationNumber = serviceBureauBean.getGenerationNumber();
			}
		}
		generatedAuk = KeyGenerator.getAUK( kekKey, "" );
		java.sql.Timestamp sqlTimeStamp = getSqlDateTime(currentBusinessdate.toString());
		
		flag = serviceBureauDAO.insertAuk(internalBureauId, generatedAuk, sqlTimeStamp);
		logger.debug("FLAG -->"+flag);
		if (flag==true) {	
			logger.debug("Reset Keys was successful.");	
			getAukKekDisplay(request, serviceBureauDAO, bureauBean.getInternalBureauId(),action);
			request.setAttribute("generationNumber", generationNumber);
			request.setAttribute("result", "success");
			message = "success";
		} else {
			logger.debug("Reset Keys was unsuccessful");
			opeErrorList.add(IErrorCodes.SERVICE_BUREAU_INSERT_ERROR);
			request.setAttribute("result", "failed");
			message = "failed";
		}
		return message;
	}
	
	/**
	 * Delete keys
	 * @param request
	 * @param bureauBean
	 * @param opeErrorList
	 * @param action
	 * @return
	 * @throws AgreementSystemException
	 * @throws AgreementBusinessException
	 */
	public String deleteKeys(HttpServletRequest request, ServiceBureauBean bureauBean,				
			List opeErrorList,String action) throws AgreementSystemException,
			AgreementBusinessException {

		String message = "";
		boolean ret=false;
		logger.debug("You are in generating AEK and KEK keys action");
		List keyList = new ArrayList();
		List KEKandAUKList = new ArrayList();
		String oldKEK=null;
		String generationNumber=null;
		Date businessDate = null;
		String versionNo=null;
		//boolean flag=false;
		action="G";
		// Obtain the user id from session
		SessionDataHandler sdh = SessionDataHandler.getSessionDataHandler(request.getSession());
		try{
			if (sdh != null) {						
				businessDate = (java.sql.Date)sdh.getCurrentBusinessDate();
			} else {
				logger.error("Could not retreive data from session");
				throw new CommonSystemException(IErrorCodes.SESSION_LOST_ERROR,
				"LOST USER SESSION");
			}
		}catch(Exception e){
			logger.error("Could not retreive data from session");
		}

		/** 
		 * Create the required DAO-Factory 
		 */			
		DAOOpe daoOpe = DAOOpe.getDAOOpe(1);
		/**  
		 * Create an ServiceBureauDAO
		 */
		ServiceBureauDAO serviceBureauDAO = daoOpe.getServiceBureauDAO();
		ServiceBureauBean e=new ServiceBureauBean();
		action=request.getParameter("action");
		if(bureauBean.getEditorHold().equals("DELETE_KEYS")){				 	
			keyList = serviceBureauDAO.generateKeys(bureauBean);
			if(keyList!=null){
				Iterator iter = keyList.iterator();
				while (iter.hasNext()) {
					ServiceBureauBean serviceBureauBean = (ServiceBureauBean) iter.next();
					versionNo = serviceBureauBean.getVersionNo();
				}						
			}
			generationNumber = Integer.toString(0);			
			KEKandAUKList = generateKEKandAUK(oldKEK,generationNumber);
			Iterator iter = KEKandAUKList.iterator();
			while (iter.hasNext()){		
				e=(ServiceBureauBean)iter.next();
				request.setAttribute("keyKEK",e.getKeyKEK());
				request.setAttribute("keyAUK",e.getKeyAUK());
				request.setAttribute("keyKEKPart1",e.getKeyKEKPart1());
				request.setAttribute("keyKEKPart2",e.getKeyKEKPart2());
				request.setAttribute("keyKVV",e.getKeyKVV());						 
				request.setAttribute("generationNumber", generationNumber);
				e.setInternalBureauId(bureauBean.getInternalBureauId());
				e.setGenerationNumber(generationNumber);
			}			
		}
		java.sql.Timestamp sqlTimeStamp = getSqlDateTime(businessDate.toString());
		ret = serviceBureauDAO.insertKeys(e,sqlTimeStamp,versionNo,action);
		logger.debug("bureauBean.getInternalBureauId() -->"+bureauBean.getInternalBureauId());
		message = getAukKekDisplay(request, serviceBureauDAO, bureauBean.getInternalBureauId(),action);
		return message;
	}

	/**
	 * Method is used to generate the KEK and AUK keys
	 * @param newKekString
	 * @param generationNumber
	 * @return
	 */
	public List generateKEKandAUK(String newKekString,String generationNumber)
	{
		logger.debug("Inside GenerateKEKandAUK() method -");

		String kekPart1 = KeyGenerator.getKEKPart1();
		String kekPart2 = KeyGenerator.getKEKPart2();
		String kekString = KeyGenerator.generateKEK( kekPart1 , kekPart2 );						
		String kvvString = KeyGenerator.getKVV(kekString);
		/**
		 * For the first time, pass new kek as null
		 * For the second time, pass the new kek
		 */
		if(newKekString==null){
			newKekString=null;
		}			
		String aukString = null;
		/**
		 * If generation number is 0,
		 * KeyGenerator.getAUK( kekString, newKekString );
		 * If generation number is not 0,
		 * KeyGenerator.getNewAUK();
		 */
		aukString = generationNumber.equals("0") ? KeyGenerator.getAUK( kekString, newKekString ) : KeyGenerator.getNewAuk();
		ServiceBureauBean e=new ServiceBureauBean();
		logger.debug("generationNumber --**-->"+generationNumber);
		logger.debug("aukString-->"+aukString);
		List list = new ArrayList();
		e.setKeyKEK(kekString);
		e.setKeyAUK(aukString);
		e.setKeyKEKPart1(kekPart1);
		e.setKeyKEKPart2(kekPart2);
		e.setKeyKVV(kvvString);			
		list.add(e);			
		return list;
	}

	/**
	 * Method use to update active and inactive keys to the user interface  
	 * @param request
	 * @param serviceBureauDAO
	 * @param internalBureaId
	 * @param keyStatus
	 * @return
	 */
	public String getAukKekDisplay(HttpServletRequest request, ServiceBureauDAO serviceBureauDAO,String internalBureaId, String keyStatus){
		List kekaukList = null;
		String message = "success";
		try {
			kekaukList = serviceBureauDAO.getKekAukDisplay(internalBureaId);
		} catch (AgreementSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AgreementBusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		KekAukServiceBureauBean KekAukBean = new KekAukServiceBureauBean();
		logger.debug("kekaukList -->"+kekaukList.size());
		Iterator iterator = kekaukList.iterator();
		while (iterator.hasNext()) {
			KekAukBean = (KekAukServiceBureauBean)iterator.next();
			try{
				String generationNo = KekAukBean.getAukGenerationNo().toString();
				if(generationNo!=null){
					//request.setAttribute("KEK_GENERATION_NO", generationNo);
					//code modified for generation number
					request.setAttribute("KEK_GENERATION_NO", KekAukBean.getKekGenerationNo());
				}
				request.setAttribute("GENERATION_DATE", KekAukBean.getGenerationDate());
				request.setAttribute("KVV", KekAukBean.getKvv());
				request.setAttribute("AUK_GENERATION_NO", KekAukBean.getAukGenerationNo());
				String generationNo1 = KekAukBean.getKekGenerationNo1().toString();
				if(!generationNo1.equals("null")){
					request.setAttribute("KEK_GENERATION_NO1", generationNo1);
				}
				request.setAttribute("GENERATION_DATE1", KekAukBean.getGenerationDate1());	 
				request.setAttribute("KVV1", KekAukBean.getKvv1());
				request.setAttribute("KEY_STATUS", keyStatus);
			}catch(NullPointerException npe){
				logger.error("Null Value in getDisplay ::::"+npe.getMessage());
			}
		}
		return message; 
	}

	/**
	 * Method use to print the keys in when user clicks on print patu id button
	 * @param request
	 * @param printKeyList
	 * @return
	 */
	public String printKeys(HttpServletRequest request, List printKeyList){
		String message = "success";
		Iterator iterator = printKeyList.iterator();
		ServiceBureauBean serviceBureauBean = new ServiceBureauBean();
		try{
			while (iterator.hasNext()){	
				serviceBureauBean =(ServiceBureauBean)iterator.next();
				request.setAttribute("keyKEK",serviceBureauBean.getKeyKEK());
				request.setAttribute("keyAUK",serviceBureauBean.getKeyAUK());
				request.setAttribute("keyKEKPart1",serviceBureauBean.getKeyKEKPart1());
				request.setAttribute("keyKEKPart2",serviceBureauBean.getKeyKEKPart2());
				request.setAttribute("keyKVV",serviceBureauBean.getKeyKVV());						 
				request.setAttribute("generationNumber", serviceBureauBean.getGenerationNumber());
				request.setAttribute("generationDateTime", serviceBureauBean.getGenerationDate());
				request.setAttribute("customerName1", serviceBureauBean.getContactPerson1());
				request.setAttribute("customerName2", serviceBureauBean.getContactPerson2());
				request.setAttribute("controlCode", serviceBureauBean.getControlCode());
								
				String address1 = request.getParameter("patuKeyAddress1");
				String address2 = request.getParameter("patuKeyAddress2");
				
				request.setAttribute("address1", address1);
				request.setAttribute("address2", address2);
				
			}	
		}catch(NullPointerException npe){
			logger.debug("Null value ::::"+npe.getMessage());
		}
		return message;
	}
	
	 /**
     * anandkumar.b 05-12-2008
     * method added to get the current business date with time stamp.  
     * @return
     */
    public java.sql.Timestamp getSqlDateTime(String currentBusinessDate){
    	
    	java.sql.Timestamp sqltDate = null;
    	try {
		    DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
			java.util.Date date = new java.util.Date();
			String time = dateFormat.format(date);
			String stringDate= currentBusinessDate+" "+time;
			DateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			java.util.Date parsedUtilDate = formater.parse(stringDate);
			sqltDate= new java.sql.Timestamp(parsedUtilDate.getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sqltDate;
    }
}