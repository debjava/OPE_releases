package com.ope.patu.payments.lum2;
import org.apache.log4j.Logger;

import com.ope.patu.payment.core.Parser;
import com.ope.patu.payments.lum2.beans.LUM2BatchRecordBean;

public class LUM2BatchRecordParser implements Parser
{
	protected static Logger logger = Logger.getLogger(LUM2BatchRecordParser.class);
	
	public Object parse( Object ... objects )
	{
		LUM2BatchRecordBean batchRecordBean = new LUM2BatchRecordBean();
		String subdata = ( String )objects[0];
		try{
			String data_code = subdata.substring(0,4);
			String rec_code = subdata.substring(4,5);
			String tran_type = subdata.substring(5,6);
			
			batchRecordBean.setLineLength(subdata.length());
			// logger.debug("--> CBPBatchRecordParser :: setLineLength ::"+subdata.length());
			
			batchRecordBean.setApplicationCode(subdata.substring(0,4));
			// logger.debug("--> CBPBatchRecordParser :: setApplicationCode ::"+subdata.substring(0,4));
			
			batchRecordBean.setRecordCode(subdata.substring(4,5));
			// logger.debug("--> CBPBatchRecordParser :: setRecordCode ::"+subdata.substring(4,5));
			
			batchRecordBean.setFileType(subdata.substring(5,6));
			// logger.debug("--> CBPBatchRecordParser :: setFileType ::"+subdata.substring(5,6));
			
			batchRecordBean.setReservedWord1(subdata.substring(6,7));
			// logger.debug("--> CBPBatchRecordParser :: setReservedWord1 ::"+subdata.substring(6,7));
			
			batchRecordBean.setPayersBusinessIdentityCode(subdata.substring(7,18));
			logger.debug("--> CBPBatchRecordParser :: setPayersBusinessIdentityCode ::"+subdata.substring(7,18));
			
			batchRecordBean.setCustomerCodeExtension(subdata.substring(18,23));
			// logger.debug("--> CBPBatchRecordParser :: setCustomerCodeExtension ::"+subdata.substring(18,23));
			
			batchRecordBean.setReservedWord2(subdata.substring(23,25));
			// logger.debug("--> CBPBatchRecordParser :: setReservedWord2 ::"+subdata.substring(23,25));
			
			batchRecordBean.setAcceptanceCode(subdata.substring(25,26));
			// logger.debug("--> CBPBatchRecordParser :: setAcceptanceCode ::"+subdata.substring(25,26));
			
			batchRecordBean.setFileCreationTime(subdata.substring(26,36));
			// logger.debug("--> CBPBatchRecordParser :: setFileCreationTime ::"+subdata.substring(26,36));
			
			batchRecordBean.setReservedWord3(subdata.substring(36,50));
			// logger.debug("--> CBPBatchRecordParser :: setReservedWord3 ::"+subdata.substring(36,50));
			
			batchRecordBean.setDebitDate(subdata.substring(50,58));
			logger.debug("--> CBPBatchRecordParser :: setDebitDate ::"+subdata.substring(50,58));
						
			batchRecordBean.setReservedWord4(subdata.substring(58,600));
			// logger.debug("--> CBPBatchRecordParser :: setReservedWord4 ::"+subdata.substring(58,600));
			
		}catch(StringIndexOutOfBoundsException siobe){
			logger.debug(siobe.getMessage());
		}catch(Exception ee){
			logger.debug(ee.getMessage());
		}
		return batchRecordBean;
	}
}
