package com.ope.patu.payments.lmp300;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.ope.patu.server.constant.ServerConstants;
/**
 * This is for Bill Payment Service constatns
 * @author anandkumar.b
 */
public class PaymentConstants {

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
	}

	public static String BILL_PAYMENT_SERVICE = "Bill Payment Service";
	public static String TRANSMISSION_FEEDBACK = "Transmission feedback";
	public static String BATCH = "batch";
	public static String TRANSACTIONS = "transaction";
	public static String ITEMISATION = "itemisation";
	public static String SUM = "sum";
	public static String MESSAGETRANS = "messagetrans";
	public static String MESSAGEITEM = "messageitem";
	public static String INVALIDRECORD = "invalidrecord";
	public static String BATCH_FAILED = "batch_failed";
	public static String SUM_FAILED = "sum_failed";
	public static String CURRENCY_CODE = "EUR ";//"euro";
	public static String DECIMAL = "0.00";
	public static String DATA_CODE_LM03 = "LM03";
	public static String DATA_CODE_LM02 = "LM02";
	public static String PATU_OUT_FILE = "LMP300_U";
	public static String FEEDBACK_LMPPAL = "U_LMPPAL";
	public static String U_KUITTAUS = "U_KUITTAUS";
	public static String SENDERSCODE="SENDERSCODE";
	public static String FILECREATIONDATE="FILECREATIONDATE";
	public static String PAYERS_NAME="PAYERS_NAME";
	public static String PAYERS_ACCOUNT_NO = "PAYERS_ACCOUNT_NO";
	public static String PAYERS_CODE = "PAYERS_CODE";
	public static String PAYERS_DUE_DATE = "PAYERS_DUE_DATE";
	public static String SEQ_NO = "SEQ_NO";
	public static String PAYERS_ACC_NO = "PAYERS_ACC_NO";
	public static String PAYERS_SUM = "PAYERS_SUM";
	public static String PAYERS_REF_NO = "PAYERS_REF_NO";
	public static String ERRORS_MSG = "ERRORS_MSG";
	public static String MATERIAL_RECEIVED_ = "MATERIAL_RECEIVED";
	public static String SENDERS_CODE = "senders_code";
	public static String FILE_CREATION_DATE = "file_creation_date";
	public static String FILE_CREATION_TIME = "file_creation_time";
	public static String PAYRS_NAME = "payrs_name";
	public static String ACCOUNT_NO = "account_no";
	public static String CODE = "code";
	public static String DUE_DATE = "due_date";
	public static String REJECTED = "rejected";
	public static String SENDERS_CODE_HEADER = "Sender's code";
	public static String FILE_CREATION_TIME_HEADER = "File creation time";
	public static String PAYERS_NAME_HEADER = "Payer's name";
	public static String ACCOUNT_HEADER = "Account";
	public static String CODE_HEADER = "Code";
	public static String DUE_DATE_HEADER = "Due date";
	public static String NUMBER_HEADER = "Number";
	public static String PAYEES_NAME_HEADER = "Payee's name";
	public static String ACCOUNT_HEADER_TRANS = "Account";
	public static String AMOUNT_HEADER_TRANS = "Amount";
	public static String FAULTY_BILL_PAYMENT_TRANSACTIONS = "Faulty bill payment transactions";
	public static String REFRANCE = "Reference" ;

	public static String FAILED_RECORDS = "failed_records";
	public static String SUCCESS_RECORDS = "success_records";
	public static String ITEM_FAILED = "item_failed";
	public static String ERRORS = "Please correct the errors and send corrected bills again";

	public static String ACCEPTED_TRANS = "Accepted transactions in the material go on for further processing";
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

	public static String  DECIMAL_FORMAT = "0000000000000000";
	public static String  NO_FORMAT = "000000";
	public static String  STR_ACCEPTED_SUM = "0000000000.00";
	public static String  DEC_FORMAT = "0000000000.00";
	public static String  SUM_FORMAT = "0000000000000";
	public static String  ACK_NO_FORMAT = "0000000000000000";

	public static String  ACCEPTED_VAL = "000";
	public static String  DEC_FMT = "000";
	public static String  DATE_FMT = "yy.MM.dd";

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

	public static String  FB_BILL_PAYMENT_SERVICE = "BILL_PAYMENT_SERVICE";
	public static String  FB_TRANSMISSION_FEEDBACK = "TRANSMISSION_FEEDBACK";
	public static String  FB_SENDERS_CODE_HEADER = "SENDERS_CODE_HEADER";
	public static String  FB_FILE_CREATION_TIME_HEADER = "FILE_CREATION_TIME_HEADER";
	public static String  FB_SENDERSCODE = "FB_SENDERS_CODE";
	public static String  FB_FILECREATIONDATE = "FILECREATIONDATE";
	public static String  FB_PAYERS_NAME_HEADER = "PAYERS_NAME_HEADER";
	public static String  FB_ACCOUNT_HEADER = "ACCOUNT_HEADER";
	public static String  FB_CODE_HEADER = "CODE_HEADER";
	public static String  FB_DUE_DATE_HEADER = "DUE_DATE_HEADER";
	public static String  FB_PAYERS_NAME = "PAYERS_NAME";
	public static String  FB_PAYERS_ACCOUNT_NO = "PAYERS_ACCOUNT_NO";
	public static String  FB_PAYERS_CODE = "FB_PAYERS_CODE";
	public static String  FB_PAYERS_DUE_DATE = "PAYERS_DUE_DATE";
	public static String  FB_FAULTY_BILL_PAYMENT_TRANSACTIONS = "FAULTY_BILL_PAYMENT_TRANSACTIONS";
	public static String  FB_NUMBER_HEADER = "NUMBER_HEADER";
	public static String  FB_PAYEES_NAME_HEADER = "PAYEES_NAME_HEADER";
	public static String  FB_ACCOUNT_HEADER_TRANS = "ACCOUNT_HEADER_TRANS";
	public static String  FB_AMOUNT_HEADER_TRANS = "AMOUNT_HEADER_TRANS";
	public static String  FB_REFRANCE = "REFERENCE";
	public static String  FB_SEQ_NO = "SEQ_NO";
	public static String  FB_PAYERS_ACC_NO = "PAYERS_ACC_NO";
	public static String  FB_PAYERS_SUM = "PAYERS_SUM";
	public static String  FB_PAYERS_REF_NO = "PAYERS_REF_NO";
	public static String  FB_ERRORS_MSG = "ERRORS_MSG";
	public static String  FB_BATCH_LIST = "BATCH_LIST";
	public static String  FB_TRANS_LIST = "TRANS_LIST";
	public static String  FB_SUM_LIST = "SUM_LIST";
	public static String  FB_ERRORS = "ERRORS";
	public static String  FB_ACCEPTED_TRANS = "ACCEPTED_TRANS";
	public static String  FB_MATERIAL_RECEIVED = "MATERIAL_RECEIVED";
	public static String  FB_ACCEPTED_TRANSATIONS = "ACCEPTED_TRANSATIONS";
	public static String  FB_ALTOGETHER_1 = "ALTOGETHER_1";
	public static String  FB_ACCEPTED_VAL = "ACCEPTED_VAL";
	public static String  FB_ACCEPTED_AMT = "ACCEPTED_AMT";
	public static String  FB_CURRENCY_EUR_1 = "CURRENCY_EUR_1";
	public static String  FB_REJECTED_TRANSATIONS = "REJECTED_TRANSATIONS";
	public static String  FB_ALTOGETHER_2 = "ALTOGETHER_2";
	public static String  FB_REJECTED_VAL = "REJECTED_VAL";
	public static String  FB_REJECTED_AMT = "REJECTED_AMT";
	public static String  FB_CURRENCY_EUR_2 = "CURRENCY_EUR_2";
	public static String  FB_BPS = "BPS";
	public static String  SIX_ZERO = "000000";
	public static String  THREE_ZERO = "000";
	public static String  SIXTEEN_ZERO = "0000000000000000";
	public static String  TOTAL_RECORDS = "total_Records";
	public static String  VALID_RECORD_COUNT = "Valid_Record_Count";
	public static String  INVALID_RECORD_COUNT = "Invalid_Record_count";
	public static String  ACK = "ACK";
	public static String  BPS = "BPS";
	public static String  SPS = "SPS";
	public static String  BillPayemntsFeedbackTemplate = "BillPayemntsFeedbackTemplate.vm";
	public static String  PaymentsAcknowledgement = "PaymentsAcknowledgement.vm";
	public static String  SalaryPayemntsFeedbackTemplate = "SalaryPayemntsFeedbackTemplate.vm";
	public static String  CONFIG_FILE = confLocation;
	public static String  VELOCITY = "velocity";
	public static String  DOUBLE_VALUE = "0.00";

	public static String  ACK_RETURN_CODE_ERROR = "001";
	public static String  ACK_RETURN_CODE_SUCCESS = "000";
	public static String  ERROR_MESSAGE_R = "File rejected";
	public static String  FILE_STATUS_F = "F";
	public static String  FILE_STATUS_S = "S";
	public static String  ERRORMESSAGE_A = "File accepted";	
	public static String  ACK_CBP = "ACK_CBP";

	public static String TRANS_ERR = "File rejected, all trasactions are failed.";
	public static String ALL_TRANS_FAILED = "File rejected, all transactions are failed.";
	public static String  TWO_ZERO = "00";
	public static String  ERR = "ERR";
	public static String  RETURN_CODE_ERR = "RETURN_CODE";
	public static String  NOTIFICATION_ERR = "NOTIFICATION_ERR";
	public static String  SEQ_COUNT = "SEQ_COUNT";
	public static String  ACCEPTED_TRANS_ERR = "ACCEPTED_TRANS";
	public static String  YY_MM = "yy.MM";
	public static String  HH_MM = "hh:mm";
	public static String  FILE_PATH = "FILE_PATH";

	public static String  RECORD_CODE_ERR = "IN VALID RECORD CODE ,";
	public static String  RECORD_LENGTH_ERR = "Record length is less then 80";
	public static String  TRANS_50_ERR = "More then 50 transactions are failed.";

	public static String RETURN_CODE_1 = "001";
	public static String RETURN_CODE_2 = "002";
	public static String RETURN_CODE_3 = "003";
	public static String RETURN_CODE_4 = "004";
	public static String RETURN_CODE_5 = "005";
	public static String RETURN_CODE_6 = "006";
	public static String RETURN_CODE_7 = "007";
	public static String RETURN_CODE_8 = "008";
	public static String RETURN_CODE_9 = "009";
}
