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
package org.riverock.generic.test.cases;

import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.config.GenericConfig;
import org.riverock.generic.annotation.schema.db.DbSchemaType;

import junit.framework.TestCase;
import org.xml.sax.InputSource;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

/**
 * User: Admin
 * Date: Dec 19, 2002
 * Time: 11:41:42 AM
 *
 * $Id$
 */
public class TestMarshal extends TestCase
{
    public TestMarshal(String testName)
    {
        super(testName);
    }

    private DbSchemaType makeSchema(String nameConnection, String nameOutputFiel)
        throws Exception
    {
        DatabaseAdapter db_ = DatabaseAdapter.getInstance( nameConnection );
        DbSchemaType schema = DatabaseManager.getDbStructure(db_ );

        String encoding = "UTF-8";
        String nameFile = nameOutputFiel;
        String outputSchemaFile = GenericConfig.getGenericDebugDir()+nameFile;
        System.out.println("Marshal data to file " + nameFile);

        FileOutputStream fos = new FileOutputStream( outputSchemaFile );
        Marshaller marsh = new Marshaller(new OutputStreamWriter(fos, encoding));
        marsh.setMarshalAsDocument( true );
        marsh.setEncoding( encoding );
        marsh.marshal( schema );

        return schema;
    }

    private String fileName = "webmill-schema-test.xml";

    public void doTest()
        throws Exception
    {
        org.riverock.generic.startup.StartupApplication.init();

        makeSchema("ORACLE", fileName);

        System.out.println("Unmarshal data from file");
        InputSource inSrc = new InputSource(
            new FileInputStream( GenericConfig.getGenericDebugDir()+fileName )
        );
        DbSchemaType millSchema =
            (DbSchemaType) Unmarshaller.unmarshal(DbSchemaType.class, inSrc);

    }

    public static void main(String args[])
        throws Exception
    {
        TestMarshal test = new TestMarshal("test");
        test.doTest();
    }
}
