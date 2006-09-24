/*
 * org.riverock.portlet - Portlet Library
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
package org.riverock.portlet.manager.site;

import java.io.Serializable;

import org.riverock.portlet.manager.site.bean.CssBean;
import org.riverock.portlet.manager.site.bean.SiteExtended;
import org.riverock.portlet.manager.site.bean.SiteLanguageBean;
import org.riverock.portlet.manager.site.bean.TemplateBean;
import org.riverock.portlet.manager.site.bean.XsltBean;

/**
 * @author Sergei Maslyukov
 *         16.05.2006
 *         20:24:52
 */
public class SiteSessionBean implements Serializable {
    private static final long serialVersionUID = 2058005508L;

    public static final int UNKNOWN_TYPE = 0;
    public static final int SITE_TYPE = 1;
    public static final int SITE_LANGUAGE_TYPE = 2;
    public static final int TEMPLATE_TYPE = 3;
    public static final int XSLT_TYPE = 4;
    public static final int CSS_TYPE = 5;

    private boolean isEdit;
    private Long id = null;
    private int objectType=0;

    private SiteExtended siteExtended = null;
    private SiteLanguageBean siteLanguage = null;
    private TemplateBean template = null;
    private CssBean css = null;
    private XsltBean xslt = null;

    private String currentVirtualHost = null;
    private String newVirtualHost = null;
    private Long currentSiteId=null;

    public Long getCurrentSiteId() {
        return currentSiteId;
    }

    public void setCurrentSiteId(Long currentSiteId) {
        this.currentSiteId = currentSiteId;
    }

    public String getCurrentVirtualHost() {
        return currentVirtualHost;
    }

    public void setCurrentVirtualHost(String currentVirtualHost) {
//        this.currentVirtualHost = FacesTools.convertParameter(currentVirtualHost);
        this.currentVirtualHost = currentVirtualHost;
    }

    public String getNewVirtualHost() {
        return newVirtualHost;
    }

    public void setNewVirtualHost(String newVirtualHost) {
        this.newVirtualHost = newVirtualHost;
    }

    public SiteExtended getSiteExtended() {
        return siteExtended;
    }

    public void setSiteExtended(SiteExtended siteExtended) {
        this.siteExtended = siteExtended;
    }

    public SiteLanguageBean getSiteLanguage() {
        return siteLanguage;
    }

    public void setSiteLanguage(SiteLanguageBean siteLanguage) {
        this.siteLanguage = siteLanguage;
    }

    public TemplateBean getTemplate() {
        return template;
    }

    public void setTemplate(TemplateBean template) {
        this.template = template;
    }

    public CssBean getCss() {
        return css;
    }

    public void setCss(CssBean css) {
        this.css = css;
    }

    public XsltBean getXslt() {
        return xslt;
    }

    public void setXslt(XsltBean xslt) {
        this.xslt = xslt;
    }

    public int getSiteType() {
        return SITE_TYPE;
    }

    public int getSiteLanguageType() {
        return SITE_LANGUAGE_TYPE;
    }

    public int getTemplateType() {
        return TEMPLATE_TYPE;
    }

    public int getXsltType() {
        return XSLT_TYPE;
    }

    public int getCssType() {
        return CSS_TYPE;
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean isEdit) {
        this.isEdit = isEdit;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getObjectType() {
        return objectType;
    }

    public void setObjectType(int objectType) {
        this.objectType = objectType;
    }
}
