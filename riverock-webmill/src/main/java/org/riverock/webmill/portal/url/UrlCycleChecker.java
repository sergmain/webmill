package org.riverock.webmill.portal.url;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * User: SMaslyukov
 * Date: 23.08.2007
 * Time: 12:52:18
 */
public class UrlCycleChecker {

    public static List<String> isCycle(Map<String, Object> parameters) {
        String url = (String)parameters.get("url");
        String alias = (String)parameters.get("alias");
        return isCycle(UrlProviderFactory.getUrlProvider(), url, alias);
    }

    public static List<String> isCycle(String url, String alias) {
        return isCycle(UrlProviderFactory.getUrlProvider(), url, alias);
    }

    public static List<String> isCycle(UrlProvider urlProvider, String url, String alias) {
        List<String> list = new ArrayList<String>();
        String aliasTemp = urlProvider.getAlias(url);
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
            targetUrl = urlProvider.getAlias(targetUrl);
            if (targetUrl==null) {
                return null;
            }
        }
    }
}
