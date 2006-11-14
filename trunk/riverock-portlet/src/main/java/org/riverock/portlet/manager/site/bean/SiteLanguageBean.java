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
package org.riverock.portlet.manager.site.bean;

import java.io.Serializable;

import org.riverock.interfaces.portal.bean.SiteLanguage;

/**
 * @author Sergei Maslyukov
 *         Date: 17.05.2006
 *         Time: 17:38:58
 */
public class SiteLanguageBean implements Serializable, SiteLanguage {
    private static final long serialVersionUID = 2057005504L;

    private Long siteLanguageId;
    private Long siteId;
    private String customLanguage;
    private String nameCustomLanguage;

    public SiteLanguageBean() {
    }

    public SiteLanguageBean(SiteLanguage siteLanguage) {
        this.siteLanguageId = siteLanguage.getSiteLanguageId();
        this.siteId = siteLanguage.getSiteId();
        this.customLanguage = siteLanguage.getCustomLanguage();
        this.nameCustomLanguage = siteLanguage.getNameCustomLanguage();
    }

    public Long getSiteLanguageId() {
        return siteLanguageId;
    }

    public void setSiteLanguageId(Long siteLanguageId) {
        this.siteLanguageId = siteLanguageId;
    }

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public String getCustomLanguage() {
        return customLanguage;
    }

    public void setCustomLanguage(String customLanguage) {
        this.customLanguage = customLanguage;
    }

    public String getNameCustomLanguage() {
        return nameCustomLanguage;
    }

    public void setNameCustomLanguage(String nameCustomLanguage) {
        this.nameCustomLanguage = nameCustomLanguage;
    }
}
