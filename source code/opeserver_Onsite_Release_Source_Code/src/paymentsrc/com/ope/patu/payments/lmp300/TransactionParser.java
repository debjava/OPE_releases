package com.ope.patu.payments.lmp300;

import org.apache.log4j.Logger;

import com.ope.patu.payments.lmp300.beans.PaymentServiceBeanRC1;
import com.ope.patu.payment.core.Parser;

/**
 * This class is for transaction record parser
 * @author anandkumar.b
 *
 */
public class TransactionParser implements Parser
{
	protected static Logger logger = Logger.getLogger(TransactionParser.class);
	/**
	 * Method parses the transaction type record and returns the transaction bean object for validation
	 */
	int seqNo=0;
	public Object parse( Object ... objects )
	{
		seqNo++;
		String decimalAmount="";
		PaymentServiceBeanRC1 paymentServiceBeanRC1 = new PaymentServiceBeanRC1();
		String subdata = ( String )objects[0];
		try{
			paymentServiceBeanRC1.setSeq_no(seqNo);
			paymentServiceBeanRC1.setData_code(subdata.substring(0, 4));
			paymentServiceBeanRC1.setRecord_code(subdata.substring(4,5));
			paymentServiceBeanRC1.setTransaction_type(subdata.substring(5,6));
			paymentServiceBeanRC1.setReserved_1(subdata.substring(6,20));
			paymentServiceBeanRC1.setPayee_qualifier_1(subdata.substring(20,50));
			paymentServiceBeanRC1.setPayee_qualifier_2(subdata.substring(50, 70));
			paymentServiceBeanRC1.setPayee_qualifier_3(subdata.substring(70,90));
			paymentServiceBeanRC1.setPayees_account_number(subdata.substring(90,104));
			paymentServiceBeanRC1.setReserved_2(subdata.substring(104,107));
			paymentServiceBeanRC1.setMessage_type(subdata.substring(107,108));
			paymentServiceBeanRC1.setMessage(subdata.substring(108,178));
			paymentServiceBeanRC1.setReserved_3(subdata.substring(178,180));
			paymentServiceBeanRC1.setReserved_4(subdata.substring(180,186));
			String transactionAmount = subdata.substring(186,198);
			try{
				  String realAmount = transactionAmount.substring(0, 10);
				  decimalAmount = realAmount+"."+transactionAmount.substring(10, 12);
			  }catch(StringIndexOutOfBoundsException siobe){
				  logger.error("StringIndexOutOfBoundsException Trans parser:::::::: "+siobe.getMessage()); 
			  }
			paymentServiceBeanRC1.setSum(decimalAmount);
			paymentServiceBeanRC1.setReserved_5(subdata.substring(198,199));
			paymentServiceBeanRC1.setReserved_6(subdata.substring(199,203));
			paymentServiceBeanRC1.setReserved_7(subdata.substring(203,215));
			paymentServiceBeanRC1.setInternal_data(subdata.substring(215,235));
			paymentServiceBeanRC1.setCost_centre(subdata.substring(235 ,255));
			paymentServiceBeanRC1.setReserved_8(subdata.substring(255,300));
		}catch(StringIndexOutOfBoundsException siobe){
			logger.error("StringIndexOutOfBoundsException Trans parser:::::::: "+siobe.getMessage());
		}catch(Exception ee){
			logger.error("Exception in TransactionParser ::::"+ee.getMessage());
		}
		return paymentServiceBeanRC1;
	}
}
