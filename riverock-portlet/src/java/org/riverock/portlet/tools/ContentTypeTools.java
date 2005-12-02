package org.riverock.portlet.tools;

import javax.servlet.http.HttpServletResponse;
import javax.portlet.PortletException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.riverock.webmill.container.portlet.PortletContainerFactory;

/**
 * @author smaslyukov
 *         Date: 29.07.2005
 *         Time: 19:52:00
 *         $Id$
 */
public class ContentTypeTools {
    private final static Log log = LogFactory.getLog(PortletContainerFactory.class);

    public static final String CONTENT_TYPE_UTF8 = "utf-8";
    public static final String CONTENT_TYPE_8859_1 = "8859_1";

    public static void setContentType(HttpServletResponse response, String charset) throws PortletException {

        final String type = "text/html; charset=" + charset;

        if (log.isDebugEnabled()) {
            log.debug("set new charset: " + type);
            log.debug("response: " + response);
        }

        try {
            response.setContentType(type);
        } catch (Exception e) {
            final String es = "Error set new content type to " + charset;
            log.error(es, e);
            throw new PortletException(es, e);
        }
    }

}
