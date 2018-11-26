
/********************************************************************************************
 * Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                        *
 * IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.       *
 * The Program or any portion thereof may not be reproduced in any form whatsoever except    *
 * as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd. *
 *																							*
 * File Name                   : ServiceBureauDTO.java                                      *
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
package com.dnb.agreement.DTO;

import org.apache.struts.action.ActionForm;
import com.dnb.agreement.common.OPEConstants;

import java.util.Date;
import java.text.SimpleDateFormat;

public class ServiceBureauDTO extends ActionForm  
{
	
	private static final long serialVersionUID = 1L;
	/** 
	 * Declarations
	 */
	private String editorHold,versionNo,versionPos,internalBureauId,bureauId,bureauName,bureauDescription,city,
	addressLine1,addressLine2,addressLine3,addressLine4,telephoneNo1,telephoneNo2,authorizedOn,authorizedBy,
	pinCode,country,companyName,patuId,keyKEK,keyAUK,createdBy,lastUpdatedBy,status,reopen,
	keyKEKPart1,keyKEKPart2,keyKVV,generationNumber,generationDate,keyPart1,keyPart2,keyExpireDate,kekStatus;
	/**
	 * These declarations are for display the KEK and AUK keys in service bureau edit page
	 */
	private String createdOn,lastUpdatedOn,controlCode;
	private String country1,country2;
	private String contactPerson1,contactPerson2;
	private String city1,city2;
	private String pinCode1,pinCode2;
	private String aukGenerationNo;


	/** 
	 * Reopen
	 */
	public String getReopen() {
		return reopen;
	}

	public void setReopen(String reopen) {
		this.reopen = reopen;
	}

	/** 
	 * Edit OR Hold : Hold status
	 */
	public void setEditorHold(String editorHold)
	{
		this.editorHold=editorHold;
	}	

	public String getEditorHold()
	{
		return editorHold;
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
		versionNo=versionNo.trim();
		if(versionNo.length()==0){
			return null;
		}else
			return versionNo;
	}	

	/** 
	 * Version Position No: Get last version as Y/N
	 */
	public void setVersionPos(String versionPos)
	{
		this.versionPos=versionPos;
	}	

	public String getVersionPos()
	{
		return versionPos;
	}	

	/** 
	 * Address Line 1
	 */	
	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}
	/** 
	 * Address Line 2
	 */
	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}
	/** 
	 * Address Line 3
	 */
	public String getAddressLine3() {
		return addressLine3;
	}

	public void setAddressLine3(String addressLine3) {
		this.addressLine3 = addressLine3;
	}

	/** 
	 * Address Line 4
	 */
	public String getAddressLine4() {
		return addressLine4;
	}

	public void setAddressLine4(String addressLine4) {
		this.addressLine4 = addressLine4;
	}

	/** 
	 * Bureau Description
	 */
	public String getBureauDescription() {
		return bureauDescription;
	}

	public void setBureauDescription(String bureauDescription) {
		this.bureauDescription = bureauDescription;
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
	 * Bureau Name
	 */

	public String getBureauName() {
		return bureauName;
	}

	public void setBureauName(String bureauName) {
		this.bureauName = bureauName;
	}

	/** 
	 * City
	 */
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	/** 
	 * Country
	 */
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
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
	 * Key KEK
	 */

	public String getKeyKEK() {
		return keyKEK;
	}

	public void setKeyKEK(String keyKEK) {
		this.keyKEK = keyKEK;
	}

	/** 
	 * Key KEK
	 */

	public String getKeyAUK() {
		return keyAUK;
	}

	public void setKeyAUK(String keyAUK) {
		this.keyAUK = keyAUK;
	}

	/** 
	 * Pincode
	 */	
	public String getPinCode() {
		return pinCode;
	}

	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}

	/** 
	 * Telephone No 1
	 */
	public String getTelephoneNo1() {
		return telephoneNo1;
	}

	public void setTelephoneNo1(String telephoneNo1) {
		this.telephoneNo1 = telephoneNo1;
	}

	/** 
	 * Telephone No 2
	 */
	public String getTelephoneNo2() {
		return telephoneNo2;
	}

	public void setTelephoneNo2(String telephoneNo2) {
		this.telephoneNo2 = telephoneNo2;
	}

	/**************************** Audit Log Details ****************************/

	/**  
	 * Created By
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
	 * Created On
	 */
	public void setCreatedOn(String createdOn)
	{
		this.createdOn=createdOn;
	}	

	public Date getCreatedOn()
	{
		return parseDate(createdOn);
	}

	/**  
	 * Last Updated By
	 */
	public void setLastUpdatedBy(String lastUpdatedBy)
	{
		this.lastUpdatedBy=lastUpdatedBy;
	}	

	public String getLastUpdatedBy()
	{
		return lastUpdatedBy;
	}

	/**  
	 * Last Updated On
	 */
	public void setLastUpdatedOn(String lastUpdatedOn)
	{
		this.lastUpdatedOn=lastUpdatedOn;
	}	

	public Date getLastUpdatedOn()
	{
		return parseDate(lastUpdatedOn);
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
	 * kek part 1
	 * @return
	 */
	public String getKeyKEKPart1() {
		return keyKEKPart1;
	}

	public void setKeyKEKPart1(String keyKEKPart1) {
		this.keyKEKPart1 = keyKEKPart1;
	}

	/**
	 * kek part 2
	 * @return
	 */
	public String getKeyKEKPart2() {
		return keyKEKPart2;
	}

	public void setKeyKEKPart2(String keyKEKPart2) {
		this.keyKEKPart2 = keyKEKPart2;
	}

	/**
	 * KVV key
	 * @return
	 */
	public String getKeyKVV() {
		return keyKVV;
	}

	public void setKeyKVV(String keyKVV) {
		this.keyKVV = keyKVV;
	}

	/**
	 * parse string date to sql date 
	 * @param dt
	 * @return
	 */
	private Date parseDate(String dt)
	{
		Date parsedDate=null;
		try{
			SimpleDateFormat sdf=new SimpleDateFormat(OPEConstants.DATE_FORMAT);
			parsedDate= sdf.parse(dt);
		} 
		catch(java.text.ParseException pe)
		{
			System.out.println("Date Parse Exception in EnterpriseDTO");
		}
		return parsedDate;
	}
	/**
	 * generation number
	 * @return
	 */
	public String getGenerationNumber() {
		return generationNumber;
	}

	public void setGenerationNumber(String generationNumber) {
		this.generationNumber = generationNumber;
	}

	/**
	 * generation date
	 * @return
	 */
	public String getGenerationDate() {
		return generationDate;
	}

	public void setGenerationDate(String generationDate) {
		this.generationDate = generationDate;
	}


	public String getKeyPart1() {
		return keyPart1;
	}

	public void setKeyPart1(String keyPart1) {
		this.keyPart1 = keyPart1;
	}

	public String getKeyPart2() {
		return keyPart2;
	}

	public void setKeyPart2(String keyPart2) {
		this.keyPart2 = keyPart2;
	}

	public String getKeyExpireDate() {
		return keyExpireDate;
	}

	public void setKeyExpireDate(String keyExpireDate) {
		this.keyExpireDate = keyExpireDate;
	}
	/**
	 * key status
	 * @return
	 */
	public String getKekStatus() {
		return kekStatus;
	}

	public void setKekStatus(String kekStatus) {
		this.kekStatus = kekStatus;
	}
	/**
	 * control code
	 * @return
	 */
	public String getControlCode() {
		return controlCode;
	}

	public void setControlCode(String controlCode) {
		this.controlCode = controlCode;
	}
	/**
	 * country 1
	 * @return
	 */
	public String getCountry1() {
		return country1;
	}

	public void setCountry1(String country1) {
		this.country1 = country1;
	}
	/**
	 * country 1
	 * @return
	 */
	public String getCountry2() {
		return country2;
	}

	public void setCountry2(String country2) {
		this.country2 = country2;
	}
	/**
	 * contact person 1
	 * @return
	 */
	public String getContactPerson1() {
		return contactPerson1;
	}

	public void setContactPerson1(String contactPerson1) {
		this.contactPerson1 = contactPerson1;
	}
	/**
	 * contact person 2
	 * @return
	 */
	public String getContactPerson2() {
		return contactPerson2;
	}

	public void setContactPerson2(String contactPerson2) {
		this.contactPerson2 = contactPerson2;
	}
	/**
	 * 
	 * @return
	 */
	public String getCity1() {
		return city1;
	}

	public void setCity1(String city1) {
		this.city1 = city1;
	}
	/**
	 * city 2
	 * @return
	 */
	public String getCity2() {
		return city2;
	}

	public void setCity2(String city2) {
		this.city2 = city2;
	}
	/**
	 * pin code 1
	 * @return
	 */
	public String getPinCode1() {
		return pinCode1;
	}

	public void setPinCode1(String pinCode1) {
		this.pinCode1 = pinCode1;
	}
	/**
	 * pin code 2
	 * @return
	 */
	public String getPinCode2() {
		return pinCode2;
	}

	public void setPinCode2(String pinCode2) {
		this.pinCode2 = pinCode2;
	}
	/**
	 * auk generation no
	 * @return
	 */
	public String getAukGenerationNo() {
		return aukGenerationNo;
	}

	public void setAukGenerationNo(String aukGenerationNo) {
		this.aukGenerationNo = aukGenerationNo;
	}	
	
	/**  
	 * Authorized By
	 */
	public void setAuthorizedBy(String authorizedBy)
	{
		this.authorizedBy=authorizedBy;
	}	

	public String getAuthorizedBy()
	{
		return authorizedBy;
	}

	/**  
	 * Authorized On
	 */
	public void setAuthorizedOn(String authorizedOn)
	{
		this.authorizedOn=authorizedOn;
	}	

	public Date getAuthorizedOn()
	{
		return parseDate(authorizedOn);
	}
}
