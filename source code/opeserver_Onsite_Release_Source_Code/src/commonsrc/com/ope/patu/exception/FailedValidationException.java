package com.ope.patu.exception;

public class FailedValidationException extends Exception 
{
	
	private static final long serialVersionUID = -5795649241130073631L;

	public FailedValidationException() 
	{
		super();
	}
	
	public FailedValidationException( String message ) 
	{
		super( message );
	}
	
	public String getMessage() 
	{
		return "Validation failed";
	}
}
