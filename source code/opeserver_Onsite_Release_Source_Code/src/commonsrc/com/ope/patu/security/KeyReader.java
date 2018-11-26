package com.ope.patu.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class KeyReader 
{
	public static String getPublicKeyString()
	{
		String publicString = null;
		try
		{
			Properties prop = new Properties();
			String publicKeyPath = System.getProperty("user.dir")
					+ File.separator + "keys" + File.separator + "public.key";
			InputStream in = new FileInputStream( publicKeyPath );
			prop.load( in );
			publicString = prop.getProperty("key");
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		return publicString;
	}
	
	public static String getPrivateKeyString()
	{
		String publicString = null;
		try
		{
			Properties prop = new Properties();
			String publicKeyPath = System.getProperty("user.dir")
					+ File.separator + "keys" + File.separator + "private.key";
			InputStream in = new FileInputStream( publicKeyPath );
			prop.load( in );
			publicString = prop.getProperty("key");
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		return publicString;
	}
}
