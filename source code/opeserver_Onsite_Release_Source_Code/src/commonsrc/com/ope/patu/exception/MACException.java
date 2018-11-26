package com.ope.patu.exception;

public class MACException extends Exception
{
	public MACException()
	{
		super();
	}
	public MACException( String msg )
	{
		super( msg );
	}
	
	public String getMessage() 
	{
		String errorMsg = "MAC generation exception \nprobably error in mAC invocation";
		return errorMsg;
	}
	
}
