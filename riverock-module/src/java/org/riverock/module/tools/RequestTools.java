package org.riverock.module.tools;

import javax.portlet.PortletRequest;

import org.riverock.webmill.container.tools.PortletService;

/**
 * @author SergeMaslyukov
 *         Date: 22.11.2005
 *         Time: 20:29:02
 *         $Id$
 */
public class RequestTools {

    public static final String CONTENT_TYPE_UTF8 = "utf-8";
    public static final String CONTENT_TYPE_8859_1 = "8859_1";

    public static String getString( final PortletRequest request, final String f, String defaultValue) {
        return PortletService.getString(request, f, defaultValue, CONTENT_TYPE_8859_1, CONTENT_TYPE_UTF8);
    }

    public static String getString( final PortletRequest request, final String f) {
        return PortletService.getString(request, f, null, CONTENT_TYPE_8859_1, CONTENT_TYPE_UTF8);
    }
}
