package org.riverock.webmill.portal.aliases;

import java.util.Observer;

/**
 * User: SMaslyukov
 * Date: 23.08.2007
 * Time: 12:55:47
 */
public interface UrlProvider extends Observer {
    String getAlias(Long siteId, String url);
}
