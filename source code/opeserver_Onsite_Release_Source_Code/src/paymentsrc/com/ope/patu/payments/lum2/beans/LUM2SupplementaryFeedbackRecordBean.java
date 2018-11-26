package com.ope.patu.payments.lum2.beans;

import java.util.ArrayList;
import java.util.List;


public class LUM2SupplementaryFeedbackRecordBean {

	String applicationCode,recordCode,acceptanceCode,beneficiaryBankSwiftCode,beneficiaryBank,
	beneficiaryBankAccountNumber,intermediaryBankSwiftCode, intermediaryBankNameAddress,intermediaryBankAccountNumber,
	customerInformation,reservedWord1;
	
	int lineLength;
	
	int seq_no;
    
	private List<String> transErrorMsg = new ArrayList<String>();
	private List<String> transSuccessMsg = new ArrayList<String>();
	private List<String> itemisationErrorMsg = new ArrayList<String>();;
	
	public List<String> getTransErrorMsg() {
		return transErrorMsg;
	}

	public void setTransErrorMsg(List<String> transErrorMsg) {
		this.transErrorMsg = transErrorMsg;
	}

	public List<String> getTransSuccessMsg() {
		return transSuccessMsg;
	}

	public void setTransSuccessMsg(List<String> transSuccessMsg) {
		this.transSuccessMsg = transSuccessMsg;
	}
	

	public List<String> getItemisationErrorMsg() {
		return itemisationErrorMsg;
	}

	public void setItemisationErrorMsg(List<String> itemisationErrorMsg) {
		this.itemisationErrorMsg = itemisationErrorMsg;
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

	public String getAcceptanceCode() {
		return acceptanceCode;
	}

	public void setAcceptanceCode(String acceptanceCode) {
		this.acceptanceCode = acceptanceCode;
	}	

	public String getReservedWord1() {
		return reservedWord1;
	}

	public void setReservedWord1(String reservedWord1) {
		this.reservedWord1 = reservedWord1;
	}

	public int getSeq_no() {
		return seq_no;
	}

	public void setSeq_no(int seq_no) {
		this.seq_no = seq_no;
	}

	public String getBeneficiaryBankSwiftCode() {
		return beneficiaryBankSwiftCode;
	}

	public void setBeneficiaryBankSwiftCode(String beneficiaryBankSwiftCode) {
		this.beneficiaryBankSwiftCode = beneficiaryBankSwiftCode;
	}

	public String getBeneficiaryBank() {
		return beneficiaryBank;
	}

	public void setBeneficiaryBank(String beneficiaryBank) {
		this.beneficiaryBank = beneficiaryBank;
	}

	public String getBeneficiaryBankAccountNumber() {
		return beneficiaryBankAccountNumber;
	}

	public void setBeneficiaryBankAccountNumber(String beneficiaryBankAccountNumber) {
		this.beneficiaryBankAccountNumber = beneficiaryBankAccountNumber;
	}

	public String getIntermediaryBankSwiftCode() {
		return intermediaryBankSwiftCode;
	}

	public void setIntermediaryBankSwiftCode(String intermediaryBankSwiftCode) {
		this.intermediaryBankSwiftCode = intermediaryBankSwiftCode;
	}

	public String getIntermediaryBankNameAddress() {
		return intermediaryBankNameAddress;
	}

	public void setIntermediaryBankNameAddress(String intermediaryBankNameAddress) {
		this.intermediaryBankNameAddress = intermediaryBankNameAddress;
	}

	public String getIntermediaryBankAccountNumber() {
		return intermediaryBankAccountNumber;
	}

	public void setIntermediaryBankAccountNumber(
			String intermediaryBankAccountNumber) {
		this.intermediaryBankAccountNumber = intermediaryBankAccountNumber;
	}

	public String getCustomerInformation() {
		return customerInformation;
	}

	public void setCustomerInformation(String customerInformation) {
		this.customerInformation = customerInformation;
	}

	public int getLineLength() {
		return lineLength;
	}

	public void setLineLength(int lineLength) {
		this.lineLength = lineLength;
	}
 }
