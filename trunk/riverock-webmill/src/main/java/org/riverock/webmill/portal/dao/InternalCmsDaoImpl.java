/*
 * org.riverock.webmill - Webmill portal with support jsr-168, xml/xslt and others things.
 * For more information, please visit project site http://webmill.riverock.org
 *
 * Copyright (C) 2000-2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
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
package org.riverock.webmill.portal.dao;

import java.sql.Types;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.schema.db.CustomSequenceType;
import org.riverock.generic.schema.db.types.PrimaryKeyTypeTypeType;
import org.riverock.interfaces.portal.bean.Article;
import org.riverock.interfaces.portal.bean.NewsGroup;
import org.riverock.interfaces.portal.bean.News;
import org.riverock.common.tools.RsetTools;
import org.riverock.webmill.portal.bean.ArticleBean;
import org.riverock.webmill.portal.bean.NewsGroupBean;
import org.riverock.webmill.portal.bean.NewsBean;

/**
 * @author Sergei Maslyukov
 *         Date: 24.05.2006
 *         Time: 18:31:00
 */
@SuppressWarnings({"UnusedAssignment"})
public class InternalCmsDaoImpl implements InternalCmsDao {
    private final static Logger log = Logger.getLogger(InternalCmsDaoImpl.class);

    public void deleteArticleForSite(DatabaseAdapter adapter, Long siteId) {
        try {
            DatabaseManager.runSQL(
                adapter,
                "delete from WM_PORTLET_ARTICLE_DATA " +
                    "where ID_SITE_CTX_ARTICLE in " +
                    "(select a.ID_SITE_CTX_ARTICLE from WM_PORTLET_ARTICLE a, WM_PORTAL_SITE_LANGUAGE b " +
                    " where b.ID_SITE=? and a.ID_SITE_SUPPORT_LANGUAGE=b.ID_SITE_SUPPORT_LANGUAGE)",

                new Object[]{siteId}, new int[]{Types.DECIMAL}
            );

            DatabaseManager.runSQL(
                adapter,
                "delete from WM_PORTLET_ARTICLE " +
                    "where ID_SITE_SUPPORT_LANGUAGE in " +
                    "(select ID_SITE_SUPPORT_LANGUAGE from WM_PORTAL_SITE_LANGUAGE where ID_SITE=?)",

                new Object[]{siteId}, new int[]{Types.DECIMAL}
            );
        } catch (SQLException e) {
            String es = "Error delete articles";
            log.error(es, e);
            throw new IllegalStateException( es, e);
        }
    }

    public void deleteArticleForSiteLanguage(DatabaseAdapter adapter, Long siteLanguageId) {
        try {
            DatabaseManager.runSQL(
                adapter,
                "delete from WM_PORTLET_ARTICLE_DATA " +
                    "where ID_SITE_CTX_ARTICLE in " +
                    "(select a.ID_SITE_CTX_ARTICLE from WM_PORTLET_ARTICLE a " +
                    " where a.ID_SITE_SUPPORT_LANGUAGE=?)",

                new Object[]{siteLanguageId}, new int[]{Types.DECIMAL}
            );

            DatabaseManager.runSQL(
                adapter,
                "delete from WM_PORTLET_ARTICLE " +
                    "where ID_SITE_SUPPORT_LANGUAGE=? ",

                new Object[]{siteLanguageId}, new int[]{Types.DECIMAL}
            );
        } catch (SQLException e) {
            String es = "Error delete articles";
            log.error(es, e);
            throw new IllegalStateException( es, e);
        }
    }

    public void deleteNewsForSite(DatabaseAdapter adapter, Long siteId) {

        try {
            DatabaseManager.runSQL(
                adapter,
                "delete from WM_NEWS_ITEM_TEXT " +
                    "where ID in " +
                    "(select a.ID from WM_NEWS_ITEM a, WM_NEWS_LIST b, WM_PORTAL_SITE_LANGUAGE c " +
                    "where c.ID_SITE=? and b.ID_SITE_SUPPORT_LANGUAGE=c.ID_SITE_SUPPORT_LANGUAGE and" +
                    "      b.ID_NEWS=a.ID_NEWS )",

                new Object[]{siteId}, new int[]{Types.DECIMAL}
            );

            DatabaseManager.runSQL(
                adapter,
                "delete from WM_NEWS_ITEM " +
                    "where ID_NEWS in " +
                    "(select b.ID_NEWS from WM_NEWS_LIST b, WM_PORTAL_SITE_LANGUAGE c " +
                    " where c.ID_SITE=? and b.ID_SITE_SUPPORT_LANGUAGE=c.ID_SITE_SUPPORT_LANGUAGE ) ",
                new Object[]{siteId}, new int[]{Types.DECIMAL}
            );

            DatabaseManager.runSQL(
                adapter,
                "delete from WM_NEWS_LIST " +
                    "where ID_SITE_SUPPORT_LANGUAGE in " +
                    "(select ID_SITE_SUPPORT_LANGUAGE from WM_PORTAL_SITE_LANGUAGE where ID_SITE=?)",

                new Object[]{siteId}, new int[]{Types.DECIMAL}
            );
        } catch (SQLException e) {
            String es = "Error delete news";
            log.error(es, e);
            throw new IllegalStateException( es, e);
        }
    }

