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



/**

 * $Id$

 */

package org.riverock.portlet.portlets;



import java.sql.PreparedStatement;

import java.sql.ResultSet;



import org.riverock.common.tools.RsetTools;

import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.generic.db.DatabaseManager;

import org.riverock.generic.main.CacheFactory;

import org.riverock.portlet.schema.portlet.news_block.NewsGroupType;



import org.apache.log4j.Logger;



public class NewsGroup

{

    private static Logger log = Logger.getLogger( NewsGroup.class  );



    public NewsGroupType newsGroup = new NewsGroupType();



    private static CacheFactory cache = new CacheFactory( NewsGroup.class.getName() );



    protected void finalize() throws Throwable

    {

        super.finalize();

    }



    public NewsGroup()

    {

    }



    public static NewsGroup getInstance(DatabaseAdapter db__, long id__)

            throws Exception

    {

        return getInstance(db__, new Long(id__) );

    }



    public static NewsGroup getInstance(DatabaseAdapter db__, Long id__)

            throws Exception

    {

        return (NewsGroup) cache.getInstanceNew(db__, id__);

    }



    static String sql_ = null;

    static

    {

        sql_ =

            "select a.* from MAIN_LIST_NEWS a where a.IS_DELETED=0 and a.ID_NEWS=?";



        try

        {

            org.riverock.sql.cache.SqlStatement.registerSql( sql_, new NewsGroup().getClass() );

        }

        catch(Exception e)

        {

            log.error("Exception in registerSql, sql\n"+sql_, e);

        }

        catch(Error e)

        {

            log.error("Error in registerSql, sql\n"+sql_, e);

        }

    }



    public NewsGroup(DatabaseAdapter db_, Long id_)

            throws PortletException

    {

        if (log.isDebugEnabled())

            log.debug("start create object of NewsGroup");



        PreparedStatement ps = null;

        ResultSet rs = null;



        try

        {

            newsGroup.setNewsGroupId( id_ );



            if (log.isDebugEnabled())

                log.debug("ID_NEWS - "+id_+" newsGroupId - "+ newsGroup.getNewsGroupId() );



            ps = db_.prepareStatement(sql_);

            RsetTools.setLong(ps, 1, newsGroup.getNewsGroupId() );



            rs = ps.executeQuery();

            if (rs.next())

            {

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

        catch (Exception e)

        {

            log.error("Exception create NewsGroup",e);

            throw new PortletException(e.toString());

        }

        catch (Error e)

        {

            log.error("Error create NewsGroup",e);

            throw new PortletException(e.toString());

        }

        finally

        {

            DatabaseManager.close(rs, ps);

            rs = null;

            ps = null;

        }

    }



    static String sql1_ = null;

    static

    {

        sql1_ =

            "select a.ID from MAIN_NEWS a, MAIN_LIST_NEWS b " +

            "where b.ID_NEWS=? and a.ID_NEWS=b.ID_NEWS and a.IS_DELETED=0 " +

            "order by EDATE desc ";



        try

        {

            org.riverock.sql.cache.SqlStatement.registerSql( sql1_, new NewsGroup().getClass() );

        }

        catch(Exception e)

        {

            log.error("Exception in registerSql, sql\n"+sql1_, e);

        }

        catch(Error e)

        {

            log.error("Error in registerSql, sql\n"+sql1_, e);

        }

    }



    public void fillNewsItems(DatabaseAdapter db_)

            throws PortletException

    {

        PreparedStatement ps = null;

        ResultSet rs = null;

        try

        {

            ps = db_.prepareStatement(sql1_);

            RsetTools.setLong(ps, 1, newsGroup.getNewsGroupId() );



            rs = ps.executeQuery();

            int i = 0;

            int maxNews = newsGroup.getMaxNews()!=null?newsGroup.getMaxNews().intValue():10;

            while (rs.next() && i++ < maxNews )

            {

                Long idNews = RsetTools.getLong(rs, "ID");



                if (log.isDebugEnabled())

                    log.debug("getInstance of NewsItem. id - "+idNews);



                NewsItem item = NewsItem.getInstance(db_, idNews );



                newsGroup.addNewsItem( item.newsItem );

            }

        }

        catch (Exception e)

        {

            log.error("Exception fillNewsItems", e);

            throw new PortletException(e.toString());

        }

        catch (Error e)

        {

            log.error("Error fillNewsItems", e);

            throw new PortletException(e.toString());

        }

        finally

        {

            DatabaseManager.close(rs, ps);

            rs = null;

            ps = null;

        }

    }

}