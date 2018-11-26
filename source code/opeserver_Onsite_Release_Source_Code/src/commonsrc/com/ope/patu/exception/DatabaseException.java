package com.ope.patu.exception;

public class DatabaseException extends Exception 
{
	private static final long serialVersionUID = 1975738456552050002L;

	public DatabaseException() 
	{
		super();
	}
	
	public DatabaseException( String message ) 
	{
		super( message );
	}
	
	public String getMessage()
	{
		return "Server has encountered a wrong data from the database";
	}
}
