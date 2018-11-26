package com.ope.patu.messages;

import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

import javax.crypto.SecretKey;


import org.apache.log4j.Logger;

import com.coldcore.coloradoftp.session.Session;
import com.ope.patu.exception.DatabaseException;
import com.ope.patu.exception.FailedValidationException;
import com.ope.patu.exception.MessageParsingException;
import com.ope.patu.server.db.AuditLogger;
import com.ope.patu.handler.Parser;
import com.ope.patu.handler.Validator;
import com.ope.patu.parsers.VARMessageParser;
import com.ope.patu.server.beans.MessageBean;
import com.ope.patu.server.constant.SecurityMessageConstants;
import com.ope.patu.server.constant.ServerConstants;
import com.ope.patu.server.db.AbstractDAOFactory;
import com.ope.patu.server.db.ServerDAO;
import com.ope.patu.util.CommonUtil;
import com.ope.patu.util.DESUtil;
import com.ope.patu.util.DateUtil;
import com.ope.patu.util.FileUtil;

public class VARMessageValidator implements Validator
{
	private MessageBean msgBean;
	private String errorMessage;
	private String announcementCode;
	private String patuId;
	private Properties secureProp;
	private Map<String, String> keyMap = null;
	protected static Logger logger = Logger.getLogger( VARMessageValidator.class );
	Properties languageProp = new Properties();
	
	public VARMessageValidator()
	{
		super();
		secureProp = FileUtil.getSecurityProperties();
	}
	
	public Object getValidatedObject(Object... objects) 
	{
		return msgBean;
	}
	
	public boolean validate(Object... objects) throws FailedValidationException
	{
		boolean validationFlag = false;
		String varMsgContents = ( String )objects[0];
		String fileName = ( String )objects[1];
		Session session = ( Session )objects[2];
		String actualDataContents = ( String )objects[3];
		Parser parser = new VARMessageParser();
		try
		{
			msgBean = ( MessageBean )parser.parse( varMsgContents );
		}
		catch( MessageParsingException mpe )
		{
			logger.error(mpe);
			logger.error(mpe.getMessage());
			mpe.printStackTrace();
		}
		languageProp = ( Properties )session.getAttribute(ServerConstants.LANGUAGE_PROP);
		/*
		 * Store the original message in the session
		 */
		session.setAttribute(ServerConstants.VARA_MSG_BEAN, (MessageBean)msgBean.clone());
		String filePath = FileUtil.getSessionTempPath(session, fileName);
		AuditLogger.logSecurityMessage(session, fileName, filePath, SecurityMessageConstants.INCOMING,
				(MessageBean) msgBean.clone(),null);
		try
		{
			if( !checkMsgContentsLength(varMsgContents,msgBean.getMessage_length()) )
			{
				validationFlag = false;
				// msgBean.setAcceptance_code("E");
				msgBean.setAcceptance_code(SecurityMessageConstants.ACCEPTANCE_CODE_NO);
				msgBean.setAnnouncement_code(announcementCode);
				msgBean.setAnnouncement(DateUtil.getTime()+" "+errorMessage);
				msgBean.setSoftware(secureProp.getProperty("software"));
				logger.debug("in VAR Message Content length validation----------"+validationFlag);
				return validationFlag;
			}
			else if( !checkVersion(msgBean) )
			{
				validationFlag = false;
				// msgBean.setAcceptance_code("E");
				msgBean.setAcceptance_code(SecurityMessageConstants.ACCEPTANCE_CODE_NO);
				msgBean.setAnnouncement_code(announcementCode);
				msgBean.setAnnouncement(DateUtil.getTime()+" "+errorMessage);
				msgBean.setSoftware(secureProp.getProperty("software"));
				logger.debug("In VAR Version validation----------"+validationFlag);
				return validationFlag;
			}
			else if( !checkReceiversId(msgBean) )
			{
				validationFlag = false;
				// msgBean.setAcceptance_code("E");
				msgBean.setAcceptance_code(SecurityMessageConstants.ACCEPTANCE_CODE_NO);
				msgBean.setAnnouncement_code(announcementCode);
				msgBean.setAnnouncement(DateUtil.getTime()+" "+errorMessage);
				msgBean.setSoftware(secureProp.getProperty("software"));
				logger.debug("In VAR ReceiversId validation----------"+validationFlag);
				return validationFlag;
			}
			else if( !isGenerationNoValid(msgBean) )
			{
				validationFlag = false;
				// msgBean.setAcceptance_code("E");
				msgBean.setAcceptance_code(SecurityMessageConstants.ACCEPTANCE_CODE_NO);
				msgBean.setAnnouncement_code(announcementCode);
				msgBean.setAnnouncement(DateUtil.getTime()+" "+errorMessage);
				msgBean.setSoftware(secureProp.getProperty("software"));
				logger.debug("In VAR isGenerationNoValid validation----------"+validationFlag);
				return validationFlag;
			}
			else if( !validateDateTime(msgBean) )
			{
				validationFlag = false;
				// msgBean.setAcceptance_code("E");
				msgBean.setAcceptance_code(SecurityMessageConstants.ACCEPTANCE_CODE_NO);
				msgBean.setAnnouncement_code(announcementCode);
				msgBean.setAnnouncement(DateUtil.getTime()+" "+errorMessage);
				msgBean.setSoftware(secureProp.getProperty("software"));
				logger.debug("In VAR validateDateTime validation----------"+validationFlag);
				return validationFlag;
			}
			else if( !isHashValueSame(msgBean, actualDataContents,session) )
			{
				validationFlag = false;
				// msgBean.setAcceptance_code("E");
				msgBean.setAcceptance_code(SecurityMessageConstants.ACCEPTANCE_CODE_NO);
				msgBean.setAnnouncement_code(announcementCode);
				msgBean.setAnnouncement(DateUtil.getTime()+" "+errorMessage);
				msgBean.setSoftware(secureProp.getProperty("software"));
				logger.debug("In VAR isHashValueSame validation----------"+validationFlag);
				return validationFlag;
			}
			else if( !isAuthenticationTrue(msgBean,varMsgContents.substring(0,144)) )
			{
				validationFlag = false;
				// msgBean.setAcceptance_code("E");
				msgBean.setAcceptance_code(SecurityMessageConstants.ACCEPTANCE_CODE_NO);
				msgBean.setAnnouncement_code(announcementCode);
				msgBean.setAnnouncement(DateUtil.getTime()+" "+errorMessage);
				msgBean.setSoftware(secureProp.getProperty("software"));
				logger.debug("In VAR isAuthenticationTrue validation----------"+validationFlag);
				return validationFlag;
			}
			else if( !isTimeStampRepeated(msgBean) )
			{
				validationFlag = false;
				// msgBean.setAcceptance_code("E");
				msgBean.setAcceptance_code(SecurityMessageConstants.ACCEPTANCE_CODE_NO);
				msgBean.setAnnouncement_code(announcementCode);
				msgBean.setAnnouncement(DateUtil.getTime()+" "+errorMessage);
				msgBean.setSoftware(secureProp.getProperty("software"));
				logger.debug("In VAR isTimeStampRepeated validation----------"+validationFlag);
				return validationFlag;
			}
			else
			{
				validationFlag = true;
				// msgBean.setAcceptance_code("K");
//				msgBean.setAnnouncement_code("1001");
				msgBean.setAcceptance_code(SecurityMessageConstants.ACCEPTANCE_CODE_YES);
				msgBean.setAnnouncement_code(SecurityMessageConstants.MESSAGE_VALIDATION_PASS_CODE);
				
				session.setAttribute(ServerConstants.VAR_MSG_STATUS, validationFlag);
				session.setAttribute(ServerConstants.VARP_MSG_BEAN, msgBean);
				logger.debug("In VAR ALL validation----------"+validationFlag);
				return validationFlag;
			}
		}
		catch( Exception e )
		{
			logger.error(e);
			throw new FailedValidationException();
		}
	}
	
