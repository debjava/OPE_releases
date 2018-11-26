package com.ope.patu.unittest.keys;


import javax.crypto.SecretKey;

import com.ope.patu.key.MACGenerator;
import com.ope.patu.util.CommonUtil;
import com.ope.patu.util.DESUtil;

public class TestAuthValue 
{
	public static String getHexString(byte[] b) throws Exception {
		String result = "";
		for (int i=0; i < b.length; i++) {
			result +=
				Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
		}
		return result;
	}
	
	public static String getAUKHexString()
	{
		String aukString = null;
		SecretKey key = DESUtil.getsecretKey();
		aukString = CommonUtil.bytesToHex( key.getEncoded() );
		return aukString;
	}
	
	public static String getcustomerCalcAuth( String strToAuth )
	{
		String macString = null;
		try
		{
			/*
			 * Get the AUK from database
			 */
			//KEK Part1=340E7EDC49EA0D5A
			//KEK Part2=BF2733586FDCFCD7
			//KEK=8A294C852637F18C
			//KVV=ED0C95
			//AUK=29543191DF469125
			//5d02cd85c88c97e0//Auk String
			
			/*
			 * AUK value calculated based upon the
			 * data sent by Shripad
			 * AUK=AEBAE983D6406D07
			 */
//			String hexString = "29543191DF469125";//"5d02cd85c88c97e0";
			String hexString = "AEBAE983D6406D07";//"5d02cd85c88c97e0";
			byte[] hexBytes = CommonUtil.hexToBytes(hexString);
			MACGenerator macGenerator = new MACGenerator();
//			macGenerator.generateMAC( strToAuth , hexBytes );
			macString = macGenerator.generateMAC( strToAuth , hexBytes );//macGenerator.getMacString();
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		return macString;
	}
	
	public static String getPatuCalcAuth( String strToAuth )
	{
		String macString = null;
		try
		{
			/*
			 * Get the AUK from database
			 */
			//KEK Part1=340E7EDC49EA0D5A
			//KEK Part2=BF2733586FDCFCD7
			//KEK=8A294C852637F18C
			//KVV=ED0C95
			//AUK=29543191DF469125
			//5d02cd85c88c97e0//Auk String
			
			/*
			 * AUK value calculated based upon the
			 * data sent by Shripad
			 * AUK=AEBAE983D6406D07
			 */
//			String hexString = "29543191DF469125";//"5d02cd85c88c97e0";
			String hexString = "AEBAE983D6406D07";//This is AUK
			byte[] hexBytes = CommonUtil.hexToBytes(hexString);
			MACGenerator macGenerator = new MACGenerator();
//			macGenerator.generateMAC( strToAuth , hexBytes );
			macString = macGenerator.generateMAC( strToAuth , hexBytes );//macGenerator.getMacString();
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		return macString;
	}

	public static void main(String[] args) throws Exception
	{
//		String strToAuth = ">>ESI161120     BANKSOFT    4.10SMH00370844052922227        WMDATA20101302204        11080827124248123                                          ";
//		String strToAuth = ">>ESI161110 0000BasWare Bank740 SMH0037084405292227         99910000011111111        01080829141540000                                          ";
//		/*
//		 * customer side calculation
//		 */
//		String custAuthenticatedVal = getcustomerCalcAuth(strToAuth);
//		System.out.println("Authenticated Value by customer-------"+custAuthenticatedVal);
//		/*
//		 * Server side calculation
//		 */
//		String patuAuthenticatedVal = getPatuCalcAuth(strToAuth);
//		System.out.println("Authenticated value by PATU server------"+patuAuthenticatedVal);
//		System.out.println("Check condition----"+custAuthenticatedVal.equals(patuAuthenticatedVal));
		
		MACGenerator macObj = new MACGenerator();
//		String stringForAuthentication = ">>ESI161110 0000Basware Bank750 SMH0037084405292227         99910000011111111        01080919193025000                                          ";
		String stringForAuthentication = ">>ESI161110 0000Basware Bank750 SMH0037084405292227         9991000000R4S2057        00081010164505000                                          ";
		String encryptionKey = "70E9940E5B70C1BF";//CA5C372189FA2770//C1192440CF405F02//DD998350EF90C0E9
		String expectedOutput = "1E523C6905098252";
		String generatedValue = macObj.generateMAC(stringForAuthentication.toUpperCase(),
				encryptionKey);//4DB14C655EB569B9
		System.out.println("***********Test Case 4***********");
		if (!(expectedOutput.toUpperCase()).equals(generatedValue.toUpperCase()))
			System.out.println("Fail  - All Hell Broke Lose - We are doomed");
		System.out.println("Expected Value:" + expectedOutput
				+ " - Generated Value:" + generatedValue);
		
		
	}
}
