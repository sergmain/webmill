/*
 * org.riverock.webmill.container - Webmill portlet container implementation
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
 * $Id: PortletConfigImpl.java 1055 2006-11-14 17:56:15Z serg_main $
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
