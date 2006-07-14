/*
 * org.riverock.webmill.init - Webmill portal initializer web application
 * For more information about Webmill portal, please visit project site
 * http://webmill.askmore.info
 *
 * Copyright (C) 2000-2006, Riverock Software, All Rights Reserved.
 * Riverock - The Open-source Java Development Community,
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
package org.riverock.webmill.init.bean;

import java.io.Serializable;
import java.util.Date;

import org.riverock.common.tools.StringTools;
import org.riverock.webmill.init.utils.FacesTools;

/**
 * @author SergeMaslyukov
 *         Date: 13.07.2006
 *         Time: 21:55:44
 *         $Id: PortalUserSessionBean.java 753 2006-07-10 07:53:57Z serg_main $
 */
public class UserBean implements Serializable {
    private static final long serialVersionUID = 2057005507L;

    private Long userId = null;

    private Long companyId = null;
    private String companyName = null;
    private String firstName = null;
    private String middleName = null;
    private String lastName = null;

    private Date createdDate = null;
    private Date closedDate = null;
    private String address = null;
    private String phone = null;
    private String email = null;
    private boolean isDeleted = false;

    public UserBean() {
    }

    public UserBean(UserBean beanPortal) {
        this.userId = beanPortal.getUserId();
        this.companyId = beanPortal.getCompanyId();
        this.firstName = beanPortal.getFirstName();
        this.middleName = beanPortal.getMiddleName();
        this.lastName = beanPortal.getLastName();
        this.createdDate = beanPortal.getCreatedDate();
        this.closedDate = beanPortal.getDeletedDate();
        this.address = beanPortal.getAddress();
        this.phone = beanPortal.getPhone();
        this.email = beanPortal.getEmail();
        this.isDeleted = beanPortal.isDeleted();
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName( String companyName ) {
        this.companyName = FacesTools.convertParameter(companyName);
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId( Long companyId ) {
        this.companyId = companyId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName( String firstName ) {
        this.firstName = FacesTools.convertParameter(firstName);
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName( String middleName ) {
        this.middleName = FacesTools.convertParameter(middleName);
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName( String lastName ) {
        this.lastName = FacesTools.convertParameter(lastName);
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate( Date createdDate ) {
        this.createdDate = createdDate;
    }

    public Date getDeletedDate() {
        return closedDate;
    }

    public void setDeletedDate( Date closedDate ) {
        this.closedDate = closedDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress( String address ) {
        this.address = FacesTools.convertParameter(address);
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone( String phone ) {
        this.phone = FacesTools.convertParameter(phone);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail( String email ) {
        this.email = FacesTools.convertParameter(email);
    }

    public String getName() {
        return StringTools.getUserName( firstName, middleName, lastName );
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId( Long id ) {
        this.userId = id;
    }

    public boolean equals( UserBean portalUserBean ) {
        if( portalUserBean == null || portalUserBean.getUserId()==null || userId ==null ) {
            return false;
        }
        return portalUserBean.getUserId().equals( userId );
    }

    public String toString() {
        return "[name:" + getName() + ",userId:" + userId + "]";
    }
}
