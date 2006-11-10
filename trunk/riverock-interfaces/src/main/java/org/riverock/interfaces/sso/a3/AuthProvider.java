/*
 * org.riverock.interfaces - Common classes and interafces shared between projects
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
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
package org.riverock.interfaces.sso.a3;

import java.util.List;
import java.io.Serializable;

import org.riverock.interfaces.sso.a3.bean.AuthParameterBean;
import org.riverock.interfaces.sso.a3.bean.RoleBean;
import org.riverock.interfaces.portal.bean.Company;
import org.riverock.interfaces.portal.bean.Holding;

/**
 * @author SergeMaslyukov
 *         Date: 31.01.2006
 *         Time: 15:28:42
 *         $Id$
 */
public interface AuthProvider extends Serializable {
    public boolean isUserInRole( AuthSession authSession, String role_ );
    public boolean checkAccess( AuthSession authSession, String serverName );
    public void setParameters( List<List<AuthParameterBean>> params );

    public UserInfo getUserInfo( AuthSession authSession );

    public List<Long> getGrantedUserIdList( AuthSession authSession );
    public List<Long> getGrantedCompanyIdList( AuthSession authSession );
    public List<Long> getGrantedHoldingIdList( AuthSession authSession );

    public Long checkCompanyId(Long companyId, AuthSession authSession );
    public Long checkHoldingId(Long holdingId, AuthSession authSession );

    public boolean checkRigthOnUser( Long id_auth_user_check, Long id_auth_user_owner );

    public AuthInfo getAuthInfo(AuthSession authSession);
    public AuthInfo getAuthInfo(AuthSession authSession, Long authUserId);
    public List<AuthInfo> getAuthInfoList(AuthSession authSession);

    public RoleBean getRole(AuthSession authSession, Long roleId );

    // get list of all roles
    public List<RoleBean> getRoleList( AuthSession authSession );
    public List<RoleBean> getRoleList( AuthSession authSession, Long authUserId );

    // get list of granted roles for specific user
    public List<RoleBean> getUserRoleList( AuthSession authSession );

    public Long addRole( AuthSession authSession, RoleBean roleBean);
    public void updateRole( AuthSession authSession, RoleBean roleBean );
    public void deleteRole( AuthSession authSession, RoleBean roleBean );

    public Long addUser( AuthSession authSession, AuthUserExtendedInfo infoAuth );
    public void updateUser( AuthSession authSession, AuthUserExtendedInfo infoAuth );
    public void deleteUser( AuthSession authSession, AuthUserExtendedInfo infoAuth );

    public List<UserInfo> getUserList( AuthSession authSession );
    public List<Company> getCompanyList( AuthSession authSession );
    public List<Holding> getHoldingList( AuthSession authSession );
}

