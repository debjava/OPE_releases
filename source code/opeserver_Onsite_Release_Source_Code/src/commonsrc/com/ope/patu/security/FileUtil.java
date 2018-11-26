package com.ope.patu.security;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

public class FileUtil 
{
	public static String getFileContents( String filePath )
	{
		String fileContents = null;
		try
		{
			FileReader fr = new FileReader( filePath );
			BufferedReader buffReader = new BufferedReader( fr );
			String tmp;
			StringBuffer sb = new StringBuffer();
			while((tmp = buffReader.readLine()) != null)
			{
				sb.append(tmp);
			}
			buffReader.close();
			fr.close();
			fileContents = sb.toString();
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		return fileContents;
	}
	
	public static void writeToFile( String filePath , String fileContents )
	{
		FileWriter writer = null;
		BufferedWriter bf = null;
		try
		{
			writer = new FileWriter( filePath );
			bf = new BufferedWriter( writer );
			bf.write( fileContents );
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				bf.close();
				writer.close();
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
		}
	}
}
