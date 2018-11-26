package com.ope.patu.util;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.log4j.Logger;

public class DESUtil 
{
	private static String algorithmName = "DES";
	private static String algorithmSpec = "DES/CBC/NoPadding";
	protected static Logger logger = Logger.getLogger(DESUtil.class);
	
	/**
	 * Gets the SecretKey object
	 * 
	 * @param keyBytes
	 *            Byte array of the key
	 * @param algoName
	 *            Internal algorithm for the spec
	 * @return Secret Key object
	 */
	public static SecretKey getSecretKey(byte[] keyBytes, String algoName) {
		SecretKey secretKey = new SecretKeySpec(keyBytes, algoName);
		return secretKey;
	}
	
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
			logger.error(e);
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
		catch (NoSuchAlgorithmException e) 
		{
			e.printStackTrace();
			logger.error(e);
		}
		catch( Exception e )
		{
			e.printStackTrace();
			logger.error(e);
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
	
	public static byte[] getDecryptedBytesWithIV(byte[] bytes, SecretKey secretKey) {
		byte[] encryptedBytes = null;
		try {
			byte[] zeroByte = new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
					0x00, 0x00 };
			AlgorithmParameterSpec algParamSpec = new IvParameterSpec(zeroByte);
			Cipher ecipher = Cipher.getInstance(algorithmSpec);
			ecipher.init(Cipher.DECRYPT_MODE, secretKey,algParamSpec);
			encryptedBytes = ecipher.doFinal(bytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return encryptedBytes;
	}
	
	public static byte[] getDecryptedBytes(byte[] bytes, SecretKey secretKey)
	{
		byte[] encryptedBytes = null;
		try 
		{
			byte[] zeroByte = new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
					0x00, 0x00 };
			AlgorithmParameterSpec algParamSpec = new IvParameterSpec(zeroByte);
			Cipher ecipher = Cipher.getInstance( algorithmSpec );
			ecipher.init(Cipher.DECRYPT_MODE, secretKey , algParamSpec );
			encryptedBytes = ecipher.doFinal(bytes);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return encryptedBytes;
	}
	
	/*
	 * Another way
	 */
	public static byte[] getEncryptedBytesWithIV(byte[] bytes,
			SecretKey secretKey) {
		byte[] encryptedBytes = null;
		try 
		{
			byte[] zeroByte = new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
					0x00, 0x00 };
			AlgorithmParameterSpec algParamSpec = new IvParameterSpec(zeroByte);
			Cipher ecipher = Cipher.getInstance("DES/CBC/NoPadding");
			ecipher.init(Cipher.ENCRYPT_MODE, secretKey, algParamSpec);
			encryptedBytes = ecipher.doFinal(bytes);
		}
		catch (NoSuchAlgorithmException e) 
		{
			logger.error(e);
			e.printStackTrace();
		}
		catch (NoSuchPaddingException e) 
		{
			logger.error(e);
			e.printStackTrace();
		}
		catch (InvalidKeyException e) 
		{
			logger.error(e);
			e.printStackTrace();
		}
		catch (InvalidAlgorithmParameterException e) 
		{
			logger.error(e);
			e.printStackTrace();
		}	
		catch (IllegalBlockSizeException e) 
		{
			logger.error(e);
			e.printStackTrace();
		}
		catch (BadPaddingException e) 
		{
			logger.error(e);
			e.printStackTrace();
		}
//		try {
//			byte[] zeroByte = new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
//					0x00, 0x00 };
//			AlgorithmParameterSpec algParamSpec = new IvParameterSpec(zeroByte);
//			Cipher ecipher = Cipher.getInstance("DES/CBC/NoPadding");
//			ecipher.init(Cipher.ENCRYPT_MODE, secretKey, algParamSpec);
//			encryptedBytes = ecipher.doFinal(bytes);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
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
		catch (NoSuchAlgorithmException e) 
		{
			logger.error(e);
			e.printStackTrace();
		}
		catch (NoSuchPaddingException e) 
		{
			logger.error(e);
			e.printStackTrace();
		}
		catch (InvalidKeyException e) 
		{
			logger.error(e);
			e.printStackTrace();
		}
		catch (InvalidAlgorithmParameterException e) 
		{
			logger.error(e);
			e.printStackTrace();
		}	
		catch (IllegalBlockSizeException e) 
		{
			logger.error(e);
			e.printStackTrace();
		}
		catch (BadPaddingException e) 
		{
			logger.error(e);
			e.printStackTrace();
		}
		catch( Exception e )
		{
			e.printStackTrace();
			logger.error("Exception while Encryption>-->"+e.getMessage());
		}
		return encryptedBytes;
	}//END OF getEncryptedBytes
}
