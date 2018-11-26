package com.ope.patu.messages;

import java.util.Map;
import java.util.Properties;
import javax.crypto.SecretKey;
import org.apache.log4j.Logger;
import com.coldcore.coloradoftp.session.Session;
import com.ope.patu.exception.DatabaseException;
import com.ope.patu.exception.FailedValidationException;
import com.ope.patu.exception.MessageParsingException;
import com.ope.patu.handler.Parser;
import com.ope.patu.handler.Validator;
import com.ope.patu.key.KeyGenerator;
import com.ope.patu.parsers.ESIaMessageParser;
import com.ope.patu.server.beans.MessageBean;
import com.ope.patu.server.constant.ServerConstants;
import com.ope.patu.server.constant.SecurityMessageConstants;
import com.ope.patu.server.db.AbstractDAOFactory;
import com.ope.patu.server.db.ServerDAO;
import com.ope.patu.util.CommonUtil;
import com.ope.patu.util.DESUtil;
import com.ope.patu.util.DateUtil;
import com.ope.patu.util.FileUtil;
import com.ope.patu.util.ParityUtil;

public class ESIMessageValidator implements Validator 
{
	private MessageBean msgBean;
	private String errorMessage;
	private String announcementCode;
	private String patuId;
	private Properties secureProp;
	private Map<String, String> keyMap = null;
	protected static Logger logger = Logger.getLogger( ESIMessageValidator.class );
	Properties languageProp = new Properties();

