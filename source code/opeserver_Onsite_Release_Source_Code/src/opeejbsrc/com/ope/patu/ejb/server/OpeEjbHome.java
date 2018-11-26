package com.ope.patu.ejb.server;

import java.rmi.RemoteException;
import javax.ejb.CreateException;
import javax.ejb.EJBHome;



public interface OpeEjbHome extends EJBHome
{
	public OpeEjbRemote create() throws CreateException, RemoteException;
}