    public void deleteNewsForSiteLanguage(DatabaseAdapter adapter, Long siteLanguageId) {

        try {
            DatabaseManager.runSQL(
                adapter,
                "delete from WM_NEWS_ITEM_TEXT " +
                    "where ID in " +
                    "(select a.ID from WM_NEWS_ITEM a, WM_NEWS_LIST b " +
                    " where b.ID_SITE_SUPPORT_LANGUAGE=? and b.ID_NEWS=a.ID_NEWS )",

                new Object[]{siteLanguageId}, new int[]{Types.DECIMAL}
            );

            DatabaseManager.runSQL(
                adapter,
                "delete from WM_NEWS_ITEM " +
                    "where ID_NEWS in " +
                    "(select b.ID_NEWS from WM_NEWS_LIST b where b.ID_SITE_SUPPORT_LANGUAGE=? ) ",
                new Object[]{siteLanguageId}, new int[]{Types.DECIMAL}
            );

            DatabaseManager.runSQL(
                adapter,
                "delete from WM_NEWS_LIST " +
                    "where ID_SITE_SUPPORT_LANGUAGE=? ",

                new Object[]{siteLanguageId}, new int[]{Types.DECIMAL}
            );
        } catch (SQLException e) {
            String es = "Error delete news";
            log.error(es, e);
            throw new IllegalStateException( es, e);
        }
    }

