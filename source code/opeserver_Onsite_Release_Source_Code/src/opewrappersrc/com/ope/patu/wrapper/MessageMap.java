package com.ope.patu.wrapper;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import com.ope.patu.server.beans.MessageBean;


public class MessageMap {

	protected static Logger logger = Logger.getLogger( MessageMap.class );
	
	@SuppressWarnings("unchecked")
	public static Map getMessageMap(MessageBean bean)
	{
		logger.debug(":::: Inside getMessageMap :: Bean ");
		Map map = new HashMap();
		
		map.put(ServerMapConstants.BEAN_ACCEPTANCE_CODE, bean.getAcceptance_code());
		map.put(ServerMapConstants.BEAN_ACKNOWLEDGEMENT, bean.getAcknowledgement());
		map.put(ServerMapConstants.BEAN_ANNOUNCEMENT, bean.getAnnouncement());
		map.put(ServerMapConstants.BEAN_ANNOUNCEMENT_CODE, bean.getAnnouncement_code());
		map.put(ServerMapConstants.BEAN_AUK_NO, bean.getAuk_no());
		map.put(ServerMapConstants.BEAN_AUT_VALUE, bean.getAut_value());
		map.put(ServerMapConstants.BEAN_DATE, bean.getDate());
		map.put(ServerMapConstants.BEAN_FILENAME, bean.getFileName());
		map.put(ServerMapConstants.BEAN_HASH_VALUE, bean.getHash_value());
		map.put(ServerMapConstants.BEAN_HSK, bean.getHsk());
		map.put(ServerMapConstants.BEAN_KEK_NO, bean.getKek_no());
		map.put(ServerMapConstants.BEAN_KEY_CHANGE, bean.getKey_change());
		map.put(ServerMapConstants.BEAN_MESSAGE_LENGTH, bean.getMessage_length());
		map.put(ServerMapConstants.BEAN_MESSAGECODE, bean.getMessageCode());
		map.put(ServerMapConstants.BEAN_MSGTYPE, bean.getMsgType());
		map.put(ServerMapConstants.BEAN_NEW_KEY, bean.getNew_key());
		map.put(ServerMapConstants.BEAN_PROCEDURE, bean.getProcedure());
		map.put(ServerMapConstants.BEAN_PROTECTION_LEVEL, bean.getProtection_level());
		map.put(ServerMapConstants.BEAN_RECID, bean.getRecId());
		map.put(ServerMapConstants.BEAN_RECSPEC, bean.getRecSpec());
		map.put(ServerMapConstants.BEAN_RESERVED, bean.getReserved());
		map.put(ServerMapConstants.BEAN_SENDERID, bean.getSenderId());
		map.put(ServerMapConstants.BEAN_SENDERSPEC, bean.getSenderSpec());
		map.put(ServerMapConstants.BEAN_SOFTWARE, bean.getSoftware());
		map.put(ServerMapConstants.BEAN_STAMPNO, bean.getStampNo());
		map.put(ServerMapConstants.BEAN_TIME, bean.getTime());
		map.put(ServerMapConstants.BEAN_VERSION, bean.getVersion());
		map.put(ServerMapConstants.BEAN_TRFILESTATUS, bean.getTrFileStatus());
		
		logger.debug(":::: Inside getMessageMap :: Map " + map.size());
		
		return map;
	}
	
