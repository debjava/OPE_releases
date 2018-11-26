package com.ope.patu.handler;

import org.apache.log4j.Logger;

import com.coldcore.coloradoftp.session.Session;
import com.ope.patu.parsers.GenericParser;
import com.ope.patu.server.beans.MessageBean;
import com.ope.patu.server.constant.ServerConstants;

/**
 * @author Debadatta Mishra
 *
 */
public class AbstractHandler 
{
	public static String msgTypeName = null;
	public static boolean trFileFlag = false;
	public static String paymentFileName = null;
	protected static Logger logger = Logger.getLogger(AbstractHandler.class);
	
	public static MessageHandler getMessageObject(String fileName , Session userSession )
	{
		MessageHandler msgHandler = null;
		try
		{
			if( fileName == null ) throw new NullPointerException();
			Parser msgParser = new GenericParser();
			MessageBean msgBean = (MessageBean) msgParser.parse(fileName,
					userSession);
			msgTypeName = msgBean.getMsgType();
			paymentFileName = msgBean.getFileName();
			trFileFlag = msgBean.getTrFileStatus();
			
			logger.debug("In Abstracthandler : getMessageObject --> TrFileStatus -->"+msgBean.getTrFileStatus());
			logger.debug("In Abstracthandler : getMessageObject --> FileName -->"+paymentFileName);
			logger.debug("In Abstracthandler : getMessageObject --> msgTypeName -->"+msgTypeName);
			
			Class cls = Class.forName("com.ope.patu.messages." + msgTypeName
					+ "MessageHandler");
			msgHandler = (MessageHandler) cls.newInstance();
		}
		catch( NullPointerException npe )
		{
			npe.printStackTrace();
			logger.debug("NullPointerException thrown");
			logger.debug("Error------"+npe.getMessage());
		}
		catch (InstantiationException e) 
		{
			e.printStackTrace();
			logger.debug("InstantiationException thrown");
			logger.debug("Error------"+e.getMessage());
		}
		catch (IllegalAccessException e) 
		{
			e.printStackTrace();
			logger.debug("InstantiationException thrown");
			logger.debug("Error------"+e.getMessage());
		} catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
			logger.debug("InstantiationException thrown");
			logger.debug("Error------"+e.getMessage());
		}
		catch( Exception e )
		{
			/*
			 * It means that SUO and VAR message are absent.
			 */
			e.printStackTrace();
			String errorMessage = "Not a valid bank message, no further processing will be done";
			logger.debug(errorMessage);
			logger.debug(e.getMessage());
			msgHandler = getDefaultHandler();
			userSession.setAttribute(ServerConstants.REPLYMSG, errorMessage);
		}
		return msgHandler;
	}
	
	private static MessageHandler getDefaultHandler()
	{
		MessageHandler msgHandler = null;
		try
		{
			msgTypeName = "SUO";
			Class cls = Class.forName("com.ope.patu.messages." + msgTypeName
					+ "MessageHandler");
			msgHandler = (MessageHandler) cls.newInstance();
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
		}
		return msgHandler;
	}
}
