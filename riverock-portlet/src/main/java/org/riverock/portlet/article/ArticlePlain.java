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
package org.riverock.portlet.article;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import org.riverock.common.tools.DateTools;
import org.riverock.common.tools.RsetTools;
import org.riverock.common.tools.StringTools;
import org.riverock.generic.config.GenericConfig;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.main.CacheFactory;
import org.riverock.interfaces.portal.PortalInfo;
import org.riverock.interfaces.portlet.member.ClassQueryItem;
import org.riverock.interfaces.portlet.member.PortletGetList;
import org.riverock.portlet.member.ClassQueryItemImpl;
import org.riverock.portlet.tools.ContentTypeTools;
import org.riverock.sql.cache.SqlStatement;
import org.riverock.sql.cache.SqlStatementRegisterException;
import org.riverock.webmill.container.ContainerConstants;
import org.riverock.webmill.container.portlet.extend.PortletResultContent;
import org.riverock.webmill.container.portlet.extend.PortletResultObject;

/**
 * Author: mill
 * Date: Jan 10, 2003
 * Time: 9:45:31 AM
 *
 * $Id$
 */
@SuppressWarnings({"UnusedAssignment"})
public final class ArticlePlain implements PortletResultObject, PortletGetList, PortletResultContent {
    private final static Logger log = Logger.getLogger( ArticlePlain.class );

    private final static CacheFactory cache = new CacheFactory( ArticlePlain.class);

    public Date datePost = null;
    public String nameArticle = "";
    public String text = "";
    public Long id = null;
    public boolean isTranslateCR = false;
    public boolean isPlainHTML = false;
    public boolean isSimpleTextBlock = false;
    public String articleCode = "";
    public Long idSupportLanguage = null;
    private RenderRequest renderRequest = null;
//    private ResourceBundle bundle = null;

    public void setParameters( RenderRequest renderRequest, RenderResponse renderResponse, PortletConfig portletConfig ) {
        this.renderRequest = renderRequest;
//        this.bundle = bundle;
    }

    public void reinit() {
        cache.reinit();
    }

    public synchronized void terminate( Long id ) {
        cache.terminate( id );
    }

    public PortletResultContent getInstance( DatabaseAdapter db__ ) {
        return null;
    }

    public byte[] getXml( String name ) {
        return null;
    }

    public byte[] getXml() {
        return null;
    }

    public byte[] getPlainHTML()
        throws Exception {
        if( log.isDebugEnabled() )
            log.debug( "Article plain. method is 'Plain'" );

        return ( isTranslateCR ?
            StringUtils.replace( text, "\n", "<br>\n" ) :
            text
            ).getBytes( ContentTypeTools.CONTENT_TYPE_UTF8 );
    }

    public boolean isXml() {
        return false;
    }

    public boolean isHtml() {
        return true;
    }

    public String getArticleDate() {
        return DateTools.getStringDate( datePost, "dd.MMM.yyyy", renderRequest.getLocale(), GenericConfig.getTZ() );
    }

    public String getArticleTime() {
        return DateTools.getStringDate( datePost, "HH:mm", renderRequest.getLocale(), GenericConfig.getTZ() );
    }

    public String getArticleName() {
        return nameArticle;
    }

    public String getArticleText() {
        if( log.isDebugEnabled() )
            log.debug( "Article text - " + text );

        String s = text;
        if( isTranslateCR )
            s = StringTools.prepareToParsingSimple( s );

        if( isSimpleTextBlock ) {
            if( log.isDebugEnabled() )
                log.debug( "isSimpleTextBlock - " + isSimpleTextBlock );

            s = StringEscapeUtils.unescapeXml( s );
        }

        return s;
    }

    public ArticlePlain() {
    }

    public PortletResultContent getInstance( Long id__ ) throws PortletException {
        try {
            return ( ArticlePlain ) cache.getInstanceNew( id__ );
        }
        catch( Throwable e ) {
            String es = "Error get instance of ArticlePlain";
            log.error( es, e );
            throw new PortletException( es, e );
        }
    }

