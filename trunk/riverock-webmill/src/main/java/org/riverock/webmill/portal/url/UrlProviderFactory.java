package org.riverock.webmill.portal.url;

/**
 * User: SMaslyukov
 * Date: 23.08.2007
 * Time: 12:56:12
 */
public class UrlProviderFactory {
    private static UrlProvider urlProvider = new UrlProviderImpl();

    public static UrlProvider getUrlProvider() {
        return urlProvider;
    }

    static void setUrlProvider(UrlProvider urlProvider) {
        UrlProviderFactory.urlProvider = urlProvider;
    }
}
