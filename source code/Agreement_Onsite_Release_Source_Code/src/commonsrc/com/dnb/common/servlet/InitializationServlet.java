package com.dnb.common.servlet;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import com.dnb.common.commons.ComponentCache;
import com.dnb.common.commons.ComponentCacheInitializer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Initialize the web application.
 * Create a {@link common.cache.ConfigPathLoader} using the configpath init parameter,
 * and use to initalize all of the components that are configured in the
 * InitializationServlet.properties file.
 */
public class InitializationServlet extends HttpServlet
{
// ------------------------------ FIELDS ------------------------------

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
		System.out.println("********* Inside init method of Initialization Servlet ******** "+config);
        super.init(config);
        System.out.println("config.getInitParameter(\"logger\") : "+config.getInitParameter("log4j"));
        String loggerConfig = resolveContainerPath(config.getInitParameter("log4j"));
        System.out.println("Logger Config : "+loggerConfig);
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
			log.debug("Testing Logger...");
			System.out.println("Logger Status : " +log.isDebugEnabled());
		}
        ComponentCacheInitializer.initializeComponentCache();
    }
    
    private String resolveContainerPath(String path) {
    	System.out.println("PATH : "+path);
    	if ( path != null && !"".equals(path.trim()) ) {
        	path = path.trim();
        	if ( '/' == path.charAt(0) ) {
        		System.out.println("path :"+path);
        		path = getServletContext().getRealPath(path);
        		System.out.println("path :"+path);
        	}
        }
    	return path;
    }
}
