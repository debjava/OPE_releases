package com.ope.patu.payments.lum2;

public class LUM2Constants {

	public static String RECORD_LENGTH_SUCCESS = "Record length is correct";
	public static String RECORD_LENGTH_ERROR = "Invalid record length OR Invalid field length";
	public static String APPLICATION_CODE_SUCCESS = "Application code is correct";
	public static String APPLICATION_CODE_ERROR = "Invalid application code";
	public static String RECORD_CODE_SUCCESS = "Record code is correct";
	public static String RECORD_CODE_ERROR = "Invalid record code";
	public static String FILE_TYPE_FILE = "file";
	public static String FILE_TYPE_FEEDBACK = "feedback";
	public static String FILE_TYPE_ERROR = "Invalid file type in batch record";
	public static String DUE_DATE_SUCCESS = "Due date is correct";
	public static String DUE_DATE_ERROR = "Invalid due date for batch";
	public static String BENEFICIARY_NAME_ADDRESS_ERROR = "Beneficiary name and address can not be blank";
	public static String BENEFICIARY_NAME_ADDRESS_SUCCESS = "Beneficiary name and address are correct";
	public static String BENEFICIARY_COUNTRY_CODE_ERROR = "Invalid country code";
	public static String BENEFICIARY_COUNTRY_CODE_SUCCESS = "Beneficiary country code is correct";
	public static String SWIFT_CODE_ERROR = "Invalid SWIFT code";
	public static String SWIFT_CODE_SUCCESS = "SWIFT code is correct";
	public static String PAYERS_BUSINESS_IDENTITY_CODE_ERROR = "Invalid payers business identity code";
	public static String PAYERS_BUSINESS_IDENTITY_CODE_SUCCESS = "Payers business identity code is correct";

	// PAYERS_BUSINESS_IDENTITY_CODE_SUCCESS

	public static String CURRENCY_AMOUNT_ERROR = "Invalid amount";
	public static String CURRENCY_AMOUNT_SUCCESS = "Currency amount is correct";
	public static String PAYMENT_TYPE_ERROR = "Invalid payment type";
	public static String PAYMENT_TYPE_SUCCESS = "Payment type is correct";
	public static String SERVICE_FEE_ERROR = "Invalid service fee";
	public static String SERVICE_FEE_SUCCESS = "Service fee is correct";
	public static String DEBITING_AMOUNT_ERROR = "Invalid amount";
	public static String DEBITING_AMOUNT_SUCCESS = "Currency amount is correct";
	public static String CURRENCY_CODE_ERROR = "Invalid currency code";
	public static String CURRENCY_CODE_SUCCESS = "Currency code is correct";
	public static String EURO_IS0_CODE = "1";
	public static String FEEDBACK_CURRENCY_ERROR = "Invalid feedback currency";
	public static String FEEDBACK_CURRENCY_SUCCESS = "Feedback currency is correct";
	public static String ACCOUNT_NUMBER_ERROR = "Invalid account number";
	public static String ACCOUNT_NUMBER_SUCCESS = "Account number is correct";
	public static String INVOICE_TYPE_ERROR = "Invalid invoice type";
	public static String INVOICE_TYPE_SUCCESS = "Invoice type is correct";
	public static String NUMBER_OF_PAYMENT_ERROR = "Number of payments are invalid";
	public static String NUMBER_OF_PAYMENT_SUCCESS = "Number of payments are correct";
	public static String SUM_OF_PAYMENT_AMOUNT_ERROR = "Sum record does not have the equal amount of payments";
	public static String SUM_OF_PAYMENT_AMOUNT_SUCCESS = "Sum record have the equal amount of payments";
	public static String BENEFICIARY_BANK_NAME_ADDRESS_ERROR = "Beneficiary name and address can not be blank";
	public static String BENEFICIARY_BANK_NAME_ADDRESS_SUCCESS = "Beneficiary name and address are correct";
	public static String IBAN_ACCOUNT_NUMBER_ERROR = "Invalid IBAN number";
	public static String IBAN_ACCOUNT_NUMBER_SUCCESS = "IBAN number is correct";
	public static String PATU_OUT_FILE = "LUM2_U";
	public static String U_KUITTAUS = "U_KUITTAUS";
	public static String ACK = "ACK_CBP";
	public static int BATCH_SIZE_ONE = 1;
	public static String SIGN_PLUS = "+";
	public static String SIGN_MINUS = "-";
	public static String FILE_TYPE_LUM2 = "LUM2";
	public static String BILL_PAYMENT_SERVICE = "Bill Payment Service";
	public static String TRANSMISSION_FEEDBACK = "Transmission feedback";
	
