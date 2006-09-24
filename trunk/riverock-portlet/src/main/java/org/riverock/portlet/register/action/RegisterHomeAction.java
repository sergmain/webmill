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

import org.riverock.module.action.Action;
import org.riverock.module.action.ModuleActionRequest;
import org.riverock.module.exception.ActionException;
import org.riverock.portlet.captcha.CaptchaServiceSingleton;
import org.riverock.portlet.register.Constants;
import org.riverock.portlet.register.RegisterConstants;

/**
 * @author SergeMaslyukov
 *         Date: 30.11.2005
 *         Time: 20:54:35
 *         $Id$
 */
public class RegisterHomeAction implements Action {
    public String execute(ModuleActionRequest moduleActionRequest) throws ActionException {

        String captchId = CaptchaServiceSingleton.createCaptchaId();
        moduleActionRequest.getRequest().setAttribute(RegisterConstants.CAPTCHA_ID, captchId);
        return Constants.OK_EXECUTE_STATUS;
    }

}
