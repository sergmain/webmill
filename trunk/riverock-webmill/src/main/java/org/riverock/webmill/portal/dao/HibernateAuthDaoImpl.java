/*
 * org.riverock.webmill - Webmill portal with support jsr-168, xml/xslt and others things.
 * For more information, please visit project site http://webmill.riverock.org
 *
 * Copyright (C) 2000-2006, Riverock Software, All Rights Reserved.
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
package org.riverock.webmill.portal.dao;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.log4j.Logger;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.Query;

import org.riverock.interfaces.sso.a3.bean.RoleBean;
import org.riverock.interfaces.sso.a3.bean.RoleEditableBean;
import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.interfaces.sso.a3.AuthInfo;
import org.riverock.interfaces.sso.a3.UserInfo;
import org.riverock.interfaces.sso.a3.AuthUserExtendedInfo;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.schema.db.CustomSequenceType;
import org.riverock.webmill.a3.bean.UserInfoImpl;
import org.riverock.webmill.a3.bean.AuthInfoImpl;
import org.riverock.webmill.a3.bean.RoleBeanImpl;
import org.riverock.webmill.schema.core.WmAuthUserItemType;
import org.riverock.webmill.core.GetWmAuthUserItem;
import org.riverock.webmill.portal.utils.SiteList;
import org.riverock.webmill.utils.HibernateUtils;
import org.riverock.common.tools.RsetTools;
import org.riverock.common.tools.DateTools;

/**
 * @author Sergei Maslyukov
 *         Date: 09.11.2006
 *         Time: 20:59:54
 *         <p/>
 *         $Id$
 */
public class HibernateAuthDaoImpl implements InternalAuthDao {
    private static Logger log = Logger.getLogger(HibernateAuthDaoImpl.class);

    public UserInfo getUserInfo(String userLogin) {
        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        UserInfoImpl user = (UserInfoImpl)session.createQuery(
            "select user from org.riverock.webmill.a3.bean.UserInfoImpl as user," +
            " org.riverock.webmill.a3.bean.AuthInfoImpl auth " +
            "where user.userId = auth.userId and auth.userLogin=:userLogin ")
            .setString("userLogin", userLogin)
            .uniqueResult();
        session.getTransaction().commit();
        return user;
    }

    public UserInfo getUserInfo(DatabaseAdapter db_, String userLogin) {
        return getUserInfo(userLogin);
    }

    /**
     * retrun list of users, which current user can operate
     *
     * @param username
     * @return String comma separated list
     */
    public String getGrantedUserId(String username) {
        return listToString( getGrantedUserIdList(null, username) );
    }

    private List<Long> getGrantedUserIdList(DatabaseAdapter adapter, String username) {
        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        List<AuthInfoImpl> authInfos = session.createQuery(
            "select auth from org.riverock.webmill.a3.bean.AuthInfoImpl auth " +
            "where auth.userLogin=:userLogin ")
            .setString("userLogin", username)
            .list();
        session.getTransaction().commit();
        List<Long> result = new ArrayList<Long>();
        for (AuthInfoImpl authInfo : authInfos) {
            result.add(authInfo.getUserId());
        }
        return result;
    }

    public List<Long> getGrantedUserIdList(String username) {
        return getGrantedUserIdList(null, username);
    }

    public String getGrantedCompanyId(String username) {
        return getGrantedCompanyId(null, username);
    }

    public String getGrantedCompanyId(DatabaseAdapter db, String username) {
        return listToString( getGrantedCompanyIdList(db, username) );
    }

    public List<Long> getGrantedCompanyIdList(String username) {
        return getGrantedCompanyIdList(null, username);
    }

    private List<Long> getGrantedCompanyIdList(DatabaseAdapter adapter, String username) {
/*
        String sql_ =
            "select  a01.id_firm " +
            "from    WM_AUTH_USER a01 " +
            "where   a01.is_use_current_firm = 1 and a01.user_login=? " +
            "union " +
            "select  d02.ID_COMPANY " +
            "from    WM_AUTH_USER a02, WM_LIST_R_HOLDING_COMPANY d02 " +
            "where   a02.IS_HOLDING = 1 and a02.ID_HOLDING = d02.ID_HOLDING and a02.user_login=? " +
            "union " +
            "select  b04.id_firm " +
            "from    WM_AUTH_USER a04, WM_LIST_COMPANY b04 " +
            "where   a04.is_root = 1 and a04.user_login=? ";

*/
        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        List<Long> list = session.createQuery(
            "select auth1.companyId from org.riverock.webmill.a3.bean.AuthInfoImpl auth1 " +
            "where  auth1.isCompany=true and auth1.userLogin=:userLogin1 " +
            "union " +
            "select relate2.companyId from org.riverock.webmill.a3.bean.AuthInfoImpl auth2," +
            " org.riverock.webmill.portal.bean.HoldingCompanyRelationBean relate2 " +
            "where  auth2.isHolding=true and auth2.holdingId=relate2.hodingId and auth2.userLogin=:userLogin2 " +
            "union " +
            "select company3.companyId from org.riverock.webmill.a3.bean.AuthInfoImpl auth3, " +
            " org.riverock.webmill.portal.bean.CompanyBean company3 " +
            "where  auth3.isRoot=true and auth3.userLogin=:userLogin3 ")
            .setString("userLogin1", username)
            .setString("userLogin2", username)
            .setString("userLogin3", username)
            .list();
        session.getTransaction().commit();
        return list;
    }

    private String getGrantedHoldingId(DatabaseAdapter db, String username) {
        return listToString( getGrantedHoldingIdList(db, username) );
    }

    public String getGrantedHoldingId(String username) {
        return getGrantedHoldingId(null, username);
    }

    public List<Long> getGrantedHoldingIdList(String username) {
            return getGrantedHoldingIdList(null, username);
    }

    private List<Long> getGrantedHoldingIdList(DatabaseAdapter adapter, String username) {
/*
        String sql_ =
            "select  a01.ID_HOLDING "+
            "from    WM_AUTH_USER a01 "+
            "where   a01.IS_HOLDING=1 and a01.user_login=?  "+
            "union "+
            "select  b04.ID_HOLDING "+
            "from    WM_AUTH_USER a04, WM_LIST_HOLDING b04 "+
            "where   a04.is_root=1 and a04.user_login=?";
*/
        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        List<Long> list = session.createQuery(
            "select auth1.holdingId from org.riverock.webmill.a3.bean.AuthInfoImpl auth1 " +
            "where  auth1.isHolding=true and auth1.userLogin=:userLogin1 " +
            "union " +
            "select holding2.holdingId from org.riverock.webmill.a3.bean.AuthInfoImpl auth2, " +
            " org.riverock.webmill.portal.bean.HoldingBean holding2 " +
            "where  auth2.isRoot=true and auth3.userLogin=:userLogin2 ")
            .setString("userLogin1", username)
            .setString("userLogin2", username)
            .list();
        session.getTransaction().commit();
        return list;
    }

