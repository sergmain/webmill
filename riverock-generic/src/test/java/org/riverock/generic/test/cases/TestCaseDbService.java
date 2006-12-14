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
package org.riverock.generic.test.cases;

import junit.framework.TestCase;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.annotation.schema.db.DbSchema;
import org.riverock.generic.annotation.schema.db.DbTable;
import org.riverock.generic.utils.Utils;

/**
 * User: Admin
 * Date: Mar 3, 2003
 * Time: 6:43:34 PM
 *
 * $Id$
 */
public class TestCaseDbService extends TestCase
{
    public TestCaseDbService(String testName)
    {
        super(testName);
    }

    public void testCloneTableDescription()
        throws Exception
    {
        org.riverock.generic.utils.StartupApplication.init();
        DatabaseAdapter db_=null;
//        db_ = DatabaseAdapter.getInstance( "ORACLE" );
        DbSchema schema = DatabaseManager.getDbStructure(db_ );

        DbTable sourceTable = DatabaseManager.getTableFromStructure( schema, "WM_PRICE_SHOP_LIST");
        sourceTable.setData( null );

        DbTable targetTable = DatabaseManager.cloneDescriptionTable( sourceTable );
//        XmlTools.writeToFile(sourceTable, GenericConfig.getGenericDebugDir()+"clone-src.xml");
//        XmlTools.writeToFile(targetTable, GenericConfig.getGenericDebugDir()+"clone-trg.xml");

        byte[] sourceByte = Utils.getXml( sourceTable, null );
        byte[] targetByte = Utils.getXml( targetTable, null );

        assertFalse("clone DbTable is failed",
            !new String(sourceByte).equals(new String( targetByte))
        );
    }
}
