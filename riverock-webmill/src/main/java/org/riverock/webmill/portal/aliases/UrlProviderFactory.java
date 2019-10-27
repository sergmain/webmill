package org.riverock.webmill.portal.aliases;

/**
 * User: SergeMaslyukov
 * Date: 09.09.2007
 * Time: 22:10:46
 * $Id$
 */
public class UrlProviderFactory {
    private static UrlProvider urlProvider = new UrlProviderImpl();
    private static PortletAliaslProvider portletAliaslProvider = new PortletAliaslProviderImpl();

    public static PortletAliaslProvider getPortletAliaslProvider() {
        return portletAliaslProvider;
    }

    public static void setPortletAliaslProvider(PortletAliaslProvider portletAliaslProvider) {
        UrlProviderFactory.portletAliaslProvider = portletAliaslProvider;
    }

    public static UrlProvider getUrlProvider() {
        return urlProvider;
    }

    public static void setUrlProvider(UrlProvider URL_PROVIDER) {
        UrlProviderFactory.urlProvider = URL_PROVIDER;
    }
}
