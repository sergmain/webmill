/**
 * User: serg_main
 * Date: 13.05.2004
 * Time: 17:55:24
 * @author Serge Maslyukov
 * $Id$
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


public final class HttpServletRequestWrapperInclude extends HttpServletRequestWrapper {
    Map param = null;
    public HttpServletRequestWrapperInclude(HttpServletRequest request, Map param)
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
        Map map = new HashMap();
        map.putAll( super.getParameterMap());

        if (param==null)
            return map;

        Iterator iter = param.keySet().iterator();
        while (iter.hasNext())
        {
            Object obj = iter.next();
            MainTools.putKey(map, obj, param.get(obj));
        }

        return map;
    }

    public Enumeration getParameterNames() {
        return Collections.enumeration( getParameterMap().keySet() );
    }

    public String[] getParameterValues(String name)
    {
        Object obj = getParameterMap().get(name);
        if (obj instanceof List)
            return (String[])((List)obj).toArray();

        return new String[]{obj.toString()};
    }
}
