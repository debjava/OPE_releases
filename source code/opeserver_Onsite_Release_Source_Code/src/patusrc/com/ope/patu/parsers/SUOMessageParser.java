package com.ope.patu.parsers;

import org.apache.log4j.Logger;
import com.ope.patu.server.beans.MessageBean;
import com.ope.patu.server.constant.SecurityMessageConstants;

/**This class is used to parse the SUO message.
 * @author Saurabh Thakkur
 *
 */
public class SUOMessageParser implements com.ope.patu.handler.Parser 
{
	protected static Logger logger = Logger.getLogger( SUOMessageParser.class );
	
	public Object parse(Object... objects) 
	{
		MessageBean msgBean = new MessageBean();
		String suoMsgString = ( String )objects[0];
		if( suoMsgString.length() == 0 )
		{
			msgBean.setAnnouncement_code(SecurityMessageConstants.SET_ANNOUNCEMENT_CODE);
			msgBean.setAnnouncement(SecurityMessageConstants.MISSING_SUO_MESSAGE);
		}
		String msgCode = suoMsgString.substring(0,5);
		msgBean.setMessageCode(msgCode);
		
		String msgType = msgCode.substring(2,5);
		logger.debug("Msg Type===["+msgType+"]");
		msgBean.setMsgType(msgType);
		
		logger.debug("Msg Code===["+msgCode+"]");
		String msgLength = suoMsgString.substring(5,8);
		msgBean.setMessage_length(msgLength);
		
		logger.debug("Msg Length===["+msgLength+"]");
		String version = suoMsgString.substring(8,11);
		msgBean.setVersion(version);
		
		logger.debug("version===["+version+"]");
		String acceptanceCode = suoMsgString.substring(11,12);
		msgBean.setAcceptance_code(acceptanceCode);
		
		logger.debug("acceptanceCode===["+acceptanceCode+"]");
		String announcementCode = suoMsgString.substring(12,16);
		msgBean.setAnnouncement_code(announcementCode);
		
		logger.debug("announcementCode===["+announcementCode+"]");
		String software = suoMsgString.substring(16,32);
		msgBean.setSoftware(software);
		logger.debug("software===["+software+"]");
		
		String procedure = suoMsgString.substring(32,35);
		logger.debug("procedure===["+procedure+"]");
		msgBean.setProcedure(procedure);
		
		String recId = suoMsgString.substring(35,52);
		logger.debug("recId===["+recId+"]");
		msgBean.setRecId(recId);
		
		String recSpec = suoMsgString.substring(52,60);
		logger.debug("recSpec===["+recSpec+"]");
		msgBean.setRecSpec(recSpec);
		
		String senderId = suoMsgString.substring(60,77);
		logger.debug("senderId===["+senderId+"]");
		msgBean.setSenderId(senderId);
		String senderSpec = suoMsgString.substring(77,85);
		logger.debug("senderSpec===["+senderSpec+"]");
		msgBean.setSenderSpec(senderSpec);
		
		String kekNo = suoMsgString.substring(85,86);
		logger.debug("kekNo===["+kekNo+"]");
		msgBean.setKek_no(kekNo);
		
		String aukNo = suoMsgString.substring(86,87);
		logger.debug("aukNo===["+aukNo+"]");
		msgBean.setAuk_no(aukNo);
		
		String date = suoMsgString.substring(87,93);
		logger.debug("date===["+date+"]");
		msgBean.setDate(date);
		
		String time = suoMsgString.substring(93,99);
		logger.debug("time===["+time+"]");
		msgBean.setTime(time);
		
		String stampNo = suoMsgString.substring(99,102);
		logger.debug("stampNo===["+stampNo+"]");
		msgBean.setStampNo(stampNo);
		
		String proetectionLevel = suoMsgString.substring(102,103);
		logger.debug("proetectionLevel===["+proetectionLevel+"]");
		
		msgBean.setProtection_level(proetectionLevel);
		
		String reserved = suoMsgString.substring(103,112);
		logger.debug("reserved===["+reserved+"]");
		msgBean.setReserved(reserved);
		
		String hsk = suoMsgString.substring(112,128);
		logger.debug("hsk===["+hsk+"]");
		msgBean.setHsk(hsk);

		return msgBean;
	}
}
