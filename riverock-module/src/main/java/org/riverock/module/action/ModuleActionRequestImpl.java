/*
 * org.riverock.module - Abstract layer for web module
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock -- The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
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
