package com.dnb.common.session;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import javax.servlet.http.HttpSession;


import com.dnb.common.commons.CommonConstants;
import com.dnb.common.DAO.LoginDAO;
import com.dnb.common.DAO.DAODNBCommon;



import java.util.List;

public class SessionTimeoutHandler implements HttpSessionListener {

private static Logger logger = Logger.getLogger(SessionTimeoutHandler.class);

private static int activeSessions = 0;

/**
* @see javax.servlet.http.HttpSessionListener#void (javax.servlet.http.HttpSessionEvent)
*/
public void sessionCreated(HttpSessionEvent arg0) {
activeSessions++;
}
/**
* @see javax.servlet.http.HttpSessionListener#void (javax.servlet.http.HttpSessionEvent)
*/
public void sessionDestroyed(HttpSessionEvent arg0){
		HttpSession statementSession = arg0.getSession();
		
		SessionDataHandler sdh = SessionDataHandler.getSessionDataHandler(statementSession);
		try
		{
			DAODNBCommon statement = DAODNBCommon.getDAODNB(1);
		
			LoginDAO lDAO = statement.getLoginDAO();
			boolean logout = lDAO.logoutUser(sdh.getCurrentUser());
			if(logout)
				logger.debug("User Session Expired");
		}
		catch(Exception se)
		{
			logger.debug("Could not logout from Statement");
		}
		statementSession.removeAttribute(CommonConstants.SessionKey);
	if (activeSessions>0){
		activeSessions--;		
	}
}

public static int getActiveSessions() {	
	return activeSessions;
}

}