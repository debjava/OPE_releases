
/********************************************************************************************
* Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                        *
* IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.       *
* The Program or any portion thereof may not be reproduced in any form whatsoever except    *
* as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd. *
*																							*
* File Name                   : LoginAction.java                                            *
* Author                      : Chenchi Reddy                                               *
* Creation Date               : 25-Jun-2008                                                 *
* Description                 : This file serves as the action file which calls the DAO     *
*								for database connection and does the user Authentication    *
*								and	Authorisation.											*
* Modification History        :                                                             *
*																						    *
*   Version No.  Edited By         Date               Brief description of change              *
*  ---------------------------------------------------------------------------------------  *
*   V1.0      | Balu Narayanasamy  |  29-Aug-2006   |Default Window setter and getter to    *
*             |                    |				| redirect user prefered window         *
*  ---------------------------------------------------------------------------------------  *
*********************************************************************************************/

/** 
 * Create or import Packages 
 */

package com.dnb.common.action;

import java.lang.reflect.InvocationTargetException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import oracle.sql.DATE;
import oracle.sql.TIMESTAMP;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dnb.common.DAO.DAODNBCommon;
import com.dnb.common.DAO.LoginDAO;
import com.dnb.common.DTO.LoginDTO;
import com.dnb.common.bean.LoginBean;
import com.dnb.common.bean.UserRightsBean;
import com.dnb.common.commons.CommonConstants;
import com.dnb.common.exception.IErrorCodes;
import com.dnb.common.exception.CommonBusinessException;
import com.dnb.common.exception.CommonSystemException;
import com.dnb.common.session.SessionDataHandler;
import com.dnb.common.utility.EncryptionUtility;

public class LoginAction extends Action 
{
	boolean ent_ret; 

	static Logger logger = Logger.getLogger(LoginAction.class);

    /** 
	 * Constructor for SuccessAction.
	 */

    public LoginAction() 
	{
        super();
    }

    /**  
	 * Action Methods 
	 */

