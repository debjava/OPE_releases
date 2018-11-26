package com.ope.patu.payments.lmp300;
import org.apache.log4j.Logger;

import com.ope.patu.payment.core.Parser;

/**
 * This is the factory class it receives the parser type as input parameter and calls the corresponding parser class 
 * @author anandkumar.b
 */
public abstract class ParserFactory 
{
	protected static Logger logger = Logger.getLogger(ParserFactory.class);

	/**
	 * Method reads the record type from records and calls the corresponding parser and returns the parser class
	 * @param objects
	 * @return
	 */
	public static Parser getParser( Object ... objects )
	{
		String parserType = ( String )objects[0];
		Parser parser = null;
		try{
			if( parserType.equalsIgnoreCase(PaymentConstants.BATCH)){
				parser = new BatchParser();
			}else if(parserType.equalsIgnoreCase(PaymentConstants.TRANSACTIONS)){
				parser = new TransactionParser();
			}else if(parserType.equalsIgnoreCase(PaymentConstants.ITEMISATION)){
				parser = new ItemisationParser();
			}else if(parserType.equalsIgnoreCase(PaymentConstants.SUM)){
				parser = new SumRecordParser();
			}else if(parserType.equalsIgnoreCase(PaymentConstants.MESSAGETRANS)){
				parser = new MessageTypeParser();
			}else if(parserType.equalsIgnoreCase(PaymentConstants.MESSAGEITEM)){
				parser = new MessageTypeParser();
			}
		}catch(NullPointerException npe){
			logger.error("Null value in ParserFactory :::"+npe.getMessage());
		}
		return parser;
	}
}
