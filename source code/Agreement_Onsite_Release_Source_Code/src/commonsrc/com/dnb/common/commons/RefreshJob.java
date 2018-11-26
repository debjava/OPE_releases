/********************************************************************************************
 * Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                       *
 * IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.      *
 * The Program or any portion thereof may not be reproduced in any form whatsoever except   *
 * as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd.*
 *																							*
 * File Name                   : RefreshJob.java                                        	*
 * Author                      : Raja Mohan                                                 *
 * Creation Date               : 01-May-2006                                                *
 * Description                 : Refresh job which calls components to refresh cache		*
 * Modification History        :                                                            *
 *																						    *
 *     Version No.               Date               Brief description of change             *
 *  --------------------------------------------------------------------------------------- *
 *                       |                  |											    *
 *                       |                  |											    *
 *  --------------------------------------------------------------------------------------- *
 ********************************************************************************************/
package com.dnb.common.commons;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Make a call to a component to refresh its cache.
 */
public class RefreshJob implements Job {
	/** Logger instance */
	private static Logger logger = Logger.getLogger(RefreshJob.class);

	/**
	 * Retrieve a component name from the JobExecutionContext. Retrieve that
	 * component from ComponentCache. Make that component refresh its cache.
	 * 
	 * @param jobExecCtxt
	 *            A context bundle containing handles to various environment
	 *            information
	 * @throws JobExecutionException
	 *             if component was not found in the component cache
	 */
	public void execute(JobExecutionContext jobExecCtxt)
			throws JobExecutionException {
		JobDataMap map = jobExecCtxt.getJobDetail().getJobDataMap();
		String componentName = (String) map
				.get(CacheScheduler.CACHE_REFRESH_COMPONENT_NAME);
		DataCache component = (DataCache) ComponentCache
				.getComponent(componentName);

		if (component != null) {

			logger.info("About to call refreshCache() on component: "
					+ componentName);
			component.refreshCache();
			logger.info("Refreshed cache on component: " + componentName);
		} else {
			logger.error("Unable to retrieve component: " + componentName
					+ "from component cache.");
			throw new JobExecutionException(false); // will not refire
		}
	}
}
