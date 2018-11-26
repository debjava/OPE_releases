package com.ope.patu.payments.lum2.beans;

import java.util.ArrayList;
import java.util.List;


public class LUM2BatchRecordBean {

	String applicationCode,recordCode, fileType,reservedWord1,payersBusinessIdentityCode,
	customerCodeExtension,reservedWord2,acceptanceCode,fileCreationDate,fileCreationTime,
	reservedWord3,debitDate,reservedWord4;
	
	int lineLength;
		
	private List<String> batchErrorMsg = new ArrayList<String>();
	private List<String> batchSuccessMsg = new ArrayList<String>();
	private List<String> batchRejectedMsg = new ArrayList<String>();
	
	public List<String> getBatchSuccessMsg() {
		return batchSuccessMsg;
	}

	public void setBatchSuccessMsg(List<String> batchSuccessMsg) {
		this.batchSuccessMsg = batchSuccessMsg;
	}

	public String getApplicationCode() {
		return applicationCode;
	}

	public void setApplicationCode(String applicationCode) {
		this.applicationCode = applicationCode;
	}

	public String getRecordCode() {
		return recordCode;
	}

	public void setRecordCode(String recordCode) {
		this.recordCode = recordCode;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getReservedWord1() {
		return reservedWord1;
	}

	public void setReservedWord1(String reservedWord1) {
		this.reservedWord1 = reservedWord1;
	}

	public String getPayersBusinessIdentityCode() {
		return payersBusinessIdentityCode;
	}

	public void setPayersBusinessIdentityCode(String payersBusinessIdentityCode) {
		this.payersBusinessIdentityCode = payersBusinessIdentityCode;
	}

	public String getCustomerCodeExtension() {
		return customerCodeExtension;
	}

	public void setCustomerCodeExtension(String customerCodeExtension) {
		this.customerCodeExtension = customerCodeExtension;
	}

	public String getReservedWord2() {
		return reservedWord2;
	}

	public void setReservedWord2(String reservedWord2) {
		this.reservedWord2 = reservedWord2;
	}

	public String getAcceptanceCode() {
		return acceptanceCode;
	}

	public void setAcceptanceCode(String acceptanceCode) {
		this.acceptanceCode = acceptanceCode;
	}

	public String getFileCreationDate() {
		return fileCreationDate;
	}

	public void setFileCreationDate(String fileCreationDate) {
		this.fileCreationDate = fileCreationDate;
	}

	public String getFileCreationTime() {
		return fileCreationTime;
	}

	public void setFileCreationTime(String fileCreationTime) {
		this.fileCreationTime = fileCreationTime;
	}

	public String getReservedWord3() {
		return reservedWord3;
	}

	public void setReservedWord3(String reservedWord3) {
		this.reservedWord3 = reservedWord3;
	}

	public String getDebitDate() {
		return debitDate;
	}

	public void setDebitDate(String debitDate) {
		this.debitDate = debitDate;
	}

	public String getReservedWord4() {
		return reservedWord4;
	}

	public void setReservedWord4(String reservedWord4) {
		this.reservedWord4 = reservedWord4;
	}

	public int getLineLength() {
		return lineLength;
	}

	public void setLineLength(int lineLength) {
		this.lineLength = lineLength;
	}

	public List<String> getBatchErrorMsg() {
		return batchErrorMsg;
	}

	public void setBatchErrorMsg(List<String> batchErrorMsg) {
		this.batchErrorMsg = batchErrorMsg;
	}

	public List<String> getBatchRejectedMsg() {
		return batchRejectedMsg;
	}

	public void setBatchRejectedMsg(List<String> batchRejectedMsg) {
		this.batchRejectedMsg = batchRejectedMsg;
	}

	
}
