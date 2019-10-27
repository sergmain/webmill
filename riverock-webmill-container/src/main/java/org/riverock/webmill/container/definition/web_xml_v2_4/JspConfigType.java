//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0.5-b02-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.07.03 at 06:32:53 PM MSD 
//


package org.riverock.webmill.container.definition.web_xml_v2_4;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * 
 * 
 * 	The jsp-configType is used to provide global configuration
 * 	information for the JSP files in a web application. It has
 * 	two subelements, taglib and jsp-property-group.
 * 
 *       
 * 
 * <p>Java class for jsp-configType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="jsp-configType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="taglib" type="{http://java.sun.com/xml/ns/j2ee}taglibType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="jsp-property-group" type="{http://java.sun.com/xml/ns/j2ee}jsp-property-groupType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}ID" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "jsp-configType", propOrder = {
    "taglib",
    "jspPropertyGroup"
})
public class JspConfigType {

    protected List<TaglibType> taglib;
    @XmlElement(name = "jsp-property-group")
    protected List<JspPropertyGroupType> jspPropertyGroup;
    @XmlAttribute
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    protected java.lang.String id;

    /**
     * Gets the value of the taglib property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the taglib property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTaglib().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TaglibType }
     * 
     * 
     */
    public List<TaglibType> getTaglib() {
        if (taglib == null) {
            taglib = new ArrayList<TaglibType>();
        }
        return this.taglib;
    }

    /**
     * Gets the value of the jspPropertyGroup property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the jspPropertyGroup property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getJspPropertyGroup().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JspPropertyGroupType }
     * 
     * 
     */
    public List<JspPropertyGroupType> getJspPropertyGroup() {
        if (jspPropertyGroup == null) {
            jspPropertyGroup = new ArrayList<JspPropertyGroupType>();
        }
        return this.jspPropertyGroup;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String }
     *     
     */
    public java.lang.String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String }
     *     
     */
    public void setId(java.lang.String value) {
        this.id = value;
    }

}
