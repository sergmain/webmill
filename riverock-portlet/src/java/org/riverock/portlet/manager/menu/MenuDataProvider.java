package org.riverock.portlet.manager.menu;

import java.io.Serializable;

import org.apache.log4j.Logger;

import org.riverock.interfaces.portal.bean.SiteLanguage;
import org.riverock.portlet.manager.menu.bean.MenuItemBean;
import org.riverock.portlet.manager.menu.bean.SiteExtended;
import org.riverock.portlet.manager.menu.bean.MenuCatalogBean;

/**
 * @author Sergei Maslyukov
 *         Date: 14.06.2006
 *         Time: 21:43:41
 */
public class MenuDataProvider implements Serializable {
    private final static Logger log = Logger.getLogger(MenuDataProvider.class);
    private static final long serialVersionUID = 2057005500L;

    private MenuService siteService=null;
    private MenuSessionBean siteSessionBean = null;

    private SiteExtended siteExtended = null;
    private SiteLanguage siteLanguage = null;
    private MenuItemBean menuItem = null;
    private MenuCatalogBean menuCatalog = null;

    public MenuDataProvider() {
    }

    public MenuSessionBean getSiteSessionBean() {
        return siteSessionBean;
    }

    public void setSiteSessionBean(MenuSessionBean siteSessionBean) {
        this.siteSessionBean = siteSessionBean;
    }

    public void setSiteService(MenuService siteService) {
        this.siteService = siteService;
    }

    public SiteExtended getSiteExtended() {
        if (siteSessionBean.getObjectType()!=siteSessionBean.getSiteType()) {
            throw new IllegalStateException("Query site info with not site type, current type: " + siteSessionBean.getObjectType());
        }
        Long siteId = siteSessionBean.getId();
        if (siteExtended==null) {
            siteExtended= siteService.getSiteExtended(siteId);
        }
        if (!siteExtended.getSite().equals(siteId)) {
            log.warn("Mismatch siteId");
            siteExtended= siteService.getSiteExtended(siteId);
        }

        return siteExtended;
    }

    public void clearSite() {
        this.siteExtended=null;
    }

    public SiteLanguage getSiteLanguage() {
        if (siteSessionBean.getObjectType()!=siteSessionBean.getSiteLanguageType()) {
            throw new IllegalStateException("Query site language info with not site language type, current type: " + siteSessionBean.getObjectType());
        }
        Long siteLangaugeId = siteSessionBean.getId();
        if (siteLanguage==null) {
            siteLanguage = siteService.getSiteLanguage(siteLangaugeId);
        }
    if (siteLanguage.getSiteLanguageId()==null) {
        return siteLanguage;
    }
        if (!siteLanguage.getSiteLanguageId().equals(siteLangaugeId)) {
            log.warn("Mismatch siteLangaugeId");
            siteLanguage = siteService.getSiteLanguage(siteLangaugeId);
        }

        return siteLanguage;
    }

    public void clearSiteLanguage() {
        this.siteLanguage=null;
    }

/*
    public Css getCss() {
        if (siteSessionBean.getObjectType()!=siteSessionBean.getCssType()) {
            throw new IllegalStateException("Query CSS info with not CSS type, current type: " + siteSessionBean.getObjectType());
        }
        Long cssId = siteSessionBean.getId();
        if (css==null) {
            css = siteService.getCss(cssId);
        }

    if (css.getCssId()==null) {
        return css;
    }

        if (!css.getCssId().equals(cssId)) {
            log.warn("Mismatch cssId");
            css = siteService.getCss(cssId);
        }

        return css;
    }

    public void clearCss() {
        this.css=null;
    }
*/

    public void clearMenuItem() {
        this.menuItem=null;
    }

    public void clearMenuCatalog() {
        this.menuCatalog=null;
    }

    public MenuItemBean getMenuItem() {
        if (siteSessionBean.getObjectType()!=siteSessionBean.getMenuItemType()) {
            throw new IllegalStateException("Query menu item info with not menu item type, current type: " + siteSessionBean.getObjectType());
        }
        Long menuItemId = siteSessionBean.getId();
        if (menuItem==null) {
            menuItem = siteService.getMenuItem(menuItemId);
        }

        if (menuItem.getCatalogId()==null) {
            return menuItem;
        }

        if (!menuItem.getCatalogId().equals(menuItemId)) {
            log.warn("Mismatch menuItemId");
            menuItem = siteService.getMenuItem(menuItemId);
        }

        return menuItem;
    }
}
