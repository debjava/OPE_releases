package com.ope.scheduler.action;

import com.ope.scheduler.operation.RequestOperation;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import java.util.Date;
import java.util.TimerTask;;

public class IncomingFileProcessor extends TimerTask implements Job
{
	  public void execute(JobExecutionContext arg0) throws JobExecutionException
	  {	
	  /*
		  try
		  {
			  JobDataMap jdMap  = ( JobDataMap )arg0.getJobDetail().getJobDataMap();
			  System.out.println(jdMap.get("Saourabh"));
		  }
		  catch( ClassCastException cce )
		  {
			  cce.printStackTrace();
		  }
	   */
		System.out.println("");
		System.out.println("*._.*._.*._.*._.*._.*._.*._.*._.*._.*._.*._.*._.*._.*._.*._.*._.*._.*._.*._.*._.*._.*._.*._.*._.*._.*._.*");
		System.out.println("*._.*._.*._.*._.*._.* Run - Incoming File Processor - Scheduler: " + new Date()+"._.*._.*._.*._.*");
		System.out.println("*._.*._.*._.*._.*._.*._.*._.*._.*._.*._.*._.*._.*._.*._.*._.*._.*._.*._.*._.*._.*._.*._.*._.*._.*._.*._.*");
		System.out.println("");
	    // Call a method, which will start the operation
		new RequestOperation().doTask();
	  }
	  
	  public void run() 
	  {
		System.out.println("Run - Timer Scheduler: " + new Date());
		
		// Call a method, which will start the operation
	  }
}