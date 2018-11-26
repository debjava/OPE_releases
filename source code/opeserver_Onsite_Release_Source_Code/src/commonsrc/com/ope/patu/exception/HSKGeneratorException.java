package com.ope.patu.exception;

public class HSKGeneratorException extends Exception 
{
	public HSKGeneratorException()
	{
		super();
	}
	public HSKGeneratorException( String msgString )
	{
		super( msgString );
	}
	
	public String getMessage() 
	{
		String msgString = "Error in generating the HSK,\nprobably generate method has not been invoked properly";
		return msgString;
	}
}
