package org.riverock.portlet.manager.role;

import java.io.Serializable;
import org.riverock.interfaces.sso.a3.bean.RoleBean;

/**
 * @author SergeMaslyukov
 *
 */
public class RoleSessionBean implements Serializable {
    private static final long serialVersionUID = 2057005504L;

	private RoleBean role = null;
	private Long currentRoleId = null;

	public RoleSessionBean() {
	}

	public RoleBean getRole() {
		return role;
	}

	public void setRole(RoleBean role) {
		this.role = role;
	}

	public Long getCurrentRoleId() {
		return currentRoleId;
	}

	public void setCurrentRoleId(Long currentRoleId) {
		this.currentRoleId = currentRoleId;
	}
}
