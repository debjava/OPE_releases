package com.ope.patu.common;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.LineNumberReader;

public class TestString 
{
	public static String pad(Object str, int padlen, String pad)
	{
		String padding = new String();
		try{
			int len = Math.abs(padlen) - str.toString().length();
			if (len < 1)
				return str.toString();
			for (int i = 0 ; i < len ; ++i)
				padding = padding + pad;
		}catch(NullPointerException npe){
			npe.printStackTrace();
		}
		return (padlen < 0 ? padding + str : str + padding);
	}
	
	public static String appendString( String actualString , String charToAppend , int maxLength )
	{
		String appendedString = null;
		return appendedString;
	}
	public static String getInternalCodeString( String str )
	{
		String tempStr = str.toUpperCase();
		StringBuilder internalBuilder = new StringBuilder();
		for( int i = 0 , n = tempStr.length() ; i < n ; i++ )
		{
			char ch = tempStr.charAt(i);
			int decimalVal = ( int )ch;
			String hexString = Integer.toHexString( decimalVal );
			internalBuilder.append( hexString );
		}
		return internalBuilder.toString().toUpperCase();
	}
	
	public static String removeSpecialChars( String str )
	{
		String removedStr = null;
		String pattern = "[^a-zA-Z0-9%()*+,-./:;<=>]";
		removedStr = str.replaceAll(pattern, " ");
		return removedStr;
	}
	
	public static void main(String[] args) throws Exception
	{
//		String str = "LM021029501800000014HŽMEEN LŽŽNINVEROVIRASTO%";
//		System.out.println("Original String-----"+str);
//		String removedStr = removeSpecialChars(str);
//		System.out.println("RemovedStr-----"+removedStr);
		
//		String kui = "LMP300    euro001000001000000000000    +                       080910184117000    ";
//		String fileType = kui.substring(0,10);
//		String curr = kui.substring(10,14);
//		String date = kui.substring(63, 69);
//		System.out.println(date);
		
		
//		String filePath = "D:/opetestdata/TR_BILL";
//		String filePath = "D:/opetestdata/Samplebill_wip.txt";
//		FileReader fr = new FileReader( filePath ) ;
//		BufferedReader br = new BufferedReader( fr );
//		String str = br.readLine();
		
//		LineNumberReader ln = new LineNumberReader(br);
//		int count = 0;
//		while (ln.readLine()!=null)
//		{
//			count++;
//		}
//		System.out.println("Total Lines----"+count);
		
		String s = " ";
		s = pad(s, 1, "0");
		System.out.println(s);
		
	}
}
