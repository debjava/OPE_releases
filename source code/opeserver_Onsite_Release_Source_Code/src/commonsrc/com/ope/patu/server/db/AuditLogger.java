package com.ope.patu.server.db;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.coldcore.coloradoftp.session.Session;
import com.coldcore.coloradoftp.session.SessionAttributeName;
import com.ope.patu.exception.DatabaseException;
import com.ope.patu.server.beans.MessageBean;
import com.ope.patu.server.beans.TransferRequestBean;
import com.ope.patu.server.constant.ServerConstants;
import com.ope.patu.server.db.AbstractDAOFactory;
import com.ope.patu.server.db.ServerDAO;
import com.ope.patu.util.DateUtil;

/*
 * This class is changed, now we are passing two more
 * parameter PATU_ID and AGREEMENT_ID in the audit log */
public class AuditLogger 
{
	protected static Logger logger = Logger.getLogger(AuditLogger.class);
	
	public static void logSecurityMessage(Session session, String fileName,
			String filePath, String inOut, MessageBean msgBean,String status)
	{
		String sessionIdStr = new Integer( session.getSessionId() ).toString();
		Map<String, String> auditMap = new LinkedHashMap<String, String>();
		String customerName = (String)session.getAttribute(SessionAttributeName.USERNAME);
		String patuId = (String)session.getAttribute(ServerConstants.PATUID);
		String agreementId = (String)session.getAttribute(ServerConstants.AGREEMENTTID);
		customerName = customerName == null ? "Anonymous" : customerName;
		auditMap.put(ServerConstants.PATUID, patuId );
		auditMap.put(ServerConstants.AGREEMENTTID, agreementId);
		auditMap.put("Customer_Name", customerName );
		auditMap.put("File_name", fileName);
		
		/* This part is added to remove ">>" from File Type. */
		String fileType = msgBean.getMessageCode();
		if(fileType != null) {
				fileType = fileType.substring(fileType.lastIndexOf('>')+1, fileType.length());
			}
		
		/* In ESIp message Announcement is set with date. 
		 * Ex- "03:24:36 DATE TOO EARLY"
		 * So in the Audit Logger we need to extract the actual error message 
		 * from the Announcement
		 */
		String errorMessage = msgBean.getAnnouncement();
		if(errorMessage != null) {
			errorMessage = errorMessage.substring(9, errorMessage.length());
		}
		
		auditMap.put("File_Type", fileType);

		String fileTimeStamp = new StringBuilder(msgBean.getDate()).append(
				msgBean.getTime()).append(msgBean.getStampNo()).toString();
		auditMap.put("file_Date", fileTimeStamp );
		auditMap.put("Receiver", msgBean.getRecId());
		auditMap.put("Sender", msgBean.getSenderId());
		auditMap.put("file_location", filePath);
		auditMap.put("total_Records", null);
		auditMap.put("Valid_Record_Count", null);
		auditMap.put("Invalid_Record_count", null);
		auditMap.put("In_Out", inOut);
		auditMap.put("Session_ID", sessionIdStr );
		auditMap.put("Error_Code", msgBean.getAnnouncement_code());
		auditMap.put("Error_Message", errorMessage);
		auditMap.put("status", status);
		auditMap.put("SYSTEM_DATE", DateUtil.GetDateTime());
		storeAuditLog(auditMap);
	}

	public static void logTRMessage(Session session, String fileName,
			String filePath, String inOut, TransferRequestBean trBean)
	{
		Map<String, String> auditMap = new LinkedHashMap<String, String>();
		String customerName = (String)session.getAttribute(SessionAttributeName.USERNAME);
		String patuId = (String)session.getAttribute(ServerConstants.PATUID);
		String agreementId = (String)session.getAttribute(ServerConstants.AGREEMENTTID);
		customerName = customerName == null ? "Anonymous" : customerName;
		auditMap.put(ServerConstants.PATUID, patuId );
		auditMap.put(ServerConstants.AGREEMENTTID, agreementId);
		auditMap.put("Customer_Name", customerName );
		auditMap.put("File_name", fileName);
		auditMap.put("File_Type", trBean.getRecordId());
		
		String date = trBean.getDate();

		/* In the Acknowledge case we are not getting TR file.
		 * So on in this case we have to insert the current processing date 
		 * in the Audit Log.
		 */

		if(Integer.parseInt(date)==0){
			date = DateUtil.getDate("yyMMddHHmmss");
		}
		
		auditMap.put("file_Date", date);
		MessageBean msgBean = (MessageBean) session
		.getAttribute(ServerConstants.ESIP_MSG_BEAN);
		auditMap.put("Receiver", msgBean.getRecId());
		auditMap.put("Sender", msgBean.getSenderId());
		auditMap.put("file_location", filePath);
		auditMap.put("total_Records", null);
		auditMap.put("Valid_Record_Count", null);
		auditMap.put("Invalid_Record_count", null);
		auditMap.put("In_Out", inOut);
		auditMap.put("Session_ID", new Integer( session.getSessionId()).toString());
		auditMap.put("Error_Code", null);
		auditMap.put("Error_Message", null);
		auditMap.put("status", ((Boolean)session.getAttribute(ServerConstants.TR_MSG_STATUS)).toString());
		auditMap.put("SYSTEM_DATE", DateUtil.GetDateTime());
		storeAuditLog(auditMap);
	}
	
