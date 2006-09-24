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
package org.riverock.webmill.portal.dao;

import java.util.List;
import java.util.Locale;

import org.riverock.interfaces.portal.bean.CatalogItem;
import org.riverock.interfaces.portal.bean.CatalogLanguageItem;
import org.riverock.generic.db.DatabaseAdapter;

/**
 * @author Sergei Maslyukov
 *         Date: 05.05.2006
 *         Time: 15:47:30
 */
public interface InternalCatalogDao {
    public Long getCatalogItemId(Long siteLanguageId, Long portletNameId, Long templateId );
    public Long getCatalogItemId(Long siteId, Locale locale, String portletName, String templateName );
    public Long getCatalogItemId(Long siteId, Locale locale, String pageName );
    public Long getCatalogItemId(Long siteId, Locale locale, Long catalogId );

    public List<CatalogItem> getCatalogItemList(Long catalogLanguageId);
    public CatalogItem getCatalogItem(Long catalogId);

    public CatalogLanguageItem getCatalogLanguageItem(Long catalogLanguageId );
    public List<CatalogLanguageItem> getCatalogLanguageItemList(Long siteLanguageId);

    public CatalogLanguageItem getCatalogLanguageItem(String catalogLanguageCode, Long siteLanguageId);

    public Long createCatalogItem(CatalogItem catalogItem);
    public void updateCatalogItem(CatalogItem catalogItem);
    public void deleteCatalogItem(Long catalogId);

    public Long createCatalogLanguageItem(CatalogLanguageItem catalogLanguageItem);
    public void updateCatalogLanguageItem(CatalogLanguageItem catalogLanguageItem);
    public void deleteCatalogLanguageItem(Long catalogLanguageId);

    void deleteCatalogLanguageForSiteLanguage(DatabaseAdapter adapter, Long siteLanguageId);
}
