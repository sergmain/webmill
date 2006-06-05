/*
 * org.riverock.module - Abstract layer for web module
 *
 * Copyright (C) 2004, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 */
package org.riverock.module.web.dispatcher;

import java.io.IOException;

import javax.portlet.PortletRequestDispatcher;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.PortletException;

import org.apache.log4j.Logger;

import org.riverock.module.web.request.ModuleRequest;
import org.riverock.module.web.response.ModuleResponse;
import org.riverock.module.exception.ModuleException;

/**
 * @author SMaslyukov
 *         Date: 26.04.2005
 *         Time: 15:37:08
 *         $Id$
 */
public class PortletModuleRequestDispatcherImpl implements ModuleRequestDispatcher {
    static private final Logger log = Logger.getLogger(PortletModuleRequestDispatcherImpl.class);

    private PortletRequestDispatcher portletRequestDispatcher = null;
    public PortletModuleRequestDispatcherImpl(PortletRequestDispatcher portletRequestDispatcher) {
        this.portletRequestDispatcher = portletRequestDispatcher;
    }

    public void include(ModuleRequest moduleRequest, ModuleResponse moduleResponse) throws ModuleException, IOException {
        if (moduleRequest.getOriginRequest() instanceof RenderRequest &&
            moduleResponse.getOriginResponse() instanceof RenderResponse) {
            try {
                portletRequestDispatcher.include(
                    (RenderRequest)moduleRequest.getOriginRequest(),
                    (RenderResponse)moduleResponse.getOriginResponse()
                );
            }
            catch (PortletException e) {
                String es = "Error include new resource";
                log.error(es, e);
                throw new ModuleException(es, e);
            }
        }
    }
}
