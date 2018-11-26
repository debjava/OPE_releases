package com.ope.patu.payments.lmp300.beans;

import java.util.ArrayList;
import java.util.List;
/**
 * This bean class is for batch record
 * @author anandkumar.b
 *      
 */

public class PaymentServiceBeanRC0 {

	String data_code,record_code, transaction_type, payers_account_number,payers_business_identity_code, file_creation_date,
	file_creation_time,receiving_bank,due_date,payers_name_qualifier,batch_identifier,edi_code,currency_code,reserved1,file_content,
	service_code,reserved2;
	int line_length;
	
private List<String> errorMsg = new ArrayList<String>();
private List<String> batchSuccessMsg = new ArrayList<String>();

//	private String errorMsg;

	public List<String> getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(List<String> errorMsg) {
		this.errorMsg = errorMsg;
	}
	
		
	public List<String> getBatchSuccessMsg() {
		return batchSuccessMsg;
	}

	public void setBatchSuccessMsg(List<String> batchSuccessMsg) {
		this.batchSuccessMsg = batchSuccessMsg;
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

	public String getFile_creation_time() {
		return file_creation_time;
	}

	public void setFile_creation_time(String file_creation_time) {
		this.file_creation_time = file_creation_time;
	}

	public String getReceiving_bank() {
		return receiving_bank;
	}

	public void setReceiving_bank(String receiving_bank) {
		this.receiving_bank = receiving_bank;
	}

	public String getDue_date() {
		return due_date;
	}

	public void setDue_date(String due_date) {
		this.due_date = due_date;
	}

	public String getPayers_name_qualifier() {
		return payers_name_qualifier;
	}

	public void setPayers_name_qualifier(String payers_name_qualifier) {
		this.payers_name_qualifier = payers_name_qualifier;
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

	public String getCurrency_code() {
		return currency_code;
	}

	public void setCurrency_code(String currency_code) {
		this.currency_code = currency_code;
	}

	public String getReserved1() {
		return reserved1;
	}

	public void setReserved1(String reserved1) {
		this.reserved1 = reserved1;
	}

	public String getFile_content() {
		return file_content;
	}

	public void setFile_content(String file_content) {
		this.file_content = file_content;
	}

	public String getService_code() {
		return service_code;
	}

	public void setService_code(String service_code) {
		this.service_code = service_code;
	}

	public String getReserved2() {
		return reserved2;
	}

	public void setReserved2(String reserved2) {
		this.reserved2 = reserved2;
	}

	public int getLine_length() {
		return line_length;
	}

	public void setLine_length(int line_length) {
		this.line_length = line_length;
	}
}
