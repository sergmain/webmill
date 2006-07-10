/*
 * org.riverock.module - Abstract layer for web module
 *
 * Copyright (C) 2004, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
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
 *
 */
package org.riverock.module.web.response;

import javax.portlet.PortletResponse;
import javax.portlet.ActionResponse;
import java.io.IOException;

/**
 * @author Serge Maslyukov
 *         Date: 24.04.2005
 *         Time: 15:26:22
 *         $Id$
 */
public class PortletModuleResponseImpl implements ModuleResponse {
    private PortletResponse portletResponse = null;

    public PortletModuleResponseImpl(PortletResponse portletResponse) {
        this.portletResponse = portletResponse;
    }

    public Object getOriginResponse() {
        return portletResponse;
    }

    public void sendRedirect(String newUrl) throws IOException {
        if (portletResponse instanceof ActionResponse) {
            ((ActionResponse)portletResponse).sendRedirect(newUrl);
        }
        else {
            throw new IllegalStateException("sendRedirect not supported by "+portletResponse.getClass().getName());
        }
    }
}
