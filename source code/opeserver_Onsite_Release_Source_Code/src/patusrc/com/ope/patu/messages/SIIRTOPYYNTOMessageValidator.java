package com.ope.patu.messages;

import java.util.Properties;

import org.apache.log4j.Logger;
import com.coldcore.coloradoftp.session.Session;
import com.ope.patu.exception.MessageParsingException;
import com.ope.patu.handler.Parser;
import com.ope.patu.handler.Validator;
import com.ope.patu.parsers.TRMessageParser;
import com.ope.patu.server.beans.TransferRequestBean;
import com.ope.patu.server.constant.ServerConstants;
import com.ope.patu.util.FileUtil;

public class SIIRTOPYYNTOMessageValidator implements Validator 
{
	protected static Logger logger = Logger.getLogger(SIIRTOPYYNTOMessageValidator.class);
	private TransferRequestBean trBean; 

	public Object getValidatedObject(Object... objects) 
	{
		return trBean;
	}

	public boolean validate(Object... objects) 
	{
		boolean validationFlag = false;
		String msgContents = ( String )objects[0];
		String fileName = ( String )objects[1];
		Session session = ( Session )objects[2];
		Parser parser = new TRMessageParser();
		try
		{
			trBean = ( TransferRequestBean ) parser.parse(msgContents);
		}
		catch( MessageParsingException mpe )
		{
			logger.error(mpe);
			logger.error(mpe.getMessage());
			mpe.printStackTrace();
		}
//		List validationList = new ArrayList();
		/*
		 * Write the code to validate the TR message
		 */
		if( checkFileType(trBean) )
		{
			validationFlag = true;
			session.setAttribute(ServerConstants.TR_MSG_STATUS, new Boolean( validationFlag ));
			/*
			 * Store the Service code in the session
			 */
			session.setAttribute(ServerConstants.SERVICECODE, trBean.getFileType());
		}
		logger.debug("TR Message Validation flag-------"+validationFlag);
		return validationFlag;
	}
	//Validation of file types
	private boolean checkFileType( TransferRequestBean trBean )
	{
		boolean flag = false;
		Properties fileProp = FileUtil.getFileTypes();
		if( fileProp.containsKey( trBean.getFileType()))
			flag = true;
		else
			flag = false;
		return flag;
	}
}
