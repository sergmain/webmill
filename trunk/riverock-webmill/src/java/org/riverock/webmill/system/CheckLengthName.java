/*
 * org.riverock.webmill -- Portal framework implementation
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
 * Author: mill
 * Date: Nov 28, 2002
 * Time: 3:10:19 PM
 *
 * $Id$
 */
package org.riverock.webmill.system;

import java.io.FileInputStream;

import org.riverock.generic.schema.db.structure.DbSchemaType;
import org.riverock.generic.schema.db.structure.DbTableType;
import org.riverock.generic.schema.db.structure.DbViewType;
import org.riverock.webmill.config.WebmillConfig;

import org.apache.log4j.Logger;
import org.exolab.castor.xml.Unmarshaller;
import org.xml.sax.InputSource;

/**
 * ѕроверка имен таблиц и view на превышение 18 символов. ƒанное ограничение есть в IBM DB2,
 * в остальных базах длина имен таблиц и view может быть больше
 */
public class CheckLengthName
{
    private static Logger cat = Logger.getLogger("org.riverock.webmill.system.CheckLengthName");

    public CheckLengthName(){}

    public static void main(String args[])
        throws Exception
    {
        org.riverock.generic.startup.StartupApplication.init();

        System.out.println("Unmarshal data from file");
        InputSource inSrc = new InputSource(
            new FileInputStream( WebmillConfig.getWebmillDebugDir()+"webmill-schema.xml" )
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
