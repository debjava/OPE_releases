package com.ope.patu.handler;

import com.ope.patu.exception.MessageParsingException;

public interface Parser 
{
	public Object parse( Object ... objects ) throws MessageParsingException;

}
