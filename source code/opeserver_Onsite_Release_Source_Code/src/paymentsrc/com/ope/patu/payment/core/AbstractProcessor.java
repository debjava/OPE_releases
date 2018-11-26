package com.ope.patu.payment.core;

public abstract class AbstractProcessor 
{
	public static PaymentProcessor getProcessor( String paymentTypeName )
	{
		PaymentProcessor paymentProcessor = null;
		try
		{
			String className = new StringBuilder("com.ope.patu.payments.")
					.append(paymentTypeName.toLowerCase()).append(".").append(
							paymentTypeName).append("Processor").toString();
			System.out.println("In abstractProcessDynamic class name-----"+className);
			paymentProcessor = ( PaymentProcessor ) Class.forName(className).newInstance() ;
		}
		catch( Exception e )
		{
			System.out.println("No implementation has been provided, no further processing");
			e.printStackTrace();
		}
		return paymentProcessor;
	}
}
