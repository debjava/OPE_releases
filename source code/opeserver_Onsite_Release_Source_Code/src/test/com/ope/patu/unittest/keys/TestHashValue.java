package com.ope.patu.unittest.keys;

import java.io.File;

import javax.crypto.SecretKey;

import com.ope.patu.key.HSKGenerator;
import com.ope.patu.key.MACGenerator;
import com.ope.patu.util.CommonUtil;
import com.ope.patu.util.DESUtil;
import com.ope.patu.util.FileUtil;

public class TestHashValue 
{
	public static String getFileContents()
	{
		String filePath = System.getProperty("user.dir") + File.separator
		+ "testdata" + File.separator + "billpaymentdata11.txt";
		String contents = null;
		contents = FileUtil.getLineByLineFileContents(filePath);
		return contents;
	}
	
	public static void main(String[] args) 
	{
		try
		{
//			HSKGenerator hskGenerator = new HSKGenerator();
//			hskGenerator.generateHSK();
//			byte[] hskKeyByte = hskGenerator.getHskBytes();
//			/*
//			 * Encrypt the HSK using DES with the Secret key 
//			 * as KEK.
//			 */
//			String kekHexString = "5d02cd85c88c97e0" ;
//			byte[] hexBytes = CommonUtil.hexToBytes(kekHexString);
//			SecretKey sKey = DESUtil.getsecretKey(hexBytes);
//			byte[] encryptedHskBytes = DESUtil.getEncryptedBytes(hskKeyByte, sKey);
//			String hexEnStr = CommonUtil.bytesToHex( encryptedHskBytes );
//			System.out.println("Now HSK value in SUO and VAR-----"+hexEnStr);
			
			
			/*
			 * The following is the HSK which is available
			 * at SUO and VAR message
			 */
			//F943B03DBF7E685B
			String hskStr = "F943B03DBF7E685B";
			byte[] hskByte = CommonUtil.hexToBytes( hskStr );
			
			String fileContents = getFileContents();
			/*
			 * Remove all the line feeds
			 */
			String strNoLineFeed = fileContents.replaceAll("\n", "");
			MACGenerator macGen = new MACGenerator();
//			macGen.generateMAC(strNoLineFeed,hskKeyByte);
//			macGen.generateMAC(strNoLineFeed,hskByte);
			String macString = macGen.generateMAC(strNoLineFeed,hskByte);// macGen.getMacString();
			System.out.println("Hash Value computed for message group---------"+macString);
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}

}
