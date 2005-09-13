package org.riverock.webmill.container.portlet;

import java.io.File;

import org.riverock.webmill.container.portlet.bean.PortletApplication;

/**
 * @author smaslyukov
 *         Date: 08.08.2005
 *         Time: 19:01:23
 *         $Id$
 */
public class PortletDefinitionProcessorTest {

    public static void main(String[] args) throws Exception {
        PortletDefinitionProcessor processor = new PortletDefinitionProcessorImpl();

        File file = new File("portlet.xml");

        PortletApplication application = processor.digest( file );

        int i = 0;

    }
}
