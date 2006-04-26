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

import java.util.Enumeration;
import java.util.Hashtable;

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

    /**
     * @deprecated
     */
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
        return hash.keys();
    }

    /**
     * @deprecated
     */
    public String[] getValueNames()
    {
        accessTime = System.currentTimeMillis();
        return new String[0];
    }


    private Hashtable hash = new Hashtable();
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

    private boolean isValid = true;
    public void invalidate()
    {
        isValid = false;
        accessTime = System.currentTimeMillis();
    }

    public boolean isNew()
    {
        accessTime = System.currentTimeMillis();
        return false;
    }

}
