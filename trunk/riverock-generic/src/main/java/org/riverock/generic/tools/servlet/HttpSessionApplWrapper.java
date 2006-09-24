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

import java.util.Enumeration;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

/**
 * Author: mill
 * Date: Feb 19, 2003
 * Time: 2:36:30 PM
 *
 * $Id$
*/
@SuppressWarnings({"deprecation"})
public class HttpSessionApplWrapper implements HttpSession {

    public HttpSessionApplWrapper()
    {
        creationTime = System.currentTimeMillis();
    }

    private long creationTime;
    public long getCreationTime()
    {
        return creationTime;
    }

    public String getId()
    {
        accessTime = System.currentTimeMillis();
        return null;
    }

    private long accessTime;
    public long getLastAccessedTime()
    {
        return accessTime;
    }

    public ServletContext getServletContext()
    {
        accessTime = System.currentTimeMillis();
        return null;
    }

    public void setMaxInactiveInterval(int i)
    {
        accessTime = System.currentTimeMillis();
    }

    public int getMaxInactiveInterval()
    {
        accessTime = System.currentTimeMillis();
        return 0;
    }

    public HttpSessionContext getSessionContext()
    {
        accessTime = System.currentTimeMillis();
        return null;
    }

    public Object getAttribute(String s)
    {
        accessTime = System.currentTimeMillis();
        return hash.get( s );
    }

    /**
     * @deprecated
     */
    public Object getValue(String s)
    {
        accessTime = System.currentTimeMillis();
        return null;
    }

    public Enumeration getAttributeNames()
    {
        accessTime = System.currentTimeMillis();
        return Collections.enumeration(hash.keySet());
    }

    /**
     * @deprecated
     */
    public String[] getValueNames()
    {
        accessTime = System.currentTimeMillis();
        return new String[0];
    }


    private Map<String, Object> hash = new HashMap<String, Object>();
    public void setAttribute(String s, Object o)
    {
        accessTime = System.currentTimeMillis();
        hash.put( s,  o);
    }

    /**
     * @deprecated
     */
    public void putValue(String s, Object o)
    {
        accessTime = System.currentTimeMillis();
    }

    public void removeAttribute(String s)
    {
        accessTime = System.currentTimeMillis();
        hash.remove( s );
    }

    /**
     * @deprecated
     */
    public void removeValue(String s)
    {
        accessTime = System.currentTimeMillis();
    }

    public void invalidate()
    {
        accessTime = System.currentTimeMillis();
    }

    public boolean isNew()
    {
        accessTime = System.currentTimeMillis();
        return false;
    }

}
