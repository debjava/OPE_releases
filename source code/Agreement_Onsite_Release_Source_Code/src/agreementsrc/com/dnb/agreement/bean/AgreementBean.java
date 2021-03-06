/********************************************************************************************
 * Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                       *
 * IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.      *
 * The Program or any portion thereof may not be reproduced in any form whatsoever except   *
 * as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd.*
 *																							*
 * File Name                   : AgreementBean.java	                            *
 * Author                      : Anantaraj							                    *
 * Creation Date               : 18-July-08                                                  *
 * Description                 : Bean for Agreement 							    * 								 									                 
 * Modification History        :                                                            *																						    
 * Version No.               Date               Brief description of change                 *
 *----------------------------------------------------------------------------------------- *
 *                       |                  |											    *
 *                       |                  |											    *
 *  --------------------------------------------------------------------------------------- *
 ********************************************************************************************/
package com.dnb.agreement.bean;

public class AgreementBean{
private String agreementId,branchName,contactPerson,dnbManager,address1,address2,address3,address4,postalAddress1;
private String postalAddress2,postalAddress3,postalAddress4,pincode,city,tel1,tel2,language,country,accountForFees;
private String dateofAgreement,validFrom,validTo,suffix,securityMethod,patuId;
private String versionNo,createdOn,createdBy,lastUpdatedBy,lastUpdatedOn,status;
private String customerId,customerName,address,businessId;
private String branchId;
private String internalAgreementId;
private String authorisedBy;
private String authorisedOn;

public String getAccountForFees() {
	return accountForFees;
}
public void setAccountForFees(String accountForFees) {
	this.accountForFees = accountForFees;
}
public String getAddress1() {
	return address1;
}
public void setAddress1(String address1) {
	this.address1 = address1;
}
public String getAddress2() {
	return address2;
}
public void setAddress2(String address2) {
	this.address2 = address2;
}
public String getAddress3() {
	return address3;
}
public void setAddress3(String address3) {
	this.address3 = address3;
}
public String getAddress4() {
	return address4;
}
public void setAddress4(String address4) {
	this.address4 = address4;
}
public String getAgreementId() {
	return agreementId;
}
public void setAgreementId(String agreementId) {
	this.agreementId = agreementId;
}
public String getBranchName() {
	return branchName;
}
public void setBranchName(String branchName) {
	this.branchName = branchName;
}
public String getCity() {
	return city;
}
public void setCity(String city) {
	this.city = city;
}
public String getContactPerson() {
	return contactPerson;
}
public void setContactPerson(String contactPerson) {
	this.contactPerson = contactPerson;
}
public String getCountry() {
	return country;
}
public void setCountry(String country) {
	this.country = country;
}
public String getDateofAgreement() {
	return dateofAgreement;
}
public void setDateofAgreement(String dateofAgreement) {
	this.dateofAgreement = dateofAgreement;
}
public String getDnbManager() {
	return dnbManager;
}
public void setDnbManager(String dnbManager) {
	this.dnbManager = dnbManager;
}
public String getLanguage() {
	return language;
}
public void setLanguage(String language) {
	this.language = language;
}
public String getPatuId() {
	return patuId;
}
public void setPatuId(String patuId) {
	this.patuId = patuId;
}
public String getPincode() {
	return pincode;
}
public void setPincode(String pincode) {
	this.pincode = pincode;
}
public String getPostalAddress1() {
	return postalAddress1;
}
public void setPostalAddress1(String postalAddress1) {
	this.postalAddress1 = postalAddress1;
}
public String getPostalAddress2() {
	return postalAddress2;
}
public void setPostalAddress2(String postalAddress2) {
	this.postalAddress2 = postalAddress2;
}
public String getPostalAddress3() {
	return postalAddress3;
}
public void setPostalAddress3(String postalAddress3) {
	this.postalAddress3 = postalAddress3;
}
public String getPostalAddress4() {
	return postalAddress4;
}
public void setPostalAddress4(String postalAddress4) {
	this.postalAddress4 = postalAddress4;
}
public String getSecurityMethod() {
	return securityMethod;
}
public void setSecurityMethod(String securityMethod) {
	this.securityMethod = securityMethod;
}
public String getSuffix() {
	return suffix;
}
public void setSuffix(String suffix) {
	this.suffix = suffix;
}
public String getTel1() {
	return tel1;
}
public void setTel1(String tel1) {
	this.tel1 = tel1;
}
public String getTel2() {
	return tel2;
}
public void setTel2(String tel2) {
	this.tel2 = tel2;
}
public String getValidFrom() {
	return validFrom;
}
public void setValidFrom(String validFrom) {
	this.validFrom = validFrom;
}
public String getValidTo() {
	return validTo;
}
public void setValidTo(String validTo) {
	this.validTo = validTo;
}
public String getCreatedBy() {
	return createdBy;
}
public void setCreatedBy(String createdBy) {
	this.createdBy = createdBy;
}
public String getCreatedOn() {
	return createdOn;
}
public void setCreatedOn(String createdOn) {
	this.createdOn = createdOn;
}
public String getLastUpdatedBy() {
	return lastUpdatedBy;
}
public void setLastUpdatedBy(String lastUpdatedBy) {
	this.lastUpdatedBy = lastUpdatedBy;
}
public String getLastUpdatedOn() {
	return lastUpdatedOn;
}
public void setLastUpdatedOn(String lastUpdatedOn) {
	this.lastUpdatedOn = lastUpdatedOn;
}
public String getVersionNo() {
	return versionNo;
}
public void setVersionNo(String versionNo) {
	this.versionNo = versionNo;
}
public String getStatus() {
	return status;
}
public void setStatus(String status) {
	this.status = status;
}
public String getAddress() {
	return address;
}
public void setAddress(String address) {
	this.address = address;
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
public String getBranchId() {
	return branchId;
}
public void setBranchId(String branchId) {
	this.branchId = branchId;
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
public String getAuthorisedBy() {
	return authorisedBy;
}
public void setAuthorisedBy(String authorisedBy) {
	this.authorisedBy = authorisedBy;
}
public String getAuthorisedOn() {
	return authorisedOn;
}
public void setAuthorisedOn(String authorisedOn) {
	this.authorisedOn = authorisedOn;
}
}
