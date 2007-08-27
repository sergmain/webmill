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

import org.apache.log4j.Logger;
import org.riverock.interfaces.portal.bean.SiteLanguage;
import org.riverock.interfaces.portlet.menu.MenuLanguage;
import org.riverock.webmill.portal.dao.InternalDaoFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * $Id$
 *
 *
 * return menu for all supported languages for this site
 */
public final class SiteMenu implements Observer {
    private final static Logger log = Logger.getLogger( SiteMenu.class );

    private List<MenuLanguage> menuLanguage = new ArrayList<MenuLanguage>();
    /**
     * as a key used siteId
     */
    private final static Map<Long, SiteMenu> siteMenuLaguage = new ConcurrentHashMap<Long, SiteMenu>();

    public List<MenuLanguage> getMenuLanguage() {
        return menuLanguage;
    }

//    public SiteMenu(){}

    public static void destroyAll() {
        if (siteMenuLaguage!=null) {
            for (SiteMenu siteMenu : siteMenuLaguage.values()) {
                siteMenu.destroy();
            }
            siteMenuLaguage.clear();
        }
    }

    public void destroy() {
        if (menuLanguage!=null) {
            for (MenuLanguage menuLanguage : this.menuLanguage) {
                ((PortalMenuLanguage)menuLanguage).destroy();
            }
            menuLanguage.clear();
            menuLanguage=null;
        }
    }

    private final static Object syncObject = new Object();

    public static SiteMenu getInstance( final Long siteId) {

        SiteMenu tempLangMenu = siteMenuLaguage.get( siteId );

        if (tempLangMenu!=null) {
            return tempLangMenu;
        }

        synchronized(syncObject) {
            tempLangMenu = siteMenuLaguage.get( siteId );
            if (tempLangMenu!=null) {
                return tempLangMenu;
            }
            SiteMenu temp = new SiteMenu( siteId );
            InternalDaoFactory.getInternalCatalogDao().addObserver(temp);
            siteMenuLaguage.put( siteId,  temp);
            return temp;
        }
    }

    public static void invalidateCache(Long siteLanguageId) {
        if (siteLanguageId==null) {
            return;
        }
        synchronized (syncObject) {
            SiteLanguage siteLanguage = InternalDaoFactory.getInternalSiteLanguageDao().getSiteLanguage(siteLanguageId);
            if (log.isDebugEnabled()) {
                log.debug("Invalidate portalInfo for siteLanguageId: " + siteLanguageId+", siteLanguage bean: " + siteLanguage);
            }
            if (siteLanguage==null) {
                return;
            }
            siteMenuLaguage.remove(siteLanguage.getSiteId());
        }
    }

    public MenuLanguage getMenuLanguage( final String localeName) {

        for (MenuLanguage tempCat : menuLanguage) {
            if (tempCat == null || tempCat.getLocaleStr() == null)
                continue;

            if (tempCat.getLocaleStr().equals(localeName))
                return tempCat;
        }

        log.warn("Menu for locale "+localeName+" not found");
        return new PortalMenuLanguage();
    }

    private SiteMenu( final Long siteId) {
        if (log.isDebugEnabled()) {
            log.debug("#33.60.00. Get List of language for siteId " + siteId );
        }

        List<SiteLanguage> list = InternalDaoFactory.getInternalSiteLanguageDao().getSiteLanguageList( siteId );

        if (log.isDebugEnabled()) {
            log.debug("Count of language for this site is "+list.size());
        }

        for (SiteLanguage bean : list) {
            menuLanguage.add(new PortalMenuLanguage(bean));
        }
    }

    public void update(Observable o, Object arg) {
        
    }
}
