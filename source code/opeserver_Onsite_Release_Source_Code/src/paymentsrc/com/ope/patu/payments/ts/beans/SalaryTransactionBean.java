package com.ope.patu.payments.ts.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * This bean class is used for holding the transactions records
 * @author anandkumar.b
 *
 */
public class SalaryTransactionBean {

	String record_code, payment_date, reserved_1,payees_acc_no, service_code, reasion_for_payment, amount,payees_name,payees_identity_no,reserved_2;
	
	private List<String> transErrorMsg = new ArrayList<String>();
	private List<String> transRejectedMsg = new ArrayList<String>();
	
	public List<String> getTransErrorMsg() {
		return transErrorMsg;
	}

	public void setTransErrorMsg(List<String> transErrorMsg) {
		this.transErrorMsg = transErrorMsg;
	}

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

	public String getReserved_1() {
		return reserved_1;
	}

	public void setReserved_1(String reserved_1) {
		this.reserved_1 = reserved_1;
	}

	public String getPayees_acc_no() {
		return payees_acc_no;
	}

	public void setPayees_acc_no(String payees_acc_no) {
		this.payees_acc_no = payees_acc_no;
	}

	public String getService_code() {
		return service_code;
	}

	public void setService_code(String service_code) {
		this.service_code = service_code;
	}

	public String getReasion_for_payment() {
		return reasion_for_payment;
	}

	public void setReasion_for_payment(String reasion_for_payment) {
		this.reasion_for_payment = reasion_for_payment;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getPayees_name() {
		return payees_name;
	}

	public void setPayees_name(String payees_name) {
		this.payees_name = payees_name;
	}

	public String getPayees_identity_no() {
		return payees_identity_no;
	}

	public void setPayees_identity_no(String payees_identity_no) {
		this.payees_identity_no = payees_identity_no;
	}

	public String getReserved_2() {
		return reserved_2;
	}

	public void setReserved_2(String reserved_2) {
		this.reserved_2 = reserved_2;
	}

	public List<String> getTransRejectedMsg() {
		return transRejectedMsg;
	}

	public void setTransRejectedMsg(List<String> transRejectedMsg) {
		this.transRejectedMsg = transRejectedMsg;
	} 

}
