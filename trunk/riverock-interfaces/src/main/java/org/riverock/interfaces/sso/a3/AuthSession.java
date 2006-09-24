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

import java.security.Principal;
import java.util.List;
import java.io.Serializable;

import org.riverock.interfaces.sso.a3.bean.RoleBean;
import org.riverock.interfaces.portal.bean.Company;
//import org.riverock.interfaces.portal.bean.GroupCompany;
import org.riverock.interfaces.portal.bean.Holding;

/**
 * @author SergeMaslyukov
 *         Date: 05.11.2005
 *         Time: 1:50:28
 *         $Id$
 */
public interface AuthSession extends Principal, Serializable {
    public boolean isUserInRole( String roleName );
    public String getUserLogin();
    public String getUserPassword();
    
    public boolean checkAccess( final String serverName );

    public String getGrantedUserId();
    public List<Long> getGrantedUserIdList();

    public String getGrantedCompanyId();
    public List<Long> getGrantedCompanyIdList();

    public String getGrantedHoldingId();
    public List<Long> getGrantedHoldingIdList();

    public Long checkCompanyId(Long companyId);
    public Long checkHoldingId(Long holdingId);

    public boolean checkRigthOnUser( Long id_auth_user_check, Long id_auth_user_owner );

    public UserInfo getUserInfo();

    public AuthInfo getAuthInfo();
    public AuthInfo getAuthInfo(Long authInfoId );
    public List<AuthInfo> getAuthInfoList();

    public RoleBean getRole( Long roleId );
    public List<RoleBean> getUserRoleList();
    public List<RoleBean> getRoleList();
    public List<RoleBean> getRoleList(Long authUserId);

    public Long addRole( RoleBean roleBean);
    public void updateRole( RoleBean roleBean );
    public void deleteRole( RoleBean roleBean );

    public Long addUser( AuthUserExtendedInfo authInfo );
    public void updateUser( AuthUserExtendedInfo authInfo );
    public void deleteUser( AuthUserExtendedInfo authInfo );

    public List<UserInfo> getUserList();
    public List<Company> getCompanyList();
    public List<Holding> getHoldingList();

}
