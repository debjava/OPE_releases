package com.coldcore.coloradoftp.connection.impl;

import com.ope.patu.ejb.server.OpeEjbHome;
import com.ope.patu.ejb.server.OpeEjbRemote;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.ejb.CreateException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.coldcore.coloradoftp.command.Reply;
import com.coldcore.coloradoftp.connection.ControlConnection;
import com.coldcore.coloradoftp.connection.DataConnection;
import com.coldcore.coloradoftp.connection.DataConnectionCallback;
import com.coldcore.coloradoftp.connection.DataConnectionMode;
import com.coldcore.coloradoftp.connection.TransferAbortedException;
import com.coldcore.coloradoftp.connection.TransferCompleteException;
import com.ope.patu.wrapper.MessageMap;
import com.ope.patu.wrapper.TransferMap;
import com.coldcore.coloradoftp.factory.ObjectFactory;
import com.coldcore.coloradoftp.factory.ObjectName;
import com.coldcore.coloradoftp.session.Session;
import com.coldcore.coloradoftp.session.SessionAttributeName;
import com.ope.patu.server.beans.MessageBean;
import com.ope.patu.server.beans.TransferRequestBean;
import com.ope.patu.server.constant.ServerConstants;
import com.ope.patu.util.FTPFileUtil;

/**
 * @see com.coldcore.coloradoftp.connection.DataConnection
 */
public class GenericDataConnection extends GenericConnection implements DataConnection {

	private static Logger log = Logger.getLogger(GenericDataConnection.class);
	protected ControlConnection controlConnection;
	protected ReadableByteChannel rbc;
	protected WritableByteChannel wbc;
	protected DataConnectionMode mode;
	protected String filename;
	protected boolean userAborted;
	protected boolean successful;
	protected boolean skipReply;
	protected DataConnectionCallback callback;


	
	public GenericDataConnection(int bufferSize) {
		super();

		log.debug("************ GenericDataConnection : GenericDataConnection ***************");
		//rbuffer = ByteBuffer.allocateDirect(bufferSize);
		rbuffer = ByteBuffer.allocate(bufferSize);
		rbuffer.flip();
	}


	/** Read data from user */
	protected void read() throws Exception {
		/* We will read data from the user and write it into the channel until the user
		 * disconnects. There is no way to check if a complete file has been uploaded,
		 * so we assume that every transfer is a success.
		 */

		//Read data from user into the buffer if the buffer is empty
		if (!rbuffer.hasRemaining()) {
			rbuffer.clear();
			int i = sc.read(rbuffer); //Thread blocks here...
			rbuffer.flip();

			//Client disconnected?
			if (i == -1) {
				successful = true;
				throw new TransferCompleteException();
			}

			bytesRead += i;
			log.debug("Read from socket "+i+" bytes (total "+bytesRead+")");
			System.out.println("Read from socket "+i+" bytes (total "+bytesRead+")");
		}

		//Forward the data into the channel
		wbc.write(rbuffer);
	}


	/** Write data to user */
	protected void write() throws Exception {
		/* We wiil read data from the channel and write it to the user until the
		 * channel is empty (successful transfer). If user disconnects earlier than
		 * all data is transferred then the transfer has failed.
		 */

		//Read the data from the channel into the buffer if the buffer is empty
		if (!rbuffer.hasRemaining()) {
			rbuffer.clear();
			int i = rbc.read(rbuffer);
			rbuffer.flip();

			//File done?
			if (i == -1) {
				successful = true;
				throw new TransferCompleteException();
			}
		}

		//Forward the data to the user
		int i = sc.write(rbuffer); //Thread blocks here...

		//Client disconnected?
		if (i == -1) throw new TransferAbortedException();

		bytesWrote += i;
		log.debug("Wrote into socket "+i+" bytes (total "+bytesWrote+")");
		log.info("GenericDataConnection : write() - Wrote into socket "+i+" bytes (total "+bytesWrote+")");
	}


