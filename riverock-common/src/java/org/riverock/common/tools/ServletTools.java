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

package org.riverock.common.tools;

import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.nio.charset.Charset;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

/**
 * $Id$
 */
public final class ServletTools {
    private final static Logger log = Logger.getLogger( ServletTools.class   );

    public static class ContentType {

        private StringBuffer contentType = null;
        private Charset charset = null;

        public ContentType( final String contentType ) {
            this( contentType, (String)null);
        }

        public void setCharset( String charset_ ) {
            this.charset = Charset.forName( charset_ );
        }

        public ContentType( final String contentType, final Charset charset ) {
            this.contentType = new StringBuffer(contentType);
            this.charset = charset;
        }

        public ContentType( final String contentType, final String defaultContentType ) {
            parse( contentType );
            if ( contentType==null )
                this.contentType = new StringBuffer( defaultContentType );
        }

        // format: "text/html; charset=utf-8"
        private final static String CHARSET = "charset";
        private void parse( final String contentTypeString ) {
            if (contentTypeString==null)
                return;

            int idx = contentTypeString.indexOf( ';' );
            if (idx==-1) {
                this.contentType = new StringBuffer( contentTypeString );
//                String s = contentTypeString.trim();
//                if (s.startsWith( CHARSET ) )
//                    this.charset = extractCharset( contentTypeString );
//                else
//                    this.contentType = s;

                return;
            }

            this.charset = extractCharset( contentTypeString.substring( idx+1 ) );
            this.contentType = new StringBuffer( contentTypeString.substring( 0, idx ).trim() );
        }

        private Charset extractCharset( final String contentType ) {
            if (contentType==null)
                return null;

            String s = contentType.trim();
            if (!s.startsWith( CHARSET ) )
                return null;

            int idx = s.indexOf( '=' );
            if (idx==-1)
                return null;

            return Charset.forName( s.substring( idx+1).trim() );
        }

        public String getContentType() {
            return contentType.toString();
        }

        public StringBuffer getContentTypeStringBuffer() {
            return contentType;
        }

        public Charset getCharset() {
            return charset;
        }
    }

    public static void cleanSession( final HttpSession session)
        throws Exception
    {
        if (session==null)
            return;

        // delete all objects from session
        int countLoop = 3;
        for (int i=0; i<countLoop; i++)
        {
            try
            {
                for (Enumeration e = session.getAttributeNames();
                     e.hasMoreElements();
                     e = session.getAttributeNames()
                    )
                {
                    String name = (java.lang.String) e.nextElement() ;

                    if(log.isDebugEnabled())
                        log.debug("Attribute: "+name);

                    session.removeAttribute( name );
                }
            }
            catch( java.util.ConcurrentModificationException e)
            {
                if (i==countLoop-1)
                    throw e;
            }
        }
    }

    public static String getHiddenItem( final String name, final String value)
    {
        return ("<input type=\"hidden\" name=\"" + name + "\" value=\"" + value+ "\">\n");
    }

    public static String getHiddenItem(final String name, final int value)
    {
        return ("<input type=\"hidden\" name=\"" + name + "\" value=\"" + value+ "\">\n");
    }

    public static String getHiddenItem(final String name, final Integer value)
    {
        return ("<input type=\"hidden\" name=\"" + name + "\" value=\"" + (value!=null?value.longValue():0) + "\">\n");
    }

    public static String getHiddenItem(final String name, final long value)
    {
        return ("<input type=\"hidden\" name=\"" + name + "\" value=\"" + value+ "\">\n");
    }

    public static String getHiddenItem(final String name, final Long value)
    {
        return ("<input type=\"hidden\" name=\"" + name + "\" value=\"" + (value!=null?value.longValue():0) + "\">\n");
    }

