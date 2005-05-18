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
package org.riverock.portlet.news;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.MissingResourceException;

import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.PortletConfig;

import org.riverock.common.tools.RsetTools;
import org.riverock.common.tools.StringTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.exception.GenericException;
import org.riverock.generic.site.SiteListSite;
import org.riverock.portlet.schema.portlet.news_block.NewsBlockType;
import org.riverock.portlet.schema.portlet.news_block.NewsGroupType;
import org.riverock.portlet.schema.portlet.news_block.NewsItemType;
import org.riverock.portlet.schema.portlet.news_block.NewsSiteType;

import org.riverock.interfaces.portlet.member.PortletGetList;
import org.riverock.webmill.portlet.PortletResultObject;
import org.riverock.webmill.portlet.PortletResultContent;
import org.riverock.webmill.portlet.PortletTools;
import org.riverock.sql.cache.SqlStatement;
import org.riverock.sql.cache.SqlStatementRegisterException;

import org.apache.log4j.Logger;

/**
 *
 * $Id$
 *
 */
public final class NewsSite implements PortletGetList, PortletResultObject {
    private final static Logger log = Logger.getLogger( NewsSite.class );

    public final static String NAME_ID_NEWS_PARAM = "mill.id_news_item";
    public final static String CTX_TYPE_NEWS_BLOCK = "mill.news_block";
    public final static String NEWS_TYPE = "news.type";  // 'item' or 'block'. default is 'block'
    public final static String NEWS_TYPE_ITEM = "item";  // 'item' type

    static{
        Class c = new NewsSite().getClass();
        try{
            SqlStatement.registerRelateClass( c, new NewsGroup().getClass());
            SqlStatement.registerRelateClass( c, new SiteListSite().getClass() );
        }
        catch( Throwable exception ) {
            final String es = "Exception in SqlStatement.registerRelateClass()";
            log.error( es, exception );
            throw new SqlStatementRegisterException( es, exception );
        }
    }

    private static Map newsMap = new HashMap(); // Map of NewsSiteType
    private Long siteId=null;
    private RenderRequest renderRequest = null;
    private RenderResponse renderResponse = null;
    private PortletConfig portletConfig = null;

    public void setParameters( final RenderRequest renderRequest, final RenderResponse renderResponse, final PortletConfig portletConfig ) {
        this.renderRequest = renderRequest;
        this.renderResponse = renderResponse;
        this.portletConfig = portletConfig;
    }

    protected void finalize() throws Throwable {
        siteId = null;
        super.finalize();
    }

    public NewsSite(){}

    public void reinit() {
        synchronized(syncObject){
            newsMap.clear();
        }
        lastReadData = 0;
    }

    public void terminate( Long id_ ) {
        synchronized(syncObject){
            newsMap.clear();
        }
        lastReadData = 0;
    }

