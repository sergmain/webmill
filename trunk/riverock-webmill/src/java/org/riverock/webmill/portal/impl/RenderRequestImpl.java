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

/**
 * User: Admin
 * Date: Sep 20, 2003
 * Time: 1:00:05 AM
 *
 * $Id$
 */
package org.riverock.webmill.portal.impl;

import java.util.Map;

import javax.portlet.RenderRequest;
import javax.servlet.ServletContext;

import org.riverock.webmill.portal.PortalRequestInstance;

public final class RenderRequestImpl extends WebmillPortletRequestV2 implements RenderRequest {

    public RenderRequestImpl(final Map parameters, final PortalRequestInstance portalRequestInstance, final Map renderParameters, ServletContext servletContext, Map portletAttributes, String contextPath) {
        super( servletContext, portalRequestInstance.getHttpRequest() );
        prepareRequest( parameters, portalRequestInstance, renderParameters, portletAttributes, contextPath);
    }
}
