
/********************************************************************************************
* Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                        *
* IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.       *
* The Program or any portion thereof may not be reproduced in any form whatsoever except    *
* as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd. *
*																							*
* File Name                   : StatementBusinessException.java                                    *
* Author                      : Chenchi Reddy                                                    *
* Creation Date               : 24-Jun-2008                                                 *
* Description                 : This class represents errors that are a direct result of	*
*								user input. Errors of this type can usually be remedied by  *
*								letting the user try some other input.						*
*																							*
* Modification History        :                                                             *
*																						    *
*     Version No.               Date               Brief description of change              *
*  ---------------------------------------------------------------------------------------  *
*                       |                  |											    *
*                       |                  |											    *
*  ---------------------------------------------------------------------------------------  *
*********************************************************************************************/


package com.dnb.common.exception;

public class CommonBusinessException extends Exception implements CommonErrorCodeException
{
    private String errCode;

    /**
     * Create a new business exception
     * @param errorCode Detailed error code (should be found in IErrorCodes and in
     * 				    the error.js file)
     * @param message Human-readable message (will appear in logs)
     */
    public CommonBusinessException(String errorCode, String message)
    {
        super(message);
        this.errCode = errorCode;
    }

    /**
     * Wrap a lower-level exception in a PMBusinessException
     * 
     * @param errorCode Detailed error code (should be found in IErrorCodes and in
     * 				    the error.js file)
     * @param message Human-readable message (will appear in logs)
     * @param cause Root cause of this exception
     */
    public CommonBusinessException(String errorCode, String message, Exception cause)
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
