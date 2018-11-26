
/********************************************************************************************
 * Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                       *
 * IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.      *
 * The Program or any portion thereof may not be reproduced in any form whatsoever except   *
 * as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd.*
 *																							*
 * File Name                   : IErrorCodes.java                                           *
 * Author                      : Manjunath N G                                              *
 * Creation Date               : 24-July-2008                                               *
 * Description                 :                                                            *
 * Modification History        :                                                            *
 *																						    *
 *     Version No.               Date               Brief description of change             *
 *  --------------------------------------------------------------------------------------- *
 *                       |                  |											    *
 *                       |                  |											    *
 *  --------------------------------------------------------------------------------------- *
 ********************************************************************************************/

package com.dnb.agreement.exception;

/**
 * This class holds all error codes that can be raised withim AGREEMENT
 */
public class IErrorCodes {

	/** Error Code if any exception is thrown during copy properties */
	
	public static final String NO_SUCH_METHOD_ERROR = "ERRAG-100";

	public static final String INVOCATION_TARGET_ERROR = "ERRAG-101";

	public static final String ILLEGAL_ACCESS_ERROR = "ERRAG-102";

	/** Error Code to create object for reopening deleted entity */
	public static final String RECORD_REOPEN_ERROR = "AGSYS-012";

	/** Error Code to create object for delete */
	public static final String RECORD_CREATEOBJECT_ERROR = "AGSYS-010";
	
	
	/**
	 * DEFAULT ERROR CODES
	 */

	/** Error Code if any unknown exception is thrown */
	public static final String DEFAULT_ERROR = "ERRAG-001";

	/** Error Code for Data Source Access Failure */
	public static final String CONNECTION_RETRIEVAL_ERROR = "AGSYS-001";

	/** Error Code if user session is lost */
	public static final String SESSION_LOST_ERROR = "AGSYS-011";

	/** Error Code to denote Naming Exception on Data Source Access */
	public static final String NAMING_LOOKUP_ERROR = "AGSYS-002";

	/** Error Code for Data Source Access Failure */
	public static final String INITIALIZATION_ERROR = "AGSYS-003";

	/** Error Code for creation of Descriptors */
	public static final String DB_DESCRIPTOR_ERROR = "AGSYS-004";
	
	/** Error Code if Datatype conversion fails */
	public static final String  TYPECONVERSION_ERROR= "ERRAG-002";
	
	/** Error Code for date conversion */
	public static final String DATE_CONVERSION_ERROR = "AGSYS-013";
	
	/**
	 * AGREEMENT
	 */

	/**
	 * AGREEMENT
	 */

	// Error Codes for AGREEMENT

	/** Error Code for Insert */
	public static final String AGREEMENT_INSERT_ERROR = "AGBYS-001";

	/** Error Code for Fetching records */
	public static final String AGREEMENT_FETCH_ERROR = "AGSYS-005";

	/** Error Code for Edit */
	public static final String AGREEMENT_EDIT_ERROR = "AGBYS-002";

	/** Error Code for Hold */
	public static final String AGREEMENT_HOLD_ERROR = "AGBYS-003";

	/** Error Code show next or previous version */
	public static final String AGREEMENT_VERSION_ERROR = "AGSYS-006";

	/** Error Code for Delete */
	public static final String AGREEMENT_DELETE_ERROR = "AGSYS-007";

	/** Error Code to get new Id */
	public static final String AGREEMENT_GETID_ERROR = "AGSYS-008";

	/** Error Code to make list to display */
	public static final String AGREEMENT_MAKELIST_ERROR = "AGSYS-009";

	/** Error Code to create object for delete */
	public static final String AGREEMENT_CREATEOBJECT_ERROR = "AGSYS-010";
	
	/** Msg Code for Edit-Successful */	
	public static final String AGREEMENT_EDIT_SUCCESSFUL = "AGMSG-001";

	/** Msg Code for Hold-Successful */
	public static final String AGREEMENT_HOLD_SUCCESSFUL = "AGMSG-002";
	
	/** Msg Code for Delete-Successful */
	public static final String AGREEMENT_DELETE_SUCCESSFUL = "AGMSG-003";

