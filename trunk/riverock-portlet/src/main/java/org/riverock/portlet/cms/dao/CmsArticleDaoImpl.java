/*
 * org.riverock.portlet -- Portlet Library
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
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
 */
package org.riverock.portlet.cms.dao;

import java.util.List;
import java.util.ArrayList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;

import org.riverock.portlet.cms.article.bean.ArticleBean;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.schema.db.CustomSequenceType;
import org.riverock.generic.schema.db.types.PrimaryKeyTypeTypeType;
import org.riverock.common.tools.RsetTools;
import org.riverock.interfaces.sso.a3.AuthSession;
import org.apache.log4j.Logger;

/**
 * @author Sergei Maslyukov
 *         Date: 25.08.2006
 *         Time: 21:47:40
 */
public class CmsArticleDaoImpl implements CmsArticleDao {
    private final static Logger log = Logger.getLogger( CmsArticleDaoImpl.class );

    public List<ArticleBean> getArticleList(Long siteLanguageId, boolean isXml) {
        List<ArticleBean> list = new ArrayList<ArticleBean>();
        if (siteLanguageId==null) {
            return list;
        }

        PreparedStatement ps = null;
        ResultSet rs = null;

        DatabaseAdapter db_ = null;
        try {
            db_ = DatabaseAdapter.getInstance();

            ps = db_.prepareStatement(
                "select ID_SITE_CTX_ARTICLE, ID_USER, DATE_POST, ID_SITE_SUPPORT_LANGUAGE, IS_DELETED, NAME_ARTICLE, ARTICLE_CODE, IS_PLAIN_HTML " +
                "from   WM_PORTLET_ARTICLE " +
                "where  IS_DELETED=0 and ID_SITE_SUPPORT_LANGUAGE=? and IS_PLAIN_HTML=? "
            );
            RsetTools.setLong(ps, 1, siteLanguageId );
            RsetTools.setInt(ps, 2, isXml?0:1 );

            rs = ps.executeQuery();

            while (rs.next()) {
                ArticleBean article = new ArticleBean();
                article.setSiteLanguageId(siteLanguageId);

                article.setArticleName( RsetTools.getString(rs, "NAME_ARTICLE"));
                article.setArticleCode( RsetTools.getString(rs, "ARTICLE_CODE") );
                article.setArticleId( RsetTools.getLong(rs, "ID_SITE_CTX_ARTICLE") );
                article.setCreated( RsetTools.getTimestamp(rs, "DATE_POST") );
                article.setXml(isXml);

                article.setArticleText( initArticleText(article.getArticleId()) );

                list.add(article);
            }
            return list;
        }
        catch (Throwable e) {
            final String es = "Error create list of article";
            log.error(es, e);
            throw new RuntimeException( es, e );
        }
        finally {
            DatabaseManager.close(db_, rs, ps);
        }
    }

    public ArticleBean getArticle(Long articleId) {
        if (articleId==null) {
            return null;
        }

        PreparedStatement ps = null;
        ResultSet rs = null;

        DatabaseAdapter db_ = null;
        try {
            db_ = DatabaseAdapter.getInstance();

            ps = db_.prepareStatement(
                "select ID_SITE_CTX_ARTICLE, ID_USER, DATE_POST, ID_SITE_SUPPORT_LANGUAGE, IS_DELETED, NAME_ARTICLE, ARTICLE_CODE, IS_PLAIN_HTML " +
                "from   WM_PORTLET_ARTICLE " +
                "where  IS_DELETED=0 and ID_SITE_CTX_ARTICLE=? "
            );
            RsetTools.setLong(ps, 1, articleId );

            rs = ps.executeQuery();

            if (rs.next()) {
                ArticleBean article = new ArticleBean();
                article.setSiteLanguageId(RsetTools.getLong(rs, "ID_SITE_SUPPORT_LANGUAGE"));

                article.setArticleId( articleId );

                article.setArticleName( RsetTools.getString(rs, "NAME_ARTICLE"));
                article.setArticleCode( RsetTools.getString(rs, "ARTICLE_CODE") );
                article.setSiteLanguageId( RsetTools.getLong(rs, "ID_SITE_SUPPORT_LANGUAGE") );
                article.setCreated( RsetTools.getTimestamp(rs, "DATE_POST") );
                article.setXml( RsetTools.getInt(rs, "ID_SITE_CTX_ARTICLE", 0)==0 );

                article.setArticleText( initArticleText(article.getArticleId()) );

                return article;
            }
            return null;
        }
        catch (Throwable e) {
            final String es = "Error get article";
            log.error(es, e);
            throw new RuntimeException( es, e );
        }
        finally {
            DatabaseManager.close(db_, rs, ps);
        }
    }

