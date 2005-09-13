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
package org.riverock.webmill.portal.url;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import org.riverock.webmill.container.tools.PortletService;

/**
 * @author Serge Maslyukov
 *         Date: 17.04.2005
 *         Time: 20:34:29
 *         $Id$
 */
public class PortalUrlProviderImpl implements PortalUrlProvider {
    private PortletRequest portletRequest = null;
    private PortletResponse portletResponse = null;
    public PortalUrlProviderImpl(PortletRequest portletRequest, PortletResponse portletResponse) {
        this.portletRequest = portletRequest;
        this.portletResponse = portletResponse;
    }

    public String getUrl(String portletName) {
        return PortletService.url( portletName, portletRequest, portletResponse );
    }

    public StringBuffer getUrlStrigBuffer(String portletName) {
        return PortletService.urlStringBuffer( portletName, portletRequest, portletResponse );
    }
}
