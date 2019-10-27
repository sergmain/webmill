//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0.4-b01-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2006.11.25 at 01:15:26 AM MSK 
//


package org.riverock.portlet.menu.schema;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MenuSimpleType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MenuSimpleType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="MenuModule" type="{}MenuModuleType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="MenuName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MenuSimpleType", propOrder = {
    "menuModule",
    "menuName"
})
public class MenuSimpleType {

    @XmlElement(name = "MenuModule")
    protected List<MenuModuleType> menuModule;
    @XmlElement(name = "MenuName")
    protected String menuName;

    /**
     * Gets the value of the menuModule property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the menuModule property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMenuModule().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MenuModuleType }
     * 
     * 
     */
    public List<MenuModuleType> getMenuModule() {
        if (menuModule == null) {
            menuModule = new ArrayList<MenuModuleType>();
        }
        return this.menuModule;
    }

    /**
     * Gets the value of the menuName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMenuName() {
        return menuName;
    }

    /**
     * Sets the value of the menuName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMenuName(String value) {
        this.menuName = value;
    }

}
