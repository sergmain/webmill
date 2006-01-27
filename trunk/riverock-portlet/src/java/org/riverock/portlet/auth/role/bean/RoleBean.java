package org.riverock.portlet.auth.role.bean;

import java.io.Serializable;

/**
 * @author SergeMaslyukov
 *         Date: 06.01.2006
 *         Time: 11:26:35
 *         $Id$
 */
public class RoleBean implements Serializable {
    private static final long serialVersionUID = 2057005507L;

    private Long roleId = null;
    private String name = null;

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId( Long roleId ) {
        this.roleId = roleId;
    }

    public boolean equals( RoleBean roleBean ) {
        if( roleBean == null || roleBean.getRoleId()==null || roleId==null ) {
            return false;
        }
        return roleBean.getRoleId().equals( roleId );
    }

    public String toString() {
        return "[name:" + name + ",id:" + roleId + "]";
    }
}
