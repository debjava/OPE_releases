package com.ope.patu.messages;

import java.util.Map;
import org.apache.log4j.Logger;
import com.coldcore.coloradoftp.session.Session;
import com.ope.patu.exception.FailedValidationException;
import com.ope.patu.exception.MessageParsingException;
import com.ope.patu.server.db.AuditLogger;
import com.ope.patu.handler.MessageHandler;
import com.ope.patu.handler.Parser;
import com.ope.patu.handler.Validator;
import com.ope.patu.handler.ValidatorFactory;
import com.ope.patu.parsers.ESIaMessageParser;
import com.ope.patu.server.beans.MessageBean;
import com.ope.patu.server.constant.SecurityMessageConstants;
import com.ope.patu.server.constant.ServerConstants;
import com.ope.patu.util.CommonUtil;
import com.ope.patu.util.FileUtil;

public class ESIMessageHandler implements MessageHandler 
{
	protected static Logger logger = Logger.getLogger(ESIMessageHandler.class);

	public Object handleObject( Object ... obj ) 
	{
		String fileName = ( String )obj[0];
		Session session = ( Session )obj[1];
		String msgTypeName = ( String )obj[2];
		boolean validationFlag = false;
		/*
		 * Read the message as String
		 */
		try
		{
			String filePath = FileUtil.getSessionTempPath(session, fileName);
			if( filePath == null ) throw new NullPointerException();
			logger.debug("ESIMessageHandler : filePath---->"+filePath);
			String fileContents = FileUtil.getFileContetns(filePath);
			if( fileContents.length() == 161 )
			{
				//Validate the file
				Validator validator = ValidatorFactory.getValidator(msgTypeName);
				validationFlag = validator.validate(fileContents,fileName,session);
				MessageBean msgBean = ( MessageBean ) validator.getValidatedObject();
				session.setAttribute(ServerConstants.ESI_MSG_STATUS,
						new Boolean(validationFlag));
				session.setAttribute(ServerConstants.ESIP_MSG_BEAN, msgBean );
				session.setAttribute(ServerConstants.PATUID, msgBean.getSenderId() );
				/*
				 * Gather information to store the audit log for ESIa
				 */
				MessageBean msgBeanESIa = (MessageBean) session
						.getAttribute(ServerConstants.ESIA_MSG_BEAN);
				String msgStatus = ((Boolean) session
						.getAttribute(ServerConstants.ESI_MSG_STATUS)).toString();
				AuditLogger.logSecurityMessage(session, fileName, filePath, SecurityMessageConstants.INCOMING,
						msgBeanESIa, msgStatus);
				String esiPMsgString = getESIpMsgString(msgBean,session);
				logger.debug("Total ESIp message----->>>"+esiPMsgString);
				filePath = FileUtil.getSessionTempPath(session, SecurityMessageConstants.ESIP_FILE);
				FileUtil.writeContents( filePath,esiPMsgString );
				session.setAttribute(ServerConstants.ESIP_PATH, filePath);
				session.setAttribute(ServerConstants.ESIP_MSG, esiPMsgString);
				AuditLogger.logSecurityMessage(session, null, null, SecurityMessageConstants.OUTGOING, msgBean,null);
				logger.info("ESI message validation flag------>>>"+validationFlag);
				logger.debug("ESI message validation flag------>>>"+validationFlag);
				/**anandkumar.b 01-Dec-2008
				 * Code added for disconnect the session when ESIp validation returns false 
				 */
				if(validationFlag==false){
				   String esiFlag = "Y";
				   session.setAttribute(ServerConstants.ESIP_FLAG, esiFlag);
				}else{
					String esiFlag = "N";
					session.setAttribute(ServerConstants.ESIP_FLAG, esiFlag);
				}
			}
			else
			{
				//Generate the ESIp message with error
				String errorMsg = SecurityMessageConstants.ERROR_MESSAGE;
				logger.debug("It is a system error");
				logger.fatal("FATAL ERROR, ESI message length is less than the specified length");
				session.setAttribute(ServerConstants.REPLYMSG, errorMsg);
			}
		}
		catch( NullPointerException npe )
		{
			logger.error(npe);
		}
		catch( FailedValidationException fve )
		{
			logger.error("Validation failed due to error");
			logger.error( fve );
		}
		catch( Exception e )
		{
			logger.error("Other exception thrown");
			logger.error(e);
			session.setAttribute(ServerConstants.REPLYMSG, SecurityMessageConstants.ERROR_REPLY_MSG);
		}
		return new Boolean( validationFlag ); 
	}
	
