//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0.2-b01-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2006.11.23 at 10:13:21 PM MSK 
//


package org.riverock.generic.annotation.schema.transfer;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TransferFileListType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TransferFileListType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="IsGzip" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="DateCreate" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="TransferFileContent" type="{}TransferFileContentType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TransferFileListType", propOrder = {
    "isGzip",
    "dateCreate",
    "transferFileContent"
})
public class TransferFileListType {

    @XmlElement(name = "IsGzip")
    protected boolean isGzip;
    @XmlElement(name = "DateCreate", required = true)
    protected String dateCreate;
    @XmlElement(name = "TransferFileContent")
    protected List<TransferFileContentType> transferFileContent;

    /**
     * Gets the value of the isGzip property.
     * 
     */
    public boolean isIsGzip() {
        return isGzip;
    }

    /**
     * Sets the value of the isGzip property.
     * 
     */
    public void setIsGzip(boolean value) {
        this.isGzip = value;
    }

    /**
     * Gets the value of the dateCreate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDateCreate() {
        return dateCreate;
    }

    /**
     * Sets the value of the dateCreate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDateCreate(String value) {
        this.dateCreate = value;
    }

    /**
     * Gets the value of the transferFileContent property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the transferFileContent property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTransferFileContent().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TransferFileContentType }
     * 
     * 
     */
    public List<TransferFileContentType> getTransferFileContent() {
        if (transferFileContent == null) {
            transferFileContent = new ArrayList<TransferFileContentType>();
        }
        return this.transferFileContent;
    }

}
