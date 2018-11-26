package com.dnb.agreement.utility;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import com.dnb.agreement.exception.IErrorCodes;
import com.dnb.agreement.exception.AgreementBusinessException;
import com.dnb.agreement.exception.AgreementSystemException;

/**
 * As the name implies - generic String functionality provided
 * by static methods.
 */
public class StringUtils
{
    /**
     * System-dependant line separator (newline character(s))
     */
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");

    /**
     * Hide the public constructor
     */
    private StringUtils()
    {
    }

    /**
     * Check if the string array is null or if the entire array is comprised of empty strings
     * or null values.
     * @see #isStringEmpty
     * @param testString string array to be tested
     * @return true if the string array was null or if each array element is null or empty
     * (after trim()ming)
     */
    public static boolean isStringArrayEmpty(String[] testString)
    {
        boolean emptyArray = true;

        if (testString == null || testString.length == 0)
        {
            emptyArray = true;
        }
        else
        {
            for (int i = 0; i < testString.length; i++)
            {
                if (!(isStringEmpty(testString[i])))
                {
                    emptyArray = false;
                    break;
                }
            }
        }
        return emptyArray;
    }

    /**
     * Test if a given string is empty.
     * @param testString String to be tested
     * @return true if string is null or empty (after trim()ming)
     */
    public static boolean isStringEmpty(String testString)
    {
        boolean isEmpty = false;

        if (testString == null || testString.trim().length() == 0)
        {
            isEmpty = true;
        }
        return isEmpty;
    }


    /**
     * Strip out all unwanted characters from a String
     * @param input Input String to work with
     * @param badCharacters char[] of characters that should be removed
     * @return String minus all of the unwanted characters specified in badCharacters
     */
    public static final String stripOutChars(String input, char[] badCharacters)
    {
        String temp = input;
        StringBuffer ret = new StringBuffer();
        char[] badChars = badCharacters;
        boolean match = false;
        char currentChar;
        char compareChar;

        for (int index = 0; index < temp.length(); index++)
        {
            currentChar = temp.charAt(index);

            for (int index2 = 0; index2 < badChars.length; index2++)
            {
                compareChar = badChars[index2];

                if (currentChar == compareChar)
                {
                    match = true;
                    break;
                }
            }
            if (!match)
            {
                ret.append(currentChar);
            }
            match = false;
        }
        return ret.toString();
    }


    /**
     * Replace all occurrences of a given pattern with another.
     * @param input Input String to search for pattern
     * @param replace Pattern to search for
     * @param replaceWith String to replace pattern with
     * @return String containing input string with pattern replaced by replaceWith param
     */
    public static final String replaceChars(String input, String replace, String replaceWith)
    {
        String ret = input;
        int index = input.indexOf(replace);
        int replLength = replace.length();
        while (index != -1)
        {
            ret = ret.substring(0, index) + replaceWith
                    + ret.substring(index + replLength);
            index = ret.indexOf(replace, index + 1);
        }
        return ret;
    }

    /**
     * Fill in a pattern using data from a hashmap.
     *
     * @param pattern : The pattern to be filled in.  For example,
     * "http://%SITE%" where %SITE% is the field to be replaced.  This method will
     * fill some regular expression type patterns.  The pattern
     * "[(%AREA_CD%) ]%PREFIX%-%POSTFIX%" will only output the "(%AREA_CD%) " if
     * AREA_CD is defined in the HashMap input.
     * @param inputData : HashMap containing the name-value pairs to be used to fill
     * in the pattern
     * @return Filled in pattern
     */
    public static final String fillPattern(String pattern, HashMap inputData)
    {
        HashMap data = new HashMap(inputData.size());
        Iterator iterator = inputData.keySet().iterator();
        while (iterator.hasNext())
        {
            String key = (String) iterator.next();
            data.put(key.toUpperCase(), inputData.get(key));
        }
        StringBuffer ret = new StringBuffer();
        ArrayList fields = getFields(pattern);
        for (int index = 0; index < fields.size(); index++)
        {
            String field = (String) fields.get(index);
            if (field.indexOf("%") == -1)
                ret.append(field);
            else
            {
                String value = "";
                if (field.startsWith("[") && field.endsWith("]"))
                {
                    value = replacePattern(field, "%", data);
                    if (value.indexOf("%") != -1)
                        value = "";
                    else
                        value = value.substring(1, value.length() - 1);
                }
                else
                    value = replacePattern(field, "", data);
                ret.append(value);
            }
        }
        return ret.toString();
    }

