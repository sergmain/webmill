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
import java.sql.SQLException;
import java.util.Locale;

import javax.portlet.PortletException;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;

import org.riverock.common.tools.RsetTools;
import org.riverock.common.tools.StringTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.main.CacheFactory;
import org.riverock.generic.utils.DateUtils;
import org.riverock.portlet.schema.portlet.news_block.NewsItemType;
import org.riverock.sql.cache.SqlStatement;
import org.riverock.sql.cache.SqlStatementRegisterException;


/**
 * $Id$
 */
@SuppressWarnings({"UnusedAssignment"})
public final class NewsItem {
    private final static Logger log = Logger.getLogger( NewsItem.class );

    public NewsItemType newsItem = new NewsItemType();

    private static CacheFactory cache = new CacheFactory( NewsItem.class);

    public NewsItem() {
    }

    public void reinit() {
        cache.reinit();
    }

    public void terminate(Long id) {
        cache.terminate(id);
    }

    public static NewsItem getInstance(Long id__) throws Exception {
        return (NewsItem) cache.getInstanceNew(id__);
    }

    static String sql_ = null;
    static
    {
        sql_ =
            "select a.*, c.CUSTOM_LANGUAGE " +
            "from   WM_NEWS_ITEM a, WM_NEWS_LIST b, WM_PORTAL_SITE_LANGUAGE c "+
            "where  a.ID=? and a.IS_DELETED=0 and a.ID_NEWS=b.ID_NEWS and " +
            "       b.ID_SITE_SUPPORT_LANGUAGE=c.ID_SITE_SUPPORT_LANGUAGE";

        try {
            SqlStatement.registerSql( sql_, NewsItem.class );
        }
        catch(Throwable e) {
            final String es = "Error in registerSql, sql\n"+sql_;
            log.error(es, e);
            throw new SqlStatementRegisterException( es, e );
        }
    }

    public NewsItem(Long id_) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        newsItem.setNewsItemId( id_ );
        DatabaseAdapter db_ = null;
        try {
            db_ = DatabaseAdapter.getInstance();
            ps = db_.prepareStatement(sql_);
            RsetTools.setLong(ps, 1, newsItem.getNewsItemId() );

            rs = ps.executeQuery();
            if (rs.next())
            {
                String lang = RsetTools.getString( rs, "CUSTOM_LANGUAGE");
                Locale locale = StringTools.getLocale( lang );

                newsItem.setNewsDateTime( RsetTools.getTimestamp(rs, "EDATE" ) );
                newsItem.setNewsDate(
                    DateUtils.getStringDate(newsItem.getNewsDateTime(), "dd.MMM.yyyy", locale)
                );
                if (log.isDebugEnabled()) {
                    log.debug("language of news item - "+lang);
                    log.debug("locale - "+locale);
                    log.debug("Date  - "+DateUtils.getStringDate(newsItem.getNewsDateTime(), "dd.MMM.yyyy", locale));
                    log.debug("Time  - "+DateUtils.getStringDate(newsItem.getNewsDateTime(), "HH:mm", locale));
                }

                newsItem.setNewsTime(
                    DateUtils.getStringDate(newsItem.getNewsDateTime(), "HH:mm", locale)
                );
                newsItem.setNewsHeader( StringEscapeUtils.unescapeXml( RsetTools.getString(rs, "HEADER")) );
                newsItem.setNewsAnons( StringEscapeUtils.unescapeXml(RsetTools.getString(rs, "ANONS")) );

                initTextField(db_);

            }
        }
        catch (Throwable e) {
            final String es = "Exception create NewsItem";
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
            "select TEXT from WM_NEWS_ITEM_TEXT where ID=? order by ID_MAIN_NEWS_TEXT asc";

        try {
            SqlStatement.registerSql( sql1_, NewsItem.class );
        }
        catch(Throwable e) {
            final String es = "Error in registerSql, sql\n"+sql_;
            log.error(es, e);
            throw new SqlStatementRegisterException( es, e );
        }
    }

    private void initTextField( DatabaseAdapter db_ ) throws SQLException {
        if ( newsItem.getNewsItemId() == null)
            return;

        PreparedStatement ps = null;
        ResultSet rset = null;
        try {
            ps = db_.prepareStatement( sql1_ );

            RsetTools.setLong(ps, 1, newsItem.getNewsItemId());
            rset = ps.executeQuery();
            StringBuilder text = new StringBuilder("");
            while (rset.next()) {
                text.append( RsetTools.getString(rset, "TEXT") );
            }

            if (log.isDebugEnabled()) {
                log.debug( "Result text of news: " + text.toString());
            }

            newsItem.setNewsText( text.toString() );
        }
        finally {
            DatabaseManager.close(rset, ps);
            rset = null;
            ps = null;
        }
    }
}