/*
 * org.riverock.webmill -- Portal framework implementation
 *
 * Copyright (C) 2004, Riverock Software, All Rights Reserved.
 *
 * Riverock -- The Open-source Java Development Community
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
 *
 */
package org.riverock.webmill.portal.impl;

import java.util.Enumeration;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Arrays;

import javax.portlet.PortalContext;
import javax.portlet.PortletMode;
import javax.portlet.WindowState;

/**
 * @author SergeMaslyukov
 *         Date: 20.11.2005
 *         Time: 15:42:59
 *         $Id$
 */
public class PortalContextImpl implements PortalContext {
    private String portalInfo = null;

    public PortalContextImpl(String portalInfo) {
        this.portalInfo = portalInfo;
    }

    public String getProperty(String s) {
        return null;
    }

    public Enumeration getPropertyNames() {
        return Collections.enumeration( new ArrayList() );
    }

    public Enumeration getSupportedPortletModes() {
        return Collections.enumeration( Arrays.asList( new PortletMode[]{PortletMode.VIEW} ) );
    }

    public Enumeration getSupportedWindowStates() {
        return Collections.enumeration( Arrays.asList( new WindowState[]{WindowState.NORMAL} ) );
    }

    public String getPortalInfo() {
        return portalInfo;
    }
}
