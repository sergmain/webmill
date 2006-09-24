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

import org.riverock.module.action.ModuleActionRequest;

/**
 * @author SergeMaslyukov
 *         Date: 30.11.2005
 *         Time: 20:28:28
 *         $Id$
 */
public class RegisterError {
    public static String notLoggedError(ModuleActionRequest actionBean) {
        return PortletErrors.error(actionBean, RegisterConstants.NOT_LOGGED);
    }

    public static String emailIsEmpty(ModuleActionRequest actionBean) {
        return PortletErrors.error(actionBean, RegisterConstants.EMAIL_IS_EMPTY);
    }

    public static String noSuchEmail( ModuleActionRequest actionBean ) {
        return PortletErrors.error(actionBean, RegisterConstants.NO_SUCH_EMAIL);
    }

    public static String systemError( ModuleActionRequest actionBean ) {
        return PortletErrors.error(actionBean, RegisterConstants.SYSTEM_ERROR);
    }

    public static String roleIsNull( ModuleActionRequest actionBean ) {
        return PortletErrors.error(actionBean, RegisterConstants.ROLE_IS_NULL);
    }

    public static String loginAlreadyRegistered(ModuleActionRequest moduleActionRequest) {
        return PortletErrors.error(moduleActionRequest, RegisterConstants.LOGIN_ALREADY_REGISTERED);
    }

    public static String userLoginIsNull(ModuleActionRequest moduleActionRequest) {
        return PortletErrors.error(moduleActionRequest, RegisterConstants.USER_LOGIN_IS_NULL);
    }

    public static String userPasswordIsNull(ModuleActionRequest moduleActionRequest) {
        return PortletErrors.error(moduleActionRequest, RegisterConstants.USER_PASSWORD_IS_NULL);
    }

    public static String userPasswordIsMismatch(ModuleActionRequest moduleActionRequest) {
        return PortletErrors.error(moduleActionRequest, RegisterConstants.USER_PASSWORD_IS_MISMATCH);
    }

    public static String userEmailIsNull(ModuleActionRequest moduleActionRequest) {
        return PortletErrors.error(moduleActionRequest, RegisterConstants.USER_EMAIL_IS_NULL);
    }

    public static String captchaWrong(ModuleActionRequest moduleActionRequest) {
        return PortletErrors.error(moduleActionRequest, RegisterConstants.CAPTCHA_WRONG);
    }
}
