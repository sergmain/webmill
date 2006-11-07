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

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;

import org.riverock.interfaces.portal.bean.SiteLanguage;

/**
 * @author SergeMaslyukov
 *         Date: 27.01.2006
 *         Time: 14:52:56
 *         $Id$
 */
@Entity
@Table(name="wm_portal_site_language")
@TableGenerator(
    name="TABLE_SITE_LANGUAGE",
    table="wm_portal_ids",
    pkColumnName = "sequence_name",
    valueColumnName = "sequence_next_value",
    pkColumnValue = "wm_portal_site_language",
    allocationSize=1,
    initialValue = 1
)
public class SiteLanguageBean implements Serializable,  SiteLanguage {
    private static final long serialVersionUID = 1055005503L;

    @Id
    @GeneratedValue(strategy= GenerationType.TABLE, generator = "TABLE_SITE_LANGUAGE")
    @Column(name="ID_SITE_SUPPORT_LANGUAGE")
    private Long siteLanguageId;

    @Column(name="ID_SITE")
    private Long siteId;

    @Column(name="CUSTOM_LANGUAGE")
    private String customLanguage;

    @Column(name="NAME_CUSTOM_LANGUAGE")
    private String nameCustomLanguage;

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

    public String getCustomLanguage() {
        return customLanguage;
    }

    public void setCustomLanguage(String customLanguage) {
        this.customLanguage = customLanguage;
    }

    public String getNameCustomLanguage() {
        return nameCustomLanguage;
    }

    public void setNameCustomLanguage(String nameCustomLanguage) {
        this.nameCustomLanguage = nameCustomLanguage;
    }
}
