package com.ope.patu.payments.lmp300;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.coldcore.coloradoftp.session.Session;
import com.ope.patu.payment.utility.AccountNumberValidation;
import com.ope.patu.payment.utility.ReferenceNumberValidation;
import com.ope.patu.payments.lmp300.beans.PaymentServiceBeanRC1;
import com.ope.patu.server.constant.ErrorConstants;

/**
 * This is class is for implementing the transaction validations
 * @author anandkumar.b
 *
 */
public class TransactionValidatorImpl implements PaymentValidator
{
	protected static Logger logger = Logger.getLogger(TransactionValidatorImpl.class);
	/**
	 * Method takes input parameter as a transaction record and does the all validations and returns the
	 *  transaction map with list of success records and failed records and accepted or rejected status
	 */
	public Object getValidatedObject(Object... objects) 
	{
		String validateRefNo="";
		
		PaymentServiceBeanRC1 paymentServiceBeanRc1 = ( PaymentServiceBeanRC1 )objects[0];
		
		int recordLength = (Integer)objects[3];
		/**
		 * Provide all the implementation for the validation
		 */
		  Map transMap = new LinkedHashMap();
		  String transType =    paymentServiceBeanRc1.getTransaction_type();
		  String messageType =  paymentServiceBeanRc1.getMessage_type();      	   
          String dataCode = paymentServiceBeanRc1.getData_code();
          String recordCode = paymentServiceBeanRc1.getRecord_code();
		  int seqNo = paymentServiceBeanRc1.getSeq_no();
		  String payeesName = paymentServiceBeanRc1.getPayee_qualifier_1();
		  String payeesAccNo = paymentServiceBeanRc1.getPayees_account_number();
		  String transSumRecord = paymentServiceBeanRc1.getSum();
		  String refranceNo = paymentServiceBeanRc1.getMessage();
		  logger.debug("TRANS SUM :::::::"+transSumRecord);
		  try{
		      validateRefNo = refranceNo.substring(0, 20);
		      paymentServiceBeanRc1.setMessage(validateRefNo);
		  }catch(StringIndexOutOfBoundsException sie){
			  logger.debug(sie.getMessage());
		  }
		  double transSum = Double.parseDouble(transSumRecord);
		  boolean accFlag = new AccountNumberValidation().checkAccountNumber(payeesAccNo);
		  logger.debug("accFlag ::::"+accFlag);
		  if(recordLength!=300){
			  paymentServiceBeanRc1.getTransErrorMsg().add(ErrorConstants.RECORD_LENGTH + ", "+ recordLength);
		  }
		 
		  if(dataCode.equals(PaymentConstants.DATA_CODE_LM03) || dataCode.equals(PaymentConstants.DATA_CODE_LM02))
		  {
			  
		  }else{
			  paymentServiceBeanRc1.getTransErrorMsg().add(ErrorConstants.DATA_CODE + ",   "+ dataCode);
		  }
		  
		 if(dataCode.equals(PaymentConstants.DATA_CODE_LM02) && recordCode.equals(PaymentConstants.ZERO) || recordCode.equals(PaymentConstants.ONE) || recordCode.equals(PaymentConstants.NINE)){
				
		 }else{
			 paymentServiceBeanRc1.getTransErrorMsg().add(ErrorConstants.RECORD_CODE + ",   "+ recordCode);
		 }
		  
		  if(dataCode.equals(PaymentConstants.DATA_CODE_LM03) && recordCode.equals(PaymentConstants.ZERO) || recordCode.equals(PaymentConstants.ONE) || recordCode.equals(PaymentConstants.TWO) || recordCode.equals(PaymentConstants.NINE)){
				
		  }else{
			  paymentServiceBeanRc1.getTransErrorMsg().add(ErrorConstants.RECORD_CODE + ",   "+ recordCode);
		  }
		  if(transSum == 0000000000.00 || transSumRecord.length() == 0){
				paymentServiceBeanRc1.getTransErrorMsg().add(ErrorConstants.TRANS_SUM + ",   "+ transSum);
		  }
		  if(transType.equals(PaymentConstants.ZERO) || transType.equals(PaymentConstants.TWO) || transType.equals(PaymentConstants.NINE)){
			  
		  }else{
			  paymentServiceBeanRc1.getTransErrorMsg().add(ErrorConstants.TRANS_TYPE + ",   "+ transType);
		  }
		  if(accFlag==false){
				paymentServiceBeanRc1.getTransErrorMsg().add(ErrorConstants.TRANS_ACC_NO + ",   "+ payeesAccNo);
		  }
		  if(messageType.equals(PaymentConstants.ONE) || messageType.equals(PaymentConstants.TWO) || messageType.equals(PaymentConstants.FIVE) || messageType.equals(PaymentConstants.SIX) || messageType.equals(PaymentConstants.SEVEN)){
				
		  }else{
			  paymentServiceBeanRc1.getTransErrorMsg().add(ErrorConstants.TRANS_MESSAGE_TYPE + ",   "+ messageType);
		  }
		  if(messageType.equals(PaymentConstants.ONE)|| messageType.equals(PaymentConstants.SEVEN)){
			  //refrance number
			  try{
			    boolean refFlag = new ReferenceNumberValidation().validateRefranceNumber(validateRefNo);
			    logger.debug("refFlag :::::::"+refFlag); 
			    if(refFlag==false){
				  paymentServiceBeanRc1.getTransErrorMsg().add(ErrorConstants.REFERANCE_NO + ",   "+ validateRefNo);
			    }
			  }catch(NumberFormatException nfe){
				  logger.error("Number Format Exception..."+nfe.getMessage());
			  }
		  }
		  if(paymentServiceBeanRc1.getTransErrorMsg().size() > 0){
				paymentServiceBeanRc1.setSeq_no(seqNo);
				paymentServiceBeanRc1.setPayees_name(payeesName);
				paymentServiceBeanRc1.setPayees_account_number(payeesAccNo);
				paymentServiceBeanRc1.setSum(transSumRecord);
				paymentServiceBeanRc1.setRefrance_no(validateRefNo);
				transMap.put(PaymentConstants.FAILED_RECORDS, paymentServiceBeanRc1);
		  }else{
			    transMap.put(PaymentConstants.SUCCESS_RECORDS, paymentServiceBeanRc1);    	
		  }
		return transMap;
	}
}