	public static void logAcknowledgementOrFeedback( Session session, String fileName,
			String filePath, String inOut )
	{
		Map<String, String> auditMap = new LinkedHashMap<String, String>();
		String customerName = (String)session.getAttribute(SessionAttributeName.USERNAME);
		String patuId = (String)session.getAttribute(ServerConstants.PATUID);
		String agreementId = (String)session.getAttribute(ServerConstants.AGREEMENTTID);
		
		Map transactionMap = (HashMap) session.getAttribute(ServerConstants.TRANSACTION_MAP);
		
		String totalRecords = (String)transactionMap.get(ServerConstants.TOTAL_RECORDS);
		String validRecordCount = (String)transactionMap.get(ServerConstants.VALID_RECORD_COUNT);
		String invalidRecordCount = (String)transactionMap.get(ServerConstants.INVALID_RECORD_COUNT);
		
		String errorCode = (String)transactionMap.get(ServerConstants.PAYMENT_FILE_ERROR_CODE);
		String errorMessage = (String)transactionMap.get(ServerConstants.PAYMENT_FILE_ERROR_MESSAGE);
		String status = (String)transactionMap.get(ServerConstants.PAYMENT_FILE_STATUS);
		
		logger.debug("Log_Feedback :: totalRecords -> "+totalRecords+" :: validRecordCount -> "+validRecordCount);
		logger.debug("Log_Feedback :: invalidRecordCount -> "+invalidRecordCount+" :: errorCode -> "+errorCode);
		logger.debug("Log_Feedback :: errorMessage -> "+errorMessage+" :: status -> "+status);
		
		customerName = customerName == null ? "Anonymous" : customerName;
		auditMap.put(ServerConstants.PATUID, patuId );
		auditMap.put(ServerConstants.AGREEMENTTID, agreementId);
		auditMap.put("Customer_Name", customerName );
		auditMap.put("File_name", fileName);
		TransferRequestBean trBean = (TransferRequestBean) session
		.getAttribute(ServerConstants.TR_OBJECT);
		auditMap.put("File_Type", trBean.getFileType());
		
		String date = trBean.getDate();
		
		/* If we are not getting TR file. So on in this case we have to 
		 * insert the current processing date in the Audit Log.
		 */
		
		if(Integer.parseInt(date)==0){
			date = DateUtil.getDate("yyMMddHHmmss");
		}
		
		auditMap.put("file_Date", date);
		MessageBean msgBean = (MessageBean) session
		.getAttribute(ServerConstants.ESIP_MSG_BEAN);
		auditMap.put("Receiver", msgBean.getRecId());
		auditMap.put("Sender", msgBean.getSenderId());
		auditMap.put("file_location", filePath);
		auditMap.put("total_Records", totalRecords);
		auditMap.put("Valid_Record_Count", validRecordCount);
		auditMap.put("Invalid_Record_count", invalidRecordCount);
		auditMap.put("In_Out", inOut);
		auditMap.put("Session_ID", new Integer( session.getSessionId()).toString());
		auditMap.put("Error_Code", errorCode); // Previously 'null' is hard coded.
		auditMap.put("Error_Message", errorMessage); // Previously 'null' is hard coded.
		auditMap.put("status", status ); // Previously 'null' is hard coded.
		auditMap.put("SYSTEM_DATE", DateUtil.GetDateTime());
		storeAuditLog(auditMap);
	}
	
