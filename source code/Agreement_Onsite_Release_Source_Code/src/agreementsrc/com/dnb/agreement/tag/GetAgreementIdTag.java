/********************************************************************************************
* Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                        *
* IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.       *
* The Program or any portion thereof may not be reproduced in any form whatsoever except    *
* as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd. *
*																							*
* File Name                   : GetAgreementIdTag.java                                      *
* Author                      : Anantaraj S                                                 *
* Creation Date               : 06-Aug-2008                                                 *
* Description                 : This file gives auto generated Agreement Id                 *
*                                                                                           *
* Modification History        :                                                             *
*																						    *
*     Version No.               Date               Brief description of change              *
*  ---------------------------------------------------------------------------------------  *
*                       |                  |											    *
*                       |                  |											    *
*  ---------------------------------------------------------------------------------------  *
*********************************************************************************************/
package com.dnb.agreement.tag;


import org.apache.log4j.Logger;
import oracle.jdbc.OracleConnection;

import javax.servlet.jsp.JspException;
import java.sql.Connection;
import com.dnb.agreement.utility.SQLUtils;
import com.dnb.agreement.sequence.SequenceDataDAO;
import com.dnb.common.commons.CommonConstants;



/**
 * Tag hander class which gets department data
 *
 */
public class GetAgreementIdTag extends BaseJSTLTag {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
     * The logging mechanism to be used throughout this class
     */
    static Logger logger = Logger.getLogger(GetAgreementIdTag.class);
    /**
     * Once retrieved, the data will be placed in the scope specified with this name
     */
    private String var;
    /**
     * The scope in which to store the data - eg page or request
     */
    private String scope = "page";

    
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
		String entity = "DNB";
		String pack = "ope_util";
				
		//getting connection from datasource
		try
		{
		conn = SQLUtils.getConnection(CommonConstants.DNBDatabase);
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
