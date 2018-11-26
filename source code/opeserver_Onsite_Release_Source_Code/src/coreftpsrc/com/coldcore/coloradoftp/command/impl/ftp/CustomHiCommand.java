package com.coldcore.coloradoftp.command.impl.ftp;

import org.apache.log4j.Logger;

import com.coldcore.coloradoftp.command.Reply;
import com.coldcore.coloradoftp.command.impl.AbstractCommand;

public class CustomHiCommand extends AbstractCommand
{
	
	static
	{
		System.out.println(" *********** HI **************** ");
	}
	private static Logger log = Logger.getLogger(CustomHiCommand.class);
	
	public CustomHiCommand()
	{
		super();
		System.out.println("Coming to CustomHiCommand");
	}

	
	public Reply execute()
	{
		Reply reply = null;
		try
		{
			System.out.println("Inside execute method of CustomHiCommand*******");
		    reply = getReply();
		    String path = getParameter();
		    if (path.length() == 0) {
		      reply.setCode("501");
		      reply.setText("******************");
		      return reply;
		    }
		    reply.setCode("250");
		    reply.setText("####################");
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		return reply;
		
	  }
}
