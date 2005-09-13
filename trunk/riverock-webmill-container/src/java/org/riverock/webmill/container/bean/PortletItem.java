/*
 * org.riverock.webmill.container -- Webmill portlet container implementation
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
public class PortletItem implements Serializable {
    private static final long serialVersionUID = 30430072384237876L;

    private ClassLoader classLoader = null;
    private ServletConfig servletConfig = null;
    private PortletDefinition portletDefinition = null;
    private boolean isNew = true;
    private String uniqueName = null;

    public String getUniqueName() {
        return uniqueName;
    }

    public void setUniqueName(String uniqueName) {
        this.uniqueName = uniqueName;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
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
}
