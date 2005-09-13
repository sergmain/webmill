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
package org.riverock.sso.a3;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.io.Serializable;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.site.SiteListSite;
import org.riverock.sso.main.MainUserInfo;
import org.riverock.sso.schema.config.AuthProviderParametersListType;
import org.riverock.sso.schema.core.AuthUserItemType;
import org.riverock.sso.core.GetAuthUserItem;
import org.riverock.common.tools.RsetTools;

import org.apache.log4j.Logger;

/**
 * User: Admin
 * Date: Sep 6, 2003
 * Time: 10:33:28 PM
 *
 * $Id$
 */
public final class InternalAuthProvider implements AuthProviderInterface, Serializable {
    private static final long serialVersionUID = 20434672384237113L;

    private final static Logger log = Logger.getLogger( InternalAuthProvider.class );

    public InternalAuthProvider() {
    }

    boolean checkAccess( final DatabaseAdapter adapter, final AuthSession authSession, final String serverName ) throws AuthException {
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

        try {

            Long idSite = SiteListSite.getIdSite( serverName );
            if ( log.isDebugEnabled() )
                log.debug( "serverName " + serverName + ", idSite " + idSite );

            ps = adapter.prepareStatement( sql_ );

            RsetTools.setLong( ps, 1, idSite );
            ps.setString( 2, authSession.getUserLogin() );
            ps.setString( 3, authSession.getUserPassword() );

            rs = ps.executeQuery();
            if ( rs.next() )
                isValid = true;

        }
        catch( Exception e1 ) {
            log.error( "SQL:\n" + sql_ );
            final String es = "Error check checkAccess()";
            log.error( es, e1 );
            throw new AuthException( es, e1 );
        }
        finally {
            DatabaseManager.close( rs, ps );
            rs = null;
            ps = null;
        }
        if ( log.isDebugEnabled() )
            log.debug( "isValid " + isValid );

        return isValid;
    }

    boolean checkAccessMySql( final DatabaseAdapter adapter, final AuthSession authSession, final String serverName ) throws AuthException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean isValid = false;

//        "select a.ID_USER from AUTH_USER a, MAIN_USER_INFO b, " +
//        "( " +
//        "select z1.USER_LOGIN from V$_READ_LIST_FIRM z1, SITE_LIST_SITE x1 " +
//        "where x1.ID_SITE=? and z1.ID_FIRM = x1.ID_FIRM " +
//        "union " +
//        "select y1.USER_LOGIN from AUTH_USER y1 where y1.IS_ROOT=1 " +
//        ") c " +
//        "where  a.USER_LOGIN=? and a.USER_PASSWORD=? and " +
//        "a.ID_USER = b.ID_USER and b.is_deleted=0 and a.USER_LOGIN=c.USER_LOGIN ";

        String sql_ =
            "select a.* from AUTH_USER a, MAIN_USER_INFO b " +
            "where  a.USER_LOGIN=? and a.USER_PASSWORD=? and " +
            "       a.ID_USER = b.ID_USER and b.is_deleted=0";

        try {

            Long idSite = SiteListSite.getIdSite( serverName );
            if ( log.isDebugEnabled() )
                log.debug( "serverName " + serverName + ", idSite " + idSite );

            ps = adapter.prepareStatement( sql_ );

            ps.setString( 1, authSession.getUserLogin() );
            ps.setString( 2, authSession.getUserPassword() );

            rs = ps.executeQuery();
            if ( !rs.next() )
                return false;

            AuthUserItemType item = GetAuthUserItem.fillBean( rs );
            rs.close();
            rs = null;
            ps.close();
            ps = null;

            if ( Boolean.TRUE.equals( item.getIsRoot() ) )
                return true;

            sql_ =
                "select  a01.id_firm, a01.user_login, a01.id_user, a01.id_auth_user " +
                "from    auth_user a01, SITE_LIST_SITE f01 " +
                "where   a01.is_use_current_firm = 1 and a01.ID_FIRM = f01.ID_FIRM and f01.ID_SITE=? and " +
                "        a01.user_login=? " +
                "union " +
                "select  d02.id_firm, a02.user_login, a02.id_user, a02.id_auth_user " +
                "from    auth_user a02, main_relate_service_firm d02, SITE_LIST_SITE f02 " +
                "where   a02.is_service = 1 and a02.id_service = d02.id_service and " +
                "        d02.id_firm= f02.ID_FIRM and f02.ID_SITE=? and a02.user_login=? " +
                "union " +
                "select  e03.id_firm, a03.user_login, a03.id_user, a03.id_auth_user " +
                "from    auth_user a03, main_relate_road_service d03, main_relate_service_firm e03, SITE_LIST_SITE f03 " +
                "where   a03.is_road = 1 and a03.id_road = d03.id_road and " +
                "        d03.id_service = e03.id_service and e03.id_firm = f03.ID_FIRM and f03.ID_SITE=? and " +
                "        a03.user_login=? " +
                "union " +
                "select  b04.id_firm, a04.user_login, a04.id_user, a04.id_auth_user " +
                "from    auth_user a04, main_list_firm b04, SITE_LIST_SITE f04 " +
                "where   a04.is_root = 1 and b04.ID_FIRM = f04.ID_FIRM and f04.ID_SITE=? and " +
                "        a04.user_login=? ";

            ps = adapter.prepareStatement( sql_ );

            RsetTools.setLong( ps, 1, idSite );
            ps.setString( 2, authSession.getUserLogin() );
            RsetTools.setLong( ps, 3, idSite );
            ps.setString( 4, authSession.getUserLogin() );
            RsetTools.setLong( ps, 5, idSite );
            ps.setString( 6, authSession.getUserLogin() );
            RsetTools.setLong( ps, 7, idSite );
            ps.setString( 8, authSession.getUserLogin() );

            rs = ps.executeQuery();

            if ( rs.next() )
                isValid = true;

        }
        catch( Exception e1 ) {
            log.error( "SQL:\n" + sql_ );
            final String es = "Error check checkAccess()";
            log.error( es, e1 );
            throw new AuthException( es, e1 );
        }
        finally {
            DatabaseManager.close( rs, ps );
            rs = null;
            ps = null;
        }
        if ( log.isDebugEnabled() )
            log.debug( "isValid " + isValid );

