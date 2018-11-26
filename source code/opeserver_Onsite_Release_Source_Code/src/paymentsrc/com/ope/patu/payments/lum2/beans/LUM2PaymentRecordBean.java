package com.ope.patu.payments.lum2.beans;

import java.util.ArrayList;
import java.util.List;


public class LUM2PaymentRecordBean {

	String applicationCode,recordCode,acceptanceCode,beneficiaryNameAddress,beneficiaryCountryCode,swiftCode,
	beneficiaryBankNameAddress,beneficiaryAccountCode,reasonPaymentInformation,	currencyAmount,currencyCode,
	reservedWord1,exchangeRateAgmtNo,paymentType,serviceFee, debitDate, counterValuePayment,exchangeRatePayment,
	debitingAccount, debitingAccountCurrencyCode,debitingAmount,archiveId,feedbackCurrency,reservedWord2;
	
	int lineLength;
	
	int seqNo;
    
	private List<String> paymentErrorMsg = new ArrayList<String>();
	private List<String> paymentSuccessMsg = new ArrayList<String>();
	private List<String> itemisationErrorMsg = new ArrayList<String>();
	private List<String> paymentRejectedMsg = new ArrayList<String>();
	
	public int getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(int seqNo) {
		this.seqNo = seqNo;
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

	public String getBeneficiaryNameAddress() {
		return beneficiaryNameAddress;
	}

	public void setBeneficiaryNameAddress(String beneficiaryNameAddress) {
		this.beneficiaryNameAddress = beneficiaryNameAddress;
	}

	public String getBeneficiaryCountryCode() {
		return beneficiaryCountryCode;
	}

	public void setBeneficiaryCountryCode(String beneficiaryCountryCode) {
		this.beneficiaryCountryCode = beneficiaryCountryCode;
	}

	public String getSwiftCode() {
		return swiftCode;
	}

	public void setSwiftCode(String swiftCode) {
		this.swiftCode = swiftCode;
	}

	public String getBeneficiaryBankNameAddress() {
		return beneficiaryBankNameAddress;
	}

	public void setBeneficiaryBankNameAddress(String beneficiaryBankNameAddress) {
		this.beneficiaryBankNameAddress = beneficiaryBankNameAddress;
	}

	public String getBeneficiaryAccountCode() {
		return beneficiaryAccountCode;
	}

	public void setBeneficiaryAccountCode(String beneficiaryAccountCode) {
		this.beneficiaryAccountCode = beneficiaryAccountCode;
	}

	public String getReasonPaymentInformation() {
		return reasonPaymentInformation;
	}

	public void setReasonPaymentInformation(String reasonPaymentInformation) {
		this.reasonPaymentInformation = reasonPaymentInformation;
	}

	public String getCurrencyAmount() {
		return currencyAmount;
	}

	public void setCurrencyAmount(String currencyAmount) {
		this.currencyAmount = currencyAmount;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getReservedWord1() {
		return reservedWord1;
	}

	public void setReservedWord1(String reservedWord1) {
		this.reservedWord1 = reservedWord1;
	}

	public String getExchangeRateAgmtNo() {
		return exchangeRateAgmtNo;
	}

	public void setExchangeRateAgmtNo(String exchangeRateAgmtNo) {
		this.exchangeRateAgmtNo = exchangeRateAgmtNo;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getServiceFee() {
		return serviceFee;
	}

	public void setServiceFee(String serviceFee) {
		this.serviceFee = serviceFee;
	}

	public String getDebitDate() {
		return debitDate;
	}

	public void setDebitDate(String debitDate) {
		this.debitDate = debitDate;
	}

	public String getCounterValuePayment() {
		return counterValuePayment;
	}

	public void setCounterValuePayment(String counterValuePayment) {
		this.counterValuePayment = counterValuePayment;
	}

	public String getExchangeRatePayment() {
		return exchangeRatePayment;
	}

	public void setExchangeRatePayment(String exchangeRatePayment) {
		this.exchangeRatePayment = exchangeRatePayment;
	}

	public String getDebitingAccount() {
		return debitingAccount;
	}

	public void setDebitingAccount(String debitingAccount) {
		this.debitingAccount = debitingAccount;
	}

	public String getDebitingAccountCurrencyCode() {
		return debitingAccountCurrencyCode;
	}

	public void setDebitingAccountCurrencyCode(String debitingAccountCurrencyCode) {
		this.debitingAccountCurrencyCode = debitingAccountCurrencyCode;
	}

	public String getDebitingAmount() {
		return debitingAmount;
	}

	public void setDebitingAmount(String debitingAmount) {
		this.debitingAmount = debitingAmount;
	}

	public String getArchiveId() {
		return archiveId;
	}

	public void setArchiveId(String archiveId) {
		this.archiveId = archiveId;
	}

	public String getFeedbackCurrency() {
		return feedbackCurrency;
	}

	public void setFeedbackCurrency(String feedbackCurrency) {
		this.feedbackCurrency = feedbackCurrency;
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

	public List<String> getPaymentErrorMsg() {
		return paymentErrorMsg;
	}

	public void setPaymentErrorMsg(List<String> paymentErrorMsg) {
		this.paymentErrorMsg = paymentErrorMsg;
	}

	public List<String> getPaymentSuccessMsg() {
		return paymentSuccessMsg;
	}

	public void setPaymentSuccessMsg(List<String> paymentSuccessMsg) {
		this.paymentSuccessMsg = paymentSuccessMsg;
	}

	public List<String> getPaymentRejectedMsg() {
		return paymentRejectedMsg;
	}

	public void setPaymentRejectedMsg(List<String> paymentRejectedMsg) {
		this.paymentRejectedMsg = paymentRejectedMsg;
	}
 }
