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
package org.riverock.portlet.register.bean;

import java.util.Date;

import org.riverock.interfaces.portal.bean.UserRegistration;

/**
 * @author Sergei Maslyukov
 *         Date: 26.06.2006
 *         Time: 12:58:35
 */
public class UserRegistrationBean implements UserRegistration {

    private Long userId = null;

    private Long companyId = null;
    private String companyName = null;
    private String firstName = null;
    private String middleName = null;
    private String lastName = null;

    private Date createdDate = null;
    private Date deletedDate = null;
    private String address = null;
    private String phone = null;
    private String email = null;
    private boolean isDeleted = false;

    private String userLogin=null;
    private String userPassword=null;
    private String userPassword1=null;

    public String getUserPassword1() {
        return userPassword1;
    }

    public void setUserPassword1(String userPassword1) {
        this.userPassword1 = userPassword1;
    }

    public Date getDeletedDate() {
        if (deletedDate==null) {
            return null;
        }
        return new Date(deletedDate.getTime());
    }

    public void setDeletedDate(Date deletedDate) {
        if (deletedDate==null) {
            this.deletedDate=null;
            return;
        }
        this.deletedDate = new Date(deletedDate.getTime());
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getCreatedDate() {
        if (createdDate==null) {
            return null;
        }
        return new Date(createdDate.getTime());
    }

    public void setCreatedDate(Date createdDate) {
        if (createdDate==null) {
            this.createdDate=null;
            return;
        }
        this.createdDate = new Date(createdDate.getTime());
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
