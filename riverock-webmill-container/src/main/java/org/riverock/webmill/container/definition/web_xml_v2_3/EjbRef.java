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
 *         &lt;element ref="{}ejb-ref-name"/>
 *         &lt;element ref="{}ejb-ref-type"/>
 *         &lt;element ref="{}home"/>
 *         &lt;element ref="{}remote"/>
 *         &lt;element ref="{}ejb-link" minOccurs="0"/>
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
    "ejbRefName",
    "ejbRefType",
    "home",
    "remote",
    "ejbLink"
})
@XmlRootElement(name = "ejb-ref")
public class EjbRef {

    protected Description description;
    @XmlElement(name = "ejb-ref-name", required = true)
    protected EjbRefName ejbRefName;
    @XmlElement(name = "ejb-ref-type", required = true)
    protected EjbRefType ejbRefType;
    @XmlElement(required = true)
    protected Home home;
    @XmlElement(required = true)
    protected Remote remote;
    @XmlElement(name = "ejb-link")
    protected EjbLink ejbLink;
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
     * Gets the value of the ejbRefName property.
     * 
     * @return
     *     possible object is
     *     {@link EjbRefName }
     *     
     */
    public EjbRefName getEjbRefName() {
        return ejbRefName;
    }

    /**
     * Sets the value of the ejbRefName property.
     * 
     * @param value
     *     allowed object is
     *     {@link EjbRefName }
     *     
     */
    public void setEjbRefName(EjbRefName value) {
        this.ejbRefName = value;
    }

    /**
     * Gets the value of the ejbRefType property.
     * 
     * @return
     *     possible object is
     *     {@link EjbRefType }
     *     
     */
    public EjbRefType getEjbRefType() {
        return ejbRefType;
    }

    /**
     * Sets the value of the ejbRefType property.
     * 
     * @param value
     *     allowed object is
     *     {@link EjbRefType }
     *     
     */
    public void setEjbRefType(EjbRefType value) {
        this.ejbRefType = value;
    }

    /**
     * Gets the value of the home property.
     * 
     * @return
     *     possible object is
     *     {@link Home }
     *     
     */
    public Home getHome() {
        return home;
    }

    /**
     * Sets the value of the home property.
     * 
     * @param value
     *     allowed object is
     *     {@link Home }
     *     
     */
    public void setHome(Home value) {
        this.home = value;
    }

    /**
     * Gets the value of the remote property.
     * 
     * @return
     *     possible object is
     *     {@link Remote }
     *     
     */
    public Remote getRemote() {
        return remote;
    }

    /**
     * Sets the value of the remote property.
     * 
     * @param value
     *     allowed object is
     *     {@link Remote }
     *     
     */
    public void setRemote(Remote value) {
        this.remote = value;
    }

    /**
     * Gets the value of the ejbLink property.
     * 
     * @return
     *     possible object is
     *     {@link EjbLink }
     *     
     */
    public EjbLink getEjbLink() {
        return ejbLink;
    }

    /**
     * Sets the value of the ejbLink property.
     * 
     * @param value
     *     allowed object is
     *     {@link EjbLink }
     *     
     */
    public void setEjbLink(EjbLink value) {
        this.ejbLink = value;
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