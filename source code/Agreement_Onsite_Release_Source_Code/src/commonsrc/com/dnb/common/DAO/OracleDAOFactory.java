/********************************************************************************************
 * Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                        *
 * IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.       *
 * The Program or any portion thereof may not be reproduced in any form whatsoever except    *
 * as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd. *
 *																							 *
 * File Name                   : OracleDAOFactory.java                                       *
 * Author                      : Chenchi Reddy                                               *
 * Creation Date               : 24-Jun-2008                                                 *
 * Description                 : This file serves as Oracle DAO Class. It creates            *
 *								connection string. It returns DAO for all the entities       *
 *                               for PM.                                                     *
 * Modification History        :                                                             *
 *																						     *
 *     Version No.               Date               Brief description of change              *
 *  ---------------------------------------------------------------------------------------  *
 *                       |                  |											     *
 *                       |                  |											     *
 *  ---------------------------------------------------------------------------------------  *
 *********************************************************************************************/

/**
 * Create or import Packages
 */
package com.dnb.common.DAO;



import org.apache.log4j.Logger;


/**
 * Oracle concrete DAO Factory implementation
 */
public class OracleDAOFactory extends DAODNBCommon {
	static Logger logger = Logger.getLogger(OracleDAOFactory.class);


	public LoginDAO getLoginDAO() {
		/**
		 * OracleLoginDAO implements LoginDAO
		 */
		return new OracleLoginDAO();
	}	
}