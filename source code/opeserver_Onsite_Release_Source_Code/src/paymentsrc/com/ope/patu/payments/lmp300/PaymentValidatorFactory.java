package com.ope.patu.payments.lmp300;

public abstract class PaymentValidatorFactory 
{
	/**
	 * This is the factory class takes the input parameter as record type calls the corresponding implementation class
	 * @param objects
	 * @return
	 */
	public static PaymentValidator getValidator( Object...objects )
	{
		PaymentValidator paymentValidator = null;
		String name =  ( String )objects[0];
		if( name.equalsIgnoreCase(PaymentConstants.BATCH)){
			paymentValidator = new BatchValidatorImpl();
		}else if(name.equalsIgnoreCase(PaymentConstants.TRANSACTIONS)){
			paymentValidator = new TransactionValidatorImpl();
		}else if(name.equalsIgnoreCase(PaymentConstants.ITEMISATION)){
			paymentValidator = new ItemizationValidatorImpl();
		}else if(name.equalsIgnoreCase(PaymentConstants.SUM)){
			paymentValidator = new SumRecordValidatorImpl();
		}else if(name.equalsIgnoreCase(PaymentConstants.MESSAGETRANS)){
			paymentValidator = new MessageTypeValidatorImpl();
		}else if(name.equalsIgnoreCase(PaymentConstants.MESSAGEITEM)){
			paymentValidator = new MessageTypeValidatorImpl();
		}
		return paymentValidator;
	}
}
