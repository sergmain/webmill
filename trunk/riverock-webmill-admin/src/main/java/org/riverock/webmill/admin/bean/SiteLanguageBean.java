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

