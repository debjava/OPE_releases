package com.ope.patu.parsers;

import org.apache.log4j.Logger;
import com.ope.patu.server.beans.TransferRequestBean;

public class TRMessageParser implements com.ope.patu.handler.Parser
{
	protected static Logger logger = Logger.getLogger(TRMessageParser.class);
	
	public Object parse(Object... objects) 
	{
		String esia = ( String )objects[0];
		TransferRequestBean trBean = new TransferRequestBean();
		
		try
		{
			String recordId = esia.substring(0,17);
//			logger.debug("Record Id---->"+recordId);
			trBean.setRecordId(recordId.trim());
			
			String customerId = esia.substring(17,27);
//			logger.debug("customerId---->"+customerId);
			trBean.setCustomerId(customerId);
			
			String fileType = esia.substring(27,37);
//			logger.debug("fileType---->"+fileType);
			trBean.setFileType(fileType.trim());
			String serviceId = esia.substring(37,55);
			logger.debug("In TRMessageParser : Service Id----->"+serviceId);
			trBean.setServiceId(serviceId.trim());
			
			String password = esia.substring(55,65);
//			logger.debug("Password------->"+password);
			trBean.setPassword(password);
			
			String date = esia.substring(65,71);
//			logger.debug("Date----------->"+date);
			trBean.setDate(date);
			
			String reserved = esia.substring(71,72);
//			logger.debug("Reserved------->"+reserved);
			trBean.setReserved(reserved);
			
			String endCard = esia.substring(72,73);
//			logger.debug("End Card------->"+endCard);
			trBean.setEndCard(endCard);
			
			String redo = esia.substring(73,74);
//			logger.debug("Redo----------->"+redo);
			trBean.setRedo(redo);
			String acknRequest = esia.substring(74,75);
//			logger.debug("Acknowledgement Request------->"+acknRequest);
			trBean.setAcknRequest(acknRequest);
			
			String format = esia.substring(75,76);
//			logger.debug("Format-------->"+format);
			trBean.setFormat(format);
			
			String reserved1 = esia.substring(76,77);
//			logger.debug("Reserved1------>"+reserved1);
			trBean.setReserved1(reserved1);
			
			String recordLength = esia.substring(77,80);
//			logger.debug("Record Length---->"+recordLength);
			trBean.setRecordLength(recordLength);
		}
		catch( IndexOutOfBoundsException ibe )
		{
			ibe.printStackTrace();
			logger.error(ibe);
		}
		return trBean;
	}
}
