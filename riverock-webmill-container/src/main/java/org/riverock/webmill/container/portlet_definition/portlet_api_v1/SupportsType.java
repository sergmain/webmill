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
 * 
 * 			Supports indicates the portlet modes a 
 * 			portlet supports for a specific content type. All portlets must 
 * 			support the view mode. 
 * 			Used in: portlet
 * 			
 * 
 * <p>Java class for supportsType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="supportsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="mime-type" type="{http://java.sun.com/xml/ns/portlet/portlet-app_1_0.xsd}mime-typeType"/>
 *         &lt;element name="portlet-mode" type="{http://java.sun.com/xml/ns/portlet/portlet-app_1_0.xsd}portlet-modeType" maxOccurs="unbounded" minOccurs="0"/>
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
@XmlType(name = "supportsType", propOrder = {
    "mimeType",
    "portletMode"
})
public class SupportsType {

    @XmlElement(name = "mime-type", required = true)
    protected MimeTypeType mimeType;
    @XmlElement(name = "portlet-mode")
    protected List<PortletModeType> portletMode;
    @XmlAttribute
    protected String id;

    /**
     * Gets the value of the mimeType property.
     * 
     * @return
     *     possible object is
     *     {@link MimeTypeType }
     *     
     */
    public MimeTypeType getMimeType() {
        return mimeType;
    }

    /**
     * Sets the value of the mimeType property.
     * 
     * @param value
     *     allowed object is
     *     {@link MimeTypeType }
     *     
     */
    public void setMimeType(MimeTypeType value) {
        this.mimeType = value;
    }

    /**
     * Gets the value of the portletMode property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the portletMode property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPortletMode().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PortletModeType }
     * 
     * 
     */
    public List<PortletModeType> getPortletMode() {
        if (portletMode == null) {
            portletMode = new ArrayList<PortletModeType>();
        }
        return this.portletMode;
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