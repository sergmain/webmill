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
package org.riverock.webmill.container.portlet.bean;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

/**
 * The portlet-collectionType is used to identify a subset
 * of portlets within a portlet application to which a
 * security constraint applies.
 * Used in: security-constraint
 *
 * @version $Revision$ $Date$
 */
public class PortletCollection implements Serializable {
    private static final long serialVersionUID = 30434672384237151L;


    /**
     * Field portletName
     */
    private List<String> portletName;


    public PortletCollection() {
        super();
        portletName = new ArrayList<String>();
    }

    /**
     * Method addPortletName
     *
     * @param vPortletName
     */
    public void addPortletName(String vPortletName) {
        portletName.add(vPortletName);
    }

    /**
     * Method getPortletName
     */
    public List<String> getPortletName() {
        return portletName;
    }

    /**
     * Method setPortletName
     *
     * @param portletName
     */
    public void setPortletName(List<String> portletName) {
        this.portletName=portletName;
    }
}
