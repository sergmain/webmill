/*

 * org.riverock.webmill -- Portal framework implementation

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

 *

 * $Revision$

 * $Date$

 * $RCSfile$

 *

 */

package org.riverock.webmill.main;



import java.sql.PreparedStatement;

import java.sql.ResultSet;

import java.util.Calendar;



import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.generic.db.DatabaseManager;

import org.riverock.generic.main.CacheFactory;

import org.riverock.common.tools.RsetTools;



import org.apache.log4j.Logger;



public class ContentCSS

{

    private static Logger log = Logger.getLogger( ContentCSS.class );



    public String css = "";

    public long id_site;

    public Calendar datePost = null;



    private static CacheFactory cache = new CacheFactory( ContentCSS.class.getName() );



    protected void finalize() throws Throwable

    {

        css = null;

        datePost = null;



        super.finalize();

    }



    public ContentCSS(){}



    public void terminate(Long id)

    {

        cache.reinit();

    }



    public void reinit()

    {

        cache.reinit();

    }



    public static ContentCSS getInstance(DatabaseAdapter db__, long id__)

            throws Exception

    {

        return (ContentCSS) cache.getInstanceNew(db__, new Long(id__) );

    }



    public static ContentCSS getInstance(DatabaseAdapter db__, Long id__)

            throws Exception

    {

        return (ContentCSS) cache.getInstanceNew(db__, id__);

    }



    static String sql_ = null;

    static

    {

        sql_ =

            "select a.date_post, b.css_data " +

            "from site_content_css a, site_content_css_data b " +

            "where a.ID_SITE=? and a.is_current=1 and " +

            "a.id_site_content_css=b.id_site_content_css " +

            "order by ID_SITE_CONTENT_CSS_DATA asc";



        try

        {

            ContentCSS obj = new ContentCSS();

            org.riverock.sql.cache.SqlStatement.registerSql( sql_, obj.getClass() );

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



    public ContentCSS(DatabaseAdapter db_, Long id_)

            throws Exception

    {

        if (id_ == null)

            return;



        id_site = id_.longValue();



        if (id_site == -1)

            return;



        PreparedStatement ps = null;

        ResultSet rset = null;

        boolean isFirstRecord = true;

        try

        {

            ps = db_.prepareStatement( sql_ );



            ps.setLong(1, id_site);

            rset = ps.executeQuery();

            css = "";

            while (rset.next())

            {

                if (isFirstRecord)

                {

                    datePost = RsetTools.getCalendar(rset, "DATE_POST");

                    isFirstRecord = false;

                }



                css += RsetTools.getString(rset, "CSS_DATA");

            }

        }

        catch (Exception e)

        {

            log.error("Exception in ContentCSS()", e);

            throw e;

        }

        finally

        {

            DatabaseManager.close(rset, ps);

            rset = null;

            ps = null;

        }

    }

}