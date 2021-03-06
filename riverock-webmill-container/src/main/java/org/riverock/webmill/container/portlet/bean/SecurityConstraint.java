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
 * The security-constraintType is used to associate
 * intended security constraints with one or more portlets.
 * Used in: portlet-app
 *
 * @version $Revision: 1055 $ $Date: 2006-11-14 17:56:15 +0000 (Tue, 14 Nov 2006) $
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
