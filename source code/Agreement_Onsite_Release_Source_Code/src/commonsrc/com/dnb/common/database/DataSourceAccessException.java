/********************************************************************************************
 * Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                       *
 * IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.      *
 * The Program or any portion thereof may not be reproduced in any form whatsoever except   *
 * as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd.*
 *																							*
 * File Name                   : DataSourceAccessException.java                             *
 * Author                      : Raja Mohan                                                 *
 * Creation Date               : 28-Apr-2006                                                *
 * Description                 : Exception raised when problems with datasource		        *
 * Modification History        :                                                            *
 *																						    *
 *     Version No.               Date               Brief description of change             *
 *  --------------------------------------------------------------------------------------- *
 *                       |                  |											    *
 *                       |                  |											    *
 *  --------------------------------------------------------------------------------------- *
 ********************************************************************************************/

package com.dnb.common.database;

import com.dnb.common.exception.CommonRuntimeException;

/**
 * This exception represents a notification process for
 * the production support team that access to a data source
 * has failed.<br><br>
 *
 * This exception will be used to wrap both of the possible
 * exceptions that could occur when attempting to retrieve
 * a connection from a data source.
 * <ul>
 * <li>Firstly a {@link javax.naming.NamingException}
 * which could occur in the event that no DataSource
 * has been registered in JNDI under the specific name.
 * <li>Secondly a {@link java.sql.SQLException} which
 * could occur in the event that no connection could
 * be retrieved from the DataSource
 * </ul>
 * The assumption is that due to redundant
 * connection pools represented by Multipools in our
 * production configuration this exception should never be
 * thrown. However in the event of failure on all pools
 * represented by a Multipool this exception will be raised.
 */
public class DataSourceAccessException extends CommonRuntimeException
{
    /**
     * Constructs a new DataSourceAccessException with the
     * specified detail message.
     *
     * @param   message   the detail message.
     * @see RuntimeException
     */
    public DataSourceAccessException(String errorCode,String message)
    {
        super(errorCode, message);
    }
}
