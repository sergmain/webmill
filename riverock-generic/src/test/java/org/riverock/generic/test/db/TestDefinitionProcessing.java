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
package org.riverock.generic.test.db;

import org.riverock.generic.db.DatabaseAdapter;

/**
 * User: Admin
 * Date: May 17, 2003
 * Time: 4:40:07 PM
 *
 * $Id$
 */
public class TestDefinitionProcessing
{
    public static void main(String[] s)
        throws Exception
    {
        org.riverock.generic.startup.StartupApplication.init();

        String nameConnection = "ORACLE";
//        String nameConnection = "HSQLDB";
//        String nameConnection = "MSSQL-JTDS";
        DatabaseAdapter db_ = DatabaseAdapter.getInstance( nameConnection );

//        DefinitionService.validateDatabaseStructure( db_ );
        db_.commit();
        DatabaseAdapter.close(db_);
        db_ = null;
    }
}
