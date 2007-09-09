package org.riverock.webmill.portal.aliases;

import java.util.ArrayList;
import java.util.List;

/**
 * User: SMaslyukov
 * Date: 23.08.2007
 * Time: 12:52:18
 */
public class UrlCycleChecker {

    public static List<String> isCycle(UrlProvider urlProvider, Long siteId, String url, String alias) {
        List<String> list = new ArrayList<String>();
        String aliasTemp = urlProvider.getAlias(siteId, url);
        if (aliasTemp!=null) {
            list.add(url);
            list.add(aliasTemp);
            return list;
        }

        String sourceUrl = url;
        String targetUrl = alias;

        while (true) {
            list.add(sourceUrl);
            if (list.contains(targetUrl)) {
                return list;
            }
            sourceUrl = targetUrl;
            targetUrl = urlProvider.getAlias(siteId, targetUrl);
            if (targetUrl==null) {
                return null;
            }
        }
    }
}
