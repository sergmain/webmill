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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

/**
 * ����� ServletTools ������������ ��� ������ c HttpXxx �������� � ��.
 *
 * $Id$
 */
public final class ServletTools {
    private final static Logger log = Logger.getLogger( ServletTools.class   );

    public static void cleanSession(HttpSession session)
        throws Exception
    {
        if (session==null)
            return;

// ������� �� ����� ��� �������
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

    public static String getHiddenItem(String name, String value)
    {
        return ("<input type=\"hidden\" name=\"" + name + "\" value=\"" + value+ "\">\n");
    }

    public static String getHiddenItem(String name, int value)
    {
        return ("<input type=\"hidden\" name=\"" + name + "\" value=\"" + value+ "\">\n");
    }

    public static String getHiddenItem(String name, Integer value)
    {
        return ("<input type=\"hidden\" name=\"" + name + "\" value=\"" + (value!=null?value.longValue():0) + "\">\n");
    }

    public static String getHiddenItem(String name, long value)
    {
        return ("<input type=\"hidden\" name=\"" + name + "\" value=\"" + value+ "\">\n");
    }

    public static String getHiddenItem(String name, Long value)
    {
        return ("<input type=\"hidden\" name=\"" + name + "\" value=\"" + (value!=null?value.longValue():0) + "\">\n");
    }

    public static void immediateRemoveAttribute(HttpSession session,
                                                String attr)
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
     * ���� ��� ������ �������� URL ���������� �� ����������������, �� ��������������� ��
     * �������� index.jsp
     * ���������:
     * <blockquote>
     * HttpServletRequest request	- ������ ��� request �� ��������� JSP<br>
     * HttpServletResponse response	- ������ ��� response �� ��������� JSP<br>
     * String f - ��� ���������� ��� ��������<br>
     * </blockquote>
     */
    public static boolean isNotInit(HttpServletRequest request, HttpServletResponse response, String f)
    {
        return isNotInit(request, response, f, "index.jsp");
    }

    /**
     * ���� ��� ������ �������� URL ���������� �� ����������������, �� ��������������� ��
     * �������� index.jsp
     * ���������:
     * <blockquote>
     * HttpServletRequest request	- ������ ��� request �� ��������� JSP<br>
     * HttpServletResponse response	- ������ ��� response �� ��������� JSP<br>
     * String f - ��� ���������� ��� ��������<br>
     * String defURL - URL ��� ���������������, ���� ���������� �����������
     * </blockquote>
     */
    public static boolean isNotInit(HttpServletRequest request, HttpServletResponse response, String f, String defURL)
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
     * ���������� ��������� �������� ����������. ���� ���������� �� ����������������, ���������� ������ ������
     * ���������:
     * <blockquote>
     * HttpServletRequest request	- ������ ��� request �� ��������� JSP<br>
     * String f - ��� ���������� ��� ��������� ��������<br>
     * String def  - ������ �� ���������<br>
     * </blockquote>
     */
    public static String getString(
        HttpServletRequest request, String f, String def, String fromCharset, String toCharset)
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
     * ���������� int �������� ����������. ���� ���������� �� ����������������, ���������� 0
     * ���������:
     * <blockquote>
     * HttpServletRequest request	- ������ ��� request �� ��������� JSP<br>
     * String f - ��� ���������� ��� ��������� ��������<br>
     * </blockquote>
     */
    public static Integer getInt(HttpServletRequest request, String f)
    {
        return getInt(request, f, null);
    }

    /**
     * ���������� int �������� ����������. ���� ���������� �� ����������������, ���������� 0
     * ���������:
     * <blockquote>
     * HttpServletRequest request	- ������ ��� request �� ��������� JSP<br>
     * String f - ��� ���������� ��� ��������� ��������<br>
     * int def - �������� �� ��������<br>
     * </blockquote>
     */
    public static Integer getInt(HttpServletRequest request, String f, Integer def)
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
     * ���������� long �������� ����������. ���� ���������� �� ����������������, ���������� 0
     * ���������:
     * <blockquote>
     * HttpServletRequest request	- ������ ��� request �� ��������� JSP<br>
     * String f - ��� ���������� ��� ��������� ��������<br>
     * </blockquote>
     */
    public static Long getLong(HttpServletRequest request, String f){
        return getLong(request, f, null);
    }

    /**
     * ���������� long �������� ����������. ���� ���������� �� ����������������, ���������� 0
     * ���������:
     * <blockquote>
     * HttpServletRequest request	- ������ ��� request �� ��������� JSP<br>
     * String f - ��� ���������� ��� ��������� ��������<br>
     * long def - �������� �� ��������
     * </blockquote>
     */
    public static Long getLong(HttpServletRequest request, String f, Long def){
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
     * ���������� float �������� ����������. ���� ���������� �� ����������������, ���������� 0
     * ���������:
     * <blockquote>
     * HttpServletRequest request	- ������ ��� request �� ��������� JSP<br>
     * String f - ��� ���������� ��� ��������� ��������<br>
     * </blockquote>
     */
    public static Float getFloat(HttpServletRequest request, String f)
    {
        return getFloat(request, f, null);
    }

    /**
     * ���������� float �������� ����������. ���� ���������� �� ����������������, ���������� 0
     * ���������:
     * <blockquote>
     * HttpServletRequest request	- ������ ��� request �� ��������� JSP<br>
     * String f - ��� ���������� ��� ��������� ��������<br>
     * float def - �������� �� ���������
     * </blockquote>
     */
    public static Float getFloat(HttpServletRequest request, String f, Float def)
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

    public static Double getDouble(HttpServletRequest request, String f)
    {
        return getDouble(request, f, null);
    }

    /**
     * ���������� double �������� ����������. ���� ���������� �� ����������������, ���������� 0
     * ���������:
     * <blockquote>
     * HttpServletRequest request	- ������ ��� request �� ��������� JSP<br>
     * String f - ��� ���������� ��� ��������� ��������<br>
     * double def - �������� �� ���������
     * </blockquote>
     */
    public static Double getDouble(HttpServletRequest request, String f, Double def)
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

    public static Map getParameterMap(String parameter)
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