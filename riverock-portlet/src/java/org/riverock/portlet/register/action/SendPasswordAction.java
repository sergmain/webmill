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

import java.util.Map;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import org.riverock.interfaces.portal.bean.UserOperationStatus;
import org.riverock.interfaces.portal.user.PortalUserManager;
import org.riverock.module.action.Action;
import org.riverock.module.action.ModuleActionRequest;
import org.riverock.module.exception.ActionException;
import org.riverock.portlet.register.Constants;
import org.riverock.portlet.register.PortletErrors;
import org.riverock.portlet.register.RegisterConstants;
import org.riverock.portlet.register.RegisterError;
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

        String email = moduleActionRequest.getRequest().getString( RegisterConstants.NAME_EMAIL );

        if( StringUtils.isBlank( email ) ) {
            return RegisterError.emailIsEmpty( moduleActionRequest );
        }

        PortalUserManager portalUserManager = (PortalUserManager)
            moduleActionRequest.getRequest().getAttribute(ContainerConstants.PORTAL_PORTAL_USER_MANAGER);

        Map<String, String> messages = new HashMap<String, String>();

        UserOperationStatus status = portalUserManager.sendPassword(email, messages);
        if (status.getOperationCode()==PortalUserManager.OK_OPERATION) {
            return Constants.OK_EXECUTE_STATUS;
        }
        else if (status.getOperationCode()==PortalUserManager.NO_SUCH_EMAIL) {
            return RegisterError.noSuchEmail( moduleActionRequest );
        }
        else {
            if (status.getInternalErrorMessage()!=null) {
                return PortletErrors.error(moduleActionRequest, status.getInternalErrorMessage() );
            }
            else {
                return PortletErrors.error(moduleActionRequest, "unknown error" );
            }
        }
    }
}