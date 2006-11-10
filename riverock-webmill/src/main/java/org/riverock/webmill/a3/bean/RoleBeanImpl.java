/*
 * org.riverock.webmill - Portal framework implementation
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
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

import org.riverock.interfaces.sso.a3.bean.RoleBean;

/**
 * @author SergeMaslyukov
 *         Date: 31.01.2006
 *         Time: 16:08:54
 *         $Id$
 */
@Entity
@Table(name="wm_auth_access_group")
@TableGenerator(
    name="TABLE_AUTH_ROLE",
    table="wm_portal_ids",
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
