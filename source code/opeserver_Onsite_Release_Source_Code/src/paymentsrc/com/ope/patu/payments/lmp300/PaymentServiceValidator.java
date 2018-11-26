package com.ope.patu.payments.lmp300;

import java.io.File;
import java.io.FileOutputStream;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import com.coldcore.coloradoftp.session.Session;
import com.ope.patu.payment.core.Parser;
import com.ope.patu.payment.core.VelocityTemplate;
import com.ope.patu.payments.lmp300.ParserFactory;
import com.ope.patu.payments.lmp300.PaymentValidator;
import com.ope.patu.payments.lmp300.PaymentValidatorFactory;
import com.ope.patu.payments.lmp300.SumRecordValidatorImpl;
import com.ope.patu.payments.lmp300.beans.PaymentServiceBeanRC0;
import com.ope.patu.payments.lmp300.beans.PaymentServiceBeanRC1;
import com.ope.patu.payments.lmp300.beans.PaymentServiceBeanRC9;
import com.ope.patu.payments.lmp300.PaymentConstants;
import com.ope.patu.payments.ts.SalaryConstants;
import com.ope.patu.server.beans.MessageBean;
import com.ope.patu.server.beans.TransferRequestBean;
import com.ope.patu.server.constant.ErrorConstants;
import com.ope.patu.server.constant.ServerConstants;
import com.ope.patu.util.CommonUtil;
import com.ope.patu.util.DateUtil;
import com.ope.patu.util.FileUtil;
import com.ope.patu.payment.core.CommonPaymentConstant;

public class PaymentServiceValidator 
{
	protected static Logger logger = Logger.getLogger(PaymentServiceValidator.class);
	/**
	 * This method returns the parser name depends on record code and transaction type
	 * @param recordCode
	 * @param transactionType
	 * @return
	 */

	public static String getParserName( String recordCode , String transactionType )
	{ 
		String parserName = null;
				
		if(recordCode.equals(PaymentConstants.ZERO)&& transactionType.equals(PaymentConstants.ZERO)){
			//Batch
			parserName = PaymentConstants.BATCH;
		}else if(recordCode.equals(PaymentConstants.ONE)&& transactionType.equals(PaymentConstants.ZERO) || transactionType.equals(PaymentConstants.TWO)){
			//Transaction
			parserName = PaymentConstants.TRANSACTIONS;
		}else if(recordCode.equals(PaymentConstants.TWO)&& transactionType.equals(PaymentConstants.ZERO) ){
			//Itemisation
			parserName = PaymentConstants.ITEMISATION;
		}else if(recordCode.equals(PaymentConstants.NINE)&& transactionType.equals(PaymentConstants.ZERO)){
			//Sum
			parserName = PaymentConstants.SUM;
		}else if(recordCode.equals(PaymentConstants.ONE)&& transactionType.equals(PaymentConstants.NINE)){
			//Message belongs to transaction
			parserName = PaymentConstants.MESSAGETRANS;
		}else if(recordCode.equals(PaymentConstants.TWO)&& transactionType.equals(PaymentConstants.NINE)){
			//Message belongs to itemisation
			parserName = PaymentConstants.MESSAGEITEM;
		}else{
			parserName = PaymentConstants.INVALIDRECORD;
		}
		return parserName;
	}

