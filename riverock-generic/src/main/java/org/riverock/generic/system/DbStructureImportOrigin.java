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

import java.io.File;
import java.io.FileInputStream;
import java.sql.SQLException;

import org.riverock.common.config.PropertiesProvider;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.schema.db.structure.DbSchemaType;
import org.riverock.generic.schema.db.structure.DbSequenceType;
import org.riverock.generic.schema.db.structure.DbTableType;
import org.riverock.generic.schema.db.structure.DbViewType;
import org.riverock.generic.startup.StartupApplication;

import org.exolab.castor.xml.Unmarshaller;
import org.xml.sax.InputSource;

/**
 * Author: mill
 * Date: Nov 28, 2002
 * Time: 3:10:19 PM
 *
 * $Id$
 */
/**
 * Upload data from XML file to DB
 */
public class DbStructureImportOrigin{

    public static void main(String args[]) throws Exception{

        StartupApplication.init();
        String fileName =
            PropertiesProvider.getConfigPath()+
            File.separatorChar+"data-definition" +
            File.separatorChar+"data" +
            File.separatorChar+"webmill-def-v2.xml";

//        DatabaseAdapter db_ = DatabaseAdapter.getInstance(false, "SAPDB_DBA");
//        DatabaseAdapter db_ = DatabaseAdapter.getInstance(true, "IBM-DB2");
//        DatabaseAdapter db_ = DatabaseAdapter.getInstance(true, "ORACLE_PORT");
//        DatabaseAdapter db_ = DatabaseAdapter.getInstance(true, "MYSQL");
        String dbAlias = "MYSQL";

        DbStructureImport.importStructure(fileName, true, dbAlias );
    }
}
