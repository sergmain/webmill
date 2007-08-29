package org.riverock.webmill.portal.url.interpreter;

import java.io.File;
import java.util.Locale;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import javax.portlet.PortletMode;
import javax.portlet.WindowState;

import junit.framework.TestCase;

import org.riverock.webmill.portal.dao.OfflineDaoFactory;
import org.riverock.webmill.portal.url.definition_provider.PortletDefinitionProvider;
import org.riverock.webmill.portal.url.interpreter.UrlInterpreterParameter;
import org.riverock.webmill.portal.url.interpreter.UrlInterpreterResult;
import org.riverock.webmill.portal.url.UrlInterpreterIterator;
import org.riverock.webmill.portal.info.PortalInfoImpl;
import org.riverock.interfaces.portal.PortalInfo;

/**
 * User: SMaslyukov
 * Date: 27.08.2007
 * Time: 16:14:26
 */
public class TestActionPortletUrlInterpreter extends TestCase {

    public void testPage() throws Exception {
        OfflineDaoFactory.init();

        String pathInfo = "/page/en/admin";
        PortletDefinitionProvider portletDefinitionProvider = new TestPortletDefinitionProvider();
        boolean isMultiPartRequest = false;

        File requestBodyFile = null;
        Long siteId = 16L;
        Locale predictedLocale = Locale.ENGLISH;

        Map<String, List<String>> httpRequestParameter = new HashMap<String, List<String>>();

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
        assertEquals(Locale.ENGLISH, result.getLocale());
        assertEquals("::mill.language", result.getDefaultPortletName());
        assertTrue(result.getDefaultRequestState().isActionRequest());
        assertEquals(PortletMode.VIEW, result.getDefaultRequestState().getPortletMode());
        assertEquals(WindowState.NORMAL, result.getDefaultRequestState().getWindowState());
    }
}