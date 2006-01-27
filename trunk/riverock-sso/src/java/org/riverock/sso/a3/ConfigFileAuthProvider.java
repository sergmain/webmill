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

import org.riverock.sso.schema.config.AuthProviderParametersListType;
import org.riverock.interfaces.sso.a3.UserInfo;
import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.interfaces.sso.a3.AuthInfo;

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

    public boolean isUserInRole(String userLogin, String userPassword, String role_) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean checkAccess(String userLogin, String userPassword, String serverName) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setParameters(final AuthProviderParametersListType params) {
    }

    public UserInfo initUserInfo( String userLogin ) {
        return null;
    }

    public String getGrantedUserId(String userLogin) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<Long> getGrantedUserIdList(String userLogin) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<Long> getGrantedCompanyIdList(String userLogin) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getGrantedGroupCompanyId(String userLogin) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<Long> getGrantedGroupCompanyIdList(String userLogin) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getGrantedHoldingId(String userLogin) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<Long> getGrantedHoldingIdList(String userLogin) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Long checkCompanyId(Long companyId, String userLogin) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Long checkGroupCompanyId(Long groupCompanyId, String userLogin) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Long checkHoldingId(Long holdingId, String userLogin) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean checkRigthOnUser(Long id_auth_user_check, Long id_auth_user_owner) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public AuthInfo getAuthInfo(String userLogin, String userPassword) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public AuthInfo getAuthInfo(Long authUserId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
