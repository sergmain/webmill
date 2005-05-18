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

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.sql.cache.SqlStatement;
import org.riverock.sql.cache.SqlStatementRegisterException;
import org.riverock.webmill.core.GetSiteSupportLanguageWithIdSiteList;
import org.riverock.webmill.schema.core.SiteSupportLanguageItemType;
import org.riverock.webmill.schema.core.SiteSupportLanguageListType;
import org.riverock.webmill.exception.PortalException;
import org.riverock.webmill.port.PortalInfo;
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
            Class c = new SiteMenu().getClass();
            SqlStatement.registerRelateClass( c, new MenuLanguage().getClass() );
            SqlStatement.registerRelateClass( c, new GetSiteSupportLanguageWithIdSiteList().getClass() );
        }
        catch( Exception exception ) {
            final String es = "Exception in ";
            log.error( es, exception );
            throw new SqlStatementRegisterException( es, exception );
        }
    }

    private ArrayList menuLanguage = new ArrayList(2);  //List of MenuLanguage
    private static Map siteMenuLaguage = new HashMap();

    public int getMenuLanguageCount()
    {
        if (menuLanguage==null)
            return 0;

        return menuLanguage.size();
    }

    public MenuLanguageInterface getMenuLanguage( final int index )
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

    public static SiteMenu getInstance( final DatabaseAdapter db_, final Long idSite_) throws PortalException {
        SiteMenu tempLangMenu = (SiteMenu)siteMenuLaguage.get( idSite_ );

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

    private SiteMenu( final DatabaseAdapter db_, final Long idSite) throws PortalException {
        if (log.isDebugEnabled()) {
            log.debug("#33.60.00 ");
        }

        try {
            SiteSupportLanguageListType list = PortalInfo.processSupportLanguage( db_, idSite );

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
