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
package org.riverock.webmill.container.portlet;

import javax.portlet.Portlet;
import javax.portlet.PortletConfig;
import javax.portlet.UnavailableException;
import javax.servlet.ServletConfig;

import org.riverock.webmill.container.portlet.bean.PortletDefinition;

/**
 * @author smaslyukov
 *         Date: 27.07.2005
 *         Time: 19:37:45
 *         $Id$
 */
public final class PortletEntry {
    private Portlet portlet = null;
    private boolean isPermanent = false;
    private int interval = 0;
    private long lastInitTime = 0;
    private PortletConfig portletConfig = null;
    private PortletDefinition portletDefinition = null;
    private ServletConfig servletConfig = null;
    private ClassLoader classLoader = null;
    private String uniqueName = null;

    public void destroy() {
        if (portlet!=null) {
            portlet.destroy();
        }
        portlet = null;
        portletConfig = null;
        if (portletDefinition!=null) {
            portletDefinition.destroy();
        }
        portletDefinition = null;
        servletConfig = null;
        classLoader = null;
        uniqueName = null;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public String getUniqueName() {
        return uniqueName;
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public ServletConfig getServletConfig() {
        return servletConfig;
    }

    public void setServletConfig(ServletConfig servletConfig) {
        this.servletConfig = servletConfig;
    }

    PortletEntry(PortletDefinition portletDefinition, PortletConfig portletConfig, Portlet portlet,ServletConfig servletConfig, ClassLoader classLoader, String uniqueName ) {
        this.portletDefinition = portletDefinition;
        this.portletConfig = portletConfig;
        this.portlet = portlet;
        this.servletConfig = servletConfig;
        this.classLoader = classLoader;
        this.uniqueName = uniqueName;
    }

    PortletEntry(UnavailableException e) {
        this.lastInitTime = System.currentTimeMillis();
        this.interval = e.getUnavailableSeconds();
        this.isPermanent = e.isPermanent();
        if (interval <= 0)
            this.isPermanent = true;
    }

    public boolean getIsWait() {
        if (getIsPermanent())
            return true;

        if (getInterval() > (System.currentTimeMillis() - getLastInitTime()) / 1000)
            return true;

        return false;
    }

    public PortletDefinition getPortletDefinition() {
        return portletDefinition;
    }

    public Portlet getPortlet() {
        return portlet;
    }

    public long getLastInitTime() {
        return lastInitTime;
    }

    public boolean getIsPermanent() {
        return isPermanent;
    }

    public int getInterval() {
        return interval;
    }

    protected void setPortlet(Portlet portlet) {
        this.portlet = portlet;
    }

    protected void setPermanent(boolean permanent) {
        isPermanent = permanent;
    }

    protected void setInterval(int interval) {
        this.interval = interval;
    }

    public PortletConfig getPortletConfig() {
        return portletConfig;
    }
}

