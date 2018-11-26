/********************************************************************************************
 * Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                       *
 * IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.      *
 * The Program or any portion thereof may not be reproduced in any form whatsoever except   *
 * as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd.*
 *																							*
 * File Name                   : ClasspathLoader.java                                       *
 * Author                      : Raja Mohan                                                 *
 * Creation Date               : 28-Apr-2006                                                *
 * Description                 : Schedules caching objects for refresh using quartz			*
 * Modification History        :                                                            *
 *																						    *
 *     Version No.               Date               Brief description of change             *
 *  --------------------------------------------------------------------------------------- *
 *                       |                  |											    *
 *                       |                  |											    *
 *  --------------------------------------------------------------------------------------- *
 ********************************************************************************************/

package com.dnb.common.commons;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * This class supports the retrieval and loading of Properties file, as well as
 * the retrieval of files and their conversion to an InputStream
 */
public class ClasspathLoader {
	/** Logger instance */
	private static Logger log = Logger.getLogger(ClasspathLoader.class);

	/** Private object reference */
	private ClassLoader loader;

	/**
	 * Public constructor that will first attempt to retrieve a context
	 * ClassLoader for the running thread, and if that is null it will retrieve
	 * the ClassLoader that loaded this class.
	 */
	public ClasspathLoader() {
		loader = Thread.currentThread().getContextClassLoader();
		if (loader == null) {
			loader = ClasspathLoader.class.getClassLoader();
			log.debug("Called class classloader ");
		}
	}

	/**
	 * Attempt to retrieve a component property file from the classpath and load
	 * its contents into a newly instantiated Properties object. <br>
	 * <br>
	 * 
	 * This method will check to ensure that the argument has a '.properties'
	 * extension. If it does not - then one will be added to the end of the
	 * provided property file name and that will be the name that is searched
	 * for <br>
	 * <br>
	 * 
	 * This method uses the {@link IProperties} class to perform the loading of
	 * the contents of the .properties file.
	 * 
	 * @param propertyFileName
	 *            Name of a properties file that should be retrieved from the
	 *            classpath and whose contents is to be loaded into a newly
	 *            instantiated Properties object
	 * @return A Properties file that contains the contents of the specified
	 *         propertyFileName or an empty Properties file in the event that no
	 *         file could be retrieved from the classpath.
	 * 
	 * @see IProperties
	 */
	public Properties getPropertyFile(String propertyFileName) {
		Properties ret = new Properties();
		try {
			if (!propertyFileName.endsWith(".properties"))
				propertyFileName = propertyFileName + ".properties";

			IProperties props = new IProperties();
			InputStream inStream = loader.getResourceAsStream(propertyFileName);
			if (inStream != null) {
				props.load(inStream);
				ret.putAll(props);
			} else {
				log.error("Unable to retrieve resource" + ": "
						+ propertyFileName + " from classpath");
			}
		} catch (IOException e) {
			log.error("Unable to retrieve resource with name: "
					+ propertyFileName, e);
		}
		return ret;
	}

	/**
	 * Retrieve a file from the classpath and return its contents as an
	 * InputStream
	 * 
	 * @param fileName
	 *            Name of the file that the ClassLoader should attempt to
	 *            retrieve from the classpath
	 * @return An InputStream containing the contents of the requested file or
	 *         null in the event that no file could be retrieved from the
	 *         classpath
	 */
	public InputStream getFileInputStream(String fileName) {
		return loader.getResourceAsStream(fileName);
	}

}
