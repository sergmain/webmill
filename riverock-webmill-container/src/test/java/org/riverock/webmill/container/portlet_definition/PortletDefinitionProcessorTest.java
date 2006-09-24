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

import java.io.File;

import org.riverock.webmill.container.portlet.bean.PortletApplication;
import org.riverock.webmill.container.portlet.bean.PortletDefinition;
import org.riverock.webmill.container.portlet.bean.Preferences;
import org.riverock.webmill.container.portlet_definition.PortletDefinitionProcessor;
import org.riverock.webmill.container.portlet_definition.PortletDefinitionProcessorWithDigisterImpl;
import org.riverock.webmill.container.portlet_definition.JaxbPortletDefinitionProcessorImpl;

/**
 * @author smaslyukov
 *         Date: 08.08.2005
 *         Time: 19:01:23
 *         $Id$
 */
public class PortletDefinitionProcessorTest {

    public static void main(String[] args) throws Exception {
//        PortletDefinitionProcessor processor = new PortletDefinitionProcessorWithDigisterImpl();
        PortletDefinitionProcessor processor = new JaxbPortletDefinitionProcessorImpl();

        String[] files = {
            "doc/xml/portlet.1.xml",
            "doc/xml/portlet.2.xml",
            "doc/xml/portlet.3.xml",
            "doc/xml/portlet.4.xml",
            "doc/xml/portlet.5.xml",
            "doc/xml/portlet.6.xml"
        };

        for (String file : files) {
            System.out.println("file = " + file);
            processor.process( new File(file) );
        }

//        File file = new File("portlet.2.xml");
        File file = new File("doc/xml/portlet.6.xml");

        PortletApplication application = processor.process( file );

        PortletDefinition definition = application.getPortlet().get(0);
        Preferences portletPreferences = definition.getPreferences();
        int i = 0;

    }
}
