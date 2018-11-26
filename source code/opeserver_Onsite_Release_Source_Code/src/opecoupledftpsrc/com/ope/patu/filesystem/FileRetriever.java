package com.ope.patu.filesystem;

import java.io.File;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;


import org.apache.log4j.Logger;

import com.coldcore.coloradoftp.session.Session;
import com.ope.patu.ejb.server.OpeEjbRemote;
import com.ope.patu.ejb.util.PATUEjbClass;
import com.ope.patu.exception.DatabaseException;
import com.ope.patu.server.db.AuditLogger;
import com.ope.patu.server.beans.TransferRequestBean;
import com.ope.patu.server.constant.SecurityMessageConstants;
import com.ope.patu.server.constant.ServerConstants;
import com.ope.patu.server.db.AbstractDAOFactory;
import com.ope.patu.server.db.ServerDAO;
import com.ope.patu.util.CommonUtil;
import com.ope.patu.util.DateUtil;
import com.ope.patu.util.FTPFileUtil;



public class FileRetriever {
	protected static Logger logger = Logger.getLogger(FileRetriever.class);
	
	public static String getFilePath(String fileName, Session session) {
					
		String path = null;
		try {
			TransferRequestBean trBean = (TransferRequestBean) session.getAttribute(ServerConstants.TR_OBJECT);
			if (trBean == null) {
				
					
				/*
				 * It is assumed that Transfer Request has not yet received. 
				 * So there can be two conditions..
				 * 	1. File request can be for ESIp message.
				 * 	2. File request can be for Retrievable file "WITH OUT - TR"
				 */
				
				/*
				 * So check the File Type condition
				 * 	1. For ESIp message status will be "null"
				 * 	2. For Retrievable file status will be "R"
				 */
				
				TransferRequestBean transferBean = new TransferRequestBean();
				transferBean.setFileType(fileName);
				String requestFilePath = null;
				try {
					Properties fileProp = FTPFileUtil.getFileTypes();
					
					String fileType = fileProp.getProperty(fileName);
					if(fileType!=null ? fileType.equals(ServerConstants.STATUS_R) : false) {
						/* 	File request for Retrievable file "WITH OUT - TR" */
						session.setAttribute(ServerConstants.SERVICECODE, fileName);
						requestFilePath = getRetrievableFilePath(session, ServerConstants.DEFAULT_MERGE_CONSTANT) ;
						logger.debug("getRetrievableFilePath -> Without TR -> requestFilePath ->"+requestFilePath);
					} else {
						/* 	File request for ESIp message */
						requestFilePath = getESIpFilePath(fileName, session);
					}
				} catch (Exception e) {
					FTPFileUtil.writeContents(requestFilePath, getErrorMessage(trBean));
				}
				path = requestFilePath;
			} else {
				/*
				 * File name from the transfer request
				 */
				
				String fileTypeName = trBean.getFileType();
				int redoStatus = 0;
				if(!(trBean.getRedo()==null || trBean.getRedo().equals(" ")))
					{ redoStatus = Integer.parseInt(trBean.getRedo()); }
				Properties fileProp = FTPFileUtil.getFileTypes();
				
				/*
				 * Check the type of file whether Retrieval,Transmittable/Feedback or Other type
				 */
				
				String trFileStatus = (String) session.getAttribute(ServerConstants.TR_FILE_STATUS);
				
				String fileType ="";
				if(!(trFileStatus!=null ? trFileStatus.equals(ServerConstants.STATUS_YES) : false)) {
					fileType = fileProp.getProperty(fileName);
					fileTypeName = fileName;
				} else {
					fileType = fileProp.getProperty(fileTypeName);
				}
				
				logger.debug("--@--> getFilePath -> FileTypeName => "+fileTypeName+" & FileType => "+fileType);
								
				if (fileType.equalsIgnoreCase("F")) {
					/* 	File request for Feedback file */
					path = getFeedbackAndOtherFilePath(session,fileTypeName,fileName,trBean);
				} else if (fileType.equalsIgnoreCase("R")) {
					/* 	File request for Retrievable file "WITH - TR" */
					path = getRetrievableFilePath(session, redoStatus) ;
				} else {
					/* 	File request for PTE message */
					path = getPTEFilePath(session, fileName);
				}
			}

		} catch (NullPointerException npe) {
			logger.error("NullPointerException thrown" + npe.getMessage());
		} catch (Exception e) {
			logger.error("Other exception thrown - Error in retriving the file."+ e.getMessage());
		}
		return path;
	}
	
