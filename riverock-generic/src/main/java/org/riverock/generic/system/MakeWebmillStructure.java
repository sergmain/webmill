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
package org.riverock.generic.system;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.schema.db.structure.DbSchemaType;

/**
 * User: SergeMaslyukov
 * Date: 29.12.2004
 * Time: 13:40:18
 * $Id$
 */
public class MakeWebmillStructure {
//    private static Logger log = Logger.getLogger("org.riverock.webmill.system.MakeWebmillStructure");

    public MakeWebmillStructure(){}

    private static DbSchemaType makeSchema(String nameConnection, String nameOutputFiel)
        throws Exception
    {
        DatabaseAdapter db_ = null;
        try
        {
            db_ = DatabaseAdapter.getInstance( nameConnection );
            DbSchemaType schema = DatabaseManager.getDbStructure(db_ );

/*
            String encoding = "UTF-8";
            String nameFile = nameOutputFiel;
            String outputSchemaFile = GenericConfig.getGenericDebugDir()+nameFile;
            System.out.println("Marshal data to file " + outputSchemaFile);

            FileOutputStream fos = new FileOutputStream( outputSchemaFile );
            Marshaller marsh = new Marshaller(new OutputStreamWriter(fos, encoding));
            marsh.setMarshalAsDocument( true );
            marsh.setEncoding( encoding );
            marsh.marshal( schema );
*/

            return schema;
        }
        finally {
            DatabaseAdapter.close(db_);
            db_ = null;
        }
    }

    public static void main(String args[])
        throws Exception
    {
        org.riverock.generic.startup.StartupApplication.init();

        makeSchema("MYSQL", "webmill-schema.xml");
//        makeSchema("MSSQL-JTDS", "webmill-schema-mssql.xml");

    }
}
