package org.riverock.webmill.portal.bean;

import java.io.Serializable;

import org.riverock.interfaces.sso.a3.bean.RoleEditableBean;
import org.riverock.interfaces.sso.a3.bean.RoleBean;

/**
 * @author Sergei Maslyukov
 *         Date: 23.06.2006
 *         Time: 19:11:09
 */
public class RoleEditableBeanImpl implements RoleEditableBean, Serializable {
    private static final long serialVersionUID = 20436705512L;

    private boolean isNew = false;
    private boolean isDelete = false;
    private String name = null;
    private Long roleId = null;

    public RoleEditableBeanImpl() {
    }

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
