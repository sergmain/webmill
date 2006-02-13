package org.riverock.interfaces.sso.a3.bean;

/**
 * @author SergeMaslyukov
 *         Date: 02.02.2006
 *         Time: 15:24:03
 *         $Id$
 */
public interface RoleEditableBean extends RoleBean {
    public boolean isDelete();
    public void setDelete( boolean isDelete );
    public boolean isNew();
    public void setNew( boolean isNew );
}
