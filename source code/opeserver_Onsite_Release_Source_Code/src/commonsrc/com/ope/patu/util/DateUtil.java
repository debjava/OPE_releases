package com.ope.patu.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.text.ParseException;
import org.apache.log4j.Logger;

public class DateUtil 
{
	protected static Logger logger = Logger.getLogger(DateUtil.class);
	public static long getDifference( String firstDateString , String secondDateString )
	{
		long diffValue = 0;
		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat( "yyMMddhhmmss");
			Date date1 = sdf.parse( firstDateString );
			Date date2 = sdf.parse( secondDateString );

			Calendar calendar1 = Calendar.getInstance();
			Calendar calendar2 = Calendar.getInstance();
			calendar1.setTime(date1);
			calendar2.setTime(date2);
			long diff = (calendar2.getTimeInMillis()) - (calendar1.getTimeInMillis());
			diffValue = diff / (24*60*60*1000);
		}
		catch( Exception e )
		{
			e.printStackTrace();
			logger.error(e);
		}
		return diffValue;
	}

	public static java.sql.Timestamp getTimeStamp( String dateStr , String format )
	{
		java.sql.Timestamp timeStamp = null;
		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat( format );
			Date date = sdf.parse(dateStr);
			timeStamp = new Timestamp(date.getTime());
		}
		catch( Exception e )
		{
			e.printStackTrace();
			logger.error(e);
		}
		return timeStamp;
	}

	public static String getDate()
	{
		return new SimpleDateFormat("yyMMdd").format(new Date());
	}

	public static String getDate( String format )
	{
		return new SimpleDateFormat( format ).format(new Date());
	}

	public static String GetDateTime() {

		SimpleDateFormat sdfDate = new SimpleDateFormat("yyMMdd");
		SimpleDateFormat sdfTime = new SimpleDateFormat("HHmmss");

		Date now = new Date();

		String strDate = sdfDate.format(now);
		String strTime = sdfTime.format(now);

		System.out.println("Date: " + strDate);
		System.out.println("Time: " + strTime);
		String dateTime = strDate+strTime;
		return dateTime;    
	}

	public static String GetCurrentDateTime() 
	{
		SimpleDateFormat sdfDate = new SimpleDateFormat("dd.MM.yy");
		SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
		Date now = new Date();
		String strDate = sdfDate.format(now);
		String strTime = sdfTime.format(now);
		System.out.println("Date: " + strDate);
		System.out.println("Time: " + strTime);
		String dateTime = strDate+"  "+strTime;
		return dateTime;    
	}
	
	public static java.sql.Date parseDate(String dt)
	{
		java.util.Date parsedDate_=null;
		if(dt!=null )
		{
			if(dt.length()!=0){
				  try{
				  SimpleDateFormat sdf=new SimpleDateFormat("yy.MM.dd");
				  parsedDate_= sdf.parse(dt);
				  System.out.println(parsedDate_);
				  } 
				  catch(java.text.ParseException pe){
					  System.out.println("Unable to parse the date");
				  }
				  return new java.sql.Date(parsedDate_.getTime());
				}else
				{
					return null;
				}
		}
		else{
			System.out.println("return value is "+null);
		return null;
		}
	}
	
	public static java.sql.Date parseFormatDate(String date,String receivedDateFormat, String expectedDateFormat)
	{
		java.util.Date parsedDate=null;
		if(date!=null )
		{
			if(date.length()!=0){
				try {
				  SimpleDateFormat sdf=new SimpleDateFormat(receivedDateFormat);
				  System.out.println("format->"+receivedDateFormat);
				  parsedDate= sdf.parse(date);
				  System.out.println("parsedDate->"+parsedDate);
				} catch(java.text.ParseException pe)	{
						  System.out.println("Unable to parse the date");
				}
				return new java.sql.Date(parsedDate.getTime());
			}	else {	return null;	}
		}	else	{
			System.out.println("return value is "+null);
			return null;
		}
	 }
	
	public static String getTime()
	{
		String timeString = null;
		try
		{
			Date dt = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
			timeString = sdf.format(dt);
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		return timeString;
	}
	
	/**This method is used to check the correctness of a date.
	 * This method return false for the following dates.
	 * 31-Nov-2008
	 * 31-Apr-2008
	 * @param date of type String indicating the date.
	 * @param format of type String indicating the format
	 * @return true if the date is valid
	 * @author Debadatta Mishra
	 */
	public static boolean isDateValid( String date , String format )
	{
		boolean dateFlag = false;
		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat( format );
			sdf.setLenient(false);
			sdf.parse(date);
			dateFlag = true;
		}
		catch( NullPointerException npe )
		{
			logger.error("Date is null, it should not be null");
		}
		catch( ParseException pe )
		{
			logger.error("Not a valid date, ParseException thrown");
		}
		catch( Exception e )
		{
			logger.error("Not a valid date, error in executing the function");
		}
		return dateFlag;
	}


}
