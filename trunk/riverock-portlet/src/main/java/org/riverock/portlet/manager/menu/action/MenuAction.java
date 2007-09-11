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
package org.riverock.portlet.manager.menu.action;

import java.io.Serializable;

import org.apache.log4j.Logger;

import org.riverock.interfaces.portal.bean.CatalogItem;
import org.riverock.portlet.main.AuthSessionBean;
import org.riverock.portlet.manager.menu.MenuDataProvider;
import org.riverock.portlet.manager.menu.MenuSessionBean;
import org.riverock.portlet.manager.menu.bean.MenuItemBean;
import org.riverock.portlet.manager.menu.bean.MenuItemExtended;
import org.riverock.portlet.tools.FacesTools;
import org.riverock.common.utils.PortletUtils;

/**
 * @author Sergei Maslyukov
 *         16.05.2006
 *         20:14:59
 */
public class MenuAction implements Serializable {
    private final static Logger log = Logger.getLogger(MenuAction.class);
    private static final long serialVersionUID = 2057005511L;

    private MenuSessionBean menuSessionBean = null;
    private AuthSessionBean authSessionBean = null;
    private MenuDataProvider dataProvider = null;

    static final String[] ROLES = new String[]{"webmill.portal-manager", "webmill.site-manager", "webmill.menu"};

    public MenuAction() {
    }

