package com.ope.patu.payments.ts;

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
import com.ope.patu.payment.core.CommonPaymentConstant;
import com.ope.patu.payment.core.Parser;
import com.ope.patu.payment.core.VelocityTemplate;
import com.ope.patu.payment.db.PaymentDbImpl;
import com.ope.patu.payments.lmp300.PaymentConstants;
import com.ope.patu.payments.ts.beans.SalarySumBean;
import com.ope.patu.payments.ts.beans.SalaryTransactionBean;
import com.ope.patu.server.beans.MessageBean;
import com.ope.patu.server.beans.TransferRequestBean;
import com.ope.patu.server.constant.ServerConstants;
import com.ope.patu.util.CommonUtil;
import com.ope.patu.util.DateUtil;
import com.ope.patu.util.FileUtil;

/**
 * This class is to salary payment service validations and generating the feedback file, acknowledgment file and TS_U file
 * @author anandkumar.b
 */
public class SalaryServiceValidator 
{
	protected static Logger logger = Logger.getLogger(SalaryServiceValidator.class);

	/**
	 * Method used to parse the record depend upon the record code
	 * @param record_code
	 * @return
	 */
	public String getParserName( String record_code )
	{
		String parserName = null;
		if(record_code.equals(SalaryConstants.ONE)){
			//Transaction
			parserName = SalaryConstants.SAL_TRANS;
		}else if(record_code.equals(SalaryConstants.FOUR)){
			//sum record
			parserName = SalaryConstants.SAL_SUM;
		}
		return parserName;
	}
	int count=0;
	/**
	 * Method used to validate the salary payment service, transactions records and sum record.
	 * @param objects
	 */
	public void getSalaryService( Object...objects ){

		List<String> contentsList = (List<String>) objects[0];
		Session session = ( Session )objects[1];

		String sessionId = CommonUtil.pad(new Integer( session.getSessionId()).toString(), 9, " ");
		String serviceBureauId = CommonUtil.pad("#"+(String)session.getAttribute(ServerConstants.SERVICEBUREAUID),18," ");
		String aggrementId = CommonUtil.pad((String)"#"+session.getAttribute(ServerConstants.AGREEMENTTID),10," ");
		String serviceType = CommonUtil.pad((String)"#"+session.getAttribute(ServerConstants.SERVICECODE),9," ");
		String serviceID = CommonUtil.pad((String)"#"+session.getAttribute(ServerConstants.SERVICEID),11," ");
		long timeStamp = System.currentTimeMillis();
		Date longDate = new Date( timeStamp );
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMddhhmmss");
		String longTimeStamp = simpleDateFormat.format(longDate);//new Long( longTimeStamp ).toString()+"_";

		String recordSuffixData = new StringBuffer().append(sessionId).append(serviceBureauId).
		append(aggrementId).append(serviceType).append(serviceID).append("#"+longTimeStamp).toString();
		MessageBean messageBean =(MessageBean)session.getAttribute(ServerConstants.ESIA_MSG_BEAN);
		String patuId = messageBean.getSenderId();

		String serviceFilePath = (String) session.getAttribute(ServerConstants.AGMTSERVICECODEPATH);
		int county=0;
		String status="";
		Map tempHold = new LinkedHashMap();
		Parser parser = null;
		int count=0;
		int payments=0;
		double transSum=0.0;
		int accepted=0;
		int rejected=0;
		int totalTrans=0;
		int invalidRecCount=0;
		double acceptedSum =0.0;
		double rejectedSum =0.0;
		double totalSum=0.0;
		int noOfTransactions=0;
		String accountNumber="";
		double transAmount=0.0;
		boolean statusFlag = false;
		int kuittaustrans=0;
		double kuittausSum=0.0;
		int noOfBatches=0;
		boolean flag=false;
		String strAcceptedSum = SalaryConstants.DECIEMAL_ZERO;
		String strRejectedSum = SalaryConstants.DECIEMAL_ZERO;
		List<String> rejectedList = new ArrayList<String>();
		List<String> kuittausList = new ArrayList<String>();
		List feedbackTransList = new ArrayList();
		StringBuffer feedback = new StringBuffer();
		List<String> patuFileList = new LinkedList<String>();
		List<String> patuTempList = new LinkedList<String>();
		List<String> serviceIdList = new LinkedList<String>();
		Map feedbackMap = new LinkedHashMap();
		SalarySumBean sumbean = null;
		String salaryFeedBack = serviceFilePath+File.separator+SalaryConstants.SALARY_FEEDBACK_FILE;
		String ackPath = serviceFilePath+File.separator+PaymentConstants.U_KUITTAUS;
		session.setAttribute(ServerConstants.ACKN_PATH, ackPath);

		String serviceId = (String) session.getAttribute(ServerConstants.SERVICEID);
		String servicetype = (String) session.getAttribute(ServerConstants.SERVICECODE);
		String bureauId = (String) session.getAttribute(ServerConstants.SERVICEBUREAUID);

		try{
			accountNumber = new PaymentDbImpl().getServiceCode(serviceId,servicetype, bureauId);
			if(accountNumber.equals("0")){
				accountNumber=" ";
			}
		}catch(NullPointerException npe){
			npe.printStackTrace();
		}catch(Exception ee){
			ee.printStackTrace();
		}

		int seqCount=0;
		List transErrorMsg = new ArrayList();
		List ackErrMsg = new ArrayList();


		String record = "";
		List<String> mapList = getBatchWiseRecords(contentsList);
		Iterator iter = mapList.iterator();
		while(iter.hasNext()){
			Map map = (LinkedHashMap)iter.next();
			Iterator keyValue = map.entrySet().iterator();
			Map.Entry entry = (Map.Entry) keyValue.next();
			if(entry.getKey()!=null){
				logger.debug("BATCH LINE :"+entry.getKey());
				List<String> lst = (List)entry.getValue();
				Iterator it = lst.iterator();
				for( String recordContents : lst ){
					String recordCode = recordContents.substring(0, 1);
					String parserName = getParserName(recordCode);
					if(parserName!=null){
						parser = ParserFactory.getParser(parserName);
						if(parserName.equalsIgnoreCase(SalaryConstants.SAL_SUM))
						{
							if(recordCode.equals(SalaryConstants.FOUR)){
								sumbean = (SalarySumBean)parser.parse(recordContents);
							}
						}
					}
				}
				while(it.hasNext()){
					record = (String)it.next();
					String errorMessage="";
					if(record.length()!= 80){
						rejectedList.add(SalaryConstants.RECORD_LENGTH_REJECTED);
					}
					String temp = "";
					String recordCode = record.substring(0, 1);

					if(!(recordCode.equals( SalaryConstants.ONE) || recordCode.equals(SalaryConstants.FOUR ))){
						logger.debug("INVALID RECORD CODE :::::::::");
						temp = recordCode.replace(recordCode, SalaryConstants.ONE); 
					}else{
						temp = record.substring(0, 1);
					}
					String salParserName = getParserName(temp);

					if(salParserName!=null){
						StringBuffer outBuf = new StringBuffer();
						payments++;
						parser = ParserFactory.getParser(salParserName);

						if(salParserName.equalsIgnoreCase(SalaryConstants.SAL_TRANS))
						{
							seqCount++;
							Map<String, String> feedbackTrans = new HashMap<String, String>();
							Map errorMsg = new HashMap();

							boolean transFlag=false;
							count++;
							try{
								SalaryTransactionBean transactionBean = (SalaryTransactionBean) parser.parse(record);
								SalaryValidator paymentValidator = SalaryValidatorFactory.getValidator(SalaryConstants.SAL_TRANS);
								SalaryTransactionBean transBean = (SalaryTransactionBean) paymentValidator.getValidatedObject(transactionBean,sumbean,seqCount);
								serviceIdList.add(transBean.getService_code());
								
								DecimalFormat decimalFormat = new DecimalFormat(SalaryConstants.DEC_FORMAT);
								transAmount = Double.parseDouble(transBean.getAmount());

								transSum = transSum + transAmount;
								noOfTransactions++;
								try{
									for( int k = 0; k < transBean.getTransRejectedMsg().size(); k++ ){
										rejectedList.add(transBean.getTransRejectedMsg().get(k));
									} 
								}catch(NullPointerException npe){
									logger.debug("Null value in transaction record ...");
								}
								try{
									if( transBean.getTransErrorMsg().size()>0 ){
										transFlag = true;
										rejected++;
										double transactionsSum = Double.parseDouble(transBean.getAmount());
										rejectedSum = rejectedSum + transactionsSum;
										strRejectedSum = decimalFormat.format(rejectedSum);
										status = SalaryConstants.STATUS_F;
									}else{
										accepted++;
										double transactionsSum = Double.parseDouble(transBean.getAmount());
										acceptedSum = acceptedSum + transactionsSum;
										strAcceptedSum = decimalFormat.format(acceptedSum);
										status = SalaryConstants.STATUS_S;
									}
								}catch(NullPointerException npe){
									logger.debug("Null value salary transactions ... ");
								}
								if(transFlag==true){
									String seqNo = CommonUtil.pad(count, 8, " ");
									String payeesName = CommonUtil.pad(transBean.getPayees_name(), 30, " ");
									String payersAccNo = CommonUtil.pad(transBean.getPayees_acc_no(), 25, " ");
									String amount = CommonUtil.pad(transBean.getAmount(), 22, " ");
									String payersIdentityNumber = CommonUtil.pad(transBean.getPayees_identity_no(), 40, " ");
									feedbackTrans.put(SalaryConstants.SEQ_NO, seqNo);
									feedbackTrans.put(SalaryConstants.PAYEES_NAME, payeesName);
									feedbackTrans.put(SalaryConstants.PAYERS_ACC_NO, payersAccNo);
									feedbackTrans.put(SalaryConstants.AMOUNT, amount);
									feedbackTrans.put(SalaryConstants.ID_NUMBER, payersIdentityNumber);
									String firstSpace = CommonUtil.pad(" ", 7, " ");
									for(int y=0;y<transBean.getTransErrorMsg().size();y++){
									//if(!(transBean.getTransErrorMsg().get(y).startsWith(SalaryConstants.REJECTED))){
										errorMessage = transBean.getTransErrorMsg().get(y);
										transErrorMsg.add(firstSpace+transBean.getTransErrorMsg().get(y));
										ackErrMsg.add(transBean.getTransErrorMsg().get(y));
									//}
									}
									errorMsg.put(SalaryConstants.ERRORS_MSG,transErrorMsg);
									feedbackTransList.add(feedbackTrans);
									feedbackTransList.add(errorMsg);
								}
							}catch(NullPointerException npe){
								npe.printStackTrace();
							}
							if(!rejectedList.isEmpty()){
								status = SalaryConstants.STATUS_F;
							}
							patuFileList.add(outBuf.append(record).append("#").append(recordSuffixData).append("#").append(CommonUtil.pad(errorMessage,50," ")).append("#").append(status).toString());

						}else if(salParserName.equalsIgnoreCase(SalaryConstants.SAL_SUM)){
							noOfBatches++;
							county++;
							SalarySumBean salsumbean = (SalarySumBean)parser.parse(record);
							SalaryValidator paymentValidator = SalaryValidatorFactory.getValidator(SalaryConstants.SAL_SUM);
							SalarySumBean sumBean = (SalarySumBean) paymentValidator.getValidatedObject(salsumbean,transSum,noOfTransactions,serviceIdList);
							serviceIdList.clear();
							noOfTransactions=0;
							transSum=0.0;

							try{
								for( int k = 0; k < sumBean.getSumRejectedMsg().size(); k++ ){
									errorMessage = sumBean.getSumRejectedMsg().get(k);
									rejectedList.add(sumBean.getSumRejectedMsg().get(k));
								} 
							}catch(NullPointerException npe){
								logger.error("Null value in transaction record ...");
							}
							/**
							 * else if(parserName.equalsIgnoreCase(SalaryConstants.SAL_TRANS) || parserName.equalsIgnoreCase(SalaryConstants.SAL_SUM)){
							 **/
							for (String transRecord : patuFileList){
								if(transRecord.endsWith("S")){
									statusFlag = true;
								} 
							}
							if(statusFlag==true){
								//errorMessage = "";
								status = SalaryConstants.STATUS_S;
							}else{
								errorMessage = SalaryErrorConstants.FILE_REJECTED;
								status = SalaryConstants.STATUS_F;
							}
							if(!rejectedList.isEmpty()){
								status = SalaryConstants.STATUS_F;
							}
							patuFileList.add(outBuf.append(record).append("#").append(recordSuffixData).append("#").append(CommonUtil.pad(errorMessage,50," ")).append("#").append(status).toString());

							for(int i=0;i<patuFileList.size();i++){
								try{
									if(!rejectedList.isEmpty()){
										patuTempList.add(patuFileList.get(i).replace(patuFileList.get(i).substring(patuFileList.get(i).length()-1),"F"));
									}else{
										patuTempList.add(patuFileList.get(i));
									}
								}catch(NullPointerException npe){
									logger.error(npe.getMessage());
								}
							}
							for(int i=0;i<patuFileList.size();i++){
								if(patuFileList.get(i).substring(patuFileList.get(i).length()-1).contains("S")){
									flag=true;
								}
							}
							if(flag==false){
								rejectedList.add(SalaryErrorConstants.FILE_REJECTED);
							}
							feedbackMap = getFeedBackHeaderPart(salsumbean, accountNumber, patuId,rejectedList);
							patuFileList.clear();

						}
					}
					if(recordCode.equals(SalaryConstants.FOUR)){
						if(!rejectedList.isEmpty()){
							invalidRecCount = accepted + rejected;
						}else{
							invalidRecCount = invalidRecCount + rejected;
						}
						feedbackMap.put(SalaryConstants.TRANS_SUM, rejectedList);
						feedbackMap.put(SalaryConstants.TRANS_LIST, feedbackTransList);
						totalSum = acceptedSum + rejectedSum;
						totalTrans = accepted + rejected;
						if(!rejectedList.isEmpty()){
							kuittaustrans = kuittaustrans + 0;
							strAcceptedSum = "0.00";
							kuittausSum = kuittausSum + Double.parseDouble(strAcceptedSum);
						}else{
							kuittaustrans = kuittaustrans + accepted; 
							kuittausSum = kuittausSum + Double.parseDouble(strAcceptedSum);	
						}
						for(int k=0;k<rejectedList.size();k++){
							kuittausList.add(rejectedList.get(k));
						}

						String textContents = generateFeedBackFile(feedbackMap,salaryFeedBack,rejectedList,totalSum,totalTrans,strAcceptedSum,strRejectedSum,accepted,rejected);
						feedback.append( textContents );
						feedbackMap.clear();
						rejectedList.clear();
						feedbackTransList.clear();
						totalSum=0.0;
						totalTrans=0;
						acceptedSum=0.0;
						rejectedSum=0.0;
						DecimalFormat decimalFormat = new DecimalFormat(SalaryConstants.DEC_FORMAT);
						strAcceptedSum = decimalFormat.format(acceptedSum);
						strRejectedSum = decimalFormat.format(rejectedSum); 
						accepted=0;
						rejected=0;
						transSum=0;
						transAmount=0;
						count=0;
						noOfTransactions=0;
					}
				}

			}

		}
		writeFeedBack(feedback.toString(), salaryFeedBack);	
		
		// ***** START - Content Added to transfer the feedback file to FTP client. 
		
		String feedbackKey = (String) session.getAttribute(ServerConstants.AGMTSERVICECODEPATH);
		
		String feedbackMessageKey = feedbackKey + ServerConstants.FEEDBACK_MSG;
		String feedbackPathKey = feedbackKey + ServerConstants.FEEDBACK_PATH;
		
		logger.debug(":::: PaymentServiceValidator ::: feedbackMessageKey :: "+feedbackMessageKey);
		logger.debug(":::: PaymentServiceValidator ::: feedbackPathKey :: "+feedbackPathKey);
		
		session.setAttribute(feedbackMessageKey, feedback.toString());
		session.setAttribute(feedbackPathKey, salaryFeedBack);
		
		// ***** END - Content Added to transfer the feedback file to FTP client.
		
		
		generateAcknowledgement(seqCount,kuittaustrans,invalidRecCount,kuittausSum,serviceFilePath,kuittausList,session,noOfBatches);
		String filePath = serviceFilePath+File.separator+SalaryConstants.PATU_FILE;
		FileUtil.writePaymentFile(filePath, patuTempList);
	}

