package org.riverock.interfaces.sso.a3;

import java.util.List;

import org.riverock.interfaces.sso.a3.bean.RoleEditableBean;

/**
 * @author SergeMaslyukov
 *         Date: 02.02.2006
 *         Time: 15:05:31
 *         $Id$
 */
public interface AuthUserExtendedInfo {
    public AuthInfo getAuthInfo();
    public UserInfo getUserInfo();
    
    public List<RoleEditableBean> getRoles();

    public String getUserName();
    public String getCompanyName();
    public String getHoldingName();

}
