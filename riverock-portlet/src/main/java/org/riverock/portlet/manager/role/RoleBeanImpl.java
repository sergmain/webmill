/*
 * org.riverock.portlet -- Portlet Library
 *
 * Copyright (C) 2004, Riverock Software, All Rights Reserved.
 *
 * Riverock -- The Open-source Java Development Community
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
 *
 */
package org.riverock.portlet.manager.role;

import java.io.Serializable;

import org.riverock.interfaces.sso.a3.bean.RoleBean;
import org.riverock.portlet.tools.FacesTools;

/**
 * @author SergeMaslyukov
 *         Date: 06.01.2006
 *         Time: 11:26:35
 *         $Id$
 */
public class RoleBeanImpl implements Serializable, RoleBean {
    private static final long serialVersionUID = 2057005507L;

    private Long roleId = null;
    private String name = null;

    public RoleBeanImpl() {
    }

    public RoleBeanImpl(RoleBean roleBean){
        this.roleId = roleBean.getRoleId();
        this.name = roleBean.getName();
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = FacesTools.convertParameter(name);
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId( Long roleId ) {
        this.roleId = roleId;
    }

    public boolean equals( RoleBean roleBean ) {
        if( roleBean == null || roleBean.getRoleId()==null || roleId==null ) {
            return false;
        }
        return roleBean.getRoleId().equals( roleId );
    }

    public String toString() {
        return "[name:" + name + ",id:" + roleId + "]";
    }
}