	public void writeFeedBack(String feedback, String feedBackPath){
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
		
		
	}
	/**
	 * Method generates the salary payment feed back file 
	 * @param objects
	 * @return
	 */
	public String generateFeedBackFile(Object...objects){

		Map feedbackMap = (Map) objects[0];
		String salFeedBack = (String)objects[1];
		List rejectedList = (List<String>) objects[2];
		double totalSum = Double.parseDouble(objects[3].toString());
		double totalTrans = Double.parseDouble(objects[4].toString());
		String strAcceptedSum = (String)objects[5];
		String str_rejected_sum = (String)objects[6];
		int accepted = Integer.parseInt(objects[7].toString());
		int rejected = Integer.parseInt(objects[8].toString());

		List feedbackList = new ArrayList();

		DecimalFormat decimalFormat = new DecimalFormat(SalaryConstants.DEC_FMT);
		String acceptedValue = decimalFormat.format(accepted);
		String rejectedValue = decimalFormat.format(rejected);
		NumberFormat germanFormat = NumberFormat.getCurrencyInstance(Locale.GERMANY);
		double acceptedAmt = Double.parseDouble(CommonUtil.pad(strAcceptedSum, 16, " "));
		double rejectedAmt = Double.parseDouble(CommonUtil.pad(str_rejected_sum, 16, " "));
		if(rejected!=0){
			feedbackMap.put("N", "N");
			feedbackMap.put(SalaryConstants.FB_ERRORS, SalaryConstants.ERRORS);
		}
		String currentDate = DateUtil.GetCurrentDateTime();
		String materialReceived = SalaryConstants.MATERIAL_RECEIVED +"                              "+currentDate;
		feedbackMap.put(SalaryConstants.FB_MATERIAL_RECEIVED, materialReceived);
		String acceptedValues = StringUtils.leftPad(germanFormat.format(acceptedAmt), 16 );
		String rejectedValues = StringUtils.leftPad(germanFormat.format(rejectedAmt), 16 );
		if(!rejectedList.isEmpty()){
			rejectedValue = decimalFormat.format(totalTrans);
			acceptedValue = SalaryConstants.THREE_ZERO;
			acceptedValues = SalaryConstants.EUR_FMT;
			rejectedValues = String.valueOf(totalSum);//SalaryConstants.EUR_FMT;
			strAcceptedSum = SalaryConstants.ACK_ZERO;
		}
		feedbackMap.put(SalaryConstants.ACCEPTED_TRANSATIONS_HEADER, SalaryConstants.ACCEPTED_TRANSATIONS);
		feedbackMap.put(SalaryConstants.ALTOGETHER_1, SalaryConstants.ALTOGETHER);
		feedbackMap.put(SalaryConstants.ACCEPTED_VAL, acceptedValue);
		String acceptedAmount =acceptedValues.replace("€", " ");
		feedbackMap.put(SalaryConstants.ACCEPTED_VALUES, StringUtils.leftPad(acceptedAmount,16));
		feedbackMap.put(SalaryConstants.CURRENCY_EUR_1, SalaryConstants.CURRENCY_EUR);
		feedbackMap.put(SalaryConstants.REJECTED_TRANSATIONS_HEADER, SalaryConstants.REJECTED_TRANSATIONS);
		feedbackMap.put(SalaryConstants.ALTOGETHER_2, SalaryConstants.ALTOGETHER);
		feedbackMap.put(SalaryConstants.REJECTED_VAL, rejectedValue);
		String rejectedAmount = rejectedValues.replace("€", " ");
		feedbackMap.put(SalaryConstants.REJECTED_VALUES, StringUtils.leftPad(rejectedAmount,16));
		feedbackMap.put(SalaryConstants.CURRENCY_EUR_2, SalaryConstants.CURRENCY_EUR);
		feedbackMap.put(SalaryConstants.REMARK, SalaryErrorConstants.REMARK);
		feedbackList.add(feedbackMap);
		String textContents = new VelocityTemplate().callVelocityTemplate(feedbackList,salFeedBack,SalaryConstants.FB_SPS);
		return textContents;
	}

