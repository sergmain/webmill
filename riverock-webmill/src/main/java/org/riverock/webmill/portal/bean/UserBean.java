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

import java.util.Date;
import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;

import org.riverock.interfaces.portal.bean.User;
import org.riverock.common.tools.StringTools;

/**
 * @author Sergei Maslyukov
 *         Date: 29.05.2006
 *         Time: 15:28:27
 */
@Entity
//@Table(name="WM_LIST_USER")
@Table(name="wm_list_user")
@TableGenerator(
    name="TABLE_LIST_USER",
    table="WM_PORTAL_IDS",
    pkColumnName = "sequence_name",
    valueColumnName = "sequence_next_value",
    pkColumnValue = "wm_list_user",
    allocationSize = 1,
    initialValue = 1
)
public class UserBean implements User, Serializable {
    private static final long serialVersionUID = 2057005507L;

    @Id
    @GeneratedValue(strategy= GenerationType.TABLE, generator = "TABLE_LIST_USER")
    @Column(name="ID_USER")
    private Long userId = null;

    @Column(name="DATE_BIND_PROFF")
    private Date dateBindProff;

    @Column(name="ID_FIRM")
    private Long companyId = null;

    @Column(name="FIRST_NAME")
    private String firstName = null;

    @Column(name="MIDDLE_NAME")
    private String middleName = null;

    @Column(name="LAST_NAME")
    private String lastName = null;

    @Column(name="DATE_START_WORK")
    private Date createdDate = null;

    @Column(name="DATE_FIRE")
    private Date deletedDate = null;

    @Column(name="ADDRESS")
    private String address = null;

    @Column(name="TELEPHONE")
    private String phone = null;

    @Column(name="EMAIL")
    private String email = null;

    @Column(name="IS_DELETED")
    private boolean isDeleted = false;

    public UserBean() {
    }

    public UserBean(User beanPortal) {
        this.userId = beanPortal.getUserId();
        this.companyId = beanPortal.getCompanyId();
        this.firstName = beanPortal.getFirstName();
        this.middleName = beanPortal.getMiddleName();
        this.lastName = beanPortal.getLastName();
        this.createdDate = beanPortal.getCreatedDate();
        this.deletedDate = beanPortal.getDeletedDate();
        this.address = beanPortal.getAddress();
        this.phone = beanPortal.getPhone();
        this.email = beanPortal.getEmail();
        this.isDeleted = beanPortal.isDeleted();
    }

    public Date getDateBindProff() {
        return dateBindProff;
    }

    public void setDateBindProff(Date dateBindProff) {
        this.dateBindProff = dateBindProff;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
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
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName( String middleName ) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName( String lastName ) {
        this.lastName = lastName;
    }

    public Date getCreatedDate() {
        if (createdDate==null) {
            return null;
        }
        return new Date(createdDate.getTime());
    }

    public void setCreatedDate( Date createdDate ) {
        if (createdDate==null) {
            this.createdDate=null;
            return;
        }
        this.createdDate = new Date(createdDate.getTime());
    }

    public Date getDeletedDate() {
        if (deletedDate==null) {
            return null;
        }
        return new Date(deletedDate.getTime());
    }

    public void setDeletedDate( Date closedDate ) {
        if (closedDate==null) {
            this.deletedDate=null;
            return;
        }
        this.deletedDate = new Date(closedDate.getTime());
    }

    public String getAddress() {
        return address;
    }

    public void setAddress( String address ) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone( String phone ) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail( String email ) {
        this.email = email;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId( Long id ) {
        this.userId = id;
    }

    public boolean equals( Object portalUserBean ) {
        if (!(portalUserBean instanceof User) || ((User)portalUserBean).getUserId()==null || userId ==null ) {
            return false;
        }
        return ((User)portalUserBean).getUserId().equals( userId );
    }

    public String toString() {
        return "[name:" + StringTools.getUserName(firstName, middleName, lastName) + ",userId:" + userId + "]";
    }
}
