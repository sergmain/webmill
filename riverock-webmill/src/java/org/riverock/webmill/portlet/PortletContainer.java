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
 * Date: 28.01.2004
 * Time: 17:49:32
 * @author Serge Maslyukov
 * $Id$
 */

package org.riverock.webmill.portlet;

import java.util.Map;
import java.util.HashMap;
import java.lang.reflect.Constructor;

import javax.portlet.Portlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;

import org.riverock.webmill.exception.PortalException;
import org.riverock.webmill.portlet.impl.PortletConfigImpl;
import org.riverock.webmill.portlet.impl.PortletContextImpl;
import org.riverock.interfaces.schema.javax.portlet.PortletType;

import org.apache.log4j.Logger;


public class PortletContainer {
    private static Logger log = Logger.getLogger(PortletContainer.class);

    private static Map portletsMap = new HashMap();

    private static Object syncObj = new Object();

    public static Portlet getPortletInstance( String namePortlet ) throws PortalException {
        Object obj = portletsMap.get( namePortlet );
        if (obj == null) {
            synchronized (syncObj) {
                Object temp = portletsMap.get( namePortlet );
                if (temp != null)
                    return (Portlet) temp;

                Portlet newPortlet = null;
                try {
                    newPortlet = createInstance( namePortlet );
                } catch (Exception e) {
                    String es = "Erorr create instance of portlet '"+namePortlet+"'";
                    log.error(es, e);
                    throw new PortalException(es, e);
                }
                portletsMap.put( namePortlet, newPortlet );
                return newPortlet;
            }
        }
        return (Portlet) obj;
    }

    private static Portlet createInstance( String namePortlet )
        throws Exception
    {

        PortletType portletDefinition = PortletManager.getPortletDescription( namePortlet );

        Constructor constructor = Class.forName(portletDefinition.getPortletClass()).getConstructor(null);

        if (log.isDebugEnabled())
            log.debug("#12.12.005  constructor is " + constructor);

        Portlet object = null;
        if (constructor != null)
            object = (Portlet) constructor.newInstance(null);
        else
            throw new PortalException("Error get constructor for " + portletDefinition.getPortletClass());

        PortletContext portletContext =
            new PortletContextImpl( ContextNavigator.portalServletConfig.getServletContext() );

        PortletResourceBundleProvider.PortletResourceBundle resourceBundle =
            PortletResourceBundleProvider.getInstance( portletDefinition );
        PortletConfig portletConfig =
            new PortletConfigImpl( portletContext, portletDefinition, resourceBundle);

        object.init( portletConfig );

        return object;
    }


}
