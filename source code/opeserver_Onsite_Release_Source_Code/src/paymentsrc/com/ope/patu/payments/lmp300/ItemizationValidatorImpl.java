package com.ope.patu.payments.lmp300;

import java.util.HashMap;
import java.util.Map;

import com.ope.patu.payment.utility.ReferenceNumberValidation;
import com.ope.patu.payments.lmp300.beans.PaymentServiceBeanRC1;
import com.ope.patu.server.constant.ErrorConstants;
/**
 * This class is for validation to Itemization
 * @author anandkumar.b
 *
 */
public class ItemizationValidatorImpl implements PaymentValidator
{
	/**
	 * Method takes the input parameter as a itemization record and does the all validations and returns the
	 * itemization map contains success records and failed records.  
	 */
	public Object getValidatedObject(Object... objects) 
	{
		Map itemizationMap = new HashMap();
		boolean flag=false;
		PaymentServiceBeanRC1 paymentServiceBeanRC1 = ( PaymentServiceBeanRC1 )objects[0];
		/**
		 * Provide all the implementation for the validation
		 */
		int seqNo = paymentServiceBeanRC1.getSeq_no();
		String payeesName = paymentServiceBeanRC1.getPayees_name();
		String payeesAccountNo = paymentServiceBeanRC1.getItemisation_acc_no();
		String itemizationSumRecord = paymentServiceBeanRC1.getItemisation_sum();
		String refranceNo = paymentServiceBeanRC1.getMessage();
		String messageType = paymentServiceBeanRC1.getMessage_type();
		String validRefNo="";
		try{
			validRefNo = refranceNo.substring(0, 20);
		}catch(StringIndexOutOfBoundsException sie){
			System.out.println(sie.getMessage());
		}

		if( !(paymentServiceBeanRC1.getItemisation_acc_no().equals(paymentServiceBeanRC1.getPayees_account_number()))){
			paymentServiceBeanRC1.getItemisationErrorMsg().add(ErrorConstants.ITEM_ACC_NO + ",   "+ paymentServiceBeanRC1.getItemisation_acc_no());
		}
		if(!(paymentServiceBeanRC1.getItemisation_msg_type().equals(paymentServiceBeanRC1.getMessage_type()))){
			paymentServiceBeanRC1.getItemisationErrorMsg().add(ErrorConstants.ITEM_MSG_TYPE + ",   "+ paymentServiceBeanRC1.getItemisation_msg_type());
		}
		if(!(paymentServiceBeanRC1.getItemisation_sum().equals(paymentServiceBeanRC1.getSum()))){
			paymentServiceBeanRC1.getItemisationErrorMsg().add(ErrorConstants.ITEM_SUM + ",   "+ paymentServiceBeanRC1.getItemisation_sum());
		}
		if(messageType.equals(PaymentConstants.ONE)|| messageType.equals(PaymentConstants.SEVEN)){
			//checks the reference number valid or not 
			boolean refranceFlag = new ReferenceNumberValidation().validateRefranceNumber(validRefNo);
			if(refranceFlag==false){
				paymentServiceBeanRC1.getTransErrorMsg().add(ErrorConstants.REFERANCE_NO + ",   "+ validRefNo);
			}
		}
		try{
			if(paymentServiceBeanRC1.getItemisationErrorMsg().size()>0){
				flag=true;
			}else{
				flag=false;
			}
		}catch(NullPointerException npe){
			flag = false;
			npe.printStackTrace();
		}
		if(flag==true){
			paymentServiceBeanRC1.setSeq_no(seqNo);
			paymentServiceBeanRC1.setPayees_name(payeesName);
			paymentServiceBeanRC1.setPayees_account_number(payeesAccountNo);
			paymentServiceBeanRC1.setSum(itemizationSumRecord);
			paymentServiceBeanRC1.setRefrance_no(refranceNo);
			itemizationMap.put(ErrorConstants.ITEM_FAILED, paymentServiceBeanRC1);
		}
		return itemizationMap;
	}
}
