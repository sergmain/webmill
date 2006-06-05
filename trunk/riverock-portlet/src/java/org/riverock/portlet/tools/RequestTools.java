/*
 * org.riverock.portlet -- Portlet Library
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
package org.riverock.portlet.tools;

import java.util.Map;

import javax.portlet.PortletRequest;



import org.apache.log4j.Logger;

import org.riverock.webmill.container.tools.PortletService;
import org.riverock.common.tools.StringTools;
import org.riverock.portlet.price.PriceListPosition;

/**
 * @author smaslyukov
 *         Date: 29.07.2005
 *         Time: 21:24:45
 *         $Id$
 */
public class RequestTools {
    private static Logger log = Logger.getLogger( RequestTools.class );

    public static String getString( final PortletRequest request, final String f, String defaultValue) {
        return PortletService.getString(request, f, defaultValue, ContentTypeTools.CONTENT_TYPE_8859_1, ContentTypeTools.CONTENT_TYPE_UTF8);
    }

    public static String getString( final PortletRequest request, final String f) {
        return PortletService.getString(request, f, null, ContentTypeTools.CONTENT_TYPE_8859_1, ContentTypeTools.CONTENT_TYPE_UTF8);
    }

    public static String getString( final Map map, final String f ) {
        return getString( map, f, null );
    }

    public static String getString( final Map map, final String f, final String def ) {

        String s_ = def;
        final Object obj = map.get( f );
        if (obj != null) {
            try {
                s_ = StringTools.convertString( obj.toString(), ContentTypeTools.CONTENT_TYPE_8859_1, ContentTypeTools.CONTENT_TYPE_UTF8);
            }
            catch (Exception e) {
                // not rethrow exception 'cos this method return def value in this case
                log.warn("Exception in getString(), def value will be return", e);
            }
        }
        return s_;
    }

}
