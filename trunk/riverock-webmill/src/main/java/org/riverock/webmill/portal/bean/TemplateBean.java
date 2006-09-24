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

import org.riverock.interfaces.portal.bean.Template;

/**
 * @author SergeMaslyukov
 *         Date: 27.01.2006
 *         Time: 16:32:46
 *         $Id$
 */
public class TemplateBean implements Serializable, Template {
    private static final long serialVersionUID = 1059005501L;

    private Long templateId;
    private Long siteLanguageId;
    private String templateName; 
    private String templateData;
    private String templateLanguage = null;
    private boolean isDefaultDynamic = false;

    public String getTemplateLanguage() {
        return templateLanguage;
    }

    public void setTemplateLanguage(String templateLanguage) {
        this.templateLanguage = templateLanguage;
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
        this.templateName = templateName;
    }

    public String getTemplateData() {
        return templateData;
    }

    public void setTemplateData(String templateData) {
        this.templateData = templateData;
    }

    public boolean isDefaultDynamic() {
        return isDefaultDynamic;
    }

    public void setDefaultDynamic(boolean defaultDynamic) {
        isDefaultDynamic = defaultDynamic;
    }
}
