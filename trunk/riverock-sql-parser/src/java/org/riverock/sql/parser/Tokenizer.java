/*
 * org.riverock.sql -- Classes for tracking database changes
 * 
 * Copyright (C) 2004, Riverock Software, All Rights Reserved.
 * 
 * Riverock -- The Open-source Java Development Community
 * http://www.riverock.org
 * 
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 */
package org.riverock.sql.parser;

import java.math.BigDecimal;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

public class Tokenizer
{

    private static final int NAME = 1,
    LONG_NAME = 2,
    SPECIAL = 3,
    NUMBER = 4,
    FLOAT = 5,
    STRING = 6,
    LONG = 7,
    DECIMAL = 8;

    // used only internally
    private static final int QUOTED_IDENTIFIER = 9,
    REMARK_LINE = 10,
    REMARK = 11;
    private String sCommand;
    private int iLength;
    private int iIndex;
    private int tokenIndex;
    private int nextTokenIndex;
    private int beginIndex;
    private int iType;
    private String sToken;
    private String sLongNameFirst;
    private String sLongNameLast;
    private boolean bWait;
    private static Map<String, Map> hKeyword;

    static
    {

        String keyword[] = {
            "AND", "ALL", "AVG", "BY", "BETWEEN", "COUNT", "CASEWHEN",
            "DISTINCT", "EXISTS", "MINUS", "FALSE", "FROM", "GROUP", "IF",
            "INTO", "IFNULL", "IS", "IN", "INTERSECT", "INNER", "LEFT",
            "LIKE", "MAX", "MIN", "NULL", "NOT", "ON", "ORDER", "OR", "OUTER",
            "PRIMARY", "SELECT", "SET", "SUM", "TO", "TRUE", "UNIQUE",
            "UNION", "VALUES", "WHERE", "CONVERT", "CAST", "CONCAT", "MINUS",
            "CALL", "HAVING"
        };

        hKeyword = new HashMap<String, Map>( keyword.length );

        for (final String newVar : keyword) {
            hKeyword.put(newVar, hKeyword);
        }
    }

    /**
     * Constructor declaration
     *
     *
     * @param s
     */
    Tokenizer( String s )
    {

        sCommand = s;
        iLength = s.length();
        iIndex = 0;
    }

    /**
     * Method declaration
     *
     *
     * @throws Exception
     */
    void back()
        throws Exception
    {

        nextTokenIndex = iIndex;
        iIndex = tokenIndex;
        bWait = true;
    }

    /**
     * Method declaration
     *
     *
     * @param match
     *
     * @throws Exception
     */
    void getThis( String match )
        throws Exception
    {

        getToken();

        if ( !sToken.equals( match ) )
        {
            throw new Exception("UNEXPECTED_TOKEN "+ sToken );
        }
    }

    /**
     * Method declaration
     *
     *
     * @return
     *
     * @throws Exception
     */
    String getStringToken()
        throws Exception
    {

        getToken();

        switch (iType)
        {

            case STRING:

                // fred - no longer including first quote in sToken
                return sToken.toUpperCase();

            case NAME:
                return sToken;

            case QUOTED_IDENTIFIER:
                return sToken.toUpperCase();
        }

        throw new Exception("UNEXPECTED_TOKEN "+ sToken );
    }

    /**
     * Method declaration
     *
     *
     * @return
     */
    boolean wasValue()
    {

        switch (iType)
        {

            case STRING:
            case NUMBER:
            case LONG:
            case FLOAT:
            case DECIMAL:
                return true;
        }

        return ( sToken.equals( "NULL" ) || sToken.equals( "TRUE" )
            || sToken.equals( "FALSE" ) || sToken.equals( "CURRENT_DATE" )
            || sToken.equals( "CURRENT_TIME" )
            || sToken.equals( "CURRENT_TIMESTAMP" )
            || sToken.equals( "SYSDATE" ) || sToken.equals( "NOW" )
            || sToken.equals( "TODAY" ) );
    }

    boolean wasQuotedIdentifier()
    {
        return iType==QUOTED_IDENTIFIER;
    }

    /**
     * Method declaration
     *
     *
     * @return
     */
    boolean wasLongName()
    {
        return iType==LONG_NAME;
    }

