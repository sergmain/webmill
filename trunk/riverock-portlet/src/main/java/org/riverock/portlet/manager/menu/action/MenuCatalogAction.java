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

import org.riverock.portlet.main.AuthSessionBean;
import org.riverock.portlet.manager.menu.MenuDataProvider;
import org.riverock.portlet.manager.menu.MenuSessionBean;
import org.riverock.portlet.manager.menu.bean.MenuCatalogBean;
import org.riverock.portlet.tools.FacesTools;
import org.riverock.common.utils.PortletUtils;

/**
 * @author Sergei Maslyukov
 * 16.05.2006
 * 20:14:59
 *
 *
 */
public class MenuCatalogAction implements Serializable {
    private final static Logger log = Logger.getLogger( MenuCatalogAction.class );
    private static final long serialVersionUID = 2057005511L;

    private MenuSessionBean menuSessionBean = null;
    private AuthSessionBean authSessionBean = null;
    private MenuDataProvider dataProvider = null;

    public MenuCatalogAction() {
    }

    public void setDataProvider(MenuDataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    // getter/setter methods
    public void setMenuSessionBean( MenuSessionBean siteSessionBean) {
        this.menuSessionBean = siteSessionBean;
    }

    public AuthSessionBean getAuthSessionBean() {
        return authSessionBean;
    }

    public void setAuthSessionBean( AuthSessionBean authSessionBean) {
        this.authSessionBean = authSessionBean;
    }

    // main select action
    public String selectMenuCatalog() {
        log.info( "Select menu catalog action." );
        loadCurrentObject();

        return "menu";
    }

    // changeTemplateForCatalogLanguageAction
    public String changeTemplateForCatalogLanguageAction() {
        Long siteLanguageId = FacesTools.getPortalDaoProvider().getPortalCatalogDao().getCatalogLanguageItem(
            getSessionObject().getCatalogLanguageId()
        ).getSiteLanguageId();
        menuSessionBean.setTemplates(FacesTools.getPortalDaoProvider().getPortalTemplateDao().getTemplateLanguageList(siteLanguageId));
        menuSessionBean.setTemplateId(null);

        return "menu-catalog-change-template";
    }

    public String processChangeTemplateForCatalogLanguageAction() {
        log.info( "Save changes of template catalog action." );

        PortletUtils.checkRights(FacesTools.getPortletRequest(), MenuAction.ROLES);

        if( getSessionObject() !=null ) {
            FacesTools.getPortalDaoProvider().getPortalCatalogDao().changeTemplateForCatalogLanguage(
                    getSessionObject().getCatalogLanguageId(), menuSessionBean.getTemplateId()
            );
            cleadDataProviderObject();
            loadCurrentObject();
        }

        return "menu";
    }

    public String cancelChangeTemplateForCatalogLanguageAction() {
        log.info( "Cancel change template menu catalog action." );

        setSessionObject(null);
        menuSessionBean.setTemplateId(null);
        cleadDataProviderObject();

        return "menu";
    }

// Add actions
    public String addMenuCatalogAction() {
        log.info( "Add menu catalog action." );

        MenuCatalogBean menuCatalogBean = new MenuCatalogBean();
        menuCatalogBean.setSiteLanguageId(menuSessionBean.getId());
        setSessionObject(menuCatalogBean);

        return "menu-catalog-add";
    }

    public String processAddMenuCatalogAction() {
        log.info( "Procss add menu catalog action." );

        PortletUtils.checkRights(FacesTools.getPortletRequest(), MenuAction.ROLES);

        if( getSessionObject() !=null ) {
            Long cssId = FacesTools.getPortalDaoProvider().getPortalCatalogDao().createCatalogLanguageItem(
                getSessionObject()
            );
            setSessionObject(null);
            menuSessionBean.setId(cssId);
            cleadDataProviderObject();
            loadCurrentObject();
        }

        return "menu";
    }

    public String cancelAddMenuCatalogAction() {
        log.info( "Cancel add menu catalog action." );

        setSessionObject(null);
        cleadDataProviderObject();

        return "menu";
    }

// Edit actions
    public String editMenuCatalogAction() {
        log.info( "Edit menu catalog action." );

        return "menu-catalog-edit";
    }

    public String processEditMenuCatalogAction() {
        log.info( "Save changes menu catalog action." );

        PortletUtils.checkRights(FacesTools.getPortletRequest(), MenuAction.ROLES);

        if( getSessionObject() !=null ) {
            FacesTools.getPortalDaoProvider().getPortalCatalogDao().updateCatalogLanguageItem(getSessionObject());
            cleadDataProviderObject();
            loadCurrentObject();
        }

        return "menu";
    }

    public String cancelEditMenuCatalogAction() {
        log.info( "Cancel edit menu catalog action." );

        loadCurrentObject();
        return "menu";
    }

// Delete actions
    public String deleteMenuCatalogAction() {
        log.info( "delete menu catalog action." );

        setSessionObject( new MenuCatalogBean(dataProvider.getMenuCatalog()) );

        return "menu-catalog-delete";
    }

    public String cancelDeleteMenuCatalogAction() {
        log.info( "Cancel delete menu catalog action." );

        return "menu";
    }

    public String processDeleteMenuCatalogAction() {
        log.info( "Process delete menu catalog action. getSessionObject().getCatalogLanguageId(): " +getSessionObject().getCatalogLanguageId() );

        PortletUtils.checkRights(FacesTools.getPortletRequest(), MenuAction.ROLES);

        if( getSessionObject() != null ) {
            FacesTools.getPortalDaoProvider().getPortalCatalogDao().deleteCatalogLanguageItem(getSessionObject().getCatalogLanguageId());
            setSessionObject(null);
            menuSessionBean.setId(null);
            menuSessionBean.setObjectType(MenuSessionBean.UNKNOWN_TYPE);
            cleadDataProviderObject();
        }

        return "menu";
    }

    private void setSessionObject(MenuCatalogBean bean) {
        menuSessionBean.setMenuCatalog( bean );
    }

    private void loadCurrentObject() {
        menuSessionBean.setMenuCatalog( new MenuCatalogBean(dataProvider.getMenuCatalog()) );
    }

    private void cleadDataProviderObject() {
        dataProvider.clearMenuCatalog();
    }

    private MenuCatalogBean getSessionObject() {
        return menuSessionBean.getMenuCatalog();
    }

}
