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

 *

 *  $Id$

 */



package org.riverock.sso.a3;



import java.sql.PreparedStatement;

import java.sql.ResultSet;

import java.sql.SQLException;



import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.common.tools.RsetTools;



import org.apache.log4j.Logger;



public class AuthInfo

{

    private static Logger log = Logger.getLogger(AuthInfo.class);



    public Long authUserID;

    public Long userID;

    public Long firmID;

    public Long serviceID;

    public Long roadID;



    public String userLogin = "";

    public String userPassword = "";



    public int isUseCurrentFirm = 0;

    public int isService = 0;

    public int isRoad = 0;

    public int isRoot = 0;



    protected void finalize() throws Throwable

    {

        userLogin = null;

        userPassword = null;



        super.finalize();

    }



    public AuthInfo(){}



    private void set(ResultSet rs)

        throws SQLException

    {

        authUserID = RsetTools.getLong(rs, "ID_AUTH_USER");

        userID = RsetTools.getLong(rs, "ID_USER");

        firmID = RsetTools.getLong(rs, "ID_FIRM");

        serviceID = RsetTools.getLong(rs, "ID_SERVICE");

        roadID = RsetTools.getLong(rs, "ID_ROAD");



        userLogin = RsetTools.getString(rs, "USER_LOGIN");



        userPassword = RsetTools.getString(rs, "USER_PASSWORD");



        int isUseCurrentFirmTemp = RsetTools.getInt(rs, "IS_USE_CURRENT_FIRM", new Integer(0)).intValue();

        int isServiceTemp = RsetTools.getInt(rs, "IS_SERVICE", new Integer(0)).intValue();

        int isRoadTemp = RsetTools.getInt(rs, "IS_ROAD", new Integer(0)).intValue();

        int isRootTemp = RsetTools.getInt(rs, "IS_ROOT", new Integer(0)).intValue();



//        decode(IS_USE_CURRENT_FIRM+is_service+is_road+is_root,0,0,1) IS_USE_CURRENT_FIRM,

//        decode(is_service+is_road+is_root,0,0,1) IS_SERVICE,

//        decode(is_road+is_root,0,0,1) IS_ROAD,



        isUseCurrentFirm = ((isUseCurrentFirmTemp+isServiceTemp+isRoadTemp+isRootTemp)>0?1:0);

        isService = ((isServiceTemp+isRoadTemp+isRootTemp)>0?1:0);

        isRoad = ((isRoadTemp+isRootTemp)>0?1:0);

        isRoot = ((isRootTemp)>0?1:0);

    }



    public static AuthInfo getInstance(DatabaseAdapter db_, String login_, String pass_)

            throws AuthException

    {

        PreparedStatement ps = null;

        ResultSet rs = null;

        AuthInfo auth_ = null;

        try

        {

            ps = db_.prepareStatement(

                "select * FROM AUTH_USER where USER_LOGIN=? and USER_PASSWORD=?"

            );



            ps.setString(1, login_);

            ps.setString(2, pass_);



            rs = ps.executeQuery();

            if (rs.next())

            {

                auth_ = new AuthInfo();

                auth_.set(rs);

            }



        }

        catch (Exception e)

        {

            log.error(e);

            throw new AuthException(e.toString());

        }

        finally

        {

            if (rs != null)

            {

                try

                {

                    rs.close();

                    rs = null;

                }

                catch (Exception e01)

                {

                }

            }

            if (ps != null)

            {

                try

                {

                    ps.close();

                    ps = null;

                }

                catch (Exception e02)

                {

                }

            }

        }

        return auth_;

    }



    public static AuthInfo getInstance(DatabaseAdapter db_, Long id_auth_user)

            throws AuthException

    {

        PreparedStatement ps = null;

        ResultSet rs = null;

        AuthInfo auth_ = null;

        try

        {





            ps = db_.prepareStatement(

                "select * FROM AUTH_USER where ID_AUTH_USER=?");



            RsetTools.setLong(ps, 1, id_auth_user);



            rs = ps.executeQuery();

            if (rs.next())

            {

                auth_ = new AuthInfo();

                auth_.set(rs);

            }



        }

        catch (Exception e)

        {

            throw new AuthException(e.toString());

        }

        finally

        {

            if (rs != null)

            {

                try

                {

                    rs.close();

                    rs = null;

                }

                catch (Exception e01)

                {

                }

            }

            if (ps != null)

            {

                try

                {

                    ps.close();

                    ps = null;

                }

                catch (Exception e02)

                {

                }

            }

        }

        return auth_;

    }



}



