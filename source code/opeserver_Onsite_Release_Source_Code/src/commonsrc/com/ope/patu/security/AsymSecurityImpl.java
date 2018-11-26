package com.ope.patu.security;

import java.security.PrivateKey;
import java.security.PublicKey;

import com.ope.patu.exception.OPESecurityException;

public class AsymSecurityImpl implements OPESecurity
{
	public String getDecryptedString(String ecryptedString)
			throws OPESecurityException 
	{
		
		String deString = null;
		try
		{
			PATUSecurityUtil securityUtil = new PATUSecurityUtil();
			String privateString = KeyReader.getPrivateKeyString();
			PrivateKey priKey = securityUtil.getPrivateKeyFromString(privateString);
			deString = securityUtil.decrypt(ecryptedString, priKey );
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		return deString;
	}
	
	public String getEncryptedString(String originalValue)
			throws OPESecurityException 
	{
		String enString = null;
		try
		{
			PATUSecurityUtil securityUtil = new PATUSecurityUtil();
			String publicString = KeyReader.getPublicKeyString();
			PublicKey pubKey = securityUtil.getPublicKeyFromString(publicString);
			enString = securityUtil.encrypt(originalValue, pubKey);
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		return enString;
	}
}
