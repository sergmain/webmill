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
import java.sql.Blob;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import org.riverock.interfaces.portal.bean.Template;

/**
 * @author SergeMaslyukov
 *         Date: 27.01.2006
 *         Time: 16:32:46
 *         $Id: TemplateBean.java 1338 2007-08-24 16:51:18Z serg_main $
 */
@Entity
@Table(name="WM_PORTAL_TEMPLATE")
@TableGenerator(
    name="TABLE_PORTAL_TEMPLATE",
    table="WM_PORTAL_IDS",
    pkColumnName = "sequence_name",
    valueColumnName = "sequence_next_value",
    pkColumnValue = "wm_portal_template",
    allocationSize = 1,
    initialValue = 1
)
@Cache(usage = CacheConcurrencyStrategy.NONE)
public class TemplateBean implements Serializable, Template {
    private static final long serialVersionUID = 1059005501L;

    @Id
    @GeneratedValue(strategy= GenerationType.TABLE, generator = "TABLE_PORTAL_TEMPLATE")
    @Column(name="ID_SITE_TEMPLATE")
    private Long templateId;

    @Column(name="ID_SITE_SUPPORT_LANGUAGE")
    private Long siteLanguageId;

    @Column(name="NAME_SITE_TEMPLATE")
    private String templateName;

    @Column(name="TEMPLATE_BLOB")
    private Blob templateBlob;

    @Column(name="is_default_dynamic")
    private boolean isDefaultDynamic = false;

    @Column(name="IS_MAXIMIZED_TEMPLATE")
    private boolean isMaximizedTemplate = false;

    @Column(name="IS_POPUP_TEMPLATE")
    private boolean isPopupTemplate = false;

    @Column(name="TEMPLATE_ROLE")
    private String templateRole;

    @Version
    @Column(name="VERSION")
    private int version;

    @Transient
    private String templateLanguage = null;

    @Transient
    private String templateData;

    public TemplateBean(Template t) {
        this.templateId = t.getTemplateId();
        this.siteLanguageId = t.getSiteLanguageId();
        this.templateName = t.getTemplateName();
        this.templateData = t.getTemplateData();
        this.isDefaultDynamic = t.isDefaultDynamic();
        this.version = t.getVersion();
        this.templateLanguage = t.getTemplateLanguage();
        this.templateRole = t.getRoles();
        this.isPopupTemplate = t.isPopupTemplate();
        this.isMaximizedTemplate = t.isMaximizedTemplate();
    }

    public TemplateBean() {
    }

    public String getRoles() {
        return templateRole;
    }

    public String getTemplateRole() {
        return templateRole;
    }

    public void setTemplateRole(String templateRole) {
        this.templateRole = templateRole;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Blob getTemplateBlob() {
        return templateBlob;
    }

    public void setTemplateBlob(Blob templateBlob) {
        this.templateBlob = templateBlob;
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

    public boolean isMaximizedTemplate() {
        return isMaximizedTemplate;
    }

    public boolean isPopupTemplate() {
        return isPopupTemplate;
    }

    public void setMaximizedTemplate(boolean isMaximizedTemplate) {
        this.isMaximizedTemplate = isMaximizedTemplate;
    }

    public void setPopupTemplate(boolean isPopupTemplate) {
        this.isPopupTemplate = isPopupTemplate;
    }

    public void setDefaultDynamic(boolean isDefaultDynamic) {
        this.isDefaultDynamic = isDefaultDynamic;
    }
}
