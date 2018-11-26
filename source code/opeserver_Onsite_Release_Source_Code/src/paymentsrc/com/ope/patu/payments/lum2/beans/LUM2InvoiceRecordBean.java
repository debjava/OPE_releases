package com.ope.patu.payments.lum2.beans;

import java.util.ArrayList;
import java.util.List;


public class LUM2InvoiceRecordBean {

	String applicationCode,recordCode,fileType,acceptanceCode,currencyCode,invoiceType,currencyAmount,
	paymentRate,counterValue,invoiceNumber,reservedWord1,customerComments,reservedWord2;
	
	int lineLength;
	
	int seq_no;
    
	private List<String> invoiceErrorMsg = new ArrayList<String>();
	private List<String> invoiceSuccessMsg = new ArrayList<String>();
	private List<String> invoiceRejectedMsg = new ArrayList<String>();
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

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}

	public String getCurrencyAmount() {
		return currencyAmount;
	}

	public void setCurrencyAmount(String currencyAmount) {
		this.currencyAmount = currencyAmount;
	}

	public String getPaymentRate() {
		return paymentRate;
	}

	public void setPaymentRate(String paymentRate) {
		this.paymentRate = paymentRate;
	}

	public String getCounterValue() {
		return counterValue;
	}

	public void setCounterValue(String counterValue) {
		this.counterValue = counterValue;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public String getCustomerComments() {
		return customerComments;
	}

	public void setCustomerComments(String customerComments) {
		this.customerComments = customerComments;
	}

	public String getReservedWord2() {
		return reservedWord2;
	}

	public void setReservedWord2(String reservedWord2) {
		this.reservedWord2 = reservedWord2;
	}

	public int getLineLength() {
		return lineLength;
	}

	public void setLineLength(int lineLength) {
		this.lineLength = lineLength;
	}

	public List<String> getInvoiceErrorMsg() {
		return invoiceErrorMsg;
	}

	public void setInvoiceErrorMsg(List<String> invoiceErrorMsg) {
		this.invoiceErrorMsg = invoiceErrorMsg;
	}

	public List<String> getInvoiceRejectedMsg() {
		return invoiceRejectedMsg;
	}

	public void setInvoiceRejectedMsg(List<String> invoiceRejectedMsg) {
		this.invoiceRejectedMsg = invoiceRejectedMsg;
	}

	public List<String> getInvoiceSuccessMsg() {
		return invoiceSuccessMsg;
	}

	public void setInvoiceSuccessMsg(List<String> invoiceSuccessMsg) {
		this.invoiceSuccessMsg = invoiceSuccessMsg;
	}

 }
