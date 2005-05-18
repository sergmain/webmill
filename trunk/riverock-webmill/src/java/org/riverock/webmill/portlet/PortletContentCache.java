package org.riverock.webmill.portlet;

import java.util.Map;
import java.util.HashMap;

import javax.portlet.RenderResponse;
import javax.portlet.RenderRequest;

import org.riverock.webmill.schema.site.SitePortletDataType;
import org.riverock.interfaces.schema.javax.portlet.PortletType;
import org.riverock.interfaces.schema.javax.portlet.ExpirationCacheType;

import org.apache.log4j.Logger;

/**
 * User: SergeMaslyukov
 * Date: 24.12.2004
 * Time: 1:28:00
 * $Id$
 */
public final class PortletContentCache {
    private final static Logger log = Logger.getLogger( PortletContentCache.class );

    private final static class CacheEntry {
        SitePortletDataType data = null;
        long lastInitTime = 0;

        private CacheEntry(SitePortletDataType data) {
            this.data = data;
            lastInitTime = System.currentTimeMillis();
        }
    }

    private static Map contentCache = new HashMap(300, 1.1f);
    
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

    public static SitePortletDataType getContent( final PortletType portletDefinition ) {
        ExpirationCacheType exp = portletDefinition.getExpirationCache();
        // don't cache content
        if (exp!=null && exp.getContent()==0)
            return null;



        CacheEntry entry = (CacheEntry)contentCache.get( portletDefinition.getPortletName().getContent() );
        if (entry==null)
            return null;

        if (exp.getContent()<((System.currentTimeMillis()-entry.lastInitTime)/1000) )  {
            contentCache.remove( portletDefinition.getPortletName().getContent() );
            return null;
        }
        return entry.data;
    }

    public static void setContent( PortletType portletDefinition, SitePortletDataType data, RenderRequest renderRequest ) {
        if (data==null || portletDefinition.getExpirationCache()==null)
            return;

        int expirationTime = portletDefinition.getExpirationCache().getContent();

        String exp = renderRequest.getProperty( RenderResponse.EXPIRATION_CACHE );
        try {
            if (exp!=null)
                expirationTime = Integer.parseInt( exp );
        }
        catch( NumberFormatException e ) {
            log.error(
                "Portlet '" + portletDefinition.getPortletName().getContent() + "' " +
                "set invalid value for RenderResponse.EXPIRATION_CACHE: "+exp+". " +
                "Will be used value from portlet definition: " + expirationTime
            );
        }

        if (expirationTime<0) {
            contentCache.remove( portletDefinition.getPortletName().getContent() );
            return;
        }
        contentCache.put( portletDefinition.getPortletName().getContent(), new CacheEntry( data ) );
    }
}
