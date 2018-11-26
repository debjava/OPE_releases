package com.ope.patu.payments.lum2;

import org.apache.log4j.Logger;

import com.ope.patu.payment.core.Parser;
import com.ope.patu.payments.lum2.beans.LUM2SupplementaryFeedbackRecordBean;

public class LUM2SupplementaryFeedbackParser implements Parser
{
	protected static Logger logger = Logger.getLogger(LUM2SupplementaryFeedbackParser.class);
	
	public Object parse( Object ... objects )
	{
		LUM2SupplementaryFeedbackRecordBean supplementaryFeedbackBean = new LUM2SupplementaryFeedbackRecordBean();
		String subdata = ( String )objects[0];
		try{
			supplementaryFeedbackBean.setLineLength(subdata.length());
			//logger.debug("--> CBPSupplementaryFeedbackParser :: setLineLength ::"+subdata.length());
			//1
			supplementaryFeedbackBean.setApplicationCode(subdata.substring(0,4));
			//logger.debug("--> CBPSupplementaryFeedbackParser :: setApplicationCode ::"+subdata.substring(0,4));
			//2
			supplementaryFeedbackBean.setRecordCode(subdata.substring(4,5));
			//logger.debug("--> CBPSupplementaryFeedbackParser :: setRecordCode ::"+subdata.substring(4,5));
			//3
			supplementaryFeedbackBean.setAcceptanceCode(subdata.substring(5,6));
			//logger.debug("--> CBPSupplementaryFeedbackParser :: setAcceptanceCode ::"+subdata.substring(5,6));
			//4
			supplementaryFeedbackBean.setBeneficiaryBankSwiftCode(subdata.substring(6,17));
			//logger.debug("--> CBPSupplementaryFeedbackParser :: setBeneficiaryBankSwiftCode ::"+subdata.substring(6,17));
			//5
			supplementaryFeedbackBean.setBeneficiaryBank(subdata.substring(17,157));
			//logger.debug("--> CBPSupplementaryFeedbackParser :: setBeneficiaryBank ::"+subdata.substring(17,157));
			//6
			supplementaryFeedbackBean.setBeneficiaryBankAccountNumber(subdata.substring(157,191));
			logger.debug("--> CBPSupplementaryFeedbackParser :: setBeneficiaryBankAccountNumber ::"+subdata.substring(157,191));
			//7
			supplementaryFeedbackBean.setIntermediaryBankSwiftCode(subdata.substring(191,202));
			//logger.debug("--> CBPSupplementaryFeedbackParser :: setIntermediaryBankSwiftCode ::"+subdata.substring(191,202));
			//8
			supplementaryFeedbackBean.setIntermediaryBankNameAddress(subdata.substring(202,342));
			//logger.debug("--> CBPSupplementaryFeedbackParser :: setIntermediaryBankNameAddress ::"+subdata.substring(202,342));
			//9
			supplementaryFeedbackBean.setIntermediaryBankAccountNumber(subdata.substring(342,376));
			//logger.debug("--> CBPSupplementaryFeedbackParser :: setIntermediaryBankAccountNumber ::"+subdata.substring(342,376));
			//10
			supplementaryFeedbackBean.setCustomerInformation(subdata.substring(376,586));
			//logger.debug("--> CBPSupplementaryFeedbackParser :: setCustomerInformation ::"+subdata.substring(376,586));
			//11
			supplementaryFeedbackBean.setReservedWord1(subdata.substring(586,600));
			//logger.debug("--> CBPSupplementaryFeedbackParser :: setReservedWord1 ::"+subdata.substring(586,600));
		}catch(StringIndexOutOfBoundsException siob){
			logger.debug("StringIndexOutOfBoundsException :: CBPSupplementaryFeedbackParser :: " + siob.getMessage());
		}catch(Exception ee){
			logger.debug("Exception :: CBPSupplementaryFeedbackParser :: " + ee.getMessage());
		}
		return supplementaryFeedbackBean;
	}
}
