package com.ope.patu.security;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import com.sun.crypto.provider.SunJCE;

public class PATUSecurityUtil
{
	
	private KeyPair keyPair;
	private static final String ALGORITHM = "RSA";
	private static final int KEYSIZE = 1024;
	
	public PATUSecurityUtil()
	{
		super();
		Security.addProvider(new SunJCE());
		
	}
	
	public void invokeKeys()
	{
		try
		{
			KeyPairGenerator keypairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
			keypairGenerator.initialize(KEYSIZE);
			keyPair = keypairGenerator.generateKeyPair();
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}
	
	public String getPublicKeyString( PublicKey publicKey )
	{
		return new BASE64Encoder().encode( publicKey.getEncoded() );
	}
	
	public String getPrivateKeyString( PrivateKey privateKey )
	{
		return new BASE64Encoder().encode( privateKey.getEncoded() );
	}
	
	public PrivateKey getPrivateKeyFromString(String key) throws Exception
    {
		PrivateKey privateKey = null;
		try
		{
			KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
			EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(
					new BASE64Decoder().decodeBuffer(key));
			privateKey = keyFactory.generatePrivate(privateKeySpec);
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
        return privateKey;
    }
    
    public PublicKey getPublicKeyFromString(String key) throws Exception
    {
    	PublicKey publicKey = null;
    	try
    	{
    		KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
    		EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(
					new BASE64Decoder().decodeBuffer(key));
    		publicKey = keyFactory.generatePublic(publicKeySpec);
    	}
    	catch( Exception e )
    	{
    		e.printStackTrace();
    	}
        return publicKey;
    }
    
    public String encrypt(String text, PublicKey key) throws Exception
    {
        String encryptedText;
        try
        {
        	byte[] textBytes = text.getBytes("UTF8");
        	Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        	cipher.init(Cipher.ENCRYPT_MODE, key);
        	/*
        	 * Provided by Biju
        	 */
        	// RSA is limited to 117 bytes for encryption
    		int textBytesChunkLen = 100;
    		int encryptedChunkNum = (textBytes.length - 1) / textBytesChunkLen + 1;

    		// RSA returns 128 bytes as output for 100 text bytes
    		int encryptedBytesChunkLen = 128;
    		int encryptedBytesLen = encryptedChunkNum * encryptedBytesChunkLen;

    		// Define the Output array.
    		byte[] encryptedBytes = new byte[encryptedBytesLen];

    		int textBytesChunkIndex = 0;
    		int encryptedBytesChunkIndex = 0;

    		for (int i = 0; i < encryptedChunkNum; i++) 
			{
				if (i < encryptedChunkNum - 1) 
				{
					encryptedBytesChunkIndex = encryptedBytesChunkIndex
					+ cipher.doFinal(textBytes, textBytesChunkIndex,
							textBytesChunkLen, encryptedBytes,
							encryptedBytesChunkIndex);

					textBytesChunkIndex = textBytesChunkIndex
					+ textBytesChunkLen;
				}
				else 
				{
					cipher.doFinal(textBytes, textBytesChunkIndex,
							textBytes.length - textBytesChunkIndex,
							encryptedBytes, encryptedBytesChunkIndex);
				}
			}
    		encryptedText = new BASE64Encoder().encode(encryptedBytes);
        }
        catch (Exception e)
        {
            throw e;
        }
        return encryptedText;
    }
    
    public String decrypt(String text, PrivateKey key)
    {
        String result = null;
        try
        {
        	byte[] encryptedBytes = new BASE64Decoder().decodeBuffer(text);
        	Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        	cipher.init(Cipher.DECRYPT_MODE, key);
        	
        	int encryptedByteChunkLen = 128;
			int encryptedChunkNum = encryptedBytes.length
			/ encryptedByteChunkLen;
			int decryptedByteLen = encryptedChunkNum * encryptedByteChunkLen;
			byte[] decryptedBytes = new byte[decryptedByteLen];
			int decryptedIndex = 0;
			int encryptedIndex = 0;
        	
			for (int i = 0; i < encryptedChunkNum; i++) 
			{
				if (i < encryptedChunkNum - 1) 
				{
					decryptedIndex = decryptedIndex
					+ cipher.doFinal(encryptedBytes, encryptedIndex,
							encryptedByteChunkLen, decryptedBytes,
							decryptedIndex);
					encryptedIndex = encryptedIndex + encryptedByteChunkLen;
				} 
				else 
				{
					decryptedIndex = decryptedIndex
					+ cipher.doFinal(encryptedBytes, encryptedIndex,
							encryptedBytes.length - encryptedIndex,
							decryptedBytes, decryptedIndex);
				}
			}
			   System.out.println("DECRYPTED:");
			   System.out.println(new String(decryptedBytes).trim());
			   result = new String(decryptedBytes).trim();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return result;

    }
	
	public PublicKey getPublicKey() 
	{
		return keyPair.getPublic();
	}

	public PrivateKey getPrivateKey() 
	{
		return keyPair.getPrivate();
	}


}
