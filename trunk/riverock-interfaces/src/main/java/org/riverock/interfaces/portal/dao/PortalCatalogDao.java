/*
 * org.riverock.interfaces - Common classes and interafces shared between projects
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package org.riverock.interfaces.portal.dao;

import java.util.Locale;
import java.util.List;

import org.riverock.interfaces.portal.bean.CatalogItem;
import org.riverock.interfaces.portal.bean.CatalogLanguageItem;

/**
 * @author Sergei Maslyukov
 *         Date: 17.05.2006
 *         Time: 13:57:06
 */
public interface PortalCatalogDao {
    public Long getCatalogItemId(Long siteLanguageId, Long portletNameId, Long templateId );
    public Long getCatalogItemId(Long siteId, Locale locale, String portletName, String templateName );
    public Long getCatalogItemId(Long siteId, Locale locale, String pageName );
    public Long getCatalogItemId(Long siteId, Locale locale, Long catalogId );

    public List<CatalogItem> getCatalogItemList(Long catalogLanguageId);
    public CatalogItem getCatalogItem(Long catalogId);

    public CatalogLanguageItem getCatalogLanguageItem(Long catalogLanguageId );
    public List<CatalogLanguageItem> getCatalogLanguageItemList(Long siteLanguageId);
    public CatalogLanguageItem getCatalogLanguageItem(String catalogLanguageCode, Long siteLanguageId);

    public Long getSiteId(Long catalogLanguageId);

    public Long createCatalogItem(CatalogItem catalogItem);
    public void updateCatalogItem(CatalogItem catalogItem);
    public void deleteCatalogItem(Long catalogId);

    public Long createCatalogLanguageItem(CatalogLanguageItem catalogLanguageItem);
    public void updateCatalogLanguageItem(CatalogLanguageItem catalogLanguageItem);
    public void deleteCatalogLanguageItem(Long catalogLanguageId);


}
