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
 *         $Id$
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
/*
        // hack to output where exception is created
        try {
            getException();
        }
        catch (Exception e) {
            log.error("error", e);
        }
*/
        request.setAttribute(
            Constants.ACTION_MESSAGE,
            new ActionMessage(resourceBundle, errorCode)
        );
        return Constants.ERROR_EXECUTE_STATUS;
    }

    private static void getException() throws Exception {
        throw new Exception();
    }
}
