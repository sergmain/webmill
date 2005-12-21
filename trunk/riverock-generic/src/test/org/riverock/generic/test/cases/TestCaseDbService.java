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
 * User: Admin
 * Date: Mar 3, 2003
 * Time: 6:43:34 PM
 *
 * $Id$
 */
package org.riverock.generic.test.cases;

import junit.framework.TestCase;
import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.schema.db.structure.DbSchemaType;
import org.riverock.generic.schema.db.structure.DbTableType;
import org.riverock.generic.tools.XmlTools;
import org.riverock.generic.config.GenericConfig;

public class TestCaseDbService extends TestCase
{
    public TestCaseDbService(String testName)
    {
        super(testName);
    }

    public void testCloneTableDescription()
        throws Exception
    {
        org.riverock.generic.startup.StartupApplication.init();
        DatabaseAdapter db_ = DatabaseAdapter.getInstance( "ORACLE" );
        DbSchemaType schema = DatabaseManager.getDbStructure(db_ );

        DbTableType sourceTable = DatabaseManager.getTableFromStructure( schema, "WM_PRICE_SHOP_LIST");
        sourceTable.setData( null );

        DbTableType targetTable = DatabaseManager.cloneDescriptionTable( sourceTable );
//        XmlTools.writeToFile(sourceTable, GenericConfig.getGenericDebugDir()+"clone-src.xml");
//        XmlTools.writeToFile(targetTable, GenericConfig.getGenericDebugDir()+"clone-trg.xml");

        byte[] sourceByte = XmlTools.getXml( sourceTable, null );
        byte[] targetByte = XmlTools.getXml( targetTable, null );

        assertFalse("clone DbTableType is failed",
            !new String(sourceByte).equals(new String( targetByte))
        );
    }
}
