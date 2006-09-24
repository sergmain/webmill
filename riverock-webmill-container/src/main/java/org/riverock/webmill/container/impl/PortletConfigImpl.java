/*
 * org.riverock.webmill.container - Webmill portlet container implementation
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
package org.riverock.webmill.container.impl;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;

import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;

import org.riverock.webmill.container.tools.PortletService;
import org.riverock.webmill.container.resource.PortletResourceBundle;
import org.riverock.webmill.container.portlet.bean.PortletDefinition;
import org.riverock.webmill.container.portlet.bean.InitParam;

/**
 * User: SergeMaslyukov
 * Date: 08.12.2004
 * Time: 0:22:19
 * $Id$
 */
public final class PortletConfigImpl implements PortletConfig {
    
    private PortletContext portletContext;
    private PortletDefinition portletDefinition;
    private PortletResourceBundle resourceBundle = null;

    public PortletConfigImpl(
        PortletContext portletContext,
        PortletDefinition portletDefinition,
        PortletResourceBundle resourceBundle
        )
    {
        this.portletContext = portletContext;
        this.portletDefinition = portletDefinition;
        this.resourceBundle = resourceBundle;
    }

    public String getPortletName() {
        return portletDefinition.getPortletName();
    }

    public PortletContext getPortletContext() {
        return portletContext;
    }

    public ResourceBundle getResourceBundle(Locale locale) {
        return resourceBundle.getResourceBundle( locale );
    }

    public String getInitParameter(String name){
        if (name == null){
            throw new IllegalArgumentException("Name of parameter is null");
        }
        return PortletService.getStringParam(portletDefinition, name);
    }

    public java.util.Enumeration getInitParameterNames(){
        Iterator<InitParam> iterator = portletDefinition.getInitParam().iterator();
        Set<String> names = new HashSet<String>();
        while(iterator.hasNext()) {
            names.add( iterator.next().getName() );
        }
        return Collections.enumeration( names );
    }
}
