
/********************************************************************************************
* Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                        *
* IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.       *
* The Program or any portion thereof may not be reproduced in any form whatsoever except    *
* as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd. *
*																							*
* File Name                   : BaseServiceSpecificAccountBean.java	                        *
* Author                      : Anantaraj S                                                 *
* Creation Date               : 01-Aug-2008                                                 *
* Description                 : This file serves as a base java bean for the Contents.		*
*		            			This returns value as objects for Contents.                 *
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

package com.dnb.agreement.bean;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.log4j.Logger;

public class BaseServiceSpecificAccountBean extends ActionForm 
{    
	
	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(BaseServiceSpecificAccountBean.class);	
	/**
	 * formItems is variable for ServiceSpecificBean
	 * 
	 */
	private ServiceSpecificAccountBean[] formItems1;
	private ServiceSpecificCustomerBean[] formItems;
	

    int[] arr;
	public ActionErrors validate(
			ActionMapping mapping,
			HttpServletRequest request) {
	
			ActionErrors errors = new ActionErrors();
			return errors;
	}
	
	/** 
	* Returns the FormItems.
	* @return Array of ServiceSpecificAccountBean
	*/
	public ServiceSpecificAccountBean[] getFormItems1() {
		   return formItems1;
	}
    
	/**
	 * set formItems1
	 * @param formItems1 to set
	 */
	public void setFormItems1(ServiceSpecificAccountBean[] formItems1) {
		this.formItems1 = formItems1;
	}

	
	public ServiceSpecificCustomerBean[] getFormItems() {
		return formItems;
	}

	public void setFormItems1(ServiceSpecificCustomerBean[] formItems) {
		this.formItems = formItems;
	}
		

	/**
	 * Pattern to match request parameters
	 */
		
	private Pattern itemPattern1 = Pattern.compile("formItems1\\[(\\d+)\\].*");
	private Pattern itemPattern = Pattern.compile("formItems\\[(\\d+)\\].*");
	
	
	/** 
	 * Method Reset
	 * Dynamically creates the appropriate ServiceSprcificAccountBean array based on the request
	 * 
	 * @param mapping		The Struts Action mapping
	 * @param request		The incoming request
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
						
		Enumeration paramNames = request.getParameterNames();	
		int maxSize1 = 0;
				
		while (paramNames.hasMoreElements())
		{  			
			String paramName1 = (String) paramNames.nextElement();
			Matcher itemMatcher1 = itemPattern1.matcher(paramName1);
			if (itemMatcher1.matches())
			{
				String index = itemMatcher1.group(1);				
										
				if (Integer.parseInt(index) > maxSize1)
				{
					maxSize1 = Integer.parseInt(index);
				}				
			}
		}
		
			formItems1 = new ServiceSpecificAccountBean[maxSize1 + 1];		 
			for (int i = 0; i <= maxSize1; i++)
			formItems1[i] = new ServiceSpecificAccountBean();		
			
    		reset1(mapping,request);

	}
	
        public void reset1(ActionMapping mapping, HttpServletRequest request) {
		
		Enumeration paramNames = request.getParameterNames();	
		int maxSize = 0;	
		
		while (paramNames.hasMoreElements())
		{  			
			String paramName = (String) paramNames.nextElement();
			Matcher itemMatcher = itemPattern.matcher(paramName);
			if (itemMatcher.matches())
			{
				String index = itemMatcher.group(1);				
										
				if (Integer.parseInt(index) > maxSize)
				{
					maxSize = Integer.parseInt(index);
				}				
			}
		}
		System.out.println("maxsixe"+ maxSize); 	
		formItems = new ServiceSpecificCustomerBean[maxSize+1];		 
		for (int i = 0; i <= maxSize; i++)
		{		   	
			formItems[i] = new ServiceSpecificCustomerBean();
		}
	    } 	
		
 }