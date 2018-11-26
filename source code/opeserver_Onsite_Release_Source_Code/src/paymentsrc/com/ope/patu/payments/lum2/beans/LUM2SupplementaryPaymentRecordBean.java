package com.ope.patu.payments.lum2.beans;

import java.util.ArrayList;
import java.util.List;


public class LUM2SupplementaryPaymentRecordBean {

	String applicationCode,recordCode,acceptanceCode,debitRate,reservedWord1,bankInstructions,
	customerComments,swiftCode,bankNameAddress,paymentNumber,valueDate,serviceFee,debitingAccount,reservedWord2;
	
	int lineLength, seq_no;
    
	private List<String> supPaymentErrorMsg = new ArrayList<String>();
	private List<String> supPaymentSuccessMsg = new ArrayList<String>();
	private List<String> supPaymentRejectedMsg = new ArrayList<String>();
	private List<String> itemisationErrorMsg = new ArrayList<String>();
	
	
	
	
	

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

	public String getDebitRate() {
		return debitRate;
	}

	public void setDebitRate(String debitRate) {
		this.debitRate = debitRate;
	}

	public String getReservedWord1() {
		return reservedWord1;
	}

	public void setReservedWord1(String reservedWord1) {
		this.reservedWord1 = reservedWord1;
	}

	public String getBankInstructions() {
		return bankInstructions;
	}

	public void setBankInstructions(String bankInstructions) {
		this.bankInstructions = bankInstructions;
	}

	public String getCustomerComments() {
		return customerComments;
	}

	public void setCustomerComments(String customerComments) {
		this.customerComments = customerComments;
	}

	public String getSwiftCode() {
		return swiftCode;
	}

	public void setSwiftCode(String swiftCode) {
		this.swiftCode = swiftCode;
	}

	public String getBankNameAddress() {
		return bankNameAddress;
	}

	public void setBankNameAddress(String bankNameAddress) {
		this.bankNameAddress = bankNameAddress;
	}

	public String getPaymentNumber() {
		return paymentNumber;
	}

	public void setPaymentNumber(String paymentNumber) {
		this.paymentNumber = paymentNumber;
	}

	public String getValueDate() {
		return valueDate;
	}

	public void setValueDate(String valueDate) {
		this.valueDate = valueDate;
	}

	public String getServiceFee() {
		return serviceFee;
	}

	public void setServiceFee(String serviceFee) {
		this.serviceFee = serviceFee;
	}

	public String getDebitingAccount() {
		return debitingAccount;
	}

	public void setDebitingAccount(String debitingAccount) {
		this.debitingAccount = debitingAccount;
	}

	public String getReservedWord2() {
		return reservedWord2;
	}

	public void setReservedWord2(String reservedWord2) {
		this.reservedWord2 = reservedWord2;
	}

	public int getSeq_no() {
		return seq_no;
	}

	public void setSeq_no(int seq_no) {
		this.seq_no = seq_no;
	}

	public int getLineLength() {
		return lineLength;
	}

	public void setLineLength(int lineLength) {
		this.lineLength = lineLength;
	}

	public List<String> getSupPaymentErrorMsg() {
		return supPaymentErrorMsg;
	}

	public void setSupPaymentErrorMsg(List<String> supPaymentErrorMsg) {
		this.supPaymentErrorMsg = supPaymentErrorMsg;
	}

	public List<String> getSupPaymentRejectedMsg() {
		return supPaymentRejectedMsg;
	}

	public void setSupPaymentRejectedMsg(List<String> supPaymentRejectedMsg) {
		this.supPaymentRejectedMsg = supPaymentRejectedMsg;
	}

	public List<String> getSupPaymentSuccessMsg() {
		return supPaymentSuccessMsg;
	}

	public void setSupPaymentSuccessMsg(List<String> supPaymentSuccessMsg) {
		this.supPaymentSuccessMsg = supPaymentSuccessMsg;
	}
 }
