/*
 * org.riverock.webmill - Portal framework implementation
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
package org.riverock.webmill.portal.menu;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import org.riverock.interfaces.portal.bean.CatalogLanguageItem;
import org.riverock.interfaces.portal.bean.SiteLanguage;
import org.riverock.interfaces.portlet.menu.Menu;
import org.riverock.interfaces.portlet.menu.MenuItem;
import org.riverock.interfaces.portlet.menu.MenuLanguage;
import org.riverock.webmill.portal.dao.InternalDaoFactory;

/**
 * $Id$
 */
public final class PortalMenuLanguage implements MenuLanguage {
    private final static Logger log = Logger.getLogger( PortalMenuLanguage.class );

    private List<Menu> menu = new ArrayList<Menu>();
    private SiteLanguage item = null;

    public PortalMenuLanguage(){}

    // return name of template for 'index' page
    public MenuItem getIndexMenuItem(){
        if (log.isDebugEnabled()){
            log.debug("menu: "+menu);
        }

        for (Menu catalog : this.menu) {
            MenuItem item = catalog.getIndexMenuItem();
            if (item != null)
                return item;
        }
        return null;
    }

    // return name of template for 'index' page
    public String getIndexTemplate(){
        MenuItem indexTemplate = getIndexMenuItem();
        if (indexTemplate != null)
            return indexTemplate.getNameTemplate();

        return null;
    }

    public Menu getDefault() {
        for (Menu catalog : this.menu) {
            if (catalog.getIsDefault())
                return catalog;
        }
        return null;
    }

    public Menu getCatalogByCode( String code ) {
        if (code==null)
            return null;

        for (Menu catalog : this.menu) {
            if (code.equals(catalog.getCatalogCode()))
                return catalog;
        }
        return null;
    }

    public MenuItem searchMenuItem(Long id)
    {
        if (id==null)
            return null;

        for (Menu menu : this.menu) {
            MenuItem ci = menu.searchMenuItem(id);
            if (ci != null) {
                return ci;
            }
        }
        return null;
    }

    public PortalMenuLanguage(SiteLanguage bean) {
        if (bean == null)
            return;

        this.item = bean;

        if (log.isDebugEnabled()) {
            log.debug("#33.07.00 ");
            log.debug("ID_SITE_SUPPORT_LANGUAGE -  " + bean.getSiteLanguageId() );
            log.debug("locale - " + getLocaleStr());
            log.debug("language - " + getLang());
        }

        List<CatalogLanguageItem> list = InternalDaoFactory.getInternalCatalogDao().getCatalogLanguageItemList( bean.getSiteLanguageId() );

        for (CatalogLanguageItem catalogLanguageBean : list) {
            Menu catalog = new PortalMenu(catalogLanguageBean);
            menu.add(catalog);
        }
    }

    public String getLocaleStr() {
        if (item == null)
            return null;

        return item.getCustomLanguage();
    }

    public String getLang() {
        if (item == null)
            return null;

        return item.getNameCustomLanguage();
    }

    public void reinit() {
    }
}
