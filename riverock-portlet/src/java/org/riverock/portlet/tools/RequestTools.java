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
