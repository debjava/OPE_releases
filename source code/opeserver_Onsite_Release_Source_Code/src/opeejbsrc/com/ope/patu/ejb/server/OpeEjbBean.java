package com.ope.patu.ejb.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import org.apache.log4j.Logger;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import com.coldcore.coloradoftp.factory.ObjectFactory;
import com.coldcore.coloradoftp.factory.ObjectName;
import com.coldcore.coloradoftp.factory.impl.SpringFactory;
import com.coldcore.coloradoftp.session.Session;
import com.ope.patu.exception.MessageHandlingException;
import com.ope.patu.handler.AbstractHandler;
import com.ope.patu.handler.MessageHandler;
import com.ope.patu.server.beans.MessageBean;

import com.ope.patu.server.beans.TransferRequestBean;
import com.ope.patu.server.constant.ServerConstants;
import com.ope.patu.server.db.AuditLogger;
import com.ope.patu.util.CommonUtil;
import com.ope.patu.util.DateUtil;
import com.ope.patu.util.FileUtil;
import com.ope.patu.util.MergeFile;
import com.ope.patu.wrapper.MessageMap;
import com.ope.patu.wrapper.TransferMap;

public class OpeEjbBean implements SessionBean
{
	protected static Logger logger = Logger.getLogger(OpeEjbBean.class);
	
	public void ejbActivate() 
	{
		logger.debug("EJBActivate");
	}
	public void ejbPassivate() {
		logger.debug("EJBPassivate");
	}
	public void ejbRemove() {
		logger.debug("EJBRemove");
	}
	public void setSessionContext(SessionContext a) {
		logger.debug("EJBSessionContext");
	}
	public void ejbCreate() {
		logger.debug("EJBCreate");
	}

	private String[] s1={"OPE-TEST1", "OPE-TEST2", "OPE-TEST3"};
	public String getAdvice()
	{
		int r1=(int)(Math.random()*s1.length);
		return s1[r1];
	}
	
	public String getFTPSession(Session session)	{
		// Colarado Session
		String value = null;
		Set set = session.getAttributeNames();
		Iterator it = set.iterator();
		while (it.hasNext()) {
			value = (String) it.next();
//			logger.debug("SESSION ATTRIBUTE NAME ->	: "+ value);
//			logger.debug("SESSION ATTRIBUTE VALUE ->	: "+ session.getAttribute(value));
		}
		
		String setValue = (String) session.getAttribute("opeUserName");
		return setValue;
		
	}

	public String getMyFTPMap(Map map) {
		String key = null;
		Set set = map.keySet();
		
		if(map!=null){
			logger.debug("TestBean : getMyFTPMap : Map.Size -> " + map.size());
		}
		Iterator it = set.iterator();
		while(it.hasNext()) {
			key = (String) it.next();
			logger.debug(map.get(key));
			logger.debug("TestBean : session -> key : " +key + " :: value : " + map.get(key));
		}
		return key;
	} 
	
	public Map doWrapFTPSessionMap(Map sessionMap, String fileName) throws FileNotFoundException, IOException {
		
//		InputStream fis =  ClassLoader.getSystemClassLoader().getResourceAsStream("ope-setup.properties");
		
		logger.debug("Inside doWrapFTPSessionMap");
		
		InputStream is = null;
		String beanFile = null;
		String sessionId = null;
		Map returnedMap = new HashMap();
		
		try {
		is = OpeEjbBean.class.getResourceAsStream("/ope-setup.properties");
		
		String confLocation = null;
		
		
		
		//String beanFile = "beans.xml";
		Properties prop = new Properties();
		
		prop.load(is);
//		prop.load(new FileInputStream("ope-setup.properties"));
		
		confLocation = prop.getProperty("ope.conf.location");
		
		beanFile = confLocation + File.separator + "beans.xml";
		} catch(Exception e){
			e.printStackTrace();
			logger.debug("Exception :: "+ e.getMessage());
		}
		logger.debug("Bean File Location :: "+ beanFile);
		  
		File file = new File(beanFile);
		
	    if (!file.exists()) {
	      logger.debug("Configuration file not found, terminating...");
	      logger.info("Configuration file not found, terminating...");
	    }

	    try {
	      Resource resource = new FileSystemResource(file);
	      ObjectFactory.setInternalFactory(new SpringFactory(resource));
	    } catch (Throwable e) {
	      logger.debug("Cannot initialize object factory, terminating...");
	      logger.info("Cannot initialize object factory, terminating...");
	      e.printStackTrace();
	    } 
	    logger.debug("Object factory initialized");
		logger.debug("session.id -> "+sessionMap.get("session.id"));
	    sessionId = sessionMap.get("session.id").toString();
	    
		try {
			
			Session session = (Session) ObjectFactory.getObject(ObjectName.SESSION);
		
			if(session != null)
			{
				logger.debug("Session is created successfully..!!");
				session.setSessionId(Integer.parseInt(sessionMap.get("session.id").toString()));
			
				/*	Method to set session map data into PATU session Object  */
				setEJBServerSessionObject(session,sessionMap);
			
				/*	Method to Call Patu Logic  */
				callPATUHandler(fileName, session);
			
				/*	Method to set PATU session Object into HashMap */
				returnedMap =  setEJBClientSessionMap(session);
			
			} else {
				logger.debug("Sorry..!! Session could not create.");
				logger.info("Sorry..!! Session could not create.");
			}	
		}catch (Exception e) {
		logger.debug("Caught in Exception ::"+e.getMessage());
		logger.info("Caught in Exception ::"+e.getMessage());
		e.printStackTrace();
		}
	return returnedMap;
	}
	
