package com.ope.patu.payments.lmp300;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

import com.coldcore.coloradoftp.session.Session;
import com.ope.patu.payment.db.PaymentDbImpl;
import com.ope.patu.payment.utility.AccountNumberValidation;
import com.ope.patu.payments.lmp300.beans.PaymentServiceBeanRC0;
import com.ope.patu.server.constant.ErrorConstants;
import com.ope.patu.server.constant.ServerConstants;
import com.ope.patu.util.DateUtil;

/**
 * This java file is for validating the batch record
 * @author anandkumar.b
 *
 */

public class BatchValidatorImpl implements PaymentValidator
{
	protected static Logger logger = Logger.getLogger(BatchValidatorImpl.class);
	/**
	 * This is the method takes the input as batch record and does the validation and returns the map to payment service validator
	 * This map contains success batch or failed batch message 
	 */
	public Object getValidatedObject(Object... objects) 
	{
		Map batchMap = new HashMap();
		boolean dateFlag = false;
		PaymentServiceBeanRC0 paymentServiceBeanRC0 = ( PaymentServiceBeanRC0 )objects[0];
		Session session = (Session)objects[1];
		
		String serviceBureau = (String)session.getAttribute(ServerConstants.SERVICEBUREAUID);
		String serviceType = (String)session.getAttribute(ServerConstants.SERVICECODE);
		
		/**
		 * Provide all the implementation for the batch validation
		 */
		try{
			int lineLength = paymentServiceBeanRC0.getLine_length();
			String dataCode = paymentServiceBeanRC0.getData_code();
			String sendersCode = paymentServiceBeanRC0.getPayers_business_identity_code();
			String fileCreationDate = paymentServiceBeanRC0.getFile_creation_date();
			String fileCreationTime = paymentServiceBeanRC0.getFile_creation_time();
			String payersName = paymentServiceBeanRC0.getPayers_name_qualifier();
			String accountNo = paymentServiceBeanRC0.getPayers_account_number();
			String code = paymentServiceBeanRC0.getEdi_code();
			String dueDate = paymentServiceBeanRC0.getDue_date();
			logger.debug("Due date in DomesticBill Payment----------->"+dueDate);
			/*
			 * Addded by Debadatta Mishra at onsite to provide
			 * the currency code validation.
			 */
			String currencyCode = paymentServiceBeanRC0.getCurrency_code();
			logger.debug("In Batch validation, obtained currency code :---->"+currencyCode);
//			String serviceId = paymentServiceBeanRC0.getService_code();//(String)session.getAttribute(ServerConstants.SERVICEID);
			/*
			 * The following line is added to fix the bug related service id.
			 * In the payment file, service id may appear in two fields, but
			 * service id should be taken from the session.
			 * @Debadatta Mishra
			 * Date : Jan 02,2009
			 */
			String serviceId = (String)session.getAttribute(ServerConstants.SERVICEID);
			String payersBusinessIdentityCode = paymentServiceBeanRC0.getPayers_business_identity_code();
			if(dataCode!=null){
				boolean validAccountNo = new AccountNumberValidation().checkAccountNumber(accountNo);
				boolean accountExist = false;
				/**
				 * parameters changed for data base account validation 
				 */
				if(validAccountNo==true){
					accountExist = new PaymentDbImpl().checkOPEAccountNumber(accountNo,serviceId,serviceType,serviceBureau);
				}else{
					accountExist = false;
				}
				StringBuffer dateValidation = new StringBuffer();
				/*
				 * Added by Debadatta Mishra to fix the issue related to
				 * due date in Domestic Bill Payment
				 */
//				String validDate = dateValidation.append(fileCreationDate.substring(0, 2)).append(".").append(fileCreationDate.substring(2, 4)).append(".").append(fileCreationDate.substring(4, 6)).toString();
				String validDate = dateValidation.append(
						dueDate.substring(0, 2)).append(".").append(
								dueDate.substring(2, 4)).append(".").append(
										dueDate.substring(4, 6)).toString();
				logger.debug("valid_date::::::"+validDate);
				try
				{
					/*
					 * Added by Debadatta Mishra to fix the issue related to
					 * due date validation. System has to validate whether it is
					 * a correct date or not, For example 31-Nov-2008 or 31-Apr-2008.
					 */
					boolean isDate = DateUtil.isDateValid(validDate, PaymentConstants.DATE_FMT);
					logger.debug("Is a date flag---->"+isDate);
					boolean isValidDate = PaymentDbImpl.getDueDateValidations(validDate,PaymentConstants.DATE_FMT,PaymentConstants.DATE_FMT);
//					dateFlag = new PaymentDbImpl().getDueDateValidations(validDate,PaymentConstants.DATE_FMT,PaymentConstants.DATE_FMT);
					dateFlag = (isDate == true && isValidDate == true ) ? true : false;
					logger.debug("Due date flag for Domestic Bill Payment:::"+dateFlag);
				}catch(Exception ee){
					ee.printStackTrace();
				}
				
				logger.debug("BATCH ACCOUNT NUMBER :::::::"+accountExist);
				//serviceId = new PaymentDbImpl().getServiceIdInfo(serviceId);
				try{
					batchMap.put(PaymentConstants.SENDERS_CODE, sendersCode);
					batchMap.put(PaymentConstants.FILE_CREATION_DATE,fileCreationDate);
					batchMap.put(PaymentConstants.FILE_CREATION_TIME, fileCreationTime);
					batchMap.put(PaymentConstants.PAYRS_NAME, payersName);
					batchMap.put(PaymentConstants.ACCOUNT_NO, accountNo);
					batchMap.put(PaymentConstants.CODE, code);
					batchMap.put(PaymentConstants.DUE_DATE, dueDate);
				}catch(NullPointerException npe){
					npe.printStackTrace();
				}
				if(accountExist == false){	
					paymentServiceBeanRC0.getErrorMsg().add(ErrorConstants.BATCH_ACC_NO + ", "+ accountNo);			  
				}
				if(dateFlag == false){
					paymentServiceBeanRC0.getErrorMsg().add(ErrorConstants.BATCH_DATE + ", "+ validDate);
				}
				if(serviceId.length()==0 && payersBusinessIdentityCode.length()==0){	
					paymentServiceBeanRC0.getErrorMsg().add(ErrorConstants.SERVICEID_BUSINESS_CODE + ", "+ serviceId +"  "+payersBusinessIdentityCode);			  
				}
				if(lineLength != 300){
					paymentServiceBeanRC0.getErrorMsg().add(ErrorConstants.BATCH_LINE + ", "+ lineLength);
				}
				/*
				 * Added by Debadatta Mishra at onsite for currency code
				 * validation.
				 */
				if(!(currencyCode.equals(PaymentConstants.ONE)))
				{
					logger.debug("In Batch record validation, currency code validation failed");
					paymentServiceBeanRC0.getErrorMsg().add(ErrorConstants.CURRENCY_CODE + ", "+ currencyCode);
				}
				if(!(dataCode.equals(PaymentConstants.DATA_CODE_LM03)) && !(dataCode.equals(PaymentConstants.DATA_CODE_LM02))){
					paymentServiceBeanRC0.getErrorMsg().add(ErrorConstants.DATA_CODE + ", "+ dataCode);
				}
				batchMap.put(PaymentConstants.BATCH_FAILED, paymentServiceBeanRC0);  
			}else{
				batchMap.put(PaymentConstants.BATCH_FAILED, paymentServiceBeanRC0);
			}
		}catch(NullPointerException npe){
			npe.printStackTrace();
		}
		return batchMap;
	}
}
