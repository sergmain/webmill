package org.riverock.webmill.portal.url.interpreter;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.portlet.PortletMode;
import javax.portlet.WindowState;

import junit.framework.TestCase;

import org.riverock.interfaces.portal.PortalInfo;
import org.riverock.webmill.portal.info.PortalInfoImpl;
import org.riverock.webmill.portal.dao.OfflineDaoFactory;
import org.riverock.webmill.portal.url.definition_provider.PortletDefinitionProvider;
import org.riverock.webmill.portal.url.UrlInterpreterIterator;
import org.riverock.webmill.portal.url.interpreter.UrlInterpreterParameter;
import org.riverock.webmill.portal.url.interpreter.UrlInterpreterResult;

/**
 * User: SMaslyukov
 * Date: 21.08.2007
 * Time: 11:24:21
 */
public class TestNewsCtxUrlInterpreter extends TestCase {

    public void testInvokeNewsUrl() throws Exception {
        OfflineDaoFactory.init();

        String pathInfo = "/ctx/en,templ-mill-dyn1-en,mill.news_block,r,ns48,228/ctx";
        PortletDefinitionProvider portletDefinitionProvider = new TestPortletDefinitionProvider();
        boolean isMultiPartRequest = false;

        File requestBodyFile = null;
        Long siteId = 16L;
        Locale predictedLocale = Locale.ENGLISH;

        Map<String, List<String>> httpRequestParameter = new HashMap<String, List<String>>();
        httpRequestParameter.put("mill.template", Arrays.asList("dynamic_me.askmore"));
        httpRequestParameter.put("mill.xmlroot", Arrays.asList("XmlNewsItem"));
        httpRequestParameter.put("news.type", Arrays.asList("item"));
        httpRequestParameter.put("mill.id_news_item", Arrays.asList("296"));

        PortalInfo portalInfo = PortalInfoImpl.getInstance(this.getClass().getClassLoader(), siteId);

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
        assertEquals(Locale.ENGLISH, result.getLocale());
        assertEquals(228, (long)result.getContextId());
//        assertEquals("dynamic_me.askmore", result.getTemplateName());
        assertEquals("::mill.news_block", result.getDefaultPortletName());
        assertFalse(result.getDefaultRequestState().isActionRequest());
        assertEquals(PortletMode.VIEW, result.getDefaultRequestState().getPortletMode());
        assertEquals(WindowState.NORMAL, result.getDefaultRequestState().getWindowState());

    }
}