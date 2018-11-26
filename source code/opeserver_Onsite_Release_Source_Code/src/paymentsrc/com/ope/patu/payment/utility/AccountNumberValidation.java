package com.ope.patu.payment.utility;
import java.math.BigInteger;
import org.apache.log4j.Logger;

public class AccountNumberValidation {

	protected static Logger logger = Logger.getLogger(AccountNumberValidation.class);
	
	/**
	 * @param args
	 */

	private static final String ZERO_PADDING = "0";
	private static final int ACCOUNT_NUMBER_MAX_SIZE = 14;
	private static final int MAX_SUBPART_SIZE = 8;
	private static final int MIN_SUBPART_SIZE = 2;
	private static final int FIRSTPART_SIZE = 6;
	private static final String NORDIA_BANK_1 = "1";									// A
	private static final String NORDIA_BANK_2 = "2";									// A
	private static final String HANDELSBANKEN_BANK = "31";								// A
	private static final String SKANDINAVISKA_BANK = "33";								// A	
	private static final String DANSKE_BANK = "34";										// A
	private static final String TAPIOLA_BANK = "36";									// A
	private static final String DNB_NOR_BANK = "37";									// A
	private static final String SWED_BANK = "38";										// A
	private static final String S_BANK = "39";											// A
	private static final String SAVINGS_AND_LOCAL_COOPERATIVE_AND_AKTIA_BANK = "4";		//  - B 
	private static final String COOPERATIVE_BANKS_AND_OKO_AND_OKOBANK = "5";			//  - B
	private static final String ÅLANDSBANKEN_BANK = "6";								// A
	private static final String SAMPO_BANK = "8";										// A




	/*
	 *  (SHB)
	33 = SKANDINAVISKA ENSKILDA BANKEN (SEB)
	34 = DANSKE_BANK
	36 = TAPIOLA_BANK (TAPIOLA)
	37 = DNB_NOR_BANK ASA (DNB NOR)
	38 = SWEDBANK
	39 = S-BANK
	4 = SAVINGS BANKS (SP) AND LOCAL COOPERATIVE BANKS (POP) AND AKTIA
	5 = COOPERATIVE_BANKS_AND_OKO_AND_OKOBANK
	6 = ÅLANDSBANKEN ÅAB)
	8 = SAMPO BANK (SAMPO)
	 */

	/*public static void main(String[] args) {

		String accStr = "123456-789";

		if( accStr.indexOf('-')!= -1)
		{
			logger.debug("FINAL ->"+getAccountNumber(accStr));
		}
		else
		{
			logger.debug("Invaild Account Number");
		}
		String accNo = Long.toString(getAccountNumber(accStr));

		if(checkAccountNumber(accNo))
		{
			logger.debug("VALID ACC NO");
		}
		else
		{
			logger.debug("INVALID ACC NO");
		}

	}*/
	
	/**This method is used to check the authenticity of an
	 * account number.
	 * @param number of type String
	 * @return true or false based upon the validity of the account number.
	 * @author Debadatta Mishra
	 */
	public static boolean isAccountNumberValid( String number )
	{
		boolean validityFlag = false;
		int sum = 0;
		boolean alternate = false;
		for (int i = number.length() - 1; i >= 0; i--)
		{
			int n = Integer.parseInt(number.substring(i, i + 1));
			if (alternate) 
			{
				n *= 2;
				if (n > 9) 
				{
					n = (n % 10) + 1;
				}
			}
			sum += n;
			alternate = !alternate;
		}
		validityFlag = (sum % 10 == 0);
		return validityFlag;
	}

