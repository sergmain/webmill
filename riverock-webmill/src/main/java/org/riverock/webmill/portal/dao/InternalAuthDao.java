/*
 * org.riverock.webmill - Portal framework implementation
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
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
    public List<Long> getGrantedUserIdList(String userLogin);
    public List<Long> getGrantedCompanyIdList(String userLogin);
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
