package com.ope.patu.messages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.log4j.Logger;
import com.coldcore.coloradoftp.session.Session;
import com.ope.patu.exception.DatabaseException;
import com.ope.patu.exception.FailedValidationException;
import com.ope.patu.exception.MessageHandlingException;
import com.ope.patu.exception.MessageParsingException;
import com.ope.patu.server.db.AuditLogger;
import com.ope.patu.handler.MessageHandler;
import com.ope.patu.handler.Parser;
import com.ope.patu.handler.Validator;
import com.ope.patu.handler.ValidatorFactory;
import com.ope.patu.parsers.SUOMessageParser;
import com.ope.patu.payment.core.AbstractProcessor;
import com.ope.patu.payment.core.PaymentProcessor;
import com.ope.patu.server.beans.MessageBean;
import com.ope.patu.server.beans.TransferRequestBean;
import com.ope.patu.server.constant.SecurityMessageConstants;
import com.ope.patu.server.constant.ServerConstants;
import com.ope.patu.server.db.AbstractDAOFactory;
import com.ope.patu.server.db.ServerDAO;
import com.ope.patu.util.CommonUtil;
import com.ope.patu.util.DateUtil;
import com.ope.patu.util.FileUtil;

public class SUOMessageHandler implements MessageHandler {
	private String errorMessage = "";

	private String acceptanceCode;

	private String pteMsgString;

	private String announcementCode = null;

	Properties languageProp = new Properties();

	protected static Logger logger = Logger.getLogger(SUOMessageHandler.class);

	public Object handleObject(Object... obj) throws MessageHandlingException {
		boolean validationFlag = false;
		String fileName = (String) obj[0];
		Session session = (Session) obj[1];
		String msgTypeName = (String) obj[2];
		String paymentFileName = (String) obj[3];
		boolean trFileFlag = false; 
		try {
			languageProp = (Properties) session
					.getAttribute(ServerConstants.LANGUAGE_PROP);
			Boolean trMsgStatus = (Boolean) session.getAttribute(ServerConstants.TR_MSG_STATUS);
			Boolean esiMsgStatus = (Boolean) session.getAttribute(ServerConstants.ESI_MSG_STATUS);
			
			boolean trMsgStatusFlag = false;
			boolean esiMsgStatusFlag = esiMsgStatus.booleanValue();
			
			if(esiMsgStatusFlag) {
				
			
			if(trMsgStatus != null){
				trMsgStatusFlag = trMsgStatus.booleanValue(); 
			} 
			
			String trFileStatus = (String) session.getAttribute(ServerConstants.TR_FILE_STATUS);
			
			if( trFileStatus!=null ) {
				
				if(trFileStatus.equals(ServerConstants.STATUS_YES)) {					
					logger.debug("-> Payment File with TR FILE");
					trFileFlag = false;
				}
			} else {
				logger.debug("-> Payment File without TR and Payment File Name -->"+paymentFileName);
				TransferRequestBean trBean = new TransferRequestBean();
				trBean.setFileType(paymentFileName);
				session.setAttribute(ServerConstants.TR_OBJECT,trBean);
				session.setAttribute(ServerConstants.SERVICECODE, paymentFileName);
				trFileFlag = true;
			}
						
			MessageBean msgBean = (MessageBean) session.getAttribute(ServerConstants.ESIA_MSG_BEAN);
			String esiPatuId = msgBean.getSenderId();
			/*
			 * Make database validation whether customer has agreed on the
			 * validation of SUO and VAR message.
			 */
			if (trMsgStatusFlag || trFileFlag) {
				
				/*
				 * Go ahead for the validation of Actual bill payment files
				 */
				String filePath = FileUtil.getSessionTempPath(session, fileName);
				TransferRequestBean trBean = (TransferRequestBean) session.getAttribute(ServerConstants.TR_OBJECT);
				String serviceBureauId = (String) session.getAttribute(ServerConstants.SERVICEBUREAUID);
				logger.debug("-----@@@@ Service Bureau Id -->"+serviceBureauId);
				Map<String, String> messageMap = FileUtil.getGenericMsg(filePath);

				/*
				 * Obtain the service Id for the payment file and keep this
				 * inside the session.
				 */
				Map serviceIdData = getServiceIdDataFromFile(messageMap,
						session);
				String serviceId = (String) serviceIdData.get(ServerConstants.SERVICEID);
				
				if(!(serviceId==null))
					serviceId = serviceId.trim();
				
				session.setAttribute(ServerConstants.SERVICEID, serviceId);

				String actualDataContents = (String) messageMap
						.get(SecurityMessageConstants.DATA_MSG);

				messageMap.putAll(serviceIdData);

				/*
				 * Condition -> Obtain the SUO/VAR protection flag from the
				 * database If - YES -> Perfrom SUO/VAR validation and then move
				 * onto file validation. If - No -> Perform only file validation
				 */

				if (isProtectionEnabled(serviceBureauId, trBean.getFileType(),
						serviceId)) {
					logger.debug("Processing SUO Message with protection->");
					processWithProtection(messageMap, session, msgTypeName,
							fileName, filePath);
				} else {
					// Only go for actual bill payment data
					logger.debug("Processing SUO Message without protection->");
					processWithoutProtection(messageMap, session, fileName,
							filePath);
				}
			}
			} else {
				logger.debug("ESI message validation status flag -->>"+esiMsgStatusFlag);
			}

		} catch (NullPointerException npe) {
			npe.printStackTrace();
			logger.error(npe);
		} catch (Exception mhe) {
			logger.error(mhe.getMessage());
			throw new MessageHandlingException();
		}

		return new Boolean(validationFlag);
	}

