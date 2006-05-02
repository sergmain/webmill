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

import javax.mail.MessagingException;
import javax.portlet.PortletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import org.riverock.common.mail.MailMessage;
import org.riverock.generic.config.GenericConfig;
import org.riverock.module.action.Action;
import org.riverock.module.action.ModuleActionRequest;
import org.riverock.module.exception.ActionException;
import org.riverock.portlet.register.RegisterConstants;
import org.riverock.portlet.register.RegisterError;
import org.riverock.portlet.register.bean.CreateAccountBean;
import org.riverock.portlet.register.dao.CreateAccountDAO;
import org.riverock.portlet.register.dao.RegisterDAOFactory;
import org.riverock.webmill.container.ContainerConstants;
import org.riverock.webmill.container.tools.PortletMetadataService;
import org.riverock.webmill.container.tools.PortletService;

/**
 * @author SergeMaslyukov
 *         Date: 30.11.2005
 *         Time: 22:58:07
 *         $Id$
 */
public class CreateAccountAction implements Action {
    private final static Logger log = Logger.getLogger( SendPasswordAction.class );

    public String execute( ModuleActionRequest moduleActionRequest ) throws ActionException {

        log.debug("Start createAccountAction");

        PortletRequest portletRequet = ( PortletRequest ) moduleActionRequest.getRequest().getOriginRequest();

        CreateAccountBean bean = new CreateAccountBean();
        bean.setUsername( moduleActionRequest.getRequest().getString( RegisterConstants.USERNAME_PARAM ) );
        bean.setPassword1( moduleActionRequest.getRequest().getString( RegisterConstants.PASSWORD1_PARAM ) );
        bean.setPassword2( moduleActionRequest.getRequest().getString( RegisterConstants.PASSWORD2_PARAM ) );
        bean.setFirstName( moduleActionRequest.getRequest().getString( RegisterConstants.FIRST_NAME_PARAM ) );
        bean.setLastName( moduleActionRequest.getRequest().getString( RegisterConstants.LAST_NAME_PARAM ) );
        bean.setMiddleName( moduleActionRequest.getRequest().getString( RegisterConstants.MIDDLE_NAME_PARAM ) );
        bean.setEmail( moduleActionRequest.getRequest().getString( RegisterConstants.EMAIL_PARAM ) );

        if( StringUtils.isEmpty( bean.getEmail() ) ) {
            log.warn( "email is empty" ); 
            return RegisterError.emailIsEmpty( moduleActionRequest );
        }

        // register-default-role
        String role = PortletMetadataService.getMetadata( portletRequet, RegisterConstants.DEFAULT_ROLE_METADATA );
        bean.setRole( role );
        bean.setAdminEmail( portletRequet.getPortalContext().getProperty( ContainerConstants.PORTAL_PROP_ADMIN_EMAIL ) );
        bean.setCompanyId( new Long( portletRequet.getPortalContext().getProperty( ContainerConstants.PORTAL_PROP_COMPANY_ID ) ) );

        try {
            int status = checkRegisterData( bean );
            if( status != RegisterConstants.OK_STATUS ) {
                log.warn( "checking status is not OK, value: " + status ); 
                return sendStatus( bean, moduleActionRequest, status );
            }

            RegisterDAOFactory daof = RegisterDAOFactory.getDAOFactory();
            CreateAccountDAO createAccountDAO = daof.getCreateAccountDAO();
            status = createAccountDAO.execute( moduleActionRequest, bean );

            return sendStatus( bean, moduleActionRequest, status );

        }
        catch( Exception e ) {
            log.error( "System error create account", e );
            return RegisterError.systemError( moduleActionRequest );
        }

    }

    private String sendStatus( CreateAccountBean bean, ModuleActionRequest moduleActionRequest, int status ) throws MessagingException {

        switch( status ) {
            case RegisterConstants.ROLE_IS_NULL_STATUS:
                // "Can not add new user because default role not specified in metadata"
                log.error("default role is null");
                return RegisterError.roleIsNull( moduleActionRequest );

            case RegisterConstants.USERNAME_ALREADY_EXISTS_STATUS:
                String args2[] = {bean.getUsername()};
                String aaa = PortletService.getString( moduleActionRequest.getResourceBundle(), "reg.login_exists", args2 );
                args2 = null;

                return RegisterError.roleIsNull( moduleActionRequest );

            case RegisterConstants.OK_STATUS:
                if( log.isDebugEnabled() ) {
                    log.debug( "Admin mail: " + bean.getAdminEmail() );
                }

                String s = moduleActionRequest.getResourceBundle().getString( "reg.mail_body" );
                String mailMessage =
                    MessageFormat.format( s, new Object[]{bean.getUsername(), bean.getPassword1() } );

                MailMessage.sendMessage( mailMessage + "\n\nProcess of registration was made from IP ",
                    bean.getEmail(),
                    bean.getAdminEmail(),
                    "Confirm registration",
                    GenericConfig.getMailSMTPHost() );

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

        if( StringUtils.isEmpty( bean.getRole() ) ) {
            return RegisterConstants.ROLE_IS_NULL_STATUS;
        }

        if( !bean.getPassword1().equals( bean.getPassword2() ) ) {
            return RegisterConstants.PASSWORD_NOT_EQUALS_STATUS;
        }

        return RegisterConstants.OK_STATUS;
    }

}
