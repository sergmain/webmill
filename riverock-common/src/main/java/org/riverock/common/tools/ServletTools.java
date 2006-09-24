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
package org.riverock.common.tools;

import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

/**
 * Tool class for wotk with Servlet's objects
 *
 * $Id$
 */
public final class ServletTools {
    private final static Logger log = Logger.getLogger(ServletTools.class);

    /**
     * Remove all attributes in session context. We try 3 times remove all attributes.
     * In 1st and 2nd loops ConcurrentModificationException ignored.
     *
     * @param session HttpSession session
     * @throws Exception
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

                    if (log.isDebugEnabled())
                        log.debug("Attribute: " + name);

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
            if (log.isDebugEnabled())
                log.debug("#12.12.001 search method 'clearObject'");

            Class cl = obj.getClass();
            Method m = cl.getMethod("clearObject", (Class[]) null);

            if (log.isDebugEnabled())
                log.debug("#12.12.002 invoke method 'clearObject'");

            if (m != null)
                m.invoke(obj, (Object[]) null);

            if (log.isDebugEnabled())
                log.debug("#12.12.003 complete invoke method 'clearObject'");
        }
        catch (Exception e) {
            if (log.isInfoEnabled())
                log.info("#12.12.003  method 'clearObject' not found.",  e);
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
                log.warn("Exception in getFloat(), def value will be return", exc);
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
                log.warn("Exception in getDouble(), def value will be return", exc);
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