	public static boolean checkAccountNumber(String accountNo)
	{
		boolean checkStatus = false;

		/* Check Condition for Special Characters */
		if(accountNo.matches("[0-9]+"))
		{
			/* Check Condition for Bank Groups - A */
			if( accountNo.startsWith(NORDIA_BANK_1) || accountNo.startsWith(NORDIA_BANK_2)
					|| accountNo.startsWith(HANDELSBANKEN_BANK) || accountNo.startsWith(SKANDINAVISKA_BANK)
					|| accountNo.startsWith(DANSKE_BANK) || accountNo.startsWith(TAPIOLA_BANK)
					|| accountNo.startsWith(DNB_NOR_BANK) || accountNo.startsWith(SWED_BANK)
					|| accountNo.startsWith(S_BANK) || accountNo.startsWith(ÅLANDSBANKEN_BANK)
					|| accountNo.startsWith(SAMPO_BANK) )

			{
				logger.debug("GROUP - A");
				//Modified by Debadatta Mishra at onsite to provide account number validation
				checkStatus = isAccountNumberValid(accountNo);//true;

			}
			/* Check Condition for Bank Groups - B */
			else if( accountNo.startsWith(SAVINGS_AND_LOCAL_COOPERATIVE_AND_AKTIA_BANK) || accountNo.startsWith(COOPERATIVE_BANKS_AND_OKO_AND_OKOBANK) )
			{
				logger.debug("GROUP - B");
				checkStatus = true;
				checkStatus = isAccountNumberValid(accountNo);//true;
			}
			else {
				logger.info("Invalid Account Number");
				checkStatus = false;
			}

		}
		else
		{
			logger.debug("Invalid Account Number : Contained Special Characters");
			checkStatus = false;
		}

		return checkStatus;
	}

	public static long  getAccountNumber(String accountNo)
	{
		long electronicAccNumber = 0; 

		if( accountNo.startsWith(NORDIA_BANK_1) || accountNo.startsWith(NORDIA_BANK_2)
				|| accountNo.startsWith(HANDELSBANKEN_BANK) || accountNo.startsWith(SKANDINAVISKA_BANK)
				|| accountNo.startsWith(DANSKE_BANK) || accountNo.startsWith(TAPIOLA_BANK)
				|| accountNo.startsWith(DNB_NOR_BANK) || accountNo.startsWith(SWED_BANK)
				|| accountNo.startsWith(S_BANK) || accountNo.startsWith(ÅLANDSBANKEN_BANK)
				|| accountNo.startsWith(SAMPO_BANK) )

		{
			logger.debug("GROUP - A");
			electronicAccNumber = getGroupAAccountNumer(accountNo);
		}
		else if( accountNo.startsWith(SAVINGS_AND_LOCAL_COOPERATIVE_AND_AKTIA_BANK) || accountNo.startsWith(COOPERATIVE_BANKS_AND_OKO_AND_OKOBANK) )
		{
			logger.debug("GROUP - B");
			electronicAccNumber = getGroupBAccountNumer(accountNo);
		}
		else {
			logger.info("Invalid Account Number");
		}
		return electronicAccNumber;
	}

	public static long getGroupAAccountNumer(String accountNo) {

		long electronicAccNumber = 0;

		String aacStrPart[] = accountNo.split("-"); 
		if(aacStrPart.length == 2)
		{

			if(aacStrPart[0].length()!=FIRSTPART_SIZE ||  aacStrPart[1].length()< MIN_SUBPART_SIZE || aacStrPart[1].length()> MAX_SUBPART_SIZE)
			{
				logger.info("Invalid Account Number");
			}
			else
			{
				logger.debug("Valid Account Number");

				if(aacStrPart[1].length()== MAX_SUBPART_SIZE)
				{
					accountNo = aacStrPart[0]+ aacStrPart[1];
					logger.debug("accountNo.length() =>"+accountNo.length());
				}
				else
				{
					accountNo = getPadding(aacStrPart[0],MAX_SUBPART_SIZE-aacStrPart[1].length()) + aacStrPart[1] ;
					logger.debug("accountNo.length() =>"+accountNo.length());
				}

				try
				{
					electronicAccNumber = Long.parseLong(accountNo);
					logger.debug("electronicAccNumber=>"+electronicAccNumber);
				}
				catch (NumberFormatException nfe )
				{
					logger.debug("Number Format Exception : Invalid Account Number");
				}
			}

		}
		else
		{
			logger.debug("Invalid Account Number");
		}
		return electronicAccNumber;
	}


