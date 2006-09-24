/*
 * org.riverock.portlet - Portlet Library
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.riverock.portlet.manager.menu.action;

import java.io.Serializable;

import org.apache.log4j.Logger;

import org.riverock.portlet.main.AuthSessionBean;
import org.riverock.portlet.manager.menu.MenuDataProvider;
import org.riverock.portlet.manager.menu.MenuSessionBean;
import org.riverock.portlet.manager.menu.bean.MenuCatalogBean;
import org.riverock.portlet.tools.FacesTools;

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

        if( getSessionObject() !=null ) {
            FacesTools.getPortalDaoProvider().getPortalCatalogDao().updateCatalogLanguageItem(getSessionObject());
            cleadDataProviderObject();
            loadCurrentObject();
        }

        return "menu";
    }

    public String cancelEditMenuCatalogAction() {
        log.info( "Cancel edit menu catalog action." );

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
