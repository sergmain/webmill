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
 *         &lt;element ref="{}servlet-name"/>
 *         &lt;element ref="{}url-pattern"/>
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
    "servletName",
    "urlPattern"
})
@XmlRootElement(name = "servlet-mapping")
public class ServletMapping {

    @XmlElement(name = "servlet-name", required = true)
    protected ServletName servletName;
    @XmlElement(name = "url-pattern", required = true)
    protected UrlPattern urlPattern;
    @XmlAttribute
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;

    /**
     * Gets the value of the servletName property.
     * 
     * @return
     *     possible object is
     *     {@link ServletName }
     *     
     */
    public ServletName getServletName() {
        return servletName;
    }

    /**
     * Sets the value of the servletName property.
     * 
     * @param value
     *     allowed object is
     *     {@link ServletName }
     *     
     */
    public void setServletName(ServletName value) {
        this.servletName = value;
    }

    /**
     * Gets the value of the urlPattern property.
     * 
     * @return
     *     possible object is
     *     {@link UrlPattern }
     *     
     */
    public UrlPattern getUrlPattern() {
        return urlPattern;
    }

    /**
     * Sets the value of the urlPattern property.
     * 
     * @param value
     *     allowed object is
     *     {@link UrlPattern }
     *     
     */
    public void setUrlPattern(UrlPattern value) {
        this.urlPattern = value;
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