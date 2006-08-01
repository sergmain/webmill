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
package org.riverock.webmill.main;

import java.util.Date;
import java.io.Serializable;

import org.riverock.interfaces.portal.bean.Css;

/**
 * @author SergeMaslyukov
 *         Date: 27.01.2006
 *         Time: 3:11:20
 *         $Id$
 */
public class CssBean implements Serializable, Css {
    private static final long serialVersionUID = 3037005502L;

    private Long cssId = null;
    private Long siteId = null;
    private String css = "";
    private String cssComment = "";
    private Date date = null;
    private boolean isCurrent = false;

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
}
