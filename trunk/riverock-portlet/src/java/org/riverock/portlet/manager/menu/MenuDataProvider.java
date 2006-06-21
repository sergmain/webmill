package org.riverock.portlet.manager.menu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;

import org.riverock.interfaces.portal.bean.SiteLanguage;
import org.riverock.interfaces.portal.bean.Template;
import org.riverock.portlet.manager.menu.bean.MenuCatalogBean;
import org.riverock.portlet.manager.menu.bean.MenuItemExtended;
import org.riverock.portlet.manager.menu.bean.SiteExtended;

/**
 * @author Sergei Maslyukov
 *         Date: 14.06.2006
 *         Time: 21:43:41
 */
public class MenuDataProvider implements Serializable {
    private final static Logger log = Logger.getLogger(MenuDataProvider.class);
    private static final long serialVersionUID = 2057005500L;

    private MenuService menuService =null;
    private MenuSessionBean menuSessionBean = null;

    private SiteExtended siteExtended = null;
    private SiteLanguage siteLanguage = null;
    private MenuItemExtended menuItem = null;
    private MenuCatalogBean menuCatalog = null;

    public MenuDataProvider() {
    }

    public MenuSessionBean getMenuSessionBean() {
        return menuSessionBean;
    }

    public void setMenuSessionBean(MenuSessionBean menuSessionBean) {
        this.menuSessionBean = menuSessionBean;
    }

    public void setMenuService(MenuService menuService) {
        this.menuService = menuService;
    }

    public List<SelectItem> getTemplateList() {
        List<SelectItem> list = new ArrayList<SelectItem>();
        List<Template> portletNames = menuSessionBean.getTemplates();

        for (Template portletName : portletNames) {
            if (portletName.getTemplateId() == null) {
                throw new IllegalStateException("id is null, name: " + portletName.getTemplateName());
            }

            list.add(new SelectItem(portletName.getTemplateId(), portletName.getTemplateName()));
        }
        return list;
    }

    public SiteExtended getSiteExtended() {
        if (menuSessionBean.getObjectType()!=menuSessionBean.getSiteType()) {
            throw new IllegalStateException("Query site info with not site type, current type: " + menuSessionBean.getObjectType());
        }
        Long siteId = menuSessionBean.getId();
        if (siteExtended==null) {
            siteExtended= menuService.getSiteExtended(siteId);
        }
        if (!siteExtended.getSite().equals(siteId)) {
            log.warn("Mismatch siteId");
            siteExtended= menuService.getSiteExtended(siteId);
        }

        return siteExtended;
    }

    public void clearSite() {
        this.siteExtended=null;
    }

    public SiteLanguage getSiteLanguage() {
        if (menuSessionBean.getObjectType()!=menuSessionBean.getSiteLanguageType()) {
            throw new IllegalStateException("Query site language info with not site language type, current type: " + menuSessionBean.getObjectType());
        }
        Long siteLangaugeId = menuSessionBean.getId();
        if (siteLanguage==null) {
            siteLanguage = menuService.getSiteLanguage(siteLangaugeId);
        }
    if (siteLanguage.getSiteLanguageId()==null) {
        return siteLanguage;
    }
        if (!siteLanguage.getSiteLanguageId().equals(siteLangaugeId)) {
            log.warn("Mismatch siteLangaugeId");
            siteLanguage = menuService.getSiteLanguage(siteLangaugeId);
        }

        return siteLanguage;
    }

    public void clearSiteLanguage() {
        this.siteLanguage=null;
    }

    public void clearMenuCatalog() {
        this.menuCatalog=null;
    }

    public MenuCatalogBean getMenuCatalog() {
        if (menuSessionBean.getObjectType()!=menuSessionBean.getMenuCatalogType()) {
            throw new IllegalStateException("Query menu catalog info with not menu catalog type, current type: " + menuSessionBean.getObjectType());
        }
        Long menuCatalogId = menuSessionBean.getId();
        if (menuCatalog==null) {
            menuCatalog = menuService.getMenuCatalog(menuCatalogId);
        }

        if (menuCatalog.getCatalogLanguageId()==null) {
            return menuCatalog;
        }

        if (!menuCatalog.getCatalogLanguageId().equals(menuCatalogId)) {
            log.warn("Mismatch menuCatalogId");
            menuCatalog = menuService.getMenuCatalog(menuCatalogId);
        }

        return menuCatalog;
    }

    public void clearMenuItem() {
        this.menuItem=null;
    }

    public MenuItemExtended getMenuItem() {
        if (menuSessionBean.getObjectType()!=menuSessionBean.getMenuItemType()) {
            throw new IllegalStateException("Query menu item info with not menu item type, current type: " + menuSessionBean.getObjectType());
        }
        Long menuItemId = menuSessionBean.getId();
        if (log.isDebugEnabled()) {
            log.debug("menuItemId: " + menuSessionBean.getId());
        }
        if (menuItem==null) {
            menuItem = menuService.getMenuItem(menuItemId);
        }

        if (menuItem.getMenuItem().getCatalogId()==null) {
            return menuItem;
        }

        if (!menuItem.getMenuItem().getCatalogId().equals(menuItemId)) {
            log.warn("Mismatch menuItemId");
            menuItem = menuService.getMenuItem(menuItemId);
        }

        return menuItem;
    }
}
