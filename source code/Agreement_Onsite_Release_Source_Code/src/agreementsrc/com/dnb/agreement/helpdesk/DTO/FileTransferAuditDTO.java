
/********************************************************************************************
 * Copyright 2008 IDEAL INVENT Technologies Pvt. Ltd.                                        *
 * IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.       *
 * The Program or any portion thereof may not be reproduced in any form whatsoever except    *
 * as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd. *
 *																							 *
 * File Name                   : FileTransferAuditDTO.java                                   *
 * Author                      : Manjunath N G                                         		 *
 * Creation Date               : 19-Nov-2008                                                 *
 * Description                 : This file serves as a java DTO for the File Transfer Audit  *
 * 							   : Report														 *
 *		            			 This returns value as objects for Help Desk Reports.        *
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

package com.dnb.agreement.helpdesk.DTO;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.apache.struts.action.ActionForm;

public class FileTransferAuditDTO extends ActionForm  
{
	/** 
	 * Declarations
	 */
	
	/** Input parameter for PATU Key Audit Report */
	
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
	
	/** Boolean variable for file deletion */
	
	private boolean bo;
	
	/** 
	 *  Method to delete the temp directory "Documents"
	 */

	public boolean del(File path)  
	{		
		File[] files = path.listFiles();
		for(int i=0; i<files.length; ++i)
		{
			if(files[i].isDirectory()){
				
				del(files[i]);
			}			
			bo = files[i].delete();			
		}	
		return bo;
	}
	
	/** 
	 *  Method which returns current date with timestamp
	 */
	public String userSystemDate()
	{
		Calendar cal = Calendar.getInstance();
		Date today = cal.getTime();
		SimpleDateFormat fmt =new SimpleDateFormat("yyyy-MM-dd   HH:mm:ss");
		String todayStr = fmt.format(today);
		return todayStr;
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
