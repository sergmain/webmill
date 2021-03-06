//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0.4-b01-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2006.11.25 at 01:13:52 AM MSK 
//


package org.riverock.portlet.faq.schema;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for FaqGroupType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FaqGroupType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="FaqGroupName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="FaqItem" type="{}FaqItemType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FaqGroupType", propOrder = {
    "faqGroupName",
    "faqItem"
})
public class FaqGroupType {

    @XmlElement(name = "FaqGroupName", required = true)
    protected String faqGroupName;
    @XmlElement(name = "FaqItem")
    protected List<FaqItemType> faqItem;

    /**
     * Gets the value of the faqGroupName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFaqGroupName() {
        return faqGroupName;
    }

    /**
     * Sets the value of the faqGroupName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFaqGroupName(String value) {
        this.faqGroupName = value;
    }

    /**
     * Gets the value of the faqItem property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the faqItem property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFaqItem().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FaqItemType }
     * 
     * 
     */
    public List<FaqItemType> getFaqItem() {
        if (faqItem == null) {
            faqItem = new ArrayList<FaqItemType>();
        }
        return this.faqItem;
    }

}
