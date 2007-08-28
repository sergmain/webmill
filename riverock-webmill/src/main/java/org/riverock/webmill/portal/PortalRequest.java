package org.riverock.webmill.portal;

import org.riverock.interfaces.portal.dao.PortalDaoProvider;
import org.riverock.interfaces.portal.CookieManager;
import org.riverock.interfaces.portal.xslt.XsltTransformer;
import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.webmill.portal.url.interpreter.UrlInterpreterResult;
import org.riverock.webmill.portal.url.interpreter.ExtendedCatalogItemBean;
import org.riverock.webmill.portal.template.PortalTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;
import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: SergeMaslyukov
 * Date: 28.08.2007
 * Time: 22:05:00
 * To change this template use File | Settings | File Templates.
 */
public interface PortalRequest {
    void destroy();

    PortalDaoProvider getPortalDaoProvider();

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
