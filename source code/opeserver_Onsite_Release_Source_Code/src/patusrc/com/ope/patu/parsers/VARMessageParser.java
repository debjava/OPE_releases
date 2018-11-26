package com.ope.patu.parsers;

import org.apache.log4j.Logger;

import com.ope.patu.handler.Parser;
import com.ope.patu.server.beans.MessageBean;

public class VARMessageParser implements Parser 
{
	protected static Logger logger = Logger.getLogger( VARMessageParser.class );

	public Object parse(Object... objects) 
	{
		MessageBean msgBean = new MessageBean();
		try
		{
			String varMsgString = ( String )objects[0];
			if( varMsgString.length() == 0 )
			{
				msgBean.setAnnouncement_code("R024");
				msgBean.setAnnouncement("MISSING VAR MESSAGE");
			}
			String msgCode = varMsgString.substring(0,5);
			msgBean.setMessageCode(msgCode);

			String msgType = msgCode.substring(2,5);
			logger.debug("Msg Type===["+msgType+"]");
			msgBean.setMsgType(msgType);

			logger.debug("Msg Code===["+msgCode+"]");
			String msgLength = varMsgString.substring(5,8);
			msgBean.setMessage_length(msgLength);

			logger.debug("Msg Length===["+msgLength+"]");
			String version = varMsgString.substring(8,11);
			msgBean.setVersion(version);

			logger.debug("version===["+version+"]");
			String acceptanceCode = varMsgString.substring(11,12);
			msgBean.setAcceptance_code(acceptanceCode);

			logger.debug("acceptanceCode===["+acceptanceCode+"]");
			String announcementCode = varMsgString.substring(12,16);
			msgBean.setAnnouncement_code(announcementCode);

			logger.debug("announcementCode===["+announcementCode+"]");
			String software = varMsgString.substring(16,32);
			msgBean.setSoftware(software);
			logger.debug("software===["+software+"]");

			String procedure = varMsgString.substring(32,35);
			logger.debug("procedure===["+procedure+"]");
			msgBean.setProcedure(procedure);

			String recId = varMsgString.substring(35,52);
			logger.debug("recId===["+recId+"]");
			msgBean.setRecId(recId);

			String recSpec = varMsgString.substring(52,60);
			logger.debug("recSpec===["+recSpec+"]");
			msgBean.setRecSpec(recSpec);

			String senderId = varMsgString.substring(60,77);
			logger.debug("senderId===["+senderId+"]");
			msgBean.setSenderId(senderId);
			String senderSpec = varMsgString.substring(77,85);
			logger.debug("senderSpec===["+senderSpec+"]");
			msgBean.setSenderSpec(senderSpec);

			String kekNo = varMsgString.substring(85,86);
			logger.debug("kekNo===["+kekNo+"]");
			msgBean.setKek_no(kekNo);

			String aukNo = varMsgString.substring(86,87);
			logger.debug("aukNo===["+aukNo+"]");
			msgBean.setAuk_no(aukNo);

			String date = varMsgString.substring(87,93);
			logger.debug("date===["+date+"]");
			msgBean.setDate(date);

			String time = varMsgString.substring(93,99);
			logger.debug("time===["+time+"]");
			msgBean.setTime(time);

			String stampNo = varMsgString.substring(99,102);
			logger.debug("stampNo===["+stampNo+"]");
			msgBean.setStampNo(stampNo);

			String proetectionLevel = varMsgString.substring(102,103);
			logger.debug("proetectionLevel===["+proetectionLevel+"]");

			msgBean.setProtection_level(proetectionLevel);

			String reserved = varMsgString.substring(103,112);
			logger.debug("reserved===["+reserved+"]");
			msgBean.setReserved(reserved);

			String hsk = varMsgString.substring(112,128);
			logger.debug("hsk===["+hsk+"]");
			msgBean.setHsk(hsk);

			String hashValue = varMsgString.substring(128,144);
			logger.debug("hashValue===["+hashValue+"]");
			msgBean.setHash_value(hashValue);

			String authValue = varMsgString.substring(144,160);
			logger.debug("authValue===["+authValue+"]");
			msgBean.setAut_value(authValue);

			String keyChange = varMsgString.substring(160,161);
			logger.debug("keyChange===["+keyChange+"]");
			msgBean.setKey_change(keyChange);
		}
		catch( IndexOutOfBoundsException e  )
		{
			e.printStackTrace();
			logger.error(e);
		}
		return msgBean;
	}
}
