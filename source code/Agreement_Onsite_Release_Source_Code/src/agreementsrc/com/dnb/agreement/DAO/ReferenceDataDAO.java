/********************************************************************************************
 * Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                       *
 * IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.      *
 * The Program or any portion thereof may not be reproduced in any form whatsoever except   *
 * as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd.*
 *																							*
 * File Name                   : ReferenceDataDAO.java                         			    *
 * Author                      : Manjunath N G                                              *
 * Creation Date               : 10-July-2008                                               *
 * Description                 : Data access to ReferenceDataManager				        *
 * Modification History        :                                                            *
 *																						    *
 *     Version No.               Date               Brief description of change             *
 *  --------------------------------------------------------------------------------------- *
 *                       |                  |											    *
 *                       |                  |											    *
 *  --------------------------------------------------------------------------------------- *
 ********************************************************************************************/
package com.dnb.agreement.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.collections.SequencedHashMap;
import org.apache.log4j.Logger;

import com.dnb.agreement.utility.SQLUtils;

/**
 * This class provides the Data Access methods for the ReferenceDataManager
 */
public class ReferenceDataDAO {
	/** logger for logging operations throughout this class */
	private static Logger logger = Logger.getLogger(ReferenceDataDAO.class);

	/**
	 * Method getReferenceData - runs a query and returns a HashMap of the
	 * results. It does not throw exceptions. If the data is not accessible, it
	 * simply moves on
	 * 
	 * @param sqlQuery
	 *            The query that will fetch the requisite data
	 * @param conn -
	 *            use this connection to fetch the data
	 * @return HashMap - containing the rows - the ID is the key and the name is
	 *         the value
	 */
	public SequencedHashMap getReferenceData(String sqlQuery, Connection conn) {
		PreparedStatement pStmt = null;
		ResultSet resultSet = null;
		// the initial size of the hashmap. This is on the basis that
		// each map will not exceed 100 records
		int initialSize = 100;
		SequencedHashMap dataMap = null;
		try {
			// log the query
			logger.info(sqlQuery);

			pStmt = conn.prepareStatement(sqlQuery);
			resultSet = pStmt.executeQuery();
			// create the hash map with the initial size.
			// reducing resizing optimises the code
			dataMap = new SequencedHashMap(initialSize);
			while (resultSet.next()) {
				// the ID is the Key - the first column will always be the ID
				String key = resultSet.getString(1);
				// the name is the value - the name is always the second column
				String value = resultSet.getString(2);
				// ignore other columns, if any
				dataMap.put(key, value);
			}

			return dataMap;
		} catch (SQLException sqlException) {
			logger.error(sqlException.getMessage(), sqlException);
			return dataMap;
		} finally {
			SQLUtils.ensureClosed(resultSet);
			SQLUtils.ensureClosed(pStmt);
		}
	}
}