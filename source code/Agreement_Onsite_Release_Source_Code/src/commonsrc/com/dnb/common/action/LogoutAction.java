
/********************************************************************************************
* Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                        *
* IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.       *
* The Program or any portion thereof may not be reproduced in any form whatsoever except    *
* as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd. *
*																							*
* File Name                   : LogoutAction.java                                           *
* Author                      : Chenchi Reddy                                                  *
* Creation Date               : 07-Jul-2008                                                 *
* Description                 : Action class which invalidates the session and updates the  *
*								database											*
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

package com.dnb.common.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dnb.common.DAO.DAODNBCommon;
import com.dnb.common.DAO.LoginDAO;
import com.dnb.common.exception.CommonBusinessException;
import com.dnb.common.exception.CommonSystemException;
import com.dnb.common.session.SessionDataHandler;

public class LogoutAction extends Action 
{
	boolean ent_ret; 

	static Logger logger = Logger.getLogger(LoginAction.class);

    /** 
	 * Constructor for SuccessAction.
	 */

    public LogoutAction() 
	{
        super();
    }

    /**  
	 * Action Methods 
	 */

    /**
     * Returns the <code>ActionForward</code> named "success" if one is
     * configured or <code>null</code>if it cannot be found.
     *
     * Searches first for a local forward, then a global forward.
     *
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     *
     * @exception Exception if mapping.findForward throws an Exception
     *
     * @return the "success" ActionForward, or null if it cannot be found
     */
    public ActionForward execute(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
         {
    	String msg = "failed";
		 /**  
		 * Create an LoginDAO 
		 */
    	DAODNBCommon statement = DAODNBCommon.getDAODNB(1);
    	LoginDAO lDAO = statement.getLoginDAO();
    	
    	
    	
    	SessionDataHandler sdh = SessionDataHandler.getSessionDataHandler(request.getSession());
    
    	try{
    	boolean b = lDAO.logoutUser(sdh.getCurrentUser());
    	if(b){
    		destroySession(request.getSession());
    		sdh = null;  
    		msg = "success";
    	}else
    		msg = "failed";
    	}catch(CommonSystemException se)
    	{
    		logger.error("StatementSytemException: "+se);		
    	}catch(CommonBusinessException be){
    		logger.error("RunTime Exception : "+be);
    	}		
    	return mapping.findForward(msg);	
	}

	/**
	 * Destroys a session
	 */
	public void destroySession(HttpSession statementSession) {
		statementSession.invalidate();	
	}
}
