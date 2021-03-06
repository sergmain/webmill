/*
 * org.riverock.portlet - Portlet Library
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
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
package org.riverock.portlet.manager.menu.bean;

import java.io.Serializable;

import org.riverock.interfaces.portal.bean.PortletName;

/**
 * @author SergeMaslyukov
 *         Date: 26.02.2006
 *         Time: 15:55:44
 *         $Id: PortletNameBean.java 1049 2006-11-14 15:56:05Z serg_main $
 */
public class PortletNameBean implements Serializable, PortletName {
    private static final long serialVersionUID = 2057015507L;

    private Long portletId = null;
    private String portletName = null;
    private boolean isActive = false;

    public PortletNameBean() {
    }

    public PortletNameBean(PortletName bean) {
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
        this.portletName = portletName;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean equals( PortletName portletNameBean ) {
        if( portletNameBean == null || portletNameBean.getPortletId()==null || portletId==null ) {
            return false;
        }
        return portletNameBean.getPortletId().equals( portletId );
    }

    public String toString() {
        return "[name:" + portletName + ",id:" + portletId + "]";
    }
}