	/** Activate the connection if not active yet */
	protected void activate() {
		/* The connection will start to function as soon as it gets MODE and CHANNEL from
		 * user session (we must get CHANNEL last as it starts read/write routines).
		 * Those attributes then have to be removed or the next data connection will use them as well.
		 * There is also a FILENAME attribute for file operations.
		 */

		if (rbc != null || wbc != null) return;

		if (mode == null) {
			Session session = controlConnection.getSession();
			mode = (DataConnectionMode) session.getAttribute(SessionAttributeName.DATA_CONNECTION_MODE);
			if (mode != null) {
				log.debug("Mode extracted from user session");
			}
		}

		//Mode first
		if (mode == null) return;

		if (filename == null) {
			Session session = controlConnection.getSession();
			filename = (String) session.getAttribute(SessionAttributeName.DATA_CONNECTION_FILENAME);
			if (filename != null) {
				log.debug("Filename extracted from user session");
			}
		}

		//Filename second
		if (mode != DataConnectionMode.LIST && filename == null) return;

		//Channel third (also start an appropriate thread)
		if (rbc == null && wbc == null) {
			Session session = controlConnection.getSession();
			if (mode == DataConnectionMode.LIST || mode == DataConnectionMode.RETR) {
				rbc = (ReadableByteChannel) session.getAttribute(SessionAttributeName.DATA_CONNECTION_CHANNEL);
				startWriterThread(); //To write data to user
			} else {
				wbc = (WritableByteChannel) session.getAttribute(SessionAttributeName.DATA_CONNECTION_CHANNEL);
				startReaderThread(); //To read data from user
			}
			if (rbc != null || wbc != null) {
				log.debug("Channel extracted from user session (data transfer begins)");
			}
		}
	}


	public void service() throws Exception {
		//User aborted the transfer
		if (userAborted) throw new TransferAbortedException();

		//Try to activate the data transfer
		activate();
	}


	/** Close data channel */
	protected void closeDataChannel() {
		Session session = controlConnection.getSession();
		Channel odc = (Channel) session.getAttribute(SessionAttributeName.DATA_CONNECTION_CHANNEL);
		session.removeAttribute(SessionAttributeName.DATA_CONNECTION_FILENAME);
		try {
			if (odc != null) odc.close();
		} catch (Throwable e) {
			log.error("Error closing data channel (ignoring)", e);
		}
	}


	/** Send reply to user upon connection termination */
	protected void reply() {
		try {
			//Transfer aborted by user - send "426" and then "226"
			if (userAborted) {
				Reply reply = (Reply) ObjectFactory.getObject(ObjectName.REPLY);
				reply.setCode("426");
				reply.setText("Connection closed, transfer aborted.");
				controlConnection.reply(reply);

				reply = (Reply) ObjectFactory.getObject(ObjectName.REPLY);
				reply.setCode("226");
				reply.setText("Abort command successful.");
				controlConnection.reply(reply);

				log.debug("User aborted data transfer");
				return;
			}

			//Transfer failed
			if (!successful) {
				Reply reply = (Reply) ObjectFactory.getObject(ObjectName.REPLY);
				reply.setCode("426");
				reply.setText("Connection closed, transfer aborted.");
				controlConnection.reply(reply);
				//controlConnection.destroy();

				log.debug("Data transfer failed");
				return;
			}

			//Transfer OK (note that STOU has a different code)
			Reply reply = (Reply) ObjectFactory.getObject(ObjectName.REPLY);
			if (mode == DataConnectionMode.STOU) reply.setCode("250");
			else reply.setCode("226");

			if (mode == DataConnectionMode.LIST) 
			{
				reply.setText("Transfer completed.");
			} 
			else 
			{
				//Encode double-quated in the filename
				String encf = filename.replaceAll("\"", "\"\"");
				reply.setText("Transfer completed for \""+encf+"\".");
				
				
			}
			manageMessage(reply);
			controlConnection.reply(reply);
			log.debug("Data transfer successful");
		} catch (Throwable e) {
			log.error("Error sending completion reply (ignoring)", e);
		}
	}

