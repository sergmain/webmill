package org.riverock.portlet.manager.navigation;

import java.util.ArrayList;
import java.util.List;

import org.riverock.interfaces.portal.bean.SiteLanguage;
import org.riverock.interfaces.portal.bean.Template;
import org.riverock.portlet.manager.bean.TemplateBean;
import org.riverock.portlet.manager.site.bean.SiteLanguageBean;
import org.riverock.portlet.tools.FacesTools;

/**
 * User: SergeMaslyukov
 * Date: 07.09.2007
 * Time: 0:01:17
 * $Id$
 */
public class NavigationDataProvider {
    private NavigationSessionBean navigationSessionBean;

    private NavigationService navigationService;


    public NavigationSessionBean getNavigationSessionBean() {
        return navigationSessionBean;
    }

    public void setNavigationSessionBean(NavigationSessionBean navigationSessionBean) {
        this.navigationSessionBean = navigationSessionBean;
    }

    public NavigationService getNavigationService() {
        return navigationService;
    }

    public void setNavigationService(NavigationService navigationService) {
        this.navigationService = navigationService;
    }

    public List<SiteLanguage> getSiteLanguageList() {
        Long siteId = navigationSessionBean.getCurrentSiteId();
        List<SiteLanguage> list = new ArrayList<SiteLanguage>();
        List<SiteLanguage> siteLanguages = FacesTools.getPortalSpiProvider().getPortalSiteLanguageDao().getSiteLanguageList(siteId);
        for (SiteLanguage item: siteLanguages) {
            list.add( new SiteLanguageBean(item) );
        }
        return list;
    }

    public List<Template> getTemplateList() {
        List<Template> list = new ArrayList<Template>();
        Long siteLanguageId = navigationSessionBean.getCurrentSiteLanguageId();
        if (siteLanguageId==null) {
            return list;
        }
        List<Template> templates = FacesTools.getPortalSpiProvider().getPortalTemplateDao().getTemplateLanguageList(siteLanguageId);
        for (Template item: templates) {
            list.add( new TemplateBean(item) );
        }
        return list;

    }


}
