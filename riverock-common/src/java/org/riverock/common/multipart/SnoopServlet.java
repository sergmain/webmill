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

 * 2003. Copyright (c) jSmithy. http:// multipart.jSmithy.com

 * 2001-2003. Copyright (c) Simon Brooke. http://www.weft.co.uk/library/maybeupload/

 *

 * $Id$

 */



package org.riverock.common.multipart;





import java.io.IOException;

import java.io.PrintWriter;

import java.util.Enumeration;

import java.util.Vector;



import javax.servlet.ServletContext;

import javax.servlet.ServletException;

import javax.servlet.http.Cookie;

import javax.servlet.http.HttpServletResponse;

import javax.servlet.http.HttpSession;



import org.apache.log4j.Category;



public class SnoopServlet extends MaybeUploadServlet

{

    private static Category cat = Category.getInstance("org.riverock.multipart.SnoopServlet");



    public void doPost(MaybeUploadRequestWrapper request,

                       HttpServletResponse response)

        throws ServletException, IOException

    {

        doGet(request, response);

    }



    public void doGet(MaybeUploadRequestWrapper request,

                      HttpServletResponse response)

        throws ServletException, IOException

    {

        PrintWriter out = response.getWriter();

        response.setContentType("text/plain");



        out.println("Snoop Servlet - modified, handles multipart/form-data");

        out.println();

        out.println("Servlet init parameters:");

        Enumeration e = getInitParameterNames();

        while (e.hasMoreElements())

        {

            String key = (String) e.nextElement();

            String value = getInitParameter(key);

            out.println("   " + key + " = " + value);

        }

        out.println();



        out.println("Context init parameters:");

        ServletContext context = getServletContext();

        Enumeration enum = context.getInitParameterNames();

        while (enum.hasMoreElements())

        {

            String key = (String) enum.nextElement();

            Object value = context.getInitParameter(key);

            out.println("   " + key + " = " + value);

        }

        out.println();



        out.println("Context attributes:");

        enum = context.getAttributeNames();

        while (enum.hasMoreElements())

        {

            String key = (String) enum.nextElement();

            Object value = context.getAttribute(key);

            out.println("   " + key + " = " + value);

        }

        out.println();



        out.println("Request attributes:");

        e = request.getAttributeNames();

        while (e.hasMoreElements())

        {

            String key = (String) e.nextElement();

            Object value = request.getAttribute(key);

            out.println("   " + key + " = " + value);

        }

        out.println();

        out.println("Request info:");

        out.println("Servlet Name: " + getServletName());

        out.println("Protocol: " + request.getProtocol());

        out.println("Scheme: " + request.getScheme());

        out.println("Server Name: " + request.getServerName());

        out.println("Server Port: " + request.getServerPort());

        out.println("Server Info: " + context.getServerInfo());

        out.println("Remote Addr: " + request.getRemoteAddr());

        out.println("Remote Host: " + request.getRemoteHost());

        out.println("Character Encoding: " + request.getCharacterEncoding());

        out.println("Content Length: " + request.getContentLength());

        out.println("Content Type: " + request.getContentType());

        out.println("Locale: " + request.getLocale());

        out.println("Default Response Buffer: " + response.getBufferSize());

        out.println();

        out.println("Parameter names in this request:");

        e = request.getParameterNames();

        while (e.hasMoreElements())

        {

            String key = (String) e.nextElement();

            Object value = request.get(key);



            if (value instanceof Vector)

            {

                Vector vec = (Vector) value;

                Enumeration elts = vec.elements();

                int i = 0;



                out.println("   " + key + " [Vector with " +

                    vec.size() + " elements]");



                while (elts.hasMoreElements())

                {

                    value = elts.nextElement();



                    out.println("      element " + i++ + " [" +

                        value.getClass().getName() +

                        "] = " + value);

                }

            }

            else if (value == null)

            {

                out.println("   " + key + " [" +

                    "null value" + "]");

            }

            else

                out.println("   " + key + " [" +

                    value.getClass().getName() + "] = " + value);

        }

        out.println();

        out.println("Headers in this request:");

        e = request.getHeaderNames();



        while (e.hasMoreElements())

        {

            String key = (String) e.nextElement();

            String value = request.getHeader(key);

            out.println("   " + key + ": " + value);

        }

        out.println();



        out.println("Cookies in this request:");

        Cookie[] cookies = request.getCookies();



        if (cookies != null)

        {

            for (int i = 0; i < cookies.length; i++)

            {

                Cookie cookie = cookies[i];

                out.println("   " + cookie.getName() +

                    " = " + cookie.getValue());

            }

        }

        out.println();



        out.println("Request Is Secure: " + request.isSecure());

        out.println("Auth Type: " + request.getAuthType());

        out.println("HTTP Method: " + request.getMethod());

        out.println("Remote User: " + request.getRemoteUser());

        out.println("Request URI: " + request.getRequestURI());

        out.println("Context Path: " + request.getContextPath());

        out.println("Servlet Path: " + request.getServletPath());

        out.println("Path Info: " + request.getPathInfo());

        out.println("Path Trans: " + request.getPathTranslated());

        out.println("Query String: " + request.getQueryString());



        out.println();

        HttpSession session = request.getSession();

        out.println("Requested Session Id: " +

            request.getRequestedSessionId());

        out.println("Current Session Id: " + session.getId());

        out.println("Session Created Time: " + session.getCreationTime());

        out.println("Session Last Accessed Time: " +

            session.getLastAccessedTime());

        out.println("Session Max Inactive Interval Seconds: " +

            session.getMaxInactiveInterval());

        out.println();

        out.println("Session values: ");

        Enumeration names = session.getAttributeNames();

        while (names.hasMoreElements())

        {

            String name = (String) names.nextElement();

            out.println("   " + name + " = " + session.getAttribute(name));

        }

    }

}



