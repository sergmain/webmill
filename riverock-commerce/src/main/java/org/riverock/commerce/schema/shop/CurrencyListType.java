//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0.4-b01-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2006.11.26 at 03:57:27 PM MSK 
//


package org.riverock.commerce.schema.shop;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CurrencyListType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CurrencyListType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CurrencySwitchUrl" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="NoCurrencyName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="CurrencyNameSwitch" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="CurrencySelectParam" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="CurrencyItem" type="{}CurrencyItemType" maxOccurs="unbounded"/>
 *         &lt;element name="HiddenParam" type="{}HiddenParamType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CurrencyListType", propOrder = {
    "currencySwitchUrl",
    "noCurrencyName",
    "currencyNameSwitch",
    "currencySelectParam",
    "currencyItem",
    "hiddenParam"
})
public class CurrencyListType {

    @XmlElement(name = "CurrencySwitchUrl", required = true)
    protected String currencySwitchUrl;
    @XmlElement(name = "NoCurrencyName", required = true, defaultValue = "no currency")
    protected String noCurrencyName;
    @XmlElement(name = "CurrencyNameSwitch", required = true)
    protected String currencyNameSwitch;
    @XmlElement(name = "CurrencySelectParam", required = true)
    protected String currencySelectParam;
    @XmlElement(name = "CurrencyItem", required = true)
    protected List<CurrencyItemType> currencyItem;
    @XmlElement(name = "HiddenParam", required = true)
    protected List<HiddenParamType> hiddenParam;

    /**
     * Gets the value of the currencySwitchUrl property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurrencySwitchUrl() {
        return currencySwitchUrl;
    }

    /**
     * Sets the value of the currencySwitchUrl property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurrencySwitchUrl(String value) {
        this.currencySwitchUrl = value;
    }

    /**
     * Gets the value of the noCurrencyName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNoCurrencyName() {
        return noCurrencyName;
    }

    /**
     * Sets the value of the noCurrencyName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNoCurrencyName(String value) {
        this.noCurrencyName = value;
    }

    /**
     * Gets the value of the currencyNameSwitch property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurrencyNameSwitch() {
        return currencyNameSwitch;
    }

    /**
     * Sets the value of the currencyNameSwitch property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurrencyNameSwitch(String value) {
        this.currencyNameSwitch = value;
    }

    /**
     * Gets the value of the currencySelectParam property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurrencySelectParam() {
        return currencySelectParam;
    }

    /**
     * Sets the value of the currencySelectParam property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurrencySelectParam(String value) {
        this.currencySelectParam = value;
    }

    /**
     * Gets the value of the currencyItem property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the currencyItem property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCurrencyItem().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CurrencyItemType }
     * 
     * 
     */
    public List<CurrencyItemType> getCurrencyItem() {
        if (currencyItem == null) {
            currencyItem = new ArrayList<CurrencyItemType>();
        }
        return this.currencyItem;
    }

    /**
     * Gets the value of the hiddenParam property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the hiddenParam property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHiddenParam().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link HiddenParamType }
     * 
     * 
     */
    public List<HiddenParamType> getHiddenParam() {
        if (hiddenParam == null) {
            hiddenParam = new ArrayList<HiddenParamType>();
        }
        return this.hiddenParam;
    }

}
