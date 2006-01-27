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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import org.riverock.interfaces.portlet.menu.MenuLanguage;
import org.riverock.sql.cache.SqlStatement;
import org.riverock.sql.cache.SqlStatementRegisterException;
import org.riverock.webmill.core.GetWmPortalSiteLanguageWithIdSiteList;
import org.riverock.webmill.portal.bean.SiteLanguageBean;
import org.riverock.webmill.portal.dao.PortalDaoFactory;

/**
 * $Id$
 *
 *
 * return menu for all supported languages for this site
 */
public final class SiteMenu {
    private final static Logger log = Logger.getLogger( SiteMenu.class );

    static {
        try {
            Class c = SiteMenu.class;
            SqlStatement.registerRelateClass( c, PortalMenuLanguage.class );
            SqlStatement.registerRelateClass( c, GetWmPortalSiteLanguageWithIdSiteList.class );
        }
        catch( Exception exception ) {
            final String es = "Exception in ";
            log.error( es, exception );
            throw new SqlStatementRegisterException( es, exception );
        }
    }

    private List<MenuLanguage> menuLanguage = new ArrayList<MenuLanguage>();
    private static Map<Long, SiteMenu> siteMenuLaguage = new HashMap<Long, SiteMenu>();

    public List<MenuLanguage> getMenuLanguage() {
        return menuLanguage;
    }

    protected void finalize() throws Throwable
    {
        if (menuLanguage != null)
        {
            menuLanguage.clear();
            menuLanguage = null;
        }

        super.finalize();
    }

    public SiteMenu(){}

//    public static SiteMenu getInstance( final long id_) throws Exception {
//        return getInstance( (Long)id_ );
//    }

    public static SiteMenu getInstance( final Long idSite_) {

        SiteMenu tempLangMenu = siteMenuLaguage.get( idSite_ );

        if (tempLangMenu!=null)
            return tempLangMenu;

        synchronized(siteMenuLaguage)
        {
            SiteMenu temp = new SiteMenu( idSite_ );
            siteMenuLaguage.put( idSite_,  temp);
            return temp;
        }
    }

    public MenuLanguage getMenuLanguage( final String localeName) {

        Iterator<MenuLanguage> it = menuLanguage.iterator();
        while (it.hasNext()) {
            MenuLanguage tempCat = it.next();

            if (tempCat==null || tempCat.getLocaleStr()==null)
                continue;

            if (tempCat.getLocaleStr().equals( localeName ) )
                return tempCat;
        }

        log.warn("Menu for locale "+localeName+" not found");
        return new PortalMenuLanguage();
    }

    private SiteMenu( final Long idSite) {
        if (log.isDebugEnabled()) {
            log.debug("#33.60.00 ");
        }

        List<SiteLanguageBean> list = PortalDaoFactory.getPortalDao().getSiteLanguageList( idSite );

        if (log.isDebugEnabled()) {
            log.debug("Count of language for this site is "+list.size());
        }

        Iterator<SiteLanguageBean> iterator = list.iterator();
        while (iterator.hasNext()) {
            SiteLanguageBean bean = iterator.next();
            menuLanguage.add( new PortalMenuLanguage( bean) );
        }
    }

    public void reinit()
    {
        if (siteMenuLaguage!=null)
        {
            synchronized(siteMenuLaguage)
            {
                siteMenuLaguage.clear();
            }
        }
    }

    public void terminate( final Long id_ )
    {
        if (siteMenuLaguage!=null)
        {
            synchronized(siteMenuLaguage)
            {
                siteMenuLaguage.clear();
            }
        }
    }
}
