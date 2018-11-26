package com.ope.patu.payments.ts;

/**
 * Generic class for salary payment service constraints
 * @author anandkumar.b
 *
 */
public class SalaryConstants {
	
	public static String SALARY_PAYMENT_SERVICE = "Salaries and pensions";
	public static String TRANSMISSION_FEEDBACK = "Transmission feedback";
	public static String FB_SALARY_PAYMENT_SERVICE = "FB_SALARY_PAYMENT_SERVICE";
	public static String FB_TRANSMISSION_FEEDBACK = "FB_TRANSMISSION_FEEDBACK";
	public static String  DATE_FMT_IN = "yyMMdd";
	public static String  DATE_FMT = "yy.MM.dd";
	public static String SAL_TRANS = "salary_trasaction";
	public static String SAL_SUM = "salary_sum";
	public static String SUM_FAILED = "sum_failed";
	public static String CURRENCY_CODE = "EUR";
	public static String REASON_CODE_10 = "10";
	public static String REASON_CODE_15 = "15";
	public static String REASON_CODE_17 = "17";
	public static String REASON_CODE_18 = "18";
	public static String REASON_CODE_19 = "19";
	public static String REASON_CODE_20 = "20";
	public static String REASON_CODE_30 = "30";
	public static String REASON_CODE_31 = "31";
	public static String REASON_CODE_34 = "34";
	public static String REASON_CODE_36 = "36";
	public static String REASON_CODE_50 = "50";
	public static String REASON_CODE_51 = "51";
	public static String REASON_CODE_55 = "55";
	public static String REASON_CODE_56 = "56";
	public static String REASON_CODE_59 = "59";
	public static String REASON_CODE_60 = "60";
	public static String REASON_CODE_61 = "61";
	public static String REASON_CODE_62 = "62";
	public static String REASON_CODE_63 = "63";
	public static String REASON_CODE_64 = "64";
	public static String REASON_CODE_68 = "68";
	public static String REASON_CODE_90 = "90";
		
	public static String SENDERS_CODE = "SENDERS_CODE";
	public static String FILE_CREATION_DATE = "file_creation_date";
	public static String FILE_CREATION_TIME = "file_creation_time";
	public static String PAYRS_NAME = "payrs_name";
	public static String ACCOUNT_NO = "account_no";
	public static String CODE = "code";
	public static String DUE_DATE = "due_date";
	public static String RECORD_LENGTH_REJECTED = "File rejected,Invalid reocrd length";
	public static String RECORD_CODE_REJECTED = "File rejected,Invalid record code";
	public static String REJECTED="rejected";
	public static String FB_SENDERS_CODE_HEADER = "FB_SENDERS_CODE_HEADER";
	public static String FB_CUSTOMER_DATA_HEADER = "FB_CUSTOMER_DATA_HEADER";
	public static String SENDERS_CODE_HEADER = "Sender's code";
	public static String CUSTOMER_DATA_HEADER = "Customer data";
	
	public static String FB_PAYERS_NAME_HEADER = "FB_PAYERS_NAME_HEADER";
	public static String PAYERS_NAME_HEADER = "Payer's name";
	public static String FB_ACCOUNT_HEADER = "FB_ACCOUNT_HEADER";
	public static String ACCOUNT_HEADER = "Account";
	public static String FB_CODE_HEADER = "FB_CODE_HEADER";
	public static String CODE_HEADER = "Payer's code";
	public static String FB_PAYMENT_DATE_HEADER = "FB_PAYMENT_DATE_HEADER";
	public static String PAYMENT_DATE_HEADER = "Payment date";
	
	public static String FB_NUMBER_HEADER = "FB_NUMBER_HEADER";
	public static String FB_PAYEES_NAME_HEADER = "FB_PAYEES_NAME_HEADER";
	public static String FB_ACCOUNT_HEADER_TRANS = "FB_ACCOUNT_HEADER_TRANS";
	public static String FB_AMOUNT_HEADER_TRANS = "FB_AMOUNT_HEADER_TRANS";
	public static String FB_REJECTED_TRANSACTIONS = "FB_REJECTED_TRANSACTIONS";
	public static String FB_ID_NUMBER_HEADER = "FB_ID_NUMBER_HEADER";
	
