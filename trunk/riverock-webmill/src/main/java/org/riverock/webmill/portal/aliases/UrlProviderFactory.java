package org.riverock.webmill.portal.aliases;

import java.util.Observer;
import java.util.Observable;

import org.riverock.webmill.portal.dao.InternalDaoFactory;

/**
 * User: SergeMaslyukov
 * Date: 09.09.2007
 * Time: 22:10:46
 * $Id$
 */
public class UrlProviderFactory {
    private static UrlProvider URL_PROVIDER = new UrlProviderImpl();

    public static UrlProvider getUrlProvider() {
        return URL_PROVIDER;
    }

    public static void setUrlProvider(UrlProvider URL_PROVIDER) {
        UrlProviderFactory.URL_PROVIDER = URL_PROVIDER;
    }
}
