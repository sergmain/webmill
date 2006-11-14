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
package org.riverock.portlet.test;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.tools.TransferFile;


public class TransferFileTest {
    private static Logger log = Logger.getLogger( TransferFileTest.class );

    public void procee( HttpServletRequest request ) throws Exception {

        DatabaseAdapter dbDyn = null;
        try {
            dbDyn = DatabaseAdapter.getInstance();
//            AuthSession auth_ = AuthSession.check( request, response );
//            if( auth_ == null )
//                return;

//            if( auth_.getRight( "SYSADMIN", "UPLOAD_FILE_X509", "A" ) ) {

                try {
                    TransferFile.processData( request );
                }
                catch( Exception e ) {
                    log.error( "Error upload signed file.", e );
                    throw e;
                }
//            }

        }
        finally {
            DatabaseManager.close( dbDyn );
            dbDyn = null;
        }
    }
}