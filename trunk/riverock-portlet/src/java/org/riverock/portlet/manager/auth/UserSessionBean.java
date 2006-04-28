package org.riverock.portlet.manager.auth;

/**
 * @author SergeMaslyukov
 *         Date: 06.01.2006
 *         Time: 11:26:35
 *         $Id$
 */
public class UserSessionBean {

	private AuthUserExtendedInfoImpl userBean = null;
	private Long currentAuthUserId = null;
	private Long currentRoleId = null;

    private boolean isAdd = false;
    private boolean isEdit = false;
    private boolean isDelete = false;

    public void resetStatus() {
	isAdd = false;
	isEdit = false;
	isDelete = false;
    }

    public void setAdd(boolean isAdd) {
        this.isAdd = isAdd;
	if (isAdd) {
		isEdit = false;
		isDelete = false;
	}
    }

    public boolean getAdd() {
        return isAdd;
    }

    public void setEdit(boolean isEdit) {
        this.isEdit = isEdit;
        if (isEdit) {
            isAdd = false;
            isDelete = false;
        }
    }

    public boolean getEdit() {
        return isEdit;
    }

    public void setDelete(boolean isDelete) {
        this.isDelete = isDelete;
        if (isDelete) {
            isEdit = false;
            isAdd = false;
        }
    }

    public boolean getDelete() {
        return isDelete;
    }

	public void setUserBean(AuthUserExtendedInfoImpl userBean) {
		this.userBean = userBean;
	}

	public AuthUserExtendedInfoImpl getUserBean() {
		return userBean;
	}

	public Long getCurrentAuthUserId() {
		return currentAuthUserId;
	}

	public void setCurrentAuthUserId( Long id ) {
		this.currentAuthUserId = id;
	}

	public Long getCurrentRoleId() {
		return currentRoleId;
	}

	public void setCurrentRoleId( Long id ) {
		this.currentRoleId = id;
	}
}
