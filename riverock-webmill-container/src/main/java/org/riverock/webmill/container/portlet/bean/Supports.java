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
 * Supports indicates the portlet modes a
 * portlet supports for a specific content type. All portlets must
 * <p/>
 * support the view mode.
 * Used in: portlet
 *
 * @version $Revision$ $Date$
 */
public class Supports implements Serializable {
    private static final long serialVersionUID = 30434672384237169L;

    /**
     * Field id
     */
    private java.lang.String id;

    /**
     * Field mimeType
     */
    private String mimeType;

    /**
     * Field portletMode
     */
    private List<String> portletMode;


    public Supports() {
        super();
        portletMode = new ArrayList<String>();
    }


    /**
     * Method addPortletMode
     *
     * @param vPortletMode
     */
    public void addPortletMode(String vPortletMode) {
        portletMode.add(vPortletMode);
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
     * Returns the value of field 'mimeType'.
     *
     * @return the value of field 'mimeType'.
     */
    public String getMimeType() {
        return this.mimeType;
    }

    /**
     * Method getPortletMode
     */
    public List<String> getPortletMode() {
        return portletMode;
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
     * Sets the value of field 'mimeType'.
     *
     * @param mimeType the value of field 'mimeType'.
     */
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    /**
     * Method setPortletMode
     *
     * @param portletMode
     */
    public void setPortletMode(List<String> portletMode) {
        this.portletMode=portletMode;
    }
}
