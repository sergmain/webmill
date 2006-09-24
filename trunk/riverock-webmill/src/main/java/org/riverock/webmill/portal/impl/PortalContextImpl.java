/*
 * org.riverock.webmill - Portal framework implementation
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

import org.riverock.webmill.container.ContainerConstants;
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
        return Collections.enumeration( Arrays.asList( new PortletMode[]{PortletMode.VIEW, PortletMode.EDIT, PortletMode.HELP} ) );
    }

    public Enumeration getSupportedWindowStates() {
        return Collections.enumeration( Arrays.asList( new WindowState[]{WindowState.NORMAL, WindowState.MAXIMIZED, WindowState.MINIMIZED} ) );
    }

    public String getPortalInfo() {
        return portalInfo;
    }
}
