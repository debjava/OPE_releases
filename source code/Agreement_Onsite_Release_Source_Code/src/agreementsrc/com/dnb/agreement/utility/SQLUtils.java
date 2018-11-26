
/********************************************************************************************
 * Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                        *
 * IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.       *
 * The Program or any portion thereof may not be reproduced in any form whatsoever except    *
 * as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd. *
 *																							 *
 * File Name                   : SQLUtils.java                                            	 *
 * Author                      : Manjunath N G                                               *
 * Creation Date               : 10-Jul-2007                                                 *
 * Description                 : Helper class for SQL related operations					 *
 * Modification History        :                                                             *
 *																						     *
 *   Version No.  Edited By         Date               Brief description of change           *
 *  ---------------------------------------------------------------------------------------  *
 *   	       | 				    | 			    |								   		 *
 *             |                    |				| 								         *
 *  ---------------------------------------------------------------------------------------  *
 *********************************************************************************************/

package com.dnb.agreement.utility;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.sql.DATE;
import oracle.sql.TIMESTAMP;

import com.dnb.common.commons.ComponentCache;
import com.dnb.common.commons.CommonConstants;
import com.dnb.common.database.DataSourceAccessException;
import com.dnb.common.database.DatabaseAccess;
import com.dnb.agreement.exception.IErrorCodes;
import com.dnb.agreement.exception.AgreementBusinessException;

/**
 * SQL Helper class -- meant to eliminate a considerable amount of redundancy.
 * 
 */
public final class SQLUtils {
	/** Hide this thing. */
	private SQLUtils() {
	}

	/** Constant String representing the apostrophe character */
	private static final String APOSTROPHE = "'";

	/**
	 * Constant String representing the appropriate escape pattern for
	 * apostrophes in SQL LIKE searches
	 */
	private static final String ESCAPED_APOSTROPHE = "''";

	/**
	 * Constant String[] that contains the LIKE search operation wildcard
	 * characters - '%' and '_'
	 */
	private static final String[] LIKE_WILDCARD_ESCAPE_CHARS = { "%", "_" };

	/**
	 * Constant String that represents the character '|' that will be used by
	 * the {@link #escapeWildCardChars(String, StringBuffer)} method to escape
	 * LIKE search operation wildcard characters as well as being incorporated
	 * into the {@link #ESCAPE_TEXT} constant
	 */
	public static final String ESCAPE_CHARACTER = "|";

	/**
	 * Constant String that contains the SQL <br>
	 * <br>
	 * 
	 * <code>ESCAPE '{@link #ESCAPE_CHARACTER}'</code> <br>
	 * <br>
	 * 
	 * which is used to inform the SQL engine that a LIKE operation contains
	 * escaped wildcard characters. This incorporates the
	 * {@link #ESCAPE_CHARACTER} constant.
	 * 
	 * @see #escapeWildCardChars(String, StringBuffer)
	 * @see #ESCAPE_CHARACTER
	 */
	public static final String ESCAPE_TEXT = " ESCAPE '" + ESCAPE_CHARACTER
			+ "'";

  private static Map moduleQueriesMap = new HashMap();
	/**
	 * Get a Connection from the specified data source
	 * 
	 * @param dataSourceName
	 *            Name of the data source
	 * @return A Connection
	 */
	public static Connection getConnection(String dataSourceName)
			throws DataSourceAccessException {
		DatabaseAccess access = (DatabaseAccess) ComponentCache
				.getComponent(dataSourceName);
		if (access == null)
			throw new RuntimeException("Unknown data source " + dataSourceName);

		return access.checkoutConnection();
	}

	/**
	 * Get a connection using the default datasource name in the PMConstants
	 * listing
	 * 
	 * @return db connection
	 * @throws DataSourceAccessException
	 */
	public static Connection getDefaultConnection()
			throws DataSourceAccessException {
		DatabaseAccess access = (DatabaseAccess) ComponentCache
				.getComponent(CommonConstants.DNBDatabase);
		if (access == null)
			throw new RuntimeException("Unknown data source "+ CommonConstants.DNBDatabase);
		return access.checkoutConnection();
	}

