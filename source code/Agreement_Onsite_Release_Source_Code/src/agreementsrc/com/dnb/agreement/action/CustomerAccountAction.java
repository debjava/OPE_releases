/********************************************************************************************
 * Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                        *
 * IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.       *
 * The Program or any portion thereof may not be reproduced in any form whatsoever except    *
 * as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd. *
 *												                        					 *
 * File Name                   : CustomerAccountAction.java                                  *
 * Author                      : Manjunath N G                                               *
 * Creation Date               : 14-August-2008                                                *
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

import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dnb.agreement.DAO.AgreementDAO;
import com.dnb.agreement.DAO.DAOOpe;
import com.dnb.agreement.bean.AgreementCommonBean;
import com.dnb.agreement.bean.CustomerIdBean;
import com.dnb.agreement.exception.IErrorCodes;
import com.dnb.agreement.exception.AgreementBusinessException;
import com.dnb.agreement.exception.AgreementSystemException;
import com.dnb.agreement.common.OPEConstants;

public class CustomerAccountAction extends Action {

	boolean result_ret;
	List lst;	

	static Logger logger = Logger.getLogger(CustomerAccountAction.class);

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

		AgreementCommonBean agreementCommonBean = null;			
		agreementCommonBean = (AgreementCommonBean) form;		

		try{			
			if (agreementCommonBean.getEditorHold().equals("GET_CUSTOMER")) 
			{				
				msg = getCustomerInformation(request, agreementCommonBean,opeErrorList);
			}
			else if(agreementCommonBean.getEditorHold().equals("GET_ACCOUNT")) 
			{				
				msg = getAccountInformation(request, agreementCommonBean,opeErrorList);
			}

		} catch (AgreementSystemException se) {
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
				entityList.add(agreementCommonBean);
				request.setAttribute(OPEConstants.OPEERRORLIST, opeErrorList);
				request.setAttribute("bureauDetails", entityList);
			}		
			return mapping.findForward(msg);
		}
	}

	/**
	 * 	Action is used to get customer information
	 */
	public String getCustomerInformation(HttpServletRequest request, AgreementCommonBean agBean,				
			List opeErrorList) throws AgreementSystemException,
			AgreementBusinessException {
				
		String message = "";

		logger.debug("You are in getting customer information action");

		String index=request.getParameter("index");
		System.out.println("CustomerAccountAction : getCustomerInformation : index --------->>>"+index);

		List customerInfo = new ArrayList();
		System.out.println("CustomerAccountAction : getCustomerInformation : agBean.getCustomerId()---->>>"+agBean.getCustomerId());
		Map custIdMap = getCustomerIdInfo(agBean.getCustomerId());
		if( custIdMap.containsKey(agBean.getCustomerId()))
		{
			CustomerIdBean custIdBean = (CustomerIdBean)custIdMap.get(agBean.getCustomerId());
			System.out.println("Main Point Name-------->>>"+custIdBean.getCustomerName());
			System.out.println("Main Point ADRS-------->>>"+custIdBean.getAddress());
			agBean.setCustomerName(custIdBean.getCustomerName());
			agBean.setAddress(custIdBean.getAddress());
			request.setAttribute("index", index);
			customerInfo.add(0,agBean);
			customerInfo.add(1,agBean);		
		}

		Iterator it = customerInfo.iterator();
		while (it.hasNext()){
			AgreementCommonBean e = (AgreementCommonBean)it.next();				 
		}

		request.setAttribute("customerList",customerInfo);

		if(customerInfo.size()>0){
			message="list";				 
		}else{
			request.setAttribute("index", index);
			message="failed";	            
			logger.debug("Unable to get customer information");
		}
		return message;
	}


	/**
	 * 	Action is used to get customer account information
	 */

	public String getAccountInformation(HttpServletRequest request, AgreementCommonBean agBean,				
			List opeErrorList) throws AgreementSystemException,
			AgreementBusinessException {	 			 

		//		System.out.println("------------ CustomerAccountAction : getAccountInformation ---------");
		String message = "";

		logger.debug("You are in getting customer account information action");	

		String index=request.getParameter("index");
		System.out.println("CustomerAccountAction : getAccountInformation - index-------"+index);
		Map accountMap = getAccountMap(agBean.getCustomerId());
		//Added by Debadatta Mishra 
		if( accountMap.containsKey(agBean.getCustomerId()))
		{
			System.out.println("It contains the key");
			String account = (String)accountMap.get(agBean.getCustomerId());
			request.setAttribute("customerAccountList",account);
			request.setAttribute("index", index);
			message="list";
		}
		else{
			request.setAttribute("index", index);
			message="failed";	            
			logger.debug("Unable to get customer account information");
		}			
		return message;
	}

	/**This method is used to obtain the information pertaining to 
	 * a customer id. Here the input is customer id and it will return
	 * a map where key is the customer id and value is the required
	 * information.
	 * @author Debadatta Mishra
	 * @param customerId of type String
	 * @return a Map
	 */
	private Map getCustomerIdInfo( String customerId )
	{
		Map custIdMap = new HashMap();
		//		CustomerIdBean custIdBean = null;
		String normativeQueryPath = "/WEB-INF/classes/normative_query.properties";
		ServletContext servletContext = getServlet().getServletContext();
		InputStream inStream = servletContext.getResourceAsStream(normativeQueryPath);

		Properties prop = new Properties();
		try
		{
			prop.load(inStream);
			String customerIdQuery = prop.getProperty("OPE_CUST_Q_1");
			System.out.println("CustomerIdQuery------->>>"+customerIdQuery);
			String custManualDetails = prop.getProperty("OPE_MANUAL_ID_DETAILS");
			System.out.println("Customer ID Prop-------->>>"+custManualDetails);
			if( custManualDetails != null )
			{
				custIdMap = getManualCustomerIdInfo(custManualDetails);
			}
			if( customerIdQuery != null )
			{
				DAOOpe daoOpe=DAOOpe.getDAOOpe(1);
				AgreementDAO agreementDAO=daoOpe.getAgreementDAO();
				CustomerIdBean custIdBean = agreementDAO.getCustomerIdDetails(customerId, customerIdQuery);
				if( custIdBean != null )
					custIdMap.put(customerId, custIdBean);
			}
		}
		catch( Exception e )
		{
			e.printStackTrace();
			System.out.println(e);
			System.out.println(e.getMessage());
		}

		return custIdMap;
	}

	/**This method is used to derive the information from
	 * the properties file called normative_query.properties.
	 * This method loads all the available customer ids and
	 * the corresponding information from the properties
	 * file.
	 * @author Debadatta Mishra
	 * @param custString of type String indicating the complete
	 * value string from the properties file.
	 * @return a Map
	 */
	private Map getManualCustomerIdInfo( String custString)
	{
		Map custIdMap = new HashMap();
		String[] custDetailsStr = custString.split("[,]");
		for( int i = 0 ; i < custDetailsStr.length ; i++ )
		{
			String tempStr = custDetailsStr[i];
			String[] custStr = tempStr.split("[-]");
			
			CustomerIdBean cidBean = new CustomerIdBean();
			cidBean.setCustomerName(custStr[1]);
			cidBean.setAddress(custStr[2]);
			custIdMap.put(custStr[0], cidBean);
		}
		return custIdMap;
	}


	/**This method is used to provide the account details
	 * for a particular account number. This method loads
	 * from the properties file as well as from database.
	 * @author Debadatta Mishra
	 * @param accountNo of type String
	 * @return a map
	 */
	private Map getAccountMap( String accountNo )
	{
		Map accountMap = new HashMap();

		String normativeQueryPath = "/WEB-INF/classes/normative_query.properties";
		ServletContext servletContext = getServlet().getServletContext();
		InputStream inStream = servletContext.getResourceAsStream(normativeQueryPath);

		Properties prop = new Properties();
		try
		{
			prop.load(inStream);
			
			String manualActDetailsStr = prop.getProperty("OPE_MANUAL_ACCOUNT_DETAILS");
			System.out.println("ManualActDetails--------->>>"+manualActDetailsStr);
			String customerAccountQuery = prop.getProperty("OPE_CUST_Q_2");
			System.out.println("CustomerAccountQuery------->>>"+customerAccountQuery);

			if( manualActDetailsStr != null )
			{
				accountMap = getManualAccountDetails(manualActDetailsStr);
			}
			if( customerAccountQuery != null )
			{
				DAOOpe daoOpe=DAOOpe.getDAOOpe(1);
				AgreementDAO agreementDAO=daoOpe.getAgreementDAO();
				String accountDetails = agreementDAO.getAccountDetails(accountNo,customerAccountQuery);
				System.out.println("getAccountMap------>>>"+accountDetails);
				if( accountDetails != null )
					accountMap.put(accountNo,accountDetails);
			}
		}
		catch( SQLException se )
		{
			se.printStackTrace();
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		return accountMap;
	}

	/**This method is used to obtain the account
	 * detail information from the properties file.
	 * @author Debadatta Mishra
	 * @param actDetailsStr of type String.
	 * @return a map
	 */
	private static Map getManualAccountDetails( String actDetailsStr )
	{
		Map manualActMap = new HashMap();
		String[] acctStrs = actDetailsStr.split("[,]");
		for( int i = 0 ; i < acctStrs.length ; i++ )
		{
			String detailsStr = acctStrs[i];
			System.out.println("Details Str ----->>>"+detailsStr);
			String[] accountStr = detailsStr.split("[-]");
			System.out.println("Account No---->>>"+accountStr[0]);
			System.out.println("Account Name---->>>"+accountStr[1]);
			manualActMap.put(accountStr[0], accountStr[1]);
		}
		return manualActMap;
	}

}