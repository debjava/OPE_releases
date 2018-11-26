package com.ope.patu.server.beans;

import java.io.Serializable;

public class TransferRequestBean implements Serializable
{
	 String recordId;
	 String customerId;
	 String fileType;
	 String serviceId;
	 String password;
	 String date="000000";
	 String reserved;
	 String endCard="9";
	 String  redo="9";
	 String acknRequest="7";
	 String format="9";
	 String reserved1;
	 String recordLength="999";

	public String getRecordId() {
		return recordId;
	}
	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getReserved() {
		return reserved;
	}
	public void setReserved(String reserved) {
		this.reserved = reserved;
	}
	public String getEndCard() {
		return endCard;
	}
	public void setEndCard(String endCard) {
		this.endCard = endCard;
	}
	public String getRedo() {
		return redo;
	}
	public void setRedo(String redo) {
		this.redo = redo;
	}
	public String getAcknRequest() {
		return acknRequest;
	}
	public void setAcknRequest(String acknRequest) {
		this.acknRequest = acknRequest;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public String getReserved1() {
		return reserved1;
	}
	public void setReserved1(String reserved1) {
		this.reserved1 = reserved1;
	}
	public String getRecordLength() {
		return recordLength;
	}
	public void setRecordLength(String recordLength) {
		this.recordLength = recordLength;
	}
}
