/*
 * org.riverock.webmill - Portal framework implementation
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
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

/**
 * Author: mill
 * Date: Mar 20, 2003
 * Time: 10:08:39 AM
 *
 * $Id$
 */

package org.riverock.webmill.test;

import org.apache.log4j.Logger;

public class TestSchemaImport
{
    private static Logger cat = Logger.getLogger( "org.riverock.webmill.test.TestSchemaImport" );

    public TestSchemaImport()
    {
    }
/*
    public static void main(String[] s)
        throws Exception
    {
        ShootType shoot = new ShootType();
        ShootItemType item1 = new ShootItemType();
        item1.setShootName("t1");
        item1.setShootNumber(1);
        ShootItemType item2 = new ShootItemType();
        item2.setShootName("t2");
        item2.setShootNumber(2);
        ShootItemType item3 = new ShootItemType();
        item3.setShootName("t3");
        item3.setShootNumber(3);

        shoot.addItem( item1 );
        shoot.addItem( item2 );
        shoot.addItem( item3 );

        String[][] ns = new String[][]
        {
            { "mill-core", "http://webmill.askmore.info/mill/xsd/mill-core.xsd" },
            { "mill-auth", "http://webmill.askmore.info/mill/xsd/mill-auth.xsd" },
            { "prr-core", "http://prr.esrr.mps/xsd/prr-core.xsd" },
            { "prr-complex", "http://prr.esrr.mps/xsd/prr-complex.xsd" }
        };
        writeToFile(shoot, "c:\\opt1\\test-import-schema.xml", "utf-8", null, true, ns);
    }

    public static void writeToFile(Object obj, String fileName, String encoding, String root,
                                   boolean isValidate, String namespace[][])
        throws Exception
    {
        FileOutputStream fos = new FileOutputStream( fileName );
        Marshaller marsh = new Marshaller( new OutputStreamWriter(fos, encoding) );
        if (root!=null)
            marsh.setRootElement( root );

        marsh.setValidation( isValidate );
        marsh.setMarshalAsDocument(true);
        marsh.setEncoding(encoding);
        marsh.setSchemaLocation(
            "http://prr.esrr.mps/xsd/prr-complex.xsd E:\\sandbox\\millengine\\src\\xsd\\test\\prr_complex_only.xsd"
        );
        marsh.setSuppressXSIType( false );
        marsh.setValidation( isValidate );
        if (namespace!=null)
        {
            for (int i=0; i<namespace.length; i++)
                marsh.setNamespaceMapping(namespace[i][0], namespace[i][1]);
        }
        marsh.marshal(obj);
        fos.flush();
        fos.close();
        fos = null;
    }
*/
}
