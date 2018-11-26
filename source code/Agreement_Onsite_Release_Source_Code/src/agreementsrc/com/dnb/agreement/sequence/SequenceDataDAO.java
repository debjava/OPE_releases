
/********************************************************************************************
 * Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                       *
 * IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.      *
 * The Program or any portion thereof may not be reproduced in any form whatsoever except   *
 * as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd.*
 *																							*
 * File Name                   : ReferenceDataDAO.java                         			    *
 * Author                      : Manjunath N G                                              *
 * Creation Date               : 24-July-2008                                               *
 * Description                 : Data access to ReferenceDataManager				        *
 * Modification History        :                                                            *
 *																						    *
 *   Version No.  Edited By         Date               Brief description of change          *
*  ---------------------------------------------------------------------------------------  *
*             |                    |				|                                       *
*  ---------------------------------------------------------------------------------------  *
 ********************************************************************************************/

package com.dnb.agreement.sequence;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.dnb.common.database.DescriptorUtility;
import com.dnb.agreement.utility.SQLUtils;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleTypes;

/**
 * This class provides the Data Access methods for the ReferenceDataManager
 */
public class SequenceDataDAO {
	/** logger for logging operations throughout this class */
	private static Logger logger = Logger.getLogger(SequenceDataDAO.class);

	/**
	 * Method getEntityId - runs a pl/sql fn and returns a HashMap of the
	 * results. It does not throw exceptions. If the data is not accessible, it
	 * simply moves on
	 * 
	 * @param entity name
	 *            The pl/sql fn that will fetch the requisite data
	 * @param conn -
	 *            use this connection to fetch the data
	 * @return HashMap - containing the rows - the ID is the key and the name is
	 *         the value
	 */
	public String getEntityId(String entity, String pack, Connection con) {

/******************************************************************************/
		logger.info("SequenceDataDAO"+entity);
		
		OracleConnection oraConRef = null;
		OracleCallableStatement ocs = null;
		String entityId = null;
		int res;
		
		try {
			oraConRef = DescriptorUtility.getOracleConnection(con);
		} catch (SQLException sq) {
			logger.error("Unable to obtain OracleConnection"+sq);
		}	

		try	{
			// prepare the	OracleCallableStatement	
			ocs = (OracleCallableStatement)
				oraConRef.prepareCall("{? = call "+ pack +".fn_create_id(?,?,?,?)}");
			
			// Bind inputs and register outputs		
			ocs.registerOutParameter(1, OracleTypes.NUMERIC);
		 	ocs.setString(2,entity); 	
		 	ocs.registerOutParameter(3, OracleTypes.VARCHAR);
		 	ocs.registerOutParameter(4, OracleTypes.VARCHAR);
	 		ocs.registerOutParameter(5, OracleTypes.VARCHAR);
			
			// Execute the procedure
		    ocs.execute();

			// Log the Result
			res = ocs.getInt(1);
			logger.info("RETURN : " + res );
			
			if(res==0) {
				logger.debug("ERROR CODE : " + ocs.getString(4));
				logger.debug("ERROR DESC : " + ocs.getString(5));			
				oraConRef.rollback();
			}
			
			if(res == 1) {			
				entityId = ocs.getString(3);
				oraConRef.commit();
			} else {
				oraConRef.rollback();	
			}
						
			return entityId;
			
		} catch (SQLException sqlException) {
			logger.error(sqlException.getMessage(), sqlException);			
		} finally{
			SQLUtils.ensureClosed(con);  //closing connection
			SQLUtils.ensureClosed(ocs); // closing statement
			oraConRef=null;             // removing reference
		}
		
		return entityId;
	}
}