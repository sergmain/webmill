package org.riverock.portlet.manager.menu;

import java.io.Serializable;
import java.util.List;

import org.riverock.interfaces.portal.bean.Template;
import org.riverock.portlet.manager.menu.bean.MenuCatalogBean;
import org.riverock.portlet.manager.menu.bean.MenuItemExtended;
import org.riverock.portlet.manager.menu.bean.SiteExtended;
import org.riverock.portlet.manager.site.bean.SiteLanguageBean;

/**
 * @author Sergei Maslyukov
 *         Date: 14.06.2006
 *         Time: 21:44:13
 */
public class MenuSessionBean  implements Serializable {
    private static final long serialVersionUID = 2058005508L;

    public static final int UNKNOWN_TYPE = 0;
    public static final int SITE_TYPE = 1;
    public static final int SITE_LANGUAGE_TYPE = 2;
    public static final int MENU_CATALOG_TYPE = 3;
    public static final int MENU_ITEM_TYPE = 4;

    private Long id = null;
    private int objectType=0;

    private SiteExtended siteExtended = null;
    private SiteLanguageBean siteLanguage = null;
    private MenuCatalogBean menuCatalog = null;
    private MenuItemExtended menuItem = null;
    private List<Template> templates=null;

    public List<Template> getTemplates() {
        return templates;
    }

    public void setTemplates(List<Template> templates) {
        this.templates = templates;
    }

    public MenuItemExtended getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(MenuItemExtended menuItem) {
        this.menuItem = menuItem;
    }

    public SiteExtended getSiteExtended() {
        return siteExtended;
    }

    public void setSiteExtended(SiteExtended siteExtended) {
        this.siteExtended = siteExtended;
    }

    public SiteLanguageBean getSiteLanguage() {
        return siteLanguage;
    }

    public void setSiteLanguage(SiteLanguageBean siteLanguage) {
        this.siteLanguage = siteLanguage;
    }

    public MenuCatalogBean getMenuCatalog() {
        return menuCatalog;
    }

    public void setMenuCatalog(MenuCatalogBean menuCatalog) {
        this.menuCatalog = menuCatalog;
    }

    public int getSiteType() {
        return SITE_TYPE;
    }

    public int getSiteLanguageType() {
        return SITE_LANGUAGE_TYPE;
    }

    public int getMenuCatalogType() {
        return MENU_CATALOG_TYPE;
    }

    public int getMenuItemType() {
        return MENU_ITEM_TYPE;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getObjectType() {
        return objectType;
    }

    public void setObjectType(int objectType) {
        this.objectType = objectType;
    }
}
