package com.ope.patu.payments.lum2;

import java.util.List;

import com.coldcore.coloradoftp.session.Session;
import com.ope.patu.payment.core.Parser;
import com.ope.patu.payment.core.VelocityTemplate;
import com.ope.patu.payment.db.PaymentDbImpl;
import com.ope.patu.server.constant.ServerConstants;
import com.ope.patu.util.CommonUtil;
import com.ope.patu.util.DateUtil;
import com.ope.patu.util.FileUtil;
import com.ope.patu.payment.core.CommonPaymentConstant;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ope.patu.payment.utility.AccountNumberValidation;
import com.ope.patu.payments.lmp300.PaymentConstants;
import com.ope.patu.payments.lum2.beans.LUM2BatchRecordBean;
import com.ope.patu.payments.lum2.beans.LUM2InvoiceRecordBean;
import com.ope.patu.payments.lum2.beans.LUM2PaymentRecordBean;
import com.ope.patu.payments.lum2.beans.LUM2SumRecordBean;
import com.ope.patu.payments.lum2.beans.LUM2SupplementaryFeedbackRecordBean;
import com.ope.patu.payments.lum2.beans.LUM2SupplementaryPaymentRecordBean;

public class LUM2ServiceValidator {

	protected static Logger logger = Logger.getLogger(LUM2ServiceValidator.class);

	/**
	 * function <b>serviceValidator()</b> <br>
	 * This function provide the validation for all records (Batch, Payment, Sum etc..) 
	 * present inside the Cross Border Payment file.
	 * 
	 * @param Object
	 * @return Object
	 */

	@SuppressWarnings("unchecked")
	public Object serviceValidator(Object... objects) {
		
		Session session = (Session) objects[0];
		List<String> fileContentList = (List) objects[1];

		String agmtServiceFilePath = (String) session.getAttribute(ServerConstants.AGMTSERVICECODEPATH);
		String paymentFilePath = agmtServiceFilePath + File.separator + LUM2Constants.PATU_OUT_FILE;
		String ackPath = agmtServiceFilePath + File.separator + LUM2Constants.U_KUITTAUS;
		session.setAttribute(ServerConstants.ACKN_PATH, ackPath);

		Map map = null;
		try {
			map = getAcceptReject(fileContentList, session);
			BigInteger totalAcceptedPaymentAmount = new BigInteger("0");
			int totalAcceptedPayments = 0;
			
			List batchRecordList = (List) map.get("batchRecordList");
			List sumRecordList = (List) map.get("sumRecordList");
			List paymentRecordList = (List) map.get("paymentRecordList");
			List invoiceRecordList = (List) map.get("invoiceRecordList");
			List supPaymentRecordList = (List) map.get("supPaymentList");
			List commonErrorList = (List) map.get("commonErrorList");
			List rejectedList = (List) map.get("rejectedList");
			List transactionList = (List) map.get("transactionList");
			List rejectedMessageList = (List)map.get("rejectedMessageList");
			totalAcceptedPayments = (Integer) map.get("totalAcceptedPayments");
			totalAcceptedPaymentAmount = (BigInteger) map.get("totalAcceptedPaymentAmount");
			
			logger.debug("This CBP file contains the ::  Batch Record ->" + batchRecordList.size());
			logger.debug("Sum Record ->" + sumRecordList.size()+ ":: Payment Record ->"	+ paymentRecordList.size());
			logger.debug("Invoice Record ->" + invoiceRecordList.size()+ ":: Sup-Payment Record ->"	+ supPaymentRecordList.size());
			logger.debug(":: Transaction List ->"	+ transactionList.size()+ ":: rejected Record List ->"	+ rejectedList.size());

			logger.debug(":: NUMBER OF REJECTED CASE => "	+ rejectedMessageList.size());
			logger.debug(":: DETAILS OF REJECTED CASE => "	+ rejectedMessageList);
			
			int acceptedPayments = 0;
			int rejectedPayments = 0;
			int numberOfBatches = batchRecordList.size();

			BigInteger acceptedPaymentSum = new BigInteger("0");
			BigInteger rejectedPaymentSum = new BigInteger("0");
			BigInteger totalPaymentSum = new BigInteger("0");

			for (int i = 0; i < paymentRecordList.size(); i++) {
				LUM2PaymentRecordBean paymentRecordBean = (LUM2PaymentRecordBean) paymentRecordList.get(i);
				List errorList = paymentRecordBean.getPaymentErrorMsg();

				if (errorList.size() != 0) {
					rejectedPaymentSum = rejectedPaymentSum.add(new BigInteger(paymentRecordBean.getCurrencyAmount()));
					rejectedPayments = rejectedPayments + 1;
				} else {
					acceptedPaymentSum = acceptedPaymentSum.add(new BigInteger(paymentRecordBean.getCurrencyAmount()));
					acceptedPayments = acceptedPayments + 1;
				}
			}
			totalPaymentSum = acceptedPaymentSum.add(rejectedPaymentSum);

			DecimalFormat lengthSix = new DecimalFormat("000000");
			DecimalFormat lengthSixteen = new DecimalFormat("0000000000000000");

			String noOfTransactions = "";
			String sumOfTransactions = "";

			FileUtil.writePaymentFile(paymentFilePath, transactionList);

			int returnCode = 1;
			String fileStatus = ServerConstants.FILE_STATUS_REJECTED;
			if ((rejectedList.size() == 0)) {
				logger.debug("FILE IS NOT INVALID :::::::: Accepted");
				noOfTransactions = lengthSix.format(totalAcceptedPayments);
				sumOfTransactions = lengthSixteen.format(totalAcceptedPaymentAmount.longValue());
				returnCode = 0;
				fileStatus = ServerConstants.FILE_STATUS_ACCEPTED;

			} else {
				logger.debug("FILE IS INVALID :::::::: Rejected");
				noOfTransactions = lengthSix.format(totalAcceptedPayments);
				sumOfTransactions = lengthSixteen.format(totalAcceptedPaymentAmount.longValue());
				returnCode = 1;
				fileStatus = ServerConstants.FILE_STATUS_REJECTED;
			}

			Map ackMap = (Map) getAcknowledgeMentFile(ackPath,
					noOfTransactions, sumOfTransactions,returnCode,numberOfBatches,rejectedMessageList);
			List ackList = new ArrayList();
			ackList.add(ackMap);
			String ackMsg = new VelocityTemplate().callVelocityTemplate(ackList, ackPath, LUM2Constants.ACK);

			FileUtil.writeContents(ackPath, ackMsg); 
			
			int totalRecords = rejectedPayments + acceptedPayments;
			
			Map<String, String> transactionMap = new HashMap<String, String>();
			transactionMap.put(PaymentConstants.TOTAL_RECORDS, String.valueOf(totalRecords));
			transactionMap.put(PaymentConstants.VALID_RECORD_COUNT, String.valueOf(acceptedPayments));
			transactionMap.put(PaymentConstants.INVALID_RECORD_COUNT, String.valueOf(rejectedPayments));
			transactionMap.put(ServerConstants.PAYMENT_FILE_ERROR_CODE, new DecimalFormat("0000").format(returnCode));
			transactionMap.put(ServerConstants.PAYMENT_FILE_ERROR_MESSAGE,fileStatus );
			transactionMap.put(ServerConstants.PAYMENT_FILE_STATUS, fileStatus);
			
			session.setAttribute(ServerConstants.TRANSACTION_MAP, transactionMap );

		} catch (Exception e) {
			logger.debug("Caught in the Exception : " + e.getMessage());
		}
		return map;
	}

	/**
	 * function <b>getAcknowledgeMentFile()</b> <br>
	 * This function is used to create the acknowledgement file.
	 * 
	 * @param Object
	 * @return Object
	 */

	@SuppressWarnings("unchecked")
	public static Map getPaymentAcceptedData(List paymentRecordList) {
		Map map = new HashMap();
		int acceptedPayments = 0;
		int rejectedPayments = 0;
		BigInteger acceptedPaymentSum = new BigInteger("0");
		BigInteger rejectedPaymentSum = new BigInteger("0");
		
		for (int i = 0; i < paymentRecordList.size(); i++) {
			LUM2PaymentRecordBean paymentRecordBean = (LUM2PaymentRecordBean) paymentRecordList.get(i);
			List errorList = paymentRecordBean.getPaymentErrorMsg();

			if (errorList.size() != 0) {
				rejectedPaymentSum = rejectedPaymentSum.add(new BigInteger(paymentRecordBean.getCurrencyAmount()));
				rejectedPayments = rejectedPayments + 1;
			} else {
				acceptedPaymentSum = acceptedPaymentSum.add(new BigInteger(paymentRecordBean.getCurrencyAmount()));
				acceptedPayments = acceptedPayments + 1;
			}
		}
		
		map.put("acceptedPayments", acceptedPayments);
		map.put("rejectedPayments", rejectedPayments);
		map.put("acceptedPaymentSum", acceptedPaymentSum);
		map.put("rejectedPaymentSum", rejectedPaymentSum);
		
		// logger.debug("getPaymentAcceptedData - map.size()"+map.size());
		return map;
	}

	/**
	 * function <b>getAcknowledgeMentFile()</b> <br>
	 * This function is used to create the acknowledgement file.
	 * 
	 * @param Object
	 * @return Object
	 */

