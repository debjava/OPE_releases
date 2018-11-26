/********************************************************************************************
* Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                        *
* IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.       *
* The Program or any portion thereof may not be reproduced in any form whatsoever except    *
* as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd. *
*																							*
* File Name                   : SessionObjectLoader.java                                    *
* Author                      : Virendrasinh Chavada                                        *
* Creation Date               : 22-May-2007                                                 *
* Description                 : This file serves as the session handler class.				*
* Modification History        :                                                             *
*																						    *
* Version No.    Name                       Date               Brief description of change  *
*  ---------------------------------------------------------------------------------------  *
*          |                       |                  |									    *
*  ---------------------------------------------------------------------------------------  *
*********************************************************************************************/
package com.dnb.common.utility;

import com.dnb.common.exception.CommonSystemException;

/**
 * <p align="justified">
 * This class is an implementation of Singleton design pattern.
 * This class will create only one instace at the time of loading
 * the application. Please follow the use of this class.
 * 
 * </p>
 * <p align="justified">
 * <code> SessionObjectLoader objLoader = {@link SessionSessionObjectLoader}.getInstance();</code>
 * </p>
 * @author Virendrasinh.c
 *
 */
public class SessionObjectLoader 
{
	/**
	 * A boolean variable to track the multiple
	 * instance of the object.For one instance it 
	 * should be true otherwise it is false.
	 */
	private static boolean checkInstance = false;
	/**
	 *Pojo is a simple bean which acts as a data holder. 
	 *<b> You are free to modify the bean as per the
	 *requirement of the application </b>.
	 */
	private static SessionData sdh = null;
	/**
	 * Instance of the {@link SessionObjectLoader}
	 */
	private static SessionObjectLoader objectLoader = null;
	
	/**
	 * Default constructor.
	 */
	private SessionObjectLoader()
	{
		super();
	}
	
	/**
	 * <p align="justified">
	 * This method is used to obtain an instance.This instance
	 * will be available throughout the application.
	 * </p>.
	 * <p align="justified">
	 * <font = "Palatino Linotype" color="RED">This method throws
	 * {@link DMException} if the instance is already available
	 * throughout the application.
	 * </font>
	 * </p>
	 * @param pojo of type {@link Pojo}
	 * @return sn instance of the {@link SessionObjectLoader}
	 * @throws DMException of type {@link DMException}
	 * see {@link DMException}
	 */
	public static synchronized SessionObjectLoader getInstance() throws CommonSystemException
	{
		try
		{
			if( SessionObjectLoader.checkInstance && objectLoader != null )
			{
				throw new CommonSystemException("SessionObjectLoader ::","Trying to instantiate more instances");
			}
			else if( !SessionObjectLoader.checkInstance && objectLoader == null)
			{
				SessionObjectLoader.checkInstance = true;
				objectLoader = new SessionObjectLoader();
			}
			else if( SessionObjectLoader.checkInstance && objectLoader == null )
			{
				System.out.println("This is a rare situation, it should not come");
				throw new CommonSystemException("SessionObjectLoader ::"," A case where check flag is true but instance is null");
			}
			else if( !SessionObjectLoader.checkInstance && objectLoader != null )
			{
				System.out.println("This is a rare situation, it should not come");
				throw new CommonSystemException("SessionObjectLoader ::"," A case where check flag is false but instance is not null");
			}
		}
		catch( CommonSystemException me )
		{
			me.printStackTrace();
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		return objectLoader;
	}


	/**Method to obtain a {@link Pojo}
	 * @return a {@link Pojo}
	 */
	public static SessionData getSessionData() {
		return sdh;
	}

	/**
	 * Method to set the {@link Pojo}
	 * @param pojo of type {@link Pojo}
	 */
	public static void setSessionData(SessionData sdh) {
		SessionObjectLoader.sdh = sdh;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#finalize()
	 */
	public void finalize()
	{
		try
		{
			SessionObjectLoader.objectLoader = null;
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}
	
}
