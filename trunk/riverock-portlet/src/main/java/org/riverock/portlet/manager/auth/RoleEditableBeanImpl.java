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

import org.riverock.interfaces.sso.a3.bean.RoleEditableBean;
import org.riverock.interfaces.sso.a3.bean.RoleBean;

/**
 * @author SergeMaslyukov
 *         Date: 02.02.2006
 *         Time: 17:23:14
 *         $Id$
 */
public class RoleEditableBeanImpl implements RoleEditableBean, Serializable {
    private static final long serialVersionUID = 2043005512L;

    private boolean isNew = false;
    private boolean isDelete = false;
    private String name = null;
    private Long roleId = null;

    public RoleEditableBeanImpl( RoleBean role ) {
       this.name = role.getName();
       this.roleId = role.getRoleId();
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew( boolean aNew ) {
        isNew = aNew;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete( boolean delete ) {
        isDelete = delete;
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
}
