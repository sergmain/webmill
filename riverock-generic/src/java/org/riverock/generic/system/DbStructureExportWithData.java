/*
 * org.riverock.generic -- Database connectivity classes
 * 
 * Copyright (C) 2004, Riverock Software, All Rights Reserved.
 * 
 * Riverock -- The Open-source Java Development Community
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
 *
 */
package org.riverock.generic.system;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import org.riverock.common.config.PropertiesProvider;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.schema.db.structure.DbSchemaType;
import org.riverock.generic.schema.db.structure.DbTableType;
import org.riverock.generic.schema.db.structure.DbViewType;
import org.riverock.generic.tools.XmlTools;
import org.riverock.generic.startup.StartupApplication;

import org.exolab.castor.xml.Unmarshaller;
import org.xml.sax.InputSource;

/**
 * Export data from DB to XML file
 *
 * Author: mill
 * Date: Nov 28, 2002
 * Time: 3:10:19 PM
 *
 * $Id$
 */
public class DbStructureExportWithData
{
//    private static Logger cat = Logger.getLogger("org.riverock.system.DbStructure");

    public static void main(String args[])
        throws Exception
    {

        StartupApplication.init();
//            table.setData(dbOra.getDataTable(table));
        String fileName =
            PropertiesProvider.getConfigPath()+
            File.separatorChar+"data-definition" +
            File.separatorChar+"data" +
            File.separatorChar+"webmill-def-v2.xml";

        DbStructureExport.export(fileName, true);
    }
}
