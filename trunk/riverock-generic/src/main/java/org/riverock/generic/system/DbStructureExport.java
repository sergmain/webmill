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

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import org.exolab.castor.xml.Unmarshaller;
import org.xml.sax.InputSource;

import org.riverock.common.config.PropertiesProvider;
import org.riverock.generic.config.GenericConfig;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.db.DatabaseStructureManager;
import org.riverock.generic.schema.db.structure.DbSchemaType;
import org.riverock.generic.schema.db.structure.DbTableType;
import org.riverock.generic.schema.db.structure.DbViewType;
import org.riverock.generic.startup.StartupApplication;
import org.riverock.generic.tools.XmlTools;

/**
 * Author: mill
 * Date: Nov 28, 2002
 * Time: 3:10:19 PM
 *
 * $Id$
 */
/**
 * Export data from DB to XML file
 */
public class DbStructureExport {

    private static final boolean IS_EXTRACT_DATA = true;

    public static void main(String args[])
        throws Exception
    {
        StartupApplication.init();
        System.out.println("GenericConfig.getGenericDebugDir() = " + GenericConfig.getGenericDebugDir());
        export(GenericConfig.getGenericDebugDir()+"webmill-schema.xml", IS_EXTRACT_DATA);
    }

    public static void export(String fileName, boolean isData)
        throws Exception
    {


        DatabaseAdapter dbOra = null;
        dbOra = DatabaseAdapter.getInstance("MYSQL");
//        DatabaseAdapter dbOra = DatabaseAdapter.getInstance(false, "HSQLDB");
//        DatabaseAdapter dbOra = DatabaseAdapter.getInstance(false, "ORACLE_TEST");
//        DatabaseAdapter dbOra = DatabaseAdapter.getInstance(false, "ORACLE");
//        dbOra = DatabaseAdapter.getInstance( "MYSQL_CONTEST");
//        DatabaseAdapter dbOra = DatabaseAdapter.getInstance(false, "ORACLE-DART");
//        DatabaseAdapter db_ = DatabaseAdapter.getInstance(false, "IBM-DB2");
//        DatabaseAdapter db_ = DatabaseAdapter.getInstance(false, "ORACLE_AAA");
//        DatabaseAdapter dbOra = DatabaseAdapter.getInstance(false, "MSSQL-JTDS");

        DatabaseManager.runSQL( dbOra, "delete from WM_PORTAL_ACCESS_STAT", null, null);
        DatabaseManager.runSQL( dbOra, "delete from WM_PORTAL_ACCESS_URL", null, null);
        DatabaseManager.runSQL( dbOra, "delete from WM_PORTAL_ACCESS_USERAGENT", null, null);
        DatabaseManager.runSQL( dbOra, "delete from wm_price_relate_user_order", null, null);
        DatabaseManager.runSQL( dbOra, "delete from wm_price_order", null, null);
        DatabaseManager.runSQL( dbOra, "delete from WM_PRICE_IMPORT_TABLE", null, null);
        DatabaseManager.runSQL( dbOra, "delete from WM_AUTH_USER where id_user in (select id_user from WM_LIST_USER where is_deleted=1)", null, null);
        DatabaseManager.runSQL( dbOra, "delete from WM_LIST_USER_METADATA where id_user in (select id_user from WM_LIST_USER where is_deleted=1)", null, null);
//        DatabaseManager.runSQL( dbOra, "delete from WM_PRICE_USER_DISCOUNT where id_user in (select id_user from WM_LIST_USER where is_deleted=1)", null, null);
        DatabaseManager.runSQL( dbOra, "delete from WM_LIST_USER where is_deleted=1", null, null);


        dbOra.commit();

        DbSchemaType schema = DatabaseManager.getDbStructure( dbOra );

        int i = 0;
        for (i = 0; i < schema.getTablesCount(); i++)
        {
            DbTableType table = schema.getTables(i);
            if (
                table.getName().toUpperCase().startsWith("A_") ||
                table.getName().toUpperCase().startsWith("BIN$") ||
                table.getName().toUpperCase().startsWith("CIH_") ||
                table.getName().toUpperCase().startsWith("TB_") ||
                table.getName().toUpperCase().startsWith("HAM_")
            )
            {
                schema.getTablesAsReference().remove(i);
                i--;
                continue;
            }
            System.out.println( "Table - " + table.getName() );

            table.setFields((ArrayList)DatabaseStructureManager.getFieldsList(dbOra, dbOra.getConnection(), table.getSchema(), table.getName()));
            table.setPrimaryKey(DatabaseStructureManager.getPrimaryKey(dbOra.getConnection(), table.getSchema(), table.getName()));
            table.setImportedKeys(DatabaseStructureManager.getImportedKeys(dbOra.getConnection(), table.getSchema(), table.getName()));

            boolean isSkipData = false;
            if ( table.getName().toUpperCase().startsWith("WM_FORUM") ||
                table.getName().toUpperCase().startsWith("WM_PORTLET_FAQ") ||
                table.getName().toUpperCase().startsWith("WM_JOB")
            ) {
                isSkipData = true;
            }

            if (isData && !isSkipData)
                table.setData(DatabaseStructureManager.getDataTable(dbOra.getConnection(), table));
        }

        String fileNameBigText =
            PropertiesProvider.getConfigPath()+
            File.separatorChar+"data-definition" +
            File.separatorChar+"data" +
            File.separatorChar+"big-text-table-def.xml";
        
        fileNameBigText =
            "\\sandbox\\riverock\\riverock-webmill-db\\xml\\webmill-schema.xml";

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
