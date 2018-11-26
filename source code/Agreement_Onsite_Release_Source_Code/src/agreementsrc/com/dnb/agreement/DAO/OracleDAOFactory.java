
/********************************************************************************************
 * Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                        *
 * IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.       *
 * The Program or any portion thereof may not be reproduced in any form whatsoever except    *
 * as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd. *
 *																							 *
 * File Name                   : OracleDAOFactory.java                                       *
 * Author                      : Manjunath N G                                               *
 * Creation Date               : 13-August-2008                                                 *
 * Description                 : This file serves as Oracle DAO Class. It creates            *
 *								connection string. It returns DAO for all the entities       *
 *                               for OPE.                                                     *
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

package com.dnb.agreement.DAO;

import org.apache.log4j.Logger;

/**
 * Oracle concrete DAO Factory implementation
 */
public class OracleDAOFactory extends DAOOpe {
	static Logger logger = Logger.getLogger(OracleDAOFactory.class);
	
	public AgreementDAO getAgreementDAO()
	{
		/**
		 *  OracleAgreementDAO implements AgreementDAO 
		 *  */
		return new OracleAgreementDAO();
	}

	
	public ServiceSpecificDAO getServiceSpecificDAO() {
		// TODO Auto-generated method stub
		/**
		 * OracleServiceSpecificDAO implements ServiceSpecificDAO
		 */
		return new OracleServiceSpecificDAO();
	}

	public ServiceBureauDAO getServiceBureauDAO() {
		/**
		 * OracleServiceBureauDAO implements ServiceBureauDAO
		 */
		return new OracleServiceBureauDAO();
	}	
}