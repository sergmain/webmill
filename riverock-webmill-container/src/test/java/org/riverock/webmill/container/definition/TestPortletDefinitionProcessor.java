/*
 * org.riverock.webmill.container - Webmill portlet container implementation
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package org.riverock.webmill.container.definition;

import java.io.InputStream;

import junit.framework.TestCase;

import org.riverock.webmill.container.portlet.bean.PortletApplication;
import org.riverock.webmill.container.portlet.bean.PortletDefinition;
import org.riverock.webmill.container.portlet.bean.Preferences;

/**
 * User: SergeMaslyukov
 * Date: 10.09.2006
 * Time: 4:16:36
 * <p/>
 * $Id$
 */
public class TestPortletDefinitionProcessor extends TestCase {
    private static class TestItem {
        private String fileName=null;
        private int countPortlets;

        public TestItem(String fileName, int countPortlets) {
            this.fileName = fileName;
            this.countPortlets = countPortlets;
        }
    }

    public void testJaxb() throws Exception {
        PortletDefinitionProcessor processor = new JaxbPortletDefinitionProcessorImpl();

        TestItem[] files = {
            new TestItem("/xml/portlet.1.xml", 1),
            new TestItem("/xml/portlet.2.xml", 1),
            new TestItem("/xml/portlet.3.xml", 17),
            new TestItem("/xml/portlet.4.xml", 61),
            new TestItem("/xml/portlet.5.xml", 26),
            new TestItem("/xml/portlet.6.xml", 4),
        };

        for (TestItem file : files) {
//            System.out.println("file = " + file.fileName);

            InputStream is = TestPortletDefinitionProcessor.class.getResourceAsStream(file.fileName); 
            PortletApplication portletApplication = processor.process(is);
            assertEquals(file.countPortlets, portletApplication.getPortlet().size());
        }

        InputStream is = TestPortletDefinitionProcessor.class.getResourceAsStream("/xml/portlet.6.xml");
        PortletApplication application = processor.process( is );

        PortletDefinition definition = application.getPortlet().get(0);
        Preferences portletPreferences = definition.getPreferences();
        int i = 0;

    }

}
