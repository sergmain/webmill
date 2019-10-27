//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0.5-b02-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.08.09 at 06:17:42 PM MSD 
//


package org.riverock.update.webmill.v580.convert_template.schema;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.riverock.update.webmill.v580.convert_template.schema package. 
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

    private final static QName _SiteTemplate_QNAME = new QName("", "SiteTemplate");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.riverock.update.webmill.v580.convert_template.schema
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ElementParameter }
     * 
     */
    public ElementParameter createElementParameter() {
        return new ElementParameter();
    }

    /**
     * Create an instance of {@link Parameter }
     * 
     */
    public Parameter createParameter() {
        return new Parameter();
    }

    /**
     * Create an instance of {@link SiteTemplateItem }
     * 
     */
    public SiteTemplateItem createSiteTemplateItem() {
        return new SiteTemplateItem();
    }

    /**
     * Create an instance of {@link Template }
     * 
     */
    public Template createTemplate() {
        return new Template();
    }

    /**
     * Create an instance of {@link Portlet }
     * 
     */
    public Portlet createPortlet() {
        return new Portlet();
    }

    /**
     * Create an instance of {@link SiteTemplate }
     * 
     */
    public SiteTemplate createSiteTemplate() {
        return new SiteTemplate();
    }

    /**
     * Create an instance of {@link Include }
     * 
     */
    public Include createInclude() {
        return new Include();
    }

    /**
     * Create an instance of {@link Xslt }
     * 
     */
    public Xslt createXslt() {
        return new Xslt();
    }

    /**
     * Create an instance of {@link Dynamic }
     * 
     */
    public Dynamic createDynamic() {
        return new Dynamic();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SiteTemplate }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "SiteTemplate")
    public JAXBElement<SiteTemplate> createSiteTemplate(SiteTemplate value) {
        return new JAXBElement<SiteTemplate>(_SiteTemplate_QNAME, SiteTemplate.class, null, value);
    }

}
