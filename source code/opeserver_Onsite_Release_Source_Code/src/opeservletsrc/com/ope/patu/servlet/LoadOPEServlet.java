package com.ope.patu.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;

import com.ope.patu.ejb.server.OpeEjbBean;
import com.ope.scheduler.action.AbstractScheduler;

@SuppressWarnings("serial")
public class LoadOPEServlet extends HttpServlet {
	
	  /*
	   * Configuration file for starting the scheduler
	  **/
	
	
//	 private static String schConfigProPath = "conf" + File.separator + "ope-scheduler.properties";
	
	 private static String schConfigProPath = null;

		protected static Logger logger = Logger.getLogger(LoadOPEServlet.class);
		
	 
	 public void init() {
		 
		 	String confLoaction = null;
		 	logger.debug( getServletName() + " : initialised" );
		    		    
		    InputStream is = null;
			is = LoadOPEServlet.class.getResourceAsStream("/ope-setup.properties");
			
			Properties prop = new Properties();
			
			try {
				prop.load(is);
			} catch (Exception e1) {
				e1.printStackTrace();
				logger.debug( "LoadOPEServlet :: init ::  Exception :: " + e1.getMessage());
			}
			
			confLoaction  = prop.getProperty("ope.conf.location");
			logger.debug( "Conf Loaction :: "+ confLoaction );
			schConfigProPath = confLoaction + File.separator + "ope-scheduler.properties";
			logger.debug( "Sch Config Pro Path :: "+ schConfigProPath );
						
		    startScheduler();
	 }

	 private static void startScheduler()
	 {
		  try
		  {
			  logger.debug("Now going to call Scheduler....");
			  logger.debug("schConfigProPath.........."+schConfigProPath);
			  logger.debug("Now going to call Scheduler....");
			  
		      Properties properties = new Properties();
		      properties.load(new FileInputStream(schConfigProPath));
		      /*
		       * Starting the scheduler for file operation
		       */
		      AbstractScheduler.getScheduler(properties).start(); 
		  }
		  catch( Exception e )
		  {
			  e.printStackTrace();
			  logger.debug("Caught in Exception :: "+e.getMessage());
		  }
	  }
}