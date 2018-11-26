package com.dnb.agreement.tag;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;

import java.util.Properties;
import javax.servlet.jsp.JspException;

import org.apache.commons.collections.SequencedHashMap;
import org.apache.log4j.Logger;
import com.dnb.common.utility.SQLUtils;
import com.dnb.agreement.refdata.ReferenceDataDAO;

/**
 * Tag hander class
 */
public class GetDropDownTag extends BaseJSTLTag {
	/**
	 * The logging mechanism to be used throughout this class
	 */
	static Logger logger = Logger.getLogger(GetDropDownTag.class);

	/**
	 * Once retrieved, the data will be placed in the scope specified with this
	 * name
	 */
	private String var;

	/**
	 * The scope in which to store the data - eg page or request
	 */
	private String scope = "page";

	/**
	 * Key specifies the query to perform on the database
	 */
	private String key;

	/**
	 * Module
	 */
	private String module;

	/**
	 * @param value
	 *            the value to check for null value
	 * @return boolean
	 */
	private boolean checkNull(String value) {
		return ((value == null) || (value.length() == 0));
	}

	/**
	 * @see javax.servlet.jsp.tagext.Tag#doEndTag()
	 */
	public int doEndTag() throws JspException {
		return SKIP_BODY;
	}

	/**
	 * @see javax.servlet.jsp.tagext.Tag#doStartTag()
	 */
	public int doStartTag() throws JspException {
		Connection conn = null;
		String moduleVal = null;		
		
		// var and key are mandatory
		String varVal = (String) evaluate(var, String.class);
		String keyVal = (String) evaluate(key, String.class);
		moduleVal = (String) evaluate(module, String.class);

		int scopeVal = super.getScope(scope);

		SequencedHashMap dataMap = null;
		String sqlQuery = new String();

		Properties props = new Properties();
		
		try {
			InputStream in = null;
			if (moduleVal == null) {
				in = this.getClass().getClassLoader().getResourceAsStream(
						"databaseproperties/agreementdatabasekey.properties");
			}
			props.load(in);
			in.close();
		} catch (IOException e) {
			logger.debug("Could not find property file");
		}
		
		sqlQuery = (String) props.getProperty(keyVal);

		// Get the connection from the datasource
		try {
			conn = SQLUtils.getConnection("DNBDatabase");

			// Execute the query
			ReferenceDataDAO refDataDAO = new ReferenceDataDAO();
			dataMap = refDataDAO.getReferenceData(sqlQuery, conn);

		} finally {
			SQLUtils.ensureClosed(conn);
		}

		// Put it into the context
		if (dataMap != null)
			pageContext.setAttribute(varVal, dataMap, scopeVal);

		else
			pageContext.removeAttribute(varVal, scopeVal);

		return EVAL_PAGE;
	}

	/**
	 * Returns the key.
	 * 
	 * @return String
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Returns the scope.
	 * 
	 * @return String
	 */
	public String getScope() {
		return scope;
	}

	/**
	 * Returns the var.
	 * 
	 * @return String
	 */
	public String getVar() {
		return var;
	}

	/**
	 * Sets the key.
	 * 
	 * @param key
	 *            The key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * Sets the scope.
	 * 
	 * @param scope
	 *            The scope to set
	 */
	public void setScope(String scope) {
		this.scope = scope;
	}

	/**
	 * Sets the var.
	 * 
	 * @param var
	 *            The var to set
	 */
	public void setVar(String var) {
		this.var = var;
	}

	/**
	 * @see javax.servlet.jsp.tagext.Tag#release()
	 */
	public void release() {
		var = null;

		scope = null;
		key = null;
		module = null;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}
}