	public static long getGroupBAccountNumer(String accountNo) {

		long electronicAccNumber = 0;

		String aacStrPart[] = accountNo.split("-"); 
		if(aacStrPart.length == 2)
		{
			if(aacStrPart[0].length()!=FIRSTPART_SIZE ||  aacStrPart[1].length()< MIN_SUBPART_SIZE || aacStrPart[1].length()> MAX_SUBPART_SIZE)
			{
				logger.debug("Invalid Account Number");
			}
			else
			{
				logger.debug("Valid Account Number");

				if(aacStrPart[1].length()== MAX_SUBPART_SIZE)
				{
					accountNo = aacStrPart[0]+ aacStrPart[1];
					logger.debug(accountNo.length());
				}
				else
				{
					accountNo = getPadding(aacStrPart[0]+(aacStrPart[1].charAt(0)),MAX_SUBPART_SIZE-aacStrPart[1].length()) + aacStrPart[1].substring(1, aacStrPart[1].length()) ;
					logger.debug(accountNo.length());
				}

				try
				{
					electronicAccNumber = Long.parseLong(accountNo);
					logger.debug("electronicAccNumber=>"+electronicAccNumber);
				}
				catch (NumberFormatException nfe )
				{
					logger.debug("Number Format Exception : Invalid Account Number");
				}
			}

		}
		else
		{
			logger.debug("Invalid Account Number");
		}
		return electronicAccNumber;
	}

	public static boolean checkIBANAccountNumber(String accountNumber) {


		//logger.debug("IBAN_Account_Number.length()->"+accountNumber.length());

		String IBAN = accountNumber.substring(4, accountNumber.length()) + accountNumber.substring(0, 2) + accountNumber.substring(2,4) ;

		String countryCode = accountNumber.substring(0, 2);
		//logger.debug("countryCode->"+countryCode);

		String checkDigits = accountNumber.substring(2, 4);
		//logger.debug("checkDigit->"+checkDigits);



		// String IBANaccountNumber = getNumericCountryCode(bankId) + accNumber.trim() + getNumericCountryCode(countryCode)  + checkDigits.trim() ;
		// logger.debug("Simple Account Number ---->"+accountNumber);
		// logger.debug("IBAN Account Number ------>"+IBAN);


		char[] chr = IBAN.toCharArray();
		StringBuilder encode = new StringBuilder();
		for(int i=0;i<chr.length;i++) {
			if ( (( (int)(chr[i]) ) >= 48)&& (( (int)(chr[i]) ) <= 57) )
				encode.append(chr[i]);
			else if ( (( (int)(chr[i]) ) >= 65)&& (( (int)(chr[i]) ) <= 91) )
				encode.append((int)chr[i]-55);
		}


		// logger.debug("IBAN Account Number ---------------->"+IBAN);
		// logger.debug("Numeric IBAN Account Number -------->"+encode.toString());
		return doIBANValidation(encode.toString());
	}

	public static boolean doIBANValidation(String IBANaccountNumber) {

		boolean stauts = false;

		logger.debug("IBANaccountNumber-->"+IBANaccountNumber);
		try {
			BigInteger bigInteger1 = new BigInteger(IBANaccountNumber);
			BigInteger bigInteger2 = BigInteger.valueOf(97);

			bigInteger1 = bigInteger1.mod(bigInteger2);

			if(bigInteger1.mod(bigInteger2).intValue()== 1)
			{
				logger.debug("In Coming IBAN Account Number is CORRECT....");
				stauts = true;
			}
			else {
				logger.debug("In Coming IBAN Account Number is INVALID....");
				stauts = false;
			}

		}catch (Exception e) {
			logger.info("Caught in Exception : "+ e.getMessage());
			stauts = false;
		}
		return stauts;
	}

	private static String getPadding(String firstPart, int noOfZero) {
		StringBuilder sb = new StringBuilder(firstPart);
		for(int i=0 ;i<noOfZero ;i++)
		{
			sb.append(ZERO_PADDING);
		}
		return sb.toString();
	}
}