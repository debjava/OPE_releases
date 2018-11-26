
/********************************************************************************************
* Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                        *
* IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.       *
* The Program or any portion thereof may not be reproduced in any form whatsoever except    *
* as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd. *
*																							*
* File Name                   : StatementSessionHandler.java                                       *
* Author                      : Chenchi Reddy G                                                    *
* Creation Date               : 03-Jul-2008                                                 *
* Description                 : This file serves as the session handler class,				*
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
package com.dnb.common.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import java.util.HashMap;
import java.util.List;
import java.util.Iterator;

import com.dnb.common.exception.IErrorCodes;
import com.dnb.common.commons.CommonConstants;


public class DNBtSessionHandler {

	static Logger logger = Logger.getLogger(DNBtSessionHandler.class);

	HttpServletRequest request;
	HttpSession statementSession;

	/**
	 * Create Session and set userid and branch into session
	 */
	public boolean createSession(HttpServletRequest request,String userId,String branch){
		
		this.request=request;
		boolean chk = validateSession(CommonConstants.StatementUser);
		if(!chk) {
			// create session here
			statementSession = request.getSession();
			// Set the session time out
			statementSession.setMaxInactiveInterval(CommonConstants.StatementSessionTimer); 
			if(statementSession != null)
			{	// set userid and branch in session
				statementSession.setAttribute(CommonConstants.StatementUser , userId);
				statementSession.setAttribute(CommonConstants.StatementBranch , branch);
			}
		} 
		return true;

	}
	/**
	 * Returns an instance of Session
	 */
	public HttpSession getSessionInstance()	{
		if(validateSession(CommonConstants.StatementUser))
			return statementSession;
		else
			return null;
	}
	/**
	 * Destroys a session
	 */
	public void destroySession(HttpSession statementSession) {
		statementSession.removeAttribute(CommonConstants.StatementUser);
		statementSession.removeAttribute(CommonConstants.StatementBranch);
		statementSession.removeAttribute(CommonConstants.StatementRole);
		statementSession.invalidate();	
	}
	/**
	 * vaildates a session
	 */
	public boolean validateSession(String param) {
		statementSession = request.getSession();
		if(statementSession.getAttribute(param)!=null)
			return true;
		else
			return false;
	}
	/**
	 * set any attribute to session
	 */
	public boolean setSessionAttribute(HttpSession statementSession,String stConst, String stValue) {
		if(validateSession(CommonConstants.StatementUser))
		{	
			statementSession.setAttribute(stConst , stValue);
			return true;
		}
		else
			return false;
	}
	/**
	 * get attributes from session
	 */

	public String getSessionStringAttribute(HttpSession statementSession, String keyAttribute){
		String retAttribute = (String)statementSession.getAttribute(keyAttribute);
		return retAttribute;
	}
	/**
	 * set roles attribute to session
	 */
	public boolean setRightAttribute(HttpSession statementSession, List lst)
	{
		statementSession.setAttribute(CommonConstants.StatementRole, lst);
		return true;
	}
}