	/**
	 * Get the service id for the appropriate file by parsing the contents. This
	 * delegates the task to the processor for each service type
	 * 
	 * @param messageMap.
	 *            Map which holds the message contents
	 * @param session.
	 *            Session object
	 * @return Map. Service id and Payers Business Identity Code for the file
	 */
	private Map getServiceIdDataFromFile(Map messageMap, Session session) {
		Map serviceIdData = null;

		String actualDataContents = (String) messageMap
				.get(SecurityMessageConstants.DATA_MSG);
		TransferRequestBean trBean = (TransferRequestBean) session
				.getAttribute(ServerConstants.TR_OBJECT);
		PaymentProcessor processor = AbstractProcessor.getProcessor(trBean
				.getFileType().trim());
		serviceIdData = (HashMap) processor.getServiceIdData(
				actualDataContents, session);

		return serviceIdData;
	}

	private void processWithoutProtection(Object... objects) {
		Map<String, String> protectedMsgMap = (Map<String, String>) objects[0];
		Session session = (Session) objects[1];
		String fileName = (String) objects[2];
		String filePath = (String) objects[3];

		String actualDataContents = protectedMsgMap
				.get(SecurityMessageConstants.DATA_MSG);
		TransferRequestBean trBean = (TransferRequestBean) session
				.getAttribute(ServerConstants.TR_OBJECT);
		PaymentProcessor processor = AbstractProcessor.getProcessor(trBean
				.getFileType().trim());
		processor.process(actualDataContents, session);
		/*
		 * Maintain the audit log for the payment file.
		 */
		AuditLogger.logPaymentMsg(session, fileName, filePath,
				SecurityMessageConstants.INCOMING);
	}

