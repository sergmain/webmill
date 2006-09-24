/*
 * org.riverock.webmill.container - Webmill portlet container implementation
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
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
package org.riverock.webmill.container.impl;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Set;

import javax.portlet.PortletContext;
import javax.portlet.PortletRequestDispatcher;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;

/**
 * Author: Apache group, projects Pluto, License Apache2
 */
public final class PortletContextImpl implements PortletContext {
    private final static int PORTLET_API_MAJOR_VERSION = 1;
    private final static int PORTLET_API_MINOR_VERSION = 0;

    private ServletContext servletContext;
    private String portalName;

    public PortletContextImpl(ServletContext servletContext, String portalName) {
        this.servletContext = servletContext;
        this.portalName = portalName;
    }

    public String getServerInfo() {
        return portalName;
    }

    public PortletRequestDispatcher getRequestDispatcher(String path) {
        return createRequestDispatcher(servletContext, path);
    }

    public static PortletRequestDispatcher createRequestDispatcher(ServletContext servletContext, String path) {
        // Check if the path name is valid. A valid path name must not be null
        //   and must start with a slash '/' as defined by the portlet spec.
        if (path == null || !path.startsWith("/")) {
            System.out.println("Failed to retrieve PortletRequestDispatcher: " +
                "path name must begin with a slash '/', path: " + path);
            return null;
        }

        // Extract query string which contains appended parameters.
        String queryString = null;
        int index = path.indexOf("?");
        if (index > 0 && index < path.length() - 1) {
            queryString = path.substring(index + 1);
            System.out.println("queryString = " + queryString);
        }

        // Construct PortletRequestDispatcher.
        PortletRequestDispatcher portletRequestDispatcher = null;
        try {
            RequestDispatcher servletRequestDispatcher = servletContext.getRequestDispatcher(path);
            if (servletRequestDispatcher != null) {
                portletRequestDispatcher = new PortletRequestDispatcherImpl(servletRequestDispatcher, queryString);
            }
            else {
                System.out.println("No matching request dispatcher found for: " + path);
            }
        } catch (Exception ex) {
            // We need to catch exception because of a Tomcat 4.x bug.
            //   Tomcat throws an exception instead of return null if the path
            //   was not found.
            ex.printStackTrace();
            portletRequestDispatcher = null;
        }
        return portletRequestDispatcher;
    }

    public PortletRequestDispatcher getNamedDispatcher(String name) {
        RequestDispatcher dispatcher = servletContext.getNamedDispatcher(name);
        if (dispatcher != null) {
            return new PortletRequestDispatcherImpl(dispatcher);
        } else {
            System.out.println("No matching request dispatcher found for name: "+ name);
        }
        return null;
    }

    public InputStream getResourceAsStream(final String path) {
        return servletContext.getResourceAsStream(path);
    }

    public int getMajorVersion() {
        return PORTLET_API_MAJOR_VERSION;
    }

    public int getMinorVersion() {
        return PORTLET_API_MINOR_VERSION;
    }

    public String getMimeType(final String file) {
        return servletContext.getMimeType(file);
    }

    public String getRealPath(final String path) {
        return servletContext.getRealPath(path);
    }

    public Set getResourcePaths(final String path) {
        return servletContext.getResourcePaths(path);
    }

    public URL getResource(final String path) throws MalformedURLException {
        if (path == null || !path.startsWith("/")) {
            throw new MalformedURLException("path must start with a '/'");
        }
        return servletContext.getResource(path);
    }

    public Object getAttribute(final String name) {
        if (name == null) {
            throw new IllegalArgumentException("Attribute name == null");
        }

        return servletContext.getAttribute(name);
    }

    public Enumeration getAttributeNames() {
        return servletContext.getAttributeNames();
    }

    public String getInitParameter(final String name) {
        if (name == null) {
            throw new IllegalArgumentException("Parameter name == null");
        }

        return servletContext.getInitParameter(name);
    }

    public Enumeration getInitParameterNames() {
        return servletContext.getInitParameterNames();
    }

    public void log(final String msg) {
        servletContext.log(msg);
    }

    public void log(final String message, final Throwable throwable) {
        servletContext.log(message, throwable);
    }

    public void removeAttribute(final String name) {
        if (name == null) {
            throw new IllegalArgumentException("Attribute name == null");
        }

        servletContext.removeAttribute(name);
    }

    public void setAttribute(final String name, final Object object) {
        if (name == null) {
            throw new IllegalArgumentException("Attribute name == null");
        }

        servletContext.setAttribute(name, object);
    }

    public String getPortletContextName() {
        return servletContext.getServletContextName();
    }

    /**
     * @deprecated
     */
    public ServletContext getServletContext() {
        return servletContext;
    }
}

