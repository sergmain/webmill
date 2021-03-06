//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0.4-b01-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2006.12.02 at 04:57:58 PM MSK 
//


package org.riverock.commerce.schema.import_price;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.riverock.commerce.schema.import_price package. 
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

    private final static QName _Prices_QNAME = new QName("", "Prices");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.riverock.commerce.schema.import_price
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link PricesType }
     * 
     */
    public PricesType createPricesType() {
        return new PricesType();
    }

    /**
     * Create an instance of {@link PriceListItemType }
     * 
     */
    public PriceListItemType createPriceListItemType() {
        return new PriceListItemType();
    }

    /**
     * Create an instance of {@link PriceListType }
     * 
     */
    public PriceListType createPriceListType() {
        return new PriceListType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PricesType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "Prices")
    public JAXBElement<PricesType> createPrices(PricesType value) {
        return new JAXBElement<PricesType>(_Prices_QNAME, PricesType.class, null, value);
    }

}
