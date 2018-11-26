package com.ope.patu.parsers;

import com.ope.patu.server.beans.KuittausBean;

public class KUITTAUSMessageParser implements com.ope.patu.handler.Parser 
{
	public Object parse(Object... objects) 
	{
		/*
		 * Write the logic to parse the KUITTAUS information
		 * and return the object.
		 */
		String fileContents = ( String )objects[0];
		KuittausBean kBean = new KuittausBean();
		kBean.setFileType( fileContents.substring(0,10));
		kBean.setDate(fileContents.substring(63, 69));
		return kBean;
	}
}