	//Validation for the message length
	private boolean checkMsgContentsLength( String varMsgContents , String msgLength )
	{
		boolean flag = false;
//		boolean flag = varMsgContents.length() == 161 ? true : false;
//		if( flag == false )
		if( varMsgContents.length() == Integer.parseInt(msgLength) )
		{
			flag = true;
		}
		else
		{
			flag = false;
//			errorMessage = languageProp.getProperty("1034");
//			announcementCode = "1034";
			announcementCode = SecurityMessageConstants.CONTENTS_LENGTH_ERROR_CODE;
			errorMessage = languageProp.getProperty(SecurityMessageConstants.CONTENTS_LENGTH_ERROR_CODE);
		}
		System.out.println("VAR message content validation-------->>>"+flag);
		logger.debug("VAR message content validation-------->>>"+flag);
		return flag;
	}
	//Validation for the Receiver's ID which is a tax no of the bank
	private boolean checkReceiversId( MessageBean msgBean )
	{
		boolean flag = false;
		if( msgBean.getRecId().startsWith( secureProp.getProperty("bank_taxno")) )
			flag = true;
		else
		{
//			errorMessage = languageProp.getProperty("1021");
//			announcementCode = "1021";
			errorMessage = languageProp.getProperty(SecurityMessageConstants.INVALID_RECEIVER_ID_ERROR_CODE);
			announcementCode = SecurityMessageConstants.INVALID_RECEIVER_ID_ERROR_CODE;
			flag = false;
		}
		return flag;
	}
	//Validation for the version
	private boolean checkVersion( MessageBean msgBean )
	{
		boolean flag = false;
		Properties secureProp = FileUtil.getSecurityProperties();
		try
		{
			int versionValue = Integer.parseInt( msgBean.getVersion() );
			int versionMinValue = Integer.parseInt( secureProp.getProperty("min_version_value") );
			int versionMaxValue = Integer.parseInt( secureProp.getProperty("max_version_value") );
			
			logger.debug("In VAR Msg Validation - versionMinValue->"+versionMinValue+" and versionMaxValue->"+versionMaxValue);
			
			if( versionValue >= versionMinValue && versionValue <= versionMaxValue )
				flag = true;
			else
			{
				flag = false;
//				errorMessage = "OLD VERSION";
//				announcementCode = "1012";
//				errorMessage = languageProp.getProperty("1012");
				announcementCode = SecurityMessageConstants.VERSION_ERROR_CODE;
				errorMessage = languageProp.getProperty(SecurityMessageConstants.VERSION_ERROR_CODE);
			}
		}
		catch( NumberFormatException nfe )
		{
			if (msgBean.getVersion().equals(
					secureProp.getProperty("security_version")))
				flag = true;
			else
			{
				flag = false;
//				errorMessage = "OLD VERSION";
//				announcementCode = "1012";
//				errorMessage = languageProp.getProperty("1012");
				announcementCode = SecurityMessageConstants.VERSION_ERROR_CODE;
				errorMessage = languageProp.getProperty(SecurityMessageConstants.VERSION_ERROR_CODE);
			}
		}
		return flag;
	}
	
