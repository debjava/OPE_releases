package com.coldcore.coloradoftp.command.impl.ftp;

import com.coldcore.coloradoftp.command.Reply;
import com.coldcore.coloradoftp.command.impl.AbstractCommand;
import com.coldcore.coloradoftp.factory.ObjectFactory;
import com.coldcore.coloradoftp.factory.ObjectName;
import com.coldcore.coloradoftp.filesystem.FileSystem;

public class CustomDeleCommand extends AbstractCommand 
{
	
	 public Reply execute() 
	  {
		  System.out.println("************  CustomDeleCommand : execute() **************");
	    Reply reply = getReply();
	    System.out.println("1");
	    
	    
	    
	    
	    
	    if (!testLogin()) return reply;
	    System.out.println("2");
	    String path = getParameter();
	    System.out.println("3");
	    System.out.println("File Path---------"+path);
	    
	    
	    
	    if (path.length() == 0) 
	    {
	    	System.out.println("4");
	      reply.setCode("501");
	      System.out.println("5");
	      reply.setText("Send path name.");
	      System.out.println("6");
	      return reply;
	      
	    }

	    FileSystem fileSystem = (FileSystem) ObjectFactory.getObject(ObjectName.FILESYSTEM);
	    System.out.println("7");
	    fileSystem.deletePath(path, controlConnection.getSession());
	    System.out.println("8");

	    reply.setCode("250");
	    System.out.println("9");
	    reply.setText("Path deleted.");
	    System.out.println("10");
	    return reply;
	  }

}
