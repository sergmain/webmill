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
package org.riverock.webmill.port;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;
import org.riverock.common.tools.MainTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.site.SiteListSite;
import org.riverock.sql.cache.SqlStatement;
import org.riverock.webmill.config.WebmillConfig;
import org.riverock.webmill.core.GetSiteListSiteItem;
import org.riverock.webmill.core.GetSiteSupportLanguageWithIdSiteList;
import org.riverock.webmill.portal.menu.MenuLanguageInterface;
import org.riverock.webmill.portal.menu.SiteMenu;
import org.riverock.webmill.schema.core.SiteListSiteItemType;
import org.riverock.webmill.schema.core.SiteSupportLanguageItemType;
import org.riverock.webmill.schema.core.SiteSupportLanguageListType;
import org.riverock.webmill.site.SiteTemplateList;

public class PortalInfo
{
    static
    {
        Class p = new PortalInfo().getClass();
        SqlStatement.registerRelateClass( p, new GetSiteListSiteItem().getClass());
        SqlStatement.registerRelateClass( p, new PortalXsltList().getClass());
        SqlStatement.registerRelateClass( p, new SiteTemplateList().getClass());
        SqlStatement.registerRelateClass( p, new GetSiteSupportLanguageWithIdSiteList().getClass());
        SqlStatement.registerRelateClass( p, new SiteMenu().getClass());
    }

    private static Logger log = Logger.getLogger( PortalInfo.class );

    public SiteListSiteItemType sites = new SiteListSiteItemType();
    private String serverName = null;

    private Locale defaultLocale = null;

    private PortalXsltList xsltList = null;
    private SiteTemplateList templates = null;

    public SiteSupportLanguageListType supportLanguage = null;
    private Map supportLanguageMap = null;
    private Map languageMenuMap = null;

    private Long siteId = null;

    public boolean isCurrentSite(Long idSiteSupportLanguage)
    {
        if (idSiteSupportLanguage==null)
            return false;

        for (int i=0;i<supportLanguage.getSiteSupportLanguageCount(); i++)
        {
            SiteSupportLanguageItemType item = supportLanguage.getSiteSupportLanguage(i);
            if (idSiteSupportLanguage.equals(item.getIdSiteSupportLanguage()) )
                return true;
        }
        return false;
    }

    public Long getIdSupportLanguage( Locale locale )
    {
        if (log.isDebugEnabled())
        {
            log.debug("get idSupportLanguage for locale "+locale.toString());
            log.debug("supportLanguage "+supportLanguage);
        }

        if (supportLanguage==null)
            return null;

        if (supportLanguageMap==null)
        {
            supportLanguageMap = new HashMap(supportLanguage.getSiteSupportLanguageCount());
            for (int i=0;i<supportLanguage.getSiteSupportLanguageCount(); i++)
            {
                SiteSupportLanguageItemType item = supportLanguage.getSiteSupportLanguage(i);
                supportLanguageMap.put(item.getCustomLanguage(), item.getIdSiteSupportLanguage() );
            }
        }

        Long obj  = (Long)supportLanguageMap.get( locale.toString() );
        if (log.isDebugEnabled())
            log.debug("result object "+obj);

        if (obj==null)
            return null;

        return obj;
    }

    protected void finalize() throws Throwable
    {
        if (log.isDebugEnabled())
            log.debug("#15.09.01 finalize");

        defaultLocale = null;
        xsltList = null;
        templates = null;

        super.finalize();
    }

    private static long lastReadData = 0;
    private final static long LENGTH_TIME_PERIOD = 30000;
    private static PortalInfo backupPortalInfo = null;
    private static Object syncObject = new Object();

    public synchronized static PortalInfo getInstance(DatabaseAdapter db_, String serverName)
        throws Exception
    {
        if (log.isDebugEnabled())
        {
            log.debug("#15.01.01 lastReadData: " + lastReadData + ", current " + System.currentTimeMillis());
            log.debug("#15.01.02 LENGTH_TIME_PERIOD " + LENGTH_TIME_PERIOD + ", status " +
                (((System.currentTimeMillis() - lastReadData) > LENGTH_TIME_PERIOD)
                || (backupPortalInfo == null))
            );
        }
        synchronized(syncObject)
        {
            if (((System.currentTimeMillis() - lastReadData) > LENGTH_TIME_PERIOD)
                || (backupPortalInfo == null))
            {
                if (log.isDebugEnabled())
                {
                    log.debug("#15.01.03 reinit cached value ");
                    log.debug("#15.01.04 old value " + backupPortalInfo);
                }

                backupPortalInfo = null;
                backupPortalInfo = new PortalInfo(db_, serverName);

                if (log.isDebugEnabled())
                    log.debug("#15.01.05 new value " + backupPortalInfo);
            }
            else
            {
                if (log.isDebugEnabled())
                    log.debug("Get from cache");
            }

            if (log.isDebugEnabled())
                log.debug("#15.01.09 ret value " + backupPortalInfo);

            lastReadData = System.currentTimeMillis();
            return backupPortalInfo;
        }
    }

