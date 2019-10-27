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
 * <p>Java class for ItemListType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ItemListType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="PriceFieldName" type="{}PriceFieldNameType"/>
 *         &lt;element name="PriceItem" type="{}PriceItemType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ItemListType", propOrder = {
    "priceFieldName",
    "priceItem"
})
public class ItemListType {

    @XmlElement(name = "PriceFieldName", required = true)
    protected PriceFieldNameType priceFieldName;
    @XmlElement(name = "PriceItem")
    protected List<PriceItemType> priceItem;

    /**
     * Gets the value of the priceFieldName property.
     * 
     * @return
     *     possible object is
     *     {@link PriceFieldNameType }
     *     
     */
    public PriceFieldNameType getPriceFieldName() {
        return priceFieldName;
    }

    /**
     * Sets the value of the priceFieldName property.
     * 
     * @param value
     *     allowed object is
     *     {@link PriceFieldNameType }
     *     
     */
    public void setPriceFieldName(PriceFieldNameType value) {
        this.priceFieldName = value;
    }

    /**
     * Gets the value of the priceItem property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the priceItem property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPriceItem().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PriceItemType }
     * 
     * 
     */
    public List<PriceItemType> getPriceItem() {
        if (priceItem == null) {
            priceItem = new ArrayList<PriceItemType>();
        }
        return this.priceItem;
    }

}
