
/********************************************************************************************
* Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                        *
* IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.       *
* The Program or any portion thereof may not be reproduced in any form whatsoever except    *
* as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd. *
*																							*
* File Name                   : SessionData.java                                            *
* Author                      : Virendra chavada                                            *
* Creation Date               : 22-May-2007                                                 *
* Description                 : This file serves as the session handler class.				*
* Modification History        :                                                             *
*																						    *
* Version No.    Name                       Date               Brief description of change  *
*  ---------------------------------------------------------------------------------------  *
*          |                       |                  |									    *
*  ---------------------------------------------------------------------------------------  *
*********************************************************************************************/

package com.dnb.common.utility;

import java.util.List;
import java.sql.Date;

import javax.servlet.http.HttpSession;

import com.dnb.common.commons.CommonConstants;
import com.dnb.common.session.SessionDataHandler;


public class SessionData
{
    String currentUser,branchName,branchId,branchCode,dateFormat,languageId,userName,countryId;
    String enterpriseId,enterpriseName,enterpriseStatus,userId,dmversionNo;
	String lastLoginDatewithTs,currentBusiDateAsString;
	String localCurrency, loginDateTime;
	String debugMode;
    Date currentBusinessDate,lastLoginDate,lastLoginDatewithTslll;
	List roleLst=null;
	String divisionid;
  
	
	
	
    /**
	 * @return the divisionid
	 */
	public String getDivisionid() {
		return divisionid;
	}


	/**
	 * @param divisionid the divisionid to set
	 */
	public void setDivisionid(String divisionid) {
		this.divisionid = divisionid;
	}


	public String getDebugMode() {
		return debugMode;
	}


	public void setDebugMode(String debugMode) {
		this.debugMode = debugMode;
	}


	public String getLocalCurrency() {
		return localCurrency;
	}


	public void setLocalCurrency(String localCurrency) {
		this.localCurrency = localCurrency;
	}


	public String getDmversionNo() {
		return dmversionNo;
	}


	public void setDmversionNo(String dmversionNo) {
		this.dmversionNo = dmversionNo;
	}


	public String getUserId() {
		return userId;
	}


	public void setUserId(String userId) {
		this.userId = userId;
	}


	/**
     * Create a new SessionData.  This should only be done at login time
     *  
     */
    public SessionData() 
    {
    	super();
    }

    
    public String getEnterpriseId() {
		return enterpriseId;
	}


	public void setEnterpriseId(String enterpriseId) {
		this.enterpriseId = enterpriseId;
	}


	public String getEnterpriseName() {
		return enterpriseName;
	}


	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}


	public String getEnterpriseStatus() {
		return enterpriseStatus;
	}


	public void setEnterpriseStatus(String enterpriseStatus) {
		this.enterpriseStatus = enterpriseStatus;
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
    public String getBranchName()
    {
        return branchName;
    }

    /**
	 * Set current branch
     * @parameter branch.
     */
    public void setBranchName(String branchName)
    {
        this.branchName=branchName;
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
     * @return Returns the lastLoginDate.
     * /
     */
    public Date getLastLoginDate()
    {
    	return lastLoginDate;
    }
    
    
	/**
	 * set lastLoginDate
     * @parameter lastLoginDate.
     */
    public void setLastLoginDate(Date lastLoginDate)
    {
        this.lastLoginDate=lastLoginDate;
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

	public String getCountryId() {
		return countryId;
	}

	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}


	public String getLastLoginDatewithTs() {
	       return lastLoginDatewithTs;
	}


	public void setLastLoginDatewithTs(String lastLoginDatewithTs) {
		this.lastLoginDatewithTs = lastLoginDatewithTs;
	}


	public Date getLastLoginDatewithTslll() {
		return lastLoginDatewithTslll;
	}


	public void setLastLoginDatewithTslll(Date lastLoginDatewithTslll) {
		this.lastLoginDatewithTslll = lastLoginDatewithTslll;
	}


	public String getLoginDateTime() {
		return loginDateTime;
	}


	public void setLoginDateTime(String loginDateTime) {
		this.loginDateTime = loginDateTime;
	}


	public void setMyObject(HttpSession statementSession,SessionDataHandler sdh)
    {
		// Store self in session
        statementSession.setAttribute(CommonConstants.SessionKey, sdh);
		//pmSession.setMaxInactiveInterval(PMConstants.ProcessMateSessionTimer); 
		
		
    }
    
	
}