    public Long createArticle(ArticleBean item, AuthSession authSession) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();

            CustomSequenceType seq = new CustomSequenceType();
            seq.setSequenceName( "seq_WM_PORTLET_ARTICLE" );
            seq.setTableName( "WM_PORTLET_ARTICLE" );
            seq.setColumnName( "ID_SITE_CTX_ARTICLE" );
            Long id = adapter.getSequenceNextValue( seq );

            String sql_ =
                "insert into WM_PORTLET_ARTICLE"+
                 "(ID_SITE_CTX_ARTICLE, ID_USER, "+
                 "DATE_POST, IS_DELETED, NAME_ARTICLE, "+
                 "IS_PLAIN_HTML, ARTICLE_CODE, ID_SITE_SUPPORT_LANGUAGE, "+
                 "ARTICLE_DATA)"+
                "values"+
                "( ?,  ?,  ?,  ?,  ?,  ?,  ?,  ?,  ?)";

            ps = adapter.prepareStatement(sql_);

            ps.setLong(1, id );

            ps.setLong(2, authSession.getUserInfo().getUserId() );

            ps.setTimestamp(3, new java.sql.Timestamp( System.currentTimeMillis()));

            ps.setInt(4, 0 );

            if (item.getArticleName()!=null)
                ps.setString(5, item.getArticleName() );
            else
                ps.setNull(5, Types.VARCHAR);

            ps.setInt(6, item.isXml()?0:1 );
            if (item.getArticleCode()!=null)
                ps.setString(7, item.getArticleCode() );
            else
                ps.setNull(7, Types.VARCHAR);

            ps.setLong(8, item.getSiteLanguageId() );
            ps.setNull(9, Types.VARCHAR);

            ps.executeUpdate();

            /**
             * @param idRec - value of PK in main table
             * @param pkName - name PK in main table
             * @param pkType - type of PK in main table
             * @param nameTargetTable  - name of slave table
             * @param namePkTargetTable - name of PK in slave table
             * @param nameTargetField - name of filed with BigText data in slave table
             * @param insertString - insert string
             * @param isDelete - delete data from slave table before insert true/false
             */
            DatabaseManager.insertBigText(
                adapter,
                id,
                "ID_SITE_CTX_ARTICLE",
                PrimaryKeyTypeTypeType.NUMBER,
                "WM_PORTLET_ARTICLE_DATA",
                "ID_SITE_CTX_ARTICLE_DATA",
                "ARTICLE_DATA",
                item.getArticleText(),
                false
            );

