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
package org.riverock.portlet.manager.menu.bean;

import java.io.Serializable;

import org.riverock.interfaces.portal.bean.CatalogLanguageItem;

/**
 * @author Sergei Maslyukov
 *         Date: 16.06.2006
 *         Time: 20:45:54
 */
public class MenuCatalogBean implements Serializable, CatalogLanguageItem {
    private static final long serialVersionUID = 1056105507L;

    private Long catalogLanguageId;
    private Boolean isDefault = false;
    private Long siteLanguageId;
    private String catalogCode = null;

    public MenuCatalogBean(){}

    public MenuCatalogBean(CatalogLanguageItem item){
        this.catalogLanguageId=item.getCatalogLanguageId();
        this.isDefault=item.getDefault();
        this.siteLanguageId=item.getSiteLanguageId();
        this.catalogCode=item.getCatalogCode();
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
