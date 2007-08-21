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
 * <p>Java class for PriceFieldNameType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PriceFieldNameType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="NameItem" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="NamePrice" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="NameCurrency" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="NameToInvoice" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PriceFieldNameType", propOrder = {
    "nameItem",
    "namePrice",
    "nameCurrency",
    "nameToInvoice"
})
public class PriceFieldNameType {

    @XmlElement(name = "NameItem", required = true, defaultValue = "Item")
    protected String nameItem;
    @XmlElement(name = "NamePrice", required = true, defaultValue = "Price")
    protected String namePrice;
    @XmlElement(name = "NameCurrency", required = true, defaultValue = "Currency")
    protected String nameCurrency;
    @XmlElement(name = "NameToInvoice", required = true)
    protected String nameToInvoice;

    /**
     * Gets the value of the nameItem property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNameItem() {
        return nameItem;
    }

    /**
     * Sets the value of the nameItem property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNameItem(String value) {
        this.nameItem = value;
    }

    /**
     * Gets the value of the namePrice property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNamePrice() {
        return namePrice;
    }

    /**
     * Sets the value of the namePrice property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNamePrice(String value) {
        this.namePrice = value;
    }

    /**
     * Gets the value of the nameCurrency property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNameCurrency() {
        return nameCurrency;
    }

    /**
     * Sets the value of the nameCurrency property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNameCurrency(String value) {
        this.nameCurrency = value;
    }

    /**
     * Gets the value of the nameToInvoice property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNameToInvoice() {
        return nameToInvoice;
    }

    /**
     * Sets the value of the nameToInvoice property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNameToInvoice(String value) {
        this.nameToInvoice = value;
    }

}