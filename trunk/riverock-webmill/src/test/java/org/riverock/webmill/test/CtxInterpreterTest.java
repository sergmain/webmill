package org.riverock.webmill.test;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.riverock.interfaces.portal.PortalInfo;
import org.riverock.webmill.portal.info.PortalInfoImpl;
import org.riverock.webmill.portal.dao.OfflineDaoFactory;
import org.riverock.webmill.portal.url.definition_provider.PortletDefinitionProvider;
import org.riverock.webmill.portal.url.UrlInterpreterIterator;
import org.riverock.webmill.portal.url.interpreter.UrlInterpreterParameter;
import org.riverock.webmill.portal.url.interpreter.UrlInterpreterResult;
import org.riverock.webmill.portal.url.interpreter.TestPortletDefinitionProvider;

/**
 * User: SMaslyukov
 * Date: 23.08.2007
 * Time: 20:09:06
 */
public class CtxInterpreterTest {
    public static String pathInfo = "/ctx/en,templ-mill-dyn1-en,mill.news_block,r,ns13,228/ctx";

    public static void main(String[] args) throws Exception {

//        HibernateUtilsTest.prepareSession();
        OfflineDaoFactory.init();

        PortletDefinitionProvider portletDefinitionProvider = new TestPortletDefinitionProvider();
        boolean isMultiPartRequest = false;

        File requestBodyFile = null;
        Long siteId = 16L;
        Locale predictedLocale = Locale.ENGLISH;

        Map<String, List<String>> httpRequestParameter = new HashMap<String, List<String>>();
        httpRequestParameter.put("mill.template", Arrays.asList("dynamic_me.askmore"));
        httpRequestParameter.put("mill.xmlroot", Arrays.asList("XmlNewsItem"));
        httpRequestParameter.put("news.type", Arrays.asList("item"));
        httpRequestParameter.put("mill.id_news_item", Arrays.asList("289"));

        PortalInfo portalInfo = PortalInfoImpl.getInstance(CtxInterpreterTest.class.getClassLoader(), siteId);

        String portalContextPath = "";
        UrlInterpreterParameter factoryParameter = new UrlInterpreterParameter(
            pathInfo,
            portletDefinitionProvider,
            isMultiPartRequest,
            requestBodyFile,
            siteId,
            predictedLocale,
            httpRequestParameter,
            portalInfo,
            portalContextPath
        );
        UrlInterpreterResult result = UrlInterpreterIterator.interpretUrl(factoryParameter);
        System.out.println("result = " + result);
    }
}
