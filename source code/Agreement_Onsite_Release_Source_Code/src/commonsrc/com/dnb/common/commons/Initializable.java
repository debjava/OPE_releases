/********************************************************************************************
 * Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                       *
 * IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.      *
 * The Program or any portion thereof may not be reproduced in any form whatsoever except   *
 * as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd.*
 *																							*
 * File Name                   : Initializable.java                                        	*
 * Author                      : Raja Mohan                                                 *
 * Creation Date               : 01-May-2006                                                *
 * Description                 : Interface for initializing a component						*
 * Modification History        :                                                            *
 *																						    *
 *     Version No.               Date               Brief description of change             *
 *  --------------------------------------------------------------------------------------- *
 *                       |                  |											    *
 *                       |                  |											    *
 *  --------------------------------------------------------------------------------------- *
 ********************************************************************************************/
package com.dnb.common.commons;

import java.util.Properties;

/**
 * Provide a method for the ComponentCache to initialize a component when
 * creating it to be stored in cache.
 */
public interface Initializable {
	/**
	 * Initialize this component using the given properties object
	 * 
	 * @param props
	 *            Properties to initialize component
	 */
	void initialize(Properties props);
}