	@SuppressWarnings("unchecked")
	public static List getPatuOutFile(Object... objects) {
		/*
		 * Generate a List object which will be used to create a patu out file.
		 * This List object contains the patu file data in following manner.....
		 * Session Id + Aggrement Id + Time Stamp + Transaction Record +
		 * Transaction Status
		 */

		Session session = (Session) objects[0];
		List transactionList = (List) objects[1];
		
		String sessionId = CommonUtil.pad(new Integer( session.getSessionId()).toString(), 9, " ");
		String serviceBureauId = CommonUtil.pad("#"+(String)session.getAttribute(ServerConstants.SERVICEBUREAUID),18," ");
		String aggrementId = CommonUtil.pad((String)"#"+session.getAttribute(ServerConstants.AGREEMENTTID),10," ");
		String serviceType = CommonUtil.pad((String)"#"+session.getAttribute(ServerConstants.SERVICECODE),11," ");
		String serviceID = CommonUtil.pad((String)"#"+session.getAttribute(ServerConstants.SERVICEID),11," ");
		long longTimeStamp = System.currentTimeMillis();
		/**
		 * To provide the format for the TimeStamp
		 */
		Date longDate = new Date( longTimeStamp );
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMddhhmmss");
		String timeStamp = simpleDateFormat.format(longDate);//new Long( longTimeStamp ).toString()+"_";

		String recordSuffixData = new StringBuffer().append(sessionId).append(serviceBureauId).
		append(aggrementId).append(serviceType).append(serviceID).append("#"+timeStamp).toString();
		
		
		StringBuffer patuOutFileContent = new StringBuffer("");
		
		List patuFileList = new ArrayList();

		for (Iterator iter = transactionList.iterator(); iter.hasNext();) {

			String transactionRecord = (String) iter.next();
			patuOutFileContent.append(transactionRecord).append("#").append(recordSuffixData);
			patuFileList.add(patuOutFileContent.toString());
		}

		// patuFileList.add(outBuf.append(session_id).append(aggrement_id).append(time_stamp).append(record).append(status).toString());

		return patuFileList;
	}

	/**
	 * function <b>getRecordSuffixData()</b> <br>
	 * This function is used to create the record suffix data for payment file.
	 * Sample -> <Session_Id>#<Service_Bureau_Id>#<Aggrement_Id>#<Service_Type>#<Service_ID>#<Time_Stamp>
	 * 
	 * @param Object
	 * @return String
	 */

	public static String getRecordSuffixData(Object... objects) {
		/*
		 * Session Id + Service Bureau Id + Aggrement Id + Service Type + Service ID + Time Stamp 
		 */

		Session session = (Session) objects[0];
				
		String sessionId = CommonUtil.pad(new Integer( session.getSessionId()).toString(), 9, " ");
		String serviceBureauId = CommonUtil.pad("#"+(String)session.getAttribute(ServerConstants.SERVICEBUREAUID),18," ");
		String aggrementId = CommonUtil.pad((String)"#"+session.getAttribute(ServerConstants.AGREEMENTTID),10," ");
		String serviceType = CommonUtil.pad((String)"#"+session.getAttribute(ServerConstants.SERVICECODE),11," ");
		String serviceID = CommonUtil.pad((String)"#"+session.getAttribute(ServerConstants.SERVICEID),11," ");
		long longTimeStamp = System.currentTimeMillis();
		
		/** To provide the format for the TimeStamp	 */
		Date longDate = new Date( longTimeStamp );
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMddhhmmss");
		String timeStamp = simpleDateFormat.format(longDate);//new Long( longTimeStamp ).toString()+"_";

		String recordSuffixData = new StringBuffer().append("#").append(sessionId).append(serviceBureauId).
		append(aggrementId).append(serviceType).append(serviceID).append("#"+timeStamp).toString();
			
		return recordSuffixData;
	}
	
	/**
	 * function <b>getAcknowledgeMentFile()</b> <br>
	 * This function is used to create the map content for acknowledgement file.
	 * 
	 * @param Object
	 * @return Object
	 */
	
	@SuppressWarnings("unchecked")
	public static Object getAcknowledgeMentFile(Object... objects) {
		/*
		 * Generate a map object which will be used to create a acknowledgement
		 * file using velocity template. This map object contains
		 * acknowledgement related fields.
		 */
		Map map = new LinkedHashMap<String, String>();
		String ackMessageFilePath = (String) objects[0];
		String noOfTransactions = (String) objects[1];
		String sumOfTransactions = (String) objects[2];
		int returnCode = (Integer) objects[3];
		int numberOfBatches = (Integer) objects[4];
		List rejectedMessageList = (List) objects[5];
		StringBuffer ackMessageBuffer = new StringBuffer("");
		DecimalFormat lengthTwo = new DecimalFormat("00");
		DecimalFormat lengthThree = new DecimalFormat("000");
		DecimalFormat lengthSix = new DecimalFormat("000000");
		DecimalFormat lengthSixteen = new DecimalFormat("0000000000000000");

		Date today = Calendar.getInstance().getTime();
		
		DateFormat ackDateFormat = new SimpleDateFormat("yyMMdd");
		SimpleDateFormat ackTimeFormat = new SimpleDateFormat("HHmmss");

		StringBuffer sbf = new StringBuffer("");
		for (Iterator iter = rejectedMessageList.iterator(); iter.hasNext();) {
			String message = (String) iter.next();
			message = CommonUtil.pad(message.replace(',', ' '),50," ");
			message = message.substring(0, 50);
			sbf.append(message).append(',');
		}
		String notificationLine = sbf.toString();
		
		// 01. File Type - 10 Characters - AN
		String fileType = CommonUtil.pad(LUM2Constants.FILE_TYPE_LUM2, 10, " ");
		ackMessageBuffer.append(fileType);
		logger.debug(":::: ACK - 01 :: fileType ---->" + fileType);
		map.put(CommonPaymentConstant.ACK_FILE_TYPE, fileType);

		// 02. Currency - 04 Characters - AN
		String currency = CommonUtil.pad(" ", 4, " ");
		ackMessageBuffer.append(currency);
		logger.debug(":::: ACK - 02 :: currency ---->" + currency);
		map.put(CommonPaymentConstant.ACK_CURRENCY, currency);

		// 03. Batches - 03 Characters - N
		String noOfBatches = lengthThree.format(numberOfBatches);
		ackMessageBuffer.append(noOfBatches);
		logger.debug(":::: ACK - 03 :: noOfBatches---->"	+ noOfBatches);
		map.put(CommonPaymentConstant.ACK_BATCHES, noOfBatches);

		// 04. No. of transactions - 06 Characters - N
		ackMessageBuffer.append(noOfTransactions);
		logger.debug(":::: ACK - 04 :: noOfTransactions---->" + noOfTransactions);
		map.put(CommonPaymentConstant.ACK_NO_OF_TRANSACTIONS, noOfTransactions);

		// 05. Sum of transactions - 16 Characters - N
		ackMessageBuffer.append(sumOfTransactions);
		logger.debug(":::: ACK - 05 :: sumOfTransactions---->"+ sumOfTransactions);
		map.put(CommonPaymentConstant.ACK_SUM_OF_TRANSACTIONS, sumOfTransactions);

		// 06. Sign - 01 Character - AN
		String transactionsAmountSign = CommonUtil.pad(LUM2Constants.SIGN_PLUS,1, " ");
		ackMessageBuffer.append(transactionsAmountSign);
		logger.debug(":::: ACK - 06 :: transactionsAmountSign---->"+ transactionsAmountSign);
		map.put(CommonPaymentConstant.ACK_TRANSACTIONS_SIGN, transactionsAmountSign);

		// 07. No. of credit entries - 06 Characters - N
		String noOfCreditEntries = lengthSix.format(0);
		ackMessageBuffer.append(noOfCreditEntries);
		logger.debug(":::: ACK - 07 :: noOfCreditEntries---->" + noOfCreditEntries);
		map.put(CommonPaymentConstant.ACK_NO_OF_CREDITS, noOfCreditEntries);

		// 08. Total sum of credit entries - 16 Characters - N
		String sumOfCreditEntries = lengthSixteen.format(0);
		ackMessageBuffer.append(sumOfCreditEntries);
		logger.debug(":::: ACK - 08 :: sumOfCreditEntries---->"+ sumOfCreditEntries);
		map.put(CommonPaymentConstant.ACK_SUM_OF_CREDITS, sumOfCreditEntries);

		// 09. Sign - 01 Character - AN
		String creditEntriesSign = CommonUtil.pad(LUM2Constants.SIGN_MINUS, 1, " ");
		ackMessageBuffer.append(creditEntriesSign);
		logger.debug(":::: ACK - 09 :: creditEntriesSign---->" + creditEntriesSign);
		map.put(CommonPaymentConstant.ACK_CREDITS_SIGN, creditEntriesSign);

		// 10. Date - 06 Characters - N
		String dateFormat = ackDateFormat.format(today);
		String date = lengthSix.format(Integer.parseInt(dateFormat));
		ackMessageBuffer.append(date);
		logger.debug(":::: ACK - 10 :: date---->" + date);
		map.put(CommonPaymentConstant.ACK_DATE, dateFormat);

		// 11. Time - 06 Characters - N
		String currentTime = ackTimeFormat.format(today.getTime());
		String time = lengthSix.format(Integer.parseInt(currentTime));
		ackMessageBuffer.append(time);
		logger.debug(":::: ACK - 11 :: time---->" + time);
		map.put(CommonPaymentConstant.ACK_TIME, currentTime);

		// 12. Return Code - 03 Characters - N
		String ackRetCode = lengthThree.format(returnCode);
		ackMessageBuffer.append(ackRetCode);
		logger.debug(":::: ACK - 12 :: ackRetCode---->"+ ackRetCode);
		map.put(CommonPaymentConstant.ACK_RETURN_CODE, ackRetCode);

		// 13. No. of Notification Line transactions - 02 Characters - N
		String noOfNotficationLines = lengthTwo.format(rejectedMessageList.size());
		ackMessageBuffer.append(noOfNotficationLines);
		logger.debug(":::: ACK - 13 :: noOfNotficationLines---->" + noOfNotficationLines);
		map.put(CommonPaymentConstant.ACK_NO_OF_NOTIFICATION_LINE, noOfNotficationLines);

		// 14. Notification Line transactions - 02 Characters - N
//		String notificationLine = CommonUtil.pad("", 0, "");
		ackMessageBuffer.append(notificationLine);
		logger.debug(":::: ACK - 14 :: notificationLine---->" + notificationLine);
		map.put(CommonPaymentConstant.ACK_NOTIFICATION_LINE, notificationLine);

		// FileUtil.writeFeedBackFile(ackMessageFilePath,
		// ackMessageBuffer.toString());

		map.put(CommonPaymentConstant.ACK_FILE_CONTENT, ackMessageBuffer.toString());

		return map;
	}

