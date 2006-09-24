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

import java.util.ArrayList;
import java.util.List;

import org.riverock.webmill.container.portlet.PortletContainer;
import org.riverock.webmill.portal.template.bean.types.PortalTemplateItemTypeImpl;
import org.riverock.interfaces.portal.template.PortalTemplateParameter;
import org.riverock.interfaces.portal.template.PortalTemplateItemType;
import org.riverock.interfaces.portal.template.PortalTemplateItem;

/**
 * Class TemplateItemType.
 *
 * @version $Revision$ $Date$
 */
public class PortalTemplateItemImpl implements PortalTemplateItem {

    /**
     * Field type
     */
    private PortalTemplateItemType portalTemplateItemType;

    /**
     * Field value
     */
    private String value;

    /**
     * Field code
     */
    private String code;

    /**
     * Field xmlRoot
     */
    private String xmlRoot;

    /**
     * Field isDisabled
     */
    private java.lang.Boolean isDisabled = false;

    /**
     * Field namespace
     */
    private String namespace;

    /**
     * Field role
     */
    private String role;

    /**
     * Field parameters
     */
    private List<PortalTemplateParameter> parameters;

    public PortalTemplateItemImpl() {
        super();
        parameters = new ArrayList<PortalTemplateParameter>();
    }

    /**
     * Method addParameter
     *
     * @param vParameter
     */
    public void addParameter(PortalTemplateParameter vParameter) {
        parameters.add(vParameter);
    }

    /**
     * Method clearParameter
     */
    public void clearParameter() {
        parameters.clear();
    }

    /**
     * Returns the value of field 'code'.
     *
     * @return the value of field 'code'.
     */
    public String getCode() {
        return this.code;
    }

    /**
     * Returns the value of field 'isDisabled'.
     *
     * @return the value of field 'isDisabled'.
     */
    public java.lang.Boolean getIsDisabled() {
        return this.isDisabled;
    }

    /**
     * Returns the value of field 'namespace'.
     *
     * @return the value of field 'namespace'.
     */
    public String getNamespace() {
        return this.namespace;
    }

    /**
     * Method getParameterAsReference
     * <p/>
     * Returns a reference to 'parameter'. No type checking is
     * performed on any modications to the Collection.
     *
     * @return returns a reference to the Collection.
     */
    public List<PortalTemplateParameter> getParameters() {
        return parameters;
    }

    /**
     * Returns the value of field 'type'.
     *
     * @return the value of field 'type'.
     */
    public String getType() {
        if (portalTemplateItemType==null) {
            return null;
        }

        return this.portalTemplateItemType.toString();
    }

    /**
     * Returns the value of field 'type'.
     *
     * @return the value of field 'type'.
     */
    public PortalTemplateItemType getTypeObject() {
        return this.portalTemplateItemType;
    }

    public String getValue() {
        return this.value;
    }

    public String getValueAsPortletName() {
        if ( value.indexOf( PortletContainer.PORTLET_ID_NAME_SEPARATOR )==-1 ) {
            return PortletContainer.PORTLET_ID_NAME_SEPARATOR + value;
        }
        else {
            return value;
        }
    }

    /**
     * Returns the value of field 'xmlRoot'.
     *
     * @return the value of field 'xmlRoot'.
     */
    public String getXmlRoot() {
        return this.xmlRoot;
    }

    /**
     * Sets the value of field 'code'.
     *
     * @param code the value of field 'code'.
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Sets the value of field 'isDisabled'.
     *
     * @param isDisabled the value of field 'isDisabled'.
     */
    public void setIsDisabled(java.lang.Boolean isDisabled) {
        this.isDisabled = isDisabled;
    }

    /**
     * Sets the value of field 'namespace'.
     *
     * @param namespace the value of field 'namespace'.
     */
    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    /**
     * Method setParameter
     * <p/>
     * Sets the value of 'parameter' by copying the given
     * ArrayList.
     *
     * @param parameterCollection the ArrayList to copy.
     */
    public void setParameters(List<PortalTemplateParameter> parameterCollection) {
        parameters.clear();
        parameters.addAll( parameterCollection );
    }

    /**
     * Sets the value of field 'type'.
     *
     * @param typePortalImpl the value of field 'type'.
     */
    public void setTypeObject(PortalTemplateItemTypeImpl typePortalImpl) {
        this.portalTemplateItemType = typePortalImpl;
    }

    /**
     * Sets the value of field 'type'.
     *
     * @param type the value of field 'type'.
     */
    public void setType(String type) {
        this.portalTemplateItemType = PortalTemplateItemTypeImpl.valueOf( type );
    }

    /**
     * Sets the value of field 'value'.
     *
     * @param value the value of field 'value'.
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Sets the value of field 'xmlRoot'.
     *
     * @param xmlRoot the value of field 'xmlRoot'.
     */
    public void setXmlRoot(String xmlRoot) {
        this.xmlRoot = xmlRoot;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
