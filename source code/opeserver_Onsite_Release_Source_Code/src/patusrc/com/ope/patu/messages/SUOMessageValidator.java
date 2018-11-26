package com.ope.patu.messages;

import java.util.Map;
import java.util.Properties;

import javax.crypto.SecretKey;

import org.apache.log4j.Logger;

import com.coldcore.coloradoftp.session.Session;
import com.ope.patu.exception.DatabaseException;
import com.ope.patu.exception.FailedValidationException;
import com.ope.patu.exception.MessageParsingException;
import com.ope.patu.server.db.AuditLogger;
import com.ope.patu.handler.Parser;
import com.ope.patu.handler.Validator;
import com.ope.patu.parsers.SUOMessageParser;
import com.ope.patu.server.beans.MessageBean;
import com.ope.patu.server.constant.ServerConstants;
import com.ope.patu.server.constant.SecurityMessageConstants;
import com.ope.patu.server.db.AbstractDAOFactory;
import com.ope.patu.server.db.ServerDAO;
import com.ope.patu.util.CommonUtil;
import com.ope.patu.util.DESUtil;
import com.ope.patu.util.DateUtil;
import com.ope.patu.util.FileUtil;

public class SUOMessageValidator implements Validator 
{
	private MessageBean msgBean;
	private String errorMessage;
	private String announcementCode;
	private String patuId;
	private Properties secureProp;
	Properties languageProp = new Properties();

	protected static Logger logger = Logger.getLogger(SUOMessageValidator.class);

