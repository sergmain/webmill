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

import java.io.Serializable;
import java.util.List;

import org.riverock.interfaces.sso.a3.UserInfo;
import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.interfaces.sso.a3.AuthInfo;
import org.riverock.interfaces.sso.a3.AuthProvider;
import org.riverock.interfaces.sso.a3.AuthUserExtendedInfo;
import org.riverock.interfaces.sso.a3.bean.AuthParameterBean;
import org.riverock.interfaces.sso.a3.bean.RoleBean;
import org.riverock.interfaces.portal.bean.Company;
import org.riverock.interfaces.portal.bean.GroupCompany;
import org.riverock.interfaces.portal.bean.Holding;

/**
 * User: Admin
 * Date: Sep 9, 2003
 * Time: 2:55:25 PM
 *
 * $Id$
 */
public final class ConfigFileAuthProvider implements AuthProvider, Serializable {
    private static final long serialVersionUID = 20434672384237105L;

    public boolean isUserInRole(final AuthSession authSession, String role) {
        return false;
    }

    public boolean checkAccess(final AuthSession authSession, final String serverName) {
        return false;
    }

    public void setParameters(List<List<AuthParameterBean>> params) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public UserInfo getUserInfo(AuthSession authSession) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getGrantedUserId(AuthSession authSession) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<Long> getGrantedUserIdList(AuthSession authSession) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getGrantedCompanyId(AuthSession authSession) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<Long> getGrantedCompanyIdList(AuthSession authSession) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getGrantedGroupCompanyId(AuthSession authSession) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<Long> getGrantedGroupCompanyIdList(AuthSession authSession) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getGrantedHoldingId(AuthSession authSession) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<Long> getGrantedHoldingIdList(AuthSession authSession) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Long checkCompanyId(Long companyId, AuthSession authSession) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Long checkGroupCompanyId(Long groupCompanyId, AuthSession authSession) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Long checkHoldingId(Long holdingId, AuthSession authSession) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean checkRigthOnUser(Long id_auth_user_check, Long id_auth_user_owner) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public AuthInfo getAuthInfo(AuthSession authSession) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public AuthInfo getAuthInfo(AuthSession authSession, Long authUserId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<AuthInfo> getAuthInfoList(AuthSession authSession) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public RoleBean getRole(AuthSession authSession, Long roleId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<RoleBean> getRoleList(AuthSession authSession) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<RoleBean> getRoleList(AuthSession authSession, Long authUserId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<RoleBean> getUserRoleList(AuthSession authSession) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Long addRole(AuthSession authSession, RoleBean roleBean) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void updateRole(AuthSession authSession, RoleBean roleBean) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void deleteRole(AuthSession authSession, RoleBean roleBean) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public Long addUser(AuthSession authSession, AuthUserExtendedInfo infoAuth) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void updateUser(AuthSession authSession, AuthUserExtendedInfo infoAuth) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void deleteUser(AuthSession authSession, AuthUserExtendedInfo infoAuth) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<UserInfo> getUserList(AuthSession authSession) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<Company> getCompanyList(AuthSession authSession) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<GroupCompany> getGroupCompanyList(AuthSession authSession) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<Holding> getHoldingList(AuthSession authSession) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

}
