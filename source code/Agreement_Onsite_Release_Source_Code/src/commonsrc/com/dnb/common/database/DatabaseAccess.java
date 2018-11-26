/********************************************************************************************
 * Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                       *
 * IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.      *
 * The Program or any portion thereof may not be reproduced in any form whatsoever except   *
 * as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd.*
 *																							*
 * File Name                   : Initializable.java                                        	*
 * Author                      : Raja Mohan                                                 *
 * Creation Date               : 01-May-2006                                                *
 * Description                 : Interface for initializing a component						*
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

/**
 * An interface for access to a data resource. This has 2 subclasses -
 * JbossAccess and DbcpAccess. This was changed to act as a bridge between the
 * legacy use of DBCP for connection pooling and the new usage of DataSources
 * through Jboss server.
 */
public interface DatabaseAccess {
	/**
	 * Constant to represent the key that should be used in a .properties file
	 * to store the name of the DataSource that a DatabaseAccess component wants
	 * to use
	 */
	public static final String DATA_SOURCE_NAME = "dsName";
	public static final String AUTH_USER_NAME = "auth.user";
	public static final String AUTH_PASSWORD = "auth.password";
	/**
	 * Get a connection out of the pool
	 * 
	 * @return The connection, null if there was a fatal error, and
	 */
	public Connection checkoutConnection()throws DataSourceAccessException;

	/**
	 * Check a connection back into the pool
	 * 
	 * @param conn
	 *            Connection to be checked in
	 */
	public void checkinConnection(Connection conn);

}
