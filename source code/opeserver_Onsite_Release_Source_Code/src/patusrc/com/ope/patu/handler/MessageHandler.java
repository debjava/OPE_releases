package com.ope.patu.handler;

import com.ope.patu.exception.MessageHandlingException;

public interface MessageHandler 
{
	public Object handleObject( Object ...objects ) throws MessageHandlingException;
	public Object getMessageObject( Object ...objects );
	
}
