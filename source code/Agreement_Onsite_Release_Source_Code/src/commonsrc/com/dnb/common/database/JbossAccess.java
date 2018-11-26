/********************************************************************************************
 * Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                       *
 * IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.      *
 * The Program or any portion thereof may not be reproduced in any form whatsoever except   *
 * as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd.*
 *																							*
 * File Name                   : JBossAccess.java                                           *
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
import java.util.Properties;

import org.apache.log4j.Logger;

import com.dnb.common.database.DataSourceAccess;
import com.dnb.common.database.DataSourceAccessException;
import com.dnb.common.database.DatabaseAccess;
import com.dnb.common.database.JbossAccess;
import com.dnb.common.commons.Initializable;
import com.dnb.common.commons.InitializationException;
import com.dnb.common.exception.IErrorCodes;
import com.dnb.common.utility.SQLUtils;

/**
 * This class honors the contract established by the DatabaseAccess interface,
 * but is a simple Proxy for the DataSourceAccess class for connection
 * retrieval. This allows components that wish to use a DataSource for
 * retrieving connections to be initialized, cached and retrieved as
 * DatabaseAccess components via ComponentCache
 */
public class JbossAccess implements DatabaseAccess, Initializable {

	/** Name of the DataSource that this component represents */
	private String dsName;
	private String userName;
	private String password;

	/** Logger instance */
	private static Logger log = Logger.getLogger(JbossAccess.class);

	/**
	 * Initialize this component
	 * 
	 * @param props
	 *            Properties file containing key/value pairs needed to
	 *            initialize this component
	 */
	public void initialize(Properties props) throws InitializationException {
		dsName = props.getProperty(DatabaseAccess.DATA_SOURCE_NAME);
		userName = props.getProperty(DatabaseAccess.AUTH_USER_NAME);
		password = props.getProperty(DatabaseAccess.AUTH_PASSWORD);
		Connection conn = null;
		try {
			conn = checkoutConnection();
		} catch (DataSourceAccessException e) {
			e.printStackTrace();
			throw new InitializationException(IErrorCodes.INITIALIZATION_ERROR,
					"Could not checkout connection");
		}
		checkinConnection(conn);
	}

	/**
	 * Retrieve a Connection from the DataSource that this component was
	 * configured to use
	 * 
	 * @return {@link Connection}
	 */
	public Connection checkoutConnection() throws DataSourceAccessException{
		return DataSourceAccess.getConnection(dsName, userName, password);
	}

	/**
	 * Close the provided connection
	 * 
	 * @param conn
	 *            {@link Connection}
	 */
	public void checkinConnection(Connection conn) {
		SQLUtils.ensureClosed(conn);
	}
}
