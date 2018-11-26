package com.ope.patu.unittest.keys;

import java.io.File;

import com.ope.patu.exception.HSKGeneratorException;
import com.ope.patu.exception.MACException;
import com.ope.patu.key.HSKGenerator;
import com.ope.patu.key.MACGenerator;
import com.ope.patu.util.CommonUtil;
import com.ope.patu.util.FileUtil;


public class TestHashValueGenerator 
{
	public static String getFileContents()
	{
		String filePath = System.getProperty("user.dir") + File.separator
		+ "testdata" + File.separator + "billpaymentdata11.txt";
		String contents = null;
		contents = FileUtil.getLineByLineFileContents(filePath);
		return contents;
	}

	public static String removeSpecialChars( String str )
	{
		String removedStr = null;
		String pattern = "[^a-zA-Z0-9]";
		removedStr = str.replaceAll(pattern, "");
		return removedStr;

	}

	public static void main(String[] args) throws HSKGeneratorException, MACException, Exception
	{
		String fileContents = getFileContents();
		/*
		 * Remove all the line feeds
		 */
		String strNoLineFeed = fileContents.replaceAll("\n", "");
		/*
		 * Get the encrypted HSK from SUO or VAR message
		 */
		String hskHexString = "F943B03DBF7E685B";
		byte[] hskKeyByte = CommonUtil.hexToBytes( hskHexString );
		System.out.println("HSK String-------"+hskHexString);
		System.out.println("hsk bytes length--------"+hskKeyByte.length);
		/*
		 * Perform the MAC operation
		 */		
		MACGenerator macGen = new MACGenerator();
//		macGen.generateMAC(strNoLineFeed,hskKeyByte);
		String macString = macGen.generateMAC(strNoLineFeed, hskKeyByte);
				//macGen.getMacString();
		System.out.println("MAC String-------"+macString);

	}
}
