/*
 * org.riverock.webmill.admin - Webmill portal admin web application
 *
 * For more information about Webmill portal, please visit project site
 * http://webmill.riverock.org
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community,
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
package org.riverock.webmill.admin.bean;

import java.io.Serializable;
import java.util.Locale;

import org.riverock.common.tools.StringTools;
import org.riverock.webmill.admin.utils.FacesTools;

/**
 * @author SergeMaslyukov
 *         Date: 13.07.2006
 *         Time: 21:55:44
 *         $Id: PortalUserSessionBean.java 753 2006-07-10 07:53:57Z serg_main $
 */
public class SiteBean implements Serializable {
    private static final long serialVersionUID = 2057005502L;

    private Long siteId=null;
    private Long companyId;
    private String siteName;
    private boolean cssDynamic;
    private String cssFile;
    private boolean registerAllowed;
    private String defLanguage;
    private String defCountry;
    private String defVariant;
    private String adminEmail;
    private String properties=null;

    public SiteBean(){
    }

    public SiteBean(SiteBean site){
        this.siteId = site.getSiteId();
        this.companyId = site.getCompanyId();
        this.siteName = site.getSiteName();
        this.cssDynamic = site.getCssDynamic();
        this.cssFile = site.getCssFile();
        this.registerAllowed = site.getRegisterAllowed();
        this.defLanguage = site.getDefLanguage();
        this.defCountry = site.getDefCountry();
        this.defVariant = site.getDefVariant();
        this.adminEmail = site.getAdminEmail();
        this.properties = site.getProperties();
    }

    public String getSiteDefaultLocale() {
        if (getDefLanguage()==null && getDefCountry()==null && getDefVariant()==null) {
            return null;
        }

        if (getDefCountry()==null && getDefVariant()==null) {
            return new Locale(getDefLanguage()).toString();
        }
        else if (getDefVariant()==null) {
            return new Locale(getDefLanguage(), getDefCountry()).toString();
        }
        else {
            return new Locale(getDefLanguage(), getDefCountry(), getDefVariant()).toString();
        }
    }

    public void setSiteDefaultLocale(String localeString) {
        Locale locale = StringTools.getLocale(FacesTools.convertParameter(localeString));
        defLanguage = locale.getLanguage();
        defCountry = locale.getCountry();
        defVariant = locale.getVariant();
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

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = FacesTools.convertParameter(siteName);
    }

    public boolean getCssDynamic() {
        return cssDynamic;
    }

    public void setCssDynamic(boolean cssDynamic) {
        this.cssDynamic = cssDynamic;
    }

    public String getCssFile() {
        return cssFile;
    }

    public void setCssFile(String cssFile) {
        this.cssFile = FacesTools.convertParameter(cssFile);
    }

    public boolean getRegisterAllowed() {
        return registerAllowed;
    }

    public void setRegisterAllowed(boolean registerAllowed) {
        this.registerAllowed = registerAllowed;
    }

    public String getDefLanguage() {
        return defLanguage;
    }

    public void setDefLanguage(String defLanguage) {
        this.defLanguage = FacesTools.convertParameter(defLanguage);
    }

    public String getDefCountry() {
        return defCountry;
    }

    public void setDefCountry(String defCountry) {
        this.defCountry = FacesTools.convertParameter(defCountry);
    }

    public String getDefVariant() {
        return defVariant;
    }

    public void setDefVariant(String defVariant) {
        this.defVariant = FacesTools.convertParameter(defVariant);
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = FacesTools.convertParameter(adminEmail);
    }
}
