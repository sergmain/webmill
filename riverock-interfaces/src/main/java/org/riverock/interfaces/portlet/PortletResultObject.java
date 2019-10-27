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
package org.riverock.interfaces.portlet;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletConfig;

/**
 *
 * $Author: serg_main $
 *
 * $Id: PortletResultObject.java 1228 2007-06-28 10:08:00Z serg_main $
 *
 */
public interface PortletResultObject {
    /**
     * method setParameters() always queried before getInstance() and getInstanceByCode() methods
     * @param renderRequest portlet's request
     * @param renderResponse portlet's response
     * @param portletConfig portlet's config
     */
    public void setParameters( RenderRequest renderRequest, RenderResponse renderResponse, PortletConfig portletConfig );
    public PortletResultContent getInstance( Long id ) throws PortletException;
    public PortletResultContent getInstanceByCode( String portletCode_ ) throws PortletException;
}