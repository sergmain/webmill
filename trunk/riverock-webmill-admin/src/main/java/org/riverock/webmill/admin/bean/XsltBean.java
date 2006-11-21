/*
 * org.riverock.webmill.admin - Webmill portal admin web application
 * For more information about Webmill portal, please visit project site
 * http://webmill.askmore.info
 *
 * Copyright (C) 2000-2006, Riverock Software, All Rights Reserved.
 * Riverock - The Open-source Java Development Community,
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
package org.riverock.webmill.admin.bean;

import java.io.Serializable;

import org.riverock.webmill.admin.utils.FacesTools;
import org.riverock.interfaces.portal.bean.Xslt;

/**
 * @author Sergei Maslyukov
 *         Date: 13.07.2006
 *         Time: 18:02:15
 */
public class XsltBean implements Xslt, Serializable {
    private static final long serialVersionUID = 2058005504L;

    private Long id = null;
    private Long siteLanguageId = null;
    private String name = null;
    private String xsltData = null;
    private boolean isCurrent = false;
    private int version;

    public XsltBean() {
    }

    public XsltBean(Xslt xslt) {
        this.id = xslt.getId();
        this.name = xslt.getName();
        this.siteLanguageId = xslt.getSiteLanguageId();
        this.xsltData = xslt.getXsltData();
        this.isCurrent = xslt.isCurrent();
        this.version = xslt.getVersion();
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
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
        this.name = FacesTools.convertParameter(name);
    }

    public String getXsltData() {
        return xsltData;
    }

    public void setXsltData(String xsltData) {
        this.xsltData = FacesTools.convertParameter(xsltData);
    }
}
