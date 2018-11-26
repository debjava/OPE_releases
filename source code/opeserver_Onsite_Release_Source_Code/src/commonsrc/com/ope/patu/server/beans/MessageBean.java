package com.ope.patu.server.beans;

public class MessageBean implements Cloneable
{
	private boolean trFileStatus;
	private String fileName;
	
	private String msgType;
	private String messageCode;
	private String message_length;
	private String version;
	//Not in use
	private String acceptance_code;
	//Not in use
	private String announcement_code;
	private String software;
	private String procedure;
//	private String receiver;
	
	private String recId;
	private String recSpec;
	
	
//	private String sender;
	
	private String senderId;
	private String senderSpec;
	
	
	private String kek_no;
	private String auk_no;
//	private String time_stamp;
	private String date;
	private String time;
	private String stampNo;
	//Not in use
	private String protection_level;
	//Not in use
	private String reserved;
	//Not in use
	private String hsk;
	//Not in use
	private String hash_value;
	private String aut_value;
	private String key_change;
	private String new_key;
	private String announcement;
	private String acknowledgement;
	
	public String getMessageCode() {
		return messageCode;
	}
	public void setMessageCode(String messageCode) {
		this.messageCode = messageCode;
	}
	public String getMessage_length() {
		return message_length;
	}
	public void setMessage_length(String message_length) {
		this.message_length = message_length;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getAcceptance_code() {
		return acceptance_code;
	}
	public void setAcceptance_code(String acceptance_code) {
		this.acceptance_code = acceptance_code;
	}
	public String getAnnouncement_code() {
		return announcement_code;
	}
	public void setAnnouncement_code(String announcement_code) {
		this.announcement_code = announcement_code;
	}
	public String getSoftware() {
		return software;
	}
	public void setSoftware(String software) {
		this.software = software;
	}
	public String getProcedure() {
		return procedure;
	}
	public void setProcedure(String procedure) {
		this.procedure = procedure;
	}
//	public String getReceiver() {
//		return receiver;
//	}
//	public void setReceiver(String receiver) {
//		this.receiver = receiver;
//	}
//	public String getSender() {
//		return sender;
//	}
//	public void setSender(String sender) {
//		this.sender = sender;
//	}
	public String getKek_no() {
		return kek_no;
	}
	public void setKek_no(String kek_no) {
		this.kek_no = kek_no;
	}
	public String getAuk_no() {
		return auk_no;
	}
	public void setAuk_no(String auk_no) {
		this.auk_no = auk_no;
	}
//	public String getTime_stamp() {
//		return time_stamp;
//	}
//	public void setTime_stamp(String time_stamp) {
//		this.time_stamp = time_stamp;
//	}
	public String getProtection_level() {
		return protection_level;
	}
	public void setProtection_level(String protection_level) {
		this.protection_level = protection_level;
	}
	public String getReserved() {
		return reserved;
	}
	public void setReserved(String reserved) {
		this.reserved = reserved;
	}
	public String getHsk() {
		return hsk;
	}
	public void setHsk(String hsk) {
		this.hsk = hsk;
	}
	public String getHash_value() {
		return hash_value;
	}
	public void setHash_value(String hash_value) {
		this.hash_value = hash_value;
	}
	public String getAut_value() {
		return aut_value;
	}
	public void setAut_value(String aut_value) {
		this.aut_value = aut_value;
	}
	public String getKey_change() {
		return key_change;
	}
	public void setKey_change(String key_change) {
		this.key_change = key_change;
	}
	public String getNew_key() {
		return new_key;
	}
	public void setNew_key(String new_key) {
		this.new_key = new_key;
	}
	public String getAnnouncement() {
		return announcement;
	}
	public void setAnnouncement(String announcement) {
		this.announcement = announcement;
	}
	public String getAcknowledgement() {
		return acknowledgement;
	}
	public void setAcknowledgement(String acknowledgement) {
		this.acknowledgement = acknowledgement;
	}
	public String getRecId() {
		return recId;
	}
	public void setRecId(String recId) {
		this.recId = recId;
	}
	public String getRecSpec() {
		return recSpec;
	}
	public void setRecSpec(String recSpec) {
		this.recSpec = recSpec;
	}
	public String getSenderId() {
		return senderId;
	}
	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}
	public String getSenderSpec() {
		return senderSpec;
	}
	public void setSenderSpec(String senderSpec) {
		this.senderSpec = senderSpec;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getStampNo() {
		return stampNo;
	}
	public void setStampNo(String stampNo) {
		this.stampNo = stampNo;
	}
	public String getMsgType() {
		return msgType;
	}
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}	
	
	public Object clone()
	{
		Object msgObject = null;
		try
		{
			msgObject =  super.clone();
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		return msgObject;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public void setTrFileStatus(boolean trFileStatus) {
		this.trFileStatus = trFileStatus;
	}
	public boolean getTrFileStatus() {
		return trFileStatus;
	}
}
