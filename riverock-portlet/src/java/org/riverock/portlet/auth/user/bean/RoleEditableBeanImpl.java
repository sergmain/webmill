package org.riverock.portlet.auth.user.bean;

import org.riverock.interfaces.sso.a3.bean.RoleEditableBean;
import org.riverock.interfaces.sso.a3.bean.RoleBean;

/**
 * @author SergeMaslyukov
 *         Date: 02.02.2006
 *         Time: 17:23:14
 *         $Id$
 */
public class RoleEditableBeanImpl implements RoleEditableBean {
    private boolean isNew = false;
    private boolean isDelete = false;
    private String name = null;
    private Long roleId = null;

    public RoleEditableBeanImpl( RoleBean role ) {
       this.name = role.getName();
       this.roleId = role.getRoleId();
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew( boolean aNew ) {
        isNew = aNew;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete( boolean delete ) {
        isDelete = delete;
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
}
