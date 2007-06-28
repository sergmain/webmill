/*
 * org.riverock.portlet - Portlet Library
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
package org.riverock.portlet.main;

import javax.portlet.PortletRequest;

import org.apache.log4j.Logger;

import org.riverock.portlet.tools.FacesTools;
import org.riverock.interfaces.ContainerConstants;
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
