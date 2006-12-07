//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0.4-b01-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2006.11.26 at 03:57:27 PM MSK 
//


package org.riverock.commerce.schema.shop;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CurrencyItemType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CurrencyItemType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CurrencyName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="CurrencyID" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="CurrencyCurs" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *       &lt;/sequence>
 *       &lt;attribute name="selectCurrentCurrency" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CurrencyItemType", propOrder = {
    "currencyName",
    "currencyID",
    "currencyCurs"
})
public class CurrencyItemType {

    @XmlElement(name = "CurrencyName", required = true)
    protected String currencyName;
    @XmlElement(name = "CurrencyID")
    protected long currencyID;
    @XmlElement(name = "CurrencyCurs")
    protected BigDecimal currencyCurs;
    @XmlAttribute
    protected String selectCurrentCurrency;

    /**
     * Gets the value of the currencyName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurrencyName() {
        return currencyName;
    }

    /**
     * Sets the value of the currencyName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurrencyName(String value) {
        this.currencyName = value;
    }

    /**
     * Gets the value of the currencyID property.
     * 
     */
    public long getCurrencyID() {
        return currencyID;
    }

    /**
     * Sets the value of the currencyID property.
     * 
     */
    public void setCurrencyID(long value) {
        this.currencyID = value;
    }

    /**
     * Gets the value of the currencyCurs property.
     * 
     */
    public BigDecimal getCurrencyCurs() {
        return currencyCurs;
    }

    /**
     * Sets the value of the currencyCurs property.
     * 
     */
    public void setCurrencyCurs(BigDecimal value) {
        this.currencyCurs = value;
    }

    /**
     * Gets the value of the selectCurrentCurrency property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSelectCurrentCurrency() {
        return selectCurrentCurrency;
    }

    /**
     * Sets the value of the selectCurrentCurrency property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSelectCurrentCurrency(String value) {
        this.selectCurrentCurrency = value;
    }

}
