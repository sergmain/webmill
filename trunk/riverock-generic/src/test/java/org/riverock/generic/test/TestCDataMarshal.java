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
package org.riverock.generic.test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import org.apache.log4j.Logger;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.xml.sax.InputSource;

/**
 * Author: mill
 * Date: Apr 9, 2003
 * Time: 2:08:35 PM
 *
 * $Id$
 */
public class TestCDataMarshal
{
    public String text = "";

    private static Logger cat = Logger.getLogger( "org.riverock.test.TestCDataMarshal" );

    public TestCDataMarshal()
    {
    }
    public static void writeToFile(Object obj, String fileName, String encoding, String root,
                                   boolean isValidate, String namespace[][])
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
            for (int i=0; i<namespace.length; i++)
            {
//                System.out.println("Add namespace: prefix - "+namespace[i][0]+", ns - "+namespace[i][1]);
                marsh.setNamespaceMapping(namespace[i][0], namespace[i][1]);
            }
        }
        marsh.marshal(obj);
        fos.flush();
        fos.close();
        fos = null;
    }

    public static void main(String args[])
        throws Exception
    {
        long mills = System.currentTimeMillis();
        org.riverock.generic.startup.StartupApplication.init();

        String tempFile = "c:\\temp\\cdata-test.xml";

//        <meta name="robots" content="index,follow"/>


        TestCDataMarshal cdata = new TestCDataMarshal();
        cdata.text = "&lt;![CDATA[&lt;a href=&quot;http://baikal.askmore.info&quot;>Prebaikalsky National Park&lt;/a>]]>";

        writeToFile( cdata, tempFile, "utf-8", null, false, null );

        InputSource inCurrSrc = new InputSource( new FileInputStream( tempFile ));
        TestCDataMarshal cdataResult = (TestCDataMarshal) Unmarshaller.unmarshal(TestCDataMarshal.class, inCurrSrc);
    }
}