package com.ope.patu.parsers;

import org.apache.log4j.Logger;
import com.coldcore.coloradoftp.session.Session;
import com.ope.patu.server.beans.MessageBean;
import com.ope.patu.server.constant.SecurityMessageConstants;
import com.ope.patu.server.constant.ServerConstants;
import com.ope.patu.util.FileUtil;

public class GenericParser implements com.ope.patu.handler.Parser 
{
	protected static Logger logger = Logger.getLogger(GenericParser.class);
	
	public Object parse(Object... objects) 
	{
		String fileName = ( String )objects[0];
		Session session = ( Session )objects[1];
		MessageBean msgBean = new MessageBean();
		
		boolean trFileStatus = false;
		/*
		 * Read the message as String
		 */
		logger.debug("***************** Inside GenericParser *****************");
		logger.debug("FileName----------|"+fileName);
		String filePath = FileUtil.getSessionTempPath(session, fileName);
		logger.debug("filePath----------|"+filePath);
		String fileContents = FileUtil.getFileContetns(filePath);
		logger.debug("File Contents----|"+fileContents);
		if( fileContents.startsWith(SecurityMessageConstants.ESI_MSG))
		{
			msgBean.setMsgType(SecurityMessageConstants.ESI);
		}
		else if( fileContents.startsWith(SecurityMessageConstants.SIIRTOPYYNTO) )
		{
			msgBean.setMsgType(SecurityMessageConstants.SIIRTOPYYNTO);
			// session.
			session.setAttribute(ServerConstants.TR_FILE_STATUS, ServerConstants.STATUS_YES);
			msgBean.setTrFileStatus(true);
		}
		if( fileContents.startsWith(SecurityMessageConstants.MSG_SUO))
		{
			msgBean.setMsgType(SecurityMessageConstants.SUO);
			msgBean.setFileName(fileName);
		}
		return msgBean;
	}
}
