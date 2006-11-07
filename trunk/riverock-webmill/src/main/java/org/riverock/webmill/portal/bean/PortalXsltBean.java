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
@Table(name="wm_portal_xslt")
@TableGenerator(
    name="TABLE_XSLT",
    table="wm_portal_ids",
    pkColumnName = "sequence_name",
    valueColumnName = "sequence_next_value",
    pkColumnValue = "wm_portal_xslt",
    allocationSize=1,
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
