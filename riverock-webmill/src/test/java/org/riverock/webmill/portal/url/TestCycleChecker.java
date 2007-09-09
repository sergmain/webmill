package org.riverock.webmill.portal.url;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

import junit.framework.TestCase;

import org.riverock.webmill.portal.aliases.UrlCycleChecker;
import org.riverock.webmill.portal.aliases.UrlProvider;

/**
 * User: SMaslyukov
 * Date: 23.08.2007
 * Time: 12:49:53
 */
public class TestCycleChecker extends TestCase {

    UrlProvider urlProvider=null;

    public void setUp() {
        Map<String, String> aliases = new HashMap<String, String>();
        aliases.put("aaa", "bbb");
        aliases.put("bbb", "ccc");
        aliases.put("ccc", "eee");

        urlProvider = new TestUrlProviderImpl(aliases);
    }

    public void testCycle() {
        List<String> path = UrlCycleChecker.isCycle( urlProvider, "bbb", "eee");
        assertNotNull(path);

        path = UrlCycleChecker.isCycle( urlProvider, "bbb", "ddd");
        assertNotNull(path);

        path = UrlCycleChecker.isCycle( urlProvider, "bbb", "ddd");
        assertNotNull(path);

        path = UrlCycleChecker.isCycle( urlProvider, "eee", "bbb");
        assertNotNull(path);

        path = UrlCycleChecker.isCycle( urlProvider, "eee", "zzz");
        assertNull(path);

    }
}
