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

import java.util.Date;
import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;

import org.riverock.interfaces.portal.user.UserMetadataItem;

/**
 * @author Sergei Maslyukov
 *         Date: 26.05.2006
 *         Time: 20:21:04
 */
@Entity
//@Table(name="WM_LIST_USER_METADATA")
@Table(name="wm_list_user_metadata")
@TableGenerator(
    name="TABLE_LIST_USER_METADATA",
    table="WM_PORTAL_IDS",
    pkColumnName = "sequence_name",
    valueColumnName = "sequence_next_value",
    pkColumnValue = "wm_list_user_metadata",
    allocationSize = 1,
    initialValue = 1
)
public class UserMetadataItemBean implements UserMetadataItem, Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.TABLE, generator = "TABLE_LIST_USER_METADATA")
    @Column(name="ID_MAIN_USER_METADATA")
    private Long metadataItemId = null;

    @Column(name="ID_SITE")
    private Long siteId = null;
    
    @Column(name="ID_USER")
    private Long userId = null;

    @Column(name="META")
    private String metadataName = null;

    @Column(name="INT_VALUE")
    private Long intValue = null;

    @Column(name="DATE_VALUE")
    private Date dateValue = null;

    @Column(name="STRING_VALUE")
    private String stringValue = null;

    public Long getMetadataItemId() {
        return metadataItemId;
    }

    public void setMetadataItemId(Long metadataItemId) {
        this.metadataItemId = metadataItemId;
    }

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getMetadataName() {
        return metadataName;
    }

    public void setMetadataName(String metadataName) {
        this.metadataName = metadataName;
    }

    public Long getIntValue() {
        return intValue;
    }

    public void setIntValue(Long intValue) {
        this.intValue = intValue;
    }

    public Date getDateValue() {
        if (dateValue==null) {
            return null;
        }
        return new Date(dateValue.getTime());
    }

    public void setDateValue(Date dateValue) {
        if (dateValue==null) {
            this.dateValue=null;
            return;
        }
        this.dateValue = new Date(dateValue.getTime());
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }
}
