/*
 * org.riverock.portlet -- Portlet Library
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
 *
 * $Id$
 *
 */
package org.riverock.portlet.portlets;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.riverock.common.tools.RsetTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.site.SiteListSite;
import org.riverock.generic.exception.GenericException;
import org.riverock.portlet.main.Constants;
import org.riverock.portlet.schema.portlet.news_block.NewsBlockType;
import org.riverock.portlet.schema.portlet.news_block.NewsGroupType;
import org.riverock.portlet.schema.portlet.news_block.NewsItemType;
import org.riverock.portlet.schema.portlet.news_block.NewsSiteType;
import org.riverock.webmill.portlet.CtxInstance;
import org.riverock.webmill.portlet.Portlet;
import org.riverock.webmill.portlet.PortletGetList;
import org.riverock.webmill.portlet.PortletParameter;
import org.riverock.webmill.portlet.PortletResultObject;

import javax.portlet.PortletException;

public class NewsSite implements Portlet, PortletGetList
{
    private static Logger log = Logger.getLogger( NewsSite.class );

    private static Map newsMap = new HashMap();

    private Long siteId=null;
    private PortletParameter param = null;

    protected void finalize() throws Throwable
    {
        param = null;
        super.finalize();
    }

    public void setParameter(PortletParameter param_)
    {
        this.param = param_;
    }

    public NewsSite(){}

    public void reinit()
    {
        synchronized(syncObject){
            newsMap.clear();
        }
        lastReadData = 0;
    }

    public void terminate(java.lang.Long id_)
    {
        synchronized(syncObject){
            newsMap.clear();
        }
        lastReadData = 0;
    }

    private static long lastReadData = 0;
    private final static long LENGTH_TIME_PERIOD = 10000;
    private static Object syncObject = new Object();

    private NewsSiteType checkInit(DatabaseAdapter db_, CtxInstance ctxInstance) throws PortletException{

        try {
            siteId = SiteListSite.getIdSite( ctxInstance.getPortletRequest().getServerName() );
        } catch (GenericException e) {
            throw new PortletException("Error get siteId for serverName '"+ctxInstance.getPortletRequest().getServerName()+"'", e);
        }
        if (log.isDebugEnabled()) log.debug("serverName '"+ctxInstance.getPortletRequest().getServerName()+"', siteId: "+siteId);


        NewsSiteType news = null;
        if (siteId!=null)
            news = (NewsSiteType)newsMap.get(siteId);

        synchronized(syncObject)
        {
            if (((System.currentTimeMillis() - lastReadData) > LENGTH_TIME_PERIOD)
                || news == null)
            {
                news = initNews( db_ );
                newsMap.put(siteId, news);
            }
            else
                if (log.isDebugEnabled()) log.debug("Get from cache");
        }
        lastReadData = System.currentTimeMillis();
        return news;
    }

    private void initNewsItem(NewsBlockType newsBlock, CtxInstance ctxInstance)
        throws PortletException {
        if (newsBlock == null)
            return;

        String str = null;
        try
        {
            if (ctxInstance.getStringManager().checkKey("main.next-news"))
                str = ctxInstance.getStringManager().getStr("main.next-news");
            else
                str = ctxInstance.getStringManager().getStr("main.next");

        }
        catch (Exception e)
        {
            log.error("Error get localized string", e);
            str = "error";
        }

        try
        {
            for (int j=0; j<newsBlock.getNewsGroupCount(); j++)
            {
                NewsGroupType newsGroupType = newsBlock.getNewsGroup(j);
                for (int i=0; i<newsGroupType.getNewsItemCount(); i++)
                {
                    NewsItemType item = newsGroupType.getNewsItem( i );
                    item.setToFullItem( str );

                    item.setUrlToFullNewsItem(
                        ctxInstance.url(Constants.CTX_TYPE_NEWS, null) + '&' +
                        Constants.NAME_ID_NEWS_PARAM + '=' + item.getNewsItemId()
                    );
                }
            }
        }
        catch (Throwable e)
        {
            String es = "Exception in initNewsItem(NewsBlockType newsBlock, CtxInstance ctxInstance)";
            log.error(es, e);
            throw new PortletException(es, e);
        }
    }

