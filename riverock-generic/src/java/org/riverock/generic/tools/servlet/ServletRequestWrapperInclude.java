package org.riverock.generic.tools.servlet;

import java.util.Map;
import java.util.Enumeration;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletRequest;

import org.riverock.common.tools.MainTools;

/**
 * User: serg_main
 * Date: 13.05.2004
 * Time: 17:55:24
 * @author Serge Maslyukov
 * $Id$
 */
public final class ServletRequestWrapperInclude extends HttpServletRequestWrapper {
    Map<String, Object> param = null;
    Map attributes = null;
    public ServletRequestWrapperInclude(HttpServletRequest request, Map<String, Object> param, Map attributes){
        super(request);
        this.param = param;
        this.attributes = attributes;
    }

    public Object getAttribute(String key) {
        Object o = attributes.get(key);
        if (o==null) {
            return super.getAttribute(key);
        }

        return o;
    }

    public Enumeration getAttributeNames() {
        List list = new ArrayList();
        list.addAll( Collections.list(super.getAttributeNames()) );
        list.addAll( attributes.keySet() );

        return Collections.enumeration(list);
    }

    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
        super.setAttribute(key, value);
    }

    public void removeAttribute(String key) {
        attributes.remove(key);
        super.removeAttribute(key);
    }

    public String getParameter(String name) {
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
