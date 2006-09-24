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
package org.riverock.generic.tools.servlet;

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

import org.riverock.common.tools.MainTools;
import org.riverock.common.tools.StringTools;
import org.riverock.common.tools.ServletTools;

/**
 * User: SergeMaslyukov
 * Date: 04.02.2005
 * Time: 0:44:53
 * $Id$
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
        if (StringTools.isEmpty( url.getPath() ) )
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
