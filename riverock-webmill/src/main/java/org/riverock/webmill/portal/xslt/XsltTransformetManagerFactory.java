package org.riverock.webmill.portal.xslt;

import org.riverock.webmill.portal.xslt.XsltTransformerManager;

/**
 * User: SMaslyukov
 * Date: 26.07.2007
 * Time: 17:56:43
 */
public class XsltTransformetManagerFactory {

    public static XsltTransformerManager getInstanse(Long siteId) {
        return new XsltTransformerManagerImpl(siteId);
    }

    public static void destroyAll() {

    }
}
