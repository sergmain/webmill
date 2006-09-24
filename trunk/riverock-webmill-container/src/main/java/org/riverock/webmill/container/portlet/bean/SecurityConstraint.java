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
 * The security-constraintType is used to associate
 * intended security constraints with one or more portlets.
 * Used in: portlet-app
 *
 * @version $Revision$ $Date$
 */
public class SecurityConstraint implements Serializable {
    private static final long serialVersionUID = 30434672384237165L;


    /**
     * Field id
     */
    private java.lang.String id;

    /**
     * Field displayName
     */
    private List<DisplayName> displayName;

    /**
     * Field portletCollection
     */
    private PortletCollection portletCollection;

    /**
     * Field userDataConstraint
     */
    private UserDataConstraint userDataConstraint;


    public SecurityConstraint() {
        super();
        displayName = new ArrayList<DisplayName>();
    }

    /**
     * Method addDisplayName
     *
     * @param vDisplayName
     */
    public void addDisplayName(DisplayName vDisplayName) {
        displayName.add(vDisplayName);
    }

    /**
     * Method getDisplayName
     */
    public List<DisplayName> getDisplayName() {
        return displayName;
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
     * Returns the value of field 'portletCollection'.
     *
     * @return the value of field 'portletCollection'.
     */
    public PortletCollection getPortletCollection() {
        return this.portletCollection;
    }

    /**
     * Returns the value of field 'userDataConstraint'.
     *
     * @return the value of field 'userDataConstraint'.
     */
    public UserDataConstraint getUserDataConstraint() {
        return this.userDataConstraint;
    }

    /**
     * Method setDisplayName
     *
     * @param displayName
     */
    public void setDisplayName(List<DisplayName> displayName) {
        this.displayName=displayName;
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
     * Sets the value of field 'portletCollection'.
     *
     * @param portletCollection the value of field
     *                          'portletCollection'.
     */
    public void setPortletCollection(PortletCollection portletCollection) {
        this.portletCollection = portletCollection;
    }

    /**
     * Sets the value of field 'userDataConstraint'.
     *
     * @param userDataConstraint the value of field
     *                           'userDataConstraint'.
     */
    public void setUserDataConstraint(UserDataConstraint userDataConstraint) {
        this.userDataConstraint = userDataConstraint;
    }
}
