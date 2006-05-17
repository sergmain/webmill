/**
 * License
 * 
 */
package org.riverock.portlet.manager.site;

import java.util.ArrayList;
import java.util.List;

import org.riverock.interfaces.portal.bean.Site;
import org.riverock.interfaces.portal.bean.SiteLanguage;
import org.riverock.portlet.tools.FacesTools;
import org.riverock.portlet.manager.site.bean.SiteBean;
import org.riverock.portlet.manager.site.bean.SiteLanguageBean;

/**
 * @author Sergei Maslyukov
 * 16.05.2006
 * 20:39:26
 *
 *
 */
public class SiteService {
    public SiteService() {
    }

    public List<Site> getSites() {
        List<Site> list = new ArrayList<Site>();
        List<Site> sites = FacesTools.getPortalDaoProvider().getPortalSiteDao().getSites();
        for (Site site: sites) {
            list.add( new SiteBean(site) );
        }
        return list;
    }

    public List<SiteLanguage> getSiteLanguage(Long siteId) {
        List<SiteLanguage> list = new ArrayList<SiteLanguage>();
        List<SiteLanguage> siteLanguages = FacesTools.getPortalDaoProvider().getPortalSiteLanguageDao().getSiteLanguageList(siteId);
        for (SiteLanguage item: siteLanguages) {
            list.add( new SiteLanguageBean(item) );
        }
        return list;
    }
}
