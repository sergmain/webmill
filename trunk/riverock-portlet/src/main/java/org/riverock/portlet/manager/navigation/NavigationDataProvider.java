package org.riverock.portlet.manager.navigation;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.riverock.interfaces.portal.bean.PortletAlias;
import org.riverock.interfaces.portal.bean.PortletName;
import org.riverock.interfaces.portal.bean.SiteLanguage;
import org.riverock.interfaces.portal.bean.Template;
import org.riverock.interfaces.portal.bean.UrlAlias;
import org.riverock.interfaces.portal.spi.PortalSpiProvider;
import org.riverock.portlet.manager.bean.TemplateBean;
import org.riverock.portlet.manager.navigation.bean.PortletAliasBean;
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

        if (navigationSessionBean.getCurrentSiteLanguageId()!=null) {
            Template template;
            PortalSpiProvider portalSpiProvider = FacesTools.getPortalSpiProvider();
            
            template = portalSpiProvider.getPortalTemplateDao().getDefaultDynamicTemplate(navigationSessionBean.getCurrentSiteLanguageId());
            if (template!=null) {
                navigationSessionBean.setDynamicTemplateId(template.getTemplateId());
            }
            else {
                navigationSessionBean.setDynamicTemplateId(null);
            }

            template = portalSpiProvider.getPortalTemplateDao().getPopupTemplate(navigationSessionBean.getCurrentSiteLanguageId());
            if (template!=null) {
                navigationSessionBean.setPopupTemplateId(template.getTemplateId());
            }
            else {
                navigationSessionBean.setPopupTemplateId(null);
            }

            template = portalSpiProvider.getPortalTemplateDao().getMaximizedTemplate(navigationSessionBean.getCurrentSiteLanguageId());
            if (template!=null) {
                navigationSessionBean.setMaximazedTemplateId(template.getTemplateId());
            }
            else {
                navigationSessionBean.setMaximazedTemplateId(null);
            }
        }
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
        if (siteId==null) {
            return list;
        }
        
        List<SiteLanguage> siteLanguages = FacesTools.getPortalSpiProvider().getPortalSiteLanguageDao().getSiteLanguageList(siteId);
        for (SiteLanguage item: siteLanguages) {
            list.add( new SiteLanguageBean(item) );
        }
        return list;
    }

    public List<SelectItem> getTemplateList() {
        List<SelectItem> list = new ArrayList<SelectItem>();
        Long siteLanguageId = navigationSessionBean.getCurrentSiteLanguageId();
        if (siteLanguageId==null) {
            return list;
        }
        
        List<Template> templates = FacesTools.getPortalSpiProvider().getPortalTemplateDao().getTemplateLanguageList(siteLanguageId);
        for (Template item: templates) {
            list.add(new SelectItem(item.getTemplateId(), item.getTemplateName()));
        }
        return list;

    }

    public List<PortletAliasBean> getPortletAliases() {
        List<PortletAliasBean> list = new ArrayList<PortletAliasBean>();
        Long siteId = navigationSessionBean.getCurrentSiteId();
        if (siteId==null) {
            return list;
        }

        PortalSpiProvider portalSpiProvider = FacesTools.getPortalSpiProvider();
        List<PortletAlias> aliases = portalSpiProvider.getPortalAliasSpi().getPortletAliases(siteId);
        for (PortletAlias item: aliases) {
            PortletName portletName = portalSpiProvider.getPortalPortletNameDao().getPortletName(item.getPortletNameId());
            Template template = portalSpiProvider.getPortalTemplateDao().getTemplate(item.getTemplateId());
            list.add( new PortletAliasBean(item, portletName.getPortletName(), template.getTemplateName()) );
        }
        return list;

    }

    public List<UrlAlias> getUrlAliases() {
        List<UrlAlias> list = new ArrayList<UrlAlias>();
        Long siteId = navigationSessionBean.getCurrentSiteId();
        if (siteId==null) {
            return list;
        }

        PortalSpiProvider portalSpiProvider = FacesTools.getPortalSpiProvider();
        List<UrlAlias> aliases = portalSpiProvider.getPortalAliasSpi().getUrlAliases(siteId);
        return aliases;
/*
        for (UrlAlias item: aliases) {
            PortletName portletName = portalSpiProvider.getPortalPortletNameDao().getPortletName(item.getPortletNameId());
            Template template = portalSpiProvider.getPortalTemplateDao().getTemplate(item.getTemplateId());
            list.add( new PortletAliasBean(item, portletName.getPortletName(), template.getTemplateName()) );
        }
        return list;
*/

    }

    public List<Template> getTemplates() {
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
