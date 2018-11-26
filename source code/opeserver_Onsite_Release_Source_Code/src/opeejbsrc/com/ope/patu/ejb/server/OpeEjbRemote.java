package com.ope.patu.ejb.server;

import java.rmi.RemoteException;
import java.util.Map;

import javax.ejb.EJBObject;
import com.coldcore.coloradoftp.session.Session;

public interface OpeEjbRemote extends EJBObject
{
	/* Following three methods are for OPE-EJB debug purpose*/
	public String getAdvice() throws  RemoteException;

	public String getFTPSession(Session session) throws  RemoteException;
	
	public String getMyFTPMap(Map map) throws  RemoteException;
	
	/* This method is used to Wrap FTP session with PATU logic */
	public Map doWrapFTPSessionMap(Map sessionMap, String fileName) throws  RemoteException;
	
	/* This method is used to move FTP file to PATU */
	public Map moveFtpToPatu(Map ftpToPatu) throws  RemoteException;
	
	/* This method is used to get Retrievable File */
	public Map getRetrievableFile(Map retrievableFileData) throws  RemoteException;	
	
	/* This method is used to update Audit Log */
	public void updateAuditLog(Map auditLogMap) throws  RemoteException;	
	
	
}
