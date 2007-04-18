/*
 * org.riverock.portlet - Portlet Library
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
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
public class MenuSessionBean implements Serializable {
    private static final long serialVersionUID = 2058005508L;

    public static final int UNKNOWN_TYPE = 0;
    public static final int SITE_TYPE = 1;
    public static final int SITE_LANGUAGE_TYPE = 2;
    public static final int MENU_CATALOG_TYPE = 3;
    public static final int MENU_ITEM_TYPE = 4;

    private Long id = null;
    private int objectType=0;

    private Long currentMenuItemId=null;
    private Long currentMenuCatalogId=null;
    private Long currentSiteId=null;

    // used for cummulative change template for menu items
    private Long templateId = null;

    private SiteExtended siteExtended = null;
    private SiteLanguageBean siteLanguage = null;
    private MenuCatalogBean menuCatalog = null;
    private MenuItemExtended menuItem = null;
    private List<Template> templates=null;
    private Long previousCreatedPortletId=null;

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public Long getPreviousCreatedPortletId() {
        return previousCreatedPortletId;
    }

    public void setPreviousCreatedPortletId(Long previousCreatedPortletId) {
        this.previousCreatedPortletId = previousCreatedPortletId;
    }

    public Long getCurrentSiteId() {
        return currentSiteId;
    }

    public void setCurrentSiteId(Long currentSiteId) {
        this.currentSiteId = currentSiteId;
    }

    public Long getCurrentMenuCatalogId() {
        return currentMenuCatalogId;
    }

    public void setCurrentMenuCatalogId(Long currentMenuCatalogId) {
        this.currentMenuItemId = null;
        this.currentMenuCatalogId = currentMenuCatalogId;
    }

    public Long getCurrentMenuItemId() {
        return currentMenuItemId;
    }

    public void setCurrentMenuItemId(Long currentMenuItemId) {
        this.currentMenuCatalogId = null;
        this.currentMenuItemId = currentMenuItemId;
    }

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
