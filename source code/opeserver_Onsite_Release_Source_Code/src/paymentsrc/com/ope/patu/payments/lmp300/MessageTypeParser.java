package com.ope.patu.payments.lmp300;

import org.apache.log4j.Logger;
import com.ope.patu.payments.lmp300.beans.PaymentServiceBeanMsgRec;
import com.ope.patu.payment.core.Parser;

/**
 * This class is for parsing the message type records
 * @author anandkumar.b
 *
 */
public class MessageTypeParser implements Parser
{
	protected static Logger logger = Logger.getLogger(MessageTypeParser.class);
	/**
	 * Method parse the message record under transaction record and does the validation returns the message bean object
	 */
	public Object parse( Object ... objects )
	{
		PaymentServiceBeanMsgRec paymentServiceBeanMessageRecord = new PaymentServiceBeanMsgRec();
		String subdata = ( String )objects[0];
		try{
			paymentServiceBeanMessageRecord.setData_code(subdata.substring(0,4));
			paymentServiceBeanMessageRecord.setRecord_code(subdata.substring(4,5));
			paymentServiceBeanMessageRecord.setTransaction_type(subdata.substring(5,6));
			paymentServiceBeanMessageRecord.setReserved_1(subdata.substring(6,20));
			paymentServiceBeanMessageRecord.setReserved_2(subdata.substring(20,50));
			paymentServiceBeanMessageRecord.setReserved_3(subdata.substring(50,70));
			paymentServiceBeanMessageRecord.setReserved_4(subdata.substring(70,90));
			paymentServiceBeanMessageRecord.setPayees_account(subdata.substring(90,104));
			paymentServiceBeanMessageRecord.setMessage_1(subdata.substring(104,139));
			paymentServiceBeanMessageRecord.setMessage_2(subdata.substring(139,174));
			paymentServiceBeanMessageRecord.setMessage_3(subdata.substring(174,209));
			paymentServiceBeanMessageRecord.setMessage_4(subdata.substring(209,244));
			paymentServiceBeanMessageRecord.setMessage_5(subdata.substring(244,279));
			paymentServiceBeanMessageRecord.setReserved_5(subdata.substring(279,300));
		}catch(StringIndexOutOfBoundsException siob){
			logger.error("String Error in MessageTypeParser :::"+siob.getMessage());
		}catch(Exception ee){
			logger.error("Exception in MessageTypeParser ::::"+ee.getMessage());
		}
		return paymentServiceBeanMessageRecord;
	}
}
