package org.riverock.portlet.auth.user.action;

import java.io.Serializable;
import java.util.Iterator;

import javax.faces.event.ActionEvent;

import org.apache.log4j.Logger;

import org.riverock.portlet.auth.user.bean.CompanyBean;
import org.riverock.portlet.auth.user.bean.RoleBean;
import org.riverock.portlet.auth.user.bean.RoleBean;
import org.riverock.portlet.auth.user.bean.UserBean;
import org.riverock.portlet.auth.user.bean.UserBean;
import org.riverock.portlet.auth.user.bean.UserHolderBean;
import org.riverock.portlet.auth.user.bean.UserHolderBean;
import org.riverock.portlet.auth.user.logic.AuthManager;
import org.riverock.portlet.tools.FacesTools;

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

    public UserBean getCurrentUser() {
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
        UserBean userBean = lookupUserBean( authUserId );
        userHolderBean.setUserBean( userBean );
        log.info( "Current user bean: " + userBean );

    }

// Role actions
    public void deleteRoleActionListener( ActionEvent event ) {
        log.info( "Delete role action." );

        Long roleId = FacesTools.getLong( event.getComponent(), "roleId" );
        log.info( "delete role with id: " + roleId );


        Iterator<RoleBean> iterator = null;

        iterator = userHolderBean.getUserBean().getAllRoles().iterator();
        while( iterator.hasNext() ) {
            RoleBean roleBean = iterator.next();

            if( roleBean.getRoleId().equals( roleId ) ) {
                log.info( "Role is found. set isDelete to true" );
                roleBean.setDelete( true );
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

        userHolderBean.getUserBean().getAllRoles().add( authManager.getRole( roleId ) );
    }

// Add actions
    public void addUserAction() {
        log.info( "Add user action." );
        userHolderBean.setUserBean( new UserBean() );
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
            authManager.processDeleteUser( userHolderBean.getUserBean() );
            userHolderBean.setUserBean( null );
            authManager.reinitCompanyBeans();
        }
    }


    private UserBean lookupUserBean( Long authUserId ) {
        log.info( "start search user bean for authUserId: " + authUserId );

        UserBean resultUserBean = null;
        Iterator<CompanyBean> iterator = authManager.getCompanyBeans().iterator();
        while( iterator.hasNext() ) {
            CompanyBean companyBean = iterator.next();

            Iterator<UserBean> it = companyBean.getUserBeans().iterator();
            while( it.hasNext() ) {
                UserBean userBean = it.next();

                if( userBean.getAuthUserId().equals( authUserId ) )
                    resultUserBean = userBean;

                userBean.setEdit( false );
            }
        }
        return resultUserBean;
    }
}