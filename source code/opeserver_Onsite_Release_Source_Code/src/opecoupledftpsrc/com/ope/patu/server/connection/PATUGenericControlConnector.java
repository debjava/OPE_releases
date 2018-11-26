package com.ope.patu.server.connection;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import org.apache.log4j.Logger;

import com.coldcore.coloradoftp.command.Command;
import com.coldcore.coloradoftp.command.CommandProcessor;
import com.coldcore.coloradoftp.connection.ConnectionPool;
import com.coldcore.coloradoftp.connection.ControlConnection;
import com.coldcore.coloradoftp.connection.ControlConnector;
import com.coldcore.coloradoftp.core.Core;
import com.coldcore.coloradoftp.core.CoreStatus;
import com.coldcore.coloradoftp.factory.ObjectFactory;
import com.coldcore.coloradoftp.factory.ObjectName;
import com.ope.patu.server.constant.ServerConstants;
import com.ope.patu.util.FTPFileUtil;

public class PATUGenericControlConnector implements ControlConnector, Runnable
{
	protected static Logger logger = Logger.getLogger(PATUGenericControlConnector.class);
	protected ServerSocketChannel ssc;
	protected int port;
	protected boolean bound;
	protected Core core;
	protected CommandProcessor commandProcessor;
	protected ConnectionPool controlConnectionPool;
	protected Thread thr;
	protected long sleep;

	public PATUGenericControlConnector()
	{
		super();
		/*
		 * Connecting to a different port ie 22 not the port 21.
		 */
		String serverPort = FTPFileUtil.getServerConfig().getProperty(ServerConstants.SERVERPORT);
		port = Integer.parseInt(serverPort);//22;
		sleep = 100L;
	}


	/** Configure connection before adding it to a pool
	 * @param connection Connection
	 */
	public void configure(ControlConnection connection) 
	{
		System.out.println("************** GenericControlConnector : GenericControlConnector ***********");
		if (core.getStatus() == CoreStatus.POISONED) {
			//Server is shutting down, reply and poison the connection
			Command command = (Command) ObjectFactory.getObject(ObjectName.COMMAND_POISONED);
			command.setConnection(connection);
			commandProcessor.execute(command);
			connection.poison();
		} else {
			//Server is ready, reply so
			Command command = (Command) ObjectFactory.getObject(ObjectName.COMMAND_WELCOME);
			command.setConnection(connection);
			commandProcessor.execute(command);
		}
	}


	public synchronized void bind() throws IOException {
		if (bound) {
			logger.warn("Connector on port "+port+" was bound when bind routine was submitted");
			throw new IllegalStateException("Unbind the connector on port "+port+" first");
		}

		//Get required objects
		core = (Core) ObjectFactory.getObject(ObjectName.CORE);
		commandProcessor = (CommandProcessor) ObjectFactory.getObject(ObjectName.COMMAND_PROCESSOR);
		controlConnectionPool = (ConnectionPool) ObjectFactory.getObject(ObjectName.CONTROL_CONNECTION_POOL);

		//Bind to the port
		ssc = ServerSocketChannel.open();
		ssc.socket().bind(new InetSocketAddress(port));

		//Start this class
		thr = new Thread(this);
		thr.start();

		bound = true;
		logger.info("Connector is bound to port "+port);
	}


	public synchronized void unbind() throws IOException {
		if (!bound) {
			logger.warn("Connector on port "+port+" was not bound when unbind routine was submitted");
			throw new IllegalStateException("Cannot unbind the connector on port "+port+", it is not bound");
		}

		//Set this now to prevent run() method from flooding errors after the socket is closed
		bound = false;

		//Unbind from the port
		if (ssc.isOpen()) ssc.close();

		//Wait for this class to stop (just in case)
		try {
			thr.join(30000);
		} catch (Throwable e) {}

		logger.info("Connector on port "+port+" is unbound");
	}


	public boolean isBound() {
		return bound;
	}


	public void run() {
		while (bound) {

			ControlConnection connection = null;
			SocketChannel sc = null;
			try {
				sc = ssc.accept(); //Thread blocks here...
				String ip = sc.socket().getInetAddress().getHostAddress();
				logger.debug("New control connection accepted (IP "+ip+")");

				//Create new connection instance and initialize it
				connection = (ControlConnection) ObjectFactory.getObject(ObjectName.CONTROL_CONNECTION);
				connection.initialize(sc);

				//Configure the control connection and add to pool
				configure(connection);
				controlConnectionPool.add(connection);
				logger.debug("New control connection is ready");

				Thread.sleep(sleep);

			} catch (Throwable e) {
				if (bound) logger.warn("Failed to accept a connection (ignoring)", e);
				try {
					connection.destroy();
				} catch (Throwable ex) {}
				try {
					sc.close();
				} catch (Throwable ex) {}
			}

		}
		logger.debug("Control connector thread finished");
	}


	public void setPort(int port) {
		if (port < 1) throw new IllegalArgumentException("Invalid port");
		this.port = port;
	}


	public int getPort() {
		return port;
	}


	/** Get thread sleep time
	 * @return Time in mills
	 */
	public long getSleep() {
		return sleep;
	}


	/** Set thread sleep time
	 * @param sleep Time in mills
	 */
	public void setSleep(long sleep) {
		this.sleep = sleep;
	}

}
