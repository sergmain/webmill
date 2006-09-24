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