	private boolean processWithProtection(Map protectedMsgMap, Session session,
			String msgTypeName, String fileName, String filePath) {

		boolean validationFlag = false;
		try {
			String suoMsgContents = (String) protectedMsgMap
					.get(SecurityMessageConstants.SUO_MSG);
			logger.debug("SUO MSG Contents->" + suoMsgContents);
			String varMsgContents = (String) protectedMsgMap
					.get(SecurityMessageConstants.VAR_MSG);
			logger.debug("VAR MSG Contents->" + varMsgContents);
			String actualDataContents = (String) protectedMsgMap
					.get(SecurityMessageConstants.DATA_MSG);
			logger.debug("actualDataContents->" + actualDataContents);

			/* Get the agreement Id from the database */

			// String agreementId = getAgmtId(session, actualDataContents);
			String agreementId = getAgreementId(session);

			if (agreementId == null)
				throw new NullPointerException("Agreement ID is null");
			// Store the agreemtnId in the session

			session.setAttribute(ServerConstants.AGREEMENTTID, agreementId);
			logger.debug("Agreement ID in SUO message handler->" + agreementId);

			Validator validator = ValidatorFactory.getValidator(msgTypeName);
			/*
			 * First validate the SUO message
			 */
			boolean suoValdnFlag = validator.validate(suoMsgContents, fileName,
					session, actualDataContents);
			logger.debug("SUO validation--------" + suoValdnFlag);
			MessageBean suoBean = (MessageBean) validator.getValidatedObject();
			/*
			 * Validate the VAR message
			 */
			validator = ValidatorFactory
					.getValidator(SecurityMessageConstants.VAR);
			boolean varValdnFlag = validator.validate(varMsgContents, fileName,
					session, actualDataContents);
			logger.debug("VAR validation--------" + varValdnFlag);
			MessageBean varBean = (MessageBean) validator.getValidatedObject();
			/*
			 * Common validations for both SUO and VAR message
			 */
			boolean commonValdnFlag = doCommonValidations(suoBean, varBean);
			logger.debug("Common SUO and VAR validation--------"
					+ commonValdnFlag);
			if (suoValdnFlag == true && varValdnFlag == true
					&& commonValdnFlag == true)
				validationFlag = true;
			else
				validationFlag = false;
			logger.debug("protected message validationFlag->" + validationFlag);
			if (validationFlag == true) {
				/*
				 * This is best condition where everything passed
				 */
				logger.debug("If validationFlag---------" + validationFlag);
				acceptanceCode = SecurityMessageConstants.ACCEPTANCE_CODE_YES;
				announcementCode = SecurityMessageConstants.MESSAGE_VALIDATION_PASS_CODE;
				errorMessage = DateUtil.getTime()
						+ " "
						+ languageProp
								.getProperty(SecurityMessageConstants.MESSAGE_VALIDATION_PASS_CODE);
				/*
				 * Maintain audit log for PTE message
				 */
				MessageBean pteBean = (MessageBean) varBean.clone();
				pteBean.setMessageCode(SecurityMessageConstants.PTE_MSG);
				AuditLogger.logSecurityMessage(session, null, null,
						SecurityMessageConstants.OUTGOING, pteBean, null);
				/*
				 * Go for actual data validation if everything is fine. Pass the
				 * actual data contents as string
				 * session.setAttribute(ServerConstants.PROTECTED_MSG_BEAN,
				 * validationFlag);
				 */
				TransferRequestBean trBean = (TransferRequestBean) session
						.getAttribute(ServerConstants.TR_OBJECT);
				logger.debug("Transfer Request File Type ->"
						+ trBean.getFileType());
				PaymentProcessor processor = AbstractProcessor
						.getProcessor(trBean.getFileType().trim());
				processor.process(actualDataContents, session);
				pteMsgString = getPTEMessage(pteBean, actualDataContents,
						session);
				/*
				 * Maintain the audit log for the payment file.
				 */
				AuditLogger.logPaymentMsg(session, fileName, filePath,
						SecurityMessageConstants.INCOMING);
			} else {
				/*
				 * 1.If SUO validation is false, pass SUO bean 2. If VAR
				 * validation is false pass VAR bean 3. If common validation
				 * fails form the error message
				 */
				acceptanceCode = SecurityMessageConstants.ACCEPTANCE_CODE_NO;
				announcementCode = SecurityMessageConstants.SYSTEM_ERROR_CODE;
				errorMessage = DateUtil.getTime()
						+ " "
						+ languageProp
								.getProperty(SecurityMessageConstants.SYSTEM_ERROR_CODE);
				;
				if (!suoValdnFlag) {
					MessageBean msgBean = (MessageBean) suoBean.clone();
					msgBean.setMessageCode(SecurityMessageConstants.PTE_MSG);
					msgBean.setHash_value(varBean.getHash_value());
					/*
					 * Check the Key change of the ESI message.
					 */
					MessageBean esiABean = (MessageBean) session
							.getAttribute(ServerConstants.ESIA_MSG_BEAN);
					manageKeyChange(msgBean, esiABean.getKey_change());
					pteMsgString = getPTEMessage(msgBean, actualDataContents,
							session);
					logger.debug("PTE MESSAGE in case SUO validation fails===="
							+ pteMsgString);
				} else if (!varValdnFlag) {
					MessageBean msgBean = (MessageBean) varBean.clone();
					msgBean.setMessageCode(SecurityMessageConstants.PTE_MSG);
					pteMsgString = getPTEMessage(msgBean, actualDataContents,
							session);
					logger.debug("PTE MESSAGE in case VAR validation fails===="
							+ pteMsgString);
				}
			}
			logger.debug("PTE MSG String-------" + pteMsgString);
			/*
			 * Write the PTE message to a file
			 */
			filePath = FileUtil.getSessionTempPath(session,
					SecurityMessageConstants.PTE_TEXT_FILE);
			FileUtil.writeContents(filePath, pteMsgString);
			session.setAttribute(ServerConstants.PTE_PATH, filePath);
			session.setAttribute(ServerConstants.PTE_MSG, pteMsgString);
		} catch (NullPointerException npe) {
			npe.printStackTrace();
			logger.error(npe);
		} catch (FailedValidationException fve) {
			fve.printStackTrace();
			logger.error(fve);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}
		return validationFlag;
	}

