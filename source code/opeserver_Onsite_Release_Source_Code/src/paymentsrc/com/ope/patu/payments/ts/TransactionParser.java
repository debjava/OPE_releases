package com.ope.patu.payments.ts;

import com.ope.patu.payment.core.Parser;
import com.ope.patu.payments.ts.beans.SalaryTransactionBean;


public class TransactionParser implements Parser
{
	/**
	 * Method parse the transaction record and returns the transaction bean object for transaction records  
	 */
	public Object parse( Object ... objects )
	{
		SalaryTransactionBean transactionbean = new SalaryTransactionBean();
		String salaryrecord = ( String )objects[0];
		try{
			transactionbean.setRecord_code(salaryrecord.substring(0, 1));
			transactionbean.setPayment_date(salaryrecord.substring(1, 7));
			transactionbean.setReserved_1(salaryrecord.substring(7, 9));
			transactionbean.setPayees_acc_no(salaryrecord.substring(9, 23));
			transactionbean.setService_code(salaryrecord.substring(23, 32));
			transactionbean.setReasion_for_payment(salaryrecord.substring(32, 34));
			String amount = salaryrecord.substring(34, 43);
			String decimal = salaryrecord.substring(43, 45);
			transactionbean.setAmount(amount.concat(".").concat(decimal));
			transactionbean.setPayees_name(salaryrecord.substring(45, 64));
			transactionbean.setPayees_identity_no(salaryrecord.substring(64, 75));
			transactionbean.setReserved_2(salaryrecord.substring(75, 80));
			
		}catch(StringIndexOutOfBoundsException sioe){
			sioe.printStackTrace();
		}catch(NullPointerException npe){
			npe.printStackTrace();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return transactionbean;
	}
}
