package org.riverock.webmill.portal.page_element;

import java.util.List;
import java.util.Locale;
import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.TestCase;

import org.riverock.webmill.portal.url.interpreter.TestPageUrlInterpreter;
import org.riverock.webmill.portal.url.interpreter.UrlInterpreterResult;
import org.riverock.webmill.portal.url.interpreter.ExtendedCatalogItemBean;
import org.riverock.webmill.portal.PortalRequest;
import org.riverock.webmill.portal.PortalTransformationParameters;
import org.riverock.webmill.portal.template.PortalTemplate;
import org.riverock.webmill.portal.template.PortalTemplateManagerFactory;
import org.riverock.interfaces.portal.dao.PortalDaoProvider;
import org.riverock.interfaces.portal.CookieManager;
import org.riverock.interfaces.portal.xslt.XsltTransformer;
import org.riverock.interfaces.sso.a3.AuthSession;

/**
 * User: SMaslyukov
 * Date: 29.08.2007
 * Time: 16:53:24
 */
public class TestInitPageElements extends TestCase {

    public void testInitPageElements() throws Exception {
        final UrlInterpreterResult result = TestPageUrlInterpreter.prepareUrl();
/*
<?xml version="1.0" encoding="utf-8" standalone="yes"?>
<Template xmlns:element="http://webmill.riverock.org/xsd/riverock-template-page-elements.xsd">
    <element:xslt name="HeaderStart"/>
    <element:xslt name="HeaderTextIndex"/>
    <element:xslt name="HeaderEnd"/>
    <element:xslt name="MainStart"/>
    <element:portlet name="mill.menu" code="mill-right1-en"/>
    <element:xslt name="GoogleAdSence_1"/>
    <element:xslt name="Separator"/>
    <element:portlet xmlRoot="TopMenu" name="mill.menu" code="mill-topmenu-en"/>
    <element:xslt name="SeparatorDynStart"/>
    <element:dynamic/>
    <element:xslt name="SeparatorDynImage"/>
    <element:portlet xmlRoot="ActionMenu" name="mill.menu" code="action-menu"/>
    <element:portlet name="mill.article_plain" code="SF.NET_LOGO"/>
    <element:xslt name="SeparatorDynEnd"/>
    <element:xslt name="Footer"/>
</Template>
*/
        final PortalTemplate template = PortalTemplateManagerFactory.getInstance(16L).getTemplate(result.getTemplateName(), result.getLocale().toString());

        PortalRequest portalRequest = new PortalRequest() {
            public void destroy() {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public PortalDaoProvider getPortalDaoProvider() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public UrlInterpreterResult getUrlInterpreterResult() {
                return result;
            }

            public String getErrorString() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public Locale[] getPreferredLocales() {
                return new Locale[0];  //To change body of implemented methods use File | Settings | File Templates.
            }

            public AuthSession getAuth() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public boolean isUserInRole(String role) {
                return false;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public Locale getLocale() {
                return result.getLocale();
            }

            public HttpServletRequest getHttpRequest() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public HttpServletResponse getHttpResponse() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public PortalTemplate getTemplate() {
                return template;
            }

            public ExtendedCatalogItemBean getDefaultCtx() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public CookieManager getCookieManager() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public File getRequestBodyFile() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public boolean isMultiPartRequest() {
                return false;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public UrlInterpreterResult getRequestContext() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public File getTempPath() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public PortalTransformationParameters getPortalTransformationParameters() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public XsltTransformer getXslt() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public long getStartMills() {
                return 0;  //To change body of implemented methods use File | Settings | File Templates.
            }
        };

        List<PageElement> pageElements = InitPageElements.initPageElements(portalRequest, null);
        assertNotNull(pageElements);
    }
}
