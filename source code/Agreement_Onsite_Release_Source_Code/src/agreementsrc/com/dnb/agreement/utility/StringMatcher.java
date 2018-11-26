package com.dnb.agreement.utility;

import java.util.Vector;

public class StringMatcher 
{
	 /**
     * java.lang.String variable to specify the pattern of the wildcard used in a String
     * An example is given below.<br>
     * <code>String string = "??ab*";</code>.
     * <br>Here the meaning is that a String is to be found having any two characters
     * followed by only two characters <code>ab</code> and followed by any number of
     * arbitrary characters.
     */
    private String wildCardPattern;
    /**
     * Length of the String containing the the wildcard characters.
     */
    private int wildCardPatternLength;
    /**
     * Bollean variable to specify the wheather search is based upon
     * wildcard characters or not.
     */
    private boolean ignoreWildCards;
    private boolean ignoreCase;
    private boolean hasLeadingStar;
    private boolean hasTrailingStar;
    private String charSegments[];
    private int charBound;
    
    /**
     * @param pattern
     * @param doesIgnoreCase
     * @param doesIgnoreWildCards
     */
    public StringMatcher(String pattern, boolean doesIgnoreCase, boolean doesIgnoreWildCards)
    {
    	super();
        charBound = 0;
        if(pattern == null)
        {
            throw new IllegalArgumentException();
        }
        ignoreCase = doesIgnoreCase;
        ignoreWildCards = doesIgnoreWildCards;
        wildCardPattern = pattern;
        wildCardPatternLength = pattern.length();
        if(ignoreWildCards)
        {
            parseNoWildCards();
        } else
        {
            parseWildCards();
        }
    }

    /**
     * This is an inner class which specify the
     * start and end position of the characters
     * in a String.
     * @author Debadatta Mishra
     *
     */
    public final static class Position
    {
        private transient int start;
        private transient int end;

        public int getStart()
        {
            return start;
        }

        public int getEnd()
        {
            return end;
        }

        public Position(int start, int end)
        {
            this.start = start;
            this.end = end;
        }
    }

    public final Position find(String text, int start, int end)
    {
        if(text == null)
        {
            throw new IllegalArgumentException();
        }
        int tlen = text.length();
        if(start < 0)
        {
            start = 0;
        }
        if(end > tlen)
        {
            end = tlen;
        }
        if(end < 0 || start >= end)
        {
            return null;
        }
        if(wildCardPatternLength == 0)
        {
            return new Position(start, start);
        }
        if(ignoreWildCards)
        {
            int x = posIn(text, start, end);
            if(x < 0)
            {
                return null;
            } else
            {
                return new Position(x, x + wildCardPatternLength);
            }
        }
        int segCount = charSegments.length;
        if(segCount == 0)
        {
            return new Position(start, end);
        }
        int curPos = start;
        int matchStart = -1;
        int i;
        for(i = 0; i < segCount && curPos < end; i++)
        {
            String current = charSegments[i];
            int nextMatch = regExpPosIn(text, curPos, end, current);
            if(nextMatch < 0)
            {
                return null;
            }
            if(i == 0)
            {
                matchStart = nextMatch;
            }
            curPos = nextMatch + current.length();
        }

        if(i < segCount)
        {
            return null;
        } else
        {
            return new Position(matchStart, curPos);
        }
    }

    public final boolean match(String text)
    {
    	System.out.println("text ::::::: "+text);
        return match( text, 0, text.length() );
    }

    public final boolean match(String text, int start, int end)
    {
        if(text == null)
        {
            throw new IllegalArgumentException();
        }
        if(start > end)
        {
            return false;
        }
        if(ignoreWildCards)
        {
            return end - start == wildCardPatternLength && wildCardPattern.regionMatches(ignoreCase, 0, text, start, wildCardPatternLength);
        }
        int segCount = charSegments.length;
        if(segCount == 0 && ( hasLeadingStar || hasTrailingStar ) )
        {
            return true;
        }
        if(start == end)
        {
            return wildCardPatternLength == 0;
        }
        if(wildCardPatternLength == 0)
        {
            return start == end;
        }
        int tlen = text.length();
        if(start < 0)
        {
            start = 0;
        }
        if(end > tlen)
        {
            end = tlen;
        }
        int tCurPos = start;
        int bound = end - charBound;
        if(bound < 0)
        {
            return false;
        }
        int i = 0;
        String current = charSegments[i];
        int segLength = current.length();
        if( !hasLeadingStar )
        {
            if(!regExpRegionMatches(text, start, current, 0, segLength))
            {
                return false;
            }
            i++;
            tCurPos += segLength;
        }
        if(charSegments.length == 1 && !hasLeadingStar && !hasTrailingStar)
        {
            return tCurPos == end;
        }
        for(; i < segCount; i++)
        {
            current = charSegments[i];
            int k = current.indexOf('\0');
            int currentMatch;
            if(k < 0)
            {
                currentMatch = textPosIn(text, tCurPos, end, current);
                if(currentMatch < 0)
                {
                    return false;
                }
            } else
            {
                currentMatch = regExpPosIn(text, tCurPos, end, current);
                if(currentMatch < 0)
                {
                    return false;
                }
            }
            tCurPos = currentMatch + current.length();
        }

        if(!hasTrailingStar && tCurPos != end)
        {
            int clen = current.length();
            return regExpRegionMatches(text, end - clen, current, 0, clen);
        }
        return i == segCount;
    }