	public ESIMessageValidator()
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
		String esiMsgContents = ( String )objects[0];
		logger.debug("ESI message contents length"
				+ esiMsgContents.length());
		String fileName = ( String )objects[1];
		Session session = ( Session )objects[2];
		Parser parser = new ESIaMessageParser();
		try
		{
			msgBean = ( MessageBean )parser.parse( esiMsgContents );
		}
		catch( MessageParsingException mpe )
		{
			logger.error(mpe.getMessage());
		}
		session.setAttribute(ServerConstants.ESIA_MSG_BEAN,
				(MessageBean) msgBean.clone());
		patuId = msgBean.getSenderId();
		languageProp = FileUtil.getlanguage(getLanguage(patuId));
		session.setAttribute(ServerConstants.LANGUAGE_PROP, languageProp);
		try
		{
			if( !checkMsgContentsLength(esiMsgContents,msgBean.getMessage_length()) )
			{
				// Case 1 : Checking for Form and Authenticity
				validationFlag = false;
				msgBean.setAnnouncement_code(announcementCode);
				msgBean.setAnnouncement(DateUtil.getTime()+" "+errorMessage);
				// msgBean.setAcceptance_code("E"); // NO
				msgBean.setAcceptance_code(SecurityMessageConstants.ACCEPTANCE_CODE_NO);
				return validationFlag;
			}
			else if( !checkVersion(msgBean))
			{
				//Case 2 : Checking for the version
				validationFlag = false;
				msgBean.setAnnouncement_code(announcementCode);
				msgBean.setAnnouncement(DateUtil.getTime()+" "+errorMessage);
				// msgBean.setAcceptance_code("E");
				msgBean.setAcceptance_code(SecurityMessageConstants.ACCEPTANCE_CODE_NO);
				return validationFlag;
			}
			else if( !checkReceiversId(msgBean))
			{
				//Case 3 : Checking for the Receiver's ID code
				validationFlag = false;
				msgBean.setAnnouncement_code(announcementCode);
				msgBean.setAnnouncement(DateUtil.getTime()+" "+errorMessage);
				// msgBean.setAcceptance_code("E");
				msgBean.setAcceptance_code(SecurityMessageConstants.ACCEPTANCE_CODE_NO);
				return validationFlag;
			}
			else if( !checkCustomerId(patuId))
			{
				//Case 4 : Checking for the Customer's Id
				validationFlag = false;
				msgBean.setAnnouncement_code(announcementCode);
				msgBean.setAnnouncement(DateUtil.getTime()+" "+errorMessage);
				// msgBean.setAcceptance_code("E");
				msgBean.setAcceptance_code(SecurityMessageConstants.ACCEPTANCE_CODE_NO);
				return validationFlag;
			}
			else if( !validateDateTime(msgBean,session) )
			{
				//Case 5 : Checking for the 5 banking days
				validationFlag = false;
				msgBean.setAnnouncement_code(announcementCode);
				msgBean.setAnnouncement(DateUtil.getTime()+" "+errorMessage);
				// msgBean.setAcceptance_code("E");
				msgBean.setAcceptance_code(SecurityMessageConstants.ACCEPTANCE_CODE_NO);
				return validationFlag;
			}
			else if( !isTimeStampRepeated(msgBean) )
			{
				//Case 6 : Checking for uniqueness of time stamp
				validationFlag = false;
				msgBean.setAnnouncement_code(announcementCode);
				msgBean.setAnnouncement(DateUtil.getTime()+" "+errorMessage);
				// msgBean.setAcceptance_code("E");
				msgBean.setAcceptance_code(SecurityMessageConstants.ACCEPTANCE_CODE_NO);
				return validationFlag;
			}
			else if( !isGenerationNoValid(msgBean,session) )
			{
				//Case 7 : Checking for the Generation no
				validationFlag = false;
				msgBean.setAnnouncement_code(announcementCode);
				msgBean.setAnnouncement(DateUtil.getTime()+" "+errorMessage);
				// msgBean.setAcceptance_code("E");
				msgBean.setAcceptance_code(SecurityMessageConstants.ACCEPTANCE_CODE_NO);
				return validationFlag;
			}
			else if( !isAuthenticationTrue(msgBean,esiMsgContents.substring(0,144)) )
			{
				//Case 8 : Checking for the Authentication value
				validationFlag = false;
				msgBean.setAnnouncement_code(announcementCode);
				msgBean.setAnnouncement(DateUtil.getTime()+" "+errorMessage);
				// msgBean.setAcceptance_code("E");
				msgBean.setAcceptance_code(SecurityMessageConstants.ACCEPTANCE_CODE_NO);
				return validationFlag;
			}
			else if( !validateKeyChange(msgBean,session) )
			{
				//Case 9 : Extra validation for the key change for Baseware.
				validationFlag = false;
				msgBean.setAnnouncement_code(announcementCode);
				msgBean.setAnnouncement(DateUtil.getTime()+" "+errorMessage);
				// msgBean.setAcceptance_code("E");
				msgBean.setAcceptance_code(SecurityMessageConstants.ACCEPTANCE_CODE_NO);
				return validationFlag;
			}
			else
			{
				/*
				 * This is the best condition where everything is fine.
				 */
				logger.debug("ESI message validation passed");
				validationFlag = true;
				// msgBean.setAcceptance_code("K"); // YES
				msgBean.setAcceptance_code(SecurityMessageConstants.ACCEPTANCE_CODE_YES);

				//msgBean.setAnnouncement_code("1001");
				//msgBean.setAnnouncement(DateUtil.getTime()+" "+languageProp.getProperty("1001"));

				msgBean.setAnnouncement_code(SecurityMessageConstants.MESSAGE_VALIDATION_PASS_CODE);
				msgBean.setAnnouncement(DateUtil.getTime()+" "+languageProp.getProperty(SecurityMessageConstants.MESSAGE_VALIDATION_PASS_CODE));

				return validationFlag;
			}
		}
		catch( Exception fve )
		{
			logger.error(fve);
			throw new FailedValidationException();
		}
	}

	//Validation for the message length
	private boolean checkMsgContentsLength( String esiMsgContents , String msgLength )
	{
		boolean flag = false;
		logger.debug("ESI Message contents -->"+esiMsgContents);
//		if( esiMsgContents.length() == 161 )
		if( esiMsgContents.length() == Integer.parseInt(msgLength) )
		{
			flag = true;
		}
		else
		{
			flag = false;
//			announcementCode = "1034";
//			errorMessage = languageProp.getProperty("1034");

			announcementCode = SecurityMessageConstants.CONTENTS_LENGTH_ERROR_CODE;
			errorMessage = languageProp.getProperty(SecurityMessageConstants.CONTENTS_LENGTH_ERROR_CODE);

		}
		logger.debug("ESI message content validation --->"+flag);
		return flag;
	}
	//Validation for the Receiver's ID which is a tax no of the bank
	private boolean checkReceiversId( MessageBean msgBean )
	{
		boolean flag = false;
		logger.debug("Receiver's ID :::" + msgBean.getRecId()
				+ " Length---" + msgBean.getRecId().length());
		logger.debug("Bank's Tax No :::"+secureProp.getProperty(SecurityMessageConstants.BANK_TAXNO));
		if( msgBean.getRecId().startsWith( secureProp.getProperty(SecurityMessageConstants.BANK_TAXNO)) )
			flag = true;
		else
		{
//			announcementCode = "1021";
//			errorMessage = languageProp.getProperty("1021");
			announcementCode = SecurityMessageConstants.INVALID_RECEIVER_ID_ERROR_CODE;
			errorMessage = languageProp.getProperty(SecurityMessageConstants.INVALID_RECEIVER_ID_ERROR_CODE);

			flag = false;
			msgBean.setAnnouncement_code(announcementCode);
			msgBean.setAnnouncement(errorMessage);
			// msgBean.setAcceptance_code("E");
			msgBean.setAcceptance_code(SecurityMessageConstants.ACCEPTANCE_CODE_NO);
		}
		logger.debug("ESI message Receiver ID validation-------->"+flag);
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
			int versionMinValue = Integer.parseInt( secureProp.getProperty(SecurityMessageConstants.MIN_VERSION_VALUE) );
			int versionMaxValue = Integer.parseInt( secureProp.getProperty(SecurityMessageConstants.MAX_VERSION_VALUE) );
			if( versionValue >= versionMinValue && versionValue <= versionMaxValue )
				flag = true;
			else
			{
				flag = false;
//				announcementCode = "1012";
//				errorMessage = languageProp.getProperty("1012");

				announcementCode = SecurityMessageConstants.VERSION_ERROR_CODE;
				errorMessage = languageProp.getProperty(SecurityMessageConstants.VERSION_ERROR_CODE);
			}
		}
		catch( NumberFormatException nfe )
		{
			if (msgBean.getVersion().equals(
					secureProp.getProperty(SecurityMessageConstants.SECURITY_VERSION)))
				flag = true;
			else
			{
				flag = false;
//				announcementCode = "1012";
//				errorMessage = languageProp.getProperty("1012");

				announcementCode = SecurityMessageConstants.VERSION_ERROR_CODE;
				errorMessage = languageProp.getProperty(SecurityMessageConstants.VERSION_ERROR_CODE);
			}
		}
		logger.debug("ESI message checkVersion validation-->"+flag);
		return flag;
	}
	
	/*
	 * Overload the old validateDateTime function
	 * Validate the time stamp. Time stamp must be less than 5 banking days old
	 * Keep the Service Bureau Id in the session.
	 */
	public boolean validateDateTime( MessageBean msgBean, Session session )
	{
		boolean flag = false;
		try
		{
			//check for too early
			//check for too old

			String timeStampString = new StringBuilder(msgBean.getDate())
			.append(msgBean.getTime()).toString();
			
			java.sql.Timestamp timeStamp = DateUtil.getTimeStamp(
					timeStampString, ServerConstants.DATE_FORMAT);
			
			ServerDAO serverDao = AbstractDAOFactory.getDefaultDAOFactory()
			.getServerDAO();
			Map<String, String> dataMap = (Map<String,String>) serverDao.getESIAgmtDetails(
					msgBean.getSenderId(), timeStamp );
			if( dataMap.get(SecurityMessageConstants.ERRORCODE) == null && dataMap.get(SecurityMessageConstants.ERRORMSG) == null )
			{
				String status = dataMap.get(SecurityMessageConstants.STATUS);
				String serviceBureauId = dataMap.get(SecurityMessageConstants.SERVICEBUREAUID);
								
				/*Set the Service Bureau Id in the Session */
				session.setAttribute(ServerConstants.SERVICEBUREAUID, serviceBureauId);
				
				/*status can be one of the followings.
				 * TOO EARLY
				 * TOO OLD
				 * ACCEPTED
				 */
				if( status.equals(SecurityMessageConstants.TOO_EARLY))
				{
					logger.debug("Date too early");
//					errorMessage = languageProp.getProperty("1016");
//					announcementCode = "1016";

					announcementCode = SecurityMessageConstants.EARLY_DATE_ERROR_CODE;
					errorMessage = languageProp.getProperty(SecurityMessageConstants.EARLY_DATE_ERROR_CODE);
					flag = false;
				}
				else if( status.equals(SecurityMessageConstants.TOO_OLD) )
				{
					logger.debug("Date too old");
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
				logger.debug("Sender Id may be wrong");
//				errorMessage = languageProp.getProperty("1025");
//				announcementCode = "1025";

				errorMessage = languageProp.getProperty(SecurityMessageConstants.SENDER_ID_ERROR_CODE);
				announcementCode = SecurityMessageConstants.SENDER_ID_ERROR_CODE;
			}
		}
		catch( DatabaseException de )
		{
			flag = false;
//			errorMessage = languageProp.getProperty("1034");
//			announcementCode = "1034";

			errorMessage = languageProp.getProperty(SecurityMessageConstants.SYSTEM_ERROR_CODE);
			announcementCode = SecurityMessageConstants.SYSTEM_ERROR_CODE;
		}
		catch( Exception e )
		{
			flag = false;
//			errorMessage = languageProp.getProperty("1034");
//			announcementCode = "1034";

			errorMessage = languageProp.getProperty(SecurityMessageConstants.SYSTEM_ERROR_CODE);
			announcementCode = SecurityMessageConstants.SYSTEM_ERROR_CODE;
		}
//		return true;
		logger.debug("ESI message validateDateTime validation-------->>>"+flag);
		return flag;
	}
	
	/*
	 * Validate the time stamp.
	 * Time stamp must be less than 5 banking days
	 * old
	 */
	public boolean validateDateTime( MessageBean msgBean )
	{
		boolean flag = false;
		try
		{
			//check for too early
			//check for too old

			String timeStampString = new StringBuilder(msgBean.getDate())
			.append(msgBean.getTime()).toString();
			logger.debug("Time stamp String---------"+timeStampString);
			java.sql.Timestamp timeStamp = DateUtil.getTimeStamp(
					timeStampString, ServerConstants.DATE_FORMAT);
			logger.debug("Time stamp-------"+timeStamp);
			logger.debug("Sender's id in ESI message-------"
					+ msgBean.getSenderId());// This may be PATU ID
			ServerDAO serverDao = AbstractDAOFactory.getDefaultDAOFactory()
			.getServerDAO();
			Map<String, String> dataMap = (Map<String,String>) serverDao.getESIAgmtDetails(
					msgBean.getSenderId(), timeStamp );
			if( dataMap.get(SecurityMessageConstants.ERRORCODE) == null && dataMap.get(SecurityMessageConstants.ERRORMSG) == null )
			{
				String status = dataMap.get(SecurityMessageConstants.STATUS);
				/*status can be one of the followings.
				 * TOO EARLY
				 * TOO OLD
				 * ACCEPTED
				 */
				if( status.equals(SecurityMessageConstants.TOO_EARLY))
				{
					logger.debug("Date too early");
//					errorMessage = languageProp.getProperty("1016");
//					announcementCode = "1016";

					announcementCode = SecurityMessageConstants.EARLY_DATE_ERROR_CODE;
					errorMessage = languageProp.getProperty(SecurityMessageConstants.EARLY_DATE_ERROR_CODE);
					flag = false;
				}
				else if( status.equals(SecurityMessageConstants.TOO_OLD) )
				{
					logger.debug("Date too old");
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
				logger.debug("Sender Id may be wrong");
//				errorMessage = languageProp.getProperty("1025");
//				announcementCode = "1025";

				errorMessage = languageProp.getProperty(SecurityMessageConstants.SENDER_ID_ERROR_CODE);
				announcementCode = SecurityMessageConstants.SENDER_ID_ERROR_CODE;
			}
		}
		catch( DatabaseException de )
		{
			flag = false;
//			errorMessage = languageProp.getProperty("1034");
//			announcementCode = "1034";

			errorMessage = languageProp.getProperty(SecurityMessageConstants.SYSTEM_ERROR_CODE);
			announcementCode = SecurityMessageConstants.SYSTEM_ERROR_CODE;
		}
		catch( Exception e )
		{
			flag = false;
//			errorMessage = languageProp.getProperty("1034");
//			announcementCode = "1034";

			errorMessage = languageProp.getProperty(SecurityMessageConstants.SYSTEM_ERROR_CODE);
			announcementCode = SecurityMessageConstants.SYSTEM_ERROR_CODE;
		}
//		return true;
		logger.debug("ESI message validateDateTime validation-------->>>"+flag);
		return flag;
	}

	private boolean isAuthenticationTrue( MessageBean msgBean , String strToAuth )
	{
		logger.debug("Total KeyMap------"+keyMap);
		logger.debug("ESI Message String to authenticate====="+strToAuth);
		boolean flag = false;
		if (CommonHandler.isAuthenticationTrue(msgBean.getAut_value(),
				strToAuth, keyMap.get(ServerConstants.AUK)))
		{
			flag = true;
		}
		else
		{
			flag = false;
//			errorMessage = languageProp.getProperty("1020");
//			announcementCode = "1020";
			errorMessage = languageProp.getProperty(SecurityMessageConstants.INVALID_AUTHENTICATION_ERROR_CODE);
			announcementCode = SecurityMessageConstants.INVALID_AUTHENTICATION_ERROR_CODE;
		}
		return flag;
	}

	private boolean isGenerationNoValid( MessageBean msgBean , Session session )
	{
		boolean flag = false;
		/*
		 * Make a database call to validate
		 * the generation number for the
		 * KEK and AUK.
		 * 
		 * Input will be KEKNo,AUKNo and PAtuId
		 */
		try
		{
			String KEKNo = msgBean.getKek_no();
			String AUKNo = msgBean.getAuk_no();
			ServerDAO serverDao = AbstractDAOFactory.getDefaultDAOFactory()
			.getServerDAO();
			keyMap = ( Map<String, String> )serverDao.getKeyGeneration(patuId,KEKNo,AUKNo);
			session.setAttribute(ServerConstants.KEY_MAP , keyMap );
			logger.debug("KEK MAP---------"+keyMap);
			if( keyMap.get(ServerConstants.ERROR_CODE) == null )
			{
				flag = true;
				String kekString = keyMap.get(ServerConstants.KEK ); 
				String newAukGenNo = keyMap.get(ServerConstants.NEW_AUK_GEN_NO);
				newAukGenNo = (newAukGenNo == null) ? msgBean.getKey_change() : newAukGenNo;
//				newAukGenNo = (newAukGenNo == null) ? "0" : "1";
//				msgBean.setKey_change(newAukGenNo);
				/*
				 * Bug fixed
				 */
				msgBean.setKey_change(msgBean.getKey_change());
				String newAukKey = keyMap.get(ServerConstants.NEW_AUK );
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
				logger.debug("New AUK key---------"+newAukKey);
			}
			else
			{
				flag = false;
//				errorMessage =  "INVALID KEK";
//				announcementCode = "1013";
//				errorMessage = languageProp.getProperty("1013");

				announcementCode = SecurityMessageConstants.INVALID_KEK_ERROR_CODE;
				errorMessage = languageProp.getProperty(SecurityMessageConstants.INVALID_KEK_ERROR_CODE);

			}
			logger.debug("ESI message isGenerationNoValid validation-------->>>"+flag);
		}
		catch( DatabaseException de )
		{
			de.printStackTrace();
			logger.error(de);
			logger.error(de.getMessage());
		}
		catch( Exception e )
		{
			e.printStackTrace();
			logger.error(e);
			logger.error(e.getMessage());
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
			logger.debug("TimeStamp in ESI message------"+inTimeStamp);
			ServerDAO serverDao = AbstractDAOFactory.getDefaultDAOFactory()
			.getServerDAO();
			flag = ((Boolean) serverDao.isTimeStampUnique(inTimeStamp))
			.booleanValue();
			logger.debug("Time stamp flag------------"+flag);
			if( flag )
			{
				//Time stamp ok
			}
			else
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
			logger.error(de.getMessage());
		}
		catch( Exception e )
		{
			e.printStackTrace();
			logger.error(e);
		}
		return flag;
	}

	public boolean isESIAuthenticationUsed( MessageBean msgBean )
	{
		boolean flag = true;
		/*
		 * I think it will be validated against the
		 * PATU ID.
		 * 
		 * Make a database call , provide PATU id 
		 * or sender's as input to the database call
		 * and check whether it is true or not.
		 */
		return flag;
	}

	public boolean validateKeyChange( MessageBean msgBean , Session session )
	{
		boolean flag = true;
		logger.debug("msgBean.getKey_change()------->>>"+msgBean.getKey_change());
		try
		{
			String newAukKey = keyMap.get(ServerConstants.NEW_AUK );
			String kekString = keyMap.get(ServerConstants.KEK ); 
			/**anandkumr.b 20-Nov-2008
			 * Code changed to implement the key exchange process
			 */
			String newKeyRequest = keyMap.get(ServerConstants.FLAG);
			if(newKeyRequest == null){
				newKeyRequest = "N";
			}
			if( msgBean.getAuk_no().equals("0") || newKeyRequest.equals("Y"))
			{
				/*
				 * This is as per the Nordea bank
				 */
				String newAUKKey = KeyGenerator.getNewAuk();
				logger.debug("In validateKeyChange New AUK key--------"+newAUKKey);
				byte[] newAUKbytes = CommonUtil.hexToBytes(newAUKKey) ;
				/*
				 * Set the odd parity
				 */
				newAUKbytes = ParityUtil.getOddPariyByte(newAUKbytes);

				keyMap = (Map<String,String>)session.getAttribute(ServerConstants.KEY_MAP);
				keyMap.put( ServerConstants.NEW_AUK, newAUKKey );
				session.setAttribute(ServerConstants.KEY_MAP, keyMap);
				ServerDAO serverDao = AbstractDAOFactory.getDefaultDAOFactory()
				.getServerDAO();
				Map<String, String> dataMap = (Map<String, String>) serverDao
				.insertNewAukKey(patuId, newAUKKey);
//				String kekString = keyMap.get(ServerConstants.KEK);
				logger.debug("In validateKeyChange KEK String-------"+kekString);
				byte[] kekBytes = CommonUtil.hexToBytes(kekString);
				SecretKey secretKey = DESUtil.getSecretKey(kekBytes, SecurityMessageConstants.SECRETKEY_DES);
				byte[] encryptedBytes = DESUtil.getEncryptedBytesWithIV(newAUKbytes, secretKey);
				String encryptedAUK = CommonUtil.bytesToHex(encryptedBytes);
				logger.debug("In validateKeyChange encryptedAUK-------"+encryptedAUK);
				msgBean.setNew_key(encryptedAUK);
				msgBean.setKey_change(ServerConstants.NEW_KEY_CHANGE);
			}
			else if( newAukKey != null )
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
				msgBean.setNew_key(newAukKey);
				/*
				 * Bug fixed
				 */
//				msgBean.setKey_change(msgBean.getKey_change());
				msgBean.setKey_change(ServerConstants.NEW_KEY_CHANGE);
			}
			else if( msgBean.getKey_change().equals("1") )
			{
				logger.debug("Coming for new AUK block for KEY change");
				/*
				 * Generate new AUK key
				 * Store into database, input will be patu id and new AUK
				 * Set the new key no and the new AUK key into the bean.
				 */
				String newAUKKey = KeyGenerator.getNewAuk();
				byte[] newAUKbytes = CommonUtil.hexToBytes(newAUKKey);

				keyMap = (Map<String,String>)session.getAttribute(ServerConstants.KEY_MAP);
				keyMap.put( ServerConstants.AUK, newAUKKey );
				session.setAttribute(ServerConstants.KEY_MAP, keyMap);
				ServerDAO serverDao = AbstractDAOFactory.getDefaultDAOFactory()
				.getServerDAO();
				Map<String, String> dataMap = (Map<String, String>) serverDao
				.insertNewAukKey(patuId, newAUKKey);
//				String kekString = keyMap.get(ServerConstants.KEK);
				byte[] kekBytes = CommonUtil.hexToBytes(kekString);
				SecretKey secretKey = DESUtil.getSecretKey(kekBytes, SecurityMessageConstants.SECRETKEY_DES);
				byte[] encryptedBytes = DESUtil.getEncryptedBytesWithIV(newAUKbytes, secretKey);
				String encryptedAUK = CommonUtil.bytesToHex(encryptedBytes);
				msgBean.setNew_key(encryptedAUK);
				msgBean.setKey_change(dataMap.get( ServerConstants.NEW_AUK_GEN_NO));
			}
			else
			{
				//do nothing
			}

		}
		catch( DatabaseException de )
		{
			de.printStackTrace();
			logger.error(de);
			logger.error(de.getMessage());
		}
		catch( Exception e )
		{
			e.printStackTrace();
			logger.error(e);
		}
		return flag;
	}

	private String getLanguage( String patuId )
	{
		String language = null;
		try
		{
			ServerDAO serverDao = AbstractDAOFactory.getDefaultDAOFactory()
			.getServerDAO();
			logger.debug("Server DAO-------"+serverDao);
			Map<String, String> languageMap = (Map<String, String>) serverDao
			.getLanguage(patuId);
			logger.debug("Language Map-----"+languageMap);
			if( languageMap.get(ServerConstants.ERROR_CODE) != null )
			{
				language = languageMap.get(ServerConstants.LANGUAGE);
				logger.debug("Language IN ESI-------"+language);
				language = language == null ? SecurityMessageConstants.LANGUAGE:language;
				logger.debug("Language IN ESI after change-------"+language);
			}
			else
				language = SecurityMessageConstants.LANGUAGE;
		}
		catch( NullPointerException npe )
		{
			language = SecurityMessageConstants.LANGUAGE;
		}
		catch( Exception e )
		{
			language = SecurityMessageConstants.LANGUAGE;
		}
		return language;
	}
	
	@SuppressWarnings("unused")
	private boolean checkCustomerId(String patuID)
	{
		boolean status = false;
		/*
		 * Make a database call to check whether PATU_ID is exist or not.
		 */
		try {
			logger.debug("Patu ID in ESI message------" + patuID);
			ServerDAO serverDao = AbstractDAOFactory.getDefaultDAOFactory().getServerDAO();
			status = serverDao.isPatuIdExists(patuID);
			
			if( !status ) {
				announcementCode = SecurityMessageConstants.INVALID_CUSTOMERID_ERROR_CODE;
				errorMessage = languageProp.getProperty(SecurityMessageConstants.INVALID_CUSTOMERID_ERROR_CODE);
			}
		} catch( DatabaseException de )	{
			logger.debug("checkCustomerId : DatabaseException :: " + de.getMessage());			
		}
		catch( Exception e ) {
			logger.debug("checkCustomerId : Exception :: " + e.getMessage());
		}
		return status;
	}
}