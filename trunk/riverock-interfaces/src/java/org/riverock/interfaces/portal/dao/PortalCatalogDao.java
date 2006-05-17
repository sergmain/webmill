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

    public Long createCatalogItem(CatalogItem catalogItem);
    public Long createCatalogLanguageItem(CatalogLanguageItem catalogLanguageItem);

    public CatalogLanguageItem getCatalogLanguageItem(String catalogLanguageCode, Long siteLanguageId);
}
