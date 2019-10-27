/*
 * org.riverock.portlet - Portlet Library
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package org.riverock.portlet.manager.auth;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;
import org.apache.commons.lang.StringUtils;

import org.riverock.common.tools.StringTools;
import org.riverock.interfaces.portal.bean.Company;
import org.riverock.interfaces.portal.bean.Holding;
import org.riverock.interfaces.portal.bean.User;
import org.riverock.interfaces.sso.a3.AuthInfo;
import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.interfaces.sso.a3.bean.RoleBean;
import org.riverock.interfaces.sso.a3.bean.RoleEditableBean;
import org.riverock.portlet.main.AuthSessionBean;

/**
 * @author SergeMaslyukov
 *         Date: 02.01.2006
 *         Time: 9:26:35
 *         $Id: DataProvider.java 1450 2007-10-01 14:50:54Z serg_main $
 */
public class DataProvider implements Serializable {
    private final static Logger log = Logger.getLogger( DataProvider.class );

    private static final long serialVersionUID = 2043005511L;

    private AuthSessionBean authSessionBean = null;
    private UserSessionBean userSessionBean= null;

    public DataProvider() {
    }

    // getter/setter methods
    public void setUserSessionBean( UserSessionBean userSessionBean) {
        this.userSessionBean = userSessionBean;
    }

    public UserSessionBean getUserSessionBean() {
        return userSessionBean;
    }

    public AuthUserExtendedInfoImpl getCurrentUser() {
        return userSessionBean.getUserBean();
    }

    public AuthSessionBean getAuthSessionBean() {
        return authSessionBean;
    }

    public void setAuthSessionBean( AuthSessionBean authSessionBean) {
        this.authSessionBean = authSessionBean;
    }

    public List<SelectItem> getUserList() {
        List<SelectItem> list = new ArrayList<SelectItem>();

        if (log.isDebugEnabled()) {
            log.debug("authSessionBean: " +authSessionBean);
            log.debug("authSessionBean.getAuthSession(: " + authSessionBean.getAuthSession());
        }
        List<User> userList = authSessionBean.getAuthSession().getUserList();
        for (User userInfo : userList) {
            String userName = StringTools.getUserName(
                userInfo.getFirstName(), userInfo.getMiddleName(), userInfo.getLastName()
            );

            if (userName == null) {
                userName = "userName is null";
            }

            list.add(new SelectItem(userInfo.getUserId(), userName));
        }
        return list;
    }

    public List<SelectItem> getRoleList() {
        List<SelectItem> list = new ArrayList<SelectItem>();
        List<RoleBean> roles = authSessionBean.getAuthSession().getRoleList();

        for (RoleBean roleBean : roles) {
            if (!isAlreadyBinded(roleBean)) {
                list.add(new SelectItem(roleBean.getRoleId(), roleBean.getName()));
            }
        }
        return list;
    }

    private boolean isAlreadyBinded( RoleBean roleBean ) {
        for (RoleEditableBean roleImpl : userSessionBean.getUserBean().getRoles()) {
            if (roleImpl.getRoleId().equals(roleBean.getRoleId()) && !roleImpl.isDelete()) {
                return true;
            }
        }
        return false;
    }

    public String getCompanyName(Long companyId) {
        for (SelectItem selectItem : getCompanyList()) {
            if (selectItem.getValue().equals(companyId)) {
                return selectItem.getLabel();
            }
        }
        return null;
    }

    public String getHoldingName(Long holdingId) {
        for (SelectItem selectItem : getHoldingList()) {
            if (selectItem.getValue().equals(holdingId)) {
                return selectItem.getLabel();
            }
        }
        return null;
    }

    public List<SelectItem> getCompanyList() {
        List<SelectItem> list = new ArrayList<SelectItem>();
        List<Company> companies = authSessionBean.getAuthSession().getCompanyList();

        for (Company company : companies) {
            if (company.getId() == null) {
                throw new IllegalStateException("id is null, name: " + company.getName());
            }
            String companyName = company.getName();
            if (StringUtils.isBlank(companyName)) {
                companyName = "<empty company name>";
            }
            list.add(new SelectItem(company.getId(), companyName));
        }
        return list;
    }

    public List<SelectItem> getHoldingList() {
        List<SelectItem> list = new ArrayList<SelectItem>();
        List<Holding> holdings = authSessionBean.getAuthSession().getHoldingList();

        if (holdings==null) {
            return list;
        }

        for (Holding holding : holdings) {
            list.add(new SelectItem(holding.getId(), holding.getName()));
        }
        return list;
    }

    private List<CompanyBean> companyBeans = null;

    public List<CompanyBean> getCompanyBeans() {
        if( companyBeans == null ) {
            companyBeans = initCompanyBeans( authSessionBean.getAuthSession() );
        }
        return companyBeans;
    }

    public void reinitCompanyBeans() {
        companyBeans = initCompanyBeans( authSessionBean.getAuthSession() );
    }

    private List<CompanyBean> initCompanyBeans( AuthSession authSession ) {
        List<CompanyBean> list = new ArrayList<CompanyBean>();
        if (authSession==null) {
            return list;
        }

        List<Company> companies = authSession.getCompanyList();
        List<User> userList = authSession.getUserList();
        List<AuthInfo> authList = authSession.getAuthInfoList();

        for (Company company : companies) {
            CompanyBean companyBean = new CompanyBean();
            companyBean.setCompanyId(company.getId());
            companyBean.setCompanyName(company.getName());

            for (User user : userList) {
                if (user.getCompanyId().equals(company.getId())) {
                    for (AuthInfo authInfo : authList) {
                        if (user.getUserId().equals(authInfo.getUserId())) {
                            AuthUserExtendedInfoImpl bean = new AuthUserExtendedInfoImpl();
                            bean.setAuthInfo(new AuthInfoImpl(authInfo));
                            bean.setUser(user);

                            List<RoleBean> roles = authSession.getRoleList(authInfo.getAuthUserId());
                            for (RoleBean roleBean : roles) {
                                bean.addRole(new RoleEditableBeanImpl(roleBean));
                            }
                            companyBean.addUserBeans(bean);
                        }
                    }
                }
            }
            list.add(companyBean);
        }
        return list;
    }

}