    public Long checkCompanyId(Long companyId, String userLogin) {
        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        Long id = (Long)session.createQuery(
            "select company.companyId from org.riverock.webmill.portal.bean.CompanyBean as company " +
            "where company.companyId = :companyId and company.companyId in ( "+

                "select auth1.companyId from org.riverock.webmill.a3.bean.AuthInfoImpl auth1 " +
                "where  auth1.isCompany=true and auth1.userLogin=:userLogin1 " +
                "union " +
                "select relate2.companyId from org.riverock.webmill.a3.bean.AuthInfoImpl auth2," +
                " org.riverock.webmill.portal.bean.HoldingCompanyRelationBean relate2 " +
                "where  auth2.isHolding=true and auth2.holdingId=relate2.hodingId and auth2.userLogin=:userLogin2 " +
                "union " +
                "select company3.companyId from org.riverock.webmill.a3.bean.AuthInfoImpl auth3, " +
                " org.riverock.webmill.portal.bean.CompanyBean company3 " +
                "where  auth3.isRoot=true and auth3.userLogin=:userLogin3 " +
             ")")
            .setLong("companyId", companyId)
            .setString("userLogin1", userLogin)
            .setString("userLogin2", userLogin)
            .setString("userLogin3", userLogin)
            .uniqueResult();
        session.getTransaction().commit();
        return id;
    }

    public Long checkHoldingId(Long holdingId, String userLogin) {
        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        Long id = (Long)session.createQuery(
            "select holding.holdingId from org.riverock.webmill.portal.bean.HoldingBean as holding " +
            "where holding.holdingId = :holdingId and holding.holdingId in ( "+

                "select auth1.holdingId from org.riverock.webmill.a3.bean.AuthInfoImpl auth1 " +
                "where  auth1.isHolding=true and auth1.userLogin=:userLogin1 " +
                "union " +
                "select holding2.holdingId from org.riverock.webmill.a3.bean.AuthInfoImpl auth2, " +
                " org.riverock.webmill.portal.bean.HoldingBean holding2 " +
                "where  auth2.isRoot=true and auth3.userLogin=:userLogin2 " +
             ")")
            .setLong("holdingId", holdingId)
            .setString("userLogin1", userLogin)
            .setString("userLogin2", userLogin)
            .uniqueResult();
        session.getTransaction().commit();
        return id;
    }

    public boolean checkRigthOnUser( Long id_auth_user_check, Long id_auth_user_owner ) {
/*
        WmAuthUserItemType auth = GetWmAuthUserItem.getInstance(db, id_auth_user_owner).item;
        if (auth==null)
            return false;

        ps = db.prepareStatement(
            "select null " +
            "from   WM_AUTH_USER a, WM_LIST_USER b " +
            "where  a.ID_USER=b.ID_USER and a.ID_AUTH_USER=? and " +
            "       b.ID_FIRM  in ("+getGrantedCompanyId(db, auth.getUserLogin())+") "
        );
*/
        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        AuthInfoImpl auth = (AuthInfoImpl)session.createQuery(
            "select auth from org.riverock.webmill.a3.bean.AuthInfoImpl as auth," +
            "where auth.authUserId=:id_auth_user_owner ")
            .setLong("id_auth_user_owner", id_auth_user_owner)
            .uniqueResult();

        Long id = null;
        if (auth!=null) {
            id = (Long)session.createQuery(
                "select auth.authUserId " +
                    "from org.riverock.webmill.a3.bean.AuthInfoImpl as auth, " +
                    "     org.riverock.webmill.a3.bean.UserInfoImpl as user " +
                    "where auth.authUserId = :id_auth_user_check and auth.userId=user.userId and company.companyId in ( "+

                    "select auth1.companyId from org.riverock.webmill.a3.bean.AuthInfoImpl auth1 " +
                    "where  auth1.isCompany=true and auth1.userLogin=:userLogin1 " +
                    "union " +
                    "select relate2.companyId from org.riverock.webmill.a3.bean.AuthInfoImpl auth2," +
                    " org.riverock.webmill.portal.bean.HoldingCompanyRelationBean relate2 " +
                    "where  auth2.isHolding=true and auth2.holdingId=relate2.hodingId and auth2.userLogin=:userLogin2 " +
                    "union " +
                    "select company3.companyId from org.riverock.webmill.a3.bean.AuthInfoImpl auth3, " +
                    " org.riverock.webmill.portal.bean.CompanyBean company3 " +
                    "where  auth3.isRoot=true and auth3.userLogin=:userLogin3 " +
                    ")")
                .setLong("id_auth_user_check", id_auth_user_check)
                .setString("userLogin1", auth.getUserLogin())
                .setString("userLogin2", auth.getUserLogin())
                .setString("userLogin3", auth.getUserLogin())
                .uniqueResult();
        }
        session.getTransaction().commit();
        return id!=null;
    }

    public boolean isUserInRole( String userLogin, String userPassword, final String role_ ) {
        if ( log.isDebugEnabled() )
            log.debug( "role '" + role_ + "', user login '" + userLogin + "'  " );

        if ( StringUtils.isBlank(userLogin) || userPassword == null || StringUtils.isBlank(role_))
            return false;

        long startMills = 0;
        if ( log.isInfoEnabled() )
            startMills = System.currentTimeMillis();

        Session session = HibernateUtils.getSession();
        session.beginTransaction();

        try {
            AuthInfoImpl authInfo = (AuthInfoImpl)session.createQuery(
                "select auth from org.riverock.webmill.a3.bean.AuthInfoImpl auth " +
                "where auth.userLogin=:userLogin and auth.userPassword=:userPassword")
                .setString("userLogin", userLogin)
                .setString("userPassword", userPassword)
                .uniqueResult();

            if ( authInfo == null )
                return false;

            if ( log.isDebugEnabled() )
                log.debug( "userLogin " + userLogin + " role " + role_ );

            if ( authInfo.isRoot() )
                return true;

            if ( log.isDebugEnabled() )
                log.debug( "#1.011" );

            Long authUserId = (Long)session.createQuery(
                "select auth.authUserId " +
                    "from org.riverock.webmill.a3.bean.AuthInfoImpl auth, " +
                    "     org.riverock.webmill.a3.bean.AuthRelateRole relateRole, " +
                    "     org.riverock.webmill.a3.bean.RoleBeanImpl role " +
                    "where auth.userLogin=:userLogin and auth.authUserId=relateRole.authUserId and " +
                    "      relateRole.roleId=role.roleId and role.name=:roleName ")
                .setString("userLogin", userLogin)
                .setString("roleName", role_)
                .uniqueResult();

            return authUserId!=null;
        }
        finally {
            session.getTransaction().commit();
        }
    }


    private AuthInfo setAuthInfo(ResultSet rs) throws SQLException {
        AuthInfoImpl bean = new AuthInfoImpl();
        bean.setAuthUserId( RsetTools.getLong(rs, "ID_AUTH_USER") );
        bean.setUserId( RsetTools.getLong(rs, "ID_USER") );
        bean.setCompanyId( RsetTools.getLong(rs, "ID_FIRM") );
        bean.setHoldingId( RsetTools.getLong(rs, "ID_HOLDING") );
        bean.setUserLogin( RsetTools.getString(rs, "USER_LOGIN") );
        bean.setUserPassword( RsetTools.getString(rs, "USER_PASSWORD") );

        int isUseCurrentFirmTemp = RsetTools.getInt(rs, "IS_USE_CURRENT_FIRM", 0);
        int isRoadTemp = RsetTools.getInt(rs, "IS_HOLDING", 0);
        int isRootTemp = RsetTools.getInt(rs, "IS_ROOT", 0);

        bean.setCompany( (isUseCurrentFirmTemp+isRoadTemp+isRootTemp)>0 );
        bean.setHolding( (isRoadTemp+isRootTemp)>0 );
        bean.setRoot( (isRootTemp)>0 );

        return bean;
    }