	/**
	 * function <b>getRecordsList()</b> <br>
	 * This function is used to get the record list from the file contents.
	 * 
	 * @param String
	 * @return List
	 */

	public static List getRecordsList(String fileContents) {
		List<String> recordsList = new LinkedList<String>();
		String[] contents = fileContents.split("\\n");
		for (String str : contents)
			recordsList.add(str);
		return recordsList;
	}

	/**
	 * function <b>getFileContents()</b> <br>
	 * This function is used to get the file contents.
	 * 
	 * @param String
	 * @return String
	 */

	public static String getFileContents(String filePath) {
		StringBuffer sb = new StringBuffer();
		FileReader fr = null;
		BufferedReader br = null;
		try {
			fr = new FileReader(filePath);
			br = new BufferedReader(fr);
			String subdata = null;
			while ((subdata = br.readLine()) != null) {
				sb.append(subdata);
				sb.append(LUM2Constants.NEW_LINE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * function <b>getParserName()</b> <br>
	 * This function is used to get the correct parser name for corresponding
	 * record code.
	 * 
	 * @param String
	 * @return String
	 */

	public static String getParserName(String recordCode) {
		String parserName = null;
		if (recordCode.equals(LUM2Constants.ZERO)) {
			// Batch Record
			parserName = LUM2Constants.BATCH;
		} else if (recordCode.equals(LUM2Constants.ONE)) {
			// Payment Record
			parserName = LUM2Constants.PAYMENT;
		} else if (recordCode.equals(LUM2Constants.TWO)
				|| recordCode.equals(" ")) {
			// Invoice Record (Optional)
			parserName = LUM2Constants.INVOICE;
		} else if (recordCode.equals(LUM2Constants.NINE)) {
			// Sum Record
			parserName = LUM2Constants.SUM;
		} else if (recordCode.equals(LUM2Constants.L) || recordCode.equals(" ")) {
			// Supplementary Payment Record (Optional)
			parserName = LUM2Constants.SUPPLEMENTARY_PAYMENT;
		} else if (recordCode.equals(LUM2Constants.P)) {
			// Supplementary feedback Record
			parserName = LUM2Constants.SUPPLEMENTARY_FEEDBACK;
		}
		return parserName;
	}

	/**
	 * function <b>getAcceptReject()</b> <br>
	 * This function is used to perform following operation..
	 * <ol>
	 * <li> Validate the all the records of the file.
	 * <li> Return the error, success and rejected message bean on the record
	 * wise in a List object.
	 * <li> Show the errors in case any problem or exceptions.
	 */

	@SuppressWarnings("unchecked")
	public static Map getAcceptReject(List<String> contentsList, Session session) {

		List batchList = new ArrayList();
		List paymentList = new ArrayList();
		List invoiceList = new ArrayList();
		List sumList = new ArrayList();
		List commonErrorList = new ArrayList();
		List supPaymentList = new ArrayList();
		List rejectedList = new ArrayList();
		List transactionList = new LinkedList<String>();
		List transactionRecordList = new LinkedList<String>();
		List batchPaymentList = new LinkedList<String>();
		List rejectedMessageList = new LinkedList<String>();
		
		Map ackMap = new LinkedHashMap();
		Parser parser = null;
		int numberOfPayments = 0;
		BigInteger paymentAmount = new BigInteger("0");
		BigInteger totalAcceptedPaymentAmount = new BigInteger("0");
		int totalAcceptedPayments = 0;
		boolean paymentFlagStatus = false;
		int acceptedTransaction = 0;
		boolean batchStatus = false;
		
		String recordSuffixData =  getRecordSuffixData(session);
		String batchRecordData = "";
		String paymentRecordData = "";
		
		try {
			for (String str : contentsList) {
				/**
				 * Write the code here to validate 
				 * 1. To parse the string 
				 * 2. Set the parsed object in bean 
				 * 3. Validate the parsed object
				 */

//				String applicationCode = str.substring(0, 4); // No Need
				String recordCode = str.substring(4, 5);
//				String acceptanceCode = str.substring(5, 6); // No Need

				String parserName = getParserName(recordCode);

				logger.debug("CBP :: parserName -> " + parserName);
				
				if (parserName != null) {
					parser = LUM2ParserFactory.getParser(parserName);
					if (parserName.equalsIgnoreCase(LUM2Constants.BATCH)) {
						LUM2BatchRecordBean batchRecordBean = (LUM2BatchRecordBean) parser.parse(str);
						Map map = validateBatchRecord(batchRecordBean);
						List errorMsgList = (List) map.get("errorMsgList");
						List successMsgList = (List) map.get("successMsgList");
						List rejectedMsgList = (List) map.get("rejectedMsgList");
						String errorData = CommonUtil.pad(" ", 50, " ");
						if (errorMsgList.size() == 0) {
							// STATUS - SUCCESS
							batchRecordData = str.concat(recordSuffixData).concat("#").concat(errorData).concat("#").concat(LUM2Constants.STATUS_S);
							batchStatus = true;
						} else if ( (errorMsgList.size()!=0) || (rejectedMsgList.size()!=0) ) {
							// STATUS - FAILED
							if(errorMsgList.size()!=0) {
								errorData = (String) errorMsgList.get(0);
								errorData = CommonUtil.pad(errorData, 50, " ");
								errorData = errorData.substring(0, 50);
							}
							batchStatus = false;
							batchRecordData = str.concat(recordSuffixData).concat("#").concat(errorData).concat("#").concat(LUM2Constants.STATUS_F);
							rejectedMessageList.addAll(rejectedMsgList);
						}
						
						logger.debug("BATCH :: Error Msg List -->" + errorMsgList);
//						logger.debug("BATCH :: success Msg List -->" + successMsgList);
						logger.debug("BATCH :: Rejected Msg List -->" + rejectedMsgList);
						
						batchRecordBean.setBatchErrorMsg(errorMsgList);
						batchRecordBean.setBatchSuccessMsg(successMsgList);
						batchRecordBean.setBatchRejectedMsg(rejectedMsgList);
						batchList.add(batchRecordBean);
					} else if (parserName.equalsIgnoreCase(LUM2Constants.PAYMENT)) {
						LUM2PaymentRecordBean paymentRecordBean = (LUM2PaymentRecordBean) parser.parse(str);
						Map map = validatePaymentRecord(paymentRecordBean,paymentAmount, session);
						List errorMsgList = (List) map.get("errorMsgList");
						List successMsgList = (List) map.get("successMsgList");
						List rejectedMsgList = (List) map.get("rejectedMsgList");
												
						String errorData = CommonUtil.pad(" ", 50, " ");
						
						
						logger.debug("PAYMENT :: Error Msg List -->" + errorMsgList);
//						logger.debug("PAYMENT :: success Msg List -->" + successMsgList);
						logger.debug("PAYMENT :: Rejected Msg List -->" + rejectedMsgList);

						if ((errorMsgList.size() == 0) && batchStatus) {
							// STATUS - SUCCESS
							str = str.concat(recordSuffixData).concat("#").concat(errorData).concat("#").concat(LUM2Constants.STATUS_S);
							transactionList.add(str);
							paymentFlagStatus = true;
							acceptedTransaction = acceptedTransaction + 1;
							paymentRecordData = str;
						} else if ( (errorMsgList.size()!=0) || (rejectedMsgList.size()!=0) || (batchStatus==false) ) {
							
							if(errorMsgList.size()!=0) {
								errorData = (String) errorMsgList.get(0);
								errorData = CommonUtil.pad(errorData, 50, " ");
								errorData = errorData.substring(0, 50);
							}
							// STATUS - FAILED
							str = str.concat(recordSuffixData).concat("#").concat(errorData).concat("#").concat(LUM2Constants.STATUS_F);
							transactionList.add(str);
							paymentFlagStatus = false;
							paymentRecordData = str;
							rejectedMessageList.addAll(rejectedMsgList);
						}
						paymentRecordBean.setPaymentErrorMsg(errorMsgList);
						paymentRecordBean.setPaymentSuccessMsg(successMsgList);
						paymentRecordBean.setPaymentRejectedMsg(rejectedMsgList);
						numberOfPayments = numberOfPayments + 1;
						paymentRecordBean.setSeqNo(numberOfPayments);
						paymentAmount = (BigInteger) map.get("amountAddition");
						logger.debug("numberOfPayments =>"+ numberOfPayments + " :: Total Amount =>"+ paymentAmount.longValue());
						paymentList.add(paymentRecordBean);
						batchPaymentList.add(paymentRecordBean);
					} else if (parserName.equalsIgnoreCase(LUM2Constants.INVOICE)) {
						LUM2InvoiceRecordBean invoiceRecordBean = (LUM2InvoiceRecordBean) parser.parse(str);
						Map map = validateInvoiceRecord(invoiceRecordBean);
						List errorMsgList = (List) map.get("errorMsgList");
						List successMsgList = (List) map.get("successMsgList");
						List rejectedMsgList = (List) map.get("rejectedMsgList");
						String errorData = CommonUtil.pad(" ", 50, " ");

						logger.debug("INVOICE :: Error Msg List -->" + errorMsgList);
//						logger.debug("INVOICE :: success Msg List -->" + successMsgList);
//						logger.debug("INVOICE :: Rejected Msg List -->" + rejectedMsgList);
	
						if (errorMsgList.size() == 0 && paymentFlagStatus) {
							// STATUS - SUCCESS
							str = str.concat(recordSuffixData).concat("#").concat(errorData).concat("#").concat(LUM2Constants.STATUS_S);
							transactionList.add(str);
						} else if ( ((errorMsgList.size()!=0) || (rejectedMsgList.size()!=0) ) ||  (paymentFlagStatus==false) ){
							// STATUS - FAILED
							
							if(errorMsgList.size()!=0) {
								errorData = (String) errorMsgList.get(0);
								errorData = CommonUtil.pad(errorData, 50, " ");
								errorData = errorData.substring(0, 50);
							}
							str = str.concat(recordSuffixData).concat("#").concat(errorData).concat("#").concat(LUM2Constants.STATUS_F);
							transactionList.add(str);
							rejectedMessageList.addAll(rejectedMsgList);
						}
						
						invoiceRecordBean.setInvoiceErrorMsg(errorMsgList);
						invoiceRecordBean.setInvoiceSuccessMsg(successMsgList);
						invoiceRecordBean.setInvoiceRejectedMsg(rejectedMsgList);
						invoiceList.add(invoiceRecordBean);
					}  else if (parserName.equalsIgnoreCase(LUM2Constants.SUPPLEMENTARY_PAYMENT)) {
						LUM2SupplementaryPaymentRecordBean supplementaryPaymentRecordBean = (LUM2SupplementaryPaymentRecordBean) parser.parse(str);
						Map map = validateSupplementaryPaymentsRecord(supplementaryPaymentRecordBean);
						List errorMsgList = (List) map.get("errorMsgList");
						List successMsgList = (List) map.get("successMsgList");
						List rejectedMsgList = (List) map.get("rejectedMsgList");
						String errorData = CommonUtil.pad(" ", 50, " ");
						
						logger.debug("SUPPLEMENTARY_PAYMENT :: Error Msg List -->" + errorMsgList);
//						logger.debug("SUPPLEMENTARY_PAYMENT :: success Msg List -->" + successMsgList);
//						logger.debug("SUPPLEMENTARY_PAYMENT :: Rejected Msg List -->" + rejectedMsgList);
						
						if (errorMsgList.size() == 0 && paymentFlagStatus) {
							// STATUS - SUCCESS
							str = str.concat(recordSuffixData).concat("#").concat(errorData).concat("#").concat(LUM2Constants.STATUS_S);
							transactionList.add(str);
						} else if ( (errorMsgList.size()!=0) || (rejectedMsgList.size()!=0) || (paymentFlagStatus==false)) {
							// STATUS - FAILED

							if(errorMsgList.size()!=0) {
								errorData = (String) errorMsgList.get(0);
								errorData = CommonUtil.pad(errorData, 50, " ");
								errorData = errorData.substring(0, 50);
							}
							str = str.concat(recordSuffixData).concat("#").concat(errorData).concat("#").concat(LUM2Constants.STATUS_F);
							transactionList.add(str);
							rejectedMessageList.addAll(rejectedMsgList);
						}
						supplementaryPaymentRecordBean.setSupPaymentErrorMsg(errorMsgList);
						supplementaryPaymentRecordBean.setSupPaymentSuccessMsg(successMsgList);
						supplementaryPaymentRecordBean.setSupPaymentRejectedMsg(rejectedMsgList);
						supPaymentList.add(supplementaryPaymentRecordBean);
					} else if (parserName.equalsIgnoreCase(LUM2Constants.SUM)) {
						LUM2SumRecordBean sumRecordBean = (LUM2SumRecordBean) parser.parse(str);
						Map map = validateSumRecord(sumRecordBean, batchList,numberOfPayments, paymentAmount);
						List errorMsgList = (List) map.get("errorMsgList");
						List successMsgList = (List) map.get("successMsgList");
						List rejectedMsgList = (List) map.get("rejectedMsgList");
						String sumRecordData = "";
						boolean sumRecordStatus = false;
						BigInteger acceptedPaymentAmount = new BigInteger("0");
						int acceptedPayments = 0;
						String errorData = CommonUtil.pad(" ", 50, " ");
						
						logger.debug("SUM :: Error Msg List -->" + errorMsgList);
						// logger.debug("SUM :: success Msg List -->" + successMsgList);
						logger.debug("SUM :: Rejected Msg List -->" + rejectedMsgList);

						if ( (errorMsgList.size() == 0 ) && (acceptedTransaction != 0) ) {
							// STATUS - SUCCESS
							sumRecordData = str.concat(recordSuffixData).concat("#").concat(errorData).concat("#").concat(LUM2Constants.STATUS_S);
							sumRecordStatus = true;
							logger.debug("SUM Record ::::: TRUE");
							// transactionList.add(str);
							
						} else if ( (errorMsgList.size()!=0) || (rejectedMsgList.size()!=0) || (acceptedTransaction==0)) {
							// STATUS - FAILED
							if(errorMsgList.size()!=0) {
								errorData = (String) errorMsgList.get(0);
								errorData = CommonUtil.pad(errorData, 50, " ");
								errorData = errorData.substring(0, 50);
							}
							
							sumRecordData = str.concat(recordSuffixData).concat("#").concat(errorData).concat("#").concat(LUM2Constants.STATUS_F);
							sumRecordStatus = false;
							transactionList = rejectTransaction(transactionList);
							logger.debug("SUM Record ::::: FALSE");
							rejectedMessageList.addAll(rejectedMsgList);
							// transactionList.add(str);
						}
						/* If accepted payment transactions are equal to ZERO then 
						 * it means whole transactions of the batch should be rejected.
						 * Status of BATCH and SUM will be 'F'*/

						Map paymentAcceptedMap =  getPaymentAcceptedData(batchPaymentList);
					
						acceptedPayments = (Integer) paymentAcceptedMap.get("acceptedPayments");
						acceptedPaymentAmount = (BigInteger) paymentAcceptedMap.get("acceptedPaymentSum");
						if( (acceptedTransaction == 0) || (sumRecordStatus == false)){
							batchRecordData = batchRecordData.substring(0, batchRecordData.length()-1).concat(LUM2Constants.STATUS_F);
							rejectedList.add(batchRecordData);
							acceptedPayments = 0;
							acceptedPaymentAmount = new BigInteger("0");
							
						} 
						
						totalAcceptedPaymentAmount = totalAcceptedPaymentAmount.add(acceptedPaymentAmount);
						totalAcceptedPayments = totalAcceptedPayments +  acceptedPayments;
						logger.debug("totalAcceptedPaymentAmount->"+totalAcceptedPaymentAmount.longValue());
						logger.debug("totalAcceptedPayments->"+totalAcceptedPayments);
						transactionRecordList.add(batchRecordData); // Add the Batch Record
						transactionRecordList.addAll(transactionList); // Add the Payment, Sup-Payment/Invoice Record
						transactionRecordList.add(sumRecordData); // Add the Sum Record
								
						sumRecordBean.setSumErrorMsg(errorMsgList);
						sumRecordBean.setSumSuccessMsg(successMsgList);
						sumRecordBean.setSumRejectedMsg(rejectedMsgList);
						
						sumList.add(sumRecordBean);
						/* Reset the flag after the batch */
						acceptedTransaction = 0;
						batchRecordData = "";
						numberOfPayments = 0;
						paymentAmount = new BigInteger("0");
						
						batchPaymentList.removeAll(batchPaymentList);
						transactionList.removeAll(transactionList);
						
					}
					/*
					 * As per the discussion this validation has been removed.
					 * -------------------------------------------------------
					 * else
					 * if(parserName.equalsIgnoreCase(CBPConstants.SUPPLEMENTARY_FEEDBACK)) {
					 * CBPSupplementaryFeedbackRecordBean
					 * supplementaryFeedbackRecordBean =
					 * (CBPSupplementaryFeedbackRecordBean)parser.parse(str);
					 * 
					 * Map map = validateSupplementaryFeedbackRecord(supplementaryFeedbackRecordBean);
					 * List errorMsgList = (List)map.get("errorMsgList"); List
					 * successMsgList = (List)map.get("successMsgList");
					 *  // This loop is only to print -> SUPPLEMENTARY_FEEDBACK
					 *  // Error/Rejected/Success Message List 
					 *  for (Iterator iter =
					 * successMsgList.iterator(); iter.hasNext();) { String
					 * element = (String) iter.next();
					 * logger.debug("Success Msg List ----->"+element);
					 *  }
					 * 
					 * for (Iterator iter = errorMsgList.iterator();
					 * iter.hasNext();) { String element = (String) iter.next();
					 * logger.debug("Error Msg List ----->"+element); }
					 * 
					 * supFeedBackList.add(supplementaryFeedbackRecordBean); }
					 */
				} else {
					commonErrorList.add(LUM2Constants.RECORD_CODE_ERROR);
					logger.debug(LUM2Constants.RECORD_CODE_ERROR);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("Exception in getAcceptReject()"+ e.getMessage());
		}

		ackMap.put("batchRecordList", batchList);
		ackMap.put("paymentRecordList", paymentList);
		ackMap.put("invoiceRecordList", invoiceList);
		ackMap.put("sumRecordList", sumList);
		ackMap.put("supPaymentList", supPaymentList);
		ackMap.put("commonErrorList", commonErrorList);
		ackMap.put("rejectedList", rejectedList);
		ackMap.put("totalAcceptedPaymentAmount", totalAcceptedPaymentAmount);
		ackMap.put("totalAcceptedPayments", totalAcceptedPayments);
		ackMap.put("transactionList", transactionRecordList);
		ackMap.put("rejectedMessageList", rejectedMessageList);
		
		return ackMap;
	}

	public static List rejectTransaction(List<String> list) {
		List updatedList = new LinkedList<String>();

		for (String str : list) {
			str = str.substring(0, str.length()-1).concat("F");
			updatedList.add(str);
		}
		return updatedList;
	}
	
	/**
	 * function <b>validateBatchRecord()</b> <br>
	 * This function is used to perform following operation..
	 * <ol>
	 * <li> Validate the batch records of the file.
	 * <li> Return the error, success and rejected message List in a
	 * CBPBatchRecordBean bean object.
	 * <li> Show the errors in case any problem or exceptions.
	 */

	@SuppressWarnings("unchecked")
	private static Map validateBatchRecord(Object... objects) {
		List errorList = new ArrayList();
		List successList = new ArrayList();
		List rejectedList = new ArrayList();
		Map map = new HashMap();
		LUM2BatchRecordBean batchRecordBean = (LUM2BatchRecordBean) objects[0];

		try {

			/* ----------------1. RECORD LENGTH VALIDATION ------------ */

			if (((batchRecordBean.getLineLength()) == LUM2Constants.LINE_LENGTH)
					&& (checkBatchMandetoryFields(batchRecordBean))) {
				successList.add(LUM2Constants.RECORD_LENGTH_SUCCESS);
				// logger.debug("BATCH - RECORD_LENGTH_SUCCESS");
			} else {
				errorList.add(LUM2Constants.RECORD_LENGTH_ERROR);
				rejectedList.add(LUM2Constants.RECORD_LENGTH_ERROR);
				logger.debug("BATCH - RECORD_LENGTH_ERROR");
			}

			/* ----------------2. APPLICATION CODE VALIDATION ------------ */

			if (batchRecordBean.getApplicationCode().equals(
					LUM2Constants.APPLICATION_CODE_LUM2)
					|| batchRecordBean.getApplicationCode().equals(
							LUM2Constants.APPLICATION_CODE_VLU2)) {
				successList.add(LUM2Constants.APPLICATION_CODE_SUCCESS);
				// logger.debug("BATCH - APPLICATION_CODE_SUCCESS");
			} else {
				errorList.add(LUM2Constants.APPLICATION_CODE_ERROR.concat(", "
						+ batchRecordBean.getApplicationCode()));
				logger.debug("BATCH - APPLICATION_CODE_ERROR");
			}

			/* ----------------3. RECORD CODE VALIDATION ------------ */

			if (batchRecordBean.getRecordCode().equals(LUM2Constants.ZERO)
					|| batchRecordBean.getRecordCode().equals(LUM2Constants.ONE)
					|| batchRecordBean.getRecordCode().equals(LUM2Constants.TWO)
					|| batchRecordBean.getRecordCode().equals(LUM2Constants.NINE)
					|| batchRecordBean.getRecordCode().equals(LUM2Constants.STATUS_L)
					|| batchRecordBean.getRecordCode().equals(LUM2Constants.STATUS_P)) {
				successList.add(LUM2Constants.RECORD_CODE_SUCCESS);
				// logger.debug("BATCH - RECORD_CODE_SUCCESS");
			} else {
				errorList.add(LUM2Constants.RECORD_CODE_ERROR.concat(", "+ batchRecordBean.getRecordCode()));
				logger.debug("BATCH - RECORD_CODE_ERROR");
			}

			/* ----------------4. FILE TYPE VALIDATION ------------ */

			if (batchRecordBean.getFileType().equals(LUM2Constants.ZERO)) {
				successList.add(LUM2Constants.FILE_TYPE_FILE);
				logger.debug("BATCH - FILE_TYPE_FILE");
			} else if (batchRecordBean.getFileType().equals(LUM2Constants.NINE)) {
				successList.add(LUM2Constants.FILE_TYPE_FEEDBACK);
				logger.debug("BATCH - FILE_TYPE_FEEDBACK");
			} else {
				errorList.add(LUM2Constants.FILE_TYPE_ERROR.concat(", "	+ batchRecordBean.getFileType()));
				logger.debug("BATCH - FILE_TYPE_ERROR");
			}

			/* ----------------5. DEBIT DATE VALIDATION ------------ */
			try {

				String dueDate = batchRecordBean.getDebitDate();
				/*
				 * Added by Debadatta Mishra to fix the issue related to
				 * due date validation. System has to validate whether it is
				 * a correct date or not, For example 31-Nov-2008 or 31-Apr-2008.
				 */
				boolean isDate = DateUtil.isDateValid(dueDate.trim(), "ddMMyyyy");
				boolean isValidDate = PaymentDbImpl.getDueDateValidations(dueDate.trim(), "yyyyMMdd", "yyMMdd");
				boolean dueDateFlag = (isDate == true && isValidDate == true ) ? true : false;
				logger.debug("Due date flag for Cross Border Payment:::"+dueDateFlag);	
//				if (PaymentDbImpl.getDueDateValidations(dueDate.trim(), "yyyyMMdd", "yyMMdd")) {
				if (dueDateFlag) {
					logger.debug("Batch Record : getDueDateValidations :: TRUE");
					successList.add(LUM2Constants.DUE_DATE_SUCCESS);
				} else {
					logger.debug("Batch Record : getDueDateValidations :: FALSE");
					errorList.add(LUM2Constants.DUE_DATE_ERROR.concat(", "	+ batchRecordBean.getDebitDate()));
					rejectedList.add(LUM2Constants.DUE_DATE_ERROR.concat(", " + batchRecordBean.getDebitDate()));
				}
			} catch (Exception e) {
				logger.debug("Batch Record : getDueDateValidations"	+ e.getMessage());
			}

			/* ----------------6. CREATION TIME VALIDATION ------------ */

			// ############################################
			/* ----------------7. PAYERS BUSINESS IDENTITY CODE ------------ */

			if (PaymentDbImpl.checkBusinessIdentityCode(batchRecordBean
					.getPayersBusinessIdentityCode())) {
				successList.add(LUM2Constants.PAYERS_BUSINESS_IDENTITY_CODE_SUCCESS);
			} else {
				errorList.add(LUM2Constants.PAYERS_BUSINESS_IDENTITY_CODE_ERROR.concat(", "
								+ batchRecordBean.getPayersBusinessIdentityCode()));
			}

		} catch (Exception e) {
			logger.debug("Exception :: To validate Batch Record");
		}

		/* ---------------- RETURN THE MAP AFTER VALIDATION ------------ */
		map.put("errorMsgList", errorList);
		map.put("successMsgList", successList);
		map.put("rejectedMsgList", rejectedList);

		return map;
	}

	/**
	 * function <b>validatePaymentRecord()</b> <br>
	 * This function is used to perform following operation..
	 * <ol>
	 * <li> Validate the payment records of the file.
	 * <li> Return the error, success and rejected message List in a
	 * CBPPaymentRecordBean's bean object.
	 * <li> Show the errors in case any problem or exceptions.
	 */
	
	@SuppressWarnings("unchecked")
	private static Map validatePaymentRecord(Object... objects) {
		List errorList = new ArrayList();
		List successList = new ArrayList();
		List rejectedList = new ArrayList();
		Map map = new HashMap();

		LUM2PaymentRecordBean paymentRecordBean = (LUM2PaymentRecordBean) objects[0];
		BigInteger number = (BigInteger) objects[1];
		Session session = (Session) objects[2];

		String serviceBureau = (String) session.getAttribute(ServerConstants.SERVICEBUREAUID);
		String serviceType = (String) session.getAttribute(ServerConstants.SERVICECODE);
		String serviceId = (String) session.getAttribute(ServerConstants.SERVICEID);

		try {
			/* ---- 1. RECORD LENGTH VALIDATION ------- */

			if (((paymentRecordBean.getLineLength()) == LUM2Constants.LINE_LENGTH)
					&& (checkPaymentMandetoryFields(paymentRecordBean))) {
				successList.add(LUM2Constants.RECORD_LENGTH_SUCCESS);
				// logger.debug("PAYMENT - RECORD_LENGTH_SUCCESS");
			} else {
				errorList.add(LUM2Constants.RECORD_LENGTH_ERROR);
				rejectedList.add(LUM2Constants.RECORD_LENGTH_ERROR);
				logger.debug("PAYMENT - RECORD_LENGTH_ERROR");
			}

			/* ---- 2. BENEFICIARY NAME ADDRESS VALIDATION ----- */

			if (paymentRecordBean.getBeneficiaryNameAddress().trim().equals("")) {
				errorList.add(LUM2Constants.BENEFICIARY_NAME_ADDRESS_ERROR.concat(", "+ paymentRecordBean.getBeneficiaryNameAddress()));
				logger.debug("PAYMENT - BENEFICIARY_NAME_ADDRESS_ERROR");
			} else {
				successList.add(LUM2Constants.BENEFICIARY_NAME_ADDRESS_SUCCESS);
				// logger.debug("PAYMENT - BENEFICIARY_NAME_ADDRESS_SUCCESS");
			}

			/* ---- 3. BENEFICIARY COUNTRY CODE VALIDATION ----- */

			if (((paymentRecordBean.getBeneficiaryCountryCode().trim().equals("")) 
					&& (paymentRecordBean.getSwiftCode().trim().equals("")))) {
				errorList.add(LUM2Constants.BENEFICIARY_COUNTRY_CODE_ERROR.concat(", "+ paymentRecordBean.getBeneficiaryCountryCode()));
				logger.debug("PAYMENT - BENEFICIARY_COUNTRY_CODE_ERROR");
			} else {
				successList.add(LUM2Constants.BENEFICIARY_COUNTRY_CODE_SUCCESS);
				// logger.debug("PAYMENT - BENEFICIARY_COUNTRY_CODE_SUCCESS");
			}

			/* ----4. SWIFT CODE VALIDATION ------------ */

			if (((paymentRecordBean.getSwiftCode().trim().equals("")) && (paymentRecordBean
					.getBeneficiaryCountryCode().trim().equals("")))) {
				errorList.add(LUM2Constants.SWIFT_CODE_ERROR.concat(", " + paymentRecordBean.getSwiftCode()));
				logger.debug("PAYMENT - SWIFT_CODE_ERROR");
			} else {
				successList.add(LUM2Constants.SWIFT_CODE_SUCCESS);
				// logger.debug("PAYMENT - SWIFT_CODE_SUCCESS");
			}

			/* ----5. BENEFICIARY'S BANK NAME AND ADDRESS VALIDATION ----- */
			//Modified by Debadatta Mishra at onsite to remove the validation
			//for beneficiary bank name and address.
			if (paymentRecordBean.getBeneficiaryBankNameAddress().trim().equals(""))  
			{ 
//				errorList.add(LUM2Constants.BENEFICIARY_BANK_NAME_ADDRESS_ERROR .concat(", " + paymentRecordBean.getBeneficiaryBankNameAddress()));
//				logger.debug("PAYMENT - BENEFICIARY_BANK_NAME_ADDRESS_ERROR");
				successList.add(LUM2Constants.BENEFICIARY_BANK_NAME_ADDRESS_SUCCESS);
			}
			else 
			{
				successList.add(LUM2Constants.BENEFICIARY_BANK_NAME_ADDRESS_SUCCESS);
				// logger.debug("PAYMENT - BENEFICIARY_BANK_NAME_ADDRESS_SUCCESS");
			}

			/* ----6. BENEFICIARY'S ACCOUNT NUMBER VALIDATION ----- */
			//Added by Debadatta Mishra at onsite to eliminate
			// IBAN number validation. There is no IBAN number
			// validation in PATU server
			if( paymentRecordBean.getBeneficiaryAccountCode() != null )
			{
				successList.add(LUM2Constants.IBAN_ACCOUNT_NUMBER_SUCCESS);
				logger.debug("PAYMENT - IBAN_ACCOUNT_NUMBER_SUCCESS");
			}
			else
			{
				errorList.add(LUM2Constants.IBAN_ACCOUNT_NUMBER_ERROR.concat(", "
						+ paymentRecordBean.getBeneficiaryAccountCode()));
			rejectedList.add(LUM2Constants.IBAN_ACCOUNT_NUMBER_ERROR.concat(", "
						+ paymentRecordBean.getBeneficiaryAccountCode()));
			logger.debug("PAYMENT - IBAN_ACCOUNT_NUMBER_ERROR");
			}
//			if (AccountNumberValidation.checkIBANAccountNumber(paymentRecordBean.getBeneficiaryAccountCode().trim())) {
//				successList.add(LUM2Constants.IBAN_ACCOUNT_NUMBER_SUCCESS);
//				logger.debug("PAYMENT - IBAN_ACCOUNT_NUMBER_SUCCESS");
//			} else {
//				errorList.add(LUM2Constants.IBAN_ACCOUNT_NUMBER_ERROR.concat(", "
//							+ paymentRecordBean.getBeneficiaryAccountCode()));
//				rejectedList.add(LUM2Constants.IBAN_ACCOUNT_NUMBER_ERROR.concat(", "
//							+ paymentRecordBean.getBeneficiaryAccountCode()));
//				logger.debug("PAYMENT - IBAN_ACCOUNT_NUMBER_ERROR");
//			}

			/* ----7. CURRENCY AMOUNT OF PAYMENT VALIDATION -----*/

			if ((Integer.parseInt(paymentRecordBean.getCurrencyAmount()) > 0)
					|| (Integer.parseInt(paymentRecordBean.getDebitingAmount()) > 0)) {
				successList.add(LUM2Constants.CURRENCY_AMOUNT_SUCCESS);
				// logger.debug("CURRENCY_AMOUNT_SUCCESS");

				try {
					if (Integer.parseInt(paymentRecordBean.getCurrencyAmount()) == 0) {
						number = number.add(new BigInteger(paymentRecordBean
								.getDebitingAmount().trim()));
					} else {
						number = number.add(new BigInteger(paymentRecordBean
								.getCurrencyAmount().trim()));
					}
				} catch (Exception e) {
					logger.debug("Exception :: On calculating the total amount"
									+ e.getMessage());
				}
				
			} else {
				errorList.add(LUM2Constants.CURRENCY_AMOUNT_ERROR.concat(", "
						+ paymentRecordBean.getCurrencyAmount()));
				logger.debug("CURRENCY_AMOUNT_ERROR");
			}

			/* -----8. CURRENCY CODE VALIDATION ------------ */

			// Query - SELECT 1 FROM dmtm_currency_defn WHERE
			// currency_code='EUR'
			if (PaymentDbImpl.checkCurrencyCode(paymentRecordBean
					.getCurrencyCode())) {
				successList.add(LUM2Constants.CURRENCY_CODE_SUCCESS);
				logger.debug("CURRENCY_CODE_SUCCESS");
			} else {
				errorList.add(LUM2Constants.CURRENCY_CODE_ERROR.concat(", "
						+ paymentRecordBean.getCurrencyCode()));
				logger.debug("CURRENCY_CODE_ERROR");
			}

			/* -----9. PAYMENT TYPE VALIDATION ------------ */

			if (paymentRecordBean.getPaymentType().equals(
					LUM2Constants.STATUS_M)
					|| paymentRecordBean.getPaymentType().equals(
							LUM2Constants.STATUS_P)
					|| paymentRecordBean.getPaymentType().equals(
							LUM2Constants.STATUS_Q)
					|| paymentRecordBean.getPaymentType().equals(
							LUM2Constants.STATUS_S)
					|| paymentRecordBean.getPaymentType().equals(
							LUM2Constants.STATUS_T)
					|| paymentRecordBean.getPaymentType().equals(
							LUM2Constants.STATUS_K)
					|| paymentRecordBean.getPaymentType().equals(
							LUM2Constants.STATUS_U)) {
				successList.add(LUM2Constants.PAYMENT_TYPE_SUCCESS);
				// logger.debug("PAYMENT_TYPE_SUCCESS");
			} else {
				errorList.add(LUM2Constants.PAYMENT_TYPE_ERROR.concat(", "
						+ paymentRecordBean.getPaymentType()));
				logger.debug("PAYMENT_TYPE_ERROR");
			}

			/* ----- 10. SERVICE FEE VALIDATION ------------ */

			if (paymentRecordBean.getServiceFee()
					.equals(LUM2Constants.STATUS_J)
					|| paymentRecordBean.getServiceFee().equals(
							LUM2Constants.STATUS_T)) {
				successList.add(LUM2Constants.SERVICE_FEE_SUCCESS);
				// logger.debug("SERVICE_FEE_SUCCESS");
			} else {
				errorList.add(LUM2Constants.SERVICE_FEE_ERROR.concat(", "
						+ paymentRecordBean.getServiceFee()));
				logger.debug("SERVICE_FEE_ERROR");
			}

			/* ----- 11. DEBITING ACCOUNT VALIDATION ------------ */

			if ((AccountNumberValidation.checkAccountNumber(paymentRecordBean
					.getDebitingAccount()))
					&&	(PaymentDbImpl.checkOPEAccountNumber(paymentRecordBean
							.getDebitingAccount(), serviceId, serviceType,	serviceBureau)))

			{
				successList.add(LUM2Constants.ACCOUNT_NUMBER_SUCCESS);
				// logger.debug("ACCOUNT_NUMBER_SUCCESS");
			} else {
				errorList.add(LUM2Constants.ACCOUNT_NUMBER_ERROR.concat(", "
						+ paymentRecordBean.getDebitingAccount()));
				logger.debug("ACCOUNT_NUMBER_ERROR");
			}

			/* ----- 12. DEBITING ACCOUNT'S CURRENCY CODE VALIDATION ------------ */

			if (PaymentDbImpl.checkCurrencyCode(paymentRecordBean
					.getDebitingAccountCurrencyCode())) {
				successList.add(LUM2Constants.CURRENCY_CODE_SUCCESS);
				// logger.debug("CURRENCY_CODE_SUCCESS");
			} else {
				errorList.add(LUM2Constants.CURRENCY_CODE_ERROR.concat(", "
						+ paymentRecordBean.getDebitingAccountCurrencyCode()));
				logger.debug("CURRENCY_CODE_ERROR");
			}

			/* ----- 13. DEBITING AMOUNT VALIDATION ------------ */

			if ((Integer.parseInt(paymentRecordBean.getDebitingAmount()) > 0)
					|| (Integer.parseInt(paymentRecordBean.getCurrencyAmount()) > 0)) {
				successList.add(LUM2Constants.DEBITING_AMOUNT_SUCCESS);
				// logger.debug("DEBITING_AMOUNT_SUCCESS");
			} else {
				errorList.add(LUM2Constants.DEBITING_AMOUNT_ERROR.concat(", "
						+ paymentRecordBean.getDebitingAmount()));
				logger.debug("DEBITING_AMOUNT_ERROR");
			}

			/* -----14. FEEDBACK CURRENCY VALIDATION ------------ */

			if (paymentRecordBean.getFeedbackCurrency().equals(
					LUM2Constants.EURO_IS0_CODE)) {
				successList.add(LUM2Constants.FEEDBACK_CURRENCY_SUCCESS);
				// logger.debug("FEEDBACK_CURRENCY_SUCCESS");
			} else {
				errorList.add(LUM2Constants.FEEDBACK_CURRENCY_ERROR.concat(", "
						+ paymentRecordBean.getFeedbackCurrency()));
				logger.debug("FEEDBACK_CURRENCY_ERROR");
			}

			/* ----- RETURN THE MAP AFTER VALIDATION ------------ */

		} catch (Exception e) {
			logger.debug("Exception :: To validate Payment Record");
		}

		map.put("errorMsgList", errorList);
		map.put("successMsgList", successList);
		map.put("rejectedMsgList", rejectedList);
		map.put("amountAddition", number);

		return map;
	}

	/**
	 * function <b>validateInvoiceRecord()</b> <br>
	 * This function is used to perform following operation..
	 * <ol>
	 * <li> Validate the invoice records of the file.
	 * <li> Return the error, success and rejected message List in a
	 * CBPInvoiceRecordBean's bean object.
	 * <li> Show the errors in case any problem or exceptions.
	 */

	@SuppressWarnings("unchecked")
	private static Map validateInvoiceRecord(Object... objects) {

		List errorList = new ArrayList();
		List successList = new ArrayList();
		List rejectedList = new ArrayList();

		Map map = new HashMap();

		LUM2InvoiceRecordBean invoiceRecordBean = (LUM2InvoiceRecordBean) objects[0];

		try {

			/* ----------------1. RECORD LENGTH VALIDATION ------------ */

			if (((invoiceRecordBean.getLineLength()) == LUM2Constants.LINE_LENGTH)
					&& (checkInvoiceMandetoryFields(invoiceRecordBean))) {
				successList.add(LUM2Constants.RECORD_LENGTH_SUCCESS);
				// logger.debug("RECORD_LENGTH_SUCCESS");
			} else {
				errorList.add(LUM2Constants.RECORD_LENGTH_ERROR);
				rejectedList.add(LUM2Constants.RECORD_LENGTH_ERROR);
				logger.debug("RECORD_LENGTH_ERROR");
			}

			/* ----------------2. INVOICE TYPE VALIDATION ------------ */

			if (invoiceRecordBean.getInvoiceType().equals(LUM2Constants.ZERO)
					|| invoiceRecordBean.getInvoiceType().equals(
							LUM2Constants.TWO)) {
				successList.add(LUM2Constants.INVOICE_TYPE_SUCCESS);
				// logger.debug("INVOICE_TYPE_SUCCESS");
			} else {
				errorList.add(LUM2Constants.INVOICE_TYPE_ERROR.concat(", "
						+ invoiceRecordBean.getInvoiceType()));
				logger.debug("INVOICE_TYPE_ERROR");
			}

			/* ----------------3. CURRENCY AMOUNT VALIDATION ------------ */

			if ((Integer.parseInt(invoiceRecordBean.getCurrencyAmount()) > 0)) {
				successList.add(LUM2Constants.CURRENCY_AMOUNT_SUCCESS);
				// logger.debug("CURRENCY_AMOUNT_SUCCESS");
			} else {
				errorList.add(LUM2Constants.CURRENCY_AMOUNT_ERROR.concat(", "
						+ invoiceRecordBean.getCurrencyAmount()));
				logger.debug("CURRENCY_AMOUNT_ERROR");
			}

			/* ---------------- RETURN THE MAP AFTER VALIDATION ------------ */

		} catch (Exception e) {
			logger.debug("Exception :: To validate Invoice Record");
		}

		map.put("errorMsgList", errorList);
		map.put("successMsgList", successList);
		map.put("rejectedMsgList", rejectedList);

		return map;
	}

	/**
	 * function <b>validateSumRecord()</b> <br>
	 * This function is used to perform following operation..
	 * <ol>
	 * <li> Validate the sum records of the file.
	 * <li> Return the error, success and rejected message List in a
	 * CBPSumRecordBean's bean object.
	 * <li> Show the errors in case any problem or exceptions.
	 */

	@SuppressWarnings("unchecked")
	private static Map validateSumRecord(Object... objects) {

		List errorList = new ArrayList();
		List successList = new ArrayList();
		List rejectedList = new ArrayList();
		List batchList = new ArrayList();
		LUM2BatchRecordBean batchRecordBean = null;
		Map map = new HashMap();

		LUM2SumRecordBean sumRecordBean = (LUM2SumRecordBean) objects[0];
		batchList = (List) objects[1];
		if (batchList.size() > 0) {
			batchRecordBean = (LUM2BatchRecordBean) batchList.get(0);
		}
		int numberOfPayments = (Integer) objects[2];
		BigInteger paymentsCurrencyAmount = (BigInteger) objects[3];

		try {

			/* ----------------1. RECORD LENGTH VALIDATION ------------ */

			if (((sumRecordBean.getLineLength()) == LUM2Constants.LINE_LENGTH)
					&& (checkSumMandetoryFields(sumRecordBean))) {
				successList.add(LUM2Constants.RECORD_LENGTH_SUCCESS);
				// logger.debug("RECORD_LENGTH_SUCCESS");
			} else {
				errorList.add(LUM2Constants.RECORD_LENGTH_ERROR);
				rejectedList.add(LUM2Constants.RECORD_LENGTH_ERROR);
				logger.debug("RECORD_LENGTH_ERROR");
			}

			/* ----------------2. PAYERS BUSINESS IDENTITY CODE ------------ */

			if ((sumRecordBean.getPayerBIC()).equals(batchRecordBean
					.getPayersBusinessIdentityCode())) {
				successList
						.add(LUM2Constants.PAYERS_BUSINESS_IDENTITY_CODE_SUCCESS);
			} else {
				errorList.add(LUM2Constants.PAYERS_BUSINESS_IDENTITY_CODE_ERROR
						.concat(", " + sumRecordBean.getPayerBIC()));
			}

			/* ----------------3. NUMBER OF PAYMENTS - VALIDATION ------------ */

			logger.debug("SUM : numberOfPayments in Batch ---->"+ numberOfPayments);
			logger.debug("SUM : numberOfPayments in SUM ---->"+ sumRecordBean.getNumberOfPayments());

			if (Integer.parseInt(sumRecordBean.getNumberOfPayments().trim()) == numberOfPayments) {
				successList.add(LUM2Constants.NUMBER_OF_PAYMENT_SUCCESS);
			} else {
				errorList.add(LUM2Constants.NUMBER_OF_PAYMENT_ERROR.concat(", "
						+ sumRecordBean.getNumberOfPayments()));
				rejectedList.add(LUM2Constants.NUMBER_OF_PAYMENT_ERROR
						.concat(", " + sumRecordBean.getNumberOfPayments()));

			}

			/*
			 * ------- 4. CURRENCY AMOUNT OF PAYMENTS - VALIDATION ------------
			 */

			BigInteger sumCurrencyAmount = new BigInteger(sumRecordBean.getPaymentsCurrencyAmount().trim());

			logger.debug("SUM : paymentsCurrencyAmount in Batch ---->"+ paymentsCurrencyAmount.longValue());
			logger.debug("SUM : sumCurrencyAmount in SUM ---->"+ sumCurrencyAmount.longValue());

			
			if (paymentsCurrencyAmount.longValue() == sumCurrencyAmount.longValue()) {
				successList.add(LUM2Constants.SUM_OF_PAYMENT_AMOUNT_SUCCESS);
			} else {
				errorList.add(LUM2Constants.SUM_OF_PAYMENT_AMOUNT_ERROR
						.concat(", "+ sumRecordBean.getPaymentsCurrencyAmount().trim()));
				rejectedList.add(LUM2Constants.SUM_OF_PAYMENT_AMOUNT_ERROR
						.concat(", "+ sumRecordBean.getPaymentsCurrencyAmount().trim()));
			}

			/* ---------------- RETURN THE MAP AFTER VALIDATION ------------ */

		} catch (Exception e) {
			logger.debug("Exception :: To validate Sum Record");
		}
		map.put("errorMsgList", errorList);
		map.put("successMsgList", successList);
		map.put("rejectedMsgList", rejectedList);
		logger.debug("SUM : map.size() ::  ---->"+ map.size());
		return map;
	}

	/**
	 * function <b>validateSupplementaryFeedbackRecord()</b> <br>
	 * This function is used to perform following operation..
	 * <ol>
	 * <li> Validate the supplementary feedback records of the file.
	 * <li> Return the error, success and rejected message List in a
	 * CBPSupplementaryFeedbackRecordBean's bean object.
	 * <li> Show the errors in case any problem or exceptions.
	 */

	@SuppressWarnings("unchecked")
	private static Map validateSupplementaryFeedbackRecord(Object... objects) {

		List errorList = new ArrayList();
		List successList = new ArrayList();
		List rejectedList = new ArrayList();
		Map map = new HashMap();

		LUM2SupplementaryFeedbackRecordBean supplementaryFeedbackBean = (LUM2SupplementaryFeedbackRecordBean) objects[0];

		try {

			/* ----------------1. RECORD LENGTH VALIDATION ------------ */

			if (((supplementaryFeedbackBean.getLineLength()) == LUM2Constants.LINE_LENGTH)
					&& (checkSupFeedbackMandetoryFields(supplementaryFeedbackBean))) {
				successList.add(LUM2Constants.RECORD_LENGTH_SUCCESS);
				// logger.debug("RECORD_LENGTH_SUCCESS");
			} else {
				errorList.add(LUM2Constants.RECORD_LENGTH_ERROR);
				rejectedList.add(LUM2Constants.RECORD_LENGTH_ERROR);
				logger.debug("RECORD_LENGTH_ERROR");
			}

			/* ---------------- RETURN THE MAP AFTER VALIDATION ------------ */

		} catch (Exception e) {
			logger.debug("Exception :: To validate Supplementary Feedback Record");
		}

		map.put("errorMsgList", errorList);
		map.put("successMsgList", successList);
		map.put("rejectedMsgList", rejectedList);

		return map;
	}

	/**
	 * function <b>validateSupplementaryPaymentsRecord()</b> <br>
	 * This function is used to perform following operation..
	 * <ol>
	 * <li> Validate the supplementary payments records of the file.
	 * <li> Return the error, success and rejected message List in a
	 * CBPSupplementaryPaymentRecordBean's bean object.
	 * <li> Show the errors in case any problem or exceptions.
	 */

	@SuppressWarnings("unchecked")
	private static Map validateSupplementaryPaymentsRecord(Object... objects) {

		List errorList = new ArrayList();
		List successList = new ArrayList();
		List rejectedList = new ArrayList();

		Map map = new HashMap();

		LUM2SupplementaryPaymentRecordBean supplementaryFeedbackBean = (LUM2SupplementaryPaymentRecordBean) objects[0];

		try {
			/* ----------------1. RECORD LENGTH VALIDATION ------------ */

			if (((supplementaryFeedbackBean.getLineLength()) == LUM2Constants.LINE_LENGTH)
					&& (checkSupPaymentMandetoryFields(supplementaryFeedbackBean))) {
				successList.add(LUM2Constants.RECORD_LENGTH_SUCCESS);
				// logger.debug("RECORD_LENGTH_SUCCESS");
			} else {
				errorList.add(LUM2Constants.RECORD_LENGTH_ERROR);
				rejectedList.add(LUM2Constants.RECORD_LENGTH_ERROR);
				logger.debug("RECORD_LENGTH_ERROR");
			}

			/* ---------------- RETURN THE MAP AFTER VALIDATION ------------ */

		} catch (Exception e) {
			logger.debug("Exception :: To validate Supplementary Payment Record");
		}

		map.put("errorMsgList", errorList);
		map.put("successMsgList", successList);
		map.put("rejectedMsgList", rejectedList);

		return map;
	}

	/**
	 * function <b>checkBatchMandetoryFields()</b> <br>
	 * This function is used to check the mandetory fields for Batch records.
	 * <br>
	 * 
	 * @param CBPBatchRecordBean
	 *            batchRecordBean <br>
	 * @return boolean
	 */

	private static boolean checkBatchMandetoryFields(
			LUM2BatchRecordBean batchRecordBean) {
		boolean status = false;
		try {
			if ((batchRecordBean.getApplicationCode().trim().equals(""))
					|| (batchRecordBean.getRecordCode().trim().equals(""))
					|| (batchRecordBean.getFileType().trim().equals(""))
					|| (batchRecordBean.getPayersBusinessIdentityCode().trim()
							.equals(""))
					|| (batchRecordBean.getFileCreationTime().trim().equals(""))) {
				status = false;
				logger.debug("Batch Record :: Mandetory Fields are missed.");
			} else {
				status = true;
			}
		} catch (Exception e) {
			logger.debug("Exception :: To check Batch Record Mandetory Fields"
							+ e.getMessage());
			status = false;
		}

		return status;
	}

	/**
	 * function <b>checkPaymentMandetoryFields()</b> <br>
	 * This function is used to check the mandetory fields for Payments records.
	 * <br>
	 * 
	 * @param CBPPaymentRecordBean
	 *            paymentRecordBean <br>
	 * @return boolean
	 */

	private static boolean checkPaymentMandetoryFields(
			LUM2PaymentRecordBean paymentRecordBean) {
		boolean status = false;
		try {
			if ((paymentRecordBean.getApplicationCode().trim().equals(""))
					|| (paymentRecordBean.getRecordCode().trim().equals(""))
					|| (paymentRecordBean.getBeneficiaryNameAddress().trim()
							.equals(""))
					|| (paymentRecordBean.getBeneficiaryCountryCode().trim()
							.equals(""))
					|| (paymentRecordBean.getCurrencyAmount().trim().equals(""))
					|| (paymentRecordBean.getCurrencyCode().trim().equals(""))
					|| (paymentRecordBean.getPaymentType().trim().equals(""))
					|| (paymentRecordBean.getServiceFee().trim().equals(""))
					|| (paymentRecordBean.getDebitingAccount().trim()
							.equals(""))
					|| (paymentRecordBean.getDebitingAccountCurrencyCode()
							.trim().equals(""))) {
				status = false;
				logger.debug("Payment Record :: Mandetory Fields are missed.");
			} else {
				status = true;
			}
		} catch (Exception e) {
			logger.debug("Exception :: To check Payment Record Mandetory Fields"
							+ e.getMessage());
			status = false;
		}
		return status;
	}

	/**
	 * function <b>checkInvoiceMandetoryFields()</b> <br>
	 * This function is used to check the mandetory fields for Invoice records.
	 * <br>
	 * 
	 * @param CBPInvoiceRecordBean
	 *            invoiceRecordBean <br>
	 * @return boolean
	 */

	private static boolean checkInvoiceMandetoryFields(
			LUM2InvoiceRecordBean invoiceRecordBean) {
		boolean status = false;
		try {
			if ((invoiceRecordBean.getApplicationCode().trim().equals(""))
					|| (invoiceRecordBean.getRecordCode().trim().equals(""))
					|| (invoiceRecordBean.getFileType().trim().equals(""))
					|| (invoiceRecordBean.getCurrencyCode().trim().equals(""))
					|| (invoiceRecordBean.getInvoiceType().trim().equals(""))
					|| (invoiceRecordBean.getCurrencyAmount().trim().equals(""))) {
				status = false;
				logger.debug("Invoice Record :: Mandetory Fields are missed.");
			} else {
				status = true;
			}
		} catch (Exception e) {
			logger.debug("Exception :: To check Invoice Record Mandetory Fields"
							+ e.getMessage());
			status = false;
		}
		return status;
	}

	/**
	 * function <b>checkSumMandetoryFields()</b> <br>
	 * This function is used to check the mandetory fields for Sum records. <br>
	 * 
	 * @param CBPSumRecordBean
	 *            sumRecordBean <br>
	 * @return boolean
	 */

	private static boolean checkSumMandetoryFields(
			LUM2SumRecordBean sumRecordBean) {
		boolean status = false;
		try {
			if ((sumRecordBean.getApplicationCode().trim().equals(""))
					|| (sumRecordBean.getRecordCode().trim().equals(""))
					|| (sumRecordBean.getFileType().trim().equals(""))
					|| (sumRecordBean.getPayerBIC().trim().equals(""))
					|| (sumRecordBean.getNumberOfPayments().trim().equals(""))
					|| (sumRecordBean.getPaymentsCurrencyAmount().trim()
							.equals(""))) {
				status = false;
				logger.debug("Sum Record :: Mandetory Fields are missed.");
			} else {
				status = true;
			}
		} catch (Exception e) {
			logger.debug("Exception :: To check Sum Record Mandetory Fields"
							+ e.getMessage());
			status = false;
		}
		return status;
	}

	/**
	 * function <b>checkSupFeedbackMandetoryFields()</b> <br>
	 * This function is used to check the mandetory fields for Supplementary
	 * Feedback records. <br>
	 * 
	 * @param CBPSupplementaryFeedbackRecordBean
	 *            supFeedbackRecordBean <br>
	 * @return boolean
	 */

	private static boolean checkSupFeedbackMandetoryFields(
			LUM2SupplementaryFeedbackRecordBean supFeedbackRecordBean) {
		boolean status = false;
		try {
			if ((supFeedbackRecordBean.getApplicationCode().trim().equals(""))
					|| (supFeedbackRecordBean.getRecordCode().trim().equals(""))) {
				status = false;
				logger.debug("Sup Feedback Record :: Mandetory Fields are missed.");
			} else {
				status = true;
			}
		} catch (Exception e) {
			logger.debug("Exception :: To check Sup Feedback Record Mandetory Fields"
							+ e.getMessage());
			status = false;
		}
		return status;
	}

	/**
	 * function <b>checkSupPaymentMandetoryFields()</b>
	 * <br> This function is used to check the mandetory fields for Supplementary Payment records.
	 * <br> @param CBPSupplementaryPaymentRecordBean supPaymentRecordBean
	 * <br> @return boolean
	 */

	private static boolean checkSupPaymentMandetoryFields(
			LUM2SupplementaryPaymentRecordBean supPaymentRecordBean) {
		boolean status = false;
		try {
			if ((supPaymentRecordBean.getApplicationCode().trim().equals(""))
					|| (supPaymentRecordBean.getRecordCode().trim().equals(""))) {
				status = false;
				logger.debug("Sup Payment Record :: Mandetory Fields are missed.");
			} else {
				status = true;
			}
		} catch (Exception e) {
			logger.debug("Exception :: To check Sup Payment Record Mandetory Fields"
							+ e.getMessage());
			status = false;
		}
		return status;
	}
}