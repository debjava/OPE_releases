
/********************************************************************************************
* Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                        *
* IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.       *
* The Program or any portion thereof may not be reproduced in any form whatsoever except    *
* as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd. *
*																							*
* File Name                   : SessionDataHandler.java                                     *
* Author                      : Chenchi Reddy                                                    *
* Creation Date               : 4-Jul-2008                                                *
* Description                 : This file serves as the session handler class.				*
* Modification History        :                                                             *
*																						    *
* Version No.    Name                       Date               Brief description of change  *
*  ---------------------------------------------------------------------------------------  *
*    v1.0  |Balu Narayanasamy      |   29-Aug-2006    |		Default window property added   *
*          |                       |                  |									    *
*  ---------------------------------------------------------------------------------------  *
*********************************************************************************************/

package com.dnb.common.session;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.dnb.common.commons.CommonConstants;

public class SessionDataHandler implements Serializable
{
    String currentUser,branch,branchId,branchCode,dateFormat,languageId,dfw,userName; //lastLoginDate,
	Date currentBusinessDate;
	String lastLoginDate;
	List roleLst=null;
	String divisionId;
	String enterpriseId;
	

    /** Debug/error logger */
    private static Logger logger = Logger.getLogger(SessionDataHandler.class);

   

	/**
     * A method to retrieve the user's current session data object
     * @param session User's session object
     * @return Existing SessionData, or null
     */
    public static SessionDataHandler getSessionDataHandler(HttpSession session)
    {
    	return (SessionDataHandler)session.getAttribute(CommonConstants.SessionKey);
    }

    /**
     * Create a new SessionData.  This should only be done at login time
     *  
     */
    public SessionDataHandler() 
    {
    }

    /**
     * @return Returns the currentUser.
     */
    public String getCurrentUser()
    {
        return currentUser;
    }
	
	/**
     * set the currentUser.
	 * @ parameter userId.
     */
    public void setCurrentUser(String currentUser)
    {
        this.currentUser=currentUser;
    }


    /**
     * @return Returns the branch.
     */
    public String getBranch()
    {
        return branch;
    }

    /**
	 * Set current branch
     * @parameter branch.
     */
    public void setBranch(String branch)
    {
        this.branch=branch;
    }
	
	/**
     * @return Returns the branchId.
     */
    public String getBranchId()
    {
        return branchId;
    }

	/**
	 * set currrent branchId
	 * @parameter branchId.
     */
    public void setBranchId(String branchId)
    {
        this.branchId=branchId;
    }
    /**
     * @return Returns the branchCode.
     */
    public String getBranchCode()
    {
        return branchCode;
    }

	/**
	 * set currrent branchCode
	 * @parameter branchCode.
     */
    public void setBranchCode(String branchCode)
    {
        this.branchCode=branchCode;
    }
    /**
     * @return Returns the dateFormat.
     */
    public String getDateFormat()
    {
        return dateFormat;
    }

	/**
	 * set dateFormat
     * @parameter dateFormat.
     */
    public void setDateFormat(String dateFormat)
    {
        this.dateFormat=dateFormat;
    }

	
	/**
     * @return Returns the currentBusinessDate.
     */
    public Date getCurrentBusinessDate()
    {
        return currentBusinessDate;
    }

	/**
	 * set currentBusinessDate
     * @parameter currentBusinessDate.
     */
    public void setCurrentBusinessDate(Date currentBusinessDate)
    {
        this.currentBusinessDate=currentBusinessDate;
    }
	
	/**
     * @return Returns the languageId.
     */
    public String getLanguageId()
    {
        return languageId;
    }	

	/**
	 * set languageId
     * @parameter languageId.
     */
    public void setLanguageId(String languageId)
    {
        this.languageId=languageId;
    }

    /**
	 * set DefaultWindow
     * @parameter Default Window.
     */
    public void setDefaultWindow(String dfw)
    {
        this.dfw=dfw;
    }

	    
    /**
     * @return Returns the Default Window.
     */
    public String getDefaultWindow()
    {
        return dfw;
    }
    
	/**
     * @return Returns the roleList.
     */
    
    public List getRoleList()
    {
        return roleLst;
    }
	
	/**
	 * set role / rights 
     * @parameter roleLst.
     */
    public void setRoleList(List roleLst)
    {
        this.roleLst=roleLst;
    }
	
	/**
     * @set cur Object in session.
     */
    
    
    /**		
     * userName ( FirstName LastName)
	 */	
	public void setUserName(String userName)
	{
		this.userName=userName;
	}	
	
	public String getUserName()
	{
		return userName;
	}	
	
	
	public void setMyObject(HttpSession statementSession,SessionDataHandler sdh)
    {
		// Store self in session
        statementSession.setAttribute(CommonConstants.SessionKey, sdh);
		//pmSession.setMaxInactiveInterval(PMConstants.ProcessMateSessionTimer); 
		
		
    }

	/**
	 * @return the divisionId
	 */
	public String getDivisionId() {
		return divisionId;
	}

	/**
	 * @param divisionId the divisionId to set
	 */
	public void setDivisionId(String divisionId) {
		this.divisionId = divisionId;
	}

	/**
	 * @return the enterpriseId
	 */
	public String getEnterpriseId() {
		return enterpriseId;
	}

	/**
	 * @param enterpriseId the enterpriseId to set
	 */
	public void setEnterpriseId(String enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public void setLastLoginDate(String lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	public String getLastLoginDate() {
		return lastLoginDate;
	}
}
