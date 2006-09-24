/*
 * org.riverock.portlet - Portlet Library
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.riverock.portlet.register.action;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

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

    public String execute( ModuleActionRequest moduleActionRequest ) throws ActionException {

        String email = moduleActionRequest.getRequest().getString( RegisterConstants.NAME_EMAIL );

        if( StringUtils.isBlank( email ) ) {
            return RegisterError.emailIsEmpty( moduleActionRequest );
        }

        PortalUserManager portalUserManager = (PortalUserManager)
            moduleActionRequest.getRequest().getAttribute(ContainerConstants.PORTAL_PORTAL_USER_MANAGER);

        Map<String, String> messages = new HashMap<String, String>();
        messages.put(PortalUserManager.SEND_PASSWORD_SUBJECT_MESSAGE, "Requested info");
        messages.put(
            PortalUserManager.SEND_PASSWORD_BODY_ONE_PASSWORD_MESSAGE,
            moduleActionRequest.getResourceBundle().getString( "reg.send-password.your-password" )
        );

        UserOperationStatus status = portalUserManager.sendPassword(email, messages);
        if (status.getOperationCode()==PortalUserManager.STATUS_OK_OPERATION) {
            return Constants.OK_EXECUTE_STATUS;
        }
        else if (status.getOperationCode()==PortalUserManager.STATUS_NO_SUCH_EMAIL) {
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