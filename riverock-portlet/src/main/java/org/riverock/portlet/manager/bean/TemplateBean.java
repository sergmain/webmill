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
package org.riverock.portlet.manager.bean;

import java.io.Serializable;

import org.riverock.interfaces.portal.bean.Template;

/**
 * @author Sergei Maslyukov
 *         Date: 18.05.2006
 *         Time: 11:47:23
 */
public class TemplateBean implements Serializable, Template {
    private static final long serialVersionUID = 2059015504L;

    private Long templateId;
    private Long siteLanguageId;
    private String templateName;
    private String templateData;
    private String templateLanguage = null;
    private boolean isDefaultDynamic = false;
    private int version;
    private boolean isMaximizedTemplate = false;
    private boolean isPopupTemplate = false;
    private String roles;

    public TemplateBean(){
    }

    public TemplateBean(Template template){
        this.templateId=template.getTemplateId();
        this.siteLanguageId=template.getSiteLanguageId();
        this.templateName=template.getTemplateName();
        this.templateData=template.getTemplateData();
        this.templateLanguage=template.getTemplateLanguage();
        this.isDefaultDynamic=template.isDefaultDynamic();
        this.version=template.getVersion();
        this.isMaximizedTemplate=template.isMaximizedTemplate();
        this.isPopupTemplate=template.isPopupTemplate();
        this.roles=template.getRoles();
    }

    public boolean isMaximizedTemplate() {
        return isMaximizedTemplate;
    }

    public void setMaximizedTemplate(boolean maximizedTemplate) {
        isMaximizedTemplate = maximizedTemplate;
    }

    public boolean isPopupTemplate() {
        return isPopupTemplate;
    }

    public void setPopupTemplate(boolean popupTemplate) {
        isPopupTemplate = popupTemplate;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

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
