package com.ope.patu.payments.ts;

import com.ope.patu.payment.core.Parser;


public abstract class ParserFactory 
{
	public static Parser getParser( Object ... objects )
	{
		String parserType = ( String )objects[0];
		Parser parser = null;
		try{
		if( parserType.equalsIgnoreCase(SalaryConstants.SAL_TRANS)){
			parser = new TransactionParser();
		}else if(parserType.equalsIgnoreCase(SalaryConstants.SAL_SUM)){
			parser = new SumRecordParser();
		}
		}catch(NullPointerException npe){
			npe.printStackTrace();
			System.out.println("-------");
		}
		return parser;
	}
}
