package org.riverock.portlet.auth.user.bean;

import org.riverock.portlet.auth.user.bean.UserBean;

/**
 * @author SergeMaslyukov
 *         Date: 06.01.2006
 *         Time: 11:26:35
 *         $Id$
 */
public class UserHolderBean {

	private UserBean userBean = null;

	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}

	public UserBean getUserBean() {
		return userBean;
	}
}
