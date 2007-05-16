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

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;

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

    public static <T> T getObjectFromXml(final Class<T> classType, final String str) throws Exception {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance ( classType.getPackage().getName() );
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            Source inSrc = new StreamSource(new StringReader(str));
            return unmarshaller.unmarshal(inSrc, classType).getValue();
        }
        catch (Exception e) {
            log.error("Error get object from xml string\n" + str, e);
            throw e;
        }
    }

    public static <T> T getObjectFromXml(final Class<T> classType, InputStream is) throws Exception {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance ( classType.getPackage().getName() );
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            Source inSrc = new StreamSource(is);
            return unmarshaller.unmarshal(inSrc, classType).getValue();
        }
        catch (Exception e) {
            log.error("Error get object from xml string\n", e);
            throw e;
        }
    }

    public static String getXmlAsString(final Object obj, final String rootElement) throws Exception {
        if (log.isDebugEnabled()) {
            byte b[] = getXml(obj, rootElement);
            log.debug("new String(b) " + new String(b));
            return new String(b);
        }
        else
            return new String(getXml(obj, rootElement));
    }

    public static byte[] getXml(final Object obj, final String rootElement) throws Exception {
        return getXml(obj, rootElement, "utf-8");
    }

    public static byte[] getXml(final Object obj, final String rootElement, final String encoding) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("getXml(). Object to marshaling " + obj);
            log.debug("getXml(). rootElement " + rootElement);
            log.debug("getXml(). encoding " + encoding);
        }
        ByteArrayOutputStream fos=null;
        try {
            fos = new ByteArrayOutputStream(1000);

            if (log.isDebugEnabled())
                log.debug("ByteArrayOutputStream object - " + fos);

            JAXBContext jaxbContext = JAXBContext.newInstance ( obj.getClass().getPackage().getName() );
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);

            if (rootElement != null && rootElement.trim().length() > 0) {
                // http://weblogs.java.net/blog/kohsuke/archive/2005/10/101_ways_to_mar.html
                marshaller.marshal( new JAXBElement(new QName("",rootElement), obj.getClass(), obj ), fos);
            }
            else {
                marshaller.marshal(obj, fos);
            }

            fos.flush();
            fos.close();

            return fos.toByteArray();
        }
        catch (Exception e) {
            log.error("Exception when marshaling object", e);
            throw e;
        }
        catch (Error err) {
            log.error("Error when marshaling object", err);
            throw err;
        }
        finally {
            fos.close();
            fos = null;
        }
    }

    public static void writeToFile(final Object obj, final String fileName) throws Exception {
        writeToFile(obj, fileName, "utf-8");
    }

    public static void writeToFile(final Object obj, final String fileName, final String encoding) throws Exception {
        writeToFile(obj, new File(fileName), encoding);
    }

    public static void writeToFile(final Object obj, final File file, final String encoding) throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance ( obj.getClass().getPackage().getName() );
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);
        FileOutputStream fos = new FileOutputStream(file);
        marshaller.marshal(obj, fos);
    }
}
