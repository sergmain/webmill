/*
 * org.riverock.portlet -- Portlet Library
 *
 * Copyright (C) 2004, Riverock Software, All Rights Reserved.
 *
 * Riverock -- The Open-source Java Development Community
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
 *
 */
package org.riverock.portlet.register.action;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import org.riverock.interfaces.portal.bean.UserOperationStatus;
import org.riverock.interfaces.portal.user.PortalUserManager;
import org.riverock.module.action.Action;
import org.riverock.module.action.ModuleActionRequest;
import org.riverock.module.exception.ActionException;
import org.riverock.portlet.register.RegisterConstants;
import org.riverock.portlet.register.RegisterError;
import org.riverock.portlet.register.bean.CreateAccountBean;
import org.riverock.portlet.register.bean.UserRegistrationBean;
import org.riverock.webmill.container.ContainerConstants;
import org.riverock.webmill.container.tools.PortletService;

/**
 * @author SergeMaslyukov
 *         Date: 30.11.2005
 *         Time: 22:58:07
 *         $Id$
 */
public class CreateAccountAction implements Action {
    private final static Logger log = Logger.getLogger( CreateAccountAction.class );

    public String execute( ModuleActionRequest moduleActionRequest ) throws ActionException {

        log.debug("Start createAccountAction");

        String password1 = moduleActionRequest.getRequest().getString(RegisterConstants.PASSWORD1_PARAM);
        String password2 = moduleActionRequest.getRequest().getString(RegisterConstants.PASSWORD2_PARAM);

        UserRegistrationBean bean = new UserRegistrationBean();
        bean.setUserLogin( moduleActionRequest.getRequest().getString( RegisterConstants.USERNAME_PARAM ) );

        bean.setUserPassword( password1 );
        bean.setFirstName( moduleActionRequest.getRequest().getString( RegisterConstants.FIRST_NAME_PARAM ) );
        bean.setLastName( moduleActionRequest.getRequest().getString( RegisterConstants.LAST_NAME_PARAM ) );
        bean.setMiddleName( moduleActionRequest.getRequest().getString( RegisterConstants.MIDDLE_NAME_PARAM ) );
        bean.setEmail( moduleActionRequest.getRequest().getString( RegisterConstants.EMAIL_PARAM ) );

        if( StringUtils.isEmpty( bean.getEmail() ) ) {
            log.warn( "email is empty" );
            return RegisterError.emailIsEmpty( moduleActionRequest );
        }

        PortalUserManager portalUserManager = (PortalUserManager)
            moduleActionRequest.getRequest().getAttribute(ContainerConstants.PORTAL_PORTAL_USER_MANAGER);

        Map<String, String> messages = new HashMap<String, String>();
        String remodeAddr = (String)moduleActionRequest.getRequest().getAttribute(ContainerConstants.PORTAL_REMOTE_ADDRESS_ATTRIBUTE);

        String s = moduleActionRequest.getResourceBundle().getString( "reg.mail_body" );
        String mailMessage = MessageFormat.format( s, new Object[]{bean.getUserLogin(), bean.getUserPassword() } );
        messages.put(PortalUserManager.CREATE_ACCOUNT_BODY_MESSAGE, mailMessage + "\n\nProcess of registration was made from IP " + remodeAddr);
        messages.put(PortalUserManager.CREATE_ACCOUNT_SUBJECT_MESSAGE, "Confirm registration");

        UserOperationStatus status = portalUserManager.registerNewUser(bean, messages);

        return sendStatus( bean, moduleActionRequest, status );
    }

    private String sendStatus( UserRegistrationBean bean, ModuleActionRequest moduleActionRequest, UserOperationStatus status ) {

        switch( status.getOperationCode() ) {
            case PortalUserManager.STATUS_ROLE_NOT_DEFINED:
                // "Can not add new user because default role not specified in metadata"
                log.error("default role is null");
                return RegisterError.roleIsNull( moduleActionRequest );

            case PortalUserManager.STATUS_LOGIN_ALREADY_REGISTERED:
                String args2[] = {bean.getUserLogin()};
                String aaa = PortletService.getString( moduleActionRequest.getResourceBundle(), "reg.login_exists", args2 );
                args2 = null;

                return RegisterError.roleIsNull( moduleActionRequest );

            case PortalUserManager.STATUS_OK_OPERATION:
                return RegisterConstants.OK_EXECUTE_STATUS;

            default:
                throw new IllegalStateException("unknown action status: " + status);
        }
    }

    private int checkRegisterData( CreateAccountBean bean ) {

        if( bean.getUsername() == null ) {
            return RegisterConstants.USERNAME_IS_NULL_STATUS;
        }

        if( bean.getPassword1() == null ) {
            return RegisterConstants.PASSWORD1_IS_NULL_STATUS;
        }

        if( bean.getPassword2() == null ) {
            return RegisterConstants.PASSWORD2_IS_NULL_STATUS;
        }

        if( bean.getEmail() == null ) {
            return RegisterConstants.EMAIL_IS_NULL_STATUS;
        }

        if( StringUtils.isBlank( bean.getRole() ) ) {
            return RegisterConstants.ROLE_IS_NULL_STATUS;
        }

        if( !bean.getPassword1().equals( bean.getPassword2() ) ) {
            return RegisterConstants.PASSWORD_NOT_EQUALS_STATUS;
        }

        return RegisterConstants.OK_STATUS;
    }

}