	private String getPTEMessage(MessageBean msgBean,
			String actualDataContents, Session session) 
	{
		logger.debug("AUK value in VAR for forming the PTE----------"
				+ msgBean.getAut_value());
		logger.debug("MessageBean softwate --------->>>"+msgBean.getSoftware());
		String pteString = "";
		try {
			StringBuilder sb = new StringBuilder(pteString);
			pteString = sb.append(msgBean.getMessageCode()).append(
					SecurityMessageConstants.APPEND_317)
					.append(msgBean.getVersion())
					.append(acceptanceCode)
					.append(announcementCode)
					//.append(ServerConstants.BANK_SOFTWARE)
					.append(msgBean.getSoftware())
					.// Bank's software
					append(SecurityMessageConstants.SMH).append(
							msgBean.getSenderId()).append(msgBean.getRecSpec())
					.append(msgBean.getRecId()).append(msgBean.getSenderSpec())
					.append(msgBean.getKek_no()).append(msgBean.getAuk_no())
					.append(msgBean.getDate()).append(msgBean.getTime())
					.append(msgBean.getStampNo()).append(
							msgBean.getProtection_level()).append(
							msgBean.getReserved()).append(msgBean.getHsk())
					.append(msgBean.getHash_value()).toString();
			sb = new StringBuilder(pteString);
			sb.append(getAuthenticationValue(pteString, session)).append(
					getFlagAndKeyFromESI(session)).append(
					CommonUtil.pad(errorMessage, 60, " ")).append(
					getAcknowledgmentInfo(session)).toString();
			pteString = sb.toString();
			logger.debug("Formed PTE message------------" + pteString);
		} catch (NullPointerException npe) {
			npe.printStackTrace();
			logger.error(npe);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}
		return pteString;
	}

	/*
	 * private String getNewKey( Session session ) { String newKey = null;
	 * String esiPmsg = ( String
	 * )session.getAttribute(ServerConstants.ESIP_MSG); newKey =
	 * esiPmsg.substring(161,177); logger.debug("New Key for
	 * PTE-----------------"+newKey); return newKey; }
	 */

	/**
	 * 07.11.2008 Method changed by anandkumar.b to resolve the success message
	 * issue in basware
	 */
	private String getFlagAndKeyFromESI(Session session) {
		String newKey = null;
		String esiPmsg = (String) session
				.getAttribute(ServerConstants.ESIP_MSG);
		// newKey = esiPmsg.substring(160,161);
		newKey = esiPmsg.substring(160, 177);
		logger.debug("Flag and new key as in ESI Message::" + newKey);
		return newKey;
	}

	private String getAcknowledgmentInfo(Session session) {
		String acknString = null;
		String filePath = (String) session
				.getAttribute(ServerConstants.ACKN_PATH);
		if (filePath != null) {
			acknString = FileUtil.getFileContetns(filePath);
		} else
			acknString = CommonUtil.pad(" ", 80, " ");
		return acknString;
	}

	private String getAuthenticationValue(String strToAuth, Session session) {
		String authenticationValue = null;
		logger.debug("String to authenticate in PTE-------" + strToAuth);
		Map<String, String> keyMap = (Map<String, String>) session
				.getAttribute(ServerConstants.KEY_MAP);
		String aukString = keyMap.get(ServerConstants.AUK);
		authenticationValue = CommonHandler.getComputedAuthValue(strToAuth,
				aukString);
		return authenticationValue;
	}

