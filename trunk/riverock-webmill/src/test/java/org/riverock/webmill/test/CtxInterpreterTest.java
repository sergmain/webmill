package org.riverock.webmill.test;

import java.util.List;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.io.File;

import org.riverock.webmill.utils.HibernateUtilsTest;
import org.riverock.webmill.portal.dao.InternalCssDao;
import org.riverock.webmill.portal.dao.HibernateCssDaoImpl;
import org.riverock.webmill.portal.url.UrlInterpreterIterator;
import org.riverock.webmill.portal.url.UrlInterpreterParameter;
import org.riverock.webmill.portal.url.PortletDefinitionProvider;
import org.riverock.webmill.portal.url.UrlInterpreterResult;
import org.riverock.webmill.portal.url.interpreter.TestPortletDefinitionProvider;
import org.riverock.webmill.main.CssBean;
import org.riverock.webmill.port.PortalInfoImpl;
import org.riverock.interfaces.portal.bean.Css;
import org.riverock.interfaces.portal.PortalInfo;

/**
 * User: SMaslyukov
 * Date: 23.08.2007
 * Time: 20:09:06
 */
public class CtxInterpreterTest {
    public static String pathInfo = "/ctx/en,templ-mill-dyn1-en,mill.news_block,r,ns13,228/ctx";

    public static void main(String[] args) {

        HibernateUtilsTest.prepareSession();

        PortletDefinitionProvider portletDefinitionProvider = new TestPortletDefinitionProvider();
        boolean isMultiPartRequest = false;

        File requestBodyFile = null;
        Long siteId = 16L;
        Locale predictedLocale = Locale.ENGLISH;

        Map<String, List<String>> httpRequestParameter = null;
            PortalInfo portalInfo = PortalInfoImpl.getInstance(siteId);

        UrlInterpreterParameter factoryParameter = new UrlInterpreterParameter(
            pathInfo,
            portletDefinitionProvider,
            isMultiPartRequest,
            requestBodyFile,
            siteId,
            predictedLocale,
            httpRequestParameter,
            portalInfo
        );
        UrlInterpreterResult result = UrlInterpreterIterator.interpretUrl(factoryParameter);
        
    }
}
