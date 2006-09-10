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

import org.riverock.webmill.container.tools.ContainertStringUtils;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

/**
 * The init-param element contains a name/value pair as an
 * initialization param of the portlet
 * Used in:portlet
 *
 * @version $Revision$ $Date$
 */
public class InitParam implements Serializable {
    private static final long serialVersionUID = 30434672384237145L;

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

    /**
     * Field value
     */
    private String value;


    public InitParam() {
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
     * Returns the value of field 'value'.
     *
     * @return the value of field 'value'.
     */
    public String getValue() {
        return this.value;
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

    /**
     * Sets the value of field 'value'.
     * 
     * According to the final portlet 1.0 spec, page 81,
     * "Portlet containers should ignore all leading whitespace characters before the first non-whitespace character,
     * and all trailing whitespace characters after the last nonwhitespace character for PCDATA within text nodes of a deployment descriptor."
     *
     * @param value the value of field 'value'.
     */
    public void setValue(String value) {
        this.value = ContainertStringUtils.strip(value);
    }
}
