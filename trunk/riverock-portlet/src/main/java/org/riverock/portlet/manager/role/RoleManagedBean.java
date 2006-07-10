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
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

import org.riverock.portlet.main.AuthSessionBean;
import org.riverock.interfaces.sso.a3.bean.RoleBean;

/**
 * @author SergeMaslyukov
 *
 */
public class RoleManagedBean implements Serializable {
    private static final long serialVersionUID = 2057005505L;

	private RoleSessionBean roleSessionBean = null;
	private AuthSessionBean authSessionBean= null;

	public RoleManagedBean() {
	}

	public AuthSessionBean getAuthSessionBean() {
		return authSessionBean;
	}

	public void setAuthSessionBean(AuthSessionBean authSessionBean) {
		this.authSessionBean = authSessionBean;
	}
	
	public RoleSessionBean getRoleSessionBean() {
		return roleSessionBean;
	}

	public void setRoleSessionBean(RoleSessionBean roleSessionBean) {
		this.roleSessionBean = roleSessionBean;
	}
	
	public List<RoleBean> getRoleList() {
		List<RoleBean> list = authSessionBean.getAuthSession().getRoleList();

        	if (list==null) {
            		return null;
        	}

        	Iterator<RoleBean> iterator = list.iterator();
        	List<RoleBean> roles = new ArrayList<RoleBean>();
        	while(iterator.hasNext()) {
            		RoleBean role = iterator.next();
            		roles.add( new RoleBeanImpl(role) );
        	}
        	return roles;

	}

}
