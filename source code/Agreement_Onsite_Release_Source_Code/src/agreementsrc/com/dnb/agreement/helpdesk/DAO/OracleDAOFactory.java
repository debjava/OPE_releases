
/********************************************************************************************
 * Copyright 2008 IDEAL INVENT Technologies Pvt. Ltd.                                        *
 * IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.       *
 * The Program or any portion thereof may not be reproduced in any form whatsoever except    *
 * as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd. *
 *																							 *
 * File Name                   : OracleDAOFactory.java                                       *
 * Author                      : Manjunath N G                                               *
 * Creation Date               : 06-Nov-2008                                                 *
 * Description                 : This file serves as Oracle DAO Class. It creates            *
 *								connection string. It returns DAO for all the entities       *
 *                               for OPE.                                                    *
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

package com.dnb.agreement.helpdesk.DAO;

import org.apache.log4j.Logger;

/**
 * Oracle concrete DAO Factory implementation
 */
public class OracleDAOFactory extends HelpDeskDAOOpe {
	static Logger logger = Logger.getLogger(OracleDAOFactory.class);
	
	public HelpDeskReportsDAO getHelpDeskReportsDAO() {
		/**
		 * OracleHelpDeskReportsDAO implements HelpDeskReportsDAO
		 */
		return new OracleHelpDeskReportsDAO();
	}	
}