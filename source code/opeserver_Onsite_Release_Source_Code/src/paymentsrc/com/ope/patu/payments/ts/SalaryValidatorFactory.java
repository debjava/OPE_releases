package com.ope.patu.payments.ts;

/**
 *   
 * @author anandkumar.b
 *
 */
public abstract class SalaryValidatorFactory 
{
	/**
	 * This is a generic method for salary payment service it calls the vaidator class depends on input parameter  
	 * @param objects
	 * @return
	 */
	public static SalaryValidator getValidator( Object...objects )
	{
		SalaryValidator salaryValidator = null;
		String name =  ( String )objects[0];
		if( name.equalsIgnoreCase(SalaryConstants.SAL_TRANS) ){
			salaryValidator = new TransactionValidatorImpl();
		}else if(name.equalsIgnoreCase(SalaryConstants.SAL_SUM)){
			salaryValidator = new SumValidatorImpl();
		}
		return salaryValidator;
	}
}
