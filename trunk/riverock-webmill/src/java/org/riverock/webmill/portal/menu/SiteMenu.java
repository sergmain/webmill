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

/**
 * $Id$
 */
package org.riverock.webmill.portal.menu;

import java.util.ArrayList;
import java.util.HashMap;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.sql.cache.SqlStatement;
import org.riverock.webmill.core.GetSiteSupportLanguageWithIdSiteList;
import org.riverock.webmill.schema.core.SiteSupportLanguageItemType;
import org.riverock.webmill.schema.core.SiteSupportLanguageListType;
import org.riverock.webmill.exception.PortalException;

import org.apache.log4j.Logger;

/**
 * return menu for all supported languages for this site
 */
public class SiteMenu
{
    private static Logger log = Logger.getLogger( SiteMenu.class );

    static
    {
        try
        {
            SqlStatement.registerRelateClass( new SiteMenu().getClass(), new MenuLanguage().getClass());
        }
        catch (Exception exception)
        {
            log.error("Exception in ", exception);
        }
    }

//    private static Object syncObject = new Object();

    private ArrayList menuLanguage = new ArrayList(2);  //vector of MenuLanguage
    private static HashMap siteMenuLaguage = new HashMap();

    public int getMenuLanguageCount()
    {
        if (menuLanguage==null)
            return 0;

        return menuLanguage.size();
    }

    public MenuLanguageInterface getMenuLanguage(int index)
            throws java.lang.IndexOutOfBoundsException
    {
        if ((index < 0) || (index > menuLanguage.size())) {
            throw new IndexOutOfBoundsException();
        }

         if (menuLanguage==null)
            return null;

        return (MenuLanguageInterface)menuLanguage.get(index);
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

    public static SiteMenu getInstance(DatabaseAdapter db_, Long idSite_) throws PortalException
    {
        SiteMenu tempLangMenu = (SiteMenu)siteMenuLaguage.get( idSite_ );

        if (tempLangMenu!=null)
            return tempLangMenu;

        synchronized(siteMenuLaguage)
        {
            SiteMenu temp = new SiteMenu(db_, idSite_);
//            if (temp==null)
//                return null;

            siteMenuLaguage.put( idSite_,  temp);
            return temp;

//            SiteMenu tempLangMenu = (SiteMenu)siteMenuLaguage.get( new Long(idSite_));
//            if (tempLangMenu == null)
//            {
//                SiteMenu tempCatalog = new SiteMenu(db_, idSite_);
//                siteMenuLaguage.put(new Long(idSite_),  tempCatalog);
//
//                return tempCatalog;
//            }
//            else
//            {
//                if (log.isDebugEnabled())
//                    log.debug("Get from cache");
//
//                return tempLangMenu;
//            }
        }
    }

    public MenuLanguageInterface getMenuLanguage(String localeName)
    {
        for (int i = 0; i < getMenuLanguageCount(); i++)
        {
            MenuLanguageInterface tempCat = getMenuLanguage(i);

            if (tempCat==null || tempCat.getLocaleStr()==null)
                continue;

            if (tempCat.getLocaleStr().equals( localeName ) )
                return tempCat;
        }

        log.warn("Menu for locale "+localeName+" not found");
        return new MenuLanguage();
    }

    private SiteMenu(DatabaseAdapter db_, Long idSite) throws PortalException{
        if (log.isDebugEnabled())log.debug("#33.60.00 ");

        try {
            SiteSupportLanguageListType list = GetSiteSupportLanguageWithIdSiteList.getInstance(db_, idSite).item;

            if (log.isDebugEnabled()) log.debug("Count of language for this site is "+list.getSiteSupportLanguageCount());

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

    public static SiteMenu getInstance(DatabaseAdapter db_, long id_)
        throws Exception
    {
        return getInstance(db_, new Long(id_) );
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

    public void terminate(java.lang.Long id_)
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
