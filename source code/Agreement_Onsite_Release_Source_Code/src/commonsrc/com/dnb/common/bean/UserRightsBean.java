
/***************************************************************************************************
* Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.											   *
* IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.			   *
* The Program or any portion thereof may not be reproduced in any form whatsoever except		   *
* as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd.		   *
*																								   *
* File Name                   : UserRightsBean.java                                                *
* Author                      : Chenchi Reddy G														   *
* Creation Date               : 03-Jul-2008                                                        *
* Description                 : This file serves as the User Rights Bean and hold all user rights. *
*		            																			   *
* Modification History        :																	   *
*																								   *
Version No.  Edited By         Date               Brief description of change                      *
*  ------------------------------------------------------------------------------------------------*
*   V1.0      | Balu Narayanasamy  |  29-Aug-2006   |Default Window setter and getter method added *
*             |                    |				|         							           *
*  ------------------------------------------------------------------------------------------------*
****************************************************************************************************/

/** 
 * Create or import Packages
 */

package com.dnb.common.bean;

import java.util.Date;
import oracle.sql.TIMESTAMP;
import oracle.sql.DATE;


public class UserRightsBean
{
    /** 
     * Declarations
	 */
	public String entityCode,rightName,rightNo,rightValue,roleName,roleId,status,versionNo,vpos,createdBy,lastUpdatedBy;
	public String branch,branchId,branchCode,dateFormat,languageId,dfw,userName;
	public Date createdOn,lastUpdatedOn;
	public Date currentBusinessDate;
	public String lastLoginDate;
	public String divisionId;
	public String enterpriseId;
	String rights;
	/**
	 * @return the divisionId
	 */
	

	/**
	 * @return the currentBusinessDate
	 */
	public Date getCurrentBusinessDate() {
		return currentBusinessDate;
	}

	/**
	 * @param currentBusinessDate the currentBusinessDate to set
	 */
	public void setCurrentBusinessDate(Date currentBusinessDate) {
		this.currentBusinessDate = currentBusinessDate;
	}

	

	
	/**
	 * @return the dfw
	 */
	public String getDfw() {
		return dfw;
	}

	/**
	 * @param dfw the dfw to set
	 */
	public void setDfw(String dfw) {
		this.dfw = dfw;
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

	/**
	 * @return the vpos
	 */
	public String getVpos() {
		return vpos;
	}

	/**
	 * @param vpos the vpos to set
	 */
	public void setVpos(String vpos) {
		this.vpos = vpos;
	}

	/**
	 * @return the rights
	 */
	public String getRights() {
		return rights;
	}

	/**
	 * @param rights the rights to set
	 */
	public void setRights(String rights) {
		this.rights = rights;
	}

	/** 
     * Entity Code
	 */
	public void setEntityCode(String entityCode)
	{
		this.entityCode=entityCode;
	}	
	
	public String getEntityCode()
	{
		return entityCode;
	}
	
public void setStatus(String status)
	{
		this.status=status;
	}	
	
	public String getStatus()
	{
		return status;
	}

	/** 
     * Right No.
	 */
	public void setRightNo(String rightNo)
	{
		this.rightNo=rightNo;
	}	
	
	public String getRightNo()
	{
		return rightNo;
	}
	/** 
     * Right Value
	 */
	public void setRightValue(String rightValue)
	{
		this.rightValue=rightValue;
	}	
	
	public String getRightValue()
	{
		return rightValue;
	}	
	/** 
     * Right Name
	 */
	public void setRightName(String rightName)
	{
		this.rightName=rightName;
	}	
	
	public String getRightName()
	{
		return rightName;
	}
	/** 
     * Role Name
	 */
	public void setRoleName(String roleName)
	{
		this.roleName=roleName;
	}	
	
	public String getRoleName()
	{
		return roleName;
	}
	
	/**
     * Getter for the RoleId
     *
     * @return the RoleId
     */
    public String getRoleId() {
        return this.roleId;
    }

    /**
     * Setter for the roleId
     *
     * @param roleId 
     */
    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

	/** 
     * Branch 
	 */
	public void setBranch(String branch)
	{
		this.branch=branch;
	}	
	
	public String getBranch()
	{
		return branch;
	}
	/** 
     * Branch Id 
	 */
	public void setBranchId(String branchId)
	{
		this.branchId=branchId;
	}	
	
	public String getBranchId()
	{
		return branchId;
	}
	/** 
     * Branch Code 
	 */
	public void setBranchCode(String branchCode)
	{
		this.branchCode=branchCode;
	}	
	
	public String getBranchCode()
	{
		return branchCode;
	}
	/**		
     * DateFormat
	 */
	public void setDateFormat(String dateFormat)
	{
		this.dateFormat=dateFormat;
	}	
	
	public String getDateFormat()
	{
		return dateFormat;
	}

	

	
	
	/**		
     * languageId
	 */
	public void setLanguageId(String languageId)
	{
		this.languageId=languageId;
	}	
	
	public String getLanguageId()
	{
		return languageId;
	}

	/**		
     * Default Window
	 */
	public void setDefaultWindow(String dfw)
	{
		this.dfw=dfw;
	}	
	
	public String getDefaultWindow()
	{
		return dfw;
	}
	
	/**		
     * versionNo
	 */
	
	public void setVersionNo(String versionNo)
	{
		this.versionNo=versionNo;
	}	
	
	public String getVersionNo()
	{
		return versionNo;
	}	
	
	/** 
     * Version Position No: Get last version as Y/N
	 */
	public void setVersionPos(String vpos)
	{
		this.vpos=vpos;
	}	
	
	public String getVersionPos()
	{
		return vpos;
	}	
	/**		
     * lastUpdatedOn
	 */	
	public void setLastUpdatedOn(Date lastUpdatedOn)
	{
		this.lastUpdatedOn=lastUpdatedOn;
	}	
	
	public Date getLastUpdatedOn()
	{
		return lastUpdatedOn;
	}

	/**		
     * lastUpdatedBy
	 */	
	public void setLastUpdatedBy(String lastUpdatedBy)
	{
		this.lastUpdatedBy=lastUpdatedBy;
	}	
	public String getLastUpdatedBy()
	{
		return lastUpdatedBy;
	}

	public void setCreatedOn(Date createdOn)
	{
		this.createdOn=createdOn;
	}	
	/**		
     * createdOn
	 */	
	public Date getCreatedOn()
	{
		return createdOn;
	}

	/**		
     * createdBy
	 */	
	public void setCreatedBy(String createdBy)
	{
		this.createdBy=createdBy;
	}	
	
	public String getCreatedBy()
	{
		return createdBy;
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

	public void setLastLoginDate(String lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	public String getLastLoginDate() {
		return lastLoginDate;
	}
}
