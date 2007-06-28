/*
 * org.riverock.module - Abstract layer for web module
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock -- The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package org.riverock.module.web.request;

import javax.portlet.PortletRequest;

import org.riverock.module.web.user.ModuleUser;
import org.riverock.module.web.user.WebmillModuleUserImpl;
import org.riverock.interfaces.ContainerConstants;
import org.riverock.webmill.container.tools.PortletService;

/**
 * @author Serge Maslyukov
 *         Date: 24.04.2005
 *         Time: 20:44:52
 *         $Id$
 */
public class WebmillPortletModuleRequestImpl extends PortletModuleRequestImpl {

    public WebmillPortletModuleRequestImpl(PortletRequest portletRequest) {
        this.portletRequest = portletRequest;
    }

    public String getRemoteAddr() {
        return PortletService.getRemoteAddr( portletRequest );
    }

    public String getUserAgent() {
        return PortletService.getUserAgent( portletRequest );
    }

    public Long getSiteId() {
        return new Long(portletRequest.getPortalContext().getProperty( ContainerConstants.PORTAL_PROP_SITE_ID ));
    }

    public Object getAttribute(String key) {
        return portletRequest.getAttribute( key );
    }

    public ModuleUser getUser() {
        if (portletRequest.getUserPrincipal()!=null) {
            return new WebmillModuleUserImpl( portletRequest.getUserPrincipal() );
        }
        else {
            return null;
        }
    }
}
