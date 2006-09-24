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
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.riverock.generic.tools;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;

import org.apache.log4j.Logger;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.xml.sax.InputSource;

/**
 * Author: mill
 * Date: Jan 22, 2003
 * Time: 9:33:19 AM
 *
 * $Id$
 */
public final class XmlTools
{
    private final static Logger log = Logger.getLogger(XmlTools.class);

    public XmlTools()
    {
    }

    public static Object getObjectFromXml( final Class classType, final String str ) throws Exception
    {
        try
        {
            InputSource inSrc = new InputSource( new StringReader(str) );
            return Unmarshaller.unmarshal(classType, inSrc);
        }
        catch(Exception e)
        {
            log.error("Error get object from xml string\n"+str, e);
            throw e;
        }
    }

    public static String getXmlAsString( final Object obj, final String rootElement ) throws Exception
    {
        if (log.isDebugEnabled())
        {
            byte b[] = getXml(obj, rootElement);
            log.debug("new String(b) " + new String(b));
            return new String(b);
        }
        else
            return new String( getXml(obj, rootElement) );
    }

    public static byte[] getXml( final Object obj, final String rootElement ) throws Exception
    {
        return getXml(obj, rootElement, null, "utf-8");
    }

    public static byte[] getXml( final Object obj, final String rootElement, final String namespace[][] ) throws Exception
    {
        return getXml(obj, rootElement, namespace, "utf-8");
    }

    public static byte[] getXml( final Object obj, final String rootElement, final String namespace[][], final String encoding ) throws Exception
    {
        ByteArrayOutputStream fos = null;
        if (log.isDebugEnabled())
        {
            log.debug("getXml(). Object to marshaling "+obj);
            log.debug("getXml(). rootElement "+rootElement);
            log.debug("getXml(). namespace "+namespace);
            log.debug("getXml(). encoding "+encoding);
        }
        try
        {
            fos = new ByteArrayOutputStream(1000);

            if (log.isDebugEnabled())
                log.debug("ByteArrayOutputStream object - "+fos);

            Marshaller marsh = new Marshaller( new OutputStreamWriter(fos, encoding) );

            if (rootElement!=null && rootElement.trim().length()>0)
                marsh.setRootElement( rootElement );

            marsh.setMarshalAsDocument(true);
            marsh.setEncoding(encoding);
            if (namespace!=null)
            {
                for (int i=0; i<namespace.length; i++)
                {
                    marsh.setNamespaceMapping(namespace[i][0], namespace[i][1]);
                }
            }
            marsh.marshal(obj);
            marsh = null;

            fos.flush();
            fos.close();

            return fos.toByteArray();
        }
        catch (Exception e)
        {
            log.error("Exception when marshaling object", e);
            throw e;
        }
        catch (Error err)
        {
            log.error("Error when marshaling object", err);
            throw err;
        }
        finally
        {
            fos = null;
        }
    }

    public static void writeToFile( final Object obj, final String fileName )
        throws Exception
    {
        writeToFile(obj, fileName, "utf-8", null, true, null);
    }

    public static void writeToFile( final Object obj, final String fileName, final String encoding )
        throws Exception
    {
        writeToFile(obj, fileName, encoding, null, true, null);
    }

    public static void writeToFile( final Object obj, final String fileName, final String encoding, final String root )
        throws Exception
    {
        writeToFile(obj, fileName, encoding, root, true, null);
    }

    public static void writeToFile( final Object obj, final String fileName, final String encoding, final String root,
                                   final boolean isValidate)
        throws Exception
    {
        writeToFile(obj, fileName, encoding, root, isValidate, null);
    }

    public static void writeToFile( final Object obj, final String fileName, final String encoding, final String root,
                                   final String namespace[][])
        throws Exception
    {
        writeToFile(obj, fileName, encoding, root, true, namespace);
    }

    public static void writeToFile( final Object obj, final String fileName, final String encoding, final String root,
                                   final boolean isValidate, final String namespace[][] )
        throws Exception
    {
        FileOutputStream fos = new FileOutputStream( fileName );
        Marshaller marsh = new Marshaller( new OutputStreamWriter(fos, encoding) );
        if (root!=null && root.trim().length()>0)
            marsh.setRootElement( root );

        marsh.setValidation( isValidate );
        marsh.setMarshalAsDocument(true);
        marsh.setEncoding(encoding);
        if (namespace!=null)
        {
            for (String[] aNamespace : namespace) {
                marsh.setNamespaceMapping(aNamespace[0], aNamespace[1]);
            }
        }
        marsh.marshal(obj);
        fos.flush();
        fos.close();
        fos = null;
    }

}
