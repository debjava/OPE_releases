package com.dnb.agreement.key;

import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class DESUtil 
{
	private static String algorithmName = "DES";
	private static String algorithmSpec = "DES/ECB/NoPadding";
	
	
	public static SecretKey getsecretKey( byte[] keyBytes ) 
	{
		SecretKey secretKey = null;
		try
		{
			secretKey = new SecretKeySpec( keyBytes , algorithmName );
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		return secretKey;
	}
	
	public static SecretKey getsecretKey() 
	{
		SecretKey secretKey = null;
		try
		{
			SecureRandom sr = new SecureRandom();
			KeyGenerator keyGen = KeyGenerator.getInstance( algorithmName );
			keyGen.init(sr);
			secretKey = keyGen.generateKey();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return secretKey;
	}
	
	public static byte[] getEncryptedBytes(byte[] bytes, SecretKey secretKey)
	{
		byte[] encryptedBytes = null;
		try 
		{
			Cipher ecipher = Cipher.getInstance( algorithmSpec );
			ecipher.init(Cipher.ENCRYPT_MODE, secretKey);
			encryptedBytes = ecipher.doFinal(bytes);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return encryptedBytes;
	}
	
	public static byte[] getDecryptedBytes(byte[] bytes, SecretKey secretKey)
	{
		byte[] encryptedBytes = null;
		try 
		{
			Cipher ecipher = Cipher.getInstance( algorithmSpec );
			ecipher.init(Cipher.DECRYPT_MODE, secretKey);
			encryptedBytes = ecipher.doFinal(bytes);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return encryptedBytes;
	}
	
	public static byte[] getKVVEncryptedBytes(SecretKey key )
	{
		byte[] encryptedBytes = null;
		try
		{
		    // create zero byte array
		    byte[] zeroByte =new byte[]{0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
		    AlgorithmParameterSpec algParamSpec = new IvParameterSpec( zeroByte );
		    Cipher m_encrypter = Cipher.getInstance( "DES/CBC/NoPadding" );
		    m_encrypter.init(Cipher.ENCRYPT_MODE, key, algParamSpec);
		   encryptedBytes = m_encrypter.doFinal( zeroByte );
		}
		catch( Exception e )
		{
			e.printStackTrace();
			System.out.println("Exception while Encryption>-->"+e.getMessage());
		}
		return encryptedBytes;
	}//END OF getEncryptedBytes
}
