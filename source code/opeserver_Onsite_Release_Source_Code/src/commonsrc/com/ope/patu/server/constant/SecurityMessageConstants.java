package com.ope.patu.server.constant;

public interface SecurityMessageConstants 
{
	public static final String MESSAGE_VALIDATION_PASS_CODE = "1001";
	public static final String VERSION_ERROR_CODE = "1012";
	public static final String INVALID_KEK_ERROR_CODE = "1013";
	public static final String INVALID_AUK_ERROR_CODE = "1014";
	public static final String OLD_DATE_ERROR_CODE = "1015";
	public static final String EARLY_DATE_ERROR_CODE = "1016";
	public static final String OLD_HSK_ERROR_CODE = "1017";
	public static final String OLD_TIME_STAMP_ERROR_CODE = "1018";
	public static final String INVALID_HASH_VALUE_ERROR_CODE = "1019";
	public static final String INVALID_AUTHENTICATION_ERROR_CODE = "1020";
	public static final String INVALID_RECEIVER_ID_ERROR_CODE = "1021";
	public static final String SENDER_ID_ERROR_CODE = "1025";
	public static final String CONTENTS_LENGTH_ERROR_CODE = "1034";
	public static final String SYSTEM_ERROR_CODE = "1034";
	/*--------------------------------------------------------------------*/
	public static final String ACCEPTED_CODE = "1001";
	public static final String ACCEPTED_KEY_EXCHANGE_CODE = "1002";
	public static final String KEY_EXCHANGE_ACCEPTED_CODE = "1003";
	public static final String KEY_EXCHANGE_REJECTED_CODE = "1004";
	public static final String KEY_EXCHANGE_TOO_OFTEN_CODE = "1005";
	public static final String NNN_VVV_SYNTAX_ERROR_CODE = "1010";
	public static final String NNN_VVV_INVALID_VALUE_ERROR_CODE = "1011";
	public static final String INVALID_TIME_STAMP_ERROR_CODE = "1022";
	public static final String MISSING_SUO_MESSAGE_ERROR_CODE = "1023";
	public static final String MISSING_VAR_MESSAGE_ERROR_CODE = "1024";
	public static final String MISSING_PTE_MESSAGE_ERROR_CODE = "1029";
	public static final String AUK_PARITY_ERROR_CODE = "1030";
	public static final String HSK_PARITY_ERROR_CODE = "1031";
	public static final String SHORT_SECURITY_MESSAGE_CODE = "1032";
	public static final String INVALID_CUSTOMERID_ERROR_CODE = "1033";
	public static final String NO_PATU_AGREEMENT_ERROR_CODE = "1035";
	
	public static final String ACCEPTANCE_CODE_YES = "K";
	public static final String ACCEPTANCE_CODE_NO = "E";

	public static final String ANNOUNCEMENT_CODE_R001 = "R001";
	
	public static final String  SECURITY_VERSION = "security_version";
	public static final String  MIN_VERSION_VALUE = "min_version_value";
	public static final String  MAX_VERSION_VALUE = "max_version_value";
	public static final String  ERRORCODE = "errorcode";
	public static final String  ERRORMSG  = "errormsg";
	public static final String  STATUS = "status";
	public static final String  TOO_EARLY = "TOO EARLY";
	public static final String  TOO_OLD = "TOO OLD";
	public static final String  SECRETKEY_DES = "DES";
	public static final String  BANK_TAXNO = "bank_taxno";
	public static final String  LANGUAGE = "en";
	
	public static final String  DATA_MSG = "DATA_MSG";
	public static final String  INCOMING= "IN";
	public static final String  SUO_MSG= "SUO_MSG";
	public static final String  VAR_MSG= "VAR_MSG";
	public static final String  VAR= "VAR";
	public static final String  PTE_MSG = ">>PTE";
	public static final String  OUTGOING = "OUT";
	public static final String  PTE_TEXT_FILE = "PTE.txt";
	public static final String  APPEND_317 = "317";
	public static final String  SMH = "SMH";
	public static final String  APPEND_LENGTH = "SMH";
	public static final String  ERROR_MESSAGE = "FATAL ERROR IN THE MESSAGE";
	public static final String  ESIP_FILE = "ESIp.txt";
	public static final String  ERROR_MSG = "FATAL ERROR IN THE MESSAGE";
	public static final String  ESP_LENGTH = "237";
	public static final String  AUTH_ERROR = "Authentication values mismatch";
	public static final String  DES = "DES";
	public static final String  ONE = "1";
	public static final String  ERROR_REPLY_MSG = "SERVER ERROR DUE TO FAILED VALIDATION";
	public static final String  SERVICEBUREAUID = "serviceBureauId"; 
	public static final String  SET_ANNOUNCEMENT_CODE =  "R023";
	public static final String  MISSING_SUO_MESSAGE =  "MISSING SUO MESSAGE";
	
	public static final String  ESI_MSG = ">>ESI";
	public static final String  ESI = "ESI";
	public static final String  SIIRTOPYYNTO = "SIIRTOPYYNTO";
	public static final String  MSG_SUO = ">>SUO";
	public static final String  SUO = "SUO";
	public static final String  ANNOUNCEMENT_CODE_VAR = "R024";
	public static final String  MISSING_VAR_ERROR = "MISSING VAR MESSAGE";
	public static final String  VALIDATOR_CLASS =  "com.ope.patu.messages.";
	public static final String  MESSAGE_VALIDATOR =  "MessageValidator";
	
/*	 	1001= ACCEPTED
		1002= ACCEPTED, KEY EXCHANGE
		1003= ACCEPTED, KEY EXCHANGE ACCEPTED 
		1004= ACCEPTED, KEY EXCHANGE REJECTED 
		1005= ACCEPTED, KEY EXCHANGE TOO OFTEN 
		1010= SYNTAX ERROR IN THE FIELD NNN VVV 
		1011=INVALID VALUE IN THE FIELD NNN VVV 
		1020=INVALID AUTHENTICATION VALUE
		1022=INVALID TIME STAMP IN ESI-MESSAGE
		1023=MISSING SUO MESSAGE
		1024=MISSING VAR MESSAGE
		1025=AGREEMENT NOT EXISTING
		1029=MISSING PTE MESSAGE
		1030=PARITY ERROR IN AUK
		1031=PARITY ERROR IN HSK
		1032=SHORT SECURITY MESSAGE
		1033=INVALID CUSTOMER ID
		1035=NO PATU AGREEMENT
		------------------------------------------------------
		1026=FIELD NNN: IN SUO MESSAGE <> IN VAR MESSAGE
		1027=FIELD NNN: IN SUO MESSAGE <> IN PTE MESSAGE
		1028=HASH VALUE: IN VAR MESSAGE <> IN PTE MESSAGE
		1036=ACCEPTED, CHANGE-OVER PERIOD CUT 
		1037=ACCEPTED, CHANGE-OVER PERIOD CUTTING REJECTED
*/
	
}