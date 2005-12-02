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
import java.util.Map;
import java.util.List;
import java.util.Iterator;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.sql.cache.SqlStatement;
import org.riverock.sql.cache.SqlStatementRegisterException;
import org.riverock.webmill.core.GetSiteSupportLanguageWithIdSiteList;
import org.riverock.webmill.schema.core.SiteSupportLanguageItemType;
import org.riverock.webmill.schema.core.SiteSupportLanguageListType;
import org.riverock.webmill.exception.PortalException;
import org.riverock.webmill.port.PortalInfoImpl;
import org.riverock.interfaces.portlet.menu.MenuLanguageInterface;

import org.apache.log4j.Logger;

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
            SqlStatement.registerRelateClass( c, MenuLanguage.class );
            SqlStatement.registerRelateClass( c, GetSiteSupportLanguageWithIdSiteList.class );
        }
        catch( Exception exception ) {
            final String es = "Exception in ";
            log.error( es, exception );
            throw new SqlStatementRegisterException( es, exception );
        }
    }

    private List<MenuLanguageInterface> menuLanguage = new ArrayList<MenuLanguageInterface>();
    private static Map<Long, SiteMenu> siteMenuLaguage = new HashMap<Long, SiteMenu>();

    public List<MenuLanguageInterface> getMenuLanguage() {
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


//    private static long lastReadData = 0;
//    private final static long LENGTH_TIME_PERIOD = 10000;

    public static SiteMenu getInstance( final DatabaseAdapter db_, final Long idSite_) throws PortalException {

//        if ((System.currentTimeMillis() - lastReadData) > LENGTH_TIME_PERIOD ) {
//            log.debug("#15.01.03 reinit cached value ");
//
//            synchronized(siteMenuLaguage) {
//                siteMenuLaguage.clear();
//                lastReadData = System.currentTimeMillis();
//            }
//        }

        SiteMenu tempLangMenu = siteMenuLaguage.get( idSite_ );

        if (tempLangMenu!=null)
            return tempLangMenu;

        synchronized(siteMenuLaguage)
        {
            SiteMenu temp = new SiteMenu(db_, idSite_);
            siteMenuLaguage.put( idSite_,  temp);
            return temp;
        }
    }

    public MenuLanguageInterface getMenuLanguage( final String localeName) {

        Iterator<MenuLanguageInterface> it = menuLanguage.iterator();
        while (it.hasNext()) {
            MenuLanguageInterface tempCat = it.next();

            if (tempCat==null || tempCat.getLocaleStr()==null)
                continue;

            if (tempCat.getLocaleStr().equals( localeName ) )
                return tempCat;
        }

        log.warn("Menu for locale "+localeName+" not found");
        return new MenuLanguage();
    }

    private SiteMenu( final DatabaseAdapter db_, final Long idSite) throws PortalException {
        if (log.isDebugEnabled()) {
            log.debug("#33.60.00 ");
        }

        try {
            SiteSupportLanguageListType list = PortalInfoImpl.processSupportLanguage( db_, idSite );

            if (log.isDebugEnabled()) {
                log.debug("Count of language for this site is "+list.getSiteSupportLanguageCount());
            }

            for (int i=0; i<list.getSiteSupportLanguageCount(); i++) {
                SiteSupportLanguageItemType item = list.getSiteSupportLanguage(i);
                menuLanguage.add( new MenuLanguage(db_, item) );
            }
        }
        catch(Throwable e) {
            String es = "Error in SiteMenu()";
            log.error(es, e);
            throw new PortalException(es, e);
        }
    }

    public static SiteMenu getInstance( final DatabaseAdapter db_, final long id_) throws Exception {
        return getInstance(db_, (Long)id_ );
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
