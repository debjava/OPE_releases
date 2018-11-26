package com.ope.patu.payments.ts;

import com.ope.patu.payments.ts.beans.SalarySumBean;
import java.util.List;
/**
 * This class is written for sum validations 
 * @author anandkumar.b
 *
 */
public class SumValidatorImpl implements SalaryValidator
{
	/**
	 * Method returns the object for salary payment service validations  
	 */
	public Object getValidatedObject(Object... objects) 
	{
		SalarySumBean salsumbean = ( SalarySumBean )objects[0];
		double transSum = (Double)objects[1];
		int noOfTransactions = ( Integer )objects[2];
		List<String> serviceIdList = ( List<String> )objects[3];
		//String key = (String) objects[4];
		double totalSum = Double.parseDouble(salsumbean.getTotal_sum_of_payments());
		int totalNoOfPayments = Integer.parseInt(salsumbean.getNumber_of_payments());
		
		System.out.println("transSum->"+transSum);
		System.out.println("totalSum->"+totalSum);
		System.out.println("noOfTransactions  ->"+noOfTransactions);
		System.out.println("totalNoOfPayments ->"+totalNoOfPayments);

		try{
			for(String data: serviceIdList){ 
				if(!data.contains(salsumbean.getService_code())){
					salsumbean.getSumRejectedMsg().add(SalaryErrorConstants.INVALID_SERVICE_CODE+",  "+data);
				}
			}  
			serviceIdList.clear();
			
			if(Math.round(transSum) != Math.round(totalSum)){
				salsumbean.getSumRejectedMsg().add(SalaryErrorConstants.INVALID_SUM_PAYMENTS+",  "+salsumbean.getTotal_sum_of_payments());
			}
			if(noOfTransactions != totalNoOfPayments){
				salsumbean.getSumRejectedMsg().add(SalaryErrorConstants.INVALID_NO_OF_PAYMENTS+",  "+salsumbean.getNumber_of_payments());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return salsumbean;
	}
}
