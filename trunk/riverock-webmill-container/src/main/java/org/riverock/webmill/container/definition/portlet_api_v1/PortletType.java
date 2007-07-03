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

//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0.2-b01-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2006.09.10 at 01:22:46 AM MSD 
//


package org.riverock.webmill.container.definition.portlet_api_v1;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 			The portlet element contains the declarative data of a portlet. 
 * 			Used in: portlet-app
 * 			
 * 
 * <p>Java class for portletType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="portletType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="description" type="{http://java.sun.com/xml/ns/portlet/portlet-app_1_0.xsd}descriptionType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="portlet-name" type="{http://java.sun.com/xml/ns/portlet/portlet-app_1_0.xsd}portlet-nameType"/>
 *         &lt;element name="display-name" type="{http://java.sun.com/xml/ns/portlet/portlet-app_1_0.xsd}display-nameType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="portlet-class" type="{http://java.sun.com/xml/ns/portlet/portlet-app_1_0.xsd}portlet-classType"/>
 *         &lt;element name="init-param" type="{http://java.sun.com/xml/ns/portlet/portlet-app_1_0.xsd}init-paramType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="expiration-cache" type="{http://java.sun.com/xml/ns/portlet/portlet-app_1_0.xsd}expiration-cacheType" minOccurs="0"/>
 *         &lt;element name="supports" type="{http://java.sun.com/xml/ns/portlet/portlet-app_1_0.xsd}supportsType" maxOccurs="unbounded"/>
 *         &lt;element name="supported-locale" type="{http://java.sun.com/xml/ns/portlet/portlet-app_1_0.xsd}supported-localeType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="resource-bundle" type="{http://java.sun.com/xml/ns/portlet/portlet-app_1_0.xsd}resource-bundleType"/>
 *         &lt;element name="portlet-info" type="{http://java.sun.com/xml/ns/portlet/portlet-app_1_0.xsd}portlet-infoType" minOccurs="0"/>
 *         &lt;element name="portlet-preferences" type="{http://java.sun.com/xml/ns/portlet/portlet-app_1_0.xsd}portlet-preferencesType" minOccurs="0"/>
 *         &lt;element name="security-role-ref" type="{http://java.sun.com/xml/ns/portlet/portlet-app_1_0.xsd}security-role-refType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "portletType", propOrder = {
    "description",
    "portletName",
    "displayName",
    "portletClass",
    "initParam",
    "expirationCache",
    "supports",
    "supportedLocale",
    "resourceBundle",
    "portletInfo",
    "portletPreferences",
    "securityRoleRef"
})
public class PortletType {

    protected List<DescriptionType> description;
    @XmlElement(name = "portlet-name", required = true)
    protected PortletNameType portletName;
    @XmlElement(name = "display-name")
    protected List<DisplayNameType> displayName;
    @XmlElement(name = "portlet-class", required = true)
    protected String portletClass;
    @XmlElement(name = "init-param")
    protected List<InitParamType> initParam;
    @XmlElement(name = "expiration-cache")
    protected ExpirationCacheType expirationCache;
    @XmlElement(required = true)
    protected List<SupportsType> supports;
    @XmlElement(name = "supported-locale")
    protected List<SupportedLocaleType> supportedLocale;
    @XmlElement(name = "resource-bundle", required = true)
    protected ResourceBundleType resourceBundle;
    @XmlElement(name = "portlet-info")
    protected PortletInfoType portletInfo;
    @XmlElement(name = "portlet-preferences")
    protected PortletPreferencesType portletPreferences;
    @XmlElement(name = "security-role-ref")
    protected List<SecurityRoleRefType> securityRoleRef;
    @XmlAttribute
    protected String id;

    /**
     * Gets the value of the description property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the description property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDescription().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DescriptionType }
     * 
     * 
     */
    public List<DescriptionType> getDescription() {
        if (description == null) {
            description = new ArrayList<DescriptionType>();
        }
        return this.description;
    }

    /**
     * Gets the value of the portletName property.
     * 
     * @return
     *     possible object is
     *     {@link PortletNameType }
     *     
     */
    public PortletNameType getPortletName() {
        return portletName;
    }

    /**
     * Sets the value of the portletName property.
     * 
     * @param value
     *     allowed object is
     *     {@link PortletNameType }
     *     
     */
    public void setPortletName(PortletNameType value) {
        this.portletName = value;
    }

    /**
     * Gets the value of the displayName property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the displayName property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDisplayName().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DisplayNameType }
     * 
     * 
     */
    public List<DisplayNameType> getDisplayName() {
        if (displayName == null) {
            displayName = new ArrayList<DisplayNameType>();
        }
        return this.displayName;
    }

    /**
     * Gets the value of the portletClass property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPortletClass() {
        return portletClass;
    }

    /**
     * Sets the value of the portletClass property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPortletClass(String value) {
        this.portletClass = value;
    }

    /**
     * Gets the value of the initParam property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the initParam property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInitParam().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link InitParamType }
     * 
     * 
     */
    public List<InitParamType> getInitParam() {
        if (initParam == null) {
            initParam = new ArrayList<InitParamType>();
        }
        return this.initParam;
    }

    /**
     * Gets the value of the expirationCache property.
     * 
     * @return
     *     possible object is
     *     {@link ExpirationCacheType }
     *     
     */
    public ExpirationCacheType getExpirationCache() {
        return expirationCache;
    }

    /**
     * Sets the value of the expirationCache property.
     * 
     * @param value
     *     allowed object is
     *     {@link ExpirationCacheType }
     *     
     */
    public void setExpirationCache(ExpirationCacheType value) {
        this.expirationCache = value;
    }

    /**
     * Gets the value of the supports property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the supports property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSupports().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SupportsType }
     * 
     * 
     */
    public List<SupportsType> getSupports() {
        if (supports == null) {
            supports = new ArrayList<SupportsType>();
        }
        return this.supports;
    }

    /**
     * Gets the value of the supportedLocale property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the supportedLocale property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSupportedLocale().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SupportedLocaleType }
     * 
     * 
     */
    public List<SupportedLocaleType> getSupportedLocale() {
        if (supportedLocale == null) {
            supportedLocale = new ArrayList<SupportedLocaleType>();
        }
        return this.supportedLocale;
    }

    /**
     * Gets the value of the resourceBundle property.
     * 
     * @return
     *     possible object is
     *     {@link ResourceBundleType }
     *     
     */
    public ResourceBundleType getResourceBundle() {
        return resourceBundle;
    }

    /**
     * Sets the value of the resourceBundle property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResourceBundleType }
     *     
     */
    public void setResourceBundle(ResourceBundleType value) {
        this.resourceBundle = value;
    }

    /**
     * Gets the value of the portletInfo property.
     * 
     * @return
     *     possible object is
     *     {@link PortletInfoType }
     *     
     */
    public PortletInfoType getPortletInfo() {
        return portletInfo;
    }

    /**
     * Sets the value of the portletInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link PortletInfoType }
     *     
     */
    public void setPortletInfo(PortletInfoType value) {
        this.portletInfo = value;
    }

    /**
     * Gets the value of the portletPreferences property.
     * 
     * @return
     *     possible object is
     *     {@link PortletPreferencesType }
     *     
     */
    public PortletPreferencesType getPortletPreferences() {
        return portletPreferences;
    }

    /**
     * Sets the value of the portletPreferences property.
     * 
     * @param value
     *     allowed object is
     *     {@link PortletPreferencesType }
     *     
     */
    public void setPortletPreferences(PortletPreferencesType value) {
        this.portletPreferences = value;
    }

    /**
     * Gets the value of the securityRoleRef property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the securityRoleRef property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSecurityRoleRef().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SecurityRoleRefType }
     * 
     * 
     */
    public List<SecurityRoleRefType> getSecurityRoleRef() {
        if (securityRoleRef == null) {
            securityRoleRef = new ArrayList<SecurityRoleRefType>();
        }
        return this.securityRoleRef;
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

}
