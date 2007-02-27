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
import java.sql.Blob;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.riverock.interfaces.portal.bean.Xslt;

/**
 * @author Sergei Maslyukov
 *         Date: 02.05.2006
 *         Time: 17:21:17
 */
@Entity
//@Table(name="WM_PORTAL_XSLT")
@Table(name="wm_portal_xslt")
@TableGenerator(
    name="TABLE_XSLT",
    table="WM_PORTAL_IDS",
    pkColumnName = "sequence_name",
    valueColumnName = "sequence_next_value",
    pkColumnValue = "wm_portal_xslt",
    allocationSize = 1,
    initialValue = 1
)
public class PortalXsltBean implements Serializable, Xslt {
    private static final long serialVersionUID = 3255005501L;

    @Id
    @GeneratedValue(strategy= GenerationType.TABLE, generator = "TABLE_XSLT")
    @Column(name="ID_SITE_XSLT")
    private Long id = null;

    @Column(name="ID_SITE_SUPPORT_LANGUAGE")
    private Long siteLanguageId = null;

    @Column(name="TEXT_COMMENT")
    private String name = null;

    @Column(name="IS_CURRENT")
    private boolean isCurrent = false;

    @Column(name="XSLT_BLOB")
    private Blob xsltBlob;

    @Version
    @Column(name="VERSION")
    private int version;

    @Transient
    private String xsltData = null;


    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Blob getXsltBlob() {
        return xsltBlob;
    }

    public void setXsltBlob(Blob xsltBlob) {
        this.xsltBlob = xsltBlob;
    }

    public boolean isCurrent() {
        return isCurrent;
    }

    public void setCurrent(boolean current) {
        isCurrent = current;
    }

    public Long getSiteLanguageId() {
        return siteLanguageId;
    }

    public void setSiteLanguageId(Long siteLanguageId) {
        this.siteLanguageId = siteLanguageId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getXsltData() {
        return xsltData;
    }

    public void setXsltData(String xsltData) {
        this.xsltData = xsltData;
    }

    public String toString() {
        return "[xsltId:"+id+",siteLanguageId:"+siteLanguageId+",name:"+name+",version:"+version+"]";
    }
}
