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
package org.riverock.webmill.a3.bean;

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
 *  $Id$
 */
@Entity
@Table(name="wm_auth_user")
@TableGenerator(
    name="TABLE_AUTH_USER",
    table="wm_portal_ids",
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
    private boolean isHolding = false;

    @Column(name="IS_ROOT")
    private boolean isRoot = false;



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

