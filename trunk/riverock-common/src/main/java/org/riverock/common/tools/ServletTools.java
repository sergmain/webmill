/*
 * org.riverock.common - Supporting classes, interfaces, and utilities
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
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
 */
package org.riverock.common.tools;

import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Tool class for wotk with Servlet's objects
 *
 * $Id$
 */
public final class ServletTools {

    /**
     * Remove all attributes in session context. We try 3 times remove all attributes.
     * In 1st and 2nd loops ConcurrentModificationException ignored.
     *
     * @param session HttpSession session
     * @throws Exception on error
     */
    public static void cleanSession(final HttpSession session) throws Exception {
        if (session == null)
            return;

        // delete all objects from session
        int countLoop = 3;
        for (int i = 0; i < countLoop; i++) {
            try {
                for (Enumeration e = session.getAttributeNames();
                     e.hasMoreElements();
                     e = session.getAttributeNames()
                    ) {
                    String name = (java.lang.String) e.nextElement();

                    session.removeAttribute(name);
                }
            }
            catch (java.util.ConcurrentModificationException e) {
                if (i == countLoop - 1)
                    throw e;
            }
        }
    }

    public static String getHiddenItem(final String name, final String value) {
        return ("<input type=\"hidden\" name=\"" + name + "\" value=\"" + value + "\">\n");
    }

    public static String getHiddenItem(final String name, final int value) {
        return ("<input type=\"hidden\" name=\"" + name + "\" value=\"" + value + "\">\n");
    }

    public static String getHiddenItem(final String name, final Integer value) {
        return ("<input type=\"hidden\" name=\"" + name + "\" value=\"" + (value != null ? value.longValue() : 0) + "\">\n");
    }

    public static String getHiddenItem(final String name, final long value) {
        return ("<input type=\"hidden\" name=\"" + name + "\" value=\"" + value + "\">\n");
    }

    public static String getHiddenItem(final String name, final Long value) {
        return ("<input type=\"hidden\" name=\"" + name + "\" value=\"" + (value != null ? value : 0) + "\">\n");
    }

    /**
     * Remove attribete from session and invoke before 'clearObject' method
     *
     * @param session HttpSession session
     * @param attr String name of attribute for remove
     */
    public static void immediateRemoveAttribute(final HttpSession session, final String attr) {

        Object obj = session.getAttribute(attr);
        try {
            Class cl = obj.getClass();
            Method m = cl.getMethod("clearObject", (Class[]) null);

            if (m != null) {
                m.invoke(obj, (Object[]) null);
            }

        }
        catch (Exception e) {
            //
        }

        session.removeAttribute(attr);
        obj = null;
    }

    /**
     * Return text value of parameter. If parameter not exist, return default value.
     * Before return string value converter from 'fromCharset' charset to 'toCharset' charset
     *
     * @param request HttpServletRequest request
     * @param parameterName String name of parameter
     * @param defaultValue String default value
     * @param fromCharset String convert from charset
     * @param toCharset String convert to charset
     * @return String parameter value
     */
    public static String getString(final HttpServletRequest request, final String parameterName, final String defaultValue, final String fromCharset, final String toCharset) {
        String s_ = defaultValue;
        if (request.getParameter(parameterName) != null) {
            try {
                s_ = StringTools.convertString(request.getParameter(parameterName), fromCharset, toCharset);
            }
            catch (Exception e) {
                // return defaultValue value
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
    public static Integer getInt(final HttpServletRequest request, final String f) {
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
    public static Integer getInt(final HttpServletRequest request, final String f, final Integer def) {
        Integer i_ = def;
        if (request.getParameter(f) != null) {
            try {
                String s_ = request.getParameter(f);
                i_ = new Integer(s_);
            }
            catch (Exception exc) {
                // not rethrow exception 'cos this method return def value in this case
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
    public static Long getLong(final HttpServletRequest request, final String f) {
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
    public static Long getLong(final HttpServletRequest request, final String f, final Long def) {
        Long i_ = def;
        if (request.getParameter(f) != null) {
            try {
                String s_ = request.getParameter(f);
                i_ = new Long(s_);
            }
            catch (Exception exc) {
                // not rethrow exception 'cos this method return def value in this case
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
    public static Float getFloat(final HttpServletRequest request, final String f) {
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
    public static Float getFloat(final HttpServletRequest request, final String f, final Float def) {
        Float i_ = def;
        if (request.getParameter(f) != null) {
            try {
                String s_ = request.getParameter(f);
                s_ = s_.replace(',', '.');

                i_ = new Float(s_);
            }
            catch (Exception exc) {
                // not rethrow exception 'cos this method return def value in this case
            }
        }
        return i_;
    }

    public static Double getDouble(final HttpServletRequest request, final String f) {
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
    public static Double getDouble(final HttpServletRequest request, final String f, final Double def) {
        Double i_ = def;
        if (request.getParameter(f) != null) {
            try {
                String s_ = request.getParameter(f);
                s_ = s_.replace(',', '.');

                i_ = new Double(s_);
            }
            catch (Exception exc) {
                // not rethrow exception 'cos this method return def value in this case
            }
        }
        return i_;
    }

    public static Map<String, Object> getParameterMap(final String parameter) {
        if (parameter == null)
            return null;

        Map<String, Object> map = new HashMap<String, Object>();

        String s;
        if (parameter.indexOf('?') != -1)
            s = parameter.substring(parameter.indexOf('?') + 1);
        else
            s = parameter;

        StringTokenizer st = new StringTokenizer(s, "&", false);
        while (st.hasMoreTokens()) {
            String param = st.nextToken();
            int idx = param.indexOf('=');
            if (idx == -1)
                MainTools.putKey(map, param, "");
            else
                MainTools.putKey(map, param.substring(0, idx), param.substring(idx + 1));
        }

        return map;
    }
}