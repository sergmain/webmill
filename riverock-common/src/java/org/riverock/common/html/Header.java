/*
 * org.riverock.common -- Supporting classes, interfaces, and utilities
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
package org.riverock.common.html;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

/**
 * $Id$
 */
public final class Header {

    public static final String USER_AGENT_HEADER = "User-Agent";
    public static final String ACCEPT_LANGUAGE_HEADER = "Accept-Language";
    public static final String REFERER_HEADER = "Referer";

    public static String getUserAgent( final ServletRequest request ) {
        if ( request instanceof HttpServletRequest )
            return ( (HttpServletRequest)request ).getHeader( USER_AGENT_HEADER );
        else
            return null;
    }

    public static AcceptLanguageWithLevel[] getAcceptLanguageArray( final ServletRequest request ) {
        if ( request == null || !( request instanceof HttpServletRequest ) )
            return null;

        return getAcceptLanguageArray( getAcceptLanguage( request ) );
    }

    public static AcceptLanguageWithLevel[] getAcceptLanguageArray( final String accept ) {
        List v = getAcceptLanguageAsList( accept );
        AcceptLanguageWithLevel[] array = new AcceptLanguageWithLevel[v.size()];
        for( int i = 0; i<v.size(); i++ ) {
            array[i] = (AcceptLanguageWithLevel)v.get( i );
        }

        return array;
    }

    public static List getAcceptLanguageAsList( final ServletRequest request ) {
        if ( request == null || !( request instanceof HttpServletRequest ) )
            return null;

        return getAcceptLanguageAsList( getAcceptLanguage( request ) );
    }

    public static Locale[] getAcceptLanguageAsLocaleListSorted( final ServletRequest request ) {
        if ( request == null || !( request instanceof HttpServletRequest ) )
            return new Locale[]{};

        return getAcceptLanguageAsLocaleListSorted( getAcceptLanguage( request ) );
    }

    public static Locale[] getAcceptLanguageAsLocaleListSorted( final String headerLocale ) {
        if ( headerLocale == null )
            return new Locale[]{};

        List list = getAcceptLanguageAsList( headerLocale );

        Object array[] = list.toArray();
        if ( array.length == 0 )
            return new Locale[]{};

        int min;
        AcceptLanguageWithLevel t = null;

        for( int i = 0; i<array.length - 1; i++ ) {
            min = i;
            for( int j = i + 1; j<array.length; j++ ) {
                if ( ( (AcceptLanguageWithLevel)array[j] ).level>( (AcceptLanguageWithLevel)array[min] ).level )
                    min = j;

                t = (AcceptLanguageWithLevel)array[min];
                array[min] = array[i];
                array[i] = t;
            }
        }
        Locale[] temp = new Locale[array.length];
        for( int i = 0; i<array.length; i++ ) {
            temp[i] = ( (AcceptLanguageWithLevel)array[i] ).locale;
        }

        return temp;
    }

    public static List getAcceptLanguageAsList( final String accept ) {
        List v = new ArrayList();

        if ( accept == null )
            return v;

        StringTokenizer st = new StringTokenizer( accept, "," );
        while( st.hasMoreTokens() ) {
            v.add( AcceptLanguageWithLevel.getInstance( st.nextToken() ) );
        }
        return v;
    }

    public static String getAcceptLanguage( final ServletRequest request ) {
        if ( request instanceof HttpServletRequest )
            return ( (HttpServletRequest)request ).getHeader( ACCEPT_LANGUAGE_HEADER );
        else
            return null;
    }

    public static String getReferer( final ServletRequest request ) {
        if ( request instanceof HttpServletRequest )
            return ( (HttpServletRequest)request ).getHeader( REFERER_HEADER );
        else
            return null;
    }

    public static TypeBrowser getTypeBrowser( final HttpServletRequest request ) {
        return new TypeBrowser( getUserAgent( request ) );
    }
}