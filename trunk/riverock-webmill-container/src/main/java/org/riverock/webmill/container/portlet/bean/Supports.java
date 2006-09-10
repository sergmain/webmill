/*
 * org.riverock.webmill.container -- Webmill portlet container implementation
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
package org.riverock.webmill.container.portlet.bean;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

/**
 * Supports indicates the portlet modes a
 * portlet supports for a specific content type. All portlets must
 * <p/>
 * support the view mode.
 * Used in: portlet
 *
 * @version $Revision$ $Date$
 */
public class Supports implements Serializable {
    private static final long serialVersionUID = 30434672384237169L;

    /**
     * Field id
     */
    private java.lang.String id;

    /**
     * Field mimeType
     */
    private String mimeType;

    /**
     * Field portletMode
     */
    private List<String> portletMode;


    public Supports() {
        super();
        portletMode = new ArrayList<String>();
    }


    /**
     * Method addPortletMode
     *
     * @param vPortletMode
     */
    public void addPortletMode(String vPortletMode) {
        portletMode.add(vPortletMode);
    }

    /**
     * Returns the value of field 'id'.
     *
     * @return the value of field 'id'.
     */
    public java.lang.String getId() {
        return this.id;
    }

    /**
     * Returns the value of field 'mimeType'.
     *
     * @return the value of field 'mimeType'.
     */
    public String getMimeType() {
        return this.mimeType;
    }

    /**
     * Method getPortletMode
     */
    public List<String> getPortletMode() {
        return portletMode;
    }

    /**
     * Sets the value of field 'id'.
     *
     * @param id the value of field 'id'.
     */
    public void setId(java.lang.String id) {
        this.id = id;
    }

    /**
     * Sets the value of field 'mimeType'.
     *
     * @param mimeType the value of field 'mimeType'.
     */
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    /**
     * Method setPortletMode
     *
     * @param portletMode
     */
    public void setPortletMode(List<String> portletMode) {
        this.portletMode=portletMode;
    }
}
