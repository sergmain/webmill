package org.riverock.webmill.portal.url.interpreter;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.portlet.PortletMode;
import javax.portlet.WindowState;

import junit.framework.TestCase;

import org.riverock.interfaces.portal.PortalInfo;
import org.riverock.webmill.portal.dao.OfflineDaoFactory;
import org.riverock.webmill.portal.info.PortalInfoImpl;
import org.riverock.webmill.portal.url.UrlInterpreterIterator;
import org.riverock.webmill.portal.url.definition_provider.PortletDefinitionProvider;

/**
 * User: SMaslyukov
 * Date: 27.08.2007
 * Time: 16:14:26
 */
public class TestPageUrlInterpreter extends TestCase {

    public void testPage() throws Exception {
        UrlInterpreterResult result = prepareUrl();

        assertEquals(Locale.ENGLISH, result.getLocale());
//        assertEquals("dynamic_me.askmore", result.getTemplateName());
        assertEquals("::mill.article_plain", result.getDefaultPortletName());
        assertFalse(result.getDefaultRequestState().isActionRequest());
        assertEquals(PortletMode.VIEW, result.getDefaultRequestState().getPortletMode());
        assertEquals(WindowState.NORMAL, result.getDefaultRequestState().getWindowState());

        
    }

    public static UrlInterpreterResult prepareUrl() throws Exception {
        OfflineDaoFactory.init();

        String pathInfo = "/page/en/action/download";
        PortletDefinitionProvider portletDefinitionProvider = new TestPortletDefinitionProvider();
        boolean isMultiPartRequest = false;

        File requestBodyFile = null;
        Long siteId = 16L;
        Locale predictedLocale = Locale.ENGLISH;

        Map<String, List<String>> httpRequestParameter = new HashMap<String, List<String>>();

        PortalInfo portalInfo = PortalInfoImpl.getInstance(TestPageUrlInterpreter.class.getClassLoader(), siteId);

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
        return result;
    }
}
