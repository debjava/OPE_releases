package com.dnb.common.commons;

import com.dnb.common.exception.IErrorCodes;
import com.dnb.common.utility.StringUtils;

import java.util.*;

import org.apache.log4j.Logger;

/**
 * This class is responsible for initializing ComponentCache. It will identify
 * component config files and pass the component names into ComponentCache for
 * setup & storing.
 * 
 * Note: This class is mostly code from InitializationServlet, brought out of
 * that class in order to support a more generic startup approach.
 * 
 */
public class ComponentCacheInitializer {
	/** Debug/error logger */
	private static Logger log = Logger
			.getLogger(ComponentCacheInitializer.class);

	/**
	 * Constant that represents the initialization properties file for this
	 * class
	 */
	private static final String COMPONENT_CACHE_INIT_PROPERTIES_FILE = "ComponentCacheInitializer";

	/**
	 * Initialize component cache - all components will be loaded in threads
	 */
	public static void initializeComponentCache()
			throws InitializationException {
		Properties initProps = ComponentCache
				.getPropertyFile(COMPONENT_CACHE_INIT_PROPERTIES_FILE);

		if ((initProps == null) || initProps.isEmpty()) {
			throw new InitializationException(IErrorCodes.INITIALIZATION_ERROR,
					"Unable to find properties file");
		} else {

			log.debug("About to initialize the following " + "components: \n"
					+ initProps);
		}

		// Initialize components in initialThread
		String initObjects = initProps.getProperty("initialThread");
		initializeComponents(initObjects);

		String textMatch = "thread-";
		int propertyCount = initProps.size();
		String threadName;
		String componentList;
		Iterator iter = initProps.entrySet().iterator();
		Map.Entry entrySet;
		int myThreadsCount = 0;
		List threads = new ArrayList();
		Thread tmpThread;

		for (int i = 0; i < propertyCount; i++) {
			entrySet = (Map.Entry) iter.next();
			threadName = (String) entrySet.getKey();
			if (threadName.startsWith(textMatch)) {
				componentList = (String) entrySet.getValue();
				tmpThread = new InitThread(componentList);
				tmpThread.start();
				threads.add(tmpThread);
				myThreadsCount++;
			}
		}

		for (int j = 0; j < myThreadsCount; j++) {
			try {
				((Thread) threads.get(j)).join();
			} catch (InterruptedException e) {
				log.error("Interrupted Initialize Thread: "
						+ ((Thread) threads.get(j)).getName());
			}
		}

		// Initialize components in finalThread
		initObjects = initProps.getProperty("finalThread");
		initializeComponents(initObjects);
	}

	/**
	 * Initialize a thread of components in order.
	 * 
	 * @param initObjects
	 *            Comma-delimited list of component names to initialize
	 */
	static final void initializeComponents(String initObjects) {
		if (!(StringUtils.isStringEmpty(initObjects))) {
			StringTokenizer token = new StringTokenizer(initObjects, ",");
			while (token.hasMoreTokens()) {
				String propFile = token.nextToken();
				log.info("Initialize component[" + propFile + "]");
				log.info("Calling addComponent" + propFile);
				ComponentCache.addComponent(propFile);
			}
		}
	}

	// -------------------------- INNER CLASSES --------------------------

	/**
	 * An internal class to multi-thread the initialization of components
	 */
	private static class InitThread extends Thread {
		/** Contain the component name for this thread */
		private String componentList;

		/**
		 * Create a new thread.
		 * 
		 * @param componentList
		 *            The name of the component being initialized
		 */
		public InitThread(String componentList) {
			super(componentList);
			this.componentList = componentList;
		}

		/**
		 * Initialize the named component
		 */
		public void run() {
			ComponentCacheInitializer.initializeComponents(componentList);
		}
	}
}
