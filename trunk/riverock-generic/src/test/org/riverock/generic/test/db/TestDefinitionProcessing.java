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

 * Date: May 17, 2003

 * Time: 4:40:07 PM

 *

 * $Id$

 */

package org.riverock.test.db;



import org.riverock.generic.db.definition.DefinitionService;

import org.riverock.generic.db.DatabaseAdapter;



public class TestDefinitionProcessing

{

    public static void main(String[] s)

        throws Exception

    {

        org.riverock.generic.startup.StartupApplication.init();



        String nameConnection = "ORACLE";

//        String nameConnection = "HSQLDB";

//        String nameConnection = "MSSQL-JTDS";

        DatabaseAdapter db_ = DatabaseAdapter.getInstance(true, nameConnection);



        DefinitionService.validateDatabaseStructure( db_ );

        db_.commit();

        DatabaseAdapter.close(db_);

        db_ = null;

    }

}

