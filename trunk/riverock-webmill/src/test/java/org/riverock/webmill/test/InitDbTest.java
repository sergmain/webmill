/*
 * org.riverock.webmill - Webmill portal with support jsr-168, xml/xslt and others things.
 * For more information, please visit project site http://webmill.askmore.info
 *
 * Copyright (C) 2000-2006, Riverock Software, All Rights Reserved.
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

package org.riverock.webmill.test;

import org.riverock.generic.startup.StartupApplication;
import org.riverock.generic.system.DbStructureCreateTable;

/**
 * @author Sergei Maslyukov
 *         Date: 31.07.2006
 *         Time: 14:12:49
 */
public class InitDbTest {

    public static void main(String args[]) throws Exception
    {
/*
        if (args.length<3) {
            System.out.println( "Command line format: <DB_ALIAS> <IMPORT_FILE> <TABLE_NAME>" );
            return;
        }

        final String dbAlias = args[0];
        final String fileName = args[1];
        final String tableName = args[2];             MYSQL ..\src-core\xml\idea-schema.xml TH_IDEA_CATEGORY
*/
        final String dbAlias = "MYSQL";
        final String fileName = "..\\..\\riverock-webmill-db\\xml\\webmill-schema.xml";

        StartupApplication.init();
        DbStructureCreateTable.importStructure(fileName, false, dbAlias, "WM_LIST_R_HOLDING_COMPANY");
    }
}
