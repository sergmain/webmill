/*
 * org.riverock.webmill.init - Webmill portal initializer web application
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
package org.riverock.webmill.init.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author Sergei Maslyukov
 *         Date: 13.07.2006
 *         Time: 18:44:56
 */
public class CatalogLanguageBean implements Serializable {
    private static final long serialVersionUID = 1057005507L;

    private Long catalogLanguageId;
    private Boolean isDefault = false;
    private Long siteLanguageId;
    private String catalogCode = null;
    private List<CatalogItemBean> catalogItems = null;

    public List<CatalogItemBean> getCatalogItems() {
        return catalogItems;
    }

    public void setCatalogItems(List<CatalogItemBean> catalogItems) {
        this.catalogItems = catalogItems;
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
