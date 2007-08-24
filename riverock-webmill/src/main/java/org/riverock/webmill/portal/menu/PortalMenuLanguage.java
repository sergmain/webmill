/*
 * org.riverock.webmill - Webmill portal with support jsr-168, xml/xslt and others things.
 * For more information, please visit project site http://webmill.riverock.org
 *
 * Copyright (C) 2000-2006, Riverock Software, All Rights Reserved.
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
package org.riverock.webmill.portal.menu;

import java.util.List;
import java.util.ArrayList;

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

    private List<Menu> menu = null;
    private SiteLanguage item = null;
    private boolean isNotInited=true;

    public PortalMenuLanguage(){}

    public void destroy() {
        this.item = null;
        if (menu!=null) {
            for (Menu menu1 : menu) {
                ((PortalMenu)menu1).destroy();
            }
            menu.clear();
            menu=null;
        }
    }

    // return name of template for 'index' page
    public MenuItem getIndexMenuItem(){
        if (isNotInited) {
            initMenus();
        }
        if (log.isDebugEnabled()){
            log.debug("menu: "+menu);
        }

        for (Menu catalog : this.menu) {
            MenuItem item = catalog.getIndexMenuItem();
            if (item != null) {
                return item;
            }
        }
        return null;
    }

    // return name of template for 'index' page
    public String getIndexTemplate(){
        if (isNotInited) {
            initMenus();
        }
        MenuItem indexTemplate = getIndexMenuItem();
        if (indexTemplate != null) {
            return indexTemplate.getNameTemplate();
        }

        return null;
    }

    public Menu getDefault() {
        if (isNotInited) {
            initMenus();
        }
        for (Menu catalog : this.menu) {
            if (catalog.getIsDefault())
                return catalog;
        }
        return null;
    }

    public Menu getCatalogByCode( String code ) {
        if (code==null) {
            return null;
        }

        if (isNotInited) {
            initMenus();
        }
        for (Menu catalog : this.menu) {
            if (code.equals(catalog.getCatalogCode()))
                return catalog;
        }
        return null;
    }

    public MenuItem searchMenuItem(Long id) {
        if (id==null) {
            return null;
        }

        if (isNotInited) {
            initMenus();
        }
        for (Menu menu : this.menu) {
            MenuItem ci = menu.searchMenuItem(id);
            if (ci != null) {
                return ci;
            }
        }
        return null;
    }

    public PortalMenuLanguage(SiteLanguage bean) {
        if (bean == null) {
            return;
        }

        this.item = bean;

        if (log.isDebugEnabled()) {
            log.debug("#33.07.00 ");
            log.debug("ID_SITE_SUPPORT_LANGUAGE -  " + bean.getSiteLanguageId() );
            log.debug("locale - " + getLocaleStr());
            log.debug("language - " + getLang());
        }

    }

    private synchronized void initMenus() {
        if (!isNotInited) {
            return;
        }
        this.menu = new ArrayList<Menu>();
        List<CatalogLanguageItem> list = InternalDaoFactory.getInternalCatalogDao().getCatalogLanguageItemList( item.getSiteLanguageId() );

        for (CatalogLanguageItem catalogLanguageBean : list) {
            Menu catalog = new PortalMenu(catalogLanguageBean);
            menu.add(catalog);
        }
    }

    public String getLocaleStr() {
        if (item == null) {
            return null;
        }

        return item.getCustomLanguage();
    }

    public String getLang() {
        if (item == null) {
            return null;
        }

        return item.getNameCustomLanguage();
    }

    public void reinit() {
    }
}
