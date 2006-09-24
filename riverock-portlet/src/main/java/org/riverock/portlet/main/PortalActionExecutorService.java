/*
 * org.riverock.portlet - Portlet Library
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
package org.riverock.portlet.main;

import javax.portlet.PortletRequest;

import org.apache.log4j.Logger;

import org.riverock.portlet.tools.FacesTools;
import org.riverock.webmill.container.ContainerConstants;
import org.riverock.interfaces.portal.action.PortalActionExecutor;

/**
 * @author Sergei Maslyukov
 *         Date: 22.09.2006
 *         Time: 20:37:01
 *         <p/>
 *         $Id$
 */
public class PortalActionExecutorService {
    private static Logger log = Logger.getLogger( PortalActionExecutorService.class );

    public String createGoogleSitemap() {
        log.debug("Start PortalActionExecutorService.createGoogleSitemap()");
        
        PortletRequest portletRequest = FacesTools.getPortletRequest();
        PortalActionExecutor executor =
            (PortalActionExecutor)portletRequest.getAttribute(ContainerConstants.PORTAL_PORTAL_ACTION_EXECUTOR);

        if (log.isDebugEnabled()) {
            log.debug("executor: " + executor);
        }
        executor.execute("create-google-sitemap", null);
        return "manager-index";
    }

}
