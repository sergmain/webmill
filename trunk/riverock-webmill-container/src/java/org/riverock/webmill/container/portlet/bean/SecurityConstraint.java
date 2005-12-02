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
 * The security-constraintType is used to associate
 * intended security constraints with one or more portlets.
 * Used in: portlet-app
 *
 * @version $Revision$ $Date$
 */
public class SecurityConstraint implements Serializable {
    private static final long serialVersionUID = 30434672384237165L;


    /**
     * Field _id
     */
    private java.lang.String _id;

    /**
     * Field _displayNameList
     */
    private List<DisplayName> _displayNameList;

    /**
     * Field _portletCollection
     */
    private PortletCollection _portletCollection;

    /**
     * Field _userDataConstraint
     */
    private UserDataConstraint _userDataConstraint;


    public SecurityConstraint() {
        super();
        _displayNameList = new ArrayList<DisplayName>();
    }

    /**
     * Method addDisplayName
     *
     * @param vDisplayName
     */
    public void addDisplayName(DisplayName vDisplayName) {
        _displayNameList.add(vDisplayName);
    }

    /**
     * Method addDisplayName
     *
     * @param index
     * @param vDisplayName
     */
    public void addDisplayName(int index, DisplayName vDisplayName) {
        _displayNameList.add(index, vDisplayName);
    }

    /**
     * Method clearDisplayName
     */
    public void clearDisplayName() {
        _displayNameList.clear();
    }

    /**
     * Method getDisplayName
     *
     * @param index
     */
    public DisplayName getDisplayName(int index) {
        //-- check bounds for index
        if ((index < 0) || (index > _displayNameList.size())) {
            throw new IndexOutOfBoundsException();
        }

        return (DisplayName) _displayNameList.get(index);
    }

    /**
     * Method getDisplayName
     */
    public DisplayName[] getDisplayName() {
        int size = _displayNameList.size();
        DisplayName[] mArray = new DisplayName[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (DisplayName) _displayNameList.get(index);
        }
        return mArray;
    }

    /**
     * Method getDisplayNameCount
     */
    public int getDisplayNameCount() {
        return _displayNameList.size();
    }

    /**
     * Returns the value of field 'id'.
     *
     * @return the value of field 'id'.
     */
    public java.lang.String getId() {
        return this._id;
    }

    /**
     * Returns the value of field 'portletCollection'.
     *
     * @return the value of field 'portletCollection'.
     */
    public PortletCollection getPortletCollection() {
        return this._portletCollection;
    }

    /**
     * Returns the value of field 'userDataConstraint'.
     *
     * @return the value of field 'userDataConstraint'.
     */
    public UserDataConstraint getUserDataConstraint() {
        return this._userDataConstraint;
    }

    /**
     * Method removeDisplayName
     *
     * @param vDisplayName
     */
    public boolean removeDisplayName(DisplayName vDisplayName) {
        boolean removed = _displayNameList.remove(vDisplayName);
        return removed;
    }

    /**
     * Method setDisplayName
     *
     * @param index
     * @param vDisplayName
     */
    public void setDisplayName(int index, DisplayName vDisplayName) {
        //-- check bounds for index
        if ((index < 0) || (index > _displayNameList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _displayNameList.set(index, vDisplayName);
    }

    /**
     * Method setDisplayName
     *
     * @param displayNameArray
     */
    public void setDisplayName(DisplayName[] displayNameArray) {
        //-- copy array
        _displayNameList.clear();
        for (final DisplayName newVar : displayNameArray) {
            _displayNameList.add(newVar);
        }
    }

    /**
     * Sets the value of field 'id'.
     *
     * @param id the value of field 'id'.
     */
    public void setId(java.lang.String id) {
        this._id = id;
    }

    /**
     * Sets the value of field 'portletCollection'.
     *
     * @param portletCollection the value of field
     *                          'portletCollection'.
     */
    public void setPortletCollection(PortletCollection portletCollection) {
        this._portletCollection = portletCollection;
    }

    /**
     * Sets the value of field 'userDataConstraint'.
     *
     * @param userDataConstraint the value of field
     *                           'userDataConstraint'.
     */
    public void setUserDataConstraint(UserDataConstraint userDataConstraint) {
        this._userDataConstraint = userDataConstraint;
    }
}
