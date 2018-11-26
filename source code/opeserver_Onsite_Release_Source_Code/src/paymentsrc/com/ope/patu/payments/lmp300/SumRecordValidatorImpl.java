package com.ope.patu.payments.lmp300;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import com.ope.patu.payment.utility.AccountNumberValidation;
import com.ope.patu.payments.lmp300.beans.PaymentServiceBeanRC0;
import com.ope.patu.payments.lmp300.beans.PaymentServiceBeanRC9;
import com.ope.patu.server.constant.ErrorConstants;

/**
 * This java class is for validating the sum type record
 * @author anandkumar.b
 *
 */
public class SumRecordValidatorImpl implements PaymentValidator
{
	protected static Logger logger = Logger.getLogger(SumRecordValidatorImpl.class);

	PaymentServiceBeanRC0 pmtserbean_rc0=null;
	public Object getValidatedObject(Object... objects) 
	{
		PaymentServiceBeanRC9 paymentServiceBeanRc9 = ( PaymentServiceBeanRC9 )objects[0];
		return paymentServiceBeanRc9;
	}
	/**
	 * Method does the all validations for sum type record and returns accepted or rejected status in the map contains list 
	 * @param paymentServiceBeanRC9
	 * @param pmtserbean_rc0
	 * @param transCount
	 * @param totalTransSum
	 * @return
	 */
	public Object getValidatedObject(PaymentServiceBeanRC9 paymentServiceBeanRC9, PaymentServiceBeanRC0 pmtserbean_rc0,int transCount, double totalTransSum, double totalCreditNoteSum, int totalCreditNoteTransaction, int recordLength) {
		Map sumMap = new HashMap();
		// TODO Auto-generated method stub
		DecimalFormat transDateFormat = new DecimalFormat( PaymentConstants.DEC_FORMAT );
		DecimalFormat noOfPayments = new DecimalFormat( PaymentConstants.NO_FORMAT );
		String totalTransactionSum = transDateFormat.format(totalTransSum);
		String totalSum = "";
		String totalCreditSum = "";
		String totalSumRecord = paymentServiceBeanRC9.getTotal_sum_of_payments();
		String sumCreditEntries = paymentServiceBeanRC9.getReserved_2();
		
		try{
			DecimalFormat dateFormat = new DecimalFormat( PaymentConstants.SUM_FORMAT );
			double totalSumValue = Double.parseDouble(totalSumRecord);
			String formatValue = dateFormat.format(totalSumValue);
			String beforeDecimal = formatValue.substring(0, 11).concat(".");
			String AfterDecimal = formatValue.substring(11,13);
			totalSum = beforeDecimal+AfterDecimal;
			
			double creditNoteSum = Double.parseDouble(sumCreditEntries);
			String noFormat = dateFormat.format(creditNoteSum);
			String prevalue = noFormat.substring(0, 11).concat(".");
			String postvalue = noFormat.substring(11,13);
			totalCreditSum = prevalue+postvalue;
			
		}catch(StringIndexOutOfBoundsException sioe){
			sioe.printStackTrace();
		}
		
		double transactionSum = Double.parseDouble(totalTransactionSum);
		double sumsSum = Double.parseDouble(totalSum);
		double totalCreditSumsSum = Double.parseDouble(totalCreditSum);

		String noOfPmts = paymentServiceBeanRC9.getNumber_of_payments();
		String noOfTransactionCount = noOfPayments.format( transCount );
		logger.debug("no_of_pmts in sum --->"+noOfPmts);
		logger.debug("no_trans_count    --->"+noOfTransactionCount);
		String payersAccNoInSum =paymentServiceBeanRC9.getPayers_account_number();
		String payersAccNoInTrans = pmtserbean_rc0.getPayers_account_number();
		String noOfCreditNoteTrans = paymentServiceBeanRC9.getReserved_1();
				
		int noOfCreditNoteTransactions = Integer.parseInt(noOfCreditNoteTrans);
		logger.debug("noOfCreditNoteTrans -->"+noOfCreditNoteTrans);
		logger.debug("sumCreditEntries    -->"+sumCreditEntries);
				
		boolean flag = new AccountNumberValidation().checkAccountNumber(payersAccNoInSum);
		logger.debug("flag :::"+flag);
		
		 if(recordLength!=300){
			 paymentServiceBeanRC9.getSumErrorMsg().add(ErrorConstants.RECORD_LENGTH + ", "+ recordLength);
		  }
		 
		if(flag==true){
			if(!payersAccNoInSum.equals(payersAccNoInTrans)){
				paymentServiceBeanRC9.getSumErrorMsg().add(ErrorConstants.SUM_ACC_NO + ",   "+ paymentServiceBeanRC9.getPayers_account_number());
			}
		}else{
			paymentServiceBeanRC9.getSumErrorMsg().add(ErrorConstants.SUM_ACC_NO + ",   "+ paymentServiceBeanRC9.getPayers_account_number());
		}
		if(!(paymentServiceBeanRC9.getPayers_business_identity_code().equals(pmtserbean_rc0.getPayers_business_identity_code()))){
			paymentServiceBeanRC9.getSumErrorMsg().add(ErrorConstants.SUM_IDENTITY_NO + ",   "+ paymentServiceBeanRC9.getPayers_business_identity_code());
		}
		if(!(paymentServiceBeanRC9.getFile_creation_date().equals(pmtserbean_rc0.getFile_creation_date()))){
			paymentServiceBeanRC9.getSumErrorMsg().add(ErrorConstants.SUM_CREATION_DATE + ",   "+ paymentServiceBeanRC9.getFile_creation_date());
		}
		//This validation we need to discuss with onsite people
		if(!(paymentServiceBeanRC9.getBatch_identifier().equals(pmtserbean_rc0.getBatch_identifier()))){
			//paymentServiceBean_RC9.getSumErrorMsg().add(ErrorConstants.SUM_BATCH_IDENTIFIER+ ",   "+ paymentServiceBean_RC9.getBatch_identifier());
		}
		logger.debug("sums noOfPmts              -->"+noOfPmts);
		logger.debug("trans noOfTransactionCount -->"+noOfTransactionCount);
		if(!(noOfPmts.equals(noOfTransactionCount))){
			paymentServiceBeanRC9.getSumErrorMsg().add(ErrorConstants.SUM_NO_OF_PAYMENTS+ ",   "+ noOfPmts);
		}
		
		logger.debug("sums  noOfCreditNoteTransactions -->"+noOfCreditNoteTransactions);
		logger.debug("trans totalCreditNoteTransaction -->"+totalCreditNoteTransaction);
		if(noOfCreditNoteTransactions != totalCreditNoteTransaction){
			paymentServiceBeanRC9.getSumErrorMsg().add(ErrorConstants.SUM_NO_OF_PAYMENTS+ ",   "+ noOfPmts);
		}
				
		logger.debug("trans_sum :::::"+transactionSum);
		logger.debug("sums_sum  :::::"+sumsSum);
		if(sumsSum != transactionSum){
			paymentServiceBeanRC9.getSumErrorMsg().add(ErrorConstants.SUM_TOTAL_NO_OF_PAYMENTS+ ",   "+ paymentServiceBeanRC9.getTotal_sum_of_payments());
		}
				
		logger.debug("trans totalCreditNoteSum :::::"+totalCreditNoteSum);
		logger.debug("Sums  creditNoteSum      :::::"+totalCreditSumsSum);
		if(totalCreditNoteSum != totalCreditSumsSum){
			paymentServiceBeanRC9.getSumErrorMsg().add(ErrorConstants.SUM_TOTAL_NO_OF_CREDIT_ENTRIES+ ",   "+ paymentServiceBeanRC9.getReserved_2());
		}
		try{
			if(paymentServiceBeanRC9.getSumErrorMsg().size() > 0){
				sumMap.put(ErrorConstants.SUM_FAILED, paymentServiceBeanRC9);
			}else{
				sumMap.put(ErrorConstants.SUM_SUCCESS, paymentServiceBeanRC9);
			}
		}catch(NullPointerException npe){
			sumMap.put(ErrorConstants.SUM_SUCCESS, paymentServiceBeanRC9);
			logger.error("Null value in sum record :::::"+npe.getMessage());
		}
		return sumMap;
	}
}
