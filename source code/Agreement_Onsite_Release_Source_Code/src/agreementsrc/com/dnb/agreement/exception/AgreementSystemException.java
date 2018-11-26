
/********************************************************************************************
* Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                        *
* IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.       *
* The Program or any portion thereof may not be reproduced in any form whatsoever except    *
* as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd. *
*																							*
* File Name                   : AgreementSystemException.java                               *
* Author                      : Manjunath N G                                               *
* Creation Date               : 24-July-2008                                                *
* Description                 : This class represents system-level exceptions that are		*
*								not a result of invalid user input. The user might be able	*
*								to succesfully retry the operation, maybe not.				*
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

public class AgreementSystemException extends Exception implements AgreementErrorCodeException
{
    /** Reference to error code */
    private String errCode;

    /**
     * Create a new system exception
     * @param errorCode : Detailed error code 
     * @param message Human-readable message (will appear in logs)
     */
    public AgreementSystemException(String errorCode, String message)
    {
        super(message);
        this.errCode = errorCode;
    }

    /**
     * Wrap a lower-level exception in a PMSystemException
     * @param errorCode Detailed error code
     * @param message Human-readable message (will appear in logs)
     * @param cause Root cause of this exception
     */
    public AgreementSystemException(String errorCode, String message, Exception cause)
    {
        super(message, cause);
        this.errCode = errorCode;
    }

    /* (non-Javadoc)
     * @see com.pm.exception.PMErrorCodeException#getErrorCode()
     */
    public String getErrorCode()
    {
        return errCode;
    }
}
