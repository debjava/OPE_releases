/********************************************************************************************
 * Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                       *
 * IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.      *
 * The Program or any portion thereof may not be reproduced in any form whatsoever except   *
 * as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd.*
 *																							*
 * File Name                   : InitializationException.java                               *
 * Author                      : Raja Mohan                                                 *
 * Creation Date               : Apr 28, 2006                                               *
 * Description                 :                                                            *
 * Modification History        :                                                            *
 *																						    *
 *     Version No.               Date               Brief description of change             *
 *  --------------------------------------------------------------------------------------- *
 *                       |                  |											    *
 *                       |                  |											    *
 *  --------------------------------------------------------------------------------------- *
 ********************************************************************************************/

package com.dnb.common.commons;

import com.dnb.common.exception.CommonRuntimeException;

/**
 * Exception class that should be used by Initializable components that wish to
 * declare themselves as not being fit for use by an application.
 * 
 */
public class InitializationException extends CommonRuntimeException {
	/**
	 * Constructs a new InitializationException with the specified detail
	 * message.
	 * 
	 * @param message
	 *            the detail message.
	 * @see RuntimeException
	 */
	public InitializationException(String errorCode, String message) {
		super(errorCode, message);
	}
}
