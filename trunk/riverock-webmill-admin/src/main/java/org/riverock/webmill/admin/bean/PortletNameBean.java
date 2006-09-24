/*
 * org.riverock.webmill.admin - Webmill portal admin web application
 *
 * For more information about Webmill portal, please visit project site
 * http://webmill.riverock.org
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community,
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
package org.riverock.webmill.admin.bean;

import java.io.Serializable;

import org.riverock.webmill.admin.utils.FacesTools;

/**
 * @author Sergei Maslyukov
 *         Date: 13.07.2006
 *         Time: 18:22:34
 */
public class PortletNameBean implements Serializable {
    private static final long serialVersionUID = 2057005507L;

    private Long portletId = null;
    private String portletName = null;
    private boolean isActive = false;

    public PortletNameBean() {
    }

    public PortletNameBean(PortletNameBean bean) {
        this.portletId = bean.getPortletId();
        this.portletName = bean.getPortletName();
        this.isActive = bean.isActive();
    }

    public Long getPortletId() {
        return portletId;
    }

    public void setPortletId(Long portletId) {
        this.portletId = portletId;
    }

    public String getPortletName() {
        return portletName;
    }

    public void setPortletName(String portletName) {
        this.portletName = FacesTools.convertParameter(portletName);
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean equals( PortletNameBean portletNameBean ) {
        if( portletNameBean == null || portletNameBean.getPortletId()==null || portletId==null ) {
            return false;
        }
        return portletNameBean.getPortletId().equals( portletId );
    }

    public String toString() {
        return "[name:" + portletName + ",id:" + portletId + "]";
    }
}
