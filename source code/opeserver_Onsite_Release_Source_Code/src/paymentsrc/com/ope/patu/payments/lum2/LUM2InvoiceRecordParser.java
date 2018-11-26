package com.ope.patu.payments.lum2;

import org.apache.log4j.Logger;

import com.ope.patu.payment.core.Parser;
import com.ope.patu.payments.lum2.beans.LUM2InvoiceRecordBean;

public class LUM2InvoiceRecordParser implements Parser
{
	protected static Logger logger = Logger.getLogger(LUM2InvoiceRecordParser.class);
	
	public Object parse( Object ... objects )
	{
		LUM2InvoiceRecordBean invoiceRecordBean = new LUM2InvoiceRecordBean();
		String subdata = ( String )objects[0];
		try{
			
			invoiceRecordBean.setLineLength(subdata.length());
			// logger.debug("--> CBPInvoiceRecordParser :: setLineLength ::"+subdata.length());
			//1
			invoiceRecordBean.setApplicationCode(subdata.substring(0,4));
			// logger.debug("--> CBPInvoiceRecordParser :: setApplicationCode ::"+subdata.substring(0,4));
			//2
			invoiceRecordBean.setRecordCode(subdata.substring(4,5));
			// logger.debug("--> CBPInvoiceRecordParser :: setRecordCode ::"+subdata.substring(4,5));
			//3
			invoiceRecordBean.setFileType(subdata.substring(5,6));
			// logger.debug("--> CBPInvoiceRecordParser :: setFileType ::"+subdata.substring(5,6));
			//4
			invoiceRecordBean.setAcceptanceCode(subdata.substring(6,7));
			// logger.debug("--> CBPInvoiceRecordParser :: setAcceptanceCode ::"+subdata.substring(6,7));
			//5
			invoiceRecordBean.setCurrencyCode(subdata.substring(7,10));
			// logger.debug("--> CBPInvoiceRecordParser :: setCurrencyCode ::"+subdata.substring(7,10));
			//6
			invoiceRecordBean.setInvoiceType(subdata.substring(10,11));
			// logger.debug("--> CBPInvoiceRecordParser :: setInvoiceType ::"+subdata.substring(10,11));
			//7
			invoiceRecordBean.setCurrencyAmount(subdata.substring(11,26));
			// logger.debug("--> CBPInvoiceRecordParser :: setCurrencyAmount ::"+subdata.substring(11,26));
			//8
			invoiceRecordBean.setPaymentRate(subdata.substring(26,37));
			// logger.debug("--> CBPInvoiceRecordParser :: setPaymentRate ::"+subdata.substring(26,37));
			//9
			invoiceRecordBean.setCounterValue(subdata.substring(37,52));
			// logger.debug("--> CBPInvoiceRecordParser :: setCounterValue ::"+subdata.substring(37,52));
			//10
			invoiceRecordBean.setInvoiceNumber(subdata.substring(52,72));
			// logger.debug("--> CBPInvoiceRecordParser :: setInvoiceNumber ::"+subdata.substring(52,72));
			//11
			invoiceRecordBean.setReservedWord1(subdata.substring(72,80));
			// logger.debug("--> CBPInvoiceRecordParser :: setReservedWord1 ::"+subdata.substring(72,80));
			//12
			invoiceRecordBean.setCustomerComments(subdata.substring(80,130));
			//logger.debug("--> CBPInvoiceRecordParser :: setCustomerComments ::"+subdata.substring(80,130));
			//13
			invoiceRecordBean.setReservedWord2(subdata.substring(130,600));
			// logger.debug("--> CBPInvoiceRecordParser :: setReservedWord4 ::"+subdata.substring(130,600));
		}catch(StringIndexOutOfBoundsException siobe){
			logger.debug("StringIndexOutOfBoundsException :: CBPInvoiceRecordParser :: " + siobe.getMessage());
		}catch(Exception ee){
			logger.debug("Exception :: CBPInvoiceRecordParser :: " + ee.getMessage());
		}
		return invoiceRecordBean;
	}
}