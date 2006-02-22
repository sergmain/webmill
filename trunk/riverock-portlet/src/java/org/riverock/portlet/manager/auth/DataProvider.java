package org.riverock.portlet.manager.auth;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.riverock.common.tools.StringTools;
import org.riverock.interfaces.sso.a3.bean.RoleBean;
import org.riverock.interfaces.sso.a3.bean.RoleEditableBean;
import org.riverock.interfaces.sso.a3.AuthInfo;
import org.riverock.interfaces.sso.a3.UserInfo;
import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.interfaces.portal.bean.Company;
import org.riverock.interfaces.portal.bean.Holding;
import org.riverock.portlet.main.AuthSessionBean;

/**
 * @author SergeMaslyukov
 *         Date: 02.01.2006
 *         Time: 9:26:35
 *         $Id$
 */
public class DataProvider implements Serializable {
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

        List<UserInfo> userList = authSessionBean.getAuthSession().getUserList();
        Iterator<UserInfo> iterator = userList.iterator();
        while( iterator.hasNext() ) {
            UserInfo userInfo =  iterator.next();

		String userName = StringTools.getUserName(
                    userInfo.getFirstName(),userInfo.getMiddleName(), userInfo.getLastName()
                );

		if (userName==null) {
			userName = "userName is null";
		}

            list.add( new SelectItem( userInfo.getUserId(), userName ) );
        }
        return list;
    }

    public List<SelectItem> getRoleList() {
        List<SelectItem> list = new ArrayList<SelectItem>();
        List<RoleBean> roles = authSessionBean.getAuthSession().getRoleList();

        Iterator<RoleBean> iterator = roles.iterator();
        while( iterator.hasNext() ) {
            RoleBean roleBean = iterator.next();

            if( !isAlreadyBinded( roleBean ) ) {
                list.add( new SelectItem( roleBean.getRoleId(), roleBean.getName() ) );
            }
        }
        return list;
    }

    private boolean isAlreadyBinded( RoleBean roleBean ) {
        Iterator<RoleEditableBean> iterator = userSessionBean.getUserBean().getRoles().iterator();
        while( iterator.hasNext() ) {
            RoleEditableBean roleImpl = iterator.next();
            if( roleImpl.getRoleId().equals( roleBean.getRoleId() ) ) {
                return true;
            }
        }
        return false;
    }

    public List<SelectItem> getCompanyList() {
        List<SelectItem> list = new ArrayList<SelectItem>();
        List<Company> companies = authSessionBean.getAuthSession().getCompanyList();

        Iterator<Company> iterator = companies.iterator();
        while( iterator.hasNext() ) {
            Company company = iterator.next();

		if (company.getId()==null) {
			throw new IllegalStateException("id is null, name: " + company.getName());
		}
            list.add( new SelectItem( new Long(company.getId()), company.getName() ) );
        }
        return list;
    }

    public List<SelectItem> getHoldingList() {
        List<SelectItem> list = new ArrayList<SelectItem>();
        List<Holding> holdings = authSessionBean.getAuthSession().getHoldingList();

	if (holdings==null)
		return list;

        Iterator<Holding> iterator = holdings.iterator();
        while( iterator.hasNext() ) {
            Holding holding = iterator.next();

            list.add( new SelectItem( holding.getId(), holding.getName() ) );
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

}