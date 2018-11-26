package com.ope.patu.key;

import java.security.SecureRandom;
import javax.crypto.SecretKey;

import com.ope.patu.util.CommonUtil;
import com.ope.patu.util.DESUtil;
import com.ope.patu.util.ParityUtil;


public class KeyGenerator 
{
	
	private static String getKey()
	{
		SecureRandom sr = new SecureRandom(); 
		byte[] randomBytes = new byte[8];
		sr.nextBytes(randomBytes); 
		return CommonUtil.bytesToHex(randomBytes);
	}
	
	public static String getNewAuk()
	{
		return getKey();
	}
	
	public static String getKEKPart1()
	{
		return getKey();
	}

	public static String getKEKPart2()
	{
		return getKey();
	}

	public static String generateKEK(String part1,String part2) 
	{
		byte[] part1Bytes = CommonUtil.hexToBytes(part1);
		byte[] part2Bytes = CommonUtil.hexToBytes(part2);

		// Checks the odd parity ,if not set parity odd
		for(int i=0;i< part1Bytes.length;i++){

			// For Part1
			boolean flag1= ParityUtil.isParrityOdd(part1Bytes[i]);

			if(!flag1)
			{
				part1Bytes[i]=ParityUtil.oddParity(part1Bytes[i]);
			}

			//	For Part1
			boolean flag2= ParityUtil.isParrityOdd(part2Bytes[i]);
			if(!flag2){

				part2Bytes[i]=ParityUtil.oddParity(part2Bytes[i]);
			}
		} // end of checking parity.

		// Do the Ex or operation
		byte[] resTemp = ParityUtil.xorArray( part1Bytes, part2Bytes );

		// Set the odd parity of temp result and get the KEK
		for (int i = 0; i < resTemp.length; i++) {
			boolean flag3 = ParityUtil.isParrityOdd(resTemp[i]);

			if (!flag3) {
				resTemp[i] =ParityUtil.oddParity(resTemp[i]);
			}

		}
		return CommonUtil.bytesToHex(resTemp);
	}

	public static String getAUK(String kek,String newKek)
	{
		byte[] kekByte = CommonUtil.hexToBytes( kek );
		byte[] newkekByte = null;
		// Generates the secreat key
		SecretKey secretKey = DESUtil.getsecretKey( kekByte );

		if(newKek == null || newKek.equals(""))
		{
			newkekByte =new byte[]{0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
		}
		else
		{
			newkekByte = CommonUtil.hexToBytes(newKek);
		}

		byte[] temp = DESUtil.getDecryptedBytes( newkekByte , secretKey );

		// set the odd parity
		for(int i=0;i< temp.length;i++)
		{
			boolean flag1= ParityUtil.isParrityOdd(temp[i]);
			if(!flag1)
			{
				temp[i]=ParityUtil.oddParity(temp[i]);
			}
		}
		return CommonUtil.bytesToHex(temp);
	}
	
	public static String getKVV( String kekString ) 
	{

		byte[] kekBytes = CommonUtil.hexToBytes( kekString );
		SecretKey secretKey = DESUtil.getsecretKey( kekBytes );
		// KVV 8 bytes
		byte[] tempKVV = DESUtil.getKVVEncryptedBytes(secretKey);
		// Convert the kvv into hexadecimal String
		String kvv = CommonUtil.bytesToHex( tempKVV );
		return kvv.substring(0,6);

	}

}
