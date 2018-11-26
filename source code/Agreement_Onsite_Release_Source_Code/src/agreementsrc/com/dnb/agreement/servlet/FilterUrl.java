/********************************************************************************************
 * Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                       *
 * IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.      *
 * The Program or any portion thereof may not be reproduced in any form whatsoever except   *
 * as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd.*
 *																							*
 * File Name                   : FilterUrl.java 			                                *
 * Author                      : Saurabh Thakkur											*
 * Creation Date               : 22-Nov-2007                                                *
 * Description                 : To prevent force close browsing. 							*
 * 								 (Stop user to access unauthorized files or jsp pages.)		*												*
 * Modification History        :                                                            *
 *																						    *
 *     Version No.               Date               Brief description of change             *
 *  --------------------------------------------------------------------------------------- *
 *                       |                  |											    *
 *                       |                  |											    *
 *  --------------------------------------------------------------------------------------- *
 ********************************************************************************************/

package com.dnb.agreement.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.log4j.Logger;

/**
  This filter will stop user to do forceclose browsing.
  
  For Example - 
  
  	http://localhost:8080/agreement/common/
	http://localhost:8080/agreement/jsp/common/
	http://localhost:8080/agreement/jsp/
 
 	Now with this filter user will not be able to see the contents or files
 	inside above specified directory structure form client side.
 */

public class FilterUrl implements Filter {

	private static Logger logger = Logger.getLogger(FilterUrl.class);

	private FilterConfig config;

	public void destroy() {
		// TODO Auto-generated method stub
		this.config = null;
	}

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain fc)
			throws IOException, ServletException {
		req.getRequestDispatcher("/unauthorized.jsp").forward(req, res);
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		this.config = filterConfig;
	}
}