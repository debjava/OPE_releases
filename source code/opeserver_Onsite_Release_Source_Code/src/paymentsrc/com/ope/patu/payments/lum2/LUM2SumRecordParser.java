package com.ope.patu.payments.lum2;

import org.apache.log4j.Logger;

import com.ope.patu.payment.core.Parser;
import com.ope.patu.payments.lum2.beans.LUM2SumRecordBean;

public class LUM2SumRecordParser implements Parser
{
	protected static Logger logger = Logger.getLogger(LUM2SumRecordParser.class);
	
	public Object parse( Object ... objects )
	{
		LUM2SumRecordBean sumRecordBean = new LUM2SumRecordBean();
		String subdata = ( String )objects[0];
		try{
			sumRecordBean.setLineLength(subdata.length());
			// logger.debug("--> LUM2SumRecordParser :: setLineLength ::"+subdata.length());
			//1
			sumRecordBean.setApplicationCode(subdata.substring(0,4));
			//logger.debug("--> LUM2SumRecordParser :: setApplicationCode ::"+subdata.substring(0,4));
			//2
			sumRecordBean.setRecordCode(subdata.substring(4,5));
			//logger.debug("--> LUM2SumRecordParser :: setRecordCode ::"+subdata.substring(4,5));
			//3
			sumRecordBean.setFileType(subdata.substring(5,6));
			//logger.debug("--> LUM2SumRecordParser :: setFileType ::"+subdata.substring(5,6));
			//4
			sumRecordBean.setReservedWord1(subdata.substring(6,7));
			//logger.debug("--> LUM2SumRecordParser :: setReservedWord1 ::"+subdata.substring(6,7));
			//5
			sumRecordBean.setPayerBIC(subdata.substring(7,18));
			logger.debug("--> LUM2SumRecordParser :: setPayerBIC ::"+subdata.substring(7,18));
			//6
			sumRecordBean.setCustomerCodeExtension(subdata.substring(18,23));
			//logger.debug("--> LUM2SumRecordParser :: setCustomerCodeExtension ::"+subdata.substring(18,23));
			//7
			sumRecordBean.setReservedWord2(subdata.substring(23,25));
			//logger.debug("--> LUM2SumRecordParser :: setReservedWord2 ::"+subdata.substring(23,25));
			//8
			sumRecordBean.setAcceptanceCode(subdata.substring(25,26));
			//logger.debug("--> LUM2SumRecordParser :: setAcceptanceCode ::"+subdata.substring(25,26));
			//9
			sumRecordBean.setNumberOfPayments(subdata.substring(26,31));
			//logger.debug("--> LUM2SumRecordParser :: setNumberOfPayments ::"+subdata.substring(26,31));
			//10
			sumRecordBean.setNumberOfInvoices(subdata.substring(31,36));
			//logger.debug("--> LUM2SumRecordParser :: setNumberOfInvoices ::"+subdata.substring(31,36));
			//11
			sumRecordBean.setPaymentsCurrencyAmount(subdata.substring(36,51));
			//logger.debug("--> LUM2SumRecordParser :: setPaymentsCurrencyAmount ::"+subdata.substring(36,51));
			//12
			sumRecordBean.setReservedWord3(subdata.substring(51,600));
			//logger.debug("--> LUM2SumRecordParser :: setReservedWord3 ::"+subdata.substring(51,600));
			}catch(StringIndexOutOfBoundsException siobe){
				logger.debug("StringIndexOutOfBoundsException :: LUM2SumRecordParser :: " + siobe.getMessage());
			}catch(Exception ee){
				logger.debug("Exception :: LUM2SumRecordParser :: " + ee.getMessage());
			}
		return sumRecordBean;
	}
}