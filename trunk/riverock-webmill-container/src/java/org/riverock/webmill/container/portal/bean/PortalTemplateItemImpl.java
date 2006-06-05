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
package org.riverock.webmill.container.portal.bean;

import java.util.ArrayList;
import java.util.List;

import org.riverock.webmill.container.portal.bean.types.PortalTemplateItemTypeImpl;
import org.riverock.webmill.container.portlet.PortletContainer;
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
}
