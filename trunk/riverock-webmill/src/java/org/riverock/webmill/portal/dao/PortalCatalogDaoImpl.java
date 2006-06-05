/*
 * org.riverock.webmill -- Portal framework implementation
 *
 * Copyright (C) 2004, Riverock Software, All Rights Reserved.
 *
 * Riverock -- The Open-source Java Development Community
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
 *
 */
package org.riverock.webmill.portal.dao;

import java.util.Locale;
import java.util.List;

import org.riverock.interfaces.portal.dao.PortalCatalogDao;
import org.riverock.interfaces.portal.bean.CatalogItem;
import org.riverock.interfaces.portal.bean.CatalogLanguageItem;
import org.riverock.interfaces.sso.a3.AuthSession;

/**
 * @author Sergei Maslyukov
 *         Date: 17.05.2006
 *         Time: 13:59:34
 */
public class PortalCatalogDaoImpl implements PortalCatalogDao {
    private AuthSession authSession = null;

    PortalCatalogDaoImpl(AuthSession authSession) {
        this.authSession = authSession;
    }

    public Long getCatalogItemId(Long siteLanguageId, Long portletNameId, Long templateId) {
        return InternalDaoFactory.getInternalCatalogDao().getCatalogItemId(siteLanguageId, portletNameId, templateId);
    }

    public Long getCatalogItemId(Long siteId, Locale locale, String portletName, String templateName) {
        return InternalDaoFactory.getInternalCatalogDao().getCatalogItemId(siteId, locale, portletName, templateName);
    }

    public Long getCatalogItemId(Long siteId, Locale locale, String pageName) {
        return InternalDaoFactory.getInternalCatalogDao().getCatalogItemId(siteId, locale, pageName);
    }

    public Long getCatalogItemId(Long siteId, Locale locale, Long catalogId) {
        return InternalDaoFactory.getInternalCatalogDao().getCatalogItemId(siteId, locale, catalogId);
    }

    public List<CatalogItem> getCatalogItemList(Long catalogLanguageId) {
        return InternalDaoFactory.getInternalCatalogDao().getCatalogItemList(catalogLanguageId);
    }

    public CatalogItem getCatalogItem(Long catalogId) {
        return InternalDaoFactory.getInternalCatalogDao().getCatalogItem(catalogId);
    }

    public CatalogLanguageItem getCatalogLanguageItem(Long catalogLanguageId) {
        return InternalDaoFactory.getInternalCatalogDao().getCatalogLanguageItem(catalogLanguageId);
    }

    public List<CatalogLanguageItem> getCatalogLanguageItemList(Long siteLanguageId) {
        return InternalDaoFactory.getInternalCatalogDao().getCatalogLanguageItemList(siteLanguageId);
    }

    public Long createCatalogItem(CatalogItem catalogItem) {
        return InternalDaoFactory.getInternalCatalogDao().createCatalogItem(catalogItem);
    }

    public Long createCatalogLanguageItem(CatalogLanguageItem catalogLanguageItem) {
        return InternalDaoFactory.getInternalCatalogDao().createCatalogLanguageItem(catalogLanguageItem);
    }

    public CatalogLanguageItem getCatalogLanguageItem(String catalogLanguageCode, Long siteLanguageId) {
        return InternalDaoFactory.getInternalCatalogDao().getCatalogLanguageItem(catalogLanguageCode, siteLanguageId);
    }
}