    /**
     * Method declaration
     *
     *
     * @return
     */
    boolean wasName()
    {

        if ( iType==QUOTED_IDENTIFIER )
        {
            return true;
        }

        if ( iType!=NAME )
        {
            return false;
        }

        return !hKeyword.containsKey( sToken );
    }

    /**
     * Method declaration
     *
     *
     * @return
     */
    String getLongNameFirst()
    {
        return sLongNameFirst;
    }

    /**
     * Method declaration
     *
     *
     * @return
     */
    String getLongNameLast()
    {
        return sLongNameLast;
    }

    /**
     * Method declaration
     *
     *
     * @return
     *
     * @throws Exception
     */
    String getName()
        throws Exception
    {

        getToken();

        if ( !wasName() )
        {
            throw new Exception("UNEXPECTED_TOKEN "+ sToken );
        }

        return sToken;
    }

    /**
     * Method declaration
     *
     *
     * @return
     *
     * @throws Exception
     */
    String getString()
        throws Exception
    {

        getToken();

        return sToken;
    }

    /**
     * Method declaration
     *
     *
     * @return
     */
    int getType()
    {

        // todo: make sure it's used only for Values!
        // todo: synchronize type with hColumn
        switch (iType)
        {

            case STRING:
                return Types.VARCHAR;

            case NUMBER:
                return Types.INTEGER;

            case LONG:
                return Types.BIGINT;

            case FLOAT:
                return Types.DOUBLE;

            case DECIMAL:
                return Types.DECIMAL;

            default :
                return Types.NULL;
        }
    }

    /**
     * Method declaration
     *
     *
     * @return
     *
     * @throws Exception
     */
    Object getAsValue()
        throws Exception
    {

        if ( !wasValue() )
        {
            throw new Exception("UNEXPECTED_TOKEN "+ sToken );
        }

        switch (iType)
        {

            case STRING:
                {

                    //fredt - no longer returning string with a singlequote as last char
                    return sToken;
                }
            case NUMBER:
                {

                    // fredt - this returns unsigned values which are later negated.
                    // as a result Integer.MIN_VALUE or Long.MIN_VALUE are promoted
                    // to a wider type.
                    if ( sToken.length()<10 )
                    {
                        return new Integer( sToken );
                    }

                    if ( sToken.length()==10 )
                    {
                        try
                        {
                            return new Integer( sToken );
                        }
                        catch (Exception e1)
                        {
                            iType = LONG;

                            return new Long( sToken );
                        }
                    }

                    if ( sToken.length()<19 )
                    {
                        iType = LONG;

                        return new Long( sToken );
                    }

                    if ( sToken.length()==19 )
                    {
                        try
                        {
                            return new Long( sToken );
                        }
                        catch (Exception e2)
                        {
                            iType = DECIMAL;

                            return new BigDecimal( sToken );
                        }
                    }

                    iType = DECIMAL;

                    return new BigDecimal( sToken );
                }
            case FLOAT:
                {
                    return new Double( sToken );
                }
            case DECIMAL:
                {
                    return new BigDecimal( sToken );
                }
        }

        if ( sToken.equals( "NULL" ) )
        {

            // convert NULL to null String if not a String
            // todo: make this more straightforward
            return null;
        }

        return sToken;
    }

    /**
     * return the current position to be used for VIEW processing
     *
     * @return
     */
    int getPosition()
    {
        return iIndex;
    }

    /**
     * mark the current position to be used for future getLastPart() calls
     *
     * @return
     */
    String getPart( int begin, int end )
    {
        return sCommand.substring( begin, end );
    }

    /**
     * mark the current position to be used for future getLastPart() calls
     *
     * @return
     */
    int getPartMarker()
    {
        return beginIndex;
    }

    /**
     * mark the current position to be used for future getLastPart() calls
     *
     */
    void setPartMarker()
    {
        beginIndex = iIndex;
    }

    /**
     * mark the position to be used for future getLastPart() calls
     *
     */
    void setPartMarker( int position )
    {
        beginIndex = position;
    }

    /**
     * return part of the command string from the last marked position
     *
     * @return
     */
    String getLastPart()
    {
        return sCommand.substring( beginIndex, iIndex );
    }

// fredt@users 20020910 - patch 1.7.1 by Nitin Chauhan - rewrite as switch