    /**
     *  Helper method for the fillPattern method<br>
     *  This performs the actual string replacement for the pattern
     *  @param pattern The pattern to be filled in
     *  @param defaultData What to fill in a missing field with
     *  @param data HashMap containing the data to be used to fill in the pattern
     *  @return String containing the filled in pattern
     */
    private static final String replacePattern(String pattern, String defaultData, HashMap data)
    {
        int index = pattern.indexOf("%");
        while (index >= 0)
        {
            String param = "";
            int idx2 = pattern.indexOf("%", index + 1);
            if (idx2 > index)
            {
                param = pattern.substring(index + 1, idx2);
                String value = (String) data.get(param.toUpperCase());
                if (value == null)
                    value = defaultData;
                param = "%" + param + "%";
                pattern = replaceChars(pattern, param, value);
                index = pattern.indexOf("%");
            }
            else
                index = -1;
        }
        return pattern;
    }

    /**
     *  Helper method for the fillPattern method<br>
     *  Get all of the fields from a pattern
     *  @param pattern to be filled in by fillPattern
     *  @return ArrayList of all of the fields
     */
    private static final ArrayList getFields(String pattern)
    {
        ArrayList fields = new ArrayList();
        int index = 0;
        while (index >= 0)
        {
            int idx = pattern.indexOf("[", index);
            if (idx > index)
                fields.add(pattern.substring(index, idx));
            if (idx >= 0)
            {
                int idx2 = pattern.indexOf("]", idx);
                if (idx2 > idx)
                {
                    fields.add(pattern.substring(idx, idx2 + 1));
                    index = idx2 + 1;
                }
                else
                {
                    fields.add(pattern.substring(idx));
                    index = -1;
                }
            }
            else
            {
                fields.add(pattern.substring(index));
                index = idx;
            }
        }
        return fields;
    }

    /**
     * Split a string into an array based on the presence of a char<br>
     * For example, the string "ABC.DEF.GHI.JKL" would be split on '.' into
     * the string array {"ABC", "DEF", "GHI", "JKL"}.
     * @param input String to split
     * @param splitChar char to use to split
     * @return String[] Split array
     */
    public static final String[] split(String input, char splitChar)
    {
        return split(input, String.valueOf(splitChar));
    }

    /**
     * Split a string into an array based on the presence of a char<br>
     * For example, the string "ABC.DEF.GHI.JKL" would be split on '.' into
     * the string array {"ABC", "DEF", "GHI", "JKL"}.
     * @param input String to split
     * @param split Separator to use to split
     * @return String[] Split array
     */
    public static final String[] split(String input, String split)
    {
        ArrayList list = new ArrayList();
        int index = input.indexOf(split);
        int prevIndex = 0;
        while (index >= 0)
        {
            list.add(input.substring(prevIndex, index).trim());
            prevIndex = index + 1;
            index = input.indexOf(split, prevIndex);
        }
        list.add(input.substring(prevIndex));
        String[] ret = new String[list.size()];
        list.toArray(ret);
        return ret;
    }

    /**
     * Capitalize only the first character of each word in a string, and lowercase the
     * rest of the word.
     * @param input String to be capitalized
     * @return String containing a capital first letter, and lowercase rest of string.
     */
    public static final String capitalizeFirstChar(String input)
    {
        if (input.length() < 2)
            return input.toUpperCase();
        StringTokenizer token = new StringTokenizer(input);
        StringBuffer ret = new StringBuffer();
        while (token.hasMoreElements())
        {
            ret.append(" ");
            ret.append(capitalizeFirstCharOfWord(token.nextToken()));
        }
        return ret.substring(1);
    }

    /**
     * Helper method to capitalize the first character of a string
     * and lowercase the rest of it.
     *
     * @param input String that will have its first character capitalized
     * @return String that now has its first character capitalized and the
     * rest in lowercase
     */
    private static final String capitalizeFirstCharOfWord(String input)
    {
        if (input.length() < 2)
            return input.toUpperCase();
        String firstChar = input.substring(0, 1);
        String rest = input.substring(1);
        return firstChar.toUpperCase() + rest.toLowerCase();
    }

