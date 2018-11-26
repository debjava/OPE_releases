
/********************************************************************************************
 * Copyright 2008 IDEAL INVENT Technologies Pvt. Ltd.                                        *
 * IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.       *
 * The Program or any portion thereof may not be reproduced in any form whatsoever except    *
 * as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd. *
 *																							 *
 * File Name                   : FileTransferAuditBean.java                                  *
 * Author                      : Manjunath N G                                               *
 * Creation Date               : 19-Nov-2008                                                 *
 * Description                 : This file serves as a java bean for the Help Desk Reports   *
 *		            			 This returns value as objects for File Transfer Audit Report*
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

public class FileTransferAuditBean extends ActionForm  
{
	/** 
	 * Declarations
	 */
	
	/** Input parameter for File Transfer Audit Report */
	
	/** String which stores PATU Id */
	private String patuId;	
	
	/** String which stores from date to be considered */
	private String dateFrom;
	
	/** String which stores to date to be considered */
	private String dateTo;
	
	/** String which stores to Agreement Id be considered */
	private String agreementId;
	
	/** String which stores to Service Type be considered */
	private String serviceType;
	
	/** String which stores to Group By be considered */
	private String groupBy;
	
	/** String which stores to Hours be considered */
	private String hoursFrom;
	
	/** String which stores to AM/PM be considered */
	private String amPmFrom;
	
	/** String which stores to Hours be considered */
	private String hoursTo;
	
	/** String which stores to AM/PM be considered */
	private String amPmTo;
	
	public String getPatuId()
	{
		return patuId;
	}
	
	public void setPatuId(String patuId)
	{
		this.patuId=patuId;
	}
	
	public String getAgreementId()
	{
		return agreementId;
	}
	
	public void setAgreementId(String agreementId)
	{
		this.agreementId=agreementId;
	}
	
	public String getServiceType()
	{
		return serviceType;
	}
	
	public void setServiceType(String serviceType)
	{
		this.serviceType=serviceType;
	}
	
	public String getGroupBy()
	{
		return groupBy;
	}
	
	public void setGroupBy(String groupBy)
	{
		this.groupBy=groupBy;
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
	
	public String getHoursFrom()
	{
		return hoursFrom;
	}
	
	public void setHoursFrom(String hoursFrom)
	{
		this.hoursFrom=hoursFrom;
	}
	
	public String getAmpmFrom()
	{
		return amPmFrom;
	}
	
	public void setAmpmFrom(String amPmFrom)
	{
		this.amPmFrom=amPmFrom;
	}
	
	public String getHoursTo()
	{
		return hoursTo;
	}
	
	public void setHoursTo(String hoursTo)
	{
		this.hoursTo=hoursTo;
	}
	
	public String getAmpmTo()
	{
		return amPmTo;
	}
	
	public void setAmpmTo(String amPmTo)
	{
		this.amPmTo=amPmTo;
	}
}