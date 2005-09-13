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
package org.riverock.portlet.article;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.PortletConfig;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.riverock.cache.impl.CacheException;
import org.riverock.common.tools.DateTools;
import org.riverock.common.tools.RsetTools;
import org.riverock.common.tools.StringTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.main.CacheFactory;
import org.riverock.portlet.core.GetSiteCtxArticleItem;
import org.riverock.portlet.member.ClassQueryItemImpl;
import org.riverock.portlet.schema.core.SiteCtxArticleItemType;
import org.riverock.portlet.tools.ContentTypeTools;
import org.riverock.interfaces.portlet.member.PortletGetList;
import org.riverock.sql.cache.SqlStatement;
import org.riverock.sql.cache.SqlStatementRegisterException;
import org.riverock.interfaces.portlet.member.ClassQueryItem;
import org.riverock.webmill.container.portlet.extend.PortletResultObject;
import org.riverock.webmill.container.portlet.extend.PortletResultContent;
import org.riverock.webmill.container.portal.PortalInfo;
import org.riverock.webmill.container.ContainerConstants;



/**
 * Author: mill
 * Date: Jan 10, 2003
 * Time: 9:41:20 AM
 *
 * $Id$
 */

public final class ArticleXml implements PortletResultObject, PortletGetList, PortletResultContent {
    private final static Log log = LogFactory.getLog( ArticleXml.class );

    private static final CacheFactory cache = new CacheFactory( ArticleXml.class.getName() );
    private static final String DEFAULT_ROOT_NAME = "Article";

    private Calendar datePost = null;
    private String dateText = null;
    private String timeText = null;

    private String nameArticle = "";
    private String text = "";
    private Long id = null;

    private RenderRequest renderRequest = null;

    public void setParameters( RenderRequest renderRequest, RenderResponse renderResponse, PortletConfig portletConfig ) {
        if(log.isDebugEnabled())
            log.debug("renderRequest: "+renderRequest);

        this.renderRequest = renderRequest;
    }

    public void reinit() {
        cache.reinit();
    }

    public synchronized void terminate(Long id) throws CacheException {
        cache.terminate(id);
    }

    protected void finalize() throws Throwable {
        datePost = null;
        dateText = null;
        timeText = null;
        nameArticle = null;
        text = null;

        super.finalize();
    }

    public byte[] getXml(String rootName) throws Exception {
        if(log.isDebugEnabled()) {
            log.debug( "ArticleXml. method is 'Xml'. Root: "+rootName );
            log.debug( "Article date: " + datePost );
            log.debug( "renderRequest: " + renderRequest );
        }
        dateText = DateTools.getStringDate( datePost, "dd.MMM.yyyy", renderRequest.getLocale() );
        timeText = DateTools.getStringDate( datePost, "HH:mm", renderRequest.getLocale() );

        String xml = new StringBuffer().
            append( "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" ).
            append( "<" ).append( rootName ).
            append( "><ArticleDate>" ).append( dateText ).append( "</ArticleDate>" ).
            append( "<ArticleTime>" ).append( timeText ).append( "</ArticleTime>" ).
            append( "<ArticleName>" ).append( nameArticle ).append( "</ArticleName>" ).
            append( "<ArticleText>" ).append( text ).append( "</ArticleText></" ).
            append( rootName == null ?DEFAULT_ROOT_NAME :rootName ).append( ">" ).toString();

        if (log.isDebugEnabled())
            log.debug( "ArticleXml. getXml - "+xml );

        return xml.getBytes( ContentTypeTools.CONTENT_TYPE_UTF8 );
    }

    public byte[] getXml() throws Exception {
        if(log.isDebugEnabled())
            log.debug("ArticleXml. method is 'Xml'");

        return getXml(DEFAULT_ROOT_NAME);
    }

    public byte[] getPlainHTML() {
        return null;
    }

    public ArticleXml() {
    }

