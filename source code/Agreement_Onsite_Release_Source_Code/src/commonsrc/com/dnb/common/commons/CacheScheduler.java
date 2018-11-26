/********************************************************************************************
 * Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                       *
 * IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.      *
 * The Program or any portion thereof may not be reproduced in any form whatsoever except   *
 * as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd.*
 *																							*
 * File Name                   : CacheScheduler.java                                        *
 * Author                      : Raja Mohan                                                 *
 * Creation Date               : 28-Apr-2006                                                *
 * Description                 : Schedules caching objects for refresh using quartz			*
 * Modification History        :                                                            *
 *																						    *
 *     Version No.               Date               Brief description of change             *
 *  --------------------------------------------------------------------------------------- *
 *                       |                  |											    *
 *                       |                  |											    *
 *  --------------------------------------------------------------------------------------- *
 ********************************************************************************************/

package com.dnb.common.commons;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

import com.dnb.common.utility.StringUtils;

/**
 * Component to set up specified components on a schedule for cache refresh.<br>
 * <br>
 * Schedule to Component mappings are passed in by property file.<br>
 * <br>
 * Property names are structured in the form Schedule1, Schedule2, ...<br>
 * <br>
 * Property values are structured in the form
 * <i>&lt;schedule&gt;:&lt;component1&gt;,&lt;component2&gt;,....</i><br>
 * <br>
 * Schedules are in Cron syntax (e.g. 0 15 1 * * ? for 1:15 am every day of the
 * year) - please refer to the <a
 * href="http://quartz.sourceforge.net/javadoc/org/quartz/CronTrigger.html">CronTrigger</a>
 * JavaDoc for correct formatting<br>
 * <br>
 * Components are separated by a ','<br>
 * <br>
 * Schedule and Components for that Schedule are separated by a ':'<br>
 * <br>
 * 
 * <b>Note:</b> This class will create all JobDetails and Triggers within the
 * same default group.
 * <P>
 * 
 * TODO - Consider grouping components together to facilitate multiple component
 * refreshes by one call.<br>
 * <br>
 * TODO - Allow for Schedules to be configured that are of type SimpleTrigger as
 * well as CronTrigger
 */
public class CacheScheduler implements Initializable {
	/** Logger instance */
	private static Logger logger = Logger.getLogger(CacheScheduler.class);

	/** Constant used in JobName creation */
	private static final String JOB_CONST = "Job";

	/** Constant used in TriggerName creation */
	private static final String TRIGGER_CONST = "Trigger";

	/**
	 * Constant used for storing a component name in JobDataMap which is then
	 * placed inside a JobDetail
	 */
	public static final String CACHE_REFRESH_COMPONENT_NAME = "CacheRefreshComponentName";

	/** Quartz Scheduler */
	private static Scheduler scheduler;

	static {
		try {
			scheduler = StdSchedulerFactory.getDefaultScheduler();
		} catch (SchedulerException e) {
			String errorMsg = "Unable to retrieve reference to Scheduler - so cannot support "
					+ "Cache Refresh. THIS IS BAD!!!!!";
			logger.error(errorMsg, e);
			throw new RuntimeException(errorMsg, e);
		}
	}

	/**
	 * Retrieve Schedules and Components from this classes' property file.
	 * Create JobDetails for each Component and tie them to a Trigger based on
	 * that Component's schedule.
	 * 
	 * Start the Quartz Scheduler running<br>
	 * <br>
	 * 
	 * TODO - Currently this code will not support a run time re-initialization
	 * of this Component (e.g. to load in changes/updates to the property file).
	 * To implement it is simply a matter of surrounding the call to
	 * scheduler.start() with a check to see if the Scheduler is currently
	 * running. If it is - then don't start it again.
	 * 
	 * @param props
	 *            Properties file for this component containing mappings between
	 *            different schedules and the components that want to be
	 *            refreshed on those schedules
	 */
	public void initialize(Properties props) {
		Enumeration keyEnum = props.keys();
		String scheduleKey = "Schedule";
		ArrayList scheduleAndComponents = new ArrayList();
		String key;
		boolean jobsToRun = false;

		while (keyEnum.hasMoreElements()) {
			key = (String) keyEnum.nextElement();
			if (key.indexOf(scheduleKey) != -1) {
				String propertyStr = props.getProperty(key);
				scheduleAndComponents.add(propertyStr);
			}
		}

		if (scheduleAndComponents != null && scheduleAndComponents.size() > 0) {
			jobsToRun = scheduleCacheJobs(scheduleAndComponents);
			// only start the Scheduler if jobs were registered with it
			if (jobsToRun) {
				try {
					scheduler.start();
				} catch (SchedulerException e) {
					logger.info("Error Prevented Jobs From Being Started: ", e);
				}
			} else {
				logger.info("No Jobs were scheduled with Scheduler. Exiting.");
			}
		} else {
			logger.info("No Components Configured for Cache Refresh Scheduling ");
		}
	}

