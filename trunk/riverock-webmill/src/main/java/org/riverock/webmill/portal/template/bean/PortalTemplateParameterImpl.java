/*
 * org.riverock.webmill - Portal framework implementation
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
package org.riverock.webmill.portal.template.bean;

import org.riverock.interfaces.portal.template.PortalTemplateParameter;

/**
 * Class SiteTemplateParameterType.
 *
 * @version $Revision$ $Date$
 */
public class PortalTemplateParameterImpl implements PortalTemplateParameter {

    /**
     * Field _name
     */
    private java.lang.String _name;

    /**
     * Field _value
     */
    private java.lang.String _value;

    public PortalTemplateParameterImpl() {
        super();
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
     * Returns the value of field 'value'.
     *
     * @return the value of field 'value'.
     */
    public String getValue() {
        return this._value;
    }

    /**
     * Sets the value of field 'name'.
     *
     * @param name the value of field 'name'.
     */
    public void setName(java.lang.String name) {
        this._name = name;
    } //-- void setName(java.lang.String) 

    /**
     * Sets the value of field 'value'.
     *
     * @param value the value of field 'value'.
     */
    public void setValue(java.lang.String value) {
        this._value = value;
    } //-- void setValue(java.lang.String) 
}
