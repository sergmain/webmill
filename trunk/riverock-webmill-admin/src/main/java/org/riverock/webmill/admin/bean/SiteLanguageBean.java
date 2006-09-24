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
import java.util.List;

import org.riverock.webmill.admin.utils.FacesTools;

/**
 * @author Sergei Maslyukov
 *         Date: 13.07.2006
 *         Time: 18:05:28
 */
public class SiteLanguageBean implements Serializable {
    private static final long serialVersionUID = 2057005504L;

    private Long siteLanguageId;
    private Long siteId;
    private String locale;
    private String nameCustomLanguage;
    private XsltBean xslt=null;
    private List<TemplateBean> templates =null;
    private List<CatalogLanguageBean> catalogLanguages=null;

    public SiteLanguageBean() {
    }

    public SiteLanguageBean(String locale, String nameCustomLanguage) {
        this.locale = locale;
        this.nameCustomLanguage = nameCustomLanguage;
    }

    public SiteLanguageBean(SiteLanguageBean siteLanguage) {
        this.siteLanguageId = siteLanguage.getSiteLanguageId();
        this.siteId = siteLanguage.getSiteId();
        this.locale = siteLanguage.getLocale();
        this.nameCustomLanguage = siteLanguage.getNameCustomLanguage();
    }

    public List<CatalogLanguageBean> getCatalogLanguages() {
        return catalogLanguages;
    }

    public void setCatalogLanguages(List<CatalogLanguageBean> catalogLanguages) {
        this.catalogLanguages = catalogLanguages;
    }

    public List<TemplateBean> getTemplates() {
        return templates;
    }

    public void setTemplates(List<TemplateBean> templates) {
        this.templates = templates;
    }

    public XsltBean getXslt() {
        return xslt;
    }

    public void setXslt(XsltBean xslt) {
        this.xslt = xslt;
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

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = FacesTools.convertParameter(locale);
    }

    public String getNameCustomLanguage() {
        return nameCustomLanguage;
    }

    public void setNameCustomLanguage(String nameCustomLanguage) {
        this.nameCustomLanguage = FacesTools.convertParameter(nameCustomLanguage);
    }
}

