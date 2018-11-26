
/********************************************************************************************
 * Copyright 2008 IDEAL INVENT Technologies Pvt. Ltd.                                        *
 * IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.       *
 * The Program or any portion thereof may not be reproduced in any form whatsoever except    *
 * as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd. *
 *																							 *
 * File Name                   : PatuKeyAuditSearchBean.java                                 *
 * Author                      : Manjunath N G                                               *
 * Creation Date               : 05-Nov-2008                                                 *
 * Description                 : This file serves as a java bean for the Help Desk Reports   *
 *		            			 This returns value as objects for Patu Key Audit Report.    *
 * Modification History        :                                                             *
 *																						     *
 *   Version No.  Edited By         Date               Brief description of change           *
 *  ---------------------------------------------------------------------------------------  *
 *             |                    |                |                                       *
 *             |                    |				 |                                       *
 *  ---------------------------------------------------------------------------------------  *
 *********************************************************************************************/

/** 
 * Create or import Packages
 */

package com.dnb.agreement.helpdesk.bean;

import org.apache.struts.action.ActionForm;

public class PatuKeyAuditSearchBean extends ActionForm  
{
	/** 
	 * Declarations
	 */
	
	/** Input parameter for PATU Key Audit Report */
	
	/** String which stores PATU User Id */	
	private String patuUserId;
	
	/** String which stores PATU Id */
	private String patuId;
	
	/** String which stores Service Bureau Id */
	private String serviceBureauId;
	
	/** String which stores from date to be considered */
	private String dateFrom;
	
	/** String which stores to date to be considered */
	private String dateTo;
	
	/** String which stores to Service Bureau Name be considered */
	private String serviceBureauName;
	
	public String getPatuUserId()
	{
		return patuUserId;
	}
	
	public void setPatuUserId(String patuUserId)
	{
		this.patuUserId=patuUserId;
	}
	
	public String getPatuId()
	{
		return patuId;
	}
	
	public void setPatuId(String patuId)
	{
		this.patuId=patuId;
	}
	
	public String getServiceBureauId()
	{
		return serviceBureauId;
	}
	
	public void setServiceBureauId(String serviceBureauId)
	{
		this.serviceBureauId=serviceBureauId;
	}
	
	/**
	 * Sets the body of the message
	 * 
	 * @param body
	 *            String which is the DateFrom of the Process
	 */
	public void setDateFrom(String dateFrom)
	{
		this.dateFrom=dateFrom;
	}	

	/**
	 * Returns the body of the message
	 * 
	 * @return String the DateFrom of the Process
	 */
	public String getDateFrom()
	{
		return dateFrom;
	}	

	/**
	 * Sets the body of the message
	 * 
	 * @param body
	 *            String which is the DateTo of the Process
	 */
	public void setDateTo(String dateTo)
	{
		this.dateTo=dateTo;
	}	

	/**
	 * Returns the body of the message
	 * 
	 * @return String the DateTo of the Process
	 */
	public String getDateTo()
	{
		return dateTo;
	}	
	
	public String getServiceBureauName()
	{
		return serviceBureauName;
	}
	
	public void setServiceBureauName(String serviceBureauName)
	{
		this.serviceBureauName=serviceBureauName;
	}
}