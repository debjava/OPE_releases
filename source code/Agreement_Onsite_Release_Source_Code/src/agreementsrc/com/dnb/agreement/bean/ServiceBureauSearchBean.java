
/********************************************************************************************
* Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                        *
* IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.       *
* The Program or any portion thereof may not be reproduced in any form whatsoever except    *
* as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd. *
*																							*
* File Name                   : ServiceBureauSearchBean.java                                      *
* Author                      : Manjunath N G                                               *
* Creation Date               : 28-July-2008                                                *
* Description                 : This file serves as a java bean for							* 
* 								the Service Bureau Entity.									*
*		            			This returns value as objects for Service Bureau Entity.    *
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

import org.apache.struts.action.ActionForm;

public class ServiceBureauSearchBean extends ActionForm
{
    /** 
     * Declarations
	 */
	
	private String versionNo,internalBureauId,bureauId,bureauName,companyName,patuId,deleteMe,editorHold;
	private String[] selectedStatus;

	/** 
     * Editor Hold
     */	
	public String getEditorHold() {
		return editorHold;
	}

	public void setEditorHold(String editorHold) {
		this.editorHold = editorHold;
	}

	/** 
     * Version No
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
     * Internal Bureau Id
	 */	
	public String getInternalBureauId() {
		return internalBureauId;
	}

	public void setInternalBureauId(String internalBureauId) {
		this.internalBureauId = internalBureauId;
	}
	
	/** 
     * Bureau Id
	 */	
	public String getBureauId() {
		return bureauId;
	}

	public void setBureauId(String bureauId) {
		this.bureauId = bureauId;
	}
	
	/** 
     * Service Bureau Name
	 */
	public String getBureauName() {
		return bureauName;
	}

	public void setBureauName(String bureauName) {
		this.bureauName = bureauName;
	}
	
	/** 
     * Company Name
	 */	
	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	/** 
     * PATU ID
	 */	
	public String getPatuId() {
		return patuId;
	}

	public void setPatuId(String patuId) {
		this.patuId = patuId;
	}
    
	/** 
     * Delete
	 */
	public void setDeleteMe(String deleteMe)
	{
		this.deleteMe=deleteMe;
	}	
	
	public String getDeleteMe()
	{
		return deleteMe;
	}
	
	/**
	 * @return Selected Status
	 */
	public String[] getSelectedStatus()
	{
		
			return selectedStatus;
		
	}
	/**
	 * @param Selected Status
	 */
	public void setSelectedStatus(String[] selectedStatus) {
		this.selectedStatus = selectedStatus;
	}
}