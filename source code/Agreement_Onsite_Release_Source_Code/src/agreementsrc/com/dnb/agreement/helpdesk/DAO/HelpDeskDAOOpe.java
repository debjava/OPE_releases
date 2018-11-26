
/********************************************************************************************
* Copyright 2008 IDEAL INVENT Technologies Pvt. Ltd.                                        *
* IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.       *
* The Program or any portion thereof may not be reproduced in any form whatsoever except    *
* as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd. *
*																							*
* File Name                   : HelpDeskDAOOpe.java                                         *
* Author                      : Manjunath N G                                               *
* Creation Date               : 06-Nov-2008                                                 *
* Description                 : This file serves as the main DAO class. It creates          *
*								connection the string.It defines to which database          *
*								OPE connects.                                         *
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

package com.dnb.agreement.helpdesk.DAO;

import com.dnb.agreement.helpdesk.DAO.HelpDeskReportsDAO;

 /** 
  * Abstract class DAO Factory for OPE
  */

public abstract class HelpDeskDAOOpe 
{

 /** 
  * List of DAO types supported by this factory
  */

  public static final int ORACLE = 1;
  
  public abstract HelpDeskReportsDAO getHelpDeskReportsDAO(); 
    
  public static HelpDeskDAOOpe getDAOOpe(int DNBFactory) 
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