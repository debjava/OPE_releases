package com.ope.patu.server;

import com.coldcore.coloradoftp.command.Reply;
import com.coldcore.coloradoftp.command.impl.system.NotImplementedCommand;
import com.coldcore.coloradoftp.connection.DataConnection;
import com.coldcore.coloradoftp.connection.DataPortListener;
import com.coldcore.coloradoftp.connection.DataPortListenerSet;
import com.coldcore.coloradoftp.factory.ObjectFactory;
import com.coldcore.coloradoftp.factory.ObjectName;
import com.coldcore.coloradoftp.session.Session;
import com.coldcore.coloradoftp.session.SessionAttributeName;
import com.ope.patu.server.constant.ServerConstants;

public class NotSupportedCommand extends NotImplementedCommand 
{
	public Reply execute() 
	{
		Reply reply = (Reply) ObjectFactory.getObject(ObjectName.REPLY);
		reply.setCode("502");
		reply.setText( ServerConstants.UNSUPPORTED );
		/*
		 * Closing the session and the connection
		 */
		close(reply);
		return reply;
	}

	private void close(Reply reply)
	{
		try
		{
			controlConnection.getDataConnectionInitiator().abort();
			DataPortListenerSet listeners = (DataPortListenerSet) ObjectFactory.getObject(ObjectName.DATA_PORT_LISTENER_SET);
			for (DataPortListener listener : listeners.list())
				listener.removeConnection(controlConnection);

			Session session = getConnection().getSession();
			session.setAttribute(SessionAttributeName.BYTE_MARKER_POISONED, controlConnection.getBytesWrote());
			controlConnection.poison();

			DataConnection dataConnection = controlConnection.getDataConnection();

			reply.setCode("221");
			reply.setText("Logged out, closing control connection.");
//			if (dataConnection != null) 
//			{
//				reply.setCode("221");
//				reply.setText("Logged out, closing control connection.");
//			}
//			else
//			{
//				reply.setCode("221");
//				reply.setText("Logged out, closing control connection.");
//			}
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}


	public String getName() 
	{
		return ServerConstants.UNSUPPORTED ;
	}
}
