package com.ope.patu.util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.log4j.Logger;

public class CommonUtil 
{
	protected static Logger logger = Logger.getLogger( CommonUtil.class );
	public static Map<Integer, String> getFileSequence()
	{
		Map<Integer, String> sequenceMap = new HashMap<Integer, String>();
		sequenceMap.put(1, "ESIp.txt");
		sequenceMap.put(2, "billpmt.akn");
		sequenceMap.put(3, "billpmt.fbk");
		return sequenceMap;
	}

	public static String getFileName( int value )
	{
		return getFileSequence().get(value);
	}

	public static String getInternalCodeString( String str )
	{

		//Convert the entire String into upper Case
		//tempStr = tempStr.toUpperCase();
		// Remove Special Characters
		str = removeSpecialChars(str);
		// Build the Hex String which is the internal character representation
		StringBuilder internalBuilder = new StringBuilder();
		for (int i = 0, n = str.length(); i < n; i++) {
			String hexString = Integer.toHexString((int) str.charAt(i));
			internalBuilder.append(hexString);
		}
		return internalBuilder.toString();
	}

	/**
	 * Return an hex representation of the string which is passed in 1) Replace
	 * special characters 2) Convert to Hex Value 3) In case of length less than
	 * blocksize, append zeroes
	 * 
	 * @param tempStr
	 * @return
	 */
	public static String returnHexString(String tempStr) {

		// Remove Special Characters
		tempStr = removeSpecialChars(tempStr);

		// Build the Hex String which is the internal character representation
		StringBuffer tempBuffer = new StringBuffer();
		for (int i = 0, n = tempStr.length(); i < n; i++) {
			int decimalVal = (int) tempStr.charAt(i);
			tempBuffer.append(Integer.toHexString(decimalVal));
		}
		if (tempBuffer.length() < 16) {
			for (int i = tempBuffer.length(); i < 16; i += 2)
				tempBuffer.append("00");
		}
		return tempBuffer.toString();
	}

	public static String[] getPartitionString( String str , int blocksize )
	{

		// Convert the entire String into upper Case
		// str = str.toUpperCase();

		System.out.println("Total String length::" + str.length());
		int stlen = str.length();
		int reminder = 0;
		reminder = stlen % blocksize;

		// Initialize String Array with appropriate size
		String[] arrStr = null;
		int loopVal = stlen / blocksize;
		if (reminder != 0) {
			arrStr = new String[loopVal + 1];
		} else {
			arrStr = new String[loopVal];
		}
		for (int i = 0; i < loopVal; i++) {
			// Get the 8 character String and convert it into an Hex String
			arrStr[i] = returnHexString(str.substring((i * blocksize), (i + 1)
					* blocksize));
		}
		// This is in case of the last String
		if (reminder != 0) {
			arrStr[loopVal] = returnHexString(str.substring(
					loopVal * blocksize, stlen));
		}
		return arrStr;
	}

	/**
	 * Converts byte array into String
	 * @param data
	 * @return string
	 */
	public static String bytesToHex(byte[] data) 
	{
		if (data==null) 
		{
			return null;
		}
		else 
		{
			int len = data.length;
			String str = "";
			for (int i=0; i<len; i++) 
			{
				if ((data[i]&0xFF)<16) str = str + "0" 
				+ java.lang.Integer.toHexString(data[i]&0xFF);
				else str = str
				+ java.lang.Integer.toHexString(data[i]&0xFF);
			}
			return str.toUpperCase();
		}
	}

	/**
	 * Converts String into byte array
	 * @param str
	 * @return byte array
	 */
	public static byte[] hexToBytes(String str) 
	{
		if (str==null) 
		{
			return null;
		}
		else if (str.length() < 2) 
		{
			return null;
		}
		else 
		{
			int len = str.length() / 2;
			byte[] buffer = new byte[len];
			for (int i=0; i<len; i++)
			{
				buffer[i] = (byte) Integer.parseInt(
						str.substring(i*2,i*2+2),16);
			}
			return buffer;
		}
	}

	public static String removeSpecialChars( String str )
	{
		String removedStr = null;
		String pattern = "[^a-zA-Z0-9%()*+,-./:;<=>]";
		removedStr = str.replaceAll(pattern, " ");
		return removedStr;
	}

	/**
	 * anandkumar b
	 * This method is written for padding the ASCII character to empty space in feed back file and acknowledgment file   
	 * @param str
	 * @param padlen
	 * @param pad
	 * @return
	 */
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

	
	public static List<String> getLinesList( String fileContents )
	{
		List< String > recordsList = new LinkedList<String>();
		String[] contents = fileContents.split("\\n");
		for( String str : contents )
			recordsList.add(str);
		return recordsList;
	}

	public static String getFirstLine(String dataContents)
	{
		String firstLine = null;
		try {
			Scanner lineScanner = new Scanner(dataContents);
			firstLine = lineScanner.nextLine();
		}
		catch( NullPointerException npe )
		{
			npe.printStackTrace();
			logger.error(npe);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			logger.error(e);
		}
		return firstLine;
	}



}
