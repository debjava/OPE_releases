package com.ope.patu.handler;

import org.apache.log4j.Logger;

import com.ope.patu.server.constant.SecurityMessageConstants;

public abstract class ValidatorFactory 
{
	protected static Logger logger = Logger.getLogger(ValidatorFactory.class);
	public static Validator getValidator( String msgType )
	{
		Validator validator = null;
		try {
			validator = (Validator) Class.forName(SecurityMessageConstants.VALIDATOR_CLASS + msgType + SecurityMessageConstants.MESSAGE_VALIDATOR)
					.newInstance();
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
		return validator;
	}
}
