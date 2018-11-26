package com.ope.patu.payments.ts;

import com.ope.patu.payment.core.Parser;
import com.ope.patu.payments.ts.beans.SalarySumBean;


public class SumRecordParser implements Parser
{
	/**
	 * This is the abstract method it parses the record and set the values to bean object
	 * bean object contains the salary sum record
	 */
	public Object parse( Object ... objects )
	{
		SalarySumBean salarysumbean = new SalarySumBean();
		String salarysumrecord = ( String )objects[0];
		try{
			
			salarysumbean.setRecord_code(salarysumrecord.substring(0, 1));
			salarysumbean.setPayment_date(salarysumrecord.substring(1, 7));
			salarysumbean.setPayrs_name_qualifier(salarysumrecord.substring(7, 23));
			salarysumbean.setService_code(salarysumrecord.substring(23, 32));
			salarysumbean.setReason_for_payments(salarysumrecord.substring(32, 34));
					
			String amount = salarysumrecord.substring(34, 43);
			String decimal = salarysumrecord.substring(43, 45);
			salarysumbean.setTotal_sum_of_payments(amount.concat(".").concat(decimal));
						
			salarysumbean.setCurrency_code(salarysumrecord.substring(45, 46));
			salarysumbean.setReserved(salarysumrecord.substring(46, 64));
			salarysumbean.setNumber_of_payments(salarysumrecord.substring(64, 70));
			salarysumbean.setData_from_customer(salarysumrecord.substring(70, 80));
			
		}catch(StringIndexOutOfBoundsException sioe){
			sioe.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
		return salarysumbean;
	}
}