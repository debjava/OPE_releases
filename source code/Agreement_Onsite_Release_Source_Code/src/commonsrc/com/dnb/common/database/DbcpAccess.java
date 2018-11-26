/********************************************************************************************
 * Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                       *
 * IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.      *
 * The Program or any portion thereof may not be reproduced in any form whatsoever except   *
 * as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd.*
 *																							*
 * File Name                   : DbcpAccess.java                            				*
 * Author                      : Raja Mohan                                                 *
 * Creation Date               : 01-May-2006                                                *
 * Description                 : Dbcp access for unit test cases					        *
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

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.log4j.Logger;

import com.dnb.common.commons.Initializable;

/**
 * A wrapper class for accessing the database through <A
 * href="http://jakarta.apache.org/commons/dbcp/">DBCP</a> There is an attempt
 * to provide database failover here, but it is contingent upon the use of the
 * checkinConnection method.
 */
public class DbcpAccess implements Initializable, DatabaseAccess {
	/**
	 * Logger for this class
	 */
	private static Logger log = Logger.getLogger(DbcpAccess.class);

	/**
	 * Configured property to specify the amount of time to wait between
	 * database failover attempts
	 */
	private static final String FAILOVER_LIMIT = "failover_time_wait";

	/**
	 * The amount of time to wait between database failover attempts, default: 1
	 * minute
	 */
	private long waitTime = 60000;

	/**
	 * Number of active connections in the pool
	 */
	private static final String DBCP_MAXACTIVE = "dbcp_maxactive";

	/**
	 * What to do when the pool is exhausted:
	 * <UL>
	 * <LI>0: WHEN_EXHAUSTED_FAIL</LI>
	 * <LI>1: WHEN_EXHAUSTED_BLOCK</LI>
	 * <LI>2: WHEN_EXHAUSTED_GROW</LI>
	 * </UL>
	 */
	private static final String DBCP_WHENEXHAUSTED = "dbcp_whenexhausted";

	/**
	 * How long to wait, if DBCP_WHENEXHAUSTED is set to WHEN_EXHAUSTED_BLOCK
	 */
	private static final String DBCP_MAXWAIT = "dbcp_maxwait";

	/**
	 * Number of unused connections allowed in the pool
	 */
	private static final String DBCP_MAXIDLE = "dbcp_maxidle";

	/**
	 * JDBC URL to connect to database
	 */
	private static final String DBCP_JDBCURL = "dbcp_dburl";

	/**
	 * User to connect to database
	 */
	private static final String DBCP_USER = "dbcp_dbuser";

	/**
	 * Password to connect to database
	 */
	private static final String DBCP_PASSWORD = "dbcp_dbpassword";

	/**
	 * Backup URL to connect to database
	 */
	private static final String DBCP_BACKUP_JDBCURL = "dbcp_backup_dburl";

	/**
	 * Backup user to connect to database
	 */
	private static final String DBCP_BACKUP_USER = "dbcp_backup_dbuser";

	/**
	 * Backup password to connect to database
	 */
	private static final String DBCP_BACKUP_PASSWORD = "dbcp_backup_dbpassword";

	/**
	 * Test the connection when checking out from the pool
	 */
	private static final String DBCP_TESTBORROW = "dbcp_test_on_borrow";

	/**
	 * Test the connection when returning it to the pool
	 */
	private static final String DBCP_TESTRETURN = "dbcp_test_on_return";

	/**
	 * Query to validate the connection before checkin
	 */
	private static final String VALID_QUERY = "SELECT 1 from DUAL";

	/**
	 * Store the initialization properties for database failover
	 */
	private Properties initProperties;

	/**
	 * Store the primary jdbc url
	 */
	private String jdbcURL;

	/**
	 * Store the primary jdbc username
	 */
	private String jdbcUser;

	/**
	 * Store the primary jdbc password
	 */
	private String jdbcPassword;

	/**
	 * Store the backup jdbc url
	 */
	private String backupJdbcURL;

	/**
	 * Store the backup jdbc username
	 */
	private String backupJdbcUser;

	/**
	 * Store the backup jdbc password
	 */
	private String backupJdbcPassword;

	/**
	 * Store the last time that the pool was rebuilt.
	 */
	private long poolBuilt;

	/**
	 * Store the PoolingDataSource that the connection pool is based on. This is
	 * used to failover the connection to the backup database.
	 */
	private PoolingDataSource ds;

