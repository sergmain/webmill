package org.riverock.portlet.auth.user.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import org.riverock.portlet.auth.user.bean.RoleBean;

/**
 * @author SMaslyukov
 *         Date: 22.05.2005
 *         Time: 16:35:58
 *         $Id$
 */
public class UserBean implements Serializable {
    private static final long serialVersionUID = 2043005502L;

    private Long authUserId = null;
    private Long userId = null;

    private String userName = null;

    private boolean isCompany;
    private boolean isGroupCompany;
    private boolean isHolding;
    private boolean isRoot;

    private Long companyId = null;
    private Long groupCompanyId = null;
    private Long holdingId = null;

    private boolean isAdd = false;
    private boolean isEdit = false;
    private boolean isDelete = false;

    private List<RoleBean> roles = new ArrayList<RoleBean>();

    private String companyName = null;
    private String groupCompanyName = null;
    private String holdingName = null;

    private String userLogin = null;
    private String userPassword = null;
    private String userPassword2 = null;

	private Long newRoleId = null;

	public Long getNewRoleId() {
		return newRoleId;
	}

	public void setNewRoleId( Long newRoleId ) {
		this.newRoleId = newRoleId;
	}

    public List<RoleBean> getRoles() {
        List<RoleBean> resultRoles = new ArrayList<RoleBean>();
        Iterator<RoleBean> iterator = roles.iterator();
        while( iterator.hasNext() ) {
            RoleBean roleBean = iterator.next();
            if (!roleBean.isDelete() ) {
                resultRoles.add( roleBean );
            }
        }
        return resultRoles;
    }

    public List<RoleBean> getAllRoles() {
        return roles;
    }

    public void setRoles( List<RoleBean> roles) {
//        this.roles = roles;
    }

    public void setAllRoles( List<RoleBean> roles) {
        this.roles = roles;
    }

    public void addRole( RoleBean role ) {
        this.roles.add(role);
    }

    public void setAdd(boolean isAdd) {
	this.isAdd = isAdd;
    }

    public boolean getAdd() {
	return isAdd;
    }

    public void setEdit(boolean isEdit) {
	this.isEdit = isEdit;
    }

    public boolean getEdit() {
	return isEdit;
    }

    public void setDelete(boolean isDelete) {
	this.isDelete = isDelete;
    }

    public boolean getDelete() {
	return isDelete;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId( Long userId ) {
        this.userId = userId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId( Long companyId ) {
        this.companyId = companyId;
    }

    public Long getGroupCompanyId() {
        return groupCompanyId;
    }

    public void setGroupCompanyId( Long groupCompanyId ) {
        this.groupCompanyId = groupCompanyId;
    }

    public Long getHoldingId() {
        return holdingId;
    }

    public void setHoldingId( Long holdingId ) {
        this.holdingId = holdingId;
    }

    public Long getAuthUserId() {
        return authUserId;
    }

    public void setAuthUserId( Long authUserId ) {
        this.authUserId = authUserId;
    }

    public boolean isRoot() {
        return isRoot;
    }

    public void setRoot( boolean root) {
        isRoot = root;
    }

    public boolean isCompany() {
        return isCompany;
    }

    public void setCompany( boolean company ) {
        isCompany = company;
    }

    public boolean isGroupCompany() {
        return isGroupCompany;
    }

    public void setGroupCompany( boolean groupCompany ) {
        isGroupCompany = groupCompany;
    }

    public boolean isHolding() {
        return isHolding;
    }

    public void setHolding( boolean holding ) {
        isHolding = holding;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName( String userName ) {
        this.userName = userName;
    }

    public void setUserLogin( String userLogin ) {
        this.userLogin = userLogin;
    }

    public String getUserLogin() {
        return userLogin;
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

    public String getGroupCompanyName() {
        return groupCompanyName;
    }

    public void setGroupCompanyName( String groupCompanyName ) {
        this.groupCompanyName = groupCompanyName;
    }

    public String getHoldingName() {
        return holdingName;
    }

    public void setHoldingName( String holdingName ) {
        this.holdingName = holdingName;
    }
}