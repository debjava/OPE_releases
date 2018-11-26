package com.ope.scheduler.action;

import java.util.Properties;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;


public class QuartzScheduler implements OPEScheduler 
{

	Properties properties = null;
	
	public QuartzScheduler(Object...objects)
	{
		this.properties = (Properties)objects[0];
	}
	
	public void start(Object... objects) {
		 try {
			 
			  CronTrigger cronTrigger;
			  JobDetail jDetail;
			  System.out.println("properties=>"+properties.getProperty("interval.time"));
			  
			  SchedulerFactory sf=new StdSchedulerFactory();
			  Scheduler sched=sf.getScheduler();
			  sched.start();
			  
			  
			   /* ************** Pass the data and all other parameters in the JobDataMap *****************
			   * 
			   *    JobDataMap dataMap = new JobDataMap();
			   *	dataMap.put("Saurabh", new Integer(23));
			   *	jd.setJobDataMap(dataMap);
			   *
			   * ******************************************************************************************/
			  
			  /* *  ************** Configuration for the First Scheduler ********************************/
			  
			  jDetail = new JobDetail("Job1","group1",Class.forName(properties.getProperty("scheduler1.class")));
			  cronTrigger = new CronTrigger("cronTrigger1","group1",properties.getProperty("scheduler1.time"));
			  sched.scheduleJob(jDetail, cronTrigger);
			  
			  /* *  ************** Configuration for the Second Scheduler ********************************/
			  
			  jDetail = new JobDetail("Job2","group2",Class.forName(properties.getProperty("scheduler2.class")));
			  cronTrigger = new CronTrigger("cronTrigger2","group2",properties.getProperty("scheduler2.time"));
			  sched.scheduleJob(jDetail, cronTrigger);
			   
			  /* *****************************************************************************************/
			    
			  
		  }
		 catch (ClassNotFoundException cnfe) {
			  System.out.println("Caught in Scheduler : Class Not Found Exception");
		}catch (Exception e) {
			  System.out.println("Caught in Scheduler Exception");
		}	  
	}
}