    static
    {
        try
        {
            org.riverock.sql.cache.SqlStatement.registerRelateClass( new NewsSite().getClass(), new NewsGroup().getClass());
        }
        catch (Exception exception)
        {
            log.error("Exception in ", exception);
        }
    }

    static String sql_ = null;
    static
    {
        sql_ =
            "select a.*, c.CUSTOM_LANGUAGE " +
            "from MAIN_LIST_NEWS a, SITE_SUPPORT_LANGUAGE c " +
            "where c.ID_SITE=? and a.IS_DELETED=0 and " +
            "a.ID_SITE_SUPPORT_LANGUAGE=c.ID_SITE_SUPPORT_LANGUAGE " +
            "order by a.ORDER_FIELD ASC";

        try
        {
            org.riverock.sql.cache.SqlStatement.registerSql( sql_, new NewsSite().getClass() );
        }
        catch(Exception e)
        {
            log.error("Exception in registerSql, sql\n"+sql_, e);
        }
        catch(Error e)
        {
            log.error("Error in registerSql, sql\n"+sql_, e);
        }
    }

    private NewsSiteType initNews( DatabaseAdapter db_ ) throws PortletException {

        if (log.isDebugEnabled())
            log.debug("start create object");

        PreparedStatement ps = null;
        ResultSet rs = null;
        NewsSiteType newsSite = new NewsSiteType();

        try
        {

            ps = db_.prepareStatement(sql_);
            RsetTools.setLong(ps, 1, siteId);

            rs = ps.executeQuery();
            while (rs.next())
            {
                NewsGroup newsGroup = NewsGroup.getInstance( db_, RsetTools.getLong(rs, "ID_NEWS" ) );

                Long idSiteLanguage = RsetTools.getLong(rs, "ID_SITE_SUPPORT_LANGUAGE" );
                String codeLanguage = RsetTools.getString(rs, "CUSTOM_LANGUAGE" );

                if (log.isDebugEnabled())
                    log.debug("lang of news group. idSiteLanguage " + idSiteLanguage + " code " + codeLanguage);

                boolean isLangExists = false;
                NewsBlockType nb = null;
                if (log.isDebugEnabled()) log.debug("Start check lang of news group.");

                for (int i=0; i<newsSite.getNewsBlockCount(); i++)
                {
                    nb = newsSite.getNewsBlock(i);
                    if (log.isDebugEnabled()) log.debug("Check lang of news group. idLanguage " + nb.getIdSiteLanguage() );

                    if ( nb.getIdSiteLanguage() == idSiteLanguage )
                    {
                        isLangExists = true;
                        break;
                    }
                }

                if (log.isDebugEnabled()) log.debug("End check lang of news group. isLangExist " + isLangExists);

                if ( isLangExists )
                    nb.addNewsGroup( newsGroup.newsGroup );
                else
                {
                    if (log.isDebugEnabled()) log.debug("End check lang of news group. isLangExist " + isLangExists);

                    NewsBlockType newsBlock = new NewsBlockType();

                    newsBlock.addNewsGroup( newsGroup.newsGroup );
                    newsBlock.setIdSiteLanguage( idSiteLanguage );
                    newsBlock.setCodeLanguage( codeLanguage );
                    newsSite.addNewsBlock( newsBlock );
                }
            }

            if (log.isDebugEnabled())
            {
                log.debug("count of block - "+newsSite.getNewsBlockCount());
                for (int i=0; i<newsSite.getNewsBlockCount(); i++)
                {
                    NewsBlockType nb = new NewsBlockType();
                    log.debug("count of group in block - "+ nb.getNewsGroupCount());
                }
            }

            return newsSite;
        }
        catch (Throwable e){
            String es = "Error get news block ";
            log.error(es, e);
            throw new PortletException(es, e);
        }
        finally{
            DatabaseManager.close(rs, ps);
            rs = null;
            ps = null;
        }
    }

