/*********************************************************************************************
 * Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                        *
 * IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.       *
 * The Program or any portion thereof may not be reproduced in any form whatsoever except    *
 * as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd. *
 *																							 *
 * File Name                   : DatabaseUtil.java                                 	 	     *
 * Author                      : Debadatta Mishra                                            *
 * Creation Date               : July 18, 2008                                               *
 * Modification History        :                											 *
 *																						     *
 *     Version No.               Date               Brief description of change              *
 *  ---------------------------------------------------------------------------------------  *
 *                       |      			|				 								 *
 *                       |                  |											 	 *
 *  ---------------------------------------------------------------------------------------  *
 *********************************************************************************************/
package com.ope.patu.server.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.sql.PooledConnection;

import org.apache.log4j.Logger;

import com.ope.patu.server.constant.ServerConstants;
import oracle.jdbc.pool.OracleConnectionPoolDataSource;

/**This class is a utility class for database specific
 * operation. This is a singleton class for database operation.
 * @author Debadatta Mishra
 *
 */
public class DatabaseUtil 
{
	/**
	 * Object of type {@link Properties} for database configuration
	 */
	private static Properties dbProp = new Properties();
	/**
	 * String object for database config file.
	 */
	private static String configFileName = null;
	protected static Logger logger = Logger.getLogger(DatabaseUtil.class);
	
	/**
	 * Constructor
	 */
	private DatabaseUtil()
	{
		super();
		try
		{
			InputStream inputStream = new FileInputStream(
					ServerConstants.CONFIG_DIR + File.separator
							+ configFileName);
			dbProp.load(inputStream);
		}
		catch( FileNotFoundException fnfe )
		{
			System.out.println("Database config file not found in the location");
			fnfe.printStackTrace();
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}
	
	/**This is an auxilliary for class for instance holder.
	 * This class is an implementation for singleton design
	 * of the class {@link DatabaseUtil}.
	 * @author Debadatta Mishra
	 *
	 */
	private static class DatabaseUtilHolder
	{
		private static DatabaseUtil dbUtil = new DatabaseUtil( );
	}
	
	/**This method is used to obtain the instance of the
	 * class {@link DatabaseUtil}
	 * @param dbConfigFileName of type String indicating the
	 * database config file name.
	 * @return object of type {@link DatabaseUtil}
	 */
	public static DatabaseUtil getInstance( String dbConfigFileName )
	{
		configFileName = dbConfigFileName;
		return DatabaseUtilHolder.dbUtil;
	}
	
	/**This method is used to obtain the {@link Connection}
	 * @return a {@link Connection}
	 * @throws Exception
	 */
	public Connection getPooledDbConnection() throws Exception 
	{
		OracleConnectionPoolDataSource ocpds = new OracleConnectionPoolDataSource();
		// Set connection parameters
		String dbURL = new StringBuffer("jdbc:oracle:thin:@").
		append(dbProp.getProperty("DATABASE_SERVER_IP"))
		.append(":").append(dbProp.getProperty("DATABASE_SERVER_PORT")).
		append(":").append(dbProp.getProperty("DATABASE_SID"))
		.toString();
		
		ocpds.setURL(dbURL);
		ocpds.setUser(dbProp.getProperty("USERNAME"));
		ocpds.setPassword(dbProp.getProperty("PASSWORD") );
		// Create a pooled connection
		PooledConnection pc = ocpds.getPooledConnection();
		// Get a Logical connection
		Connection conn = pc.getConnection();
		return conn;
	}
	
	/**
	 * Obtain a pooled DB Connection for the specified database
	 * 
	 * @param userName
	 *            User name of the database
	 * @param password
	 *            Password of the database
	 * @param dbURL
	 *            DB URL of the database which contains SID
	 * @return Connection object of the database
	 * @throws Exception
	 */
	public static Connection getPooledDbConnection(String userName,
			String password, String dbURL) throws Exception {
		OracleConnectionPoolDataSource ocpds = new OracleConnectionPoolDataSource();
		// Set connection parameters
		ocpds.setURL(dbURL);
		ocpds.setUser(userName);
		ocpds.setPassword(password);
		// Create a pooled connection
		PooledConnection pc = ocpds.getPooledConnection();
		// Get a Logical connection
		Connection conn = pc.getConnection();
		return conn;
	}
	
	/**
	 * Function to close result set
	 * 
	 * @param resultSet
	 *            Result set object to be closed
	 * @return boolean indicating the success/failure of the operation
	 */
	public static boolean closeResultSet(ResultSet resultSet) {
		boolean checkFlag = false;
		try {
			if (resultSet != null)
				resultSet.close();
		} catch( SQLException se )
		{
			se.printStackTrace();
			logger.error(se);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			logger.error(e);
		}
		return checkFlag;
	}

	/**
	 * Function to close statement
	 * 
	 * @param stmt
	 *            Statement object to be closed
	 * @return boolean indicating the success/failure of the operation
	 */
	public static boolean closeStatement(Statement stmt) {
		boolean checkFlag = false;
		try {
			if (stmt != null)
				stmt.close();
		} 
		catch( SQLException se )
		{
			se.printStackTrace();
			logger.error(se);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			logger.error(e);
		}
		return checkFlag;
	}

	/**
	 * Function to close prepared statement
	 * 
	 * @param pstmt
	 *            Prepared statement to be closed
	 * @return boolean indicating the success/failure of the operation
	 */
	public static boolean closePreparedStatement(PreparedStatement pstmt) {
		boolean checkFlag = false;
		try
		{
			if (pstmt != null)
				pstmt.close();
		} 
		catch( SQLException se )
		{
			se.printStackTrace();
			logger.error(se);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			logger.error(e);
		}
		return checkFlag;
	}



}
