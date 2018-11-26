package com.ope.patu.exception;

public class OPESecurityException extends Exception 
{

	/**
	 * Default generated serial version id
	 */
	private static final long serialVersionUID = 5590951646322090185L;
	
	public OPESecurityException()
	{
		super();
	}
	
	public OPESecurityException( String message )
	{
		super( message );
	}
	
	public String getExceptionMessage()
	{
		return "Error in security policy";
	}
}
