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

import org.riverock.interfaces.portal.bean.VirtualHost;

/**
 * @author Sergei Maslyukov
 *         Date: 02.05.2006
 *         Time: 17:56:55
 */
@Entity
//@Table(name="WM_PORTAL_VIRTUAL_HOST")
@Table(name="wm_portal_virtual_host")
@TableGenerator(
    name="TABLE_VIRTUAL_HOST",
    table="WM_PORTAL_IDS",
    pkColumnName = "sequence_name",
    valueColumnName = "sequence_next_value",
    pkColumnValue = "wm_portal_virtual_host",
    allocationSize=1,
    initialValue = 1
)
public class VirtualHostBean implements Serializable, VirtualHost {
    private static final long serialVersionUID = 3255005504L;

    @Id
    @GeneratedValue(strategy= GenerationType.TABLE, generator = "TABLE_VIRTUAL_HOST")
    @Column(name="ID_SITE_VIRTUAL_HOST")
    private Long id = null;

    @Column(name="ID_SITE")
    private Long siteId = null;

    @Column(name="NAME_VIRTUAL_HOST")
    private String host = null;

    public VirtualHostBean() {
    }

    public VirtualHostBean(Long id, Long siteId, String host) {
        this.id = id;
        this.siteId = siteId;
        this.host = host;
    }

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
