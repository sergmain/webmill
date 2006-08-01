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
package org.riverock.webmill.a3.bean;

import java.io.Serializable;
import java.util.Date;

import org.riverock.interfaces.sso.a3.UserInfo;

/**
 * @author SergeMaslyukov
 *         Date: 31.01.2006
 *         Time: 16:32:21
 *         $Id$
 */
public class UserInfoImpl implements UserInfo, Serializable {
    private static final long serialVersionUID = 3043770851L;

    private java.lang.Long userId;
    private java.lang.Long companyId;
    private java.lang.String firstName;
    private java.lang.String middleName;
    private java.lang.String lastName;
    private java.util.Date dateStartWork;
    private java.util.Date dateFire;
    private java.lang.String address;
    private java.lang.String telephone;
    private java.util.Date dateBindProff;
    private java.lang.String homeTelephone;
    private java.lang.String email;
    private java.lang.Double discount;

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

    public Date getDateStartWork() {
        if (dateStartWork==null) {
            return null;
        }
        return new Date(dateStartWork.getTime());
    }

    public void setDateStartWork(Date dateStartWork) {
        if (dateStartWork==null) {
            this.dateStartWork=null;
            return;
        }
        this.dateStartWork = new Date(dateStartWork.getTime());
    }

    public Date getDateFire() {
        if (dateFire==null) {
            return null;
        }
        return new Date(dateFire.getTime());
    }

    public void setDateFire(Date dateFire) {
        if (dateFire==null) {
            this.dateFire=null;
            return;
        }
        this.dateFire = new Date(dateFire.getTime());
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Date getDateBindProff() {
        if (dateBindProff==null) {
            return null;
        }
        return new Date(dateBindProff.getTime());
    }

    public void setDateBindProff(Date dateBindProff) {
        if (dateBindProff==null) {
            this.dateBindProff=null;
            return;
        }
        this.dateBindProff = new Date(dateBindProff.getTime());
    }

    public String getHomeTelephone() {
        return homeTelephone;
    }

    public void setHomeTelephone(String homeTelephone) {
        this.homeTelephone = homeTelephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }
}
