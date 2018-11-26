package com.ope.patu.payments.ts.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * This bean is used for salary payment service sum record
 * @author anandkumar.b
 *
 */
public class SalarySumBean {
	
	String record_code, payment_date,payrs_name_qualifier, service_code,reason_for_payments,total_sum_of_payments,currency_code,reserved,number_of_payments,data_from_customer;
	
	private List<String> sumErrorMsg = new ArrayList<String>();
	private List<String> sumRejectedMsg = new ArrayList<String>();

	public String getRecord_code() {
		return record_code;
	}

	public void setRecord_code(String record_code) {
		this.record_code = record_code;
	}

	public String getPayment_date() {
		return payment_date;
	}

	public void setPayment_date(String payment_date) {
		this.payment_date = payment_date;
	}

	public String getPayrs_name_qualifier() {
		return payrs_name_qualifier;
	}

	public void setPayrs_name_qualifier(String payrs_name_qualifier) {
		this.payrs_name_qualifier = payrs_name_qualifier;
	}

	public String getService_code() {
		return service_code;
	}

	public void setService_code(String service_code) {
		this.service_code = service_code;
	}

	public String getReason_for_payments() {
		return reason_for_payments;
	}

	public void setReason_for_payments(String reason_for_payments) {
		this.reason_for_payments = reason_for_payments;
	}

	public String getTotal_sum_of_payments() {
		return total_sum_of_payments;
	}

	public void setTotal_sum_of_payments(String total_sum_of_payments) {
		this.total_sum_of_payments = total_sum_of_payments;
	}

	public String getCurrency_code() {
		return currency_code;
	}

	public void setCurrency_code(String currency_code) {
		this.currency_code = currency_code;
	}

	public String getReserved() {
		return reserved;
	}

	public void setReserved(String reserved) {
		this.reserved = reserved;
	}

	public String getNumber_of_payments() {
		return number_of_payments;
	}

	public void setNumber_of_payments(String number_of_payments) {
		this.number_of_payments = number_of_payments;
	}

	public String getData_from_customer() {
		return data_from_customer;
	}

	public void setData_from_customer(String data_from_customer) {
		this.data_from_customer = data_from_customer;
	}

	public List<String> getSumRejectedMsg() {
		return sumRejectedMsg;
	}

	public void setSumRejectedMsg(List<String> sumRejectedMsg) {
		this.sumRejectedMsg = sumRejectedMsg;
	}

	public List<String> getSumErrorMsg() {
		return sumErrorMsg;
	}

	public void setSumErrorMsg(List<String> sumErrorMsg) {
		this.sumErrorMsg = sumErrorMsg;
	}
}
