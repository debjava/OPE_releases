package com.ope.patu.payments.lmp300.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * This bean class is for transaction and itemisation records
 * @author anandkumar.b
 *
 */
public class PaymentServiceBeanRC1 {

	String data_code, record_code, transaction_type, reserved_1,	payee_qualifier_1,payee_qualifier_2,
	payee_qualifier_3, payees_account_number, message_type, message, reserved_2, reserved_3,
	sum, reserved_4, reserved_5, reserved_6, internal_data, cost_centre, reserved_7, reserved_8, payees_name, refrance_no, itemisation_acc_no,
	itemisation_msg_type, itemisation_sum;
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
	
	public String getData_code() {
		return data_code;
	}

	public void setData_code(String data_code) {
		this.data_code = data_code;
	}

	public String getRecord_code() {
		return record_code;
	}

	public void setRecord_code(String record_code) {
		this.record_code = record_code;
	}

	public String getTransaction_type() {
		return transaction_type;
	}

	public void setTransaction_type(String transaction_type) {
		this.transaction_type = transaction_type;
	}

	public String getReserved_1() {
		return reserved_1;
	}

	public void setReserved_1(String reserved_1) {
		this.reserved_1 = reserved_1;
	}

	public String getPayee_qualifier_1() {
		return payee_qualifier_1;
	}

	public void setPayee_qualifier_1(String payee_qualifier_1) {
		this.payee_qualifier_1 = payee_qualifier_1;
	}

	public String getPayee_qualifier_2() {
		return payee_qualifier_2;
	}

	public void setPayee_qualifier_2(String payee_qualifier_2) {
		this.payee_qualifier_2 = payee_qualifier_2;
	}

	public String getPayee_qualifier_3() {
		return payee_qualifier_3;
	}

	public void setPayee_qualifier_3(String payee_qualifier_3) {
		this.payee_qualifier_3 = payee_qualifier_3;
	}

	public String getPayees_account_number() {
		return payees_account_number;
	}

	public void setPayees_account_number(String payees_account_number) {
		this.payees_account_number = payees_account_number;
	}

	public String getMessage_type() {
		return message_type;
	}

	public void setMessage_type(String message_type) {
		this.message_type = message_type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getReserved_2() {
		return reserved_2;
	}

	public void setReserved_2(String reserved_2) {
		this.reserved_2 = reserved_2;
	}

	public String getReserved_3() {
		return reserved_3;
	}

	public void setReserved_3(String reserved_3) {
		this.reserved_3 = reserved_3;
	}

	public String getSum() {
		return sum;
	}

	public void setSum(String sum) {
		this.sum = sum;
	}

	public String getReserved_4() {
		return reserved_4;
	}

	public void setReserved_4(String reserved_4) {
		this.reserved_4 = reserved_4;
	}

	public String getReserved_5() {
		return reserved_5;
	}

	public void setReserved_5(String reserved_5) {
		this.reserved_5 = reserved_5;
	}

	public String getReserved_6() {
		return reserved_6;
	}

	public void setReserved_6(String reserved_6) {
		this.reserved_6 = reserved_6;
	}

	public String getInternal_data() {
		return internal_data;
	}

	public void setInternal_data(String internal_data) {
		this.internal_data = internal_data;
	}

	public String getCost_centre() {
		return cost_centre;
	}

	public void setCost_centre(String cost_centre) {
		this.cost_centre = cost_centre;
	}

	public String getReserved_7() {
		return reserved_7;
	}

	public void setReserved_7(String reserved_7) {
		this.reserved_7 = reserved_7;
	}

	public String getReserved_8() {
		return reserved_8;
	}

	public void setReserved_8(String reserved_8) {
		this.reserved_8 = reserved_8;
	}

	public String getPayees_name() {
		return payees_name;
	}

	public void setPayees_name(String payees_name) {
		this.payees_name = payees_name;
	}

	public String getRefrance_no() {
		return refrance_no;
	}

	public void setRefrance_no(String refrance_no) {
		this.refrance_no = refrance_no;
	}

	public int getSeq_no() {
		return seq_no;
	}

	public void setSeq_no(int seq_no) {
		this.seq_no = seq_no;
	}

	public List<String> getItemisationErrorMsg() {
		return itemisationErrorMsg;
	}

	public void setItemisationErrorMsg(List<String> itemisationErrorMsg) {
		this.itemisationErrorMsg = itemisationErrorMsg;
	}

	public String getItemisation_acc_no() {
		return itemisation_acc_no;
	}

	public void setItemisation_acc_no(String itemisation_acc_no) {
		this.itemisation_acc_no = itemisation_acc_no;
	}

	public String getItemisation_msg_type() {
		return itemisation_msg_type;
	}

	public void setItemisation_msg_type(String itemisation_msg_type) {
		this.itemisation_msg_type = itemisation_msg_type;
	}

	public String getItemisation_sum() {
		return itemisation_sum;
	}

	public void setItemisation_sum(String itemisation_sum) {
		this.itemisation_sum = itemisation_sum;
	}
 }
