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
package org.riverock.webmill.portlet;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.riverock.generic.exception.GenericException;
import org.riverock.generic.tools.StringManager;
import org.riverock.interfaces.schema.javax.portlet.PortletType;
import org.riverock.webmill.exception.PortalException;

import org.apache.log4j.Logger;

/**
 * User: SergeMaslyukov
 * Date: 06.12.2004
 * Time: 14:36:12
 * $Id$
 */
public final class PortletResourceBundleProvider {
    private final static Logger log = Logger.getLogger( PortletResourceBundleProvider.class );

    private static Map messages = null;
    private static Map portlets = Collections.unmodifiableMap( new HashMap() );

    static Map getMessagesMapInternal(){
        return messages;
    }

    static Map getPortletsMapInternal(){
        return portlets;
    }

    public static final Object syncObj = new Object();
    public static PortletResourceBundle getInstance( final PortletType portletDefinition ) throws GenericException, PortalException {

        final String portletName = portletDefinition.getPortletName().getContent();
        if ( log.isDebugEnabled() ) {
            log.debug( "Start create resource bundle. portlet name: " + portletName );
        }

        if (messages==null) {
//            messages = Collections.unmodifiableMap( StringManager.getMessages() );
            messages = StringManager.getMessages();
        }

        Object obj = portlets.get( portletName );
        if (obj!=null) {
            return (PortletResourceBundle)obj;
        }

        PortletResourceBundle bundle = null;
        synchronized( syncObj ) {
            obj = portlets.get( portletName );
            if (obj!=null) {
                return (PortletResourceBundle)obj;
            }

            bundle = PortletResourceBundle.getInstance( portletDefinition );
            Map map = new HashMap( portlets );
            map.put( portletName, bundle );
            portlets = Collections.unmodifiableMap( map );
        }
        return bundle;
    }
}
