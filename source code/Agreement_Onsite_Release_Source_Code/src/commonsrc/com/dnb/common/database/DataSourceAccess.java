/********************************************************************************************
 * Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                       *
 * IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.      *
 * The Program or any portion thereof may not be reproduced in any form whatsoever except   *
 * as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd.*
 *																							*
 * File Name                   : DataSourceAccess.java                                      *
 * Author                      : Raja Mohan                                                 *
 * Creation Date               : Apr 28, 2006                                               *
 * Description                 :                                                            *
 * Modification History        :                                                            *
 *																						    *
 *     Version No.               Date               Brief description of change             *
 *  --------------------------------------------------------------------------------------- *
 *                       |                  |											    *
 *                       |                  |											    *
 *  --------------------------------------------------------------------------------------- *
 ********************************************************************************************/

package com.dnb.common.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import javax.sql.DataSource;

import com.dnb.common.exception.IErrorCodes;

/**
 * Provide a generic way to retrieve a connection via a data source. Data source
 * will be looked up based on JNDI context key - and references will be stored
 * mapped to those keys to avoid per request context lookups
 */
public class DataSourceAccess {

	/* Private constructor for Singleton implementation */
	private DataSourceAccess() {
	}

	/*
	 * Map to cache references to DataSources retrieved through the
	 * getConnection() method
	 */
	private static Map dataSources = new HashMap();

	/**
	 * Attempt to retrieve a Connection from a specific DataSource. <br>
	 * <br>
	 * <p/> This method will attempt to look up a DataSource via the provided
	 * parameter. Once the reference to the DataSource has been successfully
	 * retrieved - it will be cached by this class to prevent subsequent JNDI
	 * lookups. <br>
	 * <br>
	 * Once the reference to the DataSource has been retrieved this method will
	 * attempt to retrieve a Connection from it. <br>
	 * <br>
	 * Either of the above scenarios can throw Exceptions and these will be
	 * wrapped by a {@link DataSourceAccessException}. This should only be
	 * thrown in the event of something seriously wrong occuring with the
	 * application server environment or the database. <br>
	 * <br>
	 * It is also possible that upon a subsequent call to this method, a
	 * requesting method could pass in the name of a DataSource that this class
	 * already has a reference to. In the event that this method is unable to
	 * retrieve a Connection from that reference, it will undertake another
	 * attempt to look up the reference to that DataSource and retrieve a
	 * Connection. If that were to fail - again a
	 * {@link DataSourceAccessException} would be thrown.
	 * 
	 * @param dsName
	 *            Name of the {@link DataSource} that is to provide a
	 *            {@link Connection}
	 * @return {@link Connection}
	 * @throws DataSourceAccessException
	 *             This is a subclass of {@link RuntimeException} used to denote
	 *             that something seriously wrong has occurred in the attempt to
	 *             retrieve a connection.
	 */
	public static Connection getConnection(String dsName, String userName, String password)throws DataSourceAccessException {
		Connection conn = null;
		DataSource ds = (DataSource) dataSources.get(dsName);
		if (ds == null) {
			// attempt to lookup the DataSource again out of
			// InitialContext and replace it in the dataSources
			// cache
			ds = retrieveDataSource(dsName);
			dataSources.put(dsName, ds);
		}

		if (ds != null) {
			try {
				conn = ds.getConnection(userName, password);
			} catch (SQLException sqlex) {
				System.out.println("Caught exception: " + sqlex );
				System.out.println("First attempt...lets try again");
			}
			if ( conn == null ) {
				try {
					conn = ds.getConnection();
				} catch (SQLException sqle2) {
					System.out.println("Life does not give you second chances my boy !!");
					throw new DataSourceAccessException(
							IErrorCodes.CONNECTION_RETRIEVAL_ERROR,
							"Unable to retreive a connection");
				}
				if ( conn == null ) {
					// if you still cannot get a connection call the electrician !!
					throw new DataSourceAccessException(
							IErrorCodes.CONNECTION_RETRIEVAL_ERROR,
							"Unable to retreive a connection");
				}
			}
		} else {
			throw new DataSourceAccessException(
					IErrorCodes.CONNECTION_RETRIEVAL_ERROR,
					"Unable to locate datasource");
		}
		try {
			conn.setAutoCommit(false);
		} catch (SQLException sql) {
			throw new DataSourceAccessException(new Integer(sql.getErrorCode())
					.toString(), sql.getMessage());
		}
		
		return conn;
	}

	/**
	 * Helper method to retrieve a DataSource from InitialContext using the
	 * provided argument as the name that the DataSource should have been
	 * registered under.
	 * 
	 * @param dsName
	 *            Name of the {@link DataSource} that is to provide a
	 *            {@link Connection}
	 * @return {@link DataSource} or null if no reference to a DataSource can be
	 *         retrieved from InitialContext via the provided dsName
	 */
	private static DataSource retrieveDataSource(String dsName) throws DataSourceAccessException{
		DataSource ds = null;
		try {
			InitialContext ctxt = new InitialContext();			
			Object o = ctxt.lookup(dsName);			
			ds = (DataSource) PortableRemoteObject.narrow(o, DataSource.class);			
		} catch (NamingException ne) {
			ne.printStackTrace();
			throw new DataSourceAccessException(
					IErrorCodes.NAMING_LOOKUP_ERROR,
					"Exception in looking up datasource");
		}
		return ds;
	}

}
