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
 */
package org.riverock.webmill.portal.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.Session;

import org.riverock.interfaces.sso.a3.AuthInfo;
import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.interfaces.sso.a3.AuthUserExtendedInfo;
import org.riverock.interfaces.sso.a3.bean.RoleBean;
import org.riverock.interfaces.sso.a3.bean.RoleEditableBean;
import org.riverock.interfaces.portal.bean.User;
import org.riverock.webmill.a3.bean.AuthInfoImpl;
import org.riverock.webmill.a3.bean.AuthRelateRole;
import org.riverock.webmill.a3.bean.RoleBeanImpl;
import org.riverock.webmill.portal.bean.UserBean;
import org.riverock.webmill.portal.utils.SiteList;
import org.riverock.webmill.utils.HibernateUtils;

/**
 * @author Sergei Maslyukov
 *         Date: 09.11.2006
 *         Time: 20:59:54
 *         <p/>
 *         $Id$
 */
public class HibernateAuthDaoImpl implements InternalAuthDao {
    private static Logger log = Logger.getLogger(HibernateAuthDaoImpl.class);

    public User getUser(String userLogin) {
        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();
            UserBean user = (UserBean)session.createQuery(
                "select user from org.riverock.webmill.portal.bean.UserBean as user, " +
                " org.riverock.webmill.a3.bean.AuthInfoImpl auth " +
                "where user.userId = auth.userId and auth.userLogin=:userLogin ")
                .setString("userLogin", userLogin)
                .uniqueResult();
            session.getTransaction().commit();
            return user;
        }
        finally {
            session.close();
        }
    }

    public List<Long> getGrantedUserIdList(String username) {
        Session session = HibernateUtils.getSession();
        try {
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
        finally {
            session.close();
        }
    }

    public List<Long> getGrantedCompanyIdList(String username) {
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

        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();
            List<Long> list = session
                .createSQLQuery(sql_)
                .addScalar("id_firm", Hibernate.LONG)
                .setString(0, username)
                .setString(1, username)
                .setString(2, username)
                .list();
/*
        Query query = session.createQuery(
            "select auth1.companyId from org.riverock.webmill.a3.bean.AuthInfoImpl auth1 " +
            "where  auth1.isCompany=true and auth1.userLogin=:userLogin1 " +
            "union " +
            "select relate2.companyId from org.riverock.webmill.a3.bean.AuthInfoImpl auth2, " +
            " org.riverock.webmill.portal.bean.HoldingCompanyRelationBean relate2 " +
            "where  auth2.isHolding=true and auth2.holdingId=relate2.holdingId and auth2.userLogin=:userLogin2 " +
            "union " +
            "select company3.companyId from org.riverock.webmill.a3.bean.AuthInfoImpl auth3, " +
            " org.riverock.webmill.portal.bean.CompanyBean company3 " +
            "where  auth3.isRoot=true and auth3.userLogin=:userLogin3 ");

        List<Long> list = query
            .setString("userLogin1", username)
            .setString("userLogin2", username)
            .setString("userLogin3", username)
            .list();
*/
            session.getTransaction().commit();
            return list;
        }
        finally {
            session.close();
        }
    }

    public List<Long> getGrantedHoldingIdList(String username) {
        String sql_ =
            "select  a01.ID_HOLDING "+
            "from    WM_AUTH_USER a01 "+
            "where   a01.IS_HOLDING=1 and a01.user_login=?  "+
            "union "+
            "select  b04.ID_HOLDING "+
            "from    WM_AUTH_USER a04, WM_LIST_HOLDING b04 "+
            "where   a04.is_root=1 and a04.user_login=?";
        
        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();
            List<Long> list = session.createSQLQuery( sql_ )
    /*
                "select auth1.holdingId from org.riverock.webmill.a3.bean.AuthInfoImpl auth1 " +
                "where  auth1.isHolding=true and auth1.userLogin=:userLogin1 " +
                "union " +
                "select holding2.holdingId from org.riverock.webmill.a3.bean.AuthInfoImpl auth2, " +
                " org.riverock.webmill.portal.bean.HoldingBean holding2 " +
                "where  auth2.isRoot=true and auth3.userLogin=:userLogin2 ")
    */
                .addScalar("ID_HOLDING", Hibernate.LONG)
                .setString(0, username)
                .setString(1, username)
                .list();
            session.getTransaction().commit();
            if (log.isDebugEnabled()) {
                log.debug("list: " + list);
                if (!list.isEmpty()) {
                    log.debug("   class: " + ((List)list).get(0).getClass().getName());
                }
            }
            return list;
        }
        finally {
            session.close();
        }
    }

    public Long checkCompanyId(Long companyId, String userLogin) {
        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();
            Long id = (Long)session.createQuery(
                "select company.id from org.riverock.webmill.portal.bean.CompanyBean as company " +
                "where company.id = :companyId and company.id in (:companyIds) ")
                .setParameterList("companyIds", getGrantedCompanyIdList(userLogin))
                .setLong("companyId", companyId)
                .uniqueResult();
            session.getTransaction().commit();
            return id;
        }
        finally {
            session.close();
        }
    }

    public Long checkHoldingId(Long holdingId, String userLogin) {
        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();
            Long id = (Long)session.createQuery(
                "select holding.id from org.riverock.webmill.portal.bean.HoldingBean as holding " +
                "where  holding.id = :holdingId and holding.id in (:holdingIds) ")
                .setParameterList("holdingIds", getGrantedHoldingIdList(userLogin))
                .setLong("holdingId", holdingId)
                .uniqueResult();
            session.getTransaction().commit();
            return id;
        }
        finally {
            session.close();
        }
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
        try {
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
                        "from  org.riverock.webmill.a3.bean.AuthInfoImpl as auth, " +
                        "      org.riverock.webmill.portal.bean.UserBean as user " +
                        "where auth.authUserId = :id_auth_user_check and auth.userId=user.userId and " +
                        "      user.companyId in ( :companyIds )" )
    /*

                        "select auth1.companyId from org.riverock.webmill.a3.bean.AuthInfoImpl auth1 " +
                        "where  auth1.isCompany=true and auth1.userLogin=:userLogin1 " +
                        "union " +
                        "select relate2.companyId from org.riverock.webmill.a3.bean.AuthInfoImpl auth2," +
                        " org.riverock.webmill.portal.bean.HoldingCompanyRelationBean relate2 " +
                        "where  auth2.isHolding=true and auth2.holdingId=relate2.holdingId and auth2.userLogin=:userLogin2 " +
                        "union " +
                        "select company3.companyId from org.riverock.webmill.a3.bean.AuthInfoImpl auth3, " +
                        " org.riverock.webmill.portal.bean.CompanyBean company3 " +
                        "where  auth3.isRoot=true and auth3.userLogin=:userLogin3 " +
                        ")")
                    .setString("userLogin1", auth.getUserLogin())
                    .setString("userLogin2", auth.getUserLogin())
                    .setString("userLogin3", auth.getUserLogin())
    */
                    .setParameterList("companyIds", getGrantedCompanyIdList(auth.getUserLogin()))
                    .setLong("id_auth_user_check", id_auth_user_check)
                    .uniqueResult();
            }
            session.getTransaction().commit();
            return id!=null;
        }
        finally {
            session.close();
        }
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
        try {
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
                        "from  org.riverock.webmill.a3.bean.AuthInfoImpl auth, " +
                        "      org.riverock.webmill.a3.bean.AuthRelateRole relateRole, " +
                        "      org.riverock.webmill.a3.bean.RoleBeanImpl role " +
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
        finally {
            session.close();
        }
    }

    public AuthInfo getAuthInfo(String login_, String pass_) {
        Session session = HibernateUtils.getSession();
        try {
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
        finally {
            session.close();
        }
    }

    public AuthInfo getAuthInfo(Long authUserId) {
        if (authUserId==null) {
            return null;
        }
        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();

            AuthInfoImpl authInfo = (AuthInfoImpl)session.createQuery(
                "select auth from org.riverock.webmill.a3.bean.AuthInfoImpl auth " +
                    "where auth.authUserId=:authUserId")
                .setLong("authUserId", authUserId)
                .uniqueResult();

            session.getTransaction().commit();
            return authInfo;
        }
        finally {
            session.close();
        }
    }

    public List<AuthInfo> getAuthInfoList(AuthSession authSession) {
        /*
        "select a.* " +
                "from   WM_AUTH_USER a, WM_LIST_USER b " +
                "where  a.ID_USER=b.ID_USER and  " +
                "       b.ID_FIRM  in ("+getGrantedCompanyId(db, authSession.getUserLogin())+") "
        */
        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();
            List<AuthInfoImpl> authInfos = session.createQuery(
                "select auth from org.riverock.webmill.a3.bean.AuthInfoImpl auth, " +
                    "     org.riverock.webmill.portal.bean.UserBean as user " +
                    "where auth.userId=user.userId and user.companyId in (:companyIds) ")
    /*

                    "select auth1.companyId from org.riverock.webmill.a3.bean.AuthInfoImpl auth1 " +
                    "where  auth1.isCompany=true and auth1.userLogin=:userLogin1 " +
                    "union " +
                    "select relate2.companyId from org.riverock.webmill.a3.bean.AuthInfoImpl auth2," +
                    " org.riverock.webmill.portal.bean.HoldingCompanyRelationBean relate2 " +
                    "where  auth2.isHolding=true and auth2.holdingId=relate2.holdingId and auth2.userLogin=:userLogin2 " +
                    "union " +
                    "select company3.companyId from org.riverock.webmill.a3.bean.AuthInfoImpl auth3, " +
                    " org.riverock.webmill.portal.bean.CompanyBean company3 " +
                    "where  auth3.isRoot=true and auth3.userLogin=:userLogin3 " +
                    ")")
                .setString("userLogin1", authSession.getUserLogin())
                .setString("userLogin2", authSession.getUserLogin())
                .setString("userLogin3", authSession.getUserLogin())
    */
                .setParameterList("companyIds", getGrantedCompanyIdList(authSession.getUserLogin()))
                .list();
            session.getTransaction().commit();
            return (List) authInfos;
        }
        finally {
            session.close();
        }
    }

    public List<AuthInfo> getAuthInfo(Long userId, Long siteId) {
        List<AuthInfo> list = new ArrayList<AuthInfo>();

        if (userId==null || siteId==null) {
            return list;
        }

        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();

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
            List ids = session.createSQLQuery( sql )
                .addScalar("id_auth_user", Hibernate.LONG)
                .setLong(0, siteId)
                .setLong(1, userId)
                .setLong(2, siteId)
                .setLong(3, userId)
                .setLong(4, siteId)
                .setLong(5, userId)
                .list();

            list = session.createQuery(
                "select auth from org.riverock.webmill.a3.bean.AuthInfoImpl auth " +
                    "where auth.authUserId in ( :ids ) ")
    /*

                    "select auth1.authUserId " +
                    "from   org.riverock.webmill.a3.bean.AuthInfoImpl auth1, " +
                    "       org.riverock.webmill.portal.bean.SiteBean site1 " +
                    "where  auth1.isCompany=true and auth1.companyId=site1.companyId and " +
                    "       auth1.userId=:userId1 and site1.siteId=:siteId1 " +
                    "union " +
                    "select auth2.authUserId " +
                    "from   org.riverock.webmill.a3.bean.AuthInfoImpl auth2, " +
                    "       org.riverock.webmill.portal.bean.SiteBean site2, " +
                    "       org.riverock.webmill.portal.bean.HoldingCompanyRelationBean relate2 " +
                    "where  auth2.isHolding=true and auth2.holdingId=relate2.holdingId and " +
                    "       relate2.companyId=site2.companyId and " +
                    "       auth2.userId=:userId2 and site2.siteId=:siteId2 " +
                    "union " +
                    "select auth3.authUserId " +
                    "from   org.riverock.webmill.a3.bean.AuthInfoImpl auth3, " +
                    "       org.riverock.webmill.portal.bean.CompanyBean company3, " +
                    "       org.riverock.webmill.portal.bean.SiteBean site3 " +
                    "where  auth3.isRoot=true and company3.companyId=site3.companyId and " +
                    "       auth3.userId=:userId3 and site3.siteId=:siteId3 " +
                    ")")
                .setLong("userId1", userId)
                .setLong("siteId1", siteId)
                .setLong("userId2", userId)
                .setLong("siteId2", siteId)
                .setLong("userId3", userId)
                .setLong("siteId3", siteId)
    */
                .setParameterList("ids", ids)
                .list();
            session.getTransaction().commit();
            return (List)list;
        }
        finally {
            session.close();
        }
    }

    public boolean checkAccess( String userLogin, String userPassword, final String serverName ) {
        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();

/*
        String sql_ =
            "select a.* from WM_AUTH_USER a, WM_LIST_USER b " +
            "where  a.USER_LOGIN=? and a.USER_PASSWORD=? and " +
            "       a.ID_USER = b.ID_USER and b.is_deleted=0";
*/

            try {
                AuthInfoImpl authInfo = (AuthInfoImpl)session.createQuery(
                    "select auth " +
                        "from  org.riverock.webmill.a3.bean.AuthInfoImpl auth, " +
                        "      org.riverock.webmill.portal.bean.UserBean as user " +
                        "where auth.userLogin=:userLogin and auth.userPassword=:userPassword and " +
                        "      auth.userId=user.userId and user.isDeleted=false")
                    .setString("userLogin", userLogin)
                    .setString("userPassword", userPassword)
                    .uniqueResult();

                if ( authInfo==null )
                    return false;

                if (  authInfo.isRoot() ) {
                    return true;
                }

    /*
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
    */

                Long siteId = SiteList.getSiteId( serverName );
                if ( log.isDebugEnabled() ) {
                    log.debug( "serverName " + serverName + ", siteId " + siteId);
                }

                List list = session.createQuery(
                        "select auth1.authUserId " +
                        "from   org.riverock.webmill.a3.bean.AuthInfoImpl auth1, " +
                        "       org.riverock.webmill.portal.bean.SiteBean site1 " +
                        "where  auth1.isCompany=true and auth1.companyId=site1.companyId and " +
                        "       auth1.userLogin=:userLogin1 and site1.siteId=:siteId1 "
                    )
                    .setString("userLogin1", userLogin)
                    .setLong("siteId1", siteId)
                    .list();
                if (!list.isEmpty()) {
                    return true;
                }

                list = session.createQuery(
                        "select auth2.authUserId " +
                        "from   org.riverock.webmill.a3.bean.AuthInfoImpl auth2, " +
                        "       org.riverock.webmill.portal.bean.SiteBean site2, " +
                        "       org.riverock.webmill.portal.bean.HoldingCompanyRelationBean relate2 " +
                        "where  auth2.isHolding=true and auth2.holdingId=relate2.holdingId and " +
                        "       relate2.companyId=site2.companyId and " +
                        "       auth2.userLogin=:userLogin2 and site2.siteId=:siteId2 "
                    )
                    .setString("userLogin2", userLogin)
                    .setLong("siteId2", siteId)
                    .list();
                if (!list.isEmpty()) {
                    return true;
                }

                list = session.createQuery(
                    "select auth3.authUserId " +
                        "from   org.riverock.webmill.a3.bean.AuthInfoImpl auth3, " +
                        "       org.riverock.webmill.portal.bean.CompanyBean company3, " +
                        "       org.riverock.webmill.portal.bean.SiteBean site3 " +
                        "where  auth3.isRoot=true and company3.companyId=site3.companyId and " +
                        "       auth3.userLogin=:userLogin3 and site3.siteId=:siteId3 ")
                    .setString("userLogin3", userLogin)
                    .setLong("siteId3", siteId)
                    .list();

                return !list.isEmpty();
            }
            finally {
                session.getTransaction().commit();
            }
        }
        finally {
            session.close();
        }
    }

    public List<RoleBean> getUserRoleList( AuthSession authSession ) {
        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();
/*
            ps = db.prepareStatement(
                "select c.ID_ACCESS_GROUP, c.NAME_ACCESS_GROUP " +
                "from   WM_AUTH_ACCESS_GROUP c, WM_AUTH_RELATE_ACCGROUP b " +
                "where  b.ID_AUTH_USER=? and b.ID_ACCESS_GROUP=c.ID_ACCESS_GROUP "
            );
*/
            List<RoleBeanImpl> roles = session.createQuery(
                "select role " +
                    "from  org.riverock.webmill.a3.bean.RoleBeanImpl role, " +
                    "      org.riverock.webmill.a3.bean.AuthRelateRole relate " +
                    "where role.roleId=relate.roleId and relate.authUserId=:authUserId ")
                .setLong("authUserId", authSession.getAuthInfo().getAuthUserId())
                .list();
            session.getTransaction().commit();
            return (List)roles;
        }
        finally {
            session.close();
        }
    }

    public List<RoleBean> getRoleList( AuthSession authSession ) {
        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();
/*
            ps = db.prepareStatement(
                "select  ID_ACCESS_GROUP, NAME_ACCESS_GROUP " +
                "from    WM_AUTH_ACCESS_GROUP "
            );
*/
            List<RoleBeanImpl> roles = session.createQuery(
                "select role from  org.riverock.webmill.a3.bean.RoleBeanImpl role ")
                .list();
            session.getTransaction().commit();
            return (List)roles;
        }
        finally {
            session.close();
        }
    }

    public List<RoleBean> getRoleList(AuthSession authSession, Long authUserId) {
        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();
/*
            ps = db.prepareStatement(
                "select c.ID_ACCESS_GROUP, c.NAME_ACCESS_GROUP  " +
                "from   WM_AUTH_RELATE_ACCGROUP b, WM_AUTH_ACCESS_GROUP c " +
                "where  b.ID_AUTH_USER=? and " +
                "       b.ID_ACCESS_GROUP=c.ID_ACCESS_GROUP"
            );
*/
            List<RoleBeanImpl> roles = session.createQuery(
                "select role " +
                    "from  org.riverock.webmill.a3.bean.RoleBeanImpl role, " +
                    "      org.riverock.webmill.a3.bean.AuthRelateRole relate " +
                    "where role.roleId=relate.roleId and relate.authUserId=:authUserId ")
                .setLong("authUserId", authUserId)
                .list();
            session.getTransaction().commit();
            return (List)roles;
        }
        finally {
            session.close();
        }
    }