    public PortletResultContent getInstance(DatabaseAdapter db__, long id__) throws Exception {
        return getInstance(db__, id__ );
    }

    public PortletResultContent getInstance( Long id__ )
            throws PortletException
    {
        DatabaseAdapter db__ = null;
        try {
            db__ = DatabaseAdapter.getInstance();
            PortletResultContent portletObject = (PortletResultContent) cache.getInstanceNew(db__, id__);
            return portletObject;
        }
        catch(Throwable e) {
            String es = "Error get instance of ArticleXml";
            log.error(es, e);
            throw new PortletException(es, e);
        }
        finally {
            DatabaseManager.close(db__);
            db__ = null;
        }
    }

    static String sql_ = null;
    static {
        sql_ =
            "select a.ID_SITE_CTX_ARTICLE " +
            "from SITE_CTX_ARTICLE a " +
            "where a.ID_SITE_SUPPORT_LANGUAGE=? and a.ARTICLE_CODE=? and a.IS_DELETED=0";

        try {
            SqlStatement.registerSql( sql_, ArticleXml.class );
        }
        catch(Throwable e) {
            final String es = "Error in registerSql, sql\n"+sql_;
            log.error(es, e);
            throw new SqlStatementRegisterException( es, e );
        }
    }

    public PortletResultContent getInstanceByCode( String articleCode_ )
        throws PortletException
    {
        if (log.isDebugEnabled())
            log.debug("#10.01.01 " + articleCode_);

        PortalInfo portalInfo = (PortalInfo)renderRequest.getAttribute(ContainerConstants.PORTAL_INFO_ATTRIBUTE);
        Long idSupportLanguageCurrent = portalInfo.getSupportLanguageId( renderRequest.getLocale() );

        PreparedStatement ps = null;
        ResultSet rs = null;
        DatabaseAdapter db__ = null;
        try {
            db__ = DatabaseAdapter.getInstance();
            ps = db__.prepareStatement(sql_);
            RsetTools.setLong(ps, 1, idSupportLanguageCurrent );
            ps.setString(2, articleCode_);

            rs = ps.executeQuery();
            if (rs.next()) {
                if (log.isDebugEnabled())
                    log.debug("#10.01.04 " + RsetTools.getLong(rs, "ID_SITE_CTX_ARTICLE"));

                return getInstance(db__, RsetTools.getLong(rs, "ID_SITE_CTX_ARTICLE"));
            }

            if (log.isDebugEnabled())
                log.debug("#10.01.05 ");

            // return dummy Article
            return getInstance(db__, -1);
        }
        catch(Throwable e) {
            String es = "Exception in ArticleXml.getInstanceByCode()";
            log.error(es, e);
            throw new PortletException(es, e);
        }
        finally {
            DatabaseManager.close(db__, rs, ps);
            rs = null;
            ps = null;
            db__ = null;
        }
    }

    static String sql1_ = null;
    static {
        sql1_ =
            "select * from SITE_CTX_ARTICLE where ID_SITE_CTX_ARTICLE=? and IS_DELETED=0";

        try {
            SqlStatement.registerSql( sql1_, new ArticleXml().getClass() );
        }
        catch(Throwable e) {
            final String es = "Error in registerSql, sql\n"+sql1_;
            log.error(es, e);
            throw new SqlStatementRegisterException( es, e );
        }
    }

    public ArticleXml(DatabaseAdapter db_, Long id_) throws Exception {
        SiteCtxArticleItemType article = GetSiteCtxArticleItem.getInstance(db_, id_).item;

        if (article==null || Boolean.TRUE.equals( article.getIsDeleted()) )
            return;

        this.id = id_;
        datePost = Calendar.getInstance();
        datePost.setTimeInMillis( article.getDatePost().getTime() );
        nameArticle = article.getNameArticle();
        initTextField( db_ );
    }

