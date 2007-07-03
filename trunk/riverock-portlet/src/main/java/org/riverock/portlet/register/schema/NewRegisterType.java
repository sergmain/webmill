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
 * <p>Java class for NewRegisterType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="NewRegisterType">
 *   &lt;complexContent>
 *     &lt;extension base="{}BaseActionType">
 *       &lt;sequence>
 *         &lt;element name="Login" type="{}FieldType"/>
 *         &lt;element name="Password" type="{}FieldType"/>
 *         &lt;element name="PasswordRepeat" type="{}FieldType"/>
 *         &lt;element name="FirstName" type="{}FieldType"/>
 *         &lt;element name="LastName" type="{}FieldType"/>
 *         &lt;element name="MiddleName" type="{}FieldType"/>
 *         &lt;element name="Phone" type="{}FieldType"/>
 *         &lt;element name="Address" type="{}FieldType"/>
 *         &lt;element name="EMail" type="{}FieldType"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NewRegisterType", propOrder = {
    "login",
    "password",
    "passwordRepeat",
    "firstName",
    "lastName",
    "middleName",
    "phone",
    "address",
    "eMail"
})
public class NewRegisterType
    extends BaseActionType
{

    @XmlElement(name = "Login", required = true)
    protected FieldType login;
    @XmlElement(name = "Password", required = true)
    protected FieldType password;
    @XmlElement(name = "PasswordRepeat", required = true)
    protected FieldType passwordRepeat;
    @XmlElement(name = "FirstName", required = true)
    protected FieldType firstName;
    @XmlElement(name = "LastName", required = true)
    protected FieldType lastName;
    @XmlElement(name = "MiddleName", required = true)
    protected FieldType middleName;
    @XmlElement(name = "Phone", required = true)
    protected FieldType phone;
    @XmlElement(name = "Address", required = true)
    protected FieldType address;
    @XmlElement(name = "EMail", required = true)
    protected FieldType eMail;

    /**
     * Gets the value of the login property.
     * 
     * @return
     *     possible object is
     *     {@link FieldType }
     *     
     */
    public FieldType getLogin() {
        return login;
    }

    /**
     * Sets the value of the login property.
     * 
     * @param value
     *     allowed object is
     *     {@link FieldType }
     *     
     */
    public void setLogin(FieldType value) {
        this.login = value;
    }

    /**
     * Gets the value of the password property.
     * 
     * @return
     *     possible object is
     *     {@link FieldType }
     *     
     */
    public FieldType getPassword() {
        return password;
    }

    /**
     * Sets the value of the password property.
     * 
     * @param value
     *     allowed object is
     *     {@link FieldType }
     *     
     */
    public void setPassword(FieldType value) {
        this.password = value;
    }

    /**
     * Gets the value of the passwordRepeat property.
     * 
     * @return
     *     possible object is
     *     {@link FieldType }
     *     
     */
    public FieldType getPasswordRepeat() {
        return passwordRepeat;
    }

    /**
     * Sets the value of the passwordRepeat property.
     * 
     * @param value
     *     allowed object is
     *     {@link FieldType }
     *     
     */
    public void setPasswordRepeat(FieldType value) {
        this.passwordRepeat = value;
    }

    /**
     * Gets the value of the firstName property.
     * 
     * @return
     *     possible object is
     *     {@link FieldType }
     *     
     */
    public FieldType getFirstName() {
        return firstName;
    }

    /**
     * Sets the value of the firstName property.
     * 
     * @param value
     *     allowed object is
     *     {@link FieldType }
     *     
     */
    public void setFirstName(FieldType value) {
        this.firstName = value;
    }

    /**
     * Gets the value of the lastName property.
     * 
     * @return
     *     possible object is
     *     {@link FieldType }
     *     
     */
    public FieldType getLastName() {
        return lastName;
    }

    /**
     * Sets the value of the lastName property.
     * 
     * @param value
     *     allowed object is
     *     {@link FieldType }
     *     
     */
    public void setLastName(FieldType value) {
        this.lastName = value;
    }

    /**
     * Gets the value of the middleName property.
     * 
     * @return
     *     possible object is
     *     {@link FieldType }
     *     
     */
    public FieldType getMiddleName() {
        return middleName;
    }

    /**
     * Sets the value of the middleName property.
     * 
     * @param value
     *     allowed object is
     *     {@link FieldType }
     *     
     */
    public void setMiddleName(FieldType value) {
        this.middleName = value;
    }

    /**
     * Gets the value of the phone property.
     * 
     * @return
     *     possible object is
     *     {@link FieldType }
     *     
     */
    public FieldType getPhone() {
        return phone;
    }

    /**
     * Sets the value of the phone property.
     * 
     * @param value
     *     allowed object is
     *     {@link FieldType }
     *     
     */
    public void setPhone(FieldType value) {
        this.phone = value;
    }

    /**
     * Gets the value of the address property.
     * 
     * @return
     *     possible object is
     *     {@link FieldType }
     *     
     */
    public FieldType getAddress() {
        return address;
    }

    /**
     * Sets the value of the address property.
     * 
     * @param value
     *     allowed object is
     *     {@link FieldType }
     *     
     */
    public void setAddress(FieldType value) {
        this.address = value;
    }

    /**
     * Gets the value of the eMail property.
     * 
     * @return
     *     possible object is
     *     {@link FieldType }
     *     
     */
    public FieldType getEMail() {
        return eMail;
    }

    /**
     * Sets the value of the eMail property.
     * 
     * @param value
     *     allowed object is
     *     {@link FieldType }
     *     
     */
    public void setEMail(FieldType value) {
        this.eMail = value;
    }

}