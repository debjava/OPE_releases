package com.ope.patu.payments.lum2;

import org.apache.log4j.Logger;

import com.ope.patu.payment.core.Parser;
import com.ope.patu.payments.lum2.beans.LUM2PaymentRecordBean;

public class LUM2PaymentRecordParser implements Parser
{
	protected static Logger logger = Logger.getLogger(LUM2PaymentRecordParser.class);
	
	int seq_no=0;
	public Object parse( Object ... objects )
	{
		seq_no++;
		//String decimal_amount="";
		LUM2PaymentRecordBean paymentRecordBean = new LUM2PaymentRecordBean();
		String subdata = ( String )objects[0];
		try {
			
			paymentRecordBean.setLineLength(subdata.length());
			//logger.debug("--> CBPPaymentRecordParser :: setLineLength ::"+subdata.length());
			
			//1
			paymentRecordBean.setApplicationCode(subdata.substring(0,4));
			//logger.debug("--> CBPPaymentRecordParser :: setApplicationCode ::"+subdata.substring(0,4));
			//2
			paymentRecordBean.setRecordCode(subdata.substring(4,5));
			//logger.debug("--> CBPPaymentRecordParser :: setRecordCode ::"+subdata.substring(4,5));
			//3
			paymentRecordBean.setAcceptanceCode(subdata.substring(5,6));
			//logger.debug("--> CBPPaymentRecordParser :: setAcceptanceCode ::"+subdata.substring(5,6));
			//4			
			paymentRecordBean.setBeneficiaryNameAddress(subdata.substring(6,146));
			//logger.debug("--> CBPPaymentRecordParser :: setBeneficiaryNameAddress ::"+subdata.substring(6,146));
			//5
			paymentRecordBean.setBeneficiaryCountryCode(subdata.substring(146,148));
			//logger.debug("--> CBPPaymentRecordParser :: setBeneficiaryCountryCode ::"+subdata.substring(146,148));
			//6
			paymentRecordBean.setSwiftCode(subdata.substring(148,159));
			//logger.debug("--> CBPPaymentRecordParser :: setSwiftCode ::"+subdata.substring(148,159));
			//7
			paymentRecordBean.setBeneficiaryBankNameAddress(subdata.substring(159,299));
			logger.debug("--> CBPPaymentRecordParser :: setBeneficiaryBankNameAddress ::"+subdata.substring(159,299));
			//8
			paymentRecordBean.setBeneficiaryAccountCode(subdata.substring(299,333));
			//logger.debug("--> CBPPaymentRecordParser :: setBeneficiaryAccountCode ::"+subdata.substring(299,333));
			//9
			paymentRecordBean.setReasonPaymentInformation(subdata.substring(333,473));
			//logger.debug("--> CBPPaymentRecordParser :: setReasonPaymentInformation ::"+subdata.substring(333,473));
			//10
			paymentRecordBean.setCurrencyAmount(subdata.substring(473,488));
			//logger.debug("--> CBPPaymentRecordParser :: setCurrencyAmount ::"+subdata.substring(473,488));
			//11
			paymentRecordBean.setCurrencyCode(subdata.substring(488,491));
			logger.debug("--> CBPPaymentRecordParser :: setCurrencyCode ::"+subdata.substring(488,491));
			//12
			paymentRecordBean.setReservedWord1(subdata.substring(491,494));
			//logger.debug("--> CBPPaymentRecordParser :: setReservedWord1 ::"+subdata.substring(491,494));
			//13
			paymentRecordBean.setExchangeRateAgmtNo(subdata.substring(494,508));
			//logger.debug("--> CBPPaymentRecordParser :: setExchangeRateAgmtNo ::"+subdata.substring(494,508));
			//14
			paymentRecordBean.setPaymentType(subdata.substring(508,509));
			//logger.debug("--> CBPPaymentRecordParser :: setPaymentType ::"+subdata.substring(508,509));
			//15
			paymentRecordBean.setServiceFee(subdata.substring(509,510));
			//logger.debug("--> CBPPaymentRecordParser :: setServiceFee ::"+subdata.substring(509,510));
			//16
			paymentRecordBean.setDebitDate(subdata.substring(510,518));
			logger.debug("--> CBPPaymentRecordParser :: setDebitDate ::"+subdata.substring(510,518));
			//17
			paymentRecordBean.setCounterValuePayment(subdata.substring(518,533));
			//logger.debug("--> CBPPaymentRecordParser :: setCounterValuePayment ::"+subdata.substring(518,533));
			//18
			paymentRecordBean.setExchangeRatePayment(subdata.substring(533,544));
			//logger.debug("--> CBPPaymentRecordParser :: setExchangeRatePayment ::"+subdata.substring(533,544));
			//19
			paymentRecordBean.setDebitingAccount(subdata.substring(544,558));
			logger.debug("--> CBPPaymentRecordParser :: setDebitingAccount ::"+subdata.substring(544,558));
			//20
			paymentRecordBean.setDebitingAccountCurrencyCode(subdata.substring(558,561));
			//logger.debug("--> CBPPaymentRecordParser :: setDebitingAccountCurrencyCode ::"+subdata.substring(558,561));
			//21
			paymentRecordBean.setDebitingAmount(subdata.substring(561,576));
			logger.debug("--> CBPPaymentRecordParser :: setDebitingAmount ::"+subdata.substring(561,576));
			//22			
			paymentRecordBean.setArchiveId(subdata.substring(577,596));
			//logger.debug("--> CBPPaymentRecordParser :: setArchiveId ::"+subdata.substring(577,596));
			//23
			paymentRecordBean.setFeedbackCurrency(subdata.substring(596,597));
			//logger.debug("--> CBPPaymentRecordParser :: setFeedbackCurrency ::"+subdata.substring(596,597));
			//24
			paymentRecordBean.setReservedWord2(subdata.substring(597,600));
			//logger.debug("--> CBPPaymentRecordParser :: setReservedWord2 ::"+subdata.substring(597,600));
			
		}catch(StringIndexOutOfBoundsException siobe){
			logger.debug("StringIndexOutOfBoundsException :: CBPPaymentRecordParser :: " + siobe.getMessage());
		}catch(Exception ee){
			logger.debug("Exception :: CBPPaymentRecordParser :: " + ee.getMessage());
		}
		return paymentRecordBean;
	}
}