	/**
	 * Method generates the salary payment service acknowledgment
	 * @param objects
	 * @return
	 */
	public boolean generateAcknowledgement( Object...objects ){

		int seqCount = Integer.parseInt(objects[0].toString());
		int accepted = Integer.parseInt(objects[1].toString());
		//String strAcceptedSum = String.valueOf(objects[3]);
		DecimalFormat totalsum = new DecimalFormat(SalaryConstants.ACK_ZERO);
		String strAcceptedSum = totalsum.format(objects[3]);
		String agmtServiceFilePath = (String) objects[4];
		List rejectedList = (List<String>)objects[5];
		Session session = (Session)objects[6];
		int noOfBatches = (Integer)objects[7];

		StringBuffer notifyErrorMessage = new StringBuffer();
		int noOfLines = 0;

		String errorMessage = "";
		String fileStatus = "";
		try{

			Map ackMap = new LinkedHashMap();
			List ackList = new ArrayList();

			String formatString="";
			DecimalFormat twoZero = new DecimalFormat(SalaryConstants.TWO_ZERO);
			DecimalFormat threeZeroFormat = new DecimalFormat(SalaryConstants.THREE_ZERO);
			TransferRequestBean transferRequestBean = ( TransferRequestBean ) session.getAttribute(ServerConstants.TR_OBJECT);
			String filetype = transferRequestBean.getFileType();
			//DecimalFormat accepted_sums = new DecimalFormat(PaymentConstants.DECIMAL_FORMAT);
			String fileType = CommonUtil.pad(filetype, 10, " ");
			String ackCurrency = CommonUtil.pad(SalaryConstants.CURRENCY_CODE, 4, " ");
			String ackBatch = CommonUtil.pad(threeZeroFormat.format(noOfBatches), 3, " ");
			//String ackNoOfTransactions = CommonUtil.pad(numberFormat.format(accepted), 6, "0");

			try{
				String [] stringArray = strAcceptedSum.split("\\.");
				String decimals = stringArray[1].substring(0,2);
				formatString = SalaryConstants.FOUR_ZERO+stringArray[0] + decimals;
			}catch(StringIndexOutOfBoundsException sioe){
				logger.debug("String index out of bounds exception :::"+sioe.getMessage());
			}
			String ackNetAmountSign = CommonUtil.pad("+", 1, " ");
			String noOfCreditEntries = CommonUtil.pad("0", 6, "0");
			String totalSumOfCreditEntries = CommonUtil.pad("0", 16, "0");
			String date = CommonUtil.pad(DateUtil.GetDateTime(), 12, " ");
			String ackReturnCode = CommonUtil.pad(SalaryConstants.THREE_ZERO, 3, " ");
			String noOfNotficationLines = ""; 

			DecimalFormat six = new DecimalFormat(SalaryConstants.SIX_ZERO);
			ackMap.put(CommonPaymentConstant.ACK_FILE_TYPE, fileType);
			ackMap.put(CommonPaymentConstant.ACK_CURRENCY, ackCurrency);
			ackMap.put(CommonPaymentConstant.ACK_BATCHES, ackBatch);
			DecimalFormat decimalFmt = new DecimalFormat(PaymentConstants.ACK_NO_FORMAT);

			DecimalFormat totalSumSixteen = new DecimalFormat(SalaryConstants.SIXTEEN_ZERO);
			try{
				ackMap.put(CommonPaymentConstant.ACK_NO_OF_TRANSACTIONS, six.format(accepted));
				ackMap.put(CommonPaymentConstant.ACK_SUM_OF_TRANSACTIONS, totalSumSixteen.format(Long.parseLong(formatString)));
				if(!rejectedList.isEmpty()){
					for (int i = 0; i < rejectedList.size(); i++) {
						noOfLines++;
						noOfNotficationLines = twoZero.format(noOfLines);
						notifyErrorMessage.append(CommonUtil.pad(rejectedList.get(i), 50, " ")).append(",");
					}
					ackReturnCode = SalaryConstants.ACK_RETURN_CODE_ERROR;
					errorMessage = SalaryConstants.ERROR_MESSAGE_R;
					fileStatus = SalaryConstants.FILE_STATUS_F;
				}else{
					noOfNotficationLines = CommonUtil.pad("00", 2, " ");
					notifyErrorMessage.append(CommonUtil.pad(" ", 1, " "));
					ackReturnCode = SalaryConstants.ACK_RETURN_CODE_SUCCESS;
					errorMessage = SalaryConstants.ERRORMESSAGE_A;
					fileStatus = SalaryConstants.FILE_STATUS_S;
				}
			}catch(Exception ee){
				ee.printStackTrace();
			}
			
			ackMap.put(CommonPaymentConstant.ACK_TRANSACTIONS_SIGN, ackNetAmountSign);
			ackMap.put(CommonPaymentConstant.ACK_NO_OF_CREDITS, noOfCreditEntries);
			ackMap.put(CommonPaymentConstant.ACK_SUM_OF_CREDITS,totalSumOfCreditEntries);
			ackMap.put(CommonPaymentConstant.ACK_CREDITS_SIGN, " ");
			ackMap.put(CommonPaymentConstant.ACK_DATE, date);

			ackMap.put(CommonPaymentConstant.ACK_RETURN_CODE, ackReturnCode);
			ackMap.put(CommonPaymentConstant.ACK_NO_OF_NOTIFICATION_LINE, noOfNotficationLines);
			ackMap.put(CommonPaymentConstant.ACK_NOTIFICATION_LINE, notifyErrorMessage.toString());

			ackList.add(ackMap);
			String ackFilePath = agmtServiceFilePath+File.separator+SalaryConstants.ACK_FILE;
			new VelocityTemplate().callVelocityTemplate(ackList,ackFilePath,SalaryConstants.ACK);

			Map<String, String> transactionMap = new HashMap<String, String>();

			int acceptedTrans = seqCount - accepted; 
			logger.debug("seqCount            -->"+seqCount);
			logger.debug("ackNoOfTransactions -->"+accepted);
			logger.debug("rejected            -->"+acceptedTrans);

			transactionMap.put(PaymentConstants.TOTAL_RECORDS, String.valueOf(seqCount));
			transactionMap.put(PaymentConstants.VALID_RECORD_COUNT, String.valueOf(accepted));
			transactionMap.put(PaymentConstants.INVALID_RECORD_COUNT, String.valueOf(acceptedTrans));
			transactionMap.put(ServerConstants.PAYMENT_FILE_ERROR_CODE, ackReturnCode);
			transactionMap.put(ServerConstants.PAYMENT_FILE_ERROR_MESSAGE, errorMessage);
			transactionMap.put(ServerConstants.PAYMENT_FILE_STATUS, fileStatus);
			session.setAttribute(ServerConstants.TRANSACTION_MAP, transactionMap );	
			/**
			 * Store the map in the session
			 */
		}catch(NullPointerException npe){
			logger.debug("Null value in acknowledgement ::::");
		}
		return true;
	}

