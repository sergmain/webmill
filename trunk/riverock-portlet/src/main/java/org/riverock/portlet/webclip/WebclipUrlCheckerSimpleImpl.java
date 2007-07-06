package org.riverock.portlet.webclip;

import java.util.Set;
import java.util.HashSet;
import java.util.List;

import org.riverock.interfaces.portal.dao.PortalDaoProvider;
import org.riverock.interfaces.portal.bean.SiteLanguage;
import org.riverock.interfaces.portal.bean.CatalogLanguageItem;
import org.riverock.interfaces.portal.bean.CatalogItem;

/**
 * User: SMaslyukov
 * Date: 06.07.2007
 * Time: 14:14:22
 */
public class WebclipUrlCheckerSimpleImpl implements WebclipUrlChecker {
    private PortalDaoProvider portalDaoProvider;

    public WebclipUrlCheckerSimpleImpl(PortalDaoProvider portalDaoProvider) {
        this.portalDaoProvider = portalDaoProvider;
    }

    public boolean isExist(Long siteLanguageId, String url) {
        return portalDaoProvider.getPortalCatalogDao().getCatalogItemId(siteLanguageId, url)!=null;
    }
}
