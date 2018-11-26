package com.dnb.agreement.tag;

import oracle.jdbc.OracleConnection;
import org.apache.log4j.Logger;
import javax.servlet.jsp.JspException;
import java.sql.Connection;
import com.dnb.agreement.utility.SQLUtils;
import com.dnb.agreement.sequence.SequenceDataDAO;
import com.dnb.common.commons.CommonConstants;

/**
 * Tag hander class which gets Service Bureau Id * 
 * 
 */
public class GetServiceBureauIdTag extends BaseJSTLTag {
    /**
     * The logging mechanism to be used throughout this class
     */
    static Logger logger = Logger.getLogger(GetServiceBureauIdTag.class);
    /**
     * Once retrieved, the data will be placed in the scope specified with this name
     */
    private String var;
    /**
     * The scope in which to store the data - eg page or request
     */
    private String scope = "page";

    /**
     * @param value the value to check for null value
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
    public int doStartTag() throws JspException
    {
		Connection conn  = null;
		OracleConnection oraConRef = null;

		//var is mandatory
		String varVal = (String) evaluate(var, String.class);

		 int scopeVal = super.getScope(scope);

		String entityId = null;
		String entity = "SVRB";
		String pack = "ope_util";

		// Get the connection from the datasource
		try
		{
			conn = SQLUtils.getConnection(CommonConstants.DNBDatabase);
		// Execute the query
		SequenceDataDAO seqDataDAO = new SequenceDataDAO();
		entityId = seqDataDAO.getEntityId(entity, pack , conn);
		}
		finally
		{
				SQLUtils.ensureClosed(conn);
		}
		
		// Put it into the context
		if (entityId != null)
		{
			pageContext.setAttribute(varVal, entityId, scopeVal);
		}
		else
        	pageContext.removeAttribute(varVal, scopeVal);


         return EVAL_PAGE;
    }

    /**
     * Returns the scope.
     * @return String
     */
    public String getScope() {
        return scope;
    }
    /**
     * Returns the var.
     * @return String
     */
    public String getVar() {
        return var;
    }

    /**
     * Sets the scope.
     * @param scope The scope to set
     */
    public void setScope(String scope) {
        this.scope = scope;
    }
    /**
     * Sets the var.
     * @param var The var to set
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
    }
}