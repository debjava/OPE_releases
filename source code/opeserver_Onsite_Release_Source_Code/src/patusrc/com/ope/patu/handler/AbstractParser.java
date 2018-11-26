package com.ope.patu.handler;

import org.apache.log4j.Logger;
/**
 * @author Debadatta Mishra
 *
 */
public abstract class AbstractParser 
{
	protected static Logger logger = Logger.getLogger(AbstractParser.class);
	
	public static Parser getParser( String msgTypeName )
	{
		Parser parser = null;
		try
		{
			if( msgTypeName == null ) throw new NullPointerException();
			String className = "com.ope.patu.parsers."+msgTypeName+"MessageParser";
			parser = ( Parser )Class.forName( className ).newInstance();
		}
		catch( NullPointerException npe )
		{
			npe.printStackTrace();
			logger.error(npe);
			logger.error(npe.getMessage());
		}
		catch (InstantiationException e) 
		{
			logger.error( e );
			e.printStackTrace();
		}
		catch (IllegalAccessException e) 
		{
			logger.error( e );
			e.printStackTrace();
		}
		catch (ClassNotFoundException e) 
		{
			logger.error( e );
			e.printStackTrace();
		}
		catch( Exception e )
		{
			e.printStackTrace();
			logger.equals(e);
			logger.error(e.getMessage());
		}
		return parser;
	}
}
