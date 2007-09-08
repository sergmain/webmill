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
    private SiteLanguage siteLanguage = null;
    private boolean isNotInited=true;
    private ClassLoader classLoader;

    public PortalMenuLanguage(){}

    public void destroy() {
        this.siteLanguage = null;
        if (menu!=null) {
            for (Menu menu1 : menu) {
                ((PortalMenu)menu1).destroy();
            }
            menu.clear();
            menu=null;
        }
        classLoader=null;
    }

    // return name of template for 'index' page
    public MenuItem getIndexMenuItem(){
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
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
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }

    // return name of template for 'index' page
    public String getIndexTemplate(){
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            if (isNotInited) {
                initMenus();
            }
            MenuItem indexTemplate = getIndexMenuItem();
            if (indexTemplate != null) {
                return indexTemplate.getNameTemplate();
            }

            return null;
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }

    public Menu getDefault() {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            if (isNotInited) {
                initMenus();
            }
            for (Menu catalog : this.menu) {
                if (catalog.getIsDefault())
                    return catalog;
            }
            return null;
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }

    public Menu getCatalogByCode( String code ) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
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
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }

    public MenuItem searchMenuItem(Long id) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
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
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }

    public PortalMenuLanguage(ClassLoader classLoader, SiteLanguage bean) {
        if (bean == null) {
            return;
        }

        this.classLoader = classLoader;
        this.siteLanguage = bean;

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
        List<CatalogLanguageItem> list = InternalDaoFactory.getInternalCatalogDao().getCatalogLanguageItemList( siteLanguage.getSiteLanguageId() );

        for (CatalogLanguageItem catalogLanguageBean : list) {
            Menu catalog = new PortalMenu(siteLanguage.getSiteId(), catalogLanguageBean);
            menu.add(catalog);
        }
    }

    public String getLocaleStr() {
        if (siteLanguage == null) {
            return null;
        }

        return siteLanguage.getCustomLanguage();
    }

    public String getLang() {
        if (siteLanguage == null) {
            return null;
        }

        return siteLanguage.getNameCustomLanguage();
    }

    public void reinit() {
    }
}