    static String sql2_ = null;
    static
    {
        sql2_ =
            "select ARTICLE_DATA " +
            "from SITE_CTX_ARTICLE_DATA " +
            "where ID_SITE_CTX_ARTICLE = ? " +
            "order by ID_SITE_CTX_ARTICLE_DATA ASC";

        try {
            SqlStatement.registerSql( sql2_, new ArticleXml().getClass() );
        }
        catch(Throwable e) {
            final String es = "Error in registerSql, sql\n"+sql2_;
            log.error(es, e);
            throw new SqlStatementRegisterException( es, e );
        }
    }

    private void initTextField( DatabaseAdapter db_ ) throws PortletException {
        if (id == null)
            return;

        PreparedStatement ps = null;
        ResultSet rset = null;
        try {
            ps = db_.prepareStatement( sql2_ );

            RsetTools.setLong(ps, 1, id);
            rset = ps.executeQuery();
            StringBuffer sb = new StringBuffer("");
            while (rset.next()) {
                sb.append( RsetTools.getString(rset, "ARTICLE_DATA") );
            }

            if (log.isDebugEnabled()) {
                log.debug( "Result text of article: " + sb.toString());
            }

            text = sb.toString();
        }
        catch(Exception e) {
            String es = "Exception in ArticleXml.initTextField";
            log.error(es, e);
            throw new PortletException(es, e);
        }
        catch(Error e) {
            String es = "Error in ArticleXml.initTextField";
            log.error(es, e);
            throw new PortletException(es, e);
        }
        finally {
            DatabaseManager.close(rset, ps);
            rset = null;
            ps = null;
        }
    }

    static String sql3_ = null;
    static {
        sql3_ =
            "SELECT b.ID_SITE_CTX_ARTICLE, b.NAME_ARTICLE, b.ARTICLE_CODE "+
            "FROM site_ctx_lang_catalog a, site_ctx_article b "+
            "where a.ID_SITE_CTX_LANG_CATALOG=? and "+
            "a.ID_SITE_SUPPORT_LANGUAGE=b.ID_SITE_SUPPORT_LANGUAGE and "+
            "b.IS_PLAIN_HTML=0";

        try {
            SqlStatement.registerSql( sql3_, ArticleXml.class );
        }
        catch(Throwable e) {
            final String es = "Error in registerSql, sql\n"+sql3_;
            log.error(es, e);
            throw new SqlStatementRegisterException( es, e );
        }
    }

    public List getList( final Long idSiteCtxLangCatalog, final Long idContext) {
        if (log.isDebugEnabled())
            log.debug("Get list of ArticleXml. idSiteCtxLangCatalog - " + idSiteCtxLangCatalog);

        PreparedStatement ps = null;
        ResultSet rs = null;
        DatabaseAdapter db_ = null;

        List v = new ArrayList();
        try
        {
            db_ = DatabaseAdapter.getInstance();
            ps = db_.prepareStatement( sql3_ );

            RsetTools.setLong(ps, 1, idSiteCtxLangCatalog );

            rs = ps.executeQuery();
            while (rs.next())
            {
                Long id = RsetTools.getLong(rs, "ID_SITE_CTX_ARTICLE");
                String name = ""+id + ", "+
                        RsetTools.getString(rs, "ARTICLE_CODE")+ ", "+
                        RsetTools.getString(rs, "NAME_ARTICLE");

                ClassQueryItem item =
                        new ClassQueryItemImpl(id, StringTools.truncateString(name, 60) );

                if (item.getIndex().equals(idContext) )
                    item.setSelected(true);

                v.add( item );
            }
            return v;

        }
        catch(Exception e)
        {
            log.error("Exception Get list of ArticlePlain. idSiteCtxLangCatalog - " + idSiteCtxLangCatalog, e);
            return null;
        }
        catch(Error e)
        {
            log.error("Error Get list of ArticlePlain. idSiteCtxLangCatalog - " + idSiteCtxLangCatalog, e);
            return null;
        }
        finally
        {
            DatabaseManager.close(db_, rs, ps);
            rs = null;
            ps = null;
            db_ = null;
        }
    }
}
