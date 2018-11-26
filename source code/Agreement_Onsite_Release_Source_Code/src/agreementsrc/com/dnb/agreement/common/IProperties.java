/********************************************************************************************
 * Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                       *
 * IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.      *
 * The Program or any portion thereof may not be reproduced in any form whatsoever except   *
 * as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd.*
 *																							*
 * File Name                   : IProperties.java                                    	    *
 * Author                      : Raja Mohan                                                 *
 * Creation Date               : 01-May-2006                                                *
 * Description                 : Extension of properties objectg							*
 * Modification History        :                                                            *
 *																						    *
 *     Version No.               Date               Brief description of change             *
 *  --------------------------------------------------------------------------------------- *
 *                       |                  |											    *
 *                       |                  |											    *
 *  --------------------------------------------------------------------------------------- *
 ********************************************************************************************/

package com.dnb.agreement.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Properties;
import java.util.StringTokenizer;

/**
 * Description: This is an extension of Propeties object that allows us to map a
 * key to a map in the Property file. e.g.
 * mapName=map^key1=value1,key2=value2,etc...
 * <ul>
 * Caveats:
 * <li> map^ must precede the mapping of the values in order for it to be
 * identified as a map
 * <li> might have issues with special characters
 * </ul>
 */
public class IProperties extends Properties {
	/** Identify a map value */
	private final static String mapIdentifier = "map^";

	/** Separators for keys and values */
	private final static String keyValueSeparators = "=: \t\r\n\f";

	/** Strict key, value separators */
	private final static String strictKeyValueSeparators = "=:";

	/** Special save characters */
	private final static String specialSaveChars = "=: \t\r\n\f#!";

	/** Whitespace */
	private final static String whiteSpaceChars = " \t\r\n\f";

	/** Item separator for maps */
	private final static String mapItemSeparator = "|";

