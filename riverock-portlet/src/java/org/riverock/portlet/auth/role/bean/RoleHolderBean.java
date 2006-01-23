package org.riverock.portlet.auth.role.bean;

import org.riverock.portlet.auth.role.logic.RoleProvider;

/**
 * @author SergeMaslyukov
 *         Date: 13.01.2006
 *         Time: 9:07:45
 *         $Id$
 */
public class RoleHolderBean {

    private RoleListBean roles = null;
    private RoleProvider roleProvider = null;

    public RoleProvider getRoleProvider() {
        return roleProvider;
    }

    public void setRoleProvider( RoleProvider roleProvider ) {
        this.roleProvider = roleProvider;
    }

    public RoleListBean getRoles() {
        if (roles==null) {
            roles = new RoleListBean( roleProvider.getRoleList() );
        }
        return roles;
    }

    public void setRoles( RoleListBean roles ) {
        this.roles = roles;
    }
}
