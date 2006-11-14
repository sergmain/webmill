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
package org.riverock.webmill.container.bean;

import java.io.Serializable;

/**
 * @author smaslyukov
 *         Date: 29.07.2005
 *         Time: 15:40:15
 *         $Id$
 */
public class SitePortletData implements Serializable {
    private static final long serialVersionUID = 30434672384237703L;

    /**
     * Field _data
     */
    private byte[] _data;

    /**
     * Field _isError
     */
    private Boolean _isError = Boolean.FALSE;

    /**
     * Field _isXml
     */
    private Boolean _isXml = Boolean.FALSE;


    public SitePortletData()
     {
        super();
    }

    /**
     * Returns the value of field 'data'.
     *
     * @return byte
     * @return the value of field 'data'.
     */
    public byte[] getData()
    {
        return this._data;
    }

    /**
     * Returns the value of field 'isError'.
     *
     * @return Boolean
     * @return the value of field 'isError'.
     */
    public java.lang.Boolean getIsError()
    {
        return this._isError;
    }

    /**
     * Returns the value of field 'isXml'.
     *
     * @return Boolean
     * @return the value of field 'isXml'.
     */
    public java.lang.Boolean getIsXml()
    {
        return this._isXml;
    }

    /**
     * Sets the value of field 'data'.
     *
     * @param data the value of field 'data'.
     */
    public void setData(byte[] data)
    {
        this._data = data;
    }

    /**
     * Sets the value of field 'isError'.
     *
     * @param isError the value of field 'isError'.
     */
    public void setIsError(java.lang.Boolean isError)
    {
        this._isError = isError;
    }

    /**
     * Sets the value of field 'isXml'.
     *
     * @param isXml the value of field 'isXml'.
     */
    public void setIsXml(java.lang.Boolean isXml)
    {
        this._isXml = isXml;
    }
}

