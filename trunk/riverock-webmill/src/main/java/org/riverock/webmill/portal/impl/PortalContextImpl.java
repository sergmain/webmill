/*
 * org.riverock.webmill - Webmill portal with support jsr-168, xml/xslt and others things.
 * For more information, please visit project site http://webmill.riverock.org
 *
 * Copyright (C) 2000-2006, Riverock Software, All Rights Reserved.
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
package org.riverock.webmill.portal.impl;

import java.util.Enumeration;
import java.util.Collections;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;

import javax.portlet.PortalContext;
import javax.portlet.PortletMode;
import javax.portlet.WindowState;

import org.apache.log4j.Logger;

import org.riverock.interfaces.ContainerConstants;
import org.riverock.interfaces.portal.PortalInfo;

/**
 * @author SergeMaslyukov
 *         Date: 20.11.2005
 *         Time: 15:42:59
 *         $Id$
 */
public class PortalContextImpl implements PortalContext {
    private final static Logger log = Logger.getLogger( PortalContextImpl.class );

    private String portalInfo = null;
    private Map<String, String> properties = null;

    public PortalContextImpl(String portalInfoName, String contextPath, PortalInfo portalInfo) {
        this.portalInfo = portalInfoName;

        Map<String,String> map = new HashMap<String, String>();

        map.put( ContainerConstants.PORTAL_PROP_SITE_ID, portalInfo.getSiteId().toString() );
        map.put( ContainerConstants.PORTAL_PROP_COMPANY_ID, portalInfo.getCompanyId().toString() );
        map.put( ContainerConstants.PORTAL_PORTAL_CONTEXT_PATH, contextPath );
        if (log.isDebugEnabled()) {
            log.debug("portal context path: '" + contextPath +"'" );
            log.debug("portal context path in properties: '" +
                map.get(ContainerConstants.PORTAL_PORTAL_CONTEXT_PATH) +"'" );
        }
        map.putAll( portalInfo.getPortalProperties() );

        this.properties = map;
    }

    public String getProperty(String key) {
        return properties.get( key );
    }

    public Enumeration getPropertyNames() {
        return Collections.enumeration( properties.keySet() );
    }

    public Enumeration getSupportedPortletModes() {
        return Collections.enumeration( Arrays.asList(PortletMode.VIEW, PortletMode.EDIT, PortletMode.HELP) );
    }

    public Enumeration getSupportedWindowStates() {
        return Collections.enumeration( Arrays.asList(WindowState.NORMAL, WindowState.MAXIMIZED, WindowState.MINIMIZED) );
    }

    public String getPortalInfo() {
        return portalInfo;
    }
}
