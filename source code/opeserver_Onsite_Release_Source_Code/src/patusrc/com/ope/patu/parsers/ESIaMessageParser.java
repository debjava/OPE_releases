package com.ope.patu.parsers;

import org.apache.log4j.Logger;

import com.ope.patu.exception.MessageParsingException;
import com.ope.patu.server.beans.MessageBean;

/**This class is used to parse the ESIa message.
 * @author Debadatta Mishra
 *
 */
public class ESIaMessageParser implements com.ope.patu.handler.Parser 
{
	protected static Logger logger = Logger.getLogger( ESIaMessageParser.class );

	public Object parse(Object... objects)
	{
		MessageBean msgBean = new MessageBean();
		try
		{
			String esiaMsgString = ( String )objects[0];
			String msgCode = esiaMsgString.substring(0,5);
			msgBean.setMessageCode(msgCode);

			String msgType = msgCode.substring(2,5);
			logger.debug("Msg Type===["+msgType+"]");
			msgBean.setMsgType(msgType);

			logger.debug("Msg Code===["+msgCode+"]");
			String msgLength = esiaMsgString.substring(5,8);
			msgBean.setMessage_length(msgLength);

			logger.debug("Msg Length===["+msgLength+"]");
			String version = esiaMsgString.substring(8,11);
			msgBean.setVersion(version);

			logger.debug("version===["+version+"]");
			String acceptanceCode = esiaMsgString.substring(11,12);
			msgBean.setAcceptance_code(acceptanceCode);

			logger.debug("acceptanceCode===["+acceptanceCode+"]");
			String announcementCode = esiaMsgString.substring(12,16);
			msgBean.setAnnouncement_code(announcementCode);

			logger.debug("announcementCode===["+announcementCode+"]");
			String software = esiaMsgString.substring(16,32);
			msgBean.setSoftware(software);
			logger.debug("software===["+software+"]");

			String procedure = esiaMsgString.substring(32,35);
			logger.debug("procedure===["+procedure+"]");
			msgBean.setProcedure(procedure);

			String recId = esiaMsgString.substring(35,52);
			logger.debug("recId===["+recId+"]");
			msgBean.setRecId(recId);

			String recSpec = esiaMsgString.substring(52,60);
			logger.debug("recSpec===["+recSpec+"]");
			msgBean.setRecSpec(recSpec);

			String senderId = esiaMsgString.substring(60,77);
			logger.debug("senderId===["+senderId+"]");
			msgBean.setSenderId(senderId);
			String senderSpec = esiaMsgString.substring(77,85);
			logger.debug("senderSpec===["+senderSpec+"]");
			msgBean.setSenderSpec(senderSpec);

			String kekNo = esiaMsgString.substring(85,86);
			logger.debug("kekNo===["+kekNo+"]");
			msgBean.setKek_no(kekNo);

			String aukNo = esiaMsgString.substring(86,87);
			logger.debug("aukNo===["+aukNo+"]");
			msgBean.setAuk_no(aukNo);

			String date = esiaMsgString.substring(87,93);
			logger.debug("date===["+date+"]");
			msgBean.setDate(date);

			String time = esiaMsgString.substring(93,99);
			logger.debug("time===["+time+"]");
			msgBean.setTime(time);

			String stampNo = esiaMsgString.substring(99,102);
			logger.debug("stampNo===["+stampNo+"]");
			msgBean.setStampNo(stampNo);

			String proetectionLevel = esiaMsgString.substring(102,103);
			logger.debug("proetectionLevel===["+proetectionLevel+"]");

			msgBean.setProtection_level(proetectionLevel);

			String reserved = esiaMsgString.substring(103,112);
			logger.debug("reserved===["+reserved+"]");
			msgBean.setReserved(reserved);

			String hsk = esiaMsgString.substring(112,128);
			logger.debug("hsk===["+hsk+"]");
			msgBean.setHsk(hsk);

			String hashValue = esiaMsgString.substring(128,144);
			logger.debug("hashValue===["+hashValue+"]");
			msgBean.setHash_value(hashValue);

			String authValue = esiaMsgString.substring(144,160);
			logger.debug("authValue===["+authValue+"]");
			msgBean.setAut_value(authValue);

			String keyChange = esiaMsgString.substring(160,161);
			System.out.println("key change value at parsing-------"+keyChange);
			logger.debug("keyChange===["+keyChange+"]");
			msgBean.setKey_change(keyChange);

//			String newKey = esiaMsgString.substring(161,177);
//			logger.debug("newKey===["+newKey+"]");
//			msgBean.setNew_key(newKey);

//			String announcement = esiaMsgString.substring(177,237);
//			logger.debug("announcement===["+announcement+"]");
//			msgBean.setAnnouncement(announcement);

//			String acknowledgement = esiaMsgString.substring(237,317);
//			logger.debug("acknowledgement===["+acknowledgement+"]");
//			msgBean.setAcknowledgement(acknowledgement);

		}
		catch( IndexOutOfBoundsException ibe )
		{
			ibe.printStackTrace();
			logger.error("Index out of bound exception thrown");
		}
		return msgBean;
	}
}
