/*
 * org.riverock.generic - Database connectivity classes, part of Webmill portal
 * For more information about Webmill portal, please visit project site
 * http://webmill.askmore.info
 *
 * Copyright (C) 2000-2006, Riverock Software, All Rights Reserved.
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
package org.riverock.common.tools.servlet;

import java.util.Map;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Collections;
import java.net.URL;
import java.net.MalformedURLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.lang.StringUtils;

import org.riverock.common.tools.MainTools;
import org.riverock.common.tools.StringTools;
import org.riverock.common.tools.ServletTools;

/**
 * User: SergeMaslyukov
 * Date: 04.02.2005
 * Time: 0:44:53
 * $Id: HttpServletRequestWrapperFromString.java 1217 2007-06-26 19:45:31Z serg_main $
 */
public final class HttpServletRequestWrapperFromString extends HttpServletRequestWrapper {
    Map<String, Object> param = null;
    URL url = null;
    public HttpServletRequestWrapperFromString(HttpServletRequest request, String url) throws MalformedURLException {
        super(request);
        this.url = new URL( url );
        this.param = ServletTools.getParameterMap( this.url.getQuery() );
    }

    public int getServerPort() {
        return url.getPort()!=-1?url.getPort():80;
    }

    public String getServerName() {
        return url.getHost();
    }

    public String getScheme() {
        return url.getProtocol();
    }

    public String getPathInfo() {
        if (StringUtils.isEmpty( url.getPath() ) )
            return null;

        if (url.getPath().startsWith( super.getContextPath() ) ) {
            return url.getPath().substring( super.getContextPath().length() );
        }
        else {
            return url.getPath();
        }
    }

    public String getContextPath() {
        if (url.getPath().startsWith( super.getContextPath() ) ) {
            return super.getContextPath();
        }
        else {
            return "/";
        }
    }

    public String getParameter(String name)
    {
        String s = null;
        if (param!=null)
        {
            Object obj = param.get(name);
            if (obj!=null)
            {
                // if List return value of 1st parameter
                if (obj instanceof List)
                    return ((List)obj).get(0).toString();
                else
                    return obj.toString();
            }
        }
        return s;
    }

    public Map getParameterMap()
    {
        Map<String, Object> map = new HashMap<String, Object>();

        if (param==null)
            return map;

        Iterator<String> iter = param.keySet().iterator();
        while (iter.hasNext())
        {
            String key = iter.next();
            MainTools.putKey(map, key, param.get(key));
        }

        return map;
    }

    public Enumeration getParameterNames() {
        return Collections.enumeration( getParameterMap().keySet() );
    }

    public String[] getParameterValues(String name)
    {
        Object obj = getParameterMap().get(name);
        if (obj instanceof List) {
            Object[] objects = ((List)obj).toArray();
            String[] strings = new String[objects.length];
            for (int i=0; i<objects.length; i++) {
                strings[i] = (String)objects[i];
            }
            return strings;
        }

        return new String[]{obj.toString()};
    }
}
