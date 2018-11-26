/********************************************************************************************
 * Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                        *
 * IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.       *
 * The Program or any portion thereof may not be reproduced in any form whatsoever except    *
 * as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd. *
 *																							*
 * File Name                   : LoginDAO.java                                               *
 * Author                      : Chenchi Reddy                                                    *
 * Creation Date               : 24-Jun-2008                                                 *
 * Description                 : This file serves as the Interface Class, which contains     *
 *                               all the methods which are used for Login.                   *
 * Modification History        :                                                             *
 *																						    *
 *     Version No.               Date               Brief description of change              *
 *  ---------------------------------------------------------------------------------------  *
 *                       |                  |											    *
 *                       |                  |											    *
 *  ---------------------------------------------------------------------------------------  *
 *********************************************************************************************/

/** 
 * Create or import Packages
 */
package com.dnb.common.DAO;

import java.util.List;

import com.dnb.common.DTO.LoginDTO;
import com.dnb.common.exception.CommonBusinessException;
import com.dnb.common.exception.CommonSystemException;

/**
 * Interface that all LoginDAO methods must support
 */
public interface LoginDAO {
	public boolean validateUser(LoginDTO e) throws CommonSystemException,
			CommonBusinessException;

	public List getUserDetails(String userId) throws CommonSystemException,
			CommonBusinessException;

	public boolean logoutUser(String userId) throws CommonSystemException,
			CommonBusinessException;
}