    static String sql_ = null;

    static {
        sql_ =
            "select a.ID_SITE_CTX_ARTICLE " +
            "from   WM_PORTLET_ARTICLE a " +
            "where  a.ID_SITE_SUPPORT_LANGUAGE=? and a.ARTICLE_CODE=? and a.IS_DELETED=0";

        try {
            SqlStatement.registerSql( sql_, ArticlePlain.class );
        }
        catch( Throwable e ) {
            final String es = "Error in registerSql, sql\n" + sql_;
            log.error( es, e );
            throw new SqlStatementRegisterException( es, e );
        }
    }

    public PortletResultContent getInstanceByCode( String articleCode_ )
        throws PortletException {
        if( log.isDebugEnabled() )
            log.debug( "#10.01.01 " + articleCode_ );

        PreparedStatement ps = null;
        ResultSet rs = null;
        DatabaseAdapter db__ = null;
        try {
            db__ = DatabaseAdapter.getInstance();

            PortalInfo portalInfo = ( PortalInfo ) renderRequest.getAttribute( ContainerConstants.PORTAL_INFO_ATTRIBUTE );
            Long idSupportLanguageCurrent = portalInfo.getSupportLanguageId( renderRequest.getLocale() );

            ps = db__.prepareStatement( sql_ );
            RsetTools.setLong( ps, 1, idSupportLanguageCurrent );
            ps.setString( 2, articleCode_ );

            rs = ps.executeQuery();
            if( rs.next() ) {
                if( log.isDebugEnabled() )
                    log.debug( "#10.01.04 " + RsetTools.getLong( rs, "ID_SITE_CTX_ARTICLE" ) );

                return getInstance( RsetTools.getLong( rs, "ID_SITE_CTX_ARTICLE" ) );
            }

            if( log.isDebugEnabled() )
                log.debug( "#10.01.05 " );

            // return dummy Article
            return new ArticlePlain();

        }
        catch( Throwable e ) {
            String es = "Exception in Menu(....";
            log.error( es, e );
            throw new PortletException( es, e );
        }
        finally {
            DatabaseManager.close( db__, rs, ps );
            rs = null;
            ps = null;
            db__ = null;
        }
    }

    static String sql1_ = null;

    static {
        sql1_ =
            "select * from WM_PORTLET_ARTICLE where ID_SITE_CTX_ARTICLE=? and IS_DELETED=0";

        try {
            SqlStatement.registerSql( sql1_, ArticlePlain.class );
        }
        catch( Throwable e ) {
            final String es = "Error in registerSql, sql\n" + sql_;
            log.error( es, e );
            throw new SqlStatementRegisterException( es, e );
        }
    }

    public ArticlePlain( Long id_ ) throws Exception {

        PreparedStatement ps = null;
        ResultSet rs = null;
        id = id_;
        DatabaseAdapter db_ = null;
        try {
            db_ = DatabaseAdapter.getInstance();
            ps = db_.prepareStatement( sql1_ );
            RsetTools.setLong( ps, 1, id );

            rs = ps.executeQuery();
            if( rs.next() ) {
                Timestamp timestamp = RsetTools.getTimestamp(rs, "DATE_POST");
                if (timestamp!=null)
                    datePost = new Date(timestamp.getTime() );
                else
                    datePost=new Date();
                nameArticle = RsetTools.getString( rs, "NAME_ARTICLE" );
                isTranslateCR = ( RsetTools.getInt( rs, "IS_TRANSLATE_CR", 0 ) == 1 );
                isPlainHTML = ( RsetTools.getInt( rs, "IS_PLAIN_HTML", 0 ) == 1 );
                isSimpleTextBlock = ( RsetTools.getInt( rs, "IS_SIMPLE_TEXT", 0 ) == 1 );
                articleCode = RsetTools.getString( rs, "ARTICLE_CODE" );
                idSupportLanguage = RsetTools.getLong( rs, "ID_SITE_CTX_ARTICLE" );

                initTextField( db_ );
            }
        }
        finally {
            DatabaseManager.close( db_, rs, ps );
            rs = null;
            ps = null;
            db_ = null;
        }
    }