	public Map getFeedBackHeaderPart(SalarySumBean sumbean, String accountNumber, String patuId,List rejectedList){

		Map feedbackMap = new LinkedHashMap();
		StringBuffer dateBuf = new StringBuffer();
		feedbackMap.put(SalaryConstants.FB_SALARY_PAYMENT_SERVICE, SalaryConstants.SALARY_PAYMENT_SERVICE);
		feedbackMap.put(SalaryConstants.FB_TRANSMISSION_FEEDBACK, SalaryConstants.TRANSMISSION_FEEDBACK);
		feedbackMap.put(SalaryConstants.FB_SENDERS_CODE_HEADER, SalaryConstants.SENDERS_CODE_HEADER);
		feedbackMap.put(SalaryConstants.FB_CUSTOMER_DATA_HEADER, SalaryConstants.CUSTOMER_DATA_HEADER);
		String sendersCode = CommonUtil.pad(patuId, 34, " ");
		String customerData = CommonUtil.pad(sumbean.getData_from_customer(), 20, " ");
		feedbackMap.put(SalaryConstants.SENDERS_CODE, sendersCode);
		feedbackMap.put(SalaryConstants.CUSTOMER_DATA, customerData);
		feedbackMap.put(SalaryConstants.FB_PAYERS_NAME_HEADER, CommonUtil.pad(SalaryConstants.PAYERS_NAME_HEADER,37," "));
		feedbackMap.put(SalaryConstants.FB_ACCOUNT_HEADER,CommonUtil.pad(SalaryConstants.ACCOUNT_HEADER,25," "));
		feedbackMap.put(SalaryConstants.FB_CODE_HEADER, CommonUtil.pad(SalaryConstants.CODE_HEADER,22," "));
		feedbackMap.put(SalaryConstants.FB_PAYMENT_DATE_HEADER, CommonUtil.pad(SalaryConstants.PAYMENT_DATE_HEADER,27," "));

		String payersName = CommonUtil.pad(sumbean.getPayrs_name_qualifier(), 37, " ");
		String payersAccountNo = CommonUtil.pad(accountNumber, 25, " ");
		String payersCode = CommonUtil.pad(sumbean.getService_code(), 22, " ");
		String payersDate = sumbean.getPayment_date();
		String paymentDate = dateBuf.append(payersDate.substring(4, 6)).append(".").append(payersDate.substring(2, 4)).append(".").append(payersDate.substring(0, 2)).toString();
		String payersDueDate = CommonUtil.pad(paymentDate, 27, " ");

		feedbackMap.put(SalaryConstants.PAYERS_NAME,payersName);
		feedbackMap.put(SalaryConstants.PAYERS_ACCOUNT_NO,payersAccountNo);
		feedbackMap.put(SalaryConstants.PAYERS_CODE,payersCode);
		feedbackMap.put(SalaryConstants.PAYERS_DUE_DATE,payersDueDate);
		if(rejectedList.isEmpty()){
			feedbackMap.put(SalaryConstants.REJECTED_TRANSACTIONS_FB,SalaryConstants.REJECTED_TRANSACTIONS);
		}
		feedbackMap.put(SalaryConstants.FB_NUMBER_HEADER,CommonUtil.pad(SalaryConstants.NUMBER_HEADER,8," "));
		feedbackMap.put(SalaryConstants.FB_PAYEES_NAME_HEADER, CommonUtil.pad(SalaryConstants.PAYEES_NAME_HEADER,30," "));
		feedbackMap.put(SalaryConstants.FB_ACCOUNT_HEADER_TRANS, CommonUtil.pad(SalaryConstants.ACCOUNT_HEADER_TRANS,25," "));
		feedbackMap.put(SalaryConstants.FB_AMOUNT_HEADER_TRANS, CommonUtil.pad(SalaryConstants.AMOUNT_HEADER_TRANS, 22," "));
		feedbackMap.put(SalaryConstants.FB_ID_NUMBER_HEADER, CommonUtil.pad(SalaryConstants.ID_NUMBER_HEADER,40," "));

		return feedbackMap;
	}


	public List getBatchWiseRecords(List<String> contentsList){
		Map map = new LinkedHashMap();
		List resList = new LinkedList();
		try{
			List list = new LinkedList();
			boolean flag = false;
			int count=0;
			String temp="";
			for( String record : contentsList ){

				String recordCode = record.substring(0, 1);

				if(!(recordCode.equals( SalaryConstants.ONE) || recordCode.equals(SalaryConstants.FOUR ))){
					temp = recordCode.replace(recordCode, SalaryConstants.ONE); 
				}else{
					temp = record.substring(0, 1);
				}

				if(temp.equals(SalaryConstants.ONE)){
					list.add(record);
				}else if(temp.equals(SalaryConstants.FOUR)){
					list.add(record);
					flag = true;
					count++;
				}
				if(flag==true){
					map = new LinkedHashMap();
					List mapList = new LinkedList();
					for (int i = 0; i < list.size(); i++) {
						mapList.add(list.get(i)); 
					}
					map.put("LINE"+count, mapList);
					resList.add(map);
					flag = false;
					list.clear();
				}
			}
		}catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
		return resList;
	}
}