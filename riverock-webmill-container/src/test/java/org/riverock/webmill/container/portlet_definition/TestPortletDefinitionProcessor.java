/*
 * org.riverock.webmill.container - Webmill portlet container implementation
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.riverock.webmill.container.portlet_definition;

import junit.framework.TestCase;

import java.io.File;

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
            new TestItem("../riverock-portlet/src/main/webapp/WEB-INF/portlet.xml", 24),
            new TestItem("src/test/resources/xml/portlet.1.xml", 1),
            new TestItem("src/test/resources/xml/portlet.2.xml", 1),
            new TestItem("src/test/resources/xml/portlet.3.xml", 17),
            new TestItem("src/test/resources/xml/portlet.4.xml", 61),
            new TestItem("src/test/resources/xml/portlet.5.xml", 26),
            new TestItem("src/test/resources/xml/portlet.6.xml", 4),
            new TestItem("../riverock-commerce/src/main/webapp/WEB-INF/portlet.xml", 5)
        };

        for (TestItem file : files) {
            System.out.println("file = " + file.fileName);
            PortletApplication portletApplication = processor.process( new File(file.fileName) );
            assertTrue(file.countPortlets==portletApplication.getPortlet().size());
        }

//        File file = new File("portlet.2.xml");
        File file = new File("doc/xml/portlet.6.xml");

        PortletApplication application = processor.process( file );

        PortletDefinition definition = application.getPortlet().get(0);
        Preferences portletPreferences = definition.getPreferences();
        int i = 0;

    }

}
