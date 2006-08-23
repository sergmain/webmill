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

import javax.portlet.PortletException;

import org.apache.log4j.Logger;

import org.riverock.common.tools.RsetTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.main.CacheFactory;
import org.riverock.portlet.schema.portlet.news_block.NewsGroupType;
import org.riverock.sql.cache.SqlStatement;
import org.riverock.sql.cache.SqlStatementRegisterException;

/**
 * $Id$
 */
public final class NewsGroup {
    private final static Logger log = Logger.getLogger( NewsGroup.class  );

    public NewsGroupType newsGroup = new NewsGroupType();

    private static CacheFactory cache = new CacheFactory( NewsGroup.class);

    public void reinit() {
        cache.reinit();
    }

    public NewsGroup() {
    }

    public static NewsGroup getInstance(Long id__) throws Exception {
        return (NewsGroup) cache.getInstanceNew(id__);
    }

    static String sql_ = null;
    static {
        sql_ = "select a.* from WM_NEWS_LIST a where a.IS_DELETED=0 and a.ID_NEWS=?";

        try {
            SqlStatement.registerSql( sql_, NewsGroup.class );
        }
        catch(Throwable e) {
            final String es = "Error in registerSql, sql\n"+sql_;
            log.error(es, e);
            throw new SqlStatementRegisterException( es, e );
        }
    }

    public NewsGroup(Long id_) throws PortletException {
        if (log.isDebugEnabled())
            log.debug("start create object of NewsGroup");

        getNewsGroup(id_);
    }

    private void getNewsGroup(Long newsGroupId) throws PortletException {
        PreparedStatement ps = null;
        ResultSet rs = null;

        DatabaseAdapter db_ = null;
        try {
            db_ = DatabaseAdapter.getInstance();
            newsGroup.setNewsGroupId( newsGroupId );

            if (log.isDebugEnabled())
                log.debug("ID_NEWS - "+newsGroupId +" newsGroupId - "+ newsGroup.getNewsGroupId() );

            ps = db_.prepareStatement(sql_);
            RsetTools.setLong(ps, 1, newsGroup.getNewsGroupId() );

            rs = ps.executeQuery();
            if (rs.next()) {
                newsGroup.setNewsGroupName( RsetTools.getString(rs, "NAME_NEWS") );
                newsGroup.setNewsGroupCode( RsetTools.getString(rs, "CODE_NEWS_GROUP") );
                newsGroup.setMaxNews( RsetTools.getLong(rs, "COUNT_NEWS") );

                if (log.isDebugEnabled())
                    log.debug("fillNewsItems();");

                fillNewsItems(db_);

                if (log.isDebugEnabled())
                    log.debug("newsGroupName - "+newsGroup.getNewsGroupName() + " maxNews - "+newsGroup.getMaxNews() );
            }
        }
        catch (Throwable e) {
            final String es = "Error create NewsGroup";
            log.error(es, e);
            throw new PortletException( es, e );
        }
        finally {
            DatabaseManager.close(db_, rs, ps);
            rs = null;
            ps = null;
            db_ = null;
        }
    }

    static String sql1_ = null;
    static {
        sql1_ =
            "select a.ID from WM_NEWS_ITEM a, WM_NEWS_LIST b " +
            "where b.ID_NEWS=? and a.ID_NEWS=b.ID_NEWS and a.IS_DELETED=0 " +
            "order by EDATE desc ";

        try {
            SqlStatement.registerSql( sql1_, NewsGroup.class );
        }
        catch(Throwable e) {
            final String es = "Error in registerSql, sql\n"+sql_;
            log.error(es, e);
            throw new SqlStatementRegisterException( es, e );
        }
    }

    private void fillNewsItems(DatabaseAdapter db_) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = db_.prepareStatement(sql1_);
            RsetTools.setLong(ps, 1, newsGroup.getNewsGroupId() );

            rs = ps.executeQuery();
            int i = 0;
            int maxNews = newsGroup.getMaxNews()!=null?newsGroup.getMaxNews().intValue():10;
            while (rs.next() && i++ < maxNews ) {
                Long idNews = RsetTools.getLong(rs, "ID");

                if (log.isDebugEnabled())
                    log.debug("getInstance of NewsItem. id - "+idNews);

                NewsItem item = NewsItem.getInstance(idNews );

                newsGroup.addNewsItem( item.newsItem );
            }
        }
        finally {
            DatabaseManager.close(rs, ps);
            rs = null;
            ps = null;
        }
    }
}