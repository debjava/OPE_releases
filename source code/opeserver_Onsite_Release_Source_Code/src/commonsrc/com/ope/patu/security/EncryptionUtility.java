package com.ope.patu.security;

import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;
import java.security.MessageDigest;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;



import sun.misc.BASE64Encoder;


/** 
 * The purpose of this class is to encrypt and decrypt strings.  The encrypted
 * strings are also Base-64 encoded so they may be sent via HTTP.  This
 * implementation uses the DESede (triple DES) algorithm, and a key that is at
 * the bottom of this file.
 */
public class EncryptionUtility
{
	 /** Class logger */
    private static Logger logger = Logger.getLogger(EncryptionUtility.class);
    
    /**
     * Cipher used for both encrypting and decrypting strings.  Do not make
     * static unless access to this object is synchronized
     */
    private Cipher desCipher;
    /**
     * A representation of a DES-EDE key.  Do not make static unless access to
     * this object is synchronized
     */
    private DESedeKeySpec desedeKeySpec;
    /**
     * This factory will create secret keys based on desedeKeySpec. Do not make
     * static unless access to this object is synchronized
     */
    private SecretKeyFactory keyFactory;

   /** The byte array which stands for "PROCESSMATEPROCESSMATEPM." in English */
    private static final byte[] KEY_DATA =
            {
                (byte) 0x50, (byte) 0x52, (byte) 0x4f, 
				(byte) 0x43, (byte) 0x45, (byte) 0x53,
                (byte) 0x53, (byte) 0x4d, (byte) 0x41, 
				(byte) 0x54, (byte) 0x45,
                (byte) 0x50, (byte) 0x52, (byte) 0x4f, 
				(byte) 0x43, (byte) 0x45, (byte) 0x53,
                (byte) 0x53, (byte) 0x4d, (byte) 0x41, 
				(byte) 0x54, (byte) 0x45,
				(byte) 0x50, (byte) 0x4d

            };

