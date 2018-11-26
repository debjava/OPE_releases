package com.ope.patu.payments.ts;

/**
 * Generic class for salary payment service error constraints
 * @author anandkumar.b
 *
 */
public class SalaryErrorConstants {

	public static String REJECTED = "rejected";
	public static String TRANS_ACC_NO = "Invalid account number";
	public static String INVALID_REC_CODE = "Invalid record code";
	public static String REASON_CODE = "Invalid reason code";
	public static String INVALID_DATE ="Invalid due date";
	public static String INVALID_REASON_PMT = "Invalid reason for payment";
	public static String AMOUNT = "Invalid transaction amount";
	public static String PAYEES_IDENTITY_NO = "Invalid payees identity no";
	public static String PAYMENT_DATE = "Rejected file, Invalid payment date";
	public static String ACC_NOT_EXIST ="Account number formally incorrect";
	public static String SERVICE_CODE = "Invalid service code";
	public static String PAYMENT_REASON = "Rejected file, Invalid reason for payments";
	public static String INVALID_SUM_PAYMENTS = "Rejected,Invalid sum of payments";
	public static String INVALID_NO_OF_PAYMENTS = "Rejected, Invalid number of payments";
	public static String INVALID_SUM_DATE = "Rejected file, Invalid date in sum record";
	public static String INVALID_SERVICE_CODE = "Rejected file, Invalid service code";
	public static String FILE_REJECTED = "Rejected file, all transactions are failed";
	public static String ACC_NO = "00000000000";
	public static String AMOUNT_NUM = "00000000000";
	public static String PAYEES_IDENTITY = "00000000000";
	public static String ERRORS = "Correct the rejected transactions and send them for re-processing.";
	public static String  NEW_LINE = "\n";
	public static String  REMARK = "Accepted transactions go on for further processing.";
	public static String ZERO = "0";
}
