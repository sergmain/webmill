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



import java.util.Hashtable;

import java.util.Locale;



import org.apache.log4j.Logger;



import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.webmill.core.GetSiteListSiteItem;

import org.riverock.webmill.core.GetSiteSupportLanguageWithIdSiteList;

import org.riverock.webmill.schema.core.SiteListSiteItemType;

import org.riverock.webmill.schema.core.SiteSupportLanguageItemType;

import org.riverock.webmill.schema.core.SiteSupportLanguageListType;

import org.riverock.webmill.schema.site.SiteTemplate;

import org.riverock.generic.site.SiteListSite;

import org.riverock.webmill.site.SiteTemplateList;

import org.riverock.webmill.site.SiteTemplateMember;

import org.riverock.webmill.config.WebmillConfig;

import org.riverock.common.tools.MainTools;

import org.riverock.generic.db.DatabaseAdapter;



public class PortalInfo

{

    static

    {

        Class p = new PortalInfo().getClass();

        org.riverock.sql.cache.SqlStatement.registerRelateClass( p, new GetSiteListSiteItem().getClass());

        org.riverock.sql.cache.SqlStatement.registerRelateClass( p, new PortalXsltList().getClass());

        org.riverock.sql.cache.SqlStatement.registerRelateClass( p, new SiteTemplateList().getClass());

        org.riverock.sql.cache.SqlStatement.registerRelateClass( p, new SiteTemplateMember().getClass());

        org.riverock.sql.cache.SqlStatement.registerRelateClass( p, new GetSiteSupportLanguageWithIdSiteList().getClass());

    }



    private static Logger log = Logger.getLogger( PortalInfo.class );



    public SiteListSiteItemType sites = new SiteListSiteItemType();

    private String serverName = null;



    public Locale defaultLocale = null;



    public PortalXsltList xsltList = null;

    public SiteTemplateList templates = null;

    public SiteTemplateMember memberTemplates = null;



    public SiteSupportLanguageListType supportLanguage = null;

    private Hashtable supportLanguageHash = null;



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



        if (supportLanguageHash==null)

        {

            supportLanguageHash = new Hashtable(supportLanguage.getSiteSupportLanguageCount());

            for (int i=0;i<supportLanguage.getSiteSupportLanguageCount(); i++)

            {

                SiteSupportLanguageItemType item = supportLanguage.getSiteSupportLanguage(i);

                supportLanguageHash.put(item.getCustomLanguage(), item.getIdSiteSupportLanguage() );

            }

        }



        Long obj  = (Long)supportLanguageHash.get( locale.toString() );

        if (log.isDebugEnabled())

            log.debug("result object "+obj);



        if (obj==null)

            return null;



        return obj;

    }



    public SiteTemplate getMemberTemplate()

    {

        SiteTemplate st = null;



        if (log.isDebugEnabled())

            log.debug("Looking template for locale " + defaultLocale.toString());



        if (memberTemplates != null && memberTemplates.memberTemplate != null)

            st = (SiteTemplate) memberTemplates.memberTemplate.get(defaultLocale.toString());



        if (st != null)

            return st;



        if (log.isInfoEnabled())

            log.info("memberTemplate for Locale " + defaultLocale.toString() + " not initialized");



        return new SiteTemplate();

    }



    protected void finalize() throws Throwable

    {

        if (log.isDebugEnabled())

            log.debug("#15.09.01 finalize");



        defaultLocale = null;

        xsltList = null;

        templates = null;

        memberTemplates = null;



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

                log.debug("Main language 1.1: " + defaultLocale.toString());



        }

        else

        {

            defaultLocale = MainTools.getLocale(WebmillConfig.getMainLanguage());

            if (log.isDebugEnabled())

            {

                log.debug("Language InitParam.getMainLanguage(): " + WebmillConfig.getMainLanguage());

                log.debug("Language locale: " + defaultLocale.toString());

            }

        }



        long mills = 0; // System.currentTimeMillis();



        if (log.isInfoEnabled())

            mills = System.currentTimeMillis();



        xsltList = PortalXsltList.getInstance(db_, sites.getIdSite());

        if (log.isInfoEnabled())

        {

            log.info("Init xsltList for  "

                + (System.currentTimeMillis() - mills) + " milliseconds");

        }



        if (log.isInfoEnabled())

            mills = System.currentTimeMillis();

        templates = SiteTemplateList.getInstance(db_, sites.getIdSite());

        if (log.isInfoEnabled())

        {

            log.info("Init templates for  "

                + (System.currentTimeMillis() - mills) + " milliseconds");

        }



        if (log.isInfoEnabled())

            mills = System.currentTimeMillis();



        memberTemplates = SiteTemplateMember.getInstance(db_, sites.getIdSite());

        if (log.isInfoEnabled())

        {

            log.info("init member templates for  "

                + (System.currentTimeMillis() - mills) + " milliseconds");

        }

        if (log.isInfoEnabled())

            mills = System.currentTimeMillis();



        if (log.isInfoEnabled())

            mills = System.currentTimeMillis();



        supportLanguage = GetSiteSupportLanguageWithIdSiteList.getInstance(db_, sites.getIdSite()).item;

        if (log.isInfoEnabled())

        {

            log.info("init currency list for "+(System.currentTimeMillis()-mills)+" milliseconds");

        }

    }



    public PortalInfo(){}



// Кусок кода для принудительной переинициализации класса

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



}

