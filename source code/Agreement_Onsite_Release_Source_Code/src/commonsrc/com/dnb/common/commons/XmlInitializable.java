/********************************************************************************************
 * Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                       *
 * IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.      *
 * The Program or any portion thereof may not be reproduced in any form whatsoever except   *
 * as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd.*
 *																							*
 * File Name                   : XmlInitializable.java                                      *
 * Author                      : Raja Mohan                                                 *
 * Creation Date               : 01-May-2006                                                *
 * Description                 : To pass in a XML file for configuration					*
 * Modification History        :                                                            *
 *																						    *
 *     Version No.               Date               Brief description of change             *
 *  --------------------------------------------------------------------------------------- *
 *                       |                  |											    *
 *                       |                  |											    *
 *  --------------------------------------------------------------------------------------- *
 ********************************************************************************************/

package com.dnb.common.commons;

import org.w3c.dom.Document;

/**
 * Provide a method for the ComponentCache to pass an XML config file to a
 * component as it is being created and stored in cache
 * 
 */
public interface XmlInitializable {
	/**
	 * Initialize this component using the given XML file (represented as a
	 * document)
	 * 
	 * @param document
	 *            Component-specific document.<br>
	 *            <br>
	 *            <b>Important Note: </b> This document CAN be null. It is up to
	 *            the implementor of this method to determine how to handle a
	 *            null Document.
	 * @see ComponentCache#passXmlToComponent(String, XmlInitializable)
	 */
	public void initialize(Document document);
}
