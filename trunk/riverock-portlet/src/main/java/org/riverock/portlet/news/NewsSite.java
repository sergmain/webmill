/*
 * org.riverock.portlet - Portlet Library
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.riverock.portlet.news;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.log4j.Logger;

import org.riverock.common.tools.RsetTools;
import org.riverock.common.tools.StringTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.interfaces.portlet.member.ClassQueryItem;
import org.riverock.interfaces.portlet.member.PortletGetList;
import org.riverock.portlet.schema.portlet.news_block.NewsBlockType;
import org.riverock.portlet.schema.portlet.news_block.NewsGroupType;
import org.riverock.portlet.schema.portlet.news_block.NewsItemType;
import org.riverock.portlet.schema.portlet.news_block.NewsSiteType;
import org.riverock.sql.cache.SqlStatement;
import org.riverock.sql.cache.SqlStatementRegisterException;
import org.riverock.webmill.container.ContainerConstants;
import org.riverock.webmill.container.portlet.extend.PortletResultContent;
import org.riverock.webmill.container.portlet.extend.PortletResultObject;
import org.riverock.webmill.container.resource.PortletResourceBundleWithLocale;

/**
 * $Id$
 */
public final class NewsSite implements PortletGetList, PortletResultObject {
    private final static Logger log = Logger.getLogger( NewsSite.class );

    public final static String NAME_ID_NEWS_PARAM = "mill.id_news_item";
    public final static String CTX_TYPE_NEWS_BLOCK = "mill.news_block";
    public final static String NEWS_TYPE = "news.type";  // 'item' or 'block'. default is 'block'
    public final static String NEWS_TYPE_ITEM = "item";  // 'item' type

    static {
        Class c = NewsSite.class;
        try {
            SqlStatement.registerRelateClass( c, NewsGroup.class );
        }
        catch( Throwable exception ) {
            final String es = "Exception in SqlStatement.registerRelateClass()";
            log.error( es, exception );
            throw new SqlStatementRegisterException( es, exception );
        }
    }

    private static Map<Long, NewsSiteType> newsMap = new HashMap<Long, NewsSiteType>();
    private Long siteId = null;
    private RenderRequest renderRequest = null;
    private RenderResponse renderResponse = null;
    private PortletConfig portletConfig = null;

    public void setParameters( final RenderRequest renderRequest, final RenderResponse renderResponse, final PortletConfig portletConfig ) {
        this.renderRequest = renderRequest;
        this.renderResponse = renderResponse;
        this.portletConfig = portletConfig;
    }

    public NewsSite() {
    }

    public void reinit() {
        synchronized( syncObject ) {
            newsMap.clear();
        }
        lastReadData = 0;
    }

    public void terminate( Long id_ ) {
        synchronized( syncObject ) {
            newsMap.clear();
        }
        lastReadData = 0;
    }

    private static long lastReadData = 0;
    private final static long LENGTH_TIME_PERIOD = 10000;
    private final static Object syncObject = new Object();

    private NewsSiteType checkInit( PortletRequest portletRequest ) throws PortletException {

        siteId = new Long( renderRequest.getPortalContext().getProperty( ContainerConstants.PORTAL_PROP_SITE_ID ) );
        if( log.isDebugEnabled() ) {
            log.debug( "serverName: '" + portletRequest.getServerName() + "', siteId: " + siteId + ", newsMap: " + newsMap );
        }

        NewsSiteType news = null;
        if( siteId != null )
            news = newsMap.get( siteId );

        synchronized( syncObject ) {
            if( ( ( System.currentTimeMillis() - lastReadData ) > LENGTH_TIME_PERIOD )
                || news == null ) {
                news = initNews();
                newsMap.put( siteId, news );
            }
            else if( log.isDebugEnabled() ) log.debug( "Get from cache" );
        }
        lastReadData = System.currentTimeMillis();
        return news;
    }

