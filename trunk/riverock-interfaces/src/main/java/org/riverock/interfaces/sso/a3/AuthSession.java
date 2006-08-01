/*
 * org.riverock.interfaces -- Common classes and interafces shared between projects
 *
 * Copyright (C) 2004, Riverock Software, All Rights Reserved.
 *
 * Riverock -- The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
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
