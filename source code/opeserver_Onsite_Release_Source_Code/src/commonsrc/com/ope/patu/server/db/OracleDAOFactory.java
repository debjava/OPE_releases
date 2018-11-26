/*********************************************************************************************
 * Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                        *
 * IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.       *
 * The Program or any portion thereof may not be reproduced in any form whatsoever except    *
 * as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd. *
 *																							 *
 * File Name                   : OracleDAOFactory.java                                 	 	 *
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
 * This is a factory class.
 * @author Debadatta Mishra
 *
 */
public class OracleDAOFactory extends AbstractDAOFactory 
{
	/**
	 * String type variable for datbase config file.
	 */
	private String dbConfigFileName = null; 

	/**
	 * Default constructor
	 */
	public OracleDAOFactory() 
	{
		super();
	}
	
	/**Constructor for database config file.
	 * @param dbConfigFileName of type String indicating the
	 * database config file name.
	 */
	public OracleDAOFactory( String dbConfigFileName ) 
	{
		super();
		this.dbConfigFileName = dbConfigFileName;
	}
	
	/* (non-Javadoc)
	 * @see com.ope.patu.server.db.AbstractDAOFactory#getServerDAO()
	 */
	public ServerDAO getServerDAO()
	{
		this.dbConfigFileName = "db.properties";
		return new OracleServerDAOImpl( dbConfigFileName );
	}

}
