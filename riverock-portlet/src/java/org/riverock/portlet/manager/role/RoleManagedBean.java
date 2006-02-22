package org.riverock.portlet.manager.role;

import java.io.Serializable;
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
		return authSessionBean.getAuthSession().getRoleList();
	}

}