    public static void immediateRemoveAttribute(final HttpSession session,
                                                final String attr)
    {
        Object obj = session.getAttribute(attr);
        try
        {
            if (log.isDebugEnabled())
                log.debug("#12.12.001 search method 'clearObject'");

            Class cl = obj.getClass();
            Method m = cl.getMethod("clearObject", null);

            if (log.isDebugEnabled())
                log.debug("#12.12.002 invoke method 'clearObject'");

            if (m != null)
                m.invoke(obj, null);

            if (log.isDebugEnabled())
                log.debug("#12.12.003 complete invoke method 'clearObject'");
        }
        catch (Exception e)
        {
            if (log.isInfoEnabled())
                log.info("#12.12.003  method 'clearObject' not found. Error " + e.toString());
        }

        session.removeAttribute(attr);
        obj = null;
    }

    /**
     * @deprecated
     * Если при вызове текущего URL переменная не инициализирована, то перенаправление на
     * страницу index.jsp
     * Параметры:
     * <blockquote>
     * HttpServletRequest request	- обычно это request из окружения JSP<br>
     * HttpServletResponse response	- обычно это response из окружения JSP<br>
     * String f - имя переменной для проверки<br>
     * </blockquote>
     */
    public static boolean isNotInit(final HttpServletRequest request, final HttpServletResponse response, final String f)
    {
        return isNotInit(request, response, f, "index.jsp");
    }

    /**
     * @deprecated
     * Если при вызове текущего URL переменная не инициализирована, то перенаправление на
     * страницу index.jsp
     * Параметры:
     * <blockquote>
     * HttpServletRequest request	- обычно это request из окружения JSP<br>
     * HttpServletResponse response	- обычно это response из окружения JSP<br>
     * String f - имя переменной для проверки<br>
     * String defURL - URL для перенаправления, если переменная отсутствует
     * </blockquote>
     */
    public static boolean isNotInit(final HttpServletRequest request, final HttpServletResponse response, final String f, final String defURL)
    {

        if (request.getParameter(f) == null)
        {
            try
            {
                response.sendRedirect(defURL);
            }
            catch (Exception e)
            {
            }

            return true;
        }
        return false;
    }

    /**
     * Возвращает текстовое значение переменной. Если переменная не инициализирована, возвращает пустую строку
     * Параметры:
     * <blockquote>
     * HttpServletRequest request	- обычно это request из окружения JSP<br>
     * String f - имя переменной для получения значения<br>
     * String def  - строка по умолчанию<br>
     * </blockquote>
     */
    public static String getString(
        final HttpServletRequest request, final String f, final String def, final String fromCharset, final String toCharset)
    {
        String s_ = def;
        if (request.getParameter(f) != null)
        {
            try
            {
                s_ = StringTools.convertString( request.getParameter(f), fromCharset, toCharset);
            }
            catch (Exception e)
            {
            }
        }
        return s_;
    }


    /**
     * Возвращает int значение переменной. Если переменная не инициализирована, возвращает 0
     * Параметры:
     * <blockquote>
     * HttpServletRequest request	- обычно это request из окружения JSP<br>
     * String f - имя переменной для получения значения<br>
     * </blockquote>
     */
    public static Integer getInt(final HttpServletRequest request, final String f)
    {
        return getInt(request, f, null);
    }

    /**
     * Возвращает int значение переменной. Если переменная не инициализирована, возвращает 0
     * Параметры:
     * <blockquote>
     * HttpServletRequest request	- обычно это request из окружения JSP<br>
     * String f - имя переменной для получения значения<br>
     * int def - значение по молчанию<br>
     * </blockquote>
     */
    public static Integer getInt(final HttpServletRequest request, final String f, final Integer def)
    {
        Integer i_ = def;
        if (request.getParameter(f) != null)
        {
            try
            {
                String s_ = request.getParameter(f);
                i_ = new Integer(s_);
            }
            catch (Exception exc)
            {
                // not rethrow exception 'cos this method return def value in this case
                log.warn("Exception in getInt(), def value will be return", exc);
            }
        }
        return i_;
    }

