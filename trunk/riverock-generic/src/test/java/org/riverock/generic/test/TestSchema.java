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
package org.riverock.generic.test;

import org.riverock.generic.config.GenericConfig;
import org.exolab.castor.xml.schema.*;
import org.exolab.castor.xml.schema.reader.SchemaReader;
import org.exolab.castor.xml.schema.util.DatatypeHandler;
import org.exolab.castor.xml.schema.writer.SchemaWriter;
import org.xml.sax.InputSource;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

/**
 * User: Admin
 * Date: Feb 28, 2003
 * Time: 12:02:41 AM
 *
 * $Id$
 */
public class TestSchema
{
    private static void read()
        throws Exception
    {
        InputSource inSrc = new InputSource(
            new FileInputStream( GenericConfig.getGenericDebugDir()+"mill-auth.xsd" )
        );

        SchemaReader reader = new SchemaReader(inSrc);

//        Schema a = reader.read();
    }

    public static void main(String[] s)
        throws Exception
    {
        org.riverock.generic.startup.StartupApplication.init();

        Schema a = new Schema();

        a.addNamespace("xs", "http://www.w3.org/2001/XMLSchema" );

        ComplexType cType = a.createComplexType("Qqq");
        Group group = new Group();
        group.setOrder( Order.seq );
        cType.addGroup( group );



        ElementDecl element = new ElementDecl(a, "TestElement");
        element.setTypeReference( DatatypeHandler.DATETIME_TYPE );

        group.addElementDecl( element );


        AttributeDecl attr = cType.createAttributeDecl("isTest");
//        AttributeDecl attr = new AttributeDecl( a, "isTest");
        attr.setSimpleTypeReference( DatatypeHandler.DATETIME_TYPE );

        cType.addAttributeDecl( attr );
        a.addComplexType( cType );


        FileOutputStream fos = new FileOutputStream( GenericConfig.getGenericDebugDir()+"test-schema.xml" );
        SchemaWriter writer = new SchemaWriter(new OutputStreamWriter(fos, "utf-8") );
        writer.write( a );
    }
}
