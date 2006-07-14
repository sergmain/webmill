/*
 * org.riverock.webmill.init - Webmill portal initializer web application
 * For more information about Webmill portal, please visit project site
 * http://webmill.askmore.info
 *
 * Copyright (C) 2000-2006, Riverock Software, All Rights Reserved.
 * Riverock - The Open-source Java Development Community,
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
 */
package org.riverock.webmill.init.bean;

import java.io.Serializable;

import org.riverock.webmill.init.utils.FacesTools;

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