	public static MessageBean getMessageBean(Map map)
	{
		logger.debug(":::: Inside getMessageBean :: Bean.Size -> " + map.size());
		
		MessageBean bean = new MessageBean();
		
		 if(map.get(ServerMapConstants.BEAN_ACCEPTANCE_CODE) != null) {
			bean.setAcceptance_code(map.get(ServerMapConstants.BEAN_ACCEPTANCE_CODE).toString());
//			logger.debug(":::: getMessageBean :: BEAN_ACCEPTANCE_CODE " + map.get(ServerMapConstants.BEAN_ACCEPTANCE_CODE).toString());
		}
		 
		 if(map.get(ServerMapConstants.BEAN_ACKNOWLEDGEMENT) != null) { 
			bean.setAcknowledgement(map.get(ServerMapConstants.BEAN_ACKNOWLEDGEMENT).toString());
//			logger.debug(":::: getMessageBean :: BEAN_ACKNOWLEDGEMENT " + map.get(ServerMapConstants.BEAN_ACKNOWLEDGEMENT).toString());
		}
		
		if(map.get(ServerMapConstants.BEAN_ANNOUNCEMENT) != null) { 
			bean.setAnnouncement(map.get(ServerMapConstants.BEAN_ANNOUNCEMENT).toString());
//			logger.debug(":::: getMessageBean :: BEAN_ANNOUNCEMENT " + map.get(ServerMapConstants.BEAN_ANNOUNCEMENT).toString());
		}
		
		if(map.get(ServerMapConstants.BEAN_ANNOUNCEMENT_CODE) != null) {
			bean.setAnnouncement_code(map.get(ServerMapConstants.BEAN_ANNOUNCEMENT_CODE).toString());
//			logger.debug(":::: getMessageBean :: BEAN_ANNOUNCEMENT_CODE " + map.get(ServerMapConstants.BEAN_ANNOUNCEMENT_CODE).toString());
		}
		
		if(map.get(ServerMapConstants.BEAN_AUK_NO) != null) {
			bean.setAuk_no(map.get(ServerMapConstants.BEAN_AUK_NO).toString());
//			logger.debug(":::: getMessageBean :: BEAN_AUK_NO " + map.get(ServerMapConstants.BEAN_AUK_NO).toString());
		}
		
		if(map.get(ServerMapConstants.BEAN_AUT_VALUE) != null) {
			bean.setAut_value(map.get(ServerMapConstants.BEAN_AUT_VALUE).toString());
//			logger.debug(":::: getMessageBean :: BEAN_AUT_VALUE " + map.get(ServerMapConstants.BEAN_AUT_VALUE).toString());
		}
		
		if(map.get(ServerMapConstants.BEAN_DATE) != null) 
		bean.setDate(map.get(ServerMapConstants.BEAN_DATE).toString());
		
		if(map.get(ServerMapConstants.BEAN_FILENAME) != null) 
		bean.setFileName(map.get(ServerMapConstants.BEAN_FILENAME).toString());
		
		if(map.get(ServerMapConstants.BEAN_HASH_VALUE) != null) 
		bean.setHash_value(map.get(ServerMapConstants.BEAN_HASH_VALUE).toString());
		
		if(map.get(ServerMapConstants.BEAN_HSK) != null) 
		bean.setHsk(map.get(ServerMapConstants.BEAN_HSK).toString());
		
		if(map.get(ServerMapConstants.BEAN_KEK_NO) != null) { 
			bean.setKek_no(map.get(ServerMapConstants.BEAN_KEK_NO).toString());
//			logger.debug(":::: getMessageBean :: BEAN_KEK_NO " + map.get(ServerMapConstants.BEAN_KEK_NO).toString());
		}
		
		if(map.get(ServerMapConstants.BEAN_KEY_CHANGE) != null) 
		bean.setKey_change(map.get(ServerMapConstants.BEAN_KEY_CHANGE).toString());
		
		if(map.get(ServerMapConstants.BEAN_MESSAGE_LENGTH) != null) 
		bean.setMessage_length(map.get(ServerMapConstants.BEAN_MESSAGE_LENGTH).toString());
		
		if(map.get(ServerMapConstants.BEAN_MESSAGECODE) != null) 
		bean.setMessageCode(map.get(ServerMapConstants.BEAN_MESSAGECODE).toString());
		
		if(map.get(ServerMapConstants.BEAN_MSGTYPE) != null) 
		bean.setMsgType(map.get(ServerMapConstants.BEAN_MSGTYPE).toString());
		
		if(map.get(ServerMapConstants.BEAN_NEW_KEY) != null) 
		bean.setNew_key(map.get(ServerMapConstants.BEAN_NEW_KEY).toString());
		
		if(map.get(ServerMapConstants.BEAN_PROCEDURE) != null) 
		bean.setProcedure(map.get(ServerMapConstants.BEAN_PROCEDURE).toString());
		
		if(map.get(ServerMapConstants.BEAN_PROTECTION_LEVEL) != null) {
			bean.setProtection_level(map.get(ServerMapConstants.BEAN_PROTECTION_LEVEL).toString());
//			logger.debug(":::: getMessageBean :: BEAN_PROTECTION_LEVEL " + map.get(ServerMapConstants.BEAN_PROTECTION_LEVEL).toString());
		}	
		
		if(map.get(ServerMapConstants.BEAN_RECID) != null) 
		bean.setRecId(map.get(ServerMapConstants.BEAN_RECID).toString());
		
		if(map.get(ServerMapConstants.BEAN_RECSPEC) != null) 
		bean.setRecSpec(map.get(ServerMapConstants.BEAN_RECSPEC).toString());
		
		if(map.get(ServerMapConstants.BEAN_RESERVED) != null) 
		bean.setReserved(map.get(ServerMapConstants.BEAN_RESERVED).toString());
		
		if(map.get(ServerMapConstants.BEAN_SENDERID) != null) 
		bean.setSenderId(map.get(ServerMapConstants.BEAN_SENDERID).toString());
		
		if(map.get(ServerMapConstants.BEAN_SENDERSPEC) != null) 
		bean.setSenderSpec(map.get(ServerMapConstants.BEAN_SENDERSPEC).toString());
		
		if(map.get(ServerMapConstants.BEAN_SOFTWARE) != null) 
		bean.setSoftware(map.get(ServerMapConstants.BEAN_SOFTWARE).toString());
		
		if(map.get(ServerMapConstants.BEAN_STAMPNO) != null) 
		bean.setStampNo(map.get(ServerMapConstants.BEAN_STAMPNO).toString());
		
		if(map.get(ServerMapConstants.BEAN_TIME) != null) 
		bean.setTime(map.get(ServerMapConstants.BEAN_TIME).toString());
		
		if(map.get(ServerMapConstants.BEAN_VERSION) != null) 
		bean.setVersion(map.get(ServerMapConstants.BEAN_VERSION).toString());
		
		if(map.get(ServerMapConstants.BEAN_TRFILESTATUS) != null) {
			bean.setTrFileStatus(Boolean.getBoolean(map.get(ServerMapConstants.BEAN_TRFILESTATUS).toString()));
//			logger.debug(":::: getMessageBean :: BEAN_TRFILESTATUS " + map.get(ServerMapConstants.BEAN_TRFILESTATUS).toString());
		}		
		logger.debug(" :::: Bean has been SET :::: ");
		
		return bean;
	}
}