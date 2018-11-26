/********************************************************************************************
 * Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                       *
 * IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.      *
 * The Program or any portion thereof may not be reproduced in any form whatsoever except   *
 * as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd.*
 *																							*
 * File Name                   : ComponentCache.java                                        *
 * Author                      : Raja Mohan                                                 *
 * Creation Date               : 28-Apr-2006                                                *
 * Description                 : Cache objects using a given key							*
 * Modification History        :                                                            *
 *																						    *
 *     Version No.               Date               Brief description of change             *
 *  --------------------------------------------------------------------------------------- *
 *                       |                  |											    *
 *                       |                  |											    *
 *  --------------------------------------------------------------------------------------- *
 ********************************************************************************************/
package com.dnb.common.commons;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;
import java.util.List;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import com.dnb.common.exception.IErrorCodes;

/**
 * Cache objects using a given key.
 * 
 * If the object being cached implements Initializable, then the initialize
 * method will be called with the component's property file once the object is
 * created.
 * 
 * If the object being cached implements XmlInitializable, then the initialize
 * method will be called with the component's XML document once the object is
 * created.
 * 
 */
public class ComponentCache {
	/** Store the objects */
	static HashMap cache = new HashMap();

	/** Logger for this class */
	private static Logger localLog = Logger.getLogger(ComponentCache.class);

	/** Classpath Loader to retrieve property files */
	private static ClasspathLoader loader = new ClasspathLoader();

	/**
	 * Private constructor to keep this class a singleton
	 */
	private ComponentCache() {
	}

	/**
	 * Add a component to the cache
	 * 
	 * @param name
	 *            Name of the component to add. This is the properties file to
	 *            load.
	 * @return false if component could not be loaded
	 */
	public static boolean addComponent(String name) {
		localLog.info("************** Property file name ***********" + name);
		Properties prop = ComponentCache.getPropertyFile(name);
		if (prop == null || prop.isEmpty()) {
			localLog.debug("Unable to find property file: " + name);
			return false;
		}
		return addComponent(name, prop);
	}

	/**
	 * Add a component to the cache. Initialize it with the properties object.
	 * 
	 * @param name
	 *            Component to be loaded and cached
	 * @param properties
	 *            Properties to initialize the component
	 * @return false if component could not be loaded
	 */
	public static boolean addComponent(String name, Properties properties) {
		String className = properties.getProperty("$class");
		localLog.info("************** className ***********" + className);
		try {
			Class clazz = Class.forName(className);
			Object obj = clazz.newInstance();
			if (obj instanceof Initializable) {
				Initializable comp = (Initializable) obj;
				comp.initialize(properties);
			}
			if (obj instanceof XmlInitializable) {
				passXmlToComponent(name, (XmlInitializable) obj);
			}
			cache.put(name, obj);
		} catch (InitializationException ie) {
			throw ie;
		} catch (Exception e) {
			throw new InitializationException(IErrorCodes.INITIALIZATION_ERROR,
					"Exception in Add component");
		}
		return true;
	}

	/**
	 * Get a reference to the component in cache
	 * 
	 * @param name
	 *            Name of the component to lookup in the cache
	 * @return The component found in cache (or null if not found)
	 */
	public static Object getComponent(String name) {
		return cache.get(name);
	}

	/**
	 * Retrieve a List of all components that are currently contained in the
	 * cache
	 * 
	 * @return List containing each of the Components that are contained in the
	 *         cache
	 */
	public static List getComponents() {
		List componentList = new ArrayList();
		componentList.addAll(cache.values());
		return componentList;
	}

	/**
	 * Clear out the cache. This can be called at shutdown.
	 */
	public static void clear() {
		cache.clear();
	}

	/**
	 * Return a property file loaded from the Classpath
	 * 
	 * @param filename
	 *            Property file to load
	 * @return Properties object containing the cumulative property data
	 */
	public static Properties getPropertyFile(String filename) {
		return loader.getPropertyFile(filename);
	}

	/**
	 * Load an XML file as a Document and pass it to a an XmlInitializable
	 * component.<br>
	 * <br>
	 * <b>Important Note:</b> This Document can be null if the ClasspathLoader
	 * is unable to retrieve it from the classpath
	 * 
	 * @param name
	 *            Name of the component - taken to be the name of the XML file
	 *            as well
	 * @param obj
	 *            XmlInitializable that is waiting for the file
	 */
	private static void passXmlToComponent(String name, XmlInitializable obj) {
		InputStream xmlFile = loader.getFileInputStream(name + ".xml");
		if (xmlFile == null) {
			throw new InitializationException(IErrorCodes.INITIALIZATION_ERROR,
					"Unable to find XML config document");
		} else {
			Document doc = null;
			try {
				DocumentBuilderFactory factory = DocumentBuilderFactory
						.newInstance();
				factory.setValidating(false);
				DocumentBuilder builder = factory.newDocumentBuilder();
				doc = builder.parse(xmlFile);
			} catch (Exception e) {
				throw new InitializationException(
						IErrorCodes.INITIALIZATION_ERROR,
						"Error while attempting to generate DOM");
			}
			obj.initialize(doc);
		}
	}

}