	public static String NUMBER_HEADER = "Number";
	public static String PAYEES_NAME_HEADER = "Payee's name";
	public static String ACCOUNT_HEADER_TRANS = "Account";
	public static String AMOUNT_HEADER_TRANS = "Amount";
	public static String REJECTED_TRANSACTIONS_FB = "REJECTED_TRANSACTIONS_FB";
	public static String REJECTED_TRANSACTIONS = "Rejected transactions";
	public static String ID_NUMBER_HEADER = "ID Number" ;
	
	public static String FAILED_RECORDS = "failed_records";
	public static String SUCCESS_RECORDS = "success_records";
	public static String ITEM_FAILED = "item_failed";
	public static String ERRORS = "Please correct the rejected transactions and send them for re-processing";
	public static String FB_ERRORS = "FB_ERRORS";
	public static String ACCEPTED_TRANS = "Accepted transactions in the material go on for further processing.";
	public static String FB_MATERIAL_RECEIVED = "FB_MATERIAL_RECEIVED";
	public static String MATERIAL_RECEIVED = "Material received";
	public static String ACCEPTED_TRANSATIONS = "Accepted transactions";
	
	public static String ALTOGETHER = "altogether";
	public static String CURRENCY_EUR = "EUR";
	public static String REJECTED_TRANSATIONS = "Rejected transactions";
	public static String  FILE_TYPE = "file_type";
	public static String  CURRENCY = "currency";
	public static String  BATCHES = "batches";
	public static String  NO_OF_TRANS = "no_of_trans";
	public static String  SUM_TRANS = "sum_trans";
	public static String  NET_AMT_SIGN = "net_amt_sign";
	public static String  NO_OF_CREDIT_ENTRIES = "no_of_credit_entries";
	public static String  TOT_SUM_OF_CREDIT_ENTRIES = "tot_sum_of_credit_entries";
	public static String  TOT_SUM_CRDT_ENTR = "tot_sum_crdt_entr";
	public static String  DATE = "date";
	public static String  TIME = "time";
	public static String  RETURN_CODE = "return_code";
	public static String  NO_OF_NOTFI_LINES = "no_of_notfi_lines";
	public static String  NOTIFICATION_LINE = "notification_line";
	public static String  SALARY_FEEDBACK_FILE = "U_TSPAL";
	public static String  ACK_FILE = "U_KUITTAUS";
	public static String  PATU_FILE = "TS_U";
	public static String  ACK = "ACK";
	public static String  ERR = "ERR";
	public static String  FB_SPS = "SPS";
	public static String  DECIMAL_FORMAT = "0000000000000000";
	public static String  NO_FORMAT = "000000";
	public static String  TWO_ZERO = "00";
	public static String  STR_ACCEPTED_SUM = "0000000000.00";
	public static String  DEC_FORMAT = "0000000000.00";    
	public static String  DEC_FMT = "000";
	public static String  EUR_FMT = "00,0";
	public static String  BATCH_DEF = "001";
	public static String  FOUR_ZERO = "0000";	
	public static String  SERVICE_CODE = "service_code";
	public static String  PAYMENT_DATE = "payment_date";
	public static String  REASON_FOR_PAYMENT = "reason_for_payment";
	public static String  ACK_ZERO = "00000000000000.00";		
	public static String  ZERO = "0";
	public static String  ONE = "1";
	public static String  TWO = "2";
	public static String  THREE = "3";
	public static String  FOUR = "4";
	public static String  FIVE = "5";
	public static String  SIX = "6";
	public static String  SEVEN = "7";
	public static String  EIGHT = "8";
	public static String  NINE = "9";
	public static String  NEW_LINE = "\n";
	public static String  STATUS_S = "S";
	public static String  STATUS_F = "F";
	public static String  THREE_ZERO = "000";
	public static String  DECIEMAL_ZERO = "0.00";
	
	//--------------------------------------------
	public static String CUSTOMER_DATA = "CUSTOMER_DATA";
	
	/*public static String SENDERS_CODE = "";
	public static String PAYERS_NAME_HEADER ="";
	public static String ACCOUNT_HEADER = "";
	public static String CODE_HEADER ="";
	public static String PAYMENT_DATE_HEADER ="";*/
	
