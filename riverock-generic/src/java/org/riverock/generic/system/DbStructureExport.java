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



/**

 * Author: mill

 * Date: Nov 28, 2002

 * Time: 3:10:19 PM

 *

 * $Id$

 */

package org.riverock.generic.system;



import java.io.File;

import java.io.FileInputStream;

import java.util.ArrayList;



import org.riverock.common.config.PropertiesProvider;

import org.riverock.common.config.JsmithyNamespases;

import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.generic.db.DatabaseManager;

import org.riverock.generic.schema.db.structure.DbSchemaType;

import org.riverock.generic.schema.db.structure.DbTableType;

import org.riverock.generic.schema.db.structure.DbViewType;

import org.riverock.generic.tools.XmlTools;

import org.riverock.generic.config.GenericConfig;

import org.riverock.generic.startup.StartupApplication;



import org.exolab.castor.xml.Unmarshaller;

import org.xml.sax.InputSource;



/**

 * Export data from DB to XML file

 */

public class DbStructureExport

{

//    private static Logger cat = Logger.getLogger("org.riverock.system.DbStructure");



    public static void main(String args[])

        throws Exception

    {

        StartupApplication.init();

        export(GenericConfig.getGenericDebugDir()+"webmill-schema.xml", false);

    }



    public static void export(String fileName, boolean isData)

        throws Exception

    {





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



            if (isData)

                table.setData(dbOra.getDataTable(table));

        }



        String fileNameBigText =

            PropertiesProvider.getConfigPath()+

            File.separatorChar+"data-definition" +

            File.separatorChar+"data" +

            File.separatorChar+"big-text-table-def.xml";

        InputSource inSrc = new InputSource(new FileInputStream( fileNameBigText ));

        DbSchemaType schemaBigTable = (DbSchemaType) Unmarshaller.unmarshal(DbSchemaType.class, inSrc);



        schema.setBigTextTable( schemaBigTable.getBigTextTableAsReference() );



        ArrayList views = new ArrayList();

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



        schema.setViews( views );



        System.out.println("Marshal data to file "+fileName);

        XmlTools.writeToFile(schema, fileName);

//        XmlTools.writeToFile(schema, fileName, "utf-8", "SchemaElement", JsmithyNamespases.namespace );

    }

}

