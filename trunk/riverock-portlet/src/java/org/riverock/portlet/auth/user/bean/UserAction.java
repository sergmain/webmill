package org.riverock.portlet.auth.user.bean;

import java.io.Serializable;
import java.util.Iterator;

import javax.faces.event.ActionEvent;

import org.apache.log4j.Logger;

import org.riverock.portlet.tools.FacesTools;
import org.riverock.interfaces.sso.a3.AuthUserExtendedInfo;
import org.riverock.interfaces.sso.a3.bean.RoleEditableBean;
import org.riverock.portlet.main.AuthSessionBean;

/**
 * @author SergeMaslyukov
 *         Date: 02.01.2006
 *         Time: 9:26:35
 *         $Id$
 */
public class UserAction implements Serializable {
    private final static Logger log = Logger.getLogger( UserAction.class );
    private static final long serialVersionUID = 2043005511L;

    private UserSessionBean userSessionBean = null;
    private AuthSessionBean authSessionBean = null;
    private DataProvider dataProvider = null;

    public UserAction() {
    }

    // getter/setter methods
    public void setUserSessionBean( UserSessionBean userSessionBean) {
        this.userSessionBean = userSessionBean;
    }

    public AuthUserExtendedInfo getCurrentUser() {
        return userSessionBean.getUserBean();
    }

    public AuthSessionBean getAuthSessionBean() {
        return authSessionBean;
    }

    public void setAuthSessionBean( AuthSessionBean authSessionBean) {
        this.authSessionBean = authSessionBean;
    }

    public DataProvider getDataProvider() {
        return dataProvider;
    }

    public void setDataProvider( DataProvider dataProvider ) {
        this.dataProvider = dataProvider;
    }

// main tree action
    public String selectUserAction( ActionEvent event ) {
        log.info( "Select auth user action." );
	loadCurrentUser();

	userSessionBean.resetStatus();
	return "auth";
    }


// Role actions
    public void deleteRoleActionListener( ActionEvent event ) {
        log.info( "Delete role action." );

        Long roleId = userSessionBean.getCurrentRoleId();
        log.info( "delete role with id: " + roleId );

        Iterator<RoleEditableBean> iterator = null;
        iterator = userSessionBean.getUserBean().getRoles().iterator();
        while( iterator.hasNext() ) {
            RoleEditableBean role = iterator.next();

            if( role.getRoleId().equals( roleId ) ) {
                log.info( "Role is found. set isDelete to true" );
                role.setDelete( true );
                break;
            }
        }
    }

    public void addRoleAction() {
        log.info( "Add role action." );

        Long roleId = userSessionBean.getUserBean().getNewRoleId();
        
        log.info( "New id of role: " + roleId );
        if( roleId == null ) {
            return;
        }

        RoleEditableBeanImpl role = 
		new RoleEditableBeanImpl( authSessionBean.getAuthSession().getRole( roleId ) );
        role.setNew( true );
	
        userSessionBean.getUserBean().getRoles().add( role );

    }

// Add actions
    public String addUserAction() {
        log.info( "Add user action." );
	AuthUserExtendedInfoImpl bean = new AuthUserExtendedInfoImpl();
	bean.setAuthInfo( new AuthInfoImpl() );
        userSessionBean.setUserBean( bean );

	userSessionBean.setAdd( true );

	return "auth-add";
    }

    public String processAddUserAction() {
        log.info( "Procss add user action." );
        if( userSessionBean.getUserBean() != null ) {
            authSessionBean.getAuthSession().addUser( userSessionBean.getUserBean() );
            userSessionBean.setUserBean( null );

            dataProvider.reinitCompanyBeans();
        }

	userSessionBean.resetStatus();
	return "auth";
    }

    public String cancelAddUserAction() {
        log.info( "Cancel add user action." );
        userSessionBean.setUserBean( null );

	userSessionBean.resetStatus();
	return "auth";
    }

// Edit actions
    public String editUserAction() {
        log.info( "Edit user action." );

	userSessionBean.setEdit( true );
	return "auth-edit";
    }

    public String saveUserAction() {
        log.info( "Save user action." );
        if( userSessionBean.getUserBean() != null ) {
            authSessionBean.getAuthSession().updateUser( userSessionBean.getUserBean() );
            userSessionBean.setUserBean( null );
            dataProvider.reinitCompanyBeans();
        }

	userSessionBean.resetStatus();
	return "auth";
    }

    public String cancelEditUserAction() {
        log.info( "Cancel edit user action." );

	userSessionBean.resetStatus();
	return "auth";
    }

// Delete actions
    public String deleteUserAction() {
        log.info( "delete user action." );

	userSessionBean.setDelete( true );
	return "auth-delete";
    }

    public String cancelDeleteUserAction() {
        log.info( "Cancel delete user action." );

	userSessionBean.resetStatus();
	return "auth";
    }

    public String processDeleteUserAction() {
        log.info( "Process delete user action." );
        if( userSessionBean.getUserBean() != null ) {
            authSessionBean.getAuthSession().deleteUser( userSessionBean.getUserBean() );
            userSessionBean.setUserBean( null );
            dataProvider.reinitCompanyBeans();
        }

	userSessionBean.resetStatus();
	return "auth";
    }

    private void loadCurrentUser() {
        userSessionBean.setUserBean( lookupUserBean( userSessionBean.getCurrentAuthUserId() ) );
    }

    private AuthUserExtendedInfoImpl lookupUserBean( Long authUserId ) {
        log.info( "start search user bean for authUserId: " + authUserId );

        AuthUserExtendedInfoImpl resultAuthUserExtendedInfoImpl = null;
        Iterator<CompanyBean> iterator = dataProvider.getCompanyBeans().iterator();
        while( iterator.hasNext() ) {
            CompanyBean companyBean = iterator.next();

            Iterator<AuthUserExtendedInfoImpl> it = companyBean.getUserBeans().iterator();
            while( it.hasNext() ) {
                AuthUserExtendedInfoImpl userBean = it.next();

                if( userBean.getAuthInfo().getAuthUserId().equals( authUserId ) )
                    resultAuthUserExtendedInfoImpl = userBean;
            }
        }

        log.info( "end search user");
        return resultAuthUserExtendedInfoImpl;
    }
}