	private static String getFeedbackAndOtherFilePath(Session session,String fileTypeName, String fileName,TransferRequestBean trBean) throws DatabaseException, RemoteException {
		logger.debug(":::::::::::::: I'm inside getFeedbackAndOtherFilePath :::::::::::::");
		String agmtServicePath = null;
		String agmtId = (String) session.getAttribute(ServerConstants.AGREEMENTTID);
		
		if (agmtId == null) {
			/*
			 * Get the agmtId service code path. Make a procedure to get the agmtId
			 */
			logger.debug(" getFeedbackAndOtherFilePath -> Agmt Id is NULL");
			
			String serviceBureauId = (String) session.getAttribute(ServerConstants.SERVICEBUREAUID);
			String serviceType = (String) session.getAttribute(ServerConstants.SERVICECODE);
			String serviceId = (String) session.getAttribute(ServerConstants.SERVICEID);
			
			Map<String, String> fileDataMap = new HashMap<String, String>();
			fileDataMap.put(ServerConstants.SERVICEBUREAUID,serviceBureauId);
			fileDataMap.put(ServerConstants.SERVICECODE,serviceType);
			fileDataMap.put(ServerConstants.SERVICEID, serviceId);

			/* Make a database call to get Agreement Id from DB */
			ServerDAO serverDAO = AbstractDAOFactory.getDefaultDAOFactory().getServerDAO();
			Map<String, String> dataMap = (Map<String, String>) serverDAO.getServiceIdInfo(fileDataMap);

			if (((String) dataMap.get(ServerConstants.ERROR_CODE)) == null) {
				agmtId = (String) dataMap.get(ServerConstants.AGREEMENTTID);
				logger.debug("getFeedbackAndOtherFilePath -> Agmt Id ->"+agmtId);
				fileDataMap.put(ServerConstants.AGREEMENTTID,agmtId);

				/* The file path has been change according to new dir structure */

				agmtServicePath = FTPFileUtil.getPatuAgmtServiceFilePath(fileDataMap);
				logger.debug("AgmtServiceCodePath=>"+ agmtServicePath);
			} else {
				logger.debug("Error Code for AGMT ID=>" + dataMap.get(ServerConstants.ERROR_CODE));
				logger.debug("Error Message for AGMT ID=>" + dataMap.get(ServerConstants.ERROR_MSG));
			}
		} else
			agmtServicePath = (String) session.getAttribute(ServerConstants.AGMTSERVICECODEPATH);
		
		logger.debug("::::: getFeedbackAndOtherFilePath :::: agmtServicePath ::"+agmtServicePath);
		
		/*
		 * It is of type retrieval type This file will be present
		 * inside the data path which is -> ServiceBureauId/AgmtId/ServiceType/ServiceId/date. 
		 */
		
		String feedbackKey = (String) session.getAttribute(ServerConstants.AGMTSERVICECODEPATH);
		
		String feedbackMessageKey = feedbackKey + ServerConstants.FEEDBACK_MSG;
		String feedbackPathKey = feedbackKey + ServerConstants.FEEDBACK_PATH;
		
		logger.debug("@:::: FeedBack - PATH - KEY ::: feedbackPathKey :: "+feedbackPathKey);
		
		
		String feedBackFileContent = (String) session.getAttribute(feedbackMessageKey);
		String feedBackFilePath = (String) session.getAttribute(feedbackPathKey);
		
		logger.debug("@:::: FeedBack - PATH - VALUE ::: feedBackFilePath :: "+feedBackFilePath);
		logger.debug("@:::: FeedBack - CONTENT - VALUE ::: feedBackFileContent :: "+feedBackFileContent);
		
		agmtServicePath = feedbackKey;
		
		if (agmtServicePath == null)
			throw new NullPointerException("AgreementService file path is null");
		
		if(agmtServicePath.lastIndexOf("data")!= -1)
			agmtServicePath =   System.getProperty("user.dir") + File.separator + agmtServicePath.substring(agmtServicePath.lastIndexOf("data"));;

		File agmtServiceDir = new File(agmtServicePath);
			
		if (!agmtServiceDir.exists()) {
			logger.debug(":::: getFeedbackFilePath ::: AgmtServicePath does not EXISTS....");
			
				if(agmtServiceDir.mkdirs())
					logger.debug(":::: getFeedbackFilePath ::: AgmtServicePath DIR's creation SUCCEED..!!");
				else 
					logger.debug(":::: getFeedbackFilePath ::: AgmtServicePath DIR's creation FAILED..!!");
			
			FTPFileUtil.setRights(agmtServiceDir);
		}
		
		logger.debug(":::: getFeedbackFilePath ::: agmtServicePath :: " + agmtServicePath);	
		
		if(feedBackFilePath.lastIndexOf("data")!= -1)
			feedBackFilePath =   System.getProperty("user.dir") + File.separator + feedBackFilePath.substring(feedBackFilePath.lastIndexOf("data"));;
		
		logger.debug(":::: getFeedbackFilePath ::: feedBackFilePath :: " + feedBackFilePath);	
			
		FTPFileUtil.writeContents(feedBackFilePath, feedBackFileContent);
				
//				
//		logger.info("Agmt Service Path-------" + agmtServicePath);
		String reqFileName = agmtServicePath + File.separator + fileName;
//		logger.info("Requested file name-----" + reqFileName);
//		String existingFileName = "U_" + fileTypeName;
		File existingFile = new File(feedBackFilePath);
		Map auditLogMap = new HashMap();
		OpeEjbRemote remote = PATUEjbClass.getEjbRemoteObject();
		
		if (existingFile.exists()) {
			logger.debug("Feedback file exists.... Location :-"+ feedBackFilePath);
		
			/*
			 * First rename the file to P
			 */
		/*
			String renamedPath = agmtServicePath + File.separator + fileTypeName + "_P";
			logger.debug("renamedPath ----->"+renamedPath);
			File renFile = new File(renamedPath);
			
			if (renFile.exists()) {
				logger.debug("renFile exists so delete the file------");
				renFile.delete();
			}
			
			logger.debug("renFile ----> getAbsolutePath() -->"+renFile.getAbsolutePath());
			logger.debug("existingFile ----> getAbsolutePath() -->"+existingFile.getAbsolutePath());
			
			if(existingFile.renameTo(renFile))
				logger.debug("Existing File has been renamed successfully..");
			else
				logger.debug("Existing File could not be renamed..");
			
			/*
			 * Copy the contents to the requested file name
			 */
			FTPFileUtil.copyFile(existingFile.getAbsolutePath(), reqFileName);
			auditLogMap = AuditLogger.getFeedbackLogMap(session,fileName, reqFileName, SecurityMessageConstants.OUTGOING);
			remote.updateAuditLog(auditLogMap);
			
		} else {
			/*
			 * Create an error file
			 */
			String errorMsg = getErrorMessage(trBean);
			logger.info("Error Message-----" + errorMsg);
			logger.debug("Error Message in FileRetriever-----"+ errorMsg);
			FTPFileUtil.writeContents(reqFileName, errorMsg);
			auditLogMap = AuditLogger.getFeedbackLogMap(session, fileName, reqFileName, SecurityMessageConstants.OUTGOING);
			remote.updateAuditLog(auditLogMap);
		}
		
		return reqFileName;
	}