        return isValid;
    }

    public boolean checkAccess( final AuthSession authSession, final String serverName ) throws AuthException {
        DatabaseAdapter db_ = null;
        try {
            db_ = DatabaseAdapter.getInstance();
            switch( db_.getFamaly() ) {
                case DatabaseManager.MYSQL_FAMALY:
                    return checkAccessMySql( db_, authSession, serverName );
                default:
                    return checkAccess( db_, authSession, serverName );
            }
        }
        catch( Exception e1 ) {
            final String es = "Error check checkAccess()";
            log.error( es, e1 );
            throw new AuthException( es, e1 );
        }
        finally {
            DatabaseManager.close( db_ );
            db_ = null;
        }
    }


    public void setParameters( final AuthProviderParametersListType params ) throws Exception {
    }

    public boolean isUserInRole( final AuthSession authSession, final String role_ )
        throws AuthException {
        if ( log.isDebugEnabled() )
            log.debug( "role '" + role_ + "', user login '" + authSession.getUserLogin() + "'  " );

        if ( authSession.getUserLogin() == null ||
            authSession.getUserLogin().trim().length() == 0 ||
            authSession.getUserPassword() == null ||
            role_ == null ||
            role_.trim().length() == 0 )
            return false;

        long startMills = 0;
        if ( log.isInfoEnabled() )
            startMills = System.currentTimeMillis();

        PreparedStatement ps = null;
        ResultSet rset = null;
        DatabaseAdapter db_ = null;
        AuthInfo authInfo = null;
        try {
            db_ = DatabaseAdapter.getInstance();
            authInfo = AuthInfo.getInstance( db_, authSession.getUserLogin(), authSession.getUserPassword() );

            if ( authInfo == null )
                return false;

            if ( log.isDebugEnabled() )
                log.debug( "userLogin " + authSession.getUserLogin() + " role " + role_ );

            if ( authInfo.getRoot() == 1 )
                return true;

            if ( log.isDebugEnabled() )
                log.debug( "#1.011" );

            ps = db_.prepareStatement( "select null " +
                "from   AUTH_USER a, AUTH_RELATE_ACCGROUP b, AUTH_ACCESS_GROUP c " +
                "where  a.USER_LOGIN=? and " +
                "       a.ID_AUTH_USER=b.ID_AUTH_USER and " +
                "       b.ID_ACCESS_GROUP=c.ID_ACCESS_GROUP and " +
                "       c.NAME_ACCESS_GROUP=?" );

            ps.setString( 1, authSession.getUserLogin() );
            ps.setString( 2, role_ );

            rset = ps.executeQuery();

            return rset.next();
        }
        catch( Exception e ) {
            final String es = "Error getRigth(), role " + role_;
            log.error( es, e );
            throw new AuthException( es, e );
        }
        finally {
            org.riverock.generic.db.DatabaseManager.close( db_, rset, ps );
            rset = null;
            ps = null;
            db_ = null;

            if ( log.isInfoEnabled() )
                log.info( "right processed for " + ( System.currentTimeMillis() - startMills ) + " milliseconds" );
        }
    }

    public static AuthInfo getAuthInfo( final AuthSession authSession )
        throws AuthException {
        DatabaseAdapter db_ = null;
        try {
            db_ = DatabaseAdapter.getInstance();
            return AuthInfo.getInstance( db_, authSession.getUserLogin(), authSession.getUserPassword() );
        }
        catch( Exception e ) {
            final String es = "Error getAuthInfo ";
            log.error( es, e );
            throw new AuthException( es, e );
        }
        finally {
            DatabaseManager.close( db_ );
            db_ = null;
        }
    }

    public void initUserInfo( final AuthSession authSession ) throws AuthException {
        try {
            MainUserInfo info = MainUserInfo.getInstance( authSession.getUserLogin() );
            authSession.setUserInfo( info );
        }
        catch( Exception e ) {
            final String es = "Error MainUserInfo.getInstance(db_, getUserLogin())";
            log.error( es, e );
            throw new AuthException( es, e );
        }
    }
}
