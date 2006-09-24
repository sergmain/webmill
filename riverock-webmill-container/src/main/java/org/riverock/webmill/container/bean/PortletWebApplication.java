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
package org.riverock.webmill.container.bean;

import java.io.Serializable;

import javax.servlet.ServletConfig;

import org.riverock.webmill.container.portlet.bean.PortletDefinition;

/**
 * @author smaslyukov
 *         Date: 29.07.2005
 *         Time: 16:31:19
 *         $Id$
 */
public class PortletWebApplication implements Serializable {
    private static final long serialVersionUID = 30430072384237876L;

    private ClassLoader classLoader = null;
    private ServletConfig servletConfig = null;
    private PortletDefinition portletDefinition = null;
    private String uniqueName = null;
    /**
     * boolean flag indicated, what portal already register this portlet
     */
    private boolean isRegistered=false;

    public String getUniqueName() {
        return uniqueName;
    }

    public void setUniqueName(String uniqueName) {
        this.uniqueName = uniqueName;
    }

    public PortletDefinition getPortletDefinition() {
        return portletDefinition;
    }

    public void setPortletDefinition(PortletDefinition portletDefinition) {
        this.portletDefinition = portletDefinition;
    }

    public ServletConfig getServletConfig() {
        return servletConfig;
    }

    public void setServletConfig(ServletConfig servletConfig) {
        this.servletConfig = servletConfig;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public boolean isRegistered() {
        return isRegistered;
    }

    public void setRegistered(boolean registered) {
        isRegistered = registered;
    }
}
