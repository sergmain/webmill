package org.riverock.portlet.auth.role.bean;

import java.io.Serializable;
import org.riverock.interfaces.sso.a3.AuthSession;

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
		roleSessionBean.setRole( new RoleBean() );
	
		return "role-add";
	}

	public String processAddRole() {
		Long roleId = roleSessionBean.getRoleDAO().processAddRole(
			roleSessionBean.getRole(), 
			authSessionBean.getUserLogin(),
			authSessionBean.getGroupCompanyId(),
			authSessionBean.getHoldingId(),
            		authSessionBean.getAuthSession() );
			roleSessionBean.setCurrentRoleId( roleId );
		return "role";
	}

	public String cancelAddrole() {
		loadCurrentRole();
		return "role";
	}

	public String processEditRole() {
		sessionBean.getRoleDAO().processSaveRole(
			roleSessionBean.getRole(), authSessionBean.getAuthSession() );
		
		return "role";
	}

	public String cancelEditRole() {
		loadCurrentRole();
		return "role";
	}

	public String processDeleteRole() {
		roleSessionBean.getRoleDAO().processDeleteRole(
			roleSessionBean.getRole(), authSessionBean.getAuthSession() );

		roleSessionBean.setRole( null );
		return "role";
	}
	
	public String selectRole() {
		loadCurrentRole();
		return "role";
	}

	private void loadCurrentRole() {
		RoleBean bean = roleSessionBean.getRoleDAO().loadRole(
			sessionBean.getCurrentRoleId(), authSessionBean.getAuthSession() );
		sessionBean.setRole( bean );
	}
}
