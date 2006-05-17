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