    private void initNewsItem(NewsBlockType newsBlock, RenderResponse renderResponse) throws PortletException {

        if( newsBlock == null )
            return;

        String nextNews = null;
        try {
            ResourceBundle bundle = portletConfig.getResourceBundle( renderRequest.getLocale() );
            if( log.isDebugEnabled() ) {
                log.debug( "resource bundle: " + bundle );
                if ( bundle instanceof PortletResourceBundleWithLocale) {
                    log.debug("bundle class name: " + ((PortletResourceBundleWithLocale)bundle).getResourceBundleClassName() );
                    log.debug("bundle locale name: " + ((PortletResourceBundleWithLocale)bundle).getLocaleName() );
                }
                else {
                    log.debug("bundle is not instanceof PortletResourceBundleWithLocale");
                }
            }


            try {
                nextNews = bundle.getString( "main.next-news" );
            }
            catch( MissingResourceException e ) {
                // catch for try create string again
            }

            if( nextNews == null ) {
                try {
                    nextNews = bundle.getString( "main.next" );
                }
                catch( MissingResourceException e ) {
                    if (log.isDebugEnabled()) {
                        int i=0;
                        Enumeration keys = bundle.getKeys();
                        while(keys.hasMoreElements()) {
                            i++;
                            String key = (String)keys.nextElement();
                            String value = bundle.getString( key );
                            log.debug("bundle key: " + key+", value: " + value);
                        }
                        if (i==0) {
                            log.debug("bundle is empty");
                        }
                    }
                    throw e;
                }
            }
        }
        catch( MissingResourceException e ) {
            log.error( "Error get localized string", e );
            nextNews = "error";
        }

        try {
            for( int j = 0; j < newsBlock.getNewsGroupCount(); j++ ) {
                NewsGroupType newsGroupType = newsBlock.getNewsGroup( j );
                for( int i = 0; i < newsGroupType.getNewsItemCount(); i++ ) {
                    NewsItemType item = newsGroupType.getNewsItem( i );
                    item.setToFullItem( nextNews );

                    PortletURL portletUrl = renderResponse.createRenderURL();
                    portletUrl.setParameter(NEWS_TYPE, NewsSite.NEWS_TYPE_ITEM);
                    portletUrl.setParameter(NAME_ID_NEWS_PARAM, ""+item.getNewsItemId());
                    item.setUrlToFullNewsItem( portletUrl.toString() );
                }
            }
        }
        catch( Throwable e ) {
            String es = "Exception in initNewsItem( NewsBlockType newsBlock )";
            log.error( es, e );
            throw new PortletException( es, e );
        }
    }

    static String sql_ = null;

    static {
        sql_ =
            "select a.*, c.CUSTOM_LANGUAGE " +
            "from   WM_NEWS_LIST a, WM_PORTAL_SITE_LANGUAGE c " +
            "where  c.ID_SITE=? and a.IS_DELETED=0 and " +
            "       a.ID_SITE_SUPPORT_LANGUAGE=c.ID_SITE_SUPPORT_LANGUAGE " +
            "order by a.ORDER_FIELD ASC";

        try {
            SqlStatement.registerSql( sql_, NewsSite.class );
        }
        catch( Throwable e ) {
            final String es = "Exception in registerSql, sql\n" + sql_;
            log.error( es, e );
            throw new SqlStatementRegisterException( es, e );
        }
    }

    private NewsSiteType initNews() throws PortletException {

        if( log.isDebugEnabled() )
            log.debug( "start create object" );

        PreparedStatement ps = null;
        ResultSet rs = null;
        NewsSiteType newsSite = new NewsSiteType();

        DatabaseAdapter db_ = null;
        try {
            db_ = DatabaseAdapter.getInstance();

            ps = db_.prepareStatement( sql_ );
            RsetTools.setLong( ps, 1, siteId );

            rs = ps.executeQuery();
            while( rs.next() ) {
                NewsGroup newsGroup = NewsGroup.getInstance( RsetTools.getLong( rs, "ID_NEWS" ) );

                Long idSiteLanguage = RsetTools.getLong( rs, "ID_SITE_SUPPORT_LANGUAGE" );
                String codeLanguage = StringTools.getLocale( RsetTools.getString( rs, "CUSTOM_LANGUAGE" ) ).toString();

                if( log.isDebugEnabled() )
                    log.debug( "lang of news group. idSiteLanguage " + idSiteLanguage + " code " + codeLanguage );

                boolean isLangExists = false;
                NewsBlockType nb = null;
                if( log.isDebugEnabled() )
                    log.debug( "Start check lang of news group." );

                for( int i = 0; i < newsSite.getNewsBlockCount(); i++ ) {
                    nb = newsSite.getNewsBlock( i );
                    if( log.isDebugEnabled() )
                        log.debug( "Check lang of news group. idLanguage " + nb.getIdSiteLanguage() );

                    if( idSiteLanguage.equals( nb.getIdSiteLanguage() ) ) {
                        isLangExists = true;
                        break;
                    }
                }

                if( log.isDebugEnabled() )
                    log.debug( "End check lang of news group. isLangExist " + isLangExists );

                if( isLangExists ) {
                    nb.addNewsGroup( newsGroup.newsGroup );
                }
                else {
                    if( log.isDebugEnabled() )
                        log.debug( "End check lang of news group. isLangExist " + isLangExists );

                    NewsBlockType newsBlock = new NewsBlockType();

                    newsBlock.addNewsGroup( newsGroup.newsGroup );
                    newsBlock.setIdSiteLanguage( idSiteLanguage );
                    newsBlock.setCodeLanguage( codeLanguage );
                    newsSite.addNewsBlock( newsBlock );
                }
            }

            if( log.isDebugEnabled() ) {
                log.debug( "count of block - " + newsSite.getNewsBlockCount() );
                for( int i = 0; i < newsSite.getNewsBlockCount(); i++ ) {
                    NewsBlockType nb = new NewsBlockType();
                    log.debug( "count of group in block - " + nb.getNewsGroupCount() );
                }
            }

            return newsSite;
        }
        catch( Throwable e ) {
            String es = "Error get news block ";
            log.error( es, e );
            throw new PortletException( es, e );
        }
        finally {
            DatabaseManager.close( db_, rs, ps );
            rs = null;
            ps = null;
            db_ = null;
        }
    }

