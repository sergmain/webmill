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
 * Date: Feb 27, 2003
 * Time: 12:06:35 AM
 *
 * $Id$
 */
package org.riverock.portlet.test;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import org.exolab.castor.xml.Marshaller;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.annotation.schema.db.DbSchema;
import org.riverock.portlet.tools.SiteUtils;


public class TestGetDbStructure
{

    public static void main(String[] s)
        throws Exception
    {
        org.riverock.generic.startup.StartupApplication.init();
        DatabaseAdapter db_ = DatabaseAdapter.getInstance( "HSQLDB");

        DbSchema schema = DatabaseManager.getDbStructure(db_ );

        FileOutputStream fos = new FileOutputStream( SiteUtils.getTempDir()+"hypersonic-schema.xml" );
        Marshaller marsh = new Marshaller(new OutputStreamWriter(fos, "utf-8"));
//        marsh.setRootElement("TestLanguage");
        marsh.setMarshalAsDocument(true);
        marsh.setEncoding("utf-8");
        marsh.marshal(schema);
        fos.flush();
        fos.close();
        fos = null;
    }
}