	public Object getMessageObject(Object... objects) {
		String fileName = (String) objects[0];
		Session session = (Session) objects[1];
		/*
		 * Read the message as String
		 */
		String filePath = FileUtil.getFilePath(session, fileName);
		String fileContents = FileUtil.getFileContetns(filePath);
		Parser parser = new SUOMessageParser();
		MessageBean msgBean = null;
		try {
			msgBean = (MessageBean) parser.parse(fileContents);
		} catch (NullPointerException npe) {
			npe.printStackTrace();
			logger.error(npe);
		} catch (MessageParsingException mpe) {
			logger.error(mpe.getMessage());
			mpe.printStackTrace();
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return msgBean;
	}

	private boolean doCommonValidations(MessageBean suoBean, MessageBean varBean) {
		boolean valdnFlag = false;
		List<Boolean> valdnList = new ArrayList<Boolean>();
		valdnList.add(suoBean.getVersion().equals(varBean.getVersion()));
		valdnList.add(suoBean.getRecId().equals(varBean.getRecId()));
		valdnList.add(suoBean.getSenderId().equals(varBean.getSenderId()));
		valdnList.add(suoBean.getHsk().equals(varBean.getHsk()));
		valdnList.add(suoBean.getKek_no().equals(varBean.getKek_no()));
		valdnList.add(suoBean.getAuk_no().equals(varBean.getAuk_no()));
		valdnList.add(suoBean.getProtection_level().equals(
				varBean.getProtection_level()));
		valdnList.add(suoBean.getDate().equals(varBean.getDate()));
		valdnList.add(suoBean.getTime().equals(varBean.getTime()));
		valdnList.add(suoBean.getStampNo().equals(varBean.getStampNo()));
		if (valdnList.contains(false))
			valdnFlag = false;
		else
			valdnFlag = true;
		return valdnFlag;
	}

	private String getAgreementId(Session session) {
		String agreementId = null;
		Map<String, String> dataMap = null;
		Map<String, String> filePathData = new HashMap();

		try {
			TransferRequestBean trBean = (TransferRequestBean) session
					.getAttribute(ServerConstants.TR_OBJECT);
			String serviceType = trBean.getFileType();
			String serviceBureauId = (String) session
					.getAttribute(ServerConstants.SERVICEBUREAUID);
			String serviceId = (String) session
					.getAttribute(ServerConstants.SERVICEID);

			logger.debug("Service Id in the Payment files ->" + serviceId);
			/*
			 * Make database call to get the agreement id
			 */
			ServerDAO serverDao = AbstractDAOFactory.getDefaultDAOFactory()
					.getServerDAO();
			/*
			 * Input to the database call 1. Service Id 2. Service Type 3.
			 * Service Bureau Id
			 */

			logger.debug("getAgreementId - ServiceType -> " + serviceType);
			logger.debug("getAgreementId - ServiceId -> " + serviceId);
			logger.debug("getAgreementId - serviceBureauId -> "
					+ serviceBureauId);

			dataMap = (Map<String, String>) serverDao.getServiceIdInfo(
					serviceId.trim(), serviceType, serviceBureauId);
			agreementId = dataMap.get(ServerConstants.AGREEMENTTID);
			if (agreementId == null)
				throw new NullPointerException("Agreement ID is null");
			// String agmtServicePath =
			// FileUtil.getAgmtServiceFilePath(agreementId,serviceType);

			filePathData.put(ServerConstants.SERVICEBUREAUID, serviceBureauId);
			filePathData.put(ServerConstants.AGREEMENTTID, agreementId);
			filePathData.put(ServerConstants.SERVICECODE, serviceType);
			filePathData.put(ServerConstants.SERVICEID, serviceId);
			String agmtServicePath = FileUtil
					.getPatuAgmtServiceFilePath(filePathData);

			/*
			 * Store the agreement id and service code path in the session
			 */
			session.setAttribute(ServerConstants.AGMTSERVICECODEPATH,
					agmtServicePath);
		} catch (NullPointerException npe) {
			npe.printStackTrace();
			logger.error("Data Map from db-----" + dataMap);
		} catch (DatabaseException de) {
			de.printStackTrace();
			logger.error(de);
			logger.error(de.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}
		return agreementId;
	}

	/**
	 * @param Session
	 *            session
	 * @param String
	 *            dataContents
	 * @return String
	 * @deprecated
	 */

	private String getAgmtId(Session session, String dataContents) {
		String agmtId = null;
		String serviceIdString = null;
		Map<String, String> dataMap = null;
		try {
			String firstLine = CommonUtil.getFirstLine(dataContents);
			String payersBusinessIDCode = firstLine.substring(20, 29);
			String serviceId = firstLine.substring(224, 234);
			if (serviceId.trim().length() == 0) {
				serviceIdString = payersBusinessIDCode;
			} else {
				serviceIdString = serviceId;
			}
			logger.debug("Service Id in the Payment files------"
					+ serviceIdString);
			/*
			 * Store the service id in the session
			 */
			session.setAttribute(ServerConstants.SERVICEID, serviceIdString);
			/*
			 * Make database call to get the agreement id
			 */
			ServerDAO serverDao = AbstractDAOFactory.getDefaultDAOFactory()
					.getServerDAO();
			/*
			 * Input to the database call 1. Service Id 2. Service code
			 */
			TransferRequestBean trBean = (TransferRequestBean) session
					.getAttribute(ServerConstants.TR_OBJECT);
			String serviceCode = trBean.getFileType();
			logger.debug("SUOMessageHandler : getAgmtId - ServiceCode ------"
					+ serviceCode);
			logger
					.debug("SUOMessageHandler : getAgmtId - ServiceIdString------"
							+ serviceIdString);
			dataMap = (Map<String, String>) serverDao.getServiceIdInfo(
					serviceIdString.trim(), serviceCode);
			agmtId = dataMap.get(ServerConstants.AGREEMENTTID);
			if (agmtId == null)
				throw new NullPointerException("Agreement ID is null");
			String agmtServicePath = FileUtil.getAgmtServiceFilePath(agmtId,
					serviceCode);
			/*
			 * Store the agreement id and service code path in the session
			 */
			session.setAttribute(ServerConstants.AGMTSERVICECODEPATH,
					agmtServicePath);
		} catch (NullPointerException npe) {
			npe.printStackTrace();
			logger.error("Data Map from db-----" + dataMap);
		} catch (DatabaseException de) {
			de.printStackTrace();
			logger.error(de);
			logger.error(de.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}
		return agmtId;
	}

	/**
	 * @param String
	 *            patuId
	 * @param String
	 *            serviceCode
	 * @return boolean
	 * @deprecated use isProtectionEnabled(String serviceBureauId , String
	 *             serviceCode, String serviceId)instead of this method.
	 */

	private boolean isProtectionEnabled(String patuId, String serviceCode) {
		boolean flag = false;
		try {
			ServerDAO serverDao = AbstractDAOFactory.getDefaultDAOFactory()
					.getServerDAO();
			Boolean bool = (Boolean) serverDao.isMessageProtected(patuId,
					serviceCode);
			logger.debug("Returned boolean value-------" + bool);
			if (bool.booleanValue())
				flag = true;
			else
				flag = false;
		} catch (DatabaseException de) {
			de.printStackTrace();
			logger.error(de);
			flag = false;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			flag = false;
		}
		/*
		 * Call the database function to get whether protection is enabled or
		 * not.
		 */

		return flag;
	}

	private boolean isProtectionEnabled(String serviceBureauId,
			String serviceType, String serviceId) {

		boolean flag = false;
		try {
			ServerDAO serverDao = AbstractDAOFactory.getDefaultDAOFactory()
					.getServerDAO();
			Boolean bool = (Boolean) serverDao.isMessageProtected(
					serviceBureauId, serviceType, serviceId);
			logger.debug("Returned boolean value-------" + bool);
			if (bool.booleanValue())
				flag = true;
			else
				flag = false;
		} catch (DatabaseException de) {
			de.printStackTrace();
			logger.error(de);
			flag = false;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			flag = false;
		}
		/*
		 * Call the database function to get whether protection is enabled or
		 * not.
		 */

		return flag;
	}

	private void manageKeyChange(MessageBean msgBean, String keyChange) {
		int keyVal = Integer.parseInt(keyChange);
		if (keyVal == 0) {
			// do nothing
			String tempString = "                ";
			msgBean.setNew_key(tempString);
		} else if (keyVal == 1) {
			/*
			 * Get the new AUK Encrypt the new AUK Convert into hexadecimal Put
			 * the value in the new key field. Set the AUK no as the latest one.
			 */
		} else if (keyVal == 2) {
			/*
			 * For cut off period
			 */
		}
	}
}