package com.dnb.agreement.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Date;
import com.dnb.agreement.common.OPEConstants;


/**
 * As the name implies - generic String functionality provided
 * by static methods.
 */
public class DateUtils
{
    /**
     * System-dependant line separator (newline character(s))
     */
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");

    /**
     * Hide the public constructor
     */
    private DateUtils()
    {
    }
    /**
     * Parses a string date and returns java.sql.Date in the 'yyyy-MM-dd' format
     * @param format : format of the stringdate
     * @param date
     * @return
     */
    public static Date parseDate(String format, String date){
   		java.util.Date utilDate=null;
   		Date sqlDate=null;
   		try {
   			SimpleDateFormat sdf= new SimpleDateFormat(format);
   			utilDate=sdf.parse(date);
   			sqlDate = new Date(utilDate.getTime());
   		} catch (ParseException e) {
   			e.printStackTrace();
   		}
   		return sqlDate;	   		
   	}
    
    /**
     * <p align="justified">
     * This method is used to obtain a String of date from the
     * java.sql.TimeStamp. While retrieving the date type data,
     * it comes in an object of type java.sql.TimeStamp.
     * </p>
     * @author debadatta.mishra
     * @param timeStamp
     * @return
     */
    public static String getStringDateStringFromTimeStamp(
			java.sql.Timestamp timeStamp) 
    {
		String stringDate = null;
		String strDate = timeStamp.toString();
		String[] temp = strDate.split("[ ]");
		stringDate = temp[0];
		return stringDate;
	}
    
    /**
     * @author Debadatta.Mishra
     * @param dateTimeString
     * @return
     */
    public static java.sql.Date getSQLDateFromTimeStampString( String dateTimeString )
    {
    	java.sql.Date sqlDate = null;
    	try
    	{
    		java.sql.Timestamp timeStamp = new java.sql.Timestamp( 0 ).valueOf( dateTimeString );
    		sqlDate = new java.sql.Date( timeStamp.getYear(),timeStamp.getMonth(),timeStamp.getDay());
    	}
    	catch( Exception e )
    	{
    		e.printStackTrace();
    	}
    	return sqlDate;
    }
    
    /**
     * @author Debadatta.Mishra
     * @param timeStamp
     * @return
     */
    public static java.sql.Date getSQLDateFromQLTimeStamp( java.sql.Timestamp timeStamp )
    {
    	java.sql.Date sqlDate = null;
    	try
    	{
    		sqlDate = new java.sql.Date(timeStamp.getYear(),timeStamp.getMonth(),timeStamp.getDay());
    	}
    	catch( Exception e )
    	{
    		e.printStackTrace();
    	}
    	return sqlDate;
    }
    
    /**
     * @author Debadatta.Mishra
     * @param dateString
     * @return
     */
    public static java.sql.Date getSQLDateFromString( String dateString )
	{
		return new java.sql.Date( 111 ).valueOf( dateString );
	}
	
	/**
	 * @author Debadatta.Mishra
	 * @param utilDate
	 * @return
	 */
	public static java.sql.Date getSQLDateFromUtilDate( java.util.Date utilDate )
	{
		java.sql.Date sqlDate = new java.sql.Date(utilDate.getYear(), utilDate
				.getMonth(), utilDate.getDay());
		return sqlDate;
	}
	
	/**
	 * @author Debadatta.Mishra
	 * @param utilDate
	 * @param format
	 * @return
	 */
	public static java.sql.Date getSQLDateFromUtilDate( java.util.Date utilDate , String format )
	{
		java.sql.Date sqlDate = null;
		try
		{
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat( format );
			String strDate = simpleDateFormat.format( utilDate );
			sqlDate = getSQLDateFromString( strDate );
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		return sqlDate;
	}
	
	/**
	 * @author Debadatta.Mishra
	 * @param sqlDate
	 * @param format
	 * @return
	 */
	public static java.util.Date getUtilDateFromSQLDate( java.sql.Date sqlDate , String format )
	{
		java.util.Date utilDate = null;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat( format );
		String ss = sqlDate.toString();
		try
		{
			utilDate = simpleDateFormat.parse( ss );
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		return utilDate;
	}
	
	/**
	 * @author Debadatta.Mishra
	 * @param source
	 * @return
	 */
	public static Object getSQLDate( Object source )
    {
        if (source instanceof java.util.Date)
        {
         	return new java.sql.Date( ((java.util.Date) source).getTime());
        }
        else
        {
         	return source;
        }
    }
	
	public final static java.sql.Timestamp sqlTimestamp( java.util.Date utilDate ) 
	{
		return new java.sql.Timestamp(utilDate.getTime());
	}
   
    /**
     * @author Debadatta.Mishra
     * @param source
     * @return
     */
    public static Object getUtilDate(Object source)
    {
        if (source instanceof java.sql.Date)
        {
         	return new java.util.Date( ((java.sql.Date) source).getTime());
        }
        else
        {
         	return source;
        }
    }
    
    /**
     * @author Debadatta.Mishra
     * @param timestampObject
     * @return
     */
    public static java.util.Date getUtilDateFromTimestamp(java.sql.Timestamp timestampObject) 
	{
        long time = timestampObject.getTime();
        boolean appendNanos = ( (time % 1000) == 0 );
        if ( appendNanos ) 
        {
            return new java.util.Date( time + ( timestampObject.getNanos() / 1000000 ) );
        }
        else 
        {
            return new java.util.Date( time );
        }
    }
    
	/**
	 * @author Debadatta.Mishra
	 * @param timestampObject
	 * @return
	 */
	public static java.util.Date getUtilDateFromTimestamp( Object timestampObject) 
	{
		java.util.Date utilDate = null;
		if( timestampObject instanceof java.sql.Timestamp )
		{
			java.sql.Timestamp timestampObject1 = ( java.sql.Timestamp )timestampObject;
			
			 long time = timestampObject1.getTime();
		        boolean appendNanos = ( (time % 1000) == 0 );
		        if ( appendNanos ) 
		        {
		        	utilDate = new java.util.Date( time + ( timestampObject1.getNanos() / 1000000 ) );
		        }
		        else 
		        {
		        	utilDate = new java.util.Date( time );
		        }
		}
        return utilDate;
    }
	
	/**
	 * @author Manjunath N G
	 * @param dateObject
	 * @return
	 */

	public static String parseDate(Object b){	 
			String s="";
						
			if(b!=null){
				String a=b.toString();
				a=a.trim();	
				if(a.length()>=10)
				s=a.substring(0,10);
			}	 
			 return s;
	}
	
	public static java.util.Date dateWithTime(java.util.Date dateValue){
		java.util.Date forModification = new java.util.Date();
		dateValue.setHours(forModification.getHours());
		dateValue.setMinutes(forModification.getMinutes());
		dateValue.setSeconds(forModification.getSeconds());
		return dateValue;
	}
	
	/**
	 * Method is used to convert String date to SQL date
	 * @param dt
	 * @return
	 */
	public static java.sql.Date parseDate(String dt)
	{
			java.util.Date parsedDate=null;
			if(dt!=null )
			{
				if(dt.length()!=0){
					try{
						SimpleDateFormat sdf=new SimpleDateFormat(OPEConstants.DATE_FORMAT);
						parsedDate= sdf.parse(dt);
					} 
					catch(java.text.ParseException pe){
						pe.printStackTrace();
					}
					return new java.sql.Date(parsedDate.getTime());
				}else
				{
					return null;
				}
			}
			else{
				return null;
			}
		}
}
