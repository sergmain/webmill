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
