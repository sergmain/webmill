/*
 * org.riverock.webmill - Webmill portal with support jsr-168, xml/xslt and others things.
 * For more information, please visit project site http://webmill.riverock.org
 *
 * Copyright (C) 2000-2006, Riverock Software, All Rights Reserved.
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
package org.riverock.webmill.portal.bean;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.Transient;

import org.riverock.interfaces.portal.bean.PortletName;

/**
 * @author SergeMaslyukov
 *         Date: 27.01.2006
 *         Time: 16:39:36
 *         $Id$
 */
@Entity
@Table(name="WM_PORTAL_PORTLET_NAME")
//@Table(name="wm_portal_portlet_name")
@TableGenerator(
    name="TABLE_PORTLET_NAME",
    table="WM_PORTAL_IDS",
    pkColumnName = "sequence_name",
    valueColumnName = "sequence_next_value",
    pkColumnValue = "wm_portal_portlet_name",
    allocationSize = 1,
    initialValue = 1
)
public class PortletNameBean implements Serializable, PortletName {
    private static final long serialVersionUID = 1057005503L;

    @Id
    @GeneratedValue(strategy= GenerationType.TABLE, generator = "TABLE_PORTLET_NAME")
    @Column(name="ID_SITE_CTX_TYPE")
    private Long portletId;

    @Column(name="TYPE")
    private String portletName;

    @Transient
    private boolean isActive;

    public PortletNameBean() {
    }

    public PortletNameBean(PortletName portletName) {
        this.portletId = portletName.getPortletId();
        this.portletName = portletName.getPortletName();
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
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
}
