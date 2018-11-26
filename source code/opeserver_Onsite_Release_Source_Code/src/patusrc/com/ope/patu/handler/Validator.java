package com.ope.patu.handler;

import com.ope.patu.exception.FailedValidationException;

public interface Validator 
{
	public boolean validate( Object ... objects ) throws FailedValidationException;
	public Object getValidatedObject( Object ... objects );
}
