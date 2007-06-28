package org.riverock.portlet.webclip.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;

import org.riverock.interfaces.portal.bean.CatalogLanguageItem;
import org.riverock.interfaces.portal.bean.SiteLanguage;
import org.riverock.interfaces.portal.dao.PortalDaoProvider;
import org.riverock.portlet.tools.FacesTools;
import org.riverock.interfaces.ContainerConstants;

/**
 * User: SMaslyukov
 * Date: 18.05.2007
 * Time: 16:29:34
 */
public class WebclipService implements Serializable {
    private final static Logger log = Logger.getLogger(WebclipService.class);
    private static final long serialVersionUID = 2057005500L;

    public WebclipService() {
    }

    public List<SelectItem> getCatalogList() {

        List<SelectItem> list = new ArrayList<SelectItem>();

        Long siteId = new Long( FacesTools.getPortletRequest().getPortalContext().getProperty( ContainerConstants.PORTAL_PROP_SITE_ID ) );
        log.debug("siteId: " + siteId);
        PortalDaoProvider portalDaoProvider = FacesTools.getPortalDaoProvider();

        List<SiteLanguage> languages = portalDaoProvider.getPortalSiteLanguageDao().getSiteLanguageList(siteId);
        for (SiteLanguage language : languages) {
            List<CatalogLanguageItem> catalogLanguageItems = portalDaoProvider.getPortalCatalogDao().getCatalogLanguageItemList(language.getSiteLanguageId());
            for (CatalogLanguageItem catalogLanguageItem : catalogLanguageItems) {

                list.add(new SelectItem(catalogLanguageItem.getCatalogLanguageId(), language.getCustomLanguage() + ", " + catalogLanguageItem.getCatalogCode()));
            }
        }
        return list;
    }
}

