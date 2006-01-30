/*
 * org.riverock.generic -- Database connectivity classes
 * 
 * Copyright (C) 2004, Riverock Software, All Rights Reserved.
 * 
 * Riverock -- The Open-source Java Development Community
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
 *
 */

/**
 * User: Admin
 * Date: Feb 28, 2003
 * Time: 12:02:41 AM
 *
 * $Id$
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