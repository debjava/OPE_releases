package com.ope.patu.security;

import java.security.spec.KeySpec;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import com.ope.patu.exception.OPESecurityException;

public class PBESecurityImpl implements OPESecurity
{
	private static final String pbeAlgorithm = "PBEWithMD5AndDES";
    private static final String defaultPassword = "ideal invent ope patu finland bank";
    
    private static byte[] salt = 
    {
        (byte) 0x56, (byte) 0x35, (byte) 0xE3, (byte) 0x03,
        (byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32
    };
    private static final int iterationCount = 5;
    
    private SecretKey secretKey;
    private PBEParameterSpec parameterSpec;
    private Cipher encryptCipher;
    private Cipher decryptCipher;
    
	protected PBESecurityImpl()
	{
		this( defaultPassword );
	}
	
	protected PBESecurityImpl( String password )
	{
		try 
        {
            parameterSpec = new PBEParameterSpec(salt, iterationCount);
            secretKey = createSecretKey(password);
            encryptCipher = createEncryptCipher();
            decryptCipher = createDecryptCipher();
        }
        catch (Exception e) 
        {
            throw new RuntimeException(e);
        }
	}
	
	private SecretKey createSecretKey(String secretKey) throws Exception 
    {
        KeySpec keySpec = new PBEKeySpec(secretKey.toCharArray());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(pbeAlgorithm);
        return keyFactory.generateSecret(keySpec);
    }
    
    private Cipher createEncryptCipher() throws Exception 
    {
        Cipher encryptCipher = Cipher.getInstance(pbeAlgorithm);
        encryptCipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);
        return encryptCipher;
    }
    
    private Cipher createDecryptCipher() throws Exception 
    {
        Cipher decryptCipher = Cipher.getInstance(pbeAlgorithm);
        decryptCipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);
        return decryptCipher;
    }


	public String getDecryptedString(String ecryptedString)
			throws OPESecurityException 
	{
		try 
        {
            return new sun.misc.BASE64Encoder().encode( encryptCipher.doFinal(ecryptedString.getBytes()) );
        }
        catch (Exception e) 
        {
            throw new OPESecurityException( "Normal Security Error in case of decryption");
        }
	}


	public String getEncryptedString(String originalValue)
			throws OPESecurityException 
	{
		try 
        {
            return new sun.misc.BASE64Encoder().encode(encryptCipher
					.doFinal(originalValue.getBytes()));
        }
        catch (Exception e) 
        {
        	throw new OPESecurityException( "Normal Security Error in case of encryption");
        }
	}

}