	public static Map getFeedbackLogMap( Session session, String fileName,
			String filePath, String inOut )
	{
		Map<String, String> auditMap = new LinkedHashMap<String, String>();
		String customerName = (String)session.getAttribute(SessionAttributeName.USERNAME);
		String patuId = (String)session.getAttribute(ServerConstants.PATUID);
		String agreementId = (String)session.getAttribute(ServerConstants.AGREEMENTTID);
		
		Map transactionMap = (HashMap) session.getAttribute(ServerConstants.TRANSACTION_MAP);
		
		String totalRecords = (String)transactionMap.get(ServerConstants.TOTAL_RECORDS);
		String validRecordCount = (String)transactionMap.get(ServerConstants.VALID_RECORD_COUNT);
		String invalidRecordCount = (String)transactionMap.get(ServerConstants.INVALID_RECORD_COUNT);
		
		String errorCode = (String)transactionMap.get(ServerConstants.PAYMENT_FILE_ERROR_CODE);
		String errorMessage = (String)transactionMap.get(ServerConstants.PAYMENT_FILE_ERROR_MESSAGE);
		String status = (String)transactionMap.get(ServerConstants.PAYMENT_FILE_STATUS);
		
		logger.debug("Log_Feedback :: totalRecords -> "+totalRecords+" :: validRecordCount -> "+validRecordCount);
		logger.debug("Log_Feedback :: invalidRecordCount -> "+invalidRecordCount+" :: errorCode -> "+errorCode);
		logger.debug("Log_Feedback :: errorMessage -> "+errorMessage+" :: status -> "+status);
		
		customerName = customerName == null ? "Anonymous" : customerName;
		auditMap.put(ServerConstants.PATUID, patuId );
		auditMap.put(ServerConstants.AGREEMENTTID, agreementId);
		auditMap.put("Customer_Name", customerName );
		auditMap.put("File_name", fileName);
		TransferRequestBean trBean = (TransferRequestBean) session
		.getAttribute(ServerConstants.TR_OBJECT);
		auditMap.put("File_Type", trBean.getFileType());
		
		String date = trBean.getDate();
		
		/* If we are not getting TR file. So on in this case we have to 
		 * insert the current processing date in the Audit Log.
		 */
		
		if(Integer.parseInt(date)==0){
			date = DateUtil.getDate("yyMMddHHmmss");
		}
		
		auditMap.put("file_Date", date);
		MessageBean msgBean = (MessageBean) session
		.getAttribute(ServerConstants.ESIP_MSG_BEAN);
		auditMap.put("Receiver", msgBean.getRecId());
		auditMap.put("Sender", msgBean.getSenderId());
		auditMap.put("file_location", filePath);
		auditMap.put("total_Records", totalRecords);
		auditMap.put("Valid_Record_Count", validRecordCount);
		auditMap.put("Invalid_Record_count", invalidRecordCount);
		auditMap.put("In_Out", inOut);
		auditMap.put("Session_ID", new Integer( session.getSessionId()).toString());
		auditMap.put("Error_Code", errorCode); // Previously 'null' is hard coded.
		auditMap.put("Error_Message", errorMessage); // Previously 'null' is hard coded.
		auditMap.put("status", status ); // Previously 'null' is hard coded.
		auditMap.put("SYSTEM_DATE", DateUtil.GetDateTime());
		
		// storeAuditLog(auditMap);
		
		return auditMap;
	}
	
	public static void logErrorMsg( Session session, String fileName,
			String filePath, String inOut )
	{
		Map<String, String> auditMap = new LinkedHashMap<String, String>();
		String customerName = (String)session.getAttribute(SessionAttributeName.USERNAME);
		String patuId = (String)session.getAttribute(ServerConstants.PATUID);
		String agreementId = (String)session.getAttribute(ServerConstants.AGREEMENTTID);
		customerName = customerName == null ? "Anonymous" : customerName;
		auditMap.put(ServerConstants.PATUID, patuId );
		auditMap.put(ServerConstants.AGREEMENTTID, agreementId);
		auditMap.put("Customer_Name", customerName );
		auditMap.put("File_name", fileName);
		TransferRequestBean trBean = (TransferRequestBean) session
		.getAttribute(ServerConstants.TR_OBJECT);
		auditMap.put("File_Type", trBean.getFileType());
		auditMap.put("file_Date", trBean.getDate());
		MessageBean msgBean = (MessageBean) session
		.getAttribute(ServerConstants.ESIP_MSG_BEAN);
		auditMap.put("Receiver", msgBean.getRecId());
		auditMap.put("Sender", msgBean.getSenderId());
		auditMap.put("file_location", filePath);
		auditMap.put("total_Records", null);
		auditMap.put("Valid_Record_Count", null);
		auditMap.put("Invalid_Record_count", null);
		auditMap.put("In_Out", inOut);
		auditMap.put("Session_ID", new Integer( session.getSessionId()).toString());
		auditMap.put("Error_Code", null);
		auditMap.put("Error_Message", "Requested file does not exist");
		auditMap.put("status", null );
		auditMap.put("SYSTEM_DATE", DateUtil.GetDateTime());
		storeAuditLog(auditMap);
	}
	
