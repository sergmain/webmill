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

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import org.exolab.castor.xml.Marshaller;

import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.schema.db.structure.DbSchemaType;
import org.riverock.generic.config.GenericConfig;

/**
 * User: Admin
 * Date: Feb 27, 2003
 * Time: 12:06:35 AM
 *
 * $Id$
 */
public class TestGetDbStructure
{

    public static void main(String[] s)
        throws Exception
    {
        org.riverock.generic.startup.StartupApplication.init();
        DatabaseAdapter db_ = DatabaseAdapter.getInstance( "HSQLDB" );

        DbSchemaType schema = DatabaseManager.getDbStructure(db_ );

        FileOutputStream fos = new FileOutputStream( GenericConfig.getGenericDebugDir()+"hypersonic-schema.xml" );
        Marshaller marsh = new Marshaller(new OutputStreamWriter(fos, "utf-8"));
//        marsh.setRootElement("TestLanguage");
        marsh.setMarshalAsDocument(true);
        marsh.setEncoding("utf-8");
        marsh.marshal(schema);
        fos.flush();
        fos.close();
        fos = null;
    }
}
