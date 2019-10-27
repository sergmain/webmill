/*
 * org.riverock.interfaces - Common classes and interafces shared between projects
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
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
 */
package org.riverock.interfaces.sso.a3;

import java.util.List;
import java.io.Serializable;

import org.riverock.interfaces.sso.a3.bean.AuthParameterBean;
import org.riverock.interfaces.sso.a3.bean.RoleBean;
import org.riverock.interfaces.portal.bean.Company;
import org.riverock.interfaces.portal.bean.Holding;
import org.riverock.interfaces.portal.bean.User;

/**
 * @author SergeMaslyukov
 *         Date: 31.01.2006
 *         Time: 15:28:42
 *         $Id: AuthProvider.java 1056 2006-11-15 19:02:57Z serg_main $
 */
public interface AuthProvider extends Serializable {
    public boolean isUserInRole( AuthSession authSession, String role_ );
    public boolean checkAccess( AuthSession authSession, String serverName );
    public void setParameters( List<List<AuthParameterBean>> params );

    public User getUser( AuthSession authSession );

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

    public Long addUser( AuthSession authSession, AuthUserExtendedInfo usserBean );
    public void updateUser( AuthSession authSession, AuthUserExtendedInfo infoAuth );
    public void deleteUser( AuthSession authSession, AuthUserExtendedInfo infoAuth );

    public List<User> getUserList( AuthSession authSession );
    public List<Company> getCompanyList( AuthSession authSession );
    public List<Holding> getHoldingList( AuthSession authSession );
}