	private static String getESIpFilePath(String fileName, Session session) {

		logger.debug("getFilePath :: getESIpFilePath -> fileName ->"+fileName);
		String path ="";		
		String filePathFromSession = (String) session.getAttribute(ServerConstants.ESIP_PATH);
		String esipFileContent = (String) session.getAttribute(ServerConstants.ESIP_MSG);

		logger.debug("getFilePath :: getESIpFilePath -> filePathFromSession ->"+filePathFromSession);
		
		if (filePathFromSession == null || esipFileContent == null)
			throw new NullPointerException("FilePath is null");
		
		if(filePathFromSession.lastIndexOf("home")!= -1)
			path =  System.getProperty("user.dir") + File.separator + filePathFromSession.substring(filePathFromSession.lastIndexOf("home"));
		
		logger.info("ESIp_file retriever availableFilePath-->" + path);
		FTPFileUtil.writeContents( path, esipFileContent);
		
		String requestFilePath = FTPFileUtil.getSessionTempPath(session,fileName);
		if (requestFilePath == null)
			throw new NullPointerException("Requested filePath is null");
		
		logger.debug("ESIp_file retriever requestedFilePath-->"+ requestFilePath);
		FTPFileUtil.copyFile(path, requestFilePath);

		return requestFilePath;
	}
	
