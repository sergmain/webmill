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

import java.util.ResourceBundle;

import org.riverock.module.web.config.ModuleConfig;
import org.riverock.module.web.request.ModuleRequest;
import org.riverock.module.web.response.ModuleResponse;
import org.riverock.module.web.url.UrlProvider;

/**
 * @author SMaslyukov
 *         Date: 26.04.2005
 *         Time: 14:25:43
 *         $Id$
 */
public class ModuleActionRequestImpl implements ModuleActionRequest {
    private ModuleConfig moduleConfig = null;
    private ResourceBundle bundle = null;
    private ModuleRequest request = null;
    private ModuleResponse response = null;
    private UrlProvider urlProvider = null;
    private ActionNameProvider actionNameProvider = null;
    private String status = null;

    public ModuleActionRequestImpl(ModuleRequest request, ModuleResponse response, ModuleConfig moduleConfig, UrlProvider urlProvider, ActionNameProvider actionNameProvider) {
        this.request = request;
        this.response = response;
        this.moduleConfig = moduleConfig;
        this.bundle = moduleConfig.getResourceBundle( request.getLocale() );
        this.urlProvider = urlProvider;
        this.actionNameProvider = actionNameProvider;
    }

    public void destroy() {
        this.bundle = null;
        this.request = null;
        this.response = null;
    }

    public ModuleConfig getConfig() {
        return moduleConfig;
    }

    public ResourceBundle getResourceBundle() {
        return bundle;
    }

    public ModuleRequest getRequest() {
        return request;
    }

    public ModuleResponse getResponse() {
        return response;
    }

    public UrlProvider getUrlProvider() {
        return urlProvider;  
    }

    public String getActionName() {
        return actionNameProvider.getActionName();
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }
}
