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

 * Author: mill

 * Date: Nov 28, 2002

 * Time: 3:10:19 PM

 *

 * $Id$

 */

package org.riverock.portlet.system;



import java.io.FileInputStream;

import java.io.File;

import java.util.ArrayList;

import java.util.List;



import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.generic.db.DatabaseManager;

import org.riverock.generic.schema.db.structure.DbSchemaType;

import org.riverock.generic.schema.db.structure.DbTableType;

import org.riverock.generic.schema.db.structure.DbViewType;



import org.riverock.generic.tools.XmlTools;

import org.riverock.common.config.PropertiesProvider;



import org.exolab.castor.xml.Unmarshaller;

import org.xml.sax.InputSource;



/**

 * Заливаем данные из БД в XML файл

 */

public class DbStructureExport

{

//    private static Logger cat = Logger.getLogger("org.riverock.system.DbStructure");



    public static void main(String args[])

        throws Exception

    {



        org.riverock.generic.startup.StartupApplication.init();



//        DatabaseAdapter dbOra = DatabaseAdapter.getInstance(false, "ORACLE_PORT");

        DatabaseAdapter dbOra = DatabaseAdapter.getInstance(false, "ORACLE");

//        DatabaseAdapter dbOra = DatabaseAdapter.getInstance(false, "ORACLE-DART");

//        DatabaseAdapter db_ = DatabaseAdapter.getInstance(false, "IBM-DB2");

//        DatabaseAdapter db_ = DatabaseAdapter.getInstance(false, "ORACLE_AAA");

//        DatabaseAdapter dbOra = DatabaseAdapter.getInstance(false, "MSSQL-JTDS");



        DbSchemaType schema = DatabaseManager.getDbStructure(dbOra );



        int i = 0;

        for (i = 0; i < schema.getTablesCount(); i++)

        {

            DbTableType table = schema.getTables(i);

            if (table.getName().startsWith("A_") || table.getName().startsWith("HAM_"))

            {

                schema.getTablesAsReference().remove(i);

                i--;

                continue;

            }

            System.out.println( "Table - " + table.getName() );



            table.setFields(dbOra.getFieldsList(table.getSchema(), table.getName()));

            table.setPrimaryKey(dbOra.getPrimaryKey(table.getSchema(), table.getName()));

            table.setImportedKeys(dbOra.getImportedKeys(table.getSchema(), table.getName()));

            table.setData(dbOra.getDataTable(table));

        }



        String fileNameBigText =

            PropertiesProvider.getConfigPath()+File.separatorChar+"data-definition" +File.separatorChar+ "data" +File.separatorChar+ "big-text-table-def.xml";



        InputSource inSrc = new InputSource(new FileInputStream( fileNameBigText ));

        DbSchemaType schemaBigTable = (DbSchemaType) Unmarshaller.unmarshal(DbSchemaType.class, inSrc);



        schema.setBigTextTable( schemaBigTable.getBigTextTableAsReference() );



        List views = new ArrayList();

        for (int k=0; k<schema.getViewsCount(); k++ )

        {

            DbViewType view = schema.getViews(k);

            if (!view.getName().toUpperCase().startsWith("F_D_") &&

                !view.getName().toUpperCase().startsWith("F_DEL_") &&

                !view.getName().toUpperCase().startsWith("FOR_DEL_") &&

                !view.getName().toUpperCase().startsWith("FOR_D_")

            )

                views.add( view );

        }



        schema.setViews( (ArrayList)views );



//        String fileName = WebmillConfig.getWebmillDebugDir()+"webmill-schema.xml";

        String fileName = PropertiesProvider.getConfigPath() +File.separatorChar+ "data-definition" +File.separatorChar+ "data" +File.separatorChar+ "webmill-def-v2.xml";

        System.out.println("Marshal data to file "+fileName);

        XmlTools.writeToFile(schema, fileName );

    }

}

