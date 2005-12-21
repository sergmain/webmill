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
import java.util.List;
import java.util.Iterator;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.sql.cache.SqlStatement;
import org.riverock.sql.cache.SqlStatementRegisterException;
import org.riverock.webmill.core.GetWmPortalCatalogLanguageWithIdSiteSupportLanguageList;
import org.riverock.webmill.schema.core.WmPortalCatalogLanguageItemType;
import org.riverock.webmill.schema.core.WmPortalCatalogLanguageListType;
import org.riverock.webmill.schema.core.WmPortalSiteLanguageItemType;
import org.riverock.interfaces.portlet.menu.MenuInterface;
import org.riverock.interfaces.portlet.menu.MenuLanguageInterface;
import org.riverock.interfaces.portlet.menu.MenuItemInterface;

import org.apache.log4j.Logger;

/**
 * $Id$
 */
public final class MenuLanguage implements MenuLanguageInterface {
    private final static Logger log = Logger.getLogger( MenuLanguage.class );

    static{
        Class c = new MenuLanguage().getClass();
        try{
            SqlStatement.registerRelateClass( c, new Menu().getClass() );
            SqlStatement.registerRelateClass( c, new GetWmPortalCatalogLanguageWithIdSiteSupportLanguageList().getClass() );
        }
        catch( Exception exception ) {
            final String es = "Exception in SqlStatement.registerRelateClass()";
            log.error( es, exception );
            throw new SqlStatementRegisterException( es, exception );
        }
    }

    private List<MenuInterface> menu = new ArrayList<MenuInterface>();
    private WmPortalSiteLanguageItemType item = null;

    public MenuLanguage(){}

    // return name of template for 'index' page
    public MenuItemInterface getIndexMenuItem(){
        if (log.isDebugEnabled()){
            log.debug("menu: "+menu);
        }

        Iterator<MenuInterface> iterator = menu.iterator();
        while (iterator.hasNext()) {
            MenuInterface catalog = iterator.next();
            MenuItemInterface item = catalog.getIndexMenuItem();
            if (item != null)
                return item;
        }
        return null;
    }

    // return name of template for 'index' page
    public String getIndexTemplate(){
        MenuItemInterface indexTemplate = getIndexMenuItem();
        if (indexTemplate != null)
            return indexTemplate.getNameTemplate();

        return null;
    }

    public MenuInterface getDefault() {
        Iterator<MenuInterface> iterator = menu.iterator();
        while (iterator.hasNext()) {
            MenuInterface catalog = iterator.next();
            if (catalog.getIsDefault())
                return catalog;
        }
        return null;
    }

    public MenuInterface getCatalogByCode( String code ) {
        if (code==null)
            return null;

        Iterator<MenuInterface> iterator = menu.iterator();
        while (iterator.hasNext()) {
            MenuInterface catalog = iterator.next();
            if (code.equals( catalog.getCatalogCode()) )
                return catalog;
        }
        return null;
    }

    public MenuItemInterface searchMenuItem(Long id)
    {
        if (id==null)
            return null;

        Iterator<MenuInterface> iterator = menu.iterator();
        while (iterator.hasNext()) {
            MenuInterface menu = iterator.next();
            MenuItemInterface ci = menu.searchMenuItem(id);
            if (ci!=null) {
                return ci;
            }
        }
        return null;
    }

    public MenuLanguage(DatabaseAdapter db_, WmPortalSiteLanguageItemType item_) throws Exception {
        if (item_ == null)
            return;

        this.item = item_;

        if (log.isDebugEnabled()) {
            log.debug("#33.07.00 ");
            log.debug("ID_SITE_SUPPORT_LANGUAGE -  " + item_.getIdSiteSupportLanguage());
            log.debug("locale - " + getLocaleStr());
            log.debug("language - " + getLang());
        }

        try {
            WmPortalCatalogLanguageListType list =
                GetWmPortalCatalogLanguageWithIdSiteSupportLanguageList.
                getInstance(db_, item_.getIdSiteSupportLanguage())
                .item;

            if (log.isDebugEnabled())
                log.debug("Count of WmPortalCatalogLanguageListType: " + list.getWmPortalCatalogLanguageCount());

            Iterator iterator = list.getWmPortalCatalogLanguageAsReference().iterator();
            while (iterator.hasNext()) {
                WmPortalCatalogLanguageItemType ic = (WmPortalCatalogLanguageItemType) iterator.next();
                MenuInterface catalog = new Menu(db_, ic);
                menu.add(catalog);
            }
        }
        catch (Exception e) {
            log.error("Exception in MenuLanguage(....", e);
            throw e;
        }
        catch (Error e) {
            log.error("Error in MenuLanguage(....", e);
            throw e;
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
