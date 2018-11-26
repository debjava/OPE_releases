package com.ope.patu.exception;

public class MessageParsingException extends Exception 
{
	private static final long serialVersionUID = -2362096307163010082L;

	public MessageParsingException()
	{
		super();
	}
	
	public MessageParsingException( String message )
	{
		super( message );
	}
	
	public String getExceptionMessage()
	{
		return "Error in parsing the message file";
	}
	
	public String getMessage() 
	{
		return "Error in parsing the message file";
	}
}
