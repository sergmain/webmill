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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import org.riverock.interfaces.portal.bean.SiteLanguage;
import org.riverock.interfaces.portlet.menu.MenuLanguage;
import org.riverock.webmill.portal.dao.InternalDaoFactory;

/**
 * $Id$
 *
 *
 * return menu for all supported languages for this site
 */
public final class SiteMenu {
    private final static Logger log = Logger.getLogger( SiteMenu.class );

    private List<MenuLanguage> menuLanguage = new ArrayList<MenuLanguage>();
    private final static Map<Long, SiteMenu> siteMenuLaguage = new HashMap<Long, SiteMenu>();

    public List<MenuLanguage> getMenuLanguage() {
        return menuLanguage;
    }

    protected void finalize() throws Throwable {
        if (menuLanguage != null) {
            menuLanguage.clear();
            menuLanguage = null;
        }

        super.finalize();
    }

    public SiteMenu(){}

    public static SiteMenu getInstance( final Long idSite_) {

        SiteMenu tempLangMenu = siteMenuLaguage.get( idSite_ );

        if (tempLangMenu!=null)
            return tempLangMenu;

        synchronized(siteMenuLaguage) {
            SiteMenu temp = new SiteMenu( idSite_ );
            siteMenuLaguage.put( idSite_,  temp);
            return temp;
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

    private SiteMenu( final Long idSite) {
        if (log.isDebugEnabled()) {
            log.debug("#33.60.00. Get List of language for siteId " + idSite );
        }

        List<SiteLanguage> list = InternalDaoFactory.getInternalSiteLanguageDao().getSiteLanguageList( idSite );

        if (log.isDebugEnabled()) {
            log.debug("Count of language for this site is "+list.size());
        }

        for (SiteLanguage bean : list) {
            menuLanguage.add(new PortalMenuLanguage(bean));
        }
    }

    public void reinit() {
        if (siteMenuLaguage != null) {
            synchronized (siteMenuLaguage) {
                siteMenuLaguage.clear();
            }
        }
    }

    public void terminate(final Long id_) {
        if (siteMenuLaguage != null) {
            synchronized (siteMenuLaguage) {
                siteMenuLaguage.clear();
            }
        }
    }
}