	/**
	 * Code added by anandkumar.b
	 * 
	 */
	
	public Map moveFtpToPatu(Map mapFtpToPatu) {
		Map<String, String> returnToFtp = new LinkedHashMap<String, String>();
		String  fileName = (String)mapFtpToPatu.get("FILE_NAME");
		String  filePath = (String)mapFtpToPatu.get("FILE_PATH");
		String  fileContent = (String)mapFtpToPatu.get("FILE_CONTENT");
		logger.debug("@@ FtpToPatu @@ fileName :::::::::"+fileName);
		logger.debug("@@ FtpToPatu @@ filePath :::::::::"+filePath);
		logger.debug("@@ FtpToPatu @@ fileContent ::::::"+fileContent);
				
		filePath = FileUtil.getHomeDirPath() + File.separator + filePath ;
		
		logger.debug("EJB-SERVER ::::::"+filePath);
		
		File homeDirfile = new File(filePath);
		if (!homeDirfile.exists())
			homeDirfile.mkdirs();
		new FileUtil().setRights(homeDirfile);
		
		filePath = filePath + File.separator + fileName;
		
		FileUtil.writeContents(filePath, fileContent);
		
		returnToFtp.put("MSG", "File moved success fully."); 
		return returnToFtp;
	}
	
		
	public static Object callPATUHandler(String filename, Session session) throws MessageHandlingException  {
		
		logger.debug("callPATUHandler :: filename : "+filename);
		Object obj = null;
		MessageHandler messageHandler = AbstractHandler.getMessageObject(filename,session);
				
		if( messageHandler != null )	{
			
			/** proceed for validation **/
			
			messageHandler.handleObject(filename, session,AbstractHandler.msgTypeName,AbstractHandler.paymentFileName);
		} else {
			logger.debug(" Message Handler is NULL .....");
		}

		return session;
	}
	
	
	public static void setEJBServerSessionObject(Session session, Map sessionMap) throws MessageHandlingException  {
		
		Set<Map.Entry<String, Object>>  set = sessionMap.entrySet();

	    for (Map.Entry<String, Object>  me : set) {
	    	String key = (String) me.getKey();
		
			if(key.equals("ESIA_MSG_BEAN")) {
				 
				 HashMap updatedESIaMap = (HashMap) sessionMap.get("ESIA_MSG_BEAN");
				 MessageBean esiABean = (MessageBean) new MessageMap().getMessageBean(updatedESIaMap);
				 session.setAttribute("ESIA_MSG_BEAN", esiABean);
		
			} else if(key.equals("VARP_MSG_BEAN")) {
			
				 HashMap updatedVARpMap = (HashMap) sessionMap.get("VARP_MSG_BEAN");
				 MessageBean varABean = (MessageBean) new MessageMap().getMessageBean(updatedVARpMap);
				 session.setAttribute("VARP_MSG_BEAN", varABean);
				 
			 } else if(key.equals("SUOP_MSG_BEAN")) {
		 
				 HashMap updatedSUOpMap = (HashMap) sessionMap.get("SUOP_MSG_BEAN");
				 MessageBean suoPBean = (MessageBean) new MessageMap().getMessageBean(updatedSUOpMap);
				 session.setAttribute("SUOP_MSG_BEAN", suoPBean);
				 
			} else if(key.equals("ESIP_MSG_BEAN")) {
				 
				 HashMap updatedESIpMap = (HashMap) sessionMap.get("ESIP_MSG_BEAN");
				 MessageBean esiPBean = (MessageBean) new MessageMap().getMessageBean(updatedESIpMap);
				 session.setAttribute("ESIP_MSG_BEAN", esiPBean);
				 
			 } else if(key.equals("SUOA_MSG_BEAN")) {
				 
				 HashMap updatedSUOaMap = (HashMap) sessionMap.get("SUOA_MSG_BEAN");
				 MessageBean suoABean = (MessageBean) new MessageMap().getMessageBean(updatedSUOaMap);
				 session.setAttribute("SUOA_MSG_BEAN", suoABean);
				 
			 } else if(key.equals("TRANSFER_REQUEST_OBJECT")) {
		 
				 HashMap updatedTRMap = (HashMap) sessionMap.get("TRANSFER_REQUEST_OBJECT");
				 TransferRequestBean trBean = (TransferRequestBean) new TransferMap().getTransferBean(updatedTRMap);
				 session.setAttribute("TRANSFER_REQUEST_OBJECT", trBean);
				 
			 } else {
				 session.setAttribute(key,sessionMap.get(key));
			 }
		}
	}
	
