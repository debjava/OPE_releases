package com.ope.patu.messages;

import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import javax.crypto.SecretKey;
import org.apache.log4j.Logger;
import org.bouncycastle.util.encoders.Hex;

import com.ope.patu.exception.MACException;
import com.ope.patu.key.MACGenerator;
import com.ope.patu.server.constant.SecurityMessageConstants;
import com.ope.patu.util.CommonUtil;
import com.ope.patu.util.DESUtil;
import com.ope.patu.util.ParityUtil;

public class CommonHandler 
{
	protected static Logger logger = Logger.getLogger(CommonHandler.class);

	public static String getComputedAuthValue( String strToAuth , String aukString )
	{
		String authenticationValue = null;
		try
		{
			if( strToAuth == null || aukString == null ) throw new NullPointerException();
			byte[] aukBytes = CommonUtil.hexToBytes(aukString);
			SecretKey key = DESUtil.getsecretKey(aukBytes);//This should be the AUK
			MACGenerator macGenerator = new MACGenerator();
			authenticationValue = macGenerator.generateMAC(strToAuth.toUpperCase(), key.getEncoded());
			logger.debug("Authentication value ------"+authenticationValue);
		}
		catch( NullPointerException npe )
		{
			npe.printStackTrace();
			logger.error( npe );
			//Added by Debadatta Mishra
			authenticationValue = CommonUtil.pad(" ", 16, " ");
		}
		catch( NoSuchAlgorithmException nse )
		{
			nse.printStackTrace();
			logger.error( nse );
		}
		catch( Exception e )
		{
			e.printStackTrace();
			logger.error( e );
		}
		return authenticationValue;
	}

	public static boolean isAuthenticationTrue(String actualValue,
			String valueToAuthenticate, String key)
	{
		boolean flag = false;
		try
		{
			MACGenerator macGenerator = new MACGenerator();
			/*
			 * Get the AUK and pass to the method
			 * to generate the AUK.
			 * Get the AUK as String and form
			 * the SecretKey object.
			 */
			String aukString = key ;
			logger.debug("AUK String for authentication------------"+aukString);
			byte[] aukBytes = CommonUtil.hexToBytes(aukString);
			SecretKey secretKey = DESUtil.getsecretKey(aukBytes);//This should be the AUK
			String macString = macGenerator.generateMAC(valueToAuthenticate.toUpperCase(), secretKey
					.getEncoded());
			logger.debug("Actual Authentication value------"+actualValue );
			logger.debug("Computed MAC String-------"+macString);
			if( macString.equalsIgnoreCase( actualValue ) )
				flag = true;
			else
			{
				flag = false;
				throw new Exception("Authentication values mismatch");
			}
		}
		catch( NullPointerException npe )
		{
			npe.printStackTrace();
			logger.error( npe );
			flag = false;
		}
		catch( NoSuchAlgorithmException nse )
		{
			nse.printStackTrace();
			logger.error( nse );
			flag = false;
		}
		catch( Exception e )
		{
			flag = false;
			e.printStackTrace();
		}
		logger.debug("ESI message isAuthenticationTrue validation-------->>>"+flag);
		return flag;
	}

	public static boolean isHSKParityOdd( String hskString )
	{
		boolean flag = false;
		byte[] hskBytes = CommonUtil.hexToBytes(hskString);
		flag = ParityUtil.isParityOdd(hskBytes);
		return flag;
	}

	public static boolean isHashValueSame(String actualHashValue,
			String hskKey, String actualDataContents, String KEK)
	{
		boolean flag = false;
		/*
		 * Make operation with the actual data contents to remove the
		 * line feeds.
		 */
		try
		{
			Scanner scan = new Scanner(actualDataContents);
			StringBuffer sb = new StringBuffer();
			while( scan.hasNext() )
			{
				sb.append(scan.nextLine().trim());
			}
			String strForHashValue = sb.toString();
			SecretKey sk = DESUtil.getSecretKey(CommonUtil.hexToBytes(KEK), SecurityMessageConstants.DES);
			byte[] resultHSK = DESUtil.getDecryptedBytesWithIV(CommonUtil
					.hexToBytes(hskKey), sk);
			String encryptionHSK = new String(Hex.encode(resultHSK));
			MACGenerator macObj = new MACGenerator();
			String generatedValue = macObj.generateMAC(strForHashValue
					.toUpperCase(), encryptionHSK);
			logger.debug("Generated Hash value for VAR----"+generatedValue);
			logger.debug("From the VAR message Hash value-----"+actualHashValue );
			if( actualHashValue.equals(generatedValue))
				flag = true;
			else
				flag = false;
		}
		catch( NullPointerException npe )
		{
			npe.printStackTrace();
			logger.error( npe );
			flag = false;
		}
		catch( MACException me )
		{
			me.printStackTrace();
			logger.error( me );
			flag = false;
		}
		catch( Exception e )
		{
			e.printStackTrace();
			logger.error( e );
			flag = false;
		}
		return flag;
	}

}
