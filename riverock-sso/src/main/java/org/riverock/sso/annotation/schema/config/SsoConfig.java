//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0.4-b01-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2006.11.24 at 05:56:32 PM MSK 
//


package org.riverock.sso.annotation.schema.config;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SsoConfig complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SsoConfig">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SsoTempDir" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SsoDebugDir" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Auth" type="{}Auth"/>
 *       &lt;/sequence>
 *       &lt;attribute name="isDebugDirInit" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="isTempDirInit" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SsoConfig", propOrder = {
    "ssoTempDir",
    "ssoDebugDir",
    "auth"
})
public class SsoConfig {

    @XmlElement(name = "SsoTempDir", defaultValue = "\\\\tmp")
    protected String ssoTempDir;
    @XmlElement(name = "SsoDebugDir", defaultValue = "c:\\\\opt1")
    protected String ssoDebugDir;
    @XmlElement(name = "Auth", required = true)
    protected Auth auth;
    @XmlAttribute
    protected Boolean isDebugDirInit;
    @XmlAttribute
    protected Boolean isTempDirInit;

    /**
     * Gets the value of the ssoTempDir property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSsoTempDir() {
        return ssoTempDir;
    }

    /**
     * Sets the value of the ssoTempDir property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSsoTempDir(String value) {
        this.ssoTempDir = value;
    }

    /**
     * Gets the value of the ssoDebugDir property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSsoDebugDir() {
        return ssoDebugDir;
    }

    /**
     * Sets the value of the ssoDebugDir property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSsoDebugDir(String value) {
        this.ssoDebugDir = value;
    }

    /**
     * Gets the value of the auth property.
     * 
     * @return
     *     possible object is
     *     {@link Auth }
     *     
     */
    public Auth getAuth() {
        return auth;
    }

    /**
     * Sets the value of the auth property.
     * 
     * @param value
     *     allowed object is
     *     {@link Auth }
     *     
     */
    public void setAuth(Auth value) {
        this.auth = value;
    }

    /**
     * Gets the value of the isDebugDirInit property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isIsDebugDirInit() {
        if (isDebugDirInit == null) {
            return false;
        } else {
            return isDebugDirInit;
        }
    }

    /**
     * Sets the value of the isDebugDirInit property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsDebugDirInit(Boolean value) {
        this.isDebugDirInit = value;
    }

    /**
     * Gets the value of the isTempDirInit property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isIsTempDirInit() {
        if (isTempDirInit == null) {
            return false;
        } else {
            return isTempDirInit;
        }
    }

    /**
     * Sets the value of the isTempDirInit property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsTempDirInit(Boolean value) {
        this.isTempDirInit = value;
    }

}
