//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0.4-b01-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2006.11.26 at 03:57:27 PM MSK 
//


package org.riverock.commerce.schema.shop;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for HiddenParamType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="HiddenParamType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="HiddenParamName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="HiddenParamValue" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "HiddenParamType", propOrder = {
    "hiddenParamName",
    "hiddenParamValue"
})
public class HiddenParamType {

    @XmlElement(name = "HiddenParamName", required = true)
    protected String hiddenParamName;
    @XmlElement(name = "HiddenParamValue", required = true)
    protected String hiddenParamValue;

    /**
     * Gets the value of the hiddenParamName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHiddenParamName() {
        return hiddenParamName;
    }

    /**
     * Sets the value of the hiddenParamName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHiddenParamName(String value) {
        this.hiddenParamName = value;
    }

    /**
     * Gets the value of the hiddenParamValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHiddenParamValue() {
        return hiddenParamValue;
    }

    /**
     * Sets the value of the hiddenParamValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHiddenParamValue(String value) {
        this.hiddenParamValue = value;
    }

}
