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

import org.riverock.common.startup.StartupApplication;

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

    public static void main(String args[]) throws Exception {

        StartupApplication.init();
        String fileName;
        fileName = "test.xml";
/*
        fileName = PropertiesProvider.getConfigPath()+
            File.separatorChar+"data-definition" +
        File.separatorChar+"data" +
        File.separatorChar+"webmill-def-v2.xml";
*/

        DbStructureExport.export(fileName, true);
    }
}
