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
@Table(name="WM_PORTAL_SITE_LANGUAGE")
//@Table(name="wm_portal_site_language")
@TableGenerator(
    name="TABLE_SITE_LANGUAGE",
    table="WM_PORTAL_IDS",
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

    public SiteLanguageBean() {
    }

    public SiteLanguageBean(SiteLanguage siteLanguage) {
        this.siteLanguageId = siteLanguage.getSiteLanguageId();
        this.siteId = siteLanguage.getSiteId();
        this.customLanguage = siteLanguage.getCustomLanguage();
        this.nameCustomLanguage = siteLanguage.getNameCustomLanguage();
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
