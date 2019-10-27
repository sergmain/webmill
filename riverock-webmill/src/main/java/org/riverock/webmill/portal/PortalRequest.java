package org.riverock.webmill.portal;

import java.io.File;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.riverock.interfaces.portal.CookieManager;
import org.riverock.interfaces.portal.spi.PortalSpiProvider;
import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.webmill.portal.template.PortalTemplate;
import org.riverock.webmill.portal.url.interpreter.ExtendedCatalogItemBean;
import org.riverock.webmill.portal.url.interpreter.UrlInterpreterResult;
import org.riverock.webmill.portal.xslt.XsltTransformer;

/**
 * User: SergeMaslyukov
 * Date: 28.08.2007
 * Time: 22:05:00
 */
public interface PortalRequest {
    void destroy();

    PortalSpiProvider getPortalSpiProvider();

    UrlInterpreterResult getUrlInterpreterResult();

    String getErrorString();

    Locale[] getPreferredLocales();

    AuthSession getAuth();

    boolean isUserInRole(String role);

    Locale getLocale();

    HttpServletRequest getHttpRequest();

    HttpServletResponse getHttpResponse();

    PortalTemplate getTemplate();

    ExtendedCatalogItemBean getDefaultCtx();

    CookieManager getCookieManager();

    File getRequestBodyFile();

    boolean isMultiPartRequest();

    UrlInterpreterResult getRequestContext();

    File getTempPath();

    PortalTransformationParameters getPortalTransformationParameters();

    XsltTransformer getXslt();

    long getStartMills();
}
