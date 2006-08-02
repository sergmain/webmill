/*
 * org.riverock.webmill -- Portal framework implementation
 *
 * Copyright (C) 2004, Riverock Software, All Rights Reserved.
 *
 * Riverock -- The Open-source Java Development Community
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
 *
 */
package org.riverock.webmill.portal.bean;

import java.io.Serializable;

import org.apache.commons.lang.CharEncoding;

import org.riverock.interfaces.portal.bean.Site;

/**
 * @author SergeMaslyukov
 *         Date: 27.01.2006
 *         Time: 14:29:04
 *         $Id$
 */
public class SiteBean implements Serializable, Site {
    private static final long serialVersionUID = 3255005503L;

    private Long siteId;
    private Long companyId;
    private boolean isCssDynamic = false;
    private boolean isRegisterAllowed = false;
    private String defLanguage;
    private String defCountry;
    private String defVariant;
    private String siteName;
    private String adminEmail=null;
    private String cssFile = "/front_styles.css";
    private String properties=null;
    private String portalCharset = CharEncoding.UTF_8;

    public String getPortalCharset() {
        if (portalCharset==null)
            portalCharset=CharEncoding.UTF_8;
        
        return portalCharset;
    }

    public void setPortalCharset(String portalCharset) {
        this.portalCharset = portalCharset;
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public boolean getCssDynamic() {
        return isCssDynamic;
    }

    public void setCssDynamic(boolean cssDynamic) {
        isCssDynamic = cssDynamic;
    }

    public boolean getRegisterAllowed() {
        return isRegisterAllowed;
    }

    public void setRegisterAllowed(boolean registerAllowed) {
        isRegisterAllowed = registerAllowed;
    }

    public String getDefLanguage() {
        return defLanguage;
    }

    public void setDefLanguage(String defLanguage) {
        this.defLanguage = defLanguage;
    }

    public String getDefCountry() {
        return defCountry;
    }

    public void setDefCountry(String defCountry) {
        this.defCountry = defCountry;
    }

    public String getDefVariant() {
        return defVariant;
    }

    public void setDefVariant(String defVariant) {
        this.defVariant = defVariant;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }

    public String getCssFile() {
        return cssFile;
    }

    public void setCssFile(String cssFile) {
        this.cssFile = cssFile;
    }
}