            adapter.commit();
            return id;
        } catch (Throwable e) {
            try {
                if (adapter!=null)
                    adapter.rollback();
            }
            catch(Throwable th) {
                // catch rollback error
            }
            String es = "Error create article";
            log.error(es, e);
            throw new IllegalStateException( es, e);
        } finally {
            DatabaseManager.close(adapter, rs, ps);
        }
    }

    public void updateArticle(ArticleBean article, AuthSession authSession) {
        String sql_ =
            "update WM_PORTLET_ARTICLE "+
            "set"+
            "    ID_USER=?, "+
            "    NAME_ARTICLE=?, "+
            "    IS_PLAIN_HTML=?, "+
            "    ARTICLE_CODE=?, "+
            "    ID_SITE_SUPPORT_LANGUAGE=?, "+
            "    ARTICLE_DATA=? "+
            "where ID_SITE_CTX_ARTICLE=?";

        PreparedStatement ps = null;
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();

            ps = adapter.prepareStatement(sql_);

            ps.setLong(1, authSession.getUserInfo().getUserId() );

            if (article.getArticleName()!=null)
                ps.setString(2, article.getArticleName() );
            else
                ps.setNull(2, Types.VARCHAR);

            ps.setInt(3, article.isXml()?0:1 );
            
            if (article.getArticleCode()!=null)
                ps.setString(4, article.getArticleCode() );
            else
                ps.setNull(4, Types.VARCHAR);

            ps.setLong(5, article.getSiteLanguageId() );

            ps.setNull(6, Types.VARCHAR);

            // prepare PK
            ps.setLong(7, article.getArticleId() );

            ps.executeUpdate();

            /**
             * @param idRec - value of PK in main table
             * @param pkName - name PK in main table
             * @param pkType - type of PK in main table
             * @param nameTargetTable  - name of slave table
             * @param namePkTargetTable - name of PK in slave table
             * @param nameTargetField - name of filed with BigText data in slave table
             * @param insertString - insert string
             * @param isDelete - delete data from slave table before insert true/false
             */
            DatabaseManager.insertBigText(
                adapter,
                article.getArticleId(),
                "ID_SITE_CTX_ARTICLE",
                PrimaryKeyTypeTypeType.NUMBER,
                "WM_PORTLET_ARTICLE_DATA",
                "ID_SITE_CTX_ARTICLE_DATA",
                "ARTICLE_DATA",
                article.getArticleText(),
                true
            );

            adapter.commit();
        }
        catch (Exception e) {
            try {
                if( adapter != null )
                    adapter.rollback();
            }
            catch( Exception e001 ) {
                //catch rollback error
            }
            String es = "Error delete article";
            log.error( es, e );
            throw new IllegalStateException( es, e );
       }
       finally {
            DatabaseManager.close(adapter, ps);
       }
    }

    public void deleteArticle(Long articleId) {
        if (articleId==null) {
            return;
        }

        DatabaseAdapter dbDyn = null;
        try {

            dbDyn = DatabaseAdapter.getInstance();

            DatabaseManager.runSQL(
                dbDyn,
                "update WM_PORTLET_ARTICLE set IS_DELETED=1 where ID_SITE_CTX_ARTICLE=?",
                new Object[]{articleId}, new int[]{Types.DECIMAL}
            );

            dbDyn.commit();
        }
        catch( Exception e ) {
            try {
                if( dbDyn != null )
                    dbDyn.rollback();
            }
            catch( Exception e001 ) {
                //catch rollback error
            }
            String es = "Error delete article";
            log.error( es, e );
            throw new IllegalStateException( es, e );
        }
        finally {
            DatabaseManager.close( dbDyn);
        }
    }

    private String initArticleText(Long articleId) {
        if (articleId==null) {
            return "";
        }

        PreparedStatement ps = null;
        ResultSet rs = null;

        DatabaseAdapter db_ = null;
        try {
            db_ = DatabaseAdapter.getInstance();

            ps = db_.prepareStatement(
                "select  ARTICLE_DATA " +
                "from    WM_PORTLET_ARTICLE_DATA " +
                "where   ID_SITE_CTX_ARTICLE=? " +
                "order by ID_SITE_CTX_ARTICLE_DATA ASC"
            );
            RsetTools.setLong(ps, 1, articleId );

            rs = ps.executeQuery();

            StringBuilder sb = new StringBuilder();
            while (rs.next()) {
                sb.append( RsetTools.getString(rs, "ARTICLE_DATA") );
            }
            return sb.toString();
        }
        catch (Throwable e) {
            final String es = "Error get text for article";
            log.error(es, e);
            throw new RuntimeException( es, e );
        }
        finally {
            DatabaseManager.close(db_, rs, ps);
        }
    }
}
