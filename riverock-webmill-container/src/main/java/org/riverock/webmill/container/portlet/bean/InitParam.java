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
