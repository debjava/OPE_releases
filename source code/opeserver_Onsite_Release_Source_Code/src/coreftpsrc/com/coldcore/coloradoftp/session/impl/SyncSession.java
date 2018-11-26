/**
 * @see com.coldcore.coloradoftp.session.Session
 *
 * This class is thread safe as it takes care of all synchronizations.
 */
package com.coldcore.coloradoftp.session.impl;

import com.coldcore.coloradoftp.connection.ControlConnection;
import com.coldcore.coloradoftp.session.Session;

import java.util.*;

public class SyncSession implements Session {

	protected Map<String,Object> attributes;
	protected ControlConnection controlConnection;
	/*
	 * Added by Debadatta Mishra
	 */

	private int sessionId = 0;
	private Random random = null;//new Random();

	public SyncSession() {
		attributes = Collections.synchronizedMap(new HashMap<String,Object>());
		/*
		 * Added by Debadatta Mishra
		 */
		random = new Random();
		random.setSeed(System.currentTimeMillis());
		sessionId = random.nextInt();
		sessionId = sessionId < 0 ? (-1)*sessionId : sessionId;
		sessionId = get9CharSessionId(sessionId);
	}


	public void setAttribute(String key, Object value) {
		attributes.put(key, value);
	}


	public Object getAttribute(String key) {
		return attributes.get(key);
	}


	public void removeAttribute(String key) {
		attributes.remove(key);
	}


	public Set<String> getAttributeNames() {
		return new HashSet<String>(attributes.keySet());
	}


	public ControlConnection getControlConnection() {
		return controlConnection;
	}


	public void setControlConnection(ControlConnection controlConnection) {
		this.controlConnection = controlConnection;
	}

	public int getSessionId() 
	{
		return sessionId;
	}
	
	public static int get9CharSessionId( int sessionId )
	{
		int charsessionId = sessionId;
		int sessionIdLen = String.valueOf(sessionId).length();
		if(sessionIdLen > 9)
		{
			charsessionId = sessionId/10;
		}
		if( sessionIdLen < 9 )
		{
			charsessionId = sessionId*10;
		}
		return charsessionId;
	}
	
	public void setSessionId(int sessionId) 
	{
		this.sessionId =sessionId ;
	}
	
	public Map getSessionMap(){
		
		return attributes;
	}
}
