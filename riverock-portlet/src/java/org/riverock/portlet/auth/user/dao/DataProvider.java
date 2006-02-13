package org.riverock.portlet.auth.user.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.riverock.portlet.auth.user.bean.AuthUserExtendedInfoImpl;
import org.riverock.portlet.auth.user.bean.UserHolderBean;
import org.riverock.portlet.auth.user.bean.UserItemBean;
import org.riverock.interfaces.sso.a3.bean.RoleBean;
import org.riverock.interfaces.sso.a3.bean.RoleEditableBean;
import org.riverock.interfaces.portal.bean.Company;
import org.riverock.interfaces.portal.bean.GroupCompany;
import org.riverock.interfaces.portal.bean.Holding;

/**
 * @author SergeMaslyukov
 *         Date: 02.01.2006
 *         Time: 9:26:35
 *         $Id$
 */
public class DataProvider implements Serializable {
    private static final long serialVersionUID = 2043005511L;

    private UserHolderBean userHolderBean = null;
    private AuthManager authManager = null;

    public DataProvider() {
    }

    // getter/setter methods
    public void setUserHolderBean( UserHolderBean userHolderBean ) {
        this.userHolderBean = userHolderBean;
    }

    public AuthUserExtendedInfoImpl getCurrentUser() {
        return userHolderBean.getUserBean();
    }

    public AuthManager getAuthManager() {
        return authManager;
    }

    public void setAuthManager( AuthManager authManager ) {
        this.authManager = authManager;
    }

    public List<SelectItem> getUserList() {
        List<SelectItem> list = new ArrayList<SelectItem>();
        List<UserItemBean> companies = authManager.getUserList();

        Iterator<UserItemBean> iterator = companies.iterator();
        while( iterator.hasNext() ) {
            UserItemBean user = iterator.next();

            list.add( new SelectItem( user.getUserId(), user.getUserName() ) );
        }
        return list;
    }

    private boolean isAlreadyBinded( RoleBean roleBean ) {
        Iterator<RoleEditableBean> iterator = userHolderBean.getUserBean().getRoles().iterator();
        while( iterator.hasNext() ) {
            RoleEditableBean roleImpl = iterator.next();
            if( roleImpl.getRoleId().equals( roleBean.getRoleId() ) ) {
                return true;
            }
        }
        return false;
    }

    public List<SelectItem> getRoleList() {
        List<SelectItem> list = new ArrayList<SelectItem>();
        List<RoleBean> roles = authManager.getModuleUserBean().getAuthSession().getRoleList();

        Iterator<RoleBean> iterator = roles.iterator();
        while( iterator.hasNext() ) {
            RoleBean roleBean = iterator.next();

            if( !isAlreadyBinded( roleBean ) ) {
                list.add( new SelectItem( roleBean.getRoleId(), roleBean.getName() ) );
            }
        }
        return list;
    }

    public List<SelectItem> getCompanyList() {
        List<SelectItem> list = new ArrayList<SelectItem>();
        List<Company> companies = authManager.getModuleUserBean().getAuthSession().getCompanyList();

        Iterator<Company> iterator = companies.iterator();
        while( iterator.hasNext() ) {
            Company company = iterator.next();

            list.add( new SelectItem( company.getId(), company.getName() ) );
        }
        return list;
    }

    public List<SelectItem> getGroupCompanyList() {
        List<SelectItem> list = new ArrayList<SelectItem>();
        List<GroupCompany> groupCompanies = authManager.getModuleUserBean().getAuthSession().getGroupCompanyList();

        Iterator<GroupCompany> iterator = groupCompanies.iterator();
        while( iterator.hasNext() ) {
            GroupCompany groupCompany = iterator.next();

            list.add( new SelectItem( groupCompany.getId(), groupCompany.getName() ) );
        }
        return list;
    }

    public List<SelectItem> getHoldingList() {
        List<SelectItem> list = new ArrayList<SelectItem>();
        List<Holding> holdings = authManager.getModuleUserBean().getAuthSession().getHoldingList();

        Iterator<Holding> iterator = holdings.iterator();
        while( iterator.hasNext() ) {
            Holding holding = iterator.next();

            list.add( new SelectItem( holding.getId(), holding.getName() ) );
        }
        return list;
    }
}