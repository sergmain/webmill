//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0.5-b02-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.07.03 at 06:32:53 PM MSD 
//


package org.riverock.webmill.container.definition.web_xml_v2_4;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * 
 * 
 * 	  The resource-env-refType is used to define
 * 	  resource-env-type elements.  It contains a declaration of a
 * 	  Deployment Component's reference to an administered object
 * 	  associated with a resource in the Deployment Component's
 * 	  environment.  It consists of an optional description, the
 * 	  resource environment reference name, and an indication of
 * 	  the resource environment reference type expected by the
 * 	  Deployment Component code.
 * 
 * 	  Example:
 * 
 * 	  <resource-env-ref>
 * 	      <resource-env-ref-name>jms/StockQueue
 * 	      </resource-env-ref-name>
 * 	      <resource-env-ref-type>javax.jms.Queue
 * 	      </resource-env-ref-type>
 * 	  </resource-env-ref>
 * 
 * 	  
 * 
 * <p>Java class for resource-env-refType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="resource-env-refType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="description" type="{http://java.sun.com/xml/ns/j2ee}descriptionType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="resource-env-ref-name" type="{http://java.sun.com/xml/ns/j2ee}jndi-nameType"/>
 *         &lt;element name="resource-env-ref-type" type="{http://java.sun.com/xml/ns/j2ee}fully-qualified-classType"/>
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
@XmlType(name = "resource-env-refType", propOrder = {
    "description",
    "resourceEnvRefName",
    "resourceEnvRefType"
})
public class ResourceEnvRefType {

    protected List<DescriptionType> description;
    @XmlElement(name = "resource-env-ref-name", required = true)
    protected JndiNameType resourceEnvRefName;
    @XmlElement(name = "resource-env-ref-type", required = true)
    protected FullyQualifiedClassType resourceEnvRefType;
    @XmlAttribute
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    protected java.lang.String id;

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
     * Gets the value of the resourceEnvRefName property.
     * 
     * @return
     *     possible object is
     *     {@link JndiNameType }
     *     
     */
    public JndiNameType getResourceEnvRefName() {
        return resourceEnvRefName;
    }

    /**
     * Sets the value of the resourceEnvRefName property.
     * 
     * @param value
     *     allowed object is
     *     {@link JndiNameType }
     *     
     */
    public void setResourceEnvRefName(JndiNameType value) {
        this.resourceEnvRefName = value;
    }

    /**
     * Gets the value of the resourceEnvRefType property.
     * 
     * @return
     *     possible object is
     *     {@link FullyQualifiedClassType }
     *     
     */
    public FullyQualifiedClassType getResourceEnvRefType() {
        return resourceEnvRefType;
    }

    /**
     * Sets the value of the resourceEnvRefType property.
     * 
     * @param value
     *     allowed object is
     *     {@link FullyQualifiedClassType }
     *     
     */
    public void setResourceEnvRefType(FullyQualifiedClassType value) {
        this.resourceEnvRefType = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String }
     *     
     */
    public java.lang.String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String }
     *     
     */
    public void setId(java.lang.String value) {
        this.id = value;
    }

}
