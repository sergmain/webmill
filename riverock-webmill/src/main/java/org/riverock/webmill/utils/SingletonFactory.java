package org.riverock.webmill.utils;

import javax.xml.bind.ValidationEventHandler;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

/**
 * User: SMaslyukov
 * Date: 21.08.2007
 * Time: 16:48:22
 */
public class SingletonFactory {
    final static NamespacePrefixMapper namespacePrefixMapper = new NamespacePrefixMapperImpl();
    final static ValidationEventHandler validationEventHandler = new JaxbValidationEventHandler();

    public static NamespacePrefixMapper getNamespacePrefixMapper() {
        return namespacePrefixMapper;
    }

    public static ValidationEventHandler getValidationEventHandler() {
        return validationEventHandler;
    }
}
