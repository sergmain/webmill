//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0.2-b01-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2006.09.19 at 03:00:30 PM MSD 
//


package org.riverock.portlet.google.sitemap.schema.siteindex;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.riverock.portlet.google.sitemap.schema.siteindex package. 
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

    private final static QName _Lastmod_QNAME = new QName("http://www.google.com/schemas/sitemap/0.84", "lastmod");
    private final static QName _Loc_QNAME = new QName("http://www.google.com/schemas/sitemap/0.84", "loc");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.riverock.portlet.google.sitemap.schema.siteindex
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Sitemap }
     *
     * @return Sitemap
     */
    public Sitemap createSitemap() {
        return new Sitemap();
    }

    /**
     * Create an instance of {@link Sitemapindex }
     *
     * @return Sitemapindex
     */
    public Sitemapindex createSitemapindex() {
        return new Sitemapindex();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     * @param value String
     * @return JAXBElement<String>
     */
    @XmlElementDecl(namespace = "http://www.google.com/schemas/sitemap/0.84", name = "lastmod")
    public JAXBElement<String> createLastmod(String value) {
        return new JAXBElement<String>(_Lastmod_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     * @param value String
     * @return JAXBElement<String>
     */
    @XmlElementDecl(namespace = "http://www.google.com/schemas/sitemap/0.84", name = "loc")
    public JAXBElement<String> createLoc(String value) {
        return new JAXBElement<String>(_Loc_QNAME, String.class, null, value);
    }

}
