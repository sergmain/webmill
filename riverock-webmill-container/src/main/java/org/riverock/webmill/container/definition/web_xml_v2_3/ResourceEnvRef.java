//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.1.4-b02-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.10.02 at 06:50:27 PM MSD 
//


package org.riverock.webmill.container.definition.web_xml_v2_3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}description" minOccurs="0"/>
 *         &lt;element ref="{}resource-env-ref-name"/>
 *         &lt;element ref="{}resource-env-ref-type"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}ID" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "description",
    "resourceEnvRefName",
    "resourceEnvRefType"
})
@XmlRootElement(name = "resource-env-ref")
public class ResourceEnvRef {

    protected Description description;
    @XmlElement(name = "resource-env-ref-name", required = true)
    protected ResourceEnvRefName resourceEnvRefName;
    @XmlElement(name = "resource-env-ref-type", required = true)
    protected ResourceEnvRefType resourceEnvRefType;
    @XmlAttribute
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link Description }
     *     
     */
    public Description getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link Description }
     *     
     */
    public void setDescription(Description value) {
        this.description = value;
    }

    /**
     * Gets the value of the resourceEnvRefName property.
     * 
     * @return
     *     possible object is
     *     {@link ResourceEnvRefName }
     *     
     */
    public ResourceEnvRefName getResourceEnvRefName() {
        return resourceEnvRefName;
    }

    /**
     * Sets the value of the resourceEnvRefName property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResourceEnvRefName }
     *     
     */
    public void setResourceEnvRefName(ResourceEnvRefName value) {
        this.resourceEnvRefName = value;
    }

    /**
     * Gets the value of the resourceEnvRefType property.
     * 
     * @return
     *     possible object is
     *     {@link ResourceEnvRefType }
     *     
     */
    public ResourceEnvRefType getResourceEnvRefType() {
        return resourceEnvRefType;
    }

    /**
     * Sets the value of the resourceEnvRefType property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResourceEnvRefType }
     *     
     */
    public void setResourceEnvRefType(ResourceEnvRefType value) {
        this.resourceEnvRefType = value;
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