    /**
     * Возвращает long значение переменной. Если переменная не инициализирована, возвращает 0
     * Параметры:
     * <blockquote>
     * HttpServletRequest request	- обычно это request из окружения JSP<br>
     * String f - имя переменной для получения значения<br>
     * </blockquote>
     */
    public static Long getLong(final HttpServletRequest request, final String f){
        return getLong(request, f, null);
    }

    /**
     * Возвращает long значение переменной. Если переменная не инициализирована, возвращает 0
     * Параметры:
     * <blockquote>
     * HttpServletRequest request	- обычно это request из окружения JSP<br>
     * String f - имя переменной для получения значения<br>
     * long def - значение по молчанию
     * </blockquote>
     */
    public static Long getLong(final HttpServletRequest request, final String f, final Long def){
        Long i_ = def;
        if (request.getParameter(f) != null)
        {
            try
            {
                String s_ = request.getParameter(f);
                i_ = new Long(s_);
            }
            catch (Exception exc)
            {
                // not rethrow exception 'cos this method return def value in this case
                log.warn("Exception in getLong(), def value will be return", exc);
            }
        }
        return i_;
    }

    /**
     * Возвращает float значение переменной. Если переменная не инициализирована, возвращает 0
     * Параметры:
     * <blockquote>
     * HttpServletRequest request	- обычно это request из окружения JSP<br>
     * String f - имя переменной для получения значения<br>
     * </blockquote>
     */
    public static Float getFloat(final HttpServletRequest request, final String f)
    {
        return getFloat(request, f, null);
    }

    /**
     * Возвращает float значение переменной. Если переменная не инициализирована, возвращает 0
     * Параметры:
     * <blockquote>
     * HttpServletRequest request	- обычно это request из окружения JSP<br>
     * String f - имя переменной для получения значения<br>
     * float def - значение по умолчанию
     * </blockquote>
     */
    public static Float getFloat(final HttpServletRequest request, final String f, final Float def)
    {
        Float i_ = def;
        if (request.getParameter(f) != null)
        {
            try
            {
                String s_ = request.getParameter(f);
                s_ = s_.replace(',', '.');

                i_ = new Float(s_);
            }
            catch (Exception exc)
            {
                // not rethrow exception 'cos this method return def value in this case
                log.warn("Exception in getFloat(), def value will be return", exc);
            }
        }
        return i_;
    }

    public static Double getDouble(final HttpServletRequest request, final String f)
    {
        return getDouble(request, f, null);
    }

    /**
     * Возвращает double значение переменной. Если переменная не инициализирована, возвращает 0
     * Параметры:
     * <blockquote>
     * HttpServletRequest request	- обычно это request из окружения JSP<br>
     * String f - имя переменной для получения значения<br>
     * double def - значение по умолчанию
     * </blockquote>
     */
    public static Double getDouble(final HttpServletRequest request, final String f, final Double def)
    {
        Double i_ = def;
        if (request.getParameter(f) != null)
        {
            try
            {
                String s_ = request.getParameter(f);
                s_ = s_.replace(',', '.');

                i_ = new Double(s_);
            }
            catch (Exception exc)
            {
                // not rethrow exception 'cos this method return def value in this case
                log.warn("Exception in getDouble(), def value will be return", exc);
            }
        }
        return i_;
    }

    public static Map getParameterMap(final String parameter)
    {
        if (parameter==null)
            return null;

        Map map = new HashMap();

        String s = parameter;
        if (parameter.indexOf('?')!=-1)
            s = parameter.substring( parameter.indexOf('?')+1);
        else
            s = parameter;

        StringTokenizer st = new StringTokenizer(s, "&", false);
        while (st.hasMoreTokens())
        {
            String param = st.nextToken();
            int idx = param.indexOf('=');
            if (idx==-1)
                MainTools.putKey(map, param, "");
            else
                MainTools.putKey(map, param.substring(0, idx), param.substring(idx+1));
        }

        return map;
    }
}