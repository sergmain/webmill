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
@Table(name="wm_portal_list_site")
@TableGenerator(
    name="TABLE_SITE",
    table="wm_portal_ids",
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
    }

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

    public String toString() {
        return "[site #"+siteId+",name:"+siteName+", locale:"+new Locale(defLanguage, defCountry, defVariant)+"]";
    }
}
