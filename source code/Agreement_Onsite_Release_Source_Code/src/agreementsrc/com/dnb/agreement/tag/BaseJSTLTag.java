/********************************************************************************************
 * Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                        *
 * IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.       *
 * The Program or any portion thereof may not be reproduced in any form whatsoever except    *
 * as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd. *
 *																							 *
 * File Name                   : BaseJSTLTag.java                                     		 *
 * Author                      : Manjunath N G                                               *
 * Creation Date               : 24-July-2008                                                *
 * Description                 : Helper class which provides basic implementation for JSTL	 *
 * 								 enabled tags			                        			 *
 * Modification History        :                                                             *
 *																						     *
 *     Version No.               Date               Brief description of change              *
 *  ---------------------------------------------------------------------------------------  *
 *                       |                  |											     *
 *                       |                  |											     *
 *  ---------------------------------------------------------------------------------------  *
 *********************************************************************************************/
package com.dnb.agreement.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.log4j.Logger;
import org.apache.taglibs.standard.lang.support.ExpressionEvaluatorManager;

/**
 * This is a helper class that provides a base implementation of the tag class
 * for JSTL-enabled tags. It handles the 'var' and 'scope' attributes, and
 * provides convenience methods for storing data into context as well as
 * evaluating JSTL expressions.
 */
public abstract class BaseJSTLTag extends BodyTagSupport {

	/** Variable name to store the results in */
	protected String var;

	/** Scope to store results in */
	protected String scope;

	/** Default to page scope */
	private static final String DEFAULT_SCOPE = "page";

	/** Debug/error logger */
	private static Logger logger = Logger.getLogger(BaseJSTLTag.class);

	/** Default empty constructor */
	public BaseJSTLTag() {
		// Default scope is page
		scope = DEFAULT_SCOPE;
	}

	/**
	 * This method will store a value into the appropriate var/scope
	 * 
	 * @param value
	 *            The value to store into var/scope
	 */
	protected void storeValue(Object value) {
		if (value == null)
			pageContext.removeAttribute(var, getScope(scope));
		else
			pageContext.setAttribute(var, value, getScope(scope));
	}

	/**
	 * Evaluate given expression, and return a value cast to the given type.
	 * 
	 * @param expression
	 *            a JSTL expression, passed by the JSP
	 * @param type
	 *            The class that the value should be cast to
	 * @return An object of the requested type representing the value of the
	 *         expression
	 * @throws JspException
	 *             Thrown if an error occurred evaluating the expression
	 */
	protected Object evaluate(String expression, Class type)
			throws JspException {
		if (expression == null)
			return null;
		return ExpressionEvaluatorManager.evaluate("an attribute", expression,
				type, this, pageContext);
	}

	/**
	 * Reset tag state. Children tags should call this method from their own
	 * release method.
	 */
	public void release() {
		super.release();

		var = null;
		scope = DEFAULT_SCOPE;
	}

	/**
	 * Set the scope to store the results in. Default is 'page' scope
	 * 
	 * @param scope
	 *            A valid scope name ('page', 'request', 'session'). Should not
	 *            be a JSTL expression itself
	 */
	public void setScope(String scope) {
		this.scope = scope;
	}

	/**
	 * The name to store the results under.
	 * 
	 * @param var
	 *            The name of the attribute to store the results under. Should
	 *            not be a JSTL expression itself
	 */
	public void setVar(String var) {
		this.var = var;
	}

	protected int getScope(String scopeName) {
		if ("request".equals(scopeName))
			return PageContext.REQUEST_SCOPE;
		if ("page".equals(scopeName))
			return PageContext.PAGE_SCOPE;
		if ("session".equals(scopeName))
			return PageContext.SESSION_SCOPE;
		if ("application".equals(scopeName))
			return PageContext.APPLICATION_SCOPE;

		Exception ex = new Exception();
		logger.info("Attempting to set variable to " + " unknown scope: '"
				+ scope + "'.  Will use page " + "scope", ex);
		return PageContext.PAGE_SCOPE;
	}
}