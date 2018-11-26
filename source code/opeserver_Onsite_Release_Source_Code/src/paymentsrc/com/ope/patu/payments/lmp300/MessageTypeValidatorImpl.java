package com.ope.patu.payments.lmp300;

import com.ope.patu.payments.lmp300.beans.PaymentServiceBeanMsgRec;
/**
 * This class is for message type validations for feature enhancement use
 * @author anandkumar.b
 *
 */
public class MessageTypeValidatorImpl implements PaymentValidator
{
	/**
	 * This method is for message type record validations for feature implementation
	 */
	public Object getValidatedObject(Object... objects) 
	{
		PaymentServiceBeanMsgRec pmtserbean_rcmsgrec = ( PaymentServiceBeanMsgRec )objects[0];
		/**
		 * Provide all the implementation for the validation
		 */
		return null;
	}
}
