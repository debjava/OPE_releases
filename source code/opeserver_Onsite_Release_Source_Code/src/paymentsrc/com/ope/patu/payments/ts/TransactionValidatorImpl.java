package com.ope.patu.payments.ts;

import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import com.ope.patu.payment.db.PaymentDbImpl;
import com.ope.patu.payment.utility.AccountNumberValidation;
import com.ope.patu.payments.lmp300.PaymentConstants;
import com.ope.patu.payments.ts.beans.SalarySumBean;
import com.ope.patu.payments.ts.beans.SalaryTransactionBean;
import com.ope.patu.util.DateUtil;

/**
 * This is the implementation class for salary payment service transaction validations
 * @author anandkumar.b
 **/
public class TransactionValidatorImpl implements SalaryValidator
{
	protected static Logger logger = Logger.getLogger(TransactionValidatorImpl.class);
	/**
	 * Method will do the all validations related to the transaction records returns the bean object  
	 */
	public Object getValidatedObject( Object... objects ) 
	{
		Map tempMap=null;
		StringBuffer dateBuf = new StringBuffer();
		double sumOfTransactions=0.0;
		SalaryTransactionBean transactionBean = ( SalaryTransactionBean )objects[0];
		
		SalarySumBean salarySumBean = ( SalarySumBean ) objects[1];
		//int seqCount = ( Integer )objects[2];
		boolean dueDateFlag = false;
		boolean accountNoFlag = false;
		
		/**
		 * Provide all the implementation for the validation
		 */
		try{
						
			double transactionSum = Double.parseDouble(transactionBean.getAmount());
			sumOfTransactions = sumOfTransactions + transactionSum;
			
			if(!transactionBean.getRecord_code().equals( SalaryConstants.ONE)){
				transactionBean.getTransErrorMsg().add(SalaryErrorConstants.INVALID_REC_CODE+", "+transactionBean.getRecord_code());
			}
			
			accountNoFlag  = new AccountNumberValidation().checkAccountNumber(transactionBean.getPayees_acc_no());
			if(accountNoFlag == false){
				//boolean accountFlag = new PaymentDbImpl().checkOPEAccountNumber(payeesAccNo, serviceId, serviceType,  serviceBureau);
				transactionBean.getTransErrorMsg().add(SalaryErrorConstants.ACC_NOT_EXIST+",  "+transactionBean.getPayees_acc_no());
			}
			String transDate = transactionBean.getPayment_date();
			String validDate = dateBuf.append(transDate.substring(0, 2)).append(".").append(transDate.substring(2, 4)).append(".").append(transDate.substring(4, 6)).toString();
			
			boolean flag = new DateUtil().isDateValid(validDate, PaymentConstants.DATE_FMT);
			try{
				if(flag==true){
					dueDateFlag = new PaymentDbImpl().getDueDateValidations(transactionBean.getPayment_date(),SalaryConstants.DATE_FMT_IN, SalaryConstants.DATE_FMT);
				}
			}catch(Exception e){
				logger.error("Date validation error :::"+e.getMessage());
			}
			logger.debug("due Date Flag -->"+dueDateFlag);
			String reasonCode = transactionBean.getReasion_for_payment();
			if(!(transactionBean.getPayment_date().equals(salarySumBean.getPayment_date()))){
				//transactionBean.getTransRejectedMsg().add(SalaryErrorConstants.INVALID_SUM_DATE+",  "+transactionBean.getPayment_date());
				transactionBean.getTransErrorMsg().add(SalaryErrorConstants.INVALID_SUM_DATE+",  "+transactionBean.getReasion_for_payment());
			}
			if(!(transactionBean.getReasion_for_payment().equals(salarySumBean.getReason_for_payments()))){
				//transactionBean.getTransRejectedMsg().add(SalaryErrorConstants.INVALID_REASON_PMT+",  "+transactionBean.getReasion_for_payment());
				transactionBean.getTransErrorMsg().add(SalaryErrorConstants.INVALID_REASON_PMT+",  "+transactionBean.getReasion_for_payment());
			}
			if(dueDateFlag==false){
				//transactionBean.getTransRejectedMsg().add(SalaryErrorConstants.INVALID_DATE+",  "+transactionBean.getPayment_date());
				transactionBean.getTransErrorMsg().add(SalaryErrorConstants.INVALID_DATE+",  "+transactionBean.getPayment_date());
			}
			if(accountNoFlag==false){
			//if(seqCount==1){
				transactionBean.getTransErrorMsg().add(SalaryErrorConstants.TRANS_ACC_NO+",  "+transactionBean.getPayees_acc_no());
			}
			if (!(reasonCode.startsWith(SalaryConstants.REASON_CODE_10)
					|| reasonCode.startsWith(SalaryConstants.REASON_CODE_15)
					|| reasonCode.startsWith(SalaryConstants.REASON_CODE_17)
					|| reasonCode.startsWith(SalaryConstants.REASON_CODE_18)
					|| reasonCode.startsWith(SalaryConstants.REASON_CODE_19)
					|| reasonCode.startsWith(SalaryConstants.REASON_CODE_20)
					|| reasonCode.startsWith(SalaryConstants.REASON_CODE_30)
					|| reasonCode.startsWith(SalaryConstants.REASON_CODE_31)
					|| reasonCode.startsWith(SalaryConstants.REASON_CODE_34)
					|| reasonCode.startsWith(SalaryConstants.REASON_CODE_36)
					|| reasonCode.startsWith(SalaryConstants.REASON_CODE_50)
					|| reasonCode.startsWith(SalaryConstants.REASON_CODE_51)
					|| reasonCode.startsWith(SalaryConstants.REASON_CODE_55)
					|| reasonCode.startsWith(SalaryConstants.REASON_CODE_56)
					|| reasonCode.startsWith(SalaryConstants.REASON_CODE_59)
					|| reasonCode.startsWith(SalaryConstants.REASON_CODE_60)
					|| reasonCode.startsWith(SalaryConstants.REASON_CODE_61)
					|| reasonCode.startsWith(SalaryConstants.REASON_CODE_62)
					|| reasonCode.startsWith(SalaryConstants.REASON_CODE_63)
					|| reasonCode.startsWith(SalaryConstants.REASON_CODE_64)
					|| reasonCode.startsWith(SalaryConstants.REASON_CODE_68)
					|| reasonCode.startsWith(SalaryConstants.REASON_CODE_90))) {

				transactionBean.getTransErrorMsg().add(SalaryErrorConstants.REASON_CODE+",  "+reasonCode);
			}
			if(transactionBean.getAmount().equals(SalaryErrorConstants.ZERO) || transactionBean.getAmount().equals(SalaryErrorConstants.AMOUNT_NUM)){
				transactionBean.getTransErrorMsg().add(SalaryErrorConstants.AMOUNT+",  "+transactionBean.getAmount());
			}

			if(transactionBean.getPayees_identity_no().length()==0 || transactionBean.getPayees_identity_no().equals(SalaryErrorConstants.PAYEES_IDENTITY)){
				transactionBean.getTransErrorMsg().add(SalaryErrorConstants.PAYEES_IDENTITY_NO+",  "+transactionBean.getPayees_identity_no());
			}

			if(!(transactionBean.getPayment_date().equals(salarySumBean.getPayment_date()))){
				//transactionBean.getTransRejectedMsg().add(SalaryErrorConstants.PAYMENT_DATE+",  "+transactionBean.getPayment_date());
				transactionBean.getTransErrorMsg().add(SalaryErrorConstants.PAYMENT_DATE+",  "+transactionBean.getPayment_date());
			}

			if(!(transactionBean.getReasion_for_payment().equals(salarySumBean.getReason_for_payments()))){
				transactionBean.getTransErrorMsg().add(SalaryErrorConstants.PAYMENT_REASON+",  "+transactionBean.getReasion_for_payment());
				//transactionBean.getTransRejectedMsg().add(SalaryErrorConstants.PAYMENT_REASON+",  "+transactionBean.getReasion_for_payment());
			}
		}catch(NullPointerException npe){
			logger.error("Null pointer exception in salary transactions");
		}catch(Exception e){
			e.printStackTrace();
		}
		return transactionBean;
	}
}