	public boolean validateDateTime( MessageBean msgBean )
	{
		boolean flag = false;
		try
		{
			ServerDAO serverDao = AbstractDAOFactory.getDefaultDAOFactory()
			.getServerDAO();
			String timeStampString = new StringBuilder(msgBean.getDate())
			.append(msgBean.getTime()).toString();
			java.sql.Timestamp timeStamp = DateUtil.getTimeStamp(
					timeStampString, ServerConstants.DATE_FORMAT);
			Map<String, String> dataMap = (Map) serverDao.getESIAgmtDetails(
					msgBean.getSenderId(), timeStamp );
			if( dataMap.get("errorcode") == null && dataMap.get("errormsg") == null )
			{
				//Get the patu id and the date
				patuId = dataMap.get("patuid");
				String status = dataMap.get("status");
				/*status can be one of the followings.
				 * TOO EARLY
				 * TOO OLD
				 * ACCEPTED
				 */
				
				// EARLY_DATE_ERROR_CODE
				
				if( status.equals("TOO EARLY"))
				{
//					announcementCode = "1016";
//					errorMessage = languageProp.getProperty("1016");
					announcementCode = SecurityMessageConstants.EARLY_DATE_ERROR_CODE;
					errorMessage = languageProp.getProperty(SecurityMessageConstants.EARLY_DATE_ERROR_CODE);
					flag = false;
				}
				else if( status.equals("TOO OLD") )
				{
//					announcementCode = "1015";
//					errorMessage = languageProp.getProperty("1015");
					announcementCode = SecurityMessageConstants.OLD_DATE_ERROR_CODE;
					errorMessage = languageProp.getProperty(SecurityMessageConstants.OLD_DATE_ERROR_CODE);
					flag = false;
				}
				else
					flag = true;
			}
			else
			{
				flag = false;
//				announcementCode = "1033";
//				errorMessage = languageProp.getProperty("1033");
				announcementCode = SecurityMessageConstants.INVALID_CUSTOMERID_ERROR_CODE;
				errorMessage = languageProp.getProperty(SecurityMessageConstants.INVALID_CUSTOMERID_ERROR_CODE);
			}
		}
		catch( DatabaseException de )
		{
			flag = false;
			de.printStackTrace();
			logger.error(de);
		}
		catch( Exception e )
		{
			flag = false;
			e.printStackTrace();
			logger.error(e);
		}
		return flag;
	}
	
