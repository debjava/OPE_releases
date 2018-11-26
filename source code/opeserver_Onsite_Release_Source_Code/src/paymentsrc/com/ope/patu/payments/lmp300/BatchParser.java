package com.ope.patu.payments.lmp300;

import org.apache.log4j.Logger;
import com.ope.patu.payments.lmp300.beans.PaymentServiceBeanRC0;
import com.ope.patu.payment.core.Parser;

/**
 * This file is for parsing the batch record
 * @author anandkumar.b
 *
 */
public class BatchParser implements Parser
{
	protected static Logger logger = Logger.getLogger(BatchParser.class);
	/**
	 * Method is for batch record parser and returns the bean object  
	 */
	public Object parse( Object ... objects )
	{
		PaymentServiceBeanRC0 paymentServiceBeanRC0 = new PaymentServiceBeanRC0();
		String subdata = ( String )objects[0];
		try{
			String dataCode = subdata.substring(0,4);
			String recordCode = subdata.substring(4,5);
			String transactionType = subdata.substring(5,6);
			paymentServiceBeanRC0.setLine_length(subdata.length());
			paymentServiceBeanRC0.setData_code(dataCode); 
			paymentServiceBeanRC0.setRecord_code(recordCode);
			paymentServiceBeanRC0.setTransaction_type(transactionType);
			paymentServiceBeanRC0.setPayers_account_number(subdata.substring(6,20));
			paymentServiceBeanRC0.setPayers_business_identity_code(subdata.substring(20,29));
			paymentServiceBeanRC0.setFile_creation_date(subdata.substring(29,35));
			paymentServiceBeanRC0.setFile_creation_time(subdata.substring(35,39));
			paymentServiceBeanRC0.setReceiving_bank(subdata.substring(40,41));
			paymentServiceBeanRC0.setDue_date(subdata.substring(41,47));
			paymentServiceBeanRC0.setPayers_name_qualifier(subdata.substring(47,82));
			paymentServiceBeanRC0.setBatch_identifier(subdata.substring(82,117));
			paymentServiceBeanRC0.setEdi_code(subdata.substring(117,134));
			paymentServiceBeanRC0.setCurrency_code(subdata.substring(134,135));
			paymentServiceBeanRC0.setReserved1(subdata.substring(135,223));
			paymentServiceBeanRC0.setFile_content(subdata.substring(223,224));
			paymentServiceBeanRC0.setService_code(subdata.substring(224,234));
			paymentServiceBeanRC0.setReserved2(subdata.substring(234,300));
		}catch(StringIndexOutOfBoundsException siobe){
			logger.error("String error in batch parser :::::"+siobe.getMessage());
		}catch(Exception ee){
			logger.error("Exception batch parser ::::::"+ee.getMessage());
		}
		return paymentServiceBeanRC0;
	}
}
