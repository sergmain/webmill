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



package org.riverock.portlet.forum;



import java.sql.PreparedStatement;

import java.sql.ResultSet;

import java.sql.SQLException;



import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.generic.main.CacheFactory;

import org.riverock.common.tools.RsetTools;

import org.riverock.portlet.portlets.utils.RsetUtils;



import org.apache.log4j.Logger;



public class SimpleForumMessage extends ForumMessage

{

    private static Logger log = Logger.getLogger( SimpleForumMessage.class );



    private static CacheFactory cache = new CacheFactory( SimpleForumMessage.class.getName() );



    public SimpleForumMessage(){}



    public static SimpleForumMessage getInstance(DatabaseAdapter db__, Long id__)

            throws Exception

    {

        if (id__==null)

            return null;



        return (SimpleForumMessage) cache.getInstanceNew(db__, id__ );

    }



    public SimpleForumMessage(DatabaseAdapter db__, Long id_)

            throws Exception

    {

        PreparedStatement st = null;

        ResultSet rs = null;

        try

        {

            String sql_ =

                "select  a.* " +

                "from " + (new SimpleForum()).getNameTable() + " a " +

                "where   a.id = ? ";



            org.riverock.sql.cache.SqlStatement.registerSql( sql_, this.getClass() );



/*

	"select  a.* "+

	"from " + (new SimpleForum()).getNameTable() + " a, " +

	(new SimpleForum()).getNameTable() + " b " +

	"where   b.id = ? and a.id_thread = b.id_thread "+

	"order by a.date_post asc";

*/



//cat.debug("SQL: "+sql_);



            st = db__.prepareStatement(sql_);

            st.setLong(1, id_.longValue());

            rs = st.executeQuery();



//cat.debug("ID #1.002: "+id_);



            if (rs.next())

            {

                header = RsetTools.getString(rs, "header", "");

                fio = RsetTools.getString(rs, "fio", "");

                email = RsetTools.getString(rs, "email", "");

                ip = RsetTools.getString(rs, "ip", "");



                id = RsetTools.getLong(rs, "id");

                id_main = RsetTools.getLong(rs, "id_main");

                id_forum = RsetTools.getLong(rs, "id_forum");

                id_thread = RsetTools.getLong(rs, "id_thread");



                datePost = RsetTools.getCalendar(rs, "date_post");



//                text = RsetTools.getString(rs, "MESSAGE_TEXT", "");

                text = RsetUtils.getBigTextField(db__, id, "MESSAGE_TEXT", "MAIN_FORUM_TEXT", "ID", "ID_MAIN_FORUM_TEXT");



                isOk = true;

            }

        }

        catch (SQLException e)

        {

            log.error("Exception in SimpleForumMessage(...", e);

        }

        finally

        {

            try

            {

                if (rs != null)

                {

                    rs.close();

                    rs = null;

                }

            }

            catch (SQLException e1)

            {

            }



            try

            {

                if (st != null)

                {

                    st.close();

                    st = null;

                }

            }

            catch (SQLException e2)

            {

            }

        }

    }

}