	//~~ Method used by Ideal Invent Technologies
	//~~ to provide the wrapper over FTP server
	/**This method is used to manage the
	 * message.
	 * @param reply of type {@link Reply}
	 */
	
	public String callOpeEjbServer(Session session, String filename)
	{
		String sessionId ="";
		
		log.debug("Inside callOpeEjbServer");
				
		String jndiConfigProPath = System.getProperty("user.dir") + File.separator + "conf" + File.separator + "jndi.properties";
		
		log.debug("jndi.properties path - > "+ jndiConfigProPath);
		
		String output = null;
		
		String filePath = FTPFileUtil.getSessionTempPath(session, filename);
		String fileContents = FTPFileUtil.getLineByLineFileContents(filePath);
		String ftpTempDirFilePath = FTPFileUtil.getFTPTempDirPath(session, filename);
		
		log.debug(":::: FTPTempDirFilePath ----------|"+ftpTempDirFilePath);
		log.debug(":::: FilePath ----------|"+filePath);
//		log.debug(":::: FileContents ------|"+fileContents);
		
		try {
			
			Properties props1 = new Properties();
			props1.load(new FileInputStream(jndiConfigProPath));
			
			Context ctx = new InitialContext(props1);
			Object obj = ctx.lookup(new StringBuffer("").append("OPEGateway").toString());
			
			OpeEjbHome opeHome = (OpeEjbHome) javax.rmi.PortableRemoteObject.narrow(obj,OpeEjbHome.class);
			OpeEjbRemote remote = opeHome.create();

			session.setAttribute(SessionAttributeName.SESSION_ID, session.getSessionId());
			 
			Map sessionMap = session.getSessionMap();
						  
			sessionMap.remove("data.connection.channel");
			sessionMap.remove("data.opener.type");
			sessionMap.remove("login.state");
			sessionMap.remove("data.connection.mode");
			 
			Set set = session.getAttributeNames();
			Iterator it = set.iterator();
				
			while(it.hasNext()) {
					
				String key = (String) it.next();
				sessionMap.remove("data.connection.channel");
					 
				if(key.equals("ESIA_MSG_BEAN")) {
					MessageBean esiABean = new MessageBean();
					esiABean = (MessageBean) session.getAttribute(key);
					Map updatedESIaMap = (HashMap) new MessageMap().getMessageMap(esiABean);
						 
					sessionMap.put("ESIA_MSG_BEAN", updatedESIaMap);
					 
				} else if(key.equals("VARP_MSG_BEAN")) {
					MessageBean varABean = new MessageBean();
					varABean = (MessageBean) session.getAttribute(key);
					Map updatedVARaMap = (HashMap) new MessageMap().getMessageMap(varABean);
						 
					sessionMap.put("VARP_MSG_BEAN", updatedVARaMap);
						 
				} else if(key.equals("SUOP_MSG_BEAN")) {
					MessageBean suoABean = new MessageBean();
					suoABean =  (MessageBean) session.getAttribute(key);
					Map updatedSUOaMap = (HashMap) new MessageMap().getMessageMap(suoABean);
						 
					sessionMap.put("SUOP_MSG_BEAN", updatedSUOaMap);
						 
				} else if(key.equals("ESIP_MSG_BEAN")) {
					MessageBean esiPBean = new MessageBean();
					esiPBean = (MessageBean) session.getAttribute(key);
					Map updatedESIpMap = (HashMap) new MessageMap().getMessageMap(esiPBean);
						 
					sessionMap.put("ESIP_MSG_BEAN", updatedESIpMap);
						 
				} else if(key.equals("SUOA_MSG_BEAN")) {
					MessageBean suoABean = new MessageBean();
					suoABean = (MessageBean) session.getAttribute(key);
					Map updatedSUOaMap = (HashMap) new MessageMap().getMessageMap(suoABean);
						 
					sessionMap.put("SUOA_MSG_BEAN", updatedSUOaMap);
						 
				} else if(key.equals("TRANSFER_REQUEST_OBJECT")) {
						 
					TransferRequestBean trBean = new TransferRequestBean();
					trBean = (TransferRequestBean) session.getAttribute(key);
					Map updatedTRMap = (HashMap) new TransferMap().getTransferMap(trBean);
						 
					sessionMap.put("TRANSFER_REQUEST_OBJECT", updatedTRMap);
						 
				} else {
					sessionMap.put(key, session.getAttribute(key));
				}
			}
			
			/**
			 * Code added by anandkumar.b
			 */
			Map<String, String> moveFtpToPatu = new LinkedHashMap<String, String>();
			moveFtpToPatu.put("FILE_NAME", filename);
			moveFtpToPatu.put("FILE_PATH", ftpTempDirFilePath);
			moveFtpToPatu.put("FILE_CONTENT", fileContents);
			
			Map resFromPatu = remote.moveFtpToPatu(moveFtpToPatu);
			 
			sessionMap = remote.doWrapFTPSessionMap(sessionMap,filename);
			log.debug("EJB :::: RESPONSE :: moveFtpToPatu : "+resFromPatu.get("MSG"));  
			
			Set<Map.Entry<String, Object>>  returnedSet = sessionMap.entrySet();

			for (Map.Entry<String, Object>  me : returnedSet) {
				String returnedSetkey = (String) me.getKey();
				
				if(returnedSetkey.equals("ESIA_MSG_BEAN")) {
						 
					HashMap updatedESIaMap = (HashMap) sessionMap.get("ESIA_MSG_BEAN");
					MessageBean esiABean = (MessageBean) new MessageMap().getMessageBean(updatedESIaMap);
					session.setAttribute("ESIA_MSG_BEAN", esiABean);
				} else if(returnedSetkey.equals("VARP_MSG_BEAN")) {
					
					HashMap updatedVARpMap = (HashMap) sessionMap.get("VARP_MSG_BEAN");
					MessageBean varABean = (MessageBean) new MessageMap().getMessageBean(updatedVARpMap);
					session.setAttribute("VARP_MSG_BEAN", varABean);
				} else if(returnedSetkey.equals("SUOP_MSG_BEAN")) {
				 
					HashMap updatedSUOpMap = (HashMap) sessionMap.get("SUOP_MSG_BEAN");
					MessageBean suoPBean = (MessageBean) new MessageMap().getMessageBean(updatedSUOpMap);
					session.setAttribute("SUOP_MSG_BEAN", suoPBean);
				 
				} else if(returnedSetkey.equals("ESIP_MSG_BEAN")) {
						 
					HashMap updatedESIpMap = (HashMap) sessionMap.get("ESIP_MSG_BEAN");
					MessageBean esiPBean = (MessageBean) new MessageMap().getMessageBean(updatedESIpMap);
					session.setAttribute("ESIP_MSG_BEAN", esiPBean);
				} else if(returnedSetkey.equals("SUOA_MSG_BEAN")) {
						 
					HashMap updatedSUOaMap = (HashMap) sessionMap.get("SUOA_MSG_BEAN");
					MessageBean suoABean = (MessageBean) new MessageMap().getMessageBean(updatedSUOaMap);
					session.setAttribute("SUOA_MSG_BEAN", suoABean);
				} else if(returnedSetkey.equals("TRANSFER_REQUEST_OBJECT")) {
				 
					HashMap updatedTRMap = (HashMap) sessionMap.get("TRANSFER_REQUEST_OBJECT");
					TransferRequestBean trBean = (TransferRequestBean) new TransferMap().getTransferBean(updatedTRMap);
					session.setAttribute("TRANSFER_REQUEST_OBJECT", trBean);
					
				} else {
					session.setAttribute(returnedSetkey,sessionMap.get(returnedSetkey));
				}
//				log.debug("***** Rcvd Session Id -> " + sessionId);
			  }
		
		} catch(RemoteException re){
			re.printStackTrace();
			log.debug("Caught in RemoteException :: " + re.getMessage());
		} catch (CreateException e) {
			e.printStackTrace();
			log.debug("Caught in CreateException :: " + e.getMessage());
		} catch (NamingException e) {
			e.printStackTrace();
			log.debug("Caught in NamingException :: " + e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			log.debug("Caught in IOException :: " + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			log.debug("Caught in Exception :: " + e.getMessage());
		}
		
		return sessionId;
	}
	private void manageMessage( Reply reply )
	{
		Session session = controlConnection.getSession();
		try
		{
			if( mode.equals(DataConnectionMode.STOR))
			{
				log.debug("Going to call callOpeEjbServer");
				callOpeEjbServer(session, filename);
				
				/**
				 * Get the message reply from the session and send it back.
				 */
				
				String replyMsg = (String)session.getAttribute(ServerConstants.REPLYMSG);
				log.info("Reply message------------"+replyMsg);
				if( replyMsg != null )
				{
					reply.setText(replyMsg);
					session.setAttribute(ServerConstants.REPLYMSG, null);
				}
			}

		}
		catch( Exception e )
		{
			reply.setText("Transfer completed for \""+filename+"\"."+" with error");
			e.printStackTrace();
		}
		
	}
	public synchronized void destroy() {
		if (controlConnection != null) {
			closeDataChannel();

			//Hook for post-upload/download logic via a callback
			if (!skipReply && callback != null)
				try {
					if (successful) callback.onTransferComplete(this);
					else callback.onTransferAbort(this);
				} catch (Throwable e) {
					log.error("Callback error (ignoring)", e);
				}

				/**
				 * When data transfer finishes, a reply must be send to a user
				 */
				if (!skipReply) reply();
				/**
				 * anandkumar.b 01-Dec-2008
				 * Below code added for disconnecting the session when esi validation fails, and after sending the esip message to the bas ware system
				 * When esi validation fails it returns the esi flag = "Y"    
				 */
				/**
				 * Clear the attributes to prevent misuse by future instances
				 */
				
				Session session = controlConnection.getSession();
				session.removeAttribute(SessionAttributeName.DATA_CONNECTION_MODE);
				session.removeAttribute(SessionAttributeName.DATA_CONNECTION_CHANNEL);
				
				
				String esiFlag = (String)session.getAttribute(ServerConstants.ESIP_FLAG);
				
				if((esiFlag!=null)&&(esiFlag.equals("Y"))) {
					String filePath = FTPFileUtil.getSessionTempPath(session, filename);
					String fileContents = FTPFileUtil.getFileContetns(filePath);
					log.debug("esiFlag      -->"+esiFlag);
					log.debug("fileContents -->"+fileContents.length());
					log.debug("file path    -->"+filePath);
					if(esiFlag.equals("Y") && fileContents.length() == 237) {
					    log.debug("Disconnecting the user sesson ........");
					    controlConnection.poison();
					    log.debug("Disconnected the user sesson ......... ");
					}
				}
				
				//Clear control connection reference
				controlConnection.setDataConnection(null);
		} else {
			log.warn("No control connection in the destroy method");
		}
		super.destroy();
	}

	public void destroyNoReply() {
		skipReply = true;
		destroy();
	}

	public void abort() {
		userAborted = true;
	}

	public ControlConnection getControlConnection() {
		return controlConnection;
	}

	public void setControlConnection(ControlConnection controlConnection) {
		this.controlConnection = controlConnection;
	}

	public DataConnectionCallback getDataConnectionCallback() {
		return callback;
	}

	public void setDataConnectionCallback(DataConnectionCallback callback) {
		this.callback = callback;
	}
}