	public SUOMessageValidator()
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
		String suoMsgContents = ( String )objects[0];
		String fileName = ( String )objects[1];
		Session session = ( Session )objects[2];
		String actualDataContents = ( String )objects[3];
		Parser parser = new SUOMessageParser();
		try
		{
			msgBean = ( MessageBean )parser.parse( suoMsgContents );
		}
		catch( MessageParsingException mpe )
		{
			logger.error(mpe);
			logger.error(mpe.getMessage());
			mpe.printStackTrace();
		}
		languageProp = ( Properties )session.getAttribute(ServerConstants.LANGUAGE_PROP);
		/*
		 * Store the original SUO message in the session
		 * Manage the audit log for the original SUO message
		 */
		session.setAttribute(ServerConstants.SUOA_MSG_BEAN, (MessageBean)msgBean.clone());
		String filePath = FileUtil.getSessionTempPath(session, fileName);
		
//		AuditLogger.logSecurityMessage(session, fileName, filePath, "INCOMING",
//				(MessageBean) msgBean.clone(), null);
		try
		{
			if( !checkMsgContentsLength(suoMsgContents,msgBean.getMessage_length()) )
			{
				validationFlag = false;
				// msgBean.setAcceptance_code("E"); // NO
				msgBean.setAcceptance_code(SecurityMessageConstants.ACCEPTANCE_CODE_NO);
				msgBean.setAnnouncement_code(announcementCode);
				msgBean.setAnnouncement(DateUtil.getTime()+" "+errorMessage);
				msgBean.setSoftware(secureProp.getProperty("software"));
				logger.debug("in SUO Message Content length validation----------"+validationFlag);
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
				logger.debug("In SUO Version validation----------"+validationFlag);
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
				logger.debug("In SUO ReceiversId validation----------"+validationFlag);
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
				logger.debug("In SUO validateDateTime validation----------"+validationFlag);
				return validationFlag;
			}
			else if( !isHSKParityOdd(msgBean) )
			{
				validationFlag = false;
				// msgBean.setAcceptance_code("E");
				msgBean.setAcceptance_code(SecurityMessageConstants.ACCEPTANCE_CODE_NO);
				msgBean.setAnnouncement_code(announcementCode);
				msgBean.setAnnouncement(DateUtil.getTime()+" "+errorMessage);
				msgBean.setSoftware(secureProp.getProperty("software"));
				logger.debug("In SUO HSKParityOdd validation----------"+validationFlag);
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
				logger.debug("In SUO GenerationNoValid validation----------"+validationFlag);
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
				logger.debug("In SUO isTimeStampRepeated validation----------"+validationFlag);
				return validationFlag;
			}
			else
			{
				validationFlag = true;
				// msgBean.setAcceptance_code("K"); // YES
				msgBean.setAcceptance_code(SecurityMessageConstants.ACCEPTANCE_CODE_YES);
				// msgBean.setAnnouncement_code("R001");
				msgBean.setAnnouncement_code(SecurityMessageConstants.ANNOUNCEMENT_CODE_R001);
				session.setAttribute(ServerConstants.SUO_MSG_STATUS, validationFlag);
				session.setAttribute(ServerConstants.SUOP_MSG_BEAN, msgBean);
				logger.debug("In SUO ALL validation----------"+validationFlag);
				return validationFlag;
			}
		}
		catch( Exception e )
		{
			e.printStackTrace();
			logger.error(e);
			throw new FailedValidationException();
		}

	}

	//Validation for the message length
	private boolean checkMsgContentsLength( String suoMsgContents , String msgLength )
	{
		boolean flag = false;
//		boolean flag = suoMsgContents.length() == 128 ? true : false;
//		if( flag == false )
		if( suoMsgContents.length() == Integer.parseInt(msgLength) )
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
		logger.debug("SUO message content validation-------->>>"+flag);
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
//			announcementCode = "1021";
//			errorMessage = DateUtil.getTime()+" "+languageProp.getProperty("1021");
			announcementCode = SecurityMessageConstants.INVALID_RECEIVER_ID_ERROR_CODE;
			errorMessage = DateUtil.getTime()+" "+languageProp.getProperty(SecurityMessageConstants.INVALID_RECEIVER_ID_ERROR_CODE);
	
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
			
			logger.debug("In SUO Msg Validation - versionMinValue->"+versionMinValue+" and versionMaxValue->"+versionMaxValue);
			
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
					secureProp.getProperty("security_version")))
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
		return flag;
	}

	public boolean validateDateTime( MessageBean msgBean )
	{
		boolean flag = false;
		try
		{
			//check for too early
			//check for too old
			ServerDAO serverDao = AbstractDAOFactory.getDefaultDAOFactory()
			.getServerDAO();
//			System.out.println("msgBean.getDate()------------"+msgBean.getDate());
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
//			errorMessage = languageProp.getProperty("1034");
//			announcementCode = "1034";
			errorMessage = languageProp.getProperty(SecurityMessageConstants.SYSTEM_ERROR_CODE);
			announcementCode = SecurityMessageConstants.SYSTEM_ERROR_CODE;
		}
		catch( Exception e )
		{
			flag = false;
//			announcementCode = "1034";
//			errorMessage = languageProp.getProperty("1034");
			errorMessage = languageProp.getProperty(SecurityMessageConstants.SYSTEM_ERROR_CODE);
			announcementCode = SecurityMessageConstants.SYSTEM_ERROR_CODE;
		}
		return flag;
	}

	private boolean isHSKParityOdd( MessageBean msgBean )
	{
		boolean flag = CommonHandler.isHSKParityOdd(msgBean.getHsk());
		if( !flag )
		{
//			announcementCode = "1031";
//			errorMessage = languageProp.getProperty("1031");
			announcementCode = SecurityMessageConstants.HSK_PARITY_ERROR_CODE;
			errorMessage = languageProp.getProperty(SecurityMessageConstants.HSK_PARITY_ERROR_CODE);
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
			Map<String, String> keyMap = (Map<String, String>) serverDao
			.getKeyGeneration(patuId, KEKNo, AUKNo);
			if( keyMap.get(ServerConstants.ERROR_CODE) == null )
			{
				flag = true;
				String kekString = keyMap.get(ServerConstants.KEK ); 
				String newAukGenNo = keyMap.get(ServerConstants.NEW_AUK_GEN_NO);
				newAukGenNo = (newAukGenNo == null) ? msgBean.getKey_change() : newAukGenNo;
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
			}
			else
			{
				flag = false;
//				errorMessage = languageProp.getProperty("1013");
//				announcementCode = "1013";
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
		String inTimeStamp = new StringBuilder(msgBean.getDate()).append(
				msgBean.getTime()).append(msgBean.getStampNo()).toString();
		logger.debug("Time Stamp in SUO----------"+inTimeStamp);
		try
		{
			ServerDAO serverDao = AbstractDAOFactory.getDefaultDAOFactory()
			.getServerDAO();
			flag = ((Boolean) serverDao.isTimeStampUnique(inTimeStamp))
					.booleanValue();
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
			logger.error(e.getMessage());
		}
		return flag;
	}



}