	/**
	 * The form for a SQL LIKE operation is:<br>
	 * <br>
	 * 
	 * <code>WHERE SEARCH_COLUMN LIKE ('%FOO%')</code> - where FOO represents
	 * the characters to be searched for.<br>
	 * <br>
	 * 
	 * However if instead of search text <code>FOO</code> our search text
	 * looked like:<br>
	 * <br>
	 * 
	 * <code>WHERE SEARCH_COLUMN LIKE ('%FO'O%')</code><br>
	 * <br>
	 * 
	 * the database parser would reject the SQL due to there being an
	 * unterminated string constant. It is therefore necessary to escape any
	 * apostrophes that are part of the search text by prepending a second
	 * apostrophe.<br>
	 * <br>
	 * 
	 * Using this method <code>FO'O</code> would be transformed into
	 * <code>FO''O</code>
	 * 
	 * @param sql
	 *            String representing SQL text that may require apostrophes to
	 *            be escaped
	 * @return String <code>sql</code> parameter with all apostrophes inside
	 *         it escaped or <code>null</code> if the <code>sql</code>
	 *         parameter was empty
	 */
	public static String escapeApostrophes(String sql) {
		if (!(StringUtils.isStringEmpty(sql)))
			return sql.replaceAll(APOSTROPHE, ESCAPED_APOSTROPHE);
		else
			return null;
	}

	/**
	 * The SQL LIKE operation provides for the use of two different wildcards -
	 * '%' and '_' - for matching any number of characters and any single
	 * character respectively.<br>
	 * <br>
	 * 
	 * In order to search for text that contains these wildcard characters it is
	 * necessary to escape them. <br>
	 * <br>
	 * 
	 * For example - the text:<br>
	 * <br>
	 * 
	 * <code>100% American_Made!</code> <br>
	 * <br>
	 * 
	 * would need to be escaped in the following way - given an '|' escape
	 * character :<br>
	 * <br>
	 * 
	 * <code>%100|% American|_Made!</code> <br>
	 * <br>
	 * 
	 * This method will apply the {@link #ESCAPE_CHARACTER} to any occurence of
	 * either wildcard character found in the <code>text</code> parameter and
	 * put that escaped version of the text into the <code>escapedText</code>
	 * StringBuffer argument.<br>
	 * <br>
	 * 
	 * It will return a <code>boolean</code> to the calling method indicating
	 * if a match was found. If true, then the calling method will need to add
	 * {@link #ESCAPE_TEXT} to the query for each LIKE operation that contains
	 * text with escaped wildcard characters.<br>
	 * <br>
	 * 
	 * To continue with the example above, incorporating that escaped text into
	 * a LIKE operation would look like: <br>
	 * <br>
	 * 
	 * <code>
	 * WHERE search_col LIKE ('%100|% American|_Made!%') ESCAPE '|'
	 * </code><br>
	 * <br>
	 * 
	 * where <code>ESCAPE '|'</code> is the constant {@link #ESCAPE_TEXT} <br>
	 * <br>
	 * 
	 * <b>NOTE: </b> This method will apply the escape logic to any occurence of
	 * the {@link #ESCAPE_CHARACTER} prior to attempting to escape the wildcard
	 * characters
	 * 
	 * 
	 * @param text
	 *            String containing text that requires all LIKE wildcard
	 *            characters be escaped
	 * @param escapedText
	 *            empty initialized StringBuffer to hold the escaped text
	 * @return boolean Return true if at least one wildcard character was found
	 *         in the text parameter. This should be used by the calling method
	 *         to determine if it is necessary to add {@link #ESCAPE_TEXT} to
	 *         the query.
	 * @see #ESCAPE_CHARACTER
	 * @see #ESCAPE_TEXT
	 */
	public static boolean escapeWildCardChars(String text,
			StringBuffer escapedText) {
		int escapeCharIndex = 0;
		boolean escapeMatch = false;
		String currChar;

		// must escape any ESCAPE_CHARACTERs from the text prior to
		// escaping other characters.
		StringBuffer newText = new StringBuffer();
		for (int i = 0, len = text.length(); i < len; i++) {
			char tmpChar = text.charAt(i);
			if (!("" + tmpChar).equals(ESCAPE_CHARACTER)) {
				newText.append(tmpChar);
			} else {
				newText.append(ESCAPE_CHARACTER + ESCAPE_CHARACTER);
				escapeMatch = true;
			}
		}

		text = newText.toString();

		for (int j = 0; j < LIKE_WILDCARD_ESCAPE_CHARS.length; j++) {
			currChar = LIKE_WILDCARD_ESCAPE_CHARS[j];
			escapeCharIndex = text.indexOf(currChar);
			if (escapeCharIndex != -1) {
				text = text.replaceAll(currChar, ESCAPE_CHARACTER + currChar);
				escapeMatch = true;
			}
		}
		escapedText.append(text);
		return escapeMatch;
	}

