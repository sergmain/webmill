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
/*
 * Copyright 2003,2004 The Apache Software Foundation.
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
package org.riverock.webmill.portal.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.portlet.PortletContext;
import javax.portlet.PortletSession;
import javax.portlet.PortletSessionUtil;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import org.riverock.webmill.portal.namespace.Namespace;

/**
 * Implementation of the <code>javax.portlet.PortletSession</code> interface.
 *
 * @author <a href="mailto:ddewolf@apache.org">David H. DeWolf</a>
 * @author <a href="mailto:zheng@apache.org">ZHENG Zhong</a>
 */
public class PortletSessionImpl implements PortletSession, HttpSession {
    private static final Logger log = Logger.getLogger(PortletSessionImpl.class);

    /**
     * The default scope (<code>PORTLET_SCOPE</code>) for storing objects.
     */
    private static final int DEFAULT_SCOPE = PortletSession.PORTLET_SCOPE;

    /**
     * The portlet scope namespace as defined in PLT. 15.3.
     */
    private static final String PORTLET_SCOPE_NAMESPACE = "javax.portlet.p.";

    /**
     * The portlet window ID / attribute name separator as defined in PLT. 15.3.
     */
    private static final char ID_NAME_SEPARATOR = '?';

    // Private Member Variables ------------------------------------------------

    /**
     * The wrapped HttpSession object.
     */
    private HttpSession httpSession = null;

    /**
     * The portlet context.
     */
    private PortletContext portletContext = null;

    /**
     * The internal portlet window.
     */
    private Namespace namespace = null;

    // Constructor -------------------------------------------------------------

    /**
     * Constructs an instance.
     */
    public PortletSessionImpl(HttpSession httpSession, PortletContext portletContext, Namespace namespace) {
        if (log.isDebugEnabled()) {
            log.debug("Create new portlet session. HttpSession: " + httpSession);
            log.debug("http session ID: "+httpSession.getId());
            log.debug("http session context path: "+httpSession.getServletContext().getRealPath("/"));
        }
        if (httpSession == null) {
            throw new NullPointerException("session object is null");
        }
        this.portletContext = portletContext;
        this.namespace = namespace;
        this.httpSession = httpSession;
    }

    // PortletSession Impl: Attributes -----------------------------------------

    public Object getAttribute(String name) {
        return getAttribute(name, DEFAULT_SCOPE);
    }

    /**
     * Returns the attribute of the specified name under the given scope.
     *
     * @param name  the attribute name.
     * @param scope the scope under which the attribute object is stored.
     * @return the attribute object.
     */
    public Object getAttribute(String name, int scope) {
        validateNotNull("attributeName", name);
        String key = (scope == PortletSession.APPLICATION_SCOPE)
            ? name : createPortletScopedId(name);
        return httpSession.getAttribute(key);
    }

    public Enumeration getAttributeNames() {
        return getAttributeNames(DEFAULT_SCOPE);
    }

    public Enumeration getAttributeNames(int scope) {
        // Return all attribute names in the nested HttpSession object.
        if (scope == PortletSession.APPLICATION_SCOPE) {
            return httpSession.getAttributeNames();
        }
        // Return attribute names with the portlet-scoped prefix.
        else {
            List<String> portletScopedNames = new ArrayList<String>();
            for (Enumeration en = httpSession.getAttributeNames(); en.hasMoreElements();) {
                String name = (String) en.nextElement();
                if (isInCurrentPortletScope(name)) {
                    portletScopedNames.add(
                        PortletSessionUtil.decodeAttributeName(name));
                }
            }
            return Collections.enumeration(portletScopedNames);
        }
    }

    public void removeAttribute(String name) {
        removeAttribute(name, DEFAULT_SCOPE);
    }

    public void removeAttribute(String name, int scope) {
        validateNotNull("attributeName", name);
        if (scope == PortletSession.APPLICATION_SCOPE) {
            httpSession.removeAttribute(name);
        } else {
            httpSession.removeAttribute(createPortletScopedId(name));
        }
    }

    public void setAttribute(String name, Object value) {
        setAttribute(name, value, DEFAULT_SCOPE);
    }

    public void setAttribute(String name, Object value, int scope) {
        validateNotNull("attributeName", name);
        if (scope == PortletSession.APPLICATION_SCOPE) {
            httpSession.setAttribute(name, value);
        } else {
            httpSession.setAttribute(createPortletScopedId(name), value);
        }
    }

