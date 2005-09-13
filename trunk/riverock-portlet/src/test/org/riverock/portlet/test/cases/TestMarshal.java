/*
 * org.riverock.portlet -- Portlet Library
 * 
 * Copyright (C) 2004, Riverock Software, All Rights Reserved.
 * 
 * Riverock -- The Open-source Java Development Community
 * http://www.riverock.org
 * 
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 */

/**
 * User: Admin
 * Date: Dec 19, 2002
 * Time: 11:41:42 AM
 *
 * $Id$
 */
package org.riverock.portlet.test.cases;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import junit.framework.TestCase;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.xml.sax.InputSource;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.schema.db.structure.DbSchemaType;
import org.riverock.portlet.tools.SiteUtils;

public class TestMarshal extends TestCase
{
    public TestMarshal(String testName)
    {
        super(testName);
    }

    private DbSchemaType makeSchema(String nameConnection, String nameOutputFiel)
        throws Exception
    {
        DatabaseAdapter db_ = DatabaseAdapter.getInstance(false, nameConnection);
        DbSchemaType schema = DatabaseManager.getDbStructure(db_ );

        String encoding = "UTF-8";
        String nameFile = nameOutputFiel;
        String outputSchemaFile = SiteUtils.getTempDir()+nameFile;
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
            new FileInputStream( SiteUtils.getTempDir()+fileName )
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