    /**
     * Helper method to convert an array or list into a single string, separated by a given
     * separator
     * @param input The list or array to convert
     * @param sep The separator string to include in the output string
     * @return The output string of all items in the array or list separated by sep
     */
    public static final String convertArray(Object input, String sep)
    {
        List items = null;
        if (input instanceof Object[])
        {
            items = Arrays.asList((Object[]) input);
        }
        else if (input instanceof List)
        {
            items = (List) input;
        }
        StringBuffer buffer = new StringBuffer();
        for (int index = 0; index < items.size(); index++)
        {
            buffer.append(items.get(index));
            buffer.append(sep);
        }
        buffer.setLength(buffer.length() - sep.length());
        return buffer.toString();
    }

    /**
     * A safe cast of an arbitrary string to an Integer.  If the string is
     * null or unable to be cast as an Integer, null is returned
     * @param input String to convert
     * @return An integer corresponding to the input, or null if the input could not be converted
     */
    public static Integer getAsInteger(String input)
    {
        // I'm sure there is a more efficient way to do this, please let me (jake) know if you
        // think of one
        Integer ret = null;
        try
        {
            if (input != null)
                ret = new Integer(input);
        }
        catch (NumberFormatException nfe)
        {
            ret = null;
        }
        return ret;
    }

    /**
     * A safe cast of an arbitrary string to a BigDecimal.  If the string is
     * null or unable to be cast as a BigDecimal, null is returned
     * @param input String to convert
     * @return A BigDecimal corresponding to the input, or null if the input could not be converted
     */
    public static BigDecimal getAsBigDecimal(String input)
    {
        // I'm sure there is a more efficient way to do this, please let me (jake) know if you
        // think of one
        BigDecimal ret = null;
        try
        {
            if (input != null)
                ret = new BigDecimal(input);
        }
        catch (NumberFormatException nfe)
        {
            ret = null;
        }
        return ret;
    }
    
    /**
     * Method returns object array of record after splitting using the expression
     * @param ids
     * @param exp : any separator
     * @return Object Array
     */
	 public static Object[] createObject(String input,String exp){
		
		Object[] loadAll=null;

		try	{
			loadAll=input.split(exp);
			return loadAll;
		} catch(ArrayIndexOutOfBoundsException e) {
			loadAll = null;
		} catch (RuntimeException e){
			loadAll = null;
		}		
		return loadAll;
	}   
	 /**
	  * Returns number of occurences of value separated by the exp
	  * Eg: 1,23,3  exp= "," 
	  * 
	  * @param ids
	  * @return
	  */
	 public static int getLength(String input,String exp) {
			return input.split(exp).length;
		}
	 
	 

	 /**
		     * Parses a Unparsed Nmber and returns parsed Number as String
		     * @param unParsed : String(Number) which contains comma(,)
		     * @return parsed  : String(Number) After replaced dot(.) for comma(,) 
		     * and also removes all spaces
		     */
		    
		    public static String parseFormattedNumber(String unParsed)
			{	 String parsed=null;	

				try{
						
				if((unParsed!=null)&&(unParsed.length() > 0))
					{	
					unParsed=unParsed.trim();
					parsed=unParsed.replaceAll(" ","");
					parsed=unParsed.replace(',', '.');
					
					}			
				}catch(Exception e){
					
					e.printStackTrace();
				}
				return parsed;
			 }		    	   
			
		   /** 
		   * Method returns object of record from StringTokenizer
		   */
			
			 public static Object[] createObject(String ids)throws AgreementSystemException,
					AgreementBusinessException {
				
				int i=0;
				Object[] loadAll=null;

				try	{
					StringTokenizer token = new StringTokenizer(ids,",");
					loadAll=new Object[getLength(ids)];
				
					while (token.hasMoreTokens()) 
					{
						loadAll[i]=token.nextToken();
						i++;	
					}
					return loadAll;
				}
				catch(ArrayIndexOutOfBoundsException e) {					
					throw new AgreementSystemException(IErrorCodes.RECORD_CREATEOBJECT_ERROR,
							"Failed to Delete Records", e);
				}
			}

		 /** 
		 * Method returns number of occurences of ids from StringTokenizer .
		 * Used to create an Object Array. 
		 */
			public static int getLength(String ids) {
					return ids.split(",").length;
			}

		/** 
		 * Method returns the string if given string is null.
		 *  
		 */
			
			public static String makeString(Object st){
	    		String s="";
		    	if(st!=null){
	    			s=(st.toString());
	    		}
			 	return s;
	    	}	
		    
}
