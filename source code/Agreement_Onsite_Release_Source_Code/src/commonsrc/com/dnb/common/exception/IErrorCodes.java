/********************************************************************************************
 * Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                       *
 * IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.      *
 * The Program or any portion thereof may not be reproduced in any form whatsoever except   *
 * as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd.*
 *																							*
 * File Name                   : IErrorCodes.java                                           *
 * Author                      : Chenchi Reddy                                                *
 * Creation Date               : 24-Jun-2008                                               *
 * Description                 :                                                            *
 * Modification History        :                                                            *
 *																						    *
 *     Version No.               Date               Brief description of change             *
 *  --------------------------------------------------------------------------------------- *
 *                       |                  |											    *
 *                       |                  |											    *
 *  --------------------------------------------------------------------------------------- *
 ********************************************************************************************/
package com.dnb.common.exception;

/**
 * This class holds all error codes that can be raised withim ProcessMate
 */
public class IErrorCodes {

	
	/**
	 * ENTERPRISE ERROR CODES
	 */
	
	/** Error Code if any unknown exception is thrown */
	public static final String DEFAULT_ERROR = "ERRPM-001";

	/** Error Code for Data Source Access Failure */
	public static final String CONNECTION_RETRIEVAL_ERROR = "STSYS-001";

	/** Error Code if user session is lost */
	public static final String SESSION_LOST_ERROR = "STSYS-011";

	/** Error Code to denote Naming Exception on Data Source Access */
	public static final String NAMING_LOOKUP_ERROR = "STSYS-002";
	
	/** Error Code for Data Source Access Failure */
	public static final String INITIALIZATION_ERROR = "STSYS-003";
	
	/** Error Code for creation of Descriptors */
	public static final String DB_DESCRIPTOR_ERROR = "STSYS-004";
		
	/**
	 * LOGIN ERROR CODES
	 */
	/** Error Code to check user Login */
	public static final String LOGIN_CHECK_ERROR = "STBYS-004";

	/** Error Code to validate user Login */
	public static final String LOGIN_VALIDATE_ERROR = "STBYS-005";

	/** Error Code to Update User Pwd */
	public static final String LOGIN_UPDATE_ERROR = "STSYS-011";

	/** Error Code to change password */
	public static final String LOGIN_CHANGEPWD_ERROR = "STBYS-006";
	
	/** Error Code for new password */
	public static final String LOGIN_NEW_PASSWORD_ERROR = "STBYS-006";

	/** Error Code for confirm password */
	public static final String LOGIN_CONFIRM_PASSWORD_ERROR = "STBYS-007";

	/** Error Code for check matching for new pwd and and confirm password */
	public static final String LOGIN_NC_PASSWORD_ERROR = "STBYS-008";

	/** Error Code to confirm change password */
	public static final String LOGIN_CHANGE_PWD_SUCCESS_ERROR = "STBYS-009";

	/** Error Code  for change pwd failed */
	public static final String LOGIN_CHANGE_PWD_FAIL_ERROR = "STBYS-0010";

	/** Error Code  for user doesn't exists */
	public static final String LOGIN_USER_NOTEXISTS_ERROR = "STBYS-011";

	/** Error Code  for update login error */
	public static final String LOGIN_UPDATE_LOGIN_ERROR = "STBYS-012";

	/** Error Code  for : could't get rights */
	public static final String LOGIN_RIGHTS_ERROR = "STBYS-013";

	/** Error Code for new or old password */
	public static final String LOGIN_OLD_NEW_PASSWORD_ERROR = "STBYS-014";

	/** Error Code for date conversion */
	public static final String DATE_CONVERSION_ERROR = "STBYS-014";
	
	/** Error Code for date conversion */
	public static final String LOGIN_MAKELIST_ERROR = "STBYS-014";
	
	
	
	
	
	
	/**  For Request */
	
	/** Error Code for Insert */
	public static final String REQUEST_INSERT_ERROR = "SYBYS-001";

	/** Error Code for Fetching records */
	public static final String REQUEST_FETCH_ERROR = "SYSYS-005";
	
	
	
	
	
	
	
	
	
	
  
}