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

