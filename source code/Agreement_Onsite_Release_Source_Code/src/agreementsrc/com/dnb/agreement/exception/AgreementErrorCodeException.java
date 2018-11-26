
/********************************************************************************************
* Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                        *
* IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.       *
* The Program or any portion thereof may not be reproduced in any form whatsoever except    *
* as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd. *
*																							*
* File Name                   : AgreementErrorCodeException.java                            *
* Author                      : Manjunath N G                                               *
* Creation Date               : 24-July-2008                                                *
* Description                 : This interface covers exceptions that carry codes that		*
*								give further details on what happened to cause the error	*
*																							*
* Modification History        :                                                             *
*																						    *
*     Version No.               Date               Brief description of change              *
*  ---------------------------------------------------------------------------------------  *
*                       |                  |											    *
*                       |                  |											    *
*  ---------------------------------------------------------------------------------------  *
*********************************************************************************************/

package com.dnb.agreement.exception;

public interface AgreementErrorCodeException
{
    /**
     * @return The application-specific error code 
     */
    public String getErrorCode();
}