	/**
	 * Reads a property list (key and element pairs) from the input stream. The
	 * stream is assumed to be using the ISO 8859-1 character encoding.
	 * <p>
	 * 
	 * Every property occupies one line of the input stream. Each line is
	 * terminated by a line terminator (<code>\n</code> or <code>\r</code>
	 * or <code>\r\n</code>). Lines from the input stream are processed until
	 * end of file is reached on the input stream.
	 * <p>
	 * 
	 * A line that contains only whitespace or whose first non-whitespace
	 * character is an ASCII <code>#</code> or <code>!</code> is ignored
	 * (thus, <code>#</code> or <code>!</code> indicate comment lines).
	 * <p>
	 * 
	 * Every line other than a blank line or a comment line describes one
	 * property to be added to the table (except that if a line ends with \,
	 * then the following line, if it exists, is treated as a continuation line,
	 * as described below). The key consists of all the characters in the line
	 * starting with the first non-whitespace character and up to, but not
	 * including, the first ASCII <code>=</code>, <code>:</code>, or
	 * whitespace character. All of the key termination characters may be
	 * included in the key by preceding them with a \. Any whitespace after the
	 * key is skipped; if the first non-whitespace character after the key is
	 * <code>=</code> or <code>:</code>, then it is ignored and any
	 * whitespace characters after it are also skipped. All remaining characters
	 * on the line become part of the associated element string. Within the
	 * element string, the ASCII escape sequences <code>\t</code>,
	 * <code>\n</code>, <code>\r</code>, <code>\\</code>,
	 * <code>\"</code>, <code>\'</code>, <code>\ &#32;</code> &#32;(a
	 * backslash and a space), and <code>&#92;u</code><i>xxxx</i> are
	 * recognized and converted to single characters. Moreover, if the last
	 * character on the line is <code>\</code>, then the next line is treated
	 * as a continuation of the current line; the <code>\</code> and line
	 * terminator are simply discarded, and any leading whitespace characters on
	 * the continuation line are also discarded and are not part of the element
	 * string.
	 * <p>
	 * 
	 * As an example, each of the following four lines specifies the key
	 * <code>"Truth"</code> and the associated element value
	 * <code>"Beauty"</code>:
	 * <p>
	 * 
	 * <pre>
	 *  Truth = Beauty
	 *  Truth:Beauty
	 *  Truth   :Beauty
	 * </pre>
	 * 
	 * As another example, the following three lines specify a single property:
	 * <p>
	 * 
	 * <pre>
	 *  fruits    apple, banana, pear, \
	 *                                   cantaloupe, watermelon, \
	 *                                   kiwi, mango
	 * </pre>
	 * 
	 * The key is <code>"fruits"</code> and the associated element is:
	 * <p>
	 * 
	 * <pre>
	 * &quot;apple, banana, pear, cantaloupe, watermelon, kiwi, mango&quot;
	 * </pre>
	 * 
	 * Note that a space appears before each <code>\</code> so that a space
	 * will appear after each comma in the final result; the <code>\</code>,
	 * line terminator, and leading whitespace on the continuation line are
	 * merely discarded and are <i>not</i> replaced by one or more other
	 * characters.
	 * <p>
	 * 
	 * As a third example, the line:
	 * <p>
	 * 
	 * <pre>
	 * cheeses
	 * </pre>
	 * 
	 * specifies that the key is <code>"cheeses"</code> and the associated
	 * element is the empty string.
	 * <p>
	 * 
	 * 
	 * 
	 * @param inStream
	 *            the input stream.
	 * @exception IOException
	 *                if an error occurred when reading from the input stream.
	 */
	public synchronized void load(InputStream inStream) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(inStream,
				"8859_1"));
		while (true) {
			// Get next line
			String line = in.readLine();
			if (line == null) {
				return;
			}

			if (line.length() > 0) {
				// Continue lines that end in slashes if they are not comments
				char firstChar = line.charAt(0);
				if ((firstChar != '#') && (firstChar != '!')) {
					while (continueLine(line)) {
						String nextLine = in.readLine();
						if (nextLine == null) {
							nextLine = "";
						}
						String loppedLine = line
								.substring(0, line.length() - 1);
						// Advance beyond whitespace on new line
						int startIndex = 0;
						for (startIndex = 0; startIndex < nextLine.length(); startIndex++) {
							if (whiteSpaceChars.indexOf(nextLine
									.charAt(startIndex)) == -1) {
								break;
							}
						}
						nextLine = nextLine.substring(startIndex, nextLine
								.length());
						line = new String(loppedLine + nextLine);
					}

					// Find start of key
					int len = line.length();
					int keyStart;
					for (keyStart = 0; keyStart < len; keyStart++) {
						if (whiteSpaceChars.indexOf(line.charAt(keyStart)) == -1) {
							break;
						}
					}

					// Blank lines are ignored
					if (keyStart == len) {
						continue;
					}

					// Find separation between key and value
					int separatorIndex;
					for (separatorIndex = keyStart; separatorIndex < len; separatorIndex++) {
						char currentChar = line.charAt(separatorIndex);
						if (currentChar == '\\') {
							separatorIndex++;
						} else if (keyValueSeparators.indexOf(currentChar) != -1) {
							break;
						}
					}

					// Skip over whitespace after key if any
					int valueIndex;
					for (valueIndex = separatorIndex; valueIndex < len; valueIndex++) {
						if (whiteSpaceChars.indexOf(line.charAt(valueIndex)) == -1) {
							break;
						}
					}

					// Skip over one non whitespace key value separators if any
					if (valueIndex < len) {
						if (strictKeyValueSeparators.indexOf(line
								.charAt(valueIndex)) != -1) {
							valueIndex++;
						}
					}

					// Skip over white space after other separators if any
					while (valueIndex < len) {
						if (whiteSpaceChars.indexOf(line.charAt(valueIndex)) == -1) {
							break;
						}
						valueIndex++;
					}
					String key = line.substring(keyStart, separatorIndex);
					String value = (separatorIndex < len) ? line.substring(
							valueIndex, len) : "";

					// Convert then store key and value
					key = loadConvert(key);
					value = loadConvert(value);
					Object finalValue = parseForMap(value);
					put(key, finalValue);
				}
			}
		}
	}

	/**
	 * Converts encoded &#92;uxxxx to unicode chars and changes special saved
	 * chars to their original forms
	 * 
	 * @param theString
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	private String loadConvert(String theString) {
		char aChar;
		int len = theString.length();
		StringBuffer outBuffer = new StringBuffer(len);

		for (int x = 0; x < len;) {
			aChar = theString.charAt(x++);
			if (aChar == '\\') {
				aChar = theString.charAt(x++);
				if (aChar == 'u') {
					// Read the xxxx
					int value = 0;
					for (int i = 0; i < 4; i++) {
						aChar = theString.charAt(x++);
						switch (aChar) {
						case '0':
						case '1':
						case '2':
						case '3':
						case '4':
						case '5':
						case '6':
						case '7':
						case '8':
						case '9':
							value = (value << 4) + aChar - '0';
							break;
						case 'a':
						case 'b':
						case 'c':
						case 'd':
						case 'e':
						case 'f':
							value = (value << 4) + 10 + aChar - 'a';
							break;
						case 'A':
						case 'B':
						case 'C':
						case 'D':
						case 'E':
						case 'F':
							value = (value << 4) + 10 + aChar - 'A';
							break;
						default:
							throw new IllegalArgumentException(
									"Malformed \\uxxxx encoding.");
						}
					}
					outBuffer.append((char) value);
				} else {
					if (aChar == 't') {
						aChar = '\t';
					} else if (aChar == 'r') {
						aChar = '\r';
					} else if (aChar == 'n') {
						aChar = '\n';
					} else if (aChar == 'f') {
						aChar = '\f';
					}
					outBuffer.append(aChar);
				}
			} else {
				outBuffer.append(aChar);
			}
		}
		return outBuffer.toString();
	}

	/**
	 * Returns true if the given line is a line that must be appended to the
	 * next line
	 * 
	 * @param line
	 *            Line to be checked for continuation character
	 * @return true if line continues to next line in file
	 */
	private boolean continueLine(String line) {
		int slashCount = 0;
		int index = line.length() - 1;
		while ((index >= 0) && (line.charAt(index--) == '\\')) {
			slashCount++;
		}
		return (slashCount % 2 == 1);
	}

	/**
	 * Checks the input line for a possibility of containing a map and outputs
	 * an Object of either String if no map is found or HashMap if map is found
	 * and created
	 * 
	 * @param inputString
	 *            Line to check for map input
	 * @return Parsed value of the input, either a string or a Map
	 */
	private Object parseForMap(String inputString) {
		if (inputString.startsWith(mapIdentifier)) {
			HashMap map = new HashMap();
			StringTokenizer st = new StringTokenizer(inputString
					.substring(mapIdentifier.length()), mapItemSeparator);
			String tempToken = new String();
			boolean sumTokens = false;
			while (st.hasMoreTokens()) {
				String pair = st.nextToken();
				if (sumTokens) {
					pair = tempToken + mapItemSeparator + pair;
					sumTokens = false;
					tempToken = new String();
				}
				if (pair.endsWith("\\")) {
					sumTokens = true;
					tempToken = pair;
					continue;
				}
				StringTokenizer st2 = new StringTokenizer(pair, "=");
				if (st2.hasMoreTokens()) {
					String key = st2.nextToken();
					if (st2.hasMoreTokens()) {
						String value = st2.nextToken();
						map.put(key, value);
					}
				}
			}
			return map;
		} else {
			return inputString;
		}
	}
}
