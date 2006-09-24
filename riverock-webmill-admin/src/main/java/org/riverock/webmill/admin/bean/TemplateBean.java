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

import org.riverock.webmill.admin.utils.FacesTools;

/**
 * @author Sergei Maslyukov
 *         Date: 13.07.2006
 *         Time: 18:04:07
 */
public class TemplateBean implements Serializable {
    private static final long serialVersionUID = 2059005504L;

    private Long templateId;
    private Long siteLanguageId;
    private String templateName;
    private String templateData;
    private String templateLanguage = null;
    private boolean isDefaultDynamic = false;

    public TemplateBean(){
    }

    public TemplateBean(Long siteLanguageId, String templateName, String templateData, boolean defaultDynamic) {
        this.siteLanguageId = siteLanguageId;
        this.templateName = templateName;
        this.templateData = templateData;
        isDefaultDynamic = defaultDynamic;
    }

    public TemplateBean(TemplateBean template){
        this.templateId=template.getTemplateId();
        this.siteLanguageId=template.getSiteLanguageId();
        this.templateName=template.getTemplateName();
        this.templateData=template.getTemplateData();
        this.templateLanguage=template.getTemplateLanguage();
        this.isDefaultDynamic=template.isDefaultDynamic();
    }

    public String getTemplateLanguage() {
        return templateLanguage;
    }

    public void setTemplateLanguage(String templateLanguage) {
        this.templateLanguage = FacesTools.convertParameter(templateLanguage);
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public Long getSiteLanguageId() {
        return siteLanguageId;
    }

    public void setSiteLanguageId(Long siteLanguageId) {
        this.siteLanguageId = siteLanguageId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = FacesTools.convertParameter(templateName);
    }

    public String getTemplateData() {
        return templateData;
    }

    public void setTemplateData(String templateData) {
        this.templateData = FacesTools.convertParameter(templateData);
    }

    public boolean isDefaultDynamic() {
        return isDefaultDynamic;
    }

    public void setDefaultDynamic(boolean defaultDynamic) {
        isDefaultDynamic = defaultDynamic;
    }
}

