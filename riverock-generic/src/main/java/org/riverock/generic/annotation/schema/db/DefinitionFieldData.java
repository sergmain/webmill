//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0.2-b01-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2006.11.24 at 10:53:49 AM MSK 
//


package org.riverock.generic.annotation.schema.db;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for DefinitionFieldData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DefinitionFieldData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="TypeField">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="string"/>
 *               &lt;enumeration value="date"/>
 *               &lt;enumeration value="number"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;choice>
 *           &lt;element name="StringData" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *           &lt;element name="DateData" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *           &lt;element name="NumberData" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *       &lt;attribute name="isNull" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="nameField" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DefinitionFieldData", namespace = "", propOrder = {
    "typeField",
    "stringData",
    "dateData",
    "numberData"
})
public class DefinitionFieldData {

    @XmlElement(name = "TypeField", required = true)
    protected String typeField;
    @XmlElement(name = "StringData")
    protected String stringData;
    @XmlElement(name = "DateData")
    protected XMLGregorianCalendar dateData;
    @XmlElement(name = "NumberData")
    protected BigDecimal numberData;
    @XmlAttribute(required = true)
    protected boolean isNull;
    @XmlAttribute(required = true)
    protected String nameField;

    /**
     * Gets the value of the typeField property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTypeField() {
        return typeField;
    }

    /**
     * Sets the value of the typeField property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTypeField(String value) {
        this.typeField = value;
    }

    /**
     * Gets the value of the stringData property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStringData() {
        return stringData;
    }

    /**
     * Sets the value of the stringData property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStringData(String value) {
        this.stringData = value;
    }

    /**
     * Gets the value of the dateData property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateData() {
        return dateData;
    }

    /**
     * Sets the value of the dateData property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateData(XMLGregorianCalendar value) {
        this.dateData = value;
    }

    /**
     * Gets the value of the numberData property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getNumberData() {
        return numberData;
    }

    /**
     * Sets the value of the numberData property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setNumberData(BigDecimal value) {
        this.numberData = value;
    }

    /**
     * Gets the value of the isNull property.
     * 
     */
    public boolean isIsNull() {
        return isNull;
    }

    /**
     * Sets the value of the isNull property.
     * 
     */
    public void setIsNull(boolean value) {
        this.isNull = value;
    }

    /**
     * Gets the value of the nameField property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNameField() {
        return nameField;
    }

    /**
     * Sets the value of the nameField property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNameField(String value) {
        this.nameField = value;
    }

}