    public List<Article> getArticleList(Long siteLanguageId, boolean isXml) {
        List<ArticleBean> list = new ArrayList<ArticleBean>();
        if (siteLanguageId==null) {
            return (List)list;
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
                article.setPostDate( RsetTools.getTimestamp(rs, "DATE_POST") );
                article.setPlain(!isXml);

                article.setArticleData( initArticleText(db_, article.getArticleId()) );

                list.add(article);
            }
            return (List)list;
        }
        catch (Throwable e) {
            final String es = "Error create list of article";
            log.error(es, e);
            throw new RuntimeException( es, e );
        }
        finally {
            DatabaseManager.close(db_, rs, ps);
            rs=null;
            ps=null;
            db_=null;
        }
    }

    public Article getArticle(Long articleId) {
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
                article.setPostDate( RsetTools.getTimestamp(rs, "DATE_POST") );
                article.setPlain( RsetTools.getInt(rs, "IS_PLAIN_HTML", 0)==1 );

                article.setArticleData( initArticleText(db_, article.getArticleId()) );

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
            rs=null;
            ps=null;
            db_=null;
        }
    }

    public Long createArticle(Article item) {
        if (true) throw new RuntimeException("not supported");
        
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

            ps.setLong(2, 0 );

            ps.setTimestamp(3, new java.sql.Timestamp( System.currentTimeMillis()));

            ps.setInt(4, 0 );

            if (item.getArticleName()!=null)
                ps.setString(5, item.getArticleName() );
            else
                ps.setNull(5, Types.VARCHAR);

            ps.setInt(6, item.isPlain()?1:0 );
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
                item.getArticleData(),
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
            rs=null;
            ps=null;
            adapter=null;
        }
    }

    public void updateArticle(Article article) {
        if (true) throw new RuntimeException("not supported");

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

            ps.setLong(1, 1 );

            if (article.getArticleName()!=null)
                ps.setString(2, article.getArticleName() );
            else
                ps.setNull(2, Types.VARCHAR);

            ps.setInt(3, article.isPlain()?1:0 );

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
                article.getArticleData(),
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
            String es = "Error update article";
            log.error( es, e );
            throw new IllegalStateException( es, e );
       }
       finally {
            DatabaseManager.close(adapter, ps);
            ps=null;
            adapter=null;
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
            DatabaseManager.close(dbDyn);
            dbDyn=null;
        }
    }

    private String initArticleText(DatabaseAdapter db_, Long articleId) {
        if (articleId==null) {
            return "";
        }

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
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
            DatabaseManager.close(rs, ps);
            rs=null;
            ps=null;
        }
    }

    public List<NewsGroup> getNewsGroupList(Long siteLanguageId) {
          List<NewsGroupBean> list = new ArrayList<NewsGroupBean>();
          if (siteLanguageId==null) {
              return (List)list;
          }

          PreparedStatement ps = null;
          ResultSet rs = null;

          DatabaseAdapter db_ = null;
          try {
              db_ = DatabaseAdapter.getInstance();

              ps = db_.prepareStatement(
                  "select ID_NEWS, NAME_NEWS, COUNT_NEWS, ORDER_FIELD, IS_DELETED, CODE_NEWS_GROUP, ID_SITE_SUPPORT_LANGUAGE " +
                  "from   WM_NEWS_LIST " +
                  "where  IS_DELETED=0 and ID_SITE_SUPPORT_LANGUAGE=?"
              );
              RsetTools.setLong(ps, 1, siteLanguageId );

              rs = ps.executeQuery();

              while (rs.next()) {
                  NewsGroupBean newsGroup = new NewsGroupBean();
                  newsGroup.setSiteLanguageId(siteLanguageId);

                  newsGroup.setCountNewsPerGroup( RsetTools.getInt(rs, "COUNT_NEWS", 0));
                  newsGroup.setDeleted( RsetTools.getInt(rs, "IS_DELETED", 0)==1 );
                  newsGroup.setNewsGroupCode( RsetTools.getString(rs, "CODE_NEWS_GROUP") );
                  newsGroup.setNewsGroupId( RsetTools.getLong(rs, "ID_NEWS") );
                  newsGroup.setNewsGroupName( RsetTools.getString(rs, "NAME_NEWS") );
                  newsGroup.setOrderField( RsetTools.getInt(rs, "ORDER_FIELD") );

                  list.add(newsGroup);
              }
              return (List)list;
          }
          catch (Throwable e) {
              final String es = "Error create list of NewsGroupBean";
              log.error(es, e);
              throw new RuntimeException( es, e );
          }
          finally {
              DatabaseManager.close(db_, rs, ps);
              rs = null;
              ps = null;
              db_ = null;
          }
      }

      public List<News> getNewsList(Long newsGroupId) {
          List<NewsBean> list = new ArrayList<NewsBean>();
          if (newsGroupId==null) {
              return (List)list;
          }

          PreparedStatement ps = null;
          ResultSet rs = null;

          DatabaseAdapter db_ = null;
          try {
              db_ = DatabaseAdapter.getInstance();

              ps = db_.prepareStatement(
                  "select ID, ID_NEWS, EDATE, HEADER, ANONS, IS_DELETED " +
                  "from   WM_NEWS_ITEM " +
                  "where  IS_DELETED=0 and ID_NEWS=?"
              );
              RsetTools.setLong(ps, 1, newsGroupId );

              rs = ps.executeQuery();

              while (rs.next()) {
                  NewsBean news = new NewsBean();
                  news.setNewsGroupId(newsGroupId);

                  news.setNewsId(RsetTools.getLong(rs, "ID"));
                  news.setDeleted( RsetTools.getInt(rs, "IS_DELETED", 0)==1 );
                  news.setPostDate( RsetTools.getTimestamp(rs, "EDATE") );

                  news.setNewsAnons( RsetTools.getString(rs, "ANONS") );
                  news.setNewsHeader( RsetTools.getString(rs, "HEADER") );
                  news.setNewsText( initNewsText(news.getNewsId()) );

                  list.add(news);
              }
              return (List)list;
          }
          catch (Throwable e) {
              final String es = "Error create list of NewsBean";
              log.error(es, e);
              throw new RuntimeException( es, e );
          }
          finally {
              DatabaseManager.close(db_, rs, ps);
              rs = null;
              ps = null;
              db_ = null;
          }
      }

      private String initNewsText(Long newsId) {
          if (newsId==null) {
              return "";
          }

          PreparedStatement ps = null;
          ResultSet rs = null;

          DatabaseAdapter db_ = null;
          try {
              db_ = DatabaseAdapter.getInstance();

              ps = db_.prepareStatement(
                  "select  TEXT " +
                  "from    WM_NEWS_ITEM_TEXT " +
                  "where   ID=? " +
                  "order by ID_MAIN_NEWS_TEXT ASC"
              );
              RsetTools.setLong(ps, 1, newsId );

              rs = ps.executeQuery();

              StringBuilder sb = new StringBuilder();
              while (rs.next()) {
                  sb.append( RsetTools.getString(rs, "TEXT") );
              }
              return sb.toString();
          }
          catch (Throwable e) {
              final String es = "Error get text for news";
              log.error(es, e);
              throw new RuntimeException( es, e );
          }
          finally {
              DatabaseManager.close(db_, rs, ps);
              rs = null;
              ps = null;
              db_ = null;
          }
      }

      public NewsGroupBean getNewsGroup(Long newsGroupId) {
          if (newsGroupId==null) {
              return null;
          }

          PreparedStatement ps = null;
          ResultSet rs = null;

          DatabaseAdapter db_ = null;
          try {
              db_ = DatabaseAdapter.getInstance();

              if (log.isDebugEnabled())
                  log.debug("ID_NEWS - "+newsGroupId +" newsGroupId - "+ newsGroupId );

              ps = db_.prepareStatement(
                  "select ID_NEWS, NAME_NEWS, COUNT_NEWS, ORDER_FIELD, IS_DELETED, CODE_NEWS_GROUP, ID_SITE_SUPPORT_LANGUAGE " +
                  "from   WM_NEWS_LIST " +
                  "where  IS_DELETED=0 and ID_NEWS=?"
              );
              RsetTools.setLong(ps, 1, newsGroupId );

              rs = ps.executeQuery();

              NewsGroupBean newsGroup=null;
              if (rs.next()) {
                  newsGroup = new NewsGroupBean();
                  newsGroup.setNewsGroupId(newsGroupId);

                  newsGroup.setSiteLanguageId(RsetTools.getLong(rs, "ID_SITE_SUPPORT_LANGUAGE"));
                  newsGroup.setCountNewsPerGroup( RsetTools.getInt(rs, "COUNT_NEWS", 0) );
                  newsGroup.setNewsGroupCode( RsetTools.getString(rs, "CODE_NEWS_GROUP") );
                  newsGroup.setNewsGroupName( RsetTools.getString(rs, "NAME_NEWS") );
                  newsGroup.setOrderField(RsetTools.getInt(rs, "ORDER_FIELD"));
              }
              return newsGroup;
          }
          catch (Throwable e) {
              final String es = "Error create NewsGroup";
              log.error(es, e);
              throw new RuntimeException( es, e );
          }
          finally {
              DatabaseManager.close(db_, rs, ps);
              rs = null;
              ps = null;
              db_ = null;
          }
      }

      public NewsBean getNews(Long newsId) {
          if (newsId==null) {
              return null;
          }

          PreparedStatement ps = null;
          ResultSet rs = null;

          DatabaseAdapter db_ = null;
          try {
              db_ = DatabaseAdapter.getInstance();

              ps = db_.prepareStatement(
                  "select ID, ID_NEWS, EDATE, HEADER, ANONS, IS_DELETED " +
                  "from   WM_NEWS_ITEM " +
                  "where  IS_DELETED=0 and ID=?"
              );
              RsetTools.setLong(ps, 1, newsId );

              rs = ps.executeQuery();

              if (rs.next()) {
                  NewsBean news = new NewsBean();
                  news.setNewsId(newsId);
                  news.setNewsGroupId(RsetTools.getLong(rs, "ID_NEWS"));

                  news.setDeleted( RsetTools.getInt(rs, "IS_DELETED", 0)==1 );
                  news.setPostDate( RsetTools.getTimestamp(rs, "EDATE") );

                  news.setNewsAnons( RsetTools.getString(rs, "ANONS") );
                  news.setNewsHeader( RsetTools.getString(rs, "HEADER") );
                  news.setNewsText( initNewsText(news.getNewsId()) );

                  return news;
              }
              return null;
          }
          catch (Throwable e) {
              final String es = "Error get NewsBean";
              log.error(es, e);
              throw new RuntimeException( es, e );
          }
          finally {
              DatabaseManager.close(db_, rs, ps);
              rs = null;
              ps = null;
              db_ = null;
          }
      }

      public Long createNews(News news) {
          PreparedStatement ps = null;
          ResultSet rs = null;
          DatabaseAdapter adapter = null;
          try {
              adapter = DatabaseAdapter.getInstance();

              CustomSequenceType seq = new CustomSequenceType();
              seq.setSequenceName( "seq_WM_NEWS_ITEM" );
              seq.setTableName( "WM_NEWS_ITEM" );
              seq.setColumnName( "ID" );
              Long id = adapter.getSequenceNextValue( seq );

              String sql_ =
                  "insert into WM_NEWS_ITEM"+
                   "(ID, ID_NEWS, EDATE, HEADER, IS_DELETED, ANONS, TEXT)"+
                  "values"+
                  "( ?,  ?,  ?,  ?,  ?,  ?,  ?)";

              ps = adapter.prepareStatement(sql_);

              ps.setLong(1, id );
              ps.setLong(2, news.getNewsGroupId() );
              if (news.getPostDate()!=null)
                  ps.setTimestamp(3, new java.sql.Timestamp( news.getPostDate().getTime()) );
              else
                  ps.setTimestamp(3, new java.sql.Timestamp( System.currentTimeMillis() ) );

              if (news.getNewsHeader()!=null)
                  ps.setString(4, news.getNewsHeader() );
              else
                  ps.setNull(4, Types.VARCHAR);

              ps.setInt(5, news.isDeleted()?1:0 );
              if (news.getNewsAnons()!=null)
                  ps.setString(6, news.getNewsAnons() );
              else
                  ps.setNull(6, Types.VARCHAR);

              ps.setNull(7, Types.VARCHAR);


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
                  "ID",
                  PrimaryKeyTypeTypeType.NUMBER,
                  "WM_NEWS_ITEM_TEXT",
                  "ID_MAIN_NEWS_TEXT",
                  "TEXT",
                  news.getNewsText(),
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
              String es = "Error create news";
              log.error(es, e);
              throw new IllegalStateException( es, e);
          } finally {
              DatabaseManager.close(adapter, rs, ps);
              rs = null;
              ps = null;
              adapter = null;
          }
      }

      public void updateNews(News news) {
          String sql_ =
              "update WM_NEWS_ITEM "+
              "set"+
              "    ID_NEWS=?, "+
              "    EDATE=?, "+
              "    HEADER=?, "+
              "    IS_DELETED=?, "+
              "    ANONS=?, "+
              "    TEXT=? "+
              "where ID=?";

          PreparedStatement ps = null;
          DatabaseAdapter adapter = null;
          try {
              adapter = DatabaseAdapter.getInstance();

              ps = adapter.prepareStatement(sql_);

              ps.setLong(1, news.getNewsGroupId() );
              if (news.getPostDate()!=null)
                  ps.setTimestamp(2, new java.sql.Timestamp( news.getPostDate().getTime()) );
              else
                  ps.setTimestamp(2, new java.sql.Timestamp( System.currentTimeMillis()) );

              if (news.getNewsHeader()!=null)
                  ps.setString(3, news.getNewsHeader() );
              else
                  ps.setNull(3, Types.VARCHAR);

              ps.setInt(4, news.isDeleted()?1:0 );
              if (news.getNewsAnons()!=null)
                  ps.setString(5, news.getNewsAnons() );
              else
                  ps.setNull(5, Types.VARCHAR);

              ps.setNull(6, Types.VARCHAR);

              // prepare PK
              ps.setLong(7, news.getNewsId() );

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
                  news.getNewsId(),
                  "ID",
                  PrimaryKeyTypeTypeType.NUMBER,
                  "WM_NEWS_ITEM_TEXT",
                  "ID_MAIN_NEWS_TEXT",
                  "TEXT",
                  news.getNewsText(),
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
              String es = "Error delete news";
              log.error( es, e );
              throw new IllegalStateException( es, e );
         }
         finally {
              DatabaseManager.close(adapter, ps);
              ps = null;
              adapter = null;
         }
      }

      public void deleteNews(Long newsId) {
          if (newsId==null) {
              return;
          }

          DatabaseAdapter dbDyn = null;
          try {

              dbDyn = DatabaseAdapter.getInstance();

              DatabaseManager.runSQL(
                  dbDyn,
                  "update WM_NEWS_ITEM set IS_DELETED=1 where ID=?",
                  new Object[]{newsId}, new int[]{Types.DECIMAL}
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
              String es = "Error delete news";
              log.error( es, e );
              throw new IllegalStateException( es, e );
          }
          finally {
              DatabaseManager.close( dbDyn);
              dbDyn = null;
          }
      }

      public Long createNewsGroup(NewsGroup item) {
          DatabaseAdapter adapter = null;
          PreparedStatement ps = null;
          ResultSet rs = null;
          try {
              adapter = DatabaseAdapter.getInstance();

              CustomSequenceType seq = new CustomSequenceType();
              seq.setSequenceName( "seq_WM_NEWS_LIST" );
              seq.setTableName( "WM_NEWS_LIST" );
              seq.setColumnName( "ID_NEWS" );
              Long id = adapter.getSequenceNextValue( seq );

              String sql_ =
                  "insert into WM_NEWS_LIST"+
                  "(ID_NEWS, NAME_NEWS, COUNT_NEWS, ORDER_FIELD, IS_DELETED, CODE_NEWS_GROUP, "+
                  "ID_SITE_SUPPORT_LANGUAGE)"+
                  "values"+
                      "( ?,  ?,  ?,  ?,  ?,  ?,  ?)";

              ps = adapter.prepareStatement(sql_);

              ps.setLong(1, id );
              if (item.getNewsGroupName()!=null)
                  ps.setString(2, item.getNewsGroupName() );
              else
                  ps.setNull(2, Types.VARCHAR);

              ps.setInt(3, item.getCountNewsPerGroup() );
              ps.setInt(4, item.getOrderField() );
              ps.setInt(5, item.isDeleted()?1:0 );

              if (item.getNewsGroupCode()!=null)
                  ps.setString(6, item.getNewsGroupCode() );
              else
                  ps.setNull(6, Types.VARCHAR);

              ps.setLong(7, item.getSiteLanguageId() );

              ps.executeUpdate();

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
              String es = "Error create news group";
              log.error(es, e);
              throw new IllegalStateException( es, e);
          } finally {
              DatabaseManager.close(adapter, rs, ps);
              rs = null;
              ps = null;
              adapter = null;
          }
      }

      public void deleteNewsGroup(Long newsGroupId) {
          if (newsGroupId==null) {
              return;
          }

          DatabaseAdapter dbDyn = null;
          try {

              dbDyn = DatabaseAdapter.getInstance();

              DatabaseManager.runSQL(
                  dbDyn,
                  "update WM_NEWS_LIST set IS_DELETED=1 where ID_NEWS=?",
                  new Object[]{newsGroupId}, new int[]{Types.DECIMAL}
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
              String es = "Error delete news group";
              log.error( es, e );
              throw new IllegalStateException( es, e );
          }
          finally {
              DatabaseManager.close( dbDyn);
              dbDyn = null;
          }
      }

      public void updateNewsGroup(NewsGroup newsGroup) {

          String sql_ =
              "update WM_NEWS_LIST "+
              "set"+
              "    NAME_NEWS=?, "+
              "    COUNT_NEWS=?, "+
              "    ORDER_FIELD=?, "+
              "    IS_DELETED=?, "+
              "    CODE_NEWS_GROUP=?, "+
              "    ID_SITE_SUPPORT_LANGUAGE=? "+
              "where ID_NEWS=?";

          PreparedStatement ps = null;
          DatabaseAdapter adapter = null;
          try {
              adapter = DatabaseAdapter.getInstance();
              ps = adapter.prepareStatement(sql_);

              if (newsGroup.getNewsGroupName()!=null)
                  ps.setString(1, newsGroup.getNewsGroupName() );
              else
                  ps.setNull(1, Types.VARCHAR);

              ps.setInt(2, newsGroup.getCountNewsPerGroup() );
              ps.setInt(3, newsGroup.getOrderField() );
              ps.setInt(4, newsGroup.isDeleted()?1:0 );
              if (newsGroup.getNewsGroupCode()!=null)
                  ps.setString(5, newsGroup.getNewsGroupCode() );
              else
                  ps.setNull(5, Types.VARCHAR);

              ps.setLong(6, newsGroup.getSiteLanguageId() );

              // prepare PK
              ps.setLong(7, newsGroup.getNewsGroupId() );

              ps.executeUpdate();

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
              String es = "Error update news group";
              log.error( es, e );
              throw new IllegalStateException( es, e );
         }
         finally {
              DatabaseManager.close(adapter, ps);
              ps = null;
              adapter = null;
         }
      }

    public Article getArticleByCode(Long siteLanguageId, String articleCode) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
