/*
 * org.riverock.module - Abstract layer for web module
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
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
package org.riverock.module.action;

import org.riverock.module.web.request.ModuleRequest;
import org.riverock.module.WebmillPortletConstants;

/**
 * @author SMaslyukov
 *         Date: 04.05.2005
 *         Time: 13:26:15
 *         $Id$
 */
public class WebmillPortletActionNameProviderImpl implements ActionNameProvider {

    private ModuleRequest request = null;
    public WebmillPortletActionNameProviderImpl(ModuleRequest request) {
        this.request = request;
    }

    public String getActionName() {
        return request.getParameter( WebmillPortletConstants.ACTION_NAME_PARAM );
    }
}
