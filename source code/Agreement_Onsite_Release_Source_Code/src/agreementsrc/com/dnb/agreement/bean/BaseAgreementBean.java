
/********************************************************************************************
* Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                        *
* IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.       *
* The Program or any portion thereof may not be reproduced in any form whatsoever except    *
* as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd. *
*																							*
* File Name                   : Base AgreementBean.java	                            *
* Author                      : Anantaraj S                                               *
* Creation Date               : 28-July-2008                                                 *
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

public class BaseAgreementBean extends ActionForm 
{    
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(BaseAgreementBean.class);	
	/**
	 * formItems is variable for AgreementBean
	 * 
	 */
	private AgreementBean[] formItems;
	

    int[] arr;
	public ActionErrors validate(
			ActionMapping mapping,
			HttpServletRequest request) {
	
			ActionErrors errors = new ActionErrors();
			return errors;
		}
	
		/** 
		 * Returns the FormItems.
		 * @return Array of AgreementBean
		 */
		public AgreementBean[] getFormItems() {
			return formItems;
		}
	
		/** 
		 * Set the FormItems.
		 * @param FormItems The FormItems to set
		 */
		public void setFormItems(AgreementBean[] FormItems) {
			this.formItems = FormItems;
		}
		
		

	/**
	 * Pattern to match request parameters
	 */
		
	private Pattern itemPattern = Pattern.compile("formItems\\[(\\d+)\\].*");
	
	
	/** 
	 * Method Reset
	 * Dynamically creates the appropriate WorkflowContentBean array based on the request
	 * 
	 * @param mapping		The Struts Action mapping
	 * @param request		The incoming request
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
						
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
			formItems = new AgreementBean[maxSize + 1];		 
			for (int i = 0; i <= maxSize; i++)
			formItems[i] = new AgreementBean();		
						
	}
		
 }