	public static Map setEJBClientSessionMap(Session session) throws MessageHandlingException  {
		
		Map sessionMap = new HashMap();
		
		logger.debug("Inside makeEJBClientSessionMap");
		
		Set set = session.getAttributeNames();
		Iterator it = set.iterator();
		
		while(it.hasNext()) {
			String key = (String) it.next();
			sessionMap.remove("data.connection.channel");
			 
			 if(key.equals("ESIA_MSG_BEAN")) {
				 MessageBean esiABean = new MessageBean();
//				 logger.debug(" session.getAttribute(key) :: "+ session.getAttribute(key));
				 esiABean = (MessageBean) session.getAttribute(key);
				 Map updatedESIaMap = (HashMap) new MessageMap().getMessageMap(esiABean);
				 sessionMap.put("ESIA_MSG_BEAN", updatedESIaMap);
			 
			 } else if(key.equals("VARP_MSG_BEAN")) {
				 MessageBean varABean = new MessageBean();
				 varABean = (MessageBean) session.getAttribute(key);
				 Map updatedVARaMap = (HashMap) new MessageMap().getMessageMap(varABean);
				 
				 sessionMap.put("VARP_MSG_BEAN", updatedVARaMap);
				 
			 } else if(key.equals("SUOP_MSG_BEAN")) {
				 MessageBean suoABean = new MessageBean();
				 suoABean =  (MessageBean) session.getAttribute(key);
				 Map updatedSUOaMap = (HashMap) new MessageMap().getMessageMap(suoABean);
				 
				 sessionMap.put("SUOP_MSG_BEAN", updatedSUOaMap);
				 
			 } else if(key.equals("ESIP_MSG_BEAN")) {
				 MessageBean esiPBean = new MessageBean();
				 esiPBean = (MessageBean) session.getAttribute(key);
				 Map updatedESIpMap = (HashMap) new MessageMap().getMessageMap(esiPBean);
				 
				 sessionMap.put("ESIP_MSG_BEAN", updatedESIpMap);
				 
			} else if(key.equals("SUOA_MSG_BEAN")) {
				 MessageBean suoABean = new MessageBean();
				 suoABean = (MessageBean) session.getAttribute(key);
				 Map updatedSUOaMap = (HashMap) new MessageMap().getMessageMap(suoABean);
				 
				 sessionMap.put("SUOA_MSG_BEAN", updatedSUOaMap);
				 
			 } else if(key.equals("TRANSFER_REQUEST_OBJECT")) {
				 
				 TransferRequestBean trBean = new TransferRequestBean();
				 trBean = (TransferRequestBean) session.getAttribute(key);
				 Map updatedTRMap = (HashMap) new TransferMap().getTransferMap(trBean);
				 
				 sessionMap.put("TRANSFER_REQUEST_OBJECT", updatedTRMap);
				 
			  } else {
				  sessionMap.put(key, session.getAttribute(key));
			  }
		}
		return sessionMap;
	}
	
	public Map getRetrievableFile(Map retrievableFileData) {
		

		String requestFilePath = null;
		Map map = new HashMap();
		try {
		String serviceBureauId = (String) retrievableFileData.get(ServerConstants.SERVICEBUREAUID);
		String serviceType = (String) retrievableFileData.get(ServerConstants.SERVICECODE);
		String serviceId = (String) retrievableFileData.get(ServerConstants.SERVICEID);
		int retrievalStatus = Integer.parseInt(retrievableFileData.get(ServerConstants.RETRIEVAL_STATUS).toString());
//		String fileType = (String) retrievableFileData.get(ServerConstants.FILE_TYPE);
		
		/* 	
		 * 	Now we have to file the ServiceType folder inside serviceBureauId
		 *	Ex - data\IDEAL003\DNB003001\TITO\SAUTEST001\081126
		 *	DATA-DIR\ServiceBureauId\AgreementId\serviceType\serviceId\date\<<file-name>>_U
		*/ 
		
		String rootPath = FileUtil.getDataDirPath();
		map = MergeFile.getFile(rootPath, serviceType, serviceBureauId, serviceId, null, retrievalStatus);
		requestFilePath = (String) map.get(ServerConstants.MERGED_FILE_PATH);
			if((!new File(requestFilePath).exists()))	{ 
				String errorMsg = getErrorMessage(serviceType);
				logger.info("Error Message-----" + errorMsg);
				logger.debug("Error Message in FileRetriever-----"+ errorMsg);
				FileUtil.writeContents(requestFilePath,	errorMsg);
				map.put(ServerConstants.MERGED_FILE_CONTENT, errorMsg);
			}
		} catch (Exception e) {
			logger.error("Exception :: getRetrievableFilePath "+e.getMessage());
			logger.debug("Exception :: getRetrievableFilePath "+e.getMessage());
		}
		return map;
	}
	
	public static String getErrorMessage(String serviceType) {
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
			String fileType = CommonUtil.pad(serviceType, 10, " ");//trBean.getFileType();
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
	
	public void updateAuditLog(Map auditLogMap) {
		AuditLogger.storeAuditLog(auditLogMap);
	}
}