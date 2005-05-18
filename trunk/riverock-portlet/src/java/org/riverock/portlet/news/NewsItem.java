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
import java.util.Locale;

import org.riverock.common.tools.RsetTools;
import org.riverock.common.tools.StringTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.main.CacheFactory;
import org.riverock.generic.utils.DateUtils;
import org.riverock.portlet.schema.portlet.news_block.NewsItemType;
import org.riverock.cache.impl.CacheException;
import org.riverock.sql.cache.SqlStatementRegisterException;
import org.riverock.sql.cache.SqlStatement;

import org.apache.log4j.Logger;

import javax.portlet.PortletException;


/**
 * $Id$
 */
public final class NewsItem {
    private final static Logger log = Logger.getLogger( NewsItem.class );

    public NewsItemType newsItem = new NewsItemType();

    private static CacheFactory cache = new CacheFactory( NewsItem.class.getName() );

    public NewsItem()
    {
    }

    public void reinit()
    {
        cache.reinit();
    }

    public void terminate(Long id) throws CacheException
    {
        cache.terminate(id);
    }

    public static NewsItem getInstance(DatabaseAdapter db__, long id__)
        throws Exception
    {
        return getInstance(db__, new Long(id__) );
    }

    public static NewsItem getInstance(DatabaseAdapter db__, Long id__)
        throws Exception
    {
        return (NewsItem) cache.getInstanceNew(db__, id__);
    }

    static String sql_ = null;
    static
    {
        sql_ =
            "select a.*, c.CUSTOM_LANGUAGE " +
            "from MAIN_NEWS a, MAIN_LIST_NEWS b, SITE_SUPPORT_LANGUAGE c "+
            "where a.ID=? and a.IS_DELETED=0 and a.ID_NEWS=b.ID_NEWS and " +
            "b.ID_SITE_SUPPORT_LANGUAGE=c.ID_SITE_SUPPORT_LANGUAGE";

        try
        {
            SqlStatement.registerSql( sql_, new NewsItem().getClass() );
        }
        catch(Throwable e)
        {
            final String es = "Error in registerSql, sql\n"+sql_;
            log.error(es, e);
            throw new SqlStatementRegisterException( es, e );
        }
    }

    public NewsItem(DatabaseAdapter db_, Long id_)
        throws Exception
    {
        PreparedStatement ps = null;
        ResultSet rs = null;
        newsItem.setNewsItemId( id_ );
        try
        {
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
                newsItem.setNewsHeader( StringTools.toOrigin( RsetTools.getString(rs, "HEADER")) );
                newsItem.setNewsAnons( StringTools.toOrigin(RsetTools.getString(rs, "ANONS")) );

                initTextField();

            }
        }
        catch (Throwable e) {
            final String es = "Exception create NewsItem";
            log.error(es, e);
            throw new PortletException( es, e );
        }
        finally {
            DatabaseManager.close(rs, ps);
            rs = null;
            ps = null;
        }
    }

    static String sql1_ = null;
    static
    {
        sql1_ =
            "select TEXT from MAIN_NEWS_TEXT where ID=? order by ID_MAIN_NEWS_TEXT asc";

        try
        {
            SqlStatement.registerSql( sql1_, new NewsItem().getClass() );
        }
        catch(Throwable e)
        {
            final String es = "Error in registerSql, sql\n"+sql_;
            log.error(es, e);
            throw new SqlStatementRegisterException( es, e );
        }
    }

    public void initTextField()
            throws PortletException
    {
        if ( newsItem.getNewsItemId() == null)
            return;

        DatabaseAdapter db_ = null;
        PreparedStatement ps = null;
        ResultSet rset = null;
        try {
            db_ = DatabaseAdapter.getInstance(false);
            ps = db_.prepareStatement( sql1_ );

            RsetTools.setLong(ps, 1, newsItem.getNewsItemId());
            rset = ps.executeQuery();
            String text = "";
            while (rset.next()) {
                text += RsetTools.getString(rset, "TEXT");
            }
            newsItem.setNewsText( text );
        }
        catch (Throwable e) {
            final String es = "Exception initTextField";
            log.error(es, e);
            throw new PortletException( es, e );
        }
        finally {
            DatabaseManager.close(db_, rset, ps);
            rset = null;
            ps = null;
            db_ = null;
        }
    }
}