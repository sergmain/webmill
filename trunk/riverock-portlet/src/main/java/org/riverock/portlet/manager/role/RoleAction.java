/*
 * org.riverock.portlet - Portlet Library
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
package org.riverock.portlet.manager.role;

import java.io.Serializable;
import org.riverock.portlet.main.AuthSessionBean;
import org.riverock.interfaces.sso.a3.bean.RoleBean;

/**
 * @author SergeMaslyukov
 *
 */
public class RoleAction implements Serializable {
    private static final long serialVersionUID = 2057005501L;

	private RoleSessionBean roleSessionBean = null;
	private AuthSessionBean authSessionBean = null;

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
        authSessionBean.getAuthSession().updateRole( roleSessionBean.getRole() );
		return "role";
	}

	public String cancelEditRole() {
		loadCurrentRole();
		return "role";
	}

	public String processDeleteRole() {
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
