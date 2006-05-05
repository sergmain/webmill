package org.riverock.webmill.portal.dao;

import java.util.Locale;
import java.util.List;

import org.riverock.webmill.portal.bean.CatalogBean;
import org.riverock.webmill.portal.bean.CatalogLanguageBean;
import org.riverock.interfaces.portal.bean.CatalogItem;

/**
 * @author Sergei Maslyukov
 *         Date: 05.05.2006
 *         Time: 15:47:30
 */
public interface InternalCatalogDao {
    public Long getCatalogItemId(Long siteId, Locale locale, String portletName, String templateName );
    public Long getCatalogItemId(Long siteId, Locale locale, String pageName );
    public Long getCatalogItemId(Long siteId, Locale locale, Long catalogId );

    public List<CatalogItem> getCatalogItemList(Long catalogLanguageId);
    public CatalogItem getCatalogItem(Long catalogId);

    public CatalogLanguageBean getCatalogLanguageBean(Long catalogLanguageId );
    public List<CatalogLanguageBean> getCatalogLanguageList(Long siteLanguageId);
    public Long createCataloc
}
