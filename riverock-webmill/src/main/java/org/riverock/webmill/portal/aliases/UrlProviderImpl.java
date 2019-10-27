package org.riverock.webmill.portal.aliases;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.Observable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.riverock.webmill.portal.dao.InternalDaoFactory;
import org.riverock.interfaces.portal.bean.UrlAlias;

/**
 * User: SMaslyukov
 * Date: 23.08.2007
 * Time: 12:55:56
 */
public class UrlProviderImpl implements UrlProvider {

    private final ConcurrentMap<Long, Map<String, String>> urlAliases = new ConcurrentHashMap<Long, Map<String, String>>();

    public UrlProviderImpl() {
        InternalDaoFactory.getInternalAliasDao().addObserver(this);
    }

    public String getAlias(Long siteId, String targetUrl) {
        Map<String, String> aliases = urlAliases.get(siteId);

        if (aliases==null) {
            aliases = loadAliases(siteId);
            urlAliases.put(siteId, aliases);
        }

        return aliases.get(targetUrl);  
    }

    public synchronized void invalidateCache() {
        urlAliases.clear();
    }

    private synchronized Map<String, String> loadAliases(Long siteId) {
        Map<String, String> aliases = urlAliases.get(siteId);

        if (aliases!=null) {
            return aliases;
        }
        aliases = new HashMap<String, String>();
        List<UrlAlias> urlAliases = InternalDaoFactory.getInternalAliasDao().getUrlAliases(siteId);
        for (UrlAlias urlAlias : urlAliases) {
            aliases.put(urlAlias.getUrl(), getEffectiveAlias(urlAlias.getUrl(), urlAliases));
        }
        return aliases;
    }

    private String getEffectiveAlias(String url, List<UrlAlias> urlAliases) {
        for (UrlAlias urlAlias : urlAliases) {
            if (urlAlias.getUrl().equals(url)) {
                return getEffectiveAlias(urlAlias.getAlias(), urlAliases);
            }
        }
        return url;
    }

    public void update(Observable o, Object arg) {
        invalidateCache();
    }
}
