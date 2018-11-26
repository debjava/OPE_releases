package com.ope.patu.unittest.keys;

import javax.crypto.SecretKey;

import com.ope.patu.key.MACGenerator;
import com.ope.patu.util.DESUtil;

public class TestMAC 
{
	public static void main(String[] args) 
	{
		String str = ">>ESI161120     BANKSOFT    4.10SMH00370844052922227        WMDATA20101302204        11080715124248123                                          ";
		try
		{
			for( int i = 0 ; i < 100 ; i++ )
			{
				MACGenerator macGenerator = new MACGenerator();
				SecretKey key = DESUtil.getsecretKey();
//				macGenerator.generateMAC( str,key.getEncoded() );
				String macString = macGenerator.generateMAC(str, key
						.getEncoded());// macGenerator.getMacString();
				System.out.println(i+"  MAC String-------"+macString);
			}
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}
}
