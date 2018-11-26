/*********************************************************************************************
 * Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                        *
 * IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.       *
 * The Program or any portion thereof may not be reproduced in any form whatsoever except    *
 * as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd. *
 *																							 *
 * File Name                   : AbstractDAOFactory.java                                 	 *
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

/**
 * @author Debadatta Mishra
 * 
 */
public abstract class AbstractDAOFactory 
{
	/**
	 * int type variable for Oracle
	 */
	public static final int ORACLE = 1;

	/**
	 * @return an object of type {@link ServerDAO}
	 */
	public abstract ServerDAO getServerDAO();

	/**
	 * Method to obtain an object of type {@link AbstractDAOFactory}
	 * 
	 * @param factory
	 *            of type int.
	 * @return
	 */
	public static AbstractDAOFactory getDAOFactory(int factory) 
	{
		switch (factory) {
		case ORACLE:
			return new OracleDAOFactory();
		default:
			return null;
		}
	}
	
	/**This method is used to obtain the object for
	 * database interaction based upon the database
	 * name and the database config file.
	 * @param factory of type int indicating the name
	 * of the database.
	 * @param dbConfigFileName of type String indicating the
	 * database config file
	 * @return an object of type {@link AbstractDAOFactory}
	 */
	public static AbstractDAOFactory getDAOFactory( int factory , String dbConfigFileName )
	{
		switch (factory) {
		case ORACLE:
			return new OracleDAOFactory( dbConfigFileName );
		default:
			return null;
		}
	}

	/**This method is used to obtain the default object
	 * for database configuration.
	 * @return an object of type {@link AbstractDAOFactory}
	 */
	public static AbstractDAOFactory getDefaultDAOFactory() {
		return new OracleDAOFactory();
	}

}