    public AuthInfo getAuthInfo(String login_, String pass_) {
        return getAuthInfo(null, login_, pass_);
    }

    private AuthInfo getAuthInfo(DatabaseAdapter db_, String login_, String pass_) {
        Session session = HibernateUtils.getSession();
        session.beginTransaction();

        AuthInfoImpl authInfo = (AuthInfoImpl)session.createQuery(
            "select auth from org.riverock.webmill.a3.bean.AuthInfoImpl auth " +
                "where auth.userLogin=:userLogin and auth.userPassword=:userPassword")
            .setString("userLogin", login_)
            .setString("userPassword", pass_)
            .uniqueResult();

        session.getTransaction().commit();
        return authInfo;
    }

    public AuthInfo getAuthInfo(Long authUserId) {
        return getAuthInfo(null, authUserId);
    }

    public List<AuthInfo> getAuthInfoList(AuthSession authSession) {
        return getAuthInfoList(null, authSession );
    }

    private List<AuthInfo> getAuthInfoList(DatabaseAdapter db, AuthSession authSession) {
/*
        "select a.* " +
        "from   WM_AUTH_USER a, WM_LIST_USER b " +
        "where  a.ID_USER=b.ID_USER and  " +
        "       b.ID_FIRM  in ("+getGrantedCompanyId(db, authSession.getUserLogin())+") "
*/
        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        List<AuthInfoImpl> authInfos = session.createQuery(
            "select auth from org.riverock.webmill.a3.bean.AuthInfoImpl auth " +
                "     org.riverock.webmill.a3.bean.UserInfoImpl as user " +
                "where auth.userId=user.userId and company.companyId in ( "+

                "select auth1.companyId from org.riverock.webmill.a3.bean.AuthInfoImpl auth1 " +
                "where  auth1.isCompany=true and auth1.userLogin=:userLogin1 " +
                "union " +
                "select relate2.companyId from org.riverock.webmill.a3.bean.AuthInfoImpl auth2," +
                " org.riverock.webmill.portal.bean.HoldingCompanyRelationBean relate2 " +
                "where  auth2.isHolding=true and auth2.holdingId=relate2.hodingId and auth2.userLogin=:userLogin2 " +
                "union " +
                "select company3.companyId from org.riverock.webmill.a3.bean.AuthInfoImpl auth3, " +
                " org.riverock.webmill.portal.bean.CompanyBean company3 " +
                "where  auth3.isRoot=true and auth3.userLogin=:userLogin3 " +
                ")")
            .setString("userLogin1", authSession.getUserLogin())
            .setString("userLogin2", authSession.getUserLogin())
            .setString("userLogin3", authSession.getUserLogin())
            .list();
        session.getTransaction().commit();
        return (List)authInfos;
    }

    private AuthInfo getAuthInfo(DatabaseAdapter db_, Long authUserId) {
        if (authUserId==null) {
            return null;
        }
        Session session = HibernateUtils.getSession();
        session.beginTransaction();

        AuthInfoImpl authInfo = (AuthInfoImpl)session.createQuery(
            "select auth from org.riverock.webmill.a3.bean.AuthInfoImpl auth " +
                "where auth.authUserId=:authUserId")
            .setLong("authUserId", authUserId)
            .uniqueResult();

        session.getTransaction().commit();
        return authInfo;
    }

    public List<AuthInfo> getAuthInfo(DatabaseAdapter db_, Long userId, Long siteId) {
        switch( db_.getFamily() ) {
            case DatabaseManager.MYSQL_FAMALY:
                return getAuthInfoMySql( db_, userId, siteId );
            default:
                return getAuthInfoDefault( db_, userId, siteId );
        }
    }

