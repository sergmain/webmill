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
 * The portlet-collectionType is used to identify a subset
 * of portlets within a portlet application to which a
 * security constraint applies.
 * Used in: security-constraint
 *
 * @version $Revision$ $Date$
 */
public class PortletCollection implements Serializable {
    private static final long serialVersionUID = 30434672384237151L;


    /**
     * Field portletName
     */
    private List<String> portletName;


    public PortletCollection() {
        super();
        portletName = new ArrayList<String>();
    }

    /**
     * Method addPortletName
     *
     * @param vPortletName
     */
    public void addPortletName(String vPortletName) {
        portletName.add(vPortletName);
    }

    /**
     * Method getPortletName
     */
    public List<String> getPortletName() {
        return portletName;
    }

    /**
     * Method setPortletName
     *
     * @param portletName
     */
    public void setPortletName(List<String> portletName) {
        this.portletName=portletName;
    }
}
