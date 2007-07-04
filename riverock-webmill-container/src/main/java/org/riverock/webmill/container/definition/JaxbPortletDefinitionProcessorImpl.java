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

import org.riverock.webmill.container.portlet.bean.PortletApplication;

import java.io.File;
import java.io.InputStream;

/**
 * User: SergeMaslyukov
 * Date: 09.09.2006
 * Time: 17:24:18
 * <p/>
 * $Id$
 */
public class JaxbPortletDefinitionProcessorImpl implements PortletDefinitionProcessor {
    public PortletApplication process(File portletFile) {
        System.out.println("Start unmarshal portlet file: " + portletFile);
        return ProcessAs_v1_0.processAs_v1_0(portletFile);
    }

    public PortletApplication process(InputStream inputStream) {
        System.out.println("Start unmarshal portlet file from input stream.");
        return ProcessAs_v1_0.processAs_v1_0(inputStream);
    }

}
