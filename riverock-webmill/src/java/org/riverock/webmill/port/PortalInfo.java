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

import org.apache.log4j.Logger;
import org.riverock.common.tools.MainTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.site.SiteListSite;
import org.riverock.generic.exception.GenericException;
import org.riverock.sql.cache.SqlStatement;
import org.riverock.webmill.config.WebmillConfig;
import org.riverock.webmill.core.GetSiteListSiteItem;
import org.riverock.webmill.core.GetSiteSupportLanguageWithIdSiteList;
import org.riverock.webmill.exception.PortalException;
import org.riverock.webmill.portal.menu.SiteMenu;
import org.riverock.webmill.schema.core.SiteListSiteItemType;
import org.riverock.webmill.schema.core.SiteSupportLanguageItemType;
import org.riverock.webmill.schema.core.SiteSupportLanguageListType;
import org.riverock.webmill.site.SiteTemplateList;
import org.riverock.interfaces.portlet.menu.MenuLanguageInterface;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public final class PortalInfo
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

    private static Map portatInfoMap = new HashMap();

    private static Logger log = Logger.getLogger( PortalInfo.class );

    private SiteListSiteItemType sites = new SiteListSiteItemType();
    private SiteSupportLanguageListType supportLanguage = null;

    private Locale defaultLocale = null;

    private PortalXsltList xsltList = null;
    private SiteTemplateList templates = null;

    private Map supportLanguageMap = null;
    private Map languageMenuMap = null;

    private Long siteId = null;

    public boolean isCurrentSite(Long idSiteSupportLanguage)
    {
        if (idSiteSupportLanguage==null)
            return false;

        for (int i=0;i<getSupportLanguage().getSiteSupportLanguageCount(); i++)
        {
            SiteSupportLanguageItemType item = getSupportLanguage().getSiteSupportLanguage(i);
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
            log.debug("supportLanguage "+getSupportLanguage());
        }

        if (getSupportLanguage()==null)
            return null;

        if (supportLanguageMap==null)
        {
            supportLanguageMap = new HashMap(getSupportLanguage().getSiteSupportLanguageCount());
            for (int i=0;i<getSupportLanguage().getSiteSupportLanguageCount(); i++)
            {
                SiteSupportLanguageItemType item = getSupportLanguage().getSiteSupportLanguage(i);
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
    private static Object syncObject = new Object();
    // Todo replace synchronized with synchronized(syncObject)
    public synchronized static PortalInfo getInstance(DatabaseAdapter db_, String serverName)
        throws PortalException
    {
        if (log.isDebugEnabled())
        {
            log.debug("#15.01.01 lastReadData: " + lastReadData + ", current " + System.currentTimeMillis());
            log.debug("#15.01.02 LENGTH_TIME_PERIOD " + LENGTH_TIME_PERIOD + ", status " +
                ((System.currentTimeMillis() - lastReadData) > LENGTH_TIME_PERIOD)
            );
        }

        Long id=null;
        try {
            id = SiteListSite.getIdSite(serverName);
        } catch (GenericException e) {
            throw new PortalException("Error get siteId for serverName '"+serverName+"'", e);
        }
        if (log.isDebugEnabled()) log.debug("ServerName:"+serverName+", siteId: "+id);

        synchronized(syncObject)
        {

            PortalInfo p=null;
            if (id!=null)
                p = (PortalInfo)portatInfoMap.get(id);

            if ((System.currentTimeMillis()-lastReadData)>LENGTH_TIME_PERIOD || p==null){
                log.debug("#15.01.03 reinit cached value ");

                p = new PortalInfo(db_, id);
                portatInfoMap.put(id, p);
            }
            lastReadData = System.currentTimeMillis();
            return p;
        }
    }

    private PortalInfo(DatabaseAdapter db_, Long siteId) throws PortalException
    {
        this.siteId = siteId;
        long mills = 0; // System.currentTimeMillis();
        try {
            sites = GetSiteListSiteItem.getInstance( db_, siteId ).item;

            if (getSites().getDefLanguage()==null)
                getSites().setDefLanguage("");

            if (getSites().getDefLanguage()!= null && getSites().getDefLanguage().length()>0 &&
                getSites().getDefCountry() != null && getSites().getDefCountry().length()>0 )
            {

                defaultLocale = new Locale(
                    getSites().getDefLanguage(),
                    getSites().getDefCountry(),
                    getSites().getDefVariant()==null?"":getSites().getDefVariant()
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

            mills = 0;

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

        } catch (Throwable e) {
            String es = "Error in PortalInfo(DatabaseAdapter db_, Long siteId)";
            log.error(es, e);
            throw new PortalException(es, e);
        }
        if (log.isInfoEnabled())
            log.info("init currency list for "+(System.currentTimeMillis()-mills)+" milliseconds");

        initMenu(db_);
    }

    public PortalInfo(){}

    public static PortalInfo getInstance(DatabaseAdapter db_, java.lang.Long id_) {
        return new PortalInfo();
    }

    public void reinit() {
        synchronized(syncObject) {
            portatInfoMap.clear();
        }
        lastReadData = 0;
    }

    public void terminate(java.lang.Long id_) {
        synchronized(syncObject) {
            portatInfoMap.clear();
        }
        lastReadData = 0;
    }

    public Long getSiteId() {
        return siteId;
    }

    public Locale getDefaultLocale() {
        return defaultLocale;
    }

    public PortalXsltList getXsltList() {
        return this.xsltList;
    }

    public SiteTemplateList getTemplates() {
        return templates;
    }

    private void initMenu(DatabaseAdapter db_) throws PortalException {
        if (log.isDebugEnabled()) log.debug("start get menu for site "+siteId);

        // Build menu
        SiteMenu sc = SiteMenu.getInstance(db_, siteId);
        languageMenuMap = new HashMap(sc.getMenuLanguageCount());
        for (int i = 0; i < sc.getMenuLanguageCount(); i++){
            MenuLanguageInterface tempCat = sc.getMenuLanguage(i);
            languageMenuMap.put(tempCat.getLocaleStr(), tempCat);
        }
    }

    public MenuLanguageInterface getMenu(String locale) {
        MenuLanguageInterface tempCat = null;
        if (locale!=null)
            tempCat = (MenuLanguageInterface)languageMenuMap.get(locale);

        if (tempCat!=null)
            return tempCat;
        else
            log.warn("Menu for locale "+locale+" not found");

        return null;
    }

    public SiteListSiteItemType getSites() {
        return sites;
    }

    public SiteSupportLanguageListType getSupportLanguage() {
        return supportLanguage;
    }
}
