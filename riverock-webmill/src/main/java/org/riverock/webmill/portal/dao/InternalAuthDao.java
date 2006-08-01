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
package org.riverock.webmill.portal.dao;

import java.util.List;
import java.io.Serializable;

import org.riverock.interfaces.sso.a3.AuthInfo;
import org.riverock.interfaces.sso.a3.UserInfo;
import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.interfaces.sso.a3.AuthUserExtendedInfo;
import org.riverock.interfaces.sso.a3.bean.RoleBean;
import org.riverock.interfaces.sso.a3.bean.RoleEditableBean;
import org.riverock.generic.db.DatabaseAdapter;

/**
 * @author SergeMaslyukov
 *         Date: 30.01.2006
 *         Time: 19:33:27
 *         $Id$
 */
public interface InternalAuthDao extends Serializable {
    public String getGrantedUserId(String userLogin);
    public String getGrantedCompanyId(String userLogin);

    public List<Long> getGrantedUserIdList(String userLogin);
    public List<Long> getGrantedCompanyIdList(String userLogin);

    public String getGrantedHoldingId(String userLogin);
    public List<Long> getGrantedHoldingIdList(String userLogin);

    public Long checkCompanyId(Long companyId, String userLogin);
    public Long checkHoldingId(Long holdingId, String userLogin);

    public boolean checkRigthOnUser( Long id_auth_user_check, Long id_auth_user_owner );

    public boolean isUserInRole( String userLogin, String userPassword, final String role_ );

    public RoleBean getRole(Long roleId);
    public RoleBean getRole(DatabaseAdapter db, Long roleId);
    public RoleBean getRole(DatabaseAdapter db, String roleName);
    public List<RoleBean> getUserRoleList( AuthSession authSession );

    public AuthInfo getAuthInfo(String userLogin, String userPassword);
    public AuthInfo getAuthInfo(Long authUserId);
    public List<AuthInfo> getAuthInfoList(AuthSession authSession);

    public boolean checkAccess( String userLogin, String userPassword, final String serverName ) ;

    public UserInfo getUserInfo(String userLogin);

    public List<RoleBean> getRoleList( AuthSession authSession );
    public List<RoleBean> getRoleList( AuthSession authSession, Long authUserId );

    public Long addRole( AuthSession authSession, RoleBean roleBean);
    public void updateRole( AuthSession authSession, RoleBean roleBean );
    public void deleteRole( AuthSession authSession, RoleBean roleBean );

    public Long addUserInfo(AuthSession authSession, AuthUserExtendedInfo infoAuth);
    public Long addUserInfo(AuthSession authSession, AuthInfo authInfo, List<RoleEditableBean> roles);
    public Long addUserInfo(DatabaseAdapter db_, AuthInfo authInfo, List<RoleEditableBean> roles,
                            Long companyId, Long holdingId);

    public void updateUserInfo(AuthSession authSession, AuthUserExtendedInfo infoAuth);
    public void deleteUserInfo(AuthSession authSession, AuthUserExtendedInfo infoAuth);
    public List<UserInfo> getUserInfoList(AuthSession authSession);

    public UserInfo getUserInfo(DatabaseAdapter db_, String userLogin);

    public List<AuthInfo> getAuthInfo(DatabaseAdapter db_, Long userId, Long siteId);
}
