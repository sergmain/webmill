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
@Table(name="wm_portal_virtual_host")
@TableGenerator(
    name="TABLE_VIRTUAL_HOST",
    table="wm_portal_ids",
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
    @Column(name="ID_SITE_SUPPORT_LANGUAGE")
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