    // PortletSession Impl: Other Methods --------------------------------------

    public PortletContext getPortletContext() {
        return portletContext;
    }

    public long getCreationTime() {
        return httpSession.getCreationTime();
    }

    public String getId() {
        return httpSession.getId();
    }

    public long getLastAccessedTime() {
        return httpSession.getLastAccessedTime();
    }

    public int getMaxInactiveInterval() {
        return httpSession.getMaxInactiveInterval();
    }

    public void invalidate() throws IllegalStateException {
        httpSession.invalidate();
    }

    public boolean isNew() throws IllegalStateException {
        return httpSession.isNew();
    }

    /**
     * Specifies the time, in seconds, between client requests, before the
     * portlet container invalidates this session. A negative time indicates
     * the session should never timeout.
     * <p/>
     * [Portlet Spec. PLT. 15.4.] If the PortletSession object is invalidated
     * by a portlet, the portlet container must invalidate the associated
     * HttpSession object.
     * </p>
     *
     * @param interval an integer specifying the number of seconds.
     */
    public void setMaxInactiveInterval(int interval) {
        httpSession.setMaxInactiveInterval(interval);
        if (log.isDebugEnabled()) {
            log.debug("Session timeout set to: " + interval);
        }
    }

    // Private Methods ---------------------------------------------------------

    /**
     * Creates portlet-scoped ID for the specified attribute name.
     * Portlet-scoped ID for a given attribute name has the following form:
     * <code>javax.portlet.p.&lt;ID&gt;?&lt;name&gt;</code>
     * where <code>ID</code> is a unique identification for the portlet window
     * (assigned by the portal/portlet-container) that must not contain a '?'
     * character. <code>name</code> is the attribute name.
     * <p/>
     * Refer to Portlet Specification PLT. 15.3 for more details.
     * </p>
     *
     * @param name the attribute name.
     * @return portlet-scoped ID for the attribute name.
     */
    private String createPortletScopedId(String name) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(PORTLET_SCOPE_NAMESPACE);
        buffer.append(namespace.getNamespace());
        buffer.append(ID_NAME_SEPARATOR);
        buffer.append(name);
        return buffer.toString();
    }

    /**
     * Checks if the attribute name in APPLICATION_SCOPE is in the current
     * portlet scope.
     *
     * @param name the attribute name to check.
     * @return true if the attribute name is in the current portlet scope.
     * @see #createPortletScopedId(String)
     */
    private boolean isInCurrentPortletScope(String name) {
        // Portlet-scoped attribute names MUST start with "javax.portlet.p.",
        //   and contain the ID-name separator '?'.
        if (name.startsWith(PORTLET_SCOPE_NAMESPACE)
            && name.indexOf(ID_NAME_SEPARATOR) > -1) {
            String id = name.substring(PORTLET_SCOPE_NAMESPACE.length(),
                name.indexOf(ID_NAME_SEPARATOR));
            return (id.equals(namespace.getNamespace()));
        }
        // Application-scoped attribute names are not in portlet scope.
        else {
            return false;
        }
    }

    private void validateNotNull(String argumentName, Object argument) throws IllegalArgumentException {
        if (argument == null) {
            if (log.isDebugEnabled()) {
                log.debug("Validation failed for argument: " + argumentName + ": argument should not be null.");
            }
            throw new IllegalArgumentException("Illegal Argument: " + argumentName+ " (argument should not be null)");
        }
    }

    // HttpSession Impl --------------------------------------------------------

    public ServletContext getServletContext() {
        return httpSession.getServletContext();
    }

    /**
     * DEPRECATED: implemented for backwards compatability with HttpSession.
     *
     * @deprecated
     */
    @SuppressWarnings({"deprecation"})
    public javax.servlet.http.HttpSessionContext getSessionContext() {
        return httpSession.getSessionContext();
    }

    public Object getValue(String name) {
        return this.getAttribute(name, DEFAULT_SCOPE);
    }

    /**
     * DEPRECATED: Implemented for backwards compatibility with HttpSession.
     *
     * @deprecated
     */
    @SuppressWarnings({"deprecation"})
    public String[] getValueNames() {
        return httpSession.getValueNames();
    }

    public void putValue(String name, Object value) {
        this.setAttribute(name, value, DEFAULT_SCOPE);
    }

    public void removeValue(String name) {
        this.removeAttribute(name, DEFAULT_SCOPE);
    }

}
