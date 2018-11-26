package com.ope.patu.exception;

public class MessageHandlingException extends Exception 
{
	private static final long serialVersionUID = -8704377712776480024L;

	public MessageHandlingException() 
	{
		super();
	}

	public MessageHandlingException( String message ) 
	{
		super( message );
	}

	public String getMessage() 
	{
		return "Error in Handling the message";
	}
}
