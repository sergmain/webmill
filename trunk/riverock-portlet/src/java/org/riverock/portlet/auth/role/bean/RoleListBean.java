package org.riverock.portlet.auth.role.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author SergeMaslyukov
 *         Date: 13.01.2006
 *         Time: 9:13:14
 *         $Id$
 */
public class RoleListBean {
    private List<RoleBean> roles = null;
    private List<RoleBean> originRoles = new ArrayList<RoleBean>();

    public RoleListBean( List<RoleBean> roles ) {
        this.roles = new ArrayList<RoleBean>(roles);
        this.originRoles = new ArrayList<RoleBean>(roles);
    }

    public List<RoleBean> getRoles() {
        return roles;
    }

    public void setRoles( List<RoleBean> roles) {
        this.roles = roles;
    }

    public List<RoleBean> getOriginRoles() {
        return originRoles;
    }

    public void setOriginRoles( List<RoleBean> originRoles) {
        this.originRoles = originRoles;
    }


}