    /**
     * Method declaration
     *
     *
     * @throws Exception
     */
    private void getToken()
        throws Exception
    {

        if ( bWait )
        {
            bWait = false;
            iIndex = nextTokenIndex;

            return;
        }

        while ( iIndex<iLength
            && Character.isWhitespace( sCommand.charAt( iIndex ) ) )
        {
            iIndex++;
        }

        sToken = "";
        tokenIndex = iIndex;

        if ( iIndex>=iLength )
        {
            iType = 0;

            return;
        }

        char c = sCommand.charAt( iIndex );
        boolean point = false,
            digit = false,
            exp = false,
            afterexp = false;
        boolean end = false;
        char cfirst = 0;

        if ( Character.isJavaIdentifierStart( c ) )
        {
            iType = NAME;
        }
        else if ( Character.isDigit( c ) )
        {
            iType = NUMBER;
            digit = true;
        }
        else
        {
            switch (c)
            {

                case '(':
                case ')':
                case ',':
                case '*':
                case '=':
                case ';':
                case '+':
                case '%':
                case '?':
                    iType = SPECIAL;

                    iIndex++;

                    sToken = String.valueOf( c );

                    return;

                case ':':
                    iType = NAME;
                    while ( iIndex<iLength
                        && !Character.isWhitespace( sCommand.charAt( iIndex ) ) )
                    {
                        iIndex++;
                    }
//                    iType = SPECIAL;
                    sToken = String.valueOf( c );

                    return;

                case '\"':
                    iType = QUOTED_IDENTIFIER;

                    iIndex++;

                    sToken = getString( '"' );

                    if ( iIndex==sCommand.length() )
                    {
                        return;
                    }

                    c = sCommand.charAt( iIndex );

                    if ( c=='.' )
                    {
                        sLongNameFirst = sToken;

                        iIndex++;

// fredt - todo - avoid recursion - this has problems when there is whitespace
// after the dot - the same with NAME
                        getToken();

                        sLongNameLast = sToken;
                        iType = LONG_NAME;

                        StringBuilder sb =
                            new StringBuilder( sLongNameFirst.length()+1
                            +sLongNameLast.length() );

                        sb.append( sLongNameFirst );
                        sb.append( '.' );
                        sb.append( sLongNameLast );

                        sToken = sb.toString();
                    }

                    return;

                case '\'':
                    iType = STRING;

                    iIndex++;

                    sToken = getString( '\'' );

                    return;

                case '!':
                case '<':
                case '>':
                case '|':
                case '/':
                case '-':
                    cfirst = c;
                    iType = SPECIAL;
                    break;

                case '.':
                    iType = DECIMAL;
                    point = true;
                    break;

                default :
                    throw new Exception("UNEXPECTED_TOKEN "+String.valueOf( c ) );
            }
        }

        int start = iIndex++;

        while ( true )
        {
            if ( iIndex>=iLength )
            {
                c = ' ';
                end = true;

                if ( iType==STRING || iType==QUOTED_IDENTIFIER)
                    throw new Exception("UNEXPECTED_END_OF_COMMAND" );
            }
            else
            {
                c = sCommand.charAt( iIndex );
            }

            switch (iType)
            {

                case NAME:
                    if ( Character.isJavaIdentifierPart( c ) )
                    {
                        break;
                    }

                    // fredt - new char[] will back sToken
                    sToken = sCommand.substring( start, iIndex ).toUpperCase();

                    if ( c=='.' )
                    {
                        sLongNameFirst = sToken;

                        iIndex++;

                        getToken();    // todo: eliminate recursion

                        sLongNameLast = sToken;
                        iType = LONG_NAME;
                        sToken = sLongNameFirst+"."+sLongNameLast;
                    }

                    return;

                case QUOTED_IDENTIFIER:
                case STRING:

                    // shouldn't get here
                    break;

                case REMARK:
                    if ( end )
                    {

                        // unfinished remark
                        // maybe print error here
                        iType = 0;

                        return;
                    }
                    else if ( c=='*' )
                    {
                        iIndex++;

                        if ( iIndex<iLength
                            && sCommand.charAt( iIndex )=='/' )
                        {

                            // using recursion here
                            iIndex++;

                            getToken();

                            return;
                        }
                    }
                    break;

                case REMARK_LINE:
                    if ( end )
                    {
                        iType = 0;

                        return;
                    }
                    else if ( c=='\r' || c=='\n' )
                    {

                        // using recursion here
                        getToken();

                        return;
                    }
                    break;

                case SPECIAL:
                    if ( c=='/' && cfirst=='/' )
                    {
                        iType = REMARK_LINE;

                        break;
                    }
                    else if ( c=='-' && cfirst=='-' )
                    {
                        iType = REMARK_LINE;

                        break;
                    }
                    else if ( c=='*' && cfirst=='/' )
                    {
                        iType = REMARK;

                        break;
                    }
                    else if ( c=='>' || c=='=' || c=='|' )
                    {
                        break;
                    }

                    sToken = sCommand.substring( start, iIndex );

                    return;

                case NUMBER:
                case FLOAT:
                case DECIMAL:
                    if ( Character.isDigit( c ) )
                    {
                        digit = true;
                    }
                    else if ( c=='.' )
                    {
                        iType = DECIMAL;

                        if ( point )
                        {
                            throw new Exception("UNEXPECTED_TOKEN, '.'" );
                        }

                        point = true;
                    }
                    else if ( c=='E' || c=='e' )
                    {
                        if ( exp )
                        {
                            throw new Exception("UNEXPECTED_TOKEN, 'E'" );
                        }

                        // HJB-2001-08-2001 - now we are sure it's a float
                        iType = FLOAT;

                        // first character after exp may be + or -
                        afterexp = true;
                        point = true;
                        exp = true;
                    }
                    else if ( c=='-' && afterexp )
                    {
                        afterexp = false;
                    }
                    else if ( c=='+' && afterexp )
                    {
                        afterexp = false;
                    }
                    else
                    {
                        afterexp = false;

                        if ( !digit )
                        {
                            if ( point && start==iIndex-1 )
                            {
                                sToken = ".";
                                iType = SPECIAL;

                                return;
                            }

                            throw new Exception("UNEXPECTED_TOKEN "+String.valueOf( c ) );
                        }

                        sToken = sCommand.substring( start, iIndex );

                        return;
                    }
            }

            iIndex++;
        }
    }

// fredt - strings are constructed from new char[] objects to avoid slack
// because these strings might end up as part of internal data structures
// or table elements.
// we may consider using pools to avoid recreating the strings
    private String getString( char quoteChar )
        throws Exception
    {

        try
        {
            int nextIndex = iIndex;
            boolean quoteInside = false;

            for ( ; ; )
            {
                nextIndex = sCommand.indexOf( quoteChar, nextIndex );

                if ( nextIndex<0 )
                {
                    throw new Exception("UNEXPECTED_END_OF_COMMAND" );
                }

                if ( nextIndex<iLength-1
                    && sCommand.charAt( nextIndex+1 )==quoteChar )
                {
                    quoteInside = true;
                    nextIndex += 2;

                    continue;
                }

                break;
            }

            char[] chBuffer = new char[nextIndex-iIndex];

            sCommand.getChars( iIndex, nextIndex, chBuffer, 0 );

            int j = chBuffer.length;

            if ( quoteInside )
            {
                j = 0;

                // fredt - loop assumes all occurences of quoteChar are paired
                for ( int i = 0; i<chBuffer.length; i++, j++ )
                {
                    if ( chBuffer[i]==quoteChar )
                    {
                        i++;
                    }

                    chBuffer[j] = chBuffer[i];
                }
            }

            iIndex = ++nextIndex;

            return new String( chBuffer, 0, j );
        }
        catch (Exception e)
        {
            throw e;
        }
    }

// fredt@users 20020420 - patch523880 by leptipre@users - VIEW support

    /**
     * Method declaration
     *
     *
     * @param s
     */
    void setString( String s, int pos )
    {

        sCommand = s;
        iLength = s.length();
        bWait = false;
        iIndex = pos;
    }

    /**
     * Method declaration
     *
     *
     * @return
     */
    int getLength()
    {
        return iLength;
    }
}