    public PortletResultObject getInstance(DatabaseAdapter db__, Long id) throws PortletException {
        if (log.isDebugEnabled()) log.debug("getInstance(DatabaseAdapter db__, Long id)");
        return getInstance(db__);
    }

    public PortletResultObject getInstance(DatabaseAdapter db_) throws PortletException {
        CtxInstance ctxInstance = (CtxInstance)param.getPortletRequest().getPortletSession().getAttribute( org.riverock.webmill.main.Constants.PORTLET_REQUEST_SESSION );
        if (log.isDebugEnabled())
        {
            log.debug("getInstance(DatabaseAdapter db__)");
            log.debug("param.ctxInstance.getPortletRequest().getLocale().toString() - " + param.getPortletRequest().getLocale().toString());
        }

        NewsSiteType newsSite = checkInit( db_, ctxInstance);

        if (log.isDebugEnabled()) log.debug("newsSite.newsBlockCount() - " + newsSite.getNewsBlockCount());

        NewsBlockType newsBlock = new NewsBlockType();
        for (int i=0; i< newsSite.getNewsBlockCount(); i++)
        {
            NewsBlockType nb = newsSite.getNewsBlock(i);

            if (log.isDebugEnabled()) log.debug("NewsBlockType.getCodeLanguage() - " + nb.getCodeLanguage());

            if (nb.getCodeLanguage().equals( param.getPortletRequest().getLocale().toString() ))
            {
                initNewsItem( nb, ctxInstance );
                for (int j=0; j<nb.getNewsGroupCount(); j++)
                {
                    NewsGroupType newsGroupType = nb.getNewsGroup(j);

                    if (log.isDebugEnabled()) log.debug("newsGroupType.getNewsGroupCode() - "+newsGroupType.getNewsGroupCode());

                    newsBlock.addNewsGroup( newsGroupType );
                }
                break;
            }
        }
        return new NewsBlock(newsBlock);
    }

    public PortletResultObject getInstanceByCode(DatabaseAdapter db_, String portletCode_)
            throws PortletException {

        CtxInstance ctxInstance = (CtxInstance)param.getPortletRequest().getPortletSession().getAttribute( org.riverock.webmill.main.Constants.PORTLET_REQUEST_SESSION );
        if (log.isDebugEnabled())
        {
            log.debug("getInstanceByCode(DatabaseAdapter db__, String portletCode_)");
            log.debug("code - " + portletCode_);
            log.debug("param.ctxInstance.getPortletRequest().getLocale().toString() - " + param.getPortletRequest().getLocale().toString());
        }

        NewsBlockType newsBlock = new NewsBlockType();
        if ( portletCode_!=null)
        {
            log.debug("Start checkInit()");
            NewsSiteType newsSite = checkInit( db_, ctxInstance);

            if (log.isDebugEnabled()) log.debug("newsSite.newsBlockCount() - " + newsSite.getNewsBlockCount());

            for (int i=0; i< newsSite.getNewsBlockCount(); i++)
            {
                NewsBlockType nb = newsSite.getNewsBlock(i);

                if (log.isDebugEnabled()) log.debug("NewsBlockType.getCodeLanguage() - " + nb.getCodeLanguage());

                if (nb.getCodeLanguage().equals( param.getPortletRequest().getLocale().toString() ))
                {

                    if (log.isDebugEnabled()) log.debug("nb.getNewsGroupCount() - " + nb.getNewsGroupCount());

                    for (int j=0; j<nb.getNewsGroupCount(); j++)
                    {
                        NewsGroupType newsGroupType = nb.getNewsGroup(j);

                        if (log.isDebugEnabled()) log.debug("newsGroupType.getNewsGroupCode() - "+newsGroupType.getNewsGroupCode());

                        if (portletCode_.equals( newsGroupType.getNewsGroupCode()))
                        {
                            newsBlock.addNewsGroup( newsGroupType );
                            break;
                        }
                    }
                }
            }
        }

        initNewsItem( newsBlock, ctxInstance );
        return new NewsBlock(newsBlock);
    }

    public List getList(Long idSiteCtxLangCatalog, Long idContext)
    {
        return null;
    }

}