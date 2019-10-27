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
package org.riverock.portlet.register;

import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import org.riverock.module.action.ActionMessage;
import org.riverock.module.action.ModuleActionRequest;
import org.riverock.module.web.request.ModuleRequest;


/**
 * @author SMaslyukov
 *         Date: 22.05.2005
 *         Time: 15:15:40
 *         $Id: PortletErrors.java 1049 2006-11-14 15:56:05Z serg_main $
 */
public class PortletErrors {
    private static final Logger log = Logger.getLogger(PortletErrors.class);

    public static final String SYSTEM_ERROR = "system_error";

    public static String systemError(ModuleRequest request, ResourceBundle resourceBundle) {
        return error(request, resourceBundle, SYSTEM_ERROR);
    }

    public static String error(ModuleActionRequest actionBean, String errorCode) {
        return error(actionBean.getRequest(), actionBean.getResourceBundle(), errorCode);
    }

    public static String error(ModuleRequest request, ResourceBundle resourceBundle, String errorCode) {

        ActionMessage message = new ActionMessage(resourceBundle, errorCode);
        return error(request, message);
    }

    public static String error(ModuleRequest request, ActionMessage message) {
        initMessage(request, message);

        return Constants.ERROR_EXECUTE_STATUS;
    }

    public static void initMessage(ModuleRequest request, ActionMessage message) {
        ActionMessages actionMessages = (ActionMessages)request.getAttribute(Constants.ACTION_MESSAGE);
        if (actionMessages==null) {
            actionMessages = new ActionMessages();
            request.setAttribute( Constants.ACTION_MESSAGE,actionMessages);
        }
        actionMessages.getMessages().add(message);
    }
}
