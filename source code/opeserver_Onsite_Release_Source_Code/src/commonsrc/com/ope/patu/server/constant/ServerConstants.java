/*********************************************************************************************
 * Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                        *
 * IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.       *
 * The Program or any portion thereof may not be reproduced in any form whatsoever except    *
 * as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd. *
 *																							 *
 * File Name                   : ServerConstants.java                                 	 	 *
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

package com.ope.patu.server.constant;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**This interface is used for constants for server specific
 * information.
 * @author Debadatta Mishra
 *
 */


public class ServerConstants 
{
	static String confLocation = null;
	
	static {
		InputStream is = null;

		is = ServerConstants.class.getResourceAsStream("/ope-setup.properties");
	
		Map returnedMap = new HashMap();

		Properties prop = new Properties();
		
		try {
			prop.load(is);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		prop.load(new FileInputStream("ope-setup.properties"));
		
		confLocation = prop.getProperty("ope.conf.location");
		System.out.println(":::: confLocation =>"+confLocation);
	}
//	public static final String WELCOMEMESSAGE = "****************************" +
//			"******\n Welcome to PATU FTP Protocol Server\n ******" +
//			"*******************************";
	public static final String WELCOMEMESSAGE = "PATU FTP Service";
	public static final int DB_NAME = 1;// For oracle
	
	public static final String CONFIG_DIR = confLocation;
	public static final String USER_INFO_QUERY = "select * from OPE_USER_INFO" +
			" where USER_ID=? and STATUS='A' " +
			"and VERSION_NO=(SELECT MAX(VERSION_NO) from  OPE_USER_INFO WHERE USER_ID=?)";
	public static final String INSERT_AUDIT_LOG = "INSERT INTO ope_audit_log_tab" +
			" (customer_name,file_name,file_type,file_date)" +
			" VALUES ( ?,?,?,?)";
		
		
	public static final String DB_PROPERTY = "db.properties";
	public static final String NORMAL_SECURITY = "normal";
	public static final String ASYMMETRIC_SECURITY = "ASYMMETRIC";
	
	/**
	 * It gives the file storage home directory
	 */
	public static final String FILE_HOME_DIR = System.getProperty("user.dir")
			+ File.separator + "home";
	public static final String DATE_FORMAT = "yyMMddhhmmss";
	public static final String ESI_MSG_STATUS = "ESI_MSG_STATUS";
	public static final String TR_MSG_STATUS = "TR_MSG_STATUS";
	public static final String ACTUAL_DATA_LOCATION = "ACTUAL_DATA_LOCATION";
	public static final String TR_STATUS = "TR_STATUS";
	public static final String AGREEMENTTID = "AGMTID";
	public static final String SERVICECODE = "SERVICECODE";
	public static final String SERVICEID = "SERVICEID";
	public static final String PAYERS_BUSINESS_ID_CODE = "PAYERS_BUSINESS_ID_CODE";
	public static final String SERVICEBUREAUID = "SERVICEBUREAUID";
	public static final String SUO_MSG_STATUS = "SUO_MSG_STATUS";
	public static final String VAR_MSG_STATUS = "VAR_MSG_STATUS";
	/**
	 * For incoming message from customer to bank
	 */
	public static final String ESIA_MSG_BEAN = "ESIA_MSG_BEAN";
	/**
	 * For message after validation
	 */
	public static final String ESIP_MSG_BEAN = "ESIP_MSG_BEAN";
	/**
	 * For incoming message from customer to bank
	 */
	public static final String SUOA_MSG_BEAN = "SUOA_MSG_BEAN";
	/**
	 * For message after validation
	 */
	public static final String SUOP_MSG_BEAN = "SUOP_MSG_BEAN";
	/**
	 * For incoming message from customer to bank
	 */
	public static final String VARA_MSG_BEAN = "VARP_MSG_BEAN";
	public static final String PTE_MSG = "PTE_MSG";
	/**
	 * For message after validation
	 */
	public static final String VARP_MSG_BEAN = "VARP_MSG_BEAN";
	public static final String PROTECTED_MSG_BEAN = "PROTECTED_MSG_BEAN";
	public static final String AGMTSERVICECODEPATH = "AgmtServicePath";
	public static final String TR_OBJECT = "TRANSFER_REQUEST_OBJECT";
	public static final String REPLYMSG = "REPLY_MSG";
	public static final String PATUID = "PATIUD";
	public static final String TRANSACTION_MAP = "TRANSACTION_MAP";
	public static final String KEK_PART1 = "KEK1";
	public static final String KEK_PART2 = "KEK2";
	public static final String KEK = "KEK";
	public static final String AUK = "AUK";
	public static final String ERROR_CODE = "error_code";
	public static final String ERROR_MSG = "error_msg";
	public static final String PROTECTION_STATUS = "protection_status";
	
	public static final String ESIP_PATH = "ESIP_PATH";
	public static final String PTE_PATH = "PTE_PATH";
	public static final String ESIP_MSG = "ESIP_MSG";
	public static final String ESIP_FLAG = "ESIP_FLAG";
	
	public static final String ESI_MSG_CONTENTS = "ESI_MSG_CONTENTS";
	
	public static final String NEW_AUK = "NEW_AUK";
	public static final String NEW_AUK_GEN_NO = "NEW_AUK_GEN_NO";
	public static final String KEY_MAP = "KEY_MAP";
	public static final String ACKN_PATH = "ACKN_PATH";
	public static final String LANGUAGE = "LANGUAGE";
	public static final String LANGUAGE_PROP = "LANGUAGE_PROP";
	public static final String BANK_SOFTWARE = "MERITA-SOLO 1.20";
	public static final String NEW_KEY_CHANGE = "1";
	public static final String UNSUPPORTED = "Command not supported";
	public static final String SERVERCONFIG = "serverconfig.properties";
	public static final String SERVERPORT = "PATU_PORT" ;
	public static final String STATUS_YES = "YES";
	public static final String STATUS_NO = "NO";
	public static final String TR_FILE_STATUS = "TR_FILE_STATUS";
	public static final String STATUS_U = "U";
	public static final String STATUS_P = "P";
	public static final String STATUS_M = "M";
	public static final String STATUS_R = "R";
	public static final String STATUS_O = "O";
	public static final String STATUS_F = "F";
	public static final int DEFAULT_MERGE_CONSTANT = 9;
	public static final int REDO_MERGE_CONSTANT = 8;
	public static final String MERGED_FILE_PATH = "MERGED_FILE_PATH";
	public static final String MERGED_FILE_LIST = "MERGED_FILE_LIST";
	public static final String MERGED_FILE_CONTENT = "MERGED_FILE_CONTENT";
	public static final String MERGED_FILE_NAME = "MERGED_FILE_NAME";
	public static final String MERGED_FILE_FTP_PATH = "MERGED_FILE_FTP_PATH";
	
	public static String  TOTAL_RECORDS = "total_Records";
	public static String  VALID_RECORD_COUNT = "Valid_Record_Count";
	public static String  INVALID_RECORD_COUNT = "Invalid_Record_count";
	
	public static String  PAYMENT_FILE_ERROR_CODE = "Payment_File_Error_Code";
	public static String  PAYMENT_FILE_ERROR_MESSAGE = "Payment_File_Error_Message";
	public static String  PAYMENT_FILE_STATUS = "Payment_File_status";
	public static String  FILE_STATUS_ACCEPTED = "Accepted";
	public static String  FILE_STATUS_REJECTED = "Rejected";	
	
	public static final String DATA_DIR_NAME = "data";
	public static final String HOME_DIR_NAME = "home";
	public static final String TEMP_DIR_NAME = "temp";
	public static final String FEEDBACK_MSG = "FEEDBACK_MSG";
	public static final String FEEDBACK_PATH = "FEEDBACK_PATH";
	
	/**
	 * Added flag for key exchange process
	 */
	public static final String FLAG = "FLAG";
	
	public static String  OPE_SHARED_LOCATION_ROOT = "ope.shared.location.root";
	public static String  RETRIEVAL_STATUS = "RETRIEVAL_STATUS";
	public static String  FILE_TYPE = "FILE_TYPE";
	
	
}
