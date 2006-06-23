package org.riverock.portlet.manager.menu;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;

import org.riverock.common.collections.TreeUtils;
import org.riverock.interfaces.common.TreeItem;
import org.riverock.interfaces.portal.bean.CatalogItem;
import org.riverock.interfaces.portal.bean.CatalogLanguageItem;
import org.riverock.interfaces.portal.bean.Company;
import org.riverock.interfaces.portal.bean.PortletName;
import org.riverock.interfaces.portal.bean.Site;
import org.riverock.interfaces.portal.bean.SiteLanguage;
import org.riverock.portlet.manager.menu.bean.MenuCatalogBean;
import org.riverock.portlet.manager.menu.bean.MenuItemBean;
import org.riverock.portlet.manager.menu.bean.MenuItemExtended;
import org.riverock.portlet.manager.menu.bean.SiteBean;
import org.riverock.portlet.manager.menu.bean.SiteExtended;
import org.riverock.portlet.manager.menu.bean.SiteLanguageBean;
import org.riverock.portlet.tools.FacesTools;

/**
 * @author Sergei Maslyukov
 *         Date: 14.06.2006
 *         Time: 21:44:02
 */
public class MenuService {
    private final static Logger log = Logger.getLogger(MenuService.class);

    public MenuService() {
    }

    public List<SelectItem> getPortletList() {
        List<SelectItem> list = new ArrayList<SelectItem>();
        List<PortletName> portletNames = FacesTools.getPortalDaoProvider().getPortalPortletNameDao().getPortletNameList();

        for (PortletName portletName : portletNames) {
            if (portletName.getPortletId() == null) {
                throw new IllegalStateException("id is null, name: " + portletName.getPortletName());
            }

            list.add(new SelectItem(portletName.getPortletId(), portletName.getPortletName()));
        }
        return list;
    }

    public List<Site> getSites() {
        List<Site> list = new ArrayList<Site>();
        List<Site> sites = FacesTools.getPortalDaoProvider().getPortalSiteDao().getSites();
        for (Site site : sites) {
            list.add(new SiteBean(site));
        }
        return list;
    }

    public List<CatalogLanguageItem> getMenuCatalogList(Long siteLanguageId) {
        List<CatalogLanguageItem> list = new ArrayList<CatalogLanguageItem>();
        List<CatalogLanguageItem> items = FacesTools.getPortalDaoProvider().getPortalCatalogDao().getCatalogLanguageItemList(siteLanguageId);
        for (CatalogLanguageItem item : items) {
            list.add(new MenuCatalogBean(item));
        }
        return list;
    }

    public List<SiteLanguage> getSiteLanguageList(Long siteId) {
        List<SiteLanguage> list = new ArrayList<SiteLanguage>();
        List<SiteLanguage> siteLanguages = FacesTools.getPortalDaoProvider().getPortalSiteLanguageDao().getSiteLanguageList(siteId);
        for (SiteLanguage item : siteLanguages) {
            list.add(new SiteLanguageBean(item));
        }
        return list;
    }

    public SiteExtended getSiteExtended(Long siteId) {
        SiteExtended siteExtended = new SiteExtended();
        siteExtended.setSite(FacesTools.getPortalDaoProvider().getPortalSiteDao().getSite(siteId));
        Long companyId = siteExtended.getSite().getCompanyId();
        Company company = FacesTools.getPortalDaoProvider().getPortalCompanyDao().getCompany(companyId);
        if (log.isDebugEnabled()) {
            log.debug("companyId: " + companyId);
            log.debug("company: " + company);
        }
        siteExtended.setCompany(company);
        return siteExtended;
    }

    public SiteLanguage getSiteLanguage(Long siteLanguageId) {
        SiteLanguage siteLanguage = FacesTools.getPortalDaoProvider().getPortalSiteLanguageDao().getSiteLanguage(siteLanguageId);
        if (siteLanguage != null) {
            return new SiteLanguageBean(siteLanguage);
        } else {
            return new SiteLanguageBean();
        }
    }

    public MenuItemExtended getMenuItem(Long catalogId) {
        MenuItemExtended menuExtended = new MenuItemExtended();
        menuExtended.setMenuItem(FacesTools.getPortalDaoProvider().getPortalCatalogDao().getCatalogItem(catalogId));
        menuExtended.setPortletName(FacesTools.getPortalDaoProvider().getPortalPortletNameDao().getPortletName(menuExtended.getMenuItem().getPortletId()));
        if (menuExtended.getMenuItem().getTemplateId()!=null) {
            menuExtended.setTemplate(FacesTools.getPortalDaoProvider().getPortalTemplateDao().getTemplate(menuExtended.getMenuItem().getTemplateId()));
        }
        return menuExtended;
    }

    public List<CatalogItem> getMenuItemList(Long menuCatalogId) {
        List<CatalogItem> list = new ArrayList<CatalogItem>();
        List<CatalogItem> items = FacesTools.getPortalDaoProvider().getPortalCatalogDao().getCatalogItemList(menuCatalogId);
        items = (List<CatalogItem>)(List) TreeUtils.rebuildTree((List<TreeItem>)((List)items));

        for (CatalogItem item : items) {
            list.add(new MenuItemBean(item));
        }
        return list;
    }

    public MenuCatalogBean getMenuCatalog(Long menuCatalogId) {
        CatalogLanguageItem bean = FacesTools.getPortalDaoProvider().getPortalCatalogDao().getCatalogLanguageItem(menuCatalogId);
        if (bean != null) {
            return new MenuCatalogBean(bean);
        } else {
            return new MenuCatalogBean();
        }
    }
}