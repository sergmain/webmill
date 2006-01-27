/*
 * org.riverock.webmill -- Portal framework implementation
 * 
 * Copyright (C) 2004, Riverock Software, All Rights Reserved.
 * 
 * Riverock -- The Open-source Java Development Community
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
 *
 */
package org.riverock.webmill.portal.menu;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import org.riverock.interfaces.portlet.menu.Menu;
import org.riverock.interfaces.portlet.menu.MenuItem;
import org.riverock.interfaces.portlet.menu.MenuLanguage;
import org.riverock.sql.cache.SqlStatement;
import org.riverock.sql.cache.SqlStatementRegisterException;
import org.riverock.webmill.core.GetWmPortalCatalogLanguageWithIdSiteSupportLanguageList;
import org.riverock.webmill.portal.bean.CatalogLanguageBean;
import org.riverock.webmill.portal.bean.SiteLanguageBean;
import org.riverock.webmill.portal.dao.PortalDaoFactory;

/**
 * $Id$
 */
public final class PortalMenuLanguage implements MenuLanguage {
    private final static Logger log = Logger.getLogger( PortalMenuLanguage.class );

    static{
        Class c = PortalMenuLanguage.class;
        try{
            SqlStatement.registerRelateClass( c, PortalMenu.class );
            SqlStatement.registerRelateClass( c, GetWmPortalCatalogLanguageWithIdSiteSupportLanguageList.class );
        }
        catch( Exception exception ) {
            final String es = "Exception in SqlStatement.registerRelateClass()";
            log.error( es, exception );
            throw new SqlStatementRegisterException( es, exception );
        }
    }

    private List<Menu> menu = new ArrayList<Menu>();
    private SiteLanguageBean item = null;

    public PortalMenuLanguage(){}

    // return name of template for 'index' page
    public MenuItem getIndexMenuItem(){
        if (log.isDebugEnabled()){
            log.debug("menu: "+menu);
        }

        Iterator<Menu> iterator = menu.iterator();
        while (iterator.hasNext()) {
            Menu catalog = iterator.next();
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
        Iterator<Menu> iterator = menu.iterator();
        while (iterator.hasNext()) {
            Menu catalog = iterator.next();
            if (catalog.getIsDefault())
                return catalog;
        }
        return null;
    }

    public Menu getCatalogByCode( String code ) {
        if (code==null)
            return null;

        Iterator<Menu> iterator = menu.iterator();
        while (iterator.hasNext()) {
            Menu catalog = iterator.next();
            if (code.equals( catalog.getCatalogCode()) )
                return catalog;
        }
        return null;
    }

    public MenuItem searchMenuItem(Long id)
    {
        if (id==null)
            return null;

        Iterator<Menu> iterator = menu.iterator();
        while (iterator.hasNext()) {
            Menu menu = iterator.next();
            MenuItem ci = menu.searchMenuItem(id);
            if (ci!=null) {
                return ci;
            }
        }
        return null;
    }

    public PortalMenuLanguage(SiteLanguageBean bean) {
        if (bean == null)
            return;

        this.item = bean;

        if (log.isDebugEnabled()) {
            log.debug("#33.07.00 ");
            log.debug("ID_SITE_SUPPORT_LANGUAGE -  " + bean.getSiteLanguageId() );
            log.debug("locale - " + getLocaleStr());
            log.debug("language - " + getLang());
        }

        List<CatalogLanguageBean> list = PortalDaoFactory.getPortalDao().getCatalogLanguageList( bean.getSiteLanguageId() );

        Iterator<CatalogLanguageBean> iterator = list.iterator();
        while (iterator.hasNext()) {
            CatalogLanguageBean catalogLanguageBean = iterator.next();
            Menu catalog = new PortalMenu( catalogLanguageBean );
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
