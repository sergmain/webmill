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

import org.riverock.generic.schema.db.structure.DbSchemaType;
import org.riverock.generic.schema.db.structure.DbTableType;
import org.riverock.generic.schema.db.structure.DbViewType;
import org.riverock.generic.config.GenericConfig;
import org.exolab.castor.xml.Unmarshaller;
import org.xml.sax.InputSource;

import java.io.FileInputStream;

/**
 * Author: mill
 * Date: Nov 28, 2002
 * Time: 3:10:19 PM
 *
 * $Id$
 */
/**
 * ѕроверка имен таблиц и view на превышение 18 символов. ƒанное ограничение есть в IBM DB2,
 * в остальных базах длина имен таблиц и view может быть больше
 */
public class CheckLengthName
{
//    private static Logger cat = Logger.getLogger("org.riverock.system.CheckLengthName");

    public CheckLengthName(){}

    public static void main(String args[])
        throws Exception
    {
        org.riverock.generic.startup.StartupApplication.init();

        System.out.println("Unmarshal data from file");
        InputSource inSrc = new InputSource(
            new FileInputStream( GenericConfig.getGenericDebugDir()+"webmill-schema.xml" )
        );
        DbSchemaType millSchema =
            (DbSchemaType) Unmarshaller.unmarshal(DbSchemaType.class, inSrc);

        for (int i=0; i<millSchema.getTablesCount(); i++)
        {
            DbTableType table = millSchema.getTables(i);
            if (table.getName().length()>18)
                System.out.println("Name of table '"+table.getName()+
                    "' is wrong. Exceed "+(table.getName().length()-18)+" of chars");
        }

        for (int i=0; i<millSchema.getViewsCount(); i++)
        {
            DbViewType view = millSchema.getViews(i);
            if (view.getName().length()>18)
                System.out.println("Name of view '"+view.getName()+
                    "' is wrong. Exceed "+(view.getName().length()-18)+" of chars");
        }
    }
}
