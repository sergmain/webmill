/*

 * org.riverock.sso -- Single Sign On implementation

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

 * User: Admin

 * Date: Sep 6, 2003

 * Time: 10:33:28 PM

 *

 * $Id$

 */



package org.riverock.sso.utils;



import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.generic.db.DatabaseManager;

import org.riverock.common.tools.RsetTools;

import org.apache.log4j.Logger;



import java.sql.SQLException;

import java.sql.PreparedStatement;

import java.sql.ResultSet;

import java.util.List;

import java.util.ArrayList;



/**

 * User: smaslyukov

 * Date: 03.08.2004

 * Time: 14:57:19

 * $Id$

 */

public class AuthHelper {

    private static Logger log = Logger.getLogger(AuthHelper.class );



    public static String getGrantedUserId(DatabaseAdapter adapter, String username)

        throws SQLException

    {

        List list = getGrantedUserIdList(adapter, username);

        if (list.size()==0)

            return "NULL";



        String r = "";

        for (int i=0; i<list.size(); i++)

        {

            if (r.length()!=0)

                r += ",";



            r += list.get(i).toString();

        }

        return r;

    }



    public static List getGrantedUserIdList(DatabaseAdapter adapter, String username)

        throws SQLException

    {

        PreparedStatement ps = null;

        ResultSet rs = null;

        try {

            String sql_ =

                "select ID_USER from AUTH_USER where USER_LOGIN=? ";



            ps = adapter.prepareStatement(sql_);

            ps.setString(1, username);



            rs = ps.executeQuery();



            List list = new ArrayList();

            while(rs.next())

            {

                Long id = RsetTools.getLong(rs, "ID_USER" );

                if (id==null)

                    continue;

                list.add( id );

            }

            return list;

        }

        catch(Exception e)

        {

            log.error("Exception get userID list", e);

            throw new SQLException(e.getMessage());

        }

        finally {

            DatabaseManager.close(rs, ps);

            rs = null;

            ps = null;

        }

    }



    public static String getGrantedFirmId(DatabaseAdapter adapter, String username)

        throws SQLException

    {

        List list = getGrantedFirmIdList(adapter, username);

        if (list.size()==0)

            return "NULL";



        String r = "";

        for (int i=0; i<list.size(); i++)

        {

            if (r.length()!=0)

                r += ", ";



            r += list.get(i).toString();

        }

        return r;

    }



    public static List getGrantedFirmIdList(DatabaseAdapter adapter, String username)

        throws SQLException

    {

        PreparedStatement ps = null;

        ResultSet rs = null;

        try {

            String sql_ =

                "select  a01.id_firm " +

                "from    auth_user a01 " +

                "where   a01.is_use_current_firm = 1 and a01.user_login=? " +

                "union " +

                "select  d02.id_firm " +

                "from    auth_user a02, main_relate_service_firm d02 " +

                "where   a02.is_service = 1 and a02.id_service = d02.id_service and a02.user_login=? " +

                "union " +

                "select  e03.id_firm " +

                "from    auth_user a03, main_relate_road_service d03, main_relate_service_firm e03 " +

                "where   a03.is_road = 1 and a03.id_road = d03.id_road and d03.id_service = e03.id_service and " +

                "        a03.user_login=? " +

                "union " +

                "select  b04.id_firm " +

                "from    auth_user a04, main_list_firm b04 " +

                "where   a04.is_root = 1 and a04.user_login=? ";



            ps = adapter.prepareStatement(sql_);

            ps.setString(1, username);

            ps.setString(2, username);

            ps.setString(3, username);

            ps.setString(4, username);



            rs = ps.executeQuery();



            List list = new ArrayList();

            while(rs.next())

            {

                Long id = RsetTools.getLong(rs, "ID_FIRM" );

                if (id==null)

                    continue;

                list.add( id );

            }

            return list;

        }

        catch(Exception e)

        {

            log.error("Exception get firmID list", e);

            throw new SQLException(e.getMessage());

        }

        finally {

            DatabaseManager.close(rs, ps);

            rs = null;

            ps = null;

        }

    }



    public static String getGrantedServiceId(DatabaseAdapter adapter, String username)

        throws SQLException

    {

        List list = getGrantedServiceIdList(adapter, username);

        if (list.size()==0)

            return "NULL";



        String r = "";

        for (int i=0; i<list.size(); i++)

        {

            if (r.length()!=0)

                r += ", ";



            r += list.get(i).toString();

        }

        return r;

    }



    public static List getGrantedServiceIdList(DatabaseAdapter adapter, String username)

        throws SQLException

    {

        PreparedStatement ps = null;

        ResultSet rs = null;

        try {

            String sql_ =

                "select  a01.id_service "+

                "from    auth_user a01 "+

                "where   a01.is_service = 1 and a01.user_login=?  "+

                "union "+

                "select  b02.id_service "+

                "from    auth_user a02, main_relate_road_service b02 "+

                "where   a02.is_road = 1 and a02.id_road = b02.id_road and a02.user_login=?  "+

                "union "+

                "select  b04.id_service "+

                "from    auth_user a04, main_list_service b04 "+

                "where   a04.is_root = 1 and a04.user_login=? ";





            ps = adapter.prepareStatement(sql_);

            ps.setString(1, username);

            ps.setString(2, username);

            ps.setString(3, username);



            rs = ps.executeQuery();



            List list = new ArrayList();

            while(rs.next())

            {

                Long id = RsetTools.getLong(rs, "id_service" );

                if (id==null)

                    continue;

                list.add( id );

            }

            return list;

        }

        catch(Exception e)

        {

            log.error("Exception get serviceID list", e);

            throw new SQLException(e.getMessage());

        }

        finally {

            DatabaseManager.close(rs, ps);

            rs = null;

            ps = null;

        }

    }



    public static String getGrantedRoadId(DatabaseAdapter adapter, String username)

        throws SQLException

    {

        List list = getGrantedRoadIdList(adapter, username);

        if (list.size()==0)

            return "NULL";



        String r = "";

        for (int i=0; i<list.size(); i++)

        {

            if (r.length()!=0)

                r += ", ";



            r += list.get(i).toString();

        }

        return r;

    }



    public static List getGrantedRoadIdList(DatabaseAdapter adapter, String username)

        throws SQLException

    {

        PreparedStatement ps = null;

        ResultSet rs = null;

        try {

            String sql_ =

                "select  a01.id_road "+

                "from    auth_user a01 "+

                "where   a01.is_road=1 and a01.user_login=?  "+

                "union "+

                "select  b04.id_road "+

                "from    auth_user a04, main_list_road b04 "+

                "where   a04.is_root=1 and a04.user_login=?";



            ps = adapter.prepareStatement(sql_);

            ps.setString(1, username);

            ps.setString(2, username);



            rs = ps.executeQuery();



            List list = new ArrayList();

            while(rs.next())

            {

                Long id = RsetTools.getLong(rs, "id_road" );

                if (id==null)

                    continue;

                list.add( id );

            }

            return list;

        }

        catch(Exception e)

        {

            log.error("Exception get serviceID list", e);

            throw new SQLException(e.getMessage());

        }

        finally {

            DatabaseManager.close(rs, ps);

            rs = null;

            ps = null;

        }

    }

}

