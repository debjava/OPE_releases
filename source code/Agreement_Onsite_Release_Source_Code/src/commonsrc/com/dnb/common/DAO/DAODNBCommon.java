
/********************************************************************************************
* Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                        *
* IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.       *
* The Program or any portion thereof may not be reproduced in any form whatsoever except    *
* as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd. *
*																							*
* File Name                   : DAOStatement.java                                           *
* Author                      : Chenchi Reddy                                               *
* Creation Date               : 24-Jun-2008                                                 *
* Description                 : This file serves as the main DAO class. It creates          *
*								connection the string.It defines to which database          *
*								Statement connects.                                         *
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

import com.dnb.common.DAO.LoginDAO;;

 /** 
  * Abstract class DAO Factory for ProcessMate
  */

public abstract class DAODNBCommon 
{

 /** 
  * List of DAO types supported by this factory
  */

  public static final int ORACLE = 1;
  
  public abstract LoginDAO getLoginDAO();  
  public static DAODNBCommon getDAODNB(int DNBFactory) 
  {  
    switch (DNBFactory) 
	{
      case 1    :         
					      /** 
					       * Oracle Factory
						   */
				          return new OracleDAOFactory();      
      default   : 
				          return null;
    }
  }
}