/*
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
*/

    public Long addRole( AuthSession authSession, RoleBean roleBean) {
        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();
            RoleBeanImpl bean = new RoleBeanImpl(roleBean);
            session.save(bean);
            session.flush();
            session.getTransaction().commit();
            return bean.getRoleId();
        }
        finally {
            session.close();
        }
    }

    public void updateRole( AuthSession authSession, RoleBean roleBean ) {
        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();

            RoleBeanImpl bean = (RoleBeanImpl) session.createQuery(
                "select role from org.riverock.webmill.a3.bean.RoleBeanImpl role " +
                    "where role.roleId=:roleId")
                .setLong("roleId", roleBean.getRoleId())
                .uniqueResult();
            if (bean!=null) {
                bean.setName(roleBean.getName());
            }
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }

    public void deleteRole( AuthSession authSession, RoleBean roleBean ) {
        if (roleBean==null) {
            return;
        }
        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();
            session.createQuery(
            "delete org.riverock.webmill.a3.bean.RoleBeanImpl role where role.roleId=:roleId")
                .setLong("roleId", roleBean.getRoleId())
                .executeUpdate();
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }

    public Long addUserInfo(AuthSession authSession, AuthUserExtendedInfo infoAuth) {
        if (infoAuth==null) {
            throw new IllegalStateException("Error add new user, infoAuth is null");
        }
        return addUserInfo(authSession, infoAuth.getAuthInfo(), infoAuth.getRoles());
    }

    public Long addUserInfo(AuthSession authSession, AuthInfo authInfo, List<RoleEditableBean> roles) {
        Long companyId = authSession.checkCompanyId( authInfo.getCompanyId() );
        Long holdingId = authSession.checkHoldingId( authInfo.getHoldingId() );
        return addUserInfo(authInfo, roles, companyId, holdingId);
    }

    public Long addUserInfo(AuthInfo authInfo, List<RoleEditableBean> roles, Long companyId, Long holdingId) {
        if (authInfo==null) {
            throw new IllegalStateException("Error add new auth of user, infoAuth.getAuthInfoInternal() is null");
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
        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();
            AuthInfoImpl bean = new AuthInfoImpl();

            if( log.isDebugEnabled() ) {
                log.debug( "companyId: " + companyId );
                log.debug( "holdingId: " + holdingId );
                log.debug( "isRoot: " + authInfo.isRoot() );
                log.debug( "isCompany: " + authInfo.isCompany() );
                log.debug( "isHolding: " + authInfo.isHolding() );
            }
            bean.setCompanyId(companyId);
            bean.setHoldingId(holdingId);
            bean.setUserId(authInfo.getUserId());
            bean.setUserLogin(authInfo.getUserLogin());
            bean.setUserPassword(authInfo.getUserPassword());
            bean.setCompany(authInfo.isCompany());
            bean.setHolding(authInfo.isHolding());
            bean.setRoot(authInfo.isRoot());

            session.save(bean);

            for (RoleEditableBean role : roles) {
                if (log.isInfoEnabled()) {
                    log.info("Role: "+role.getName()+", id: "+role.getRoleId()+", " +
                        "new: " + role.isNew() + ", delete: " + role.isDelete());
                }
                if (!role.isNew() || role.isDelete()) {
                    log.info("Skip this role");
                    continue;
                }
                AuthRelateRole relate = new AuthRelateRole();
                relate.setAuthUserId(bean.getAuthUserId());
                relate.setRoleId(role.getRoleId());
                session.save(relate);
            }

            session.flush();
            session.getTransaction().commit();
            return bean.getAuthUserId();
        }
        finally {
            session.close();
        }
    }

    public void updateUserInfo(AuthSession authSession, AuthUserExtendedInfo infoAuth) {
        log.info("Start update auth");

        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();
            AuthInfoImpl bean = (AuthInfoImpl) session.createQuery(
                "select auth from org.riverock.webmill.a3.bean.AuthInfoImpl auth " +
                    "where auth.authUserId=:authUserId")
                .setLong("authUserId", infoAuth.getAuthInfo().getAuthUserId())
                .uniqueResult();
            if (bean!=null) {
                if (infoAuth.getAuthInfo().getCompanyId()==null) {
                    bean.setCompanyId(null);
                    bean.setCompany(false);
                }
                else {
                    bean.setCompanyId(infoAuth.getAuthInfo().getCompanyId());
                    bean.setCompany(infoAuth.getAuthInfo().isCompany());
                }
                if (infoAuth.getAuthInfo().getHoldingId()==null) {
                    bean.setHoldingId(null);
                    bean.setHolding(false);
                }
                else {
                    bean.setHoldingId( infoAuth.getAuthInfo().getHoldingId() );
                    bean.setHolding( infoAuth.getAuthInfo().isHolding() );
                }
            }

            processDeletedRoles( session, infoAuth );
            processNewRoles( session, infoAuth.getRoles(), infoAuth.getAuthInfo().getAuthUserId() );

            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }

    private void processDeletedRoles( Session session, AuthUserExtendedInfo infoAuth ) {

        // do not process delete of roles for new users
        if (infoAuth==null || infoAuth.getAuthInfo()==null || infoAuth.getAuthInfo().getAuthUserId()==null) {
            return;
        }

        log.info( "Start delete roles for authUserId: " + infoAuth.getAuthInfo().getAuthUserId() +
            ", roles list: " + infoAuth.getRoles() );

        if (infoAuth.getRoles()==null) {
            log.info( "Role list is null, return.");
            return;
        }
        for (RoleEditableBean roleBeanImpl : infoAuth.getRoles()) {
            log.info("role: " + roleBeanImpl);

            if (!roleBeanImpl.isDelete()) {
                continue;
            }
            AuthRelateRole relate = (AuthRelateRole) session.createQuery(
                "select relate from org.riverock.webmill.a3.bean.AuthRelateRole relate " +
                    "where relate.roleId=:roleId and relate.authUserId=:authUserId")
                .setLong("roleId", roleBeanImpl.getRoleId())
                .setLong("authUserId", infoAuth.getAuthInfo().getAuthUserId())
                .uniqueResult();
            session.delete(relate);
        }
    }

    private void processNewRoles( Session session, List<RoleEditableBean> roles, Long authUserId ) {
        log.info("Start insert new roles for authUserId: " + authUserId +", roles list: " + roles);

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
            AuthRelateRole bean = new AuthRelateRole();
            bean.setAuthUserId(authUserId);
            bean.setRoleId(role.getRoleId());
            session.save(bean);
        }
    }

    public void deleteUserInfo(AuthSession authSession, AuthUserExtendedInfo infoAuth) {
        log.info("Start delete auth");

        if (infoAuth==null) {
            return;
        }
        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();
            session.createQuery(
            "delete org.riverock.webmill.a3.bean.AuthRelateRole relate where relate.authUserId=:authUserId ")
                .setLong("authUserId", infoAuth.getAuthInfo().getAuthUserId())
                .executeUpdate();
            session.createQuery(
            "delete org.riverock.webmill.a3.bean.AuthInfoImpl auth where auth.authUserId=:authUserId ")
                .setLong("authUserId", infoAuth.getAuthInfo().getAuthUserId())
                .executeUpdate();

            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }

    public List<User> getUserInfoList(AuthSession authSession) {
            List<UserBean> users = new ArrayList<UserBean>();

        if( authSession==null ) {
            return (List)users;
        }
        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();
            users = session.createQuery(
                "select user from org.riverock.webmill.portal.bean.UserBean as user " +
                    "where user.isDeleted=false and user.companyId in ( :companyIds )")
    /*
                    "select auth1.companyId from org.riverock.webmill.a3.bean.AuthInfoImpl auth1 " +
                    "where  auth1.isCompany=true and auth1.userLogin=:userLogin1 " +
                    "union " +
                    "select relate2.companyId from org.riverock.webmill.a3.bean.AuthInfoImpl auth2," +
                    " org.riverock.webmill.portal.bean.HoldingCompanyRelationBean relate2 " +
                    "where  auth2.isHolding=true and auth2.holdingId=relate2.holdingId and auth2.userLogin=:userLogin2 " +
                    "union " +
                    "select company3.companyId from org.riverock.webmill.a3.bean.AuthInfoImpl auth3, " +
                    " org.riverock.webmill.portal.bean.CompanyBean company3 " +
                    "where  auth3.isRoot=true and auth3.userLogin=:userLogin3 " +
                    ")")
                    .setString("userLogin1", authSession.getUserLogin())
                    .setString("userLogin2", authSession.getUserLogin())
                    .setString("userLogin3", authSession.getUserLogin())
    */
                .setParameterList("companyIds", getGrantedCompanyIdList(authSession.getUserLogin()) )
                .list();
            session.getTransaction().commit();
            return (List)users;
        }
        finally {
            session.close();
        }
    }

    public RoleBean getRole(Long roleId) {
        if( roleId == null ) {
            return null;
        }
        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();
            RoleBeanImpl bean = (RoleBeanImpl)session.createQuery(
                "select role from  org.riverock.webmill.a3.bean.RoleBeanImpl role " +
                "where role.roleId=:roleId ")
                .setLong("roleId", roleId)
                .uniqueResult();
            session.getTransaction().commit();
            return bean;
        }
        finally {
            session.close();
        }
    }

    public RoleBean getRole(String roleName) {
        if( roleName == null ) {
            return null;
        }
        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();
            RoleBeanImpl bean = (RoleBeanImpl)session.createQuery(
                "select role from  org.riverock.webmill.a3.bean.RoleBeanImpl role " +
                "where role.name=:name ")
                .setString("name", roleName)
                .uniqueResult();
            session.getTransaction().commit();
            return bean;
        }
        finally {
            session.close();
        }
    }
}
