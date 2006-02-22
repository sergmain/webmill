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
		roleSessionBean.setRole( bean );
	}
}
