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

import org.apache.log4j.Logger;

import org.riverock.portlet.cms.news.bean.NewsGroupBean;
import org.riverock.portlet.cms.news.bean.NewsBean;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.schema.db.CustomSequenceType;
import org.riverock.generic.schema.db.types.PrimaryKeyTypeTypeType;
import org.riverock.common.tools.RsetTools;

/**
 * @author Sergei Maslyukov
 *         Date: 23.08.2006
 *         Time: 16:28:08
 */
@SuppressWarnings({"UnusedAssignment"})
public class CmsNewsDaoImpl implements CmsNewsDao {
    private final static Logger log = Logger.getLogger( CmsNewsDaoImpl.class );

    public List<NewsGroupBean> getNewsGroupList(Long siteLanguageId) {
        List<NewsGroupBean> list = new ArrayList<NewsGroupBean>();
        if (siteLanguageId==null) {
            return list;
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

                newsGroup.setMaxNews( RsetTools.getLong(rs, "COUNT_NEWS"));
                newsGroup.setDeleted( RsetTools.getInt(rs, "IS_DELETED", 0)==1 );
                newsGroup.setNewsGroupCode( RsetTools.getString(rs, "CODE_NEWS_GROUP") );
                newsGroup.setNewsGroupId( RsetTools.getLong(rs, "ID_NEWS") );
                newsGroup.setNewsGroupName( RsetTools.getString(rs, "NAME_NEWS") );
                newsGroup.setOrderValue( RsetTools.getInt(rs, "ORDER_FIELD") );

                list.add(newsGroup);
            }
            return list;
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

    public List<NewsBean> getNewsList(Long newsGroupId) {
        List<NewsBean> list = new ArrayList<NewsBean>();
        if (newsGroupId==null) {
            return list;
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
                news.setNewsDate( RsetTools.getTimestamp(rs, "EDATE") );

                news.setNewsAnons( RsetTools.getString(rs, "ANONS") );
                news.setNewsHeader( RsetTools.getString(rs, "HEADER") );
                news.setNewsText( initNewsText(news.getNewsId()) );

                list.add(news);
            }
            return list;
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
                newsGroup.setMaxNews( RsetTools.getLong(rs, "COUNT_NEWS") );
                newsGroup.setNewsGroupCode( RsetTools.getString(rs, "CODE_NEWS_GROUP") );
                newsGroup.setNewsGroupName( RsetTools.getString(rs, "NAME_NEWS") );
                newsGroup.setOrderValue(RsetTools.getInt(rs, "ORDER_FIELD"));
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
                news.setNewsDate( RsetTools.getTimestamp(rs, "EDATE") );

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

    public Long createNews(NewsBean news) {
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
            if (news.getNewsDate()!=null)
                ps.setTimestamp(3, new java.sql.Timestamp( news.getNewsDate().getTime()) );
            else
                ps.setNull(3, Types.DATE);

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

    public void updateNews(NewsBean news) {
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
            if (news.getNewsDate()!=null)
                ps.setTimestamp(2, new java.sql.Timestamp( news.getNewsDate().getTime()) );
            else
                ps.setNull(2, Types.DATE);

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

    public Long createNewsGroup(NewsGroupBean item) {
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

            if (item.getMaxNews() != null )
                ps.setInt(3, item.getMaxNews().intValue() );
            else
                ps.setNull(3, Types.INTEGER);

            if (item.getOrderValue() != null )
                ps.setInt(4, item.getOrderValue() );
            else
                ps.setNull(4, Types.INTEGER);

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

    public void updateNewsGroup(NewsGroupBean newsGroup) {

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

            if (newsGroup.getMaxNews() != null )
                ps.setInt(2, newsGroup.getMaxNews().intValue() );
            else
                ps.setNull(2, Types.INTEGER);

            if (newsGroup.getOrderValue() != null )
                ps.setInt(3, newsGroup.getOrderValue() );
            else
                ps.setNull(3, Types.INTEGER);

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
}
