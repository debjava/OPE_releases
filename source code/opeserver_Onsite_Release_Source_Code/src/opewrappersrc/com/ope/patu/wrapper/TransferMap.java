package com.ope.patu.wrapper;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import com.ope.patu.server.beans.TransferRequestBean;

public class TransferMap {

	protected static Logger logger = Logger.getLogger( TransferMap.class );
	
	@SuppressWarnings("unchecked")
	public static Map getTransferMap(TransferRequestBean bean)
	{
		logger.debug(":::: Inside getTransferMap :: Bean ");
		Map map = new HashMap();
		
		map.put(ServerMapConstants.TRBEAN_ACKNREQUEST, bean.getAcknRequest());
		map.put(ServerMapConstants.TRBEAN_CUSTOMERID, bean.getCustomerId());
		map.put(ServerMapConstants.TRBEAN_DATE, bean.getDate());
		map.put(ServerMapConstants.TRBEAN_ENDCARD, bean.getEndCard());
		map.put(ServerMapConstants.TRBEAN_FILETYPE, bean.getFileType());
		map.put(ServerMapConstants.TRBEAN_FORMAT, bean.getFormat());
		map.put(ServerMapConstants.TRBEAN_PASSWORD, bean.getPassword());
		map.put(ServerMapConstants.TRBEAN_RECORDID, bean.getRecordId());
		map.put(ServerMapConstants.TRBEAN_RECORDLENGTH, bean.getRecordLength());
		map.put(ServerMapConstants.TRBEAN_REDO, bean.getRedo());
		map.put(ServerMapConstants.TRBEAN_RESERVED, bean.getReserved());
		map.put(ServerMapConstants.TRBEAN_RESERVED1, bean.getReserved1());
		map.put(ServerMapConstants.TRBEAN_SERVICEID, bean.getServiceId());
				
		logger.debug(":::: Inside getTransferMap :: Map.size() -> " + map.size());
		
		return map;
	}
	
	public static TransferRequestBean getTransferBean(Map map)
	{
		TransferRequestBean bean = new TransferRequestBean();
		
		 if(map.get(ServerMapConstants.TRBEAN_ACKNREQUEST) != null) {
			bean.setAcknRequest(map.get(ServerMapConstants.TRBEAN_ACKNREQUEST).toString());
//			logger.debug(":::: getTransferBean :: TRBEAN_ACKNREQUEST " + map.get(ServerMapConstants.TRBEAN_ACKNREQUEST).toString());
		}
		 
		 if(map.get(ServerMapConstants.TRBEAN_CUSTOMERID) != null) { 
			bean.setCustomerId(map.get(ServerMapConstants.TRBEAN_CUSTOMERID).toString());
//			logger.debug(":::: getTransferBean :: TRBEAN_CUSTOMERID " + map.get(ServerMapConstants.TRBEAN_CUSTOMERID).toString());
		}
		
		if(map.get(ServerMapConstants.TRBEAN_DATE) != null) { 
			bean.setDate(map.get(ServerMapConstants.TRBEAN_DATE).toString());
//			logger.debug(":::: getTransferBean :: TRBEAN_DATE " + map.get(ServerMapConstants.TRBEAN_DATE).toString());
		}
		
		if(map.get(ServerMapConstants.TRBEAN_ENDCARD) != null) {
			bean.setEndCard(map.get(ServerMapConstants.TRBEAN_ENDCARD).toString());
//			logger.debug(":::: getTransferBean :: TRBEAN_ENDCARD " + map.get(ServerMapConstants.TRBEAN_ENDCARD).toString());
		}
		
		if(map.get(ServerMapConstants.TRBEAN_FILETYPE) != null) {
			bean.setFileType(map.get(ServerMapConstants.TRBEAN_FILETYPE).toString());
//			logger.debug(":::: getTransferBean :: TRBEAN_FILETYPE " + map.get(ServerMapConstants.TRBEAN_FILETYPE).toString());
		}
		
		if(map.get(ServerMapConstants.TRBEAN_FORMAT) != null) {
			bean.setFormat(map.get(ServerMapConstants.TRBEAN_FORMAT).toString());
//			logger.debug(":::: getTransferBean :: TRBEAN_FORMAT " + map.get(ServerMapConstants.TRBEAN_FORMAT).toString());
		}
		
		if(map.get(ServerMapConstants.TRBEAN_PASSWORD) != null) 
		bean.setPassword(map.get(ServerMapConstants.TRBEAN_PASSWORD).toString());
		
		if(map.get(ServerMapConstants.TRBEAN_RECORDID) != null) 
		bean.setRecordId(map.get(ServerMapConstants.TRBEAN_RECORDID).toString());
		
		if(map.get(ServerMapConstants.TRBEAN_RECORDLENGTH) != null) 
		bean.setRecordLength(map.get(ServerMapConstants.TRBEAN_RECORDLENGTH).toString());
		
		if(map.get(ServerMapConstants.TRBEAN_REDO) != null) 
		bean.setRedo(map.get(ServerMapConstants.TRBEAN_REDO).toString());
		
		if(map.get(ServerMapConstants.TRBEAN_RESERVED) != null) { 
			bean.setReserved(map.get(ServerMapConstants.TRBEAN_RESERVED).toString());
//			logger.debug(":::: getTransferBean :: TRBEAN_RESERVED " + map.get(ServerMapConstants.TRBEAN_RESERVED).toString());
		}
		
		if(map.get(ServerMapConstants.TRBEAN_RESERVED1) != null) 
		bean.setReserved1(map.get(ServerMapConstants.TRBEAN_RESERVED1).toString());
		
		if(map.get(ServerMapConstants.TRBEAN_SERVICEID) != null) 
		bean.setServiceId(map.get(ServerMapConstants.TRBEAN_SERVICEID).toString());
				
		logger.debug(" :::: getTransferBean :: Bean has been SET :::: ");
		
		return bean;
	}
}