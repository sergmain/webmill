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

//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0.2-b01-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2006.09.10 at 01:22:46 AM MSD 
//


package org.riverock.webmill.container.portlet_definition.portlet_api_v1;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for portlet-appType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="portlet-appType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="portlet" type="{http://java.sun.com/xml/ns/portlet/portlet-app_1_0.xsd}portletType" maxOccurs="unbounded"/>
 *         &lt;element name="custom-portlet-mode" type="{http://java.sun.com/xml/ns/portlet/portlet-app_1_0.xsd}custom-portlet-modeType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="custom-window-state" type="{http://java.sun.com/xml/ns/portlet/portlet-app_1_0.xsd}custom-window-stateType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="user-attribute" type="{http://java.sun.com/xml/ns/portlet/portlet-app_1_0.xsd}user-attributeType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="security-constraint" type="{http://java.sun.com/xml/ns/portlet/portlet-app_1_0.xsd}security-constraintType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="version" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "portlet-appType", propOrder = {
    "portlet",
    "customPortletMode",
    "customWindowState",
    "userAttribute",
    "securityConstraint"
})
public class PortletAppType {

    @XmlElement(required = true)
    protected List<PortletType> portlet;
    @XmlElement(name = "custom-portlet-mode")
    protected List<CustomPortletModeType> customPortletMode;
    @XmlElement(name = "custom-window-state")
    protected List<CustomWindowStateType> customWindowState;
    @XmlElement(name = "user-attribute")
    protected List<UserAttributeType> userAttribute;
    @XmlElement(name = "security-constraint")
    protected List<SecurityConstraintType> securityConstraint;
    @XmlAttribute
    protected String id;
    @XmlAttribute(required = true)
    protected String version;

    /**
     * Gets the value of the portlet property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the portlet property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPortlet().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PortletType }
     * 
     * 
     */
    public List<PortletType> getPortlet() {
        if (portlet == null) {
            portlet = new ArrayList<PortletType>();
        }
        return this.portlet;
    }

    /**
     * Gets the value of the customPortletMode property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the customPortletMode property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCustomPortletMode().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CustomPortletModeType }
     * 
     * 
     */
    public List<CustomPortletModeType> getCustomPortletMode() {
        if (customPortletMode == null) {
            customPortletMode = new ArrayList<CustomPortletModeType>();
        }
        return this.customPortletMode;
    }

    /**
     * Gets the value of the customWindowState property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the customWindowState property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCustomWindowState().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CustomWindowStateType }
     * 
     * 
     */
    public List<CustomWindowStateType> getCustomWindowState() {
        if (customWindowState == null) {
            customWindowState = new ArrayList<CustomWindowStateType>();
        }
        return this.customWindowState;
    }

    /**
     * Gets the value of the userAttribute property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the userAttribute property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUserAttribute().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link UserAttributeType }
     * 
     * 
     */
    public List<UserAttributeType> getUserAttribute() {
        if (userAttribute == null) {
            userAttribute = new ArrayList<UserAttributeType>();
        }
        return this.userAttribute;
    }

    /**
     * Gets the value of the securityConstraint property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the securityConstraint property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSecurityConstraint().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SecurityConstraintType }
     * 
     * 
     */
    public List<SecurityConstraintType> getSecurityConstraint() {
        if (securityConstraint == null) {
            securityConstraint = new ArrayList<SecurityConstraintType>();
        }
        return this.securityConstraint;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersion(String value) {
        this.version = value;
    }

}