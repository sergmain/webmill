package org.riverock.portlet.auth.user.bean;

import java.io.Serializable;

/**
 * @author SergeMaslyukov
 *         Date: 06.01.2006
 *         Time: 11:26:35
 *         $Id$
 */
public class RoleBean implements Serializable {
    private static final long serialVersionUID = 2043005507L;

    private Long roleId = null;
    private String name = null;
    private boolean isDelete = false;
    private boolean isNew = false;

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete( boolean delete ) {
        isDelete = delete;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew( boolean aNew ) {
        isNew = aNew;
    }

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
        return "[name:" + name + ",id:" + roleId + ",deleted:" + isDelete + ",new:" + isNew + "]";
    }
}
