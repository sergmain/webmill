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

import org.riverock.interfaces.sso.a3.AuthInfo;

/**
 * @author SergeMaslyukov
 *         Date: 02.02.2006
 *         Time: 17:12:12
 *         $Id$
 */
public class AuthInfoImpl implements AuthInfo, Serializable {

    private Long authUserId;
    private Long userId;
    private Long companyId;
    private Long holdingId;

    private String userLogin = "";
    private String userPassword = "";

    private boolean isCompany = false;
    private boolean isHolding = false;
    private boolean isRoot = false;

    public AuthInfoImpl() {
    }

    public AuthInfoImpl(AuthInfo info) {
        this.authUserId = info.getAuthUserId();
        this.userId = info.getUserId();
        this.companyId = info.getCompanyId();
        this.holdingId = info.getHoldingId();
        this.userLogin = info.getUserLogin();
        this.userPassword = info.getUserPassword();
        this.isCompany = info.isCompany();
        this.isHolding = info.isHolding();
        this.isRoot = info.isRoot();
    }

    public Long getAuthUserId() {
        return authUserId;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public Long getHoldingId() {
        return holdingId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public boolean isCompany() {
        return isCompany;
    }

    public boolean isHolding() {
        return isHolding;
    }

    public boolean isRoot() {
        return isRoot;
    }

    public void setAuthUserId(Long authUserId) {
        this.authUserId = authUserId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public void setHoldingId(Long holdingId) {
        this.holdingId = holdingId;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public void setCompany(boolean isCompany) {
        this.isCompany = isCompany;
    }

    public void setHolding(boolean isHolding) {
        this.isHolding = isHolding;
    }

    public void setRoot(boolean isRoot) {
        this.isRoot = isRoot;
    }
}
