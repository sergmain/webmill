//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0.2-b01-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2006.11.23 at 10:13:21 PM MSK 
//


package org.riverock.common.annotation.schema.transfer;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.riverock.generic.annotation.schema.transfer package. 
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

    private final static QName _TransferFileList_QNAME = new QName("", "TransferFileList");
    private final static QName _TransferFileConfig_QNAME = new QName("", "TransferFileConfig");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.riverock.generic.annotation.schema.transfer
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link TransferFileConfigType }
     * 
     */
    public TransferFileConfigType createTransferFileConfigType() {
        return new TransferFileConfigType();
    }

    /**
     * Create an instance of {@link TransferFileListType }
     * 
     */
    public TransferFileListType createTransferFileListType() {
        return new TransferFileListType();
    }

    /**
     * Create an instance of {@link TransferFileContentType }
     * 
     */
    public TransferFileContentType createTransferFileContentType() {
        return new TransferFileContentType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TransferFileListType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "TransferFileList")
    public JAXBElement<TransferFileListType> createTransferFileList(TransferFileListType value) {
        return new JAXBElement<TransferFileListType>(_TransferFileList_QNAME, TransferFileListType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TransferFileConfigType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "TransferFileConfig")
    public JAXBElement<TransferFileConfigType> createTransferFileConfig(TransferFileConfigType value) {
        return new JAXBElement<TransferFileConfigType>(_TransferFileConfig_QNAME, TransferFileConfigType.class, null, value);
    }

}
