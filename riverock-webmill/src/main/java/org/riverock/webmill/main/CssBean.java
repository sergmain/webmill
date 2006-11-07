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
package org.riverock.webmill.main;

import java.util.Date;
import java.io.Serializable;
import java.sql.Blob;

import javax.persistence.*;

import org.riverock.interfaces.portal.bean.Css;

/**
 * @author SergeMaslyukov
 *         Date: 27.01.2006
 *         Time: 3:11:20
 *         $Id$
 */
@Entity
@Table(name="wm_portal_css")
@TableGenerator(
    name="TABLE_CSS",
    table="wm_portal_ids",
    pkColumnName = "sequence_name",
    valueColumnName = "sequence_next_value",
    pkColumnValue = "wm_portal_css",
    allocationSize=1,
    initialValue = 1
)
public class CssBean implements Serializable, Css {
    private static final long serialVersionUID = 3037005502L;

    @Id
    @GeneratedValue(strategy=GenerationType.TABLE, generator = "TABLE_CSS")
    @Column(name="ID_SITE_CONTENT_CSS")
    private Long cssId = null;

    @Column(name="ID_SITE")
    private Long siteId = null;

    @Column(name="CSS_BLOB")
    private Blob cssBlob;

    @Column(name="TEXT_COMMENT")
    private String cssComment = "";

    @Column(name="DATE_POST")
    private Date date = null;

    @Column(name="IS_CURRENT")
    private boolean isCurrent = false;

    @Transient
    private String css = "";


    public Blob getCssBlob() {
        return cssBlob;
    }

    public void setCssBlob(Blob cssBlob) {
        this.cssBlob = cssBlob;
    }

    public String getCssComment() {
        return cssComment;
    }

    public void setCssComment(String cssComment) {
        this.cssComment = cssComment;
    }

    public Long getCssId() {
        return cssId;
    }

    public void setCssId(Long cssId) {
        this.cssId = cssId;
    }

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public boolean isCurrent() {
        return isCurrent;
    }

    public void setCurrent(boolean current) {
        isCurrent = current;
    }

    public String getCss() {
        return css;
    }

    public void setCss(String css) {
        this.css = css;
    }

    public Date getDate() {
        if (date==null) {
            return null;
        }
        return new Date(date.getTime());
    }

    public void setDate(Date date) {
        if (date==null) {
            this.date=null;
            return;
        }
        this.date = new Date(date.getTime());
    }

    public String toString() {
        return "[cssId:"+cssId+";siteId:"+siteId+"]";
    }
}
