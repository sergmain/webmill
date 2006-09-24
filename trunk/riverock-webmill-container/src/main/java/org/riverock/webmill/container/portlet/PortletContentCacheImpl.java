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
        Integer exp = portletDefinition.getExpirationCache();
        // don't cache content
        if (exp==null || exp==0) {
            return null;
        }

        CacheEntry entry = contentCache.get( portletDefinition.getFullPortletName() );
        if (entry==null)
            return null;

        if (exp <((System.currentTimeMillis()-entry.lastInitTime)/1000) )  {
            contentCache.remove( portletDefinition.getFullPortletName() );
            return null;
        }
        return entry.data;
    }

    public void setContent( PortletDefinition portletDefinition, SitePortletData data, RenderRequest renderRequest ) {
        if (data==null || portletDefinition.getExpirationCache()==null)
            return;

        int expirationTime = portletDefinition.getExpirationCache();

        String exp = renderRequest.getProperty( RenderResponse.EXPIRATION_CACHE );
        try {
            if (exp!=null)
                expirationTime = Integer.parseInt( exp );
        }
        catch( NumberFormatException e ) {
            //Todo decide - throw or not exception?
        }

        if (expirationTime<0) {
            contentCache.remove( portletDefinition.getFullPortletName() );
            return;
        }
        contentCache.put( portletDefinition.getPortletName(), new CacheEntry( data ) );
    }
}