    public void setDataProvider(MenuDataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    // getter/setter methods
    public void setMenuSessionBean(MenuSessionBean siteSessionBean) {
        this.menuSessionBean = siteSessionBean;
    }

    public AuthSessionBean getAuthSessionBean() {
        return authSessionBean;
    }

    public void setAuthSessionBean(AuthSessionBean authSessionBean) {
        this.authSessionBean = authSessionBean;
    }

// main select action
    public String selectMenuItem() {
        log.info("Select menu item action.");
        loadCurrentObject();

        return "menu";
    }

    // Add actions
    public String addMenuItemAction() {
        log.info("Add menu item action.");

        MenuItemBean menuItemBean = new MenuItemBean();

        menuItemBean.setCatalogId(menuSessionBean.getId());
        setSessionObject(new MenuItemExtended(menuItemBean, null, null));
        menuSessionBean.getMenuItem().getMenuItem().setPortletId(menuSessionBean.getPreviousCreatedPortletId());

        Long menuCatalogId;
        if (menuSessionBean.getCurrentMenuCatalogId() != null) {
            menuCatalogId =  menuSessionBean.getCurrentMenuCatalogId();
        } else {
            CatalogItem catalogItem = FacesTools.getPortalSpiProvider().getPortalCatalogDao().getCatalogItem(
                menuSessionBean.getCurrentMenuItemId()
            );
            menuCatalogId = catalogItem.getCatalogLanguageId();
        }
        Long siteLanguageId = FacesTools.getPortalSpiProvider().getPortalCatalogDao().getCatalogLanguageItem(menuCatalogId).getSiteLanguageId();
        menuSessionBean.setTemplates(FacesTools.getPortalSpiProvider().getPortalTemplateDao().getTemplateLanguageList(siteLanguageId));

        return "menu-add";
    }

    public String processAddMenuItemAction() {
        log.info("Procss add menu item action.");

        PortletUtils.checkRights(FacesTools.getPortletRequest(), ROLES);

        if (getSessionObject() != null) {

            if (menuSessionBean.getCurrentMenuItemId() == null && menuSessionBean.getCurrentMenuCatalogId() == null) {
                throw new IllegalStateException("Both currentMenuItemId and currentMenuCatalogId are null");
            }

            if (menuSessionBean.getCurrentMenuItemId() != null && menuSessionBean.getCurrentMenuCatalogId() != null) {
                throw new IllegalStateException("Both currentMenuItemId and currentMenuCatalogId are not null");
            }

            MenuItemBean menuItem = getSessionObject().getMenuItem();
            if (menuSessionBean.getCurrentMenuCatalogId() != null) {
                menuItem.setCatalogLanguageId( menuSessionBean.getCurrentMenuCatalogId() );
                menuItem.setTopCatalogId(0L);
            } else {
                CatalogItem catalogItem = FacesTools.getPortalSpiProvider().getPortalCatalogDao().getCatalogItem(
                    menuSessionBean.getCurrentMenuItemId()
                );
                menuItem.setCatalogLanguageId( catalogItem.getCatalogLanguageId() );
                menuItem.setTopCatalogId( catalogItem.getCatalogId() );
            }

            Long menuItemId = FacesTools.getPortalSpiProvider().getPortalCatalogDao().createCatalogItem(menuItem);
            setSessionObject(null);
            menuSessionBean.setPreviousCreatedPortletId(menuItem.getPortletId());
            menuSessionBean.setId(menuItemId);
            cleadDataProviderObject();
            loadCurrentObject();
        }

        return "menu";
    }

    public String cancelAddMenuItemAction() {
        log.info("Cancel add menu item action.");

        setSessionObject(null);
        cleadDataProviderObject();

        return "menu";
    }

// Edit actions
    public String editMenuItemAction() {
        log.info("Edit menu item action.");

        Long menuCatalogId = getSessionObject().getMenuItem().getCatalogLanguageId();
        Long siteLanguageId = FacesTools.getPortalSpiProvider().getPortalCatalogDao().getCatalogLanguageItem(menuCatalogId).getSiteLanguageId();
        menuSessionBean.setTemplates(FacesTools.getPortalSpiProvider().getPortalTemplateDao().getTemplateLanguageList(siteLanguageId));

        return "menu-edit";
    }

    public String processEditMenuItemAction() {
        log.info("Save changes menu item action.");

        PortletUtils.checkRights(FacesTools.getPortletRequest(), ROLES);

        if (getSessionObject() != null) {
            if (log.isDebugEnabled()) {
                if (getSessionObject().getMenuItem()!=null) {
                    log.debug("Metadata: " + getSessionObject().getMenuItem().getMetadata());
                    log.debug("contextId: " + getSessionObject().getMenuItem().getContextId());
                }
                else {
                    log.debug("Menu item is null.");
                }
            }
            FacesTools.getPortalSpiProvider().getPortalCatalogDao().updateCatalogItem(getSessionObject().getMenuItem());
            cleadDataProviderObject();
            loadCurrentObject();
        }
        else {
            log.warn("getSessionObject() is null");
        }

        return "menu";
    }

    public String cancelEditMenuItemAction() {
        log.info("Cancel edit menu item action.");
        loadCurrentObject();

        return "menu";
    }

// Delete actions
    public String deleteMenuItemAction() {
        log.info("delete menu item action.");

        setSessionObject(dataProvider.getMenuItem());

        return "menu-delete";
    }

    public String cancelDeleteMenuItemAction() {
        log.info("Cancel delete menu item action.");

        return "menu";
    }

    public String processDeleteMenuItemAction() {
        log.info("Process delete menu item action.");

        PortletUtils.checkRights(FacesTools.getPortletRequest(), ROLES);

        if (getSessionObject() != null) {
            FacesTools.getPortalSpiProvider().getPortalCatalogDao().deleteCatalogItem(getSessionObject().getMenuItem().getCatalogId());
            setSessionObject(null);
            menuSessionBean.setId(null);
            menuSessionBean.setObjectType(MenuSessionBean.UNKNOWN_TYPE);
            cleadDataProviderObject();
        }

        return "menu";
    }

    private void setSessionObject(MenuItemExtended bean) {
        menuSessionBean.setMenuItem(bean);
    }

    private void loadCurrentObject() {
        log.debug("start loadCurrentObject()");

        menuSessionBean.setMenuItem(dataProvider.getMenuItem());
    }

    private void cleadDataProviderObject() {
        dataProvider.clearMenuItem();
    }

    private MenuItemExtended getSessionObject() {
        return menuSessionBean.getMenuItem();
    }

}