	/**
	 * Closes Connection, swallows any exceptions that could occur.
	 * 
	 * @param conn
	 *            Connectoin reference.
	 */
	public static void ensureClosed(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
				conn = null;
			} catch (SQLException e) {
				// Swallow.
			}
		}
	}

	/**
	 * Closes ResultSet, swallows any exceptions that could occur.
	 * 
	 * @param rslt
	 *            ResultSet to close.
	 */
	public static void ensureClosed(ResultSet rslt) {
		if (rslt != null) {
			try {
				rslt.close();
				rslt = null;
			} catch (SQLException e) {
				// Swallow.
			}
		}
	}

	/**
	 * Closes Statement, swallows any exceptions that could occur.
	 * 
	 * @param stmt
	 *            Statement to close.
	 */
	public static void ensureClosed(Statement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
				stmt = null;
			} catch (SQLException e) {
				// SWALLOW.
			}
		}
	}

	/**
	 * Method closes the Native connectionm, statement and nulls the reference
	 * connection, It also swallows any exceptions that could occur.
	 * 
	 * @param stmt
	 *            Statement to close.
	 * @param conn
	 *            Statement to close.
	 * @param oraConRef
	 *            Statement to close.
	 */
	public static void ensureClosed(Statement stmt, Connection conn,
			OracleConnection oraConRef) {
		try {
			ensureClosed(stmt);
			ensureClosed(conn);
			
//			if (stmt != null) {
//				stmt.close();
//			}
//			if (!conn.isClosed()) {
//				conn.close();
//			}
			if (oraConRef != null)
				oraConRef = null;
		} 
		catch (Exception e) {
			// SWALLOW.
		}
	}

	/**
	 * Parses a ResultSet into HashMap containing Lists of HashMaps -- each list
	 * effectively "grouped by" columnKey.
	 * <p>
	 * Given a ResultSet that contains the following content:
	 * 
	 * <pre>
	 *           COL_1 | COL_2 | COL_3 | COL_4
	 *           foo     bar     wing    nut
	 *           bar     bar     wing    nut
	 *           bar     bar     wing    nut
	 * </pre>
	 * 
	 * This method, when invoked with columnKey = "COL_1" would generate a
	 * HashMap with two entries:
	 * 
	 * <pre>
	 *            key | value
	 *            foo   java.util.List
	 *                    java.util.Map
	 *                      COL_1 | foo
	 *                      COL_2 | bar
	 *                      COL_3 | wing
	 *                      COL_4 | nut
	 *          
	 *            bar   java.util.List
	 *                    java.util.Map
	 *                       COL_1 | bar
	 *                       COL_2 | bar
	 *                       COL_3 | wing
	 *                       COL_4 | nut
	 *                    java.util.Map
	 *                       COL_1 | bar
	 *                       COL_2 | bar
	 *                       COL_3 | wing
	 *                       COL_4 | nut
	 * </pre>
	 * 
	 * Note that the null reference will be returned in the event that the
	 * ResultSet argument is null. <p/>
	 * 
	 * @param rs
	 *            ResultSet to pull data from.
	 * @param columnKey
	 *            Column to use as HashMap key.
	 * 
	 * @return HashMap containing list of HashMaps with key = columNkey.
	 * @throws java.sql.SQLException
	 *             SQLException thrown by ResultSet functions.
	 */
	public static Map toMap(ResultSet rs, String columnKey) throws SQLException {
		Map map = null;

		if (rs != null) {
			map = new LinkedHashMap();
			List list = null;

			ResultSetMetaData md = rs.getMetaData();
			int columnCount = md.getColumnCount();

			while (rs.next()) {
				Object key = rs.getObject(columnKey);
				if (!map.containsKey(key)) {
					map.put(key, list = new ArrayList());
				} else {
					list = (List) map.get(key);
				}

				Map row = new LinkedHashMap();
				for (int i = 1; i <= columnCount; i++) {
					row.put(md.getColumnName(i), rs.getObject(i));
				}

				list.add(row);
			}
		}

		return map;
	}

	/**
	 * Marshall ResultSet data in List of HashMaps where key represents column
	 * name and the value represents -- ummm...the value. <p/> Note that this
	 * method returns thte null reference when the ResultSet argument is null.
	 * 
	 * @param rs
	 *            ResultSet to pull data from.
	 * 
	 * @return List populated with HashMap instances representing rows.
	 * @throws java.sql.SQLException
	 *             Allows caller to take appropriate action.
	 */
	public static List toList(ResultSet rs) throws SQLException {
		List rows = null;
		if (rs != null) {
			rows = new ArrayList();

			ResultSetMetaData dbm = rs.getMetaData();
			int resultSetColumns = dbm.getColumnCount();

			while (rs.next()) {
				Map map = new HashMap(resultSetColumns, 1.0f);

				for (int i = 1; i <= resultSetColumns; i++) {
					map.put(dbm.getColumnName(i), rs.getObject(i));
				}

				rows.add(map);
			}
		}

		return rows;
	}

	/**
	 * Append " AND " to the clause if there is already some data there.
	 * 
	 * @param clause
	 *            Where to append
	 */
	public static final void and(StringBuffer clause) {
		if (clause.length() > 0) {
			clause.append(" AND ");
		}
	}

	/**
	 * Closes Connection, Statement and removes Oracle Reference
	 * 
	 */

	public static void ensureClosedConnection(Connection con,
			OracleCallableStatement ocs, OracleConnection oraConRef) {
		SQLUtils.ensureClosed(ocs); // closing statement
		SQLUtils.ensureClosed(con); // closing connection		
		oraConRef = null; // removing reference
	}

	/**
	 * Converts a java.sql.DATE object into java.util.Date object
	 * 
	 * @param date
	 *            SQL Date object
	 * @return Util Date object
	 */
	public static Date convertDateFormat(DATE date) {
		DATE DAT = ((DATE) date);
		Date jsd = DAT.dateValue();
		return jsd;
	}

	/**
	 * Converts a Oracle TIMESTAMP to the Util Timestamp
	 * 
	 * @param date
	 *            Oracle TIMESTAMP object
	 * @return Util TimeStamp object
	 * @throws PMBusinessException
	 *             In case of date conversion error
	 */
	public static Timestamp convertTimestampFormat(TIMESTAMP date)
			throws AgreementBusinessException {
		try {
			TIMESTAMP DAT = ((TIMESTAMP) date);
			Timestamp jsd = DAT.timestampValue();
			return jsd;
		} catch (SQLException t) {
			throw new AgreementBusinessException(IErrorCodes.DATE_CONVERSION_ERROR,
					"Failed to convert timestamp object", t);
		}
	}
  private static Properties loadModule(String module) {
    final String locationPrefix = "databaseproperties/databasekey";
    final String locationSuffix = ".properties";
    Properties props = new Properties();
    String moduleLocation = locationPrefix + ((module != null) ? "_" +  module + locationSuffix : locationSuffix);
    InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(moduleLocation);
    if ( is != null ) {
      try {
        props.load(is);
        if ( !props.isEmpty() ) {
          // cache for later
          moduleQueriesMap.put(module, props);
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return props;
  }
  public static String getQuery(String module, String queryId) {
    Properties props = (Properties) moduleQueriesMap.get(module);
    if ( props == null ) {
      props = loadModule(module);
    }
    return props.getProperty(queryId);
  }
  public static String getQuery(String queryId) {
    return getQuery(null, queryId);
  }
}
