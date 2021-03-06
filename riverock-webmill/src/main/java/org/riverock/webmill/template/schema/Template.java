//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0.5-b02-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.08.13 at 09:28:43 PM MSD 
//


package org.riverock.webmill.template.schema;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element ref="{}html"/>
 *         &lt;choice maxOccurs="unbounded">
 *           &lt;element ref="{http://webmill.riverock.org/xsd/riverock-template-page-elements.xsd}portlet"/>
 *           &lt;element ref="{http://webmill.riverock.org/xsd/riverock-template-page-elements.xsd}dynamic"/>
 *           &lt;element ref="{http://webmill.riverock.org/xsd/riverock-template-page-elements.xsd}xslt"/>
 *           &lt;element ref="{http://webmill.riverock.org/xsd/riverock-template-page-elements.xsd}include"/>
 *         &lt;/choice>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "html",
    "portletOrDynamicOrXslt"
})
@XmlRootElement(name = "Template")
public class Template {

    protected Html html;
    @XmlElements({
        @XmlElement(name = "portlet", namespace = "http://webmill.riverock.org/xsd/riverock-template-page-elements.xsd", type = Portlet.class),
        @XmlElement(name = "xslt", namespace = "http://webmill.riverock.org/xsd/riverock-template-page-elements.xsd", type = Xslt.class),
        @XmlElement(name = "include", namespace = "http://webmill.riverock.org/xsd/riverock-template-page-elements.xsd", type = Include.class),
        @XmlElement(name = "dynamic", namespace = "http://webmill.riverock.org/xsd/riverock-template-page-elements.xsd", type = Dynamic.class)
    })
    protected List<java.lang.Object> portletOrDynamicOrXslt;

    /**
     * Gets the value of the html property.
     * 
     * @return
     *     possible object is
     *     {@link Html }
     *     
     */
    public Html getHtml() {
        return html;
    }

    /**
     * Sets the value of the html property.
     * 
     * @param value
     *     allowed object is
     *     {@link Html }
     *     
     */
    public void setHtml(Html value) {
        this.html = value;
    }

    /**
     * Gets the value of the portletOrDynamicOrXslt property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the portletOrDynamicOrXslt property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPortletOrDynamicOrXslt().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Portlet }
     * {@link Xslt }
     * {@link Include }
     * {@link Dynamic }
     * 
     * 
     */
    public List<java.lang.Object> getPortletOrDynamicOrXslt() {
        if (portletOrDynamicOrXslt == null) {
            portletOrDynamicOrXslt = new ArrayList<java.lang.Object>();
        }
        return this.portletOrDynamicOrXslt;
    }

}
