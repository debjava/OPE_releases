package com.ope.scheduler.action;

import java.util.Properties;


public abstract class AbstractScheduler 
{
	public static OPEScheduler getScheduler( Object ...objects )
	{
		/*
		 * 1. Pass the scheduler name as an input
		 * 2. Pass the specific config file for a specific scheduler
		 * 
		 */
		Properties properties = new Properties();
		OPEScheduler scheduler = null;
		// String schedulerType = ( String )objects[0];
		properties = (Properties) objects[0];
		
		String schedulerType = properties.getProperty("scheduler.type") ;
		if(schedulerType.equals("QUARTZ"))
		{	scheduler = new QuartzScheduler(properties);	}
		else if(schedulerType.equals("JAVAUTIL_TIMER"))
		{	scheduler = new TimerScheduler(properties);	}
		else
		{	
			scheduler = null;	
		}
		
		return scheduler;
	}
}
