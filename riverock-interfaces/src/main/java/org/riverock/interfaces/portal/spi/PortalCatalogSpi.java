package org.riverock.interfaces.portal.spi;

import java.util.Locale;
import java.util.List;

import org.riverock.interfaces.portal.bean.CatalogItem;
import org.riverock.interfaces.portal.bean.CatalogLanguageItem;
import org.riverock.interfaces.portal.dao.PortalCatalogDao;

/**
 * User: SergeMaslyukov
 * Date: 02.09.2007
 * Time: 12:41:50
 * $Id$
 */
public interface PortalCatalogSpi extends PortalCatalogDao {
    public Long getCatalogItemId(Long siteLanguageId, Long portletNameId, Long templateId );
    public Long getCatalogItemId(Long siteLanguageId, String pageUrl );

    public Long getCatalogItemId(Long siteId, Locale locale, String portletName, String templateName );
    public Long getCatalogItemId(Long siteId, Locale locale, String pageUrl );
    public Long getCatalogItemId(Long siteId, Locale locale, Long catalogId );

    boolean isUrlExist(String url, Long siteLanguageId);

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

    public void changeTemplateForCatalogLanguage(Long catalogLanguageId, Long templateId);
}
