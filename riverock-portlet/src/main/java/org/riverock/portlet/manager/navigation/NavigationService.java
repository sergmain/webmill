package org.riverock.portlet.manager.navigation;

import java.util.List;
import java.util.ArrayList;

import javax.faces.model.SelectItem;

import org.riverock.interfaces.portal.bean.SiteLanguage;
import org.riverock.interfaces.portal.bean.Site;
import org.riverock.portlet.tools.FacesTools;
import org.riverock.portlet.manager.site.bean.SiteLanguageBean;

/**
 * User: SergeMaslyukov
 * Date: 04.09.2007
 * Time: 22:18:25
 * $Id$
 */
public class NavigationService {

    public List<SelectItem> getSiteList() {
        List<SelectItem> list = new ArrayList<SelectItem>();
        List<Site> sites = FacesTools.getPortalSpiProvider().getPortalSiteDao().getSites();

        for (Site site : sites) {
            if (site.getSiteId() == null) {
                throw new IllegalStateException("siteId is null, name: " + site.getSiteName());
            }

            list.add(new SelectItem(site.getSiteId(), site.getSiteName()));
        }
        return list;
    }
}
