package com.ope.patu.ejb.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Properties;

import javax.ejb.CreateException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.ope.patu.ejb.server.OpeEjbHome;
import com.ope.patu.ejb.server.OpeEjbRemote;
import com.ope.patu.server.constant.ServerConstants;

public class PATUEjbClass {

	protected static Logger logger = Logger.getLogger(PATUEjbClass.class);
	
	public static OpeEjbRemote getEjbRemoteObject()
	{
		OpeEjbRemote remote = null;
		try {
			
			String jndiConfigProPath = ServerConstants.CONFIG_DIR + File.separator + "jndi.properties";
	
			logger.debug("jndi.properties path - > "+ jndiConfigProPath);
	
			Properties props1 = new Properties();
			props1.load(new FileInputStream(jndiConfigProPath));
			
			Context ctx = new InitialContext(props1);
			Object obj = ctx.lookup(new StringBuffer("").append("OPEGateway").toString());
			
			OpeEjbHome opeHome = (OpeEjbHome) javax.rmi.PortableRemoteObject.narrow(obj,OpeEjbHome.class);
			remote = opeHome.create();
		} catch(RemoteException re){
			re.printStackTrace();
			logger.debug("Caught in RemoteException :: " + re.getMessage());
		} catch (CreateException e) {
			e.printStackTrace();
			logger.debug("Caught in CreateException :: " + e.getMessage());
		} catch (NamingException e) {
			e.printStackTrace();
			logger.debug("Caught in NamingException :: " + e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			logger.debug("Caught in IOException :: " + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("Caught in Exception :: " + e.getMessage());
		}
		return remote;		
	}
	 
}