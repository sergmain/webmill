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

import org.riverock.interfaces.portal.bean.CatalogLanguageItem;

/**
 * @author SergeMaslyukov
 *         Date: 27.01.2006
 *         Time: 15:39:41
 *         $Id$
 */
@Entity
@Table(name="wm_portal_catalog_language")
@TableGenerator(
    name="TABLE_PORTAL_CATALOG_LANGUAGE",
    table="wm_portal_ids",
    pkColumnName = "sequence_name",
    valueColumnName = "sequence_next_value",
    pkColumnValue = "wm_portal_catalog_language",
    allocationSize = 1,
    initialValue = 1
)
public class CatalogLanguageBean implements Serializable, CatalogLanguageItem {
    private static final long serialVersionUID = 1057005507L;

    @Id
    @GeneratedValue(strategy= GenerationType.TABLE, generator = "TABLE_PORTAL_CATALOG_LANGUAGE")
    @Column(name="ID_SITE_CTX_LANG_CATALOG")
    private Long catalogLanguageId;

    @Column(name="IS_DEFAULT")
    private Boolean isDefault = false;
     
    @Column(name="ID_SITE_SUPPORT_LANGUAGE")
    private Long siteLanguageId;

    @Column(name="CATALOG_CODE")
    private String catalogCode = null;

    public CatalogLanguageBean() {
    }

    public CatalogLanguageBean(CatalogLanguageItem catalogLanguageItem) {
        this.catalogLanguageId = catalogLanguageItem.getCatalogLanguageId();
        isDefault = catalogLanguageItem.getDefault();
        this.siteLanguageId = catalogLanguageItem.getSiteLanguageId();
        this.catalogCode = catalogLanguageItem.getCatalogCode();
    }

    public Long getCatalogLanguageId() {
        return catalogLanguageId;
    }

    public void setCatalogLanguageId(Long catalogLanguageId) {
        this.catalogLanguageId = catalogLanguageId;
    }

    public Boolean getDefault() {
        return isDefault;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }

    public Long getSiteLanguageId() {
        return siteLanguageId;
    }

    public void setSiteLanguageId(Long siteLanguageId) {
        this.siteLanguageId = siteLanguageId;
    }

    public String getCatalogCode() {
        return catalogCode;
    }

    public void setCatalogCode(String catalogCode) {
        this.catalogCode = catalogCode;
    }
}
