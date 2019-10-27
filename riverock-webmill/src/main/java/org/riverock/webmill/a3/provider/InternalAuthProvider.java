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
package org.riverock.webmill.a3.provider;

import java.io.Serializable;
import java.util.List;

import org.riverock.interfaces.portal.bean.Company;
import org.riverock.interfaces.portal.bean.Holding;
import org.riverock.interfaces.portal.bean.User;
import org.riverock.interfaces.sso.a3.AuthInfo;
import org.riverock.interfaces.sso.a3.AuthProvider;
import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.interfaces.sso.a3.AuthUserExtendedInfo;
import org.riverock.interfaces.sso.a3.bean.AuthParameterBean;
import org.riverock.interfaces.sso.a3.bean.RoleBean;
import org.riverock.webmill.portal.dao.InternalAuthDao;
import org.riverock.webmill.portal.dao.InternalCompanyDao;
import org.riverock.webmill.portal.dao.InternalDaoFactory;
import org.riverock.webmill.portal.dao.InternalHoldingDao;

/**
 * User: Admin
 * Date: Sep 6, 2003
 * Time: 10:33:28 PM
 *
 * $Id: InternalAuthProvider.java 1425 2007-09-12 19:44:02Z serg_main $
 */
public final class InternalAuthProvider implements AuthProvider, Serializable {
    private static final long serialVersionUID = 4055005503L;

    private InternalAuthDao authDao = InternalDaoFactory.getInternalAuthDao();
    private InternalCompanyDao companyDao = InternalDaoFactory.getInternalCompanyDao();
    private InternalHoldingDao internalHoldingDao = InternalDaoFactory.getInternalHoldingDao();

    private transient ClassLoader classLoader = null;

    public InternalAuthProvider() {
        // Todo remove hack with getting classLoader ref
        this.classLoader = Thread.currentThread().getContextClassLoader();
    }

    public boolean checkAccess( AuthSession authSession, final String serverName ) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            return authDao.checkAccess( authSession.getUserLogin(), authSession.getUserPassword(), serverName );
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }

    public void setParameters(List<List<AuthParameterBean>> params) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }

    public boolean isUserInRole( AuthSession authSession, final String role_ ) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            return authDao.isUserInRole(authSession.getUserLogin(), authSession.getUserPassword(), role_);
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }

    public User getUser( AuthSession authSession ) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            return authDao.getUser( authSession.getUserLogin() );
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }

    public List<Long> getGrantedUserIdList( AuthSession authSession ) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            return authDao.getGrantedUserIdList( authSession.getUserLogin() );
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }

    public List<Long> getGrantedCompanyIdList( AuthSession authSession ) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            return authDao.getGrantedCompanyIdList( authSession.getUserLogin() );
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }

    public List<Long> getGrantedHoldingIdList( AuthSession authSession ) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            return authDao.getGrantedHoldingIdList( authSession.getUserLogin() );
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }

    public Long checkCompanyId(Long companyId , AuthSession authSession ) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            return authDao.checkCompanyId( companyId, authSession.getUserLogin() );
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }

    public Long checkHoldingId(Long holdingId, AuthSession authSession ) {
        if (holdingId==null) {
            return null;
        }
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            return authDao.checkHoldingId( holdingId, authSession.getUserLogin() );
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }

    public boolean checkRigthOnUser(Long id_auth_user_check, Long id_auth_user_owner) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            return authDao.checkRigthOnUser( id_auth_user_check, id_auth_user_owner );
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }

    public AuthInfo getAuthInfo(AuthSession authSession) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            return authDao.getAuthInfo( authSession.getUserLogin(), authSession.getUserPassword() );
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }

    public List<AuthInfo> getAuthInfoList(AuthSession authSession) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            return authDao.getAuthInfoList( authSession );
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }

    public AuthInfo getAuthInfo(AuthSession authSession, Long authUserId) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );

            final AuthInfo currentAuthInfo = getAuthInfo( authSession );
            final AuthInfo authInfo = authDao.getAuthInfo( authUserId );
            if( currentAuthInfo==null || authInfo==null ||
                !checkRigthOnUser(currentAuthInfo.getAuthUserId(), authInfo.getAuthUserId() ) ) {
                return null;
            }
            return authInfo;
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }

    public RoleBean getRole( AuthSession authSession , Long roleId) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            return authDao.getRole(roleId );
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }

    public List<RoleBean> getUserRoleList( AuthSession authSession ) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            return authDao.getUserRoleList( authSession );
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }

    public List<RoleBean> getRoleList( AuthSession authSession ) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            return authDao.getRoleList( authSession );
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }

    public List<RoleBean> getRoleList(AuthSession authSession, Long authUserId) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            return authDao.getRoleList( authSession, authUserId );
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }

    public Long addRole( AuthSession authSession, RoleBean roleBean) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            return authDao.addRole( authSession, roleBean );
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }

    public void updateRole( AuthSession authSession, RoleBean roleBean ) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            authDao.updateRole( authSession, roleBean );
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }

    public void deleteRole( AuthSession authSession, RoleBean roleBean ) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            authDao.deleteRole( authSession, roleBean );
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }

    public Long addUser(AuthSession authSession, AuthUserExtendedInfo infoAuth) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            return authDao.addUserInfo( authSession, infoAuth );
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }

    public void updateUser(AuthSession authSession, AuthUserExtendedInfo infoAuth) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            authDao.updateUserInfo( authSession, infoAuth );
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }

    public void deleteUser(AuthSession authSession, AuthUserExtendedInfo infoAuth) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            authDao.deleteUserInfo( authSession, infoAuth );
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }

    public List<User> getUserList(AuthSession authSession) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            return authDao.getUserInfoList( authSession );
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }

    public List<Company> getCompanyList(AuthSession authSession) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            return companyDao.getCompanyList( authSession );
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }

    public List<Holding> getHoldingList(AuthSession authSession) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            return internalHoldingDao.getHoldingList( authSession );
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }
}
