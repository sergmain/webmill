//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0.2-b01-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2006.11.24 at 10:53:49 AM MSK 
//


package org.riverock.generic.annotation.schema.db;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DefinitionAction complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DefinitionAction">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ActionType">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="CUSTOM_SQL"/>
 *               &lt;enumeration value="CUSTOM_CLASS_ACTION"/>
 *               &lt;enumeration value="CREATE_SEQUENCE"/>
 *               &lt;enumeration value="CREATE_TABLE"/>
 *               &lt;enumeration value="ADD_TABLE_COLUMN"/>
 *               &lt;enumeration value="DROP_TABLE_COLUMN"/>
 *               &lt;enumeration value="ADD_PRIMARY_KEY"/>
 *               &lt;enumeration value="ADD_FOREIGN_KEY"/>
 *               &lt;enumeration value="DROP_PRIMARY_KEY"/>
 *               &lt;enumeration value="DROP_FOREIGN_KEY"/>
 *               &lt;enumeration value="DROP_TABLE"/>
 *               &lt;enumeration value="DROP_SEQUENCE"/>
 *               &lt;enumeration value="DELETE_BEFORE_FK"/>
 *               &lt;enumeration value="COPY_COLUMN"/>
 *               &lt;enumeration value="CLONE_COLUMN"/>
 *               &lt;enumeration value="COPY_TABLE"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ActionParameters" type="{}DefinitionActionDataList" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="isSilensAction" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DefinitionAction", namespace = "", propOrder = {
    "actionType",
    "actionParameters"
})
public class DefinitionAction {

    @XmlElement(name = "ActionType", required = true)
    protected String actionType;
    @XmlElement(name = "ActionParameters")
    protected DefinitionActionDataList actionParameters;
    @XmlAttribute
    protected Boolean isSilensAction;

    /**
     * Gets the value of the actionType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getActionType() {
        return actionType;
    }

    /**
     * Sets the value of the actionType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setActionType(String value) {
        this.actionType = value;
    }

    /**
     * Gets the value of the actionParameters property.
     * 
     * @return
     *     possible object is
     *     {@link DefinitionActionDataList }
     *     
     */
    public DefinitionActionDataList getActionParameters() {
        return actionParameters;
    }

    /**
     * Sets the value of the actionParameters property.
     * 
     * @param value
     *     allowed object is
     *     {@link DefinitionActionDataList }
     *     
     */
    public void setActionParameters(DefinitionActionDataList value) {
        this.actionParameters = value;
    }

    /**
     * Gets the value of the isSilensAction property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isIsSilensAction() {
        if (isSilensAction == null) {
            return false;
        } else {
            return isSilensAction;
        }
    }

    /**
     * Sets the value of the isSilensAction property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsSilensAction(Boolean value) {
        this.isSilensAction = value;
    }

}