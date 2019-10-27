/*
 * org.riverock.webmill - Webmill portal with support jsr-168, xml/xslt and others things.
 * For more information, please visit project site http://webmill.riverock.org
 *
 * Copyright (C) 2000-2006, Riverock Software, All Rights Reserved.
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
package org.riverock.webmill.portal.bean;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;

import org.riverock.interfaces.sso.a3.AuthInfo;

/**
 *
 *  $Id: AuthInfoImpl.java 1350 2007-08-28 10:08:41Z serg_main $
 */
@Entity
@Table(name="WM_AUTH_USER")
@TableGenerator(
    name="TABLE_AUTH_USER",
    table="WM_PORTAL_IDS",
    pkColumnName = "sequence_name",
    valueColumnName = "sequence_next_value",
    pkColumnValue = "wm_auth_user",
    allocationSize = 1,
    initialValue = 1
)
public class AuthInfoImpl implements AuthInfo, Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.TABLE, generator = "TABLE_AUTH_USER")
    @Column(name="ID_AUTH_USER")
    private Long authUserId;
    
    @Column(name="ID_USER")
    private Long userId;

    @Column(name="ID_FIRM")
    private Long companyId;

    @Column(name="ID_HOLDING")
    private Long holdingId;

    @Column(name="USER_LOGIN")
    private String userLogin = "";

    @Column(name="USER_PASSWORD")
    private String userPassword = "";

    @Column(name="IS_USE_CURRENT_FIRM")
    private boolean isCompany = false;

    @Column(name="IS_HOLDING")
    private boolean isHolding;

    @Column(name="IS_ROOT")
    private boolean isRoot;



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

