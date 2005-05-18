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
import java.util.ResourceBundle;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.PortletConfig;

import org.riverock.cache.impl.CacheException;
import org.riverock.common.tools.DateTools;
import org.riverock.common.tools.RsetTools;
import org.riverock.common.tools.StringTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.main.CacheFactory;
import org.riverock.portlet.member.ClassQueryItemImpl;
import org.riverock.webmill.config.WebmillConfig;
import org.riverock.webmill.port.PortalInfo;
import org.riverock.webmill.portal.PortalConstants;
import org.riverock.interfaces.portlet.member.PortletGetList;
import org.riverock.webmill.portlet.PortletResultObject;
import org.riverock.webmill.portlet.PortletResultContent;
import org.riverock.sql.cache.SqlStatement;
import org.riverock.sql.cache.SqlStatementRegisterException;
import org.riverock.interfaces.portlet.member.ClassQueryItem;

import org.apache.log4j.Logger;

/**
 * Author: mill
 * Date: Jan 10, 2003
 * Time: 9:45:31 AM
 *
 * $Id$
 */
public final class ArticlePlain implements PortletResultObject, PortletGetList, PortletResultContent {
    private final static Logger log = Logger.getLogger( ArticlePlain.class );

    private final static CacheFactory cache = new CacheFactory( ArticlePlain.class.getName() );

    public Calendar datePost = null;
    public String nameArticle = "";
    public String text = "";
    public Long id = null;
    public boolean isTranslateCR = false;
    public boolean isPlainHTML = false;
    public boolean isSimpleTextBlock = false;
    public String articleCode = "";
    public Long idSupportLanguage = null;
    private RenderRequest renderRequest = null;
    private RenderResponse renderResponse = null;
    private ResourceBundle bundle = null;

    public void setParameters( RenderRequest renderRequest, RenderResponse renderResponse, PortletConfig portletConfig ) {
        this.renderRequest = renderRequest;
        this.renderResponse = renderResponse;
        this.bundle = bundle;
    }

    public void reinit()
    {
        cache.reinit();
    }

    public synchronized void terminate(Long id) throws CacheException {
        cache.terminate(id);
    }

    protected void finalize() throws Throwable {
        datePost = null;
        nameArticle = null;
        text = null;
        articleCode = null;

        super.finalize();
    }

    public PortletResultContent getInstance(DatabaseAdapter db__) throws PortletException {
        return null;
    }

    public byte[] getXml(String name) {
        return null;
    }

    public byte[] getXml() {
        return null;
    }

    public byte[] getPlainHTML()
        throws Exception
    {
        if(log.isDebugEnabled())
            log.debug("Article plain. method is 'Plain'");

        return (isTranslateCR?
                StringTools.replaceString(text, "\n", "<br>\n"):
                text
                ).getBytes( WebmillConfig.getHtmlCharset() );
    }

    public boolean isXml(){ return false; }
    public boolean isHtml(){ return true; }

    public String getArticleDate() {
        return DateTools.getStringDate(datePost, "dd.MMM.yyyy", renderRequest.getLocale());
    }

    public String getArticleTime() {
        return DateTools.getStringDate(datePost, "HH:mm", renderRequest.getLocale());
    }

    public String getArticleName() {
        return nameArticle;
    }

    public String getArticleText()
    {
        if(log.isDebugEnabled())
            log.debug("Article text - "+text);

        String s = text;
        if (isTranslateCR)
            s = StringTools.prepareToParsingSimple( s );

        if ( isSimpleTextBlock)
        {
            if (log.isDebugEnabled() )
                log.debug("isSimpleTextBlock - "+isSimpleTextBlock);

            s = StringTools.toOrigin(s );
        }

        return s;
    }

    public ArticlePlain() {
    }

    public PortletResultContent getInstance(DatabaseAdapter db__, long id__)
            throws Exception {
        return getInstance(db__, new Long(id__) );
    }

    public PortletResultContent getInstance(DatabaseAdapter db__, Long id__)
            throws PortletException {
        try {
            return (ArticlePlain) cache.getInstanceNew(db__, id__);
        }
        catch(Throwable e) {
            String es = "Error get instance of ArticlePlain";
            log.error(es, e);
            throw new PortletException(es, e);
        }

    }

    static String sql_ = null;
    static {
        sql_ =
            "select a.ID_SITE_CTX_ARTICLE " +
            "from SITE_CTX_ARTICLE a " +
            "where a.ID_SITE_SUPPORT_LANGUAGE=? and a.ARTICLE_CODE=? and a.IS_DELETED=0";

        try
        {
            SqlStatement.registerSql( sql_, new ArticlePlain().getClass() );
        }
        catch(Throwable e)
        {
            final String es = "Error in registerSql, sql\n"+sql_;
            log.error(es, e);
            throw new SqlStatementRegisterException( es, e );
        }
    }

