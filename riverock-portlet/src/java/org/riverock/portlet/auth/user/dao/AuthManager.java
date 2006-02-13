package org.riverock.portlet.auth.user.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.riverock.common.tools.StringTools;
import org.riverock.interfaces.portal.bean.Company;
import org.riverock.interfaces.sso.a3.AuthInfo;
import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.interfaces.sso.a3.UserInfo;
import org.riverock.interfaces.sso.a3.bean.RoleBean;
import org.riverock.portlet.auth.user.bean.AuthInfoImpl;
import org.riverock.portlet.auth.user.bean.AuthUserExtendedInfoImpl;
import org.riverock.portlet.auth.user.bean.CompanyBean;
import org.riverock.portlet.auth.user.bean.ModuleUserBean;
import org.riverock.portlet.auth.user.bean.RoleBeanImpl;
import org.riverock.portlet.auth.user.bean.RoleEditableBeanImpl;
import org.riverock.portlet.auth.user.bean.UserItemBean;

/**
 * @author SergeMaslyukov
 *         Date: 02.01.2006
 *         Time: 9:42:36
 *         $Id$
 */
public class AuthManager implements Serializable {
    private static final long serialVersionUID = 2043005501L;

    private ModuleUserBean moduleUserBean = null;
    private List<CompanyBean> companyBeans = null;

    public ModuleUserBean getModuleUserBean() {
        return moduleUserBean;
    }

    public void setModuleUserBean( ModuleUserBean moduleUserBean ) {
        this.moduleUserBean = moduleUserBean;
    }

    public List<CompanyBean> getCompanyBeans() {
        if( companyBeans == null ) {
            companyBeans = initCompanyBeans( moduleUserBean.getAuthSession() );
        }
        return companyBeans;
    }

    public void reinitCompanyBeans() {
        companyBeans = initCompanyBeans( moduleUserBean.getAuthSession() );
    }

    private List<CompanyBean> initCompanyBeans( AuthSession authSession ) {
        List<CompanyBean> list = new ArrayList<CompanyBean>();

        List<Company> companies = authSession.getCompanyList();
        List<UserInfo> userList = authSession.getUserList();
        List<AuthInfo> authList = authSession.getAuthInfoList();

        Iterator<Company> iterator = companies.iterator();
        while( iterator.hasNext() ) {
            Company company = iterator.next();

            CompanyBean companyBean = new CompanyBean();
            companyBean.setCompanyId( company.getId() );
            companyBean.setCompanyName( company.getName() );

            Iterator<UserInfo> userIterator = userList.iterator();
            while( userIterator.hasNext() ) {
                UserInfo userInfo = userIterator.next();

                if (userInfo.getCompanyId().equals( company.getId() )) {
                    Iterator<AuthInfo> authIterator = authList.iterator();
                    while( authIterator.hasNext() ) {
                        AuthInfo authInfo = authIterator.next();

                        if (userInfo.getUserId().equals( authInfo.getUserId() )) {
                            AuthUserExtendedInfoImpl bean = new AuthUserExtendedInfoImpl();
                            bean.setAuthInfo( new AuthInfoImpl(authInfo) );
                            bean.setUserInfo( userInfo );
                            List<RoleBean> roles = authSession.getRoleList( authInfo.getAuthUserId() );
                            Iterator<RoleBean> roleIterator = roles.iterator();
                            while( roleIterator.hasNext() ) {
                                RoleBean roleBean = roleIterator.next();
                                bean.addRole( new RoleEditableBeanImpl(roleBean) );
                            }
                            companyBean.addUserBeans( bean );
                        }
                    }
                }
            }
            list.add( companyBean );
        }
        return list;
    }

    public void deleteUser( AuthUserExtendedInfoImpl authUserExtendedInfoImpl ) {
        if( authUserExtendedInfoImpl == null ) {
            throw new IllegalStateException( "auth user info bean is null" );
        }

        moduleUserBean.getAuthSession().deleteUser( authUserExtendedInfoImpl );
    }

    public void processSaveUser( AuthUserExtendedInfoImpl authUserExtendedInfoImpl ) {
        if( authUserExtendedInfoImpl == null ) {
            throw new IllegalStateException( "auth user info bean is null" );
        }

        moduleUserBean.getAuthSession().updateUser( authUserExtendedInfoImpl );
    }

    public void processAddUser( AuthUserExtendedInfoImpl authUserExtendedInfoImpl ) {
        if( authUserExtendedInfoImpl == null ) {
            throw new IllegalStateException( "auth user info bean is null" );
        }

        moduleUserBean.getAuthSession().addUser( authUserExtendedInfoImpl );
    }


    public List<UserItemBean> getUserList() {

        List<UserInfo> list = moduleUserBean.getAuthSession().getUserList();
        Iterator<UserInfo> iterator = list.iterator();
        List<UserItemBean> beans = new ArrayList<UserItemBean>();
        while( iterator.hasNext() ) {
            UserInfo userInfo =  iterator.next();
            UserItemBean bean = new UserItemBean();
            bean.setUserId( userInfo.getUserId() );
            bean.setUserName(
                StringTools.getUserName(
                    userInfo.getFirstName(),userInfo.getMiddleName(), userInfo.getLastName()
                )
            );
            beans.add( bean );
        }
        return beans;
    }

    public RoleEditableBeanImpl getRole( Long roleId ) {
        RoleBeanImpl role = new RoleBeanImpl( moduleUserBean.getAuthSession().getRole( roleId ) );
        role.setNew( true );
        return new RoleEditableBeanImpl( role );
    }
}
