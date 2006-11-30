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
package org.riverock.portlet.webclip;

import java.util.Date;
import java.io.Serializable;
import java.sql.Blob;

import javax.persistence.Entity;
import javax.persistence.TableGenerator;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.Transient;
import javax.persistence.Version;

/**
 * User: SergeMaslyukov
 * Date: 10.09.2006
 * Time: 17:49:00
 * <p/>
 * $Id$
 */
@Entity
@Table(name="wm_portlet_webclip")
@TableGenerator(
    name="TABLE_PORTLET_WEBCLIP",
    table="wm_portal_ids",
    pkColumnName = "sequence_name",
    valueColumnName = "sequence_next_value",
    pkColumnValue = "wm_portlet_webclip",
    allocationSize = 1,
    initialValue = 1
)
public class WebclipBean implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.TABLE, generator = "TABLE_PORTLET_WEBCLIP")
    @Column(name="ID_WEBCLIP")
    private Long webclipId;

    @Column(name="ID_SITE")
    private Long siteId;

    @Column(name="WEBCLIP_BLOB")
    private Blob webclipBlob;

    @Column(name="DATE_POST")
    private Date datePost;

    @Version
    @Column(name="VERSION")
    private int version;

    @Transient
    private String webclipData;

    public WebclipBean() {
    }

    public WebclipBean(Long siteId, Long webclipId, String webclipData, Date datePost) {
        this.webclipId = webclipId;
        this.siteId = siteId;
        this.webclipData = webclipData;
        this.datePost = datePost;
    }

    public Blob getWebclipBlob() {
        return webclipBlob;
    }

    public void setWebclipBlob(Blob webclipBlob) {
        this.webclipBlob = webclipBlob;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Long getWebclipId() {
        return webclipId;
    }

    public void setWebclipId(Long webclipId) {
        this.webclipId = webclipId;
    }

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public String getWebclipData() {
        return webclipData;
    }

    public void setWebclipData(String webclipData) {
        this.webclipData = webclipData;
    }

    public Date getDatePost() {
        return datePost;
    }

    public void setDatePost(Date datePost) {
        this.datePost = datePost;
    }

    public String toString() {
        return "webclipId: "+webclipId + ", siteId: "+siteId;
    }
}
