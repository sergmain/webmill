package org.riverock.portlet.manager.auth;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import org.riverock.common.tools.StringTools;
import org.riverock.interfaces.sso.a3.AuthUserExtendedInfo;
import org.riverock.interfaces.sso.a3.UserInfo;
import org.riverock.interfaces.sso.a3.bean.RoleEditableBean;

/**
 * @author SMaslyukov
 *         Date: 22.05.2005
 *         Time: 16:35:58
 *         $Id$
 */
public class AuthUserExtendedInfoImpl implements AuthUserExtendedInfo, Serializable {
    private static final long serialVersionUID = 2043005502L;

    private AuthInfoImpl authInfo = null;
    private UserInfo userInfo = null;

    private String userName = null;
    private String companyName = null;
    private String holdingName = null;

    private String userPassword2 = null;
    private String userPassword = null;


    private List<RoleEditableBeanImpl> roles = new ArrayList<RoleEditableBeanImpl>();

	private Long newRoleId = null;

	public Long getNewRoleId() {
		return newRoleId;
	}

	public void setNewRoleId( Long newRoleId ) {
		this.newRoleId = newRoleId;
	}

    public AuthInfoImpl getAuthInfo() {
        return authInfo;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setAuthInfo( AuthInfoImpl authInfo ) {
        this.authInfo = authInfo;
    }

    public void setUserInfo( UserInfo userInfo ) {
        this.userInfo = userInfo;
	if (userInfo!=null) {
            userName = StringTools.getUserName(
                    userInfo.getFirstName(), userInfo.getMiddleName(), userInfo.getLastName()
            );
	}
    }

    public List<RoleEditableBean> getCurrentRoles() {
        List<RoleEditableBean> resultRoles = new ArrayList<RoleEditableBean>();
        Iterator<RoleEditableBeanImpl> iterator = roles.iterator();
        while( iterator.hasNext() ) {
            RoleEditableBeanImpl role = iterator.next();
            if (!role.isDelete() ) {
                resultRoles.add( role );
            }
        }
        return resultRoles;
    }

    public List<RoleEditableBean> getRoles() {
        return (List)roles;
    }

    public void setCurrentRoles( List<RoleEditableBean> roles) {
//        this.roles = roles;
    }

    public void setRoles( List<RoleEditableBeanImpl> roles) {
        this.roles = roles;
    }

    public void addRole( RoleEditableBeanImpl roleImpl ) {
        this.roles.add(roleImpl);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName( String userName ) {
        this.userName = userName;
    }

    public void setUserPassword( String userPassword ) {
        this.userPassword = userPassword;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword2( String userPassword2 ) {
        this.userPassword2 = userPassword2;
    }

    public String getUserPassword2() {
        return userPassword2;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName( String companyName ) {
        this.companyName = companyName;
    }

    public String getHoldingName() {
        return holdingName;
    }

    public void setHoldingName( String holdingName ) {
        this.holdingName = holdingName;
    }

}
