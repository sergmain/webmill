/*
 * org.riverock.webmill -- Portal framework implementation
 * 
 * Copyright (C) 2004, Riverock Software, All Rights Reserved.
 * 
 * Riverock -- The Open-source Java Development Community
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
 *
 */

/**
 * User: serg_main
 * Date: 29.01.2004
 * Time: 20:28:36
 * @author Serge Maslyukov
 * $Id$
 */

package org.riverock.webmill.portlet.impl;

import java.util.Locale;

import org.riverock.interfaces.schema.javax.portlet.PortletType;

import org.apache.pluto.om.portlet.PortletDefinition;
import org.apache.pluto.om.portlet.ContentTypeSet;
import org.apache.pluto.om.portlet.PortletApplicationDefinition;
import org.apache.pluto.om.common.Description;
import org.apache.pluto.om.common.LanguageSet;
import org.apache.pluto.om.common.ParameterSet;
import org.apache.pluto.om.common.SecurityRoleRefSet;
import org.apache.pluto.om.common.PreferenceSet;
import org.apache.pluto.om.common.DisplayName;
import org.apache.pluto.om.common.ObjectID;
import org.apache.pluto.om.servlet.ServletDefinition;


public class PortletDefinitionImp implements PortletDefinition
{
    private PortletType portletType = null;

    public ObjectID getId()
    {
        return null;
    }

    public String getClassName()
    {
        return null;
    }

    public String getName()
    {
        return null;
    }

    public Description getDescription(Locale locale)
    {
        return null;
    }

    public LanguageSet getLanguageSet()
    {
        return null;
    }

    public ParameterSet getInitParameterSet()
    {
        return null;
    }

    public SecurityRoleRefSet getInitSecurityRoleRefSet()
    {
        return null;
    }

    public PreferenceSet getPreferenceSet()
    {
        return null;
    }

    public ContentTypeSet getContentTypeSet()
    {
        return null;
    }

    public PortletApplicationDefinition getPortletApplicationDefinition()
    {
        return null;
    }

    public ServletDefinition getServletDefinition()
    {
        return null;
    }

    public DisplayName getDisplayName(Locale locale)
    {
        return null;
    }

    public String getExpirationCache()
    {
        return null;
    }

    public ClassLoader getPortletClassLoader()
    {
        return null;
    }


}