    /**
     * Returns the <code>ActionForward</code> named "success" if one is
     * configured or <code>null</code>if it cannot be found.
     *
     * Searches first for a local forward, then a global forward.
     *
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     *
     * @exception Exception if mapping.findForward throws an Exception
     *
     * @return the "success" ActionForward, or null if it cannot be found
     */
    public ActionForward execute(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws CommonSystemException,CommonBusinessException {
		String msg="failed";			
		List StatementErrorList = new ArrayList();	
		String time="";
	try
	{		
		SessionDataHandler sdh_chk = SessionDataHandler.getSessionDataHandler(request.getSession());
		if(sdh_chk != null)
		{			
			if(sdh_chk.getCurrentUser()!=null)
			{							
			msg="success";						
			return mapping.findForward(msg);
			}
		}
		EncryptionUtility eu = new EncryptionUtility();		
		/** 
		 * Get the Login Form Bean 
		 */
		LoginBean lb = (LoginBean) form; 
		LoginDTO ldto = new LoginDTO();
		PropertyUtils.copyProperties(ldto, lb);
        ldto.setPassword(eu.encrypt(ldto.getPassword()));
		/** 
		 * Create the required DAO-Factory 
		 */
		DAODNBCommon pmf = DAODNBCommon.getDAODNB(1);
		  /**  
		 * Create an LoginDAO 
		 */		
		LoginDAO lDAO = pmf.getLoginDAO();	  
		{
			boolean arr = lDAO.validateUser(ldto);
			if(arr)
			{
				List lst = lDAO.getUserDetails(ldto.getUserId());//null;//lDAO.getUserRightsAndAttributes(ldto);
				if(lst.size()>0)
				{
					UserRightsBean urb = null;
					HttpSession statementSession = request.getSession();
					try {
						 HashMap rightMap = new HashMap();
						 Iterator it = lst.iterator();
						 while (it.hasNext()) {
						   urb = (UserRightsBean)it.next();
						   BitSet bs = (BitSet) rightMap.get(urb.getEntityCode());
						   if ( bs == null ) {
						    bs = new BitSet();
						   }
						   int rightNo = -1;
						   try {
						    rightNo = Integer.parseInt(urb.getRightNo());
						   } catch(NumberFormatException nfe) {
						    // can do nothing :(
						   } finally {
						    if ( rightNo != -1 ) {
						     bs.set(rightNo);
						    }
						   }
						   rightMap.put(urb.getEntityCode(), bs);
						 }
						 statementSession.setAttribute("__USER_RIGHTS_MAP__", rightMap);
						} catch (Exception e) {
						 logger.error(e);
						 e.printStackTrace();
						}
					SessionDataHandler sdh = new SessionDataHandler();
					if(urb.getUserName()!=""&&urb.getUserName()!=null)
					{
					sdh.setUserName(urb.getUserName());
					sdh.setCurrentUser(ldto.getUserId());
					}
					else
						sdh.setUserName("");
					if(urb.getDateFormat()!=""&&urb.getDateFormat()!=null)
						sdh.setDateFormat(urb.getDateFormat());
					else
						sdh.setDateFormat("");	
					
					if(urb.getCurrentBusinessDate()!=null)
						sdh.setCurrentBusinessDate(urb.getCurrentBusinessDate());
					else
						sdh.setCurrentBusinessDate(null);
						
					if(urb.getLastLoginDate()!=null){
					
						sdh.setLastLoginDate(urb.getLastLoginDate());						
						time=sdh.getLastLoginDate();
						if(time.length() > 8)
						{
							time = time.substring(0, time.indexOf("."));
						}
						sdh.setLastLoginDate(time);					
					
					}else{
						sdh.setLastLoginDate(null);
					}
					
					if(urb.getLanguageId()!=""&&urb.getLanguageId()!=null)
					sdh.setLanguageId(urb.getLanguageId());
					else
						sdh.setLanguageId("");	
					if(urb.getDivisionId()!=""&&urb.getDivisionId()!=null)
					sdh.setDivisionId(urb.getDivisionId());
					else
						sdh.setDivisionId("");
					if(urb.getEnterpriseId()!=""&&urb.getEnterpriseId()!=null)
					sdh.setEnterpriseId(urb.getEnterpriseId());
					else
						sdh.setEnterpriseId("");	
					
					sdh.setRoleList(lst);
					
					sdh.setMyObject(statementSession,sdh);
					msg = "success";												
				}				
			}//if
		}				
		
	    /**
		 * Show Message in UI on failure 
		 */
	}
	catch(IllegalAccessException e)
	{
		if(StatementErrorList.size()==0)
			StatementErrorList.add("DEFAULT");
		logger.error("DESCRIPTION : Could not copy LoginBean LoginDTO. "+ e);
	}
	catch(InvocationTargetException e)
	{
		if(StatementErrorList.size()==0)
			StatementErrorList.add("DEFAULT");
		logger.error("DESCRIPTION : Could not copy LoginBean LoginDTO. "+ e);
	}
	catch(NoSuchMethodException e)
	{
		if(StatementErrorList.size()==0)
			StatementErrorList.add("DEFAULT");
		logger.error("DESCRIPTION : Could not copy LoginBean LoginDTO. "+ e);
	}
	catch(CommonBusinessException be)
	{
		/* if error raises due to server slowdown having ':' in error code,  ex: 17000:, 17800: etc,  it will use
		   DEFAULT error code.
		 * if error due to procedure, doesnt have any ':' char,  ex:  ERRGD-025, in this same error code will use
		   in jsp.
		*/
		if(be.getErrorCode().indexOf(":")!=-1){
			logger.debug("Default error, when server is down : "+be.getErrorCode());
			StatementErrorList.add("DEFAULT");
		}else{
			logger.error("CommonBusinessException: "+be.getErrorCode());
			StatementErrorList.add(be.getErrorCode());	
		}	
				
	}
	catch(CommonSystemException se)
	{
		StatementErrorList.add(se.getErrorCode());
		logger.error("StatementSytemException: "+se.getErrorCode());		
	}
	catch(RuntimeException re)
	{
		if(StatementErrorList.size()==0)
			StatementErrorList.add("DEFAULT");

			logger.error("RunTime Exception : "+re);
	}
	catch(Exception re)
	{
		if(StatementErrorList.size()==0)
			StatementErrorList.add("DEFAULT");
		logger.error("Exception DESCRIPTION : "+re);
		re.printStackTrace();
		
	} finally	{
		
		// If its failed, mapping.findForward to its own action
		logger.debug("msg: "+msg);
		if(msg.equals("failed")) {	
			
			request.setAttribute(CommonConstants.COMMONERRORLIST, StatementErrorList);
		}
		return mapping.findForward(msg);
}		
}

	// Converts a Oracle timestamp date to java timestamped date
  private Timestamp convertTimestampFormat(TIMESTAMP date) throws CommonBusinessException
  {
	  try{
  		TIMESTAMP DAT = ((TIMESTAMP)date);
	    Timestamp jsd = DAT.timestampValue();
		return jsd;
	   } catch (SQLException t) {
			logger.error("Could't convert oracle date to java timestamped date  : " + t);
			throw new CommonBusinessException(IErrorCodes.DATE_CONVERSION_ERROR,
				"Failed to convert oracle timestamped date to java timestamped date", t);
		}
  }

  // Converts a Oracle date to java date
  private Date convertDateFormat(DATE date)
  {
  		DATE DAT = ((DATE)date);
	    Date jsd = DAT.dateValue();
		return jsd;
  }
}
