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

import java.io.File;

import org.riverock.webmill.container.portlet.bean.PortletApplication;
import org.riverock.webmill.container.portlet.bean.PortletDefinition;
import org.riverock.webmill.container.portlet.bean.Preferences;

/**
 * @author smaslyukov
 *         Date: 08.08.2005
 *         Time: 19:01:23
 *         $Id$
 */
public class PortletDefinitionProcessorTest {

    public static void main(String[] args) throws Exception {
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
