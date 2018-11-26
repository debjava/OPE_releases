package com.ope.patu.payments.lmp300;

import org.apache.log4j.Logger;

import com.ope.patu.payments.lmp300.beans.PaymentServiceBeanRC1;
import com.ope.patu.payment.core.Parser;
/**
 * This class is for parsing the itemization records
 * @author anandkumar.b
 *
 */
public class ItemisationParser implements Parser
{
	protected static Logger logger = Logger.getLogger(ItemisationParser.class);
	/**
	 * This parse method parses the itemization record and returns the bean object
	 */
	public Object parse( Object ... objects )
	{
		PaymentServiceBeanRC1 paymentServiceBeanRC1 = new PaymentServiceBeanRC1();
		String subdata = ( String )objects[0];
		try{
			paymentServiceBeanRC1.setData_code(subdata.substring(0, 4));
			paymentServiceBeanRC1.setRecord_code(subdata.substring(4,5));
			paymentServiceBeanRC1.setTransaction_type(subdata.substring(5,6));
			paymentServiceBeanRC1.setReserved_1(subdata.substring(6,20));
			paymentServiceBeanRC1.setPayee_qualifier_1(subdata.substring(20,50));
			paymentServiceBeanRC1.setPayee_qualifier_2(subdata.substring(50, 70));
			paymentServiceBeanRC1.setPayee_qualifier_3(subdata.substring(70,90));
			paymentServiceBeanRC1.setItemisation_acc_no(subdata.substring(90,104));
			paymentServiceBeanRC1.setReserved_2(subdata.substring(104,107));
			paymentServiceBeanRC1.setItemisation_msg_type(subdata.substring(107,108));
			paymentServiceBeanRC1.setMessage(subdata.substring(108,178));
			paymentServiceBeanRC1.setReserved_3(subdata.substring(178,180));
			paymentServiceBeanRC1.setReserved_4(subdata.substring(180,186));
			paymentServiceBeanRC1.setItemisation_sum(subdata.substring(186,198));
			paymentServiceBeanRC1.setReserved_5(subdata.substring(198,199));
			paymentServiceBeanRC1.setReserved_6(subdata.substring(199,203));
			paymentServiceBeanRC1.setReserved_7(subdata.substring(203,215));
			paymentServiceBeanRC1.setInternal_data(subdata.substring(215,235));
			paymentServiceBeanRC1.setCost_centre(subdata.substring(235 ,255));
			paymentServiceBeanRC1.setReserved_8(subdata.substring(255,300));
		}catch(StringIndexOutOfBoundsException siobe){
			logger.error("String error in ItemisationParser :::"+siobe.getMessage());
		}catch(Exception ee){
			logger.error("Exception in ItemisationParser :::"+ee.getMessage());
		}
		return paymentServiceBeanRC1;
	}
}
