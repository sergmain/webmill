/*
 * org.riverock.generic - Database connectivity classes, part of Webmill portal
 * For more information about Webmill portal, please visit project site
 * http://webmill.askmore.info
 *
 * Copyright (C) 2000-2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package org.riverock.common.tools;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.StringReader;
import java.io.InputStream;
import java.io.File;
import java.io.OutputStream;
import java.io.FileNotFoundException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.JAXBException;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

/**
 * Author: mill
 * Date: Jan 22, 2003
 * Time: 9:33:19 AM
 * <p/>
 * $Id$
 */
public final class XmlTools {
    private final static Logger log = Logger.getLogger(XmlTools.class);

    public XmlTools() {
    }

    public static <T> T getObjectFromXml(final Class<T> classType, final String str) throws JAXBException {
        return getObjectFromXml(classType, new StreamSource(new StringReader(str)), null);
    }

    public static <T> T getObjectFromXml(final Class<T> classType, InputStream is) throws JAXBException {
        return getObjectFromXml(classType, new StreamSource(is), null);
    }

    public static <T> T getObjectFromXml(final Class<T> classType, InputStream is, ValidationEventHandler handler) throws JAXBException {
        return getObjectFromXml(classType, new StreamSource(is), handler);
    }

    public static <T> T getObjectFromXml(final Class<T> classType, Source inSrc, ValidationEventHandler handler) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance ( classType.getPackage().getName() );
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        if (handler!=null) {
            unmarshaller.setEventHandler(handler);
        }

        return unmarshaller.unmarshal(inSrc, classType).getValue();
    }

    public static String getXmlAsString(final Object obj, final String rootElement) throws JAXBException {
        if (log.isDebugEnabled()) {
            byte b[] = getXml(obj, rootElement);
            log.debug("new String(b) " + new String(b));
            return new String(b);
        }
        else {
            return new String(getXml(obj, rootElement));
        }
    }

    public static byte[] getXml(final Object obj, final String rootElement) throws JAXBException {
        return getXml(obj, rootElement, "utf-8");
    }

    public static byte[] getXml(final Object obj, final String rootElement, final String encoding) throws JAXBException {
        if (log.isDebugEnabled()) {
            log.debug("getXml(). Object to marshaling " + obj);
            log.debug("getXml(). rootElement " + rootElement);
            log.debug("getXml(). encoding " + encoding);
        }
        return getXml(obj, rootElement, encoding, false, null);
    }

    /**
     *
     * @param obj
     * @param rootElement
     * @param encoding
     * @param isIndent
     * @param namespacePrefixMappers
     * @return
     * @throws JAXBException
     */
    public static byte[] getXml(final Object obj, final String rootElement, final String encoding, boolean isIndent, NamespacePrefixMapper[] namespacePrefixMappers) throws JAXBException {
        if (log.isDebugEnabled()) {
            log.debug("getXml(). Object to marshaling " + obj);
            log.debug("getXml(). rootElement " + rootElement);
            log.debug("getXml(). encoding " + encoding);
        }
        ByteArrayOutputStream fos = new ByteArrayOutputStream(1000);

        if (log.isDebugEnabled()) {
            log.debug("ByteArrayOutputStream object - " + fos);
        }

        writeMarshalToOutputStream(obj, encoding, rootElement, fos, isIndent, namespacePrefixMappers);
        return fos.toByteArray();
    }

    /**
     *
     * @param obj
     * @param encoding
     * @param rootElement
     * @param fos
     * @throws javax.xml.bind.JAXBException
     */
    public static void writeMarshalToOutputStream(
        Object obj, String encoding, String rootElement, OutputStream fos,
        boolean isIndent, NamespacePrefixMapper[] namespacePrefixMappers) throws JAXBException {

        JAXBContext jaxbContext = JAXBContext.newInstance ( obj.getClass().getPackage().getName() );
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, isIndent);

        if (namespacePrefixMappers!=null) {
            for (NamespacePrefixMapper namespacePrefixMapper : namespacePrefixMappers) {
                marshaller.setProperty( "com.sun.xml.bind.namespacePrefixMapper", namespacePrefixMapper );
            }
        }

        if (rootElement != null && rootElement.trim().length() > 0) {
            // http://weblogs.java.net/blog/kohsuke/archive/2005/10/101_ways_to_mar.html
            marshaller.marshal( new JAXBElement(new QName("",rootElement), obj.getClass(), obj ), fos);
        }
        else {
            marshaller.marshal(obj, fos);
        }
    }

    public static void writeToFile(final Object obj, final String fileName) throws JAXBException, FileNotFoundException {
        writeToFile(obj, fileName, "utf-8");
    }

    public static void writeToFile(final Object obj, final String fileName, final String encoding) throws JAXBException, FileNotFoundException {
        writeToFile(obj, new File(fileName), encoding);
    }

    public static void writeToFile(final Object obj, final File file, final String encoding) throws JAXBException, FileNotFoundException {
        JAXBContext jaxbContext = JAXBContext.newInstance ( obj.getClass().getPackage().getName() );
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);
        FileOutputStream fos = new FileOutputStream(file);
        marshaller.marshal(obj, fos);
    }
}
