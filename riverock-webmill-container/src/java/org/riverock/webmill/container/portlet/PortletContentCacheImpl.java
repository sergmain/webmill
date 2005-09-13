/*
 * org.riverock.webmill.container -- Webmill portlet container implementation
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
package org.riverock.webmill.container.portlet;

import java.util.Map;
import java.util.HashMap;

import javax.portlet.RenderResponse;
import javax.portlet.RenderRequest;

import org.riverock.webmill.container.bean.SitePortletData;
import org.riverock.webmill.container.portlet.bean.PortletDefinition;

/**
 * User: SergeMaslyukov
 * Date: 24.12.2004
 * Time: 1:28:00
 * $Id$
 */
public final class PortletContentCacheImpl implements PortletContentCache {

    private final static class CacheEntry {
        SitePortletData data = null;
        long lastInitTime = 0;

        private CacheEntry(SitePortletData data) {
            this.data = data;
            lastInitTime = System.currentTimeMillis();
        }
    }

    public void invalidate(String portletName) {
        synchronized(PortletContentCacheImpl.class) {
            contentCache.remove( portletName );
        }
    }

    public PortletContentCache getInstance() {
        return new PortletContentCacheImpl();
    }

    private Map<String, CacheEntry> contentCache = new HashMap<String, CacheEntry>(300, 1.1f);
    
    // Todo
    /*
If the content of a portlet is cached, the cache has not expired and the portlet is not the
target of the client request, then the request handling methods of the portlet should not be
invoked as part of the client request. Instead, the portlet-container should use the data
from the cache.

If the content of a portlet is cached and a client request is targeted to the portlet, the 5
portlet container must discard the cache and invoke the request handling methods of the
portlet.
    */

    public SitePortletData getContent( final PortletDefinition portletDefinition ) {
        String exp = portletDefinition.getExpirationCache();
        // don't cache content
        if (exp==null )
            return null;

        int expire = Integer.parseInt(exp);
        if (expire==0)
            return null;

        CacheEntry entry = (CacheEntry)contentCache.get( portletDefinition.getPortletName() );
        if (entry==null)
            return null;

        if (expire<((System.currentTimeMillis()-entry.lastInitTime)/1000) )  {
            contentCache.remove( portletDefinition.getPortletName() );
            return null;
        }
        return entry.data;
    }

    public void setContent( PortletDefinition portletDefinition, SitePortletData data, RenderRequest renderRequest ) {
        if (data==null || portletDefinition.getExpirationCache()==null)
            return;

        int expirationTime = Integer.parseInt( portletDefinition.getExpirationCache() );

        String exp = renderRequest.getProperty( RenderResponse.EXPIRATION_CACHE );
        try {
            if (exp!=null)
                expirationTime = Integer.parseInt( exp );
        }
        catch( NumberFormatException e ) {
/*
            log.error(
                "Portlet '" + portletDefinition.getPortletName() + "' " +
                "set invalid value for RenderResponse.EXPIRATION_CACHE: "+exp+". " +
                "Will be used value from portlet definition: " + expirationTime
            );
*/
        }

        if (expirationTime<0) {
            contentCache.remove( portletDefinition.getPortletName() );
            return;
        }
        contentCache.put( portletDefinition.getPortletName(), new CacheEntry( data ) );
    }
}
