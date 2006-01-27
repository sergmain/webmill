/**
 * 
 */
package org.riverock.portlet.auth.role.bean;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import org.riverock.portlet.auth.role.dao.RoleDAO;

/**
 * @author SergeMaslyukov
 *
 */
public class RoleSessionBean implements Serializable {
    private static final long serialVersionUID = 2057005504L;

	private RoleBean role = null;
	private Long currentRoleId = null;
	private RoleDAO roleDAO= null;

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

	public RoleDAO getRoleDAO() {
		return roleDAO;
	}

	public void setRoleDAO(RoleDAO roleDAO) {
		this.roleDAO = roleDAO;
	}
}
