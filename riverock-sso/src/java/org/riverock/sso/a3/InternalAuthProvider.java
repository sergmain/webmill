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

package org.riverock.sso.a3;



import java.sql.PreparedStatement;

import java.sql.ResultSet;



import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.generic.site.SiteListSite;

import org.riverock.sso.main.MainUserInfo;

import org.riverock.sso.schema.config.AuthProviderParametersListType;

import org.riverock.common.tools.RsetTools;



import org.apache.log4j.Logger;



public class InternalAuthProvider implements AuthProviderInterface

{

    private static Logger log = Logger.getLogger("org.riverock.sso.a3.InternalAuthProvider");



    public InternalAuthProvider(){}



    public boolean checkAccess( AuthSession authSession, String serverName ) throws AuthException

    {

        PreparedStatement ps = null;

        ResultSet rs = null;

        boolean isValid = false;



        String sql_ =

            "select a.ID_USER from AUTH_USER a, MAIN_USER_INFO b, " +

            "( " +

            "select z1.USER_LOGIN from V$_READ_LIST_FIRM z1, SITE_LIST_SITE x1 " +

            "where x1.ID_SITE=? and z1.ID_FIRM = x1.ID_FIRM " +

            "union " +

            "select y1.USER_LOGIN from AUTH_USER y1 where y1.IS_ROOT=1 " +

            ") c " +

            "where  a.USER_LOGIN=? and a.USER_PASSWORD=? and " +

            "a.ID_USER = b.ID_USER and b.is_deleted=0 and a.USER_LOGIN=c.USER_LOGIN ";



        DatabaseAdapter db_ = null;

        try

        {

            db_ = DatabaseAdapter.getInstance(false);



            Long idSite = SiteListSite.getIdSite(serverName);

            if (log.isDebugEnabled())

                log.debug("serverName " +serverName+", idSite "+idSite);



            ps = db_.prepareStatement(sql_);



            RsetTools.setLong(ps, 1, idSite);

            ps.setString(2, authSession.getUserLogin());

            ps.setString(3, authSession.getUserPassword());



            rs = ps.executeQuery();

            if (rs.next())

                isValid = true;



        }

        catch (Exception e1)

        {

            log.error("Error check checkAccess()", e1);

            throw new AuthException(e1.toString());

        }

        finally

        {

            org.riverock.generic.db.DatabaseManager.close(db_, rs, ps);

            rs = null;

            ps = null;

            db_ = null;

        }

        if (log.isDebugEnabled())

            log.debug("isValid " +isValid);



        return isValid;

     }



    public void setParameters( AuthProviderParametersListType params ) throws Exception

    {

    }



    public boolean isUserInRole( AuthSession authSession, String role_ )

        throws AuthException

    {

        if (log.isDebugEnabled())

            log.debug("role " +role_+", user login "+authSession.getUserLogin()+", user password "+authSession.getUserPassword());



        if ( authSession.getUserLogin()==null ||

            authSession.getUserLogin().trim().length()==0 ||

            authSession.getUserPassword()==null ||

            role_==null ||

            role_.trim().length()==0)

            return false;



        long startMills = 0;

        if (log.isInfoEnabled())

            startMills = System.currentTimeMillis();



        PreparedStatement ps = null;

        ResultSet rset = null;

        DatabaseAdapter db_ = null;

        AuthInfo authInfo = null;

        try

        {

            db_ = DatabaseAdapter.getInstance(false);

            authInfo = AuthInfo.getInstance(db_, authSession.getUserLogin(), authSession.getUserPassword());



            if (authInfo == null)

                return false;



            if (log.isDebugEnabled())

                log.debug("userLogin " + authSession.getUserLogin() + " role " + role_);



            if (authInfo.isRoot == 1)

                return true;



            if (log.isDebugEnabled())

                log.debug("#1.011");



            ps = db_.prepareStatement(

                "select null " +

                "from   AUTH_USER a, AUTH_RELATE_ACCGROUP b, AUTH_ACCESS_GROUP c " +

                "where  a.USER_LOGIN=? and " +

                "       a.ID_AUTH_USER=b.ID_AUTH_USER and " +

                "       b.ID_ACCESS_GROUP=c.ID_ACCESS_GROUP and " +

                "       c.NAME_ACCESS_GROUP=?"

            );



            ps.setString(1, authSession.getUserLogin());

            ps.setString(2, role_);



            rset = ps.executeQuery();



            return rset.next();

        }

        catch (Exception e)

        {

            log.error("Error getRigth(), role "+role_, e);

            throw new AuthException(e.toString());

        }

        finally

        {

            org.riverock.generic.db.DatabaseManager.close(db_, rset, ps);

            rset = null;

            ps = null;

            db_ = null;



            if (log.isInfoEnabled())

                log.info("right processed for " + (System.currentTimeMillis() - startMills) + " milliseconds");

        }

    }



    public static AuthInfo getAuthInfo( AuthSession authSession )

        throws AuthException

    {

        DatabaseAdapter db_ = null;

        try

        {

            db_ = DatabaseAdapter.getInstance(false);

            return AuthInfo.getInstance( db_, authSession.getUserLogin(), authSession.getUserPassword());

        }

        catch (Exception e)

        {

            log.error("Error getAuthInfo ", e);

            throw (AuthException)e;

        }

        finally

        {

            org.riverock.generic.db.DatabaseManager.close(db_);

            db_ = null;

        }

    }



    public void initUserInfo( AuthSession authSession ) throws AuthException

    {

        try

        {

            MainUserInfo info = MainUserInfo.getInstance( authSession.getUserLogin() );

            authSession.setUserInfo(info);

        }

        catch (Exception e)

        {

            log.error("Error MainUserInfo.getInstance(db_, getUserLogin())", e);

            throw (AuthException)e;

        }

    }

}