    /** Series of operations to perform when encrypting string */
    private static final String TRANSFORMATION = "DESede/ECB/PKCS5Padding";
    /** Algorithm to use */
    private static final String ALGORITHM = "DESede";
    private static final String HASH      = "MD5";
    /**
     * Initialize the utility.
     * @throws EncryptionException Thrown if the encryption implementation could
     *     not be initalized
     */
    public EncryptionUtility()
    {
    	try{
    		System.out.println("EncryptionUtility :: Calling : addProvider()");
    		addProvider();
    	} catch(Exception e){
    		e.printStackTrace();
    	}
        try
        {
            desCipher = Cipher.getInstance(TRANSFORMATION);
            desedeKeySpec = new DESedeKeySpec(KEY_DATA);
            keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
        }
        catch (GeneralSecurityException ex)
        {
            System.out.println("EncryptionUtility initializion failed : "+ex);
            
        }
    }

    
	/**This method is used to add the Sun JCE provider
	 * dynamically.
	 * @author Debadatta Mishra
	 */
	public static void addProvider()
	{
		try
		{
			boolean sunProviderFlag = false;
			Provider[] providers = Security.getProviders();
			System.out.println("Initial default providers list"+Arrays.asList(providers));
			logger.debug("Initial default providers list"+Arrays.asList(providers));
			for( int i = 0 ; i < providers.length ; i++)
			{
				
				System.out.println("Provider Name at position [ "+(i+1)+" ] "+providers[i].getName());
				logger.debug("Provider Name at position [ "+(i+1)+" ] "+providers[i].getName());
				if( i == 0 && providers[i].getName().equalsIgnoreCase("Sun")&& providers[i].getVersion() >= 1.5 )
				{
					sunProviderFlag = true;
					System.out.println("Sun Provider is found in the first position");
					logger.debug("Sun Provider is found in the first position");
					break;
				}
				else if( i == 0 && providers[i].getName().equalsIgnoreCase("SunJCE")&& providers[i].getVersion() >= 1.5 )
				{
					sunProviderFlag = true;
					System.out.println("SunJCE Provider is found in the first position");
					logger.debug("SunJCE Provider is found in the first position");
					break;
				}
				else if( providers[i].getName().equalsIgnoreCase("SunJCE") && providers[i].getVersion() >= 1.5 )
				{
					System.out.println("Coming to SunJCE if block");
					logger.debug("Coming to SunJCE if block");
					Provider tempProvider = Security.getProvider(providers[i].getName());
					Security.removeProvider(providers[i].getName());
					System.out.println("After Removal of SunJCE provider===>"+Arrays.asList(Security.getProviders()));
					logger.debug("After Removal of SunJCE provider===>"+Arrays.asList(Security.getProviders()));
					Security.insertProviderAt(tempProvider, 1);
					sunProviderFlag = true;
					break;
				}
				else if( providers[i].getName().equalsIgnoreCase("Sun") && providers[i].getVersion() >= 1.5 )
				{
					System.out.println("Coming to this SUN if block");
					logger.debug("Coming to this SUN if block");
					Provider tempProvider = Security.getProvider(providers[i].getName());
					Security.removeProvider(providers[i].getName());
					System.out.println("After Removal of SUN provider===>"+Arrays.asList(Security.getProviders()));
					logger.debug("After Removal of SUN provider===>"+Arrays.asList(Security.getProviders()));
					Security.insertProviderAt(tempProvider, 1);
					sunProviderFlag = true;
					break;
				}
				
			}
			System.out.println("New provider list===>"+Arrays.asList(Security.getProviders()));
			logger.debug("New provider list===>"+Arrays.asList(Security.getProviders()));
			if( sunProviderFlag == false )
			{
				System.out.println("No SunJCE provider was found");
				logger.debug("No SunJCE provider was found");
				/*
				 * It means that no sun jce is found.
				 * So we have to load it dynamically.
				 */
				Provider sunJce = new com.sun.crypto.provider.SunJCE();
		        Security.addProvider(sunJce);
		        System.out.println("SunJCE provider was added to the providers list");
		        logger.debug("SunJCE provider was added to the providers list");
			}
		}
		catch( Exception e )
		{
			e.printStackTrace();
			System.out.println(e);
			logger.error(e);
			logger.error(e.getMessage());
			System.out.println(e.getMessage());
		}
	}
    
    
    /**
     * This method will take a String, encrypt it, then base 64 encode it.  The
     * result will be another String.
     *
     * @param plainText The text to encrypt.
     * @return Encrypted, encoded string.  If the input is null, the method
     *         returns null.
     * @throws EncryptionException Thrown if the encryption failed
     */
    public String encrypt(String plainText)
    {
		byte[] encoded = null;
        if (plainText == null)
            return null;

        try
        {
			byte[] bytes = chars2Bytes(plainText.toCharArray());
            byte[] encrypted = doCipher(bytes, true);

            // Encryption will produce binary data that may contain control or
            // otherwise invalid characters that would break HTTP.  Therefore,
            // we encode the data into a HTTP-safe format.
            encoded = Base64.encodeBase64(encrypted);            
        }
        catch (GeneralSecurityException ex)
        {
           System.out.println("Unable to encrypt string "+ ex);
            
        }
		return new String(encoded);
    }

    /**
     * This method takes a string created by the encrypt method and returns it
     * to it's original form.
     * @param input A base64 encode, encrypted String
     * @return The original (decrypted) text.  If the input is null, the method
     *         returns null
     * @throws EncryptionException Thrown if the decryption failed
     */
    public String decrypt(String input)
    {
		byte[] decryptedBytes = null;

        if (input == null)
            return null;

        try
        {
            // Return the base-64 encoded value back into the raw binary
            // encrypted form, then decrypt it
            byte[] encryptedBytes = Base64.decodeBase64(input.getBytes());
            decryptedBytes = doCipher(encryptedBytes, false);            
        }
        catch (GeneralSecurityException ex)
        {
            System.out.println("Unable to decrypt string '" + input + "' : "+ ex);
            
        }
		return new String(bytes2Chars(decryptedBytes));
    }

