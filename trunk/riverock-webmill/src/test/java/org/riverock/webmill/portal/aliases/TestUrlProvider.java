package org.riverock.webmill.portal.aliases;

import junit.framework.TestCase;

import org.riverock.webmill.portal.dao.OfflineDaoFactory;

/**
 * User: SergeMaslyukov
 * Date: 09.09.2007
 * Time: 22:33:13
 * $Id$
 */
public class TestUrlProvider extends TestCase {

    public void testPage() throws Exception {
        OfflineDaoFactory.init();

        assertEquals("/", UrlProviderFactory.getUrlProvider().getAlias(16L, "/mill"));
        assertEquals("/", UrlProviderFactory.getUrlProvider().getAlias(16L, "/mill/ctx"));
        assertEquals("/", UrlProviderFactory.getUrlProvider().getAlias(16L, "/aaa"));
        assertEquals("/rrr", UrlProviderFactory.getUrlProvider().getAlias(16L, "/ddd"));
        assertNull(UrlProviderFactory.getUrlProvider().getAlias(16L, "/not-exist"));
    }
}
