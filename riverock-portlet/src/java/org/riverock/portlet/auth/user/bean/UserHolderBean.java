package org.riverock.portlet.auth.user.bean;

/**
 * @author SergeMaslyukov
 *         Date: 06.01.2006
 *         Time: 11:26:35
 *         $Id$
 */
public class UserHolderBean {

	private AuthUserExtendedInfoImpl userBean = null;

	public void setUserBean(AuthUserExtendedInfoImpl userBean) {
		this.userBean = userBean;
	}

	public AuthUserExtendedInfoImpl getUserBean() {
		return userBean;
	}
}
