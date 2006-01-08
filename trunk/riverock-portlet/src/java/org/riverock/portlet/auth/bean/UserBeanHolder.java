package org.riverock.portlet.auth.bean;

/**
 * @author SergeMaslyukov
 *         Date: 06.01.2006
 *         Time: 11:26:35
 *         $Id$
 */
public class UserBeanHolder {

	private UserBean userBean = null;

	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}

	public UserBean getUserBean() {
		return userBean;
	}
}
