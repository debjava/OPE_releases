package com.ope.patu.payments.lum2;
import org.apache.log4j.Logger;

import com.ope.patu.payment.core.Parser;

public abstract class LUM2ParserFactory 
{
	protected static Logger logger = Logger.getLogger(LUM2ParserFactory.class);
	
	public static Parser getParser( Object ... objects )
	{
		String parserType = ( String )objects[0];
		Parser parser = null;
		try{
		if( parserType.equalsIgnoreCase("batch")){
			parser = new LUM2BatchRecordParser();
		}else if(parserType.equalsIgnoreCase("payment")){
			parser = new LUM2PaymentRecordParser();
		}else if(parserType.equalsIgnoreCase("invoice")){
			parser = new LUM2InvoiceRecordParser();
		}else if(parserType.equalsIgnoreCase("sum")){
			parser = new LUM2SumRecordParser();
		}else if(parserType.equalsIgnoreCase("supplementary_payment")){
			parser = new LUM2SupplementaryPaymentParser();
		}else if(parserType.equalsIgnoreCase("supplementary_feedback")){
			parser = new LUM2SupplementaryFeedbackParser();
		}
		}catch(NullPointerException npe){
			logger.debug("NullPointerException : "+npe.getMessage());
		}
		return parser;
	}
}
