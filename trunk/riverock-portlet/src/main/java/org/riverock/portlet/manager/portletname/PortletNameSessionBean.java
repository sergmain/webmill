/*
 * org.riverock.portlet - Portlet Library
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
package org.riverock.portlet.manager.portletname;

import java.io.Serializable;

import org.riverock.interfaces.portal.bean.PortletName;

/**
 * @author SergeMaslyukov
 *         Date: 26.02.2006
 *         Time: 15:55:44
 *         $Id$
 */
public class PortletNameSessionBean implements Serializable {
    private static final long serialVersionUID = 2057005504L;

    private PortletName portletName = null;
    private Long currentPortletNameId = null;

    public PortletNameSessionBean() {
    }

    public PortletName getPortletName() {
        return portletName;
    }

    public void setPortletName(PortletName portletName) {
        this.portletName = portletName;
    }

    public Long getCurrentPortletNameId() {
        return currentPortletNameId;
    }

    public void setCurrentPortletNameId(Long currentPortletNameId) {
        this.currentPortletNameId = currentPortletNameId;
    }
}
