package com.ope.scheduler.action;

import java.util.Properties;
import java.util.Timer;
import java.util.Calendar;

public class TimerScheduler implements OPEScheduler 
{
	
	Properties properties = null;
	
	public TimerScheduler(Properties prop)
	{
		this.properties = prop;
	}
	
	public void start(Object... objects) 
	{
	 try {
		 // Code for Timer Class 
		 Timer timer = new Timer();
		 Calendar date = Calendar.getInstance();
		 date.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
		 date.set(Calendar.HOUR, 0);
		 date.set(Calendar.MINUTE, 0);
		 date.set(Calendar.SECOND, 0);
		 date.set(Calendar.MILLISECOND, 0);
			  
		 //timer.schedule(new Scheduler(),date.getTime(),1000 * 60 * 60 * 24 * 7);
		// timer.schedule(new JobScheduler(),date.getTime(),Long.parseLong(properties.getProperty("interval.time")));
	 }catch (Exception e) {
			  // TODO: handle exception
			  System.out.println("Caught in Scheduler Exception");
	 }	  
	}
}
