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

/**
 *
 *  $Id$
 *
 */
package org.riverock.common.html;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

public class Header
{

    public static String getUserAgent(ServletRequest request)
    {
        if (request instanceof HttpServletRequest)
            return ((HttpServletRequest)request).getHeader("User-Agent");
        else
            return null;
    }

    public static AcceptLanguageWithLevel[] getAcceptLanguageArray(ServletRequest request)
    {
        if ( request==null || !(request instanceof HttpServletRequest))
            return null;

        return getAcceptLanguageArray( getAcceptLanguage(request) );
    }

    public static AcceptLanguageWithLevel[] getAcceptLanguageArray(String accept)
    {
        List v = getAcceptLanguageAsList(accept);
        AcceptLanguageWithLevel[] array = new AcceptLanguageWithLevel[v.size()];
        for (int i=0; i<v.size(); i++)
        {
            array[i] = (AcceptLanguageWithLevel)v.get(i);
        }

        return array;
    }

    public static List getAcceptLanguageAsList(ServletRequest request)
    {
        if ( request==null || !(request instanceof HttpServletRequest))
            return null;

        return getAcceptLanguageAsList( getAcceptLanguage(request) );
    }

    public static Locale[] getAcceptLanguageAsLocaleListSorted(ServletRequest request)
    {
        if ( request==null || !(request instanceof HttpServletRequest))
            return new Locale[]{};

        return getAcceptLanguageAsLocaleListSorted(getAcceptLanguage(request));
    }

    public static Locale[] getAcceptLanguageAsLocaleListSorted(String headerLocale)
    {
        if ( headerLocale==null)
            return new Locale[]{};

        List list = getAcceptLanguageAsList( headerLocale );

        System.out.println("list = " + list.size());
        for (int i=0; i<list.size(); i++)
            System.out.println("elem = " + list.get(i)+", " + list.get(i).getClass().getName());

        Object array[] = list.toArray();
        if (array.length==0)
            return new Locale[]{};

        int max;
        int min;
        AcceptLanguageWithLevel t = null;
/*
        for (int i=array.length-1; i>0; i--)
        {
            max = i;
            for (int j=i-1; j>=0; j--)
            {
                if (((AcceptLanguageWithLevel)array[j]).level>((AcceptLanguageWithLevel)array[max]).level)
                    max=j;

                t = (AcceptLanguageWithLevel)array[max];
                array[max]=array[i];
                array[i]=t;
            }
        }
*/
        for (int i=0; i<array.length-1; i++)
        {
            min = i;
            for (int j=i+1; j<array.length; j++)
            {
                if (((AcceptLanguageWithLevel)array[j]).level>((AcceptLanguageWithLevel)array[min]).level)
                    min=j;

                t = (AcceptLanguageWithLevel)array[min];
                array[min]=array[i];
                array[i]=t;
            }
        }
/*
procedure selection;
var
i, j, min, t : integer;
begin
    for i:=1 to N-1 do
    begin
        min := i;
        for j:=i+1 to N do
            if a[j]<a[min] then
min := j;
        t := a[min];
a[min] :=a[i];
a[i] := t;
    end;
end;
*/

        Locale[] temp = new Locale[array.length];
        for (int i=0; i<array.length; i++)
        {
            temp[i]=((AcceptLanguageWithLevel)array[i]).locale;
        }

        return temp;
    }

    public static List getAcceptLanguageAsList(String accept)
    {
        List v = new ArrayList();

        if (accept==null)
            return v;

        StringTokenizer st = new StringTokenizer(accept, ",");
        while (st.hasMoreTokens())
        {
            v.add( AcceptLanguageWithLevel.getInstance( st.nextToken() ));
        }
        return v;
    }

    public static String getAcceptLanguage(ServletRequest request)
    {
        if (request instanceof HttpServletRequest)
            return ((HttpServletRequest)request).getHeader("Accept-Language");
        else
            return null;
    }

    public static String getReferer(ServletRequest request)
    {
        if (request instanceof HttpServletRequest)
            return ((HttpServletRequest)request).getHeader("Referer");
        else
            return null;
    }

    public static TypeBrowser getTypeBrowser(HttpServletRequest request)
    {
        return new TypeBrowser(getUserAgent(request));
    }
}