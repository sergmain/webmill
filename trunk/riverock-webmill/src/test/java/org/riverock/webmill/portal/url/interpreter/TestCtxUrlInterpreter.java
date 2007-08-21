package org.riverock.webmill.portal.url.interpreter;

import java.io.File;
import java.util.Locale;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import junit.framework.TestCase;

import org.riverock.webmill.portal.url.UrlInterpreterParameter;
import org.riverock.webmill.portal.url.PortletDefinitionProviderImpl;
import org.riverock.webmill.portal.url.UrlInterpreterIterator;
import org.riverock.webmill.portal.url.UrlInterpreterResult;
import org.riverock.webmill.portal.utils.PortalUtils;
import org.riverock.interfaces.portal.PortalInfo;
import org.riverock.interfaces.portal.bean.Site;
import org.riverock.interfaces.portlet.menu.MenuLanguage;

/**
 * User: SMaslyukov
 * Date: 21.08.2007
 * Time: 11:24:21
 */
public class TestCtxUrlInterpreter extends TestCase {

    public void testCtxUrl() {
        boolean isMultiPartRequest = false;
        Map<String, List<String>> params = new HashMap<String, List<String>>();
//        File requestBodyFile = new File("aaa");
        File requestBodyFile = null;
        UrlInterpreterParameter interpreterParameter =
            new UrlInterpreterParameter(
                "/ctx",
                new TestPortletDefinitionProvider(),
                false,
                requestBodyFile,
                16L,
                Locale.ENGLISH,
                (isMultiPartRequest
                    ?null
                    :params
                ),
                new PortalInfo() {
                    public Long getSiteId() {
                        return null;  //To change body of implemented methods use File | Settings | File Templates.
                    }

                    public Long getCompanyId() {
                        return null;  //To change body of implemented methods use File | Settings | File Templates.
                    }

                    public Long getSiteLanguageId(Locale locale) {
                        return null;  //To change body of implemented methods use File | Settings | File Templates.
                    }

                    public MenuLanguage getMenu(String string) {
                        return null;  //To change body of implemented methods use File | Settings | File Templates.
                    }

                    public Map<String, String> getPortalProperties() {
                        return null;  //To change body of implemented methods use File | Settings | File Templates.
                    }

                    public Locale getDefaultLocale() {
                        return null;  //To change body of implemented methods use File | Settings | File Templates.
                    }

                    public Site getSite() {
                        return null;  //To change body of implemented methods use File | Settings | File Templates.
                    }
                }
        );

        UrlInterpreterResult urlInterpreterResult = UrlInterpreterIterator.interpretUrl(interpreterParameter);

                }
}
