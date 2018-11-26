package com.ope.patu.payments.lmp300.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * This bean class is for sum record
 * @author anandkumar.b
 *
 */
public class PaymentServiceBeanRC9 {
	String data_code,record_code,transaction_type,payers_account_number,payers_business_identity_code,file_creation_date,number_of_payments,
	       total_sum_of_payments,reserved_1,reserved_2,reserved_3,batch_identifier,edi_code,reserved_4;

	private List<String> sumErrorMsg = new ArrayList<String>();
	private List<String> sumSuccessMsg = new ArrayList<String>();
	
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

	public String getPayers_account_number() {
		return payers_account_number;
	}

	public void setPayers_account_number(String payers_account_number) {
		this.payers_account_number = payers_account_number;
	}

	public String getPayers_business_identity_code() {
		return payers_business_identity_code;
	}

	public void setPayers_business_identity_code(
			String payers_business_identity_code) {
		this.payers_business_identity_code = payers_business_identity_code;
	}

	public String getFile_creation_date() {
		return file_creation_date;
	}

	public void setFile_creation_date(String file_creation_date) {
		this.file_creation_date = file_creation_date;
	}

	public String getNumber_of_payments() {
		return number_of_payments;
	}

	public void setNumber_of_payments(String number_of_payments) {
		this.number_of_payments = number_of_payments;
	}

	public String getTotal_sum_of_payments() {
		return total_sum_of_payments;
	}

	public void setTotal_sum_of_payments(String total_sum_of_payments) {
		this.total_sum_of_payments = total_sum_of_payments;
	}

	public String getReserved_1() {
		return reserved_1;
	}

	public void setReserved_1(String reserved_1) {
		this.reserved_1 = reserved_1;
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

	public String getBatch_identifier() {
		return batch_identifier;
	}

	public void setBatch_identifier(String batch_identifier) {
		this.batch_identifier = batch_identifier;
	}

	public String getEdi_code() {
		return edi_code;
	}

	public void setEdi_code(String edi_code) {
		this.edi_code = edi_code;
	}

	public String getReserved_4() {
		return reserved_4;
	}

	public void setReserved_4(String reserved_4) {
		this.reserved_4 = reserved_4;
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
 }
