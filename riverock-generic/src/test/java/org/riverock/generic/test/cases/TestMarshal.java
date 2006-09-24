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
package org.riverock.generic.test.cases;

import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.schema.db.structure.DbSchemaType;
import org.riverock.generic.config.GenericConfig;
import junit.framework.TestCase;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
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
