/*
 * org.riverock.module - Abstract layer for web module
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
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
package org.riverock.module.web.request;

import javax.portlet.PortletRequest;

import org.riverock.module.web.user.ModuleUser;
import org.riverock.module.web.user.WebmillModuleUserImpl;
import org.riverock.webmill.container.ContainerConstants;
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
