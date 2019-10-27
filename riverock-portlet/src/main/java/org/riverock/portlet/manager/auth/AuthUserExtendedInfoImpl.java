/*
 * org.riverock.portlet - Portlet Library
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
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
package org.riverock.portlet.manager.auth;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.riverock.common.tools.StringTools;
import org.riverock.interfaces.sso.a3.AuthUserExtendedInfo;
import org.riverock.interfaces.sso.a3.bean.RoleEditableBean;
import org.riverock.interfaces.portal.bean.User;

/**
 * @author SMaslyukov
 *         Date: 22.05.2005
 *         Time: 16:35:58
 *         $Id: AuthUserExtendedInfoImpl.java 1056 2006-11-15 19:02:57Z serg_main $
 */
public class AuthUserExtendedInfoImpl implements AuthUserExtendedInfo, Serializable {
    private static final long serialVersionUID = 2043005502L;

    private AuthInfoImpl authInfo = null;
    private User userInfo = null;

    private String userName = null;
    private String companyName = null;
    private String holdingName = null;

    private String userPassword2 = null;
    private String userPassword = null;


    private List<RoleEditableBeanImpl> roles = new ArrayList<RoleEditableBeanImpl>();

    private Long newRoleId = null;

    public Long getNewRoleId() {
        return newRoleId;
    }

    public void setNewRoleId( Long newRoleId ) {
        this.newRoleId = newRoleId;
    }

    public AuthInfoImpl getAuthInfo() {
        return authInfo;
    }

    public User getUser() {
        return userInfo;
    }

    public void setAuthInfo( AuthInfoImpl authInfo ) {
        this.authInfo = authInfo;
    }

    public void setUser( User userInfo ) {
        this.userInfo = userInfo;
        if (userInfo!=null) {
            userName = StringTools.getUserName(
                userInfo.getFirstName(), userInfo.getMiddleName(), userInfo.getLastName()
            );
        }
    }

    public List<RoleEditableBean> getCurrentRoles() {
        List<RoleEditableBean> resultRoles = new ArrayList<RoleEditableBean>();
        for (RoleEditableBeanImpl role : roles) {
            if (!role.isDelete()) {
                resultRoles.add(role);
            }
        }
        return resultRoles;
    }

    @SuppressWarnings({"unchecked", "RedundantCast"})
    public List<RoleEditableBean> getRoles() {
        return (List)roles;
    }

    public void setCurrentRoles( List<RoleEditableBean> roles) {
//        this.roles = roles;
    }

    public void setRoles( List<RoleEditableBeanImpl> roles) {
        this.roles = roles;
    }

    public void addRole( RoleEditableBeanImpl roleImpl ) {
        this.roles.add(roleImpl);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName( String userName ) {
        this.userName = userName;
    }

    public void setUserPassword( String userPassword ) {
        this.userPassword = userPassword;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword2( String userPassword2 ) {
        this.userPassword2 = userPassword2;
    }

    public String getUserPassword2() {
        return userPassword2;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName( String companyName ) {
        this.companyName = companyName;
    }

    public String getHoldingName() {
        return holdingName;
    }

    public void setHoldingName( String holdingName ) {
        this.holdingName = holdingName;
    }

}
