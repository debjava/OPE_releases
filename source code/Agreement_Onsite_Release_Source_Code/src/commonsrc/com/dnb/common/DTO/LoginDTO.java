/********************************************************************************************
 * Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                        *
 * IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.       *
 * The Program or any portion thereof may not be reproduced in any form whatsoever except    *
 * as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd. *
 *																							*
 * File Name                   : LoginDTO.java                                               * 
 * Author                      : Chenchi Reddy                                                    *
 * Creation Date               : 24-Jun-2008                                                 *
 * Description                 : This file serves as a java DTO for ProcessMate Login Page.  *
 *		                                                                                    *
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

package com.dnb.common.DTO;

import org.apache.struts.action.ActionForm;

public class LoginDTO{

    /** 
     * Declarations
	 */
	private String userId,password,status,newPassword,confirmPassword,changePassword;

	/** 
     * User ID
	 */
	public void setUserId(String userId)
	{
		this.userId=userId;
	}	
	
	public String getUserId()
	{
		return userId;
	}

	/** 
     * Password
	 */
	public void setPassword(String password)
	{
		this.password=password;
	}	
	
	public String getPassword()
	{
		return password;
	}
	/** 
     * Status
	 */
	public void setStatus(String status)
	{
		this.status=status;
	}	
	
	public String getStatus()
	{
		return status;
	}	

	/** 
     * New Password
	 */
	public void setNewPassword(String newPassword)
	{
		this.newPassword=newPassword;
	}	
	
	public String getNewPassword()
	{
		return newPassword;
	}
	/** 
     * Confirm Password
	 */
	public void setConfirmPassword(String confirmPassword)
	{
		this.confirmPassword=confirmPassword;
	}	
	
	public String getConfirmPassword()
	{
		return confirmPassword;
	}	
	/** 
     * Change Password
	 */
	public void setChangePassword(String changePassword)
	{
		this.changePassword=changePassword;
	}	
	
	public String getChangePassword()
	{
		return changePassword;
	}	
}
