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



import org.apache.log4j.Logger;



public class ForumConfig

{

    private static Logger log = Logger.getLogger( ForumConfig.class );



    private static CacheFactory cache = new CacheFactory( ForumConfig.class.getName() );



    public ForumConfig(){}



    public static ForumConfig getInstance(DatabaseAdapter db_, long id)

            throws Exception

    {

        return getInstance(db_, new Long(id) );

    }



    public static ForumConfig getInstance(DatabaseAdapter db_, Long id)

            throws Exception

    {

        return (ForumConfig) cache.getInstanceNew(db_, id);

    }



    public Long id_forum = null;

    public boolean isAllThread = false;

    public boolean isUseLocale = false;

    public String nameForum = "";

    public boolean isUseProperties = false;





    static String sql_ = null;

    static

    {

        sql_ = "select * from main_forum where id_forum = ? ";



        try

        {

            ForumConfig obj = new ForumConfig();

            org.riverock.sql.cache.SqlStatement.registerSql( sql_,  obj.getClass() );

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



    public ForumConfig(DatabaseAdapter db_, Long id_forum_)

    {

        PreparedStatement ps = null;

        ResultSet rs = null;

        try

        {

            ps = db_.prepareStatement(sql_);

            RsetTools.setLong(ps, 1, id_forum_ );

            rs = ps.executeQuery();



            if (rs.next())

            {

                id_forum = id_forum_;

                isAllThread = (

                    RsetTools.getInt(rs, "is_all_thread", new Integer(0)).intValue() == 0

                    ?false

                    :true

                    );

                isUseLocale = (

                    RsetTools.getInt(rs, "is_use_locale", new Integer(0)).intValue() == 0

                    ?false

                    :true

                    );

                nameForum = RsetTools.getString(rs, "name_forum", "");

                isUseProperties = (

                    RsetTools.getInt(rs, "is_use_properties", new Integer(0)).intValue() == 0

                    ?false

                    :true

                    );

            }

        }

        catch (Exception e)

        {

            log.error("Exception in ForumConfig(DatabaseAdapter db_, Long id_forum_)", e);

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

                if (ps != null)

                {

                    ps.close();

                    ps = null;

                }

            }

            catch (SQLException e2)

            {

            }

        }

    }



}

