//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.1.4-b02-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.10.02 at 06:50:27 PM MSD 
//


package org.riverock.webmill.container.definition.web_xml_v2_3;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}icon" minOccurs="0"/>
 *         &lt;element ref="{}servlet-name"/>
 *         &lt;element ref="{}display-name" minOccurs="0"/>
 *         &lt;element ref="{}description" minOccurs="0"/>
 *         &lt;choice>
 *           &lt;element ref="{}servlet-class"/>
 *           &lt;element ref="{}jsp-file"/>
 *         &lt;/choice>
 *         &lt;element ref="{}init-param" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}load-on-startup" minOccurs="0"/>
 *         &lt;element ref="{}run-as" minOccurs="0"/>
 *         &lt;element ref="{}security-role-ref" maxOccurs="unbounded" minOccurs="0"/>
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
@XmlType(name = "", propOrder = {
    "icon",
    "servletName",
    "displayName",
    "description",
    "servletClass",
    "jspFile",
    "initParam",
    "loadOnStartup",
    "runAs",
    "securityRoleRef"
})
@XmlRootElement(name = "servlet")
public class Servlet {

    protected Icon icon;
    @XmlElement(name = "servlet-name", required = true)
    protected ServletName servletName;
    @XmlElement(name = "display-name")
    protected DisplayName displayName;
    protected Description description;
    @XmlElement(name = "servlet-class")
    protected ServletClass servletClass;
    @XmlElement(name = "jsp-file")
    protected JspFile jspFile;
    @XmlElement(name = "init-param")
    protected List<InitParam> initParam;
    @XmlElement(name = "load-on-startup")
    protected LoadOnStartup loadOnStartup;
    @XmlElement(name = "run-as")
    protected RunAs runAs;
    @XmlElement(name = "security-role-ref")
    protected List<SecurityRoleRef> securityRoleRef;
    @XmlAttribute
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;

    /**
     * Gets the value of the icon property.
     * 
     * @return
     *     possible object is
     *     {@link Icon }
     *     
     */
    public Icon getIcon() {
        return icon;
    }

    /**
     * Sets the value of the icon property.
     * 
     * @param value
     *     allowed object is
     *     {@link Icon }
     *     
     */
    public void setIcon(Icon value) {
        this.icon = value;
    }

    /**
     * Gets the value of the servletName property.
     * 
     * @return
     *     possible object is
     *     {@link ServletName }
     *     
     */
    public ServletName getServletName() {
        return servletName;
    }

    /**
     * Sets the value of the servletName property.
     * 
     * @param value
     *     allowed object is
     *     {@link ServletName }
     *     
     */
    public void setServletName(ServletName value) {
        this.servletName = value;
    }

    /**
     * Gets the value of the displayName property.
     * 
     * @return
     *     possible object is
     *     {@link DisplayName }
     *     
     */
    public DisplayName getDisplayName() {
        return displayName;
    }

    /**
     * Sets the value of the displayName property.
     * 
     * @param value
     *     allowed object is
     *     {@link DisplayName }
     *     
     */
    public void setDisplayName(DisplayName value) {
        this.displayName = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link Description }
     *     
     */
    public Description getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link Description }
     *     
     */
    public void setDescription(Description value) {
        this.description = value;
    }

    /**
     * Gets the value of the servletClass property.
     * 
     * @return
     *     possible object is
     *     {@link ServletClass }
     *     
     */
    public ServletClass getServletClass() {
        return servletClass;
    }

    /**
     * Sets the value of the servletClass property.
     * 
     * @param value
     *     allowed object is
     *     {@link ServletClass }
     *     
     */
    public void setServletClass(ServletClass value) {
        this.servletClass = value;
    }

    /**
     * Gets the value of the jspFile property.
     * 
     * @return
     *     possible object is
     *     {@link JspFile }
     *     
     */
    public JspFile getJspFile() {
        return jspFile;
    }

    /**
     * Sets the value of the jspFile property.
     * 
     * @param value
     *     allowed object is
     *     {@link JspFile }
     *     
     */
    public void setJspFile(JspFile value) {
        this.jspFile = value;
    }

    /**
     * Gets the value of the initParam property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the initParam property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInitParam().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link InitParam }
     * 
     * 
     */
    public List<InitParam> getInitParam() {
        if (initParam == null) {
            initParam = new ArrayList<InitParam>();
        }
        return this.initParam;
    }

    /**
     * Gets the value of the loadOnStartup property.
     * 
     * @return
     *     possible object is
     *     {@link LoadOnStartup }
     *     
     */
    public LoadOnStartup getLoadOnStartup() {
        return loadOnStartup;
    }

    /**
     * Sets the value of the loadOnStartup property.
     * 
     * @param value
     *     allowed object is
     *     {@link LoadOnStartup }
     *     
     */
    public void setLoadOnStartup(LoadOnStartup value) {
        this.loadOnStartup = value;
    }

    /**
     * Gets the value of the runAs property.
     * 
     * @return
     *     possible object is
     *     {@link RunAs }
     *     
     */
    public RunAs getRunAs() {
        return runAs;
    }

    /**
     * Sets the value of the runAs property.
     * 
     * @param value
     *     allowed object is
     *     {@link RunAs }
     *     
     */
    public void setRunAs(RunAs value) {
        this.runAs = value;
    }

    /**
     * Gets the value of the securityRoleRef property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the securityRoleRef property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSecurityRoleRef().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SecurityRoleRef }
     * 
     * 
     */
    public List<SecurityRoleRef> getSecurityRoleRef() {
        if (securityRoleRef == null) {
            securityRoleRef = new ArrayList<SecurityRoleRef>();
        }
        return this.securityRoleRef;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

}
