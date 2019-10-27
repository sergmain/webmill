/*
 * org.riverock.portlet - Portlet Library
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
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
 */
package org.riverock.portlet.register.action;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import org.riverock.interfaces.portal.bean.UserOperationStatus;
import org.riverock.interfaces.portal.user.PortalUserManager;
import org.riverock.module.action.ModuleActionRequest;
import org.riverock.module.action.ActionInstance;
import org.riverock.module.exception.ActionException;
import org.riverock.portlet.register.Constants;
import org.riverock.portlet.register.PortletErrors;
import org.riverock.portlet.register.RegisterConstants;
import org.riverock.portlet.register.RegisterError;
import org.riverock.portlet.register.RegisterUtils;
import org.riverock.interfaces.ContainerConstants;

/**
 * @author SergeMaslyukov
 *         Date: 30.11.2005
 *         Time: 22:10:17
 *         $Id: SendPasswordAction.java 1435 2007-09-23 17:08:57Z serg_main $
 */
public class SendPasswordAction implements ActionInstance {

    public String execute( ModuleActionRequest moduleActionRequest ) throws ActionException {

        String email = moduleActionRequest.getRequest().getString( RegisterConstants.NAME_EMAIL );

        if( StringUtils.isBlank( email ) ) {
            return RegisterError.emailIsEmpty( moduleActionRequest );
        }

        String checkStatus = RegisterUtils.checkCaptcha(moduleActionRequest, moduleActionRequest.getRequest());
        if (checkStatus != null) {
            return checkStatus;
        }

        PortalUserManager portalUserManager = (PortalUserManager)
            moduleActionRequest.getRequest().getAttribute(ContainerConstants.PORTAL_PORTAL_USER_MANAGER);

        Map<String, String> messages = new HashMap<String, String>();
        messages.put(PortalUserManager.SEND_PASSWORD_SUBJECT_MESSAGE, "Requested info");
        messages.put(
            PortalUserManager.SEND_PASSWORD_BODY_ONE_PASSWORD_MESSAGE,
            moduleActionRequest.getResourceBundle().getString( "reg.send-password.your-password" )
        );
        messages.put(
            PortalUserManager.SEND_PASSWORD_START_BODY_MANY_PASSWORDS_MESSAGE,
            moduleActionRequest.getResourceBundle().getString( "reg.send-password.start-body.your-password" )
        );
        messages.put(
            PortalUserManager.SEND_PASSWORD_END_BODY_MANY_PASSWORDS_MESSAGE,
            moduleActionRequest.getResourceBundle().getString( "reg.send-password.end-body.your-password" )
        );

        UserOperationStatus status = portalUserManager.sendPassword(email, messages);
        if (status.getOperationCode()==PortalUserManager.STATUS_OK_OPERATION) {
            return Constants.OK_EXECUTE_STATUS;
        }
        else if (status.getOperationCode()==PortalUserManager.STATUS_NO_SUCH_EMAIL) {
            return noSuchEmailMessage(moduleActionRequest, email);
        }
        else if (status.getOperationCode()==PortalUserManager.STATUS_NOT_REGISTERED) {
            return noSuchEmailMessage(moduleActionRequest, email);
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

    private String noSuchEmailMessage(ModuleActionRequest moduleActionRequest, String email) {
        return RegisterError.noSuchEmail(moduleActionRequest, email);
/*
        ActionMessage actionMessage = new ActionMessage(moduleActionRequest.getResourceBundle(), RegisterConstants.NO_SUCH_EMAIL, email);
        PortletErrors.initMessage(moduleActionRequest.getRequest(), actionMessage);
        return Constants.OK_EXECUTE_STATUS;
*/
    }
}