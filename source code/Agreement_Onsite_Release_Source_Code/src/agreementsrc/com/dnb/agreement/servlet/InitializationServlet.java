package com.dnb.agreement.servlet;

import java.beans.PropertyEditorManager;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

import com.dnb.common.commons.ComponentCache;
import com.dnb.common.commons.ComponentCacheInitializer;

/**
 * Initialize the web application.
 * Create a {@link common.cache.ConfigPathLoader} using the configpath init parameter,
 * and use to initalize all of the components that are configured in the
 * InitializationServlet.properties file.
 */
public class InitializationServlet extends HttpServlet
{
// ------------------------------ FIELDS ------------------------------

	private static final long serialVersionUID = 1L;
	/** Logger for this class */
    private static Logger log;

// -------------------------- OTHER METHODS --------------------------

    /**
     * Stop this servlet. Shut down the Quartz Scheduler. Clean out the cache.
     */
    public void destroy()
    {
        log = null;
        ComponentCache.clear();
        try {
        	//to shut down all scheduler threads.....
			Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
			scheduler.shutdown();
		} catch (SchedulerException e) {
		}
        
        super.destroy();
    }

    /**
     * Initialize this servlet.  Create the configpathloader, start up the
     * Quartz Scheduler and start all of the configured components
     * @param config Contain the servlet configuration for this application.
     * @throws ServletException If something bad happens
     */
    public void init(ServletConfig config) throws ServletException
    {
		
        super.init(config);
        String loggerConfig = resolveContainerPath(config.getInitParameter("logger-config"));
        if ( loggerConfig != null && !"".equals(loggerConfig.trim()) ) {
        	Properties loggerProps = new Properties();
        	try {
				loggerProps.load(new FileInputStream(loggerConfig));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			PropertyConfigurator.configure(loggerProps);
			log = Logger.getLogger(getClass());
		}
        ComponentCacheInitializer.initializeComponentCache();
        try {
        	log.debug("Registering property editor for java.util.List ");
			Class clazz = Class.forName("com.dnb.agreement.tag.ListEditor");
			log.debug("PropertyEditor: " + clazz);
			PropertyEditorManager.registerEditor(List.class, clazz);
		} catch (ClassNotFoundException e) {
			log.error("Error registering property editor "+e);
			
		} catch (NullPointerException e) {

		}
    }
    private String resolveContainerPath(String path) {
    	if ( path != null && !"".equals(path.trim()) ) {
        	path = path.trim();
        	if ( '/' == path.charAt(0) ) {
        		path = getServletContext().getRealPath(path); 
        	}
        }
    	return path;
    }
}
