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
import java.util.Locale;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.Transient;

import org.apache.commons.lang.CharEncoding;

import org.riverock.interfaces.portal.bean.Site;

/**
 * @author SergeMaslyukov
 *         Date: 27.01.2006
 *         Time: 14:29:04
 *         $Id$
 */
@Entity
@Table(name="WM_PORTAL_LIST_SITE")
@TableGenerator(
    name="TABLE_SITE",
    table="WM_PORTAL_IDS",
    pkColumnName = "sequence_name",
    valueColumnName = "sequence_next_value",
    pkColumnValue = "wm_portal_site",
    allocationSize = 1,
    initialValue = 1
)
public class SiteBean implements Serializable, Site {
    private static final long serialVersionUID = 3255005503L;

    @Id
    @GeneratedValue(strategy= GenerationType.TABLE, generator = "TABLE_SITE")
    @Column(name="ID_SITE")
    private Long siteId;

    @Column(name="ID_FIRM")
    private Long companyId;

    @Column(name="IS_CSS_DYNAMIC")
    private boolean isCssDynamic = false;

    @Column(name="IS_REGISTER_ALLOWED")
    private boolean isRegisterAllowed = false;

    @Column(name="DEF_LANGUAGE")
    private String defLanguage;

    @Column(name="DEF_COUNTRY")
    private String defCountry;

    @Column(name="DEF_VARIANT")
    private String defVariant;

    @Column(name="NAME_SITE")
    private String siteName;

    @Column(name="ADMIN_EMAIL")
    private String adminEmail=null;

    @Column(name="CSS_FILE")
    private String cssFile = "/front_styles.css";

    @Column(name="PROPERTIES")
    private String properties=null;

    @Column(name="IS_ENABLE_NAVIGATION")
    private boolean isEnableNavigation = false;

    @Column(name="SERVER_TIME_ZONE")
    private String serverTimeZone;

    @Transient
    private String portalCharset = CharEncoding.UTF_8;

    public SiteBean() {
    }

    public SiteBean(Site site) {
        this.siteId = site.getSiteId();
        this.companyId = site.getCompanyId();
        isCssDynamic = site.getCssDynamic();
        isRegisterAllowed = site.getRegisterAllowed();
        this.defLanguage = site.getDefLanguage();
        this.defCountry = site.getDefCountry();
        this.defVariant = site.getDefVariant();
        this.siteName = site.getSiteName();
        this.adminEmail = site.getAdminEmail();
        this.cssFile = site.getCssFile();
        this.properties = site.getProperties();
        this.isEnableNavigation = site.isEnableNavigation();
        this.serverTimeZone = site.getServerTimeZone();
    }

    public String getPortalCharset() {
        if (portalCharset==null) {
            portalCharset=CharEncoding.UTF_8;
        }
        
        return portalCharset;
    }

    public void setEnableNavigation(boolean enableNavigation) {
        isEnableNavigation = enableNavigation;
    }

    public boolean isEnableNavigation() {
        return isEnableNavigation;
    }

    public String getServerTimeZone() {
        return serverTimeZone;
    }

    public void setServerTimeZone(String serverTimeZone) {
        this.serverTimeZone = serverTimeZone;
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

    public String toString() {
        Locale locale = new Locale(defLanguage, defCountry!=null?defCountry:"", defVariant!=null?defVariant:"");
        return "[site #"+siteId+",name:"+siteName+", locale:"+ locale +"]";
    }
}
