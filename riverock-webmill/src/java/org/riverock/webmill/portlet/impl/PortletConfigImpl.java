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



package org.riverock.webmill.portlet.impl;



import java.util.Iterator;

import java.util.Locale;

import java.util.ResourceBundle;



import javax.portlet.PortletConfig;

import javax.portlet.PortletContext;



import org.riverock.interfaces.schema.javax.portlet.PortletType;

import org.riverock.webmill.portlet.PortletTools;



import org.apache.pluto.om.common.Language;

import org.apache.pluto.om.common.LanguageSet;

import org.apache.pluto.om.common.Parameter;

import org.apache.pluto.om.portlet.PortletDefinition;



public class PortletConfigImpl implements PortletConfig

//    , InternalPortletConfig

{

    private javax.servlet.ServletConfig servletConfig;

    private PortletContext portletContext;

    private PortletType portletDefinition;



    public PortletConfigImpl(javax.servlet.ServletConfig servletConfig,

                             PortletContext portletContext,

                             PortletType portletDefinition)

    {

        this.servletConfig = servletConfig;

        this.portletContext = portletContext;

        this.portletDefinition = portletDefinition;

    }



    // javax.portlet.PortletConfig implementation -------------------------------------------------

    public String getPortletName()

    {

        return portletDefinition.getPortletName()!=null?portletDefinition.getPortletName().getContent():null;

    }



    public PortletContext getPortletContext()

    {

        return portletContext;

    }



    public ResourceBundle getResourceBundle(Locale locale)

    {

//        LanguageSet languageSet = portletDefinition.getSupportedLocaleLocale()LanguageSet();

//        Language lang = languageSet.get(locale);

//

//        if (lang == null)

//        {

//            Locale defaultLocale = languageSet.getDefaultLocale();

//            lang = languageSet.get(defaultLocale);

//        }

//

//        return lang.getResourceBundle();



        return    ResourceBundle.getBundle("MyResources", Locale.ENGLISH);



    }



    public String getInitParameter(String name)

    {

        if (name == null)

        {

            throw new IllegalArgumentException("Parameter name == null");

        }

        return PortletTools.getStringParam(portletDefinition, name);

    }



    public java.util.Enumeration getInitParameterNames()

    {

        return portletDefinition.enumerateInitParam();

//        return new java.util.Enumeration()

//        {

//            private Iterator iterator = portletDefinition.enumerateInitParam();

//

//            public boolean hasMoreElements()

//            {

//                return iterator.hasNext();

//            }

//

//            public Object nextElement()

//            {

//                if (iterator.hasNext())

//                {

//                    return((Parameter)iterator.next()).getName();

//                }

//                else

//                {

//                    return null;

//                }

//            }

//        };

    }

    // --------------------------------------------------------------------------------------------

/*

    // org.apache.pluto.core.InternalPortletConfig implementation ---------------------------------

    public javax.servlet.ServletConfig getServletConfig()

    {

        return servletConfig;

    }



    public PortletDefinition getInternalPortletDefinition()

    {

        return portletDefinition;

    }

    // --------------------------------------------------------------------------------------------

*/

}

