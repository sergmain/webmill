/*
 * org.riverock.common - Supporting classes and utilities
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
        List<AcceptLanguageWithLevel> v = getAcceptLanguageAsList( accept );
        AcceptLanguageWithLevel[] array = new AcceptLanguageWithLevel[v.size()];
        int i=0;
        for (AcceptLanguageWithLevel item : v) {
            array[i++] = item;
        }

        return array;
    }

    public static List<AcceptLanguageWithLevel> getAcceptLanguageAsList( final ServletRequest request ) {
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

        List<AcceptLanguageWithLevel> list = getAcceptLanguageAsList( headerLocale );

        AcceptLanguageWithLevel array[] = list.toArray(new AcceptLanguageWithLevel[0]);
        if ( array.length == 0 )
            return new Locale[]{};

        int min;
        AcceptLanguageWithLevel t = null;

        for( int i = 0; i<array.length - 1; i++ ) {
            min = i;
            for( int j = i + 1; j<array.length; j++ ) {
                if ( ( array[j] ).level>( array[min] ).level )
                    min = j;

                t = array[min];
                array[min] = array[i];
                array[i] = t;
            }
        }
        Locale[] temp = new Locale[array.length];
        for( int i = 0; i<array.length; i++ ) {
            temp[i] = ( array[i] ).locale;
        }

        return temp;
    }

    public static List<AcceptLanguageWithLevel> getAcceptLanguageAsList( final String accept ) {
        List<AcceptLanguageWithLevel> v = new ArrayList<AcceptLanguageWithLevel>();

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