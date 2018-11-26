package com.ope.patu.payments.lum2.beans;

import java.util.ArrayList;
import java.util.List;


public class LUM2SumRecordBean {

	String applicationCode,recordCode,fileType,reservedWord1,payerBIC,customerCodeExtension,reservedWord2,
	acceptanceCode,numberOfPayments,numberOfInvoices,paymentsCurrencyAmount,reservedWord3;
	
	int lineLength;
	int seq_no;
	
	private List<String> sumErrorMsg = new ArrayList<String>();
	private List<String> sumSuccessMsg = new ArrayList<String>();
	private List<String> sumRejectedMsg = new ArrayList<String>();
	
	
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

	

	public String getReservedWord2() {
		return reservedWord2;
	}

	public void setReservedWord2(String reservedWord2) {
		this.reservedWord2 = reservedWord2;
	}

	public String getPayerBIC() {
		return payerBIC;
	}

	public void setPayerBIC(String payerBIC) {
		this.payerBIC = payerBIC;
	}

	public String getCustomerCodeExtension() {
		return customerCodeExtension;
	}

	public void setCustomerCodeExtension(String customerCodeExtension) {
		this.customerCodeExtension = customerCodeExtension;
	}

	public String getNumberOfPayments() {
		return numberOfPayments;
	}

	public void setNumberOfPayments(String numberOfPayments) {
		this.numberOfPayments = numberOfPayments;
	}

	public String getNumberOfInvoices() {
		return numberOfInvoices;
	}

	public void setNumberOfInvoices(String numberOfInvoices) {
		this.numberOfInvoices = numberOfInvoices;
	}

	public String getPaymentsCurrencyAmount() {
		return paymentsCurrencyAmount;
	}

	public void setPaymentsCurrencyAmount(String paymentsCurrencyAmount) {
		this.paymentsCurrencyAmount = paymentsCurrencyAmount;
	}

	public String getReservedWord3() {
		return reservedWord3;
	}

	public void setReservedWord3(String reservedWord3) {
		this.reservedWord3 = reservedWord3;
	}

	public int getLineLength() {
		return lineLength;
	}

	public void setLineLength(int lineLength) {
		this.lineLength = lineLength;
	}

	public List<String> getSumErrorMsg() {
		return sumErrorMsg;
	}

	public void setSumErrorMsg(List<String> sumErrorMsg) {
		this.sumErrorMsg = sumErrorMsg;
	}

	public List<String> getSumSuccessMsg() {
		return sumSuccessMsg;
	}

	public void setSumSuccessMsg(List<String> sumSuccessMsg) {
		this.sumSuccessMsg = sumSuccessMsg;
	}

	public List<String> getSumRejectedMsg() {
		return sumRejectedMsg;
	}

	public void setSumRejectedMsg(List<String> sumRejectedMsg) {
		this.sumRejectedMsg = sumRejectedMsg;
	}

 }
