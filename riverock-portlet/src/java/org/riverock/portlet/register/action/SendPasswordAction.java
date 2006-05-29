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

import java.util.ResourceBundle;

import javax.mail.MessagingException;
import javax.portlet.PortletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import org.riverock.common.mail.MailMessage;
import org.riverock.generic.config.GenericConfig;
import org.riverock.module.action.Action;
import org.riverock.module.action.ModuleActionRequest;
import org.riverock.module.exception.ActionException;
import org.riverock.portlet.main.Constants;
import org.riverock.portlet.register.RegisterConstants;
import org.riverock.portlet.register.RegisterError;
import org.riverock.portlet.register.bean.RegisterPasswordInfoBean;
import org.riverock.portlet.register.dao.RegisterDAOFactory;
import org.riverock.portlet.register.dao.RegisterPasswordInfoDAO;
import org.riverock.webmill.container.ContainerConstants;

/**
 * @author SergeMaslyukov
 *         Date: 30.11.2005
 *         Time: 22:10:17
 *         $Id$
 */
public class SendPasswordAction implements Action {
    private final static Logger log = Logger.getLogger( SendPasswordAction.class );

    public String execute( ModuleActionRequest moduleActionRequest ) throws ActionException {

        PortletRequest portletRequet = ( PortletRequest ) moduleActionRequest.getRequest().getOriginRequest();
        String email = moduleActionRequest.getRequest().getString( RegisterConstants.NAME_EMAIL );

        if( StringUtils.isBlank( email ) ) {
            return RegisterError.emailIsEmpty( moduleActionRequest );
        }

        RegisterDAOFactory daof = RegisterDAOFactory.getDAOFactory();
        RegisterPasswordInfoDAO passwordInfoDAO = daof.getSendPasswordDAO();
        try {
            RegisterPasswordInfoBean bean = passwordInfoDAO.execute( moduleActionRequest.getRequest().getSiteId(), email );
            if( bean == null ) {
                return RegisterError.noSuchEmail( moduleActionRequest );
            }

            // Todo rewrite with portal property
            bean.setAdminEmail( portletRequet.getPortalContext().getProperty( ContainerConstants.PORTAL_PROP_ADMIN_EMAIL ) );

            return sendPassword( bean, moduleActionRequest.getResourceBundle() );

        }
        catch( Exception e ) {
            log.error( "System error send password", e);
            return RegisterError.systemError( moduleActionRequest );
        }

    }

    private String sendPassword( RegisterPasswordInfoBean bean, ResourceBundle bundle ) throws MessagingException {

        String message = bundle.getString( "reg.send-password.your-password" );
        String subj = bundle.getString( "reg.send-password.info" );

        MailMessage.sendMessage( message,
            bean.getEmail(),
            bean.getAdminEmail(),
            subj,
            GenericConfig.getMailSMTPHost() );

        return Constants.OK_EXECUTE_STATUS;
    }
}