	private static String getRetrievableFilePath(Session session, int retrievalStatus) {
		
		logger.debug("-------- I'm inside getRetrievableFilePath ---------");
		System.out.println("-------- I'm inside getRetrievableFilePath ---------");
		
		String requestFilePath = null;
		String path = null;
		Map map = new HashMap();
		try {
			String serviceBureauId = (String) session.getAttribute(ServerConstants.SERVICEBUREAUID);
			String serviceType = (String) session.getAttribute(ServerConstants.SERVICECODE);
			String serviceId = (String) session.getAttribute(ServerConstants.SERVICEID);
			
			/* 	
			 * 	Now we have to file the ServiceType folder inside serviceBureauId
			 *	Ex - data\IDEAL003\DNB003001\TITO\SAUTEST001\081126
			 *	DATA-DIR\ServiceBureauId\AgreementId\serviceType\serviceId\date\<<file-name>>_U
			*/
			
			String rootPath = FTPFileUtil.getDataDirPath();
			
			OpeEjbRemote remote = PATUEjbClass.getEjbRemoteObject();
			Map retrievableFileData = new HashMap();
			
			retrievableFileData.put(ServerConstants.SERVICEBUREAUID,serviceBureauId);
			retrievableFileData.put(ServerConstants.SERVICECODE,serviceType);
			retrievableFileData.put(ServerConstants.SERVICEID,serviceId);
			retrievableFileData.put(ServerConstants.RETRIEVAL_STATUS,retrievalStatus);
			
			
			map = remote.getRetrievableFile(retrievableFileData);
			
			String ftpFileContent = (String) map.get(ServerConstants.MERGED_FILE_CONTENT);
			String ftpFileName = (String) map.get(ServerConstants.MERGED_FILE_NAME);
//			String ftpFileDestination = (String) map.get(ServerConstants.MERGED_FILE_FTP_PATH);
			
			if( (ftpFileContent==null) || (ftpFileContent==null) ){
				
				ftpFileName = new StringBuffer().append(serviceBureauId).append("_").append(serviceType).append("_")
				.append(ServerConstants.STATUS_M).toString();
				TransferRequestBean trBean = new TransferRequestBean();
				trBean.setFileType(serviceType);
				ftpFileContent = getErrorMessage(trBean);
				path = System.getProperty("user.dir") + File.separator + ServerConstants.DATA_DIR_NAME + File.separator + ftpFileName;
				FTPFileUtil.writeContents(path, ftpFileContent);		
				logger.debug("----| FtpFileContent ----|-->"+ftpFileContent);
				System.out.println("----| FtpFileContent ----|-->"+ftpFileContent);
				logger.debug("----| FtpFileName -------|-->"+ftpFileName);
				System.out.println("----| FtpFileName -------|-->"+ftpFileName);
				logger.debug("----| Path --------------|-->"+path);
				System.out.println("----| Path --------------|-->"+path);
				
			} else {
				path = System.getProperty("user.dir") + File.separator + ServerConstants.DATA_DIR_NAME + File.separator + ftpFileName;
				FTPFileUtil.writeContents(path, ftpFileContent);
				
				logger.debug("----| FtpFileContent ----|-->"+ftpFileContent);
				System.out.println("----| FtpFileContent ----|-->"+ftpFileContent);
				logger.debug("----| FtpFileName -------|-->"+ftpFileName);
				System.out.println("----| FtpFileName -------|-->"+ftpFileName);
				logger.debug("----| Path --------------|-->"+path);
				System.out.println("----| Path --------------|-->"+path);
			}
		}
		catch (Exception e) {
			logger.debug(":::: RetrievableFile :: Caught in Exception : "+e.getMessage());
		}
		return path;
	}

