//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.1.4-b02-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.09.27 at 05:58:11 PM MSD 
//


package org.riverock.module.config.schema;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.riverock.module.config.schema package. 
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

    private final static QName _ActionConfig_QNAME = new QName("", "action-config");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.riverock.module.config.schema
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SecurityRole }
     * 
     */
    public SecurityRole createSecurityRole() {
        return new SecurityRole();
    }

    /**
     * Create an instance of {@link DefaultAction }
     * 
     */
    public DefaultAction createDefaultAction() {
        return new DefaultAction();
    }

    /**
     * Create an instance of {@link Forward }
     * 
     */
    public Forward createForward() {
        return new Forward();
    }

    /**
     * Create an instance of {@link ActionConfig }
     * 
     */
    public ActionConfig createActionConfig() {
        return new ActionConfig();
    }

    /**
     * Create an instance of {@link Action }
     * 
     */
    public Action createAction() {
        return new Action();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ActionConfig }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "action-config")
    public JAXBElement<ActionConfig> createActionConfig(ActionConfig value) {
        return new JAXBElement<ActionConfig>(_ActionConfig_QNAME, ActionConfig.class, null, value);
    }

}