	/**
	 * 
	 * Cycle through each element contained in the ArrayList parameter and build
	 * a Quartz JobDetail and CronTrigger for each component that is tied to
	 * that schedule.<br>
	 * <br>
	 * 
	 * Register the JobDetail/Trigger pair with Quarz's Scheduler class
	 * 
	 * @param scheduleAndComponents
	 *            ArrayList of Strings, each containing a schedule and the
	 *            components that wish to be updated on that schedule. String
	 *            must have a format of:<br>
	 *            <i>ScheduleX=&lt;schedule&gt;:&lt;component1&gt;,&lt;component2&gt;</i>
	 * @return boolean flag to indicate if any Jobs were scheduled with the
	 *         Quartz Scheduler
	 */
	private boolean scheduleCacheJobs(ArrayList scheduleAndComponents) {
		String schedule;
		String components;
		String componentName = "";
		String scheduleComponentSepChar = ":";
		String componentsSepChar = ",";
		JobDetail jobDetail;
		CronTrigger trigger;
		Object component;
		int tokenCount;
		boolean jobsToRun = false;

		for (int i = 0; i < scheduleAndComponents.size(); i++) {
			StringTokenizer tokens = new StringTokenizer(
					(String) scheduleAndComponents.get(i),
					scheduleComponentSepChar);
			tokenCount = tokens.countTokens();
			if (tokenCount == 2) {
				schedule = tokens.nextToken();
				components = tokens.nextToken();
				if (!(StringUtils.isStringEmpty(components))) {
					StringTokenizer tokens2 = new StringTokenizer(components,
							componentsSepChar);
					try {
						while (tokens2.hasMoreTokens()) {
							componentName = tokens2.nextToken();
							component = ComponentCache
									.getComponent(componentName);
							if (component instanceof DataCache) {
								jobDetail = buildJobDetail(componentName);
								trigger = buildTrigger(schedule, componentName,
										jobDetail);
								scheduler.scheduleJob(jobDetail, trigger);
								// let calling method know that at least one Job
								// has been
								// scheduled with the Scheduler
								jobsToRun = true;

								logger
										.info("Component: "
												+ componentName
												+ " Entered Into Cache Refresh Scheduler.\n"
												+ "Next Run Time Will Be At: "
												+ trigger.getNextFireTime());
							} 
//							else {
//								logger
//										.info("Attempted to schedule component: "
//												+ componentName
//												+ " for cache refresh that is not of type: "
//												+ DataCache.class
//												+ "\nThis component will be ignored");
//							}
						}
					} catch (SchedulerException e) {
						logger.info("Problem occurred Scheduling component: "
								+ componentName, e);
						return false;
					} catch (ParseException pe) {
						logger
								.info(
										"CRON Syntax Error: Please Update Property File"
												+ " With Correct Schedule Syntax\n",
										pe);
						return false;
					}
				} else {
					logger
							.info("No Components were configured to be refreshed "
									+ "for schedule: " + schedule);
				}
			} else {
				logger
						.error("Property File Is Not Configured Correctly. Please "
								+ "Specify A Schedule In CRON Format And A Comma-Delimited List Of "
								+ "Components. The Schedule And The Components Need To Be Delimited "
								+ "By A ':' Character");
			}
		}
		return jobsToRun;
	}

	/**
	 * Create a JobDetail that will contain the classname of the Job to be run
	 * and also contain any run time information to be used by the Job within a
	 * JobDataMap
	 * 
	 * @param componentName
	 *            String containing the name of the component that this
	 *            JobDetail will base its name on
	 * @return JobDetail Conveys the detail properties of a given Job instance.
	 */
	private JobDetail buildJobDetail(String componentName) {
		JobDataMap map = new JobDataMap();
		map.put(CACHE_REFRESH_COMPONENT_NAME, componentName);
		String jobName = componentName + JOB_CONST;
		JobDetail jobDetail = new JobDetail(jobName, Scheduler.DEFAULT_GROUP,
				RefreshJob.class);
		jobDetail.setJobDataMap(map);
		return jobDetail;
	}

	/**
	 * Create and return a CronTrigger given:<br> - a schedule in CRON syntax<br> -
	 * a component name<br> - a JobDetail
	 * 
	 * @param schedule
	 *            String containing a CRON syntax schedule
	 * @param componentName
	 *            String containing the name of the component that this Trigger
	 *            will base its name on
	 * @param jobDetail
	 *            Conveys the detail properties of a given Job instance.
	 * @return CronTrigger A concrete Trigger that is used to fire a JobDetail
	 *         at given moments in time, defined with Unix 'cron-like'
	 *         definitions.
	 * @throws ParseException
	 *             Thrown if the CRON syntax is incorrect
	 */
	private CronTrigger buildTrigger(String schedule, String componentName,
			JobDetail jobDetail) throws ParseException {
		String triggerName = componentName + TRIGGER_CONST;
		CronTrigger trigger = new CronTrigger(triggerName,
				Scheduler.DEFAULT_GROUP, jobDetail.getName(), jobDetail
						.getGroup(), schedule);
		return trigger;
	}
}
