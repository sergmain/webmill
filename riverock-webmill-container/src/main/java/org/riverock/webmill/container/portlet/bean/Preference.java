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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Persistent preference values that may be used for customization
 * and personalization by the portlet.
 *
 * @version $Revision$ $Date$
 */
public class Preference implements Serializable {
    private static final long serialVersionUID = 30434672384237162L;


    /**
     * Field _id
     */
    private java.lang.String _id;

    /**
     * Field _name
     */
    private String _name;

    /**
     * Field _valueList
     */
    private Collection<String> _valueList = null;

    /**
     * Field _readOnly
     */
    private Boolean _readOnly = null;


    public Preference() {
    }

    public void setModifiable(String value) {
        if (value.equalsIgnoreCase("true") || value.equals("1") ) {
            _readOnly = Boolean.FALSE;
        }
        else {
            _readOnly = Boolean.TRUE;
        }
    }

    public void setValue(Collection<String> values) {
        this._valueList = values;
    }

/*
    public Iterator<String> getValue() {
        if (_valueList == null) {
            setValue(new ArrayList<String>());
        }

        return _valueList.iterator();
    }
*/

    public Collection<String> getValue() {
        return _valueList;
    }

    /**
     * Method addValue
     *
     * @param vValue
     */
    public void addValue(String vValue) {
        if (_valueList == null) {
            setValue(new ArrayList<String>());
        }

        _valueList.add(vValue);
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
     * Returns the value of field 'name'.
     *
     * @return the value of field 'name'.
     */
    public String getName() {
        return this._name;
    }

    /**
     * Returns the value of field 'readOnly'.
     *
     * @return the value of field 'readOnly'.
     */
    public Boolean getReadOnly() {
        return this._readOnly;
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
     * Sets the value of field 'name'.
     *
     * @param name the value of field 'name'.
     */
    public void setName(String name) {
        this._name = name;
    }

    /**
     * Sets the value of field 'readOnly'.
     *
     * @param readOnly the value of field 'readOnly'.
     */
    public void setReadOnly(Boolean readOnly) {
        this._readOnly = readOnly;
    }
}
