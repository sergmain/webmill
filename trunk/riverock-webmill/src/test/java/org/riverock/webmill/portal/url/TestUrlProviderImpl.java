package org.riverock.webmill.portal.url;

import java.util.Map;
import java.util.HashMap;

/**
 * User: SMaslyukov
 * Date: 23.08.2007
 * Time: 13:23:41
 */
public class TestUrlProviderImpl implements UrlProvider {

    private Map<String, String> aliases = new HashMap<String, String>();

    public TestUrlProviderImpl() {
    }

    public TestUrlProviderImpl(Map<String, String> aliases) {
        this.aliases = aliases;
    }

    public String getAlias(String url) {
        return aliases.get(url);
    }
}