	/**
	 * Decision/warning/error/info
	 */
	
	/** Error Code if Datatype conversion fails */
	
	public static final String  AGREEMENT_NAME_NULL= "DECAG-001";
	
	/**
	 * SERVICE SPECIFIC
	 */
	
	// Error codes for service specific
	
	/** Error Code for Insert */
	public static final String SERVICE_SPECIFIC_INSERT_ERROR = "AGBYS-001";

	/** Error Code for Fetching records */
	public static final String SERVICE_SPECIFIC_FETCH_ERROR = "AGSYS-005";

	/** Error Code for Edit */
	public static final String SERVICE_SPECIFIC_EDIT_ERROR = "AGBYS-002";

	/** Error Code for Hold */
	public static final String SERVICE_SPECIFIC_HOLD_ERROR = "AGBYS-003";

	/** Error Code show next or previous version */
	public static final String SERVICE_SPECIFIC_VERSION_ERROR = "AGSYS-006";

	/** Error Code for Delete */
	public static final String SERVICE_SPECIFIC_DELETE_ERROR = "AGSYS-007";

	/** Error Code to get new Id */
	public static final String SERVICE_SPECIFIC_GETID_ERROR = "AGSYS-008";

	/** Error Code to make list to display */
	public static final String SERVICE_SPECIFIC_MAKELIST_ERROR = "AGSYS-009";

	/** Error Code to create object for delete */
	public static final String SERVICE_SPECIFIC_CREATEOBJECT_ERROR = "AGSYS-010";
	
	/** Msg Code for Edit-Successful */	
	public static final String SERVICE_SPECIFIC_EDIT_SUCCESSFUL = "AGMSG-001";

	/** Msg Code for Hold-Successful */
	public static final String SERVICE_SPECIFIC_HOLD_SUCCESSFUL = "AGMSG-002";
	
	/** Msg Code for Delete-Successful */
	public static final String SERVICE_SPECIFIC_DELETE_SUCCESSFUL = "AGMSG-003";
	
	/**
	 * SERVICE BUREAU
	 */

	// Error Codes for SERVICE BUREAU

	/** Error Code for Insert */
	public static final String SERVICE_BUREAU_INSERT_ERROR = "AGBYS-001";

	/** Error Code for Fetching records */
	public static final String SERVICE_BUREAU_FETCH_ERROR = "AGSYS-005";

	/** Error Code for Edit */
	public static final String SERVICE_BUREAU_EDIT_ERROR = "AGBYS-002";

	/** Error Code for Hold */
	public static final String SERVICE_BUREAU_HOLD_ERROR = "AGBYS-003";

	/** Error Code show next or previous version */
	public static final String SERVICE_BUREAU_VERSION_ERROR = "AGSYS-006";

	/** Error Code for Delete */
	public static final String SERVICE_BUREAU_DELETE_ERROR = "AGSYS-007";

	/** Error Code to get new Id */
	public static final String SERVICE_BUREAU_GETID_ERROR = "AGSYS-008";

	/** Error Code to make list to display */
	public static final String SERVICE_BUREAU_MAKELIST_ERROR = "AGSYS-009";

	/** Error Code to create object for delete */
	public static final String SERVICE_BUREAU_CREATEOBJECT_ERROR = "AGSYS-010";
	
	/** Msg Code for Edit-Successful */	
	public static final String SERVICE_BUREAU_EDIT_SUCCESSFUL = "AGMSG-001";

	/** Msg Code for Hold-Successful */
	public static final String SERVICE_BUREAU_HOLD_SUCCESSFUL = "AGMSG-002";
	
	/** Msg Code for Delete-Successful */
	public static final String SERVICE_BUREAU_DELETE_SUCCESSFUL = "AGMSG-003";

	/**
	 * Decision/warning/error/info
	 */
	
	/** Error Code if Datatype conversion fails */
	
	public static final String  SERVICE_BUREAU_NAME_NULL= "DECAG-001";
	
	public static final String REPORT_SEARCH_ERROR = "AGSYS-005";
	
}