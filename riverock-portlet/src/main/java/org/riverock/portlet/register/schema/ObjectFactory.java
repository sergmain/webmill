//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0.4-b01-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2006.11.25 at 01:17:29 AM MSK 
//


package org.riverock.portlet.register.schema;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.riverock.portlet.register.schema package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Register_QNAME = new QName("", "Register");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.riverock.portlet.register.schema
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link BaseActionType }
     * 
     */
    public BaseActionType createBaseActionType() {
        return new BaseActionType();
    }

    /**
     * Create an instance of {@link FieldType }
     * 
     */
    public FieldType createFieldType() {
        return new FieldType();
    }

    /**
     * Create an instance of {@link NewRegisterType }
     * 
     */
    public NewRegisterType createNewRegisterType() {
        return new NewRegisterType();
    }

    /**
     * Create an instance of {@link SendPasswordType }
     * 
     */
    public SendPasswordType createSendPasswordType() {
        return new SendPasswordType();
    }

    /**
     * Create an instance of {@link RegisterType }
     * 
     */
    public RegisterType createRegisterType() {
        return new RegisterType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RegisterType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "Register")
    public JAXBElement<RegisterType> createRegister(RegisterType value) {
        return new JAXBElement<RegisterType>(_Register_QNAME, RegisterType.class, null, value);
    }

}