    /**
     * This method actually performs the encryption/decryption.  The method is
     * synchronized because state is set up on desCipher at the start of the
     * method, and that state must not change before the end of the method.
     *
     * @param input    Byte array containing data to encrypt/decrypt
     * @param encrypt  If true, encrypt the input.  If false, decrypt the input
     * @return         Encrypted or decrypted form of the input
     * @throws GeneralSecurityException Thrown by underlying encryption API
     */
    private synchronized byte[] doCipher(byte[] input, boolean encrypt)
            throws GeneralSecurityException
    {
        SecretKey desKey = keyFactory.generateSecret(desedeKeySpec);
        if (!encrypt)
        {
            desCipher.init(Cipher.DECRYPT_MODE, desKey);
        }
        else
        {
            desCipher.init(Cipher.ENCRYPT_MODE, desKey);
        }

        return desCipher.doFinal(input);
    }

    /**
     * This method will convert a byte value in the range -128 - 127 to a char
     * in the range 0-255.  This is used by the method bytes2Chars to compress
     * two bytes into a single character.
     *
     * @param  b byte to convert
     * @return A character represntation of that byte.
     */
    private char byte2Char(byte b)
    {
        // This mask is used to filter out the highest bit of a byte
        byte highestBitMask = (byte) 0x3f;

        int i = b >= 0 ? b : ((b & highestBitMask) - 128);
        return (char) i;
    }

    /**
     * Transfer a byte array, which contains even number of bytes, into an
     * array of chars. Every two bytes in the byte array will be merged into
     * a char. The first byte (from left to right) will be stored exactly as
     * it is in binary format into the high-byte of the char. The second byte
     * of the bytes pair will be stored in the low byte of the char.
     *
     * @param bytes An array of bytes (produced by chars2Bytes)
     * @return An array of characters that the input represents
     * @throws IllegalArgumentException if the number of bytes in the byte array
     *         is not even.
     */
    private char[] bytes2Chars(byte[] bytes)
    {
        if (bytes == null)
            return null;

        int count = bytes.length;
        if (count % 2 != 0)
            throw new IllegalArgumentException("The number of bytes " + count +
                    " is not even");

        int charsLen = count / 2;
        char[] chars = new char[charsLen];

        for (int i = 0; i < charsLen; i++)
        {
            char highChar = (char) (byte2Char(bytes[2 * i + 1]) << 8);
            char lowChar = byte2Char(bytes[2 * i]);
            chars[i] = (char) (highChar | lowChar);
        }

        return chars;
    }

    /**
     * This is the reverse processing of bytes2Chars.
     *
     * @param chars Array of characters to turn into bytes
     * @return Array of bytes representing the characters input.  Each character
     *         will be represented by two bytes.
     */
    private byte[] chars2Bytes(char[] chars)
    {
        // This mask is used to filter out the lower byte of a char
        char highByteMark = '\uff00';

        if (chars == null) return null;
        if (chars.length < 1) return new byte[0];

        byte[] bytes = new byte[chars.length * 2];

        for (int i = 0; i < chars.length; i++)
        {
            // Split the char into two bytes. The high-byte will be stored
            // in the higher order element in the byte array.
            char c = chars[i];
            byte highByte = (byte) ((c & highByteMark) >> 8);
            byte lowByte = (byte) c;

            bytes[i * 2 + 1] = highByte;
            bytes[i * 2] = lowByte;
        }

        return bytes;
    }
    
    
    /**
	 * Remove this method in the Datamatrice-Client
	 */
    public String getHash(String input) {
      try {
        MessageDigest digest = MessageDigest.getInstance(HASH);
        byte[] inputBytes = input.getBytes();
        byte[] hashBytes = new byte[inputBytes.length + KEY_DATA.length + KEY_DATA.length];
        System.arraycopy(KEY_DATA, 0, hashBytes, 0, KEY_DATA.length);
        System.arraycopy(inputBytes, 0, hashBytes, KEY_DATA.length, inputBytes.length);
        System.arraycopy(KEY_DATA, 0, hashBytes, KEY_DATA.length + inputBytes.length, KEY_DATA.length);
        return new BASE64Encoder().encode(digest.digest(hashBytes));
      } catch (NoSuchAlgorithmException e) {
      }
      return null;
    }
}