	/**
	 * Method takes the input parameter as records and session, it will do the all bill payment service validations
	 * and generates the acknowledgment, feedback file, patu file and returns the map for PTE message    
	 * @param objects
	 * @return
	 */
	public void billPaymentService( Object...objects )
	{
		List batchList = new ArrayList();
		Map ackMap = new LinkedHashMap();
		List <String> contentsList = (List<String>)objects[0];
		Session session = ( Session )objects[1];
		Parser parser = null;
		int transcount=0;
		int errmsg=0;
		String transPayeesAccNo="";
		String transMessageType="";
		String transSum = "";
		PaymentServiceBeanRC0 paymentServiceBeanRC0 = null;
		double totalTransactionSum = 0.0;
		int seqCount = 0;
		int rejected = 0;
		int accepted = 0;
		int temp=0;
		int creditNoteTransactions = 0;
		int ackAcceptedTransactions = 0;
		int auditAcceptedTransactions = 0;
		int ackRejectedTransactions = 0;
		int rejectedCreditNoteTransaction=0;
		int totalCreditNoteTransaction=0;
		int batchCount=0;
		double rejectedSum = 0;
		double acceptedSum = 0;
		double tempacceptedsum=0;
		double rejectedCreditNoteSum=0.0;
		double totalCreditNoteSum=0.0;
		double acceptedCreditNoteSum = 0;
		double tempCreditNoteSum=0.0;
		String strAcceptedCreditNoteSum = PaymentConstants.DECIMAL;
		String strAcceptedSum = PaymentConstants.DECIMAL;
		String strRejectedSum = PaymentConstants.DECIMAL;
		double ackacceptedsum = 0.00;
		double ackacceptedCreditNotesum = 0.00;
		double tempAckAcceptedSum=0.00;
		String acceptedsum = "";
		String tempAcceptedSum = "";

		String strAcceptedCreditNotesum = "";
		String successItem="";
		String dateTime="";
		String fileCreationDate="";
		boolean success = false;
		int totalRecordCount=0;
		String sessionId = CommonUtil.pad(new Integer( session.getSessionId()).toString(), 9, " ");
		String serviceBureauId = CommonUtil.pad("#"+(String)session.getAttribute(ServerConstants.SERVICEBUREAUID),18," ");
		String aggrementId = CommonUtil.pad((String)"#"+session.getAttribute(ServerConstants.AGREEMENTTID),10," ");
		String serviceType = CommonUtil.pad((String)"#"+session.getAttribute(ServerConstants.SERVICECODE),9," ");
		String serviceID = CommonUtil.pad((String)"#"+session.getAttribute(ServerConstants.SERVICEID),11," ");
		String serviceCode = (String)session.getAttribute(ServerConstants.SERVICEID);
		long longTimeStamp = System.currentTimeMillis();
		/**
		 * To provide the format for the TimeStamp
		 */
		Date longDate = new Date( longTimeStamp );
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMddhhmmss");
		String timeStamp = simpleDateFormat.format(longDate);//new Long( longTimeStamp ).toString()+"_";

		String recordSuffixData = new StringBuffer().append(sessionId).append(serviceBureauId).
		append(aggrementId).append(serviceType).append(serviceID).append("#"+timeStamp).toString();

		int tempAccepted=0;
		int tempRejected=0;
		int tempAuditLog=0;
		String status = "";
		String currency = "";
		String formatString="";
		String creditNoteSum="";
		int totalTransaction = 0;
		int ackCreditNoteTransactions=0;
		List transErrorMsg = new ArrayList();
		List<String> transTempList = new LinkedList<String>();
		List itemizationErrorMsg = new ArrayList();
		List feedBackList = new ArrayList();
		List feedBackTransList = new ArrayList();
		List batchErrorMsg = new ArrayList();
		List sumErrorMsg = new ArrayList();
		Map feedBackMap = new HashMap();
		Map tempMap = new HashMap();
		String serviceId = "";
		StringBuffer feedback = new StringBuffer();
		List<String> patuFileList = new LinkedList<String>();
		List<String> patuTempList = new LinkedList<String>();
		String agmtServiceFilePath = (String) session.getAttribute(ServerConstants.AGMTSERVICECODEPATH);
		String filePath=agmtServiceFilePath+File.separator+PaymentConstants.PATU_OUT_FILE;
		String feedBackPath=agmtServiceFilePath+File.separator+PaymentConstants.FEEDBACK_LMPPAL;
		String ackPath = agmtServiceFilePath+File.separator+PaymentConstants.U_KUITTAUS;
		session.setAttribute(ServerConstants.ACKN_PATH, ackPath);
		MessageBean messageBean =(MessageBean)session.getAttribute(ServerConstants.ESIA_MSG_BEAN);
		String patuId = messageBean.getSenderId();
		List<String> kuittaus = new LinkedList<String>();
		for( String record : contentsList )
		{
			/**
			 * Write the code here to validate
			 * 1. To parse the string
			 * 2. Set the parsed object in bean
			 * 3. Validate the parsed object
			 **/
			String recordCode = record.substring(4, 5); 
			String transType = record.substring(5, 6);
			String parserName = getParserName(recordCode, transType );
			
			if(parserName!=null){
				StringBuffer strBuf = new StringBuffer();
				StringBuffer outBuf = new StringBuffer();
				parser = ParserFactory.getParser(parserName);



				if(parserName.equalsIgnoreCase(PaymentConstants.BATCH))
				{
					/**
					 * This validation is for the batch record
					 */
					batchCount++;
					paymentServiceBeanRC0 = (PaymentServiceBeanRC0)parser.parse(record);
					serviceId = paymentServiceBeanRC0.getService_code();
					boolean batchFlag=false;
					feedBackMap.put(PaymentConstants.FB_BILL_PAYMENT_SERVICE, PaymentConstants.BILL_PAYMENT_SERVICE);
					feedBackMap.put(PaymentConstants.FB_TRANSMISSION_FEEDBACK, PaymentConstants.TRANSMISSION_FEEDBACK);
					PaymentValidator paymentValidator = PaymentValidatorFactory.getValidator(PaymentConstants.BATCH);
					Map batchMap = (HashMap) paymentValidator.getValidatedObject(paymentServiceBeanRC0, session);
					PaymentServiceBeanRC0 batchError = (PaymentServiceBeanRC0)batchMap.get(PaymentConstants.BATCH_FAILED);
					String currencyCode = paymentServiceBeanRC0.getCurrency_code();
					String batchErrMsg = "";
					try{
						if(batchError.getErrorMsg().size() > 0){
							logger.debug("REJECTED IN BATCH ::::::::::");
							batchFlag = true;
							for(int y=0;y < batchError.getErrorMsg().size();y++){
								
								batchErrMsg = batchError.getErrorMsg().get(y);
								kuittaus.add(batchError.getErrorMsg().get(y));
								batchErrorMsg.add(batchError.getErrorMsg().get(y));
							}
						}else{
							batchFlag = false;
						}
					}catch(NullPointerException npe){
						batchFlag = false;
					}
					if(currencyCode.equals(PaymentConstants.ONE)){
						currency = PaymentConstants.CURRENCY_CODE;
					}
					batchList.add(batchMap);
					if(batchMap!=null){
						try{
							for( int i = 0 , n = batchList.size() ; i < n  ;i++ )
							{
								Map someMap = ( Map )batchList.get( i );
								String sendersCode = ( String ) someMap.get(PaymentConstants.SENDERS_CODE);
								String dateString = ( String )someMap.get(PaymentConstants.FILE_CREATION_DATE);
								String time = ( String )someMap.get(PaymentConstants.FILE_CREATION_TIME);
								String payers_name = ( String ) someMap.get(PaymentConstants.PAYRS_NAME);
								String account = ( String ) someMap.get(PaymentConstants.ACCOUNT_NO);
								String dueDate = ( String ) someMap.get(PaymentConstants.DUE_DATE);
								StringBuffer batchDueDate = new StringBuffer();
								feedBackMap.put(PaymentConstants.FB_SENDERS_CODE_HEADER, CommonUtil.pad(PaymentConstants.SENDERS_CODE_HEADER, 40, " "));
								feedBackMap.put(PaymentConstants.FB_FILE_CREATION_TIME_HEADER, CommonUtil.pad(PaymentConstants.FILE_CREATION_TIME_HEADER,10," "));
								String senderscode = CommonUtil.pad(patuId, 44, " ");
								try{
									StringBuffer dateBuf = new StringBuffer();
									dateTime = dateBuf.append(dateString.substring(0, 2)).append(".").append(dateString.substring(2, 4)).append(".").append(dateString.substring(4, 6)).append(" ").append(time.substring(0, 2)).append(":").append(time.substring(2, 4)).toString();
									logger.debug(dateTime);
								}catch(StringIndexOutOfBoundsException sioe){
									logger.error(sioe.getMessage());
								}
								fileCreationDate = CommonUtil.pad(dateTime, 10, " ");
								logger.debug("senderscode ::::"+senderscode);
								feedBackMap.put(PaymentConstants.FB_SENDERSCODE,senderscode);
								feedBackMap.put(PaymentConstants.FB_FILECREATIONDATE,fileCreationDate);
								feedBackMap.put(PaymentConstants.FB_PAYERS_NAME_HEADER,CommonUtil.pad(PaymentConstants.PAYERS_NAME_HEADER, 45, " "));
								feedBackMap.put(PaymentConstants.FB_ACCOUNT_HEADER, CommonUtil.pad(PaymentConstants.ACCOUNT_HEADER, 25, " "));
								feedBackMap.put(PaymentConstants.FB_CODE_HEADER, CommonUtil.pad(PaymentConstants.CODE_HEADER, 25, " "));
								feedBackMap.put(PaymentConstants.FB_DUE_DATE_HEADER,CommonUtil.pad(PaymentConstants.DUE_DATE_HEADER, 30, " "));

								String payersPayersName = CommonUtil.pad(payers_name, 45, " ");
								String payersAccountNo = CommonUtil.pad(account, 25, " ");

								String payersCode = CommonUtil.pad(serviceCode, 25, " ");
								/*
								 * Added by Debadatta Mishra to fix the issue related to display of payer's date
								 * in the feedback file.
								 */
//								batchDueDate.append(dueDate.substring(4, 6)).append(".").append(dueDate.substring(2, 4)).append(".").append(dueDate.substring(0, 2)).toString();
								batchDueDate.append(dueDate.substring(0, 2))
								.append(".").append(
										dueDate.substring(2, 4))
										.append(".").append(
												dueDate.substring(4, 6))
												.toString();

								String payersDueDate = CommonUtil.pad(batchDueDate, 30, " ");

								feedBackMap.put(PaymentConstants.FB_PAYERS_NAME,payersPayersName);
								feedBackMap.put(PaymentConstants.FB_PAYERS_ACCOUNT_NO,payersAccountNo);
								feedBackMap.put(PaymentConstants.FB_PAYERS_CODE, payersCode);
								feedBackMap.put(PaymentConstants.FB_PAYERS_DUE_DATE,payersDueDate);
								feedBackMap.put(PaymentConstants.FB_FAULTY_BILL_PAYMENT_TRANSACTIONS,PaymentConstants.FAULTY_BILL_PAYMENT_TRANSACTIONS);
								feedBackMap.put(PaymentConstants.FB_NUMBER_HEADER, CommonUtil.pad(PaymentConstants.NUMBER_HEADER, 14, " "));
								feedBackMap.put(PaymentConstants.FB_PAYEES_NAME_HEADER, CommonUtil.pad(PaymentConstants.PAYEES_NAME_HEADER, 30, " "));
								feedBackMap.put(PaymentConstants.FB_ACCOUNT_HEADER_TRANS, CommonUtil.pad(PaymentConstants.ACCOUNT_HEADER_TRANS, 25, " "));
								feedBackMap.put(PaymentConstants.FB_AMOUNT_HEADER_TRANS, CommonUtil.pad(PaymentConstants.AMOUNT_HEADER_TRANS, 22, " "));
								feedBackMap.put(PaymentConstants.FB_REFRANCE,CommonUtil.pad(PaymentConstants.REFRANCE, 40, " "));
							}    
						}catch(NullPointerException npe){
							logger.error("Null value in batch record ......"+npe.getMessage());
						}
						status = PaymentConstants.STATUS_S;
					}
					if(batchFlag == true){
						logger.debug("REJECTED IN BATCH FLAG :::");
						status = PaymentConstants.STATUS_F; 
						ackMap.put(PaymentConstants.REJECTED, PaymentConstants.REJECTED);
					} 
					patuFileList.add(outBuf.append(record).append("#").append(recordSuffixData).append("#").append(CommonUtil.pad(batchErrMsg,50," ")).append("#").append(status).toString());
				}else if(parserName.equalsIgnoreCase(PaymentConstants.TRANSACTIONS)){
					/**
					 * This validation is for the transaction record
					 */
					boolean flag = false;  
					List  transList = new ArrayList();
					Map<String, String> feedbackTrans = new HashMap<String, String>();
					Map<String, String> transError = new HashMap<String, String>();
					Map errorMsg = new HashMap();
					String transErrMsg="";
					seqCount++;
					totalRecordCount++;
					/**
					 * Make transaction validation
					 */
					PaymentServiceBeanRC1 paymentServiceBeanRC1 = (PaymentServiceBeanRC1)parser.parse(record);
					PaymentValidator paymentValidator = PaymentValidatorFactory.getValidator(PaymentConstants.TRANSACTIONS);
					//Map trans_map  = (HashMap) paymentValidator.getValidatedObject(paymentServiceBeanRC1);
					Map transMap  = (HashMap) paymentValidator.getValidatedObject(paymentServiceBeanRC1, session, serviceId,record.length());
					PaymentServiceBeanRC1 errorList = (PaymentServiceBeanRC1)transMap.get(PaymentConstants.FAILED_RECORDS);
					PaymentServiceBeanRC1 successRecords = (PaymentServiceBeanRC1)transMap.get(PaymentConstants.SUCCESS_RECORDS);

					try{
						if(errorList.getTransErrorMsg().size()>0){
							flag=true;
						}
					}catch(NullPointerException npe){
						flag=false;
					}
					try{
						if(flag==true){
							rejected++;	
							transList.add(transMap);
							DecimalFormat decimalFormat = new DecimalFormat(PaymentConstants.DEC_FORMAT);
							double transSums = Double.parseDouble(paymentServiceBeanRC1.getSum());
							if(paymentServiceBeanRC1.getData_code().equals("LM02") && paymentServiceBeanRC1.getRecord_code().equals("1") && paymentServiceBeanRC1.getTransaction_type().equals("2") ){
								rejectedCreditNoteSum = rejectedCreditNoteSum + transSums;
								rejectedCreditNoteTransaction++;
							}else{
								rejectedSum = rejectedSum + transSums;	
								totalTransaction++;
							}
							strRejectedSum = decimalFormat.format(rejectedSum);
							for( int i = 0 , n = transList.size() ; i < n  ;i++ )
							{
								transcount++;
								Map someMap = ( Map )transList.get( i );
								Iterator iter = someMap.entrySet().iterator();
								while( iter.hasNext() )
								{
									Map.Entry me = ( Map.Entry )iter.next();
									PaymentServiceBeanRC1 transactionbean = (PaymentServiceBeanRC1) me.getValue();
									try {
										errmsg++;
										transactionbean.setSeq_no(seqCount);
										transPayeesAccNo = transactionbean.getPayees_account_number();
										transMessageType = transactionbean.getMessage_type();
										transSum = transactionbean.getSum();
										String seqNo = CommonUtil.pad(transactionbean.getSeq_no(), 14, " ");
										String payersName = CommonUtil.pad(transactionbean.getPayees_name(), 30, " ");
										String payersAccNo = CommonUtil.pad(transactionbean.getPayees_account_number(), 25, " ");
										String payersSum = CommonUtil.pad(transactionbean.getSum(), 22, " ");
										String payersReferenceNo = CommonUtil.pad(transactionbean.getRefrance_no(), 40, " ");
										feedbackTrans.put(PaymentConstants.FB_SEQ_NO, seqNo);
										feedbackTrans.put(PaymentConstants.FB_PAYERS_NAME, payersName);
										feedbackTrans.put(PaymentConstants.FB_PAYERS_ACC_NO, payersAccNo);
										feedbackTrans.put(PaymentConstants.FB_PAYERS_SUM, payersSum);
										feedbackTrans.put(PaymentConstants.FB_PAYERS_REF_NO, payersReferenceNo);
										String firstSpace = CommonUtil.pad(" ", 14, " ");
										for(int y=0;y < transactionbean.getTransErrorMsg().size();y++){
											transErrMsg = transactionbean.getTransErrorMsg().get(y);
											transErrorMsg.add(firstSpace+transactionbean.getTransErrorMsg().get(y));
										}
										errorMsg.put(PaymentConstants.FB_ERRORS_MSG,transErrorMsg);
										feedBackTransList.add(feedbackTrans);
										feedBackTransList.add(errorMsg);
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}
							status = PaymentConstants.STATUS_F;
							patuFileList.add(outBuf.append(record).append("#").append(recordSuffixData).append("#").append(CommonUtil.pad(transErrMsg,50," ")).append("#").append(status).toString());
							transTempList.add(outBuf.append(record).append("#").append(recordSuffixData).append("#").append(CommonUtil.pad(transErrMsg,50," ")).append("#").append(status).toString());
						}else{
							accepted++;
							status = PaymentConstants.STATUS_S;
							successRecords.setSeq_no(seqCount);
							String sequenceNumber = CommonUtil.pad(successRecords.getSeq_no(), 14, " ");
							String payersName = CommonUtil.pad(successRecords.getPayee_qualifier_1(), 30, " ");
							String payersAccNumber = CommonUtil.pad(successRecords.getPayees_account_number(), 25, " ");
							String payersSumAmount = CommonUtil.pad(successRecords.getSum(), 22, " ");
							String payersReferenceNo = CommonUtil.pad(successRecords.getMessage(), 40, " ");
							successItem = strBuf.append(sequenceNumber).append(payersName).append(payersAccNumber).append(payersSumAmount).append(payersReferenceNo).toString();
							success = true;
							double transactionSumAmount = Double.parseDouble(paymentServiceBeanRC1.getSum());
							DecimalFormat decimalformat = new DecimalFormat(PaymentConstants.DEC_FORMAT);
							//
							if(paymentServiceBeanRC1.getData_code().equals("LM02") && paymentServiceBeanRC1.getRecord_code().equals("1") && paymentServiceBeanRC1.getTransaction_type().equals("2") ){
								creditNoteTransactions++;
								acceptedCreditNoteSum = acceptedCreditNoteSum + transactionSumAmount;
								strAcceptedCreditNoteSum = decimalformat.format(acceptedCreditNoteSum);
								tempCreditNoteSum = tempCreditNoteSum + transactionSumAmount;
								auditAcceptedTransactions++;
							}else{
								boolean lmpFlag = rejectedStatus(ackMap);
								if(lmpFlag==false){
									ackAcceptedTransactions++;
									auditAcceptedTransactions++;
									acceptedSum = acceptedSum + transactionSumAmount;
								}else{
									acceptedSum = acceptedSum + 0;	
									ackRejectedTransactions++;
								}
								tempacceptedsum = tempacceptedsum + transactionSumAmount;
								strAcceptedSum = decimalformat.format(acceptedSum);
								totalTransaction++;
							}

							boolean lmpFlag = rejectedStatus(ackMap);
							if(lmpFlag==true){
								status = PaymentConstants.STATUS_F;
							}
							patuFileList.add(outBuf.append(record).append("#").append(recordSuffixData).append("#").append(CommonUtil.pad(" ",50," ")).append("#").append(status).toString());
							transTempList.add(outBuf.append(record).append("#").append(recordSuffixData).append("#").append(CommonUtil.pad(" ",50," ")).append("#").append(status).toString());
						}
					}catch(NullPointerException npe){
						logger.error("Null value in transaction :.:.:"+npe.getMessage());
					}
					if(transcount > 50){
						kuittaus.add(ErrorConstants.TRANS_ERR_50);
						ackMap.put(PaymentConstants.REJECTED, PaymentConstants.REJECTED);
					}

				}else if(parserName.equalsIgnoreCase(PaymentConstants.ITEMISATION)){
					/**
					 * This validation is for the itemisation record
					 */
					Map<String, String> feedbackTrans = new HashMap<String, String>();
					Map errorMsg = new HashMap();
					String itemErrMsg = "";
					boolean flag = false;
					PaymentServiceBeanRC1 paymentServiceBeanRC1 = (PaymentServiceBeanRC1)parser.parse(record);
					PaymentValidator paymentValidator = PaymentValidatorFactory.getValidator(PaymentConstants.ITEMISATION);
					paymentServiceBeanRC1.setPayees_account_number(transPayeesAccNo);
					paymentServiceBeanRC1.setMessage_type(transMessageType);
					paymentServiceBeanRC1.setSum(transSum);
					Map itemizationMap  = (HashMap)paymentValidator.getValidatedObject(paymentServiceBeanRC1);
					PaymentServiceBeanRC1 errorList = (PaymentServiceBeanRC1)itemizationMap.get(PaymentConstants.ITEM_FAILED);
					try{
						if(errorList.getItemisationErrorMsg().size() > 0){
							if(success == true){
								flag=true;
							}
						}
					}catch(NullPointerException npe){
						flag=false;
					}
					if(flag==true){
						itemizationErrorMsg.add(successItem);
						Iterator itr = itemizationMap.entrySet().iterator();
						String firstSpace = CommonUtil.pad(" ", 14, " ");
						while( itr.hasNext() )
						{
							Map.Entry me = ( Map.Entry )itr.next();
							PaymentServiceBeanRC1 itemizationBean = (PaymentServiceBeanRC1) me.getValue();
							for(int k=0;k< itemizationBean.getItemisationErrorMsg().size(); k++){
								itemErrMsg = itemizationBean.getItemisationErrorMsg().get(k);
								itemizationErrorMsg.add(firstSpace+itemizationBean.getItemisationErrorMsg().get(k));
							}
						}
						errorMsg.put(PaymentConstants.FB_ERRORS_MSG,itemizationErrorMsg);
						feedBackTransList.add(feedbackTrans);
						feedBackTransList.add(errorMsg);
						status = PaymentConstants.STATUS_F;
						patuFileList.add(outBuf.append(record).append("#").append(recordSuffixData).append("#").append(CommonUtil.pad(itemErrMsg,50," ")).append("#").append(status).toString());
					}else{
						status = PaymentConstants.STATUS_S;
						try{
							if (ackMap.get(PaymentConstants.REJECTED).equals(PaymentConstants.REJECTED)){ 
								status = PaymentConstants.STATUS_F;
							}
						}catch(NullPointerException npe){
							logger.debug("Not rejected in batch record :::");
						}
						boolean lmpFlag = rejectedStatus(ackMap);
						if(lmpFlag==true){
							status = PaymentConstants.STATUS_F;
						}
						patuFileList.add(outBuf.append(record).append("#").append(recordSuffixData).append("#").append(CommonUtil.pad(" ",50," ")).append("#").append(status).toString());
					}
				}else if(parserName.equalsIgnoreCase(PaymentConstants.SUM)){
					boolean flag = false;
					String sumErrMsg = "";
					/**
					 * This validation is for the sum record
					 */
					totalTransactionSum = tempacceptedsum + rejectedSum;  
					totalCreditNoteSum = acceptedCreditNoteSum + rejectedCreditNoteSum;
					totalCreditNoteTransaction = rejectedCreditNoteTransaction + creditNoteTransactions;

					PaymentServiceBeanRC9 paymentServiceBeanRC9 = (PaymentServiceBeanRC9)parser.parse(record);
					PaymentValidator paymentValidator = PaymentValidatorFactory.getValidator(PaymentConstants.SUM);
					paymentValidator.getValidatedObject(paymentServiceBeanRC9);
					SumRecordValidatorImpl sumValidatorImpl = new SumRecordValidatorImpl();
					Map sumMap = (HashMap) sumValidatorImpl.getValidatedObject(paymentServiceBeanRC9, paymentServiceBeanRC0, totalTransaction, totalTransactionSum, totalCreditNoteSum,totalCreditNoteTransaction,record.length());
					totalTransactionSum = 0.0;
					tempacceptedsum=0.0;
					acceptedSum = 0.0;
					rejectedSum = 0.0;
					totalTransaction=0;
					PaymentServiceBeanRC9 errorList = (PaymentServiceBeanRC9)sumMap.get(PaymentConstants.SUM_FAILED);
					//String firstSpace = CommonUtil.pad(" ", 6, " ");
					try{
						if(errorList.getSumErrorMsg().size()>0){
							for(int y=0;y < errorList.getSumErrorMsg().size();y++){
								kuittaus.add(errorList.getSumErrorMsg().get(y));
								sumErrMsg = errorList.getSumErrorMsg().get(y);
								sumErrorMsg.add(errorList.getSumErrorMsg().get(y));
							}
							if(success == true){
								flag=true;
							}
							logger.debug("REJECTED IN SUM RECORD :::::");

							ackMap.put(PaymentConstants.REJECTED, PaymentConstants.REJECTED); 
						}
					}catch(NullPointerException npe){
						flag=false;
						logger.error("Null Pointer Exception SUM ERROR MESSAGE::::::::");
					}
					if(flag==true){
						status = PaymentConstants.STATUS_F;
					}else{
						status = PaymentConstants.STATUS_S;
						boolean lmpFlag = rejectedStatus(ackMap);
						if(lmpFlag==true){
							creditNoteTransactions=0;
							status = PaymentConstants.STATUS_F;
						}
					}
					boolean transFlag=false;
					for (String transRecord : transTempList){
						if(transRecord.endsWith("S")){
							transFlag = true;
						}
					}
					if(transFlag==false){
						sumErrMsg = PaymentConstants.ALL_TRANS_FAILED;
					}
					patuFileList.add(outBuf.append(record).append("#").append(recordSuffixData).append("#").append(CommonUtil.pad(sumErrMsg,50," ")).append("#").append(status).toString());
				}else if(parserName.equalsIgnoreCase(PaymentConstants.MESSAGETRANS)){
					/**
					 * This validation is for the message transactions record
					 */
					status = PaymentConstants.STATUS_S;
					boolean lmpFlag = rejectedStatus(ackMap);
					if(lmpFlag==true){
						status = PaymentConstants.STATUS_F;
					}
					patuFileList.add(outBuf.append(record).append("#").append(recordSuffixData).append("#").append(CommonUtil.pad(" ",50," ")).append("#").append(status).toString());
				}else if(parserName.equalsIgnoreCase(PaymentConstants.MESSAGEITEM)){
					/**
					 * This validation is for the message item record
					 */  
					status = PaymentConstants.STATUS_S;
					boolean lmpFlag = rejectedStatus(ackMap);
					if(lmpFlag==true){
						status = PaymentConstants.STATUS_F;
					}
					patuFileList.add(outBuf.append(record).append("#").append(recordSuffixData).append("#").append(CommonUtil.pad(" ",50," ")).append("#").append(status).toString());
				}else if(parserName.equalsIgnoreCase(PaymentConstants.INVALIDRECORD)){
					status = "F";
					patuFileList.add(outBuf.append(record).append("#").append(recordSuffixData).append("#").append(CommonUtil.pad(ErrorConstants.RECORD_CODE,50," ")).append("#").append(status).toString());
				}
			}

			/**
			 * This validation is for multiple batch records and generate the feed back file
			 */
			if( recordCode.equals(PaymentConstants.NINE) && transType.equals(PaymentConstants.ZERO) ){

				boolean transFlag=false;
				for (String transRecord : transTempList){
					if(transRecord.endsWith("S")){
						transFlag = true;
					}
				}
				if(transFlag==false){
					ackMap.put(PaymentConstants.REJECTED, PaymentConstants.REJECTED);
					sumErrorMsg.add(PaymentConstants.TRANS_ERR);
					kuittaus.add(PaymentConstants.TRANS_ERR);
				}
				boolean lmpFlag = rejectedStatus(ackMap);
				for(int i=0;i<patuFileList.size();i++){
					try{
						if(lmpFlag==true || transFlag==false){
							patuTempList.add(patuFileList.get(i).replace(patuFileList.get(i).substring(patuFileList.get(i).length()-1),"F"));
						}else{
							patuTempList.add(patuFileList.get(i));
						}
					}catch(NullPointerException npe){
						logger.error("Null value :::"+npe.getMessage());
					}
				}
				patuFileList.clear();

				logger.debug("strAcceptedSum ::::::::::"+strAcceptedSum);
				ackCreditNoteTransactions = ackCreditNoteTransactions + creditNoteTransactions;
				ackacceptedsum = ackacceptedsum + Double.valueOf(strAcceptedSum);
				DecimalFormat decimalformat = new DecimalFormat(PaymentConstants.DEC_FORMAT);
				acceptedsum = decimalformat.format(ackacceptedsum);
				if(lmpFlag==true){
					ackacceptedCreditNotesum = ackacceptedCreditNotesum + 0;
					strAcceptedCreditNotesum = decimalformat.format(ackacceptedCreditNotesum);
				}else{
					ackacceptedCreditNotesum = ackacceptedCreditNotesum + Double.valueOf(acceptedCreditNoteSum);
					strAcceptedCreditNotesum = decimalformat.format(ackacceptedCreditNotesum);
				}
				double strTempAcceptedSum = Double.parseDouble(strAcceptedSum) - tempCreditNoteSum;
				double strTempRejectedSum = Double.parseDouble(strRejectedSum) - rejectedCreditNoteSum;
				strAcceptedSum = String.valueOf(strTempAcceptedSum);
				strRejectedSum = String.valueOf(strTempRejectedSum);
				//strRejectedSum = String.valueOf(strTempRejectedSum);
				feedBackMap.put(PaymentConstants.FB_BATCH_LIST, batchErrorMsg);
				feedBackMap.put(PaymentConstants.FB_SUM_LIST, sumErrorMsg);
				feedBackMap.put(PaymentConstants.FB_TRANS_LIST, feedBackTransList);
				/**
				 * Store the map in the session
				 **/
				String textcontents = generateFeedBack(feedBackMap, feedBackList, feedBackPath, errmsg, ackMap, strAcceptedSum, strRejectedSum, accepted, rejected);
				feedback.append( textcontents ); 
				feedBackMap.clear();
				feedBackList.clear();
				batchErrorMsg.clear();
				transErrorMsg.clear();
				itemizationErrorMsg.clear();
				sumErrorMsg.clear();
				feedBackTransList.clear();
				batchList.clear();
				creditNoteTransactions=0;
				totalCreditNoteSum=0.0;
				totalCreditNoteTransaction=0;
				rejectedCreditNoteSum=0.0;
				acceptedCreditNoteSum=0.0;
				rejectedCreditNoteTransaction=0;
				strTempAcceptedSum=0.0;
				seqCount=0;
				tempCreditNoteSum=0.0;
				strAcceptedSum = PaymentConstants.DOUBLE_VALUE;
				strRejectedSum = PaymentConstants.DOUBLE_VALUE;
				totalCreditNoteSum=0.0;
				totalCreditNoteTransaction=0;
				boolean statusFlag = rejectedStatus(ackMap);
				if(statusFlag==true){
					int tempValue = accepted + rejected;
					temp = temp + tempValue;
				}else{
					temp = temp + rejected;
				}
				accepted = 0;
				rejected = 0;
				feedBackTransList.clear();
				String rejectedStatus = (  String )	ackMap.get(PaymentConstants.REJECTED);
				try{
					if(rejectedStatus.equals(PaymentConstants.REJECTED)){
						auditAcceptedTransactions = 0;
						tempAuditLog = tempAuditLog + auditAcceptedTransactions;
						tempRejected = tempRejected + 0;
						acceptedsum = acceptedsum + 0.0;
						//tempAcceptedSum = tempAcceptedSum + 0;
						//strAcceptedCreditNotesum = PaymentConstants.DOUBLE_VALUE;
					}else{
						tempAuditLog = tempAuditLog + auditAcceptedTransactions;
						tempAccepted = tempAccepted + ackAcceptedTransactions; 
						tempAckAcceptedSum = Double.parseDouble(acceptedsum);
						tempAcceptedSum = String.valueOf(tempAckAcceptedSum);
					}
				}catch(NullPointerException npe){
					tempAuditLog = tempAuditLog + auditAcceptedTransactions;
					tempAccepted = tempAccepted + ackAcceptedTransactions;
					tempAckAcceptedSum = Double.parseDouble(acceptedsum);
					tempAcceptedSum = String.valueOf(tempAckAcceptedSum);
				}
				ackAcceptedTransactions=0;
				acceptedsum = PaymentConstants.DOUBLE_VALUE;
				try{
					if(rejectedStatus.equals(PaymentConstants.REJECTED)){ 
						tempMap.put(PaymentConstants.REJECTED, PaymentConstants.REJECTED);
					}
				}catch(NullPointerException npe){

				}
				ackMap.clear();
			}
		}
		FileUtil.writePaymentFile(filePath, patuTempList);
		try{
			StringWriter writer = new StringWriter();
			File file = new File(feedBackPath);
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			fileOutputStream.write(feedback.toString().getBytes());
			writer.flush();
			writer.close();
		}catch( Exception e ){
			logger.error("exception in writing file ..........."+e.getMessage());
		}
		
		// ***** START - Content Added to transfer the feedback file to FTP client. 
		
		String feedbackKey = (String) session.getAttribute(ServerConstants.AGMTSERVICECODEPATH);
		
		String feedbackMessageKey = feedbackKey + ServerConstants.FEEDBACK_MSG;
		String feedbackPathKey = feedbackKey + ServerConstants.FEEDBACK_PATH;
		
		logger.debug(":::: PaymentServiceValidator ::: feedbackMessageKey :: "+feedbackMessageKey);
		logger.debug(":::: PaymentServiceValidator ::: feedbackPathKey :: "+feedbackPathKey);
		
		session.setAttribute(feedbackMessageKey, feedback.toString());
		session.setAttribute(feedbackPathKey, feedBackPath);
		
		// ***** END - Content Added to transfer the feedback file to FTP client.
		
		
		/**
		 * Below method call generates the acknowledgment file for bill payment service  
		 */
		generateAcknowledgement(session,batchCount,currency,tempAccepted,ackCreditNoteTransactions,tempAcceptedSum,formatString,strAcceptedCreditNotesum,creditNoteSum,tempMap,totalRecordCount,temp,ackPath,tempAuditLog,kuittaus);
		tempMap.clear();
	}

	/**
	 * Method generates the acknowledgment file for Bill payment service
	 * @param objects
	 */

	public void generateAcknowledgement(Object...objects){ 

		Session session = ( Session ) objects[0];
		int batchCount = (Integer)objects[1];
		String currency = (String)objects[2];
		int ackAcceptedTransactions = (Integer)objects[3];
		int creditNoteTransactions = (Integer)objects[4];
		String acceptedsum = (String)objects[5];
		String formatString = (String)objects[6];
		String acceptedCreditNotesum = (String)objects[7];
		String creditNoteSum = (String)objects[8];
		Map ackMap = (Map) objects[9]; 
		int totalRecordCount = (Integer)objects[10];
		int temp = (Integer)objects[11];
		String ackPath = (String)objects[12];
		int acceptedAuditLog = (Integer)objects[13];
		List<String> errorMsg = (List)objects[14];
		String rejectedStatus = (  String )	ackMap.get(PaymentConstants.REJECTED);
		Map<String, String> transactionMap = new HashMap<String, String>();
		int noOfLines=0;
		if(acceptedsum.length()==0){
			acceptedsum=String.valueOf(0);
		}
		StringBuffer notifyErrorMessage = new StringBuffer();
		DecimalFormat decimalformat = new DecimalFormat(PaymentConstants.DEC_FORMAT);
		String acceptedSum = decimalformat.format(Double.parseDouble(acceptedsum));
		DecimalFormat twoZero = new DecimalFormat(PaymentConstants.TWO_ZERO);
		try{
			DecimalFormat noFormat = new DecimalFormat(PaymentConstants.NO_FORMAT);
			TransferRequestBean transferRequestBean = ( TransferRequestBean ) session.getAttribute(ServerConstants.TR_OBJECT);
			DecimalFormat threezero = new DecimalFormat(PaymentConstants.THREE_ZERO);
			String batchrecords = threezero.format( batchCount );
			String filetype = transferRequestBean.getFileType();
			String fileType = CommonUtil.pad(filetype, 10, " ");
			String ackCurrency = CommonUtil.pad(currency, 4, " ");
			String ackBatches = CommonUtil.pad(batchrecords, 3, " ");
			String ackNoOfTransactions = CommonUtil.pad(noFormat.format(ackAcceptedTransactions), 6, " ");
			String ackNoOfCreditEntries = CommonUtil.pad(noFormat.format(creditNoteTransactions), 6, " ");
			try{
				String [] stringArray = acceptedSum.split("\\.");
				String decimals = stringArray[1].substring(0,2);
				formatString = stringArray[0] + decimals;
				//Credit Note
				String [] stringCreditNoteArray = acceptedCreditNotesum.split("\\.");
				String decimalsValue = stringCreditNoteArray[1].substring(0,2);
				creditNoteSum = stringCreditNoteArray[0] + decimalsValue;
			}catch(StringIndexOutOfBoundsException sioe){
				sioe.printStackTrace();
				logger.error("String error in acknowlwdgement :::"+sioe.getMessage());
			}catch(ArrayIndexOutOfBoundsException aioe){
				logger.error("Array out of bound exception in acknowlegement :::"+aioe.getMessage());
			}
			List ackList = new ArrayList();
			Map ackVelocityMap = new LinkedHashMap();
			DecimalFormat six = new DecimalFormat(PaymentConstants.SIX_ZERO);
			String sumTransaction = CommonUtil.pad(formatString, 16, " ");
			String ackNetAmountSign = "+";//CommonUtil.pad("+", 1, " ");
			//String noOfCreditEntries = CommonUtil.pad("0", 6, "0");
			String totalSumofCreditEntries = CommonUtil.pad(creditNoteSum, 16, " ");
			//String tot_sum_crdt_entr = CommonUtil.pad(" ", 1, " ");
			String date = CommonUtil.pad(DateUtil.GetDateTime(), 12, " ");
			String ackReturnCode = "";//CommonUtil.pad(PaymentConstants.DEC_FMT, 3, " ");
			String errorMessage = "";
			String fileStatus = "";
			String noOfNotificationLines = CommonUtil.pad("0", 1, " "); 
			String notificationLines = CommonUtil.pad(" ", 1, " ");

			ackVelocityMap.put(CommonPaymentConstant.ACK_FILE_TYPE, fileType);
			ackVelocityMap.put(CommonPaymentConstant.ACK_CURRENCY, ackCurrency);
			ackVelocityMap.put(CommonPaymentConstant.ACK_BATCHES, ackBatches);

			logger.debug("ack no trans::"+ackNoOfTransactions);
			logger.debug("sum trans:::::"+sumTransaction.trim());
			DecimalFormat decFmt = new DecimalFormat(PaymentConstants.ACK_NO_FORMAT);
			try{
				ackVelocityMap.put(CommonPaymentConstant.ACK_NO_OF_TRANSACTIONS, six.format(Long.parseLong(ackNoOfTransactions)));
				ackVelocityMap.put(CommonPaymentConstant.ACK_SUM_OF_TRANSACTIONS, decFmt.format(Long.parseLong(sumTransaction.trim())));
				ackVelocityMap.put(CommonPaymentConstant.ACK_TRANSACTIONS_SIGN, ackNetAmountSign);
				ackVelocityMap.put(CommonPaymentConstant.ACK_NO_OF_CREDITS, six.format(Long.parseLong(ackNoOfCreditEntries)));
				ackVelocityMap.put(CommonPaymentConstant.ACK_SUM_OF_CREDITS, decFmt.format(Long.parseLong(totalSumofCreditEntries.trim())));
				ackVelocityMap.put(CommonPaymentConstant.ACK_CREDITS_SIGN, "-");
			}catch(Exception ee){
				logger.error("exception in acknowledgement ::::"+ee.getMessage());
			}

			ackVelocityMap.put(CommonPaymentConstant.ACK_DATE, date);
			try{
				if(rejectedStatus.equals(PaymentConstants.REJECTED)){
					for (int i = 0; i < errorMsg.size(); i++) {
						noOfLines++;
						noOfNotificationLines = twoZero.format(noOfLines);
						notifyErrorMessage.append(CommonUtil.pad(errorMsg.get(i), 50, " ")).append(",");
					}
					ackReturnCode = SalaryConstants.ACK_RETURN_CODE_ERROR;
					errorMessage = SalaryConstants.ERROR_MESSAGE_R;
					fileStatus = SalaryConstants.FILE_STATUS_F;

				}else{
					noOfNotificationLines = twoZero.format(noOfLines);
					notifyErrorMessage.append(notificationLines);
					ackReturnCode = SalaryConstants.ACK_RETURN_CODE_SUCCESS;
					errorMessage = SalaryConstants.ERRORMESSAGE_A;
					fileStatus = SalaryConstants.FILE_STATUS_S;
				}
			}catch(NullPointerException npe){
				noOfNotificationLines = twoZero.format(noOfLines);
				notifyErrorMessage.append(notificationLines);
				ackReturnCode = SalaryConstants.ACK_RETURN_CODE_SUCCESS;
				errorMessage = SalaryConstants.ERRORMESSAGE_A;
				fileStatus = SalaryConstants.FILE_STATUS_S;
			}
			ackVelocityMap.put(CommonPaymentConstant.ACK_RETURN_CODE, ackReturnCode);//ackReturnCode
			ackVelocityMap.put(CommonPaymentConstant.ACK_NO_OF_NOTIFICATION_LINE, noOfNotificationLines);
			ackVelocityMap.put(CommonPaymentConstant.ACK_NOTIFICATION_LINE, notifyErrorMessage.toString());

			ackList.add(ackVelocityMap);
			/**
			 * Transaction map to store the transaction record information
			 */
			logger.debug("totalRecordCount     -->"+totalRecordCount);
			logger.debug("Valid record count   -->"+acceptedAuditLog);
			logger.debug("Invalid record count -->"+temp);

			transactionMap.put(PaymentConstants.TOTAL_RECORDS, String.valueOf(totalRecordCount));
			transactionMap.put(PaymentConstants.VALID_RECORD_COUNT, String.valueOf(acceptedAuditLog));
			transactionMap.put(PaymentConstants.INVALID_RECORD_COUNT, String.valueOf(temp));

			transactionMap.put(ServerConstants.PAYMENT_FILE_ERROR_CODE, ackReturnCode);
			transactionMap.put(ServerConstants.PAYMENT_FILE_ERROR_MESSAGE, errorMessage);
			transactionMap.put(ServerConstants.PAYMENT_FILE_STATUS, fileStatus);

			session.setAttribute(ServerConstants.TRANSACTION_MAP, transactionMap );		
			new VelocityTemplate().callVelocityTemplate(ackList, ackPath ,PaymentConstants.ACK);
		}catch(Exception ee){
			ee.printStackTrace();
		}

		logger.debug("ackMap ::::::::::::::"+ackMap);
	}
	/**
	 * This method is for generating the feed back file for bill payment service
	 * @param feedBackMap
	 * @param feedBackList
	 * @param feedBackPath
	 * @param errorMessage
	 * @param ackMap
	 * @param strAcceptedSum
	 * @param strRejectedSum
	 * @param accepted
	 * @param rejected
	 * @return feed back file contents
	 **/
	private static String generateFeedBack(Object...objects){
		Map feedBackMap = (Map)objects[0];
		List feedBackList = (List)objects[1];
		String feedBackPath = (String)objects[2];
		int errorMessage = (Integer)objects[3];
		Map ackMap = (Map)objects[4];
		String strAcceptedSum = (String)objects[5];
		String strRejectedSum = (String)objects[6];
		int accepted = (Integer)objects[7];
		int rejected = (Integer)objects[8];

		String feedbackcontents = "";
		try{
			DecimalFormat decimalFormatNo = new DecimalFormat(PaymentConstants.DEC_FMT);
			String acceptedValue = StringUtils.leftPad(decimalFormatNo.format(accepted),16);
			String rejectedValue = StringUtils.leftPad(decimalFormatNo.format(rejected),16);
			NumberFormat germanFormat = NumberFormat.getCurrencyInstance(Locale.GERMANY);
			try{
				if(ackMap.get(PaymentConstants.REJECTED).equals(PaymentConstants.REJECTED)){
					acceptedValue = PaymentConstants.ACCEPTED_VAL;	
					/**
					 * modified file for when validation rejected no of transactions and sum amount to be display in feed back file
					 */
					long rejectedValueAmt = accepted + rejected;
					rejectedValue = StringUtils.leftPad(decimalFormatNo.format(rejectedValueAmt),1);
					String tempAmt = strRejectedSum;
					double rejectedAmount = Double.parseDouble(strRejectedSum) + Double.parseDouble(strAcceptedSum);
					strAcceptedSum = PaymentConstants.STR_ACCEPTED_SUM;
					strRejectedSum = String.valueOf(rejectedAmount);
				}
			}catch(NullPointerException npe){
				logger.error("Null Pointer Exception ::::::::");
			}
			if(errorMessage != 0){
				feedBackMap.put(PaymentConstants.FB_ERRORS, PaymentConstants.ERRORS);
			}
			feedBackMap.put(PaymentConstants.FB_ACCEPTED_TRANS, PaymentConstants.ACCEPTED_TRANS);
			String currentDate = DateUtil.GetCurrentDateTime(); //df.format(today).concat(" ").concat(hours);
			String materialReceived = PaymentConstants.MATERIAL_RECEIVED +"                              "+currentDate;
			feedBackMap.put(PaymentConstants.FB_MATERIAL_RECEIVED, materialReceived);
			double acceptedAmount = Double.parseDouble(CommonUtil.pad(strAcceptedSum, 16, " "));
			double rejectedAmount = Double.parseDouble(CommonUtil.pad(strRejectedSum, 16, " "));

			feedBackMap.put(PaymentConstants.FB_ACCEPTED_TRANSATIONS,PaymentConstants.ACCEPTED_TRANSATIONS);
			feedBackMap.put(PaymentConstants.FB_ALTOGETHER_1,PaymentConstants.ALTOGETHER);
			feedBackMap.put(PaymentConstants.FB_ACCEPTED_VAL, acceptedValue);
			String acceptedAmt = germanFormat.format(acceptedAmount);
			String accAmt = acceptedAmt.replace("€", " ");
			feedBackMap.put(PaymentConstants.FB_ACCEPTED_AMT,StringUtils.leftPad(accAmt,16));
			feedBackMap.put(PaymentConstants.FB_CURRENCY_EUR_1,PaymentConstants.CURRENCY_EUR);

			feedBackMap.put(PaymentConstants.FB_REJECTED_TRANSATIONS, PaymentConstants.REJECTED_TRANSATIONS);
			feedBackMap.put(PaymentConstants.FB_ALTOGETHER_2, PaymentConstants.ALTOGETHER);
			feedBackMap.put(PaymentConstants.FB_REJECTED_VAL, rejectedValue);
			String rejectedAmt = germanFormat.format(rejectedAmount);
			String rejAmt = rejectedAmt.replace("€", " ");
			feedBackMap.put(PaymentConstants.FB_REJECTED_AMT,StringUtils.leftPad(rejAmt,16));
			feedBackMap.put(PaymentConstants.FB_CURRENCY_EUR_2,PaymentConstants.CURRENCY_EUR);

		}catch(Exception ee){
			logger.error("exception feed back method :::"+ee.getMessage());
			ee.printStackTrace();
		}
		try{
			feedBackList.add(feedBackMap);	
			feedbackcontents = new VelocityTemplate().callVelocityTemplate(feedBackList, feedBackPath ,PaymentConstants.FB_BPS);
		}catch(Exception ee){
			ee.printStackTrace();
		}
		return feedbackcontents;
	}
	/**
	 * Method used to check the bill payment validation file status rejected or accepted.
	 * @param lmpMap
	 * @return
	 **/
	public boolean rejectedStatus(Map lmpMap){
		boolean flag = false;
		try{
			if (lmpMap.get(PaymentConstants.REJECTED).equals(PaymentConstants.REJECTED)){ 
				flag = true;
			}
		}catch(NullPointerException npe){
			flag = false;
			logger.debug("Not rejected in batch record :::");
		}
		return flag;
	}
}