	private static String getPTEFilePath(Session session, String fileName) {
		String requestFilePath = null;
		
		logger.debug("Inside getPTEFilePath ::: fileName :: "+ fileName);
		String path = "";
		String filePathFromSession = (String) session.getAttribute(ServerConstants.PTE_PATH);
		String pteFileContent = (String) session.getAttribute(ServerConstants.PTE_MSG);
		
		logger.debug(":::: getPTEFilePath ::: filePathFromSession :: "+ filePathFromSession);
		logger.debug(":::: getPTEFilePath ::: pteFileContent :: "+ pteFileContent);
		
		if ( (filePathFromSession==null) || (pteFileContent==null) )
			throw new NullPointerException("FilePath is null");
		
		if(filePathFromSession.lastIndexOf("home")!= -1)
			path =  System.getProperty("user.dir") + File.separator + filePathFromSession.substring(filePathFromSession.lastIndexOf("home"));
		
		logger.debug(":::: getPTEFilePath ::: path :: "+ path);
		
		FTPFileUtil.writeContents( path, pteFileContent);
		
		requestFilePath = FTPFileUtil.getSessionTempPath(session, fileName);
		
		FTPFileUtil.copyFile(path, requestFilePath);
		logger.debug(":::: getPTEFilePath ::: requestFilePath :: "+ requestFilePath);
		
		TransferRequestBean trBean = (TransferRequestBean) session.getAttribute(ServerConstants.TR_OBJECT);
		logger.debug(":::: getPTEFilePath ::: TR-FileType :: "+ trBean.getFileType());
		
		if(! new File(path).exists())
		{
			String errorMessage = getErrorMessage(trBean);
			FTPFileUtil.writeContents( requestFilePath, errorMessage);
		}
		
		return requestFilePath;
	}
	public static String getErrorMessage(TransferRequestBean trBean) {
		String errorMsg = null;
		try {
			String recordId = "*";
			String blank1 = " ";
			String date = DateUtil.getDate("dd.MM");
			logger.debug("In case of Error message, File Retriever Date-->"+ date);
			String blank2 = " ";
			String time = DateUtil.getDate("HH:mm");
			logger.debug("In case of Error message, File Retriever Time-->"+ time);
			String blank3 = " ";
			//Modified by Debadatta Mishra at onsite to fix the issue related to error message
			String fileType = CommonUtil.pad(trBean.getFileType(), 10, " ");//trBean.getFileType();
			String blank4 = " ";
			String notificationCode = "003";
			String blank5 = " ";
//			String notificationText = CommonUtil.pad("Requested file is no longer available", 50, " ");
			
			String notificationText = CommonUtil.pad("Requested material not available", 50, " ");
			
			logger.debug("NotificationText------>>>"+notificationText);

			errorMsg = new StringBuilder().append(recordId).append(blank1)
					.append(date).append(blank2).append(time).append(blank3)
					.append(fileType).append(blank4).append(notificationCode)
					.append(blank5).append(notificationText).toString();
		} catch (NullPointerException npe) {
			logger.error("NullPointerException thrown" + npe.getMessage());
		} catch (Exception e) {
			logger.error("Other Exception thrown" + e.getMessage());
		}
		logger.debug("errorMsg------->>>"+errorMsg);
		return errorMsg;
	}
	
}