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
package org.riverock.webmill.portal.context;

import javax.portlet.PortletMode;
import javax.portlet.WindowState;

/**
 * User: SergeMaslyukov
 * Date: 03.03.2006
 * Time: 03:26:06
 */
public final class RequestState {

    private boolean isActionRequest = false;
    private PortletMode portletMode = PortletMode.VIEW;
    private WindowState windowState = WindowState.NORMAL;

    public String toString() {
        return "[action:"+isActionRequest+",mode:"+portletMode+",state:"+windowState+"]";
    }

    public PortletMode getPortletMode() {
        return portletMode;
    }

    public void setPortletMode(PortletMode portletMode) {
        this.portletMode = portletMode;
    }

    public WindowState getWindowState() {
        return windowState;
    }

    public void setWindowState(WindowState windowState) {
        this.windowState = windowState;
    }

    public boolean isActionRequest() {
        return isActionRequest;
    }

    public void setActionRequest( boolean isActionRequest ) {
        this.isActionRequest = isActionRequest;
    }
}