	private boolean isHashValueSame(MessageBean msgBean , String actualDataContents , Session session)
	{
		boolean flag = false;
		Scanner scan = new Scanner(actualDataContents);
		StringBuffer sb = new StringBuffer();
		while( scan.hasNext() )
		{
			sb.append(scan.nextLine().trim());
		}
		String strForHashValue = sb.toString();
		String KEK = ((Map<String, String>) session
				.getAttribute(ServerConstants.KEY_MAP))
				.get(ServerConstants.KEK);
		flag = CommonHandler.isHashValueSame(msgBean.getHash_value(), msgBean
				.getHsk(), strForHashValue, KEK);
		if( flag == false)
		{
//			announcementCode = "1019";
//			errorMessage = languageProp.getProperty("1019");
			announcementCode = SecurityMessageConstants.INVALID_HASH_VALUE_ERROR_CODE;
			errorMessage = languageProp.getProperty(SecurityMessageConstants.INVALID_HASH_VALUE_ERROR_CODE);
		}
		return flag;
	}

	
	private boolean isAuthenticationTrue( MessageBean msgBean , String strToAuth )
	{
		boolean flag = false;
		String aukString = keyMap.get(ServerConstants.AUK);
		flag = CommonHandler.isAuthenticationTrue(msgBean.getAut_value(), strToAuth, aukString);
		if( flag )
		{
			//do nothing
		}
		else
		{
//			announcementCode = "1020";
//			errorMessage = languageProp.getProperty("1020");
			announcementCode = SecurityMessageConstants.INVALID_AUTHENTICATION_ERROR_CODE;
			errorMessage = languageProp.getProperty(SecurityMessageConstants.INVALID_AUTHENTICATION_ERROR_CODE);
		}
		return flag;
	}
	
	private boolean isGenerationNoValid( MessageBean msgBean )
	{
		boolean flag = false;
		/*
		 * Make a database call to validate
		 * the generation number for the
		 * KEK and AUK.
		 * 
		 * Input will be KEKNo,AUKNo and PAtuId
		 */
		String KEKNo = msgBean.getKek_no();
		String AUKNo = msgBean.getAuk_no();
		String patuId = msgBean.getSenderId();//PATU id 
		try
		{
			ServerDAO serverDao = AbstractDAOFactory.getDefaultDAOFactory()
			.getServerDAO();
			keyMap = ( Map<String, String> )serverDao.getKeyGeneration(patuId,KEKNo,AUKNo);
			String newAukKey = keyMap.get(ServerConstants.NEW_AUK );
			if( keyMap.get(ServerConstants.ERROR_CODE) == null )
			{
				flag = true;
				String kekString = keyMap.get(ServerConstants.KEK ); 
				String newAukGenNo = keyMap.get(ServerConstants.NEW_AUK_GEN_NO);
				newAukGenNo = (newAukGenNo == null) ? msgBean.getKey_change() : newAukGenNo;
				System.out.println("New AUK generation no---------"+newAukGenNo);
				msgBean.setKey_change(newAukGenNo);
				if( newAukKey == null )
				{
					newAukKey = CommonUtil.pad(" ", 16, " ");
				}
				else
				{
					/*
					 * New AUK in the hex encoded form
					 */
					byte[] newAukBytes = CommonUtil.hexToBytes(newAukKey);
					/*
					 * Now encrypt the AUK using KEK and convert into hexadecimal
					 */
					byte[] kekBytes = CommonUtil.hexToBytes(kekString);
					SecretKey secretKey = DESUtil.getsecretKey(kekBytes);
					newAukKey = CommonUtil.bytesToHex(DESUtil
							.getEncryptedBytesWithIV(newAukBytes, secretKey));
				}
				msgBean.setNew_key(newAukKey);
			}
			else
			{
				flag = false;
//				announcementCode = "1013";
//				errorMessage = languageProp.getProperty("1013");
				errorMessage = languageProp.getProperty(SecurityMessageConstants.INVALID_KEK_ERROR_CODE);
				announcementCode = SecurityMessageConstants.INVALID_KEK_ERROR_CODE;
			}
		}
		catch( NullPointerException npe )
		{
			npe.printStackTrace();
			logger.error(npe);
		}
		catch( DatabaseException de )
		{
			de.printStackTrace();
			logger.error(de);
		}
		
		return flag;
	}
	
	private boolean isTimeStampRepeated( MessageBean msgBean )
	{
		boolean flag = false;
		/*
		 * Make a database call to validate the repeated time stamp.
		 * 
		 * Input will be date, time and stamp number.
		 */
		try
		{
			String inTimeStamp = new StringBuilder(msgBean.getDate()).append(
					msgBean.getTime()).append(msgBean.getStampNo()).toString();
			System.out.println("TimeStamp in ESI message------"+inTimeStamp);
			ServerDAO serverDao = AbstractDAOFactory.getDefaultDAOFactory()
			.getServerDAO();
			flag = ((Boolean) serverDao.isTimeStampUnique(inTimeStamp))
					.booleanValue();
			if( !flag )
			{
//				announcementCode = "1022";
//				errorMessage = languageProp.getProperty("1022");
				announcementCode = SecurityMessageConstants.INVALID_TIME_STAMP_ERROR_CODE;
				errorMessage = languageProp.getProperty(SecurityMessageConstants.INVALID_TIME_STAMP_ERROR_CODE);
			}
		}
		catch( DatabaseException de )
		{
			de.printStackTrace();
			logger.error(de);
			flag = false;
		}
		catch( Exception e )
		{
			e.printStackTrace();
			logger.error(e);
			flag = false;
		}
		return flag;
	}



}
