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
package org.riverock.generic.system;

import java.io.FileInputStream;

import org.riverock.generic.annotation.schema.db.DbSchema;
import org.riverock.generic.annotation.schema.db.DbTable;
import org.riverock.generic.annotation.schema.db.DbView;
import org.riverock.generic.config.GenericConfig;
import org.riverock.generic.tools.XmlTools;

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
        FileInputStream stream = new FileInputStream(GenericConfig.getGenericDebugDir() + "webmill-schema.xml");
        DbSchema millSchema = XmlTools.getObjectFromXml(DbSchema.class, stream);

        for (DbTable table : millSchema.getTables()) {
            if (table.getName().length()>18)
                System.out.println("Name of table '"+table.getName()+
                    "' is wrong. Exceed "+(table.getName().length()-18)+" of chars");
        }

        for (DbView view : millSchema.getViews()) {
            if (view.getName().length()>18)
                System.out.println("Name of view '"+view.getName()+
                    "' is wrong. Exceed "+(view.getName().length()-18)+" of chars");
        }
    }
}
