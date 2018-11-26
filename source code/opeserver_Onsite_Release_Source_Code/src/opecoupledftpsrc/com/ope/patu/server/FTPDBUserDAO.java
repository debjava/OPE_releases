/*********************************************************************************************
 * Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                        *
 * IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.       *
 * The Program or any portion thereof may not be reproduced in any form whatsoever except    *
 * as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd. *
 *																							 *
 * File Name                   : FTPDBUserDAO.java                                 	 	     *
 * Author                      : Debadatta Mishra                                            *
 * Creation Date               : July 18, 2008                                               *
 * Modification History        :                											 *
 *																						     *
 *     Version No.               Date               Brief description of change              *
 *  ---------------------------------------------------------------------------------------  *
 *                       |      			|				 								 *
 *                       |                  |											 	 *
 *  ---------------------------------------------------------------------------------------  *
 *********************************************************************************************/

package com.ope.patu.server;

import java.util.Map;

import org.apache.log4j.Logger;

import com.coldcore.coloradoftp.plugin.gateway.User;
import com.coldcore.coloradoftp.plugin.gateway.dao.BaseUserDAO;
import com.ope.patu.security.AbstractSecurity;
import com.ope.patu.security.EncryptionUtility;
import com.ope.patu.security.OPESecurity;
import com.ope.patu.security.Security;
import com.ope.patu.server.constant.ServerConstants;
import com.ope.patu.server.db.AbstractDAOFactory;
import com.ope.patu.server.db.ServerDAO;

/**This class is used for User information
 * @author <a href:mailTo=Debadatta Mishra</a>
 *
 */
public class FTPDBUserDAO extends BaseUserDAO {
	protected static Logger logger = Logger.getLogger(FTPDBUserDAO.class);
	/**
	 * Object of type {@link ServerDAO}
	 */
	private static ServerDAO serverDAO = null;
	/**
	 * Variable of type String indicating the file name
	 * Here the file name refers to the database config .
	 */
	private String configFileName = null;

	/**
	 * @param filename of type String inidicating the
	 * database config file.
	 */
	public FTPDBUserDAO(String filename) {
		super();
		this.configFileName = filename;
	}

	/* (non-Javadoc)
	 * @see com.coldcore.coloradoftp.plugin.gateway.dao.UserDAO#getUser(java.lang.String)
	 */
	public User getUser(String username) throws Exception {
		User user = null;
		try {
			serverDAO = AbstractDAOFactory.getDAOFactory(
					ServerConstants.DB_NAME, configFileName).getServerDAO();
			Map<String, String> userInfoMap = serverDAO.getUserInfo(username);
			
			String enPassword = userInfoMap.get("password");
			EncryptionUtility enu = new EncryptionUtility();
			logger.debug("Encrypted password----------"+enPassword);
			String decryptedPwd = enu.decrypt(enPassword);
			logger.debug("Decrypted Pasword--------->>>"+decryptedPwd);
			user = constructUser(userInfoMap.get("userName"), decryptedPwd , "SU");
			
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		return user;
	}

}
