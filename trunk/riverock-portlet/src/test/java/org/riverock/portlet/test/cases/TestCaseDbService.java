/*
 * org.riverock.portlet - Portlet Library
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
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
 */

/**
 * User: Admin
 * Date: Mar 3, 2003
 * Time: 6:43:34 PM
 *
 * $Id$
 */
package org.riverock.portlet.test.cases;

import junit.framework.TestCase;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.annotation.schema.db.DbSchema;
import org.riverock.generic.annotation.schema.db.DbTable;
import org.riverock.generic.tools.XmlTools;


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
        DatabaseAdapter db_ = DatabaseAdapter.getInstance( "ORACLE");
        DbSchema schema = DatabaseManager.getDbStructure(db_ );

        DbTable sourceTable = DatabaseManager.getTableFromStructure( schema, "WM_PRICE_SHOP_LIST");
        sourceTable.setData( null );

        DbTable targetTable = DatabaseManager.cloneDescriptionTable( sourceTable );
//        XmlTools.writeToFile(sourceTable, SiteUtils.getTempDir()+"clone-src.xml");
//        XmlTools.writeToFile(targetTable, SiteUtils.getTempDir()+"clone-trg.xml");

        byte[] sourceByte = XmlTools.getXml( sourceTable, null );
        byte[] targetByte = XmlTools.getXml( targetTable, null );

        assertFalse("clone DbTable is failed",
            !new String(sourceByte).equals(new String( targetByte))
        );
    }
}