    private void parseNoWildCards()
    {
        charSegments = new String[1];
        charSegments[0] = wildCardPattern;
        charBound = wildCardPatternLength;
    }

    private void parseWildCards()
    {
        if(wildCardPattern.startsWith("*"))
        {
            hasLeadingStar = true;
        }
        if (wildCardPattern.endsWith("*") && wildCardPatternLength > 1
				&& wildCardPattern.charAt(wildCardPatternLength - 2) != '\\')
        {
            hasTrailingStar = true;
        }
        Vector temp = new Vector();
        int pos = 0;
        StringBuffer buf = new StringBuffer();
        while(pos < wildCardPatternLength) 
        {
            char c = wildCardPattern.charAt(pos++);
            switch(c)
            {
            case 92: // '\\'
                if(pos >= wildCardPatternLength)
                {
                    buf.append(c);
                    break;
                }
                char next = wildCardPattern.charAt(pos++);
                if(next == '*' || next == '?' || next == '\\')
                {
                    buf.append(next);
                } else
                {
                    buf.append(c);
                    buf.append(next);
                }
                break;

            case 42: // '*'
                if(buf.length() > 0)
                {
                    temp.addElement(buf.toString());
                    charBound += buf.length();
                    buf.setLength(0);
                }
                break;

            case 63: // '?'
                buf.append('\0');
                break;

            default:
                buf.append(c);
                break;
            }
        }
        if(buf.length() > 0)
        {
            temp.addElement(buf.toString());
            charBound += buf.length();
        }
        charSegments = new String[temp.size()];
        temp.copyInto(charSegments);
    }

    private int posIn(String text, int start, int end)
    {
        int max = end - wildCardPatternLength;
        if(!ignoreCase)
        {
            int i = text.indexOf(wildCardPattern, start);
            if(i == -1 || i > max)
            {
                return -1;
            } else
            {
                return i;
            }
        }
        for(int i = start; i <= max; i++)
        {
            if(text.regionMatches(true, i, wildCardPattern, 0, wildCardPatternLength))
            {
                return i;
            }
        }

        return -1;
    }

    private int regExpPosIn(String text, int start, int end, String p)
    {
        int plen = p.length();
        int max = end - plen;
        for(int i = start; i <= max; i++)
        {
            if(regExpRegionMatches(text, i, p, 0, plen))
            {
                return i;
            }
        }
        return -1;
    }

    private boolean regExpRegionMatches(String text, int tStart, String p, int pStart, int plen)
    {
        while(plen-- > 0) 
        {
            char tchar = text.charAt(tStart++);
            char pchar = p.charAt(pStart++);
            if ((ignoreWildCards || pchar != 0)
					&& pchar != tchar
					&& (!ignoreCase || Character.toUpperCase(tchar) != Character
							.toUpperCase(pchar)
							&& Character.toLowerCase(tchar) != Character
									.toLowerCase(pchar)))
            {
                return false;
            }
        }
        return true;
    }

    /**
     * In an example *e as pattern and actual string is abcde,
     * text will be abcde and p will be e ie String after *
     * @param text
     * @param start
     * @param end
     * @param p
     * @return
     */
    private final int textPosIn(String text, int start, int end, String p)
    {
    	System.out.println("text ::: "+text);
    	System.out.println("p ::: "+p);
        int plen = p.length();//Length of the String after *
        int max = end - plen;
        if(!ignoreCase)
        {
            int i = text.indexOf(p, start);
            //The following is a special case
            //It will search only for a condition called *.*
            if( p.equals("."))
            {
            	return 1;
            }
            if(i == -1 || i > max)
            {
                return -1;
            }
            
            else
            {
                return i;
            }
        }
        for(int i = start; i <= max; i++)
        {
            if(text.regionMatches( true, i, p, 0, plen ) )
            {
                return i;
            }
        }
        return -1;
    }
}
