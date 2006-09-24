/*
 * org.riverock.portlet - Portlet Library
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
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
import org.riverock.generic.schema.db.structure.DbSchemaType;
import org.riverock.generic.schema.db.structure.DbTableType;
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
        DbSchemaType schema = DatabaseManager.getDbStructure(db_ );

        DbTableType sourceTable = DatabaseManager.getTableFromStructure( schema, "WM_PRICE_SHOP_LIST");
        sourceTable.setData( null );

        DbTableType targetTable = DatabaseManager.cloneDescriptionTable( sourceTable );
//        XmlTools.writeToFile(sourceTable, SiteUtils.getTempDir()+"clone-src.xml");
//        XmlTools.writeToFile(targetTable, SiteUtils.getTempDir()+"clone-trg.xml");

        byte[] sourceByte = XmlTools.getXml( sourceTable, null );
        byte[] targetByte = XmlTools.getXml( targetTable, null );

        assertFalse("clone DbTableType is failed",
            !new String(sourceByte).equals(new String( targetByte))
        );
    }
}
