package com.ope.patu.payments.lmp300;

/**
 * This is the generic interface for all bill payment validations 
 * @author anandkumar.b
 *
 */
public interface PaymentValidator 
{
	public Object getValidatedObject( Object ...objects );
}
