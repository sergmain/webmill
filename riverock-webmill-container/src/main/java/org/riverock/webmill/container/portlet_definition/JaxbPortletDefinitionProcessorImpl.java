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

import org.riverock.webmill.container.portlet.bean.PortletApplication;
import org.riverock.webmill.container.portlet.PortletContainerException;

import java.io.File;

/**
 * User: SergeMaslyukov
 * Date: 09.09.2006
 * Time: 17:24:18
 * <p/>
 * $Id$
 */
public class JaxbPortletDefinitionProcessorImpl implements PortletDefinitionProcessor {
    public PortletApplication process(File portletFile) throws PortletContainerException {

        PortletApplication portletApplication = ProcessAs_v1_0.processAs_v1_0(portletFile);
        return portletApplication;
    }

}
