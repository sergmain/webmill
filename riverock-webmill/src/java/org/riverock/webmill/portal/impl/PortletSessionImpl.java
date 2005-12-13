/*
 * org.riverock.webmill -- Portal framework implementation
 *
 * Copyright (C) 2004, Riverock Software, All Rights Reserved.
 *
 * Riverock -- The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 */
package org.riverock.webmill.portal.impl;

import java.util.Enumeration;

import javax.portlet.PortletContext;
import javax.portlet.PortletSession;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

import org.apache.log4j.Logger;

/**
 * User: serg_main
 * Date: 12.05.2004
 * Time: 18:50:27
 * @author Serge Maslyukov
 * $Id$
 */
public final class PortletSessionImpl implements PortletSession, HttpSession {
    private final static Logger log = Logger.getLogger( PortletSessionImpl.class );

    private HttpSession session = null;
    public PortletSessionImpl(HttpSession session){
        if (session==null) {
            throw new NullPointerException("session object is null");
        }
        this.session = session;
//        creationTime = System.currentTimeMillis();
    }

    private long creationTime;
    public long getCreationTime(){
        return session.getCreationTime();
    }

    public String getId(){
        accessTime = System.currentTimeMillis();
        return session.getId();
    }

    private long accessTime;
    public long getLastAccessedTime(){
//        return accessTime;
        return session.getLastAccessedTime();
    }

    public void setMaxInactiveInterval(int i){
        accessTime = System.currentTimeMillis();
        session.setMaxInactiveInterval(i);
    }

    public PortletContext getPortletContext(){
        accessTime = System.currentTimeMillis();
        return null;
    }

    public int getMaxInactiveInterval(){
        accessTime = System.currentTimeMillis();
        return session.getMaxInactiveInterval();
    }

    public Object getAttribute(String s){
        accessTime = System.currentTimeMillis();
        return session.getAttribute( s );
    }

    public Object getAttribute(String s, int i){
        accessTime = System.currentTimeMillis();
        return session.getAttribute(s);
    }

    public Enumeration getAttributeNames(){
        accessTime = System.currentTimeMillis();
        return session.getAttributeNames();
    }

    public Enumeration getAttributeNames(int i){
        accessTime = System.currentTimeMillis();
        return session.getAttributeNames();
    }

    public void setAttribute(String s, Object o){
        accessTime = System.currentTimeMillis();
        session.setAttribute( s,  o);
    }

    public void setAttribute(String s, Object o, int i){
        accessTime = System.currentTimeMillis();
        session.setAttribute( s,  o);
    }

    public void removeAttribute(String s){
        accessTime = System.currentTimeMillis();
        session.removeAttribute( s);
    }

    public void removeAttribute(String s, int i){
        accessTime = System.currentTimeMillis();
        try
        {
            session.removeAttribute( s );
        }
        catch (IllegalStateException e)
        {
            log.error("session.removeAttribute() ", e);
            throw e;
        }
    }

    public void invalidate(){
        accessTime = System.currentTimeMillis();
        session.invalidate();
    }

    public boolean isNew(){
        accessTime = System.currentTimeMillis();
        return session.isNew();
    }

    // HttpSession methods implementation

    public ServletContext getServletContext() {
        return session.getServletContext();
    }

    public HttpSessionContext getSessionContext() {
        return session.getSessionContext();
    }

    public Object getValue(String s) {
        return session.getValue( s );
    }

    public String[] getValueNames() {
        return session.getValueNames();
    }

    public void putValue(String s, Object o) {
        session.putValue( s, o );
    }

    public void removeValue(String s) {
        session.removeValue( s );
    }
}
