package com.ope.patu.payments.lum2;

import org.apache.log4j.Logger;

import com.ope.patu.payment.core.Parser;
import com.ope.patu.payments.lum2.beans.LUM2SupplementaryPaymentRecordBean;

public class LUM2SupplementaryPaymentParser implements Parser
{
	protected static Logger logger = Logger.getLogger(LUM2SupplementaryPaymentParser.class);
	
	public Object parse( Object ... objects )
	{
		LUM2SupplementaryPaymentRecordBean supplementaryPaymentBean = new LUM2SupplementaryPaymentRecordBean();
		String subdata = ( String )objects[0];
		try{
			supplementaryPaymentBean.setLineLength(subdata.length());
			//logger.debug("--> CBPSupplementaryPaymentParser :: setLineLength ::"+subdata.length());
			//1
			supplementaryPaymentBean.setApplicationCode(subdata.substring(0,4));
			//logger.debug("--> CBPSupplementaryPaymentParser :: setApplicationCode ::"+subdata.substring(0,4));
			//2
			supplementaryPaymentBean.setRecordCode(subdata.substring(4,5));
			//logger.debug("--> CBPSupplementaryPaymentParser :: setRecordCode ::"+subdata.substring(4,5));
			//3
			supplementaryPaymentBean.setAcceptanceCode(subdata.substring(5,6));
			//logger.debug("--> CBPSupplementaryPaymentParser :: setAcceptanceCode ::"+subdata.substring(5,6));
			//4
			supplementaryPaymentBean.setDebitRate(subdata.substring(6,17));
			//logger.debug("--> CBPSupplementaryPaymentParser :: setDebitRate ::"+subdata.substring(6,17));
			//5
			supplementaryPaymentBean.setReservedWord1(subdata.substring(17,37));
			//logger.debug("--> CBPSupplementaryPaymentParser :: setReservedWord1 ::"+subdata.substring(17,37));
			//6
			supplementaryPaymentBean.setBankInstructions(subdata.substring(37,387));
			//logger.debug("--> CBPSupplementaryPaymentParser :: setBankInstructions ::"+subdata.substring(37,387));
			//7
			supplementaryPaymentBean.setCustomerComments(subdata.substring(387,437));
			//logger.debug("--> CBPSupplementaryPaymentParser :: setCustomerComments ::"+subdata.substring(387,437));
			//8
			supplementaryPaymentBean.setSwiftCode(subdata.substring(437,448));
			//logger.debug("--> CBPSupplementaryPaymentParser :: setSwiftCode ::"+subdata.substring(437,448));
			//9
			supplementaryPaymentBean.setBankNameAddress(subdata.substring(448,518));
			//logger.debug("--> CBPSupplementaryPaymentParser :: setBankNameAddress ::"+subdata.substring(448,518));
			//10
			supplementaryPaymentBean.setPaymentNumber(subdata.substring(518,532));
			//logger.debug("--> CBPSupplementaryPaymentParser :: setPaymentNumber ::"+subdata.substring(518,532));
			//11
			supplementaryPaymentBean.setValueDate(subdata.substring(532,540));
			//logger.debug("--> CBPSupplementaryPaymentParser :: setValueDate ::"+subdata.substring(532,540));
			//12			
			supplementaryPaymentBean.setServiceFee(subdata.substring(540,555));
			//logger.debug("--> CBPSupplementaryPaymentParser :: setServiceFee ::"+subdata.substring(540,555));
			//13
			supplementaryPaymentBean.setDebitingAccount(subdata.substring(555,569));
			logger.debug("--> CBPSupplementaryPaymentParser :: setDebitingAccount ::"+subdata.substring(555,569));
			//14
			supplementaryPaymentBean.setReservedWord2(subdata.substring(569,600));
			//logger.debug("--> CBPSupplementaryPaymentParser :: setReservedWord2 ::"+subdata.substring(569,600));
		}catch(StringIndexOutOfBoundsException siob){
			logger.debug("StringIndexOutOfBoundsException :: CBPSupplementaryPaymentParser :: " + siob.getMessage());
		}catch(Exception ee){
			logger.debug("Exception :: CBPSupplementaryPaymentParser :: " + ee.getMessage());
		}
		return supplementaryPaymentBean;
	}
}