	public Object getMessageObject(Object... objects) 
	{
		String fileName = ( String )objects[0];
		Session session = ( Session )objects[1];
		/*
		 * Read the message as String
		 */
		String filePath = FileUtil.getFilePath(session, fileName);
		String fileContents = FileUtil.getFileContetns(filePath);
		Parser parser = new ESIaMessageParser();
		MessageBean msgBean = null;
		try
		{
			msgBean = ( MessageBean )parser.parse(fileContents);
		}
		catch (MessageParsingException mpe) 
		{
			logger.error(mpe);
			logger.error(mpe.getMessage());
		}
		catch( Exception e )
		{
			logger.error(e);
			logger.error(e.getMessage());
		}
		return msgBean;
	}

	private String getESIpMsgString(MessageBean msgBean, Session session)
	throws NullPointerException, Exception
	{
		String esiString = "";
		logger.debug("MessageBean softwate --------->>>"+msgBean.getSoftware());
		try
		{
			StringBuilder sb = new StringBuilder( esiString );
			esiString = sb.append(msgBean.getMessageCode()).
			append(SecurityMessageConstants.APPEND_LENGTH).//Length of the ESIp message
			append(msgBean.getVersion()).
			append( msgBean.getAcceptance_code()).
			append( msgBean.getAnnouncement_code()).
			//append(ServerConstants.BANK_SOFTWARE).//Bank's software
			append(msgBean.getSoftware()).//Bank's software
			append(msgBean.getProcedure()).
			append(msgBean.getSenderId()).
			append(msgBean.getRecSpec()).
			append(msgBean.getRecId()).
			append(msgBean.getSenderSpec()).
			append(msgBean.getKek_no()).
			append(msgBean.getAuk_no()).
			append(msgBean.getDate()).
			append(msgBean.getTime()).
			append(msgBean.getStampNo()).
			append(msgBean.getProtection_level()).
			append(msgBean.getReserved()).
			append(msgBean.getHsk()).
			append(msgBean.getHash_value()).toString();
			String strToAuth = esiString;
			logger.debug("String to Authenticate for ESIp--------"+strToAuth);
			logger.debug("Length of the String for AUth--------"+strToAuth.length());
			Map<String , String> keyMap = (Map<String,String>) session
			.getAttribute(ServerConstants.KEY_MAP);
			String aukString = null;
			String authValueStr = null;
			if( keyMap != null )
			{
				aukString = keyMap.get(ServerConstants.AUK);
				logger.debug("AUK STRING-----------"+aukString);
				authValueStr = CommonHandler.getComputedAuthValue(strToAuth,aukString);
			}
			else
			{
				aukString = CommonUtil.pad(" ", 16, " ");
				authValueStr = msgBean.getAut_value();
			}
			logger.debug("Auth value for ESIp------"+authValueStr);
			String newKey = msgBean.getNew_key() == null ? CommonUtil.pad(" ", 16,
			" ") : msgBean.getNew_key();
			StringBuilder sb1 = new StringBuilder( esiString );
			sb1.append(authValueStr).append(msgBean.getKey_change()).
			append(newKey);
			logger.info("msgBean.getNew_key()---------"+msgBean.getNew_key());
			String tempAnnouncement = msgBean.getAnnouncement() == null ? "" : msgBean.getAnnouncement();
			sb1.append( CommonUtil.pad(tempAnnouncement, 60, " "));
			esiString = sb1.toString();
		}
		catch( NullPointerException npe )
		{
			npe.printStackTrace();
			throw npe;
		}
		catch( Exception e )
		{
			e.printStackTrace();
			throw e;
		}
		return esiString;
	}
}
