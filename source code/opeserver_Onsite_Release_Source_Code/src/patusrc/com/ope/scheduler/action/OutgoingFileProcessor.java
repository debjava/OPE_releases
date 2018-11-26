package com.ope.scheduler.action;

import com.ope.scheduler.operation.RequestOperation;
import com.ope.scheduler.operation.ResponseOperation;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import java.util.Date;
import java.util.TimerTask;;

public class OutgoingFileProcessor extends TimerTask implements Job
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
		System.out.println("*._.*._.*._.*._.*._.* Run - Outgoing File Processor - Scheduler: " + new Date()+"._.*._.*._.*._.*");
		System.out.println("*._.*._.*._.*._.*._.*._.*._.*._.*._.*._.*._.*._.*._.*._.*._.*._.*._.*._.*._.*._.*._.*._.*._.*._.*._.*._.*");
		System.out.println("");
	    // Call a method, which will start the operation
		
		new ResponseOperation().doTask();
		
		
		
	  }
	  
	  public void run() 
	  {
		System.out.println("Run - Timer Scheduler: " + new Date());
		
		// Call a method, which will start the operation
	  }
}