    private PortalInfo(DatabaseAdapter db_, String serverName_)
        throws Exception
    {
        this.setServerName(serverName_);
        this.siteId = SiteListSite.getIdSite(serverName_);

        sites = GetSiteListSiteItem.getInstance( db_, siteId ).item;

        if (sites.getDefLanguage()==null)
            sites.setDefLanguage("");

        if (sites.getDefLanguage()!= null && sites.getDefLanguage().length()>0 &&
            sites.getDefCountry() != null && sites.getDefCountry().length()>0 )
        {

            defaultLocale = new Locale(
                sites.getDefLanguage(),
                sites.getDefCountry(),
                sites.getDefVariant()==null?"":sites.getDefVariant()
            );

            if (log.isDebugEnabled())
                log.debug("Main language 1.1: " + getDefaultLocale().toString());

        }
        else
        {
            defaultLocale = MainTools.getLocale(WebmillConfig.getMainLanguage());
            if (log.isDebugEnabled())
            {
                log.debug("Language InitParam.getMainLanguage(): " + WebmillConfig.getMainLanguage());
                log.debug("Language locale: " + getDefaultLocale().toString());
            }
        }

        long mills = 0; // System.currentTimeMillis();

        if (log.isInfoEnabled())
            mills = System.currentTimeMillis();

        xsltList = PortalXsltList.getInstance(db_, siteId);
        if (log.isInfoEnabled())
            log.info("Init xsltList for " + (System.currentTimeMillis() - mills) + " milliseconds");

        if (log.isInfoEnabled())
            mills = System.currentTimeMillis();
        templates = SiteTemplateList.getInstance(db_, siteId);

        if (log.isInfoEnabled())
            log.info("Init templates for "+ (System.currentTimeMillis() - mills) + " milliseconds");

        if (log.isInfoEnabled())
            mills = System.currentTimeMillis();

        supportLanguage = GetSiteSupportLanguageWithIdSiteList.getInstance(db_, siteId).item;
        if (log.isInfoEnabled())
            log.info("init currency list for "+(System.currentTimeMillis()-mills)+" milliseconds");

        initMenu(db_);
    }

    public PortalInfo(){}

    public static PortalInfo getInstance(DatabaseAdapter db_, java.lang.Long id_)
    {
        return new PortalInfo();
    }

    public void reinit()
    {
        lastReadData = 0;
    }

    public void terminate(java.lang.Long id_)
    {
        lastReadData = 0;
    }

    /**
     * @deprecated
     * @return
     */
    public String getServerName()
    {
        return serverName;
    }

    public void setServerName(String serverName)
    {
        this.serverName = serverName;
    }

    public Long getSiteId()
    {
        return siteId;
    }

    public Locale getDefaultLocale()
    {
        return defaultLocale;
    }

    public PortalXsltList getXsltList()
    {
        return xsltList;
    }

    public SiteTemplateList getTemplates()
    {
        return templates;
    }

    private void initMenu(DatabaseAdapter db_)
        throws Exception
    {
        if (log.isDebugEnabled())
            log.debug("start get menu for site "+siteId);

        // Build menu
        SiteMenu sc = SiteMenu.getInstance(db_, siteId);
        languageMenuMap = new HashMap(sc.getMenuLanguageCount());
        for (int i = 0; i < sc.getMenuLanguageCount(); i++)
        {
            MenuLanguageInterface tempCat = sc.getMenuLanguage(i);
            languageMenuMap.put(tempCat.getLocaleStr(), tempCat);
        }
    }

    public MenuLanguageInterface getMenu(String locale)
    {
        MenuLanguageInterface tempCat = null;
        if (locale!=null)
            tempCat = (MenuLanguageInterface)languageMenuMap.get(locale);

        if (tempCat!=null)
            return tempCat;

        log.warn("Menu for locale "+locale+" not found");
        log.warn("Dump menu");
//        for (int i = 0; i < sc.getMenuLanguageCount(); i++)
//        {
//            tempCat = sc.getMenuLanguage(i);
//            log.warn("MenuLanguage: "+tempCat.getLocaleStr()+", lang "+tempCat.getLang());
//        }
        return null;
    }
}