    private static long lastReadData = 0;
    private final static long LENGTH_TIME_PERIOD = 10000;
    private static Object syncObject = new Object();
    private NewsSiteType checkInit(DatabaseAdapter db_, PortletRequest portletRequest) throws PortletException{

        try {
            siteId = SiteListSite.getIdSite( portletRequest.getServerName() );
        } catch (GenericException e) {
            throw new PortletException("Error get siteId for serverName '"+portletRequest.getServerName()+"'", e);
        }
        if (log.isDebugEnabled()) log.debug("serverName: '"+portletRequest.getServerName()+"', siteId: "+siteId+", newsMap: "+newsMap);


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

    private void initNewsItem( NewsBlockType newsBlock ) throws PortletException {

        if (newsBlock == null)
            return;
        
        String nextNews = null;
        try
        {
            ResourceBundle bundle = portletConfig.getResourceBundle( renderRequest.getLocale() );
            if ( log.isDebugEnabled() ) {
                log.debug( "resource bundle: " + bundle );
            }
            try {
                nextNews = bundle.getString( "main.next-news" );
            }
            catch( MissingResourceException e ) {}

            if (nextNews==null)
                nextNews = bundle.getString( "main.next" );
        }
        catch (MissingResourceException e)
        {
            log.error("Error get localized string", e);
            nextNews = "error";
        }

        try
        {
            for (int j=0; j<newsBlock.getNewsGroupCount(); j++)
            {
                NewsGroupType newsGroupType = newsBlock.getNewsGroup(j);
                for (int i=0; i<newsGroupType.getNewsItemCount(); i++)
                {
                    NewsItemType item = newsGroupType.getNewsItem( i );
                    item.setToFullItem( nextNews );


                    item.setUrlToFullNewsItem(
                        PortletTools.urlStringBuffer( portletConfig.getPortletName(), renderRequest, renderResponse ).
                        append( NAME_ID_NEWS_PARAM ).append( '=' ).append( item.getNewsItemId() ).append( '&' ).
                        append( NEWS_TYPE ).append( "=item" ).
                        toString()
                    );
                }
            }
        }
        catch (Throwable e)
        {
            String es = "Exception in initNewsItem( NewsBlockType newsBlock )";
            log.error(es, e);
            throw new PortletException(es, e);
        }
    }

    static String sql_ = null;
    static {
        sql_ =
            "select a.*, c.CUSTOM_LANGUAGE " +
            "from MAIN_LIST_NEWS a, SITE_SUPPORT_LANGUAGE c " +
            "where c.ID_SITE=? and a.IS_DELETED=0 and " +
            "a.ID_SITE_SUPPORT_LANGUAGE=c.ID_SITE_SUPPORT_LANGUAGE " +
            "order by a.ORDER_FIELD ASC";

        try {
            SqlStatement.registerSql( sql_, new NewsSite().getClass() );
        }
        catch( Throwable e ) {
            final String es = "Exception in registerSql, sql\n" + sql_;
            log.error( es, e );
            throw new SqlStatementRegisterException( es, e );
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
                String codeLanguage = StringTools.getLocale(
                    RsetTools.getString(rs, "CUSTOM_LANGUAGE" ) ).toString();

                if (log.isDebugEnabled())
                    log.debug("lang of news group. idSiteLanguage " + idSiteLanguage + " code " + codeLanguage);

                boolean isLangExists = false;
                NewsBlockType nb = null;
                if (log.isDebugEnabled())
                    log.debug("Start check lang of news group.");

                for (int i=0; i<newsSite.getNewsBlockCount(); i++) {
                    nb = newsSite.getNewsBlock(i);
                    if (log.isDebugEnabled())
                        log.debug("Check lang of news group. idLanguage " + nb.getIdSiteLanguage() );

                    if ( idSiteLanguage.equals( nb.getIdSiteLanguage()) ) {
                        isLangExists = true;
                        break;
                    }
                }

                if (log.isDebugEnabled())
                    log.debug("End check lang of news group. isLangExist " + isLangExists);

                if ( isLangExists ) {
                    nb.addNewsGroup( newsGroup.newsGroup );
                }
                else {
                    if (log.isDebugEnabled())
                        log.debug("End check lang of news group. isLangExist " + isLangExists);

                    NewsBlockType newsBlock = new NewsBlockType();

                    newsBlock.addNewsGroup( newsGroup.newsGroup );
                    newsBlock.setIdSiteLanguage( idSiteLanguage );
                    newsBlock.setCodeLanguage( codeLanguage );
                    newsSite.addNewsBlock( newsBlock );
                }
            }

            if (log.isDebugEnabled()) {
                log.debug("count of block - "+newsSite.getNewsBlockCount());
                for (int i=0; i<newsSite.getNewsBlockCount(); i++) {
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

    public PortletResultContent getInstance(DatabaseAdapter db__, Long id) throws PortletException {
        if (log.isDebugEnabled()) log.debug("getInstance(DatabaseAdapter db__, Long id)");
        return getInstance(db__);
    }

    public PortletResultContent getInstance(DatabaseAdapter db_) throws PortletException {

        if (log.isDebugEnabled()) {
            log.debug("getInstance(DatabaseAdapter db__)");
            log.debug("param.renderRequest.getLocale().toString() - " + renderRequest.getLocale().toString());
        }

        NewsSiteType newsSite = checkInit( db_, renderRequest);

        if (log.isDebugEnabled()) log.debug("newsSite.newsBlockCount() - " + newsSite.getNewsBlockCount());

        NewsBlockType newsBlock = new NewsBlockType();
        for (int i=0; i< newsSite.getNewsBlockCount(); i++)
        {
            NewsBlockType nb = newsSite.getNewsBlock(i);

            if (log.isDebugEnabled()) {
               log.debug("NewsBlockType.getCodeLanguage() - " + nb.getCodeLanguage());
            }

            if (nb.getCodeLanguage().equals( renderRequest.getLocale().toString() ))
            {
                initNewsItem( nb );
                for (int j=0; j<nb.getNewsGroupCount(); j++)
                {
                    NewsGroupType newsGroupType = nb.getNewsGroup(j);

                    if (log.isDebugEnabled()) {
                        log.debug("newsGroupType.getNewsGroupCode() - "+newsGroupType.getNewsGroupCode());
                    }

                    newsBlock.addNewsGroup( newsGroupType );
                }
                break;
            }
        }
        return new NewsBlock(newsBlock);
    }

    public PortletResultContent getInstanceByCode(DatabaseAdapter db_, String portletCode_)
        throws PortletException {

        if (log.isDebugEnabled()) {
            log.debug("getInstanceByCode(DatabaseAdapter db__, String portletCode_)");
            log.debug("code - " + portletCode_);
            log.debug("param.renderRequest.getLocale().toString() - " + renderRequest.getLocale().toString());
        }

        NewsBlockType newsBlock = new NewsBlockType();
        if ( portletCode_!=null)
        {
            log.debug("Start checkInit()");
            NewsSiteType newsSite = checkInit( db_, renderRequest);

            if (log.isDebugEnabled()) log.debug("newsSite.newsBlockCount() - " + newsSite.getNewsBlockCount());

            for (int i=0; i< newsSite.getNewsBlockCount(); i++)
            {
                NewsBlockType nb = newsSite.getNewsBlock(i);

                if (log.isDebugEnabled()) log.debug("NewsBlockType.getCodeLanguage() - " + nb.getCodeLanguage());

                if (nb.getCodeLanguage().equals( renderRequest.getLocale().toString() ))
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

        initNewsItem( newsBlock );
        return new NewsBlock(newsBlock);
    }

    public List getList(Long idSiteCtxLangCatalog, Long idContext)
    {
        return null;
    }

}