/********************************************************************************************
* Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                        *
* IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.       *
* The Program or any portion thereof may not be reproduced in any form whatsoever except    *
* as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd. *
*																							*
* File Name                   : AgreementSearchDTO.java                                     *
* Author                      : Anantaraj S                                                 *
* Creation Date               : 07-Aug-2008                                                *
* Description                 : This file serves as a java bean dto for the Agreement search*
*								Entity.	This returns value as objects for Agreement Entity. *
* Modification History        :                                                             *
*																						    *
*     Version No.               Date               Brief description of change              *
*  ---------------------------------------------------------------------------------------  *
*                       |                  |											    *
*                       |                  |											    *
*  ---------------------------------------------------------------------------------------  *
*********************************************************************************************/

package com.dnb.agreement.DTO;

import org.apache.struts.action.ActionForm;

public class AgreementSearchDTO extends ActionForm{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String agreementId;
	private String internalAgreementId;
	private String customerName;
	private String customerId;
	private String customizedName;
	private String patuId;
	private String accountNumber;
	private String versionNo;
	private String editorHold;
	private String deleteMe;
	private String businessId;
	private String agreementTitle;
	
	
	public String getAgreementTitle() {
		return agreementTitle;
	}
	public void setAgreementTitle(String agreementTitle) {
		this.agreementTitle = agreementTitle;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getAgreementId() {
		return agreementId;
	}
	public void setAgreementId(String agreementId) {
		this.agreementId = agreementId;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCustomizedName() {
		return customizedName;
	}
	public void setCustomizedName(String customizedName) {
		this.customizedName = customizedName;
	}
	public String getEditorHold() {
		return editorHold;
	}
	public void setEditorHold(String editorHold) {
		this.editorHold = editorHold;
	}
	public String getPatuId() {
		return patuId;
	}
	public void setPatuId(String patuId) {
		this.patuId = patuId;
	}
	public String getVersionNo() {
		return versionNo;
	}
	public void setVersionNo(String versionNo) {
		this.versionNo = versionNo;
	}
	public String getDeleteMe() {
		return deleteMe;
	}
	public void setDeleteMe(String deleteMe) {
		this.deleteMe = deleteMe;
	}
	public String getInternalAgreementId() {
		return internalAgreementId;
	}
	public void setInternalAgreementId(String internalAgreementId) {
		this.internalAgreementId = internalAgreementId;
	}
	public String getBusinessId() {
		return businessId;
	}
	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
	


}
