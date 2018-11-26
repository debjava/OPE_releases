package com.ope.patu.server.connection;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.coldcore.coloradoftp.command.Reply;
import com.coldcore.coloradoftp.connection.Connection;
import com.coldcore.coloradoftp.connection.ControlConnection;
import com.coldcore.coloradoftp.factory.ObjectFactory;
import com.coldcore.coloradoftp.factory.ObjectName;
import com.coldcore.coloradoftp.plugin.intellipack.util.Util;


public class PATUIntelControlConnector extends PATUGenericControlConnector 
{
	protected static Logger logger = Logger.getLogger(PATUIntelControlConnector.class);
	protected int maxConnections;
	protected int connectionsPerHost;
	protected Map<String,Boolean> ipMap;

	public PATUIntelControlConnector()
	{
		super();
		ipMap = new LinkedHashMap<String,Boolean>();
		
		System.out.println("###################### Coming to PATUIntelControlConnector #########################");
		
		System.out.println("PATUIntelControlConnector : Defined port number-------->>>"+getPort());
	}

	//	  public IntelControlConnector() 
	//	  {
	//		  System.out.println("IntelControlConnector : IntelControlConnector");
	//		  log.info("IntelControlConnector : IntelControlConnector");
	//	    ipMap = new LinkedHashMap<String,Boolean>();
	//	    System.out.println("ipMap------"+ipMap);
	//	    log.info("ipMap------"+ipMap);
	//	  }


	/** Set map of IPs and allow/deny permissions.
	 *  If IP matches regexp of this map then it will be allowed or denied access
	 *  based on the value for the regexp (the first regexp wins, so use LinkedHashMap to
	 *  set the rules properly).
	 * @param map Map where key is IP regular expression and value is Boolean
	 */
	public void setIpMap(Map<String,Boolean> map) {
		if (map == null) throw new IllegalArgumentException("Invalid argument");
		synchronized (ipMap) {
			ipMap.clear();
			ipMap.putAll(map);
		}
	}


	/** Get copy of the map defining IPs regular expressions and allow/deny values.
	 * @return Map copy (not the original map)
	 */
	public Map<String,Boolean> getIpMap() {
		synchronized (ipMap) {
			return new LinkedHashMap<String,Boolean>(ipMap);
		}
	}


	/** Get max number of control connections allowed
	 * @return Max allowed connections
	 */
	public int getMaxConnections() {
		return maxConnections;
	}


	/** Set max number of control connections allowed
	 * @param maxConnections Max allowed connections
	 */
	public void setMaxConnections(int maxConnections) {
		this.maxConnections = maxConnections;
	}


	/** Get max number of control connections allowed from the same host
	 * @return Max allowed connections
	 */
	public int getConnectionsPerHost() {
		return connectionsPerHost;
	}


	/** Set max number of control connections allowed from the same host
	 * @param connectionsPerHost Max allowed connections
	 */
	public void setConnectionsPerHost(int connectionsPerHost) {
		this.connectionsPerHost = connectionsPerHost;
	}


	public void configure(ControlConnection connection) {
		//Test if too many connections
		int total = controlConnectionPool.size();
		if (controlConnectionPool.size() > maxConnections && maxConnections > 0) {
			logger.debug("Too many connections already (total "+total+")");
			Reply reply = (Reply) ObjectFactory.getObject(ObjectName.REPLY);
			reply.setCode("421");
			reply.setText("Too many connections already, try again later.");
			connection.reply(reply);
			connection.poison();
			return;
		}

		//Test if too many connections from the same host
		String ip = connection.getSocketChannel().socket().getInetAddress().getHostAddress();
		Set<Connection> set = controlConnectionPool.list();
		int count = 0;
		for (Connection c : set)
			try {
				if (c.isDestroyed()) continue;
				String s = c.getSocketChannel().socket().getInetAddress().getHostAddress();
				if (s.equals(ip)) count++;
			} catch (Throwable e) {}

			if (count >= connectionsPerHost && connectionsPerHost > 0) {
				logger.debug("Too many connections from the same host already (total "+count+")");
				Reply reply = (Reply) ObjectFactory.getObject(ObjectName.REPLY);
				reply.setCode("421");
				reply.setText("Too many connections from you already.");
				connection.reply(reply);
				connection.poison();
				return;
			}

			//IP address test
			synchronized (ipMap) {
				for (String regexp : ipMap.keySet()) {
					if (Util.checkRegExp(ip, regexp)) {
						boolean allow = ipMap.get(regexp);
						logger.debug("IP ["+ip+"] matches regexp ["+regexp+"], allow ["+allow+"]");

						if (!allow) {
							Reply reply = (Reply) ObjectFactory.getObject(ObjectName.REPLY);
							reply.setCode("421");
							reply.setText("Service not available, your host is blocked.");
							connection.reply(reply);
							connection.poison();
							return;
						}

						break;
					}
				}
			}

			//Continue as normal
			super.configure(connection);
	}

}
