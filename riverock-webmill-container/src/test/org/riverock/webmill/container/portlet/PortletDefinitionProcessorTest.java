package org.riverock.webmill.container.portlet;

import java.io.File;

import javax.portlet.PortletPreferences;

import org.riverock.webmill.container.portlet.bean.PortletApplication;
import org.riverock.webmill.container.portlet.bean.PortletDefinition;

/**
 * @author smaslyukov
 *         Date: 08.08.2005
 *         Time: 19:01:23
 *         $Id$
 */
public class PortletDefinitionProcessorTest {

    public static void main(String[] args) throws Exception {
        PortletDefinitionProcessor processor = new PortletDefinitionProcessorImpl();

        File file = new File("portlet.2.xml");

        PortletApplication application = processor.digest( file );

        PortletDefinition definition = application.getPortlet(0);
        PortletPreferences portletPreferences = definition.getPortletPreferences();
        String[] values = portletPreferences.getValues(
            "RssXml",
            new String[]
            {
                "http://www.theserverside.com/rss/theserverside-0.9.rdf"
            }
        );

        int i = 0;

    }
}
