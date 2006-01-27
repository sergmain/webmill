package org.riverock.portlet.auth.user.logic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.riverock.portlet.auth.user.bean.GroupCompanyItemBean;
import org.riverock.portlet.auth.user.bean.HoldingItemBean;
import org.riverock.portlet.auth.user.bean.RoleBean;
import org.riverock.portlet.auth.user.bean.UserBean;
import org.riverock.portlet.auth.user.bean.*;
import org.riverock.portlet.auth.user.bean.UserHolderBean;
import org.riverock.portlet.auth.user.bean.UserItemBean;
import org.riverock.portlet.auth.user.logic.AuthManager;

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

    public UserBean getCurrentUser() {
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
        Iterator<RoleBean> iterator = userHolderBean.getUserBean().getRoles().iterator();
        while( iterator.hasNext() ) {
            RoleBean role = iterator.next();
            if( role.equals( roleBean ) ) {
                return true;
            }
        }
        return false;
    }

    public List<SelectItem> getRoleList() {
        List<SelectItem> list = new ArrayList<SelectItem>();
        List<RoleBean> companies = authManager.getRoleList();

        Iterator<RoleBean> iterator = companies.iterator();
        while( iterator.hasNext() ) {
            RoleBean role = iterator.next();

            if( !isAlreadyBinded( role ) ) {
                list.add( new SelectItem( role.getRoleId(), role.getName() ) );
            }
        }
        return list;
    }

    public List<SelectItem> getCompanyList() {
        List<SelectItem> list = new ArrayList<SelectItem>();
        List<CompanyItemBean> companies = authManager.getCompanyList();

        Iterator<CompanyItemBean> iterator = companies.iterator();
        while( iterator.hasNext() ) {
            CompanyItemBean company = iterator.next();

            list.add( new SelectItem( company.getId(), company.getName() ) );
        }
        return list;
    }

    public List<SelectItem> getGroupCompanyList() {
        List<SelectItem> list = new ArrayList<SelectItem>();
        List<GroupCompanyItemBean> groupCompanies = authManager.getGroupCompanyList();

        Iterator<GroupCompanyItemBean> iterator = groupCompanies.iterator();
        while( iterator.hasNext() ) {
            GroupCompanyItemBean groupCompany = iterator.next();

            list.add( new SelectItem( groupCompany.getId(), groupCompany.getName() ) );
        }
        return list;
    }

    public List<SelectItem> getHoldingList() {
        List<SelectItem> list = new ArrayList<SelectItem>();
        List<HoldingItemBean> holdings = authManager.getHoldingList();

        Iterator<HoldingItemBean> iterator = holdings.iterator();
        while( iterator.hasNext() ) {
            HoldingItemBean holding = iterator.next();

            list.add( new SelectItem( holding.getId(), holding.getName() ) );
        }
        return list;
    }
}