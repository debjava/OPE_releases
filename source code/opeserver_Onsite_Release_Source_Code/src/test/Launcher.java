import com.coldcore.coloradoftp.core.Core;
import com.coldcore.coloradoftp.factory.ObjectFactory;
import com.coldcore.coloradoftp.factory.ObjectName;
import com.coldcore.coloradoftp.factory.impl.SpringFactory;

import org.apache.log4j.Logger;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import java.io.File;

/**
 * Launcher.
 *
 * A simple class to start an FTP server as a standalone application.
 * When started, FTP server can be killed by killing the process it is running in, but doing
 * so will terminate the server immediately without calling its stop or poisoned routines.
 *
 *
 * ColoradoFTP - The Open Source FTP Server (http://cftp.coldcore.com)
 */
public class Launcher {

  private static String filename = "conf/beans.xml";
  
  protected static Logger logger = Logger.getLogger(Launcher.class);
  
  /*
   * Configuration file for starting the scheduler
   */
  private static String schConfigProPath = System.getProperty("user.dir")
			+ File.separator + "conf" + File.separator
			+ "ope-scheduler.properties";
 
  /*
  private static void startScheduler()
  {
	  try
	  {
		  logger.debug("Now going to call Scheduler....");
		  logger.debug("schConfigProPath.........."+schConfigProPath);
		  logger.debug("Now going to call Scheduler....");
	      Properties properties = new Properties();
	      properties.load(new FileInputStream(schConfigProPath));
//	       * Starting the scheduler for file operation
	
	      AbstractScheduler.getScheduler(properties).start(); 
	  }
	  catch( Exception e )
	  {
		  e.printStackTrace();
	  }
  }
*/
  public static void main(String[] args)  {
    logger.debug("");
    logger.debug("========================================");
    logger.debug("ColoradoFTP - the open source FTP server");
    logger.debug("Make  sure  to  visit   www.coldcore.com");
    logger.debug("========================================");
    
	String confLocation = System.getProperty("user.dir") + File.separator + "conf"; 
	
	String beanFile = confLocation + File.separator + "beans.xml";
    
    File file = new File(beanFile);
    if (args.length > 0) file = new File(args[0]);

    logger.debug("Reading configuration from: "+file.getAbsolutePath());
    logger.debug("To set a different configuration file use 'Launcher filename'");

    if (!file.exists()) {
      logger.debug("Configuration file not found, terminating...");
      System.exit(1);
    }

    try {
      Resource resource = new FileSystemResource(file);
      ObjectFactory.setInternalFactory(new SpringFactory(resource));
    } catch (Throwable e) {
      logger.debug("Cannot initialize object factory, terminating...");
      e.printStackTrace();
      System.exit(1);
    } 
    logger.debug("Object factory initialized");

    Core core = null;
    try {
      core = (Core) ObjectFactory.getObject(ObjectName.CORE);
      core.start();
      /*
       * Starting the scheduler
       */
     // startScheduler();
    } catch (Throwable e) {
      logger.debug("Unable to start the server, terminating...");
      e.printStackTrace();
      System.exit(1);
    }

    //todo Shutdown Hook
    //addShutdownHook(core);

    logger.debug("Server started, use Ctrl+C to kill the process");
  }

/*
  private static void addShutdownHook(final Core core) {
    Runnable shutdownHook = new Runnable() {
      public void run() {
        logger.debug("Stopping server...");
        core.poison();
        //todo wait till everyone disconnects
        core.stop();
      }
    };
    Runtime runtime = Runtime.getRuntime();
    runtime.addShutdownHook(new Thread(shutdownHook));
  }
*/

}