	public static String PAYERS_NAME = "PAYERS_NAME";
	public static String PAYERS_ACCOUNT_NO = "PAYERS_ACCOUNT_NO";
	public static String PAYERS_CODE = "PAYERS_CODE";
	public static String PAYERS_DUE_DATE = "PAYERS_DUE_DATE";
	
	public static String SEQ_NO= "SEQ_NO";
	public static String PAYEES_NAME= "PAYEES_NAME";
	public static String PAYERS_ACC_NO= "PAYERS_ACC_NO";
	public static String AMOUNT= "AMOUNT";
	public static String ID_NUMBER = "ID_NUMBER";
	
	public static String ERRORS_MSG = "ERRORS_MSG";
	public static String TRANS_SUM = "TRANS_SUM";
	public static String TRANS_ERR = "TRANS_ERR";
	public static String TRANS_LIST = "TRANS_LIST";
	public static String ALTOGETHER_1 = "ALTOGETHER_1";
	public static String ACCEPTED_TRANSATIONS_HEADER = "ACCEPTED_TRANSATIONS";
	public static String REJECTED_TRANSATIONS_HEADER ="REJECTED_TRANSATIONS";
	public static String ACCEPTED_VALUES = "ACCEPTED_VALUES";
	public static String ACCEPTED_VAL = "ACCEPTED_VAL";
	public static String CURRENCY_EUR_1 = "CURRENCY_EUR_1";
	public static String ALTOGETHER_2 = "ALTOGETHER_2";
	public static String REJECTED_VAL = "REJECTED_VAL";
	public static String REJECTED_VALUES = "REJECTED_VALUES";
	public static String CURRENCY_EUR_2 = "CURRENCY_EUR_2";
	public static String REMARK = "REMARK";
	public static String SPS = "SPS";
	
	public static String  SIX_ZERO = "000000";
	public static String  SIXTEEN_ZERO = "0000000000000000";
	
	public static String  ACK_RETURN_CODE_ERROR = "001";
	public static String  ACK_RETURN_CODE_SUCCESS = "000";
	public static String  ERROR_MESSAGE_R = "File rejected";
	public static String  FILE_STATUS_F = "F";
	public static String  FILE_STATUS_S = "S";
	public static String  ERRORMESSAGE_A = "File accepted";	
	
	public static String  RECORD_ID = "RECORD_ID";
	public static String  BLANK_1 = "BLANK_1";
	public static String  DATE_ERR = "DATE";
	public static String  BLANK_2 = "BLANK_2";
	public static String  TIME_ERR = "TIME";
	public static String  BLANK_3 = "BLANK_3";
	public static String  FILE_TYPE_ERR = "FILE_TYPE";
	public static String  BLANK_4 = "BLANK_4";
	public static String  NOTIFICATION_CODE = "NOTIFICATION_CODE";
	public static String  BLANK_5 = "BLANK_5";
	public static String  NOTIFICATION_TEXT = "NOTIFICATION_TEXT";
	
	
	public static String  RETURN_CODE_ERR = "RETURN_CODE";
	public static String  NOTIFICATION_ERR = "NOTIFICATION_ERR";
	public static String  SEQ_COUNT = "SEQ_COUNT";
	public static String  ACCEPTED_TRANS_ERR = "ACCEPTED_TRANS";
	public static String  YY_MM = "yy.MM";
	public static String  HH_MM = "hh:mm";
	public static String  FILE_PATH = "FILE_PATH";
	
	public static String  RECORD_CODE_ERR = "IN VALID RECORD CODE ,";
	public static String  RECORD_LENGTH_ERR = "Record length is less then 80";
	
	public static String RETURN_CODE_1 = "001";
	public static String RETURN_CODE_2 = "002";
	public static String RETURN_CODE_3 = "003";
	public static String RETURN_CODE_4 = "004";
	public static String RETURN_CODE_5 = "005";
	public static String RETURN_CODE_6 = "006";
	public static String RETURN_CODE_7 = "007";
	public static String RETURN_CODE_8 = "008";
	public static String RETURN_CODE_9 = "009";
	
	
	//public static String ERRORS = "";
	//public static String MATERIAL_RECEIVED ="";
	
	
	
}