	public static void logPaymentMsg( Session session, String fileName,
			String filePath, String inOut )
	{
		Map<String, String> auditMap = new LinkedHashMap<String, String>();
		String customerName = (String)session.getAttribute(SessionAttributeName.USERNAME);
		String patuId = (String)session.getAttribute(ServerConstants.PATUID);
		String agreementId = (String)session.getAttribute(ServerConstants.AGREEMENTTID);
		customerName = customerName == null ? "Anonymous" : customerName;
		auditMap.put(ServerConstants.PATUID, patuId );
		auditMap.put(ServerConstants.AGREEMENTTID, agreementId);
		auditMap.put("Customer_Name", customerName );
		auditMap.put("File_name", fileName);
		TransferRequestBean trBean = (TransferRequestBean) session
		.getAttribute(ServerConstants.TR_OBJECT);
		auditMap.put("File_Type", trBean.getFileType());
		auditMap.put("file_Date", trBean.getDate());
		MessageBean msgBean = (MessageBean) session
		.getAttribute(ServerConstants.ESIP_MSG_BEAN);
		auditMap.put("Receiver", msgBean.getRecId());
		auditMap.put("Sender", msgBean.getSenderId());
		auditMap.put("file_location", filePath);
	
		Map transactionMap = (HashMap) session.getAttribute(ServerConstants.TRANSACTION_MAP);
		
		String totalRecords = (String)transactionMap.get(ServerConstants.TOTAL_RECORDS);
		String validRecordCount = (String)transactionMap.get(ServerConstants.VALID_RECORD_COUNT);
		String invalidRecordCount = (String)transactionMap.get(ServerConstants.INVALID_RECORD_COUNT);
		
		String errorCode = (String)transactionMap.get(ServerConstants.PAYMENT_FILE_ERROR_CODE);
		String errorMessage = (String)transactionMap.get(ServerConstants.PAYMENT_FILE_ERROR_MESSAGE);
		String status = (String)transactionMap.get(ServerConstants.PAYMENT_FILE_STATUS);
		
		logger.debug("Log_Payment :: totalRecords -> "+totalRecords+" :: validRecordCount -> "+validRecordCount);
		logger.debug("Log_Payment :: invalidRecordCount -> "+invalidRecordCount+" :: errorCode -> "+errorCode);
		logger.debug("Log_Payment :: errorMessage -> "+errorMessage+" :: status -> "+status);
		
		
		auditMap.put("total_Records", totalRecords);
		auditMap.put("Valid_Record_Count", validRecordCount);
		auditMap.put("Invalid_Record_count",invalidRecordCount);
		auditMap.put("In_Out", inOut);
		auditMap.put("Session_ID", new Integer( session.getSessionId()).toString());
		auditMap.put("Error_Code", errorCode); // Previously 'null' is hard coded.
		auditMap.put("Error_Message", errorMessage); // "Requested file does not exist" message has been deleted. 
		auditMap.put("status", status );	// Previously 'null' is hard coded.
		auditMap.put("SYSTEM_DATE", DateUtil.GetDateTime());
		storeAuditLog(auditMap);
	}

	public static void storeAuditLog(Map<String,String> auditMap)
	{
		try
		{
			ServerDAO serverDAO = AbstractDAOFactory.getDefaultDAOFactory().getServerDAO();
			serverDAO.insertAuditLog(auditMap);
		}
		catch( DatabaseException de )
		{
			de.printStackTrace();
			logger.error(de);
		}
		catch( Exception e )
		{
			e.printStackTrace();
			logger.error(e);
		}
	}
}