    private List<AuthInfo> getAuthInfoDefault(DatabaseAdapter db_, Long userId, Long siteId) {
        List<AuthInfo> list = new ArrayList<AuthInfo>();

        if (userId==null || siteId==null) {
            return list;
        }

        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            String sql =
                "select a.* " +
                    "from  WM_AUTH_USER a, V$_READ_LIST_FIRM z1, WM_PORTAL_LIST_SITE x1 " +
                    "where x1.ID_SITE=? and z1.ID_FIRM = x1.ID_FIRM and " +
                    "      a.ID_AUTH_USER=z1.ID_AUTH_USER and a.ID_USER=?";

            ps = db_.prepareStatement(sql);
            ps.setLong(1, siteId);
            ps.setLong(1, userId);
            rs = ps.executeQuery();

            while (rs.next()) {
                list.add(setAuthInfo(rs));
            }
            return list;
        }
        catch (Throwable e){
            String es = "AuthInfo.getInstance() exception";
            log.error(es, e);
            throw new IllegalStateException(es, e);
        }
        finally{
            DatabaseManager.close(rs, ps);
            rs = null;
            ps = null;
        }
    }

    private List<AuthInfo> getAuthInfoMySql(DatabaseAdapter db_, Long userId, Long siteId) {
        List<AuthInfo> list = new ArrayList<AuthInfo>();

        if (userId==null || siteId==null) {
            return list;
        }

        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            String sql =
                "select  a01.id_auth_user " +
                "from    WM_AUTH_USER a01, WM_PORTAL_LIST_SITE f01 " +
                "where   a01.is_use_current_firm = 1 and a01.ID_FIRM = f01.ID_FIRM and f01.ID_SITE=? and " +
                "        a01.id_user=? " +
                "union " +
                "select  a02.id_auth_user " +
                "from    WM_AUTH_USER a02, WM_LIST_R_HOLDING_COMPANY d02, WM_PORTAL_LIST_SITE f02 " +
                "where   a02.IS_HOLDING = 1 and a02.ID_HOLDING = d02.ID_HOLDING and " +
                "        d02.ID_COMPANY = f02.ID_FIRM and f02.ID_SITE=? and a02.id_user=? " +
                "union " +
                "select  a04.id_auth_user " +
                "from    WM_AUTH_USER a04, WM_LIST_COMPANY b04, WM_PORTAL_LIST_SITE f04 " +
                "where   a04.is_root = 1 and b04.ID_FIRM = f04.ID_FIRM and f04.ID_SITE=? and " +
                "        a04.id_user=? ";

            ps = db_.prepareStatement(sql);
            ps.setLong(1, siteId);
            ps.setLong(2, userId);
            ps.setLong(3, siteId);
            ps.setLong(4, userId);
            ps.setLong(5, siteId);
            ps.setLong(6, userId);
            rs = ps.executeQuery();

            while (rs.next()) {
                list.add( getAuthInfo(db_, RsetTools.getLong(rs, "id_auth_user")));
            }
            return list;
        }
        catch (Throwable e){
            String es = "AuthInfo.getInstance() exception";
            log.error(es, e);
            throw new IllegalStateException(es, e);
        }
        finally{
            DatabaseManager.close(rs, ps);
            rs = null;
            ps = null;
        }
    }

    boolean checkAccess( final DatabaseAdapter adapter, final String userLogin, String userPassword, final String serverName ) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean isValid = false;

        String sql_ =
            "select a.ID_USER from WM_AUTH_USER a, WM_LIST_USER b, " +
            "( " +
            "select z1.USER_LOGIN from V$_READ_LIST_FIRM z1, WM_PORTAL_LIST_SITE x1 " +
            "where x1.ID_SITE=? and z1.ID_FIRM = x1.ID_FIRM " +
            "union " +
            "select y1.USER_LOGIN from WM_AUTH_USER y1 where y1.IS_ROOT=1 " +
            ") c " +
            "where  a.USER_LOGIN=? and a.USER_PASSWORD=? and " +
            "a.ID_USER = b.ID_USER and b.is_deleted=0 and a.USER_LOGIN=c.USER_LOGIN ";

        try {

            Long idSite = SiteList.getSiteId( serverName );
            if ( log.isDebugEnabled() ) {
                log.debug( "serverName " + serverName + ", idSite " + idSite );
            }

            ps = adapter.prepareStatement( sql_ );

            RsetTools.setLong( ps, 1, idSite );
            ps.setString( 2, userLogin );
            ps.setString( 3, userPassword );

            rs = ps.executeQuery();
            if ( rs.next() )
                isValid = true;

        }
        catch( Exception e1 ) {
            log.error( "SQL:\n" + sql_ );
            final String es = "Error check checkAccess()";
            log.error( es, e1 );
            throw new IllegalStateException( es, e1 );
        }
        finally {
            DatabaseManager.close( rs, ps );
            rs = null;
            ps = null;
        }
        if ( log.isDebugEnabled() ) {
            log.debug( "isValid " + isValid );
        }

        return isValid;
    }

    boolean checkAccessMySql( final DatabaseAdapter adapter, final String userLogin, String userPassword, final String serverName ) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean isValid = false;

        String sql_ =
            "select a.* from WM_AUTH_USER a, WM_LIST_USER b " +
            "where  a.USER_LOGIN=? and a.USER_PASSWORD=? and " +
            "       a.ID_USER = b.ID_USER and b.is_deleted=0";

        try {

            Long idSite = SiteList.getSiteId( serverName );
            if ( log.isDebugEnabled() ) {
                log.debug( "serverName " + serverName + ", idSite " + idSite );
            }

            ps = adapter.prepareStatement( sql_ );

            ps.setString( 1, userLogin );
            ps.setString( 2, userPassword );

            rs = ps.executeQuery();
            if ( !rs.next() )
                return false;

            WmAuthUserItemType item = GetWmAuthUserItem.fillBean( rs );
            rs.close();
            rs = null;
            ps.close();
            ps = null;

            if ( Boolean.TRUE.equals( item.getIsRoot() ) )
                return true;

            sql_ =
                "select  a01.id_firm, a01.user_login, a01.id_user, a01.id_auth_user " +
                "from    WM_AUTH_USER a01, WM_PORTAL_LIST_SITE f01 " +
                "where   a01.is_use_current_firm = 1 and a01.ID_FIRM = f01.ID_FIRM and f01.ID_SITE=? and " +
                "        a01.user_login=? " +
                "union " +
                "select  d02.ID_COMPANY, a02.user_login, a02.id_user, a02.id_auth_user " +
                "from    WM_AUTH_USER a02, WM_LIST_R_HOLDING_COMPANY d02, WM_PORTAL_LIST_SITE f02 " +
                "where   a02.IS_HOLDING = 1 and a02.ID_HOLDING = d02.ID_HOLDING and " +
                "        d02.ID_COMPANY = f02.ID_FIRM and f02.ID_SITE=? and a02.user_login=? " +
                "union " +
                "select  b04.id_firm, a04.user_login, a04.id_user, a04.id_auth_user " +
                "from    WM_AUTH_USER a04, WM_LIST_COMPANY b04, WM_PORTAL_LIST_SITE f04 " +
                "where   a04.is_root = 1 and b04.ID_FIRM = f04.ID_FIRM and f04.ID_SITE=? and " +
                "        a04.user_login=? ";

            ps = adapter.prepareStatement( sql_ );

            RsetTools.setLong( ps, 1, idSite );
            ps.setString( 2, userLogin );
            RsetTools.setLong( ps, 3, idSite );
            ps.setString( 4, userLogin );
            RsetTools.setLong( ps, 5, idSite );
            ps.setString( 6, userLogin );

            rs = ps.executeQuery();

            if ( rs.next() )
                isValid = true;

        }
        catch( Exception e1 ) {
            log.error( "SQL:\n" + sql_ );
            final String es = "Error check checkAccess()";
            log.error( es, e1 );
            throw new IllegalStateException( es, e1 );
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

    public boolean checkAccess( String userLogin, String userPassword, final String serverName ) {
        DatabaseAdapter db_ = null;
        try {
            db_ = DatabaseAdapter.getInstance();
            switch( db_.getFamily() ) {
                case DatabaseManager.MYSQL_FAMALY:
                    return checkAccessMySql( db_, userLogin, userPassword, serverName );
                default:
                    return checkAccess( db_, userLogin, userPassword, serverName );
            }
        }
        catch( Exception e1 ) {
            final String es = "Error check checkAccess()";
            log.error( es, e1 );
            throw new IllegalStateException( es, e1 );
        }
        finally {
            DatabaseManager.close( db_ );
            db_ = null;
        }
    }

    private String listToString(List<Long> list) {
        if (list.isEmpty())
            return "NULL";

        Iterator<Long> iterator = list.iterator();
        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        while (iterator.hasNext()) {
            Long aLong = iterator.next();

            if (isFirst) {
		isFirst = false;
	    }
	    else {
                sb.append( ',' );
            }

            sb.append( aLong );
        }
        return sb.toString();
    }

/*
    public static Long addRole(DatabaseAdapter db_, String role_name)
        throws Exception
    {
        PreparedStatement ps = null;
        try
        {
            String sql_ =
                "insert into WM_AUTH_ACCESS_GROUP " +
                "( ID_ACCESS_GROUP, NAME_ACCESS_GROUP ) " +
                "VALUES "+
                "( ?, ? )";

            CustomSequenceType seq = new CustomSequenceType();
            seq.setSequenceName("seq_WM_auth_access_group");
            seq.setTableName( "WM_AUTH_ACCESS_GROUP");
            seq.setColumnName( "ID_ACCESS_GROUP" );
            Long id = db_.getSequenceNextValue(seq);

            ps = db_.prepareStatement(sql_);

            ps.setObject(1, id);
            ps.setString(2, role_name);
            int i = ps.executeUpdate();

            if (log.isDebugEnabled())
                log.debug("Count of added role - "+i);

            return id;
        }
        catch (Exception e)
        {
            log.error("Error add role", e);
            throw e;
        }
        finally
        {
            DatabaseManager.close( ps );
            ps = null;
        }
    }

    public static Long checkWithCreateRole(DatabaseAdapter ora_, String role_name)
        throws Exception
    {

        Long id_group = getIDRole(ora_, role_name);
        if (id_group == null)
            id_group = addRole(ora_, role_name);

        return id_group;

    }

    public static Long getUserRole(DatabaseAdapter ora_, Long id_auth_user,
        Long id_role)
        throws Exception
    {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try
        {
            ps = ora_.prepareStatement(
                "select id_relate_accgroup from WM_AUTH_RELATE_ACCGROUP " +
                "where  id_auth_user=? and id_access_group=?"
            );
            ps.setObject(1, id_auth_user);
            ps.setObject(2, id_role);
            rs = ps.executeQuery();

            Long result = null;
            if (rs.next())
                result = RsetTools.getLong(rs, "id_relate_accgroup");

            return result;
        }
        catch (Exception e)
        {
            log.error("Error get user role", e);
            throw e;
        }
        finally
        {
            DatabaseManager.close( rs, ps );
            rs = null;
            ps = null;
        }
    }

    public static Long getUserRole(DatabaseAdapter ora_, Long id_user,
        String role_name)
        throws Exception
    {
        Long id_role = getIDRole(ora_, role_name);
        if (id_role == null)
            return null;

        return getUserRole(ora_, id_user, id_role);
    }

    public static boolean bindUserRole(DatabaseAdapter ora_, Long id_auth_user, String role_name)
        throws Exception
    {
        Long roleId = getIDRole(ora_, role_name);
        if (roleId==null) {
             return false;
        }
        return bindUserRole(ora_, id_auth_user, roleId );
    }

    public static boolean bindUserRole(DatabaseAdapter ora_, Long id_auth_user, Long id_role) {
        if (id_auth_user==null)  {
            throw new IllegalStateException( "authUserId argument must not be null" );
        }

        if (id_role==null)  {
            throw new IllegalStateException( "roleId argument must not be null" );
        }

        WmAuthRelateAccgroupItemType item = new WmAuthRelateAccgroupItemType();
        CustomSequenceType seq = new CustomSequenceType();
        seq.setSequenceName("SEQ_WM_AUTH_RELATE_ACCGROUP");
        seq.setTableName( "WM_AUTH_RELATE_ACCGROUP");
        seq.setColumnName( "ID_RELATE_ACCGROUP" );
        try {
            long id = ora_.getSequenceNextValue( seq );

            item.setIdRelateAccgroup( id );
            item.setIdAuthUser( id_auth_user );
            item.setIdAccessGroup( id_role );
            InsertWmAuthRelateAccgroupItem.process( ora_, item );
        }
        catch (Exception e) {
            final String es = "error bind role to user";
            log.error(es,e);
            throw new IllegalStateException( es );
        }
        return true;
    }

    public static Long addUserAuth(DatabaseAdapter db_, Long id_user,
        Long id_firm, Long id_service, Long id_road,
        String username, String password,
        boolean isFirm, boolean isService, boolean isRoad)
        throws Exception
    {

        PreparedStatement ps = null;
        try
        {
            CustomSequenceType seq = new CustomSequenceType();
            seq.setSequenceName("SEQ_WM_AUTH_USER");
            seq.setTableName( "WM_AUTH_USER");
            seq.setColumnName( "ID_AUTH_USER" );
            long id_auth_user = db_.getSequenceNextValue( seq );

            ps = db_.prepareStatement(
                "insert into WM_AUTH_USER " +
                "(ID_AUTH_USER, id_user, ID_FIRM, user_login, user_password, " +
                "is_service, is_road, is_use_current_firm, is_root, id_road, id_service)" +
                "values "+
                "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )"
            );

            ps.setLong(1, id_auth_user);
            RsetTools.setLong(ps, 2, id_user);
            RsetTools.setLong(ps, 3, id_firm);
            ps.setString(4, username);
            ps.setString(5, password);
            ps.setInt(6, isService ? 1 : 0);
            ps.setInt(7, isRoad ? 1 : 0);
            ps.setInt(8, isFirm ? 1 : 0);
            ps.setInt(9, 0);
            RsetTools.setLong(ps, 10, id_road);
            RsetTools.setLong(ps, 11, id_service);

            int i = ps.executeUpdate();

            if (log.isDebugEnabled())
                log.debug("Count of added role - "+i);

            return id_auth_user;
        }
        catch (Exception e)
        {
            log.error("Error add user auth", e);
            throw e;
        }
        finally
        {
            DatabaseManager.close( ps );
            ps = null;
        }
    }
*/

    public static Long addNewUser(DatabaseAdapter db_, String first_name,
        String last_name, String middle_name, Long id_firm,
        String email, String address, String phone)
        throws Exception
    {
        PreparedStatement ps = null;
        try
        {
            CustomSequenceType seq = new CustomSequenceType();
            seq.setSequenceName("seq_WM_LIST_USER");
            seq.setTableName( "WM_LIST_USER");
            seq.setColumnName( "ID_USER" );
            long id = db_.getSequenceNextValue( seq );

            ps = db_.prepareStatement(
                "insert into WM_LIST_USER " +
                "(ID_USER, FIRST_NAME, LAST_NAME, MIDDLE_NAME, " +
                "ID_FIRM, EMAIL, ADDRESS, TELEPHONE, DATE_START_WORK ) " +
                "values "+
                "( ?, ?, ?, ?, ?, ?, ?, ?, "+db_.getNameDateBind()+" )	"
            );
            ps.setLong(1, id);
            ps.setString(2, first_name);
            ps.setString(3, last_name);
            ps.setString(4, middle_name);
            ps.setObject(5, id_firm);
            ps.setString(6, email);
            ps.setString(7, address);
            ps.setString(8, phone);
            db_.bindDate(ps, 9, DateTools.getCurrentTime());
            int i = ps.executeUpdate();

            if (log.isDebugEnabled())
                log.debug("Count of added role - "+i);

            return id;
        }
        catch (Exception e)
        {
            log.error("Error addNewUser", e);
            throw e;
        }
        finally
        {
            DatabaseManager.close( ps );
            ps = null;
        }
    }

    public static Long getIDRole(DatabaseAdapter db_, String role_name)
        throws Exception
    {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try
        {
            ps = db_.prepareStatement(
                "select ID_ACCESS_GROUP from WM_AUTH_ACCESS_GROUP where NAME_ACCESS_GROUP=?"
            );
            ps.setString(1, role_name);
            rs = ps.executeQuery();
            if (rs.next())
                return RsetTools.getLong(rs, "ID_ACCESS_GROUP");

            return null;
        }
        catch (Exception e)
        {
            log.error("Error getIDRole", e);
            throw e;
        }
        finally
        {
            DatabaseManager.close(rs, ps);
            rs = null;
            ps = null;
        }
    }

    public List<RoleBean> getUserRoleList( AuthSession authSession ) {
        DatabaseAdapter db = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            db = DatabaseAdapter.getInstance();
            ps = db.prepareStatement(
                "select c.ID_ACCESS_GROUP, c.NAME_ACCESS_GROUP " +
                "from   WM_AUTH_ACCESS_GROUP c, WM_AUTH_RELATE_ACCGROUP b " +
                "where  b.ID_AUTH_USER=? and b.ID_ACCESS_GROUP=c.ID_ACCESS_GROUP "
            );

            ps.setLong( 1, authSession.getAuthInfo().getAuthUserId() );
            rs = ps.executeQuery();

            List<RoleBean> roles = new ArrayList<RoleBean>();
            while( rs.next() ) {
                RoleBeanImpl roleImpl = new RoleBeanImpl();

                roleImpl.setName( RsetTools.getString( rs, "NAME_ACCESS_GROUP" ) );
                roleImpl.setRoleId( RsetTools.getLong( rs, "ID_ACCESS_GROUP" ) );

                roles.add( roleImpl );
            }
            return roles;
        }
        catch(Exception e) {
            String es = "error";
            log.error( es, e );
            throw new IllegalStateException( es, e );
        }
        finally {
            DatabaseManager.close( rs, ps );
            rs = null;
            ps = null;
        }
    }

    public List<RoleBean> getRoleList( AuthSession authSession ) {
        DatabaseAdapter db = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            db = DatabaseAdapter.getInstance();
            ps = db.prepareStatement(
                "select  ID_ACCESS_GROUP, NAME_ACCESS_GROUP " +
                "from    WM_AUTH_ACCESS_GROUP "
            );

            rs = ps.executeQuery();
            return internalGetRoleList(rs);
        }
        catch( Exception e ) {
            String es = "error";
            log.error( es, e );
            throw new IllegalStateException( es, e );
        }
        finally {
            DatabaseManager.close( db, rs, ps );
            db = null;
            rs = null;
            ps = null;
        }
    }

    private static List<RoleBean> internalGetRoleList(ResultSet rs) throws SQLException {
        List<RoleBean> list = new ArrayList<RoleBean>();
        while( rs.next() ) {
            RoleBeanImpl bean = new RoleBeanImpl();
            Long id = RsetTools.getLong( rs, "ID_ACCESS_GROUP" );
            String name = RsetTools.getString( rs, "NAME_ACCESS_GROUP" );

            if( name != null && id != null ) {
                bean.setName( name );
                bean.setRoleId( id );
                list.add( bean );
            }
        }
        return list;
    }

    public List<RoleBean> getRoleList(AuthSession authSession, Long authUserId) {
        DatabaseAdapter db = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            db = DatabaseAdapter.getInstance();
            ps = db.prepareStatement(
                "select c.ID_ACCESS_GROUP, c.NAME_ACCESS_GROUP  " +
                "from   WM_AUTH_RELATE_ACCGROUP b, WM_AUTH_ACCESS_GROUP c " +
                "where  b.ID_AUTH_USER=? and " +
                "       b.ID_ACCESS_GROUP=c.ID_ACCESS_GROUP"
            );
            ps.setLong(1, authUserId);
            rs = ps.executeQuery();
            return internalGetRoleList(rs);
        }
        catch( Exception e ) {
            String es = "error";
            log.error( es, e );
            throw new IllegalStateException( es, e );
        }
        finally {
            DatabaseManager.close( db, rs, ps );
            db = null;
            rs = null;
            ps = null;
        }
    }

    public Long addRole( AuthSession authSession, RoleBean roleBean) {

        PreparedStatement ps = null;
        DatabaseAdapter dbDyn = null;
        try {

            dbDyn = DatabaseAdapter.getInstance();

            CustomSequenceType seq = new CustomSequenceType();
            seq.setSequenceName( "seq_WM_AUTH_ACCESS_GROUP" );
            seq.setTableName( "WM_AUTH_ACCESS_GROUP" );
            seq.setColumnName( "ID_ACCESS_GROUP" );
            Long sequenceValue = dbDyn.getSequenceNextValue( seq );


            ps = dbDyn.prepareStatement(
                "insert into WM_AUTH_ACCESS_GROUP " +
                "( ID_ACCESS_GROUP, NAME_ACCESS_GROUP ) values " +
                ( dbDyn.getIsNeedUpdateBracket() ? "(" : "" ) +
                " ?, ? " +
                ( dbDyn.getIsNeedUpdateBracket() ? ")" : "" )
            );

            RsetTools.setLong( ps, 1, sequenceValue );
            ps.setString( 2, roleBean.getName() );

            int i1 = ps.executeUpdate();

            if( log.isDebugEnabled() )
                log.debug( "Count of inserted records - " + i1 );

            dbDyn.commit();
            return sequenceValue;
        }
        catch( Exception e ) {
            try {
                if( dbDyn != null )
                    dbDyn.rollback();
            }
            catch( Exception e001 ) {
                // catch rollback error
            }
            String es = "Error add new role";
            log.error( es, e );
            throw new IllegalStateException( es, e );

        }
        finally {
            DatabaseManager.close( dbDyn, ps );
            dbDyn = null;
            ps = null;
        }
    }

    public void updateRole( AuthSession authSession, RoleBean roleBean ) {
        DatabaseAdapter dbDyn = null;
        PreparedStatement ps = null;
        try {

            dbDyn = DatabaseAdapter.getInstance();

            String sql =
                "update WM_AUTH_ACCESS_GROUP " +
                "set    NAME_ACCESS_GROUP=? " +
                "WHERE  ID_ACCESS_GROUP=? ";

            ps = dbDyn.prepareStatement( sql );

            ps.setString( 1, roleBean.getName() );
            ps.setLong( 2, roleBean.getRoleId() );

            int i1 = ps.executeUpdate();

            if( log.isDebugEnabled() )
                log.debug( "Count of updated record - " + i1 );

            dbDyn.commit();
        }
        catch( Exception e ) {
            try {
                if (dbDyn!=null)
                    dbDyn.rollback();
            }
            catch( Exception e001 ) {
                // catch rollback error
            }

            String es = "Error save role";
            log.error( es, e );
            throw new IllegalStateException( es, e );
        }
        finally {
            DatabaseManager.close( dbDyn, ps );
            dbDyn = null;
            ps = null;
        }
    }

    public void deleteRole( AuthSession authSession, RoleBean roleBean ) {
        DatabaseAdapter dbDyn = null;
        PreparedStatement ps = null;
        try {
            dbDyn = DatabaseAdapter.getInstance();

            if( roleBean.getRoleId() == null )
                throw new IllegalArgumentException( "role id is null" );

            String sql = "delete from WM_AUTH_ACCESS_GROUP where ID_ACCESS_GROUP=? ";

            ps = dbDyn.prepareStatement( sql );

            RsetTools.setLong( ps, 1, roleBean.getRoleId() );

            int i1 = ps.executeUpdate();

            if( log.isDebugEnabled() )
                log.debug( "Count of deleted records - " + i1 );

            dbDyn.commit();
        }
        catch( Exception e ) {
            try {
                if (dbDyn!=null)
                    dbDyn.rollback();
            }
            catch( Exception e001 ) {
                // catch rollback error
            }

            String es = "Error delete role";
            log.error( es, e );
            throw new IllegalStateException( es, e );
        }
        finally {
            DatabaseManager.close( dbDyn, ps );
            dbDyn = null;
            ps = null;
        }
    }

    public Long addUserInfo(AuthSession authSession, AuthUserExtendedInfo infoAuth) {
        if (infoAuth==null) {
            throw new IllegalStateException("Error add new user, infoAuth is null");
        }
        return addUserInfo(authSession, infoAuth.getAuthInfo(), infoAuth.getRoles());
    }

    public Long addUserInfo(AuthSession authSession, AuthInfo authInfo, List<RoleEditableBean> roles) {
        DatabaseAdapter db = null;
        try {
            db = DatabaseAdapter.getInstance();
            Long companyId = authSession.checkCompanyId( authInfo.getCompanyId() );
            Long holdingId = authSession.checkHoldingId( authInfo.getHoldingId() );

            Long id = addUserInfo(db, authInfo, roles, companyId, holdingId);

            db.commit();
            return id;
        }
        catch( Throwable e ) {
            try {
                if( db != null )
                    db.rollback();
            }
            catch( Exception e001 ) {
                // catch rollback error
            }
            final String es = "Error add user auth";
            log.error( es, e );
            throw new IllegalStateException( es, e );
        }
        finally {
            DatabaseManager.close( db );
            db = null;
        }
    }

    public Long addUserInfo(DatabaseAdapter db, AuthInfo authInfo, List<RoleEditableBean> roles, Long companyId, Long holdingId) {
        if (authInfo==null) {
            throw new IllegalStateException("Error add new auth of user, infoAuth.getAuthInfo() is null");
        }
        if (StringUtils.isBlank(authInfo.getUserLogin())) {
            throw new IllegalStateException("Error add new auth of user, username is null or blank");
        }
        if (StringUtils.isBlank(authInfo.getUserPassword())) {
            throw new IllegalStateException("Error add new auth of user, password is null or blank");
        }
        if (StringUtils.isBlank(authInfo.getUserPassword())) {
            throw new IllegalStateException("Error add new auth of user, password is null or blank");
        }
        if( authInfo.getUserId() == null ) {
            throw new IllegalArgumentException( "Error add new auth of user, userId is null" );
        }
        PreparedStatement ps = null;
        try {
            CustomSequenceType seq = new CustomSequenceType();
            seq.setSequenceName( "seq_WM_AUTH_USER" );
            seq.setTableName( "WM_AUTH_USER" );
            seq.setColumnName( "ID_AUTH_USER" );
            Long id = db.getSequenceNextValue( seq );

            ps = db.prepareStatement( "insert into WM_AUTH_USER " +
                "( ID_AUTH_USER, ID_FIRM, ID_HOLDING, " +
                "  ID_USER, USER_LOGIN, USER_PASSWORD, " +
                "IS_USE_CURRENT_FIRM, IS_HOLDING " +
                ") values (" +
                "?, " + // PK
                "?, " + // b1.companyId, " +
                "?, " + // b3.id_road, "+
                "?, ?, ?, ?, " +
                "? " +
                ")" );

            if( log.isDebugEnabled() ) {
                log.debug( "companyId " + companyId );
                log.debug( "holdingId " + holdingId );
            }

            RsetTools.setLong( ps, 1, id );
            if( companyId != null )
                RsetTools.setLong( ps, 2, companyId );
            else
                ps.setNull( 2, Types.INTEGER );

            if( holdingId != null )
                RsetTools.setLong( ps, 3, holdingId );
            else
                ps.setNull( 3, Types.INTEGER );


            RsetTools.setLong( ps, 4, authInfo.getUserId() );
            ps.setString( 5, authInfo.getUserLogin() );
            ps.setString( 6, authInfo.getUserPassword() );

            ps.setInt( 7, authInfo.isCompany()?1:0 );
            ps.setInt( 8, authInfo.isHolding() ? 1 : 0 );
            int i1 = ps.executeUpdate();

            if( log.isDebugEnabled() )
                log.debug( "Count of inserted records - " + i1 );

//            processDeletedRoles( db, roles );
            processNewRoles( db, roles, id );

            return id;
        }
        catch( Throwable e ) {
            final String es = "Error add user auth";
            log.error( es, e );
            throw new IllegalStateException( es, e );
        }
        finally {
            DatabaseManager.close( ps );
            ps = null;
        }
    }

    private void processDeletedRoles( DatabaseAdapter db_, AuthUserExtendedInfo infoAuth ) throws Exception {

        // do not process delete of roles for new users
        if (infoAuth==null || infoAuth.getAuthInfo()==null || infoAuth.getAuthInfo().getAuthUserId()==null) {
            return;
        }

        log.info( "Start delete roles for authUserId: " + infoAuth.getAuthInfo().getAuthUserId() +
            ", roles list: " + infoAuth.getRoles() );

        PreparedStatement ps = null;
        try {
            if (infoAuth.getRoles()==null) {
                log.info( "Role list is null, return.");
                return;
            }
            for (RoleEditableBean roleBeanImpl : infoAuth.getRoles()) {
                log.info("role: " + roleBeanImpl);

                if (!roleBeanImpl.isDelete()) {
                    continue;
                }

                ps = db_.prepareStatement(
                    "delete from WM_AUTH_RELATE_ACCGROUP " +
                        "where  ID_AUTH_USER=? and ID_ACCESS_GROUP=? ");

                ps.setLong(1, infoAuth.getAuthInfo().getAuthUserId());
                ps.setLong(2, roleBeanImpl.getRoleId());
                ps.executeUpdate();
                ps.close();
                ps = null;
            }
        }
        finally {
            DatabaseManager.close( ps );
            ps = null;

            log.info( "End delete roles");
        }
    }

    private void processNewRoles( DatabaseAdapter db, List<RoleEditableBean> roles, Long authUserId ) throws Exception {
        log.info("Start insert new roles for authUserId: " + authUserId +", roles list: " + roles);

        PreparedStatement ps = null;
        try {
            if (roles==null) {
                log.info( "Role list is null, return.");
                return;
            }

            for (RoleEditableBean role : roles) {
                log.info("Role: "+role.getName()+", id: "+role.getRoleId()+", new: " + role.isNew() + ", delete: " + role.isDelete());

                if (!role.isNew() || role.isDelete()) {
                    log.info("Skip this role");
                    continue;
                }

                CustomSequenceType seq = new CustomSequenceType();
                seq.setSequenceName("seq_WM_AUTH_RELATE_ACCGROUP");
                seq.setTableName("WM_AUTH_RELATE_ACCGROUP");
                seq.setColumnName("ID_RELATE_ACCGROUP");
                Long id = db.getSequenceNextValue(seq);

                ps = db.prepareStatement("insert into WM_AUTH_RELATE_ACCGROUP " +
                    "(ID_RELATE_ACCGROUP, ID_ACCESS_GROUP, ID_AUTH_USER ) " +
                    "values" +
                    "(?, ?, ? ) ");

                ps.setLong(1, id);
                ps.setLong(2, role.getRoleId());
                ps.setLong(3, authUserId);
                ps.executeUpdate();
                ps.close();
                ps = null;
            }
        }
        finally {
            DatabaseManager.close( ps );
            ps = null;

            log.info( "End add roles");
        }
    }

    public void updateUserInfo(AuthSession authSession, AuthUserExtendedInfo infoAuth) {
        log.info("Start update auth");

        PreparedStatement ps = null;
        DatabaseAdapter db = null;
        try {
            db = DatabaseAdapter.getInstance();
            String sql =
                "update WM_AUTH_USER " +
                    "set "+
                    "ID_FIRM=?, IS_USE_CURRENT_FIRM=?, "+
                    "ID_HOLDING=?, IS_HOLDING=? "+
                    "WHERE  ID_AUTH_USER=? ";

            ps = db.prepareStatement( sql );

            if (infoAuth.getAuthInfo().getCompanyId()==null) {
                ps.setNull( 1, Types.INTEGER );
                ps.setInt( 2, 0 );
            }
            else {
                ps.setLong( 1, infoAuth.getAuthInfo().getCompanyId() );
                ps.setInt( 2, infoAuth.getAuthInfo().isCompany()?1:0 );
            }

            if (infoAuth.getAuthInfo().getHoldingId()==null) {
                ps.setNull( 3, Types.INTEGER );
                ps.setInt( 4, 0 );
            }
            else {
                ps.setLong( 3, infoAuth.getAuthInfo().getHoldingId() );
                ps.setInt( 4, infoAuth.getAuthInfo().isHolding()?1:0 );
            }

            ps.setLong( 5, infoAuth.getAuthInfo().getAuthUserId() );
            ps.executeUpdate();

            processDeletedRoles( db, infoAuth );
            processNewRoles( db, infoAuth.getRoles(), infoAuth.getAuthInfo().getAuthUserId() );

            db.commit();
        }
        catch( Throwable e ) {
            try {
                if( db != null )
                    db.rollback();
            }
            catch( Exception e001 ) {
                // catch rollback error
            }

            final String es = "Error add user auth";
            log.error( es, e );
            throw new IllegalStateException( es, e );
        }
        finally {
            DatabaseManager.close( db, ps );
            ps = null;
            db = null;

            log.info("End update auth");
        }
    }

    public void deleteUserInfo(AuthSession authSession, AuthUserExtendedInfo infoAuth) {
        log.info("Start delete auth");

        DatabaseAdapter db = null;
        try {
            db = DatabaseAdapter.getInstance();
//            DeleteWmAuthRelateAccgroupWithIdAuthUser.process( db, infoAuth.getAuthInfo().getAuthUserId() );
//            DeleteWmAuthUserWithIdAuthUser.process( db, infoAuth.getAuthInfo().getAuthUserId() );
            DatabaseManager.runSQL(
                db, "delete from WM_AUTH_RELATE_ACCGROUP where ID_AUTH_USER=?",
                new Object[]{infoAuth.getAuthInfo().getAuthUserId()},
                new int[]{Types.DECIMAL}
            );

            DatabaseManager.runSQL(
                db, "delete from WM_AUTH_USER where ID_AUTH_USER=?",
                new Object[]{infoAuth.getAuthInfo().getAuthUserId()},
                new int[]{Types.DECIMAL}
            );

            db.commit();
        }
        catch( Throwable e ) {
            try {
                if( db != null )
                    db.rollback();
            }
            catch( Exception e001 ) {
                // catch rollback error
            }

            final String es = "Error add user auth";
            log.error( es, e );
            throw new IllegalStateException( es, e );
        }
        finally {
            DatabaseManager.close( db );
            db = null;

            log.info("End delete auth");
        }
    }

    public List<UserInfo> getUserInfoList(AuthSession authSession) {
            List<UserInfo> users = new ArrayList<UserInfo>();

        if( authSession==null ) {
            return users;
        }
        DatabaseAdapter db = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            db = DatabaseAdapter.getInstance();

	    String companyIdList = getGrantedCompanyId( db, authSession.getUserLogin() );

            String sql =
                "select * " +
                "from 	WM_LIST_USER " +
                "where  IS_DELETED=0 and ID_FIRM in (" + companyIdList + ")";

            ps = db.prepareStatement( sql );
            rs = ps.executeQuery();

            while( rs.next() ) {
		UserInfoImpl userInfo = new UserInfoImpl();
		set(rs, userInfo);
                users.add( userInfo );
            }
            return users;
        }
        catch( Exception e ) {
            String es = "Error load list of users";
            throw new IllegalStateException( es, e );
        }
        finally {
            DatabaseManager.close( db, rs, ps );
            db = null;
            rs = null;
            ps = null;
        }
    }

    public RoleBean getRole(Long roleId) {
        DatabaseAdapter db = null;
        try {
            db = DatabaseAdapter.getInstance();
            return getRole(db, roleId);
        }
        catch( Exception e ) {
            String es = "Error load role for id: " + roleId;
            throw new IllegalStateException( es, e );
        }
        finally {
            DatabaseManager.close( db );
            db = null;
        }
    }

    public RoleBean getRole(DatabaseAdapter db, Long roleId) {
        if( roleId == null ) {
            return null;
        }
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {

            String sql =
                "select ID_ACCESS_GROUP, NAME_ACCESS_GROUP " +
                "from 	WM_AUTH_ACCESS_GROUP " +
                "where  ID_ACCESS_GROUP=? ";

            ps = db.prepareStatement( sql );
            ps.setLong( 1, roleId );

            rs = ps.executeQuery();

            RoleBean role = null;
            if( rs.next() ) {
                role = loadRoleFromResultSet( rs );
            }
            return role;
        }
        catch( Exception e ) {
            String es = "Error load role for id: " + roleId;
            throw new IllegalStateException( es, e );
        }
        finally {
            DatabaseManager.close( rs, ps );
            rs = null;
            ps = null;
        }
    }

    public RoleBean getRole(DatabaseAdapter db, String roleName) {
        if( roleName == null ) {
            return null;
        }
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {

            String sql =
                "select ID_ACCESS_GROUP, NAME_ACCESS_GROUP " +
                "from 	WM_AUTH_ACCESS_GROUP " +
                "where  NAME_ACCESS_GROUP=? ";

            ps = db.prepareStatement( sql );
            ps.setString( 1, roleName);

            rs = ps.executeQuery();

            RoleBean role = null;
            if( rs.next() ) {
                role = loadRoleFromResultSet( rs );
            }
            return role;
        }
        catch( Exception e ) {
            String es = "Error load role for name: " + roleName;
            throw new IllegalStateException( es, e );
        }
        finally {
            DatabaseManager.close( rs, ps );
            rs = null;
            ps = null;
        }
    }

    private RoleBean loadRoleFromResultSet( ResultSet rs ) throws Exception {

        RoleBeanImpl role = new RoleBeanImpl();
        role.setRoleId( RsetTools.getLong( rs, "ID_ACCESS_GROUP" ) );
        role.setName( RsetTools.getString( rs, "NAME_ACCESS_GROUP" ) );

        return role;
    }

    private void set(ResultSet rs, UserInfoImpl userInfo) throws SQLException {
        userInfo.setUserId(RsetTools.getLong(rs, "ID_USER"));
        userInfo.setCompanyId(RsetTools.getLong(rs, "ID_FIRM"));
        userInfo.setFirstName(RsetTools.getString(rs, "FIRST_NAME"));
        userInfo.setMiddleName(RsetTools.getString(rs, "MIDDLE_NAME"));
        userInfo.setLastName(RsetTools.getString(rs, "LAST_NAME"));

        userInfo.setDateStartWork(RsetTools.getTimestamp(rs, "DATE_START_WORK"));

        userInfo.setDateFire(RsetTools.getTimestamp(rs, "DATE_FIRE"));

        userInfo.setAddress(RsetTools.getString(rs, "ADDRESS"));
        userInfo.setTelephone(RsetTools.getString(rs, "TELEPHONE"));

        userInfo.setDateBindProff(RsetTools.getTimestamp(rs, "DATE_BIND_PROFF"));

        userInfo.setHomeTelephone(RsetTools.getString(rs, "HOME_TELEPHONE"));
        userInfo.setEmail(RsetTools.getString(rs, "EMAIL"));
        userInfo.setDiscount(RsetTools.getDouble(rs, "DISCOUNT"));
    }
}
