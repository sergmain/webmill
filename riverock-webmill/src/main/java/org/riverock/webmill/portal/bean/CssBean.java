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
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import org.riverock.interfaces.portal.bean.Css;

/**
 * @author SergeMaslyukov
 *         Date: 27.01.2006
 *         Time: 3:11:20
 *         $Id: CssBean.java 1351 2007-08-28 10:09:30Z serg_main $
 */
@Entity
@Table(name="WM_PORTAL_CSS")
@TableGenerator(
    name="TABLE_CSS",
    table="WM_PORTAL_IDS",
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
        return this.date;
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
