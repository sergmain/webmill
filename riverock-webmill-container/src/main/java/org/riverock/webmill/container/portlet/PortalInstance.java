/*
 * org.riverock.webmill.container - Webmill portlet container implementation
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
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
 */
package org.riverock.webmill.container.portlet;

import java.util.Collection;

import javax.servlet.ServletConfig;

import org.riverock.interfaces.portal.search.PortalIndexer;

/**
 * @author smaslyukov
 *         Date: 29.07.2005
 *         Time: 17:41:20
 *         $Id$
 */
public interface PortalInstance {
    public String getPortalName();
    public int getPortalMajorVersion();
    public int getPortalMinorVersion();
    public Collection<String> getSupportedLocales();

    void registerPortlet(String fullPortletName);

    void destroyPortlet(String fullPortletName);

    PortletContainer getPortletContainer();
    
    ServletConfig getPortalServletConfig();

    PortalIndexer getPortalIndexer();

    ClassLoader getPortalClassLoader();
}
