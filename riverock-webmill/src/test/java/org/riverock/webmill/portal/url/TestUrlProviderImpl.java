package org.riverock.webmill.portal.url;

import java.util.Map;
import java.util.HashMap;
import java.util.Observable;

import org.riverock.webmill.portal.aliases.UrlProvider;

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

    public String getAlias(Long siteId, String url) {
        return aliases.get(url);
    }

    public void update(Observable o, Object arg) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
