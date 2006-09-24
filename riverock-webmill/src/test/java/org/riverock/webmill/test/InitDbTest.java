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

package org.riverock.webmill.test;

import org.riverock.generic.startup.StartupApplication;
import org.riverock.generic.system.DbStructureCreateTable;

/**
 * @author Sergei Maslyukov
 *         Date: 31.07.2006
 *         Time: 14:12:49
 */
public class InitDbTest {

    public static void main(String args[]) throws Exception
    {
/*
        if (args.length<3) {
            System.out.println( "Command line format: <DB_ALIAS> <IMPORT_FILE> <TABLE_NAME>" );
            return;
        }

        final String dbAlias = args[0];
        final String fileName = args[1];
        final String tableName = args[2];             MYSQL ..\src-core\xml\idea-schema.xml TH_IDEA_CATEGORY
*/
        final String dbAlias = "MYSQL";
        final String fileName = "..\\..\\riverock-webmill-db\\xml\\webmill-schema.xml";

        StartupApplication.init();
        DbStructureCreateTable.importStructure(fileName, false, dbAlias, "WM_LIST_R_HOLDING_COMPANY");
    }
}