    static String sql2_ = null;

    static {
        sql2_ =
            "select ARTICLE_DATA " +
            "from   WM_PORTLET_ARTICLE_DATA " +
            "where  ID_SITE_CTX_ARTICLE = ? " +
            "order by ID_SITE_CTX_ARTICLE_DATA ASC";

        try {
            SqlStatement.registerSql( sql2_, ArticlePlain.class );
        }
        catch( Throwable e ) {
            final String es = "Error in registerSql, sql\n" + sql_;
            log.error( es, e );
            throw new SqlStatementRegisterException( es, e );
        }
    }

    private void initTextField( DatabaseAdapter db_ ) throws Exception {
        if( id == null )
            return;

        PreparedStatement ps = null;
        ResultSet rset = null;
        try {
            ps = db_.prepareStatement( sql2_ );

            RsetTools.setLong( ps, 1, id );
            rset = ps.executeQuery();
            StringBuilder sb = new StringBuilder();
            while( rset.next() ) {
                sb.append( RsetTools.getString( rset, "ARTICLE_DATA" ) );
            }

            if( log.isDebugEnabled() ) {
                log.debug( "Result text of article: " + sb.toString() );
            }

            text = sb.toString();
        }
        finally {
            DatabaseManager.close( rset, ps );
            rset = null;
            ps = null;
        }
    }

    static String sql3_ = null;

    static {
        sql3_ =
            "SELECT b.ID_SITE_CTX_ARTICLE, b.NAME_ARTICLE, b.ARTICLE_CODE " +
            "FROM   WM_PORTAL_CATALOG_LANGUAGE a, WM_PORTLET_ARTICLE b " +
            "where  a.ID_SITE_CTX_LANG_CATALOG=? and " +
            "       a.ID_SITE_SUPPORT_LANGUAGE=b.ID_SITE_SUPPORT_LANGUAGE and " +
            "       b.IS_PLAIN_HTML=1";

        try {
            SqlStatement.registerSql( sql3_, ArticlePlain.class );
        }
        catch( Throwable e ) {
            final String es = "Error in registerSql, sql\n" + sql_;
            log.error( es, e );
            throw new SqlStatementRegisterException( es, e );
        }
    }

    public List<ClassQueryItem> getList( Long idSiteCtxLangCatalog, Long idContext ) {
        if( log.isDebugEnabled() )
            log.debug( "Get list of ArticlePlain. idSiteCtxLangCatalog - " + idSiteCtxLangCatalog );

        PreparedStatement ps = null;
        ResultSet rs = null;
        DatabaseAdapter db_ = null;

        List<ClassQueryItem> v = new ArrayList<ClassQueryItem>();
        try {
            db_ = DatabaseAdapter.getInstance();
            ps = db_.prepareStatement( sql3_ );

            RsetTools.setLong( ps, 1, idSiteCtxLangCatalog );

            rs = ps.executeQuery();
            while( rs.next() ) {
                Long id = RsetTools.getLong( rs, "ID_SITE_CTX_ARTICLE" );
                String name = "" + id + ", " +
                    RsetTools.getString( rs, "ARTICLE_CODE" ) + ", " +
                    RsetTools.getString( rs, "NAME_ARTICLE" );

                ClassQueryItem item =
                    new ClassQueryItemImpl( id, StringTools.truncateString( name, 60 ) );

                if( item.getIndex().equals( idContext ) )
                    item.setSelected( true );

                v.add( item );
            }
            return v;

        }
        catch( Exception e ) {
            log.error( "Get list of ArticlePlain. idSiteCtxLangCatalog - " + idSiteCtxLangCatalog, e );
            return null;
        }
        finally {
            DatabaseManager.close( db_, rs, ps );
            rs = null;
            ps = null;
        }
    }
}