	/**
	 * Initialize the DatabaseAccess object. This will read the property file,
	 * looking for the DBCP properties and creates a connection pool using the
	 * primary connection information.
	 * 
	 * @param props
	 *            Properties object to use for initialization
	 */
	public void initialize(Properties props) {
		String driverclass = props.getProperty("driverclass");
		try {
			Class.forName(driverclass);
		} catch (Exception e) {
			log.error("Unable to load database driver: " + driverclass, e);
			return;
		}
		jdbcURL = props.getProperty(DBCP_JDBCURL);
		jdbcUser = props.getProperty(DBCP_USER);
		jdbcPassword = props.getProperty(DBCP_PASSWORD);
		backupJdbcURL = props.getProperty(DBCP_BACKUP_JDBCURL, jdbcURL);
		backupJdbcUser = props.getProperty(DBCP_BACKUP_USER, jdbcUser);
		backupJdbcPassword = props.getProperty(DBCP_BACKUP_PASSWORD,
				jdbcPassword);

		log.info("Initialize DB: Primary DB[" + jdbcURL + "], backup DB["
				+ backupJdbcURL + "]");

		if (jdbcURL == null || jdbcUser == null || jdbcPassword == null) {
			log.error("Invalid configuration of DatabaseAccess. "
					+ "Missing jdbcURL, jdbcUser or jdbcPassword");
			return;
		}

		try {
			initProperties = props;
			connectDB(jdbcUser, jdbcPassword, jdbcURL);
			Connection conn = ds.getConnection();
			conn.close(); // This will actually put the connection back in the
			// pool.
		} catch (Exception e) {
			log.error("Unable to initialize database connection", e);
		}
		waitTime = Long.parseLong(initProperties.getProperty(FAILOVER_LIMIT,
				"60000"));
	}

	/**
	 * Connect to a database
	 * 
	 * @param username
	 *            Username to connect with
	 * @param password
	 *            Password to connect with
	 * @param url
	 *            URL to connect to
	 * 
	 * @throws Exception
	 *             Jakarta throws an Exception, so this just allows it to be
	 *             thrown up
	 */
	private void connectDB(String username, String password, String url)
			throws Exception {
		GenericObjectPool connectionPool = new GenericObjectPool(null, Integer
				.parseInt(initProperties.getProperty(DBCP_MAXACTIVE)), Byte
				.parseByte(initProperties.getProperty(DBCP_WHENEXHAUSTED)),
				Long.parseLong(initProperties.getProperty(DBCP_MAXWAIT)),
				Integer.parseInt(initProperties.getProperty(DBCP_MAXIDLE)),
				Boolean.valueOf(initProperties.getProperty(DBCP_TESTBORROW))
						.booleanValue(), Boolean.valueOf(
						initProperties.getProperty(DBCP_TESTRETURN))
						.booleanValue());
		ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(
				url, username, password);

		new PoolableConnectionFactory(connectionFactory, connectionPool, null,
				VALID_QUERY, false, false);
		ds = new PoolingDataSource(connectionPool);
		ds.setAccessToUnderlyingConnectionAllowed(true);
	}

	/**
	 * Get a connection out of the pool
	 * 
	 * @return The connection, null if there was a fatal error, and
	 */
	public Connection checkoutConnection() {
		try {
			return ds.getConnection();
		} catch (Exception e) {
			log.error("Unable to get a connection from pool, rebuilding pool",
					e);
			rebuildPool();
			try {
				return ds.getConnection();
			} catch (Exception sqle) {
				log.error("Still unable to get a connection from pool", sqle);
			}
		}
		return null;
	}

	/**
	 * Check a connection back into the pool
	 * 
	 * @param conn
	 *            Connection to be checked in
	 */
	public void checkinConnection(Connection conn) {
		try {
			if (conn.isClosed()) {
				log.info("Connection closed before checkin");
			} else {
				conn.close();
			}
		} catch (Exception e) {
			log.error("Unable to destroy connection, rebuilding pool", e);
			rebuildPool();
		}
	}

	/**
	 * Failover to the other database connection information
	 */
	private synchronized void rebuildPool() {
		long time = System.currentTimeMillis();
		if (time - poolBuilt <= waitTime) {
			log.debug("Skipping database failover, tried within last "
					+ waitTime + " milliseconds");

			return;
		}
		poolBuilt = time;
		log.info("DATABASE FAILOVER: Switching to " + backupJdbcURL);
		String temp = jdbcURL;
		jdbcURL = backupJdbcURL;
		backupJdbcURL = temp;

		temp = jdbcUser;
		jdbcUser = backupJdbcUser;
		backupJdbcUser = temp;

		temp = jdbcPassword;
		jdbcPassword = backupJdbcPassword;
		backupJdbcPassword = temp;

		try {
			this.connectDB(jdbcUser, jdbcPassword, jdbcURL);
		} catch (Exception e) {
			log.error("Unable to switch database", e);
		}
	}
}
