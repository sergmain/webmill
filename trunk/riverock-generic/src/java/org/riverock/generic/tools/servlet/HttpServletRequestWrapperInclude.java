/*
 * org.riverock.generic -- Database connectivity classes
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
package org.riverock.generic.tools.servlet;

import java.util.Map;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.riverock.common.tools.MainTools;

/**
 * User: serg_main
 * Date: 13.05.2004
 * Time: 17:55:24
 * @author Serge Maslyukov
 * $Id$
 */
public final class HttpServletRequestWrapperInclude extends HttpServletRequestWrapper {
    Map<String, Object> param = null;
    public HttpServletRequestWrapperInclude(HttpServletRequest request, Map<String, Object> param)
    {
        super(request);
        this.param = param;
    }

    public String getParameter(String name)
    {
        String s = super.getParameter(name);
        if (s==null && param!=null)
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
        Iterator<Map.Entry> it = super.getParameterMap().entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = it.next();
            map.put( entry.getKey().toString(), entry.getValue() );
        }

        if (param==null)
            return map;

        Iterator<String> iter = param.keySet().iterator();
        while (iter.hasNext())
        {
            String key = iter.next();
            MainTools.putKey(map, key, param.get(key) );
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