    public PortletResultContent getInstance( Long id ) throws PortletException {
        if( log.isDebugEnabled() ) log.debug( "getInstance(DatabaseAdapter db__, Long id)" );
        return getInstance();
    }

    public PortletResultContent getInstance() throws PortletException {

        if( log.isDebugEnabled() ) {
            log.debug( "getInstance(DatabaseAdapter db__)" );
            log.debug( "param.renderRequest.getLocale().toString() - " + renderRequest.getLocale().toString() );
        }

        NewsSiteType newsSite = checkInit( renderRequest );

        if( log.isDebugEnabled() ) log.debug( "newsSite.newsBlockCount() - " + newsSite.getNewsBlockCount() );

        NewsBlockType newsBlock = new NewsBlockType();
        for( int i = 0; i < newsSite.getNewsBlockCount(); i++ ) {
            NewsBlockType nb = newsSite.getNewsBlock( i );

            if( log.isDebugEnabled() ) {
                log.debug( "NewsBlockType.getCodeLanguage() - " + nb.getCodeLanguage() );
            }

            if( nb.getCodeLanguage().equals( renderRequest.getLocale().toString() ) ) {
                initNewsItem( nb, renderResponse);
                for( int j = 0; j < nb.getNewsGroupCount(); j++ ) {
                    NewsGroupType newsGroupType = nb.getNewsGroup( j );

                    if( log.isDebugEnabled() ) {
                        log.debug( "newsGroupType.getNewsGroupCode() - " + newsGroupType.getNewsGroupCode() );
                    }

                    newsBlock.addNewsGroup( newsGroupType );
                }
                break;
            }
        }
        return new NewsBlock( newsBlock );
    }

    public PortletResultContent getInstanceByCode( String portletCode_ ) throws PortletException {

        if( log.isDebugEnabled() ) {
            log.debug( "getInstanceByCode(DatabaseAdapter db__, String portletCode_)" );
            log.debug( "code - " + portletCode_ );
            log.debug( "param.renderRequest.getLocale().toString() - " + renderRequest.getLocale().toString() );
        }

        NewsBlockType newsBlock = new NewsBlockType();
        if( portletCode_ != null ) {
            log.debug( "Start checkInit()" );
            NewsSiteType newsSite = checkInit(renderRequest );

            if( log.isDebugEnabled() ) log.debug( "newsSite.newsBlockCount() - " + newsSite.getNewsBlockCount() );

            for( int i = 0; i < newsSite.getNewsBlockCount(); i++ ) {
                NewsBlockType nb = newsSite.getNewsBlock( i );

                if( log.isDebugEnabled() ) log.debug( "NewsBlockType.getCodeLanguage() - " + nb.getCodeLanguage() );

                if( nb.getCodeLanguage().equals( renderRequest.getLocale().toString() ) ) {

                    if( log.isDebugEnabled() ) log.debug( "nb.getNewsGroupCount() - " + nb.getNewsGroupCount() );

                    for( int j = 0; j < nb.getNewsGroupCount(); j++ ) {
                        NewsGroupType newsGroupType = nb.getNewsGroup( j );

                        if( log.isDebugEnabled() ) log.debug( "newsGroupType.getNewsGroupCode() - " + newsGroupType.getNewsGroupCode() );

                        if( portletCode_.equals( newsGroupType.getNewsGroupCode() ) ) {
                            newsBlock.addNewsGroup( newsGroupType );
                            break;
                        }
                    }
                }
            }
        }

        initNewsItem( newsBlock, renderResponse);
        return new NewsBlock( newsBlock );
    }

    public List<ClassQueryItem> getList( Long idSiteCtxLangCatalog, Long idContext ) {
        return null;
    }

}