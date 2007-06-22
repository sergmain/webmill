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

import java.io.Serializable;
import java.sql.Blob;
import java.sql.SQLException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * User: SergeMaslyukov
 * Date: 10.09.2006
 * Time: 17:49:00
 * <p/>
 * $Id$
 */
@Entity
@Table(name="WM_PORTLET_WEBCLIP")
@TableGenerator(
    name="TABLE_PORTLET_WEBCLIP",
    table="WM_PORTAL_IDS",
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

    @Column(name="ZIP_ORIGIN_CONTENT")
    @Cache(usage = CacheConcurrencyStrategy.NONE)
    private Blob zipOriginContent;

    @Column(name="WEBCLIP_BLOB")
    private Blob webclipBlob;

    @Column(name="IS_LOAD_CONTENT")
    private boolean isLoadContent = false;

    @Column(name="IS_PROCESS_CONTENT")
    private boolean isProcessContent = false;

    @Column(name="IS_INDEXED")
    private boolean isIndexed = false;

    @Transient
    private String webclipData = null;

    public WebclipBean() {
    }

    public byte[] getZippedOriginContentAsBytes() {
        if (zipOriginContent!=null) {
            try {
                return zipOriginContent.getBytes(1, (int)zipOriginContent.length());
            }
            catch (SQLException e) {
                String es = "Error get zipped origin content as bytes";
                throw new RuntimeException(es, e);
            }
        }
        return new byte[]{};
    }

    public Blob getZipOriginContent() {
        return zipOriginContent;
    }

    public void setZipOriginContent(Blob zipOriginContent) {
        this.zipOriginContent = zipOriginContent;
    }

    public Blob getWebclipBlob() {
        return webclipBlob;
    }

    public void setWebclipBlob(Blob webclipBlob) {
        this.webclipBlob = webclipBlob;
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

    public boolean isLoadContent() {
        return isLoadContent;
    }

    public void setLoadContent(boolean loadContent) {
        isLoadContent = loadContent;
    }

    public boolean isProcessContent() {
        return isProcessContent;
    }

    public void setProcessContent(boolean processContent) {
        isProcessContent = processContent;
    }

    public boolean isIndexed() {
        return isIndexed;
    }

    public void setIndexed(boolean indexed) {
        isIndexed = indexed;
    }

    public String toString() {
        return "[webclipId: "+webclipId + ", siteId: "+siteId+"]";
    }
}
