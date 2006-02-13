package org.riverock.portlet.auth.user.bean;

import java.io.Serializable;

import org.riverock.interfaces.sso.a3.bean.RoleBean;

/**
 * @author SergeMaslyukov
 *         Date: 06.01.2006
 *         Time: 11:26:35
 *         $Id$
 */
public class RoleBeanImpl implements RoleBean, Serializable {
    private static final long serialVersionUID = 2043005507L;

    private RoleBean roleBean = null;
    private boolean isDelete = false;
    private boolean isNew = false;

    public RoleBeanImpl( RoleBean roleBean ) {
        this.roleBean = roleBean;
    }

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
        return roleBean.getName();
    }

//    public void setName( String name ) {
//        this.name = name;
//    }

    public Long getRoleId() {
        return roleBean.getRoleId();
    }

//    public void setRoleId( Long roleId ) {
//        this.roleId = roleId;
//    }

    public boolean equals( RoleBeanImpl roleBeanImpl ) {
        if( roleBeanImpl == null || roleBeanImpl.getRoleId()==null || roleBean.getRoleId()==null ) {
            return false;
        }
        return roleBeanImpl.getRoleId().equals( roleBean.getRoleId() );
    }

    public String toString() {
        return "[name:" + roleBean.getName() + ",id:" + roleBean.getRoleId() + ",deleted:" + isDelete + ",new:" + isNew + "]";
    }
}