    public PortletResultContent getInstanceByCode(DatabaseAdapter db__, String articleCode_)
            throws PortletException
    {
        if (log.isDebugEnabled())
            log.debug("#10.01.01 " + articleCode_);

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            PortalInfo portalInfo = (PortalInfo)renderRequest.getAttribute(PortalConstants.PORTAL_INFO_ATTRIBUTE);
            Long idSupportLanguageCurrent = portalInfo.getIdSupportLanguage( renderRequest.getLocale() );

            //Todo Need optimize
//            for (int i = 0; i < cache.maxCountItems(); i++)
//            {
//
//                if (cache[i] != null &&
//                    ((ArticlePlain) cache[i]).articleCode.equals(articleCode_) &&
//                    ((ArticlePlain) cache[i]).idSupportLanguage.equals(idSupportLanguageCurrent)
//                )
//                {
//                    if (log.isDebugEnabled())
//                        log.debug("#10.01.03");
//
//                    return (ArticlePlain) cache[i];
//                }
//            }

            ps = db__.prepareStatement(sql_);
            RsetTools.setLong(ps, 1, idSupportLanguageCurrent );
            ps.setString(2, articleCode_);

            rs = ps.executeQuery();
            if (rs.next())
            {
                if (log.isDebugEnabled())
                    log.debug("#10.01.04 " + RsetTools.getLong(rs, "ID_SITE_CTX_ARTICLE"));

                return getInstance(db__, RsetTools.getLong(rs, "ID_SITE_CTX_ARTICLE"));
            }

            if (log.isDebugEnabled())
                log.debug("#10.01.05 ");

            // return dummy Article
            return getInstance(db__, -1);

        }
        catch(Throwable e){
            String es = "Exception in Menu(....";
            log.error(es, e);
            throw new PortletException(es, e);
        }
        finally
        {
            DatabaseManager.close(rs, ps);
            rs = null;
            ps = null;
        }
    }

    static String sql1_ = null;
    static {
        sql1_ =
            "select * from SITE_CTX_ARTICLE where ID_SITE_CTX_ARTICLE=? and IS_DELETED=0";

        try {
            SqlStatement.registerSql( sql1_, new ArticlePlain().getClass() );
        }
        catch(Throwable e) {
            final String es = "Error in registerSql, sql\n"+sql_;
            log.error(es, e);
            throw new SqlStatementRegisterException( es, e );
        }
    }

    public ArticlePlain(DatabaseAdapter db_, Long id_) throws Exception {

        PreparedStatement ps = null;
        ResultSet rs = null;
        id = id_;
        try {
            ps = db_.prepareStatement(sql1_);
            RsetTools.setLong(ps, 1, id);

            rs = ps.executeQuery();
            if (rs.next()) {
                datePost = RsetTools.getCalendar(rs, "DATE_POST");
                nameArticle = RsetTools.getString(rs, "NAME_ARTICLE");
                isTranslateCR = new Integer(1).equals(RsetTools.getInt(rs, "IS_TRANSLATE_CR"));
                isPlainHTML = new Integer(1).equals(RsetTools.getInt(rs, "IS_PLAIN_HTML"));
                isSimpleTextBlock = new Integer(1).equals(RsetTools.getInt(rs, "IS_SIMPLE_TEXT"));
                articleCode = RsetTools.getString(rs, "ARTICLE_CODE");
                idSupportLanguage = RsetTools.getLong(rs, "ID_SITE_CTX_ARTICLE");

                initTextField();
            }
        }
        finally {
            DatabaseManager.close(rs, ps);
            rs = null;
            ps = null;
        }
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
            SqlStatement.registerSql( sql2_, new ArticlePlain().getClass() );
        }
        catch(Throwable e) {
            final String es = "Error in registerSql, sql\n"+sql_;
            log.error(es, e);
            throw new SqlStatementRegisterException( es, e );
        }
    }

    public void initTextField() throws Exception {
        if (id == null)
            return;

        DatabaseAdapter db_ = null;
        PreparedStatement ps = null;
        ResultSet rset = null;
        try {
            db_ = DatabaseAdapter.getInstance(false);
            ps = db_.prepareStatement( sql2_ );

            RsetTools.setLong(ps, 1, id);
            rset = ps.executeQuery();
            text = "";
            while (rset.next())
            {
                text += RsetTools.getString(rset, "ARTICLE_DATA");
            }
        }
        finally {
            DatabaseManager.close(db_, rset, ps);
            rset = null;
            ps = null;
            db_ = null;
        }
    }

    static String sql3_ = null;
    static {
        sql3_ =
            "SELECT b.ID_SITE_CTX_ARTICLE, b.NAME_ARTICLE, b.ARTICLE_CODE "+
            "FROM site_ctx_lang_catalog a, site_ctx_article b "+
            "where a.ID_SITE_CTX_LANG_CATALOG=? and "+
            "a.ID_SITE_SUPPORT_LANGUAGE=b.ID_SITE_SUPPORT_LANGUAGE and "+
            "b.IS_PLAIN_HTML=1";

        try {
            SqlStatement.registerSql( sql3_, new ArticlePlain().getClass() );
        }
        catch(Throwable e) {
            final String es = "Error in registerSql, sql\n"+sql_;
            log.error(es, e);
            throw new SqlStatementRegisterException( es, e );
        }
    }

    public List getList( Long idSiteCtxLangCatalog, Long idContext) {
        if (log.isDebugEnabled())
            log.debug("Get list of ArticlePlain. idSiteCtxLangCatalog - " + idSiteCtxLangCatalog);

        PreparedStatement ps = null;
        ResultSet rs = null;
        DatabaseAdapter db_ = null;

        List v = new ArrayList();
        try {
            db_ = DatabaseAdapter.getInstance( false );
            ps = db_.prepareStatement( sql3_ );

            RsetTools.setLong(ps, 1, idSiteCtxLangCatalog );

            rs = ps.executeQuery();
            while (rs.next()) {
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
        catch(Exception e) {
            log.error("Get list of ArticlePlain. idSiteCtxLangCatalog - " + idSiteCtxLangCatalog, e);
            return null;
        }
        finally {
            DatabaseManager.close(db_, rs, ps);
            rs = null;
            ps = null;
        }
    }
}
