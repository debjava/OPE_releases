package com.ope.patu.messages;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.coldcore.coloradoftp.session.Session;
import com.ope.patu.exception.FailedValidationException;
import com.ope.patu.server.db.AuditLogger;
import com.ope.patu.handler.MessageHandler;
import com.ope.patu.handler.Validator;
import com.ope.patu.handler.ValidatorFactory;
import com.ope.patu.payment.core.AbstractProcessor;
import com.ope.patu.payment.core.PaymentProcessor;
import com.ope.patu.server.beans.TransferRequestBean;
import com.ope.patu.server.constant.SecurityMessageConstants;
import com.ope.patu.server.constant.ServerConstants;
import com.ope.patu.server.db.AbstractDAOFactory;
import com.ope.patu.server.db.ServerDAO;
import com.ope.patu.util.CommonUtil;
import com.ope.patu.util.FileUtil;

public class SIIRTOPYYNTOMessageHandler implements MessageHandler {
	protected static Logger logger = Logger
			.getLogger(SIIRTOPYYNTOMessageHandler.class);

	private TransferRequestBean trBean;

	public Object getMessageObject(Object... objects) {
		return trBean;
	}

	public Object handleObject(Object... objects) {
		String fileName = (String) objects[0];
		Session session = (Session) objects[1];
		String msgTypeName = (String) objects[2];
		boolean validationFlag = false;
		try {
			/*
			 * First check whether the ESI validation is true or false
			 */
			Boolean esiStatusValue = (Boolean) session
					.getAttribute(ServerConstants.ESI_MSG_STATUS);
			String filePath = FileUtil.getSessionTempPath(session, fileName);
			String fileContents = FileUtil.getFileContetns(filePath);
			logger.debug("Total File contents----" + fileContents);
			logger.debug("ESI Status Value -> "+esiStatusValue.booleanValue());
				
			if (esiStatusValue.booleanValue()) {
				/*
				 * Go ahead for the validation of Transfer request
				 */
				Validator validator = ValidatorFactory
						.getValidator(msgTypeName);
				int no_of_lines = FileUtil.getLineNo(filePath);
				logger.debug("Total No of Lines---------" + no_of_lines);
				if (no_of_lines == 1) {
					/*
					 * It is true that is is a Transfer request and it does not
					 * contain any other data
					 */
					validationFlag = validator.validate(fileContents, fileName,
							session);
					trBean = (TransferRequestBean) validator
							.getValidatedObject();
					logger.debug("Transfer request validation-------"+ validationFlag);
					if (validationFlag) {
						session.setAttribute(ServerConstants.TR_OBJECT, trBean);
						session.setAttribute(ServerConstants.TR_MSG_STATUS,
								new Boolean(validationFlag));
					}
				} else {
					/*
					 * There can be two cases. 1. There is no data in the file
					 * 2. It contains both Transfer request and the actual
					 * payment data. However first case is ignored as it works
					 * with multi banking software.It is assumed that multi
					 * banking software handles that no file contains 0 data.
					 * Now handle for for both
					 */
					validationFlag = validator.validate(fileContents, fileName,
							session);
					trBean = (TransferRequestBean) validator
							.getValidatedObject();
					logger.info("Transfer request validation-------"
							+ validationFlag);
					if (validationFlag) {
						session.setAttribute(ServerConstants.TR_OBJECT, trBean);
						session.setAttribute(ServerConstants.TR_MSG_STATUS,
								new Boolean(validationFlag));

						String paymentData = FileUtil
								.getPaymentDetailsFromTR(filePath);
						logger.debug("PaymentData---->>>" + paymentData);
						String agreementId = getAgmtId(session, paymentData);
						session.setAttribute(ServerConstants.AGREEMENTTID,
								agreementId);
						String agmtServiceFilePath = (String) session
								.getAttribute(ServerConstants.AGMTSERVICECODEPATH);
						logger.debug("In TR agmtServiceFilePath -------"
								+ agmtServiceFilePath);
						PaymentProcessor processor = AbstractProcessor
								.getProcessor(trBean.getFileType().trim());
						processor.process(paymentData, session);
					}
				}
			} else {
				logger
						.debug("Transfer request message validation is false, so transfer request can not be processed");
			}
			AuditLogger.logTRMessage(session, fileName, filePath,
					SecurityMessageConstants.INCOMING, trBean);
		} catch (FailedValidationException fve) {
			logger.error(fve);
		} catch (Exception e) {
			logger.error(e);
		}
		return validationFlag;
	}

	private String getAgmtId(Session session, String dataContents) {
		String agmtId = null;
		String serviceIdString = null;
		try {
			String firstLine = CommonUtil.getFirstLine(dataContents);
			String payersBusinessIDCode = firstLine.substring(20, 29);
			String serviceId = firstLine.substring(224, 234);
			if (serviceId.trim().length() == 0) {
				serviceIdString = payersBusinessIDCode;
			} else {
				serviceIdString = serviceId;
			}
			/*
			 * Store the service id in the session
			 */
			
			if(!(serviceIdString==null))
				serviceIdString = serviceIdString.trim();
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
			String serviceBureauId = (String) session
					.getAttribute(ServerConstants.SERVICEBUREAUID);

			String serviceType = trBean.getFileType();
			Map<String, String> dataMap = (Map<String, String>) serverDao
					.getServiceIdInfo(serviceIdString, serviceType,
							serviceBureauId);
			agmtId = dataMap.get(ServerConstants.AGREEMENTTID);
			Map map = new HashMap();

			// String serviceBureauId ,String agreementId,String
			// serviceType,String serviceId

			map.put(ServerConstants.SERVICEBUREAUID, serviceBureauId);
			map.put(ServerConstants.AGREEMENTTID, agmtId);
			map.put(ServerConstants.SERVICECODE, serviceType);
			map.put(ServerConstants.SERVICEID, serviceIdString);

			// String agmtServicePath = FileUtil.getAgmtServiceFilePath(agmtId,
			// serviceCode);
			String agmtServicePath = FileUtil.getPatuAgmtServiceFilePath(map);

			/*
			 * Store the agreement id and service code path in the session
			 */
			session.setAttribute(ServerConstants.AGMTSERVICECODEPATH,
					agmtServicePath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return agmtId;
	}
}