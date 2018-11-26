package com.ope.patu.payments.lmp300;

import org.apache.log4j.Logger;

import com.ope.patu.payments.lmp300.beans.PaymentServiceBeanRC9;
import com.ope.patu.payment.core.Parser;
/**
 * This class is for sum record parser
 * @author anandkumar.b
 *
 */
public class SumRecordParser implements Parser
{
	protected static Logger logger = Logger.getLogger(SumRecordParser.class);
	/**
	 * Method parses the sum type record and returns the sum bean object 
	 */
	public Object parse( Object ... objects )
	{
		PaymentServiceBeanRC9 paymentServiceBeanRC9 = new PaymentServiceBeanRC9();
		String subdata = ( String )objects[0];
		try{
				paymentServiceBeanRC9.setData_code(subdata.substring(0,4));
				paymentServiceBeanRC9.setRecord_code(subdata.substring(4,5));
				paymentServiceBeanRC9.setTransaction_type(subdata.substring(5,6));
				paymentServiceBeanRC9.setPayers_account_number(subdata.substring(6,20));
				paymentServiceBeanRC9.setPayers_business_identity_code(subdata.substring(20,29));
				paymentServiceBeanRC9.setFile_creation_date(subdata.substring(29,35));
				paymentServiceBeanRC9.setNumber_of_payments(subdata.substring(35,41));
				paymentServiceBeanRC9.setTotal_sum_of_payments(subdata.substring(41,54));
				paymentServiceBeanRC9.setReserved_1(subdata.substring(54,60));
				paymentServiceBeanRC9.setReserved_2(subdata.substring(60,73));
				paymentServiceBeanRC9.setReserved_3(subdata.substring(73,79));
				paymentServiceBeanRC9.setBatch_identifier(subdata.substring(79,114));
				paymentServiceBeanRC9.setEdi_code(subdata.substring(114,131));
				paymentServiceBeanRC9.setReserved_4(subdata.substring(131,200));
			}catch(StringIndexOutOfBoundsException siobe){
				logger.error("String exception in SumRecordParser :::"+siobe.getMessage());
			}catch(Exception ee){
				logger.error("Exception in SumRecordParser :::"+ee.getMessage());
			}
			return paymentServiceBeanRC9;
		}
}
