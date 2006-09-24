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
package org.riverock.webmill.container.portlet;

import javax.portlet.RenderRequest;

import org.riverock.webmill.container.bean.SitePortletData;
import org.riverock.webmill.container.portlet.bean.PortletDefinition;

/**
 * @author smaslyukov
 *         Date: 11.08.2005
 *         Time: 19:05:42
 *         $Id$
 */
public interface PortletContentCache {
    void invalidate(String portletName);

    SitePortletData getContent( PortletDefinition portletDefinition );

    void setContent( PortletDefinition portletDefinition, SitePortletData data, RenderRequest renderRequest );
}
