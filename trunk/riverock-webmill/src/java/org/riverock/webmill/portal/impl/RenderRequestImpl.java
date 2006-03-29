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

import java.util.Map;
import java.util.List;

import javax.portlet.RenderRequest;
import javax.portlet.PortletPreferences;
import javax.portlet.PortalContext;
import javax.servlet.ServletContext;

import org.riverock.webmill.portal.PortalRequestInstance;

/**
 * User: Admin
 * Date: Sep 20, 2003
 * Time: 1:00:05 AM
 *
 * $Id$
 */
public final class RenderRequestImpl extends WebmillPortletRequest implements RenderRequest {

    public RenderRequestImpl( 
        final Map<String, Object> parameters,
        final PortalRequestInstance portalRequestInstance, 
        final Map<String, Object> renderParameters,
        final ServletContext servletContext, 
        final Map<String, Object> portletAttributes, 
        final String contextPath, 
        final PortletPreferences portletPreferences, 
        final Map<String, List<String>> portletProperties,
        final PortalContext portalContext ) {
        
        super( servletContext, portalRequestInstance.getHttpRequest(), portletPreferences, portletProperties );
        prepareRequest( parameters, portalRequestInstance, renderParameters, portletAttributes, contextPath, portalContext);
    }
}