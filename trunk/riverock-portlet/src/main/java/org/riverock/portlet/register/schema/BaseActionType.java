//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0.4-b01-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2006.11.25 at 01:17:29 AM MSK 
//


package org.riverock.portlet.register.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BaseActionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BaseActionType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ActionName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ActionTitle" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ActionButton" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BaseActionType", propOrder = {
    "actionName",
    "actionTitle",
    "actionButton"
})
public class BaseActionType {

    @XmlElement(name = "ActionName", required = true)
    protected String actionName;
    @XmlElement(name = "ActionTitle", required = true)
    protected String actionTitle;
    @XmlElement(name = "ActionButton", required = true)
    protected String actionButton;

    /**
     * Gets the value of the actionName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getActionName() {
        return actionName;
    }

    /**
     * Sets the value of the actionName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setActionName(String value) {
        this.actionName = value;
    }

    /**
     * Gets the value of the actionTitle property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getActionTitle() {
        return actionTitle;
    }

    /**
     * Sets the value of the actionTitle property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setActionTitle(String value) {
        this.actionTitle = value;
    }

    /**
     * Gets the value of the actionButton property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getActionButton() {
        return actionButton;
    }

    /**
     * Sets the value of the actionButton property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setActionButton(String value) {
        this.actionButton = value;
    }

}