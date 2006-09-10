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
 * User attribute defines a user specific attribute that the
 * portlet application needs. The portlet within this application
 * can access this attribute via the request parameter USER_INFO
 * map.
 * Used in: portlet-app
 *
 * @version $Revision$ $Date$
 */
public class UserAttribute implements Serializable {
    private static final long serialVersionUID = 30434672384237171L;

    /**
     * Field id
     */
    private java.lang.String id;

    /**
     * Field description
     */
    private List<Description> description;

    /**
     * Field name
     */
    private String name;


    public UserAttribute() {
        super();
        description = new ArrayList<Description>();
    }

    /**
     * Method addDescription
     *
     * @param vDescription
     */
    public void addDescription(Description vDescription) {
        description.add(vDescription);
    }

    /**
     * Method getDescription
     */
    public List<Description> getDescription() {
        return description;
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
     * Returns the value of field 'name'.
     *
     * @return the value of field 'name'.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Method setDescription
     *
     * @param description
     */
    public void setDescription(List<Description> description) {
        this.description=description;
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
     * Sets the value of field 'name'.
     *
     * @param name the value of field 'name'.
     */
    public void setName(String name) {
        this.name = name;
    }
}
