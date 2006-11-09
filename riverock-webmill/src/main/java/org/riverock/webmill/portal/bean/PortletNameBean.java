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
import javax.persistence.Transient;

import org.riverock.interfaces.portal.bean.PortletName;

/**
 * @author SergeMaslyukov
 *         Date: 27.01.2006
 *         Time: 16:39:36
 *         $Id$
 */
@Entity
@Table(name="wm_portal_portlet_name")
@TableGenerator(
    name="TABLE_PORTLET_NAME",
    table="wm_portal_ids",
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
