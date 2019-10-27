package org.riverock.portlet.webclip;

import java.util.Set;
import java.util.List;
import java.util.HashSet;

import org.riverock.interfaces.portal.dao.PortalDaoProvider;
import org.riverock.interfaces.portal.bean.SiteLanguage;
import org.riverock.interfaces.portal.bean.CatalogLanguageItem;
import org.riverock.interfaces.portal.bean.CatalogItem;

/**
 * User: SMaslyukov
 * Date: 06.07.2007
 * Time: 14:14:22
 */
public class WebclipUrlCheckerImpl implements WebclipUrlChecker {
    private Long siteId;

    private Set<String> urls = new HashSet<String>();

    private PortalDaoProvider portalDaoProvider;
    private static final char SEPARATOR = '-';

    public WebclipUrlCheckerImpl(PortalDaoProvider portalDaoProvider, Long siteId) {
        this.portalDaoProvider = portalDaoProvider;
        this.siteId = siteId;

        initChecker();
    }

    public boolean isExist(Long siteLanguageId, String url) {
        return urls.contains(""+siteLanguageId+ SEPARATOR +url);
    }

    private void initChecker() {
        List<SiteLanguage> languages = portalDaoProvider.getPortalSiteLanguageDao().getSiteLanguageList(siteId);
        for (SiteLanguage language : languages) {
            List<CatalogLanguageItem> catalogLanguageItems = portalDaoProvider.getPortalCatalogDao().getCatalogLanguageItemList(language.getSiteLanguageId());
            for (CatalogLanguageItem catalogLanguageItem : catalogLanguageItems) {
                List<CatalogItem> catalogItems = portalDaoProvider.getPortalCatalogDao().getCatalogItemList(catalogLanguageItem.getCatalogLanguageId());
                for (CatalogItem catalogItem : catalogItems) {
                    urls.add(""+language.getSiteLanguageId()+ SEPARATOR +catalogItem.getUrl());
                }
            }
        }
    }
}
