package org.riverock.portlet.auth.user.bean;

import java.io.Serializable;
import java.util.Iterator;

import javax.faces.event.ActionEvent;

import org.apache.log4j.Logger;

import org.riverock.portlet.auth.user.dao.AuthManager;
import org.riverock.portlet.tools.FacesTools;
import org.riverock.interfaces.sso.a3.AuthUserExtendedInfo;
import org.riverock.interfaces.sso.a3.bean.RoleEditableBean;

/**
 * @author SergeMaslyukov
 *         Date: 02.01.2006
 *         Time: 9:26:35
 *         $Id$
 */
public class UserAction implements Serializable {
    private final static Logger log = Logger.getLogger( UserAction.class );
    private static final long serialVersionUID = 2043005511L;

    private UserHolderBean userHolderBean = null;
    private AuthManager authManager = null;

    public UserAction() {
    }

    // getter/setter methods
    public void setUserHolderBean( UserHolderBean userHolderBean ) {
        this.userHolderBean = userHolderBean;
    }

    public AuthUserExtendedInfo getCurrentUser() {
        return userHolderBean.getUserBean();
    }

    public AuthManager getAuthManager() {
        return authManager;
    }

    public void setAuthManager( AuthManager authManager ) {
        this.authManager = authManager;
    }

// main tree action
    public void selectUserAction( ActionEvent event ) {
        log.info( "Select user action." );

        Long authUserId = FacesTools.getLong( event.getComponent(), "authUserId" );
        AuthUserExtendedInfoImpl authUserExtendedInfoImpl = lookupUserBean( authUserId );
        userHolderBean.setUserBean( authUserExtendedInfoImpl );
        log.info( "Current user bean: " + authUserExtendedInfoImpl );

    }

// Role actions
    public void deleteRoleActionListener( ActionEvent event ) {
        log.info( "Delete role action." );

        Long roleId = FacesTools.getLong( event.getComponent(), "roleId" );
        log.info( "delete role with id: " + roleId );


        Iterator<RoleEditableBean> iterator = null;

        iterator = userHolderBean.getUserBean().getRoles().iterator();
        while( iterator.hasNext() ) {
            RoleEditableBean roleBeanImpl = iterator.next();

            if( roleBeanImpl.getRoleId().equals( roleId ) ) {
                log.info( "Role is found. set isDelete to true" );
                roleBeanImpl.setDelete( true );
                break;
            }
        }
    }

    public void addRoleAction() {
        log.info( "Add role action." );

        Long roleId = userHolderBean.getUserBean().getNewRoleId();
        
        log.info( "New id of role: " + roleId );
        if( roleId == null ) {
            return;
        }

        userHolderBean.getUserBean().getRoles().add( authManager.getRole( roleId ) );
    }

// Add actions
    public void addUserAction() {
        log.info( "Add user action." );
        userHolderBean.setUserBean( new AuthUserExtendedInfoImpl() );
        if( userHolderBean.getUserBean() != null ) {
            userHolderBean.getUserBean().setAdd( true );
            userHolderBean.getUserBean().setEdit( false );
            userHolderBean.getUserBean().setDelete( false );
        }

    }

    public void processAddUserAction() {
        log.info( "Procss add user action." );
        if( userHolderBean.getUserBean() != null ) {
            authManager.processAddUser( userHolderBean.getUserBean() );
            userHolderBean.setUserBean( null );
            authManager.reinitCompanyBeans();
        }
    }

    public void cancelAddUserAction() {
        log.info( "Cancel add action." );
        userHolderBean.setUserBean( null );
    }

// Edit actions
    public void editUserAction() {
        log.info( "Edit user action." );
        if( userHolderBean.getUserBean() != null ) {
            userHolderBean.getUserBean().setAdd( false );
            userHolderBean.getUserBean().setEdit( true );
            userHolderBean.getUserBean().setDelete( false );
        }

    }

    public void saveUserAction() {
        log.info( "Save user action." );
        if( userHolderBean.getUserBean() != null ) {
            userHolderBean.getUserBean().setAdd( false );
            userHolderBean.getUserBean().setEdit( false );
            userHolderBean.getUserBean().setDelete( false );

            authManager.processSaveUser( userHolderBean.getUserBean() );
            userHolderBean.setUserBean( null );
            authManager.reinitCompanyBeans();
        }
    }

    public void cancelEditUserAction() {
        log.info( "Cancel edit action." );
        if( userHolderBean.getUserBean() != null ) {
            userHolderBean.getUserBean().setAdd( false );
            userHolderBean.getUserBean().setEdit( false );
            userHolderBean.getUserBean().setDelete( false );
            userHolderBean.setUserBean( null );
            authManager.reinitCompanyBeans();
        }
    }

// Delete actions
    public void deleteUserAction() {
        log.info( "Edit user action." );
        if( userHolderBean.getUserBean() != null ) {
            userHolderBean.getUserBean().setEdit( false );
            userHolderBean.getUserBean().setDelete( true );
        }

    }

    public void cancelDeleteUserAction() {
        log.info( "Cancel delete user action." );
        if( userHolderBean.getUserBean() != null ) {
            userHolderBean.getUserBean().setEdit( false );
            userHolderBean.getUserBean().setDelete( false );
        }
    }

    public void processDeleteUserAction() {
        log.info( "Process delete user action." );
        if( userHolderBean.getUserBean() != null ) {
            authManager.deleteUser( userHolderBean.getUserBean() );
            userHolderBean.setUserBean( null );
            authManager.reinitCompanyBeans();
        }
    }


    private AuthUserExtendedInfoImpl lookupUserBean( Long authUserId ) {
        log.info( "start search user bean for authUserId: " + authUserId );

        AuthUserExtendedInfoImpl resultAuthUserExtendedInfoImpl = null;
        Iterator<CompanyBean> iterator = authManager.getCompanyBeans().iterator();
        while( iterator.hasNext() ) {
            CompanyBean companyBean = iterator.next();

            Iterator<AuthUserExtendedInfoImpl> it = companyBean.getUserBeans().iterator();
            while( it.hasNext() ) {
                AuthUserExtendedInfoImpl userBean = it.next();

                if( userBean.getAuthInfo().getAuthUserId().equals( authUserId ) )
                    resultAuthUserExtendedInfoImpl = userBean;

                userBean.setEdit( false );
            }
        }
        return resultAuthUserExtendedInfoImpl;
    }
}