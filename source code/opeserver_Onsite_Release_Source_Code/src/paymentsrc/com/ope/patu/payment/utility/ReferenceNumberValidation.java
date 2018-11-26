package com.ope.patu.payment.utility;

import org.apache.log4j.Logger;

public class ReferenceNumberValidation {
	
	protected static Logger logger = Logger.getLogger(ReferenceNumberValidation.class);

	/**
	 * @param args
	 */
	public boolean validateRefranceNumber(String refNo) 
	{
		//String refNo  = "00000000000001234561";
		//Added by Debadatta Mishra at onsite to fix the issue related to reference number.
		return isValidRefenceNo(refNo);
		/*
		 * The following lines have been commented out
		 * by Debadatta Mishra to fix the issue related
		 * to reference number validation.
		 */
//		boolean flag=false;				 
//		char calculationDigits [] = null;
//		int products [] = new int[19];
//		int sum = 0;
//		int roundFigure = 0;
//		int checkDigit =0;
//		calculationDigits = refNo.toCharArray(); 
//		
//		for(int i=calculationDigits.length-2 ; i>=0 ; i--)
//		{
//			if( ((i%3)== 0))
//			{   
//				products [i]   = 7 * (Character.getNumericValue(calculationDigits[i]));
//				sum = sum + products [i];
//				//System.out.println("For i ->"+i+" - "+Character.getNumericValue(calculationDigits[i])+"*"+7+"="+products [i]);
//			}
//			else if( (( (i+1)%3 )== 0))
//			{   
//				products [i]   = 3 * (Character.getNumericValue(calculationDigits[i]));
//				sum = sum + products [i];
//				//System.out.println("For i ->"+i+" - "+Character.getNumericValue(calculationDigits[i])+"*"+3+"="+products [i]);
//			}
//			else if( (( (i+2)%3 )== 0))
//			{   
//				products [i]   = 1 * (Character.getNumericValue(calculationDigits[i]));
//				sum = sum + products [i];
//				//System.out.println("For i ->"+i+" - "+Character.getNumericValue(calculationDigits[i])+"*"+1+"="+products [i]);
//			}
////		
//			
//		}
//		System.out.println("Sum->"+sum);
//		
//		roundFigure = sum/10;
//		roundFigure = (roundFigure+1) * 10;
//		checkDigit = roundFigure - sum;
//		if(checkDigit == Character.getNumericValue(refNo.charAt(refNo.length()-1)) ) {
//			flag=true;
//		}else{
//			flag = false;
//		}
//		System.out.println("Final Check Digit Number->"+checkDigit);
//		return flag;
	}
	
	/**This method is used to obtain the validity of a reference number.
	 * @param refNo of type String indicating the reference number
	 * @return  true if the reference number is valid.
	 * @author Debadatta Mishra
	 */
	public static boolean isValidRefenceNo( String refNo )
	{
		logger.debug("Reference number------->"+refNo);
		boolean refNoFlag = false ;
		try
		{
			int[] fixedValue = {7,3,1}; 
			int checkDigit = Integer.parseInt(refNo.substring(refNo.length() - 1));
			String refNoToCalculate = refNo.substring(0,refNo.length() - 1);
			int sum = 0;
			int counter = 0;
			for (int i = refNoToCalculate.length() - 1; i >= 0; i--)
			{
				int n = Integer.parseInt(refNoToCalculate.substring(i, i + 1));
				sum+=n*fixedValue[counter];
				counter++;
				if( counter > 2)
					counter = 0;
			}
			refNoFlag = ( ( getNext10Multiple(sum) - sum ) == checkDigit );
		}
		catch( NullPointerException npe )
		{
			logger.debug("Refernece number is null, please provide the reference number");
			refNoFlag = false ;
		}
		catch( IndexOutOfBoundsException ibe )
		{
			logger.debug("Reference number is blank, not a correct reference number");
			refNoFlag = false ;
		}
		catch( Exception e )
		{
			logger.debug("Not a proper reference number to validate, problem in validating the reference number");
			refNoFlag = false ;
		}
		return refNoFlag ;
	}
	
	/**This method is used to obtain the next 10 multiple of
	 * a number.
	 * @param number of type int
	 * @return the next 10 multiple of a number
	 * @author Debadatta Mishra
	 */
	public static int getNext10Multiple( int number )
	{
		int val = number % 10 == 0 ? number : ( (number / 10)+1)*10 ;
		return val ;
	}

}
