package com.ope.patu.unittest.keys;

import javax.crypto.SecretKey;

import com.ope.patu.key.HSKGenerator;
import com.ope.patu.util.CommonUtil;
import com.ope.patu.util.DESUtil;

public class TestHSK 
{
	public static void main(String[] args) 
	{
		HSKGenerator hskGen = new HSKGenerator();
		try
		{
			hskGen.generateHSK( );
			String hskString = hskGen.getHskString();
			System.out.println("HSK String-----"+hskString);
			byte[] hskBytes = hskGen.getHskBytes();
			/*
			 * Encrypt the hsk using KEK
			 * KEK=DA9B32C8381FBF29
			 * KEK sent by Shripad=379723239789FD9D
			 */
			String kekString = "379723239789FD9D";
			byte[] kekBytes = CommonUtil.hexToBytes( kekString );
			SecretKey secretKey = DESUtil.getsecretKey(kekBytes);
			byte[] encryptedBytes = DESUtil.getEncryptedBytes(hskBytes, secretKey);
			String encryptedHSK = CommonUtil.bytesToHex(encryptedBytes);
			System.out.println("EncryptedHSK------"+encryptedHSK);
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}
}
