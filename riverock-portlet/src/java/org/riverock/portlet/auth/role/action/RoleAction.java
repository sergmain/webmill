package org.riverock.portlet.auth.role.action;

import java.io.Serializable;
import java.util.Iterator;

import javax.faces.event.ActionEvent;

import org.apache.log4j.Logger;

import org.riverock.portlet.auth.role.bean.RoleHolderBean;
import org.riverock.portlet.auth.user.bean.RoleBean;
import org.riverock.portlet.auth.user.logic.AuthManager;
import org.riverock.portlet.tools.FacesTools;

/**
 * @author SergeMaslyukov
 *         Date: 13.01.2006
 *         Time: 9:05:35
 *         $Id$
 */
public class RoleAction implements Serializable {
    private final static Logger log = Logger.getLogger( RoleAction.class );
    private static final long serialVersionUID = 2043007701L;

    private RoleHolderBean roleHolderBean = null;

    public void deleteRoleActionListener( ActionEvent event ) {
        log.info( "Delete role action." );

        Long roleId = FacesTools.getLong( event.getComponent(), "roleId" );
        log.info( "delete role with id: " + roleId );

/*
        Iterator<RoleBean> iterator = roleHolderBean.getRoles().iterator();
        while( iterator.hasNext() ) {
            RoleBean roleBean = iterator.next();

            if( roleBean.getRoleId().equals( roleId ) ) {
                log.info( "Role is found. set isDelete to true" );
                roleBean.setDelete( true );
                break;
            }
        }
*/
    }

    public void addRoleAction() {
        log.info( "Add role action." );
/*
        Long roleId = roleHolderBean.getUserBean().getNewRoleId();

        log.info( "New id of role: " + roleId );
        if( roleId == null ) {
            return;
        }

        roleHolderBean.getAllRoles().add(  );
*/
    }

}
