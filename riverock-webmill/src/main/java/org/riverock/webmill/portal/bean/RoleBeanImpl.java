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

import org.riverock.interfaces.sso.a3.bean.RoleBean;

/**
 * @author SergeMaslyukov
 *         Date: 31.01.2006
 *         Time: 16:08:54
 *         $Id: RoleBeanImpl.java 1350 2007-08-28 10:08:41Z serg_main $
 */
@Entity
@Table(name="WM_AUTH_ACCESS_GROUP")
@TableGenerator(
    name="TABLE_AUTH_ROLE",
    table="WM_PORTAL_IDS",
    pkColumnName = "sequence_name",
    valueColumnName = "sequence_next_value",
    pkColumnValue = "wm_auth_access_group",
    allocationSize = 1,
    initialValue = 1
)
public class RoleBeanImpl implements Serializable, RoleBean {
    private static final long serialVersionUID = 2057005507L;

    @Id
    @GeneratedValue(strategy= GenerationType.TABLE, generator = "TABLE_AUTH_ROLE")
    @Column(name="ID_ACCESS_GROUP")
    private Long roleId = null;

    @Column(name="NAME_ACCESS_GROUP")
    private String name = null;

    public RoleBeanImpl() {
    }

    public RoleBeanImpl(RoleBean bean) {
        this.roleId = bean.getRoleId();
        this.name = bean.getName();
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId( Long roleId ) {
        this.roleId = roleId;
    }

    public boolean equals( Object o ) {
        if (!(o instanceof RoleBean)) {
            return false;
        }

        RoleBean roleBean = (RoleBean)o;
        if ( roleBean.getRoleId()==null || roleId==null ) {
            return false;
        }
        return roleBean.getRoleId().equals( roleId );
    }

    public String toString() {
        return "[name:" + name + ",id:" + roleId + "]";
    }
}
