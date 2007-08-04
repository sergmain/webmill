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
package org.riverock.portlet.manager.role;

import java.io.Serializable;

import org.riverock.interfaces.sso.a3.bean.RoleBean;
import org.riverock.portlet.main.AuthSessionBean;
import org.riverock.portlet.tools.FacesTools;
import org.riverock.portlet.tools.SiteUtils;

/**
 * @author SergeMaslyukov
 *
 */
public class RoleAction implements Serializable {
    private static final long serialVersionUID = 2057005501L;

	private RoleSessionBean roleSessionBean = null;
	private AuthSessionBean authSessionBean = null;

    public static final String[] ROLES = new String[]{"webmill.portal-manager"};

	public RoleAction() {
	}

	public AuthSessionBean getAuthSessionBean() {
		return authSessionBean;
	}

	public void setAuthSessionBean(AuthSessionBean authSessionBean) {
		this.authSessionBean = authSessionBean;
	}
	
	public RoleSessionBean getSessionBean() {
		return roleSessionBean;
	}

	public void setSessionBean(RoleSessionBean roleSessionBean) {
		this.roleSessionBean = roleSessionBean;
	}
			
	public String addRole() {
		roleSessionBean.setRole( new RoleBeanImpl() );
	
		return "role-add";
	}

	public String processAddRole() {
        SiteUtils.checkRights(FacesTools.getPortletRequest(), ROLES);

        Long roleId = authSessionBean.getAuthSession().addRole( roleSessionBean.getRole() );
        roleSessionBean.setCurrentRoleId( roleId );
        loadCurrentRole();
		return "role";
	}

	public String cancelAddRole() {
		loadCurrentRole();
		return "role";
	}

	public String processEditRole() {
        SiteUtils.checkRights(FacesTools.getPortletRequest(), ROLES);

        authSessionBean.getAuthSession().updateRole( roleSessionBean.getRole() );
		return "role";
	}

	public String cancelEditRole() {
		loadCurrentRole();
		return "role";
	}

	public String processDeleteRole() {
        SiteUtils.checkRights(FacesTools.getPortletRequest(), ROLES);

        authSessionBean.getAuthSession().deleteRole( roleSessionBean.getRole() );
		roleSessionBean.setRole( null );
		return "role";
	}
	
	public String selectRole() {
		loadCurrentRole();
		return "role";
	}

	private void loadCurrentRole() {
        RoleBean bean = authSessionBean.getAuthSession().getRole( roleSessionBean.getCurrentRoleId() );
        roleSessionBean.setRole( new RoleBeanImpl(bean) );
	}
}
