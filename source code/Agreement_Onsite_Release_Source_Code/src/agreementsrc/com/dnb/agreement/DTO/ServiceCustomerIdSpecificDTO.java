package com.dnb.agreement.DTO;
/**
 * @author Debadatta Mishra
 *
 */
public class ServiceCustomerIdSpecificDTO 
{
	protected String serviceId;
	protected String serviceCode;
	protected String bureauId;
	protected String bureauName;
	protected String agreementId;
	protected String internalAgreementId;
	protected String editorHold;
	protected String internalRefId;
	protected String versionNo;
	protected String versionPos;
	protected String suo;
	protected String accountNum;
	protected String accountName;
	protected String customerId;
	protected String customerName;
	protected String status;
	protected String listOfServiceId;
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public String getServiceCode() {
		return serviceCode;
	}
	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}
	public String getBureauId() {
		return bureauId;
	}
	public void setBureauId(String bureauId) {
		this.bureauId = bureauId;
	}
	public String getBureauName() {
		return bureauName;
	}
	public void setBureauName(String bureauName) {
		this.bureauName = bureauName;
	}
	public String getAgreementId() {
		return agreementId;
	}
	public void setAgreementId(String agreementId) {
		this.agreementId = agreementId;
	}
	public String getInternalAgreementId() {
		return internalAgreementId;
	}
	public void setInternalAgreementId(String internalAgreementId) {
		this.internalAgreementId = internalAgreementId;
	}
	public String getEditorHold() {
		return editorHold;
	}
	public void setEditorHold(String editorHold) {
		this.editorHold = editorHold;
	}
	public String getInternalRefId() {
		return internalRefId;
	}
	public void setInternalRefId(String internalRefId) {
		this.internalRefId = internalRefId;
	}
	public String getVersionNo() {
		return versionNo;
	}
	public void setVersionNo(String versionNo) {
		this.versionNo = versionNo;
	}
	public String getVersionPos() {
		return versionPos;
	}
	public void setVersionPos(String versionPos) {
		this.versionPos = versionPos;
	}
	public String getSuo() {
		return suo;
	}
	public void setSuo(String suo) {
		this.suo = suo;
	}
	public String getAccountNum() {
		return accountNum;
	}
	public void setAccountNum(String accountNum) {
		this.accountNum = accountNum;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getListOfServiceId() {
		return listOfServiceId;
	}
	public void setListOfServiceId(String listOfServiceId) {
		this.listOfServiceId = listOfServiceId;
	}
	
	public int hashCode()
	{
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + customerId.hashCode();
		return result;
	}
	
	public boolean equals( Object obj )
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final ServiceCustomerIdSpecificDTO other = (ServiceCustomerIdSpecificDTO) obj;
		if( customerId.equals(other.customerId))
			return true;
		return false;
	}
	
	
}