	public static String BATCH = "batch";
	public static String PAYMENT = "payment";
	public static String INVOICE = "invoice";
	public static String SUM = "sum";
	public static String SUPPLEMENTARY_PAYMENT = "supplementary_payment";
	public static String SUPPLEMENTARY_FEEDBACK = "supplementary_feedback";

	public static String MESSAGETRANS = "messagetrans";
	public static String MESSAGEITEM = "messageitem";

	public static String BATCH_FAILED = "batch_failed";
	public static String SUM_FAILED = "sum_failed";
	public static String CURRENCY_CODE = "euro";
	public static String APPLICATION_CODE_LUM2 = "LUM2";
	public static String APPLICATION_CODE_VLU2 = "VLU2";
	public static String SENDERS_CODE = "senders_code";
	public static String FILE_CREATION_DATE = "file_creation_date";
	public static String FILE_CREATION_TIME = "file_creation_time";
	public static String PAYRS_NAME = "payrs_name";
	public static String ACCOUNT_NO = "account_no";
	public static String CODE = "code";
	public static String DUE_DATE = "due_date";
	public static String REJECTED = "rejected";
	public static String FILE_CREATION_TIME_HEADER = "File creation time";
	public static String PAYERS_NAME_HEADER = "Payers name";
	public static String ACCOUNT_HEADER = "Account";
	public static String CODE_HEADER = "Code";
	public static String DUE_DATE_HEADER = "Due date";
	public static String NUMBER_HEADER = "Number";
	public static String PAYEES_NAME_HEADER = "Payee's name";
	public static String ACCOUNT_HEADER_TRANS = "Account";
	public static String AMOUNT_HEADER_TRANS = "Amount";
	public static String FAILED_RECORDS = "failed_records";
	public static String SUCCESS_RECORDS = "success_records";
	public static String ITEM_FAILED = "item_failed";
	public static String ERRORS = "Please correct the errors and send corrected bills again";
	public static String ACCEPTED_TRANS = "Accepted transactions in the material go on for further processing.";
	public static String MATERIAL_RECEIVED = "Material received";
	public static String ACCEPTED_TRANSATIONS = "Accepted transations";
	public static String ALTOGETHER = "altogether";
	public static String CURRENCY_EUR = "EUR";
	public static String EURO = "EURO";
	public static String REJECTED_TRANSATIONS = "Rejected transations";
	public static String CURRENCY = "currency";
	public static String BATCHES = "batches";
	
	public static String ZERO = "0";
	public static String ONE = "1";
	public static String TWO = "2";
	public static String THREE = "3";
	public static String FOUR = "4";
	public static String FIVE = "5";
	public static String SIX = "6";
	public static String SEVEN = "7";
	public static String EIGHT = "8";
	public static String NINE = "9";
	
	public static String NEW_LINE = "\n";
	
	public static String STATUS_S = "S";
	public static String STATUS_F = "F";
	public static String STATUS_L = "L";
	public static String STATUS_P = "P";
	
	public static String L = "L";
	public static String P = "P";

	public static String STATUS_M = "M";
	public static String STATUS_Q = "Q";
	public static String STATUS_T = "T";
	public static String STATUS_K = "K";
	public static String STATUS_U = "U";
	public static String STATUS_J = "J";

	public static int LINE_LENGTH = 600;

	public static String ACK_FILE_TYPE = "FILE_TYPE";
	public static String ACK_CURRENCY = "CURRENCY";
	public static String ACK_BATCHES = "BATCHES";
	public static String ACK_NO_OF_TRANSACTIONS = "NO_OF_TRANSACTIONS";
	public static String ACK_SUM_OF_TRANSACTIONS = "SUM_OF_TRANSACTIONS";
	public static String ACK_TRANSACTIONS_SIGN = "TRANSACTIONS_SIGN";
	public static String ACK_NO_OF_CREDITS = "NO_OF_CREDITS";
	public static String ACK_SUM_OF_CREDITS = "SUM_OF_CREDITS";
	public static String ACK_CREDITS_SIGN = "CREDITS_SIGN";
	public static String ACK_DATE = "DATE";
	public static String ACK_TIME = "TIME";
	public static String ACK_RETURN_CODE = "RETURN_CODE";
	public static String ACK_NO_OF_NOTIFICATION_LINE = "NO_OF_NOTIFICATION_LINE";
	public static String ACK_NOTIFICATION_LINE = "NOTIFICATION_LINE";
	public static String ACK_FILE_CONTENT = "ACK_FILE_CONTENT";

}
