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

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

/**
 * A custom portlet mode that one or more portlets in
 * this portlet application supports.
 * Used in: portlet-app
 *
 * @version $Revision$ $Date$
 */
public class CustomPortletMode implements Serializable {
    private static final long serialVersionUID = 30434672384237127L;

    /**
     * Field id
     */
    private java.lang.String id;

    /**
     * Field description
     */
    private List<Description> description;

    /**
     * Field portletMode
     */
    private String portletMode;


    public CustomPortletMode() {
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
     * Returns the value of field 'portletMode'.
     *
     * @return the value of field 'portletMode'.
     */
    public String getPortletMode() {
        return this.portletMode;
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
     * Sets the value of field 'portletMode'.
     *
     * @param portletMode the value of field 'portletMode'.
     */
    public void setPortletMode(String portletMode) {
        this.portletMode = portletMode;
    }
}
